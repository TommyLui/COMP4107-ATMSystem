package ATMSS.DepositCollectorHandler.Emulator;

import AppKickstarter.AppKickstarter;
import AppKickstarter.misc.MBox;
import AppKickstarter.misc.Msg;

import java.util.Date;
import java.util.logging.Logger;

import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

//======================================================================
// DepositCollectorEmulatorController
public class DepositCollectorEmulatorController {
    private String id;
    private AppKickstarter appKickstarter;
    private Logger log;
    private DepositCollectorEmulator depositCollectorEmulator;
    private MBox depositCollectorMBox;


    private String depositCollectStatus;
    private int num100Note;
    private int num500Note;
    private int num1000Note;
    private int totalNote;
    private int totalCash;
    private int amount;
    public TextField num100NoteLabel;
    public TextField num500NoteLabel;
    public TextField num1000NoteLabel;
    public TextField depositCollectStatusField;

    /**
     * Initialise the Deposit Collect emulator and its mailbox
     * @param id name of the application
     * @param appKickstarter The app kickstarter using.
     * @param log some message to print
     * @param depositCollectorEmulator a reference to the Deposit Collector
     */

    //------------------------------------------------------------
    // initialize
    public void initialize(String id, AppKickstarter appKickstarter, Logger log, DepositCollectorEmulator depositCollectorEmulator) {
        this.id = id;
        this.appKickstarter = appKickstarter;
        this.log = log;
        this.depositCollectorEmulator = depositCollectorEmulator;
        this.depositCollectorMBox = appKickstarter.getThread("DepositCollectorHandler").getMBox();
    } // initialize

    /**
     * Handle the button actions in Deposit Collector Emulator
     * @param actionEvent a action sent to the mailbox of Deposit Collector
     */

    //------------------------------------------------------------
    // buttonPressed
    public void buttonPressed(ActionEvent actionEvent) {
        Button btn = (Button) actionEvent.getSource();
        String btnTxt = btn.getText();
        log.info("The btn pressed:" + btnTxt);


        switch (btn.getText()) {
            case "Collect":
                Date currentDate = new Date();
                if(num100NoteLabel.getText() != "" && num100NoteLabel.getLength() != 0){
                    num100Note = Integer.parseInt(num100NoteLabel.getText());
                }else{
                    log.info("Enter Number on 100");
                }
                if(num500NoteLabel.getText() != "" && num500NoteLabel.getLength()!=0){
                    num500Note = Integer.parseInt(num500NoteLabel.getText());
                }else{
                    log.info("Enter Number on 500");
                }
                if(num1000NoteLabel.getText() != "" && num1000NoteLabel.getLength()!=0){
                    num1000Note = Integer.parseInt(num1000NoteLabel.getText());
                }else{
                    log.info("Enter Number on 1000");
                }

                totalNote = num100Note + num500Note + num1000Note;
                totalCash = (num100Note * 100) + (num500Note * 500) + (num1000Note * 1000);
                amount = (num100Note * 100) + (num500Note * 500) + (num1000Note * 1000);
                if(totalNote > 100){
                    log.info("Over Enter Cash");
                }
                depositCollectorMBox.send(new Msg(id, depositCollectorMBox, Msg.Type.DC_Collect_Cash,
                        totalNote + "," + amount));

                break;

            case "Count":
                totalCash = (num100Note * 100) + (num500Note * 500) + (num1000Note * 1000);

                depositCollectorMBox.send(new Msg(id, depositCollectorMBox, Msg.Type.DC_Count_Cash,
                        "\nNumber of $100 = " + num100Note + ", Total $100 = $" + num100Note * 100 + "\n" +
                                "Number of $500 = " + num500Note + ", Total $500 = $" + num500Note * 500 + "\n" +
                                "Number of $1000 = " + num1000Note + ", Total $1000 = $" + num1000Note * 1000 + "\n" +
                                "Number of $total Note = " + totalNote + ", Total Cash = $" + totalCash + "\n"));

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

    /**
     * Process the message inside the mailbox of Deposit Collector
     * @param errMsg a message sent to the mailbox of Deposit Collector
     */

    //------------------------------------------------------------
    // sendErrorMsg
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


    public void depositCollectStatus(String status) {
        depositCollectStatusField.setText(status);
    } // updateCardStatus

}
