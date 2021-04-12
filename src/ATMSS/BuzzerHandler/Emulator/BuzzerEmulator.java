package ATMSS.BuzzerHandler.Emulator;

import ATMSS.ATMSSStarter;
import ATMSS.BuzzerHandler.BuzzerHandler;
import AppKickstarter.misc.Msg;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.WindowEvent;

/**
 * Buzzer Emulator class is using to simulate a real buzzer of the atm machine
 */
//======================================================================
// BuzzerEmulator
public class BuzzerEmulator extends BuzzerHandler {
    private final int WIDTH = 320;
    private final int HEIGHT = 350;
    private ATMSSStarter atmssStarter;
    private String id;
    private Stage myStage;
    private BuzzerEmulatorController buzzerEmulatorController;

	/**
	 * The constructor for the buzzer emulator class
	 * @param id the name of the buzzer
	 * @param atmssStarter a reference to the AppKickstarter
	 */
    //------------------------------------------------------------
    // BuzzerEmulator
    public BuzzerEmulator(String id, ATMSSStarter atmssStarter) throws Exception {
	super(id, atmssStarter);
	this.atmssStarter = atmssStarter;
	this.id = id;
    } // BuzzerEmulator


	/**
	 * The start function configures the GUI setup of the buzzer emulator and runs the GUI
	 * @throws Exception
	 */
    //------------------------------------------------------------
    // start
    public void start() throws Exception {
	Parent root;
	myStage = new Stage();
	FXMLLoader loader = new FXMLLoader();
	String fxmlName = "BuzzerEmulator.fxml";
	loader.setLocation(BuzzerEmulator.class.getResource(fxmlName));
	root = loader.load();
	buzzerEmulatorController = (BuzzerEmulatorController) loader.getController();
	buzzerEmulatorController.initialize(id, atmssStarter, log, this);
	myStage.initStyle(StageStyle.DECORATED);
	myStage.setScene(new Scene(root, WIDTH, HEIGHT));
	myStage.setTitle("Buzzer");
	myStage.setResizable(false);
	myStage.setOnCloseRequest((WindowEvent event) -> {
	    atmssStarter.stopApp();
	    Platform.exit();
	});
	myStage.show();
    } // BuzzerEmulator

	/**
	 * This function is for handling the buzzing request from "atmss"
	 * @param msg The buzzing type
	 */
    //------------------------------------------------------------
    // handleBuzz
    protected void handleBuzz(Msg msg) {
	log.info(id + ": update display -- " + msg.getDetails());

	if (msg.getDetails().contains("withdrawMoney")) {
		reloadStage("BuzzerBuzzing.fxml");
		try {
			Thread.sleep(10000);
		} catch (Exception e) {
			System.out.println(e);
		}
		reloadStage("BuzzerEmulator.fxml");
	}

	switch (msg.getDetails()) {
	    case "Buzz":
		reloadStage("BuzzerBuzzing.fxml");
			try {
				Thread.sleep(2000);
			} catch (Exception e) {
				System.out.println(e);
			}
			reloadStage("BuzzerEmulator.fxml");
		break;

	    default:
		log.severe(id + ": update display with unknown display type -- " + msg.getDetails());
		break;
	}
    } // handleBuzz


	/**
	 * This function is using to reload the GUI display by a fxml file.
	 * @param fxmlFName The name of the fxml file.
	 */
    //------------------------------------------------------------
    // reloadStage
    private void reloadStage(String fxmlFName) {
        BuzzerEmulator buzzerEmulator = this;

        Platform.runLater(new Runnable() {
	    @Override
	    public void run() {
		try {
		    log.info(id + ": loading fxml: " + fxmlFName);
		    Parent root;
		    FXMLLoader loader = new FXMLLoader();
		    loader.setLocation(BuzzerEmulator.class.getResource(fxmlFName));
		    root = loader.load();
		    buzzerEmulatorController = (BuzzerEmulatorController) loader.getController();
		    buzzerEmulatorController.initialize(id, atmssStarter, log, buzzerEmulator);
		    myStage.setScene(new Scene(root, WIDTH, HEIGHT));
		} catch (Exception e) {
		    log.severe(id + ": failed to load " + fxmlFName);
		    e.printStackTrace();
		}
	    }
	});
    } // reloadStage

} // BuzzerEmulator
