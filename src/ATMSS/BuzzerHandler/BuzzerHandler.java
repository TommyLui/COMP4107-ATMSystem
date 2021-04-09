package ATMSS.BuzzerHandler;

import ATMSS.HWHandler.HWHandler;
import AppKickstarter.AppKickstarter;
import AppKickstarter.misc.Msg;


//======================================================================
// TouchDisplayHandler
public class BuzzerHandler extends HWHandler {
    //------------------------------------------------------------
    // TouchDisplayHandler
    public BuzzerHandler(String id, AppKickstarter appKickstarter) throws Exception {
	super(id, appKickstarter);
    } // TouchDisplayHandler

    //------------------------------------------------------------
    // processMsg
    protected void processMsg(Msg msg) {
        switch (msg.getType()) {
            case BZ_Buzz:
                handleUpdateBuzz(msg);
                break;

            default:
                log.warning(id + ": unknown message type: [" + msg + "]");
        }
    } // processMsg


    //------------------------------------------------------------
    // handleUpdateDisplay
    protected void handleUpdateBuzz(Msg msg) {
	log.info(id + ": update buzz -- " + msg.getDetails());
    } // handleUpdateDisplay
} // TouchDisplayHandler
