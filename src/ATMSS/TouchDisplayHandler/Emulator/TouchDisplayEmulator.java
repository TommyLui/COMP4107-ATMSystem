package ATMSS.TouchDisplayHandler.Emulator;

import ATMSS.ATMSSStarter;
import ATMSS.TouchDisplayHandler.TouchDisplayHandler;
import AppKickstarter.misc.Msg;

import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.WindowEvent;


//======================================================================
// TouchDisplayEmulator
public class TouchDisplayEmulator extends TouchDisplayHandler {
    private final int WIDTH = 680;
    private final int HEIGHT = 520;
    private ATMSSStarter atmssStarter;
    private String id;
    private Stage myStage;
    private TouchDisplayEmulatorController touchDisplayEmulatorController;

    //------------------------------------------------------------
    // TouchDisplayEmulator
    public TouchDisplayEmulator(String id, ATMSSStarter atmssStarter) throws Exception {
	super(id, atmssStarter);
	this.atmssStarter = atmssStarter;
	this.id = id;
    } // TouchDisplayEmulator


    //------------------------------------------------------------
    // start
    public void start() throws Exception {
	Parent root;
	myStage = new Stage();
	FXMLLoader loader = new FXMLLoader();
	String fxmlName = "TouchDisplayInitial.fxml";
	loader.setLocation(TouchDisplayEmulator.class.getResource(fxmlName));
	root = loader.load();
	touchDisplayEmulatorController = (TouchDisplayEmulatorController) loader.getController();
	touchDisplayEmulatorController.initialize(id, atmssStarter, log, this);
	myStage.initStyle(StageStyle.DECORATED);
	myStage.setScene(new Scene(root, WIDTH, HEIGHT));
	myStage.setTitle("Touch Display");
	myStage.setResizable(false);
	myStage.setOnCloseRequest((WindowEvent event) -> {
	    atmssStarter.stopApp();
	    Platform.exit();
	});
	myStage.show();
    } // TouchDisplayEmulator


    //------------------------------------------------------------
    // handleUpdateDisplay
    protected void handleUpdateDisplay(Msg msg) {
	log.info(id + ": update display -- " + msg.getDetails());

	switch (msg.getDetails()) {
	    case "BlankScreen":
		reloadStage("TouchDisplayEmulator.fxml");
		break;

	    case "MainMenu":
		reloadStage("TouchDisplayMainMenu.fxml");
		break;

	    case "Confirmation":
		reloadStage("TouchDisplayConfirmation.fxml");
		break;

		case "PinInputPage":
		reloadStage("TouchDisplayPinInput.fxml");
		break;

		case "PinInputted":
			Platform.runLater(new Runnable() {
				@Override
				public void run() {
					try {
						touchDisplayEmulatorController.pinInput();
					} catch (Exception e) {
						log.severe(id + ": failed to update PinInputted");
						e.printStackTrace();
					}
				}
			});
			break;

		case "PinErase":
			Platform.runLater(new Runnable() {
				@Override
				public void run() {
					try {
						touchDisplayEmulatorController.pinErase();
					} catch (Exception e) {
						log.severe(id + ": failed to do PinErase");
						e.printStackTrace();
					}
				}
			});
			break;


//		case "CheckBalance":
//			reloadStage("TouchDisplayCheckBalance.fxml");
//			break;

	    default:
		log.severe(id + ": update display with unknown display type -- " + msg.getDetails());
		break;
	}
    } // handleUpdateDisplay

	//------------------------------------------------------------
	// handleBAMSUpdateDisplay
	protected void handleBAMSUpdateDisplay(Msg msg) {
    	if (msg.getDetails().contains("accounts")) {
			reloadStage("TouchDisplayCheckBalance.fxml");
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			touchDisplayEmulatorController.setStackPaneVisibiliy(msg.getDetails());
		} else if (msg.getDetails().contains("amount")) {
//    		System.out.println("I made it!!! "+msg.getDetails());
			reloadStage("TouchDisplayCheckAccBalance.fxml");
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			touchDisplayEmulatorController.setAccBalanceText(msg.getDetails());
		}
	} // handleBAMSUpdateDisplay

    //------------------------------------------------------------
    // reloadStage
    private void reloadStage(String fxmlFName) {
        TouchDisplayEmulator touchDisplayEmulator = this;
        Platform.runLater(new Runnable() {
	    @Override
	    public void run() {
		try {
		    log.info(id + ": loading fxml: " + fxmlFName);
		    Parent root;
		    FXMLLoader loader = new FXMLLoader();
		    loader.setLocation(TouchDisplayEmulator.class.getResource(fxmlFName));
		    root = loader.load();
		    touchDisplayEmulatorController = (TouchDisplayEmulatorController) loader.getController();
		    touchDisplayEmulatorController.initialize(id, atmssStarter, log, touchDisplayEmulator);
		    myStage.setScene(new Scene(root, WIDTH, HEIGHT));
		} catch (Exception e) {
		    log.severe(id + ": failed to load " + fxmlFName);
		    e.printStackTrace();
		}
	    }
	});
    } // reloadStage
} // TouchDisplayEmulator
