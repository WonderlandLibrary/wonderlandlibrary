package my.NewSnake.Tank.module.modules.WORLD;

import com.mojang.realmsclient.gui.ChatFormatting;
import java.awt.Color;
import java.awt.Font;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Objects;
import my.NewSnake.Tank.RenderUtil;
import my.NewSnake.Tank.module.Module;
import my.NewSnake.Tank.module.ModuleManager;
import my.NewSnake.Tank.option.Option;
import my.NewSnake.event.EventTarget;
import my.NewSnake.event.events.Render2DEvent;
import my.NewSnake.utils.MCFontRenderer;
import net.minecraft.client.Minecraft;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemPotion;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;

@Module.Mod
public class HUD extends Module {
   @Option.Op(
      min = 0.0D,
      max = 255.0D,
      increment = 1.0D
   )
   private double Config5;
   private final MCFontRenderer otherfont = new MCFontRenderer(new Font("Tahoma", 1, 18), true, true);
   @Option.Op
   private boolean NORMAL;
   public double RBWSpeed = 2500.0D;
   @Option.Op(
      min = 0.0D,
      max = 255.0D,
      increment = 1.0D
   )
   private double Config1;
   public double G = 250.0D;
   private boolean animations;
   @Option.Op
   private boolean RANDOM;
   @Option.Op(
      min = 0.25D,
      max = 5.0D,
      increment = 1.0D
   )
   public static double borderWidth;
   private int colorr = (new Color(3158271)).getRGB();
   public double R = 0.0D;
   private boolean WAVE;
   @Option.Op
   private boolean WRAPPERLEFT;
   @Option.Op
   private boolean LEFT;
   @Option.Op(
      min = 0.0D,
      max = 255.0D,
      increment = 1.0D
   )
   private double Config6;
   public double B = 0.0D;
   public Color colorValue = new Color((new Color(3158271)).getRGB());
   @Option.Op
   private boolean MODECASE = true;
   public boolean Rainbow2 = true;
   @Option.Op
   private boolean font = true;
   @Option.Op
   private boolean background = true;
   @Option.Op
   private boolean RIGHT;
   @Option.Op(
      min = 0.0D,
      max = 255.0D,
      increment = 1.0D
   )
   public double backgroundAlpha;
   @Option.Op(
      min = 0.0D,
      max = 255.0D,
      increment = 1.0D
   )
   private double Config2;
   @Option.Op
   private boolean BORDERMODE = true;
   private boolean MODECOLORS = true;
   @Option.Op(
      min = 0.0D,
      max = 255.0D,
      increment = 1.0D
   )
   private double Config4;
   @Option.Op
   private boolean Configure;
   @Option.Op
   private boolean LIGHTRAINBOW = true;
   @Option.Op
   private boolean DEV;
   @Option.Op
   private boolean NONE = true;
   private boolean Fps = true;
   public HashMap modColors = new HashMap();
   public float Yogunluk = 0.3F;
   private boolean Titulo = true;
   @Option.Op(
      min = 0.0D,
      max = 255.0D,
      increment = 1.0D
   )
   private double Config3;

   private String getModuleRenderString(Module var1) {
      if (this.MODECASE) {
         return (Objects.nonNull(var1.getDisplayName()) ? var1.getDisplayName() + (Objects.nonNull(var1.getSuffix()) ? ChatFormatting.GRAY + " " + var1.getSuffix() : "") : var1.getDisplayName() + (Objects.nonNull(var1.getSuffix()) ? ChatFormatting.GRAY + " " + var1.getSuffix() : "")).toLowerCase();
      } else if (this.MODECASE) {
         return (Objects.nonNull(var1.getDisplayName()) ? var1.getDisplayName() + (Objects.nonNull(var1.getSuffix()) ? ChatFormatting.GRAY + " " + var1.getSuffix() : "") : var1.getDisplayName() + (Objects.nonNull(var1.getSuffix()) ? ChatFormatting.GRAY + " " + var1.getSuffix() : "")).toUpperCase();
      } else {
         return Objects.nonNull(var1.getDisplayName()) ? var1.getDisplayName() + (Objects.nonNull(var1.getSuffix()) ? ChatFormatting.GRAY + " " + var1.getSuffix() : "") : var1.getDisplayName() + (Objects.nonNull(var1.getSuffix()) ? ChatFormatting.GRAY + " " + var1.getSuffix() : "");
      }
   }

   private float getOffset() {
      return (float)(System.currentTimeMillis() % 2000L) / 1000.0F;
   }

