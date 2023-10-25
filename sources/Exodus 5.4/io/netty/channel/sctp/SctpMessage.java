/*
 * Decompiled with CFR 0.152.
 */
package io.netty.channel.sctp;

import com.sun.nio.sctp.MessageInfo;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import io.netty.buffer.DefaultByteBufHolder;

public final class SctpMessage
extends DefaultByteBufHolder {
    private final int streamIdentifier;
    private final int protocolIdentifier;
    private final MessageInfo msgInfo;

    public MessageInfo messageInfo() {
        return this.msgInfo;
    }

    @Override
    public String toString() {
        if (this.refCnt() == 0) {
            return "SctpFrame{streamIdentifier=" + this.streamIdentifier + ", protocolIdentifier=" + this.protocolIdentifier + ", data=(FREED)}";
        }
        return "SctpFrame{streamIdentifier=" + this.streamIdentifier + ", protocolIdentifier=" + this.protocolIdentifier + ", data=" + ByteBufUtil.hexDump(this.content()) + '}';
    }

    @Override
    public SctpMessage duplicate() {
        if (this.msgInfo == null) {
            return new SctpMessage(this.protocolIdentifier, this.streamIdentifier, this.content().duplicate());
        }
        return new SctpMessage(this.msgInfo, this.content().copy());
    }

    public int streamIdentifier() {
        return this.streamIdentifier;
    }

    @Override
    public SctpMessage retain(int n) {
        super.retain(n);
        return this;
    }

    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (object == null || this.getClass() != object.getClass()) {
            return false;
        }
        SctpMessage sctpMessage = (SctpMessage)object;
        if (this.protocolIdentifier != sctpMessage.protocolIdentifier) {
            return false;
        }
        if (this.streamIdentifier != sctpMessage.streamIdentifier) {
            return false;
        }
        return this.content().equals(sctpMessage.content());
    }

    @Override
    public SctpMessage copy() {
        if (this.msgInfo == null) {
            return new SctpMessage(this.protocolIdentifier, this.streamIdentifier, this.content().copy());
        }
        return new SctpMessage(this.msgInfo, this.content().copy());
    }

    public SctpMessage(MessageInfo messageInfo, ByteBuf byteBuf) {
        super(byteBuf);
        if (messageInfo == null) {
            throw new NullPointerException("msgInfo");
        }
        this.msgInfo = messageInfo;
        this.streamIdentifier = messageInfo.streamNumber();
        this.protocolIdentifier = messageInfo.payloadProtocolID();
    }

    public boolean isComplete() {
        if (this.msgInfo != null) {
            return this.msgInfo.isComplete();
        }
        return true;
    }

    public int protocolIdentifier() {
        return this.protocolIdentifier;
    }

    public int hashCode() {
        int n = this.streamIdentifier;
        n = 31 * n + this.protocolIdentifier;
        n = 31 * n + this.content().hashCode();
        return n;
    }

    @Override
    public SctpMessage retain() {
        super.retain();
        return this;
    }

    public SctpMessage(int n, int n2, ByteBuf byteBuf) {
        super(byteBuf);
        this.protocolIdentifier = n;
        this.streamIdentifier = n2;
        this.msgInfo = null;
    }
}

