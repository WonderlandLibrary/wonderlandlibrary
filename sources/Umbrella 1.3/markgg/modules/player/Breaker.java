/*
 * Decompiled with CFR 0.150.
 */
package markgg.modules.player;

import markgg.events.Event;
import markgg.events.listeners.EventUpdate;
import markgg.modules.Module;
import markgg.settings.BooleanSetting;
import markgg.settings.ModeSetting;
import markgg.settings.NumberSetting;
import net.minecraft.block.Block;
import net.minecraft.block.BlockBed;
import net.minecraft.block.BlockCake;
import net.minecraft.block.BlockDragonEgg;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;

public class Breaker
extends Module {
    public NumberSetting range = new NumberSetting("Range", 1.0, 1.0, 6.0, 1.0);
    public ModeSetting mode = new ModeSetting("Target", "Beds", "Beds", "Cakes", "Eggs");
    public BooleanSetting noSwing = new BooleanSetting("No Swing", false);

    public Breaker() {
        super("Breaker", 0, Module.Category.PLAYER);
        this.addSettings(this.range, this.mode, this.noSwing);
    }

    @Override
    public void onEvent(Event e) {
        if (e instanceof EventUpdate) {
            int n = (int)this.range.getValue();
            for (int i = -n; i < n; ++i) {
                for (int j = n; j > -n; --j) {
                    for (int k = -n; k < n; ++k) {
                        int n2 = (int)this.mc.thePlayer.posX + i;
                        int n3 = (int)this.mc.thePlayer.posY + j;
                        int n4 = (int)this.mc.thePlayer.posZ + k;
                        BlockPos blockPos = new BlockPos(n2, n3, n4);
                        Block block = this.mc.theWorld.getBlockState(blockPos).getBlock();
                        if (this.mode.is("Beds") && block instanceof BlockBed) {
                            if (this.noSwing.isEnabled()) {
                                this.mc.getNetHandler().addToSendQueue(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.START_DESTROY_BLOCK, blockPos, EnumFacing.DOWN));
                                this.mc.getNetHandler().addToSendQueue(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.STOP_DESTROY_BLOCK, blockPos, EnumFacing.DOWN));
                            }
                            if (!this.noSwing.isEnabled()) {
                                this.mc.thePlayer.swingItem();
                                this.mc.getNetHandler().addToSendQueue(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.START_DESTROY_BLOCK, blockPos, EnumFacing.DOWN));
                                this.mc.getNetHandler().addToSendQueue(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.STOP_DESTROY_BLOCK, blockPos, EnumFacing.DOWN));
                            }
                        }
                        if (this.mode.is("Cakes") && block instanceof BlockCake) {
                            if (this.noSwing.isEnabled()) {
                                this.mc.getNetHandler().addToSendQueue(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.START_DESTROY_BLOCK, blockPos, EnumFacing.DOWN));
                                this.mc.getNetHandler().addToSendQueue(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.STOP_DESTROY_BLOCK, blockPos, EnumFacing.DOWN));
                            }
                            if (!this.noSwing.isEnabled()) {
                                this.mc.thePlayer.swingItem();
                                this.mc.getNetHandler().addToSendQueue(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.START_DESTROY_BLOCK, blockPos, EnumFacing.DOWN));
                                this.mc.getNetHandler().addToSendQueue(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.STOP_DESTROY_BLOCK, blockPos, EnumFacing.DOWN));
                            }
                        }
                        if (!this.mode.is("Eggs") || !(block instanceof BlockDragonEgg)) continue;
                        if (this.noSwing.isEnabled()) {
                            this.mc.getNetHandler().addToSendQueue(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.START_DESTROY_BLOCK, blockPos, EnumFacing.DOWN));
                            this.mc.getNetHandler().addToSendQueue(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.STOP_DESTROY_BLOCK, blockPos, EnumFacing.DOWN));
                        }
                        if (this.noSwing.isEnabled()) continue;
                        this.mc.thePlayer.swingItem();
                        this.mc.getNetHandler().addToSendQueue(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.START_DESTROY_BLOCK, blockPos, EnumFacing.DOWN));
                        this.mc.getNetHandler().addToSendQueue(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.STOP_DESTROY_BLOCK, blockPos, EnumFacing.DOWN));
                    }
                }
            }
        }
    }
}

