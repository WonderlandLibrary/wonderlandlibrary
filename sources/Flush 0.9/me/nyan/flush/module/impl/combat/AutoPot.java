package me.nyan.flush.module.impl.combat;

import me.nyan.flush.event.SubscribeEvent;
import me.nyan.flush.event.impl.EventMotion;
import me.nyan.flush.module.Module;
import me.nyan.flush.module.settings.BooleanSetting;
import me.nyan.flush.module.settings.NumberSetting;
import me.nyan.flush.utils.other.MathUtils;
import me.nyan.flush.utils.other.Timer;
import net.minecraft.block.BlockAir;
import net.minecraft.item.Item;
import net.minecraft.item.ItemPotion;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.network.play.client.C09PacketHeldItemChange;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;

public class AutoPot extends Module {
    public AutoPot() {
        super("AutoPot", Category.COMBAT);
    }

    public final NumberSetting delay = new NumberSetting("Delay", this, 100.0, 0.0, 1000.0, 0.5);
    private final NumberSetting minHealth = new NumberSetting("Min health", this, 17.0, 1.0, 19.0, 0.5);
    private final BooleanSetting speedPots = new BooleanSetting("Speed Potions", this, true);

    private final Timer timer = new Timer();
    private int slot = -1;

    @SubscribeEvent
    public void onMotion(EventMotion e) {
        switch (e.getState()) {
            case PRE:
                slot = -1;
                if (!timer.hasTimeElapsed(delay.getValueInt(), true) ||
                        mc.thePlayer.getPosition().down(1).getBlock() instanceof BlockAir ||
                        mc.playerController.isInCreativeMode()) {
                    return;
                }

            for (int i = 36; i < 44; i++){
                if (slot != -1) {
                    break;
                }
                ItemStack stack = mc.thePlayer.inventoryContainer.getSlot(i).getStack();

                if (stack != null) {
                    Item item = stack.getItem();
                    if (item instanceof ItemPotion) {
                        if (ItemPotion.isSplash(stack.getMetadata())) {
                            for (PotionEffect effect : ((ItemPotion) item).getEffects(stack)) {
                                if ((((effect.getPotionID() == Potion.regeneration.id &&
                                        mc.thePlayer.getActivePotionEffect(Potion.regeneration) == null) ||
                                        effect.getPotionID() == Potion.heal.id) &&
                                        mc.thePlayer.getHealth() <= minHealth.getValue()) ||
                                        (effect.getPotionID() == Potion.moveSpeed.id &&
                                                mc.thePlayer.getActivePotionEffect(Potion.moveSpeed) == null &&
                                                speedPots.getValue())) {
                                    slot = i - 36;
                                    break;
                                }
                            }
                        }
                    }
                }
            }

            if (slot == -1) {
                break;
            }

            e.setPitch((float) MathUtils.getRandomNumber(90.0, 80.0));
            mc.getNetHandler().addToSendQueue(new C09PacketHeldItemChange(slot));
            break;

            case POST:
                if (slot == -1) {
                    break;
                }
                ItemStack stack = mc.thePlayer.inventoryContainer.getSlot(slot).getStack();
                if (stack == null) {
                    break;
                }
                mc.getNetHandler().addToSendQueue(new C08PacketPlayerBlockPlacement(stack));
                mc.getNetHandler().addToSendQueue(new C09PacketHeldItemChange(mc.thePlayer.inventory.currentItem));
                timer.reset();
                slot = -1;
                break;
        }
    }
}
