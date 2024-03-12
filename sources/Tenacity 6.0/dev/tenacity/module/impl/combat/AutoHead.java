// 
// Decompiled by Procyon v0.5.36
// 

package dev.tenacity.module.impl.combat;

import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.network.Packet;
import dev.tenacity.utils.server.PacketUtils;
import net.minecraft.network.play.client.C09PacketHeldItemChange;
import dev.tenacity.utils.player.InventoryUtils;
import net.minecraft.item.ItemSkull;
import net.minecraft.potion.Potion;
import dev.tenacity.event.impl.player.MotionEvent;
import dev.tenacity.module.settings.Setting;
import dev.tenacity.module.Category;
import dev.tenacity.utils.time.TimerUtil;
import dev.tenacity.module.settings.impl.NumberSetting;
import dev.tenacity.module.Module;

public class AutoHead extends Module
{
    private final NumberSetting delay;
    private final NumberSetting healPercent;
    private final TimerUtil timer;
    
    public AutoHead() {
        super("AutoHead", "Auto Head", Category.COMBAT, "auto consume heads");
        this.delay = new NumberSetting("Delay", 750.0, 3000.0, 0.0, 50.0);
        this.healPercent = new NumberSetting("Health %", 50.0, 99.0, 1.0, 1.0);
        this.timer = new TimerUtil();
        this.addSettings(this.delay, this.healPercent);
    }
    
    @Override
    public void onMotionEvent(final MotionEvent e) {
        if (AutoHead.mc.thePlayer != null && AutoHead.mc.theWorld != null && e.isPre() && (!AutoHead.mc.thePlayer.isPotionActive(Potion.moveSpeed) || !AutoHead.mc.thePlayer.isPotionActive(Potion.regeneration)) && AutoHead.mc.thePlayer.getHealth() / AutoHead.mc.thePlayer.getMaxHealth() * 100.0f <= this.healPercent.getValue() && this.timer.hasTimeElapsed(this.delay.getValue().longValue())) {
            for (int i = 0; i < 45; ++i) {
                final ItemStack is = AutoHead.mc.thePlayer.inventoryContainer.getSlot(i).getStack();
                if (is != null && is.getItem() instanceof ItemSkull && is.getDisplayName().contains("Head")) {
                    final int prevSlot = AutoHead.mc.thePlayer.inventory.currentItem;
                    if (i - 36 < 0) {
                        InventoryUtils.swap(i, 8);
                        PacketUtils.sendPacketNoEvent(new C09PacketHeldItemChange(8));
                    }
                    else {
                        PacketUtils.sendPacketNoEvent(new C09PacketHeldItemChange(i - 36));
                    }
                    PacketUtils.sendPacketNoEvent(new C08PacketPlayerBlockPlacement(is));
                    AutoHead.mc.thePlayer.inventory.currentItem = prevSlot;
                    PacketUtils.sendPacketNoEvent(new C09PacketHeldItemChange(prevSlot));
                    this.timer.reset();
                }
            }
        }
    }
}
