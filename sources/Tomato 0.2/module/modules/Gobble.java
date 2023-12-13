package cow.milkgod.cheese.module.modules;

import cow.milkgod.cheese.properties.*;
import cow.milkgod.cheese.module.*;
import net.minecraft.network.*;
import net.minecraft.util.*;
import com.darkmagician6.eventapi.*;
import net.minecraft.network.play.client.*;
import net.minecraft.item.*;
import cow.milkgod.cheese.*;
import cow.milkgod.cheese.events.*;
import cow.milkgod.cheese.utils.*;
import cow.milkgod.cheese.commands.*;

public class Gobble extends Module
{
    private Property<Integer> delay;
    
    public Gobble() {
        super("Gobble", 0, Category.EXPLOITS, 15120384, true, "Uses usable items faster.", new String[] { "fastuse", "fc", "fastc", "fconsume" });
        this.delay = new Property<Integer>(this, "delay", 13);
    }
    
    @EventTarget
    public void onTick(final EventTick event) {
        if (Wrapper.mc.thePlayer.getItemInUseDuration() >= this.delay.value && this.isUsable(Wrapper.mc.thePlayer.getCurrentEquippedItem())) {
            for (int loop = 0; loop < 25; ++loop) {
                if (Wrapper.mc.thePlayer.onGround) {
                    Wrapper.mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer(false));
                }
                else {
                    Wrapper.mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer());
                }
            }
            Wrapper.mc.thePlayer.sendQueue.addToSendQueue(new C08PacketPlayerBlockPlacement(Wrapper.mc.thePlayer.inventory.getCurrentItem()));
            Wrapper.mc.thePlayer.sendQueue.addToSendQueue(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.RELEASE_USE_ITEM, new BlockPos(-1, -1, -1), EnumFacing.fromAngle(-1.0)));
            Wrapper.mc.thePlayer.stopUsingItem();
        }
    }
    
    @EventTarget
    public void onPacketSend(final EventPacketSent e) {
        if (Wrapper.mc.thePlayer.isUsingItem() && e.getPacket() instanceof C0APacketAnimation && this.isUsable(Wrapper.mc.thePlayer.getCurrentEquippedItem()) && !Wrapper.mc.thePlayer.isSwingInProgress) {
            e.setCancelled(true);
        }
    }
    
    @EventTarget
    public void onPost(final EventPostMotionUpdates e) {
    }
    
    private boolean isUsable(final ItemStack stack) {
        if (stack.getItem() instanceof ItemBow) {
            return this.delay.value >= 10;
        }
        return stack.getItem() instanceof ItemFood || stack.getItem() instanceof ItemPotion || stack.getItem() instanceof ItemBucketMilk;
    }
    
    @Override
    protected void addCommand() {
        Cheese.getInstance();
        final CommandManager commandManager = Cheese.commandManager;
        CommandManager.addCommand(new Command("Fastconsume", "Unknown option! ", new String[] { "fs" }, "<delay>", "Changes fastconsume's delay of eating.") {
            @EventTarget
            public void onEvent(final EventTick tick) {
                Label_0424: {
                    try {
                        final String mode = EventChatSend.getMessage().split(" ")[1];
                        Label_0360: {
                            final String s;
                            final String s2;
                            switch ((s2 = (s = mode)).hashCode()) {
                                case -823812830: {
                                    if (!s2.equals("values")) {
                                        break Label_0360;
                                    }
                                    Logger.logChat("Delay: §e" + String.valueOf(Gobble.this.delay.value));
                                    break Label_0424;
                                }
                                case 100: {
                                    if (!s2.equals("d")) {
                                        break Label_0360;
                                    }
                                    break;
                                }
                                case 65915235: {
                                    if (!s2.equals("Delay")) {
                                        break Label_0360;
                                    }
                                    break;
                                }
                                case 95467907: {
                                    if (!s2.equals("delay")) {
                                        break Label_0360;
                                    }
                                    break;
                                }
                                default: {
                                    break Label_0360;
                                }
                            }
                            try {
                                final String delayerino = EventChatSend.getMessage().split(" ")[2];
                                if (delayerino.equalsIgnoreCase("actual")) {
                                    Logger.logChat("Actual delay: §e" + String.valueOf(Gobble.this.delay.value));
                                }
                                else if (delayerino.equalsIgnoreCase("reset")) {
                                    Gobble.this.delay.value = 15;
                                    Logger.logChat("Fastconsume's delay reset. Set delay to: §e" + Gobble.this.delay.value);
                                }
                                else {
                                    Gobble.this.delay.value = Integer.parseInt(delayerino);
                                    if (Gobble.this.delay.value > 25) {
                                        Gobble.this.delay.value = 25;
                                    }
                                    Logger.logChat("Fastconsume's delay set to: §e" + Gobble.this.delay.value);
                                }
                            }
                            catch (Exception e) {
                                Logger.logChat("Invalid Value! Fastconsume <actual | reset || delay>");
                            }
                            break Label_0424;
                        }
                        Logger.logChat(String.valueOf(String.valueOf(this.getErrorMessage())) + this.getArguments());
                    }
                    catch (Exception e2) {
                        Logger.logChat(String.valueOf(String.valueOf(this.getErrorMessage())) + this.getArguments());
                    }
                }
                this.Toggle();
            }
        });
    }
}
