/*
 * Decompiled with CFR 0.152.
 */
package net.ccbluex.liquidbounce.features.command.commands;

import net.ccbluex.liquidbounce.api.minecraft.client.entity.IEntityPlayerSP;
import net.ccbluex.liquidbounce.features.command.Command;
import net.ccbluex.liquidbounce.utils.MinecraftInstance;

public final class HurtCommand
extends Command {
    /*
     * WARNING - void declaration
     */
    @Override
    public void execute(String[] args) {
        int damage = 1;
        if (args.length > 1) {
            try {
                String string = args[1];
                boolean bl = false;
                damage = Integer.parseInt(string);
            }
            catch (NumberFormatException ignored) {
                this.chatSyntaxError();
                return;
            }
        }
        IEntityPlayerSP iEntityPlayerSP = MinecraftInstance.mc.getThePlayer();
        if (iEntityPlayerSP == null) {
            return;
        }
        IEntityPlayerSP thePlayer = iEntityPlayerSP;
        double x = thePlayer.getPosX();
        double y = thePlayer.getPosY();
        double z = thePlayer.getPosZ();
        int n = 0;
        int n2 = 65 * damage;
        while (n < n2) {
            void i;
            MinecraftInstance.mc.getNetHandler().addToSendQueue(MinecraftInstance.classProvider.createCPacketPlayerPosition(x, y + 0.049, z, false));
            MinecraftInstance.mc.getNetHandler().addToSendQueue(MinecraftInstance.classProvider.createCPacketPlayerPosition(x, y, z, false));
            ++i;
        }
        MinecraftInstance.mc.getNetHandler().addToSendQueue(MinecraftInstance.classProvider.createCPacketPlayerPosition(x, y, z, true));
        this.chat("You were damaged.");
    }

    public HurtCommand() {
        super("hurt", new String[0]);
    }
}

