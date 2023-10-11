package cow.milkgod.cheese.module.modules;

import cow.milkgod.cheese.properties.*;
import cow.milkgod.cheese.module.*;
import com.darkmagician6.eventapi.*;
import net.minecraft.entity.player.*;
import net.minecraft.world.*;
import net.minecraft.network.play.server.*;
import net.minecraft.item.*;
import net.minecraft.potion.*;
import java.util.*;
import cow.milkgod.cheese.*;
import cow.milkgod.cheese.events.*;
import cow.milkgod.cheese.utils.*;
import cow.milkgod.cheese.utils.Timer;
import cow.milkgod.cheese.commands.*;

public class AutoPot extends Module
{
    public static Property<Long> delay;
    public static Property<Integer> health;
    private float oldYaw;
    private float oldPitch;
    public static int pots;
    private final Timer timer;
    private boolean needsToPot;
    private static boolean potting;
    
    public AutoPot() {
        super("AutoPot", 0, Category.COMBAT, 6684876, true, "Throws potions automatically", new String[] { "apn", "autopotn" });
        AutoPot.delay = new Property<Long>(this, "Delay", 350L);
        AutoPot.health = new Property<Integer>(this, "Health", 10);
        AutoPot.potting = false;
        this.timer = new Timer();
        this.needsToPot = false;
    }
    
    @Override
    public void onEnable() {
        super.onEnable();
        this.setDisplayName("AutoPot§7 - " + AutoPot.pots);
    }
    
    @EventTarget
    public void EventPreMotionUpdates(final EventPreMotionUpdates event) {
        if (this.updateCounter() == 0) {
            return;
        }
        if (this.timer.hasTimeElapsed(AutoPot.delay.getValue(), false) && this.needsToPot && this.doesHotbarHavePots()) {
            this.oldYaw = Wrapper.mc.thePlayer.rotationYaw;
            this.oldPitch = Wrapper.mc.thePlayer.rotationPitch;
            AutoPot.potting = true;
            Wrapper.mc.thePlayer.rotationYaw = -Wrapper.mc.thePlayer.rotationYaw;
            Wrapper.mc.thePlayer.rotationPitch = 90.0f;
        }
    }
    
    @EventTarget
    public void onPost(final EventPostMotionUpdates event) {
        this.needsToPot = (Wrapper.mc.thePlayer.getHealth() <= AutoPot.health.getValue());
        if (this.timer.hasTimeElapsed(AutoPot.delay.getValue(), false) && this.needsToPot) {
            if (this.doesHotbarHavePots()) {
                if (AutoPot.potting) {
                    if (this.needsToPot) {
                        for (int i = 36; i < 45; ++i) {
                            final ItemStack stack = Wrapper.mc.thePlayer.inventoryContainer.getSlot(i).getStack();
                            if (stack != null && this.isStackSplashHealthPot(stack)) {
                                final int oldSlot = Wrapper.mc.thePlayer.inventory.currentItem;
                                Wrapper.mc.thePlayer.inventory.currentItem = i - 36;
                                Wrapper.mc.playerController.sendUseItem(Wrapper.mc.thePlayer, Wrapper.mc.theWorld, stack);
                                Wrapper.mc.thePlayer.inventory.currentItem = oldSlot;
                                Wrapper.mc.thePlayer.rotationYaw = this.oldYaw;
                                Wrapper.mc.thePlayer.rotationPitch = this.oldPitch;
                                this.needsToPot = false;
                                AutoPot.potting = false;
                                break;
                            }
                        }
                    }
                    AutoPot.potting = false;
                    this.timer.reset();
                }
            }
            else {
                this.getPotsFromInventory();
            }
        }
        this.setDisplayName("AutoPot§7 - " + AutoPot.pots);
    }
    
    @EventTarget
    public void onPacketReceive(final EventPacketReceive e) {
        if (e.getPacket() instanceof S06PacketUpdateHealth) {
            final S06PacketUpdateHealth packetUpdateHealth = (S06PacketUpdateHealth)e.getPacket();
            if (!this.needsToPot) {
                this.needsToPot = (packetUpdateHealth.getHealth() <= AutoPot.health.getValue());
            }
        }
    }
    
    @Override
    public void onDisable() {
        super.onDisable();
        AutoPot.potting = false;
        this.needsToPot = false;
    }
    
    private boolean doesHotbarHavePots() {
        for (int i = 36; i < 45; ++i) {
            final ItemStack stack = Wrapper.mc.thePlayer.inventoryContainer.getSlot(i).getStack();
            if (stack != null && this.isStackSplashHealthPot(stack)) {
                return true;
            }
        }
        return false;
    }
    
    private void getPotsFromInventory() {
        for (int i = 9; i < 36; ++i) {
            final ItemStack stack = Wrapper.mc.thePlayer.inventoryContainer.getSlot(i).getStack();
            if (stack != null && this.isStackSplashHealthPot(stack)) {
                Wrapper.mc.playerController.windowClick(Wrapper.mc.thePlayer.openContainer.windowId, i, 1, 2, Wrapper.mc.thePlayer);
                break;
            }
        }
    }
    
