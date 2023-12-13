package net.minecraft.client.gui;

import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Random;
import my.NewSnake.Tank.Ui.font.GlyphPageFontRenderer;
import my.NewSnake.Tank.module.modules.COMBAT.NoScoreBoard;
import my.NewSnake.Tank.module.modules.RENDER.Hotbar;
import my.NewSnake.event.events.Render2DEvent;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.boss.BossStatus;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.scoreboard.Score;
import net.minecraft.scoreboard.ScoreObjective;
import net.minecraft.scoreboard.ScorePlayerTeam;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.FoodStats;
import net.minecraft.util.IChatComponent;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StringUtils;
import net.minecraft.world.border.WorldBorder;
import optifine.Config;
import optifine.CustomColors;

public class GuiIngame extends Gui {
   private final GuiSpectator spectatorGui;
   private final GuiNewChat persistantChatGUI;
   private long lastSystemTime = 0L;
   private int field_175199_z;
   private int playerHealth = 0;
   private final GuiPlayerTabOverlay overlayPlayerList;
   private int remainingHighlightTicks;
   private String field_175200_y = "";
   private int lastPlayerHealth = 0;
   private long healthUpdateCounter = 0L;
   private final RenderItem itemRenderer;
   private static final ResourceLocation vignetteTexPath = new ResourceLocation("textures/misc/vignette.png");
   private int updateCounter;
   private String recordPlaying = "";
   private int recordPlayingUpFor;
   private int field_175193_B;
   private int field_175192_A;
   private static final ResourceLocation pumpkinBlurTexPath = new ResourceLocation("textures/misc/pumpkinblur.png");
   private final Minecraft mc;
   private ItemStack highlightingItemStack;
   private final GuiStreamIndicator streamIndicator;
   private static final ResourceLocation widgetsTexPath = new ResourceLocation("textures/gui/widgets.png");
   public float prevVignetteBrightness = 1.0F;
   private boolean recordIsPlaying;
   private String field_175201_x = "";
   private final GuiOverlayDebug overlayDebug;
   public GlyphPageFontRenderer clientFont;
   private int field_175195_w;
   private final Random rand = new Random();
   private static final String __OBFID = "CL_00000661";

   protected void renderTooltip(ScaledResolution var1, float var2) {
      if (this.mc.getRenderViewEntity() instanceof EntityPlayer) {
         GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
         this.mc.getTextureManager().bindTexture(widgetsTexPath);
         EntityPlayer var3 = (EntityPlayer)this.mc.getRenderViewEntity();
         int var4 = var1.getScaledWidth() / 2;
         float var5 = zLevel;
         zLevel = -90.0F;
         if (!Hotbar.Ativador) {
            drawTexturedModalRect(var4 - 91, ScaledResolution.getScaledHeight() - 22, 0, 0, 182, 22);
            drawTexturedModalRect(var4 - 91 - 1 + var3.inventory.currentItem * 20, ScaledResolution.getScaledHeight() - 22 - 1, 0, 22, 24, 22);
         }

         zLevel = var5;
         GlStateManager.enableRescaleNormal();
         GlStateManager.enableBlend();
         GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
         RenderHelper.enableGUIStandardItemLighting();

         for(int var6 = 0; var6 < 9; ++var6) {
            int var7 = var1.getScaledWidth() / 2 - 90 + var6 * 20 + 2;
            int var8 = ScaledResolution.getScaledHeight() - 16 - 3;
            this.renderHotbarItem(var6, var7, var8, var2, var3);
         }

         RenderHelper.disableStandardItemLighting();
         GlStateManager.disableRescaleNormal();
         GlStateManager.disableBlend();
      }

   }

   public void renderHorseJumpBar(ScaledResolution var1, int var2) {
      this.mc.mcProfiler.startSection("jumpBar");
      this.mc.getTextureManager().bindTexture(Gui.icons);
      float var3 = Minecraft.thePlayer.getHorseJumpPower();
      short var4 = 182;
      int var5 = (int)(var3 * (float)(var4 + 1));
      int var6 = ScaledResolution.getScaledHeight() - 32 + 3;
      drawTexturedModalRect(var2, var6, 0, 84, var4, 5);
      if (var5 > 0) {
         drawTexturedModalRect(var2, var6, 0, 89, var5, 5);
      }

      this.mc.mcProfiler.endSection();
   }

   public GuiIngame(Minecraft var1) {
      this.mc = var1;
      this.itemRenderer = var1.getRenderItem();
      this.overlayDebug = new GuiOverlayDebug(var1);
      this.spectatorGui = new GuiSpectator(var1);
      this.persistantChatGUI = new GuiNewChat(var1);
      this.streamIndicator = new GuiStreamIndicator(var1);
      this.overlayPlayerList = new GuiPlayerTabOverlay(var1, this);
      this.func_175177_a();
   }

   public GuiNewChat getChatGUI() {
      return this.persistantChatGUI;
   }

   public int getUpdateCounter() {
      return this.updateCounter;
   }

