/*
 * Decompiled with CFR 0.150.
 */
package com.viaversion.viaversion.libs.kyori.adventure.audience;

public final class MessageType
extends Enum<MessageType> {
    public static final /* enum */ MessageType CHAT = new MessageType();
    public static final /* enum */ MessageType SYSTEM = new MessageType();
    private static final /* synthetic */ MessageType[] $VALUES;

    public static MessageType[] values() {
        return (MessageType[])$VALUES.clone();
    }

    public static MessageType valueOf(String name) {
        return Enum.valueOf(MessageType.class, name);
    }

    private static /* synthetic */ MessageType[] $values() {
        return new MessageType[]{CHAT, SYSTEM};
    }

    static {
        $VALUES = MessageType.$values();
    }
}

