/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.entity.EntityPlayerSP
 *  net.minecraft.client.network.NetHandlerPlayClient
 *  net.minecraft.client.network.NetworkPlayerInfo
 *  org.jetbrains.annotations.NotNull
 */
package net.ccbluex.liquidbounce.features.command.commands;

import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import net.ccbluex.liquidbounce.features.command.Command;
import net.ccbluex.liquidbounce.utils.MinecraftInstance;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.client.network.NetworkPlayerInfo;
import org.jetbrains.annotations.NotNull;

@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, xi=2, d1={"\u0000\u001e\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\u0011\n\u0002\u0010\u000e\n\u0002\b\u0002\u0018\u00002\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0002J\u001b\u0010\u0003\u001a\u00020\u00042\f\u0010\u0005\u001a\b\u0012\u0004\u0012\u00020\u00070\u0006H\u0016\u00a2\u0006\u0002\u0010\b\u00a8\u0006\t"}, d2={"Lnet/ccbluex/liquidbounce/features/command/commands/PingCommand;", "Lnet/ccbluex/liquidbounce/features/command/Command;", "()V", "execute", "", "args", "", "", "([Ljava/lang/String;)V", "KyinoClient"})
public final class PingCommand
extends Command {
    @Override
    public void execute(@NotNull String[] args2) {
        Intrinsics.checkParameterIsNotNull(args2, "args");
        StringBuilder stringBuilder = new StringBuilder().append("\u00a73Your ping is \u00a7a");
        Minecraft minecraft = PingCommand.access$getMc$p$s1046033730();
        Intrinsics.checkExpressionValueIsNotNull(minecraft, "mc");
        NetHandlerPlayClient netHandlerPlayClient = minecraft.func_147114_u();
        EntityPlayerSP entityPlayerSP = PingCommand.access$getMc$p$s1046033730().field_71439_g;
        if (entityPlayerSP == null) {
            Intrinsics.throwNpe();
        }
        NetworkPlayerInfo networkPlayerInfo = netHandlerPlayClient.func_175102_a(entityPlayerSP.func_110124_au());
        if (networkPlayerInfo == null) {
            Intrinsics.throwNpe();
        }
        this.chat(stringBuilder.append(networkPlayerInfo.func_178853_c()).append("ms\u00a73.").toString());
    }

    public PingCommand() {
        super("ping", new String[0]);
    }

    public static final /* synthetic */ Minecraft access$getMc$p$s1046033730() {
        return MinecraftInstance.mc;
    }
}

