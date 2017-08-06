package bytebuddy;

import agent.TraceInterceptor;
import model.Account;
import model.Trace;
import model.Transaction;
import net.bytebuddy.ByteBuddy;
import net.bytebuddy.implementation.MethodDelegation;
import net.bytebuddy.matcher.ElementMatchers;
import service.TransactionService;

public class ByteBuddyTrace {
  public static void main(String[] args) throws IllegalAccessException, InstantiationException {

    Account account = new Account("1", "User");
    Transaction transaction = new Transaction(account, "SAFE", 200.23);

    TransactionService transactionService = new ByteBuddy()
        .subclass(TransactionService.class)
        .method(ElementMatchers.isAnnotatedWith(Trace.class))
        .intercept(MethodDelegation.to(TraceInterceptor.class))
        .make()
        .load(ClassLoader.getSystemClassLoader())
        .getLoaded().newInstance();

    transactionService.doTransaction(transaction);
  }
}
