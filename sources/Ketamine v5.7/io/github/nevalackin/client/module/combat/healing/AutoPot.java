package io.github.nevalackin.client.module.combat.healing;

import io.github.nevalackin.client.module.Category;
import io.github.nevalackin.client.module.Module;
import io.github.nevalackin.client.event.player.UpdatePositionEvent;
import io.github.nevalackin.client.property.BooleanProperty;
import io.github.nevalackin.client.property.DoubleProperty;
import io.github.nevalackin.client.util.math.MathUtil;
import io.github.nevalackin.client.util.player.InventoryUtil;
import io.github.nevalackin.client.util.player.WindowClickRequest;
import io.github.nevalackin.homoBus.Listener;
import io.github.nevalackin.homoBus.annotations.EventLink;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemPotion;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.network.play.client.C09PacketHeldItemChange;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class AutoPot extends Module {

    private int ticksSinceLastPot;

    private final Map<Integer, Requirement[]> potionRequirementMap = new HashMap<>();

    private final DoubleProperty healthProperty = new DoubleProperty("Health", 6.0, 1.0, 10.0, 0.5);
    private final BooleanProperty headsProperty = new BooleanProperty("Heads", true);

    private boolean potting;

    private WindowClickRequest lastRequest;

    public AutoPot() {
        super("Auto Potion", Category.COMBAT, Category.SubCategory.COMBAT_HEALING);

        this.register(this.healthProperty, this.headsProperty);

        final Requirement healthBelowRequirement = new HealthBelowRequirement();
        final Requirement betterThanCurrentRequirement = new BetterThanCurrentRequirement();

        this.potionRequirementMap.put(Potion.moveSpeed.getId(), new Requirement[]{betterThanCurrentRequirement});
        this.potionRequirementMap.put(Potion.regeneration.getId(), new Requirement[]{betterThanCurrentRequirement, healthBelowRequirement});
        this.potionRequirementMap.put(Potion.heal.getId(), new Requirement[]{healthBelowRequirement});
    }

    @EventLink(1) // Call auto pot second last
    private final Listener<UpdatePositionEvent> onUpdate = event -> {
        if (event.isPre()) {
            this.ticksSinceLastPot++;

            if (this.mc.currentScreen != null && !(this.mc.currentScreen instanceof GuiInventory))
                return;

            // 800ms pot/head delay
            if (this.ticksSinceLastPot <= 16) return;

            if (!event.isRotating()) {
                int mostImportantSlot = this.getMostImportantPotion();

                if (mostImportantSlot != -1) {
                    if (mostImportantSlot < InventoryUtil.ONLY_HOT_BAR_BEGIN) {
                        if (this.lastRequest == null || this.lastRequest.isCompleted()) {
                            // Scaffold uses the last hot bar slot
                            final int slotID = mostImportantSlot;
                            InventoryUtil.queueClickRequest(this.lastRequest = new WindowClickRequest() {
                                @Override
                                public void performRequest() {
                                    InventoryUtil.windowClick(mc, slotID, 7,
                                                              InventoryUtil.ClickType.SWAP_WITH_HOT_BAR_SLOT);
                                }
                            });
                        }
                        return;
                    } else {
                        mostImportantSlot -= InventoryUtil.ONLY_HOT_BAR_BEGIN;
                    }

                    // Calculate distance moved
                    final double distMovedInTick = MathUtil.distance(this.mc.thePlayer.lastReportedPosX, this.mc.thePlayer.lastReportedPosZ,
                                                                     this.mc.thePlayer.posX, this.mc.thePlayer.posZ);

                    // If not on ground ground or moving too fast
                    if (!this.mc.thePlayer.onGround || distMovedInTick > 0.2873 + 9.0E-4) return;
                    event.setPitch(90.f);
                    // Switch to potion slot server side
                    this.mc.thePlayer.sendQueue.sendPacket(new C09PacketHeldItemChange(mostImportantSlot));
                    this.potting = true;
                    return;
                }
            }

            if (this.headsProperty.getValue() && this.mc.thePlayer.getHealth() < this.healthProperty.getValue() * 2.0) {
                for (int slot = InventoryUtil.ONLY_HOT_BAR_BEGIN; slot < InventoryUtil.END; slot++) {
                    final ItemStack stack = this.mc.thePlayer.inventoryContainer.getSlot(slot).getStack();

                    if (stack == null) continue;
                    final int itemID = Item.getIdFromItem(stack.getItem());
                    final boolean shouldEatID = itemID == 282 || itemID == Item.getIdFromItem(Items.skull) || itemID == Item.getIdFromItem(Items.baked_potato) || itemID == Item.getIdFromItem(Items.magma_cream) || itemID == Item.getIdFromItem(Items.mutton);

                    if (shouldEatID) {
                        slot -= 36; // Normalize to hot bar slot
                        // Switch to head
                        this.mc.thePlayer.sendQueue.sendPacket(new C09PacketHeldItemChange(slot));
                        // Eat head
                        this.mc.thePlayer.sendQueue.sendPacket(new C08PacketPlayerBlockPlacement(null));
                        // Sync item slot
                        this.mc.thePlayer.sendQueue.sendPacket(new C09PacketHeldItemChange(this.mc.thePlayer.inventory.currentItem));
                        // Reset pot timer
                        this.ticksSinceLastPot = 0;
                        return;
                    }
                }
            }
        } else if (this.potting) {
            // Throw potion
            this.mc.thePlayer.sendQueue.sendPacket(new C08PacketPlayerBlockPlacement(null));
            // Sync item slot
            this.mc.thePlayer.sendQueue.sendPacket(new C09PacketHeldItemChange(this.mc.thePlayer.inventory.currentItem));
            // Reset pot timer
            this.ticksSinceLastPot = 0;

            this.potting = false;
        }
    };

    @Override
    public void onEnable() {
        this.potting = false;
    }

    @Override
    public void onDisable() {

    }

    private int getMostImportantPotion() {
        Map<Integer, PotionEffect> effectsInSlots;

        if ((effectsInSlots = doSearchPotion(InventoryUtil.ONLY_HOT_BAR_BEGIN, InventoryUtil.END)).isEmpty()) {
            effectsInSlots = doSearchPotion(InventoryUtil.EXCLUDE_ARMOR_BEGIN, InventoryUtil.ONLY_HOT_BAR_BEGIN);

            if (effectsInSlots.isEmpty()) {
                return -1;
            }
        }

        Map<Integer, Integer> potionIDAmplifiers = new HashMap<>();

        for (final Integer slot : effectsInSlots.keySet()) {
            final PotionEffect effect = effectsInSlots.get(slot);

            if (potionIDAmplifiers.containsKey(effect.getPotionID())) {
                final int amplifier = potionIDAmplifiers.get(effect.getPotionID());

                if (effect.getAmplifier() > amplifier) {
                    potionIDAmplifiers.put(effect.getPotionID(), effect.getAmplifier());
                }
            } else {
                potionIDAmplifiers.put(effect.getPotionID(), effect.getAmplifier());
            }
        }

        for (final Integer slot : effectsInSlots.keySet()) {
            final PotionEffect effect = effectsInSlots.get(slot);

            if (this.getMostImportantInList(potionIDAmplifiers, effect, Potion.heal.getId())) {
                return slot;
            }

            if (this.getMostImportantInList(potionIDAmplifiers, effect, Potion.regeneration.getId())) {
                return slot;
            }

            if (this.getMostImportantInList(potionIDAmplifiers, effect, Potion.moveSpeed.getId())) {
                return slot;
            }
        }


        return -1;
    }

    private boolean getMostImportantInList(final Map<Integer, Integer> potionIDAmplifiers,
                                           final PotionEffect effect,
                                           final int filterPotionID) {
        for (final Integer potionID : potionIDAmplifiers.keySet()) {
            final Integer amplifier = potionIDAmplifiers.get(potionID);

            if (effect.getPotionID() == potionID && effect.getAmplifier() == amplifier) {
                if (potionID == filterPotionID) return true;
            }
        }

        return false;
    }

    private Map<Integer, PotionEffect> doSearchPotion(final int start,
                                                      final int end) {
        final Map<Integer, PotionEffect> slotEffectMap = new HashMap<>();

        for (int i = start; i < end; i++) {
            final ItemStack stack = this.mc.thePlayer.inventoryContainer.getSlot(i).getStack();

            if (stack != null && stack.getItem() instanceof ItemPotion &&
                ItemPotion.isSplash(stack.getMetadata()) &&
                InventoryUtil.isBuffPotion(stack)) {
                final ItemPotion potion = (ItemPotion) stack.getItem();

                final List<PotionEffect> effects = potion.getEffects(stack.getMetadata());
                if (effects != null) {
                    for (final PotionEffect effect : effects) {
                        for (final Integer potionID : this.potionRequirementMap.keySet()) {
                            if (effect.getPotionID() == potionID) {
                                final Requirement[] requirements = this.potionRequirementMap.get(potionID);

                                if (requirements.length == 1) {
                                    if (requirements[0].test(this.mc.thePlayer, this.healthProperty.getValue().floatValue() * 2.0F,
                                                             effect.getAmplifier(), potionID)) {
                                        slotEffectMap.put(i, effect);
                                    }
                                } else if (requirements.length > 1) {
                                    boolean pass = false;

                                    for (final Requirement requirement : requirements) {
                                        pass = requirement.test(this.mc.thePlayer, this.healthProperty.getValue().floatValue() * 2.0F,
                                                                effect.getAmplifier(), potionID);
                                    }

                                    if (pass) {
                                        slotEffectMap.put(i, effect);
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        return slotEffectMap;
    }

    private interface Requirement {
        boolean test(EntityPlayerSP player, float healthTarget, int currentAmplifier, int potionId);
    }

    private static class HealthBelowRequirement implements Requirement {
        @Override
        public boolean test(EntityPlayerSP player, float healthTarget, int currentAmplifier, int potionId) {
            return player.getHealth() < healthTarget;
        }
    }

    private static class BetterThanCurrentRequirement implements Requirement {
        @Override
        public boolean test(EntityPlayerSP player, float healthTarget, int currentAmplifier, int potionId) {
            final PotionEffect effect = player.getActivePotionEffect(potionId);
            return effect == null || effect.getAmplifier() < currentAmplifier;
        }
    }
}
