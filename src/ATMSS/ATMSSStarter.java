package ATMSS;

import ATMSS.DepositCollectorHandler.DepositCollectorHandler;
import ATMSS.AdvicePrinterHandler.AdvicePrinterHandler;
import ATMSS.BAMSHandler.BAMSHandlerInATMSS;
import ATMSS.BuzzerHandler.BuzzerHandler;
import ATMSS.KeypadHandler.Emulator.KeypadEmulator;
import ATMSS.CashDispenserHandler.CashDispenserHandler;
import AppKickstarter.AppKickstarter;
import AppKickstarter.misc.Msg;
import AppKickstarter.timer.Timer;

import ATMSS.ATMSS.ATMSS;
import ATMSS.CardReaderHandler.CardReaderHandler;
import ATMSS.KeypadHandler.KeypadHandler;
import ATMSS.TouchDisplayHandler.TouchDisplayHandler;

import javafx.application.Platform;


//======================================================================
// ATMSSStarter
public class ATMSSStarter extends AppKickstarter {
    protected Timer timer;
    protected ATMSS atmss;
	protected BAMSHandlerInATMSS bamsHandler = null;
    protected CardReaderHandler cardReaderHandler;
    protected KeypadHandler keypadHandler;
    protected TouchDisplayHandler touchDisplayHandler;
    protected AdvicePrinterHandler advicePrinterHandler;
	protected BuzzerHandler buzzerHandler;
	protected DepositCollectorHandler depositCollectorHandler;
	protected CashDispenserHandler cashDispenserHandler;
	//------------------------------------------------------------
    // main
    public static void main(String [] args) { new ATMSSStarter().startApp(); } // main


    //------------------------------------------------------------
    // ATMStart
    public ATMSSStarter() {
	super("ATMSSStarter", "etc/ATM.cfg");
    } // ATMStart


    //------------------------------------------------------------
    // startApp
    protected void startApp() {
	// start our application
	log.info("");
	log.info("");
	log.info("============================================================");
	log.info(id + ": Application Starting...");

	startHandlers();
    } // startApp


    //------------------------------------------------------------
    // startHandlers
    protected void startHandlers() {
	// create handlers
	try {
		timer = new Timer("timer", this);
	    atmss = new ATMSS("ATMSS", this);
	    bamsHandler = new BAMSHandlerInATMSS("BAMSHandler", this);
	    cardReaderHandler = new CardReaderHandler("CardReaderHandler", this);
	    keypadHandler = new KeypadHandler("KeypadHandler", this);
	    touchDisplayHandler = new TouchDisplayHandler("TouchDisplayHandler", this);
		advicePrinterHandler = new AdvicePrinterHandler("AdvicePrinterHandler", this);
		buzzerHandler = new BuzzerHandler("BuzzerHandler", this);
		depositCollectorHandler = new DepositCollectorHandler("DepositCollectorHandler", this);
		cashDispenserHandler = new CashDispenserHandler("CashDispenserHandler", this);
	} catch (Exception e) {
		log.info("AppKickstarter: startApp failed");
	    e.printStackTrace();
	    Platform.exit();
	}

	// start threads
	new Thread(timer).start();
	new Thread(atmss).start();
	new Thread(bamsHandler).start();
	new Thread(cardReaderHandler).start();
	new Thread(keypadHandler).start();
	new Thread(touchDisplayHandler).start();
	new Thread(advicePrinterHandler).start();
	new Thread(buzzerHandler).start();
	new Thread(depositCollectorHandler).start();
	new Thread(cashDispenserHandler).start();
    } // startHandlers


    //------------------------------------------------------------
    // stopApp
    public void stopApp() {
	log.info("");
	log.info("");
	log.info("============================================================");
	log.info(id + ": Application Stopping...");
	atmss.getMBox().send(new Msg(id, null, Msg.Type.Terminate, "Terminate now!"));
	bamsHandler.getMBox().send(new Msg(id, null, Msg.Type.Terminate, "Terminate now!"));
	cardReaderHandler.getMBox().send(new Msg(id, null, Msg.Type.Terminate, "Terminate now!"));
	keypadHandler.getMBox().send(new Msg(id, null, Msg.Type.Terminate, "Terminate now!"));
	touchDisplayHandler.getMBox().send(new Msg(id, null, Msg.Type.Terminate, "Terminate now!"));
	timer.getMBox().send(new Msg(id, null, Msg.Type.Terminate, "Terminate now!"));
	advicePrinterHandler.getMBox().send(new Msg(id, null, Msg.Type.Terminate, "Terminate now!"));
	buzzerHandler.getMBox().send(new Msg(id, null, Msg.Type.Terminate, "Terminate now!"));
	depositCollectorHandler.getMBox().send(new Msg(id, null, Msg.Type.Terminate, "Terminate now!"));
	cashDispenserHandler.getMBox().send(new Msg(id, null, Msg.Type.Terminate, "Terminate now!"));
    } // stopApp
} // ATM.ATMSSStarter
