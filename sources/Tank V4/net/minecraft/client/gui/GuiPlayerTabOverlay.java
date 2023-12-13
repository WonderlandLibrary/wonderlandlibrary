package net.minecraft.client.gui;

import com.google.common.collect.ComparisonChain;
import com.google.common.collect.Ordering;
import com.mojang.authlib.GameProfile;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EnumPlayerModelParts;
import net.minecraft.scoreboard.IScoreObjectiveCriteria;
import net.minecraft.scoreboard.ScoreObjective;
import net.minecraft.scoreboard.ScorePlayerTeam;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IChatComponent;
import net.minecraft.util.MathHelper;
import net.minecraft.world.WorldSettings;

public class GuiPlayerTabOverlay extends Gui {
   private IChatComponent footer;
   private long lastTimeOpened;
   private IChatComponent header;
   private static final Ordering field_175252_a = Ordering.from((Comparator)(new GuiPlayerTabOverlay.PlayerComparator((GuiPlayerTabOverlay.PlayerComparator)null)));
   private boolean isBeingRendered;
   private final GuiIngame guiIngame;
   private final Minecraft mc;

   public void renderPlayerlist(int var1, Scoreboard var2, ScoreObjective var3) {
      NetHandlerPlayClient var4 = Minecraft.thePlayer.sendQueue;
      List var5 = field_175252_a.sortedCopy(var4.getPlayerInfoMap());
      int var6 = 0;
      int var7 = 0;
      Iterator var9 = var5.iterator();

      int var10;
      while(var9.hasNext()) {
         NetworkPlayerInfo var8 = (NetworkPlayerInfo)var9.next();
         var10 = Minecraft.fontRendererObj.getStringWidth(this.getPlayerName(var8));
         var6 = Math.max(var6, var10);
         if (var3 != null && var3.getRenderType() != IScoreObjectiveCriteria.EnumRenderType.HEARTS) {
            var10 = Minecraft.fontRendererObj.getStringWidth(" " + var2.getValueFromObjective(var8.getGameProfile().getName(), var3).getScorePoints());
            var7 = Math.max(var7, var10);
         }
      }

      var5 = var5.subList(0, Math.min(var5.size(), 80));
      int var33 = var5.size();
      int var34 = var33;

      for(var10 = 1; var34 > 20; var34 = (var33 + var10 - 1) / var10) {
         ++var10;
      }

      boolean var11 = this.mc.isIntegratedServerRunning() || this.mc.getNetHandler().getNetworkManager().getIsencrypted();
      int var12;
      if (var3 != null) {
         if (var3.getRenderType() == IScoreObjectiveCriteria.EnumRenderType.HEARTS) {
            var12 = 90;
         } else {
            var12 = var7;
         }
      } else {
         var12 = 0;
      }

      int var13 = Math.min(var10 * ((var11 ? 9 : 0) + var6 + var12 + 13), var1 - 50) / var10;
      int var14 = var1 / 2 - (var13 * var10 + (var10 - 1) * 5) / 2;
      int var15 = 10;
      int var16 = var13 * var10 + (var10 - 1) * 5;
      List var17 = null;
      List var18 = null;
      String var19;
      Iterator var20;
      if (this.header != null) {
         var17 = Minecraft.fontRendererObj.listFormattedStringToWidth(this.header.getFormattedText(), var1 - 50);

         for(var20 = var17.iterator(); var20.hasNext(); var16 = Math.max(var16, Minecraft.fontRendererObj.getStringWidth(var19))) {
            var19 = (String)var20.next();
         }
      }

      if (this.footer != null) {
         var18 = Minecraft.fontRendererObj.listFormattedStringToWidth(this.footer.getFormattedText(), var1 - 50);

         for(var20 = var18.iterator(); var20.hasNext(); var16 = Math.max(var16, Minecraft.fontRendererObj.getStringWidth(var19))) {
            var19 = (String)var20.next();
         }
      }

      int var21;
      if (var17 != null) {
         drawRect((double)(var1 / 2 - var16 / 2 - 1), (double)(var15 - 1), (double)(var1 / 2 + var16 / 2 + 1), (double)(var15 + var17.size() * Minecraft.fontRendererObj.FONT_HEIGHT), Integer.MIN_VALUE);

         for(var20 = var17.iterator(); var20.hasNext(); var15 += Minecraft.fontRendererObj.FONT_HEIGHT) {
            var19 = (String)var20.next();
            var21 = Minecraft.fontRendererObj.getStringWidth(var19);
            Minecraft.fontRendererObj.drawStringWithShadow(var19, (float)(var1 / 2 - var21 / 2), (float)var15, -1);
         }

         ++var15;
      }

      drawRect((double)(var1 / 2 - var16 / 2 - 1), (double)(var15 - 1), (double)(var1 / 2 + var16 / 2 + 1), (double)(var15 + var34 * 9), Integer.MIN_VALUE);

      for(int var35 = 0; var35 < var33; ++var35) {
         int var36 = var35 / var34;
         var21 = var35 % var34;
         int var22 = var14 + var36 * var13 + var36 * 5;
         int var23 = var15 + var21 * 9;
         drawRect((double)var22, (double)var23, (double)(var22 + var13), (double)(var23 + 8), 553648127);
         GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
         GlStateManager.enableAlpha();
         GlStateManager.enableBlend();
         GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
         if (var35 < var5.size()) {
            NetworkPlayerInfo var24 = (NetworkPlayerInfo)var5.get(var35);
            String var25 = this.getPlayerName(var24);
            GameProfile var26 = var24.getGameProfile();
            if (var11) {
               EntityPlayer var27 = Minecraft.theWorld.getPlayerEntityByUUID(var26.getId());
               boolean var28 = var27 != null && var27.isWearing(EnumPlayerModelParts.CAPE) && (var26.getName().equals("Dinnerbone") || var26.getName().equals("Grumm"));
               this.mc.getTextureManager().bindTexture(var24.getLocationSkin());
               int var29 = 8 + (var28 ? 8 : 0);
               int var30 = 8 * (var28 ? -1 : 1);
               Gui.drawScaledCustomSizeModalRect((double)var22, (double)var23, 8.0F, (float)var29, 8, var30, 8, 8, 64.0F, 64.0F);
               if (var27 != null && var27.isWearing(EnumPlayerModelParts.HAT)) {
                  int var31 = 8 + (var28 ? 8 : 0);
                  int var32 = 8 * (var28 ? -1 : 1);
                  Gui.drawScaledCustomSizeModalRect((double)var22, (double)var23, 40.0F, (float)var31, 8, var32, 8, 8, 64.0F, 64.0F);
               }

               var22 += 9;
            }

            if (var24.getGameType() == WorldSettings.GameType.SPECTATOR) {
               var25 = EnumChatFormatting.ITALIC + var25;
               Minecraft.fontRendererObj.drawStringWithShadow(var25, (float)var22, (float)var23, -1862270977);
            } else {
               Minecraft.fontRendererObj.drawStringWithShadow(var25, (float)var22, (float)var23, -1);
            }

            if (var3 != null && var24.getGameType() != WorldSettings.GameType.SPECTATOR) {
               int var37 = var22 + var6 + 1;
               int var38 = var37 + var12;
               if (var38 - var37 > 5) {
                  this.drawScoreboardValues(var3, var23, var26.getName(), var37, var38, var24);
               }
            }

            this.drawPing(var13, var22 - (var11 ? 9 : 0), var23, var24);
         }
      }

      if (var18 != null) {
         var15 = var15 + var34 * 9 + 1;
         drawRect((double)(var1 / 2 - var16 / 2 - 1), (double)(var15 - 1), (double)(var1 / 2 + var16 / 2 + 1), (double)(var15 + var18.size() * Minecraft.fontRendererObj.FONT_HEIGHT), Integer.MIN_VALUE);

         for(var20 = var18.iterator(); var20.hasNext(); var15 += Minecraft.fontRendererObj.FONT_HEIGHT) {
            var19 = (String)var20.next();
            var21 = Minecraft.fontRendererObj.getStringWidth(var19);
            Minecraft.fontRendererObj.drawStringWithShadow(var19, (float)(var1 / 2 - var21 / 2), (float)var15, -1);
         }
      }

   }

