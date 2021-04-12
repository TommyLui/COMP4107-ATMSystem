package ATMSS.ATMSS;

import AppKickstarter.AppKickstarter;
import AppKickstarter.misc.*;
import AppKickstarter.timer.Timer;


//======================================================================
// ATMSS
public class ATMSS extends AppThread {
    private int pollingTime;
    private MBox cardReaderMBox;
    private MBox keypadMBox;
    private MBox touchDisplayMBox;
    private MBox advicePrinterMBox;
    private MBox bamsMBox;
    private MBox buzzerMBox;
    private MBox depositCollectorMBox;
    private MBox cashDispenserMBox;

    private String loginState = "logout";
    private String cardState = "";
    private String cardNum = "";
    private String pin = "";
    private String existingPin = "";
    private String newPin = "";
    private String changePinState = "existing";
    private String cred = "";
    private int wrongPinCounter = 0;
    private int wrongExistingPinCounter = 0;
    private int wrongNewPinCounter = 0;
    private String depositAc;
    private String aid = "";
    private String withdrawalAc;
    private String getCashBuzzState = "on";

    String[] pins = {existingPin, newPin}; // 0: existing pin, 1: new pin

    //------------------------------------------------------------
    // ATMSS
    public ATMSS(String id, AppKickstarter appKickstarter) throws Exception {
        super(id, appKickstarter);
        pollingTime = Integer.parseInt(appKickstarter.getProperty("ATMSS.PollingTime"));
    } // ATMSS


