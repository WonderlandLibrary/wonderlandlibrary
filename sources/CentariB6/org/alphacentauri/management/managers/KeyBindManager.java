package org.alphacentauri.management.managers;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Map.Entry;
import java.util.function.Consumer;
import java.util.function.Predicate;
import org.alphacentauri.AC;
import org.alphacentauri.management.events.Event;
import org.alphacentauri.management.events.EventGlobalGameLoop;
import org.alphacentauri.management.events.EventListener;
import org.alphacentauri.management.events.EventRender3D;
import org.alphacentauri.management.io.ConfigFile;
import org.alphacentauri.management.keybinds.KeyBind;

public class KeyBindManager implements EventListener {
   public List keyBinds = new ArrayList();
   public KeyBind kbOverlay = new KeyBind(AC.getConfig().getOverlayKey(), "overlay show");
   private ConfigFile keyBindStorage = new ConfigFile("keybinds");

   public KeyBindManager() {
      this.load();
   }

   public void load() {
      for(Entry<String, String> entry : this.keyBindStorage.all()) {
         try {
            int key = Integer.parseInt((String)entry.getValue());
            this.keyBinds.add(new KeyBind(key, (String)entry.getKey()));
         } catch (Exception var4) {
            ;
         }
      }

   }

   private void save() {
      this.keyBindStorage.save();
   }

   public List all() {
      return this.keyBinds;
   }

   public void updateMouseButton(int mbid) {
      this.keyBinds.stream().filter((keyBind) -> {
         return keyBind.keyCode == mbid;
      }).forEach((keyBind) -> {
         keyBind.keyState = true;
      });
   }

   public void preMouse() {
      this.keyBinds.stream().filter((keyBind) -> {
         return keyBind.keyCode < 0;
      }).forEach((keyBind) -> {
         keyBind.oldKeyState = keyBind.keyState;
         keyBind.keyState = false;
      });
   }

   public void onEvent(Event event) {
      if(event instanceof EventRender3D) {
         for(KeyBind keyBind : this.keyBinds) {
            keyBind.update();
            if(keyBind.isPressed()) {
               AC.getCommandManager().execCommand(keyBind.command);
            }
         }
      } else if(event instanceof EventGlobalGameLoop) {
         this.kbOverlay.update();
         if(this.kbOverlay.isPressedIgnoreGUI()) {
            AC.getCommandManager().execCommand(this.kbOverlay.command);
         }
      }

   }

   public void addKeyBind(int key, String command) {
      this.keyBinds.add(new KeyBind(key, command));
      this.keyBindStorage.set(command, String.valueOf(key));
      this.save();
   }

   public void removeKeyBind(int key, String command) {
      for(KeyBind keyBind : this.keyBinds) {
         if(keyBind.keyCode == key && Objects.equals(keyBind.command, command)) {
            this.removeKeyBind(keyBind);
            break;
         }
      }

   }

   public void removeKeyBind(KeyBind keyBind) {
      this.keyBinds.remove(keyBind);
      this.keyBindStorage.set(keyBind.command, "-");
      this.save();
   }

   public void setKeyBind(KeyBind from, KeyBind to) {
      this.removeKeyBind(from);
      this.addKeyBind(to.keyCode, to.command);
   }
}
