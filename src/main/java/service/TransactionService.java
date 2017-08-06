package service;

import model.Account;
import model.Trace;
import model.Transaction;
import model.User;

public class TransactionService {

  @Trace(tags = {"0:account:accountId",
      "0:account:accountType",
      "0:transactionType",
      "0:amount"})
  public void doTransaction(Transaction transaction) {
    simulateLongCall();
    System.out.println("doTransaction done");
  }

  @Trace(tags = {"0:accountId",
      "0:accountType",
      "1:transactionType",
      "1:amount"})
  public void doSomeStuff(Account account, Transaction transaction) {
    System.out.println("doingSomeStuff");
  }

  @Trace(tags = {"0:age",
      "0:name",
      "0:transaction:transactionType",
      "0:transaction:amount",
      "0:transaction:account:accountId",
      "0:transaction:account:accountType"})
  public void someOtherAction(User user) {
    System.out.println("someOtherAction");
  }

  private void simulateLongCall() {
    try {
      Thread.sleep(1000);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }


}
