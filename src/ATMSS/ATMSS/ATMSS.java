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
                    bamsMBox.send(new Msg(id, mbox, Msg.Type.BAMS_Request, msg.getDetails()));
                    break;

                case BAMS_Response:
                    log.info("BAMS response: " + msg.getDetails());
                    processBAMSResponse(msg.getDetails());

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

        } else if (msgDetails.contains("logout")) {

        } else if (msgDetails.contains("accounts")) {

        } else if (msgDetails.contains("outAmount")) {

        } else if (msgDetails.contains("depAmount")) {

        } else if (msgDetails.contains("amount")) {

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
        // *** The following is an example only!! ***
        if (msg.getDetails().compareToIgnoreCase("Cancel") == 0) {
            cardReaderMBox.send(new Msg(id, mbox, Msg.Type.CR_EjectCard, ""));
        } else if (msg.getDetails().compareToIgnoreCase("1") == 0) {
            touchDisplayMBox.send(new Msg(id, mbox, Msg.Type.TD_UpdateDisplay, "BlankScreen"));
        } else if (msg.getDetails().compareToIgnoreCase("2") == 0) {
            touchDisplayMBox.send(new Msg(id, mbox, Msg.Type.TD_UpdateDisplay, "MainMenu"));
        } else if (msg.getDetails().compareToIgnoreCase("3") == 0) {
            touchDisplayMBox.send(new Msg(id, mbox, Msg.Type.TD_UpdateDisplay, "Confirmation"));
        }else if (msg.getDetails().compareToIgnoreCase("4") == 0) {
            System.out.println("4 pressed");
            buzzerMBox.send(new Msg(id, mbox, Msg.Type.BZ_Buzz, "Buzz"));
        }
    } // processKeyPressed


    //------------------------------------------------------------
    // processMouseClicked
    private void processMouseClicked(Msg msg) {
        // *** process mouse click here!!! ***
    } // processMouseClicked
} // CardReaderHandler
