package cn.hanabi.modules.modules.player;

import aLph4anTi1eaK_cN.Annotation.ObfuscationClass;
import cn.hanabi.events.EventGui;
import cn.hanabi.events.EventUpdate;
import cn.hanabi.modules.Category;
import cn.hanabi.modules.Mod;
import cn.hanabi.value.Value;
import com.darkmagician6.eventapi.EventTarget;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.item.Item;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.network.play.client.C09PacketHeldItemChange;
import org.lwjgl.input.Keyboard;

@ObfuscationClass
public class InvMove extends Mod {
   private final Value mode = new Value("InvMove", "Mode", 0);
   private boolean isWalking;

   public InvMove() {
      super("InvMove", Category.PLAYER);
      this.mode.addValue("Vanilla");
      this.mode.addValue("Hypixel");
   }

   @EventTarget
   public void onWindow(EventGui event) {
      this.setKeyStat();
   }

   @EventTarget
   public void onUpdate(EventUpdate event) {
      if (mc.currentScreen != null && !(mc.currentScreen instanceof GuiChat)) {
         this.isWalking = true;
         if (this.mode.isCurrentMode("Hypixel")) {
            try {
               int i;
               for(i = 0; i < 8; ++i) {
                  ItemStack stack = mc.thePlayer.inventory.getStackInSlot(i);
                  if (stack == null || !(stack.getItem() instanceof ItemFood) && !(stack.getItem() instanceof ItemSword) && Item.getIdFromItem(stack.getItem()) != 345) {
                     break;
                  }
               }

               if (i == 8 && Item.getIdFromItem(mc.thePlayer.inventory.getStackInSlot(8).getItem()) == 345) {
                  --i;
               }

               mc.thePlayer.sendQueue.addToSendQueue(new C09PacketHeldItemChange(i));
            } catch (Exception var4) {
               var4.printStackTrace();
            }
         }

         this.setKeyStat();
      } else if (this.isWalking) {
         if (this.mode.isCurrentMode("Hypixel")) {
            mc.thePlayer.sendQueue.addToSendQueue(new C09PacketHeldItemChange(mc.thePlayer.inventory.currentItem));
         }

         this.isWalking = false;
      }

   }

   private void setKeyStat() {
      KeyBinding[] key = new KeyBinding[]{mc.gameSettings.keyBindForward, mc.gameSettings.keyBindBack, mc.gameSettings.keyBindLeft, mc.gameSettings.keyBindRight, mc.gameSettings.keyBindSprint, mc.gameSettings.keyBindJump};
      KeyBinding[] array = key;
      int length = key.length;

      for(int i = 0; i < length; ++i) {
         KeyBinding b = array[i];
         KeyBinding.setKeyBindState(b.getKeyCode(), Keyboard.isKeyDown(b.getKeyCode()));
      }

   }
}