   private void renderVignette(float var1, ScaledResolution var2) {
      if (!Config.isVignetteEnabled()) {
         GlStateManager.enableDepth();
         GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
      } else {
         var1 = 1.0F - var1;
         var1 = MathHelper.clamp_float(var1, 0.0F, 1.0F);
         WorldBorder var3 = Minecraft.theWorld.getWorldBorder();
         float var4 = (float)var3.getClosestDistance(Minecraft.thePlayer);
         double var5 = Math.min(var3.getResizeSpeed() * (double)var3.getWarningTime() * 1000.0D, Math.abs(var3.getTargetSize() - var3.getDiameter()));
         double var7 = Math.max((double)var3.getWarningDistance(), var5);
         if ((double)var4 < var7) {
            var4 = 1.0F - (float)((double)var4 / var7);
         } else {
            var4 = 0.0F;
         }

         this.prevVignetteBrightness = (float)((double)this.prevVignetteBrightness + (double)(var1 - this.prevVignetteBrightness) * 0.01D);
         GlStateManager.disableDepth();
         GlStateManager.depthMask(false);
         GlStateManager.tryBlendFuncSeparate(0, 769, 1, 0);
         if (var4 > 0.0F) {
            GlStateManager.color(0.0F, var4, var4, 1.0F);
         } else {
            GlStateManager.color(this.prevVignetteBrightness, this.prevVignetteBrightness, this.prevVignetteBrightness, 1.0F);
         }

         this.mc.getTextureManager().bindTexture(vignetteTexPath);
         Tessellator var9 = Tessellator.getInstance();
         WorldRenderer var10 = var9.getWorldRenderer();
         var10.begin(7, DefaultVertexFormats.POSITION_TEX);
         var10.pos(0.0D, (double)ScaledResolution.getScaledHeight(), -90.0D).tex(0.0D, 1.0D).endVertex();
         var10.pos((double)var2.getScaledWidth(), (double)ScaledResolution.getScaledHeight(), -90.0D).tex(1.0D, 1.0D).endVertex();
         var10.pos((double)var2.getScaledWidth(), 0.0D, -90.0D).tex(1.0D, 0.0D).endVertex();
         var10.pos(0.0D, 0.0D, -90.0D).tex(0.0D, 0.0D).endVertex();
         var9.draw();
         GlStateManager.depthMask(true);
         GlStateManager.enableDepth();
         GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
         GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
      }

   }

   public GuiSpectator getSpectatorGui() {
      return this.spectatorGui;
   }

   public FontRenderer getFontRenderer() {
      return Minecraft.fontRendererObj;
   }

   public void renderDemo(ScaledResolution var1) {
      this.mc.mcProfiler.startSection("demo");
      String var2 = "";
      if (Minecraft.theWorld.getTotalWorldTime() >= 120500L) {
         var2 = I18n.format("demo.demoExpired");
      } else {
         var2 = I18n.format("demo.remainingTime", StringUtils.ticksToElapsedTime((int)(120500L - Minecraft.theWorld.getTotalWorldTime())));
      }

      int var3 = this.getFontRenderer().getStringWidth(var2);
      this.getFontRenderer().drawStringWithShadow(var2, (float)(var1.getScaledWidth() - var3 - 10), 5.0F, 16777215);
      this.mc.mcProfiler.endSection();
   }

   public void setRecordPlaying(IChatComponent var1, boolean var2) {
      this.setRecordPlaying(var1.getUnformattedText(), var2);
   }

   public void setRecordPlayingMessage(String var1) {
      this.setRecordPlaying(I18n.format("record.nowPlaying", var1), true);
   }

   public void renderStreamIndicator(ScaledResolution var1) {
      this.streamIndicator.render(var1.getScaledWidth() - 10, 10);
   }

   private void renderPumpkinOverlay(ScaledResolution var1) {
      GlStateManager.disableDepth();
      GlStateManager.depthMask(false);
      GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
      GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
      GlStateManager.disableAlpha();
      this.mc.getTextureManager().bindTexture(pumpkinBlurTexPath);
      Tessellator var2 = Tessellator.getInstance();
      WorldRenderer var3 = var2.getWorldRenderer();
      var3.begin(7, DefaultVertexFormats.POSITION_TEX);
      var3.pos(0.0D, (double)ScaledResolution.getScaledHeight(), -90.0D).tex(0.0D, 1.0D).endVertex();
      var3.pos((double)var1.getScaledWidth(), (double)ScaledResolution.getScaledHeight(), -90.0D).tex(1.0D, 1.0D).endVertex();
      var3.pos((double)var1.getScaledWidth(), 0.0D, -90.0D).tex(1.0D, 0.0D).endVertex();
      var3.pos(0.0D, 0.0D, -90.0D).tex(0.0D, 0.0D).endVertex();
      var2.draw();
      GlStateManager.depthMask(true);
      GlStateManager.enableDepth();
      GlStateManager.enableAlpha();
      GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
   }

   public void func_181029_i() {
      this.overlayPlayerList.func_181030_a();
   }

