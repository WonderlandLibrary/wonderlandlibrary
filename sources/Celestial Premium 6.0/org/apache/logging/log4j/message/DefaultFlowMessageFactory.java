/*
 * Decompiled with CFR 0.150.
 */
package org.apache.logging.log4j.message;

import java.io.Serializable;
import org.apache.logging.log4j.message.EntryMessage;
import org.apache.logging.log4j.message.ExitMessage;
import org.apache.logging.log4j.message.FlowMessage;
import org.apache.logging.log4j.message.FlowMessageFactory;
import org.apache.logging.log4j.message.Message;
import org.apache.logging.log4j.message.ReusableMessage;
import org.apache.logging.log4j.message.SimpleMessage;

public class DefaultFlowMessageFactory
implements FlowMessageFactory,
Serializable {
    private static final String EXIT_DEFAULT_PREFIX = "Exit";
    private static final String ENTRY_DEFAULT_PREFIX = "Enter";
    private static final long serialVersionUID = 8578655591131397576L;
    private final String entryText;
    private final String exitText;

    public DefaultFlowMessageFactory() {
        this(ENTRY_DEFAULT_PREFIX, EXIT_DEFAULT_PREFIX);
    }

    public DefaultFlowMessageFactory(String entryText, String exitText) {
        this.entryText = entryText;
        this.exitText = exitText;
    }

    public String getEntryText() {
        return this.entryText;
    }

    public String getExitText() {
        return this.exitText;
    }

    @Override
    public EntryMessage newEntryMessage(Message message) {
        return new SimpleEntryMessage(this.entryText, this.makeImmutable(message));
    }

    private Message makeImmutable(Message message) {
        if (!(message instanceof ReusableMessage)) {
            return message;
        }
        return new SimpleMessage(message.getFormattedMessage());
    }

    @Override
    public ExitMessage newExitMessage(EntryMessage message) {
        return new SimpleExitMessage(this.exitText, message);
    }

    @Override
    public ExitMessage newExitMessage(Object result, EntryMessage message) {
        return new SimpleExitMessage(this.exitText, result, message);
    }

    @Override
    public ExitMessage newExitMessage(Object result, Message message) {
        return new SimpleExitMessage(this.exitText, result, message);
    }

    private static final class SimpleExitMessage
    extends AbstractFlowMessage
    implements ExitMessage {
        private static final long serialVersionUID = 1L;
        private final Object result;
        private final boolean isVoid;

        SimpleExitMessage(String exitText, EntryMessage message) {
            super(exitText, message.getMessage());
            this.result = null;
            this.isVoid = true;
        }

        SimpleExitMessage(String exitText, Object result, EntryMessage message) {
            super(exitText, message.getMessage());
            this.result = result;
            this.isVoid = false;
        }

        SimpleExitMessage(String exitText, Object result, Message message) {
            super(exitText, message);
            this.result = result;
            this.isVoid = false;
        }

        @Override
        public String getFormattedMessage() {
            String formattedMessage = super.getFormattedMessage();
            if (this.isVoid) {
                return formattedMessage;
            }
            return formattedMessage + ": " + this.result;
        }
    }

    private static final class SimpleEntryMessage
    extends AbstractFlowMessage
    implements EntryMessage {
        private static final long serialVersionUID = 1L;

        SimpleEntryMessage(String entryText, Message message) {
            super(entryText, message);
        }
    }

    private static class AbstractFlowMessage
    implements FlowMessage {
        private static final long serialVersionUID = 1L;
        private final Message message;
        private final String text;

        AbstractFlowMessage(String text, Message message) {
            this.message = message;
            this.text = text;
        }

        @Override
        public String getFormattedMessage() {
            if (this.message != null) {
                return this.text + " " + this.message.getFormattedMessage();
            }
            return this.text;
        }

        @Override
        public String getFormat() {
            if (this.message != null) {
                return this.text + ": " + this.message.getFormat();
            }
            return this.text;
        }

        @Override
        public Object[] getParameters() {
            if (this.message != null) {
                return this.message.getParameters();
            }
            return null;
        }

        @Override
        public Throwable getThrowable() {
            if (this.message != null) {
                return this.message.getThrowable();
            }
            return null;
        }

        @Override
        public Message getMessage() {
            return this.message;
        }

        @Override
        public String getText() {
            return this.text;
        }
    }
}

