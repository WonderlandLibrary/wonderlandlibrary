package club.dortware.client.module.impl.combat;

import club.dortware.client.Client;
import club.dortware.client.manager.impl.PropertyManager;
import club.dortware.client.module.annotations.ModuleData;
import club.dortware.client.module.enums.ModuleCategory;
import club.dortware.client.property.impl.DoubleProperty;
import club.dortware.client.event.impl.UpdateEvent;
import club.dortware.client.module.Module;
import club.dortware.client.util.impl.networking.PacketUtil;
import club.dortware.client.util.impl.time.Stopwatch;
import com.google.common.eventbus.Subscribe;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemPotion;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.network.play.client.C09PacketHeldItemChange;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.BlockPos;

import java.util.List;
@ModuleData(name = "Auto Potion", description = "Automatically pots", category = ModuleCategory.COMBAT)
public class AutoPot extends Module {
    private static boolean isPotting;

    private static final Object[] NULL_OR_BAD_POTION_ARRAY = { false };
    private Stopwatch timer = new Stopwatch();

    @Override
    public void setup() {
        PropertyManager propertyManager = Client.INSTANCE.getPropertyManager();
        propertyManager.add(new DoubleProperty<>("Min Health", this, 12, 5, 20, true));
        propertyManager.add(new DoubleProperty<>("Delay", this, 300, 100, 800));
    }

    @Subscribe
    public void onUpdate(UpdateEvent event) {
        if (!event.isPre()) {
            if (isPotting()) {
                PacketUtil.sendPacketNoEvent(new C09PacketHeldItemChange(1));
                PacketUtil.sendPacketNoEvent(new C08PacketPlayerBlockPlacement(mc.thePlayer.getHeldItem(), new BlockPos(-1, -3, -1)));
                PacketUtil.sendPacketNoEvent(new C03PacketPlayer(mc.thePlayer.onGround));
                PacketUtil.sendPacketNoEvent(new C09PacketHeldItemChange(mc.thePlayer.inventory.currentItem));
                setPotting(false);
            }
            return;
        }
        PropertyManager propertyManager = Client.INSTANCE.getPropertyManager();
        Double minHealth = (Double) propertyManager.getProperty(this, "Min Health").getValue();
        long delay = ((Double) propertyManager.getProperty(this, "Delay").getValue()).longValue();
        if (mc.thePlayer.getHealth() <= minHealth && invCheck()) {
            if (timer.timeElapsed(delay)) {
                refillPotions();
                setPotting(true);
                event.setRotationPitch(90.0F);
                timer.resetTime();
            }
        }
        else {
            if (mc.thePlayer.isPotionActive(2) || !invCheckMisc()) {
                return;
            }
            if (timer.timeElapsed(delay)) {
                refillPotionsMisc();
                setPotting(true);
                event.setRotationPitch(90.0F);
                timer.resetTime();
            }
        }
    };

    private void refillPotions() {
        if (invCheck()) {
            for (int i = 0; i < 45; ++i) {
                Slot slot = mc.thePlayer.inventoryContainer.getSlot(i);
                if (!slot.getHasStack())
                    continue;
                if (i == 37)
                    continue;
                if (getPotionData(slot.getStack()) != NULL_OR_BAD_POTION_ARRAY) {
                    swap(i, 1);
                    break;
                }
            }
        }
    }

    public static void swap(int slot, int hotBarNumber) {
        mc.playerController.windowClick(mc.thePlayer.inventoryContainer.windowId, slot, hotBarNumber, 2, mc.thePlayer);
    }

    private void refillPotionsMisc() {
        if (invCheckMisc()) {
            for (int i = 0; i < 45; ++i) {
                Slot slot = mc.thePlayer.inventoryContainer.getSlot(i);
                if (!slot.getHasStack())
                    continue;
                if (i == 37)
                    continue;
                if (getPotionDataMisc(slot.getStack()) != NULL_OR_BAD_POTION_ARRAY) {
                    swap(i, 1);
                    break;
                }
            }
        }
    }


    public static boolean invCheck() {
        for (int i = 0; i < 45; ++i) {
            if (!mc.thePlayer.inventoryContainer.getSlot(i).getHasStack()
                    || getPotionData(mc.thePlayer.inventoryContainer.getSlot(i).getStack()) == NULL_OR_BAD_POTION_ARRAY)
                continue;
            Object[] dataArray = getPotionData(mc.thePlayer.inventoryContainer.getSlot(i).getStack());
            int data = (int) dataArray[1];
            if (data == Potion.regeneration.id || data == Potion.heal.id) {
                return true;
            }
        }
        return false;
    }

    public static boolean invCheckMisc() {
        for (int i = 0; i < 45; ++i) {
            if (!mc.thePlayer.inventoryContainer.getSlot(i).getHasStack()
                    || getPotionDataMisc(mc.thePlayer.inventoryContainer.getSlot(i).getStack()) == NULL_OR_BAD_POTION_ARRAY)
                continue;
            Object[] dataArray = getPotionDataMisc(mc.thePlayer.inventoryContainer.getSlot(i).getStack());
            int data = (int) dataArray[1];
            if (data == 1) {
                return true;
            }
        }
        return false;
    }

    private static Object[] getPotionDataMisc(ItemStack stack) {
        if (stack == null || !(stack.getItem() instanceof ItemPotion) || !ItemPotion.isSplash(stack.getMetadata())) {
            return NULL_OR_BAD_POTION_ARRAY;
        }
        ItemPotion itemPotion = (ItemPotion) stack.getItem();
        List<PotionEffect> potionEffectList = itemPotion.getEffects(stack);
        for (PotionEffect potionEffect : potionEffectList) {
            if (potionEffect.getPotionID() == Potion.moveSpeed.id) {
                return new Object[] { true, potionEffect.getPotionID(), stack };
            }
        }
        return NULL_OR_BAD_POTION_ARRAY;
    }
    private static Object[] getPotionData(ItemStack stack) {
        if (stack == null || !(stack.getItem() instanceof ItemPotion) || !ItemPotion.isSplash(stack.getMetadata())) {
            return NULL_OR_BAD_POTION_ARRAY;
        }
        ItemPotion itemPotion = (ItemPotion) stack.getItem();
        List<PotionEffect> potionEffectList = itemPotion.getEffects(stack);
        for (PotionEffect potionEffect : potionEffectList) {
            if (potionEffect.getPotionID() == Potion.heal.id || potionEffect.getPotionID() == Potion.regeneration.id) {
                return new Object[] { true, potionEffect.getPotionID(), stack };
            }
        }
        return NULL_OR_BAD_POTION_ARRAY;
    }

    public static boolean isPotting() {
        return isPotting;
    }

    public static void setPotting(boolean isPotting) {
        AutoPot.isPotting = isPotting;
    }
}
