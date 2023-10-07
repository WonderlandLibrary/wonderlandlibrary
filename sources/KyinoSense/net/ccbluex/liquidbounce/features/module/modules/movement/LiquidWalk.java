/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.block.Block
 *  net.minecraft.block.BlockLiquid
 *  net.minecraft.block.material.Material
 *  net.minecraft.init.Blocks
 *  net.minecraft.network.play.client.C03PacketPlayer
 *  net.minecraft.util.AxisAlignedBB
 *  net.minecraft.util.BlockPos
 */
package net.ccbluex.liquidbounce.features.module.modules.movement;

import net.ccbluex.liquidbounce.event.BlockBBEvent;
import net.ccbluex.liquidbounce.event.EventTarget;
import net.ccbluex.liquidbounce.event.JumpEvent;
import net.ccbluex.liquidbounce.event.MoveEvent;
import net.ccbluex.liquidbounce.event.PacketEvent;
import net.ccbluex.liquidbounce.event.UpdateEvent;
import net.ccbluex.liquidbounce.features.module.Module;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.features.module.ModuleInfo;
import net.ccbluex.liquidbounce.utils.block.BlockUtils;
import net.ccbluex.liquidbounce.value.BoolValue;
import net.ccbluex.liquidbounce.value.FloatValue;
import net.ccbluex.liquidbounce.value.ListValue;
import net.minecraft.block.Block;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.material.Material;
import net.minecraft.init.Blocks;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;

