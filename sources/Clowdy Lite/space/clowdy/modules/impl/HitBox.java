package space.clowdy.modules.impl;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraftforge.client.event.RenderPlayerEvent;
import net.minecraftforge.client.event.InputEvent.KeyInputEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import space.clowdy.modules.Category;
import space.clowdy.modules.Module;

public class HitBox extends Module {
     public static double hitboxValue = 0.5D;

     public void onDisable() {
          super.onDisable();
     }

     @SubscribeEvent
     public void は(KeyInputEvent keyInputEvent) {
          if (keyInputEvent.getKey() == 264 && keyInputEvent.getAction() == 1) {
               hitboxValue -= 0.05D;
          }

          if (keyInputEvent.getKey() == 265 && keyInputEvent.getAction() == 1) {
               hitboxValue += 0.05D;
          }

     }

     public void onEnable() {
          super.onEnable();
     }

     public HitBox() {
          super("HitBox", "#25;8G8205B E8B1>:AK 83@>:>2", 0, Category.GHOST);
     }

     @SubscribeEvent
     public void ぱ(RenderPlayerEvent renderPlayerEvent) {
          Entity entity3 = renderPlayerEvent.getEntity();
          if (entity3 != Minecraft.getInstance().player) {
               entity3.setBoundingBox(new AxisAlignedBB(entity3.getPosX() - hitboxValue, entity3.getBoundingBox().minY, entity3.getPosZ() - hitboxValue, entity3.getPosX() + hitboxValue, entity3.getBoundingBox().maxY, entity3.getPosZ() + hitboxValue));
          }

     }
}