   public GuiPlayerTabOverlay(Minecraft var1, GuiIngame var2) {
      this.mc = var1;
      this.guiIngame = var2;
   }

   protected void drawPing(int var1, int var2, int var3, NetworkPlayerInfo var4) {
      GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
      this.mc.getTextureManager().bindTexture(icons);
      byte var5 = 0;
      boolean var6 = false;
      byte var7;
      if (var4.getResponseTime() < 0) {
         var7 = 5;
      } else if (var4.getResponseTime() < 150) {
         var7 = 0;
      } else if (var4.getResponseTime() < 300) {
         var7 = 1;
      } else if (var4.getResponseTime() < 600) {
         var7 = 2;
      } else if (var4.getResponseTime() < 1000) {
         var7 = 3;
      } else {
         var7 = 4;
      }

      zLevel += 100.0F;
      drawTexturedModalRect(var2 + var1 - 11, var3, 0 + var5 * 10, 176 + var7 * 8, 10, 8);
      zLevel -= 100.0F;
   }

   private void drawScoreboardValues(ScoreObjective var1, int var2, String var3, int var4, int var5, NetworkPlayerInfo var6) {
      int var7 = var1.getScoreboard().getValueFromObjective(var3, var1).getScorePoints();
      if (var1.getRenderType() == IScoreObjectiveCriteria.EnumRenderType.HEARTS) {
         this.mc.getTextureManager().bindTexture(icons);
         if (this.lastTimeOpened == var6.func_178855_p()) {
            if (var7 < var6.func_178835_l()) {
               var6.func_178846_a(Minecraft.getSystemTime());
               var6.func_178844_b((long)(this.guiIngame.getUpdateCounter() + 20));
            } else if (var7 > var6.func_178835_l()) {
               var6.func_178846_a(Minecraft.getSystemTime());
               var6.func_178844_b((long)(this.guiIngame.getUpdateCounter() + 10));
            }
         }

         if (Minecraft.getSystemTime() - var6.func_178847_n() > 1000L || this.lastTimeOpened != var6.func_178855_p()) {
            var6.func_178836_b(var7);
            var6.func_178857_c(var7);
            var6.func_178846_a(Minecraft.getSystemTime());
         }

         var6.func_178843_c(this.lastTimeOpened);
         var6.func_178836_b(var7);
         int var8 = MathHelper.ceiling_float_int((float)Math.max(var7, var6.func_178860_m()) / 2.0F);
         int var9 = Math.max(MathHelper.ceiling_float_int((float)(var7 / 2)), Math.max(MathHelper.ceiling_float_int((float)(var6.func_178860_m() / 2)), 10));
         boolean var10 = var6.func_178858_o() > (long)this.guiIngame.getUpdateCounter() && (var6.func_178858_o() - (long)this.guiIngame.getUpdateCounter()) / 3L % 2L == 1L;
         if (var8 > 0) {
            float var11 = Math.min((float)(var5 - var4 - 4) / (float)var9, 9.0F);
            if (var11 > 3.0F) {
               int var12;
               for(var12 = var8; var12 < var9; ++var12) {
                  drawTexturedModalRect((float)var4 + (float)var12 * var11, (float)var2, var10 ? 25 : 16, 0, 9, 9);
               }

               for(var12 = 0; var12 < var8; ++var12) {
                  drawTexturedModalRect((float)var4 + (float)var12 * var11, (float)var2, var10 ? 25 : 16, 0, 9, 9);
                  if (var10) {
                     if (var12 * 2 + 1 < var6.func_178860_m()) {
                        drawTexturedModalRect((float)var4 + (float)var12 * var11, (float)var2, 70, 0, 9, 9);
                     }

                     if (var12 * 2 + 1 == var6.func_178860_m()) {
                        drawTexturedModalRect((float)var4 + (float)var12 * var11, (float)var2, 79, 0, 9, 9);
                     }
                  }

                  if (var12 * 2 + 1 < var7) {
                     drawTexturedModalRect((float)var4 + (float)var12 * var11, (float)var2, var12 >= 10 ? 160 : 52, 0, 9, 9);
                  }

                  if (var12 * 2 + 1 == var7) {
                     drawTexturedModalRect((float)var4 + (float)var12 * var11, (float)var2, var12 >= 10 ? 169 : 61, 0, 9, 9);
                  }
               }
            } else {
               float var17 = MathHelper.clamp_float((float)var7 / 20.0F, 0.0F, 1.0F);
               int var13 = (int)((1.0F - var17) * 255.0F) << 16 | (int)(var17 * 255.0F) << 8;
               String var14 = "" + (float)var7 / 2.0F;
               if (var5 - Minecraft.fontRendererObj.getStringWidth(var14 + "hp") >= var4) {
                  var14 = var14 + "hp";
               }

               Minecraft.fontRendererObj.drawStringWithShadow(var14, (float)((var5 + var4) / 2 - Minecraft.fontRendererObj.getStringWidth(var14) / 2), (float)var2, var13);
            }
         }
      } else {
         String var16 = "" + EnumChatFormatting.YELLOW + var7;
         Minecraft.fontRendererObj.drawStringWithShadow(var16, (float)(var5 - Minecraft.fontRendererObj.getStringWidth(var16)), (float)var2, 16777215);
      }

   }

