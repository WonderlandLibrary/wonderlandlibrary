package net.caffeinemc.phosphor.module.modules.combat;

import net.caffeinemc.phosphor.api.event.events.TickEvent;
import net.caffeinemc.phosphor.api.event.orbit.EventHandler;
import net.caffeinemc.phosphor.api.util.InvUtils;
import net.caffeinemc.phosphor.api.util.KeyUtils;
import net.caffeinemc.phosphor.module.Module;
import net.caffeinemc.phosphor.module.setting.settings.KeybindSetting;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.ItemStack;

public class AutoArmorModule extends Module {
    public final KeybindSetting activateKey = new KeybindSetting("Activate Key", -1, this);

    public AutoArmorModule() {
        super("AutoArmor", "Automatically equip best armor from inventory", Category.COMBAT);
    }

    public boolean searchArmor() {
        return KeyUtils.isKeyPressed(activateKey.getKeyCode());
    }

    @EventHandler
    private void onPreTick(TickEvent.Pre event) {
        if (mc.player == null) return;

        if (!searchArmor()) return;

        PlayerInventory playerInventory = mc.player.getInventory();

        ItemStack[] bestArmor = new ItemStack[] {
            playerInventory.armor.get(3),
            playerInventory.armor.get(2),
            playerInventory.armor.get(1),
            playerInventory.armor.get(0)
        };

        int[] bestArmorSlot = new int[] {
                36,
                37,
                38,
                39
        };

        for (int slot = 0; slot < playerInventory.main.size(); slot++) {
            ItemStack itemStack = playerInventory.getStack(slot);

            if (itemStack.getItem() instanceof ArmorItem armorItem) {
                int armorSlot;

                switch (armorItem.getType()) {
                    case HELMET -> armorSlot = 3;
                    case CHESTPLATE -> armorSlot = 2;
                    case LEGGINGS -> armorSlot = 1;
                    case BOOTS -> armorSlot = 0;
                    default -> armorSlot = -1;
                }

                int index = 3 - armorSlot;

                ItemStack bestArmorPiece = bestArmor[index];

                if (bestArmorPiece.getItem() instanceof ArmorItem) {
                    if (InvUtils.isArmorBetter(bestArmorPiece, itemStack)) {
                        bestArmor[index] = itemStack;
                        bestArmorSlot[index] = slot;
                    }
                } else {
                    bestArmor[index] = itemStack;
                    bestArmorSlot[index] = slot;
                }
            }
        }

        for (int i = 0; i < 4; i++) {
            if (!bestArmor[i].equals(playerInventory.armor.get(i))) {
                InvUtils.move().from(bestArmorSlot[i]).toArmor(Math.abs(i - 3));
            }
        }
    }
}