   public void func_175177_a() {
      this.field_175199_z = 10;
      this.field_175192_A = 70;
      this.field_175193_B = 20;
   }

   public void renderExpBar(ScaledResolution var1, int var2) {
      this.mc.mcProfiler.startSection("expBar");
      this.mc.getTextureManager().bindTexture(Gui.icons);
      int var3 = Minecraft.thePlayer.xpBarCap();
      int var6;
      if (var3 > 0) {
         short var4 = 182;
         int var5 = (int)(Minecraft.thePlayer.experience * (float)(var4 + 1));
         var6 = ScaledResolution.getScaledHeight() - 32 + 3;
         drawTexturedModalRect(var2, var6, 0, 64, var4, 5);
         if (var5 > 0) {
            drawTexturedModalRect(var2, var6, 0, 69, var5, 5);
         }
      }

      this.mc.mcProfiler.endSection();
      if (Minecraft.thePlayer.experienceLevel > 0) {
         this.mc.mcProfiler.startSection("expLevel");
         int var9 = 8453920;
         if (Config.isCustomColors()) {
            var9 = CustomColors.getExpBarTextColor(var9);
         }

         String var10 = "" + Minecraft.thePlayer.experienceLevel;
         var6 = (var1.getScaledWidth() - this.getFontRenderer().getStringWidth(var10)) / 2;
         int var7 = ScaledResolution.getScaledHeight() - 31 - 4;
         boolean var8 = false;
         this.getFontRenderer().drawString(var10, (double)(var6 + 1), (double)var7, 0);
         this.getFontRenderer().drawString(var10, (double)(var6 - 1), (double)var7, 0);
         this.getFontRenderer().drawString(var10, (double)var6, (double)(var7 + 1), 0);
         this.getFontRenderer().drawString(var10, (double)var6, (double)(var7 - 1), 0);
         this.getFontRenderer().drawString(var10, (double)var6, (double)var7, var9);
         this.mc.mcProfiler.endSection();
      }

   }

   private void func_180474_b(float var1, ScaledResolution var2) {
      if (var1 < 1.0F) {
         var1 *= var1;
         var1 *= var1;
         var1 = var1 * 0.8F + 0.2F;
      }

      GlStateManager.disableAlpha();
      GlStateManager.disableDepth();
      GlStateManager.depthMask(false);
      GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
      GlStateManager.color(1.0F, 1.0F, 1.0F, var1);
      this.mc.getTextureManager().bindTexture(TextureMap.locationBlocksTexture);
      TextureAtlasSprite var3 = this.mc.getBlockRendererDispatcher().getBlockModelShapes().getTexture(Blocks.portal.getDefaultState());
      float var4 = var3.getMinU();
      float var5 = var3.getMinV();
      float var6 = var3.getMaxU();
      float var7 = var3.getMaxV();
      Tessellator var8 = Tessellator.getInstance();
      WorldRenderer var9 = var8.getWorldRenderer();
      var9.begin(7, DefaultVertexFormats.POSITION_TEX);
      var9.pos(0.0D, (double)ScaledResolution.getScaledHeight(), -90.0D).tex((double)var4, (double)var7).endVertex();
      var9.pos((double)var2.getScaledWidth(), (double)ScaledResolution.getScaledHeight(), -90.0D).tex((double)var6, (double)var7).endVertex();
      var9.pos((double)var2.getScaledWidth(), 0.0D, -90.0D).tex((double)var6, (double)var5).endVertex();
      var9.pos(0.0D, 0.0D, -90.0D).tex((double)var4, (double)var5).endVertex();
      var8.draw();
      GlStateManager.depthMask(true);
      GlStateManager.enableDepth();
      GlStateManager.enableAlpha();
      GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
   }

   public void setRecordPlaying(String var1, boolean var2) {
      this.recordPlaying = var1;
      this.recordPlayingUpFor = 60;
      this.recordIsPlaying = var2;
   }

