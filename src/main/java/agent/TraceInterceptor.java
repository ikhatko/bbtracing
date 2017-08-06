package agent;

import model.Trace;
import net.bytebuddy.implementation.bind.annotation.AllArguments;
import net.bytebuddy.implementation.bind.annotation.Origin;
import net.bytebuddy.implementation.bind.annotation.RuntimeType;
import net.bytebuddy.implementation.bind.annotation.SuperCall;
import org.apache.log4j.Logger;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.concurrent.Callable;

public class TraceInterceptor {

  private static Logger logger = Logger.getLogger(TraceInterceptor.class);

  @RuntimeType
  public static void itercept(@AllArguments Object[] allArguments,
                              @Origin Method method, @SuperCall Callable call) {
    long start = System.currentTimeMillis();
    logger.info("Called method " + method.getName() + " with args " + Arrays.toString(allArguments));
    Trace annotation = method.getAnnotation(Trace.class);
    Trace.TracerType tracer = annotation.tracer();
    boolean isSpan = annotation.newSpan();
    String[] tags = annotation.tags();
    String tagValues = parseTags(tags, allArguments);

    if (tracer.equals(Trace.TracerType.LOGGER)) {
      useLogger(tagValues, allArguments);
    } else if (tracer.equals(Trace.TracerType.JAEGER)) {
      //use jaeger
    }

    try {
      call.call();
    } catch (Exception e) {
      logger.error("Exception in original method call: " + e.getMessage());
    } finally {
      logger.info(method.getName() + " with args: " + Arrays.toString(allArguments)
          + " took " + (System.currentTimeMillis() - start) + "ms");
    }
  }

  /*{"0:account:accountId",
      "0:account:accountType",
      "0:transactionType",
      "0:amount"}*/

  /**
   * Parse values from tags
   * @param tags the string separated by ":"
   *             first digit is the array number in the arguments array
   *             next one is the path to value
   * @param allArguments called method arguments
   * @return string with tag=value
   */
  private static String parseTags(String[] tags, Object[] allArguments) {
    StringBuilder sb = new StringBuilder();
    for (String tag : tags) {
      String[] splitTags = tag.split(":");
      //argument array number
      Integer argNum = Integer.valueOf(splitTags[0]);
      try {
        Class argClass = allArguments[argNum].getClass();
        Object invoke = allArguments[argNum];
        //for all splitTags invoke getter and get value
        for (int i = 1; i < splitTags.length; i++) {
            invoke = new PropertyDescriptor(splitTags[i], argClass).getReadMethod().invoke(invoke);
            argClass = invoke.getClass();
        }
        // append tag=value
        sb.append(splitTags[splitTags.length - 1]).append("=").append(invoke).append(" ");
      } catch (Exception e) {
        logger.warn("Exception while parsing tags: " + e.getMessage());
      }
    }
    return sb.toString();

  }

  private static void useLogger(String tags, Object[] allArguments) {
    logger.info("Tags values: " + tags);
  }

  private static void createNewSpan() {

  }


}
