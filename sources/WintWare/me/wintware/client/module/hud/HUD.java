package me.wintware.client.module.hud;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import me.wintware.client.Main;
import me.wintware.client.event.EventTarget;
import me.wintware.client.event.impl.EventRender2D;
import me.wintware.client.module.Category;
import me.wintware.client.module.Module;
import me.wintware.client.module.visual.NoPotionDebug;
import me.wintware.client.ui.login.LoginGui;
import me.wintware.client.utils.animation.AnimationUtil;
import me.wintware.client.utils.animation.Translate;
import me.wintware.client.utils.font.FontRenderer;
import me.wintware.client.utils.other.TimerUtils;
import me.wintware.client.utils.visual.ColorUtils;
import me.wintware.client.utils.visual.RenderUtil;
import me.wintware.client.viamcp.viafabric.ViaFabric;
import me.wintware.client.viamcp.viafabric.util.ProtocolUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.client.resources.I18n;
import net.minecraft.init.MobEffects;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import optifine.CustomColors;

public class HUD extends Module {
   float xd = 0.0F;
   public static String clientName;
   public static float count = 0.0F;
   public TimerUtils timer;
   int category;
   public static float globalOffset;

   public HUD() {
      super("HUD", Category.Hud);
   }

   @EventTarget
   public void onRender2D(EventRender2D e) {
      float target = mc.currentScreen instanceof GuiChat ? 15.0F : 0.0F;
      float delta = globalOffset - target;
      Minecraft var10003 = mc;
      globalOffset -= delta / (float)Math.max(1, Minecraft.getDebugFPS()) * 10.0F;
      if (!Double.isFinite(globalOffset)) {
         globalOffset = 0.0F;
      }

      if (globalOffset > 15.0F) {
         globalOffset = 15.0F;
      }

      if (globalOffset < 0.0F) {
         globalOffset = 0.0F;
      }

      ScaledResolution sr = new ScaledResolution(mc);
      this.drawPotionStatus(sr);
      this.renderWaterMark();
      this.hotBar();
   }

   public void renderWaterMark() {
      String server = mc.isSingleplayer() ? "localhost" : mc.getCurrentServerData().serverIP.toLowerCase();
      StringBuilder var10000 = (new StringBuilder()).append("nigga cat").append(" | ");
      Minecraft var10001 = mc;
      String text = var10000.append(Minecraft.player.getName()).append(" | ").append(server).append(" | version ").append(ProtocolUtils.getProtocolName(ViaFabric.clientSideVersion)).append(" | ").append(Calendar.getInstance().getTime().getHours()).append(":").append(Calendar.getInstance().getTime().getMinutes()).append(":").append(Calendar.getInstance().getTime().getSeconds()).toString();
      float width = (float)(Minecraft.getMinecraft().fontRenderer.getStringWidth(text) + 6);
      int height = 20;
      int posX = 2;
      int posY = 2;
      RenderUtil.drawRect(posX, posY, (float)posX + width + 2.0F, posY + height, (new Color(5, 5, 5, 255)).getRGB());
      RenderUtil.drawBorderedRect((double)posX + 0.5D, (double)posY + 0.5D, (double)((float)posX + width) + 1.5D, (double)(posY + height) - 0.5D, 0.5D, (new Color(40, 40, 40, 255)).getRGB(), (new Color(60, 60, 60, 255)).getRGB(), true);
      RenderUtil.drawBorderedRect(posX + 2, posY + 2, (float)posX + width, posY + height - 2, 0.5D, (new Color(22, 22, 22, 255)).getRGB(), (new Color(60, 60, 60, 255)).getRGB(), true);
      RenderUtil.drawRect((double)posX + 2.5D, (double)posY + 2.5D, (double)((float)posX + width) - 0.5D, (double)posY + 4.5D, (new Color(9, 9, 9, 255)).getRGB());
      RenderUtil.drawGradientSideways(4.0D, posY + 3, 4.0F + width / 3.0F, posY + 4, (new Color(81, 149, 219, 255)).getRGB(), (new Color(180, 49, 218, 255)).getRGB());
      RenderUtil.drawGradientSideways(4.0F + width / 3.0F, posY + 3, 4.0F + width / 3.0F * 2.0F, posY + 4, (new Color(180, 49, 218, 255)).getRGB(), (new Color(236, 93, 128, 255)).getRGB());
      RenderUtil.drawGradientSideways(4.0F + width / 3.0F * 2.0F, posY + 3, width / 3.0F * 3.0F + 1.0F, posY + 4, (new Color(236, 93, 128, 255)).getRGB(), (new Color(235, 255, 0, 255)).getRGB());
      Minecraft.getMinecraft().fontRenderer.drawStringWithShadow(text, (float)(4 + posX), (float)(8 + posY), -1);
   }