   private void renderPlayerStats(ScaledResolution var1) {
      if (this.mc.getRenderViewEntity() instanceof EntityPlayer) {
         EntityPlayer var2 = (EntityPlayer)this.mc.getRenderViewEntity();
         int var3 = MathHelper.ceiling_float_int(var2.getHealth());
         boolean var4 = this.healthUpdateCounter > (long)this.updateCounter && (this.healthUpdateCounter - (long)this.updateCounter) / 3L % 2L == 1L;
         if (var3 < this.playerHealth && var2.hurtResistantTime > 0) {
            this.lastSystemTime = Minecraft.getSystemTime();
            this.healthUpdateCounter = (long)(this.updateCounter + 20);
         } else if (var3 > this.playerHealth && var2.hurtResistantTime > 0) {
            this.lastSystemTime = Minecraft.getSystemTime();
            this.healthUpdateCounter = (long)(this.updateCounter + 10);
         }

         if (Minecraft.getSystemTime() - this.lastSystemTime > 1000L) {
            this.playerHealth = var3;
            this.lastPlayerHealth = var3;
            this.lastSystemTime = Minecraft.getSystemTime();
         }

         this.playerHealth = var3;
         int var5 = this.lastPlayerHealth;
         this.rand.setSeed((long)(this.updateCounter * 312871));
         boolean var6 = false;
         FoodStats var7 = var2.getFoodStats();
         int var8 = var7.getFoodLevel();
         int var9 = var7.getPrevFoodLevel();
         IAttributeInstance var10 = var2.getEntityAttribute(SharedMonsterAttributes.maxHealth);
         int var11 = var1.getScaledWidth() / 2 - 91;
         int var12 = var1.getScaledWidth() / 2 + 91;
         int var13 = ScaledResolution.getScaledHeight() - 39;
         float var14 = (float)var10.getAttributeValue();
         float var15 = var2.getAbsorptionAmount();
         int var16 = MathHelper.ceiling_float_int((var14 + var15) / 2.0F / 10.0F);
         int var17 = Math.max(10 - (var16 - 2), 3);
         int var18 = var13 - (var16 - 1) * var17 - 10;
         float var19 = var15;
         int var20 = var2.getTotalArmorValue();
         int var21 = -1;
         if (var2.isPotionActive(Potion.regeneration)) {
            var21 = this.updateCounter % MathHelper.ceiling_float_int(var14 + 5.0F);
         }

         this.mc.mcProfiler.startSection("armor");

         int var22;
         int var23;
         for(var22 = 0; var22 < 10; ++var22) {
            if (var20 > 0) {
               var23 = var11 + var22 * 8;
               if (var22 * 2 + 1 < var20) {
                  drawTexturedModalRect(var23, var18, 34, 9, 9, 9);
               }

               if (var22 * 2 + 1 == var20) {
                  drawTexturedModalRect(var23, var18, 25, 9, 9, 9);
               }

               if (var22 * 2 + 1 > var20) {
                  drawTexturedModalRect(var23, var18, 16, 9, 9, 9);
               }
            }
         }

         this.mc.mcProfiler.endStartSection("health");

         int var25;
         int var26;
         int var27;
         for(var22 = MathHelper.ceiling_float_int((var14 + var15) / 2.0F) - 1; var22 >= 0; --var22) {
            var23 = 16;
            if (var2.isPotionActive(Potion.poison)) {
               var23 += 36;
            } else if (var2.isPotionActive(Potion.wither)) {
               var23 += 72;
            }

            byte var24 = 0;
            if (var4) {
               var24 = 1;
            }

            var25 = MathHelper.ceiling_float_int((float)(var22 + 1) / 10.0F) - 1;
            var26 = var11 + var22 % 10 * 8;
            var27 = var13 - var25 * var17;
            if (var3 <= 4) {
               var27 += this.rand.nextInt(2);
            }

            if (var22 == var21) {
               var27 -= 2;
            }

            byte var28 = 0;
            if (var2.worldObj.getWorldInfo().isHardcoreModeEnabled()) {
               var28 = 5;
            }

            drawTexturedModalRect(var26, var27, 16 + var24 * 9, 9 * var28, 9, 9);
            if (var4) {
               if (var22 * 2 + 1 < var5) {
                  drawTexturedModalRect(var26, var27, var23 + 54, 9 * var28, 9, 9);
               }

               if (var22 * 2 + 1 == var5) {
                  drawTexturedModalRect(var26, var27, var23 + 63, 9 * var28, 9, 9);
               }
            }

            if (var19 <= 0.0F) {
               if (var22 * 2 + 1 < var3) {
                  drawTexturedModalRect(var26, var27, var23 + 36, 9 * var28, 9, 9);
               }

               if (var22 * 2 + 1 == var3) {
                  drawTexturedModalRect(var26, var27, var23 + 45, 9 * var28, 9, 9);
               }
            } else {
               if (var19 == var15 && var15 % 2.0F == 1.0F) {
                  drawTexturedModalRect(var26, var27, var23 + 153, 9 * var28, 9, 9);
               } else {
                  drawTexturedModalRect(var26, var27, var23 + 144, 9 * var28, 9, 9);
               }

               var19 -= 2.0F;
            }
         }

         Entity var34 = var2.ridingEntity;
         int var35;
         if (var34 == null) {
            this.mc.mcProfiler.endStartSection("food");

            for(var23 = 0; var23 < 10; ++var23) {
               var35 = var13;
               var25 = 16;
               byte var38 = 0;
               if (var2.isPotionActive(Potion.hunger)) {
                  var25 += 36;
                  var38 = 13;
               }

               if (var2.getFoodStats().getSaturationLevel() <= 0.0F && this.updateCounter % (var8 * 3 + 1) == 0) {
                  var35 = var13 + (this.rand.nextInt(3) - 1);
               }

               if (var6) {
                  var38 = 1;
               }

               var27 = var12 - var23 * 8 - 9;
               drawTexturedModalRect(var27, var35, 16 + var38 * 9, 27, 9, 9);
               if (var6) {
                  if (var23 * 2 + 1 < var9) {
                     drawTexturedModalRect(var27, var35, var25 + 54, 27, 9, 9);
                  }

                  if (var23 * 2 + 1 == var9) {
                     drawTexturedModalRect(var27, var35, var25 + 63, 27, 9, 9);
                  }
               }

               if (var23 * 2 + 1 < var8) {
                  drawTexturedModalRect(var27, var35, var25 + 36, 27, 9, 9);
               }

               if (var23 * 2 + 1 == var8) {
                  drawTexturedModalRect(var27, var35, var25 + 45, 27, 9, 9);
               }
            }
         } else if (var34 instanceof EntityLivingBase) {
            this.mc.mcProfiler.endStartSection("mountHealth");
            EntityLivingBase var37 = (EntityLivingBase)var34;
            var35 = (int)Math.ceil((double)var37.getHealth());
            float var36 = var37.getMaxHealth();
            var26 = (int)(var36 + 0.5F) / 2;
            if (var26 > 30) {
               var26 = 30;
            }

            var27 = var13;

            for(int var39 = 0; var26 > 0; var39 += 20) {
               int var29 = Math.min(var26, 10);
               var26 -= var29;

               for(int var30 = 0; var30 < var29; ++var30) {
                  byte var31 = 52;
                  byte var32 = 0;
                  if (var6) {
                     var32 = 1;
                  }

                  int var33 = var12 - var30 * 8 - 9;
                  drawTexturedModalRect(var33, var27, var31 + var32 * 9, 9, 9, 9);
                  if (var30 * 2 + 1 + var39 < var35) {
                     drawTexturedModalRect(var33, var27, var31 + 36, 9, 9, 9);
                  }

                  if (var30 * 2 + 1 + var39 == var35) {
                     drawTexturedModalRect(var33, var27, var31 + 45, 9, 9, 9);
                  }
               }

               var27 -= 10;
            }
         }

         this.mc.mcProfiler.endStartSection("air");
         if (var2.isInsideOfMaterial(Material.water)) {
            var23 = Minecraft.thePlayer.getAir();
            var35 = MathHelper.ceiling_double_int((double)(var23 - 2) * 10.0D / 300.0D);
            var25 = MathHelper.ceiling_double_int((double)var23 * 10.0D / 300.0D) - var35;

            for(var26 = 0; var26 < var35 + var25; ++var26) {
               if (var26 < var35) {
                  drawTexturedModalRect(var12 - var26 * 8 - 9, var18, 16, 18, 9, 9);
               } else {
                  drawTexturedModalRect(var12 - var26 * 8 - 9, var18, 25, 18, 9, 9);
               }
            }
         }

         this.mc.mcProfiler.endSection();
      }

   }

