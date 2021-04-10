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


    private String loginState = "logout";
    private String cardNum = "";
    private String pin = "";
    private String cred = "";

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
                    if (loginState != "cardInserted") {
                        loginState = "cardInserted";
                        cardNum = msg.getDetails();
                        System.out.println("cardNum: " + cardNum);
                        touchDisplayMBox.send(new Msg(id, mbox, Msg.Type.TD_UpdateDisplay, "PinInputPage"));
                    }else {
                        System.out.println("card already inserted");
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
                    bamsMBox.send(new Msg(id, mbox, Msg.Type.BAMS_Request, msg.getDetails()+","+cardNum+","+cred));
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
                    log.info("ProvideCash: " + msg.getDetails());
                    break;

                case CD_GetCash:
                    log.info("GetCash: " + msg.getDetails());
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

                case TimesUp:
                    Timer.setTimer(id, mbox, pollingTime);
                    log.info("Poll: " + msg.getDetails());
                    cardReaderMBox.send(new Msg(id, mbox, Msg.Type.Poll, ""));
                    keypadMBox.send(new Msg(id, mbox, Msg.Type.Poll, ""));
                    touchDisplayMBox.send(new Msg(id, mbox, Msg.Type.Poll, ""));
                    advicePrinterMBox.send(new Msg(id, mbox, Msg.Type.Poll, ""));
                    bamsMBox.send(new Msg(id, mbox, Msg.Type.Poll, ""));
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
            if (!msgDetails.contains("Fail")){
                String[] creds = msgDetails.split(":");
                cred = creds[1];
                System.out.println("Login successful with cred: " + cred);
                loginState = "login";
                touchDisplayMBox.send(new Msg(id, mbox, Msg.Type.TD_UpdateDisplay, "MainMenu"));
            }else{
                System.out.println("Login fail with cred: " + cred);
            }
        } else if (msgDetails.contains("logout")) {

        } else if (msgDetails.contains("accounts")) {
            System.out.println("I am accounts");
            touchDisplayMBox.send(new Msg(id, mbox, Msg.Type.BAMS_Response, msgDetails));
        } else if (msgDetails.contains("outAmount")) {

        } else if (msgDetails.contains("depAmount")) {

        } else if (msgDetails.contains("amount")) {
            System.out.println("I am amount");
            touchDisplayMBox.send(new Msg(id, mbox, Msg.Type.BAMS_Response, msgDetails));
        } else if (msgDetails.contains("transAmount")) {

        } else if (msgDetails.contains("accStmtReq")) {
            System.out.println("I am accStmtReq");
        } else if (msgDetails.contains("chqBookReq")) {

        } else if (msgDetails.contains("chgPinReq")) {

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

        System.out.println("login state: " + loginState);

        if (loginState == "cardInserted"){
            switch (msg.getDetails()) {
                case "Cancel":
                    System.out.println("Cancel pressed");
                    break;
                case "1":
                    System.out.println("1 pressed");
                    if (pin.length()<6) {
                        pin = pin + "1";
                        touchDisplayMBox.send(new Msg(id, mbox, Msg.Type.TD_UpdateDisplay, "PinInputted"));
                    }
                    break;
                case "2":
                    System.out.println("2 pressed");
                    if (pin.length()<6) {
                        pin = pin + "2";
                        touchDisplayMBox.send(new Msg(id, mbox, Msg.Type.TD_UpdateDisplay, "PinInputted"));
                    }
                    break;
                case "3":
                    System.out.println("3 pressed");
                    if (pin.length()<6) {
                        pin = pin + "3";
                        touchDisplayMBox.send(new Msg(id, mbox, Msg.Type.TD_UpdateDisplay, "PinInputted"));
                    }
                    break;
                case "4":
                    System.out.println("4 pressed");
                    if (pin.length()<6) {
                        pin = pin + "4";
                        touchDisplayMBox.send(new Msg(id, mbox, Msg.Type.TD_UpdateDisplay, "PinInputted"));
                    }
                    break;
                case "5":
                    System.out.println("5 pressed");
                    if (pin.length()<6) {
                        pin = pin + "5";
                        touchDisplayMBox.send(new Msg(id, mbox, Msg.Type.TD_UpdateDisplay, "PinInputted"));
                    }
                    break;
                case "6":
                    System.out.println("6 pressed");
                    if (pin.length()<6) {
                        pin = pin + "6";
                        touchDisplayMBox.send(new Msg(id, mbox, Msg.Type.TD_UpdateDisplay, "PinInputted"));
                    }
                    break;
                case "7":
                    System.out.println("7 pressed");
                    if (pin.length()<6) {
                        pin = pin + "7";
                        touchDisplayMBox.send(new Msg(id, mbox, Msg.Type.TD_UpdateDisplay, "PinInputted"));
                    }
                    break;
                case "8":
                    System.out.println("8 pressed");
                    if (pin.length()<6) {
                        pin = pin + "8";
                        touchDisplayMBox.send(new Msg(id, mbox, Msg.Type.TD_UpdateDisplay, "PinInputted"));
                    }
                    break;
                case "9":
                    System.out.println("9 pressed");
                    if (pin.length()<6) {
                        pin = pin + "9";
                        touchDisplayMBox.send(new Msg(id, mbox, Msg.Type.TD_UpdateDisplay, "PinInputted"));
                    }
                    break;
                case "0":
                    System.out.println("0 pressed");
                    if (pin.length()<6) {
                        pin = pin + "0";
                        touchDisplayMBox.send(new Msg(id, mbox, Msg.Type.TD_UpdateDisplay, "PinInputted"));
                    }
                    break;
                case "Erase":
                    System.out.println("Erase pressed");
                    if (pin.length()>0) {
                        pin = pin.substring(0, pin.length() - 1);
                        touchDisplayMBox.send(new Msg(id, mbox, Msg.Type.TD_UpdateDisplay, "PinErase"));
                    }
                    break;
                case "Enter":
                    System.out.println("Enter pressed");
                    if (cardNum != "" && pin != "") {
                        String loginDetails = "LoginReq," + cardNum + "," + pin;
                        bamsMBox.send(new Msg(id, mbox, Msg.Type.BAMS_Request, loginDetails));
                    }else {
                        System.out.println("No pin inputted");
                    }
                    break;
            }
            System.out.println("pin: " + pin);

        }

    } // processKeyPressed


    //------------------------------------------------------------
    // processMouseClicked
    private void processMouseClicked(Msg msg) {
        // *** process mouse click here!!! ***
    } // processMouseClicked
} // CardReaderHandler
