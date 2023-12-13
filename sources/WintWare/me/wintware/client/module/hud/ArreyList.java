package me.wintware.client.module.hud;

import java.util.ArrayList;
import me.wintware.client.Main;
import me.wintware.client.clickgui.settings.Setting;
import me.wintware.client.event.EventTarget;
import me.wintware.client.event.impl.EventRender2D;
import me.wintware.client.event.impl.EventUpdate;
import me.wintware.client.module.Category;
import me.wintware.client.module.Module;

public class ArreyList extends Module {
   public static Setting backGround;
   public static Setting border;
   public static Setting rectTop;
   public static Setting rectRight;
   public static Setting red;
   public static Setting green;
   public static Setting blue;
   public static Setting red1;
   public static Setting green1;
   public static Setting blue1;
   public static Setting red2;
   public static Setting green2;
   public static Setting blue2;
   public static Setting time;

   public ArreyList() {
      super("ArrayList", Category.Hud);
      ArrayList<String> color = new ArrayList();
      color.add("Custom");
      color.add("Rainbow");
      color.add("GreenWhite");
      color.add("White");
      color.add("Pulse");
      color.add("Astolfo");
      color.add("YellAstolfo");
      color.add("Red-Blue");
      color.add("Grape");
      color.add("None");
      color.add("Category");
      Main.instance.setmgr.rSetting(new Setting("ArrayList Color", this, "Astolfo", color));
      Main.instance.setmgr.rSetting(backGround = new Setting("Background", this, true));
      Main.instance.setmgr.rSetting(border = new Setting("Border", this, true));
      Main.instance.setmgr.rSetting(rectTop = new Setting("RectTop", this, true));
      Main.instance.setmgr.rSetting(rectRight = new Setting("RectRight", this, true));
      Main.instance.setmgr.rSetting(new Setting("BackgroundAplha", this, 35.0D, 1.0D, 255.0D, false));
      Main.instance.setmgr.rSetting(new Setting("BackgroundBright", this, 35.0D, 1.0D, 255.0D, false));
      Main.instance.setmgr.rSetting(red1 = new Setting("Custom One Red", this, 255.0D, 0.0D, 255.0D, false));
      Main.instance.setmgr.rSetting(green1 = new Setting("Custom One Green", this, 255.0D, 0.0D, 255.0D, false));
      Main.instance.setmgr.rSetting(blue1 = new Setting("Custom One Blue", this, 255.0D, 0.0D, 255.0D, false));
      Main.instance.setmgr.rSetting(red2 = new Setting("Custom Two Red", this, 255.0D, 0.0D, 255.0D, false));
      Main.instance.setmgr.rSetting(green2 = new Setting("Custom Two Green", this, 255.0D, 0.0D, 255.0D, false));
      Main.instance.setmgr.rSetting(blue2 = new Setting("Custom Two Blue", this, 255.0D, 0.0D, 255.0D, false));
      Main.instance.setmgr.rSetting(time = new Setting("Custom Color Time", this, 10.0D, 1.0D, 100.0D, false));
   }

   @EventTarget
   public void onUpdate(EventUpdate event) {
      String mode = Main.instance.setmgr.getSettingByName("ArrayList Color").getValString();
      this.setSuffix(mode);
   }

   @EventTarget
   public void onRender2D(EventRender2D e) {
      HUD hud = new HUD();
      hud.renderArrayList(e.getResolution());
   }
}
