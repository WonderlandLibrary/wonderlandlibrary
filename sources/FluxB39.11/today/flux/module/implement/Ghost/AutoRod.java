package today.flux.module.implement.Ghost;

import com.darkmagician6.eventapi.EventTarget;
import com.google.common.collect.Multimap;
import net.minecraft.block.Block;
import net.minecraft.block.BlockAir;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EnumCreatureAttribute;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import today.flux.event.PreUpdateEvent;
import today.flux.module.Category;
import today.flux.module.Module;
import today.flux.module.value.BooleanValue;
import today.flux.module.value.FloatValue;
import today.flux.utility.TimeHelper;

import java.util.Iterator;
import java.util.Map;

public class AutoRod extends Module {

    private final TimeHelper time = new TimeHelper();
    private final TimeHelper time2 = new TimeHelper();

    private Boolean switchBack = false;
    private Boolean useRod = false;

    public static FloatValue delay = new FloatValue("AutoRod", "Delay", 100f, 50f, 1000f, 50f);
    public static BooleanValue disable = new BooleanValue("AutoRod", "Auto Disable", false);


    public AutoRod() {
        super("AutoRod", Category.Ghost, false);
    }


    @EventTarget
    private void onUpdate(PreUpdateEvent event) {
        int item = Item.getIdFromItem(mc.thePlayer.getHeldItem().getItem());
        float rodDelay = delay.getValue();

        if (mc.currentScreen != null) {
            return;
        }

        if (!disable.getValue()) {
            if (!useRod && item == 346) {
                Rod();
                useRod = true;
            }

            if (time.isDelayComplete(rodDelay - 50) && switchBack) {
                switchBack();
                switchBack = false;
            }

            if (time.isDelayComplete(rodDelay) && useRod) {
                useRod = false;
            }
        } else {
            if (item == 346) {
                if (time2.isDelayComplete(rodDelay + 200)) {
                    Rod();
                    time2.reset();
                }

                if (time.isDelayComplete(rodDelay)) {
                    mc.thePlayer.inventory.currentItem = bestWeapon(mc.thePlayer);
                    time.reset();
                    toggle();
                }
            } else if (time.isDelayComplete(100)) {
                switchToRod();
                time.reset();
            }
        }
    }


    private int findRod(int startSlot, int endSlot) {
        for (int i = startSlot; i < endSlot; i++) {
            ItemStack stack = mc.thePlayer.inventoryContainer.getSlot(i).getStack();

            if (stack != null && stack.getItem() == Items.fishing_rod) {
                return i;
            }
        }

        return -1;
    }

    private void switchToRod() {
        for (int i = 36; i < 45; i++) {
            ItemStack itemstack = mc.thePlayer.inventoryContainer.getSlot(i).getStack();

            if (itemstack != null && Item.getIdFromItem(itemstack.getItem()) == 346) {
                mc.thePlayer.inventory.currentItem = i - 36;
                break;
            }
        }
    }

    private void Rod() {
        int rod = findRod(36, 45);
        mc.playerController.sendUseItem(mc.thePlayer, mc.theWorld, mc.thePlayer.inventoryContainer.getSlot(rod).getStack());
        switchBack = true;
        time.reset();
    }

    private void switchBack() {
        mc.thePlayer.inventory.currentItem = bestWeapon(mc.thePlayer);
    }

    public static int bestWeapon(Entity target) {
        int firstSlot = mc.thePlayer.inventory.currentItem = 0;
        int bestWeapon = -1;
        int j = 1;

        for (byte i = 0; i < 9; i++) {
            mc.thePlayer.inventory.currentItem = i;
            ItemStack itemStack = mc.thePlayer.getHeldItem();

            if (itemStack != null) {
                int itemAtkDamage = (int) getItemAtkDamage(itemStack);
                itemAtkDamage += EnchantmentHelper.getModifierForCreature(itemStack, EnumCreatureAttribute.UNDEFINED);

                if (itemAtkDamage > j) {
                    j = itemAtkDamage;
                    bestWeapon = i;
                }
            }
        }

        if (bestWeapon != -1) {
            return bestWeapon;
        } else {
            return firstSlot;
        }
    }


    public static float getItemAtkDamage(ItemStack itemStack) {
        final Multimap multimap = itemStack.getAttributeModifiers();
        if (!multimap.isEmpty()) {
            final Iterator iterator = multimap.entries().iterator();
            if (iterator.hasNext()) {
                final Map.Entry entry = (Map.Entry) iterator.next();
                final AttributeModifier attributeModifier = (AttributeModifier) entry.getValue();
                double damage = attributeModifier.getOperation() != 1 && attributeModifier.getOperation() != 2 ? attributeModifier.getAmount() : attributeModifier.getAmount() * 100.0;

                if (attributeModifier.getAmount() > 1.0) {
                    return 1.0f + (float) damage;
                }
                return 1.0f;
            }
        }
        return 1.0f;
    }

}
