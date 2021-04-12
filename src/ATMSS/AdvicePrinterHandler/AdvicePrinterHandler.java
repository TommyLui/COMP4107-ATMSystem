package ATMSS.AdvicePrinterHandler;

import ATMSS.HWHandler.HWHandler;
import AppKickstarter.AppKickstarter;
import AppKickstarter.misc.*;


//======================================================================
// AdvicePrinterHandler

public class AdvicePrinterHandler extends HWHandler {
    /**
     * The constructor for AdvicePrinterHandler
     * @param id
     * @param appKickstarter
     */
    //------------------------------------------------------------
    // AdvicePrinterHandler
    public AdvicePrinterHandler(String id, AppKickstarter appKickstarter) {
	super(id, appKickstarter);
    } // AdvicePrinterHandler

    /**
     * Process the message inside the mailbox of AdvicePrinter
     * @param msg a message sent to the mailbox of AdvicePrinter
     */
    //------------------------------------------------------------
    // processMsg
    protected void processMsg(Msg msg) {
        switch (msg.getType()) {
            case TEST_AP_PrintReceipt:
                atmss.send(new Msg(id, mbox, Msg.Type.AP_PrintReceipt, msg.getDetails()));
                break;
            case AP_PrintReceipt:
                handlePrintReceipt(msg.getDetails());
                break;
            case AP_PrintSuccessful:
                atmss.send(new Msg(id, mbox, Msg.Type.AP_PrintSuccessful, msg.getDetails()));
                break;
            case AP_Error:
                atmss.send(new Msg(id, mbox, Msg.Type.AP_Error, msg.getDetails()));
                break;
//            case BAMS_Request:
//                atmss.send(new Msg(id, mbox, Msg.Type.BAMS_Request, msg.getDetails()));
            default:
                log.warning(id + ": unknown message type: [" + msg + "]");
        }
    } // processMsg

    /**
     * Handle print advice request from processMsg()
     * @param content
     */
    //------------------------------------------------------------
    // handlePrintReceipt
    protected void handlePrintReceipt(String content) {
        log.info(id + ": receipt printed");
    } // handlePrintReceipt

} // AdvicePrinterHandler
