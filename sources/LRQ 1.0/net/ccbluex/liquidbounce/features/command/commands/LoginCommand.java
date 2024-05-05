/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kotlin.jvm.internal.Intrinsics
 *  kotlin.text.StringsKt
 */
package net.ccbluex.liquidbounce.features.command.commands;

import kotlin.jvm.internal.Intrinsics;
import kotlin.text.StringsKt;
import net.ccbluex.liquidbounce.api.minecraft.client.multiplayer.IWorldClient;
import net.ccbluex.liquidbounce.features.command.Command;
import net.ccbluex.liquidbounce.ui.client.altmanager.GuiAltManager;
import net.ccbluex.liquidbounce.utils.MinecraftInstance;
import net.ccbluex.liquidbounce.utils.ServerUtils;
import net.ccbluex.liquidbounce.utils.login.MinecraftAccount;

public final class LoginCommand
extends Command {
    @Override
    public void execute(String[] args) {
        if (args.length <= 1) {
            this.chatSyntax("login <username/email> [password]");
            return;
        }
        String result = args.length > 2 ? GuiAltManager.login(new MinecraftAccount(args[1], args[2])) : GuiAltManager.login(new MinecraftAccount(args[1]));
        this.chat(result);
        if (StringsKt.startsWith$default((String)result, (String)"\u00a7cYour name is now", (boolean)false, (int)2, null)) {
            if (MinecraftInstance.mc.isIntegratedServerRunning()) {
                return;
            }
            IWorldClient iWorldClient = MinecraftInstance.mc.getTheWorld();
            if (iWorldClient == null) {
                Intrinsics.throwNpe();
            }
            iWorldClient.sendQuittingDisconnectingPacket();
            ServerUtils.connectToLastServer();
        }
    }

    public LoginCommand() {
        super("login", new String[0]);
    }
}