   private double lambda$0(Module var1) {
      int var10000;
      if (this.font) {
         var10000 = this.otherfont.getStringWidth(this.getModuleRenderString(var1));
      } else {
         Minecraft var2 = mc;
         var10000 = Minecraft.fontRendererObj.getStringWidth(this.getModuleRenderString(var1));
      }

      return (double)(-var10000);
   }

   private static boolean lambda$1(Module var0) {
      return !var0.isEnabled();
   }

   public int getRandomColor() {
      return (new Color((int)(Math.random() * 255.0D), (int)(Math.random() * 255.0D), (int)(Math.random() * 255.0D), 255)).getRGB();
   }

   public int color(int var1, int var2) {
      float[] var3 = new float[3];
      Color var4 = this.colorValue;
      Color.RGBtoHSB(var4.getRed(), var4.getGreen(), var4.getBlue(), var3);
      float var5 = Math.abs((this.getOffset() + (float)var1 / (float)var2 * 4.0F) % 2.0F - 1.0F);
      var5 = 0.4F + 0.4F * var5;
      var3[2] = var5 % 1.0F;
      return Color.HSBtoRGB(var3[0], var3[1], var3[2]);
   }

   private int getPotions() {
      int var1 = 0;
      Minecraft var10000 = mc;
      Iterator var3 = Minecraft.thePlayer.inventoryContainer.inventorySlots.iterator();

      while(true) {
         while(true) {
            ItemStack var4;
            ItemPotion var5;
            do {
               do {
                  Slot var2;
                  do {
                     if (!var3.hasNext()) {
                        return var1;
                     }

                     var2 = (Slot)var3.next();
                  } while(!var2.getHasStack());

                  var4 = var2.getStack();
               } while(!(var4.getItem() instanceof ItemPotion));

               var5 = (ItemPotion)var4.getItem();
            } while(!ItemPotion.isSplash(var4.getMetadata()));

            Iterator var7 = var5.getEffects(var4).iterator();

            while(var7.hasNext()) {
               PotionEffect var6 = (PotionEffect)var7.next();
               if (var6.getPotionID() == Potion.heal.getId()) {
                  ++var1;
                  break;
               }
            }
         }
      }
   }

