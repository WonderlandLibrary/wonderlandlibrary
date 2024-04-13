package dev.tenacity.module.impl.movement;

import dev.tenacity.event.Event;
import dev.tenacity.event.IEventListener;
import dev.tenacity.event.impl.packet.PacketReceiveEvent;
import dev.tenacity.event.impl.player.MotionEvent;
import dev.tenacity.event.impl.player.SlowDownEvent;
import dev.tenacity.module.Module;
import dev.tenacity.module.ModuleCategory;
import java.util.ArrayList;
import java.util.Arrays;
import dev.tenacity.setting.impl.ModeSetting;
import net.minecraft.item.ItemSword;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C0DPacketCloseWindow;
import net.minecraft.network.play.client.C0EPacketClickWindow;
import net.minecraft.network.play.server.S2FPacketSetSlot;
import net.minecraft.world.gen.layer.GenLayerEdge;

public final class NoSlowModule extends Module {

    private boolean isItemGhost;

    private final ModeSetting mode = new ModeSetting("Mode", "Vanilla", "Watchdog");

    public NoSlowModule() {
        super("NoSlow", "Makes you not take slow down", ModuleCategory.MOVEMENT);
        initializeSettings(mode);
    }

    private final IEventListener<SlowDownEvent> onSlowDownEvent = Event::cancel;

    private final IEventListener<MotionEvent> onMotionEvent = event -> {
        switch (mode.getCurrentMode()) {
            case "Vanilla": {
                if (event.isPre() && mc.thePlayer != null) {
                    if (!isItemGhost)
                        sendBypass();
                    if (!mc.thePlayer.isUsingItem())
                        isItemGhost = false;
                }
            }
            case "Watchdog": {
                if (mc.thePlayer.isUsingItem()) {
                    if (mc.currentScreen == null && mc.thePlayer.getHeldItem() != null) {
                    }
                }
                break;
            }
        }
    };

    private final IEventListener<PacketReceiveEvent> onPacketReceiveEvent = event -> {
        if (event.getPacket() instanceof S2FPacketSetSlot && mc.thePlayer != null) {
            final S2FPacketSetSlot s2f = (S2FPacketSetSlot) event.getPacket();
            if (s2f.func_149175_c() == 0 && mc.thePlayer.isUsingItem() && mc.currentScreen == null && mc.thePlayer.getHeldItem() != null && !(mc.thePlayer.getHeldItem().getItem() instanceof ItemSword)) {
                event.cancel();
                //isItemGhost = false;
            }
        }
    };

    private void sendBypass() {
        if (mc.thePlayer.isUsingItem() && mc.currentScreen == null && mc.thePlayer.getHeldItem() != null && !(mc.thePlayer.getHeldItem().getItem() instanceof ItemSword)) {
            mc.getNetHandler().addToSendQueue(new C0DPacketCloseWindow(0));
            mc.getNetHandler().addToSendQueue(new C0EPacketClickWindow(0, 36 + mc.thePlayer.inventory.currentItem, 0, 6, null, mc.thePlayer.inventoryContainer.getNextTransactionID(mc.thePlayer.inventory)));
            isItemGhost = true;
        }
    }
}
