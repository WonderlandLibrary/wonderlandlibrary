// 
// Decompiled by Procyon v0.5.36
// 

package vip.Resolute.modules.impl.player;

import net.minecraft.item.ItemStack;
import net.minecraft.item.Item;
import vip.Resolute.Resolute;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C09PacketHeldItemChange;
import net.minecraft.init.Items;
import vip.Resolute.events.impl.EventMotion;
import vip.Resolute.events.Event;
import vip.Resolute.settings.Setting;
import vip.Resolute.util.misc.TimerUtil;
import vip.Resolute.settings.impl.ModeSetting;
import vip.Resolute.settings.impl.NumberSetting;
import vip.Resolute.modules.Module;

public class AutoGapple extends Module
{
    public NumberSetting delay;
    public NumberSetting health;
    public ModeSetting eatMode;
    public TimerUtil timer;
    
    public AutoGapple() {
        super("AutoGapple", 0, "Automatically eats gapples", Category.PLAYER);
        this.delay = new NumberSetting("Delay", 150.0, 0.0, 1000.0, 5.0);
        this.health = new NumberSetting("Health", 5.0, 1.0, 9.0, 1.0);
        this.eatMode = new ModeSetting("Consume Mode", "Instant", new String[] { "Instant" });
        this.timer = new TimerUtil();
        this.addSettings(this.delay, this.health, this.eatMode);
    }
    
    @Override
    public void onEvent(final Event e) {
        if (e instanceof EventMotion) {
            if (!this.timer.hasElapsed((long)this.delay.getValue())) {
                return;
            }
            if (AutoGapple.mc.thePlayer.getHealth() <= this.health.getValue() * 2.0) {
                this.doEat(false);
                this.timer.reset();
            }
        }
    }
    
    private void doEat(final boolean warn) {
        final int gappleInHotbar = findItem(36, 54, Items.golden_apple);
        if (gappleInHotbar != -1) {
            AutoGapple.mc.getNetHandler().addToSendQueue(new C09PacketHeldItemChange(gappleInHotbar - 36));
            AutoGapple.mc.getNetHandler().addToSendQueue(new C08PacketPlayerBlockPlacement(AutoGapple.mc.thePlayer.getHeldItem()));
            for (int i = 0; i <= 35; ++i) {
                AutoGapple.mc.getNetHandler().addToSendQueue(new C03PacketPlayer(AutoGapple.mc.thePlayer.onGround));
            }
            AutoGapple.mc.getNetHandler().addToSendQueue(new C09PacketHeldItemChange(AutoGapple.mc.thePlayer.inventory.currentItem));
            Resolute.addChatMessage("Ate gapple");
        }
        else {
            Resolute.addChatMessage("No gapples in hotbar");
        }
    }
    
    public static int findItem(final int startSlot, final int endSlot, final Item item) {
        for (int i = startSlot; i < endSlot; ++i) {
            final ItemStack stack = AutoGapple.mc.thePlayer.inventoryContainer.getSlot(i).getStack();
            if (stack != null && stack.getItem() == item) {
                return i;
            }
        }
        return -1;
    }
}
