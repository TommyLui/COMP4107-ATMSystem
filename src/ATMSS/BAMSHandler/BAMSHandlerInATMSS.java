package ATMSS.BAMSHandler;

import AppKickstarter.AppKickstarter;
import AppKickstarter.misc.*;
//import com.sun.javafx.tools.packager.Log;

import java.io.IOException;

//======================================================================
// BAMSHandler
public class BAMSHandlerInATMSS extends AppThread {
    protected MBox atmss = null;
    protected String urlPrefix = "http://cslinux0.comp.hkbu.edu.hk/comp4107_20-21_grp10/BAMS-v02.php";
    protected BAMSHandler bams = null;

    //------------------------------------------------------------
    // BAMSHandlerInATMSS
    public BAMSHandlerInATMSS(String id, AppKickstarter appKickstarter) {
        super(id, appKickstarter);
        bams = new BAMSHandler(urlPrefix);
    } // BAMSHandlerInATMSS

    //------------------------------------------------------------
    // run
    public void run() {
        atmss = appKickstarter.getThread("ATMSS").getMBox();
        log.info(id + ": starting...");

        for (boolean quit = false; !quit; ) {
            Msg msg = mbox.receive();

            log.fine(id + ": message received: [" + msg + "].");

            switch (msg.getType()) {
                case Poll:
                    atmss.send(new Msg(id, mbox, Msg.Type.PollAck, id + " is up!"));
                    break;

                case Terminate:
                    quit = true;
                    break;

                case BAMS_Request:
                    handleBAMSRequest(msg.getDetails());
                    break;

                default:
                    processMsg(msg);
            }
        }

        // declaring our departure
        appKickstarter.unregThread(this);
        log.info(id + ": terminating...");
    } // run


    //------------------------------------------------------------
    // processMsg
    protected void processMsg(Msg msg) {
        log.warning(id + ": unknown message type: [" + msg + "]");
    } // processMsg