   public void renderGameOverlay(float var1) {
      ScaledResolution var2 = new ScaledResolution(this.mc);
      int var3 = var2.getScaledWidth();
      int var4 = ScaledResolution.getScaledHeight();
      this.mc.entityRenderer.setupOverlayRendering();
      GlStateManager.enableBlend();
      if (Config.isVignetteEnabled()) {
         this.renderVignette(Minecraft.thePlayer.getBrightness(var1), var2);
      } else {
         GlStateManager.enableDepth();
         GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
      }

      ItemStack var5 = Minecraft.thePlayer.inventory.armorItemInSlot(3);
      if (this.mc.gameSettings.thirdPersonView == 0 && var5 != null && var5.getItem() == Item.getItemFromBlock(Blocks.pumpkin)) {
         this.renderPumpkinOverlay(var2);
      }

      if (!Minecraft.thePlayer.isPotionActive(Potion.confusion)) {
         float var6 = Minecraft.thePlayer.prevTimeInPortal + (Minecraft.thePlayer.timeInPortal - Minecraft.thePlayer.prevTimeInPortal) * var1;
         if (var6 > 0.0F) {
            this.func_180474_b(var6, var2);
         }
      }

      if (Minecraft.playerController.isSpectator()) {
         this.spectatorGui.renderTooltip(var2, var1);
      } else {
         this.renderTooltip(var2, var1);
      }

      GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
      this.mc.getTextureManager().bindTexture(icons);
      GlStateManager.enableBlend();
      if (this != false && this.mc.gameSettings.thirdPersonView < 1) {
         GlStateManager.tryBlendFuncSeparate(775, 769, 1, 0);
         GlStateManager.enableAlpha();
         drawTexturedModalRect(var3 / 2 - 7, var4 / 2 - 7, 0, 0, 16, 16);
      }

      GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
      this.mc.mcProfiler.startSection("bossHealth");
      this.renderBossHealth();
      this.mc.mcProfiler.endSection();
      if (Minecraft.playerController.shouldDrawHUD()) {
         this.renderPlayerStats(var2);
      }

      GlStateManager.disableBlend();
      float var7;
      int var8;
      int var12;
      if (Minecraft.thePlayer.getSleepTimer() > 0) {
         this.mc.mcProfiler.startSection("sleep");
         GlStateManager.disableDepth();
         GlStateManager.disableAlpha();
         var12 = Minecraft.thePlayer.getSleepTimer();
         var7 = (float)var12 / 100.0F;
         if (var7 > 1.0F) {
            var7 = 1.0F - (float)(var12 - 100) / 10.0F;
         }

         var8 = (int)(220.0F * var7) << 24 | 1052704;
         drawRect(0.0D, 0.0D, (double)var3, (double)var4, var8);
         GlStateManager.enableAlpha();
         GlStateManager.enableDepth();
         this.mc.mcProfiler.endSection();
      }

      GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
      var12 = var3 / 2 - 91;
      if (Minecraft.thePlayer.isRidingHorse()) {
         this.renderHorseJumpBar(var2, var12);
      } else if (Minecraft.playerController.gameIsSurvivalOrAdventure()) {
         this.renderExpBar(var2, var12);
      }

      if (this.mc.gameSettings.heldItemTooltips && !Minecraft.playerController.isSpectator()) {
         this.func_181551_a(var2);
      } else if (Minecraft.thePlayer.isSpectator()) {
         this.spectatorGui.func_175263_a(var2);
      }

      if (this.mc.isDemo()) {
         this.renderDemo(var2);
      }

      if (this.mc.gameSettings.showDebugInfo) {
         this.overlayDebug.renderDebugInfo(var2);
      }

      int var9;
      if (this.recordPlayingUpFor > 0) {
         this.mc.mcProfiler.startSection("overlayMessage");
         var7 = (float)this.recordPlayingUpFor - var1;
         var8 = (int)(var7 * 255.0F / 20.0F);
         if (var8 > 255) {
            var8 = 255;
         }

         if (var8 > 8) {
            GlStateManager.pushMatrix();
            GlStateManager.translate((float)(var3 / 2), (float)(var4 - 68), 0.0F);
            GlStateManager.enableBlend();
            GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
            var9 = 16777215;
            if (this.recordIsPlaying) {
               var9 = MathHelper.func_181758_c(var7 / 50.0F, 0.7F, 0.6F) & 16777215;
            }

            this.getFontRenderer().drawString(this.recordPlaying, (double)(-this.getFontRenderer().getStringWidth(this.recordPlaying) / 2), -4.0D, var9 + (var8 << 24 & -16777216));
            GlStateManager.disableBlend();
            GlStateManager.popMatrix();
         }

         this.mc.mcProfiler.endSection();
      }

      if (this.field_175195_w > 0) {
         this.mc.mcProfiler.startSection("titleAndSubtitle");
         var7 = (float)this.field_175195_w - var1;
         var8 = 255;
         if (this.field_175195_w > this.field_175193_B + this.field_175192_A) {
            float var14 = (float)(this.field_175199_z + this.field_175192_A + this.field_175193_B) - var7;
            var8 = (int)(var14 * 255.0F / (float)this.field_175199_z);
         }

         if (this.field_175195_w <= this.field_175193_B) {
            var8 = (int)(var7 * 255.0F / (float)this.field_175193_B);
         }

         var8 = MathHelper.clamp_int(var8, 0, 255);
         if (var8 > 8) {
            GlStateManager.pushMatrix();
            GlStateManager.translate((float)(var3 / 2), (float)(var4 / 2), 0.0F);
            GlStateManager.enableBlend();
            GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
            GlStateManager.pushMatrix();
            GlStateManager.scale(4.0F, 4.0F, 4.0F);
            var9 = var8 << 24 & -16777216;
            this.getFontRenderer().drawString(this.field_175201_x, (float)(-this.getFontRenderer().getStringWidth(this.field_175201_x) / 2), -10.0F, 16777215 | var9, true);
            GlStateManager.popMatrix();
            GlStateManager.pushMatrix();
            GlStateManager.scale(2.0F, 2.0F, 2.0F);
            this.getFontRenderer().drawString(this.field_175200_y, (float)(-this.getFontRenderer().getStringWidth(this.field_175200_y) / 2), 5.0F, 16777215 | var9, true);
            GlStateManager.popMatrix();
            GlStateManager.disableBlend();
            GlStateManager.popMatrix();
         }

         this.mc.mcProfiler.endSection();
      }

      Scoreboard var13 = Minecraft.theWorld.getScoreboard();
      ScoreObjective var17 = null;
      ScorePlayerTeam var16 = var13.getPlayersTeam(Minecraft.thePlayer.getName());
      if (var16 != null) {
         int var10 = var16.getChatFormat().getColorIndex();
         if (var10 >= 0) {
            var17 = var13.getObjectiveInDisplaySlot(3 + var10);
         }
      }

      ScoreObjective var15 = var17 != null ? var17 : var13.getObjectiveInDisplaySlot(1);
      if (var15 != null) {
         this.renderScoreboard(var15, var2);
      }

      GlStateManager.enableBlend();
      GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
      GlStateManager.disableAlpha();
      GlStateManager.pushMatrix();
      GlStateManager.translate(0.0F, (float)(var4 - 48), 0.0F);
      this.mc.mcProfiler.startSection("chat");
      this.persistantChatGUI.drawChat(this.updateCounter);
      this.mc.mcProfiler.endSection();
      GlStateManager.popMatrix();
      var15 = var13.getObjectiveInDisplaySlot(0);
      if (this.mc.gameSettings.keyBindPlayerList.isKeyDown() && (!this.mc.isIntegratedServerRunning() || Minecraft.thePlayer.sendQueue.getPlayerInfoMap().size() > 1 || var15 != null)) {
         this.overlayPlayerList.updatePlayerList(true);
         this.overlayPlayerList.renderPlayerlist(var3, var13, var15);
      } else {
         this.overlayPlayerList.updatePlayerList(false);
      }

      Render2DEvent var11 = new Render2DEvent(var2.getScaledWidth(), ScaledResolution.getScaledHeight(), var2, var1);
      var11.call();
      GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
      GlStateManager.disableLighting();
      GlStateManager.enableAlpha();
   }

