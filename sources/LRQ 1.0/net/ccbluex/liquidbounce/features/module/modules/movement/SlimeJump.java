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
import net.ccbluex.liquidbounce.event.JumpEvent;
import net.ccbluex.liquidbounce.features.module.Module;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.features.module.ModuleInfo;
import net.ccbluex.liquidbounce.utils.MinecraftInstance;
import net.ccbluex.liquidbounce.utils.block.BlockUtils;
import net.ccbluex.liquidbounce.value.FloatValue;
import net.ccbluex.liquidbounce.value.ListValue;

@ModuleInfo(name="SlimeJump", description="Allows you to to jump higher on slime blocks.", category=ModuleCategory.MOVEMENT)
public final class SlimeJump
extends Module {
    private final FloatValue motionValue = new FloatValue("Motion", 0.42f, 0.2f, 1.0f);
    private final ListValue modeValue = new ListValue("Mode", new String[]{"Set", "Add"}, "Add");

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    @EventTarget
    public final void onJump(JumpEvent event) {
        IEntityPlayerSP iEntityPlayerSP = MinecraftInstance.mc.getThePlayer();
        if (iEntityPlayerSP == null) {
            return;
        }
        IEntityPlayerSP thePlayer = iEntityPlayerSP;
        if (MinecraftInstance.mc.getThePlayer() == null || MinecraftInstance.mc.getTheWorld() == null || !MinecraftInstance.classProvider.isBlockSlime(BlockUtils.getBlock(thePlayer.getPosition().down()))) return;
        event.cancelEvent();
        String string = (String)this.modeValue.get();
        boolean bl = false;
        String string2 = string;
        if (string2 == null) {
            throw new TypeCastException("null cannot be cast to non-null type java.lang.String");
        }
        string = string2.toLowerCase();
        switch (string.hashCode()) {
            case 96417: {
                if (!string.equals("add")) return;
                break;
            }
            case 113762: {
                if (!string.equals("set")) return;
                thePlayer.setMotionY(((Number)this.motionValue.get()).floatValue());
                return;
            }
        }
        IEntityPlayerSP iEntityPlayerSP2 = thePlayer;
        iEntityPlayerSP2.setMotionY(iEntityPlayerSP2.getMotionY() + ((Number)this.motionValue.get()).doubleValue());
    }
}

