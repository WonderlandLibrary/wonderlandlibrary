/*
 * Decompiled with CFR 0.152.
 */
package net.ccbluex.liquidbounce.features.module.modules.movement.speeds.Report;

import net.ccbluex.liquidbounce.LiquidBounce;
import net.ccbluex.liquidbounce.event.MoveEvent;
import net.ccbluex.liquidbounce.features.module.modules.movement.speeds.SpeedMode;
import net.ccbluex.liquidbounce.features.module.modules.world.Scaffold;
import net.ccbluex.liquidbounce.utils.MovementUtils;
import obfuscator.NativeMethod;

public class BlocksmcHop
extends SpeedMode {
    public BlocksmcHop() {
        super("OldBlocksmc");
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
        if (!(BlocksmcHop.mc.field_71439_g.field_70134_J || BlocksmcHop.mc.field_71439_g.func_180799_ab() || BlocksmcHop.mc.field_71439_g.func_70090_H() || BlocksmcHop.mc.field_71439_g.func_70617_f_() || BlocksmcHop.mc.field_71439_g.field_70154_o != null || !MovementUtils.isMoving())) {
            BlocksmcHop.mc.field_71474_y.field_74314_A.field_74513_e = false;
            if (BlocksmcHop.mc.field_71439_g.field_70122_E) {
                BlocksmcHop.mc.field_71439_g.func_70664_aZ();
                MovementUtils.strafe(0.48f);
            }
            MovementUtils.strafe();
        }
    }

    @Override
    public void onDisable() {
        Scaffold Scaffold2 = (Scaffold)LiquidBounce.moduleManager.getModule(Scaffold.class);
        if (!BlocksmcHop.mc.field_71439_g.func_70093_af() && !Scaffold2.getState()) {
            MovementUtils.strafe(0.2f);
        }
    }

    @Override
    public void setMotion(MoveEvent event, double baseMoveSpeed, double d, boolean b) {
    }

    @Override
    public void onMove(MoveEvent event) {
    }
}

