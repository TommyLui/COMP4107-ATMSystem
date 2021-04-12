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

import java.sql.Timestamp;
import java.util.Date;

//======================================================================
// AdvicePrinterEmulator
public class AdvicePrinterEmulator extends AdvicePrinterHandler {
    private ATMSSStarter atmssStarter;
    private String id;
    private Stage myStage;
    private AdvicePrinterEmulatorController advicePrinterEmulatorController;

	/**
	 * Constructor for the AdvicePrinterEmulator
	 * @param id the name of the AdvicePrinterEmulator
	 * @param atmssStarter a reference to the atmssStarter
	 */
	//------------------------------------------------------------
    // AdvicePrinterEmulator
    public AdvicePrinterEmulator(String id, ATMSSStarter atmssStarter) {
	super(id, atmssStarter);
	this.atmssStarter = atmssStarter;
	this.id = id;
    } // AdvicePrinterEmulator


	/**
	 * Start method for constructing the GUI for AdvicePrinterEmulator
	 * @throws Exception
	 */
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

	/**
	 * Handle print advice request
	 * @param content the content in the receipt
	 */
	//------------------------------------------------------------
	// handlePrintReceipt
	protected void handlePrintReceipt(String content) {
		// fixme
		super.handlePrintReceipt(content);

//		"reDeposit," +  depAmount + "," + amount + "," + aid + "," + cardNo
		if (content.contains("reDeposit")) {
			String[] details = content.split(",");
			String depAmount = details[1];
			String amount = details[2];
			String aid = details[3];
			String cardNo = details[4];

			Date currentDate = new Date();

			content = "--------------------\n" +
					"Ref No.: "+"#"+(new Timestamp(currentDate.getTime()).getTime()) +"\n" +
					"Date: "+currentDate+"\n" +
					"Usage: "+"Deposit"+"\n" +
					"Card No. "+cardNo + "(Account id: "+aid+")"+"\n"+
					"Amount of money deposited: $"+amount+"\n" +
					"Current balance: $"+depAmount+"\n"+
					"--------------------\n";
		} else if (content.contains("outAmount")) {
			String[] details = content.split(",");
			String outAmount = details[1];
			String amount = details[2];
			String aid = details[3];
			String cardNo = details[4];

			Date currentDate = new Date();

			content = "--------------------\n" +
					"Ref No.: "+"#"+(new Timestamp(currentDate.getTime()).getTime()) +"\n" +
					"Date: "+currentDate+"\n" +
					"Usage: "+"Withdraw money"+"\n" +
					"Card No. "+cardNo + "(Account id: "+aid+")"+"\n"+
					"Amount of money withdrawn: $"+amount+"\n" +
					"Current balance: $"+outAmount+"\n"+
					"--------------------\n";
		}else if (content.contains("TransAmount")) {
			String[] details = content.split(",");
			String outAmount = details[1];
			String amount = details[2];
			String fromAcc = details[3];
			String cardNo = details[4];
			String toAcc = details[5];

			Date currentDate = new Date();

			content = "--------------------\n" +
					"Ref No.: "+"#"+(new Timestamp(currentDate.getTime()).getTime()) +"\n" +
					"Date: "+currentDate+"\n" +
					"Usage: "+"Transfer money"+"\n" +
					"Card No. "+cardNo +"\n"+
					"From account " + fromAcc + "\n" +
					"To account " + toAcc + "\n" +
					"Amount of money transfer: $"+amount+"\n" +
					"--------------------\n";
		}

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
