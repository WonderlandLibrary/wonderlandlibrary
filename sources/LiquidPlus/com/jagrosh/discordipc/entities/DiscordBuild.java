/*
 * Decompiled with CFR 0.152.
 */
package com.jagrosh.discordipc.entities;

public enum DiscordBuild {
    CANARY("//canary.discordapp.com/api"),
    PTB("//ptb.discordapp.com/api"),
    STABLE("//discordapp.com/api"),
    ANY;

    private final String endpoint;

    private DiscordBuild(String endpoint) {
        this.endpoint = endpoint;
    }

    private DiscordBuild() {
        this(null);
    }

    public static DiscordBuild from(String endpoint) {
        for (DiscordBuild value : DiscordBuild.values()) {
            if (value.endpoint == null || !value.endpoint.equals(endpoint)) continue;
            return value;
        }
        return ANY;
    }
}

