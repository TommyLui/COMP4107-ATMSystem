package ATMSS.BuzzerHandler.Emulator;

import AppKickstarter.AppKickstarter;
import AppKickstarter.misc.MBox;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;

import java.util.logging.Logger;


//======================================================================
// TouchDisplayEmulatorController
public class BuzzerEmulatorController {
    private String id;
    private AppKickstarter appKickstarter;
    private Logger log;
    private BuzzerEmulator buzzerEmulator;
    private MBox buzzerMBox;

    public TextField buzzStatusText;
    public Rectangle buzzStatus;


    //------------------------------------------------------------
    // initialize
    public void initialize(String id, AppKickstarter appKickstarter, Logger log, BuzzerEmulator buzzerEmulator) {
        this.id = id;
	this.appKickstarter = appKickstarter;
	this.log = log;
	this.buzzerEmulator = buzzerEmulator;
	this.buzzerMBox = appKickstarter.getThread("BuzzerHandler").getMBox();
    } // initialize

    public void buttonPressed(ActionEvent actionEvent) {
        Button btn = (Button) actionEvent.getSource();
        String btnTxt = btn.getText();
        System.out.println("Buzz btn pressed:" + btnTxt);
        updateBuzzerStatus(btnTxt);
    } // buttonPressed

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
    } // updateCardStatus

} // TouchDisplayEmulatorController
