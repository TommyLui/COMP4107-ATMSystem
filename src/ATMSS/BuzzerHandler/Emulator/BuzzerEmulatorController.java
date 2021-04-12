package ATMSS.BuzzerHandler.Emulator;

import AppKickstarter.AppKickstarter;
import AppKickstarter.misc.MBox;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;

import java.util.logging.Logger;

/**
 * Buzzer Emulator Controller class is using to interact with the buzzer emulator
 */
//======================================================================
// BuzzerEmulatorController
public class BuzzerEmulatorController {
    private String id;
    private AppKickstarter appKickstarter;
    private Logger log;
    private BuzzerEmulator buzzerEmulator;
    private MBox buzzerMBox;

    public TextField buzzStatusText;
    public Rectangle buzzStatus;

    /**
     * Initialize the reference
     * @param id the name of buzzer
     * @param appKickstarter a reference to the AppKickstarter
     * @param log a reference to the logger
     * @param buzzerEmulator a reference to the emulator
     */
    //------------------------------------------------------------
    // initialize
    public void initialize(String id, AppKickstarter appKickstarter, Logger log, BuzzerEmulator buzzerEmulator) {
        this.id = id;
	this.appKickstarter = appKickstarter;
	this.log = log;
	this.buzzerEmulator = buzzerEmulator;
	this.buzzerMBox = appKickstarter.getThread("BuzzerHandler").getMBox();
    } // initialize

    /**
     * Handle the button press event
     * @param actionEvent
     */
    public void buttonPressed(ActionEvent actionEvent) {
        Button btn = (Button) actionEvent.getSource();
        String btnTxt = btn.getText();
        log.info("Buzz btn pressed:" + btnTxt);
        updateBuzzerStatus(btnTxt);
    } // buttonPressed

    /**
     * Directly control the buzzer start and stop
     * @param status start or stop the buzzer
     */
    public void updateBuzzerStatus(String status) {
        switch (status){
            case "Buzz Start":
                buzzStatus.setFill(Paint.valueOf("#891500"));
                buzzStatusText.setText("Buzzer is buzzing...");
                break;

            case "Buzz Stop":
                buzzStatus.setFill(Paint.valueOf("#068900"));
                buzzStatusText.setText("Buzzer is ready");
                break;
        }
    } // updateBuzzerStatus

} // BuzzerEmulatorController
