/*
 * Decompiled with CFR 0.152.
 */
package io.netty.channel.rxtx;

import io.netty.channel.ChannelOption;
import io.netty.channel.rxtx.RxtxChannelConfig;

public final class RxtxChannelOption<T>
extends ChannelOption<T> {
    public static final RxtxChannelOption<Integer> WAIT_TIME;
    public static final RxtxChannelOption<Integer> BAUD_RATE;
    public static final RxtxChannelOption<RxtxChannelConfig.Paritybit> PARITY_BIT;
    public static final RxtxChannelOption<RxtxChannelConfig.Stopbits> STOP_BITS;
    public static final RxtxChannelOption<RxtxChannelConfig.Databits> DATA_BITS;
    public static final RxtxChannelOption<Integer> READ_TIMEOUT;
    public static final RxtxChannelOption<Boolean> DTR;
    public static final RxtxChannelOption<Boolean> RTS;

    private RxtxChannelOption(String string) {
        super(string);
    }

    static {
        BAUD_RATE = new RxtxChannelOption("BAUD_RATE");
        DTR = new RxtxChannelOption("DTR");
        RTS = new RxtxChannelOption("RTS");
        STOP_BITS = new RxtxChannelOption("STOP_BITS");
        DATA_BITS = new RxtxChannelOption("DATA_BITS");
        PARITY_BIT = new RxtxChannelOption("PARITY_BIT");
        WAIT_TIME = new RxtxChannelOption("WAIT_TIME");
        READ_TIMEOUT = new RxtxChannelOption("READ_TIMEOUT");
    }
}

