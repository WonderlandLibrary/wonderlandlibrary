/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kotlin.jvm.internal.Intrinsics
 *  kotlin.text.StringsKt
 */
package net.ccbluex.liquidbounce.features.module.modules.movement;

import kotlin.jvm.internal.Intrinsics;
import kotlin.text.StringsKt;
import net.ccbluex.liquidbounce.LiquidBounce;
import net.ccbluex.liquidbounce.api.minecraft.client.entity.IEntityPlayerSP;
import net.ccbluex.liquidbounce.api.minecraft.util.WBlockPos;
import net.ccbluex.liquidbounce.event.EventTarget;
import net.ccbluex.liquidbounce.event.UpdateEvent;
import net.ccbluex.liquidbounce.features.module.Module;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.features.module.ModuleInfo;
import net.ccbluex.liquidbounce.features.module.modules.movement.Speed;
import net.ccbluex.liquidbounce.utils.MinecraftInstance;
import net.ccbluex.liquidbounce.utils.MovementUtils;
import net.ccbluex.liquidbounce.utils.block.BlockUtils;
import net.ccbluex.liquidbounce.value.BoolValue;
import net.ccbluex.liquidbounce.value.ListValue;

@ModuleInfo(name="FastStairs", description="Allows you to climb up stairs faster.", category=ModuleCategory.MOVEMENT)
public final class FastStairs
extends Module {
    private final ListValue modeValue = new ListValue("Mode", new String[]{"Step", "NCP", "AAC3.1.0", "AAC3.3.6", "AAC3.3.13"}, "NCP");
    private final BoolValue longJumpValue = new BoolValue("LongJump", false);
    private boolean canJump;
    private boolean walkingDown;

    @EventTarget
    public final void onUpdate(UpdateEvent event) {
        double motion;
        IEntityPlayerSP thePlayer;
        block19: {
            block18: {
                IEntityPlayerSP iEntityPlayerSP = MinecraftInstance.mc.getThePlayer();
                if (iEntityPlayerSP == null) {
                    return;
                }
                thePlayer = iEntityPlayerSP;
                if (!MovementUtils.isMoving()) break block18;
                Module module = LiquidBounce.INSTANCE.getModuleManager().get(Speed.class);
                if (module == null) {
                    Intrinsics.throwNpe();
                }
                if (!module.getState()) break block19;
            }
            return;
        }
        if (thePlayer.getFallDistance() > 0.0f && !this.walkingDown) {
            this.walkingDown = true;
        } else if (thePlayer.getPosY() > thePlayer.getPrevChasingPosY()) {
            this.walkingDown = false;
        }
        String mode = (String)this.modeValue.get();
        if (!thePlayer.getOnGround()) {
            return;
        }
        WBlockPos blockPos = new WBlockPos(thePlayer.getPosX(), thePlayer.getEntityBoundingBox().getMinY(), thePlayer.getPosZ());
        if (MinecraftInstance.classProvider.isBlockStairs(BlockUtils.getBlock(blockPos)) && !this.walkingDown) {
            thePlayer.setPosition(thePlayer.getPosX(), thePlayer.getPosY() + 0.5, thePlayer.getPosZ());
            motion = StringsKt.equals((String)mode, (String)"NCP", (boolean)true) ? 1.4 : (StringsKt.equals((String)mode, (String)"AAC3.1.0", (boolean)true) ? 1.5 : (StringsKt.equals((String)mode, (String)"AAC3.3.13", (boolean)true) ? 1.2 : 1.0));
            IEntityPlayerSP iEntityPlayerSP = thePlayer;
            iEntityPlayerSP.setMotionX(iEntityPlayerSP.getMotionX() * motion);
            IEntityPlayerSP iEntityPlayerSP2 = thePlayer;
            iEntityPlayerSP2.setMotionZ(iEntityPlayerSP2.getMotionZ() * motion);
        }
        if (MinecraftInstance.classProvider.isBlockStairs(BlockUtils.getBlock(blockPos.down()))) {
            if (this.walkingDown) {
                if (StringsKt.equals((String)mode, (String)"NCP", (boolean)true)) {
                    thePlayer.setMotionY(-1.0);
                } else if (StringsKt.equals((String)mode, (String)"AAC3.3.13", (boolean)true)) {
                    IEntityPlayerSP iEntityPlayerSP = thePlayer;
                    iEntityPlayerSP.setMotionY(iEntityPlayerSP.getMotionY() - 0.014);
                }
                return;
            }
            motion = StringsKt.equals((String)mode, (String)"NCP", (boolean)true) ? 1.3 : (StringsKt.equals((String)mode, (String)"AAC3.1.0", (boolean)true) ? 1.3 : (StringsKt.equals((String)mode, (String)"AAC3.3.6", (boolean)true) ? 1.48 : (StringsKt.equals((String)mode, (String)"AAC3.3.13", (boolean)true) ? 1.52 : 1.3)));
            IEntityPlayerSP iEntityPlayerSP = thePlayer;
            iEntityPlayerSP.setMotionX(iEntityPlayerSP.getMotionX() * motion);
            IEntityPlayerSP iEntityPlayerSP3 = thePlayer;
            iEntityPlayerSP3.setMotionZ(iEntityPlayerSP3.getMotionZ() * motion);
            this.canJump = true;
        } else if (StringsKt.startsWith((String)mode, (String)"AAC", (boolean)true) && this.canJump) {
            if (((Boolean)this.longJumpValue.get()).booleanValue()) {
                thePlayer.jump();
                IEntityPlayerSP iEntityPlayerSP = thePlayer;
                iEntityPlayerSP.setMotionX(iEntityPlayerSP.getMotionX() * 1.35);
                IEntityPlayerSP iEntityPlayerSP4 = thePlayer;
                iEntityPlayerSP4.setMotionZ(iEntityPlayerSP4.getMotionZ() * 1.35);
            }
            this.canJump = false;
        }
    }

    @Override
    public String getTag() {
        return (String)this.modeValue.get();
    }
}

