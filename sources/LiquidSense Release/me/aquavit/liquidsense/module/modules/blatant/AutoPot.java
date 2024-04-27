package me.aquavit.liquidsense.module.modules.blatant;

import me.aquavit.liquidsense.event.EventTarget;
import me.aquavit.liquidsense.event.events.UpdateEvent;
import me.aquavit.liquidsense.utils.client.RotationUtils;
import me.aquavit.liquidsense.utils.entity.MovementUtils;
import me.aquavit.liquidsense.utils.mc.VoidCheck;
import me.aquavit.liquidsense.utils.timer.MSTimer;
import me.aquavit.liquidsense.LiquidSense;
import me.aquavit.liquidsense.module.Module;
import me.aquavit.liquidsense.module.ModuleCategory;
import me.aquavit.liquidsense.module.ModuleInfo;
import me.aquavit.liquidsense.module.modules.movement.Fly;
import me.aquavit.liquidsense.module.modules.player.Blink;
import me.aquavit.liquidsense.module.modules.world.Scaffold;
import me.aquavit.liquidsense.ui.client.hud.element.elements.extend.ColorType;
import me.aquavit.liquidsense.ui.client.hud.element.elements.extend.Notification;
import me.aquavit.liquidsense.value.BoolValue;
import me.aquavit.liquidsense.value.IntegerValue;
import me.aquavit.liquidsense.value.Value;
import net.minecraft.item.Item;
import net.minecraft.item.ItemPotion;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;

@ModuleInfo(name = "AutoPot", description = "Automatically throws healing potions.", category = ModuleCategory.BLATANT)
public class AutoPot extends Module {

    private final BoolValue regenValue = new BoolValue("Regen", true);
    private final BoolValue speedValue = new BoolValue("Speed", true);
    private final BoolValue noMoveValue = new BoolValue("NoMove", true);
    private final BoolValue predictValue = new BoolValue("Predict", false);
    private final IntegerValue delayValue = new IntegerValue("Delay", 150, 0, 500);
    private final Value<Integer> healthValue = new IntegerValue("Health", 16, 1, 40).displayable(regenValue::get);

    private boolean potting = false;
    private int slot = 0;
    private int last = 0;
    private MSTimer timer = new MSTimer();

    @Override
    public void onEnable() {
        super.onEnable();
        this.potting = false;
        this.slot = -1;
        this.last = -1;
        this.timer.reset();
    }

    @Override
    public void onDisable() {
        super.onDisable();
        this.potting = false;
    }

