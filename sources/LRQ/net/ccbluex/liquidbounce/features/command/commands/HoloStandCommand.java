/*
 * Decompiled with CFR 0.152.
 */
package net.ccbluex.liquidbounce.features.command.commands;

import net.ccbluex.liquidbounce.api.enums.ItemType;
import net.ccbluex.liquidbounce.api.minecraft.item.IItemStack;
import net.ccbluex.liquidbounce.api.minecraft.nbt.INBTTagCompound;
import net.ccbluex.liquidbounce.api.minecraft.nbt.INBTTagList;
import net.ccbluex.liquidbounce.features.command.Command;
import net.ccbluex.liquidbounce.utils.MinecraftInstance;
import net.ccbluex.liquidbounce.utils.misc.StringUtils;

public final class HoloStandCommand
extends Command {
    @Override
    public void execute(String[] args) {
        if (args.length > 4) {
            if (MinecraftInstance.mc.getPlayerController().isNotCreative()) {
                this.chat("\u00a7c\u00a7lError: \u00a73You need to be in creative mode.");
                return;
            }
            try {
                String string = args[1];
                boolean bl = false;
                double x = Double.parseDouble(string);
                String string2 = args[2];
                boolean bl2 = false;
                double y = Double.parseDouble(string2);
                String string3 = args[3];
                boolean bl3 = false;
                double z = Double.parseDouble(string3);
                String message = StringUtils.toCompleteString(args, 4);
                IItemStack itemStack = MinecraftInstance.classProvider.createItemStack(MinecraftInstance.classProvider.getItemEnum(ItemType.ARMOR_STAND));
                INBTTagCompound base = MinecraftInstance.classProvider.createNBTTagCompound();
                INBTTagCompound entityTag = MinecraftInstance.classProvider.createNBTTagCompound();
                entityTag.setInteger("Invisible", 1);
                entityTag.setString("CustomName", message);
                entityTag.setInteger("CustomNameVisible", 1);
                entityTag.setInteger("NoGravity", 1);
                INBTTagList position = MinecraftInstance.classProvider.createNBTTagList();
                position.appendTag(MinecraftInstance.classProvider.createNBTTagDouble(x));
                position.appendTag(MinecraftInstance.classProvider.createNBTTagDouble(y));
                position.appendTag(MinecraftInstance.classProvider.createNBTTagDouble(z));
                entityTag.setTag("Pos", position);
                base.setTag("EntityTag", entityTag);
                itemStack.setTagCompound(base);
                itemStack.setStackDisplayName("\u00a7c\u00a7lHolo\u00a7eStand");
                MinecraftInstance.mc.getNetHandler().addToSendQueue(MinecraftInstance.classProvider.createCPacketCreativeInventoryAction(36, itemStack));
                this.chat("The HoloStand was successfully added to your inventory.");
            }
            catch (NumberFormatException exception) {
                this.chatSyntaxError();
            }
            return;
        }
        this.chatSyntax("holostand <x> <y> <z> <message...>");
    }

    public HoloStandCommand() {
        super("holostand", new String[0]);
    }
}

