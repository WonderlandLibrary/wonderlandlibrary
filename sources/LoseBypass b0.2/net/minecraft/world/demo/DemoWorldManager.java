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
    private boolean field_73105_c;
    private boolean demoTimeExpired;
    private int field_73104_e;
    private int field_73102_f;

    public DemoWorldManager(World worldIn) {
        super(worldIn);
    }

    @Override
    public void updateBlockRemoving() {
        super.updateBlockRemoving();
        ++this.field_73102_f;
        long i = this.theWorld.getTotalWorldTime();
        long j = i / 24000L + 1L;
        if (!this.field_73105_c && this.field_73102_f > 20) {
            this.field_73105_c = true;
            this.thisPlayerMP.playerNetServerHandler.sendPacket(new S2BPacketChangeGameState(5, 0.0f));
        }
        boolean bl = this.demoTimeExpired = i > 120500L;
        if (this.demoTimeExpired) {
            ++this.field_73104_e;
        }
        if (i % 24000L == 500L) {
            if (j > 6L) return;
            this.thisPlayerMP.addChatMessage(new ChatComponentTranslation("demo.day." + j, new Object[0]));
            return;
        }
        if (j != 1L) {
            if (j != 5L) return;
            if (i % 24000L != 22000L) return;
            this.thisPlayerMP.addChatMessage(new ChatComponentTranslation("demo.day.warning", new Object[0]));
            return;
        }
        if (i == 100L) {
            this.thisPlayerMP.playerNetServerHandler.sendPacket(new S2BPacketChangeGameState(5, 101.0f));
            return;
        }
        if (i == 175L) {
            this.thisPlayerMP.playerNetServerHandler.sendPacket(new S2BPacketChangeGameState(5, 102.0f));
            return;
        }
        if (i != 250L) return;
        this.thisPlayerMP.playerNetServerHandler.sendPacket(new S2BPacketChangeGameState(5, 103.0f));
    }

    private void sendDemoReminder() {
        if (this.field_73104_e <= 100) return;
        this.thisPlayerMP.addChatMessage(new ChatComponentTranslation("demo.reminder", new Object[0]));
        this.field_73104_e = 0;
    }

    @Override
    public void onBlockClicked(BlockPos pos, EnumFacing side) {
        if (this.demoTimeExpired) {
            this.sendDemoReminder();
            return;
        }
        super.onBlockClicked(pos, side);
    }

    @Override
    public void blockRemoving(BlockPos pos) {
        if (this.demoTimeExpired) return;
        super.blockRemoving(pos);
    }

    @Override
    public boolean tryHarvestBlock(BlockPos pos) {
        if (this.demoTimeExpired) {
            return false;
        }
        boolean bl = super.tryHarvestBlock(pos);
        return bl;
    }

    @Override
    public boolean tryUseItem(EntityPlayer player, World worldIn, ItemStack stack) {
        if (!this.demoTimeExpired) return super.tryUseItem(player, worldIn, stack);
        this.sendDemoReminder();
        return false;
    }

    @Override
    public boolean activateBlockOrUseItem(EntityPlayer player, World worldIn, ItemStack stack, BlockPos pos, EnumFacing side, float offsetX, float offsetY, float offsetZ) {
        if (!this.demoTimeExpired) return super.activateBlockOrUseItem(player, worldIn, stack, pos, side, offsetX, offsetY, offsetZ);
        this.sendDemoReminder();
        return false;
    }
}

