/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.entity.EntityPlayerSP
 *  net.minecraft.client.multiplayer.PlayerControllerMP
 *  net.minecraft.client.network.NetHandlerPlayClient
 *  net.minecraft.item.ItemStack
 *  net.minecraft.network.Packet
 *  net.minecraft.network.play.client.C10PacketCreativeInventoryAction
 *  org.jetbrains.annotations.NotNull
 */
package net.ccbluex.liquidbounce.features.command.commands;

import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import net.ccbluex.liquidbounce.features.command.Command;
import net.ccbluex.liquidbounce.utils.MinecraftInstance;
import net.ccbluex.liquidbounce.utils.misc.StringUtils;
import net.ccbluex.liquidbounce.utils.render.ColorUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.multiplayer.PlayerControllerMP;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.item.ItemStack;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C10PacketCreativeInventoryAction;
import org.jetbrains.annotations.NotNull;

@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, xi=2, d1={"\u0000\u001e\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\u0011\n\u0002\u0010\u000e\n\u0002\b\u0002\u0018\u00002\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0002J\u001b\u0010\u0003\u001a\u00020\u00042\f\u0010\u0005\u001a\b\u0012\u0004\u0012\u00020\u00070\u0006H\u0016\u00a2\u0006\u0002\u0010\b\u00a8\u0006\t"}, d2={"Lnet/ccbluex/liquidbounce/features/command/commands/RenameCommand;", "Lnet/ccbluex/liquidbounce/features/command/Command;", "()V", "execute", "", "args", "", "", "([Ljava/lang/String;)V", "KyinoClient"})
public final class RenameCommand
extends Command {
    @Override
    public void execute(@NotNull String[] args2) {
        Intrinsics.checkParameterIsNotNull(args2, "args");
        if (args2.length > 1) {
            ItemStack item;
            PlayerControllerMP playerControllerMP = RenameCommand.access$getMc$p$s1046033730().field_71442_b;
            Intrinsics.checkExpressionValueIsNotNull(playerControllerMP, "mc.playerController");
            if (playerControllerMP.func_78762_g()) {
                this.chat("\u00a7c\u00a7lError: \u00a73You need to be in creative mode.");
                return;
            }
            EntityPlayerSP entityPlayerSP = RenameCommand.access$getMc$p$s1046033730().field_71439_g;
            if (entityPlayerSP == null) {
                Intrinsics.throwNpe();
            }
            ItemStack itemStack = item = entityPlayerSP.func_70694_bm();
            if ((itemStack != null ? itemStack.func_77973_b() : null) == null) {
                this.chat("\u00a7c\u00a7lError: \u00a73You need to hold a item.");
                return;
            }
            String string = StringUtils.toCompleteString(args2, 1);
            Intrinsics.checkExpressionValueIsNotNull(string, "StringUtils.toCompleteString(args, 1)");
            item.func_151001_c(ColorUtils.translateAlternateColorCodes(string));
            Minecraft minecraft = RenameCommand.access$getMc$p$s1046033730();
            Intrinsics.checkExpressionValueIsNotNull(minecraft, "mc");
            NetHandlerPlayClient netHandlerPlayClient = minecraft.func_147114_u();
            EntityPlayerSP entityPlayerSP2 = RenameCommand.access$getMc$p$s1046033730().field_71439_g;
            if (entityPlayerSP2 == null) {
                Intrinsics.throwNpe();
            }
            netHandlerPlayClient.func_147297_a((Packet)new C10PacketCreativeInventoryAction(36 + entityPlayerSP2.field_71071_by.field_70461_c, item));
            this.chat("\u00a73Item renamed to '" + item.func_82833_r() + "\u00a73'");
            return;
        }
        this.chatSyntax("rename <name>");
    }

    public RenameCommand() {
        super("rename", new String[0]);
    }

    public static final /* synthetic */ Minecraft access$getMc$p$s1046033730() {
        return MinecraftInstance.mc;
    }
}

