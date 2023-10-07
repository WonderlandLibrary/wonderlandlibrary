/*
 * Decompiled with CFR 0.152.
 */
package net.ccbluex.liquidbounce.features.module.modules.movement.speeds.Report;

import net.ccbluex.liquidbounce.LiquidBounce;
import net.ccbluex.liquidbounce.event.MoveEvent;
import net.ccbluex.liquidbounce.features.module.modules.movement.Speed;
import net.ccbluex.liquidbounce.features.module.modules.movement.speeds.SpeedMode;
import net.ccbluex.liquidbounce.features.module.modules.world.Scaffold;
import net.ccbluex.liquidbounce.utils.MovementUtils;
import obfuscator.NativeMethod;

public class BlocksmcHopTest
extends SpeedMode {
    public BlocksmcHopTest() {
        super("BlocksmcHopTest2");
    }

    @Override
    @NativeMethod.Entry
    public void onMotion() {
    }

    @Override
    @NativeMethod.Entry
    public void onEnable() {
    }

    @Override
    public void onUpdate() {
        Speed speed = (Speed)LiquidBounce.moduleManager.getModule(Speed.class);
        if (!(BlocksmcHopTest.mc.field_71439_g.field_70134_J || BlocksmcHopTest.mc.field_71439_g.func_180799_ab() || BlocksmcHopTest.mc.field_71439_g.func_70090_H() || BlocksmcHopTest.mc.field_71439_g.func_70617_f_() || BlocksmcHopTest.mc.field_71439_g.field_70154_o != null || !MovementUtils.isMoving())) {
            BlocksmcHopTest.mc.field_71474_y.field_74314_A.field_74513_e = false;
            if (BlocksmcHopTest.mc.field_71439_g.field_70122_E) {
                BlocksmcHopTest.mc.field_71439_g.func_70664_aZ();
                MovementUtils.strafe(((Float)speed.BlocksmcTestStrafe.get()).floatValue());
            }
            MovementUtils.strafe();
        }
    }

    @Override
    public void onDisable() {
        Scaffold Scaffold2 = (Scaffold)LiquidBounce.moduleManager.getModule(Scaffold.class);
        Speed speed = (Speed)LiquidBounce.moduleManager.getModule(Speed.class);
        if (!BlocksmcHopTest.mc.field_71439_g.func_70093_af() && !Scaffold2.getState()) {
            MovementUtils.strafe(((Float)speed.BlocksmcTestSpeed.get()).floatValue());
        }
    }

    @Override
    public void setMotion(MoveEvent event, double baseMoveSpeed, double d, boolean b) {
    }

    @Override
    public void onMove(MoveEvent event) {
    }
}

