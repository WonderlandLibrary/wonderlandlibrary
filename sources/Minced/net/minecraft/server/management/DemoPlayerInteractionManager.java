// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.server.management;

import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.item.ItemStack;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.SPacketChangeGameState;
import net.minecraft.world.World;

public class DemoPlayerInteractionManager extends PlayerInteractionManager
{
    private boolean displayedIntro;
    private boolean demoTimeExpired;
    private int demoEndedReminder;
    private int gameModeTicks;
    
    public DemoPlayerInteractionManager(final World worldIn) {
        super(worldIn);
    }
    
    @Override
    public void updateBlockRemoving() {
        super.updateBlockRemoving();
        ++this.gameModeTicks;
        final long i = this.world.getTotalWorldTime();
        final long j = i / 24000L + 1L;
        if (!this.displayedIntro && this.gameModeTicks > 20) {
            this.displayedIntro = true;
            this.player.connection.sendPacket(new SPacketChangeGameState(5, 0.0f));
        }
        this.demoTimeExpired = (i > 120500L);
        if (this.demoTimeExpired) {
            ++this.demoEndedReminder;
        }
        if (i % 24000L == 500L) {
            if (j <= 6L) {
                this.player.sendMessage(new TextComponentTranslation("demo.day." + j, new Object[0]));
            }
        }
        else if (j == 1L) {
            if (i == 100L) {
                this.player.connection.sendPacket(new SPacketChangeGameState(5, 101.0f));
            }
            else if (i == 175L) {
                this.player.connection.sendPacket(new SPacketChangeGameState(5, 102.0f));
            }
            else if (i == 250L) {
                this.player.connection.sendPacket(new SPacketChangeGameState(5, 103.0f));
            }
        }
        else if (j == 5L && i % 24000L == 22000L) {
            this.player.sendMessage(new TextComponentTranslation("demo.day.warning", new Object[0]));
        }
    }
    
    private void sendDemoReminder() {
        if (this.demoEndedReminder > 100) {
            this.player.sendMessage(new TextComponentTranslation("demo.reminder", new Object[0]));
            this.demoEndedReminder = 0;
        }
    }
    
    @Override
    public void onBlockClicked(final BlockPos pos, final EnumFacing side) {
        if (this.demoTimeExpired) {
            this.sendDemoReminder();
        }
        else {
            super.onBlockClicked(pos, side);
        }
    }
    
    @Override
    public void blockRemoving(final BlockPos pos) {
        if (!this.demoTimeExpired) {
            super.blockRemoving(pos);
        }
    }
    
    @Override
    public boolean tryHarvestBlock(final BlockPos pos) {
        return !this.demoTimeExpired && super.tryHarvestBlock(pos);
    }
    
    @Override
    public EnumActionResult processRightClick(final EntityPlayer player, final World worldIn, final ItemStack stack, final EnumHand hand) {
        if (this.demoTimeExpired) {
            this.sendDemoReminder();
            return EnumActionResult.PASS;
        }
        return super.processRightClick(player, worldIn, stack, hand);
    }
    
    @Override
    public EnumActionResult processRightClickBlock(final EntityPlayer player, final World worldIn, final ItemStack stack, final EnumHand hand, final BlockPos pos, final EnumFacing facing, final float hitX, final float hitY, final float hitZ) {
        if (this.demoTimeExpired) {
            this.sendDemoReminder();
            return EnumActionResult.PASS;
        }
        return super.processRightClickBlock(player, worldIn, stack, hand, pos, facing, hitX, hitY, hitZ);
    }
}
