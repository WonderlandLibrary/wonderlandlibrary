/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.block.BlockAir
 *  net.minecraft.block.BlockSlab
 *  net.minecraft.block.BlockSlime
 *  net.minecraft.block.BlockStairs
 *  net.minecraft.client.entity.EntityPlayerSP
 *  net.minecraft.client.multiplayer.WorldClient
 *  net.minecraft.init.Blocks
 *  net.minecraft.network.Packet
 *  net.minecraft.network.play.server.S08PacketPlayerPosLook
 *  net.minecraft.util.BlockPos
 */
package net.ccbluex.liquidbounce.features.module.modules.movement;

import java.util.ArrayList;
import net.ccbluex.liquidbounce.LiquidBounce;
import net.ccbluex.liquidbounce.event.EventTarget;
import net.ccbluex.liquidbounce.event.PacketEvent;
import net.ccbluex.liquidbounce.event.UpdateEvent;
import net.ccbluex.liquidbounce.features.module.Module;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.features.module.ModuleInfo;
import net.ccbluex.liquidbounce.features.module.modules.movement.Speed;
import net.ccbluex.liquidbounce.utils.MovementUtils;
import net.ccbluex.liquidbounce.utils.block.BlockUtils;
import net.ccbluex.liquidbounce.value.BoolValue;
import net.ccbluex.liquidbounce.value.FloatValue;
import net.ccbluex.liquidbounce.value.ListValue;
import net.minecraft.block.BlockAir;
import net.minecraft.block.BlockSlab;
import net.minecraft.block.BlockSlime;
import net.minecraft.block.BlockStairs;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.init.Blocks;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S08PacketPlayerPosLook;
import net.minecraft.util.BlockPos;

