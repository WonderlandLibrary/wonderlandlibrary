// 
// Decompiled by Procyon v0.5.36
// 

package ru.tuskevich.modules.impl.MISC;

import ru.tuskevich.event.EventTarget;
import net.minecraft.network.play.client.CPacketKeepAlive;
import net.minecraft.network.Packet;
import net.minecraft.client.Minecraft;
import net.minecraft.network.play.client.CPacketConfirmTransaction;
import ru.tuskevich.event.events.impl.EventPacket;
import ru.tuskevich.ui.dropui.setting.Setting;
import ru.tuskevich.ui.dropui.setting.imp.SliderSetting;
import ru.tuskevich.modules.Type;
import ru.tuskevich.modules.ModuleAnnotation;
import ru.tuskevich.modules.Module;

@ModuleAnnotation(name = "PingSpoof", desc = "\ufffd\ufffd\ufffd\ufffd\ufffd\ufffd\ufffd\ufffd \ufffd\ufffd\ufffd\ufffd \ufffd\ufffd \ufffd\ufffd\ufffd\ufffd\ufffd\ufffd\ufffd", type = Type.MISC)
public class PingSpoof extends Module
{
    long id;
    short tsid;
    int twid;
    int ticks;
    SliderSetting spoof;
    
    public PingSpoof() {
        this.spoof = new SliderSetting("Spoof Delay", 25000.0f, 50.0f, 30000.0f, 5.0f);
        this.add(this.spoof);
    }
    
    @EventTarget
    public void onSend(final EventPacket event) {
        final long delay = (long)this.spoof.getFloatValue();
        if (!PingSpoof.mc.isSingleplayer()) {
            if (event.getPacket() instanceof CPacketConfirmTransaction) {
                if (this.tsid == ((CPacketConfirmTransaction)event.getPacket()).getUid() && this.twid == ((CPacketConfirmTransaction)event.getPacket()).getWindowId()) {
                    return;
                }
                event.cancel();
                final long n;
                final Minecraft mc;
                Minecraft mc2;
                Minecraft mc3;
                new Thread(() -> {
                    try {
                        Thread.sleep(n);
                    }
                    catch (InterruptedException var5) {
                        var5.printStackTrace();
                    }
                    this.tsid = ((CPacketConfirmTransaction)event.getPacket()).getUid();
                    this.twid = ((CPacketConfirmTransaction)event.getPacket()).getWindowId();
                    mc = PingSpoof.mc;
                    if (Minecraft.player != null) {
                        mc2 = PingSpoof.mc;
                        if (Minecraft.player.connection != null) {
                            mc3 = PingSpoof.mc;
                            Minecraft.player.connection.sendPacket(event.getPacket());
                        }
                    }
                    return;
                }).start();
            }
            if (event.getPacket() instanceof CPacketKeepAlive) {
                if (this.id == ((CPacketKeepAlive)event.getPacket()).getKey()) {
                    return;
                }
                event.cancel();
                final long n2;
                final Minecraft mc4;
                new Thread(() -> {
                    try {
                        Thread.sleep(n2);
                    }
                    catch (InterruptedException var6) {
                        var6.printStackTrace();
                    }
                    this.id = ((CPacketKeepAlive)event.getPacket()).getKey();
                    mc4 = PingSpoof.mc;
                    Minecraft.player.connection.sendPacket(event.getPacket());
                }).start();
            }
        }
    }
}
