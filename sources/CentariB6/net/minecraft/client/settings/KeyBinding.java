package net.minecraft.client.settings;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Predicate;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.gui.GuiDisconnected;
import net.minecraft.client.multiplayer.GuiConnecting;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.IntHashMap;
import org.alphacentauri.AC;
import org.alphacentauri.modules.ModuleInvWalk;
import org.lwjgl.input.Keyboard;

public class KeyBinding implements Comparable {
   private static final List keybindArray = Lists.newArrayList();
   private static final IntHashMap hash = new IntHashMap();
   private static final Set keybindSet = Sets.newHashSet();
   private final String keyDescription;
   private final int keyCodeDefault;
   private final String keyCategory;
   private int keyCode;
   public boolean pressed;
   public int pressTime;

   public KeyBinding(String description, int keyCode, String category) {
      this.keyDescription = description;
      this.keyCode = keyCode;
      this.keyCodeDefault = keyCode;
      this.keyCategory = category;
      keybindArray.add(this);
      hash.addKey(keyCode, this);
      keybindSet.add(category);
   }

   public int compareTo(KeyBinding p_compareTo_1_) {
      int i = I18n.format(this.keyCategory, new Object[0]).compareTo(I18n.format(p_compareTo_1_.keyCategory, new Object[0]));
      if(i == 0) {
         i = I18n.format(this.keyDescription, new Object[0]).compareTo(I18n.format(p_compareTo_1_.keyDescription, new Object[0]));
      }

      return i;
   }

   public int getKeyCode() {
      return this.keyCode;
   }

   public boolean isPressed() {
      if(this.pressTime == 0) {
         return false;
      } else {
         --this.pressTime;
         return true;
      }
   }

   public void setKeyCode(int keyCode) {
      this.keyCode = keyCode;
   }

   public static void unPressAllKeys() {
      keybindArray.forEach(KeyBinding::unpressKey);
   }

   public static void setKeyBindState(int keyCode, boolean pressed) {
      if(keyCode != 0) {
         KeyBinding keybinding = (KeyBinding)hash.lookup(keyCode);
         if(keybinding != null) {
            keybinding.pressed = pressed;
         }
      }

   }

   public boolean isKeyDown() {
      return AC.getModuleManager().get(ModuleInvWalk.class).isEnabled() && AC.getMC().currentScreen != null?this.isKeyReallyDown():this.pressed;
   }

   public static void onTick(int keyCode) {
      if(keyCode != 0) {
         KeyBinding keybinding = (KeyBinding)hash.lookup(keyCode);
         if(keybinding != null) {
            ++keybinding.pressTime;
         }
      }

   }

   public static void resetKeyBindingArrayAndHash() {
      hash.clearMap();

      for(KeyBinding keybinding : keybindArray) {
         hash.addKey(keybinding.keyCode, keybinding);
      }

   }

   public String getKeyDescription() {
      return this.keyDescription;
   }

   public int getKeyCodeDefault() {
      return this.keyCodeDefault;
   }

   public static void fixKeybinds() {
      keybindArray.stream().filter(KeyBinding::isKeyReallyDown).forEach((keybinding) -> {
         keybinding.pressed = true;
      });
   }

   private void unpressKey() {
      this.pressTime = 0;
      this.pressed = false;
   }

   public static Set getKeybinds() {
      return keybindSet;
   }

   public String getKeyCategory() {
      return this.keyCategory;
   }

   public boolean isKeyReallyDown() {
      if(this.keyCode < 1) {
         return false;
      } else if(AC.getMC().currentScreen instanceof GuiChat) {
         return false;
      } else if(AC.getMC().currentScreen instanceof GuiConnecting) {
         return false;
      } else if(AC.getMC().currentScreen instanceof GuiDisconnected) {
         return false;
      } else if(this.keyCode == AC.getMC().gameSettings.keyBindSneak.keyCode) {
         return false;
      } else {
         this.pressed = Keyboard.isKeyDown(this.keyCode);
         return this.pressed;
      }
   }
}
