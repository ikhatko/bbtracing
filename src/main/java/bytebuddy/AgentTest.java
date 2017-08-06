package bytebuddy;

import model.Account;
import model.Transaction;
import model.User;
import service.TransactionService;

public class AgentTest {
  public static void main(String[] args) {
    Account account = new Account("1", "User");
    Account account1 = new Account("22", "Worker");
    Transaction transaction = new Transaction(account, "SAFE", 200.23);
    Transaction transaction1 = new Transaction(account1, "UNSAFE", 99.999);
    TransactionService transactionService = new TransactionService();
    transactionService.doTransaction(transaction);
    transactionService.doSomeStuff(account1, transaction1);
    transactionService.someOtherAction(new User(44,"John D",transaction1));
  }
}
