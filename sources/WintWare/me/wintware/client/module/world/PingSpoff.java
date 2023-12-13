package me.wintware.client.module.world;

import me.wintware.client.Main;
import me.wintware.client.clickgui.settings.Setting;
import me.wintware.client.event.EventTarget;
import me.wintware.client.event.impl.EventSendPacket;
import me.wintware.client.module.Category;
import me.wintware.client.module.Module;
import net.minecraft.client.Minecraft;
import net.minecraft.network.play.client.CPacketConfirmTransaction;
import net.minecraft.network.play.client.CPacketKeepAlive;

public class PingSpoff
extends Module {
    long id;
    short tsid;
    int twid;
    int ticks;

    public PingSpoff() {
        super("PingSpoof", Category.World);
        Main.instance.setmgr.rSetting(new Setting("Delay", this, 15000.0, 0.0, 30000.0, false));
    }

    @EventTarget
    public void onSendPacket(EventSendPacket event) {
        int ping = (int)Main.instance.setmgr.getSettingByName("Delay").getValDouble();
        if (!mc.isSingleplayer()) {
            if (event.getPacket() instanceof CPacketConfirmTransaction) {
                if (this.tsid == ((CPacketConfirmTransaction)event.getPacket()).getUid() && this.twid == ((CPacketConfirmTransaction)event.getPacket()).getWindowId()) {
                    return;
                }
                event.setCancelled(true);
                new Thread(() -> {
                    try {
                        Thread.sleep(ping);
                    }
                    catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    this.tsid = ((CPacketConfirmTransaction)event.getPacket()).getUid();
                    this.twid = ((CPacketConfirmTransaction)event.getPacket()).getWindowId();
                    if (Minecraft.player == null) {
                        return;
                    }
                    if (Minecraft.player.connection == null) {
                        return;
                    }
                    Minecraft.player.connection.sendPacket(event.getPacket());
                }).start();
            }
            if (event.getPacket() instanceof CPacketKeepAlive) {
                if (this.id == ((CPacketKeepAlive)event.getPacket()).getKey()) {
                    return;
                }
                event.setCancelled(true);
                new Thread(() -> {
                    try {
                        Thread.sleep(ping);
                    }
                    catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    this.id = ((CPacketKeepAlive)event.getPacket()).getKey();
                    Minecraft.player.connection.sendPacket(event.getPacket());
                }).start();
            }
        }
    }
}