    private boolean isStackSplashHealthPot(final ItemStack stack) {
        if (stack != null && stack.getItem() instanceof ItemPotion) {
            final ItemPotion potion = (ItemPotion)stack.getItem();
            if (ItemPotion.isSplash(stack.getItemDamage())) {
                for (final Object o : potion.getEffects(stack)) {
                    final PotionEffect effect = (PotionEffect)o;
                    if (effect.getPotionID() == Potion.heal.getId()) {
                        return true;
                    }
                }
            }
        }
        return false;
    }
    
    private int updateCounter() {
        AutoPot.pots = 0;
        for (int i = 9; i < 45; ++i) {
            final ItemStack stack = Wrapper.mc.thePlayer.inventoryContainer.getSlot(i).getStack();
            if (stack != null && this.isStackSplashHealthPot(stack)) {
                AutoPot.pots += stack.stackSize;
            }
        }
        return AutoPot.pots;
    }
    
    public static boolean CurrentlyPotting() {
        return AutoPot.potting;
    }
    
    @Override
    protected void addCommand() {
        Cheese.getInstance();
        final CommandManager commandManager = Cheese.commandManager;
        CommandManager.addCommand(new Command("AutoPot", "Invalid Arguments! Autopot ", new String[] { "ap" }, "<health | values>", "Configures Autopot.") {
            @EventTarget
            public void onTick(final EventTick tick) {
                Label_0651: {
                    try {
                        final String mode = EventChatSend.getMessage().split(" ")[1];
                        final String s;
                        final String s2;
                        switch ((s2 = (s = mode)).hashCode()) {
                            case -2137395588: {
                                if (!s2.equals("Health")) {
                                    break Label_0651;
                                }
                                break;
                            }
                            case -1221262756: {
                                if (!s2.equals("health")) {
                                    break Label_0651;
                                }
                                break;
                            }
                            case -823812830: {
                                if (!s2.equals("values")) {
                                    break Label_0651;
                                }
                                Logger.logChat("Delay: §e" + String.valueOf(AutoPot.delay.getValue()));
                                Logger.logChat("Health: §e" + String.valueOf(AutoPot.health.getValue()));
                                break Label_0651;
                            }
                            case 100: {
                                if (!s2.equals("d")) {}
                                break Label_0651;
                            }
                            case 104: {
                                if (!s2.equals("h")) {
                                    break Label_0651;
                                }
                                break;
                            }
                            case 3336: {
                                if (!s2.equals("hp")) {
                                    break Label_0651;
                                }
                                break;
                            }
                            case 65915235: {
                                if (!s2.equals("Delay")) {}
                                break Label_0651;
                            }
                            case 95467907: {
                                if (!s2.equals("delay")) {}
                                break Label_0651;
                            }
                            default: {
                                break Label_0651;
                            }
                        }
                        try {
                            final String message2 = EventChatSend.getMessage().split(" ")[2];
                            if (message2.equalsIgnoreCase("actual")) {
                                Logger.logChat("Actual health: §e" + String.valueOf(AutoPot.health.getValue()));
                            }
                            else if (message2.equalsIgnoreCase("reset")) {
                                AutoPot.health.setValue(AutoPot.health.getDefaultValue());
                                Logger.logChat("Autopot health reset. Set health to: §e" + AutoPot.health.getValue());
                            }
                            else {
                                AutoPot.health.setValue(Integer.parseInt(message2));
                                if (AutoPot.health.getValue() < 1) {
                                    AutoPot.health.setValue(1);
                                }
                                Logger.logChat("Autopot health set to: §e" + AutoPot.health.getValue());
                            }
                        }
                        catch (Exception e) {
                            Logger.logChat("Invalid Value! health <actual | reset || health>");
                        }
                        try {
                            final String message2 = EventChatSend.getMessage().split(" ")[2];
                            if (message2.equalsIgnoreCase("actual")) {
                                Logger.logChat("Actual delay: §e" + String.valueOf(AutoPot.delay.getValue()));
                            }
                            else if (message2.equalsIgnoreCase("reset")) {
                                AutoPot.delay.setValue(AutoPot.delay.getDefaultValue());
                                Logger.logChat("Autopot delay reset. Set delay to: §e" + AutoPot.delay.getValue());
                            }
                            else {
                                AutoPot.delay.value = Long.parseLong(message2);
                                if (AutoPot.delay.getValue() < 1L) {
                                    AutoPot.delay.value = 1L;
                                }
                                Logger.logChat("Autopot delay set to: §e" + AutoPot.delay.getValue());
                            }
                        }
                        catch (Exception e) {
                            Logger.logChat("Invalid Value! delay <actual | reset || delay>");
                        }
                    }
                    catch (Exception ex) {
                        Logger.logChat(String.valueOf(String.valueOf(this.getErrorMessage())) + this.getArguments());
                    }
                }
                Cheese.getInstance();
                Cheese.fileManager.saveFiles();
                this.Toggle();
            }
        });
    }
}
