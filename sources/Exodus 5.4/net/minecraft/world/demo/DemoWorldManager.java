/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.world.demo;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.server.S2BPacketChangeGameState;
import net.minecraft.server.management.ItemInWorldManager;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;

public class DemoWorldManager
extends ItemInWorldManager {
    private int field_73104_e;
    private boolean field_73105_c;
    private int field_73102_f;
    private boolean demoTimeExpired;

    private void sendDemoReminder() {
        if (this.field_73104_e > 100) {
            this.thisPlayerMP.addChatMessage(new ChatComponentTranslation("demo.reminder", new Object[0]));
            this.field_73104_e = 0;
        }
    }

    public DemoWorldManager(World world) {
        super(world);
    }

    @Override
    public void onBlockClicked(BlockPos blockPos, EnumFacing enumFacing) {
        if (this.demoTimeExpired) {
            this.sendDemoReminder();
        } else {
            super.onBlockClicked(blockPos, enumFacing);
        }
    }

    @Override
    public boolean tryHarvestBlock(BlockPos blockPos) {
        return this.demoTimeExpired ? false : super.tryHarvestBlock(blockPos);
    }

    @Override
    public boolean activateBlockOrUseItem(EntityPlayer entityPlayer, World world, ItemStack itemStack, BlockPos blockPos, EnumFacing enumFacing, float f, float f2, float f3) {
        if (this.demoTimeExpired) {
            this.sendDemoReminder();
            return false;
        }
        return super.activateBlockOrUseItem(entityPlayer, world, itemStack, blockPos, enumFacing, f, f2, f3);
    }

    @Override
    public boolean tryUseItem(EntityPlayer entityPlayer, World world, ItemStack itemStack) {
        if (this.demoTimeExpired) {
            this.sendDemoReminder();
            return false;
        }
        return super.tryUseItem(entityPlayer, world, itemStack);
    }

    @Override
    public void updateBlockRemoving() {
        super.updateBlockRemoving();
        ++this.field_73102_f;
        long l = this.theWorld.getTotalWorldTime();
        long l2 = l / 24000L + 1L;
        if (!this.field_73105_c && this.field_73102_f > 20) {
            this.field_73105_c = true;
            this.thisPlayerMP.playerNetServerHandler.sendPacket(new S2BPacketChangeGameState(5, 0.0f));
        }
        boolean bl = this.demoTimeExpired = l > 120500L;
        if (this.demoTimeExpired) {
            ++this.field_73104_e;
        }
        if (l % 24000L == 500L) {
            if (l2 <= 6L) {
                this.thisPlayerMP.addChatMessage(new ChatComponentTranslation("demo.day." + l2, new Object[0]));
            }
        } else if (l2 == 1L) {
            if (l == 100L) {
                this.thisPlayerMP.playerNetServerHandler.sendPacket(new S2BPacketChangeGameState(5, 101.0f));
            } else if (l == 175L) {
                this.thisPlayerMP.playerNetServerHandler.sendPacket(new S2BPacketChangeGameState(5, 102.0f));
            } else if (l == 250L) {
                this.thisPlayerMP.playerNetServerHandler.sendPacket(new S2BPacketChangeGameState(5, 103.0f));
            }
        } else if (l2 == 5L && l % 24000L == 22000L) {
            this.thisPlayerMP.addChatMessage(new ChatComponentTranslation("demo.day.warning", new Object[0]));
        }
    }

    @Override
    public void blockRemoving(BlockPos blockPos) {
        if (!this.demoTimeExpired) {
            super.blockRemoving(blockPos);
        }
    }
}

