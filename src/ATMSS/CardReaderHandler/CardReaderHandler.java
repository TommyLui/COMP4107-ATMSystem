package ATMSS.CardReaderHandler;

import ATMSS.HWHandler.HWHandler;
import AppKickstarter.AppKickstarter;
import AppKickstarter.misc.*;


//======================================================================
// CardReaderHandler
public class CardReaderHandler extends HWHandler {
    //------------------------------------------------------------
    // CardReaderHandler
    public CardReaderHandler(String id, AppKickstarter appKickstarter) {
	super(id, appKickstarter);
    } // CardReaderHandler


    //------------------------------------------------------------
    // processMsg
    protected void processMsg(Msg msg) {
        switch (msg.getType()) {
            case CR_CardInserted:
                atmss.send(new Msg(id, mbox, Msg.Type.CR_CardInserted, msg.getDetails()));
                break;

            case CR_EjectCard:
                handleCardEject();
                break;

            case CR_CardRemoved:
                handleCardRemove();
                break;

            case CR_Lock:
                handleCardLock();
                break;

            default:
                log.warning(id + ": unknown message type: [" + msg + "]");
        }
    } // processMsg


    //------------------------------------------------------------
    // handleCardInsert
    protected void handleCardInsert() {
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

    //------------------------------------------------------------
    // handleCardLock
    protected void handleCardLock() { log.info(id + ": card locked"); } // handleCardRemove
} // CardReaderHandler
