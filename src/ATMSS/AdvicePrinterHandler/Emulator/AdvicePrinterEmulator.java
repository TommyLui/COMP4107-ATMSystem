package ATMSS.AdvicePrinterHandler.Emulator;

import ATMSS.ATMSSStarter;
//import ATMSS.CardReaderHandler.CardReaderHandler;
import ATMSS.AdvicePrinterHandler.AdvicePrinterHandler;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.WindowEvent;

import java.util.Date;


//======================================================================
// AdvicePrinterEmulator
public class AdvicePrinterEmulator extends AdvicePrinterHandler {
    private ATMSSStarter atmssStarter;
    private String id;
    private Stage myStage;
    private AdvicePrinterEmulatorController advicePrinterEmulatorController;

    //------------------------------------------------------------
    // AdvicePrinterEmulator
    public AdvicePrinterEmulator(String id, ATMSSStarter atmssStarter) {
	super(id, atmssStarter);
	this.atmssStarter = atmssStarter;
	this.id = id;
    } // AdvicePrinterEmulator


    //------------------------------------------------------------
    // start
    public void start() throws Exception {
	Parent root;
	myStage = new Stage();
	FXMLLoader loader = new FXMLLoader();
	String fxmlName = "AdvicePrinterEmulator.fxml";
	loader.setLocation(AdvicePrinterEmulator.class.getResource(fxmlName));
	root = loader.load();
	advicePrinterEmulatorController = (AdvicePrinterEmulatorController) loader.getController();
	advicePrinterEmulatorController.initialize(id, atmssStarter, log, this);
	myStage.initStyle(StageStyle.DECORATED);
	myStage.setScene(new Scene(root, 350, 470));
	myStage.setTitle("Advice Printer");
	myStage.setResizable(false);
	myStage.setOnCloseRequest((WindowEvent event) -> {
	    atmssStarter.stopApp();
	    Platform.exit();
	});
	myStage.show();
    } // AdvicePrinterEmulator

	//------------------------------------------------------------
	// handlePrintReceipt
	protected void handlePrintReceipt(String content) {
		// fixme
		super.handlePrintReceipt(content);
		advicePrinterEmulatorController.printAdvice(content);
	} // handlePrintReceipt

//    //------------------------------------------------------------
//    // handleCardInsert
//    protected void handleCardInsert() {
//        // fixme
//	super.handleCardInsert();
//	cardReaderEmulatorController.appendTextArea("Card Inserted");
//	cardReaderEmulatorController.updateCardStatus("Card Inserted");
//    } // handleCardInsert
//
//
//    //------------------------------------------------------------
//    // handleCardEject
//    protected void handleCardEject() {
//        // fixme
//	super.handleCardEject();
//	cardReaderEmulatorController.appendTextArea("Card Ejected");
//	cardReaderEmulatorController.updateCardStatus("Card Ejected");
//    } // handleCardEject
//
//
//    //------------------------------------------------------------
//    // handleCardRemove
//    protected void handleCardRemove() {
//	// fixme
//	super.handleCardRemove();
//	cardReaderEmulatorController.appendTextArea("Card Removed");
//	cardReaderEmulatorController.updateCardStatus("Card Reader Empty");
//    } // handleCardRemove

} // AdvicePrinterEmulator