@ModuleInfo(name="BufferSpeed", description="Allows you to walk faster on slabs and stairs.", category=ModuleCategory.MOVEMENT)
public class BufferSpeed
extends Module {
    private final BoolValue speedLimitValue = new BoolValue("SpeedLimit", true);
    private final FloatValue maxSpeedValue = new FloatValue("MaxSpeed", 2.0f, 1.0f, 5.0f);
    private final BoolValue bufferValue = new BoolValue("Buffer", true);
    private final BoolValue stairsValue = new BoolValue("Stairs", true);
    private final FloatValue stairsBoostValue = new FloatValue("StairsBoost", 1.87f, 1.0f, 2.0f);
    private final ListValue stairsModeValue = new ListValue("StairsMode", new String[]{"Old", "New"}, "New");
    private final BoolValue slabsValue = new BoolValue("Slabs", true);
    private final FloatValue slabsBoostValue = new FloatValue("SlabsBoost", 1.87f, 1.0f, 2.0f);
    private final ListValue slabsModeValue = new ListValue("SlabsMode", new String[]{"Old", "New"}, "New");
    private final BoolValue iceValue = new BoolValue("Ice", false);
    private final FloatValue iceBoostValue = new FloatValue("IceBoost", 1.342f, 1.0f, 2.0f);
    private final BoolValue snowValue = new BoolValue("Snow", true);
    private final FloatValue snowBoostValue = new FloatValue("SnowBoost", 1.87f, 1.0f, 2.0f);
    private final BoolValue snowPortValue = new BoolValue("SnowPort", true);
    private final BoolValue wallValue = new BoolValue("Wall", true);
    private final FloatValue wallBoostValue = new FloatValue("WallBoost", 1.87f, 1.0f, 2.0f);
    private final ListValue wallModeValue = new ListValue("WallMode", new String[]{"Old", "New"}, "New");
    private final BoolValue headBlockValue = new BoolValue("HeadBlock", true);
    private final FloatValue headBlockBoostValue = new FloatValue("HeadBlockBoost", 1.87f, 1.0f, 2.0f);
    private final BoolValue slimeValue = new BoolValue("Slime", true);
    private final BoolValue airStrafeValue = new BoolValue("AirStrafe", false);
    private final BoolValue noHurtValue = new BoolValue("NoHurt", true);
    private double speed = 0.0;
    private boolean down;
    private boolean forceDown;
    private boolean fastHop;
    private boolean hadFastHop;
    private boolean legitHop;

    @EventTarget
    public void onUpdate(UpdateEvent event) {
        if (LiquidBounce.moduleManager.getModule(Speed.class).getState() || ((Boolean)this.noHurtValue.get()).booleanValue() && BufferSpeed.mc.field_71439_g.field_70737_aN > 0) {
            this.reset();
            return;
        }
        BlockPos blockPos = new BlockPos(BufferSpeed.mc.field_71439_g.field_70165_t, BufferSpeed.mc.field_71439_g.func_174813_aQ().field_72338_b, BufferSpeed.mc.field_71439_g.field_70161_v);
        if (this.forceDown || this.down && BufferSpeed.mc.field_71439_g.field_70181_x == 0.0) {
            BufferSpeed.mc.field_71439_g.field_70181_x = -1.0;
            this.down = false;
            this.forceDown = false;
        }
        if (this.fastHop) {
            BufferSpeed.mc.field_71439_g.field_71102_ce = 0.0211f;
            this.hadFastHop = true;
        } else if (this.hadFastHop) {
            BufferSpeed.mc.field_71439_g.field_71102_ce = 0.02f;
            this.hadFastHop = false;
        }
        if (!MovementUtils.isMoving() || BufferSpeed.mc.field_71439_g.func_70093_af() || BufferSpeed.mc.field_71439_g.func_70090_H() || BufferSpeed.mc.field_71474_y.field_74314_A.func_151470_d()) {
            this.reset();
            return;
        }
        if (BufferSpeed.mc.field_71439_g.field_70122_E) {
            float currentSpeed;
            this.fastHop = false;
            if (((Boolean)this.slimeValue.get()).booleanValue() && (BlockUtils.getBlock(blockPos.func_177977_b()) instanceof BlockSlime || BlockUtils.getBlock(blockPos) instanceof BlockSlime)) {
                BufferSpeed.mc.field_71439_g.func_70664_aZ();
                BufferSpeed.mc.field_71439_g.field_70181_x = 0.08;
                BufferSpeed.mc.field_71439_g.field_70159_w *= 1.132;
                BufferSpeed.mc.field_71439_g.field_70179_y *= 1.132;
                this.down = true;
                return;
            }
            if (((Boolean)this.slabsValue.get()).booleanValue() && BlockUtils.getBlock(blockPos) instanceof BlockSlab) {
                switch (((String)this.slabsModeValue.get()).toLowerCase()) {
                    case "old": {
                        this.boost(((Float)this.slabsBoostValue.get()).floatValue());
                        return;
                    }
                    case "new": {
                        this.fastHop = true;
                        if (this.legitHop) {
                            BufferSpeed.mc.field_71439_g.func_70664_aZ();
                            BufferSpeed.mc.field_71439_g.field_70122_E = false;
                            this.legitHop = false;
                            return;
                        }
                        BufferSpeed.mc.field_71439_g.field_70122_E = false;
                        MovementUtils.strafe(0.375f);
                        BufferSpeed.mc.field_71439_g.func_70664_aZ();
                        BufferSpeed.mc.field_71439_g.field_70181_x = 0.41;
                        return;
                    }
                }
            }
            if (((Boolean)this.stairsValue.get()).booleanValue() && (BlockUtils.getBlock(blockPos.func_177977_b()) instanceof BlockStairs || BlockUtils.getBlock(blockPos) instanceof BlockStairs)) {
                switch (((String)this.stairsModeValue.get()).toLowerCase()) {
                    case "old": {
                        this.boost(((Float)this.stairsBoostValue.get()).floatValue());
                        return;
                    }
                    case "new": {
                        this.fastHop = true;
                        if (this.legitHop) {
                            BufferSpeed.mc.field_71439_g.func_70664_aZ();
                            BufferSpeed.mc.field_71439_g.field_70122_E = false;
                            this.legitHop = false;
                            return;
                        }
                        BufferSpeed.mc.field_71439_g.field_70122_E = false;
                        MovementUtils.strafe(0.375f);
                        BufferSpeed.mc.field_71439_g.func_70664_aZ();
                        BufferSpeed.mc.field_71439_g.field_70181_x = 0.41;
                        return;
                    }
                }
            }
            this.legitHop = true;
            if (((Boolean)this.headBlockValue.get()).booleanValue() && BlockUtils.getBlock(blockPos.func_177981_b(2)) != Blocks.field_150350_a) {
                this.boost(((Float)this.headBlockBoostValue.get()).floatValue());
                return;
            }
            if (((Boolean)this.iceValue.get()).booleanValue() && (BlockUtils.getBlock(blockPos.func_177977_b()) == Blocks.field_150432_aD || BlockUtils.getBlock(blockPos.func_177977_b()) == Blocks.field_150403_cj)) {
                this.boost(((Float)this.iceBoostValue.get()).floatValue());
                return;
            }
            if (((Boolean)this.snowValue.get()).booleanValue() && BlockUtils.getBlock(blockPos) == Blocks.field_150431_aC && (((Boolean)this.snowPortValue.get()).booleanValue() || BufferSpeed.mc.field_71439_g.field_70163_u - (double)((int)BufferSpeed.mc.field_71439_g.field_70163_u) >= 0.125)) {
                if (BufferSpeed.mc.field_71439_g.field_70163_u - (double)((int)BufferSpeed.mc.field_71439_g.field_70163_u) >= 0.125) {
                    this.boost(((Float)this.snowBoostValue.get()).floatValue());
                } else {
                    BufferSpeed.mc.field_71439_g.func_70664_aZ();
                    this.forceDown = true;
                }
                return;
            }
            if (((Boolean)this.wallValue.get()).booleanValue()) {
                switch (((String)this.wallModeValue.get()).toLowerCase()) {
                    case "old": {
                        if ((!BufferSpeed.mc.field_71439_g.field_70123_F || !this.isNearBlock()) && BlockUtils.getBlock(new BlockPos(BufferSpeed.mc.field_71439_g.field_70165_t, BufferSpeed.mc.field_71439_g.field_70163_u + 2.0, BufferSpeed.mc.field_71439_g.field_70161_v)) instanceof BlockAir) break;
                        this.boost(((Float)this.wallBoostValue.get()).floatValue());
                        return;
                    }
                    case "new": {
                        if (!this.isNearBlock() || BufferSpeed.mc.field_71439_g.field_71158_b.field_78901_c) break;
                        BufferSpeed.mc.field_71439_g.func_70664_aZ();
                        BufferSpeed.mc.field_71439_g.field_70181_x = 0.08;
                        BufferSpeed.mc.field_71439_g.field_70159_w *= 0.99;
                        BufferSpeed.mc.field_71439_g.field_70179_y *= 0.99;
                        this.down = true;
                        return;
                    }
                }
            }
            if (this.speed < (double)(currentSpeed = MovementUtils.getSpeed())) {
                this.speed = currentSpeed;
            }
            if (((Boolean)this.bufferValue.get()).booleanValue() && this.speed > (double)0.2f) {
                this.speed /= (double)1.02f;
                MovementUtils.strafe((float)this.speed);
            }
        } else {
            this.speed = 0.0;
            if (((Boolean)this.airStrafeValue.get()).booleanValue()) {
                MovementUtils.strafe();
            }
        }
    }

    @EventTarget
    public void onPacket(PacketEvent event) {
        Packet<?> packet = event.getPacket();
        if (packet instanceof S08PacketPlayerPosLook) {
            this.speed = 0.0;
        }
    }

    @Override
    public void onEnable() {
        this.reset();
    }

    @Override
    public void onDisable() {
        this.reset();
    }

    private void reset() {
        if (BufferSpeed.mc.field_71439_g == null) {
            return;
        }
        this.legitHop = true;
        this.speed = 0.0;
        if (this.hadFastHop) {
            BufferSpeed.mc.field_71439_g.field_71102_ce = 0.02f;
            this.hadFastHop = false;
        }
    }

    private void boost(float boost) {
        BufferSpeed.mc.field_71439_g.field_70159_w *= (double)boost;
        BufferSpeed.mc.field_71439_g.field_70179_y *= (double)boost;
        this.speed = MovementUtils.getSpeed();
        if (((Boolean)this.speedLimitValue.get()).booleanValue() && this.speed > (double)((Float)this.maxSpeedValue.get()).floatValue()) {
            this.speed = ((Float)this.maxSpeedValue.get()).floatValue();
        }
    }

    private boolean isNearBlock() {
        EntityPlayerSP thePlayer = BufferSpeed.mc.field_71439_g;
        WorldClient theWorld = BufferSpeed.mc.field_71441_e;
        ArrayList<BlockPos> blocks = new ArrayList<BlockPos>();
        blocks.add(new BlockPos(thePlayer.field_70165_t, thePlayer.field_70163_u + 1.0, thePlayer.field_70161_v - 0.7));
        blocks.add(new BlockPos(thePlayer.field_70165_t + 0.7, thePlayer.field_70163_u + 1.0, thePlayer.field_70161_v));
        blocks.add(new BlockPos(thePlayer.field_70165_t, thePlayer.field_70163_u + 1.0, thePlayer.field_70161_v + 0.7));
        blocks.add(new BlockPos(thePlayer.field_70165_t - 0.7, thePlayer.field_70163_u + 1.0, thePlayer.field_70161_v));
        for (BlockPos blockPos : blocks) {
            if ((theWorld.func_180495_p(blockPos).func_177230_c().func_149669_A() != theWorld.func_180495_p(blockPos).func_177230_c().func_149665_z() + 1.0 || theWorld.func_180495_p(blockPos).func_177230_c().func_149751_l() || theWorld.func_180495_p(blockPos).func_177230_c() == Blocks.field_150355_j || theWorld.func_180495_p(blockPos).func_177230_c() instanceof BlockSlab) && theWorld.func_180495_p(blockPos).func_177230_c() != Blocks.field_180401_cv) continue;
            return true;
        }
        return false;
    }
}

