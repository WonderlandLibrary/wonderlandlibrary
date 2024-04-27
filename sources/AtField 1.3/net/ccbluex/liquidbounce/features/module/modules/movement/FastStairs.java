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
    private boolean walkingDown;
    private boolean canJump;
    private final BoolValue longJumpValue = new BoolValue("LongJump", false);

    @EventTarget
    public final void onUpdate(UpdateEvent updateEvent) {
        double d;
        IEntityPlayerSP iEntityPlayerSP;
        block19: {
            block18: {
                IEntityPlayerSP iEntityPlayerSP2 = MinecraftInstance.mc.getThePlayer();
                if (iEntityPlayerSP2 == null) {
                    return;
                }
                iEntityPlayerSP = iEntityPlayerSP2;
                if (!MovementUtils.isMoving()) break block18;
                Module module = LiquidBounce.INSTANCE.getModuleManager().get(Speed.class);
                if (module == null) {
                    Intrinsics.throwNpe();
                }
                if (!module.getState()) break block19;
            }
            return;
        }
        if (iEntityPlayerSP.getFallDistance() > 0.0f && !this.walkingDown) {
            this.walkingDown = true;
        } else if (iEntityPlayerSP.getPosY() > iEntityPlayerSP.getPrevChasingPosY()) {
            this.walkingDown = false;
        }
        String string = (String)this.modeValue.get();
        if (!iEntityPlayerSP.getOnGround()) {
            return;
        }
        WBlockPos wBlockPos = new WBlockPos(iEntityPlayerSP.getPosX(), iEntityPlayerSP.getEntityBoundingBox().getMinY(), iEntityPlayerSP.getPosZ());
        if (MinecraftInstance.classProvider.isBlockStairs(BlockUtils.getBlock(wBlockPos)) && !this.walkingDown) {
            iEntityPlayerSP.setPosition(iEntityPlayerSP.getPosX(), iEntityPlayerSP.getPosY() + 0.5, iEntityPlayerSP.getPosZ());
            d = StringsKt.equals((String)string, (String)"NCP", (boolean)true) ? 1.4 : (StringsKt.equals((String)string, (String)"AAC3.1.0", (boolean)true) ? 1.5 : (StringsKt.equals((String)string, (String)"AAC3.3.13", (boolean)true) ? 1.2 : 1.0));
            IEntityPlayerSP iEntityPlayerSP3 = iEntityPlayerSP;
            iEntityPlayerSP3.setMotionX(iEntityPlayerSP3.getMotionX() * d);
            IEntityPlayerSP iEntityPlayerSP4 = iEntityPlayerSP;
            iEntityPlayerSP4.setMotionZ(iEntityPlayerSP4.getMotionZ() * d);
        }
        if (MinecraftInstance.classProvider.isBlockStairs(BlockUtils.getBlock(wBlockPos.down()))) {
            if (this.walkingDown) {
                if (StringsKt.equals((String)string, (String)"NCP", (boolean)true)) {
                    iEntityPlayerSP.setMotionY(-1.0);
                } else if (StringsKt.equals((String)string, (String)"AAC3.3.13", (boolean)true)) {
                    IEntityPlayerSP iEntityPlayerSP5 = iEntityPlayerSP;
                    iEntityPlayerSP5.setMotionY(iEntityPlayerSP5.getMotionY() - 0.014);
                }
                return;
            }
            d = StringsKt.equals((String)string, (String)"NCP", (boolean)true) ? 1.3 : (StringsKt.equals((String)string, (String)"AAC3.1.0", (boolean)true) ? 1.3 : (StringsKt.equals((String)string, (String)"AAC3.3.6", (boolean)true) ? 1.48 : (StringsKt.equals((String)string, (String)"AAC3.3.13", (boolean)true) ? 1.52 : 1.3)));
            IEntityPlayerSP iEntityPlayerSP6 = iEntityPlayerSP;
            iEntityPlayerSP6.setMotionX(iEntityPlayerSP6.getMotionX() * d);
            IEntityPlayerSP iEntityPlayerSP7 = iEntityPlayerSP;
            iEntityPlayerSP7.setMotionZ(iEntityPlayerSP7.getMotionZ() * d);
            this.canJump = true;
        } else if (StringsKt.startsWith((String)string, (String)"AAC", (boolean)true) && this.canJump) {
            if (((Boolean)this.longJumpValue.get()).booleanValue()) {
                iEntityPlayerSP.jump();
                IEntityPlayerSP iEntityPlayerSP8 = iEntityPlayerSP;
                iEntityPlayerSP8.setMotionX(iEntityPlayerSP8.getMotionX() * 1.35);
                IEntityPlayerSP iEntityPlayerSP9 = iEntityPlayerSP;
                iEntityPlayerSP9.setMotionZ(iEntityPlayerSP9.getMotionZ() * 1.35);
            }
            this.canJump = false;
        }
    }

    @Override
    public String getTag() {
        return (String)this.modeValue.get();
    }
}

