package de.violence.module.modules.OTHER;

import de.violence.Violence;
import de.violence.friend.FriendManager;
import de.violence.module.Module;
import de.violence.module.ui.Category;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import org.lwjgl.input.Mouse;

public class MidClickFriends extends Module {
   public MidClickFriends() {
      super("MidClickFriends", Category.OTHER);
   }

   public void onUpdate() {
      if(this.mc.objectMouseOver != null) {
         Entity onPoint = this.mc.objectMouseOver.entityHit;
         if((onPoint != null || onPoint != null && onPoint instanceof EntityPlayer) && Mouse.isButtonDown(2)) {
            if(FriendManager.getAliasOf(onPoint.getName()) != null) {
               FriendManager.getFriendList().remove(onPoint.getName());
               Violence.getViolence().sendChat("Removed friend: " + onPoint.getName());
            } else {
               FriendManager.getFriendList().put(onPoint.getName(), "Friend");
               Violence.getViolence().sendChat("Added friend: " + onPoint.getName());
            }

            try {
               Mouse.destroy();
               Mouse.create();
            } catch (Exception var3) {
               ;
            }
         }
      }

      super.onUpdate();
   }
}
