package ATMSS.TouchDisplayHandler.Emulator;

import AppKickstarter.AppKickstarter;
import AppKickstarter.misc.MBox;
import AppKickstarter.misc.Msg;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

import java.awt.*;
import java.util.ArrayList;
import java.util.logging.Logger;


//======================================================================
// TouchDisplayEmulatorController
public class TouchDisplayEmulatorController {
    private String id;
    private AppKickstarter appKickstarter;
    private Logger log;
    private TouchDisplayEmulator touchDisplayEmulator;
    private MBox touchDisplayMBox;

    public StackPane account1StackPane;
    public StackPane account2StackPane;
    public StackPane account3StackPane;
    public StackPane account4StackPane;

    public Text account1Text;
    public Text account2Text;
    public Text account3Text;
    public Text account4Text;
    public Text balanceErrorText;

    public Text accBalanceAccNo;
    public Text accBalanceCardNo;
    public Text accBalanceAmount;
    public ArrayList<Text> accBalanceTexts = new ArrayList<>();

    public ArrayList<StackPane> stackPanes = new ArrayList<>();
    public ArrayList<Text> accountTexts = new ArrayList<>();

    public Label pinLabel;
    public Label pinWrongLabel;
    public Label notifyLabel;


    //------------------------------------------------------------
    // initialize
    public void initialize(String id, AppKickstarter appKickstarter, Logger log, TouchDisplayEmulator touchDisplayEmulator) {
    this.id = id;
	this.appKickstarter = appKickstarter;
	this.log = log;
	this.touchDisplayEmulator = touchDisplayEmulator;
	this.touchDisplayMBox = appKickstarter.getThread("TouchDisplayHandler").getMBox();
    } // initialize

    //------------------------------------------------------------
    // td_deposit
    public void td_deposit(Event event) {
        System.out.println("event: " + event);
        touchDisplayMBox.send(new Msg(id, touchDisplayMBox, Msg.Type.BAMS_Request, "GetAccDeposit"));
    }
    // td_deposit
    //------------------------------------------------------------
    // td_refresh
    public void td_refresh(Event event) {
        System.out.println("event: " + event);
        touchDisplayMBox.send(new Msg(id, touchDisplayMBox, Msg.Type.TD_UpdateDisplay, "RefreshDeposit"));
    }
    // td_refresh
    //------------------------------------------------------------
    //------------------------------------------------------------
    // td_AccDeposit
    public void td_AccDeposit(Event event) {
        String source = event.getSource().toString(); //yields complete string
        String msgDetail;

        if (source.contains("StackPane[id=account1StackPane]")) {
//            System.out.println("Src1: "+ source);
            msgDetail = "DepositReq,1";
            touchDisplayMBox.send(new Msg(id, touchDisplayMBox, Msg.Type.TD_selectedAcc, msgDetail));
        } else if (source.contains("StackPane[id=account2StackPane]")) {
//            System.out.println("Src2: "+ source);
            msgDetail = "DepositReq,2";
            touchDisplayMBox.send(new Msg(id, touchDisplayMBox, Msg.Type.TD_selectedAcc, msgDetail));
        } else if (source.contains("StackPane[id=account3StackPane]")) {
//            System.out.println("Src3: "+ source);
            msgDetail = "DepositReq,3";
            touchDisplayMBox.send(new Msg(id, touchDisplayMBox, Msg.Type.TD_selectedAcc, msgDetail));
        } else if (source.contains("StackPane[id=account4StackPane]")) {
//            System.out.println("Src4: "+ source);
            msgDetail = "DepositReq,4";
            touchDisplayMBox.send(new Msg(id, touchDisplayMBox, Msg.Type.TD_selectedAcc, msgDetail));
        }
    }
    // td_AccDeposit
    //------------------------------------------------------------
    // td_changePin
    public void td_changePin(Event event) {
        System.out.println("event: " + event);
        touchDisplayMBox.send(new Msg(id, touchDisplayMBox, Msg.Type.TD_UpdateDisplay, "ChangePinExisting"));
    }
    // td_changePin
    //------------------------------------------------------------
    // td_checkAccBalance
    public void td_checkBalance(Event event) {
        System.out.println("event: " + event);
        touchDisplayMBox.send(new Msg(id, touchDisplayMBox, Msg.Type.BAMS_Request, "GetAccReq"));
    } // td_checkAccBalance

    //------------------------------------------------------------
    // td_returnMainMenu
    public void td_returnMainMenu(Event event) {
        System.out.println("event: " + event);
        touchDisplayMBox.send(new Msg(id, touchDisplayMBox, Msg.Type.TD_UpdateDisplay, "MainMenu"));
    } // td_returnMainMenu

    public void td_ejectCard(Event event) {
        System.out.println("event: " + event);
        touchDisplayMBox.send(new Msg(id, touchDisplayMBox, Msg.Type.TD_UpdateDisplay, "EjectCard"));
    }

