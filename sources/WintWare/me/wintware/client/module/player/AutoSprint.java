package me.wintware.client.module.player;

import me.wintware.client.Main;
import me.wintware.client.event.EventTarget;
import me.wintware.client.event.impl.EventUpdate;
import me.wintware.client.module.Category;
import me.wintware.client.module.Module;
import me.wintware.client.module.combat.KillAura;
import me.wintware.client.module.movement.Scaffold;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;

public class AutoSprint extends Module {
   public AutoSprint() {
      super("AutoSprint", Category.Movement);
   }

   @EventTarget
   public void onUpdate(EventUpdate event) {
      if ((!Main.instance.moduleManager.getModuleByClass(Scaffold.class).getState() || !Main.instance.setmgr.getSettingByName("SprintOFF").getValue()) && (!Main.instance.moduleManager.getModuleByClass(KillAura.class).getState() || !Main.instance.setmgr.getSettingByName("Stop Sprinting").getValue() || KillAura.target == null)) {
         Minecraft var10000 = mc;
         Minecraft var10001 = mc;
         Minecraft.player.setSprinting(Minecraft.player.isMoving());
      }

   }

   public void onDisable() {
      EntityPlayerSP var1;
      boolean var2;
      label16: {
         label15: {
            super.onDisable();
            Minecraft var10000 = mc;
            var1 = Minecraft.player;
            Minecraft var10001 = mc;
            if (Minecraft.player.moveForward > 0.0F) {
               var10001 = mc;
               if (!Minecraft.player.isCollidedHorizontally) {
                  break label15;
               }
            }

            var10001 = mc;
            if (!Minecraft.player.isSprinting()) {
               var2 = false;
               break label16;
            }
         }

         var2 = true;
      }

      var1.setSprinting(var2);
   }
}
