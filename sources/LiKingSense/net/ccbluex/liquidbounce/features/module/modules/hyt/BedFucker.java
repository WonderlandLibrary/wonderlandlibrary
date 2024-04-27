/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.block.Block
 *  net.minecraft.network.Packet
 *  net.minecraft.network.play.client.CPacketPlayerDigging
 *  net.minecraft.network.play.client.CPacketPlayerDigging$Action
 *  net.minecraft.util.EnumFacing
 *  net.minecraft.util.math.BlockPos
 */
package net.ccbluex.liquidbounce.features.module.modules.hyt;

import net.ccbluex.liquidbounce.event.EventTarget;
import net.ccbluex.liquidbounce.event.UpdateEvent;
import net.ccbluex.liquidbounce.features.module.Module;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.features.module.ModuleInfo;
import net.ccbluex.liquidbounce.value.FloatValue;
import net.ccbluex.liquidbounce.value.ListValue;
import net.minecraft.block.Block;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketPlayerDigging;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;

@ModuleInfo(name="BedFucker", description="eee", category=ModuleCategory.RENDER)
public class BedFucker
extends Module {
    public final FloatValue radius = new FloatValue("Radius", 4.5f, 1.0f, 6.0f);
    public final ListValue modeValue = new ListValue("Break Target Mode", new String[]{"Cake", "Egg", "Bed"}, "Bed");

    @EventTarget
    public void onUpdate(UpdateEvent event) {
        for (int x = -((Float)this.radius.getValue()).intValue(); x < ((Float)this.radius.getValue()).intValue(); ++x) {
            for (int y = ((Float)this.radius.getValue()).intValue(); y > -((Float)this.radius.getValue()).intValue(); --y) {
                for (int z = -((Float)this.radius.getValue()).intValue(); z < ((Float)this.radius.getValue()).intValue(); ++z) {
                    int zPos;
                    int yPos;
                    int xPos = (int)mc.getThePlayer().getPosX() + x;
                    BlockPos blockPos = new BlockPos(xPos, yPos = (int)mc.getThePlayer().getPosY() + y, zPos = (int)mc.getThePlayer().getPosZ() + z);
                    Block block = BedFucker.mc2.field_71441_e.func_180495_p(blockPos).func_177230_c();
                    if (block.func_176194_O().func_177622_c() == Block.func_149729_e((int)92) && this.modeValue.getValue() == "Cake") {
                        BedFucker.mc2.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketPlayerDigging(CPacketPlayerDigging.Action.START_DESTROY_BLOCK, blockPos, EnumFacing.NORTH));
                        BedFucker.mc2.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketPlayerDigging(CPacketPlayerDigging.Action.STOP_DESTROY_BLOCK, blockPos, EnumFacing.NORTH));
                        mc.getThePlayer().swingItem();
                        continue;
                    }
                    if (block.func_176194_O().func_177622_c() == Block.func_149729_e((int)122) && this.modeValue.getValue() == "Egg") {
                        BedFucker.mc2.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketPlayerDigging(CPacketPlayerDigging.Action.START_DESTROY_BLOCK, blockPos, EnumFacing.NORTH));
                        BedFucker.mc2.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketPlayerDigging(CPacketPlayerDigging.Action.STOP_DESTROY_BLOCK, blockPos, EnumFacing.NORTH));
                        mc.getThePlayer().swingItem();
                        continue;
                    }
                    if (block.func_176194_O().func_177622_c() != Block.func_149729_e((int)121) || this.modeValue.getValue() != "Bed") continue;
                    BedFucker.mc2.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketPlayerDigging(CPacketPlayerDigging.Action.START_DESTROY_BLOCK, blockPos, EnumFacing.NORTH));
                    BedFucker.mc2.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketPlayerDigging(CPacketPlayerDigging.Action.STOP_DESTROY_BLOCK, blockPos, EnumFacing.NORTH));
                    mc.getThePlayer().swingItem();
                }
            }
        }
    }
}

