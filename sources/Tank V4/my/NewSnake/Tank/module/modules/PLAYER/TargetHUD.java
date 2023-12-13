package my.NewSnake.Tank.module.modules.PLAYER;

import com.ibm.icu.text.NumberFormat;
import java.awt.Color;
import java.awt.Font;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Locale;
import java.util.Objects;
import my.NewSnake.Tank.module.Module;
import my.NewSnake.Tank.option.Option;
import my.NewSnake.event.EventTarget;
import my.NewSnake.event.events.Render2DEvent;
import my.NewSnake.utils.ClientUtils;
import my.NewSnake.utils.ColorUtils;
import my.NewSnake.utils.GuiUtils;
import my.NewSnake.utils.MCFontRenderer;
import my.NewSnake.utils.RenderUtils;
import my.NewSnake.utils.Stopwatch;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

@Module.Mod
public class TargetHUD extends Module {
   private double otherHealthBarWidth;
   private float animated;
   private static double a = 0.0D;
   private float lastHealth = 0.0F;
   private static double lastP = 0.0D;
   private final MCFontRenderer otherfont = new MCFontRenderer(new Font("Tahoma", 0, 14), true, true);
   @Option.Op(
      min = 0.0D,
      max = 1500.0D,
      increment = 1.0D,
      name = "x"
   )
   public static double x;
   private final DecimalFormat decimalFormat;
   @Option.Op(
      min = 50.0D,
      max = 255.0D,
      increment = 1.0D
   )
   private double Transparenciaxz;
   public static double alternateHealth;
   @Option.Op(
      min = 0.0D,
      max = 1500.0D,
      increment = 1.0D,
      name = "z"
   )
   public static double z;
   public static transient double Vidafor = 0.0D;
   @Option.Op
   private boolean NewUpdate;
   public static transient double healthBar = 0.0D;
   public static transient double vapotarget = 0.0D;
   @Option.Op(
      min = 0.0D,
      max = 15.0D,
      increment = 0.5D
   )
   private double Range;
   @Option.Op
   private boolean PerfectHud;
   private Minecraft mc = Minecraft.getMinecraft();
   @Option.Op
   private boolean NewFlux;
   private double healthBarWidth;
   @Option.Op
   private boolean state;
   public static transient double healthBarTarget = 0.0D;
   @Option.Op(
      min = 0.0D,
      max = 255.0D,
      increment = 1.0D
   )
   private double CorDaImagem3;
   @Option.Op(
      min = 0.0D,
      max = 255.0D,
      increment = 1.0D
   )
   private double CorDaImagem2;
   @Option.Op
   private boolean NewTest;
   private int width;
   private final Stopwatch animationStopwatch;
   @Option.Op(
      min = 0.0D,
      max = 255.0D,
      increment = 1.0D
   )
   private double CorDaImagem;
   private double hudHeight;

   public static void prepareScissorBox(double var0, double var2, double var4, double var6) {
      ScaledResolution var8 = new ScaledResolution(Minecraft.getMinecraft());
      int var9 = var8.getScaleFactor();
      GL11.glScissor((int)(var0 * (double)((float)var9)), (int)(((double)((float)ScaledResolution.getScaledHeight()) - var6) * (double)((float)var9)), (int)((var4 - var0) * (double)((float)var9)), (int)((var6 - var2) * (double)((float)var9)));
   }

   private void drawFace(double var1, double var3, float var5, float var6, int var7, int var8, int var9, int var10, float var11, float var12, AbstractClientPlayer var13) {
      try {
         ResourceLocation var14 = var13.getLocationSkin();
         ClientUtils.mc().getTextureManager().bindTexture(var14);
         GL11.glEnable(3042);
         GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
         Gui.drawScaledCustomSizeModalRect(var1, var3, var5, var6, var7, var8, var9, var10, var11, var12);
         GL11.glDisable(3042);
      } catch (Exception var15) {
      }

   }

