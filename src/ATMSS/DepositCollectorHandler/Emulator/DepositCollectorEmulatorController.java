package ATMSS.DepositCollectorHandler.Emulator;

import AppKickstarter.AppKickstarter;
import AppKickstarter.misc.MBox;
import AppKickstarter.misc.Msg;

import java.sql.Timestamp;
import java.util.Date;
import java.util.logging.Logger;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import javax.xml.soap.Text;

//======================================================================
// DepositCollectorEmulatorController
public class DepositCollectorEmulatorController {
    private String id;
    private AppKickstarter appKickstarter;
    private Logger log;
    private DepositCollectorEmulator depositCollectorEmulator;
    private MBox depositCollectorMBox;

    public Label remainingPaperLabel;
    public TextArea advicePrinterTextArea;
    public TextArea logTextArea;


    // initialize
    public void initialize(String id, AppKickstarter appKickstarter, Logger log, DepositCollectorEmulator depositCollectorEmulator) {
        this.id = id;
        this.appKickstarter = appKickstarter;
        this.log = log;
        this.depositCollectorEmulator = depositCollectorEmulator;
        this.depositCollectorMBox = appKickstarter.getThread("depositCollectorHandler").getMBox();
    } // initialize
    // buttonPressed
    public void buttonPressed(ActionEvent actionEvent) {
        Button btn = (Button) actionEvent.getSource();

        switch (btn.getText()) {
            case "Enter":
                Date currentDate = new Date();

        } // buttonPressed
    }
}
