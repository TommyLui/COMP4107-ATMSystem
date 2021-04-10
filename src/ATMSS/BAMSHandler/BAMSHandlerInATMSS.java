package ATMSS.BAMSHandler;

import AppKickstarter.AppKickstarter;
import AppKickstarter.misc.*;
import ATMSS.BAMSHandler.BAMSHandler;
import ATMSS.BAMSHandler.BAMSInvalidReplyException;

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
            switch (request) {
                case "LoginReq":
                    testLogin(bams);
                    break;

                case "LogoutReq":
                    testLogout(bams);
                    break;

                case "GetAccReq":
                    getAcc(bams);
//                    testGetAcc(bams);
                    break;

                case "WithdrawReq":
                    testWithdraw(bams);
                    break;

                case "DepositReq":
                    testDeposit(bams);
                    break;

                case "EnquiryReq1":
                    enquiry(bams, "1");
//                    testEnquiry(bams);
                    break;

                case "EnquiryReq2":
                    enquiry(bams, "2");
//                    testEnquiry(bams);
                    break;

                case "EnquiryReq3":
                    enquiry(bams, "3");
//                    testEnquiry(bams);
                    break;

                case "EnquiryReq4":
                    enquiry(bams, "4");
//                    testEnquiry(bams);
                    break;

                case "TransferReq":
                    testTransfer(bams);
                    break;

                case "AccStmtReq":
                    testAccStmtReq(bams);
                    break;

                case "ChqBookReq":
                    testChqBookReq(bams);
                    break;

                case "ChgPinReq":
                    testChgPinReq(bams);
                    break;
            }


        } catch (BAMSInvalidReplyException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

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
    protected void getAcc(BAMSHandler bams) throws BAMSInvalidReplyException, IOException {
        System.out.println("GetAcc:");
        String accounts = bams.getAccounts("4107-7014", "1234");
        System.out.println("accounts: " + accounts);
        System.out.println();
        atmss.send(new Msg(id, mbox, Msg.Type.BAMS_Response, "accounts: " + accounts));
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
    protected void testDeposit(BAMSHandler bams) throws BAMSInvalidReplyException, IOException {
        System.out.println("Deposit:");
        double depAmount = bams.deposit("12345678-3", "111-222-333", "cred-3", "109703");
        System.out.println("depAmount: " + depAmount);
        System.out.println();
        atmss.send(new Msg(id, mbox, Msg.Type.BAMS_Response, "depAmount: " + depAmount));
    } // testDeposit

    //------------------------------------------------------------
    // enquiry
    protected void enquiry(BAMSHandler bams, String aid) throws BAMSInvalidReplyException, IOException {
        System.out.println("Enquiry:");
        System.out.println("Card no: 4107-7014");
        System.out.println("aid: "+aid);
        String cardNo = "4107-7014";
        double amount = bams.enquiry("4107-7014", aid, "1234");
        System.out.println("amount: " + amount);
        atmss.send(new Msg(id, mbox, Msg.Type.BAMS_Response,
                "card no: " + cardNo + "@account id: "+ aid + "@amount: " + amount));
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
    // testChgPinReq
    protected void testChgPinReq(BAMSHandler bams) throws BAMSInvalidReplyException, IOException {
        System.out.println("ChgPinReq:");
        String result = bams.chgPinReq("12345678-4", "456123789", "987321654", "cred-8");
        System.out.println("result: " + result);
        System.out.println();
        atmss.send(new Msg(id, mbox, Msg.Type.BAMS_Response, "chgPinReq result: " + result));
    } // testChgPinReq

}
