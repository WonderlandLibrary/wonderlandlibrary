package me.wintware.client.module.player;

import java.util.ArrayList;
import me.wintware.client.Main;
import me.wintware.client.clickgui.settings.Setting;
import me.wintware.client.event.EventTarget;
import me.wintware.client.event.impl.MoveEvent;
import me.wintware.client.module.Category;
import me.wintware.client.module.Module;
import me.wintware.client.utils.movement.MovementUtil;
import net.minecraft.client.Minecraft;

public class NoWeb extends Module {
   public NoWeb() {
      super("NoWeb", Category.Player);
      ArrayList<String> web = new ArrayList();
      web.add("NCP");
      web.add("Matrix");
      Main.instance.setmgr.rSetting(new Setting("NoWeb Mode", this, "Matrix", web));
   }

   @EventTarget
   public void onPreMotion(MoveEvent event) {
      if (this.getState()) {
         String mode = Main.instance.setmgr.getSettingByName("NoWeb Mode").getValString();
         Minecraft var10000;
         if (mode.equalsIgnoreCase("Matrix")) {
            label57: {
               var10000 = mc;
               if (Minecraft.player.onGround) {
                  var10000 = mc;
                  if (Minecraft.player.isInWeb) {
                     var10000 = mc;
                     Minecraft.player.isInWeb = true;
                     break label57;
                  }
               }

               if (mc.gameSettings.keyBindJump.isKeyDown()) {
                  return;
               }

               var10000 = mc;
               Minecraft.player.isInWeb = false;
            }

            var10000 = mc;
            if (Minecraft.player.isInWeb && !mc.gameSettings.keyBindSneak.isKeyDown()) {
               MovementUtil.setMotionEvent(event, 0.483D);
            }
         }

         if (mode.equalsIgnoreCase("NCP")) {
            var10000 = mc;
            if (Minecraft.player.isInWeb) {
               var10000 = mc;
               Minecraft.player.isInWeb = false;
               var10000 = mc;
               Minecraft var10001 = mc;
               float yaw = Minecraft.player.rotationYaw + (float)(Minecraft.player.moveForward < 0.0F ? 180 : 0);
               var10000 = mc;
               float lol;
               float xd;
               if (Minecraft.player.moveStrafing > 0.0F) {
                  var10000 = mc;
                  if (Minecraft.player.moveForward < 0.0F) {
                     xd = -0.5F;
                  } else {
                     var10000 = mc;
                     xd = Minecraft.player.moveForward > 0.0F ? 0.4F : 1.0F;
                  }

                  lol = -90.0F * xd;
               } else {
                  lol = 0.0F;
               }

               xd = yaw + lol;
               float xz = (float)Math.cos((double)(xd + 90.0F) * 3.141592653589793D / 180.0D);
               float var7 = (float)Math.sin((double)(xd + 90.0F) * 3.141592653589793D / 180.0D);
            }
         }
      }

   }
}
