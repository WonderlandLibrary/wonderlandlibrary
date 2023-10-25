/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  ru.wendoxd.wclassguard.WXFuscator
 */
package org.celestial.client.feature.impl.misc;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketConfirmTransaction;
import net.minecraft.network.play.client.CPacketKeepAlive;
import net.minecraft.network.play.client.CPacketPlayerTryUseItem;
import net.minecraft.network.play.client.CPacketVehicleMove;
import net.minecraft.util.EnumHand;
import org.celestial.client.Celestial;
import org.celestial.client.event.EventTarget;
import org.celestial.client.event.events.impl.packet.EventSendPacket;
import org.celestial.client.event.events.impl.player.EventUpdate;
import org.celestial.client.feature.Feature;
import org.celestial.client.feature.impl.Type;
import org.celestial.client.feature.impl.movement.Speed;
import org.celestial.client.helpers.math.MathematicHelper;
import org.celestial.client.helpers.misc.TimerHelper;
import org.celestial.client.settings.impl.BooleanSetting;
import org.celestial.client.settings.impl.ListSetting;
import org.celestial.client.settings.impl.NumberSetting;
import org.celestial.client.ui.notification.NotificationManager;
import org.celestial.client.ui.notification.NotificationType;
import ru.wendoxd.wclassguard.WXFuscator;

public class Disabler
extends Feature {
    private final List<Packet<?>> packets = new CopyOnWriteArrayList();
    private final TimerHelper timerHelper = new TimerHelper();
    public static ListSetting mode = new ListSetting("Disabler Mode", "Old MatrixVl", () -> true, "Old MatrixVl", "PingSpoof", "Sunrise New", "Sunrise Old", "Mineplex Combat");
    private final NumberSetting delay = new NumberSetting("Delay", 5000.0f, 1000.0f, 20000.0f, 1000.0f, () -> Disabler.mode.currentMode.equals("PingSpoof"));
    private final BooleanSetting noInventory = new BooleanSetting("No Inventory", true, () -> Disabler.mode.currentMode.equals("PingSpoof"));

    public Disabler() {
        super("Disabler", "\u041e\u0441\u043b\u0430\u0431\u043b\u044f\u0435\u0442 \u0432\u043e\u0437\u0434\u0435\u0439\u0441\u0442\u0432\u0438\u0435 \u0430\u043d\u0442\u0438\u0447\u0438\u0442\u043e\u0432 \u043d\u0430 \u0432\u0430\u0441", Type.Misc);
        this.addSettings(mode, this.delay, this.noInventory);
    }

    public static void init() {
    }

    @EventTarget
    public void onSendPacket1(EventSendPacket event) {
        if (!Disabler.mode.currentMode.equals("PingSpoof")) {
            return;
        }
        if (Disabler.mc.currentScreen instanceof GuiContainer && this.noInventory.getCurrentValue()) {
            return;
        }
        if (mc.isSingleplayer()) {
            return;
        }
        if (event.getPacket() instanceof CPacketKeepAlive) {
            if (this.packets.contains(event.getPacket())) {
                return;
            }
            event.setCancelled(true);
            this.packets.add(event.getPacket());
        }
        if (event.getPacket() instanceof CPacketConfirmTransaction) {
            if (this.packets.contains(event.getPacket())) {
                return;
            }
            event.setCancelled(true);
            this.packets.add(event.getPacket());
        }
    }

    @EventTarget
    public void onUpdate1(EventUpdate event) {
        if (!Disabler.mode.currentMode.equals("PingSpoof")) {
            return;
        }
        if (this.timerHelper.hasReached(this.delay.getCurrentValue())) {
            for (Packet<?> packet : this.packets) {
                if (!(packet instanceof CPacketKeepAlive) && !(packet instanceof CPacketConfirmTransaction)) continue;
                Disabler.mc.player.connection.sendPacket(packet);
            }
            this.packets.clear();
            this.timerHelper.reset();
        }
    }

    @Override
    public void onEnable() {
        if (Disabler.mode.currentMode.equals("Sunrise New")) {
            for (int i = 0; i < 5099; ++i) {
                Disabler.mc.player.connection.sendPacket(new CPacketVehicleMove(Disabler.mc.player));
            }
            NotificationManager.publicity("Disabler", "Disabler was activated! :)", 8, NotificationType.SUCCESS);
        }
        super.onEnable();
    }

    @WXFuscator
    @EventTarget
    public void onUpdate(EventUpdate event) {
        this.setSuffix(Disabler.mode.currentMode);
        if (Disabler.mode.currentMode.equals("Sunrise Old")) {
            Disabler.mc.player.connection.sendPacket(new CPacketPlayerTryUseItem(EnumHand.MAIN_HAND));
        }
        if (this.timerHelper.hasReached(5000.0) && Disabler.mode.currentMode.equals("Old MatrixVl")) {
            for (Packet<?> packet : this.packets) {
                if (!(packet instanceof CPacketKeepAlive) && !(packet instanceof CPacketConfirmTransaction)) continue;
                Disabler.mc.player.connection.sendPacket(packet);
            }
            this.packets.clear();
            this.timerHelper.reset();
        }
    }

    @EventTarget
    public void onSendPacket(EventSendPacket event) {
        if (Disabler.mode.currentMode.equals("Old MatrixVl")) {
            if (Disabler.mc.currentScreen instanceof GuiContainer && !Celestial.instance.featureManager.getFeatureByClass(Speed.class).getState()) {
                return;
            }
            if (mc.isSingleplayer()) {
                return;
            }
            if (event.getPacket() instanceof CPacketKeepAlive) {
                if (this.packets.contains(event.getPacket())) {
                    return;
                }
                event.setCancelled(true);
                this.packets.add(event.getPacket());
            }
            if (event.getPacket() instanceof CPacketConfirmTransaction) {
                if (this.packets.contains(event.getPacket())) {
                    return;
                }
                event.setCancelled(true);
                this.packets.add(event.getPacket());
            }
        } else if (Disabler.mode.currentMode.equals("Mineplex Combat") && event.getPacket() instanceof CPacketKeepAlive) {
            CPacketKeepAlive cPacketKeepAlive = (CPacketKeepAlive)event.packet;
            cPacketKeepAlive.key = (long)((float)cPacketKeepAlive.key - MathematicHelper.randomizeFloat(1000.0f, 2.14748365E9f));
        }
    }
}

