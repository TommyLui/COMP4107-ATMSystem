package ATMSS.TouchDisplayHandler;

import ATMSS.HWHandler.HWHandler;
import AppKickstarter.AppKickstarter;
import AppKickstarter.misc.*;


//======================================================================
// TouchDisplayHandler
public class TouchDisplayHandler extends HWHandler {
    //------------------------------------------------------------
    // TouchDisplayHandler
    public TouchDisplayHandler(String id, AppKickstarter appKickstarter) throws Exception {
	super(id, appKickstarter);
    } // TouchDisplayHandler


    //------------------------------------------------------------
    // processMsg
    protected void processMsg(Msg msg) {
        switch (msg.getType()) {
            case TD_MouseClicked:
                atmss.send(new Msg(id, mbox, Msg.Type.TD_MouseClicked, msg.getDetails()));
                break;

            case TD_UpdateDisplay:
                handleUpdateDisplay(msg);
                break;

            case BAMS_Request:
                atmss.send(new Msg(id, mbox, Msg.Type.BAMS_Request, msg.getDetails()));
                break;

            case BAMS_Response:
                handleBAMSUpdateDisplay(msg);
                break;

            case BAMS_Error:
                handleBAMSErrorDisplay(msg);
                break;

            default:
                log.warning(id + ": unknown message type: [" + msg + "]");
        }
    } // processMsg

    //------------------------------------------------------------
    // handleBAMSErrorDisplay
    protected void handleBAMSErrorDisplay(Msg msg) {
        log.info(id + ": BAMS update error display -- " + msg.getDetails());
    } // handleBAMSErrorDisplay

    //------------------------------------------------------------
    // handleBAMSUpdateDisplay
    protected void handleBAMSUpdateDisplay(Msg msg) {
        log.info(id + ": BAMS update display -- " + msg.getDetails());
    } // handleBAMSUpdateDisplay

    //------------------------------------------------------------
    // handleUpdateDisplay
    protected void handleUpdateDisplay(Msg msg) {
	log.info(id + ": update display -- " + msg.getDetails());
    } // handleUpdateDisplay
} // TouchDisplayHandler
