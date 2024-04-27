package net.ccbluex.liquidbounce.features.command.commands;

import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import net.ccbluex.liquidbounce.api.minecraft.client.entity.IEntityPlayerSP;
import net.ccbluex.liquidbounce.api.minecraft.client.network.IINetHandlerPlayClient;
import net.ccbluex.liquidbounce.api.minecraft.item.IItemStack;
import net.ccbluex.liquidbounce.features.command.Command;
import net.ccbluex.liquidbounce.utils.MinecraftInstance;
import net.ccbluex.liquidbounce.utils.misc.StringUtils;
import net.ccbluex.liquidbounce.utils.render.ColorUtils;
import org.jetbrains.annotations.NotNull;

@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, d1={"\u0000\n\n\n\b\n\n\u0000\n\n\n\b\u000020B¢J02\f\b00H¢\b¨\t"}, d2={"Lnet/ccbluex/liquidbounce/features/command/commands/RenameCommand;", "Lnet/ccbluex/liquidbounce/features/command/Command;", "()V", "execute", "", "args", "", "", "([Ljava/lang/String;)V", "Pride"})
public final class RenameCommand
extends Command {
    @Override
    public void execute(@NotNull String[] args) {
        Intrinsics.checkParameterIsNotNull(args, "args");
        if (args.length > 1) {
            IItemStack item;
            if (MinecraftInstance.mc.getPlayerController().isNotCreative()) {
                this.chat("§c§lError: §3You need to be in creative mode.");
                return;
            }
            IEntityPlayerSP iEntityPlayerSP = MinecraftInstance.mc.getThePlayer();
            if (iEntityPlayerSP == null) {
                Intrinsics.throwNpe();
            }
            IItemStack iItemStack = item = iEntityPlayerSP.getHeldItem();
            if ((iItemStack != null ? iItemStack.getItem() : null) == null) {
                this.chat("§c§lError: §3You need to hold a item.");
                return;
            }
            String string = StringUtils.toCompleteString(args, 1);
            Intrinsics.checkExpressionValueIsNotNull(string, "StringUtils.toCompleteString(args, 1)");
            item.setStackDisplayName(ColorUtils.translateAlternateColorCodes(string));
            IINetHandlerPlayClient iINetHandlerPlayClient = MinecraftInstance.mc.getNetHandler();
            IEntityPlayerSP iEntityPlayerSP2 = MinecraftInstance.mc.getThePlayer();
            if (iEntityPlayerSP2 == null) {
                Intrinsics.throwNpe();
            }
            iINetHandlerPlayClient.addToSendQueue(MinecraftInstance.classProvider.createCPacketCreativeInventoryAction(36 + iEntityPlayerSP2.getInventory().getCurrentItem(), item));
            this.chat("§3Item renamed to '" + item.getDisplayName() + "§3'");
            return;
        }
        this.chatSyntax("rename <name>");
    }

    public RenameCommand() {
        super("rename", new String[0]);
    }
}