   public static double round(double var0, int var2) {
      if (var2 < 0) {
         throw new IllegalArgumentException();
      } else {
         BigDecimal var3 = new BigDecimal(var0);
         var3 = var3.setScale(var2, RoundingMode.HALF_UP);
         return var3.doubleValue();
      }
   }

   public static void drawRoundedRect(double var0, double var2, double var4, double var6, double var8, int var10) {
      GL11.glScaled(0.5D, 0.5D, 0.5D);
      var0 *= 2.0D;
      var2 *= 2.0D;
      var4 *= 2.0D;
      var6 *= 2.0D;
      GL11.glDisable(3553);
      GL11.glEnable(2848);
      GlStateManager.enableBlend();
      glColor(var10);
      GL11.glBegin(9);

      int var11;
      for(var11 = 0; var11 <= 90; ++var11) {
         GL11.glVertex2d(var0 + var8 + Math.sin((double)var11 * 3.141592653589793D / 180.0D) * var8 * -1.0D, var2 + var8 + Math.cos((double)var11 * 3.141592653589793D / 180.0D) * var8 * -1.0D);
      }

      for(var11 = 90; var11 <= 180; ++var11) {
         GL11.glVertex2d(var0 + var8 + Math.sin((double)var11 * 3.141592653589793D / 180.0D) * var8 * -1.0D, var6 - var8 + Math.cos((double)var11 * 3.141592653589793D / 180.0D) * var8 * -1.0D);
      }

      for(var11 = 0; var11 <= 90; ++var11) {
         GL11.glVertex2d(var4 - var8 + Math.sin((double)var11 * 3.141592653589793D / 180.0D) * var8, var6 - var8 + Math.cos((double)var11 * 3.141592653589793D / 180.0D) * var8);
      }

      for(var11 = 90; var11 <= 180; ++var11) {
         GL11.glVertex2d(var4 - var8 + Math.sin((double)var11 * 3.141592653589793D / 180.0D) * var8, var2 + var8 + Math.cos((double)var11 * 3.141592653589793D / 180.0D) * var8);
      }

      GL11.glEnd();
      GL11.glEnable(3553);
      GL11.glScaled(2.0D, 2.0D, 2.0D);
      GL11.glColor4d(1.0D, 1.0D, 1.0D, 1.0D);
   }

   public TargetHUD() {
      this.decimalFormat = new DecimalFormat("0.#", new DecimalFormatSymbols(Locale.ENGLISH));
      this.animationStopwatch = new Stopwatch();
      this.animated = 20.0F;
   }

   public static Color blendColors(float[] var0, Color[] var1, float var2) {
      Color var3 = null;
      if (var0 == null) {
         throw new IllegalArgumentException("Fractions can't be null");
      } else if (var1 == null) {
         throw new IllegalArgumentException("Colours can't be null");
      } else if (var0.length == var1.length) {
         int[] var4 = getFractionIndicies(var0, var2);
         float[] var5 = new float[]{var0[var4[0]], var0[var4[1]]};
         Color[] var6 = new Color[]{var1[var4[0]], var1[var4[1]]};
         float var7 = var5[1] - var5[0];
         float var8 = var2 - var5[0];
         float var9 = var8 / var7;
         var3 = blend(var6[0], var6[1], (double)(1.0F - var9));
         return var3;
      } else {
         throw new IllegalArgumentException("Fractions and colours must have equal number of elements");
      }
   }

   public static Color blend(Color var0, Color var1, double var2) {
      float var4 = (float)var2;
      float var5 = 1.0F - var4;
      float[] var6 = new float[3];
      float[] var7 = new float[3];
      var0.getColorComponents(var6);
      var1.getColorComponents(var7);
      float var8 = var6[0] * var4 + var7[0] * var5;
      float var9 = var6[1] * var4 + var7[1] * var5;
      float var10 = var6[2] * var4 + var7[2] * var5;
      if (var8 < 0.0F) {
         var8 = 0.0F;
      } else if (var8 > 255.0F) {
         var8 = 255.0F;
      }

      if (var9 < 0.0F) {
         var9 = 0.0F;
      } else if (var9 > 255.0F) {
         var9 = 255.0F;
      }

      if (var10 < 0.0F) {
         var10 = 0.0F;
      } else if (var10 > 255.0F) {
         var10 = 255.0F;
      }

      Color var11 = null;

      try {
         var11 = new Color(var8, var9, var10);
      } catch (IllegalArgumentException var14) {
         NumberFormat var13 = NumberFormat.getNumberInstance();
         System.out.println(var13.format((double)var8) + "; " + var13.format((double)var9) + "; " + var13.format((double)var10));
         var14.printStackTrace();
      }

      return var11;
   }

