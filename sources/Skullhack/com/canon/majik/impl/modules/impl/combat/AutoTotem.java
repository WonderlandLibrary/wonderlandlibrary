package com.canon.majik.impl.modules.impl.combat;

import com.canon.majik.api.event.eventBus.EventListener;
import com.canon.majik.api.event.events.PacketEvent;
import com.canon.majik.api.event.events.TickEvent;
import com.canon.majik.api.utils.player.PlayerUtils;
import com.canon.majik.impl.modules.api.Category;
import com.canon.majik.impl.modules.api.Module;
import com.canon.majik.impl.setting.settings.ModeSetting;
import com.canon.majik.impl.setting.settings.NumberSetting;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.init.Items;
import net.minecraft.inventory.ClickType;
import net.minecraft.item.Item;
import net.minecraft.network.play.client.CPacketClickWindow;
import net.minecraft.network.play.client.CPacketEntityAction;

public class AutoTotem extends Module {

    NumberSetting health = setting("Health", 10, 1, 36);
    ModeSetting itemMode = setting("Item", "Crystal", "Crystal", "Totem", "Sword", "Gapple");
    NumberSetting delay = setting("Delay", 20, 0, 70);
    private float totemTime;

    public AutoTotem(String name, Category category) {
        super(name, category);
    }

    @EventListener
    public void onTick(TickEvent event) {
        if (nullCheck() && mc.currentScreen instanceof GuiContainer && !(mc.currentScreen instanceof GuiInventory))
            return;
        int totemslot = health.getValue().floatValue() >= mc.player.getHealth() ? PlayerUtils.getItemSlot(Items.TOTEM_OF_UNDYING) : PlayerUtils.getItemSlot(switchTo());
        if (mc.player.getHeldItemOffhand().getItem() != Items.TOTEM_OF_UNDYING && totemslot != -1 &&
                System.currentTimeMillis() - totemTime > delay.getValue().floatValue()) {
            mc.playerController.windowClick(mc.player.inventoryContainer.windowId, totemslot, 0, ClickType.PICKUP, mc.player);
            mc.playerController.windowClick(mc.player.inventoryContainer.windowId, 45, 0, ClickType.PICKUP, mc.player);
            mc.playerController.windowClick(mc.player.inventoryContainer.windowId, totemslot, 0, ClickType.PICKUP, mc.player);
            mc.playerController.updateController();
            totemTime = System.currentTimeMillis();
        }else if(mc.player.getHeldItemOffhand().getItem() != switchTo() && totemslot != -1 &&
                System.currentTimeMillis() - totemTime > delay.getValue().floatValue()){
            mc.playerController.windowClick(mc.player.inventoryContainer.windowId, totemslot, 0, ClickType.PICKUP, mc.player);
            mc.playerController.windowClick(mc.player.inventoryContainer.windowId, 45, 0, ClickType.PICKUP, mc.player);
            mc.playerController.windowClick(mc.player.inventoryContainer.windowId, totemslot, 0, ClickType.PICKUP, mc.player);
            mc.playerController.updateController();
            totemTime = System.currentTimeMillis();
        }
    }

    @EventListener
    public void onPacketSend(PacketEvent.Send event) {
        if (event.getPacket() instanceof CPacketClickWindow) {
            mc.player.connection.sendPacket(new CPacketEntityAction(mc.player, CPacketEntityAction.Action.STOP_SPRINTING));
        }
    }

    public Item switchTo(){
        switch (itemMode.getValue()){
            case "Crystal":
                return Items.END_CRYSTAL;
            case "Gapple":
                return Items.GOLDEN_APPLE;
        }
        return Items.TOTEM_OF_UNDYING;
    }
}