@ModuleInfo(name="LiquidWalk", description="Allows you to walk on water.", category=ModuleCategory.MOVEMENT)
public class LiquidWalk
extends Module {
    public final ListValue modeValue = new ListValue("Mode", new String[]{"Vanilla", "NCP", "AAC", "AAC3.3.11", "AACFly", "Spartan", "Dolphin"}, "NCP");
    private final BoolValue noJumpValue = new BoolValue("NoJump", false);
    private final FloatValue aacFlyValue = new FloatValue("AACFlyMotion", 0.5f, 0.1f, 1.0f);
    private boolean nextTick;

    @EventTarget
    public void onUpdate(UpdateEvent event) {
        if (LiquidWalk.mc.field_71439_g == null || LiquidWalk.mc.field_71439_g.func_70093_af()) {
            return;
        }
        switch (((String)this.modeValue.get()).toLowerCase()) {
            case "ncp": 
            case "vanilla": {
                if (!BlockUtils.collideBlock(LiquidWalk.mc.field_71439_g.func_174813_aQ(), block -> block instanceof BlockLiquid) || !LiquidWalk.mc.field_71439_g.func_70055_a(Material.field_151579_a) || LiquidWalk.mc.field_71439_g.func_70093_af()) break;
                LiquidWalk.mc.field_71439_g.field_70181_x = 0.08;
                break;
            }
            case "aac": {
                BlockPos blockPos = LiquidWalk.mc.field_71439_g.func_180425_c().func_177977_b();
                if (!LiquidWalk.mc.field_71439_g.field_70122_E && BlockUtils.getBlock(blockPos) == Blocks.field_150355_j || LiquidWalk.mc.field_71439_g.func_70090_H()) {
                    if (!LiquidWalk.mc.field_71439_g.func_70051_ag()) {
                        LiquidWalk.mc.field_71439_g.field_70159_w *= 0.99999;
                        LiquidWalk.mc.field_71439_g.field_70181_x *= 0.0;
                        LiquidWalk.mc.field_71439_g.field_70179_y *= 0.99999;
                        if (LiquidWalk.mc.field_71439_g.field_70123_F) {
                            LiquidWalk.mc.field_71439_g.field_70181_x = (float)((int)(LiquidWalk.mc.field_71439_g.field_70163_u - (double)((int)(LiquidWalk.mc.field_71439_g.field_70163_u - 1.0)))) / 8.0f;
                        }
                    } else {
                        LiquidWalk.mc.field_71439_g.field_70159_w *= 0.99999;
                        LiquidWalk.mc.field_71439_g.field_70181_x *= 0.0;
                        LiquidWalk.mc.field_71439_g.field_70179_y *= 0.99999;
                        if (LiquidWalk.mc.field_71439_g.field_70123_F) {
                            LiquidWalk.mc.field_71439_g.field_70181_x = (float)((int)(LiquidWalk.mc.field_71439_g.field_70163_u - (double)((int)(LiquidWalk.mc.field_71439_g.field_70163_u - 1.0)))) / 8.0f;
                        }
                    }
                    if (LiquidWalk.mc.field_71439_g.field_70143_R >= 4.0f) {
                        LiquidWalk.mc.field_71439_g.field_70181_x = -0.004;
                    } else if (LiquidWalk.mc.field_71439_g.func_70090_H()) {
                        LiquidWalk.mc.field_71439_g.field_70181_x = 0.09;
                    }
                }
                if (LiquidWalk.mc.field_71439_g.field_70737_aN == 0) break;
                LiquidWalk.mc.field_71439_g.field_70122_E = false;
                break;
            }
            case "spartan": {
                if (!LiquidWalk.mc.field_71439_g.func_70090_H()) break;
                if (LiquidWalk.mc.field_71439_g.field_70123_F) {
                    LiquidWalk.mc.field_71439_g.field_70181_x += 0.15;
                    return;
                }
                Block block2 = BlockUtils.getBlock(new BlockPos(LiquidWalk.mc.field_71439_g.field_70165_t, LiquidWalk.mc.field_71439_g.field_70163_u + 1.0, LiquidWalk.mc.field_71439_g.field_70161_v));
                Block blockUp = BlockUtils.getBlock(new BlockPos(LiquidWalk.mc.field_71439_g.field_70165_t, LiquidWalk.mc.field_71439_g.field_70163_u + 1.1, LiquidWalk.mc.field_71439_g.field_70161_v));
                if (blockUp instanceof BlockLiquid) {
                    LiquidWalk.mc.field_71439_g.field_70181_x = 0.1;
                } else if (block2 instanceof BlockLiquid) {
                    LiquidWalk.mc.field_71439_g.field_70181_x = 0.0;
                }
                LiquidWalk.mc.field_71439_g.field_70122_E = true;
                LiquidWalk.mc.field_71439_g.field_70159_w *= 1.085;
                LiquidWalk.mc.field_71439_g.field_70179_y *= 1.085;
                break;
            }
            case "aac3.3.11": {
                if (!LiquidWalk.mc.field_71439_g.func_70090_H()) break;
                LiquidWalk.mc.field_71439_g.field_70159_w *= 1.17;
                LiquidWalk.mc.field_71439_g.field_70179_y *= 1.17;
                if (LiquidWalk.mc.field_71439_g.field_70123_F) {
                    LiquidWalk.mc.field_71439_g.field_70181_x = 0.24;
                    break;
                }
                if (LiquidWalk.mc.field_71441_e.func_180495_p(new BlockPos(LiquidWalk.mc.field_71439_g.field_70165_t, LiquidWalk.mc.field_71439_g.field_70163_u + 1.0, LiquidWalk.mc.field_71439_g.field_70161_v)).func_177230_c() == Blocks.field_150350_a) break;
                LiquidWalk.mc.field_71439_g.field_70181_x += 0.04;
                break;
            }
            case "dolphin": {
                if (!LiquidWalk.mc.field_71439_g.func_70090_H()) break;
                LiquidWalk.mc.field_71439_g.field_70181_x += (double)0.04f;
            }
        }
    }

    @EventTarget
    public void onMove(MoveEvent event) {
        if ("aacfly".equals(((String)this.modeValue.get()).toLowerCase()) && LiquidWalk.mc.field_71439_g.func_70090_H()) {
            event.setY(((Float)this.aacFlyValue.get()).floatValue());
            LiquidWalk.mc.field_71439_g.field_70181_x = ((Float)this.aacFlyValue.get()).floatValue();
        }
    }

    @EventTarget
    public void onBlockBB(BlockBBEvent event) {
        if (LiquidWalk.mc.field_71439_g == null || LiquidWalk.mc.field_71439_g.func_174813_aQ() == null) {
            return;
        }
        if (event.getBlock() instanceof BlockLiquid && !BlockUtils.collideBlock(LiquidWalk.mc.field_71439_g.func_174813_aQ(), block -> block instanceof BlockLiquid) && !LiquidWalk.mc.field_71439_g.func_70093_af()) {
            switch (((String)this.modeValue.get()).toLowerCase()) {
                case "ncp": 
                case "vanilla": {
                    event.setBoundingBox(AxisAlignedBB.func_178781_a((double)event.getX(), (double)event.getY(), (double)event.getZ(), (double)(event.getX() + 1), (double)(event.getY() + 1), (double)(event.getZ() + 1)));
                }
            }
        }
    }

    @EventTarget
    public void onPacket(PacketEvent event) {
        if (LiquidWalk.mc.field_71439_g == null || !((String)this.modeValue.get()).equalsIgnoreCase("NCP")) {
            return;
        }
        if (event.getPacket() instanceof C03PacketPlayer) {
            C03PacketPlayer packetPlayer = (C03PacketPlayer)event.getPacket();
            if (BlockUtils.collideBlock(new AxisAlignedBB(LiquidWalk.mc.field_71439_g.func_174813_aQ().field_72336_d, LiquidWalk.mc.field_71439_g.func_174813_aQ().field_72337_e, LiquidWalk.mc.field_71439_g.func_174813_aQ().field_72334_f, LiquidWalk.mc.field_71439_g.func_174813_aQ().field_72340_a, LiquidWalk.mc.field_71439_g.func_174813_aQ().field_72338_b - 0.01, LiquidWalk.mc.field_71439_g.func_174813_aQ().field_72339_c), block -> block instanceof BlockLiquid)) {
                boolean bl = this.nextTick = !this.nextTick;
                if (this.nextTick) {
                    packetPlayer.field_149477_b -= 0.001;
                }
            }
        }
    }

    @EventTarget
    public void onJump(JumpEvent event) {
        if (LiquidWalk.mc.field_71439_g == null) {
            return;
        }
        Block block = BlockUtils.getBlock(new BlockPos(LiquidWalk.mc.field_71439_g.field_70165_t, LiquidWalk.mc.field_71439_g.field_70163_u - 0.01, LiquidWalk.mc.field_71439_g.field_70161_v));
        if (((Boolean)this.noJumpValue.get()).booleanValue() && block instanceof BlockLiquid) {
            event.cancelEvent();
        }
    }

    @Override
    public String getTag() {
        return (String)this.modeValue.get();
    }
}

