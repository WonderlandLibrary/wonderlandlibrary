/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  javax.persistence.Basic
 *  javax.persistence.Convert
 *  javax.persistence.EnumType
 *  javax.persistence.Enumerated
 *  javax.persistence.MappedSuperclass
 */
package org.apache.logging.log4j.core.appender.db.jpa;

import java.util.Map;
import javax.persistence.Basic;
import javax.persistence.Convert;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.MappedSuperclass;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.ThreadContext;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.appender.db.jpa.AbstractLogEventWrapperEntity;
import org.apache.logging.log4j.core.appender.db.jpa.converter.ContextMapAttributeConverter;
import org.apache.logging.log4j.core.appender.db.jpa.converter.ContextStackAttributeConverter;
import org.apache.logging.log4j.core.appender.db.jpa.converter.MarkerAttributeConverter;
import org.apache.logging.log4j.core.appender.db.jpa.converter.MessageAttributeConverter;
import org.apache.logging.log4j.core.appender.db.jpa.converter.StackTraceElementAttributeConverter;
import org.apache.logging.log4j.core.appender.db.jpa.converter.ThrowableAttributeConverter;
import org.apache.logging.log4j.message.Message;

@MappedSuperclass
public abstract class BasicLogEventEntity
extends AbstractLogEventWrapperEntity {
    private static final long serialVersionUID = 1L;

    public BasicLogEventEntity() {
    }

    public BasicLogEventEntity(LogEvent wrappedEvent) {
        super(wrappedEvent);
    }

    @Override
    @Basic
    @Enumerated(value=EnumType.STRING)
    public Level getLevel() {
        return this.getWrappedEvent().getLevel();
    }

    @Override
    @Basic
    public String getLoggerName() {
        return this.getWrappedEvent().getLoggerName();
    }

    @Override
    @Convert(converter=StackTraceElementAttributeConverter.class)
    public StackTraceElement getSource() {
        return this.getWrappedEvent().getSource();
    }

    @Override
    @Convert(converter=MessageAttributeConverter.class)
    public Message getMessage() {
        return this.getWrappedEvent().getMessage();
    }

    @Override
    @Convert(converter=MarkerAttributeConverter.class)
    public Marker getMarker() {
        return this.getWrappedEvent().getMarker();
    }

    @Override
    @Basic
    public String getThreadName() {
        return this.getWrappedEvent().getThreadName();
    }

    @Override
    @Basic
    public long getMillis() {
        return this.getWrappedEvent().getMillis();
    }

    @Override
    @Convert(converter=ThrowableAttributeConverter.class)
    public Throwable getThrown() {
        return this.getWrappedEvent().getThrown();
    }

    @Override
    @Convert(converter=ContextMapAttributeConverter.class)
    public Map<String, String> getContextMap() {
        return this.getWrappedEvent().getContextMap();
    }

    @Override
    @Convert(converter=ContextStackAttributeConverter.class)
    public ThreadContext.ContextStack getContextStack() {
        return this.getWrappedEvent().getContextStack();
    }

    @Override
    @Basic
    public String getFQCN() {
        return this.getWrappedEvent().getFQCN();
    }
}

