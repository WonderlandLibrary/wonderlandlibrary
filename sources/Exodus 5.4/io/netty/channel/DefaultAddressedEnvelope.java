/*
 * Decompiled with CFR 0.152.
 */
package io.netty.channel;

import io.netty.channel.AddressedEnvelope;
import io.netty.util.ReferenceCountUtil;
import io.netty.util.ReferenceCounted;
import io.netty.util.internal.StringUtil;
import java.net.SocketAddress;

public class DefaultAddressedEnvelope<M, A extends SocketAddress>
implements AddressedEnvelope<M, A> {
    private final M message;
    private final A recipient;
    private final A sender;

    public DefaultAddressedEnvelope(M m, A a) {
        this(m, a, null);
    }

    @Override
    public boolean release(int n) {
        return ReferenceCountUtil.release(this.message, n);
    }

    public DefaultAddressedEnvelope(M m, A a, A a2) {
        if (m == null) {
            throw new NullPointerException("message");
        }
        this.message = m;
        this.sender = a2;
        this.recipient = a;
    }

    @Override
    public A sender() {
        return this.sender;
    }

    public String toString() {
        if (this.sender != null) {
            return StringUtil.simpleClassName(this) + '(' + this.sender + " => " + this.recipient + ", " + this.message + ')';
        }
        return StringUtil.simpleClassName(this) + "(=> " + this.recipient + ", " + this.message + ')';
    }

    @Override
    public M content() {
        return this.message;
    }

    @Override
    public AddressedEnvelope<M, A> retain(int n) {
        ReferenceCountUtil.retain(this.message, n);
        return this;
    }

    @Override
    public boolean release() {
        return ReferenceCountUtil.release(this.message);
    }

    @Override
    public A recipient() {
        return this.recipient;
    }

    @Override
    public int refCnt() {
        if (this.message instanceof ReferenceCounted) {
            return ((ReferenceCounted)this.message).refCnt();
        }
        return 1;
    }

    @Override
    public AddressedEnvelope<M, A> retain() {
        ReferenceCountUtil.retain(this.message);
        return this;
    }
}

