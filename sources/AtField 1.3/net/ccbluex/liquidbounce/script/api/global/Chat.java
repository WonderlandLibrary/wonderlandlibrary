/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kotlin.jvm.JvmStatic
 */
package net.ccbluex.liquidbounce.script.api.global;

import kotlin.jvm.JvmStatic;
import net.ccbluex.liquidbounce.utils.ClientUtils;

public final class Chat {
    public static final Chat INSTANCE;

    private Chat() {
    }

    static {
        Chat chat;
        INSTANCE = chat = new Chat();
    }

    @JvmStatic
    public static final void print(String string) {
        ClientUtils.displayChatMessage(string);
    }
}

