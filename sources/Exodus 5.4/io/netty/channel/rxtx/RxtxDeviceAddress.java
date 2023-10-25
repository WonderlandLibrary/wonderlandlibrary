/*
 * Decompiled with CFR 0.152.
 */
package io.netty.channel.rxtx;

import java.net.SocketAddress;

public class RxtxDeviceAddress
extends SocketAddress {
    private static final long serialVersionUID = -2907820090993709523L;
    private final String value;

    public String value() {
        return this.value;
    }

    public RxtxDeviceAddress(String string) {
        this.value = string;
    }
}

