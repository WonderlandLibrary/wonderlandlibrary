package org.alphacentauri.core;

import org.alphacentauri.AC;
import org.alphacentauri.management.io.ConfigFile;
import org.alphacentauri.management.sexyness.ingame.WatermarkRenderer;

public class CoreConfig {
   ConfigFile cfg = new ConfigFile("core");

   public void save() {
      this.cfg.save();
   }

   public ConfigFile getConfig() {
      return this.cfg;
   }

   public boolean isLSD() {
      return this.cfg.getBoolean("lsd");
   }

   public boolean isInventoryTransparent() {
      return !this.cfg.contains("invtransparent") || this.cfg.getBoolean("invtransparent");
   }

   public String getPrefix() {
      String prefix = this.cfg.get("cmd_prefix");
      if(prefix == null) {
         this.setPrefix(".");
         prefix = ".";
      }

      return prefix;
   }

   public int getTabGuiColorSelectedBG() {
      return !this.cfg.contains("tabgui_color_selectedBG")?-14704705:this.cfg.getInt("tabgui_color_selectedBG");
   }

   public int getTabGuiColorSelectedFG() {
      return !this.cfg.contains("tabgui_color_selectedFG")?-1:this.cfg.getInt("tabgui_color_selectedFG");
   }

   public void setMidClickEnabled(boolean enabled) {
      this.cfg.set("midclickfriends", (Object)Boolean.valueOf(enabled));
   }

   public int getTabGuiColorUnselectedBG() {
      return !this.cfg.contains("tabgui_color_unselectedBG")?-2013265920:this.cfg.getInt("tabgui_color_unselectedBG");
   }

   public void setTabGuiColorSelectedBG(int value) {
      this.cfg.set("tabgui_color_selectedBG", (Object)Integer.valueOf(value));
   }

   public void setWatermarkDesign(int designID) {
      this.cfg.set("watermark_design", (Object)Integer.valueOf(designID));
   }

   public int getWatermarkDesign() {
      return !this.cfg.contains("watermark_design")?1:this.cfg.getInt("watermark_design");
   }

   public void setWatermarkColor(int color) {
      this.cfg.set("watermark_color", (Object)Integer.valueOf(color));
   }

   public void setTabGuiColorUnselectedFG(int value) {
      this.cfg.set("tabgui_color_unselectedFG", (Object)Integer.valueOf(value));
   }

   public void setToggleMessageVisible(boolean visible) {
      this.cfg.set("toggle_msg", (Object)Boolean.valueOf(visible));
   }

   public int getWatermarkColor() {
      return !this.cfg.contains("watermark_color")?-7829368:this.cfg.getInt("watermark_color");
   }

   public void setInventoryTransparent(boolean b) {
      this.cfg.set("invtransparent", (Object)Boolean.valueOf(b));
   }

   public void setTabGuiColorUnselectedBG(int value) {
      this.cfg.set("tabgui_color_unselectedBG", (Object)Integer.valueOf(value));
   }

   public boolean isMidClickFriendsEnabled() {
      return !this.cfg.contains("midclickfriends")?true:this.cfg.getBoolean("midclickfriends");
   }

   public int getTabGuiColorUnselectedFG() {
      return !this.cfg.contains("tabgui_color_unselectedFG")?-1:this.cfg.getInt("tabgui_color_unselectedFG");
   }

   public void setTabGuiColorSelectedFG(int value) {
      this.cfg.set("tabgui_color_selectedFG", (Object)Integer.valueOf(value));
   }

   public int getOverlayKey() {
      return !this.cfg.contains("overlay_key")?157:this.cfg.getInt("overlay_key");
   }

   public void setClickGuiR(int r) {
      if(r < 0) {
         r = 0;
      }

      if(r > 255) {
         r = 255;
      }

      this.cfg.set("clickgui_color_r", (Object)Integer.valueOf(r));
   }

   public void setLastAccount(String lastAccount) {
      this.cfg.set("last_account", lastAccount);
   }

   public void setTabGuiEnabled(boolean enabled) {
      this.cfg.set("tabgui_enabled", (Object)Boolean.valueOf(enabled));
   }

   public void setPrefix(String prefix) {
      this.cfg.set("cmd_prefix", prefix);
   }

   public int getClickGuiG() {
      return !this.cfg.contains("clickgui_color_g")?0:this.cfg.getInt("clickgui_color_g");
   }

   public void setClickGuiB(int b) {
      if(b < 0) {
         b = 0;
      }

      if(b > 255) {
         b = 255;
      }

      this.cfg.set("clickgui_color_b", (Object)Integer.valueOf(b));
   }

   public void setClickGuiG(int g) {
      if(g < 0) {
         g = 0;
      }

      if(g > 255) {
         g = 255;
      }

      this.cfg.set("clickgui_color_g", (Object)Integer.valueOf(g));
   }

   public boolean showToggleMsg() {
      if(!this.cfg.contains("toggle_msg")) {
         this.cfg.set("toggle_msg", (Object)Boolean.valueOf(true));
      }

      return this.cfg.getBoolean("toggle_msg");
   }

   public void setAutoRecon(boolean autoRecon) {
      this.cfg.set("auto_reconnect", (Object)Boolean.valueOf(autoRecon));
   }

   public String getLastAccount() {
      return this.cfg.get("last_account");
   }

   public int getClickGuiR() {
      return !this.cfg.contains("clickgui_color_r")?0:this.cfg.getInt("clickgui_color_r");
   }

   public boolean isAutoRecon() {
      return !this.cfg.contains("auto_reconnect")?false:this.cfg.getBoolean("auto_reconnect");
   }

   public void setOverlayKey(int keyCode) {
      this.cfg.set("overlay_key", (Object)Integer.valueOf(keyCode));
      AC.getKeyBindManager().kbOverlay.keyCode = keyCode;
   }

   public boolean isTabGuiEnabled() {
      return !this.cfg.contains("tabgui_enabled")?true:this.cfg.getBoolean("tabgui_enabled");
   }

   public int getClickGuiB() {
      return !this.cfg.contains("clickgui_color_b")?0:this.cfg.getInt("clickgui_color_b");
   }

   public void setLSD(boolean lsd) {
      WatermarkRenderer.setLSD(lsd);
      this.cfg.set("lsd", (Object)Boolean.valueOf(lsd));
   }
}
