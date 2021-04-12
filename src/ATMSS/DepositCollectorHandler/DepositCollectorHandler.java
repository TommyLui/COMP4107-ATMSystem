package ATMSS.DepositCollectorHandler;

import ATMSS.HWHandler.HWHandler;
import AppKickstarter.AppKickstarter;
import AppKickstarter.misc.*;

public class DepositCollectorHandler extends HWHandler {

    // DepositCollectorHandler
    public DepositCollectorHandler(String id, AppKickstarter appKickstarter) {
        super(id, appKickstarter);

        /**
         * The constructor for Deposit Collector
         * @param id name of the application
         * @param appKickstarter The app kickstarter using.
         */

    // DepositCollectorHandler
    }

    //------------------------------------------------------------
    // processMsg

    /**
     * Process the message inside the mailbox of Deposit Collector
     * @param msg a message sent to the mailbox of Deposit Collector,pass to ATM-SS
     */

    protected void processMsg(Msg msg) {
        switch (msg.getType()) {
            case DC_Collect_Cash:
                atmss.send(new Msg(id, mbox, Msg.Type.DC_Collect_Cash, msg.getDetails()));
                collectCashDeposit(msg.getDetails());
                break;

            case DC_Count_Cash:
                atmss.send(new Msg(id, mbox, Msg.Type.DC_Count_Cash, msg.getDetails()));
                countCashDeposit(msg.getDetails());
                break;

            case DC_Error:
                atmss.send(new Msg(id, mbox, Msg.Type.DC_Error, "Time out"));
                break;

            case DC_TimeOut:
                atmss.send(new Msg(id, mbox, Msg.Type.DC_TimeOut, "Time out"));
                break;

            default:
                log.warning(id + ": unknown message type: [" + msg + "]");
        }
    } // processMsg

    //------------------------------------------------------------
    // handlePrintReceipt

    /**
     * Handle print message request from processMsg()
     * @param content
     */

    protected void collectCashDeposit(String content) {
        log.info(id + ": Collect Cash Successful");
    } // handlePrintReceipt

    protected void countCashDeposit(String content) {
        log.info(id + ":Count Cash Successful");
    } // handlePrintReceipt

}