    protected void handleBAMSRequest(String request) {
        try {
            if (request.contains("LoginReq")) {
                Login(bams, request);
            } else if (request.contains("GetAccReq")) {
                getAcc(bams, request);
            } else if (request.contains("EnquiryReq")) {
                enquiry(bams, request);
            } else if (request.contains("ChgPinReq")) {
                chgPinReq(bams, request);
            } else if (request.contains("DepositReq")) {
                testDeposit(bams, request);
            }else if (request.contains("GetAccDeposit")) {
                getAcc(bams, request);
            }else if (request.contains("Logout")){
//                System.out.println("handle logout");
                Logout(bams, request);
            } else {
                switch (request) {
                    case "LogoutReq":
                        testLogout(bams);
                        break;

//                    case "GetAccReq":
//                        getAcc(bams);
//                    testGetAcc(bams);
//                        break;

                    case "WithdrawReq":
                        testWithdraw(bams);
                        break;

                    case "DepositReq":
                        //testDeposit(bams);
                        break;

//                    case "EnquiryReq1":
//                        enquiry(bams, "1");
////                    testEnquiry(bams);
//                        break;
//
//                    case "EnquiryReq2":
//                        enquiry(bams, "2");
////                    testEnquiry(bams);
//                        break;
//
//                    case "EnquiryReq3":
//                        enquiry(bams, "3");
////                    testEnquiry(bams);
//                        break;
//
//                    case "EnquiryReq4":
//                        enquiry(bams, "4");
////                    testEnquiry(bams);
//                        break;

                    case "TransferReq":
                        testTransfer(bams);
                        break;

                    case "AccStmtReq":
                        testAccStmtReq(bams);
                        break;

                    case "ChqBookReq":
                        testChqBookReq(bams);
                        break;

//                    case "ChgPinReq":
//                        chgPinReq(bams);
//                        break;
                }
        }

        } catch (BAMSInvalidReplyException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //------------------------------------------------------------
    // Login
    protected void Login(BAMSHandler bams, String request) throws BAMSInvalidReplyException, IOException {
        String[] details = request.split(",");
        System.out.println(details[0]);
        System.out.println(details[1]);
        System.out.println(details[2]);
        System.out.println("Login:");
        String cred = bams.login(details[1], details[2]);
        System.out.println("cred: " + cred);
        System.out.println();
        atmss.send(new Msg(id, mbox, Msg.Type.BAMS_Response, "cred:" + cred));
    } // Login

    // Logout
    protected void Logout(BAMSHandler bams, String request) throws BAMSInvalidReplyException, IOException {
        System.out.println("Logout:");
        String[] details = request.split(",");
        System.out.println(details[0]);
        System.out.println(details[1]);
        System.out.println(details[2]);
        String result = bams.logout(details[1], details[2]);
        System.out.println("result: " + result);
        System.out.println();
        atmss.send(new Msg(id, mbox, Msg.Type.BAMS_Response, "logout result: " + result));
    } // Logout


    //------------------------------------------------------------
    // testLogin
    protected void testLogin(BAMSHandler bams) throws BAMSInvalidReplyException, IOException {
        System.out.println("Login:");
        String cred = bams.login("4107-7014", "123456");
        System.out.println("cred: " + cred);
        System.out.println();
        atmss.send(new Msg(id, mbox, Msg.Type.BAMS_Response, "cred: " + cred));
    } // testLogin


    //------------------------------------------------------------
    // testLogout
    protected void testLogout(BAMSHandler bams) throws BAMSInvalidReplyException, IOException {
        System.out.println("Logout:");
        String result = bams.logout("12345678-0", "cred-0");
        System.out.println("result: " + result);
        System.out.println();
        atmss.send(new Msg(id, mbox, Msg.Type.BAMS_Response, "logout result: " + result));
    } // testLogout

    //------------------------------------------------------------
    // getAcc
    protected void getAcc(BAMSHandler bams, String request) throws BAMSInvalidReplyException, IOException {
        if(request.contains("GetAccDeposit")){
            System.out.println("GetAcc:");
            String[] details = request.split(",");
            System.out.println(details[0]);
            System.out.println(details[1]);
            System.out.println(details[2]);

            String cardNo = details[1];
            String cred = details[2];

            log.info("cardNo: " + cardNo);
            log.info("cred: " + cred);

            String accounts = bams.getAccounts(cardNo, cred);

            System.out.println("accounts: " + accounts);

            if (!accounts.contains("Error")) {
//            System.out.println("accounts: " + accounts);
                System.out.println();
                atmss.send(new Msg(id, mbox, Msg.Type.BAMS_Response, "accountDeposit: " + accounts));
            } else {
                // Handle error
                atmss.send(new Msg(id, mbox, Msg.Type.BAMS_Error, "Error"));
            }
        } else if (request.contains("GetAccReq")){
            System.out.println("GetAcc:");
            String[] details = request.split(",");
            System.out.println(details[0]);
            System.out.println(details[1]);
            System.out.println(details[2]);

            String cardNo = details[1];
            String cred = details[2];

            log.info("cardNo: " + cardNo);
            log.info("cred: " + cred);

            String accounts = bams.getAccounts(cardNo, cred);

            System.out.println("accounts: " + accounts);

            if (!accounts.contains("Error")) {
//            System.out.println("accounts: " + accounts);
                System.out.println();
                atmss.send(new Msg(id, mbox, Msg.Type.BAMS_Response, "accounts: " + accounts));
            } else {
                // Handle error
                atmss.send(new Msg(id, mbox, Msg.Type.BAMS_Error, "Error"));
            }
        }
    } // getAcc


//    //------------------------------------------------------------
//    // testGetAcc
//    protected void testGetAcc(BAMSHandler bams) throws BAMSInvalidReplyException, IOException {
//        System.out.println("GetAcc:");
//        String accounts = bams.getAccounts("12345678-1", "cred-1");
//        System.out.println("accounts: " + accounts);
//        System.out.println();
//        atmss.send(new Msg(id, mbox, Msg.Type.BAMS_Response, "accounts: " + accounts));
//    } // testGetAcc


    //------------------------------------------------------------
    // testWithdraw
    protected void testWithdraw(BAMSHandler bams) throws BAMSInvalidReplyException, IOException {
        System.out.println("Withdraw:");
        int outAmount = bams.withdraw("12345678-2", "111-222-332", "cred-2", "109702");
        System.out.println("outAmount: " + outAmount);
        System.out.println();
        atmss.send(new Msg(id, mbox, Msg.Type.BAMS_Response, "outAmount: " + outAmount));
    } // testWithdraw


    //------------------------------------------------------------
    // testDeposit
    protected void testDeposit(BAMSHandler bams, String request) throws BAMSInvalidReplyException, IOException {
        try {
            System.out.println("Deposit:");
            String[] details = request.split(",");
            String cardNo = details[1];
            String aid = details[2];
            String cred = details[3];
            String amount = details[4];

            System.out.println("Card no: " + cardNo);
            System.out.println("aid: " + aid);
            System.out.println("cred: " + cred);
            System.out.println("amount: " + amount);

            double depAmount = bams.deposit(cardNo, aid, cred, amount);
            System.out.println("depAmount: " + depAmount);
            System.out.println();

            atmss.send(new Msg(id, mbox, Msg.Type.BAMS_Response,
                    "reDeposit," +  depAmount));
        } catch (NumberFormatException exception) {
            atmss.send(new Msg(id, mbox, Msg.Type.BAMS_Error, "Error"));
        }

    } // testDeposit

    //------------------------------------------------------------
    // enquiry
    protected void enquiry(BAMSHandler bams, String request) throws BAMSInvalidReplyException, IOException {

        try {
            System.out.println("Enquiry:");

            String[] details = request.split(",");
            String aid = details[1];
            String cardNo = details[2];
            String cred = details[3];

            System.out.println("Card no: " + cardNo);
            System.out.println("aid: " + aid);

            double amount = bams.enquiry(cardNo, aid, cred);
            System.out.println("amount: " + amount);
            atmss.send(new Msg(id, mbox, Msg.Type.BAMS_Response,
                    "card no: " + cardNo + "@account id: " + aid + "@amount: " + amount));
        } catch (NumberFormatException exception) {
            atmss.send(new Msg(id, mbox, Msg.Type.BAMS_Error, "Error"));
        }
    } // enquiry


//    //------------------------------------------------------------
//    // testEnquiry
//    protected void testEnquiry(BAMSHandler bams) throws BAMSInvalidReplyException, IOException {
//        System.out.println("Enquiry:");
//        double amount = bams.enquiry("12345678-4", "111-222-334", "cred-4");
//        System.out.println("amount: " + amount);
//        System.out.println();
//        atmss.send(new Msg(id, mbox, Msg.Type.BAMS_Response, "amount: " + amount));
//    } // testEnquiry


    //------------------------------------------------------------
    // testTransfer
    protected void testTransfer(BAMSHandler bams) throws BAMSInvalidReplyException, IOException {
        System.out.println("Transfer:");
        double transAmount = bams.transfer("12345678-5", "cred-5", "111-222-335", "11-222-336", "109705");
        System.out.println("transAmount: " + transAmount);
        System.out.println();
        atmss.send(new Msg(id, mbox, Msg.Type.BAMS_Response, "transAmount: " + transAmount));
    } // testTransfer


    //------------------------------------------------------------
    // testAccStmtReq
    protected void testAccStmtReq(BAMSHandler bams) throws BAMSInvalidReplyException, IOException {
        System.out.println("AccStmtReq:");
        String result = bams.accStmtReq("12345678-4", "111-222-334", "cred-6");
        System.out.println("result: " + result);
        System.out.println();
        atmss.send(new Msg(id, mbox, Msg.Type.BAMS_Response, "accStmtReq result: " + result));
    } // testAccStmtReq


    //------------------------------------------------------------
    // testChqBookReq
    protected void testChqBookReq(BAMSHandler bams) throws BAMSInvalidReplyException, IOException {
        System.out.println("ChqBookReq:");
        String result = bams.chqBookReq("12345678-4", "111-222-334", "cred-7");
        System.out.println("result: " + result);
        System.out.println();
        atmss.send(new Msg(id, mbox, Msg.Type.BAMS_Response, "chqBookReq result: " + result));
    } // testChqBookReq


    //------------------------------------------------------------
    // chgPinReq
    protected void chgPinReq(BAMSHandler bams, String request) throws BAMSInvalidReplyException, IOException {

        System.out.println("chgPinReq:");
        String[] details = request.split(",");
        System.out.println(details[0]);
        System.out.println(details[1]);
        System.out.println(details[2]);
        System.out.println(details[3]);
        System.out.println(details[4]);

        String cardNo = details[1];
        String oldPin = details[2];
        String newPin = details[3];
        String cred = details[4];

        log.info("cardNo: "+cardNo);
        log.info("oldPin: "+oldPin);
        log.info("newPin: "+newPin);
        log.info("cred: "+cred);

        System.out.println("ChgPinReq:");

        String result = bams.chgPinReq(cardNo, oldPin, newPin, cred);

        System.out.println("result: " + result);
        System.out.println();

        if (result.equals("succ")) {
            atmss.send(new Msg(id, mbox, Msg.Type.BAMS_Response, "chgPinReq result: " + result + ","+ newPin));
        } else {

        }
    } // chgPinReq


//    //------------------------------------------------------------
//    // testChgPinReq
//    protected void testChgPinReq(BAMSHandler bams) throws BAMSInvalidReplyException, IOException {
//        System.out.println("ChgPinReq:");
//        String result = bams.chgPinReq("12345678-4", "456123789", "987321654", "cred-8");
//        System.out.println("result: " + result);
//        System.out.println();
//        atmss.send(new Msg(id, mbox, Msg.Type.BAMS_Response, "chgPinReq result: " + result));
//    } // testChgPinReq

}
