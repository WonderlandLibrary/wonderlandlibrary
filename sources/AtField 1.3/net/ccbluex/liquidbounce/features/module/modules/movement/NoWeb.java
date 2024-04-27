/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kotlin.TypeCastException
 */
package net.ccbluex.liquidbounce.features.module.modules.movement;

import kotlin.TypeCastException;
import net.ccbluex.liquidbounce.api.minecraft.client.entity.IEntityPlayerSP;
import net.ccbluex.liquidbounce.event.EventTarget;
import net.ccbluex.liquidbounce.event.UpdateEvent;
import net.ccbluex.liquidbounce.features.module.Module;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.features.module.ModuleInfo;
import net.ccbluex.liquidbounce.utils.MinecraftInstance;
import net.ccbluex.liquidbounce.value.ListValue;

@ModuleInfo(name="NoWeb", description="Prevents you from getting slowed down in webs.", category=ModuleCategory.MOVEMENT)
public final class NoWeb
extends Module {
    private final ListValue modeValue = new ListValue("Mode", new String[]{"None", "AAC", "LAAC", "Rewi"}, "None");

    @Override
    public String getTag() {
        return (String)this.modeValue.get();
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    @EventTarget
    public final void onUpdate(UpdateEvent updateEvent) {
        IEntityPlayerSP iEntityPlayerSP;
        block10: {
            block11: {
                IEntityPlayerSP iEntityPlayerSP2 = MinecraftInstance.mc.getThePlayer();
                if (iEntityPlayerSP2 == null) {
                    return;
                }
                iEntityPlayerSP = iEntityPlayerSP2;
                if (!iEntityPlayerSP.isInWeb()) {
                    return;
                }
                String string = (String)this.modeValue.get();
                boolean bl = false;
                String string2 = string;
                if (string2 == null) {
                    throw new TypeCastException("null cannot be cast to non-null type java.lang.String");
                }
                string = string2.toLowerCase();
                switch (string.hashCode()) {
                    case 3497029: {
                        if (!string.equals("rewi")) return;
                        break block10;
                    }
                    case 96323: {
                        if (!string.equals("aac")) return;
                        break;
                    }
                    case 3313751: {
                        if (!string.equals("laac")) return;
                        break block11;
                    }
                    case 3387192: {
                        if (!string.equals("none")) return;
                        iEntityPlayerSP.setInWeb(false);
                        return;
                    }
                }
                iEntityPlayerSP.setJumpMovementFactor(0.59f);
                if (MinecraftInstance.mc.getGameSettings().getKeyBindSneak().isKeyDown()) return;
                iEntityPlayerSP.setMotionY(0.0);
                return;
            }
            iEntityPlayerSP.setJumpMovementFactor(iEntityPlayerSP.getMovementInput().getMoveStrafe() != 0.0f ? 1.0f : 1.21f);
            if (!MinecraftInstance.mc.getGameSettings().getKeyBindSneak().isKeyDown()) {
                iEntityPlayerSP.setMotionY(0.0);
            }
            if (!iEntityPlayerSP.getOnGround()) return;
            iEntityPlayerSP.jump();
            return;
        }
        iEntityPlayerSP.setJumpMovementFactor(0.42f);
        if (!iEntityPlayerSP.getOnGround()) return;
        iEntityPlayerSP.jump();
        return;
    }
}

