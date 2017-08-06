package agent;

import model.Trace;
import net.bytebuddy.agent.builder.AgentBuilder;
import net.bytebuddy.implementation.MethodDelegation;
import net.bytebuddy.matcher.ElementMatchers;

import java.lang.instrument.Instrumentation;

public class TraceAgent {
  public static void premain(String args,
                             Instrumentation inst) {
    new AgentBuilder.Default()
        .type(ElementMatchers.any())
        .transform((builder, typeDescription, classLoader, module) ->
            builder.method(ElementMatchers.isAnnotatedWith(Trace.class))
                .intercept(MethodDelegation.to(TraceInterceptor.class)))
        .installOn(inst);
  }
}