   public void drawExhiRect(float var1, float var2, float var3, float var4) {
      float var5 = 1.5F;
      float var6 = 1.0F;
      Gui.drawRect((double)(var1 - 1.5F - 1.0F), (double)(var2 - 1.5F - 1.0F), (double)(var3 + 1.5F + 1.0F), (double)(var4 + 1.5F + 1.0F), (new Color(62, 59, 59)).getRGB());
      Gui.drawRect((double)(var1 - 1.5F), (double)(var2 - 1.5F), (double)(var3 + 1.5F), (double)(var4 + 1.5F), (new Color(42, 39, 39)).getRGB());
      Gui.drawRect((double)var1, (double)var2, (double)var3, (double)var4, (new Color(18, 16, 16)).getRGB());
   }

   public ResourceLocation getPlayerSkin(String var1) {
      if (Minecraft.thePlayer != null) {
         Iterator var3 = Minecraft.thePlayer.sendQueue.getPlayerInfoMap().iterator();

         while(var3.hasNext()) {
            Object var2 = var3.next();

            try {
               NetworkPlayerInfo var4 = (NetworkPlayerInfo)var2;
               if (var1.equals(var4.getGameProfile().getName())) {
                  return var4.getLocationSkin();
               }
            } catch (Exception var5) {
            }
         }
      }

      return null;
   }

   public static float clamp(double var0, double var2, double var4) {
      if (var0 <= var2) {
         var0 = var2;
      }

      if (var0 >= var4) {
         var0 = var4;
      }

      return (float)var0;
   }

   private int getHealthColor(EntityLivingBase var1) {
      float var2 = var1.getHealth();
      float var3 = var1.getMaxHealth();
      float var4 = Math.max(0.0F, Math.min(var2, var3) / var3);
      return Color.HSBtoRGB(var4 / 3.0F, 1.0F, 0.75F) | -16777216;
   }

