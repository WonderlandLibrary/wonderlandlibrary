/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kotlin.jvm.internal.Intrinsics
 */
package net.ccbluex.liquidbounce.features.command.commands;

import kotlin.jvm.internal.Intrinsics;
import net.ccbluex.liquidbounce.api.minecraft.client.entity.IEntityPlayerSP;
import net.ccbluex.liquidbounce.api.minecraft.item.IItemStack;
import net.ccbluex.liquidbounce.features.command.Command;
import net.ccbluex.liquidbounce.utils.MinecraftInstance;
import net.ccbluex.liquidbounce.utils.misc.StringUtils;
import net.ccbluex.liquidbounce.utils.render.ColorUtils;

public final class RenameCommand
extends Command {
    @Override
    public void execute(String[] stringArray) {
        if (stringArray.length > 1) {
            IItemStack iItemStack;
            if (MinecraftInstance.mc.getPlayerController().isNotCreative()) {
                this.chat("\u00a7c\u00a7lError: \u00a73You need to be in creative mode.");
                return;
            }
            IEntityPlayerSP iEntityPlayerSP = MinecraftInstance.mc.getThePlayer();
            if (iEntityPlayerSP == null) {
                Intrinsics.throwNpe();
            }
            IItemStack iItemStack2 = iItemStack = iEntityPlayerSP.getHeldItem();
            if ((iItemStack2 != null ? iItemStack2.getItem() : null) == null) {
                this.chat("\u00a7c\u00a7lError: \u00a73You need to hold a item.");
                return;
            }
            iItemStack.setStackDisplayName(ColorUtils.translateAlternateColorCodes(StringUtils.toCompleteString(stringArray, 1)));
            this.chat("\u00a73Item renamed to '" + iItemStack.getDisplayName() + "\u00a73'");
            return;
        }
        this.chatSyntax("rename <name>");
    }

    public RenameCommand() {
        super("rename", new String[0]);
    }
}

