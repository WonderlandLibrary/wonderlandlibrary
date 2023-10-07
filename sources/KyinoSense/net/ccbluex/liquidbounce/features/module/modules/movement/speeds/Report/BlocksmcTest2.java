/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.potion.Potion
 */
package net.ccbluex.liquidbounce.features.module.modules.movement.speeds.Report;

import net.ccbluex.liquidbounce.LiquidBounce;
import net.ccbluex.liquidbounce.event.MoveEvent;
import net.ccbluex.liquidbounce.features.module.modules.movement.Speed;
import net.ccbluex.liquidbounce.features.module.modules.movement.speeds.SpeedMode;
import net.ccbluex.liquidbounce.features.module.modules.world.Scaffold;
import net.ccbluex.liquidbounce.utils.MovementUtils;
import net.minecraft.potion.Potion;
import obfuscator.NativeMethod;

public class BlocksmcTest2
extends SpeedMode {
    public BlocksmcTest2() {
        super("BlocksmcTest2");
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
        if (MovementUtils.isMoving()) {
            if (BlocksmcTest2.mc.field_71439_g.func_70644_a(Potion.field_76424_c)) {
                MovementUtils.strafe(0.31555554f);
            } else {
                MovementUtils.strafe(0.26875857f);
            }
            if (BlocksmcTest2.mc.field_71439_g.field_70122_E) {
                BlocksmcTest2.mc.field_71439_g.func_70664_aZ();
            }
        }
        if (((Boolean)speed.DamageBoostValue.get()).booleanValue() && BlocksmcTest2.mc.field_71439_g.field_70737_aN > 0) {
            MovementUtils.strafe(0.41995555f);
        }
        BlocksmcTest2.mc.field_71428_T.field_74278_d = (Boolean)speed.DamageBoostValue.get() != false && (Boolean)speed.TimerValue.get() != false && BlocksmcTest2.mc.field_71439_g.field_70737_aN > 0 ? 1.15f : 1.0f;
    }

    @Override
    public void onDisable() {
        Scaffold Scaffold2 = (Scaffold)LiquidBounce.moduleManager.getModule(Scaffold.class);
        if (!BlocksmcTest2.mc.field_71439_g.func_70093_af() && !Scaffold2.getState()) {
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

