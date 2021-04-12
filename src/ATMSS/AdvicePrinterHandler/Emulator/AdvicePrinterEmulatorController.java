package ATMSS.AdvicePrinterHandler.Emulator;

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


//======================================================================
// AdvicePrinterEmulatorController
public class AdvicePrinterEmulatorController {
    private String id;
    private AppKickstarter appKickstarter;
    private Logger log;
    private AdvicePrinterEmulator advicePrinterEmulator;
    private MBox advicePrinterMBox;

    public Label remainingPaperLabel;
    public TextArea advicePrinterTextArea;
    public TextArea logTextArea;

    private int remainingPapers;
    private String usage;
    private Date date;
    private String cardNo;
    private String name;
    private double amountOfMoney;
    private String refNo;
    private String apStatus;

    /**
     * Store references to the id, the AppKickstarter, Log, and the AdvicePrinterEmulator
     * @param id the name of the AdvicePrinterEmulator
     * @param appKickstarter a reference to the AppKickstarter
     * @param log a reference to the Log
     * @param advicePrinterEmulator a reference to the AdvicePrinterEmulator
     */
    //------------------------------------------------------------
    // initialize
    public void initialize(String id, AppKickstarter appKickstarter, Logger log, AdvicePrinterEmulator advicePrinterEmulator) {
        this.id = id;
        this.appKickstarter = appKickstarter;
	this.log = log;
	this.advicePrinterEmulator = advicePrinterEmulator;
	this.advicePrinterMBox = appKickstarter.getThread("AdvicePrinterHandler").getMBox();
    } // initialize

    /**
     * Handle the button actions in AdvicePrinterEmulator
     * @param actionEvent an event from the user
     */
    //------------------------------------------------------------
    // buttonPressed
    public void buttonPressed(ActionEvent actionEvent) {
        Button btn = (Button) actionEvent.getSource();

        switch (btn.getText()) {
            case "Print receipt":
                Date currentDate = new Date();
                advicePrinterMBox.send(new Msg(id, advicePrinterMBox, Msg.Type.TEST_AP_PrintReceipt,
                        "--------------------\n" +
                        "Ref No.: "+"#"+(new Timestamp(currentDate.getTime()).getTime()) +"\n" +
                        "Date: "+currentDate+"\n" +
                        "Usage: "+"Deposit"+"\n" +
                        "Card Holder: "+"Mary"+"\n" +
                        "Card No. (From): "+"1234-5678"+"\n" +
                        "Card No. (To): "+"4567-8901"+"\n" +
                        "Amount of money: $"+1000.5+"\n" +
                        "--------------------\n"));
                break;

            case "Refill paper":
                remainingPapers = Integer.parseInt(remainingPaperLabel.getText());
                remainingPaperLabel.setText(Integer.toString(remainingPapers+1));
                logTextArea.appendText("1 Paper refilled"+"\n");
                log.info("Advice printer: 1 Paper refilled\n");
                break;

            case "Jam paper":
                sendErrorMsg("Jam paper");
                break;
            default:
                log.warning(id + ": unknown button: [" + btn.getText() + "]");
        } // buttonPressed
    }

    /**
     * Handle erroneous situation in Advice Printer
     * @param errMsg Corresponding error message
     */
    //------------------------------------------------------------
    // sendErrorMsg
    public void sendErrorMsg(String errMsg) {
        switch(errMsg) {
            case "Jam paper":
                apStatus = "paper jam";
//                advicePrinterMBox.send(new Msg(id, advicePrinterMBox, Msg.Type.BAMS_Request, "LoginReq"));
                break;

            case "running out of paper":
                apStatus = "running out of paper";
                break;
        }

        logTextArea.appendText(apStatus+"\n");
        advicePrinterMBox.send(new Msg(id, advicePrinterMBox, Msg.Type.AP_Error, apStatus));
    } // sendErrorMsg

    /**
     * Handle the print advice request
     * @param content the content in the receipt
     */
    //------------------------------------------------------------
    // printAdvice
    public void printAdvice(String content) {
//        this.usage = usage;
//        this.date = date;
//        this.cardNo = cardNo;
//        this.name = name;
//        this.amountOfMoney = amountOfMoney;
//        this.refNo = refNo;
        remainingPapers = Integer.parseInt(remainingPaperLabel.getText());

        if (remainingPapers > 0) {

            apStatus = "print advice okay";

            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    remainingPaperLabel.setText(Integer.toString(remainingPapers-1));
                    advicePrinterTextArea.appendText(content);
                    logTextArea.appendText(apStatus+"\n");
                }
            });

            advicePrinterMBox.send(new Msg(id, advicePrinterMBox, Msg.Type.AP_PrintSuccessful, apStatus));
        } else {
            sendErrorMsg("running out of paper");
//            apStatus = "running out of paper";
//            logTextArea.appendText("running out of paper"+"\n");
//            advicePrinterMBox.send(new Msg(id, advicePrinterMBox, Msg.Type.AP_Error, apStatus));
        }
    } // printAdvice
} // AdvicePrinterEmulatorController
