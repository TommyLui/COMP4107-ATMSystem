package ATMSS.KeypadHandler.Emulator;

import ATMSS.ATMSSStarter;
import ATMSS.KeypadHandler.KeypadHandler;

import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.WindowEvent;

/**
 * Keypad Emulator class is using to simulate a real keypad of the atm machine
 */
//======================================================================
// KeypadEmulator
public class KeypadEmulator extends KeypadHandler {
    private ATMSSStarter atmssStarter;
    private String id;
    private Stage myStage;
    private KeypadEmulatorController keypadEmulatorController;

	/**
	 * The constructor for the keypd emulator class
	 * @param id the name of the keypad
	 * @param atmssStarter a reference to the AppKickstarter
	 */
	//------------------------------------------------------------
    // KeypadEmulator
    public KeypadEmulator(String id, ATMSSStarter atmssStarter) {
	super(id, atmssStarter);
	this.atmssStarter = atmssStarter;
	this.id = id;
    } // KeypadEmulator


	/**
	 * The start function configures the GUI setup of the keypad emulator and runs the GUI
	 * @throws Exception
	 */
    //------------------------------------------------------------
    // start
    public void start() throws Exception {
	Parent root;
	myStage = new Stage();
	FXMLLoader loader = new FXMLLoader();
	String fxmlName = "KeypadEmulator.fxml";
	loader.setLocation(KeypadEmulator.class.getResource(fxmlName));
	root = loader.load();
	keypadEmulatorController = (KeypadEmulatorController) loader.getController();
	keypadEmulatorController.initialize(id, atmssStarter, log, this);
	myStage.initStyle(StageStyle.DECORATED);
	myStage.setScene(new Scene(root, 340, 270));
	myStage.setTitle("KeypadHandler");
	myStage.setResizable(false);
	myStage.setOnCloseRequest((WindowEvent event) -> {
	    atmssStarter.stopApp();
	    Platform.exit();
	});
	myStage.show();
    } // KeypadEmulator
} // KeypadEmulator
