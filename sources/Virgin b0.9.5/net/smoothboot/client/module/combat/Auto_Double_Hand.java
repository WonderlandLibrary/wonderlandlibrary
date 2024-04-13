package net.smoothboot.client.module.combat;

import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.smoothboot.client.events.Event;
import net.smoothboot.client.module.Mod;
import net.smoothboot.client.module.settings.BooleanSetting;
import net.smoothboot.client.module.settings.NumberSetting;
import net.smoothboot.client.util.InventoryUtil;

public class Auto_Double_Hand extends Mod {

    public NumberSetting DhandHealth = new NumberSetting("Health", 0, 20, 2, 1);
    public BooleanSetting checkOffhand = new BooleanSetting("Check offhand", true);
    public BooleanSetting checkCrystal = new BooleanSetting("Check crystal", true);
    public BooleanSetting checkItem = new BooleanSetting("Check item", false);

    protected MinecraftClient mc = MinecraftClient.getInstance();

    public Auto_Double_Hand() {
        super("Auto Double Hand", "Switches to totem slot when about to die", Category.Combat);
        addsettings(DhandHealth, checkItem);
    }

    private boolean swapped = false;

    private float store = 0;

    private boolean isTotem(ItemStack stack) {
        return stack.getItem() == Items.TOTEM_OF_UNDYING;
    }

    @Override
    public void onTick() {
        PlayerInventory inv = mc.player.getInventory();
        ItemStack totem = new ItemStack(Items.TOTEM_OF_UNDYING);
        int slot = inv.getSlotWithStack(totem);

        if (checkItem.isEnabled() && mc.player.isUsingItem()) {
            return;
        }
        if (store != mc.player.getHealth()) {
            store = 0;
            swapped = false;
        }
        if (!swapped && checkOffhand.isEnabled() && mc.player.getHealth() <= DhandHealth.getValueFloat()) {
            if (!isTotem(inv.getMainHandStack())) {
                    InventoryUtil.selectItemFromHotbar(Items.TOTEM_OF_UNDYING);
                    store = mc.player.getHealth();
                    swapped = true;
                if (!swapped && !checkOffhand.isEnabled() && mc.player.getHealth() <= DhandHealth.getValueFloat()) {
                if (!isTotem(inv.getStack(40))) {
                    InventoryUtil.selectItemFromHotbar(Items.TOTEM_OF_UNDYING);
                    store = mc.player.getHealth();
                    swapped = true;

                    }
                }
            }
        }
    }


}