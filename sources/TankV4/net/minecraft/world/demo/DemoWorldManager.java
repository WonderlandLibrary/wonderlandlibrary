package net.minecraft.world.demo;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.server.S2BPacketChangeGameState;
import net.minecraft.server.management.ItemInWorldManager;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;

public class DemoWorldManager extends ItemInWorldManager {
   private boolean demoTimeExpired;
   private int field_73102_f;
   private boolean field_73105_c;
   private int field_73104_e;

   public DemoWorldManager(World var1) {
      super(var1);
   }

   public boolean activateBlockOrUseItem(EntityPlayer var1, World var2, ItemStack var3, BlockPos var4, EnumFacing var5, float var6, float var7, float var8) {
      if (this.demoTimeExpired) {
         this.sendDemoReminder();
         return false;
      } else {
         return super.activateBlockOrUseItem(var1, var2, var3, var4, var5, var6, var7, var8);
      }
   }

   public boolean tryUseItem(EntityPlayer var1, World var2, ItemStack var3) {
      if (this.demoTimeExpired) {
         this.sendDemoReminder();
         return false;
      } else {
         return super.tryUseItem(var1, var2, var3);
      }
   }

   public void blockRemoving(BlockPos var1) {
      if (!this.demoTimeExpired) {
         super.blockRemoving(var1);
      }

   }

   public void updateBlockRemoving() {
      super.updateBlockRemoving();
      ++this.field_73102_f;
      long var1 = this.theWorld.getTotalWorldTime();
      long var3 = var1 / 24000L + 1L;
      if (!this.field_73105_c && this.field_73102_f > 20) {
         this.field_73105_c = true;
         this.thisPlayerMP.playerNetServerHandler.sendPacket(new S2BPacketChangeGameState(5, 0.0F));
      }

      this.demoTimeExpired = var1 > 120500L;
      if (this.demoTimeExpired) {
         ++this.field_73104_e;
      }

      if (var1 % 24000L == 500L) {
         if (var3 <= 6L) {
            this.thisPlayerMP.addChatMessage(new ChatComponentTranslation("demo.day." + var3, new Object[0]));
         }
      } else if (var3 == 1L) {
         if (var1 == 100L) {
            this.thisPlayerMP.playerNetServerHandler.sendPacket(new S2BPacketChangeGameState(5, 101.0F));
         } else if (var1 == 175L) {
            this.thisPlayerMP.playerNetServerHandler.sendPacket(new S2BPacketChangeGameState(5, 102.0F));
         } else if (var1 == 250L) {
            this.thisPlayerMP.playerNetServerHandler.sendPacket(new S2BPacketChangeGameState(5, 103.0F));
         }
      } else if (var3 == 5L && var1 % 24000L == 22000L) {
         this.thisPlayerMP.addChatMessage(new ChatComponentTranslation("demo.day.warning", new Object[0]));
      }

   }

   private void sendDemoReminder() {
      if (this.field_73104_e > 100) {
         this.thisPlayerMP.addChatMessage(new ChatComponentTranslation("demo.reminder", new Object[0]));
         this.field_73104_e = 0;
      }

   }

   public void onBlockClicked(BlockPos var1, EnumFacing var2) {
      if (this.demoTimeExpired) {
         this.sendDemoReminder();
      } else {
         super.onBlockClicked(var1, var2);
      }

   }

   public boolean tryHarvestBlock(BlockPos var1) {
      return this.demoTimeExpired ? false : super.tryHarvestBlock(var1);
   }
}