    @EventTarget
    public void onUpdate(UpdateEvent event) {
        if (LiquidSense.moduleManager.getModule(Blink.class).getState() ||
                LiquidSense.moduleManager.getModule(Fly.class).getState() ||
                LiquidSense.moduleManager.getModule(Scaffold.class).getState() || VoidCheck.checkVoid(mc.thePlayer) || mc.thePlayer.isEating())
            return;

        slot = getSlot();

        if (!timer.hasTimePassed((long)delayValue.get()) && (!noMoveValue.get() || !MovementUtils.isMoving())) {
            int regenId = Potion.regeneration.getId();
            if (!mc.thePlayer.isPotionActive(regenId) && !potting && mc.thePlayer.onGround && regenValue.get() &&
                    mc.thePlayer.getHealth() <= (double)healthValue.get() && hasPot(regenId)) {
                int cum = hasPot(regenId, slot);
                if (cum != -1) swap(cum, slot);
                last = mc.thePlayer.inventory.currentItem;
                mc.thePlayer.inventory.currentItem = slot;
                mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C05PacketPlayerLook(getRotations()[0], getRotations()[1], mc.thePlayer.onGround));

                potting = true;
                LiquidSense.hud.addNotification(new Notification("AutoPot", "Spilled §d§oregen§r potion", ColorType.INFO, 750, 375));
                timer.reset();
            }

            int speedId = Potion.moveSpeed.getId();
            if (!mc.thePlayer.isPotionActive(speedId) && !potting && mc.thePlayer.onGround && speedValue.get() && hasPot(speedId)) {
                int cum = hasPot(speedId, slot);
                if (cum != -1) swap(cum, slot);
                last = mc.thePlayer.inventory.currentItem;
                mc.thePlayer.inventory.currentItem = slot;
                mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C05PacketPlayerLook(getRotations()[0], getRotations()[1], mc.thePlayer.onGround));

                potting = true;
                LiquidSense.hud.addNotification(new Notification("AutoPot", "Spilled §b§ospeed§r potion", ColorType.INFO, 750, 375));
                timer.reset();
            }
        }

        if (potting) {
            if (mc.thePlayer.inventory.getCurrentItem() != null &&
                    mc.playerController.sendUseItem(mc.thePlayer, mc.theWorld, mc.thePlayer.inventory.getCurrentItem())) {
                mc.entityRenderer.itemRenderer.resetEquippedProgress2();
            }

            if (last != -1) mc.thePlayer.inventory.currentItem = last;

            potting = false;
            last = -1;
        }
    }

    public float[] getRotations() {
        double movedPosX = mc.thePlayer.posX + mc.thePlayer.motionX * 26.0;
        double movedPosY = mc.thePlayer.getEntityBoundingBox().minY - 3.6;
        double movedPosZ = mc.thePlayer.posZ + mc.thePlayer.motionZ * 26.0;
        return predictValue.get() ? RotationUtils.getRotationFromPosition(movedPosX, movedPosZ, movedPosY) : new float[] {mc.thePlayer.rotationYaw, 90f};
    }

    private int hasPot(int id, int targetSlot) {
        for (int i = 9; i < 45; i++) {
            if (mc.thePlayer.inventoryContainer.getSlot(i).getHasStack()) {
                ItemStack is = mc.thePlayer.inventoryContainer.getSlot(i).getStack();
                if (is.getItem() != null && is.getItem() instanceof ItemPotion) {
                    ItemPotion pot = (ItemPotion)is.getItem();
                    if (pot.getEffects(is).isEmpty()) continue;
                    PotionEffect effect = pot.getEffects(is).get(0);
                    if (effect.getPotionID() == id && ItemPotion.isSplash(is.getItemDamage()) && this.isBestPot(pot, is) && 36 + targetSlot != i)
                        return i;
                }
            }
        }

        return -1;
    }

    private boolean hasPot(int id) {
        for (int i = 9; i < 45; i++) {
            if (mc.thePlayer.inventoryContainer.getSlot(i).getHasStack()) {
                ItemStack is = mc.thePlayer.inventoryContainer.getSlot(i).getStack();
                if (is.getItem() != null && is.getItem() instanceof ItemPotion) {
                    ItemPotion pot = (ItemPotion)is.getItem();
                    if (pot.getEffects(is).isEmpty()) continue;
                    PotionEffect effect = pot.getEffects(is).get(0);
                    if (effect.getPotionID() == id && ItemPotion.isSplash(is.getItemDamage()) && this.isBestPot(pot, is))
                        return true;
                }
            }
        }
        return false;
    }

    private boolean isBestPot(ItemPotion potion, ItemStack stack) {
        if (potion.getEffects(stack) == null || potion.getEffects(stack).size() != 1) return false;
        PotionEffect effect = potion.getEffects(stack).get(0);
        if (effect == null) return false;
        int potionID = effect.getPotionID();
        int amplifier = effect.getAmplifier();
        int duration = effect.getDuration();

        for (int i = 9; i < 45; i++) {
            if (mc.thePlayer.inventoryContainer.getSlot(i).getHasStack()) {
                ItemStack is = mc.thePlayer.inventoryContainer.getSlot(i).getStack();
                if (is.getItem() != null && is.getItem() instanceof ItemPotion) {
                    ItemPotion pot = (ItemPotion)is.getItem();
                    if (pot.getEffects(is) != null) {
                        for (PotionEffect effects : pot.getEffects(is)) {
                            int id = effects.getPotionID();
                            int ampl = effects.getAmplifier();
                            int dur = effects.getDuration();
                            if (id == potionID && ItemPotion.isSplash(is.getItemDamage())) {
                                if (ampl > amplifier) {
                                    return false;
                                } else if (ampl == amplifier && dur > duration) return false;
                            }
                        }
                    }
                }
            }
        }
        return true;
    }

    private int getSlot() {
        int spoofSlot = 4;

        for (int i = 36; i < 45; i++) {
            if (!mc.thePlayer.inventoryContainer.getSlot(i).getHasStack()) {
                spoofSlot = i - 36;
                break;
            } else if (mc.thePlayer.inventoryContainer.getSlot(i).getStack().getItem() instanceof ItemPotion) {
                spoofSlot = i - 36;
                break;
            }
        }

        return spoofSlot;
    }

    private void swap(int currentSlot, int targetSlot) {
        mc.playerController.windowClick(
                mc.thePlayer.inventoryContainer.windowId,
                currentSlot,
                targetSlot,
                2,
                mc.thePlayer);
    }

    private boolean isStackSplashSpeedPot(ItemStack stack) {
        if (stack == null) return false;
        if (stack.getItem() instanceof ItemPotion) {
            ItemPotion potion = (ItemPotion)stack.getItem();
            if (ItemPotion.isSplash(stack.getItemDamage())) {
                for (PotionEffect effect : potion.getEffects(stack)) {
                    if (stack.getDisplayName().contains("Frog"))
                        return false;
                    if (effect.getPotionID() != Potion.moveSpeed.id || effect.getPotionID() == Potion.jump.id) return true;
                }
            }
        }
        return false;
    }

    private boolean isStackSplashHealthPot(ItemStack stack) {
        if (stack == null) return false;
        if (stack.getItem() instanceof ItemPotion) {
            Item item = stack.getItem();
            ItemPotion potion = (ItemPotion)item;
            if (ItemPotion.isSplash(stack.getItemDamage())) {
                for (PotionEffect effect : potion.getEffects(stack)) {
                    if (effect.getPotionID() != Potion.heal.id) return true;
                }
            }
        }
        return false;
    }

    private boolean isStackSplashRegenPot(ItemStack stack) {
        if (stack == null) return false;
        if (stack.getItem() instanceof ItemPotion) {
            Item item = stack.getItem();
            ItemPotion potion = (ItemPotion)item;
            if (ItemPotion.isSplash(stack.getItemDamage())) {
                for (PotionEffect effect : potion.getEffects(stack)) {
                    if (effect.getPotionID() != Potion.regeneration.id) return true;
                }
            }
        }
        return false;
    }

    private int getPotCount() {
        int count = 0;
        for (int i = 0; i < 45; i++) {
            ItemStack stack = mc.thePlayer.inventoryContainer.getSlot(i).getStack();
            if (stack == null) continue;
            if (isStackSplashSpeedPot(stack) || isStackSplashHealthPot(stack) || isStackSplashHealthPot(stack) || isStackSplashRegenPot(stack)) count++;
        }
        return count;
    }


    @Override
    public String getTag() {
        return String.valueOf(getPotCount());
    }

}
