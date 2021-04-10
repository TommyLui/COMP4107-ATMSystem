package ATMSS.CashDispenserHandler.Emulator;

import AppKickstarter.AppKickstarter;
import AppKickstarter.misc.MBox;
import AppKickstarter.misc.Msg;
import java.util.logging.Logger;

import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;

//======================================================================
// CashDispenserEmulatorController
public class
CashDispenserEmulatorController {
    private String id;
    private AppKickstarter appKickstarter;
    private Logger log;
    private CashDispenserEmulator cashDispenserEmulator;
    public TextField ProvideCashField;
    private MBox cashDispenserMBox;
    public TextField cashDispenserStatusField;

    //------------------------------------------------------------
    // initialize
    public void initialize(String id, AppKickstarter appKickstarter, Logger log, CashDispenserEmulator cashDispenserEmulator) {
        this.id = id;
        this.appKickstarter = appKickstarter;
        this.log = log;
        this.cashDispenserEmulator = cashDispenserEmulator;
        this.cashDispenserMBox = appKickstarter.getThread("CashDispenserHandler").getMBox();
    } // initialize

    public void buttonPressed(ActionEvent actionEvent) {
        Button btn = (Button) actionEvent.getSource();
        String btnTxt = btn.getText();
        updateCashDispenserStatus(btnTxt);
    }

    public void updateCashDispenserStatus(String status) {
        switch (status){
            case "Provide Cash":
                cashDispenserStatusField.setText(ProvideCashField.getText());
                cashDispenserMBox.send(new Msg(id, cashDispenserMBox, Msg.Type.CD_provideCash, ProvideCashField.getText()));
                break;

            case "Get Cash":
                cashDispenserStatusField.setText("Cash Dispenser Ready");
                cashDispenserMBox.send(new Msg(id, cashDispenserMBox, Msg.Type.CD_GetCash, cashDispenserStatusField.getText()));
                break;

            case "Not Enough Cash":
                cashDispenserStatusField.setText("Not Enough Cash");
                cashDispenserMBox.send(new Msg(id, cashDispenserMBox, Msg.Type.CD_NotEnoughCash, ProvideCashField.getText()));
                break;

            case "Time Out":
                cashDispenserStatusField.setText("Time Out");
                cashDispenserMBox.send(new Msg(id, cashDispenserMBox, Msg.Type.CD_TimeOut, ProvideCashField.getText()));
                break;

            case "Error":
                cashDispenserStatusField.setText("Error");
                cashDispenserMBox.send(new Msg(id, cashDispenserMBox, Msg.Type.CD_Error, ProvideCashField.getText()));
                break;
        }
    } // updateCashDispenser

} // CashDispenserEmulatorController
