package ATMSS.DepositCollectorHandler;

import ATMSS.HWHandler.HWHandler;
import AppKickstarter.AppKickstarter;
import AppKickstarter.misc.*;

/*
1.  Collect user’s cash deposit
2.  Opening/closing of slot
3.  Counts the money notes
4.  Notify ATM-SS on error
5.  Timeout

 */

public class DepositCollectorHandler extends HWHandler {

    // DepositCollectorHandler
    public DepositCollectorHandler(String id, AppKickstarter appKickstarter) {
        super(id, appKickstarter);
    // DepositCollectorHandler
    }

    //------------------------------------------------------------
    // processMsg

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
    protected void collectCashDeposit(String content) {
        log.info(id + ": Collect Cash Successful");
    } // handlePrintReceipt

    protected void countCashDeposit(String content) {
        log.info(id + ":Count Cash Successful");
    } // handlePrintReceipt

}
