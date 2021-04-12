package ATMSS.BuzzerHandler;

import ATMSS.HWHandler.HWHandler;
import AppKickstarter.AppKickstarter;
import AppKickstarter.misc.Msg;


//======================================================================
// BuzzerHandler
public class BuzzerHandler extends HWHandler {
    //------------------------------------------------------------
    // BuzzerHandler
    public BuzzerHandler(String id, AppKickstarter appKickstarter) throws Exception {
	super(id, appKickstarter);
    } // BuzzerHandler

    //------------------------------------------------------------
    // processMsg
    protected void processMsg(Msg msg) {
        switch (msg.getType()) {
            case BZ_Buzz:
                handleBuzz(msg);
                break;

            default:
                log.warning(id + ": unknown message type: [" + msg + "]");
        }
    } // processMsg


    //------------------------------------------------------------
    // handleBuzz
    protected void handleBuzz(Msg msg) {
	log.info(id + ": update buzz -- " + msg.getDetails());
    } // handleBuzz
} // BuzzerHandler
