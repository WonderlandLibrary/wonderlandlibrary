/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kotlin.jvm.internal.Intrinsics
 */
package net.ccbluex.liquidbounce.features.command.commands;

import kotlin.jvm.internal.Intrinsics;
import net.ccbluex.liquidbounce.api.minecraft.client.entity.IEntity;
import net.ccbluex.liquidbounce.api.minecraft.client.entity.IEntityPlayerSP;
import net.ccbluex.liquidbounce.features.command.Command;
import net.ccbluex.liquidbounce.utils.MinecraftInstance;

public final class VClipCommand
extends Command {
    @Override
    public void execute(String[] args) {
        if (args.length > 1) {
            try {
                IEntity iEntity;
                String string = args[1];
                boolean bl = false;
                double y = Double.parseDouble(string);
                IEntityPlayerSP iEntityPlayerSP = MinecraftInstance.mc.getThePlayer();
                if (iEntityPlayerSP == null) {
                    return;
                }
                IEntityPlayerSP thePlayer = iEntityPlayerSP;
                if (thePlayer.isRiding()) {
                    iEntity = thePlayer.getRidingEntity();
                    if (iEntity == null) {
                        Intrinsics.throwNpe();
                    }
                } else {
                    iEntity = thePlayer;
                }
                IEntity entity = iEntity;
                entity.setPosition(entity.getPosX(), entity.getPosY() + y, entity.getPosZ());
                this.chat("You were teleported.");
            }
            catch (NumberFormatException ex) {
                this.chatSyntaxError();
            }
            return;
        }
        this.chatSyntax("vclip <value>");
    }

    public VClipCommand() {
        super("vclip", new String[0]);
    }
}

