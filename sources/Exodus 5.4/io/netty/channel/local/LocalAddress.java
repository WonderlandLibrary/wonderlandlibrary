/*
 * Decompiled with CFR 0.152.
 */
package io.netty.channel.local;

import io.netty.channel.Channel;
import java.net.SocketAddress;

public final class LocalAddress
extends SocketAddress
implements Comparable<LocalAddress> {
    private static final long serialVersionUID = 4644331421130916435L;
    private final String strVal;
    private final String id;
    public static final LocalAddress ANY = new LocalAddress("ANY");

    public String id() {
        return this.id;
    }

    public boolean equals(Object object) {
        if (!(object instanceof LocalAddress)) {
            return false;
        }
        return this.id.equals(((LocalAddress)object).id);
    }

    public String toString() {
        return this.strVal;
    }

    LocalAddress(Channel channel) {
        StringBuilder stringBuilder = new StringBuilder(16);
        stringBuilder.append("local:E");
        stringBuilder.append(Long.toHexString((long)channel.hashCode() & 0xFFFFFFFFL | 0x100000000L));
        stringBuilder.setCharAt(7, ':');
        this.id = stringBuilder.substring(6);
        this.strVal = stringBuilder.toString();
    }

    public LocalAddress(String string) {
        if (string == null) {
            throw new NullPointerException("id");
        }
        if ((string = string.trim().toLowerCase()).isEmpty()) {
            throw new IllegalArgumentException("empty id");
        }
        this.id = string;
        this.strVal = "local:" + string;
    }

    @Override
    public int compareTo(LocalAddress localAddress) {
        return this.id.compareTo(localAddress.id);
    }

    public int hashCode() {
        return this.id.hashCode();
    }
}