   public void updatePlayerList(boolean var1) {
      if (var1 && !this.isBeingRendered) {
         this.lastTimeOpened = Minecraft.getSystemTime();
      }

      this.isBeingRendered = var1;
   }

   public void setHeader(IChatComponent var1) {
      this.header = var1;
   }

   public void setFooter(IChatComponent var1) {
      this.footer = var1;
   }

   public void func_181030_a() {
      this.header = null;
      this.footer = null;
   }

   public String getPlayerName(NetworkPlayerInfo var1) {
      return var1.getDisplayName() != null ? var1.getDisplayName().getFormattedText() : ScorePlayerTeam.formatPlayerName(var1.getPlayerTeam(), var1.getGameProfile().getName());
   }

   static class PlayerComparator implements Comparator {
      private PlayerComparator() {
      }

      public int compare(Object var1, Object var2) {
         return this.compare((NetworkPlayerInfo)var1, (NetworkPlayerInfo)var2);
      }

      public int compare(NetworkPlayerInfo var1, NetworkPlayerInfo var2) {
         ScorePlayerTeam var3 = var1.getPlayerTeam();
         ScorePlayerTeam var4 = var2.getPlayerTeam();
         return ComparisonChain.start().compareTrueFirst(var1.getGameType() != WorldSettings.GameType.SPECTATOR, var2.getGameType() != WorldSettings.GameType.SPECTATOR).compare(var3 != null ? var3.getRegisteredName() : "", var4 != null ? var4.getRegisteredName() : "").compare(var1.getGameProfile().getName(), var2.getGameProfile().getName()).result();
      }

      PlayerComparator(GuiPlayerTabOverlay.PlayerComparator var1) {
         this();
      }
   }
}
