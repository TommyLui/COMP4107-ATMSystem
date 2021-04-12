package ATMSS.CashDispenserHandler;

import ATMSS.HWHandler.HWHandler;
import AppKickstarter.AppKickstarter;
import AppKickstarter.misc.*;


//======================================================================
// CardReaderHandler
public class CashDispenserHandler extends HWHandler {
    //------------------------------------------------------------
    // CardReaderHandler
    public CashDispenserHandler(String id, AppKickstarter appKickstarter) {
        super(id, appKickstarter);
    } // CardReaderHandler


    //------------------------------------------------------------
    // processMsg
    protected void processMsg(Msg msg) {
        switch (msg.getType()) {
            case CD_provideCash:
                atmss.send(new Msg(id, mbox, Msg.Type.CD_provideCash, msg.getDetails()));
                break;

            case CD_GetCash:
                atmss.send(new Msg(id, mbox, Msg.Type.CD_GetCash, msg.getDetails()));
                break;

            case CD_NotEnoughCash:
                atmss.send(new Msg(id, mbox, Msg.Type.CD_NotEnoughCash, "Not Enough Cash"));
                break;

            case CD_TimeOut:
                atmss.send(new Msg(id, mbox, Msg.Type.CD_TimeOut, "Time out"));
                break;

            case CD_Error:
                atmss.send(new Msg(id, mbox, Msg.Type.CD_Error,"Error"));
                break;

            case CD_RetainMoney:
                retainMoney();

            default:
                log.warning(id + ": unknown message type: [" + msg + "]");
        }
    } // processMsg

    protected void retainMoney() {
        log.info(id + ": retain money");
    }


    //------------------------------------------------------------
    // handleCardInsert
    protected void ProvideCash() {
        log.info(id + ": card inserted");
    } // handleCardInsert


    //------------------------------------------------------------
    // handleCardEject
    protected void handleCardEject() {
        log.info(id + ": card ejected");
    } // handleCardEject


    //------------------------------------------------------------
    // handleCardRemove
    protected void handleCardRemove() {
        log.info(id + ": card removed");
    } // handleCardRemove
} // CardReaderHandler
