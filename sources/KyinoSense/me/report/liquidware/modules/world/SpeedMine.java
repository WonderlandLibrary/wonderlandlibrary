/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.block.state.IBlockState
 *  net.minecraft.entity.player.EntityPlayer
 *  net.minecraft.init.Blocks
 *  net.minecraft.network.play.client.C07PacketPlayerDigging
 *  net.minecraft.network.play.client.C07PacketPlayerDigging$Action
 *  net.minecraft.util.BlockPos
 *  net.minecraft.util.EnumFacing
 *  net.minecraft.world.World
 */
package me.report.liquidware.modules.world;

import net.ccbluex.liquidbounce.event.EventTarget;
import net.ccbluex.liquidbounce.event.MotionEvent;
import net.ccbluex.liquidbounce.event.PacketEvent;
import net.ccbluex.liquidbounce.features.module.Module;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.features.module.ModuleInfo;
import net.ccbluex.liquidbounce.utils.PacketUtils;
import net.ccbluex.liquidbounce.value.FloatValue;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import obfuscator.NativeMethod;

@ModuleInfo(name="SpeedMine", description="Speed Mine", category=ModuleCategory.WORLD)
public class SpeedMine
extends Module {
    private final FloatValue speed = new FloatValue("Speed", 1.5f, 1.0f, 3.0f);
    private EnumFacing facing;
    private BlockPos pos;
    private boolean boost;
    private float damage;

    @EventTarget
    @NativeMethod.Obfuscation(flags="+native,+tiger-black")
    public void onMotion(MotionEvent e) {
        SpeedMine.mc.field_71442_b.field_78781_i = 0;
        if (this.pos != null && this.boost) {
            IBlockState blockState = SpeedMine.mc.field_71441_e.func_180495_p(this.pos);
            if (blockState == null) {
                return;
            }
            try {
                this.damage += blockState.func_177230_c().func_180647_a((EntityPlayer)SpeedMine.mc.field_71439_g, (World)SpeedMine.mc.field_71441_e, this.pos) * ((Float)this.speed.get()).floatValue();
            }
            catch (Exception ex) {
                ex.printStackTrace();
                return;
            }
            if (this.damage >= 1.0f) {
                try {
                    SpeedMine.mc.field_71441_e.func_180501_a(this.pos, Blocks.field_150350_a.func_176223_P(), 11);
                }
                catch (Exception ex) {
                    ex.printStackTrace();
                    return;
                }
                PacketUtils.sendPacketNoEvent(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.STOP_DESTROY_BLOCK, this.pos, this.facing));
                this.damage = 0.0f;
                this.boost = false;
            }
        }
    }

    @EventTarget
    @NativeMethod.Obfuscation(flags="+native,+tiger-black")
    public void onPacket(PacketEvent e) {
        if (e.getPacket() instanceof C07PacketPlayerDigging) {
            C07PacketPlayerDigging packet = (C07PacketPlayerDigging)e.getPacket();
            if (packet.func_180762_c() == C07PacketPlayerDigging.Action.START_DESTROY_BLOCK) {
                this.boost = true;
                this.pos = packet.func_179715_a();
                this.facing = packet.func_179714_b();
                this.damage = 0.0f;
            } else if (packet.func_180762_c() == C07PacketPlayerDigging.Action.ABORT_DESTROY_BLOCK | packet.func_180762_c() == C07PacketPlayerDigging.Action.STOP_DESTROY_BLOCK) {
                this.boost = false;
                this.pos = null;
                this.facing = null;
            }
        }
    }
}