    //------------------------------------------------------------
    // run
    public void run() {
        Timer.setTimer(id, mbox, pollingTime);
        log.info(id + ": starting...");

        cardReaderMBox = appKickstarter.getThread("CardReaderHandler").getMBox();
        keypadMBox = appKickstarter.getThread("KeypadHandler").getMBox();
        touchDisplayMBox = appKickstarter.getThread("TouchDisplayHandler").getMBox();
        advicePrinterMBox = appKickstarter.getThread("AdvicePrinterHandler").getMBox();
        bamsMBox = appKickstarter.getThread("BAMSHandler").getMBox();
        buzzerMBox = appKickstarter.getThread("BuzzerHandler").getMBox();
        depositCollectorMBox = appKickstarter.getThread("DepositCollectorHandler").getMBox();
        cashDispenserMBox = appKickstarter.getThread("CashDispenserHandler").getMBox();

        for (boolean quit = false; !quit; ) {
            Msg msg = mbox.receive();

            log.fine(id + ": message received: [" + msg + "].");

            switch (msg.getType()) {
                case TD_MouseClicked:
                    log.info("MouseCLicked: " + msg.getDetails());
                    processMouseClicked(msg);
                    break;

                case KP_KeyPressed:
                    log.info("KeyPressed: " + msg.getDetails());
                    processKeyPressed(msg);
                    break;

                case CR_CardInserted:
                    log.info("CardInserted: " + msg.getDetails());
                    if (!loginState.equalsIgnoreCase("login")&&!loginState.equalsIgnoreCase("cardInserted")&&!cardState.equalsIgnoreCase("cardRetained")) {
                        loginState = "cardInserted";
                        cardNum = msg.getDetails();
                        cardState = "cardInserted";
                        log.info("cardNum: " + cardNum);
                        touchDisplayMBox.send(new Msg(id, mbox, Msg.Type.TD_UpdateDisplay, "PinInputPage"));
                    } else {
                        log.info("Card Insert button clicked but card already inserted");
                    }
                    break;

                case AP_PrintSuccessful:
                    log.info("Print advice successful: " + msg.getDetails());
                    break;

                case AP_Error:
                    log.info("Advice printer error: " + msg.getDetails());
                    break;

                case AP_PrintReceipt:
                    log.info("Print advice: " + msg.getDetails());
                    processPrintReceiptRequest(msg);
                    break;

                case BAMS_Request:
                    log.info("BAMS request: " + msg.getDetails());
                    bamsMBox.send(new Msg(id, mbox, Msg.Type.BAMS_Request, msg.getDetails() + "," + cardNum + "," + cred));
                    break;

                case BAMS_Response:
                    log.info("BAMS response: " + msg.getDetails());
                    processBAMSResponse(msg.getDetails());
                    break;

                case BAMS_Error:
                    log.info("BAMS error: " + msg.getDetails());
                    touchDisplayMBox.send(new Msg(id, mbox, Msg.Type.BAMS_Error, msg.getDetails()));
                    break;

                case CD_provideCash:
                    if (loginState.equals("login")) {
                        log.info("ProvideCash: " + msg.getDetails());
                        String provideCash = msg.getDetails();
                        log.info(provideCash);

                        String detailsCD = cardNum + "," + aid + "," + cred + "," + provideCash;
                        bamsMBox.send(new Msg(id, mbox, Msg.Type.BAMS_Request, "WithdrawReq," + detailsCD));
                    }

                    break;

                case CD_GetCash:
                    if (loginState.equals("login")) {
                    log.info("GetCash: " + msg.getDetails());
                        touchDisplayMBox.send(new Msg(id, mbox, Msg.Type.TD_UpdateDisplay, "MainMenu"));
                        getCashBuzzState = "off";
                    }
                    break;

                case CD_Error:
                    log.info("CashDispenserError: " + msg.getDetails());
                    break;

                case CD_TimeOut:
                    log.info("CashDispenserTimeOut: " + msg.getDetails());
                    break;

                case CD_NotEnoughCash:
                    log.info("CashDispenser: " + msg.getDetails());
                    break;

                case DC_Collect_Cash:
                    if (loginState.equals("login")) {
                        String[] collectCash = msg.getDetails().split(",");
                        String totalNote = collectCash[0];
                        String amount = collectCash[1];

                        log.info(totalNote);
                        log.info(amount);

                        String details = cardNum + "," + aid + "," + cred + "," + amount;
                        bamsMBox.send(new Msg(id, mbox, Msg.Type.BAMS_Request, "DepositReq," + details));
                    }
                    /*
                    log.info("DepositCollectorCash: " + msg.getDetails());
                    String DepositCollectorState = msg.getDetails();

                    if(DepositCollectorState.contains("CollectSuccess")){
                        String collectedDetail = "DepositReq" + msg.getDetails();
                        bamsMBox.send(new Msg(id, mbox, Msg.Type.Poll, collectedDetail));
                    }else{
                        touchDisplayMBox.send(new Msg(id, mbox, Msg.Type.TD_UpdateDisplay,
                                "OpenSlot"));
                    }
                    */

                    break;

                case DC_Count_Cash:
                    log.info("DepositCollectorCountCash: " + msg.getDetails());
                    break;

                case DC_Error:
                    log.info("DepositCollectorError: " + msg.getDetails());
                    break;

                case DC_TimeOut:
                    log.info("DepositCollectorTimeOut: " + msg.getDetails());
                    break;

                case TD_selectedAcc:
                    String[] tempDetail = msg.getDetails().split(",");
                    depositAc = tempDetail[1];
                    log.info(depositAc);
                    aid = tempDetail[1];
                    touchDisplayMBox.send(new Msg(id, mbox, Msg.Type.TD_UpdateDisplay, "OpenDeposit"));
                    break;

                case TD_selectedAccWithdrawal:
                    String[] tempDetail1 = msg.getDetails().split(",");
                    withdrawalAc = tempDetail1[1];
                    log.info(withdrawalAc);
                    aid = tempDetail1[1];
                    touchDisplayMBox.send(new Msg(id, mbox, Msg.Type.TD_UpdateDisplay, "Waiting"));
                    break;

                case CR_EjectCard:
                    loginState = "ejectingCard";
                    cardReaderMBox.send(new Msg(id, mbox, Msg.Type.CR_EjectCard, ""));
                    break;

                case CR_CardEjected:
                    String logoutDetails = "Logout," + cardNum +"," + cred;

                    new Thread(() -> {
                        try {
                            Thread.sleep(100);
                            buzzerMBox.send(new Msg(id, mbox, Msg.Type.BZ_Buzz, "Buzz"));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }).start();

                    bamsMBox.send(new Msg(id, mbox, Msg.Type.BAMS_Request, logoutDetails));
                    break;

                case CR_CardRemoved:
                    log.info("loginState: " + loginState);
                    if (cardState.equalsIgnoreCase("cardInserted") && cardNum.equalsIgnoreCase("")){
                        cardState = "";
                        touchDisplayMBox.send(new Msg(id, mbox, Msg.Type.TD_UpdateDisplay, "Initialization"));
                    }
                    break;

                case CR_Lock:
                        cardReaderMBox.send(new Msg(id, mbox, Msg.Type.CR_Lock, ""));
                    break;

                case TimesUp:
                    Timer.setTimer(id, mbox, pollingTime);
                    log.info("Poll: " + msg.getDetails());
                    cardReaderMBox.send(new Msg(id, mbox, Msg.Type.Poll, ""));
                    keypadMBox.send(new Msg(id, mbox, Msg.Type.Poll, ""));
                    touchDisplayMBox.send(new Msg(id, mbox, Msg.Type.Poll, ""));
                    advicePrinterMBox.send(new Msg(id, mbox, Msg.Type.Poll, ""));
                    bamsMBox.send(new Msg(id, mbox, Msg.Type.Poll, ""));
                    buzzerMBox.send(new Msg(id, mbox, Msg.Type.Poll, ""));
                    depositCollectorMBox.send(new Msg(id, mbox, Msg.Type.Poll, ""));
                    cashDispenserMBox.send(new Msg(id, mbox, Msg.Type.Poll, ""));
                    break;

                case PollAck:
                    log.info("PollAck: " + msg.getDetails());
                    break;

                case Terminate:
                    quit = true;
                    break;

                default:
                    log.warning(id + ": unknown message type: [" + msg + "]");
            }
        }

        // declaring our departure
        appKickstarter.unregThread(this);
        log.info(id + ": terminating...");
    } // run

    //------------------------------------------------------------
    // processBAMSResponse
    private void processBAMSResponse(String msgDetails) {
        if (msgDetails.contains("cred")) {
            if (!msgDetails.contains("Fail")) {
                String[] creds = msgDetails.split(":");
                cred = creds[1];
                log.info("Login successful with cred: " + cred);
                loginState = "login";
                touchDisplayMBox.send(new Msg(id, mbox, Msg.Type.TD_UpdateDisplay, "MainMenu"));
            } else {
                log.info("Login fail with cred: " + cred);
                wrongPinCounter++;

                new Thread(() -> {
                    try {
                        Thread.sleep(100);
                        buzzerMBox.send(new Msg(id, mbox, Msg.Type.BZ_Buzz, "Buzz"));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }).start();

                log.info("wrongPinCounter: " + wrongPinCounter);
                pin = "";
                if (wrongPinCounter < 3) {
                    touchDisplayMBox.send(new Msg(id, mbox, Msg.Type.TD_UpdateDisplay, "PinInputtedWrong"));
                } else {
                    loginState = "cardLocked";
                    touchDisplayMBox.send(new Msg(id, mbox, Msg.Type.TD_UpdateDisplay, "CardLocked"));
                }
            }
        } else if (msgDetails.contains("logout")) {
            log.info("logout respond msgDetails: " + msgDetails);
            loginState = "logout";
            cardNum = "";
            pin = "";
            existingPin = "";
            newPin = "";
            changePinState = "existing";
            cred = "";
            wrongPinCounter = 0;
            wrongExistingPinCounter = 0;
            wrongNewPinCounter = 0;
            depositAc = null;
            aid = "";

            new Thread(() -> {
                try {
                    Thread.sleep(10000);
                    if (cardState.equalsIgnoreCase("cardInserted") && cardNum.equalsIgnoreCase("")){
                        log.info("RemoveCardTimeOut");
                        cardState = "cardRetained";
                        touchDisplayMBox.send(new Msg(id, mbox, Msg.Type.TD_UpdateDisplay, "RemoveCardTimeOut"));
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }).start();

        } else if (msgDetails.contains("accounts")) {
            log.info("I am accounts");
            touchDisplayMBox.send(new Msg(id, mbox, Msg.Type.BAMS_Response, msgDetails));
        } else if (msgDetails.contains("accountDeposit")) {
            log.info("I am accounts for Deposit");
            touchDisplayMBox.send(new Msg(id, mbox, Msg.Type.BAMS_Response, msgDetails));
        }else if (msgDetails.contains("accountWithdrawal")) {
            log.info("I am accounts for Deposit");
            touchDisplayMBox.send(new Msg(id, mbox, Msg.Type.BAMS_Response, msgDetails));
        }else if (msgDetails.contains("outAmount")) {
            advicePrinterMBox.send(new Msg(id, mbox, Msg.Type.AP_PrintReceipt, msgDetails));
            touchDisplayMBox.send(new Msg(id, mbox, Msg.Type.TD_UpdateDisplay, "GetCash"));

            new Thread(() -> {
                try {
                    Thread.sleep(100);
                    buzzerMBox.send(new Msg(id, mbox, Msg.Type.BZ_Buzz, "Buzz,withdrawMoney"));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }).start();

            new Thread(() -> {
                try {
                    Thread.sleep(10000);
                    if (!getCashBuzzState.equals("off")) {
                        cashDispenserMBox.send(new Msg(id, mbox, Msg.Type.CD_RetainMoney, ""));
                    }
                    getCashBuzzState = "on";
                    touchDisplayMBox.send(new Msg(id, mbox, Msg.Type.TD_UpdateDisplay, "MainMenu"));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }).start();

        } else if (msgDetails.contains("depAmount")) {

        } else if (msgDetails.contains("amount")) {
            log.info("I am amount");
            touchDisplayMBox.send(new Msg(id, mbox, Msg.Type.BAMS_Response, msgDetails));
        } else if (msgDetails.contains("transAmount")) {

        } else if (msgDetails.contains("accStmtReq")) {
            log.info("I am accStmtReq");
        } else if (msgDetails.contains("chqBookReq")) {

        } else if (msgDetails.contains("chgPinReq")) {
            String[] result = msgDetails.split(",");
            pin = result[1];
            touchDisplayMBox.send(new Msg(id, mbox, Msg.Type.BAMS_Response, msgDetails));
        }else if (msgDetails.contains("reDeposit")){
            advicePrinterMBox.send(new Msg(id, mbox, Msg.Type.AP_PrintReceipt, msgDetails));
            touchDisplayMBox.send(new Msg(id, mbox, Msg.Type.TD_UpdateDisplay, "MainMenu"));

            new Thread(() -> {
                try {
                    Thread.sleep(100);
                    buzzerMBox.send(new Msg(id, mbox, Msg.Type.BZ_Buzz, "Buzz"));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }).start();

        }


    } // processBAMSResponse

    //------------------------------------------------------------
    // processPrintReceiptRequest
    private void processPrintReceiptRequest(Msg msg) {
        advicePrinterMBox.send(new Msg(id, mbox, Msg.Type.AP_PrintReceipt, msg.getDetails()));
    } // processPrintReceiptRequest

    //------------------------------------------------------------
    // processKeyPressed
    private void processKeyPressed(Msg msg) {
        log.info("login state: " + loginState);

        if (loginState.equals("cardInserted")) {
            switch (msg.getDetails()) {
                case "Cancel":
                    log.info("Cancel pressed");
                    break;
                case "1":
                    log.info("1 pressed");
                    if (pin.length() < 6) {
                        pin = pin + "1";
                        touchDisplayMBox.send(new Msg(id, mbox, Msg.Type.TD_UpdateDisplay, "PinInputted"));
                    }
                    break;
                case "2":
                    log.info("2 pressed");
                    if (pin.length() < 6) {
                        pin = pin + "2";
                        touchDisplayMBox.send(new Msg(id, mbox, Msg.Type.TD_UpdateDisplay, "PinInputted"));
                    }
                    break;
                case "3":
                    log.info("3 pressed");
                    if (pin.length() < 6) {
                        pin = pin + "3";
                        touchDisplayMBox.send(new Msg(id, mbox, Msg.Type.TD_UpdateDisplay, "PinInputted"));
                    }
                    break;
                case "4":
                    log.info("4 pressed");
                    if (pin.length() < 6) {
                        pin = pin + "4";
                        touchDisplayMBox.send(new Msg(id, mbox, Msg.Type.TD_UpdateDisplay, "PinInputted"));
                    }
                    break;
                case "5":
                    log.info("5 pressed");
                    if (pin.length() < 6) {
                        pin = pin + "5";
                        touchDisplayMBox.send(new Msg(id, mbox, Msg.Type.TD_UpdateDisplay, "PinInputted"));
                    }
                    break;
                case "6":
                    log.info("6 pressed");
                    if (pin.length() < 6) {
                        pin = pin + "6";
                        touchDisplayMBox.send(new Msg(id, mbox, Msg.Type.TD_UpdateDisplay, "PinInputted"));
                    }
                    break;
                case "7":
                    log.info("7 pressed");
                    if (pin.length() < 6) {
                        pin = pin + "7";
                        touchDisplayMBox.send(new Msg(id, mbox, Msg.Type.TD_UpdateDisplay, "PinInputted"));
                    }
                    break;
                case "8":
                    log.info("8 pressed");
                    if (pin.length() < 6) {
                        pin = pin + "8";
                        touchDisplayMBox.send(new Msg(id, mbox, Msg.Type.TD_UpdateDisplay, "PinInputted"));
                    }
                    break;
                case "9":
                    log.info("9 pressed");
                    if (pin.length() < 6) {
                        pin = pin + "9";
                        touchDisplayMBox.send(new Msg(id, mbox, Msg.Type.TD_UpdateDisplay, "PinInputted"));
                    }
                    break;
                case "0":
                    log.info("0 pressed");
                    if (pin.length() < 6) {
                        pin = pin + "0";
                        touchDisplayMBox.send(new Msg(id, mbox, Msg.Type.TD_UpdateDisplay, "PinInputted"));
                    }
                    break;
                case "Erase":
                    log.info("Erase pressed");
                    if (pin.length() > 0) {
                        pin = pin.substring(0, pin.length() - 1);
                        touchDisplayMBox.send(new Msg(id, mbox, Msg.Type.TD_UpdateDisplay, "PinErase"));
                    }
                    break;
                case "Enter":
                    log.info("Enter pressed");
                    if (!cardNum.equals("") && !pin.equals("")) {
                        String loginDetails = "LoginReq," + cardNum + "," + pin;
                        bamsMBox.send(new Msg(id, mbox, Msg.Type.BAMS_Request, loginDetails));
                    } else {
                        wrongPinCounter++;

                        new Thread(() -> {
                            try {
                                Thread.sleep(100);
                                buzzerMBox.send(new Msg(id, mbox, Msg.Type.BZ_Buzz, "Buzz"));
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }).start();

                        log.info("No pin inputted");
                        log.info("wrongPinCounter: " + wrongPinCounter);
                        pin = "";
                        if (wrongPinCounter < 3) {
                            touchDisplayMBox.send(new Msg(id, mbox, Msg.Type.TD_UpdateDisplay, "PinInputtedWrong"));
                        } else {
                            loginState = "cardLocked";
                            touchDisplayMBox.send(new Msg(id, mbox, Msg.Type.TD_UpdateDisplay, "CardLocked"));
                        }
                    }
                    break;
            }
            log.info("pin: " + pin);

        } else if (loginState.equals("login")) {

            int i = 0;

            if (changePinState.equals("existing")) {
                i = 0;
            } else {
                i = 1;
            }

            switch (msg.getDetails()) {
                case "Cancel":
                    log.info("Cancel pressed");
                    break;
                case "1":
                    log.info("1 pressed");
                    if (pins[i].length() < 6) {
                        pins[i] = pins[i] + "1";
                        touchDisplayMBox.send(new Msg(id, mbox, Msg.Type.TD_UpdateDisplay, "PinInputted"));
                    }
                    break;
                case "2":
                    log.info("2 pressed");
                    if (pins[i].length() < 6) {
                        pins[i] = pins[i] + "2";
                        touchDisplayMBox.send(new Msg(id, mbox, Msg.Type.TD_UpdateDisplay, "PinInputted"));
                    }
                    break;
                case "3":
                    log.info("3 pressed");
                    if (pins[i].length() < 6) {
                        pins[i] = pins[i] + "3";
                        touchDisplayMBox.send(new Msg(id, mbox, Msg.Type.TD_UpdateDisplay, "PinInputted"));
                    }
                    break;
                case "4":
                    log.info("4 pressed");
                    if (pins[i].length() < 6) {
                        pins[i] = pins[i] + "4";
                        touchDisplayMBox.send(new Msg(id, mbox, Msg.Type.TD_UpdateDisplay, "PinInputted"));
                    }
                    break;
                case "5":
                    log.info("5 pressed");
                    if (pins[i].length() < 6) {
                        pins[i] = pins[i] + "5";
                        touchDisplayMBox.send(new Msg(id, mbox, Msg.Type.TD_UpdateDisplay, "PinInputted"));
                    }
                    break;
                case "6":
                    log.info("6 pressed");
                    if (pins[i].length() < 6) {
                        pins[i] = pins[i] + "6";
                        touchDisplayMBox.send(new Msg(id, mbox, Msg.Type.TD_UpdateDisplay, "PinInputted"));
                    }
                    break;
                case "7":
                    log.info("7 pressed");
                    if (pins[i].length() < 6) {
                        pins[i] = pins[i] + "7";
                        touchDisplayMBox.send(new Msg(id, mbox, Msg.Type.TD_UpdateDisplay, "PinInputted"));
                    }
                    break;
                case "8":
                    log.info("8 pressed");
                    if (pins[i].length() < 6) {
                        pins[i] = pins[i] + "8";
                        touchDisplayMBox.send(new Msg(id, mbox, Msg.Type.TD_UpdateDisplay, "PinInputted"));
                    }
                    break;
                case "9":
                    log.info("9 pressed");
                    if (pins[i].length() < 6) {
                        pins[i] = pins[i] + "9";
                        touchDisplayMBox.send(new Msg(id, mbox, Msg.Type.TD_UpdateDisplay, "PinInputted"));
                    }
                    break;
                case "0":
                    log.info("0 pressed");
                    if (pins[i].length() < 6) {
                        pins[i] = pins[i] + "0";
                        touchDisplayMBox.send(new Msg(id, mbox, Msg.Type.TD_UpdateDisplay, "PinInputted"));
                    }
                    break;
                case "Erase":
                    log.info("Erase pressed");
                    if (pins[i].length() > 0) {
                        pins[i] = pins[i].substring(0, pins[i].length() - 1);
                        touchDisplayMBox.send(new Msg(id, mbox, Msg.Type.TD_UpdateDisplay, "PinErase"));
                    }
                    break;
                case "Enter":
                    log.info("Enter pressed");
//                    if (!cardNum.equals("") && !pins[i].equals("")) {

                    if (i == 0 && pins[i].equals(pin)) {
                        // proceed to next page
                        changePinState = "new";
                        log.info("Existing pin");
                        touchDisplayMBox.send(new Msg(id, mbox, Msg.Type.TD_UpdateDisplay, "ChangePinNew"));
                        wrongExistingPinCounter = 0;

                    } else if (i == 1 && !pins[1].equals(pin) && pins[1].length() == 6) {
                        String chgPinDetails = "ChgPinReq," + cardNum + "," + pins[0] + "," + pins[1] + "," + cred;
                        log.info("New pin");
                        bamsMBox.send(new Msg(id, mbox, Msg.Type.BAMS_Request, chgPinDetails));
                        pins[0] = "";
                        pins[1] = "";
                        changePinState = "existing";

                    } else if (i == 0) {
                        wrongExistingPinCounter++;

                        if (wrongExistingPinCounter < 3) {
                            pins[0] = "";
                            touchDisplayMBox.send(new Msg(id, mbox, Msg.Type.TD_UpdateDisplay, "ChangePinInputtedWrong"));
                        } else if (wrongExistingPinCounter == 3) {
                            touchDisplayMBox.send(new Msg(id, mbox, Msg.Type.TD_UpdateDisplay, "ChangePinInputtedWrong3Times"));
                            wrongExistingPinCounter = 0;
                            pins[0] = "";
                        }
                    } else if (i == 1) {
                        wrongNewPinCounter++;

                        if (wrongNewPinCounter < 3) {
                            pins[1] = "";
                            touchDisplayMBox.send(new Msg(id, mbox, Msg.Type.TD_UpdateDisplay, "ChangePinInputtedWrong"));
                        } else if (wrongNewPinCounter == 3) {
                            touchDisplayMBox.send(new Msg(id, mbox, Msg.Type.TD_UpdateDisplay, "ChangePinInputtedWrong3Times"));
                            wrongNewPinCounter = 0;
                            pins[0] = "";
                            pins[1] = "";
                            changePinState = "existing";
                        }
                    }
                    break;
            }
            log.info("pin typed in: " + pins[i]);
        }


    } // processKeyPressed


    //------------------------------------------------------------
    // processMouseClicked
    private void processMouseClicked(Msg msg) {
        // *** process mouse click here!!! ***
    } // processMouseClicked
} // CardReaderHandler
