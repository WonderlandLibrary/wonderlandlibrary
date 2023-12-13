package summer.cheat.cheats.player;

import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemPotion;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.network.play.client.C09PacketHeldItemChange;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import summer.Summer;
import summer.base.manager.CheatManager;
import summer.base.manager.Selection;
import summer.cheat.cheats.movement.Flight;
import summer.cheat.guiutil.Setting;
import summer.cheat.eventsystem.EventTarget;
import summer.cheat.eventsystem.events.player.EventUpdate;
import summer.base.utilities.TimerUtils;
import summer.base.manager.config.Cheats;

public class AutoPot extends Cheats {
    private TimerUtils timer = new TimerUtils();
    private static boolean activated;
    public Minecraft mc = Minecraft.getMinecraft();

    public Setting delay;
    public Setting autoHeal;
    public Setting healPercent;
    public Setting speed;
    public Setting jumpBoost;
    public Setting other;

    public AutoPot() {
        super("AutoPot", "Automatically throws splash pots", Selection.PLAYER);
        Summer.INSTANCE.settingsManager.Property(delay = new Setting("Delay", this, 600.0D, 150.0D, 1500.0D, true));
        Summer.INSTANCE.settingsManager.Property(autoHeal = new Setting("AutoHeal", this, true));
        Summer.INSTANCE.settingsManager.Property(healPercent = new Setting("HealPercent", this, 70.0D, 5.0D, 100.0D, true));
        Summer.INSTANCE.settingsManager.Property(speed = new Setting("Speed", this, true));
        Summer.INSTANCE.settingsManager.Property(jumpBoost = new Setting("JumpBoost", this, false));
        Summer.INSTANCE.settingsManager.Property(other = new Setting("Other", this, true));
    }

    @EventTarget
    public void update(EventUpdate e) {
        if (CheatManager.getInstance(Flight.class).isToggled() || Minecraft.thePlayer.isOnLadder())
            return;
        EventUpdate eu = e;
        if (eu.isPre() && this.timer.hasReached(delay.getValFloat())) {
            int toThrow = getStackToThrow();
            if (toThrow == -1) {
                activated = false;
                this.timer.reset();
                return;
            }
            activated = true;
            if (eu.getLastPitch() == 85.0F) {
                Minecraft.thePlayer.sendQueue.addToSendQueue(new C09PacketHeldItemChange(toThrow));
                Minecraft.thePlayer.sendQueue
                        .addToSendQueue(new C08PacketPlayerBlockPlacement(getStackToThrow2()));
                this.timer.reset();
                activated = false;
            } else {
                eu.setPitch(85.0F);
            }
        }
    }

    public static boolean isActivated() {
        return activated;
    }

    public void onDisable() {
        activated = false;
    }

    private ItemStack getStackToThrow2() {
        for (int k = 0; k < 9; k++) {
            ItemStack item = Minecraft.thePlayer.inventory.mainInventory[k];
            if (item != null && isValid(item))
                return item;
        }
        return null;
    }

    private int getStackToThrow() {
        for (int k = 0; k < 9; k++) {
            ItemStack item = Minecraft.thePlayer.inventory.mainInventory[k];
            if (item != null && isValid(item))
                return k;
        }
        return -1;
    }

    private boolean isValid(ItemStack stack) {
        if (!(stack.getItem() instanceof ItemPotion))
            return false;
        if (!ItemPotion.isSplash(stack.getMetadata()))
            return false;
        if (stack.getDisplayName().equalsIgnoreCase("Potion (20s)")
                && !jumpBoost.getValBoolean())
            return false;
        if (isBadPotion(stack))
            return false;
        ItemPotion ip = (ItemPotion) stack.getItem();
        List<PotionEffect> effects = ip.getEffects(stack);
        boolean containsBad = false;
        for (PotionEffect pe : effects) {
            if (Minecraft.thePlayer.isPotionActive(pe.getPotionID())) {
                containsBad = true;
                continue;
            }
            if (pe.getPotionID() == Potion.regeneration.id || pe.getPotionID() == Potion.heal.id) {
                if (!autoHeal.getValBoolean()) {
                    containsBad = true;
                    continue;
                }
                if (Minecraft.thePlayer.getHealth() / Minecraft.thePlayer.getMaxHealth() > healPercent.getValFloat() / 100.0F)
                    containsBad = true;
                continue;
            }
            if (pe.getPotionID() == Potion.moveSpeed.id
                    && !speed.getValBoolean()) {
                containsBad = true;
                continue;
            }
            if (!other.getValBoolean())
                containsBad = true;
        }
        if (!containsBad)
            return true;
        return false;
    }

    public static boolean isBadPotion(ItemStack stack) {
        if (stack != null && stack.getItem() instanceof ItemPotion) {
            ItemPotion potion = (ItemPotion) stack.getItem();
            if (ItemPotion.isSplash(stack.getItemDamage()))
                for (Object o : potion.getEffects(stack)) {
                    PotionEffect effect = (PotionEffect) o;
                    if (effect.getPotionID() == Potion.poison.getId() || effect.getPotionID() == Potion.harm.getId()
                            || effect.getPotionID() == Potion.moveSlowdown.getId()
                            || effect.getPotionID() == Potion.weakness.getId())
                        return true;
                }
        }
        return false;
    }
}