/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kotlin.jvm.internal.Intrinsics
 */
package net.ccbluex.liquidbounce.features.command.commands;

import kotlin.jvm.internal.Intrinsics;
import net.ccbluex.liquidbounce.api.minecraft.client.entity.IEntityPlayerSP;
import net.ccbluex.liquidbounce.features.command.Command;
import net.ccbluex.liquidbounce.utils.MinecraftInstance;
import net.ccbluex.liquidbounce.utils.misc.StringUtils;

public final class SayCommand
extends Command {
    @Override
    public void execute(String[] args) {
        if (args.length > 1) {
            IEntityPlayerSP iEntityPlayerSP = MinecraftInstance.mc.getThePlayer();
            if (iEntityPlayerSP == null) {
                Intrinsics.throwNpe();
            }
            iEntityPlayerSP.sendChatMessage(StringUtils.toCompleteString(args, 1));
            this.chat("Message was sent to the chat.");
            return;
        }
        this.chatSyntax("say <message...>");
    }

    public SayCommand() {
        super("say", new String[0]);
    }
}

