// 
// Decompiled by Procyon v0.5.36
// 

package Lavish.modules.movement;

import Lavish.utils.misc.NetUtil;
import java.util.Iterator;
import Lavish.utils.movement.MovementUtil;
import Lavish.event.events.EventUpdate;
import Lavish.event.events.EventRenderGUI;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.network.play.client.C02PacketUseEntity;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C00PacketKeepAlive;
import net.minecraft.network.play.client.C16PacketClientStatus;
import net.minecraft.network.play.client.C01PacketChatMessage;
import Lavish.event.events.EventPacket;
import Lavish.event.Event;
import Lavish.ClickGUI.settings.Setting;
import Lavish.Client;
import Lavish.modules.Category;
import Lavish.utils.misc.Timer;
import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraft.network.Packet;
import java.util.ArrayList;
import Lavish.modules.Module;

public class Teleport extends Module
{
    public ArrayList<Packet> packets;
    private EntityOtherPlayerMP fakePlayer;
    Timer timer;
    int ticks;
    
    public Teleport() {
        super("Teleport", 0, true, Category.Movement, "Teleports you");
        this.packets = new ArrayList<Packet>();
        this.timer = new Timer();
        final ArrayList<String> options = new ArrayList<String>();
        options.add("MMC");
        options.add("MMC 2");
        Client.instance.setmgr.rSetting(new Setting("Teleport Mode", this, "MMC", options));
    }
    
    @Override
    public void onEvent(final Event e) {
        if (Client.instance.setmgr.getSettingByName("Teleport Mode").getValString().equalsIgnoreCase("MMC 2")) {
            if (Teleport.mc.thePlayer == null) {
                return;
            }
            if (e instanceof EventPacket && ((EventPacket)e).isSending()) {
                if (!(((EventPacket)e).getPacket() instanceof C01PacketChatMessage) && !(((EventPacket)e).getPacket() instanceof C16PacketClientStatus) && !(((EventPacket)e).getPacket() instanceof C00PacketKeepAlive) && Teleport.mc.theWorld != null) {
                    e.setCancelled(true);
                }
                final boolean input = Teleport.mc.gameSettings.keyBindForward.pressed || Teleport.mc.gameSettings.keyBindBack.pressed || Teleport.mc.gameSettings.keyBindRight.pressed || Teleport.mc.gameSettings.keyBindLeft.pressed || Teleport.mc.gameSettings.keyBindJump.pressed || Teleport.mc.gameSettings.keyBindSneak.pressed;
                if (input && ((EventPacket)e).getPacket() instanceof C03PacketPlayer) {
                    this.packets.add(((EventPacket)e).getPacket());
                }
                if (((EventPacket)e).getPacket() instanceof C02PacketUseEntity || ((EventPacket)e).getPacket() instanceof C08PacketPlayerBlockPlacement || ((EventPacket)e).getPacket() instanceof C07PacketPlayerDigging) {
                    this.packets.add(((EventPacket)e).getPacket());
                }
            }
            if (e instanceof EventRenderGUI) {
                Client.instance.font20.drawString("Packets: " + this.packets.size(), (float)(Teleport.mc.displayWidth / 2 / 2 - Client.instance.font20.getStringWidth("Packets: " + this.packets.size()) / 2), 30.0f, -1, false);
            }
            if (e instanceof EventUpdate) {
                Teleport.mc.thePlayer.motionY = 0.0;
                MovementUtil.setSpeed(Client.instance.setmgr.getSettingByName("Fly Speed").getValDouble());
                if (Teleport.mc.thePlayer.hurtTime > 0) {
                    this.toggle();
                }
            }
        }
    }
    
    @Override
    public void onDisable() {
        if (Client.instance.setmgr.getSettingByName("Teleport Mode").getValString().equalsIgnoreCase("MMC 2")) {
            for (final Packet packet : this.packets) {
                if (packet instanceof C02PacketUseEntity || packet instanceof C08PacketPlayerBlockPlacement || packet instanceof C07PacketPlayerDigging) {
                    Teleport.mc.thePlayer.swingItem();
                }
                Teleport.mc.thePlayer.sendQueue.addToSendQueue(packet);
            }
            this.packets.clear();
        }
        Teleport.mc.timer.timerSpeed = 1.0f;
    }
    
    public static void damage() {
        for (int i = 0; i < 9; ++i) {
            NetUtil.sendPacketNoEvents(new C03PacketPlayer.C04PacketPlayerPosition(Teleport.mc.thePlayer.posX, Teleport.mc.thePlayer.posY + 0.41999998688697815, Teleport.mc.thePlayer.posZ, false));
            NetUtil.sendPacketNoEvents(new C03PacketPlayer.C04PacketPlayerPosition(Teleport.mc.thePlayer.posX, Teleport.mc.thePlayer.posY + 6.248688697814067E-5, Teleport.mc.thePlayer.posZ, false));
            NetUtil.sendPacketNoEvents(new C03PacketPlayer(false));
        }
        NetUtil.sendPacketNoEvents(new C03PacketPlayer(true));
    }
    
    @Override
    public void onEnable() {
        if (Client.instance.setmgr.getSettingByName("Teleport Mode").getValString().equalsIgnoreCase("MMC 2")) {
            this.timer.reset();
        }
        this.ticks = 0;
        Teleport.mc.timer.timerSpeed = 1.0f;
    }
    
    @Override
    public void onUpdate() {
        if (Client.instance.setmgr.getSettingByName("Teleport Mode").getValString().equalsIgnoreCase("MMC")) {
            if (this.ticks == 1) {
                Teleport.mc.timer.timerSpeed = 0.1f;
            }
            if (this.ticks >= 13) {
                Teleport.mc.timer.timerSpeed = 500000.0f;
            }
            if (this.ticks >= 120) {
                this.toggle();
            }
            ++this.ticks;
        }
    }
}