   public void updateTick() {
      if (this.recordPlayingUpFor > 0) {
         --this.recordPlayingUpFor;
      }

      if (this.field_175195_w > 0) {
         --this.field_175195_w;
         if (this.field_175195_w <= 0) {
            this.field_175201_x = "";
            this.field_175200_y = "";
         }
      }

      ++this.updateCounter;
      this.streamIndicator.func_152439_a();
      if (Minecraft.thePlayer != null) {
         ItemStack var1 = Minecraft.thePlayer.inventory.getCurrentItem();
         if (var1 == null) {
            this.remainingHighlightTicks = 0;
         } else if (this.highlightingItemStack != null && var1.getItem() == this.highlightingItemStack.getItem() && ItemStack.areItemStackTagsEqual(var1, this.highlightingItemStack) && (var1.isItemStackDamageable() || var1.getMetadata() == this.highlightingItemStack.getMetadata())) {
            if (this.remainingHighlightTicks > 0) {
               --this.remainingHighlightTicks;
            }
         } else {
            this.remainingHighlightTicks = 40;
         }

         this.highlightingItemStack = var1;
      }

   }

   private void renderScoreboard(ScoreObjective var1, ScaledResolution var2) {
      if (!(new NoScoreBoard()).getInstance().isEnabled()) {
         Scoreboard var3 = var1.getScoreboard();
         Collection var4 = var3.getSortedScores(var1);
         ArrayList var5 = Lists.newArrayList(Iterables.filter(var4, (Predicate)(new Predicate(this) {
            final GuiIngame this$0;
            private static final String __OBFID = "CL_00001958";

            {
               this.this$0 = var1;
            }

            public boolean apply(Object var1) {
               return this.apply((Score)var1);
            }

            public boolean apply(Score var1) {
               return var1.getPlayerName() != null && !var1.getPlayerName().startsWith("#");
            }
         })));
         ArrayList var6;
         if (var5.size() > 15) {
            var6 = Lists.newArrayList(Iterables.skip(var5, var4.size() - 15));
         } else {
            var6 = var5;
         }

         int var7 = this.getFontRenderer().getStringWidth(var1.getDisplayName());

         String var11;
         for(Iterator var9 = var6.iterator(); var9.hasNext(); var7 = Math.max(var7, this.getFontRenderer().getStringWidth(var11))) {
            Object var8 = var9.next();
            ScorePlayerTeam var10 = var3.getPlayersTeam(((Score)var8).getPlayerName());
            var11 = ScorePlayerTeam.formatPlayerName(var10, ((Score)var8).getPlayerName()) + ": " + EnumChatFormatting.RED + ((Score)var8).getScorePoints();
         }

         int var21 = var6.size() * this.getFontRenderer().FONT_HEIGHT;
         int var22 = ScaledResolution.getScaledHeight() / 2 + var21 / 3;
         byte var23 = 3;
         int var24 = var2.getScaledWidth() - var7 - var23;
         int var12 = 0;
         Iterator var14 = var6.iterator();

         while(var14.hasNext()) {
            Object var13 = var14.next();
            ++var12;
            ScorePlayerTeam var15 = var3.getPlayersTeam(((Score)var13).getPlayerName());
            String var16 = ScorePlayerTeam.formatPlayerName(var15, ((Score)var13).getPlayerName());
            String var17 = "" + EnumChatFormatting.RED + ((Score)var13).getScorePoints();
            int var18 = var22 - var12 * this.getFontRenderer().FONT_HEIGHT;
            int var19 = var2.getScaledWidth() - var23 + 2;
            drawRect((double)(var24 - 2), (double)var18, (double)var19, (double)(var18 + this.getFontRenderer().FONT_HEIGHT), 1342177280);
            this.getFontRenderer().drawString(var16, (double)var24, (double)var18, 553648127);
            this.getFontRenderer().drawString(var17, (double)(var19 - this.getFontRenderer().getStringWidth(var17)), (double)var18, 553648127);
            if (var12 == var6.size()) {
               String var20 = var1.getDisplayName();
               drawRect((double)(var24 - 2), (double)(var18 - this.getFontRenderer().FONT_HEIGHT - 1), (double)var19, (double)(var18 - 1), 1610612736);
               drawRect((double)(var24 - 2), (double)(var18 - 1), (double)var19, (double)var18, 1342177280);
               this.getFontRenderer().drawString(var20, (double)(var24 + var7 / 2 - this.getFontRenderer().getStringWidth(var20) / 2), (double)(var18 - this.getFontRenderer().FONT_HEIGHT), 553648127);
            }
         }

      }
   }

