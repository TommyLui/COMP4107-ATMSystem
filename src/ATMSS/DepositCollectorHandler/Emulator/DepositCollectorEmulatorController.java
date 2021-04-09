package ATMSS.DepositCollectorHandler.Emulator;

import AppKickstarter.AppKickstarter;
import AppKickstarter.misc.MBox;
import AppKickstarter.misc.Msg;

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

    private String depositCollectStatus;
    private String slotStatus;
    public TextField slotStatusText;
    private int num100Note;
    private int num500Note;
    private int num1000Note;
    private int totalNote;
    private int totalCash;
    public TextField num100NoteLabel;
    public TextField num500NoteLabel;
    public TextField num1000NoteLabel;


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
            case "Collect":
                Date currentDate = new Date();
                num100Note = Integer.parseInt(num100NoteLabel.getText());
                num500Note = Integer.parseInt(num500NoteLabel.getText());
                num1000Note = Integer.parseInt(num1000NoteLabel.getText());
                break;
            /*
            case "Slot Opening":
                slotStatus(slotStatus);

            case "Slot Closing":
                slotStatus(slotStatus);
            */
            case "Count":
                totalNote = num100Note + num500Note + num1000Note;
                totalCash = (num100Note * 100) + (num500Note * 500) + (num1000Note * 1000);
                //Solution 1:
                System.out.print("Number of 100 = " + num100Note + ", Total 100 = " + num100Note * 100);
                System.out.print("Number of 500 = " + num500Note + ", Total 500 = " + num500Note * 500);
                System.out.print("Number of 1000 = " + num1000Note + ", Total 1000 = " + num1000Note * 1000);
                System.out.print("Number of total Note = " + totalNote + ", Total Cash = " + totalCash);

                //Solution 2:
                depositCollectorMBox.send(new Msg(id, depositCollectorMBox, Msg.Type.DC_Count_Cash,
                        "--------------------\n" +
                        "Number of 100 = " + num100Note + ", Total 100 = " + num100Note * 100 +"\n" +
                        "Number of 500 = " + num500Note + ", Total 500 = " + num500Note * 500 +"\n" +
                        "Number of 1000 = " + num1000Note + ", Total 1000 = " + num1000Note * 1000+"\n" +
                        "Number of total Note = " + totalNote + ", Total Cash = " + totalCash +"\n" +
                                "--------------------\n"));
                break;

            case "Error":
                sendErrorMsg("Error");
                break;

            case "Time out":
                sendErrorMsg("Time out");
                break;

            default:
                log.warning(id + ": Time Out: [" + btn.getText() + "]");

        } // buttonPressed
    }


    public void sendErrorMsg(String errMsg) {
        switch (errMsg) {
            case "Error":
                depositCollectStatus = "Notify ATM-SS on error";
                depositCollectorMBox.send(new Msg(id, depositCollectorMBox, Msg.Type.DC_Error, depositCollectStatus));
                break;

            case "Time out":
                depositCollectStatus = "Timeout";
                depositCollectorMBox.send(new Msg(id, depositCollectorMBox, Msg.Type.DC_TimeOut, depositCollectStatus));
                break;
        }
    }
/*
    public void slotStatus(String slot) {
        switch (slot) {
            case "opening":
                slotStatus = "opening";
                slotStatusText.setText("Slot is Opening");
                break;

            case "closing":
                slotStatus = "closing";
                slotStatusText.setText("Slot is closing");
                break;
        }
    }
 */
}
