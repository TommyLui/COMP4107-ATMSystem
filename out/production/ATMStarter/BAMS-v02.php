<?php
$servername = "cslinux0.comp.hkbu.edu.hk";
$username = "comp4107_grp10";
$password = "689748";

// Create connection
$conn = new mysqli($servername, $username, $password);

// Check connection
if ($conn->connect_error) {
  die("Connection failed: " . $conn->connect_error);
}

$req = json_decode($_POST["BAMSReq"], false);

if (strcmp($req->msgType, "LoginReq") === 0) {

// $sql = "SELECT cardNo FROM Cards WHERE cardNo = " . "'" . $req->cardNo . "'" . " and pin = " . "'" . $req->pin . "';";
// $sql = "SELECT cardNo FROM Cards WHERE cardNo = " . "'" . $req->cardNo . "';";

// // Check connection
// if ($conn->connect_error) {
//   die("Connection failed: " . $conn->connect_error);
// } else {
// $reply->cred = "Credible_Credential!!!";
// }

$sql = "SELECT * FROM Cards";

$result = $conn->query($sql);

if ($result->num_rows > 0) {
  // output data of each row
  $reply->cred = "Credible_Credential!!!";
} else {
// $reply->cred = $sql
  $reply->cred = "Unsuccessful_Login!!!" . $req->cardNo . ' ' . $req->pin;
}

  $reply->msgType = "LoginReply";
  $reply->cardNo = $req->cardNo;
  $reply->pin = $req->pin;
//   $reply->cred = "Credible_Credential!!!";
} else if (strcmp($req->msgType, "LogoutReq") === 0) {
  $reply->msgType = "LogoutReply";
  $reply->cardNo = $req->cardNo;
  $reply->cred = $req->cred;
  $reply->result = "succ";
} else if (strcmp($req->msgType, "GetAccReq") === 0) {
  $reply->msgType = "GetAccReply";
  $reply->cardNo = $req->cardNo;
  $reply->cred = $req->cred;
  $reply->accounts = "111-222-333/111-222-334/111-222-335/111-222-336";
} else if (strcmp($req->msgType, "WithdrawReq") === 0) {
  $reply->msgType = "WithdrawReply";
  $reply->cardNo = $req->cardNo;
  $reply->accNo = $req->accNo;
  $reply->cred = $req->cred;
  $reply->amount = $req->amount;
  $reply->outAmount = $req->amount;
} else if (strcmp($req->msgType, "DepositReq") === 0) {
  $reply->msgType = "DepositReply";
  $reply->cardNo = $req->cardNo;
  $reply->accNo = $req->accNo;
  $reply->cred = $req->cred;
  $reply->amount = $req->amount;
  $reply->depAmount = $req->amount;
} else if (strcmp($req->msgType, "EnquiryReq") === 0) {
  $reply->msgType = "EnquiryReply";
  $reply->cardNo = $req->cardNo;
  $reply->accNo = $req->accNo;
  $reply->cred = $req->cred;
  $reply->amount = "109700";
} else if (strcmp($req->msgType, "TransferReq") === 0) {
  $reply->msgType = "TransferReply";
  $reply->cardNo = $req->cardNo;
  $reply->cred = $req->cred;
  $reply->fromAcc = $req->fromAcc;
  $reply->toAcc = $req->toAcc;
  $reply->amount = $req->amount;
  $reply->transAmount = $req->amount;
} else if (strcmp($req->msgType, "AccStmtReq") === 0) {
  $reply->msgType = "AccStmtReply";
  $reply->cardNo = $req->cardNo;
  $reply->accNo = $req->accNo;
  $reply->cred = $req->cred;
  $reply->result = "succ";
} else if (strcmp($req->msgType, "ChqBookReq") === 0) {
  $reply->msgType = "ChqBookReply";
  $reply->cardNo = $req->cardNo;
  $reply->accNo = $req->accNo;
  $reply->cred = $req->cred;
  $reply->result = "succ";
} else if (strcmp($req->msgType, "ChgPinReq") === 0) {
  $reply->msgType = "ChgPinReply";
  $reply->cardNo = $req->cardNo;
  $reply->oldPin = $req->oldPin;
  $reply->newPin = $req->newPin;
  $reply->cred = $req->cred;
  $reply->result = "succ";
} else if (strcmp($req->msgType, "ChgLangReq") === 0) {
  $reply->msgType = "ChgLangReply";
  $reply->cardNo = $req->cardNo;
  $reply->oldLang = $req->oldLang;
  $reply->newLang = $req->newLang;
  $reply->cred = $req->cred;
  $reply->result = "succ";
}

echo json_encode($reply);
?>