   public void hotBar() {
      ScaledResolution sr = new ScaledResolution(mc);
      this.xd = AnimationUtil.animation(this.xd, mc.currentScreen instanceof GuiChat ? (float)(sr.getScaledHeight() - 22) : (float)(sr.getScaledHeight() - 9), 1.0E-4F);
      Minecraft var10000 = mc;
      Minecraft var10001 = mc;
      double prevX = Minecraft.player.posX - Minecraft.player.prevPosX;
      var10000 = mc;
      var10001 = mc;
      double prevZ = Minecraft.player.posZ - Minecraft.player.prevPosZ;
      double lastDist = Math.sqrt(prevX * prevX + prevZ * prevZ);
      double currSpeed = lastDist * 15.3571428571D;
      String speed = String.format("%.2f blocks/sec", currSpeed);
      NetHandlerPlayClient var12 = Objects.requireNonNull(mc.getConnection());
      var10001 = mc;
      int ping = var12.getPlayerInfo(Minecraft.player.getUniqueID()).getResponseTime();
      FontRenderer var13 = Minecraft.getMinecraft().fontRenderer;
      StringBuilder var14 = (new StringBuilder());
      Minecraft var10002 = mc;
      var14 = var14.append(Math.round(Minecraft.player.posX)).append(" ");
      var10002 = mc;
      var14 = var14.append(Math.round(Minecraft.player.posY)).append(" ");
      var10002 = mc;
      var13.drawStringWithShadow(var14.append(Math.round(Minecraft.player.posZ)).toString(), 2.0F, this.xd, -1);
      Minecraft.getMinecraft().fontRenderer.drawStringWithShadow("Ping: §7" + ping + "ms", (float)(sr.getScaledWidth() - Minecraft.getMinecraft().fontRenderer.getStringWidth("Ping: §7" + ping + "ms") - 4), this.xd + -10.0F, -1);
      Minecraft.getMinecraft().fontRenderer.drawStringWithShadow("FPS: §7" + Minecraft.getDebugFPS(), 2.0F, this.xd + -18.0F, -1);
      float var10003 = this.xd + -9.0F;
      Minecraft.getMinecraft().fontRenderer.drawStringWithShadow(speed, 2.0F, var10003, -1);
      Minecraft.getMinecraft().fontRenderer.drawStringWithShadow("Admin version -§7 " + Main.build + "§f | UID: §7" + LoginGui.hwid.getText(), (float)(sr.getScaledWidth() - Minecraft.getMinecraft().fontRenderer.getStringWidth("Admin version -§7 " + Main.build + "§f | UID: §7" + LoginGui.hwid.getText()) - 2), this.xd, -1);
   }

   private void drawPotionStatus(ScaledResolution sr) {
      float offset = globalOffset;
      float pY = -2.0F;
      List<PotionEffect> potions = new ArrayList();
      Minecraft var10000 = mc;
      Iterator var5 = Minecraft.player.getActivePotionEffects().iterator();

      while(var5.hasNext()) {
         Object o = var5.next();
         potions.add((PotionEffect)o);
      }

      potions.sort(Comparator.comparingDouble((effectx) -> {
         return -mc.fontRendererObj.getStringWidth(I18n.format(Potion.getPotionById(CustomColors.getPotionId(effectx.getEffectName())).getName()));
      }));

      for(var5 = potions.iterator(); var5.hasNext(); pY -= 11.0F) {
         PotionEffect effect = (PotionEffect)var5.next();
         Potion potion = Potion.getPotionById(CustomColors.getPotionId(effect.getEffectName()));
         String name = I18n.format(potion.getName());
         String PType = "";
         if (effect.getAmplifier() == 1) {
            name = name + " II";
         } else if (effect.getAmplifier() == 2) {
            name = name + " III";
         } else if (effect.getAmplifier() == 3) {
            name = name + " IV";
         }

         if (effect.getDuration() < 600 && effect.getDuration() > 300) {
            PType = PType + ": " + Potion.getDurationString(effect);
         } else if (effect.getDuration() < 300) {
            PType = PType + ": " + Potion.getDurationString(effect);
         } else if (effect.getDuration() > 600) {
            PType = PType + ": " + Potion.getDurationString(effect);
         }

         mc.fontRenderer.drawStringWithShadow(name, (float)(sr.getScaledWidth() - mc.fontRenderer.getStringWidth(name + PType) - 3), (float)(sr.getScaledHeight() - 27) + pY - offset, potion.getLiquidColor());
         mc.fontRenderer.drawStringWithShadow(PType, (float)(sr.getScaledWidth() - mc.fontRenderer.getStringWidth(PType) - 2), (float)(sr.getScaledHeight() - 27) + pY - offset, -1);
      }

   }

