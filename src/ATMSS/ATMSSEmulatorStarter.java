package ATMSS;

import ATMSS.AdvicePrinterHandler.AdvicePrinterHandler;
import ATMSS.AdvicePrinterHandler.Emulator.AdvicePrinterEmulator;
import ATMSS.BAMSHandler.BAMSHandlerInATMSS;
import ATMSS.BuzzerHandler.BuzzerHandler;
import ATMSS.BuzzerHandler.Emulator.BuzzerEmulator;
import ATMSS.DepositCollectorHandler.Emulator.DepositCollectorEmulator;
import ATMSS.DepositCollectorHandler.DepositCollectorHandler;
import ATMSS.CashDispenserHandler.Emulator.CashDispenserEmulator;
import ATMSS.CashDispenserHandler.CashDispenserHandler;
import AppKickstarter.timer.Timer;

import ATMSS.ATMSS.ATMSS;
import ATMSS.CardReaderHandler.Emulator.CardReaderEmulator;
import ATMSS.KeypadHandler.KeypadHandler;
import ATMSS.TouchDisplayHandler.Emulator.TouchDisplayEmulator;
import ATMSS.CardReaderHandler.CardReaderHandler;
import ATMSS.KeypadHandler.Emulator.KeypadEmulator;
import ATMSS.TouchDisplayHandler.TouchDisplayHandler;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.Stage;

//======================================================================
// ATMSSEmulatorStarter
public class ATMSSEmulatorStarter extends ATMSSStarter {
    //------------------------------------------------------------
    // main
    public static void main(String [] args) { new ATMSSEmulatorStarter().startApp(); } // main

    //------------------------------------------------------------
    // startHandlers
    @Override
    protected void startHandlers() {
//		System.out.println("I am ATMSSEmulatorStarter");
        Emulators.atmssEmulatorStarter = this;
        new Emulators().start();
    } // startHandlers


    //------------------------------------------------------------
    // Emulators
    public static class Emulators extends Application {
        private static ATMSSEmulatorStarter atmssEmulatorStarter;

	//----------------------------------------
	// start
        public void start() {
            launch();
	} // start

	//----------------------------------------
	// start
        public void start(Stage primaryStage) {
	    Timer timer = null;
	    ATMSS atmss = null;
	    BAMSHandlerInATMSS bamsHandler = null;
	    CardReaderEmulator cardReaderEmulator = null;
	    KeypadEmulator keypadEmulator = null;
	    TouchDisplayEmulator touchDisplayEmulator = null;
	    AdvicePrinterEmulator advicePrinterEmulator = null;
	    BuzzerEmulator buzzerEmulator = null;
	    DepositCollectorEmulator depositCollectorEmulator = null;
	    CashDispenserEmulator cashDispenserEmulator = null;
			// create emulators
	    try {
	        timer = new Timer("timer", atmssEmulatorStarter);
	        atmss = new ATMSS("ATMSS", atmssEmulatorStarter);
	        bamsHandler = new BAMSHandlerInATMSS("BAMSHandler", atmssEmulatorStarter);
	        cardReaderEmulator = new CardReaderEmulator("CardReaderHandler", atmssEmulatorStarter);
	        keypadEmulator = new KeypadEmulator("KeypadHandler", atmssEmulatorStarter);
	        touchDisplayEmulator = new TouchDisplayEmulator("TouchDisplayHandler", atmssEmulatorStarter);
	        advicePrinterEmulator = new AdvicePrinterEmulator("AdvicePrinterHandler", atmssEmulatorStarter);
			buzzerEmulator = new BuzzerEmulator("BuzzerHandler", atmssEmulatorStarter);
			depositCollectorEmulator = new DepositCollectorEmulator("DepositCollectorHandler", atmssEmulatorStarter);
			cashDispenserEmulator = new CashDispenserEmulator("CashDispenserHandler", atmssEmulatorStarter);
		// start emulator GUIs
		keypadEmulator.start();
		cardReaderEmulator.start();
		touchDisplayEmulator.start();
		advicePrinterEmulator.start();
		buzzerEmulator.start();
		depositCollectorEmulator.start();
		cashDispenserEmulator.start();
	    } catch (Exception e) {
		System.out.println("Emulators: start failed");
		e.printStackTrace();
		Platform.exit();
	    }
	    atmssEmulatorStarter.setTimer(timer);
	    atmssEmulatorStarter.setATMSS(atmss);
	    atmssEmulatorStarter.setBAMSHandler(bamsHandler);
	    atmssEmulatorStarter.setCardReaderHandler(cardReaderEmulator);
	    atmssEmulatorStarter.setKeypadHandler(keypadEmulator);
	    atmssEmulatorStarter.setTouchDisplayHandler(touchDisplayEmulator);
		atmssEmulatorStarter.setAdvicePrinterHandler(advicePrinterEmulator);
		atmssEmulatorStarter.setBuzzerHandler(buzzerEmulator);
		atmssEmulatorStarter.setDepositCollectorHandler(depositCollectorEmulator);
		atmssEmulatorStarter.setCashDispenserHandler(cashDispenserEmulator);
		// start threads
	    new Thread(timer).start();
	    new Thread(atmss).start();
	    new Thread(bamsHandler).start();
	    new Thread(cardReaderEmulator).start();
	    new Thread(keypadEmulator).start();
	    new Thread(touchDisplayEmulator).start();
	    new Thread(advicePrinterEmulator).start();
	    new Thread(buzzerEmulator).start();
	    new Thread(depositCollectorEmulator).start();
	    new Thread(cashDispenserEmulator).start();
	} // start
    } // Emulators


    //------------------------------------------------------------
    //  setters
    private void setTimer(Timer timer) {
        this.timer = timer;
    }
    private void setATMSS(ATMSS atmss) {
        this.atmss = atmss;
    }
	private void setBAMSHandler(BAMSHandlerInATMSS bamsHandler) {
		this.bamsHandler = bamsHandler;
	}
    private void setCardReaderHandler(CardReaderHandler cardReaderHandler) {
        this.cardReaderHandler = cardReaderHandler;
    }
    private void setKeypadHandler(KeypadHandler keypadHandler) {
        this.keypadHandler = keypadHandler;
    }
    private void setTouchDisplayHandler(TouchDisplayHandler touchDisplayHandler) {
        this.touchDisplayHandler = touchDisplayHandler;
    }
    private void setAdvicePrinterHandler(AdvicePrinterHandler advicePrinterHandler) {
    	this.advicePrinterHandler = advicePrinterHandler;
    }
	private void setBuzzerHandler(BuzzerHandler buzzerHandler) {
		this.buzzerHandler = buzzerHandler;
	}
	private void setDepositCollectorHandler(DepositCollectorHandler depositCollectorHandler) {
		this.depositCollectorHandler = depositCollectorHandler;
	}
	private void setCashDispenserHandler(CashDispenserHandler cashDispenserHandler) {
		this.cashDispenserHandler = cashDispenserHandler;
	}
} // ATMSSEmulatorStarter
