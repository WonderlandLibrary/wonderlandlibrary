/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.fasterxml.jackson.databind.JsonDeserializer
 *  com.fasterxml.jackson.databind.JsonSerializer
 *  com.fasterxml.jackson.databind.Module$SetupContext
 *  com.fasterxml.jackson.databind.module.SimpleModule
 */
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
import org.apache.logging.log4j.core.jackson.ExtendedStackTraceElementMixIn;
import org.apache.logging.log4j.core.jackson.InstantMixIn;
import org.apache.logging.log4j.core.jackson.LevelMixIn;
import org.apache.logging.log4j.core.jackson.Log4jStackTraceElementDeserializer;
import org.apache.logging.log4j.core.jackson.LogEventJsonMixIn;
import org.apache.logging.log4j.core.jackson.LogEventWithContextListMixIn;
import org.apache.logging.log4j.core.jackson.MarkerMixIn;
import org.apache.logging.log4j.core.jackson.MessageSerializer;
import org.apache.logging.log4j.core.jackson.MutableThreadContextStackDeserializer;
import org.apache.logging.log4j.core.jackson.ObjectMessageSerializer;
import org.apache.logging.log4j.core.jackson.StackTraceElementMixIn;
import org.apache.logging.log4j.core.jackson.ThrowableProxyMixIn;
import org.apache.logging.log4j.core.jackson.ThrowableProxyWithStacktraceAsStringMixIn;
import org.apache.logging.log4j.core.jackson.ThrowableProxyWithoutStacktraceMixIn;
import org.apache.logging.log4j.core.time.Instant;
import org.apache.logging.log4j.message.Message;
import org.apache.logging.log4j.message.ObjectMessage;

class Initializers {
    Initializers() {
    }

    static class SimpleModuleInitializer {
        SimpleModuleInitializer() {
        }

        void initialize(SimpleModule simpleModule, boolean objectMessageAsJsonObject) {
            simpleModule.addDeserializer(StackTraceElement.class, (JsonDeserializer)new Log4jStackTraceElementDeserializer());
            simpleModule.addDeserializer(ThreadContext.ContextStack.class, (JsonDeserializer)new MutableThreadContextStackDeserializer());
            if (objectMessageAsJsonObject) {
                simpleModule.addSerializer(ObjectMessage.class, (JsonSerializer)new ObjectMessageSerializer());
            }
            simpleModule.addSerializer(Message.class, (JsonSerializer)new MessageSerializer());
        }
    }

    static class SetupContextJsonInitializer {
        SetupContextJsonInitializer() {
        }

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

    static class SetupContextInitializer {
        SetupContextInitializer() {
        }

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
}