   public GuiPlayerTabOverlay getTabList() {
      return this.overlayPlayerList;
   }

   private void renderHotbarItem(int var1, int var2, int var3, float var4, EntityPlayer var5) {
      ItemStack var6 = var5.inventory.mainInventory[var1];
      if (var6 != null) {
         float var7 = (float)var6.animationsToGo - var4;
         if (var7 > 0.0F) {
            GlStateManager.pushMatrix();
            float var8 = 1.0F + var7 / 5.0F;
            GlStateManager.translate((float)(var2 + 8), (float)(var3 + 12), 0.0F);
            GlStateManager.scale(1.0F / var8, (var8 + 1.0F) / 2.0F, 1.0F);
            GlStateManager.translate((float)(-(var2 + 8)), (float)(-(var3 + 12)), 0.0F);
         }

         this.itemRenderer.renderItemAndEffectIntoGUI(var6, var2, var3);
         if (var7 > 0.0F) {
            GlStateManager.popMatrix();
         }

         this.itemRenderer.renderItemOverlays(Minecraft.fontRendererObj, var6, var2, var3);
      }

   }

   public void displayTitle(String var1, String var2, int var3, int var4, int var5) {
      if (var1 == null && var2 == null && var3 < 0 && var4 < 0 && var5 < 0) {
         this.field_175201_x = "";
         this.field_175200_y = "";
         this.field_175195_w = 0;
      } else if (var1 != null) {
         this.field_175201_x = var1;
         this.field_175195_w = this.field_175199_z + this.field_175192_A + this.field_175193_B;
      } else if (var2 != null) {
         this.field_175200_y = var2;
      } else {
         if (var3 >= 0) {
            this.field_175199_z = var3;
         }

         if (var4 >= 0) {
            this.field_175192_A = var4;
         }

         if (var5 >= 0) {
            this.field_175193_B = var5;
         }

         if (this.field_175195_w > 0) {
            this.field_175195_w = this.field_175199_z + this.field_175192_A + this.field_175193_B;
         }
      }

   }

