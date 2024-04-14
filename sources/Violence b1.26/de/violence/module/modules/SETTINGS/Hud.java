package de.violence.module.modules.SETTINGS;

import de.violence.gui.VSetting;
import de.violence.module.Module;
import de.violence.module.ui.Category;
import java.util.Arrays;

public class Hud extends Module {
   private VSetting customHotbar = new VSetting("Custom Hotbar", this, false, Arrays.asList(new String[]{"Slow Scrolling-Hud", "Hotbar Background-Hud", "Hotbar Image-Hud", "Hotbar Coordinates-Hud"}));
   private VSetting slowScrolling = new VSetting("Slow Scrolling", this, false);
   private VSetting hotbarCoordinates = new VSetting("Hotbar Coordinates", this, false);
   private VSetting hotbarBackground = new VSetting("Hotbar Background", this, false);
   private VSetting hotbarImage = new VSetting("Hotbar Image", this, false);
   private VSetting chatAnimations = new VSetting("Chat Animations", this, false);
   private VSetting bWhite = new VSetting("White Array", this, false);
   private VSetting moduleBackground = new VSetting("Module Background", this, false);
   private VSetting moduleRect = new VSetting("Little Rect", this, false);
   private VSetting hudThemes = new VSetting("Hud Theme", this, Arrays.asList(new String[]{"Simple", "Reliant"}), "Simple");
   private VSetting arrayListTheme = new VSetting("ArrayList Theme", this, Arrays.asList(new String[]{"Minecraft", "Big Noodle Titling", "Arial"}), "Minecraft");

   public Hud() {
      super("Hud", Category.SETTINGS);
   }
}
