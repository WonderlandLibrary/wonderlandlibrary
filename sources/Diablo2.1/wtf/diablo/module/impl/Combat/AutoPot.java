package wtf.diablo.module.impl.Combat;

import com.google.common.eventbus.Subscribe;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.client.Minecraft;
import net.minecraft.item.Item;
import net.minecraft.item.ItemPotion;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.network.play.client.C09PacketHeldItemChange;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import wtf.diablo.events.impl.UpdateEvent;
import wtf.diablo.module.Module;
import wtf.diablo.module.data.Category;
import wtf.diablo.module.data.ServerType;
import wtf.diablo.utils.packet.PacketUtil;
import wtf.diablo.utils.world.EntityUtil;

@Getter
@Setter
public class AutoPot extends Module {
    public AutoPot() {
        super("AutoPot", "P O T I O N", Category.COMBAT, ServerType.All);
    }

    @Subscribe
    public void onUpdate(UpdateEvent e) {
        int savedSlot = mc.thePlayer.inventory.currentItem;
        int slot = -1;
        if (!mc.thePlayer.isPotionActive(Potion.moveSpeed) && mc.thePlayer.onGround) {
            slot = getFirstPotion(Potion.moveSpeed);
            if (slot != -1) {
                EntityUtil.setRotations(e, mc.thePlayer.rotationYaw, 87.5f);
                if (mc.thePlayer.ticksExisted % 20 == 0) {
                    int swapSlot = 5;
                    swap(slot, swapSlot, mc.thePlayer.openContainer.windowId);
                    PacketUtil.sendPacket(new C09PacketHeldItemChange(swapSlot));
                    PacketUtil.sendPacket(new C08PacketPlayerBlockPlacement(mc.thePlayer.inventory.getStackInSlot(1)));
                    PacketUtil.sendPacket(new C09PacketHeldItemChange(savedSlot));
                }
            }
        }
    }

    public int getFirstPotion(Potion effect) {
        int slot = -1;

        for (int i = 0; i < 45; ++i) {
            if (mc.thePlayer.inventoryContainer.getSlot(i).getHasStack()) {
                ItemStack is = mc.thePlayer.inventoryContainer.getSlot(i).getStack();
                Item item = is.getItem();
                if (item instanceof ItemPotion) {
                    ItemPotion pot = (ItemPotion) item;
                    for (PotionEffect e : pot.getEffects(is)) {
                        if (e.getPotionID() == effect.getId()) {
                            slot = i;
                            break;
                        }
                    }
                }
            }
        }
        return slot;
    }

    public static void swap(int slot1, int hotbarSlot, int windowId) {
        if (hotbarSlot == slot1 - 36)
            return;
        (Minecraft.getMinecraft()).playerController.windowClick(windowId, slot1, hotbarSlot, 2, (Minecraft.getMinecraft()).thePlayer);
    }
}