   @EventTarget
   public void onRender2D(Render2DEvent var1) {
      ArrayList var2 = new ArrayList();
      Iterator var3 = Minecraft.theWorld.loadedEntityList.iterator();

      while(true) {
         EntityPlayer var5;
         do {
            do {
               do {
                  do {
                     Object var4;
                     do {
                        if (!var3.hasNext()) {
                           return;
                        }

                        var4 = var3.next();
                     } while(!(var4 instanceof EntityPlayer));

                     var5 = (EntityPlayer)var4;
                  } while(!(var5.getHealth() > 0.0F));
               } while(var5 == Minecraft.thePlayer);
            } while(!((double)Minecraft.thePlayer.getDistanceToEntity(var5) <= this.Range));

            var2.add(var5);
         } while(var2.size() <= 0);

         var5 = (EntityPlayer)var2.get(0);
         float var6 = (float)(var1.getWidth() / 3 + (int)x);
         float var7 = (float)(var1.getHeight() / 3 + (int)z);
         float var9;
         float var10;
         int var11;
         if (this.NewTest) {
            DecimalFormat var8 = new DecimalFormat("#.#");
            this.drawExhiRect(var6 / 2.0F, var7 / 2.0F, var6 / 2.0F + 130.0F, var7 / 2.0F + 40.0F);
            Minecraft.fontRendererObj.drawString(var5.getName(), (double)((int)var6 / 2 + 3), (double)((int)var7 / 2 - Minecraft.fontRendererObj.FONT_HEIGHT + 32), -1);
            Gui.drawRect((double)(var6 / 2.0F + 3.0F - 1.5F), (double)(var7 / 2.0F + 35.0F - 1.5F), (double)(var6 / 2.0F + 127.0F + 1.5F), (double)(var7 / 2.0F + 37.0F + 1.5F), (new Color(24, 22, 22)).getRGB());
            var9 = var6 / 2.0F + 127.0F + 1.5F - (var6 / 2.0F + 3.0F - 1.5F);
            var10 = var9 / 5.0F;
            this.drawFace((double)((int)var6 / 2 + 3), (double)((int)var7 / 2 + 52 - 50), 8.0F, 8.0F, 8, 8, 7, 7, 64.0F, 64.0F, (AbstractClientPlayer)var5);
            Gui.drawRect((double)(var6 / 2.0F + 3.0F), (double)(var7 / 2.0F + 35.0F), (double)(var6 / 2.0F + 127.0F), (double)(var7 / 2.0F + 37.0F), (new Color(56, 54, 54)).getRGB());
            GuiUtils.drawBar(var5.getHealth(), var5.getMaxHealth(), var6 / 2.0F + 3.0F, var7 / 2.0F + 35.0F, var9 - 3.0F, 2.0F, getHealthColorTest(var5).getRGB());

            for(var11 = 0; var11 < 5; ++var11) {
               Gui.drawRect((double)(var6 / 2.0F + 3.0F - 1.5F + var10 * (float)var11), (double)(var7 / 2.0F + 35.0F), (double)(var6 / 2.0F + 3.0F - 1.5F + var10 * (float)var11 + 1.0F), (double)(var7 / 2.0F + 37.0F), (new Color(28, 26, 26)).getRGB());
            }

            Minecraft.fontRendererObj.drawString(var8.format((double)Minecraft.thePlayer.getDistanceToEntity(var5)) + " m", (double)((int)var6 / 2 + 3), (double)((int)var7 / 2 - Minecraft.fontRendererObj.FONT_HEIGHT + 21), -1);
            GlStateManager.pushMatrix();
            GlStateManager.scale(2.0F, 2.0F, 2.0F);
            float var27 = var5.getHealth() / 2.0F;
            Minecraft.fontRendererObj.drawString(var8.format((double)var27).replace(",", "."), (double)((int)(var6 / 2.0F + 120.0F - (float)(Minecraft.fontRendererObj.getStringWidth(var8.format((double)var27).replace(",", ".") + " ?") * 2)) / 2), (double)((int)(var7 / 2.0F - (float)Minecraft.fontRendererObj.FONT_HEIGHT + 16.0F) / 2), -1);
            GlStateManager.popMatrix();
         }

         if (this.PerfectHud) {
            double var21 = (double)(var5.getHealth() / var5.getMaxHealth());
            if (var21 > 1.0D) {
               var21 = 1.0D;
            } else if (var21 < 0.0D) {
               var21 = 0.0D;
            }

            RenderUtils.rectangle((double)(var6 / 2.0F - 200.0F), (double)(var7 / 2.0F - 42.0F), (double)(var6 / 2.0F - 200.0F + 40.0F + (float)(Minecraft.fontRendererObj.getStringWidth(var5.getName()) > 105 ? Minecraft.fontRendererObj.getStringWidth(var5.getName()) - 10 : 105)), (double)(var7 / 2.0F - 2.0F), (new Color(0, 0, 0, 150)).getRGB());
            this.drawFace((double)((int)var6 / 2 - 196), (double)((int)(var7 / 2.0F - 38.0F)), 8.0F, 8.0F, 8, 8, 32, 32, 64.0F, 64.0F, (AbstractClientPlayer)var5);
            Minecraft.fontRendererObj.drawStringWithShadow(var5.getName(), var6 / 2.0F - 196.0F + 40.0F, var7 / 2.0F - 36.0F, -1);
            RenderUtils.rectangle((double)(var6 / 2.0F - 196.0F + 40.0F), (double)(var7 / 2.0F - 26.0F), (double)(var6 / 2.0F - 196.0F + 40.0F) + 87.5D, (double)(var7 / 2.0F - 14.0F), (new Color(0, 0, 0)).getRGB());
            RenderUtils.rectangle((double)(var6 / 2.0F - 196.0F + 40.0F), (double)(var7 / 2.0F - 26.0F), (double)(var6 / 2.0F - 196.0F + 40.0F) + var21 * 1.25D * 70.0D, (double)(var7 / 2.0F - 14.0F), getHealthColorTest(var5).getRGB());
            Minecraft.fontRendererObj.drawStringWithShadow(String.format("§f%.1f", var5.getHealth()), var6 / 2.0F - 220.0F + 40.0F + 36.0F, var7 / 2.0F - 11.0F, getHealthColorTest(var5).getRGB());
            int var26 = (int)(var6 - 64.0F);
            var11 = (int)(var7 + 40.0F);
            Minecraft.fontRendererObj.drawStringWithShadow(String.valueOf(String.valueOf(String.format("", var5.getHealth() / 2.0F))) + " §c❤", (float)(var26 / 2 - 127), (float)(var11 / 2 - 32), getHealthColorTest(var5).getRGB());
         }

         float var23;
         if (this.NewFlux) {
            var23 = var5.getHealth();
            double var22 = (double)(var23 / var5.getMaxHealth());
            var22 = (double)clamp(var22, 0.0D, 1.0D);
            double var29 = 97.0D * var22;
            String var13 = String.valueOf((float)((int)var5.getHealth()) / 2.0F);
            if (this.animationStopwatch.elapsed(15L)) {
               this.healthBarWidth = animate(var29, this.healthBarWidth, 0.1029999852180481D);
               this.hudHeight = animate(40.0D, this.hudHeight, 0.10000000149011612D);
               this.animationStopwatch.reset();
            }

            Gui.drawRect(x + 125.5D, z - 9.5D, x + 265.0D, z + 30.5D, (new Color(31, 31, 31, (int)this.Transparenciaxz)).getRGB());
            Gui.drawRect(x + 166.0D, z + 6.0D, x + 263.0D, z + 15.0D, (new Color(40, 40, 40, 255)).getRGB());
            Gui.drawRect(x + 166.0D, z + 6.0D, x + 166.0D + this.healthBarWidth, z + 15.0D, this.getHealthColor(var5));
            Gui.drawRect(x + 166.0D, z + 6.0D, x + 166.0D + var29, z + 15.0D, this.getHealthColor(var5));
            FontRenderer var10000 = Minecraft.fontRendererObj;
            double var10002 = x + 128.0D + 46.0D;
            ClientUtils.mc();
            var10000.drawString(var13, var10002 - (double)((float)Minecraft.fontRendererObj.getStringWidth(var13) / 2.0F), z + 19.5D, -1);
            var10000 = Minecraft.fontRendererObj;
            var10002 = x + 128.0D + 46.0D;
            ClientUtils.mc();
            var10000.drawString("❤", var10002 + (double)Minecraft.fontRendererObj.getStringWidth(var13), z + 19.5D, (new Color(255, 0, 0)).getRGB());
            Minecraft.fontRendererObj.drawString(var5.getName(), x + 167.0D, z - 5.0D, -1);
            this.drawHead(((NetHandlerPlayClient)Objects.requireNonNull(this.mc.getNetHandler())).getPlayerInfo(var5.getUniqueID()).getLocationSkin(), (int)x + 127, (int)z - 8);
         }

         if (this.NewUpdate) {
            RenderUtils.rectangle((double)(var6 / 2.0F - 200.0F), (double)(var7 / 2.0F - 42.0F), (double)(var6 / 2.0F - 220.0F + 40.0F + (float)(Minecraft.fontRendererObj.getStringWidth(var5.getName()) > 105 ? Minecraft.fontRendererObj.getStringWidth(var5.getName()) - 10 : 105)), (double)(var7 / 2.0F - 5.0F), (new Color(0, 0, 0, 150)).getRGB());
            this.drawFace((double)((int)var6 / 2 - 196), (double)((int)(var7 / 2.0F - 38.0F)), 8.0F, 8.0F, 8, 8, 20, 20, 64.0F, 64.0F, (AbstractClientPlayer)var5);
            Minecraft.fontRendererObj.drawStringWithShadow(var5.getName(), var6 / 2.0F - 214.0F + 40.0F, var7 / 2.0F - 36.0F, -1);
            Minecraft.fontRendererObj.drawStringWithShadow(String.format("§f%.1f", var5.getHealth()), var6 / 2.0F - 152.0F + 40.0F - 60.0F, var7 / 2.0F - 25.0F, getHealthColorTest(var5).getRGB());
            int var24 = (int)(var6 - 64.0F);
            int var25 = (int)(var7 + 40.0F);
            ClientUtils.mc();
            if (Minecraft.thePlayer.getHealth() >= 0.0F) {
               ClientUtils.mc();
               if (Minecraft.thePlayer.getHealth() < 10.0F) {
                  this.width = 3;
               }
            }

            ClientUtils.mc();
            if (Minecraft.thePlayer.getHealth() >= 10.0F) {
               ClientUtils.mc();
               if (Minecraft.thePlayer.getHealth() < 100.0F) {
                  this.width = 3;
               }
            }

            GL11.glPushAttrib(1048575);
            GL11.glPushMatrix();
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
            ClientUtils.mc().getTextureManager().bindTexture(Gui.icons);

            for(var10 = 0.0F; var10 < var5.getMaxHealth() / 2.0F; ++var10) {
               Gui.drawTexturedModalRect(var6 / 2.0F - var5.getMaxHealth() / 2.5F * 49.0F / 2.0F + var10 * 8.0F, var7 / 2.0F - 16.0F, 16, 0, 9, 9);
            }

            for(var10 = 0.0F; var10 < var5.getHealth() / 2.0F; ++var10) {
               Gui.drawTexturedModalRect(var6 / 2.0F - var5.getMaxHealth() / 2.5F * 49.0F / 2.0F + var10 * 8.0F, var7 / 2.0F - 16.0F, 52, 0, 9, 9);
            }

            GL11.glPopAttrib();
            GL11.glPopMatrix();
         }

         if (this.state) {
            var23 = var5.getHealth();
            var9 = (float)var5.getTotalArmorValue();
            if (var23 == 1.0F) {
               var23 = (float)alternateHealth;
            }

            double var28 = (double)(var9 / 20.0F);
            double var12 = (double)(var23 / var5.getMaxHealth());
            var12 = MathHelper.clamp_double(var12, 0.0D, 1.0D);
            var28 = MathHelper.clamp_double(var28, 0.0D, 1.0D);
            int var14 = ColorUtils.getHealthColor(var23, var5.getMaxHealth()).getRGB();
            int var15 = ColorUtils.getHealthColor(var23, var5.getMaxHealth()).darker().getRGB();
            drawRoundedRect(x, z, x + 140.0D, z + 40.0D, 5.0D, (new Color(0, 0, 0, 100)).getRGB());
            drawRoundedRect(x, z, x + 140.0D, z + 2.0D, 2.0D, (new Color((int)this.CorDaImagem, (int)this.CorDaImagem2, (int)this.CorDaImagem3, 255)).getRGB());
            ResourceLocation var16 = this.getPlayerSkin(var5.getName());
            double var17 = 100.0D * var12;
            double var19 = 100.0D * var28;
            if (var16 != null) {
               GL11.glEnable(3089);
               prepareScissorBox(x + 5.0D, z, x + 140.0D, z + 40.0D);
               this.mc.getTextureManager().bindTexture(var16);
               Gui.drawModalRectWithCustomSizedTexture(x + 3.0D, z + 6.0D, 30.0F, 30.0F, 30.0D, 30.0D, 240.0D, 240.0D);
               GL11.glDisable(3089);
            }

            if (this.animationStopwatch.elapsed(15L)) {
               this.healthBarWidth = var17;
               this.otherHealthBarWidth = animate(var17, this.otherHealthBarWidth, 0.059999852180481D);
               this.hudHeight = animate(40.0D, this.hudHeight, 0.4500000014901161D);
               this.animationStopwatch.reset();
            }

            ClientUtils.mc();
            Minecraft.fontRendererObj.drawString(var5.getName(), x + 35.0D, z + 3.0D, -1);
            ClientUtils.mc();
            Minecraft.fontRendererObj.drawString(round((double)var23, 2) + " HP", x + 35.0D, z + 15.0D, -1);
            drawRoundedRect(x + 35.0D, z + 33.0D, x + 135.0D, z + 35.0D, 2.0D, (new Color(0, 0, 0, 130)).getRGB());
            drawRoundedRect(x + 35.0D, z + 23.0D, x + 135.0D, z + 25.0D, 2.0D, (new Color(0, 0, 0, 130)).getRGB());
            drawRoundedRect(x + 35.0D, z + 23.0D, x + 35.0D + this.otherHealthBarWidth, z + 25.0D, 2.0D, var15);
            drawRoundedRect(x + 35.0D, z + 23.0D, x + 35.0D + this.healthBarWidth, z + 25.0D, 2.0D, var14);
            if (var19 > 0.0D) {
               drawRoundedRect(x + 35.0D, z + 33.0D, x + 35.0D + var19, z + 35.0D, 2.0D, (new Color(33, 136, 255, 255)).getRGB());
            }
         }
      }
   }

