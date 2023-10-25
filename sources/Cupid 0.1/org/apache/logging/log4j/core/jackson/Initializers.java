package org.apache.logging.log4j.core.jackson;

import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.Module;
import com.fasterxml.jackson.databind.module.SimpleModule;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.ThreadContext;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.impl.ExtendedStackTraceElement;
import org.apache.logging.log4j.core.impl.ThrowableProxy;
import org.apache.logging.log4j.core.time.Instant;
import org.apache.logging.log4j.message.Message;
import org.apache.logging.log4j.message.ObjectMessage;

class Initializers {
  static class SetupContextInitializer {
    void setupModule(Module.SetupContext context, boolean includeStacktrace, boolean stacktraceAsString) {
      context.setMixInAnnotations(StackTraceElement.class, StackTraceElementMixIn.class);
      context.setMixInAnnotations(Marker.class, MarkerMixIn.class);
      context.setMixInAnnotations(Level.class, LevelMixIn.class);
      context.setMixInAnnotations(Instant.class, InstantMixIn.class);
      context.setMixInAnnotations(LogEvent.class, LogEventWithContextListMixIn.class);
      context.setMixInAnnotations(ExtendedStackTraceElement.class, ExtendedStackTraceElementMixIn.class);
      context.setMixInAnnotations(ThrowableProxy.class, includeStacktrace ? (stacktraceAsString ? ThrowableProxyWithStacktraceAsStringMixIn.class : ThrowableProxyMixIn.class) : ThrowableProxyWithoutStacktraceMixIn.class);
    }
  }
  
  static class SetupContextJsonInitializer {
    void setupModule(Module.SetupContext context, boolean includeStacktrace, boolean stacktraceAsString) {
      context.setMixInAnnotations(StackTraceElement.class, StackTraceElementMixIn.class);
      context.setMixInAnnotations(Marker.class, MarkerMixIn.class);
      context.setMixInAnnotations(Level.class, LevelMixIn.class);
      context.setMixInAnnotations(Instant.class, InstantMixIn.class);
      context.setMixInAnnotations(LogEvent.class, LogEventJsonMixIn.class);
      context.setMixInAnnotations(ExtendedStackTraceElement.class, ExtendedStackTraceElementMixIn.class);
      context.setMixInAnnotations(ThrowableProxy.class, includeStacktrace ? (stacktraceAsString ? ThrowableProxyWithStacktraceAsStringMixIn.class : ThrowableProxyMixIn.class) : ThrowableProxyWithoutStacktraceMixIn.class);
    }
  }
  
  static class SimpleModuleInitializer {
    void initialize(SimpleModule simpleModule, boolean objectMessageAsJsonObject) {
      simpleModule.addDeserializer(StackTraceElement.class, (JsonDeserializer)new Log4jStackTraceElementDeserializer());
      simpleModule.addDeserializer(ThreadContext.ContextStack.class, (JsonDeserializer)new MutableThreadContextStackDeserializer());
      if (objectMessageAsJsonObject)
        simpleModule.addSerializer(ObjectMessage.class, (JsonSerializer)new ObjectMessageSerializer()); 
      simpleModule.addSerializer(Message.class, (JsonSerializer)new MessageSerializer());
    }
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\logging\log4j\core\jackson\Initializers.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */