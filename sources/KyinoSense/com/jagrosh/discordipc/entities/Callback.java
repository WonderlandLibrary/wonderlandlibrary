/*
 * Decompiled with CFR 0.152.
 */
package com.jagrosh.discordipc.entities;

import com.jagrosh.discordipc.entities.Packet;
import java.util.function.Consumer;

public class Callback {
    private final Consumer<Packet> success;
    private final Consumer<String> failure;

    public Callback() {
        this((Consumer<Packet>)null, null);
    }

    public Callback(Consumer<Packet> success) {
        this(success, null);
    }

    public Callback(Consumer<Packet> success, Consumer<String> failure) {
        this.success = success;
        this.failure = failure;
    }

    @Deprecated
    public Callback(Runnable success, Consumer<String> failure) {
        this((Packet p) -> success.run(), failure);
    }

    @Deprecated
    public Callback(Runnable success) {
        this((Packet p) -> success.run(), null);
    }

    public boolean isEmpty() {
        return this.success == null && this.failure == null;
    }

    public void succeed(Packet packet) {
        if (this.success != null) {
            this.success.accept(packet);
        }
    }

    public void fail(String message) {
        if (this.failure != null) {
            this.failure.accept(message);
        }
    }
}