   public static Color getHealthColorTest(EntityLivingBase var0) {
      float var1 = var0.getHealth();
      float[] var2 = new float[]{0.0F, 0.15F, 0.55F, 0.7F, 0.9F};
      Color[] var3 = new Color[]{new Color(133, 0, 0), Color.RED, Color.ORANGE, Color.YELLOW, Color.GREEN};
      float var4 = var1 / var0.getMaxHealth();
      return var1 >= 0.0F ? blendColors(var2, var3, var4).brighter() : var3[0];
   }

   public static double animate(double var0, double var2, double var4) {
      boolean var6 = var0 > var2;
      if (var4 < 0.0D) {
         var4 = 0.0D;
      } else if (var4 > 1.0D) {
         var4 = 1.0D;
      }

      double var7 = Math.max(var0, var2) - Math.min(var0, var2);
      double var9 = var7 * var4;
      if (var9 < 0.1D) {
         var9 = 0.1D;
      }

      if (var6) {
         var2 += var9;
      } else {
         var2 -= var9;
      }

      return var2;
   }

   public static int RGB() {
      float var0 = (float)(System.currentTimeMillis() % 10000L) / 1000.0F;
      int var1 = Color.HSBtoRGB(var0, 1.0F, 1.0F);
      return var1;
   }

   public static int[] getFractionIndicies(float[] var0, float var1) {
      int[] var2 = new int[2];

      int var3;
      for(var3 = 0; var3 < var0.length && var0[var3] <= var1; ++var3) {
      }

      if (var3 >= var0.length) {
         var3 = var0.length - 1;
      }

      var2[0] = var3 - 1;
      var2[1] = var3;
      return var2;
   }

   public static void glColor(int var0) {
      float var1 = (float)(var0 >> 24 & 255) / 255.0F;
      float var2 = (float)(var0 >> 16 & 255) / 255.0F;
      float var3 = (float)(var0 >> 8 & 255) / 255.0F;
      float var4 = (float)(var0 & 255) / 255.0F;
      GL11.glColor4f(var2, var3, var4, var1);
   }

   public void drawHead(ResourceLocation var1, int var2, int var3) {
      GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
      this.mc.getTextureManager().bindTexture(var1);
      Gui.drawScaledCustomSizeModalRect((double)var2, (double)var3, 8.0F, 8.0F, 8, 8, 37, 37, 64.0F, 64.0F);
   }
}