   @EventTarget
   public void onRender2D(Render2DEvent var1) {
      if (!mc.gameSettings.showDebugInfo) {
         Color var2 = new Color((int)this.R, (int)this.G, (int)this.B);
         int var3 = 0;
         if (this.MODECOLORS) {
            if (this.DEV) {
               var3 = Color.getHSBColor(0.0F, 0.0F, 1.0F).getRGB();
            }

            if (this.LIGHTRAINBOW) {
               var3 = RenderUtil.getRainbow(6000, 0, 0.55F);
            }

            if (this.Configure) {
               var3 = this.getGradientOffset(new Color(255, 60, 234), new Color(27, 179, 255), (double)Math.abs(System.currentTimeMillis() / 10L) / 100.0D).getRGB();
            }

            if (this.WAVE) {
               var3 = this.color(1, 100);
            }

            if (this.Fps) {
               this.otherfont.drawStringWithShadow(" " + Minecraft.getDebugFPS(), 1.0D, 50.0D, this.Rainbow2 ? this.Sasuke((int)this.RBWSpeed, -15) : var2.getRGB());
            }
         }

         if (this.Titulo) {
            String var4 = "" + ChatFormatting.GRAY;
            if (this.MODECASE) {
            }

            Minecraft var10000;
            if (this.font) {
               this.otherfont.drawStringWithShadow(var4, 3.0D, 3.0D, var3);
            } else {
               var10000 = mc;
               Minecraft.fontRendererObj.drawStringWithShadow(var4, 3.0F, 3.0F, var3);
            }

            if (!this.animations) {
               float var5 = 3.0F;
               ArrayList var6 = new ArrayList(ModuleManager.getModules());
               var6.sort(Comparator.comparingDouble(this::lambda$0));
               Iterator var8 = var6.iterator();

               Module var7;
               while(var8.hasNext()) {
                  var7 = (Module)var8.next();
                  if (!var7.isEnabled() && this.MODECOLORS && this.modColors.get(var7) != null) {
                     this.modColors.remove(var7);
                  }
               }

               var6.removeIf(HUD::lambda$1);
               var8 = var6.iterator();

               while(var8.hasNext()) {
                  var7 = (Module)var8.next();
                  int var9 = 0;
                  if (this.MODECOLORS) {
                     if (this.DEV) {
                        var9 = RenderUtil.getRainbow(6000, (int)(var5 * 30.0F), 0.92F);
                     }

                     int var17;
                     Minecraft var19;
                     if (this.LIGHTRAINBOW) {
                        var9 = RenderUtil.getRainbow(6000, (int)(var5 * 30.0F), 0.55F);
                        if (this.Configure) {
                           Color var10001 = new Color((int)this.Config1, (int)this.Config6, (int)this.Config2);
                           Color var10002 = new Color((int)this.Config5, (int)this.Config4, (int)this.Config3);
                           double var10003 = (double)Math.abs(System.currentTimeMillis() / 10L) / 100.0D;
                           int var10005;
                           if (this.font) {
                              var10005 = this.otherfont.getHeight() + 6;
                           } else {
                              Minecraft var20 = mc;
                              var10005 = Minecraft.fontRendererObj.FONT_HEIGHT + 4;
                           }

                           var9 = this.getGradientOffset(var10001, var10002, var10003 + (double)(var5 / (float)(var10005 / 2))).getRGB();
                        }

                        if (this.RANDOM) {
                           if (!this.modColors.containsKey(var7)) {
                              this.modColors.put(var7, this.getRandomColor());
                           }

                           var9 = (Integer)this.modColors.get(var7);
                        }

                        if (this.WAVE) {
                           if (this.font) {
                              var17 = this.otherfont.getHeight() + 6;
                           } else {
                              var19 = mc;
                              var17 = Minecraft.fontRendererObj.FONT_HEIGHT + 4;
                           }

                           var9 = this.color((int)(var5 / (float)var17), var9);
                        }
                     }

                     int var13 = var1.getScaledResolution().getScaledWidth() - 0;
                     int var15;
                     Minecraft var16;
                     if (this.font) {
                        var15 = this.otherfont.getStringWidth(this.getModuleRenderString(var7));
                     } else {
                        var16 = mc;
                        var15 = Minecraft.fontRendererObj.getStringWidth(this.getModuleRenderString(var7));
                     }

                     float var10 = (float)(var13 - var15);
                     if (this.BORDERMODE) {
                        double var14;
                        double var18;
                        if (this.NONE && this.background) {
                           var14 = (double)(var10 - 2.0F);
                           var18 = (double)(var5 - 3.0F);
                           if (this.font) {
                              var17 = this.otherfont.getStringWidth(this.getModuleRenderString(var7));
                           } else {
                              var19 = mc;
                              var17 = Minecraft.fontRendererObj.getStringWidth(this.getModuleRenderString(var7));
                           }

                           RenderUtil.drawRect(var14, var18, (double)(var17 + 5), (double)(this.otherfont.getHeight() + 4), (new Color(0, 0, 0, (int)this.backgroundAlpha)).getRGB());
                        }

                        if (this.RIGHT) {
                           if (this.font) {
                              var15 = this.otherfont.getStringWidth(this.getModuleRenderString(var7));
                           } else {
                              var16 = mc;
                              var15 = Minecraft.fontRendererObj.getStringWidth(this.getModuleRenderString(var7));
                           }

                           RenderUtil.drawRect((double)(var10 + (float)var15 + 2.0F) - borderWidth, (double)(var5 - 3.0F), borderWidth, (double)(this.otherfont.getHeight() + 4), var9);
                           if (this.background) {
                              var14 = (double)var10 - borderWidth;
                              var18 = (double)(var5 - 3.0F);
                              if (this.font) {
                                 var17 = this.otherfont.getStringWidth(this.getModuleRenderString(var7));
                              } else {
                                 var19 = mc;
                                 var17 = Minecraft.fontRendererObj.getStringWidth(this.getModuleRenderString(var7));
                              }

                              RenderUtil.drawRect(var14, var18, (double)(var17 + (this.font ? 6 : 4)), (double)(this.otherfont.getHeight() + 4), (new Color(0, 0, 0, (int)this.backgroundAlpha)).getRGB());
                           }
                        }

                        if (this.LEFT) {
                           if (this.background) {
                              var14 = (double)(var10 - 2.0F);
                              var18 = (double)(var5 - 3.0F);
                              if (this.font) {
                                 var17 = this.otherfont.getStringWidth(this.getModuleRenderString(var7));
                              } else {
                                 var19 = mc;
                                 var17 = Minecraft.fontRendererObj.getStringWidth(this.getModuleRenderString(var7));
                              }

                              RenderUtil.drawRect(var14, var18, (double)(var17 + 4), (double)(this.otherfont.getHeight() + 4), (new Color(0, 0, 0, (int)this.backgroundAlpha)).getRGB());
                           }

                           RenderUtil.drawRect((double)(var10 - 2.0F) - borderWidth, (double)(var5 - 3.0F), borderWidth, (double)(this.otherfont.getHeight() + 4), var9);
                        }

                        if (this.WRAPPERLEFT) {
                           if (this.background) {
                              var14 = (double)(var10 - 2.0F);
                              var18 = (double)(var5 - 3.0F);
                              if (this.font) {
                                 var17 = this.otherfont.getStringWidth(this.getModuleRenderString(var7));
                              } else {
                                 var19 = mc;
                                 var17 = Minecraft.fontRendererObj.getStringWidth(this.getModuleRenderString(var7));
                              }

                              RenderUtil.drawRect(var14, var18, (double)(var17 + 4), (double)(this.otherfont.getHeight() + 4), (new Color(0, 0, 0, (int)this.backgroundAlpha)).getRGB());
                           }

                           RenderUtil.drawRect((double)(var10 - 2.0F) - borderWidth, (double)(var5 - 3.0F), borderWidth, (double)(this.otherfont.getHeight() + 4), var9);
                           if (var6.indexOf(var7) == var6.size() - 1) {
                              var14 = (double)(var10 - 2.0F) - borderWidth;
                              var18 = (double)(var5 + (float)this.otherfont.getHeight() + 1.0F);
                              if (this.font) {
                                 var17 = this.otherfont.getStringWidth(this.getModuleRenderString(var7));
                              } else {
                                 var19 = mc;
                                 var17 = Minecraft.fontRendererObj.getStringWidth(this.getModuleRenderString(var7));
                              }

                              RenderUtil.drawRect(var14, var18, (double)(var17 + 4) + borderWidth, borderWidth + 0.25D, var9);
                           } else {
                              Module var11 = (Module)var6.get(var6.indexOf(var7) + 1);
                              if (this.font) {
                                 var13 = this.otherfont.getStringWidth(this.getModuleRenderString(var7)) - this.otherfont.getStringWidth(this.getModuleRenderString(var11));
                              } else {
                                 var10000 = mc;
                                 var13 = Minecraft.fontRendererObj.getStringWidth(this.getModuleRenderString(var7));
                                 var16 = mc;
                                 var13 -= Minecraft.fontRendererObj.getStringWidth(this.getModuleRenderString(var11));
                              }

                              float var12 = (float)var13;
                              RenderUtil.drawRect((double)(var10 - 2.0F) - borderWidth, (double)(var5 + (float)this.otherfont.getHeight() + 1.0F), borderWidth + (double)var12, borderWidth + 0.25D, var9);
                           }
                        }
                     }

                     if (this.font) {
                        this.otherfont.drawStringWithShadow(this.getModuleRenderString(var7), (double)var10 - (this.BORDERMODE == this.RIGHT ? borderWidth - 2.0D : 0.0D), (double)(var5 - 1.0F), var9);
                     } else {
                        var10000 = mc;
                        Minecraft.fontRendererObj.drawStringWithShadow(this.getModuleRenderString(var7), (float)((double)var10 - (this.BORDERMODE == this.RIGHT ? borderWidth - 2.0D : 0.0D)), var5 - 1.0F, var9);
                     }

                     if (this.font) {
                        var15 = this.otherfont.getHeight() + 4;
                     } else {
                        var16 = mc;
                        var15 = Minecraft.fontRendererObj.FONT_HEIGHT + 2;
                     }

                     var5 += (float)var15;
                  }
               }
            }
         }

      }
   }

   private int Sasuke(int var1, int var2) {
      float var3 = (float)((System.currentTimeMillis() + (long)var2) % (long)var1);
      var3 /= (float)var1;
      return Color.getHSBColor(var3, this.Yogunluk, 1.0F).getRGB();
   }

   public Color getGradientOffset(Color var1, Color var2, double var3) {
      double var5;
      int var7;
      if (var3 > 1.0D) {
         var5 = var3 % 1.0D;
         var7 = (int)var3;
         var3 = var7 % 2 == 0 ? var5 : 1.0D - var5;
      }

      var5 = 1.0D - var3;
      var7 = (int)((double)var1.getRed() * var5 + (double)var2.getRed() * var3);
      int var8 = (int)((double)var1.getGreen() * var5 + (double)var2.getGreen() * var3);
      int var9 = (int)((double)var1.getBlue() * var5 + (double)var2.getBlue() * var3);
      return new Color(var7, var8, var9);
   }
}
