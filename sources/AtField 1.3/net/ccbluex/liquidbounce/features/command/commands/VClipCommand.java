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
    public void execute(String[] stringArray) {
        if (stringArray.length > 1) {
            try {
                IEntity iEntity;
                Object object = stringArray[1];
                boolean bl = false;
                double d = Double.parseDouble((String)object);
                IEntityPlayerSP iEntityPlayerSP = MinecraftInstance.mc.getThePlayer();
                if (iEntityPlayerSP == null) {
                    return;
                }
                object = iEntityPlayerSP;
                if (object.isRiding()) {
                    iEntity = object.getRidingEntity();
                    if (iEntity == null) {
                        Intrinsics.throwNpe();
                    }
                } else {
                    iEntity = (IEntity)object;
                }
                IEntity iEntity2 = iEntity;
                iEntity2.setPosition(iEntity2.getPosX(), iEntity2.getPosY() + d, iEntity2.getPosZ());
                this.chat("You were teleported.");
            }
            catch (NumberFormatException numberFormatException) {
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