    //------------------------------------------------------------
    // td_checkAccBalance
    public void td_checkAccBalance(MouseEvent event) {
        String source = event.getSource().toString(); //yields complete string
        String msgDetail;

        if (source.contains("StackPane[id=account1StackPane]")) {
//            System.out.println("Src1: "+ source);
            msgDetail = "EnquiryReq,1";
            touchDisplayMBox.send(new Msg(id, touchDisplayMBox, Msg.Type.BAMS_Request, msgDetail));
        } else if (source.contains("StackPane[id=account2StackPane]")) {
//            System.out.println("Src2: "+ source);
            msgDetail = "EnquiryReq,2";
            touchDisplayMBox.send(new Msg(id, touchDisplayMBox, Msg.Type.BAMS_Request, msgDetail));
        } else if (source.contains("StackPane[id=account3StackPane]")) {
//            System.out.println("Src3: "+ source);
            msgDetail = "EnquiryReq,3";
            touchDisplayMBox.send(new Msg(id, touchDisplayMBox, Msg.Type.BAMS_Request, msgDetail));
        } else if (source.contains("StackPane[id=account4StackPane]")) {
//            System.out.println("Src4: "+ source);
            msgDetail = "EnquiryReq,4";
            touchDisplayMBox.send(new Msg(id, touchDisplayMBox, Msg.Type.BAMS_Request, msgDetail));
        }

    } // td_checkAccBalance

    //------------------------------------------------------------
    // td_mouseClick
    public void td_mouseClick(MouseEvent mouseEvent) {
        int x = (int) mouseEvent.getX();
	    int y = (int) mouseEvent.getY();

	log.fine(id + ": mouse clicked: -- (" + x + ", " + y + ")");
	touchDisplayMBox.send(new Msg(id, touchDisplayMBox, Msg.Type.TD_MouseClicked, x + " " + y));
    } // td_mouseClick

    //------------------------------------------------------------
    // modifyNotifyLabel
    public void modifyNotifyLabel(String msgDetails) {
        switch (msgDetails) {
            case "ChangePinExisting":
                notifyLabel.setText("Please input your existing pin (should be 6-digits long)");
                break;
            case "ChangePinNew":
                notifyLabel.setText("Please input your new pin (should be 6-digits long)");
                break;
        }
    } // modifyNotifyLabel

    //------------------------------------------------------------
    // setChangePinText
    public void setChangePinText() {
        accBalanceCardNo.setText("Change pin successful!");
    } // setChangePinText

    //------------------------------------------------------------
    // setBalanceErrorText
    public void setBalanceErrorText () {
        balanceErrorText.setText("You have wrongly typed in your existing password / new password 3 times, please try again.");
    } // setBalanceErrorText

    //------------------------------------------------------------
    // setAccBalanceText
    public void setAccBalanceText(String msgDetails) {
        accBalanceTexts.add(accBalanceCardNo);
        accBalanceTexts.add(accBalanceAccNo);
        accBalanceTexts.add(accBalanceAmount);
        int i = 0;
        for (String msgDetail: msgDetails.split("@")) {
            accBalanceTexts.get(i).setText(msgDetail);
            i++;
        }
    } // setAccBalanceText

    //------------------------------------------------------------
    // setStackPaneVisibiliy
    public void setStackPaneVisibiliy(String msgDetails) {
        stackPanes.add(account1StackPane);
        stackPanes.add(account2StackPane);
        stackPanes.add(account3StackPane);
        stackPanes.add(account4StackPane);

        accountTexts.add(account1Text);
        accountTexts.add(account2Text);
        accountTexts.add(account3Text);
        accountTexts.add(account4Text);

        int i = 0;
        for (String account: msgDetails.split("/")) {

            if (account.contains(":")) {
                account = account.split(":")[1];
            }
//            System.out.println(stackPanes.get(i));
            stackPanes.get(i).setVisible(true);
            accountTexts.get(i).setText(account);
//            System.out.println(account);
            i++;
        }
//        cardReaderTextArea.appendText(status+"\n");
    } // setStackPaneVisibiliy


    public void pinInput() {
        String currentPinLabel = pinLabel.getText();
        currentPinLabel = currentPinLabel + "*";
        pinLabel.setText(currentPinLabel);
        System.out.println("currentPinLabel: " + currentPinLabel);
    }

    public void pinErase() {
        String currentPinLabel = pinLabel.getText();
        currentPinLabel = currentPinLabel.substring(0, currentPinLabel.length() - 1);
        pinLabel.setText(currentPinLabel);
        System.out.println("currentPinLabel: " + currentPinLabel);
    }

    public void pinWrong() {
        pinLabel.setText("");
        pinWrongLabel.setText("Please input a correct pin");
        pinWrongLabel.setTextFill(Color.color(0.9,0,0));
    }

} // TouchDisplayEmulatorController