   public void renderArrayList(ScaledResolution scaledResolution) {
      if (Main.instance.moduleManager.getModuleByClass(ArreyList.class).getState()) {
         double width = scaledResolution.getScaledWidth() - 2;
         float[] var10000 = new float[]{0.125F, 0.125F, 0.125F};
         if (Main.instance.moduleManager.getModuleByClass(ArreyList.class).getState()) {
            ArrayList<Module> sortedList = new ArrayList(Main.instance.moduleManager.getModules());
            List<Module> enabledModules = sortedList.stream().filter(Module::getState).sorted(Comparator.comparingDouble((e) -> {
               return -mc.fontRenderer.getStringWidth(e.getDisplayName());
            })).collect(Collectors.toList());
            float yDist = 4.0F;
            int yTotal = 0;

            int i;
            for(i = 0; i < enabledModules.size(); ++i) {
               yTotal += mc.fontRenderer.getHeight() + 5;
            }

            i = 0;

            for(int sortedListSize = enabledModules.size(); i < sortedListSize; ++i) {
               Module module = enabledModules.get(i);
               if (!module.getDisplayName().equals("ClickGui")) {
                  Translate translate = module.getTranslate();
                  String moduleLabel = module.getDisplayName();
                  float listOffset = 10.0F;
                  float length = (float)mc.fontRenderer.getStringWidth(moduleLabel);
                  float featureX = (float)(width - (double)length);
                  boolean enable = module.getState() && module.visible;
                  if (enable) {
                     translate.interpolate(featureX, yDist, 0.05D * Minecraft.frameTime / 5.0D);
                  } else {
                     translate.interpolate(width + 3.0D, yDist, 0.05D * Minecraft.frameTime / 5.0D);
                  }

                  Minecraft var48;
                  double var49;
                  label162: {
                     var48 = mc;
                     if (!Minecraft.player.isPotionActive(MobEffects.SPEED)) {
                        var48 = mc;
                        if (!Minecraft.player.isPotionActive(MobEffects.STRENGTH)) {
                           var48 = mc;
                           if (!Minecraft.player.isPotionActive(MobEffects.FIRE_RESISTANCE)) {
                              var49 = 0.0D;
                              break label162;
                           }
                        }
                     }

                     var49 = 24.0D;
                  }

                  double potionCheck = var49;
                  double translateX = translate.getX() - 2.5D;
                  double translateY = translate.getY() + (!Main.instance.moduleManager.getModuleByClass(NoPotionDebug.class).getState() ? potionCheck : 0.0D);
                  int color = 0;
                  double d = ArreyList.red1.getValDouble();
                  double d1 = ArreyList.green1.getValDouble();
                  double d2 = ArreyList.blue1.getValDouble();
                  double f = ArreyList.red2.getValDouble();
                  double f1 = ArreyList.green2.getValDouble();
                  double f2 = ArreyList.blue2.getValInt();
                  double time = ArreyList.time.getValDouble();
                  String mode = Main.instance.setmgr.getSettingByName("ArrayList Color").getValString();
                  String var40 = mode.toLowerCase();
                  byte var41 = -1;
                  switch(var40.hashCode()) {
                  case -1349088399:
                     if (var40.equals("custom")) {
                        var41 = 6;
                     }
                     break;
                  case -832025578:
                     if (var40.equals("red-blue")) {
                        var41 = 7;
                     }
                     break;
                  case -703561496:
                     if (var40.equals("astolfo")) {
                        var41 = 2;
                     }
                     break;
                  case -316348666:
                     if (var40.equals("greenwhite")) {
                        var41 = 1;
                     }
                     break;
                  case -150969892:
                     if (var40.equals("yellastolfo")) {
                        var41 = 5;
                     }
                     break;
                  case 3387192:
                     if (var40.equals("none")) {
                        var41 = 9;
                     }
                     break;
                  case 50511102:
                     if (var40.equals("category")) {
                        var41 = 10;
                     }
                     break;
                  case 98615627:
                     if (var40.equals("grape")) {
                        var41 = 8;
                     }
                     break;
                  case 107027353:
                     if (var40.equals("pulse")) {
                        var41 = 4;
                     }
                     break;
                  case 113101865:
                     if (var40.equals("white")) {
                        var41 = 3;
                     }
                     break;
                  case 973576630:
                     if (var40.equals("rainbow")) {
                        var41 = 0;
                     }
                  }

                  switch(var41) {
                  case 0:
                     color = ColorUtils.rainbowNew((int)(yDist * 200.0F * 0.1F), 0.8F, 1.0F);
                     break;
                  case 1:
                     color = ColorUtils.TwoColoreffect(new Color(255, 255, 255), new Color(0, 150, 150), (double)Math.abs(System.currentTimeMillis() / 10L) / 100.0D + 3.0D * (double)yDist * 2.55D / 60.0D).getRGB();
                     break;
                  case 2:
                     color = ColorUtils.astolfoColors((int)yDist, yTotal);
                     break;
                  case 3:
                     color = ColorUtils.TwoColoreffect(new Color(255, 255, 255), new Color(170, 170, 170), (double)Math.abs(System.currentTimeMillis() / 20L) / 100.0D + 3.0D * (double)yDist * 2.55D / 60.0D).getRGB();
                     break;
                  case 4:
                     color = ColorUtils.TwoColoreffect(new Color(0, 255, 255), new Color(0, 150, 255), (double)Math.abs(System.currentTimeMillis() / 10L) / 100.0D + 3.0D * (double)yDist * 2.55D / 60.0D).getRGB();
                     break;
                  case 5:
                     color = ColorUtils.Yellowastolfo((int)yDist, (float)yTotal);
                     break;
                  case 6:
                     color = ColorUtils.TwoColoreffect(new Color((int)d, (int)d1, (int)d2), new Color((int)f, (int)f1, (int)f2), (double)Math.abs(System.currentTimeMillis() / (long)time) / 100.0D + 3.0D * (double)yDist * 2.55D / 60.0D).getRGB();
                     break;
                  case 7:
                     color = ColorUtils.TwoColoreffect(new Color(255, 25, 25), new Color(0, 150, 255), (double)Math.abs(System.currentTimeMillis() / 20L) / 100.0D + 3.0D * (double)yDist * 2.55D / 60.0D).getRGB();
                     break;
                  case 8:
                     color = ColorUtils.TwoColoreffect(new Color(155, 55, 255), new Color(155, 100, 200), (double)Math.abs(System.currentTimeMillis() / 10L) / 100.0D + 0.1275D).getRGB();
                     break;
                  case 9:
                     color = -1;
                     break;
                  case 10:
                     color = module.getCategory().getColor();
                  }

                  int back = (int)Main.instance.setmgr.getSettingByName("BackgroundAplha").getValDouble();
                  int back2 = (int)Main.instance.setmgr.getSettingByName("BackgroundBright").getValDouble();
                  int nextIndex = enabledModules.indexOf(module) + 1;
                  if (ArreyList.backGround.getValue()) {
                     RenderUtil.drawNewRect(translateX - 2.0D, translateY - 1.0D, width, translateY + (double)listOffset - 1.0D, ColorUtils.getColor(back, back2));
                  }

                  double rightCheck;
                  if (ArreyList.rectTop.getValue()) {
                     label198: {
                        var48 = mc;
                        if (!Minecraft.player.isPotionActive(MobEffects.SPEED)) {
                           var48 = mc;
                           if (!Minecraft.player.isPotionActive(MobEffects.STRENGTH)) {
                              var48 = mc;
                              if (!Minecraft.player.isPotionActive(MobEffects.FIRE_RESISTANCE) || Main.instance.moduleManager.getModuleByClass(NoPotionDebug.class).getState()) {
                                 rightCheck = ArreyList.rectRight.getValue() ? 1.0D : 0.0D;
                                 double borderCheck1 = ArreyList.border.getValue() ? 2.5D : 1.5D;
                                 RenderUtil.drawNewRect(translateX - borderCheck1, 2.2D, width + rightCheck, 3.0D, color);
                                 break label198;
                              }
                           }
                        }

                        if (Main.instance.moduleManager.getModuleByClass(NoPotionDebug.class).getState()) {
                           RenderUtil.drawNewRect(translateX - 2.5D, 2.2D, width + 1.0D, 3.0D, color);
                        }
                     }
                  }

                  if (ArreyList.rectRight.getValue()) {
                     rightCheck = ArreyList.border.getValue() ? 9.5D : 9.0D;
                     RenderUtil.drawNewRect(width, translateY - 1.0D, width + 1.0D, translateY + rightCheck, color);
                  }

                  Module nextModule = null;
                  if (enabledModules.size() > nextIndex) {
                     nextModule = getNextEnabledModule((ArrayList)enabledModules, nextIndex);
                  }

                  if (ArreyList.border.getValue()) {
                     RenderUtil.drawNewRect(translateX - 2.6D, translateY - 1.0D, translateX - 2.0D, translateY + (double)listOffset - 1.0D, color);
                  }

                  double offsetY = listOffset;
                  if (nextModule != null) {
                     double dif = length - (float)mc.fontRenderer.getStringWidth(nextModule.getDisplayName());
                     if (ArreyList.border.getValue()) {
                        RenderUtil.drawNewRect(translateX - 2.6D, translateY + offsetY - 1.0D, translateX - 2.6D + dif, translateY + offsetY - 0.6D, color);
                     }
                  } else if (ArreyList.border.getValue()) {
                     RenderUtil.drawNewRect(translateX - 2.6D, translateY + offsetY - 1.0D, width, translateY + offsetY - 0.6D, color);
                  }

                  mc.fontRenderer.drawStringWithShadow(moduleLabel, (float)translateX, (float)translateY + 1.0F, color);
                  if (module.getState() && module.visible) {
                     yDist += listOffset;
                  }
               }
            }
         }
      }

   }

