/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.server.management;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.server.SPacketChangeGameState;
import net.minecraft.server.management.PlayerInteractionManager;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;

public class DemoPlayerInteractionManager
extends PlayerInteractionManager {
    private boolean displayedIntro;
    private boolean demoTimeExpired;
    private int demoEndedReminder;
    private int gameModeTicks;

    public DemoPlayerInteractionManager(World worldIn) {
        super(worldIn);
    }

    @Override
    public void updateBlockRemoving() {
        super.updateBlockRemoving();
        ++this.gameModeTicks;
        long i = this.theWorld.getTotalWorldTime();
        long j = i / 24000L + 1L;
        if (!this.displayedIntro && this.gameModeTicks > 20) {
            this.displayedIntro = true;
            this.thisPlayerMP.connection.sendPacket(new SPacketChangeGameState(5, 0.0f));
        }
        boolean bl = this.demoTimeExpired = i > 120500L;
        if (this.demoTimeExpired) {
            ++this.demoEndedReminder;
        }
        if (i % 24000L == 500L) {
            if (j <= 6L) {
                this.thisPlayerMP.addChatMessage(new TextComponentTranslation("demo.day." + j, new Object[0]));
            }
        } else if (j == 1L) {
            if (i == 100L) {
                this.thisPlayerMP.connection.sendPacket(new SPacketChangeGameState(5, 101.0f));
            } else if (i == 175L) {
                this.thisPlayerMP.connection.sendPacket(new SPacketChangeGameState(5, 102.0f));
            } else if (i == 250L) {
                this.thisPlayerMP.connection.sendPacket(new SPacketChangeGameState(5, 103.0f));
            }
        } else if (j == 5L && i % 24000L == 22000L) {
            this.thisPlayerMP.addChatMessage(new TextComponentTranslation("demo.day.warning", new Object[0]));
        }
    }

    private void sendDemoReminder() {
        if (this.demoEndedReminder > 100) {
            this.thisPlayerMP.addChatMessage(new TextComponentTranslation("demo.reminder", new Object[0]));
            this.demoEndedReminder = 0;
        }
    }

    @Override
    public void onBlockClicked(BlockPos pos, EnumFacing side) {
        if (this.demoTimeExpired) {
            this.sendDemoReminder();
        } else {
            super.onBlockClicked(pos, side);
        }
    }

    @Override
    public void blockRemoving(BlockPos pos) {
        if (!this.demoTimeExpired) {
            super.blockRemoving(pos);
        }
    }

    @Override
    public boolean tryHarvestBlock(BlockPos pos) {
        return this.demoTimeExpired ? false : super.tryHarvestBlock(pos);
    }

    @Override
    public EnumActionResult processRightClick(EntityPlayer player, World worldIn, ItemStack stack, EnumHand hand) {
        if (this.demoTimeExpired) {
            this.sendDemoReminder();
            return EnumActionResult.PASS;
        }
        return super.processRightClick(player, worldIn, stack, hand);
    }

    @Override
    public EnumActionResult processRightClickBlock(EntityPlayer player, World worldIn, ItemStack stack, EnumHand hand, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ) {
        if (this.demoTimeExpired) {
            this.sendDemoReminder();
            return EnumActionResult.PASS;
        }
        return super.processRightClickBlock(player, worldIn, stack, hand, pos, facing, hitX, hitY, hitZ);
    }
}