   public void func_181551_a(ScaledResolution var1) {
      this.mc.mcProfiler.startSection("selectedItemName");
      if (this.remainingHighlightTicks > 0 && this.highlightingItemStack != null) {
         String var2 = this.highlightingItemStack.getDisplayName();
         if (this.highlightingItemStack.hasDisplayName()) {
            var2 = EnumChatFormatting.ITALIC + var2;
         }

         int var3 = (var1.getScaledWidth() - this.getFontRenderer().getStringWidth(var2)) / 2;
         int var4 = ScaledResolution.getScaledHeight() - 59;
         if (!Minecraft.playerController.shouldDrawHUD()) {
            var4 += 14;
         }

         int var5 = (int)((float)this.remainingHighlightTicks * 256.0F / 10.0F);
         if (var5 > 255) {
            var5 = 255;
         }

         if (var5 > 0) {
            GlStateManager.pushMatrix();
            GlStateManager.enableBlend();
            GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
            this.getFontRenderer().drawStringWithShadow(var2, (float)var3, (float)var4, 16777215 + (var5 << 24));
            GlStateManager.disableBlend();
            GlStateManager.popMatrix();
         }
      }

      this.mc.mcProfiler.endSection();
   }

   private void renderBossHealth() {
      if (BossStatus.bossName != null && BossStatus.statusBarTime > 0) {
         --BossStatus.statusBarTime;
         FontRenderer var1 = Minecraft.fontRendererObj;
         ScaledResolution var2 = new ScaledResolution(this.mc);
         int var3 = var2.getScaledWidth();
         short var4 = 182;
         int var5 = var3 / 2 - var4 / 2;
         int var6 = (int)(BossStatus.healthScale * (float)(var4 + 1));
         byte var7 = 12;
         drawTexturedModalRect(var5, var7, 0, 74, var4, 5);
         drawTexturedModalRect(var5, var7, 0, 74, var4, 5);
         if (var6 > 0) {
            drawTexturedModalRect(var5, var7, 0, 79, var6, 5);
         }

         String var8 = BossStatus.bossName;
         int var9 = 16777215;
         if (Config.isCustomColors()) {
            var9 = CustomColors.getBossTextColor(var9);
         }

         this.getFontRenderer().drawStringWithShadow(var8, (float)(var3 / 2 - this.getFontRenderer().getStringWidth(var8) / 2), (float)(var7 - 10), var9);
         GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
         this.mc.getTextureManager().bindTexture(icons);
      }

   }
}
