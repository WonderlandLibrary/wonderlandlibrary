package today.flux.module.implement.Combat;

import com.darkmagician6.eventapi.EventTarget;
import com.darkmagician6.eventapi.types.Priority;
import net.minecraft.block.BlockGlass;
import net.minecraft.inventory.ContainerChest;
import net.minecraft.item.Item;
import net.minecraft.item.ItemPotion;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.network.play.client.C09PacketHeldItemChange;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import today.flux.event.MoveEvent;
import today.flux.event.PostUpdateEvent;
import today.flux.event.PreUpdateEvent;
import today.flux.event.TickEvent;
import today.flux.module.Category;
import today.flux.module.Module;
import today.flux.module.ModuleManager;
import today.flux.module.implement.Misc.disabler.Hypixel;
import today.flux.module.value.BooleanValue;
import today.flux.module.value.FloatValue;
import today.flux.utility.*;

import java.util.List;

public class AutoPot extends Module {
    private boolean jumping;
    private boolean rotated;
    public static FloatValue health = new FloatValue("AutoPot", "Health", 13.0f, 1.0f, 20.0f, 1.0f, "hp");
    public static FloatValue delay = new FloatValue("AutoPot", "Delay", 500.0f, 100.0f, 1500.0f, 50.0f, "ms");
    public static BooleanValue jump = new BooleanValue("AutoPot", "Jump", false);
    public static BooleanValue regen = new BooleanValue("AutoPot", "Regen Pot", true);
    public static BooleanValue heal = new BooleanValue("AutoPot", "Heal Pot", true);
    public static BooleanValue speed = new BooleanValue("AutoPot", "Speed Pot", false);

    public static BooleanValue nofrog = new BooleanValue("AutoPot", "No Frog", true);

    public AutoPot() {
        super("AutoPot", Category.Combat, false);
    }

    public static DelayTimer timer = new DelayTimer();
    private DelayTimer cooldown = new DelayTimer();

    private int lastPottedSlot;

    @EventTarget
    private void onMove(final MoveEvent event) {
        if (this.jumping) {
            this.mc.thePlayer.motionX = 0;
            this.mc.thePlayer.motionZ = 0;
            event.x = 0;
            event.z = 0;

            if (cooldown.hasPassed(100) && this.mc.thePlayer.onGround) {
                this.jumping = false;
            }
        }
    }

    @EventTarget(Priority.HIGH)
    private void onPreUpdate(final PreUpdateEvent event) {
        if (MoveUtils.getBlockUnderPlayer(mc.thePlayer, 0.01) instanceof BlockGlass || !MoveUtils.isOnGround(0.01) || !Hypixel.hasDisabled)  {
            timer.reset();
            return;
        }

        if (mc.thePlayer.openContainer != null) {
            if (mc.thePlayer.openContainer instanceof ContainerChest) {
                timer.reset();
                return;
            }
        }

        if (ModuleManager.scaffoldMod.isEnabled())
            return;

        if (event.isModified() || KillAura.target != null) {
            rotated = false;
            timer.reset();
            return;
        }

        final int potSlot = this.getPotFromInventory();
        if (potSlot != -1 && timer.hasPassed(delay.getValue())) {
            if (jump.getValue() && !BlockUtils.isInLiquid()) {
                event.setPitch(-89.5f);

                this.jumping = true;
                if (this.mc.thePlayer.onGround) {
                    this.mc.thePlayer.jump();
                    cooldown.reset();
                }
            } else {
                event.setPitch(89.5f);
            }

            rotated = true;
        }
    }

    @EventTarget
    private void onPostUpdate(final PostUpdateEvent event) {
        if (!rotated)
            return;

        rotated = false;

        final int potSlot = this.getPotFromInventory();
        if (potSlot != -1 && timer.hasPassed(delay.getValue()) && mc.thePlayer.isCollidedVertically) {
            final int prevSlot = mc.thePlayer.inventory.currentItem;
            if (potSlot < 9) {
                mc.thePlayer.sendQueue.addToSendQueue(new C09PacketHeldItemChange(potSlot));
                mc.thePlayer.sendQueue.addToSendQueue(
                        new C08PacketPlayerBlockPlacement(mc.thePlayer.inventory.getCurrentItem()));
                mc.thePlayer.sendQueue.addToSendQueue(new C09PacketHeldItemChange(prevSlot));
                mc.thePlayer.inventory.currentItem = prevSlot;
                timer.reset();

                this.lastPottedSlot = potSlot;
            }
        }
    }

    // Auto Refill
    @EventTarget
    public void onTick(TickEvent event) {
        if (this.mc.currentScreen != null)
            return;

        final int potSlot = this.getPotFromInventory();
        if (potSlot != -1 && potSlot > 8 && this.mc.thePlayer.ticksExisted % 4 == 0) {
            this.swap(potSlot, InvUtils.findEmptySlot(this.lastPottedSlot));
        }
    }

    private void swap(final int slot, final int hotbarNum) {
        this.mc.playerController.windowClick(this.mc.thePlayer.inventoryContainer.windowId, slot, hotbarNum, 2,
                this.mc.thePlayer);
    }

    private int getPotFromInventory() {
        // heals
        for (int i = 0; i < 36; ++i) {
            if (mc.thePlayer.inventory.mainInventory[i] != null) {
                final ItemStack is = mc.thePlayer.inventory.mainInventory[i];
                final Item item = is.getItem();

                if (!(item instanceof ItemPotion)) {
                    continue;
                }

                ItemPotion pot = (ItemPotion) item;

                if (!ItemPotion.isSplash(is.getMetadata())) {
                    continue;
                }

                List<PotionEffect> effects = pot.getEffects(is);

                for (PotionEffect effect : effects) {
                    if (mc.thePlayer.getHealth() < health.getValue() && ((heal.getValue() && effect.getPotionID() == Potion.heal.id) || (regen.getValue() && effect.getPotionID() == Potion.regeneration.id && !hasEffect(Potion.regeneration.id))))
                        return i;
                }
            }
        }

        // others
        for (int i = 0; i < 36; ++i) {
            if (this.mc.thePlayer.inventory.mainInventory[i] != null) {
                final ItemStack is = this.mc.thePlayer.inventory.mainInventory[i];
                final Item item = is.getItem();

                if (!(item instanceof ItemPotion)) {
                    continue;
                }

                List<PotionEffect> effects = ((ItemPotion) item).getEffects(is);

                for (PotionEffect effect : effects) {
                    if (effect.getPotionID() == Potion.moveSpeed.id && speed.getValue()
                            && !hasEffect(Potion.moveSpeed.id))
                        if (!is.getDisplayName().contains("\247a") || !nofrog.getValueState())
                            return i;
                }
            }
        }

        return -1;
    }

    private boolean hasEffect(int potionId) {
        for (PotionEffect item : mc.thePlayer.getActivePotionEffects()) {
            if (item.getPotionID() == potionId)
                return true;
        }
        return false;
    }
}