   private static Module getNextEnabledModule(ArrayList<Module> modules, int startingIndex) {
      int i = startingIndex;

      for(int modulesSize = modules.size(); i < modulesSize; ++i) {
         Module module = modules.get(i);
         if (module.getState() && module.getDisplayName() != "ClickGui") {
            return module;
         }
      }

      return null;
   }

   public static int rainbow(int delay, long index) {
      double rainbowState = Math.ceil((double)(System.currentTimeMillis() + index + (long)delay)) / 15.0D;
      rainbowState %= 360.0D;
      return Color.getHSBColor((float)(rainbowState / 360.0D), 0.4F, 1.0F).getRGB();
   }

   private void renderPotions(ScaledResolution scaledResolution) {
      int xd = mc.currentScreen instanceof GuiChat ? scaledResolution.getScaledHeight() - 30 : scaledResolution.getScaledHeight() - 20;
      int potionY = 11;
      Minecraft var10002 = mc;
      new ArrayList(Minecraft.player.getActivePotionEffects());
      List<PotionEffect> potions = new ArrayList();
      Minecraft var10000 = mc;
      Iterator var6 = Minecraft.player.getActivePotionEffects().iterator();

      while(var6.hasNext()) {
         Object o = var6.next();
         potions.add((PotionEffect)o);
      }

      potions.sort(Comparator.comparingDouble((effect) -> {
         return Minecraft.getMinecraft().fontRenderer.getStringWidth(effect.getPotion().getName() + effect.getAmplifier() + Potion.getPotionDurationString(effect, 1.0F));
      }));

      for(var6 = potions.iterator(); var6.hasNext(); potionY += 11) {
         PotionEffect potionEffect = (PotionEffect)var6.next();
         String effectName = I18n.format(potionEffect.getPotion().getName());
         if (potionEffect.getAmplifier() == 1) {
            effectName = effectName + " " + I18n.format("enchantment.level.2");
         } else if (potionEffect.getAmplifier() == 2) {
            effectName = effectName + " " + I18n.format("enchantment.level.3");
         } else if (potionEffect.getAmplifier() == 3) {
            effectName = effectName + " " + I18n.format("enchantment.level.4");
         }

         String finalName = effectName + "§7 " + Potion.getPotionDurationString(potionEffect, 1.0F);
         float x = (float)(scaledResolution.getScaledWidth() - Minecraft.getMinecraft().fontRenderer.getStringWidth(finalName) - 4);
         float y2 = (float)(xd - potionY);
         Minecraft.getMinecraft().fontRenderer.drawStringWithShadow(finalName, x, y2, potionEffect.getPotion().getLiquidColor());
      }

   }
}
