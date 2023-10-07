package cc.novoline.modules.combat;

import cc.novoline.events.EventTarget;
import cc.novoline.events.events.MotionUpdateEvent;
import cc.novoline.gui.screen.setting.Manager;
import cc.novoline.gui.screen.setting.Setting;
import cc.novoline.gui.screen.setting.SettingType;
import cc.novoline.modules.AbstractModule;
import cc.novoline.modules.EnumModuleType;
import cc.novoline.modules.ModuleManager;
import cc.novoline.modules.configurations.annotation.Property;
import cc.novoline.modules.configurations.property.object.*;
import cc.novoline.modules.move.Scaffold;
import cc.novoline.utils.ServerUtils;
import cc.novoline.utils.Servers;
import cc.novoline.utils.Timer;
import net.minecraft.client.gui.inventory.GuiContainerCreative;
import net.minecraft.item.Item;
import net.minecraft.item.ItemPotion;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.C03PacketPlayer.C06PacketPlayerPosLook;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.network.play.client.C09PacketHeldItemChange;
import net.minecraft.potion.PotionEffect;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.concurrent.ThreadLocalRandom;

public class AutoPot extends AbstractModule {

    /* fields */
    private final Timer timer = new Timer();
    private float prevPitch;

    /* properties @off */
    @Property("health")
    private final DoubleProperty health = PropertyFactory.createDouble(14.0D).minimum(0.5D).maximum(20.0D);
    @Property("delay")
    private final IntProperty delay = PropertyFactory.createInt(500).minimum(500).maximum(2500);
    @Property("potions")
    private final ListProperty<String> potions = PropertyFactory.createList("Regen").acceptableValues("Heal", "Regen", "Jump", "Speed", "Fire");
    @Property("overlap-effect")
    private final BooleanProperty overlap = PropertyFactory.booleanFalse();

    /* constructors @on */
    public AutoPot(@NonNull ModuleManager moduleManager) {
        super(moduleManager, "AutoPotion", "Auto Potion", EnumModuleType.COMBAT, "Automatically throws pots");
        Manager.put(new Setting("HEARTS", "Health", SettingType.SLIDER, this,
                health, 0.5D, () -> potions.contains("Regen") || potions.contains("Heal")));
        Manager.put(new Setting("DELAY", "Throw Delay", SettingType.SLIDER, this, delay, 100D, () -> !potions.isEmpty()));
        Manager.put(new Setting("AP_POTIONS", "Potions", SettingType.SELECTBOX, this, potions));
        Manager.put(new Setting("OVERLAP", "Overlap", SettingType.CHECKBOX, this, overlap));
    }

    private int getBestSpoofSlot() {
        int spoofSlot = 5;

        for (int i = 36; i < 45; i++) {
            if (!mc.player.inventoryContainer.getSlot(i).getHasStack()) {
                spoofSlot = i - 36;
                break;
            } else if (mc.player.inventoryContainer.getSlot(i).getStack().getItem() instanceof ItemPotion) {
                spoofSlot = i - 36;
                break;
            }
        }

        return spoofSlot;
    }

    private int[] potions() {
        int[] pots = new int[]{-1, -1, -1, -1, -1, -1};
        if (this.potions.contains("Heal")) pots[0] = 6;
        if (this.potions.contains("Regen")) pots[1] = 10;
        if (this.potions.contains("Jump")) pots[2] = 8;
        if (this.potions.contains("Speed")) pots[3] = 1;
        if (this.potions.contains("Fire")) pots[4] = 12;
        return pots;
    }

    private boolean shouldBuff(int potID) {
        if (potID == 6 || potID == 10) {
            return mc.player.getHealth() < health.get();
        } else if (potID == 12) {
            return mc.player.isBurning();
        }

        return potID == 1 || potID == 8;
    }

    /* methods */
    @EventTarget
    public void onPre(MotionUpdateEvent event) {
        if (event.getState().equals(MotionUpdateEvent.State.PRE)) {
            if (!mc.player.onGround || ServerUtils.serverIs(Servers.PRE) || isEnabled(Scaffold.class)
                    || ServerUtils.serverIs(Servers.LOBBY) || mc.currentScreen instanceof GuiContainerCreative) {
                return;
            }

            for (int slot = 9; slot < 45; slot++) {
                ItemStack stack = mc.player.inventoryContainer.getSlot(slot).getStack();

                if (stack != null) {
                    Item item = stack.getItem();

                    if (item instanceof ItemPotion) {
                        ItemPotion itemPotion = (ItemPotion) item;
                        PotionEffect effect = itemPotion.getEffects(stack).get(0);

                        for (int potID : potions()) {
                            if (effect.getPotionID() == potID && ItemPotion.isSplash(stack.getItemDamage())) {
                                if (onOverlap(potID, effect)) {
                                    if (shouldBuff(potID)) {
                                        if (isBestPotion(itemPotion, stack)) {
                                            if (timer.delay(delay.get())) {
                                                throwPot(event, slot);
                                                timer.reset();
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private boolean onOverlap(int potID, PotionEffect effect) {
        if (overlap.get()) {
            return !mc.player.isPotionActive(potID) || mc.player.getActivePotionsMap().containsKey(potID)
                    && mc.player.getActivePotionsMap().get(potID).getAmplifier() < effect.getAmplifier();
        } else {
            return !mc.player.isPotionActive(potID);
        }
    }

    private void throwPot(MotionUpdateEvent event, int slot) {
        mc.player.swap(slot, getBestSpoofSlot());
        sendPacket(new C09PacketHeldItemChange(getBestSpoofSlot()));
        prevPitch = event.getPitch();
        sendLookPacket(event, 89.0F + ThreadLocalRandom.current().nextFloat());
        sendPacket(new C08PacketPlayerBlockPlacement(mc.player.getHeldItem()));
        sendLookPacket(event, prevPitch);
        sendPacket(new C09PacketHeldItemChange(mc.player.inventory.currentItem));
    }

    private void sendLookPacket(MotionUpdateEvent event, float pitch) {
        sendPacket(new C06PacketPlayerPosLook(event.getX(), event.getY(), event.getZ(), event.getYaw(), pitch, event.isOnGround()));
    }

    private boolean isBestPotion(ItemPotion potion, ItemStack stack) {
        if (potion.getEffects(stack) == null || potion.getEffects(stack).size() != 1) {
            return false;
        }

        PotionEffect effect = potion.getEffects(stack).get(0);
        int potionID = effect.getPotionID(), amplifier = effect.getAmplifier(), duration = effect.getDuration();

        for (int slot = 9; slot < 45; slot++) {
            if (mc.player.inventoryContainer.getSlot(slot).getHasStack()) {
                ItemStack slotStack = mc.player.inventoryContainer.getSlot(slot).getStack();

                if (slotStack.getItem() instanceof ItemPotion) {
                    ItemPotion potionStack = (ItemPotion) slotStack.getItem();

                    if (potionStack.getEffects(slotStack) != null) {
                        for (PotionEffect potionEffect : potionStack.getEffects(slotStack)) {
                            int id = potionEffect.getPotionID(), ampl = potionEffect.getAmplifier(), dur = potionEffect.getDuration();

                            if (id == potionID && ItemPotion.isSplash(slotStack.getItemDamage())) {
                                if (ampl > amplifier) {
                                    return false;
                                } else if (ampl == amplifier && dur > duration) {
                                    return false;
                                }
                            }
                        }
                    }
                }
            }
        }

        return true;
    }
}
