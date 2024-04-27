/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kotlin.jvm.internal.Intrinsics
 */
package net.ccbluex.liquidbounce.features.command.commands;

import java.awt.Toolkit;
import java.awt.datatransfer.StringSelection;
import kotlin.jvm.internal.Intrinsics;
import net.ccbluex.liquidbounce.api.minecraft.client.entity.IEntityPlayerSP;
import net.ccbluex.liquidbounce.features.command.Command;
import net.ccbluex.liquidbounce.utils.MinecraftInstance;

public final class UsernameCommand
extends Command {
    @Override
    public void execute(String[] args) {
        IEntityPlayerSP iEntityPlayerSP = MinecraftInstance.mc.getThePlayer();
        if (iEntityPlayerSP == null) {
            Intrinsics.throwNpe();
        }
        String username = iEntityPlayerSP.getName();
        this.chat("Username: " + username);
        StringSelection stringSelection = new StringSelection(username);
        Toolkit.getDefaultToolkit().getSystemClipboard().setContents(stringSelection, stringSelection);
    }

    public UsernameCommand() {
        super("username", "ign");
    }
}

