// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.world.demo;

import net.minecraft.item.ItemStack;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.BlockPos;
import net.minecraft.util.IChatComponent;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S2BPacketChangeGameState;
import net.minecraft.world.World;
import net.minecraft.server.management.ItemInWorldManager;

public class DemoWorldManager extends ItemInWorldManager
{
    private boolean field_73105_c;
    private boolean demoTimeExpired;
    private int field_73104_e;
    private int field_73102_f;
    private static final String __OBFID = "CL_00001429";
    
    public DemoWorldManager(final World worldIn) {
        super(worldIn);
    }
    
    @Override
    public void updateBlockRemoving() {
        super.updateBlockRemoving();
        ++this.field_73102_f;
        final long var1 = this.theWorld.getTotalWorldTime();
        final long var2 = var1 / 24000L + 1L;
        if (!this.field_73105_c && this.field_73102_f > 20) {
            this.field_73105_c = true;
            this.thisPlayerMP.playerNetServerHandler.sendPacket(new S2BPacketChangeGameState(5, 0.0f));
        }
        this.demoTimeExpired = (var1 > 120500L);
        if (this.demoTimeExpired) {
            ++this.field_73104_e;
        }
        if (var1 % 24000L == 500L) {
            if (var2 <= 6L) {
                this.thisPlayerMP.addChatMessage(new ChatComponentTranslation("demo.day." + var2, new Object[0]));
            }
        }
        else if (var2 == 1L) {
            if (var1 == 100L) {
                this.thisPlayerMP.playerNetServerHandler.sendPacket(new S2BPacketChangeGameState(5, 101.0f));
            }
            else if (var1 == 175L) {
                this.thisPlayerMP.playerNetServerHandler.sendPacket(new S2BPacketChangeGameState(5, 102.0f));
            }
            else if (var1 == 250L) {
                this.thisPlayerMP.playerNetServerHandler.sendPacket(new S2BPacketChangeGameState(5, 103.0f));
            }
        }
        else if (var2 == 5L && var1 % 24000L == 22000L) {
            this.thisPlayerMP.addChatMessage(new ChatComponentTranslation("demo.day.warning", new Object[0]));
        }
    }
    
    private void sendDemoReminder() {
        if (this.field_73104_e > 100) {
            this.thisPlayerMP.addChatMessage(new ChatComponentTranslation("demo.reminder", new Object[0]));
            this.field_73104_e = 0;
        }
    }
    
    @Override
    public void func_180784_a(final BlockPos p_180784_1_, final EnumFacing p_180784_2_) {
        if (this.demoTimeExpired) {
            this.sendDemoReminder();
        }
        else {
            super.func_180784_a(p_180784_1_, p_180784_2_);
        }
    }
    
    @Override
    public void func_180785_a(final BlockPos p_180785_1_) {
        if (!this.demoTimeExpired) {
            super.func_180785_a(p_180785_1_);
        }
    }
    
    @Override
    public boolean func_180237_b(final BlockPos p_180237_1_) {
        return !this.demoTimeExpired && super.func_180237_b(p_180237_1_);
    }
    
    @Override
    public boolean tryUseItem(final EntityPlayer p_73085_1_, final World worldIn, final ItemStack p_73085_3_) {
        if (this.demoTimeExpired) {
            this.sendDemoReminder();
            return false;
        }
        return super.tryUseItem(p_73085_1_, worldIn, p_73085_3_);
    }
    
    @Override
    public boolean func_180236_a(final EntityPlayer p_180236_1_, final World worldIn, final ItemStack p_180236_3_, final BlockPos p_180236_4_, final EnumFacing p_180236_5_, final float p_180236_6_, final float p_180236_7_, final float p_180236_8_) {
        if (this.demoTimeExpired) {
            this.sendDemoReminder();
            return false;
        }
        return super.func_180236_a(p_180236_1_, worldIn, p_180236_3_, p_180236_4_, p_180236_5_, p_180236_6_, p_180236_7_, p_180236_8_);
    }
}
