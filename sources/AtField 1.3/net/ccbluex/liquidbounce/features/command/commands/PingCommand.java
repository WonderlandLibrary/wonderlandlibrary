/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kotlin.jvm.internal.Intrinsics
 */
package net.ccbluex.liquidbounce.features.command.commands;

import kotlin.jvm.internal.Intrinsics;
import net.ccbluex.liquidbounce.api.minecraft.client.entity.IEntityPlayerSP;
import net.ccbluex.liquidbounce.api.minecraft.client.network.IINetHandlerPlayClient;
import net.ccbluex.liquidbounce.api.minecraft.client.network.INetworkPlayerInfo;
import net.ccbluex.liquidbounce.features.command.Command;
import net.ccbluex.liquidbounce.utils.MinecraftInstance;

public final class PingCommand
extends Command {
    @Override
    public void execute(String[] stringArray) {
        StringBuilder stringBuilder = new StringBuilder().append("\u00a73Your ping is \u00a7a");
        IINetHandlerPlayClient iINetHandlerPlayClient = MinecraftInstance.mc.getNetHandler();
        IEntityPlayerSP iEntityPlayerSP = MinecraftInstance.mc.getThePlayer();
        if (iEntityPlayerSP == null) {
            Intrinsics.throwNpe();
        }
        INetworkPlayerInfo iNetworkPlayerInfo = iINetHandlerPlayClient.getPlayerInfo(iEntityPlayerSP.getUniqueID());
        if (iNetworkPlayerInfo == null) {
            Intrinsics.throwNpe();
        }
        this.chat(stringBuilder.append(iNetworkPlayerInfo.getResponseTime()).append("ms\u00a73.").toString());
    }

    public PingCommand() {
        super("ping", new String[0]);
    }
}

