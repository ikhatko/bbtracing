package model;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Trace {
  boolean newSpan() default true;

  String[] tags() default "";

  TracerType tracer() default TracerType.LOGGER;

   enum TracerType {
     JAEGER,ZIPKIN,LOGGER
   }
}
