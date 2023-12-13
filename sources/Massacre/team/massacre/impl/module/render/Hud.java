package team.massacre.impl.module.render;

import java.text.DecimalFormat;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import team.massacre.Massacre;
import team.massacre.api.event.api.annotations.Handler;
import team.massacre.api.module.Category;
import team.massacre.api.module.Module;
import team.massacre.api.property.Property;
import team.massacre.api.property.impl.EnumProperty;
import team.massacre.impl.event.EventKeyPress;
import team.massacre.impl.event.EventRender2D;
import team.massacre.utils.HudUtils;

public class Hud extends Module {
   public Property<Boolean> arrayList = new Property("ArrayList", true);
   public Property<Boolean> watermark = new Property("Watermark", true);
   public Property<Boolean> tabGui = new Property("TabGui", true);
   public EnumProperty<Hud.ColorMode> colorMode;
   public EnumProperty<Hud.HudMode> hudMode;
   public EnumProperty<Hud.ClientTheme> clientTheme;
   public Property<Boolean> jinthium;

   public Hud() {
      super("Hud", 0, Category.RENDER);
      this.colorMode = new EnumProperty("Color Mode", Hud.ColorMode.CLIENT);
      this.hudMode = new EnumProperty("Hud Mode", Hud.HudMode.NEW);
      this.clientTheme = new EnumProperty("Client Theme", Hud.ClientTheme.LIGHT);
      this.jinthium = new Property("Jinthium", false, () -> {
         return this.hudMode.getValue() == Hud.HudMode.NEW;
      });
      this.addValues(new Property[]{this.arrayList, this.watermark, this.tabGui, this.jinthium, this.colorMode, this.clientTheme, this.hudMode});
   }

   @Handler
   public void onRender2D(EventRender2D eventRender2D) {
      this.setSuffix(((Hud.HudMode)this.hudMode.getValue()).name());
      HudUtils hudUtils = Massacre.INSTANCE.getHudUtils();
      DecimalFormat decimalFormat = new DecimalFormat("#");
      ScaledResolution scaledResolution = new ScaledResolution(this.mc);
      String bps = String.format("%.2f", Math.hypot(this.mc.thePlayer.posX - this.mc.thePlayer.prevPosX, this.mc.thePlayer.posZ - this.mc.thePlayer.prevPosZ) * (double)this.mc.timer.timerSpeed * 20.0D);
      Gui.drawRect(0, 0, 0, 0, 0);
      if ((Boolean)this.arrayList.getValue()) {
         hudUtils.drawArraylist(scaledResolution);
      }

      if ((Boolean)this.watermark.getValue()) {
         switch((Hud.HudMode)this.hudMode.getValue()) {
         case OLD:
            hudUtils.drawWatermark(scaledResolution);
            break;
         case NEW:
            hudUtils.drawWatermarkNew(scaledResolution);
         }
      }

      hudUtils.drawBuild(scaledResolution, decimalFormat, bps);
      if ((Boolean)this.tabGui.getValue()) {
         switch((Hud.HudMode)this.hudMode.getValue()) {
         case OLD:
            hudUtils.drawTabGui((Boolean)this.watermark.getValue() ? 7.0F : 2.0F, (Boolean)this.watermark.getValue() ? 30.0F : 2.0F);
            break;
         case NEW:
            hudUtils.drawTabGui(2.0F, (Boolean)this.watermark.getValue() ? 16.0F : 2.0F);
         }
      }

   }

   @Handler
   public void onKeyPressed(EventKeyPress eventKeyPress) {
      HudUtils hudUtils = Massacre.INSTANCE.getHudUtils();
      hudUtils.getTabGuiKeys(eventKeyPress);
   }

   public static enum HudMode {
      NEW,
      OLD;
   }

   public static enum ClientTheme {
      DARK,
      LIGHT;
   }

   public static enum ColorMode {
      CLIENT,
      RAINBOW;
   }
}
