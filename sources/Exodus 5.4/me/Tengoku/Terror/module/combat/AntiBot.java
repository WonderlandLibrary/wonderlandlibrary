/*
 * Decompiled with CFR 0.152.
 */
package me.Tengoku.Terror.module.combat;

import de.Hero.settings.Setting;
import java.util.ArrayList;
import java.util.List;
import me.Tengoku.Terror.Exodus;
import me.Tengoku.Terror.event.EventTarget;
import me.Tengoku.Terror.event.events.EventMotion;
import me.Tengoku.Terror.event.events.EventPacket;
import me.Tengoku.Terror.event.events.EventUpdate;
import me.Tengoku.Terror.event.events.EventUseEntity;
import me.Tengoku.Terror.module.Category;
import me.Tengoku.Terror.module.Module;
import me.Tengoku.Terror.module.combat.KillAura;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiPlayerTabOverlay;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.play.server.S0CPacketSpawnPlayer;

public class AntiBot
extends Module {
    double y;

    @EventTarget
    public void onPre(EventMotion eventMotion) {
        this.y = EventMotion.getY();
    }

    @EventTarget
    public void onHit(EventUseEntity eventUseEntity) {
        String string = Exodus.INSTANCE.settingsManager.getSettingByName("AntiBot Mode").getValString();
        if (string.equalsIgnoreCase("UUID")) {
            Entity entity = eventUseEntity.getEntity();
            if (mc.getNetHandler().getPlayerInfo(entity.getUniqueID()) == null) {
                Minecraft.theWorld.removeEntity(entity);
            }
        }
        if (string.equalsIgnoreCase("Spoof Attack") && KillAura.target != null) {
            eventUseEntity.setEntity(KillAura.target);
        }
    }

    public static List<EntityPlayer> getTabPlayerList() {
        Minecraft minecraft = Minecraft.getMinecraft();
        NetHandlerPlayClient netHandlerPlayClient = Minecraft.thePlayer.sendQueue;
        ArrayList<EntityPlayer> arrayList = new ArrayList<EntityPlayer>();
        List list = GuiPlayerTabOverlay.field_175252_a.sortedCopy(netHandlerPlayClient.getPlayerInfoMap());
        for (Object e : list) {
            NetworkPlayerInfo networkPlayerInfo = (NetworkPlayerInfo)e;
            if (networkPlayerInfo == null) continue;
            arrayList.add(Minecraft.theWorld.getPlayerEntityByName(networkPlayerInfo.getGameProfile().getName()));
        }
        return arrayList;
    }

    public AntiBot() {
        super("Anti Bot", 0, Category.COMBAT, "Removes bots from the world.");
    }

    @EventTarget
    public void onUpdate(EventUpdate eventUpdate) {
        String string = Exodus.INSTANCE.settingsManager.getSettingByName("AntiBot Mode").getValString();
        this.setDisplayName("AntiBot \ufffdf" + string);
        if (string.equalsIgnoreCase("Watchdog")) {
            for (Entity entity : Minecraft.theWorld.loadedEntityList) {
                if (entity == Minecraft.thePlayer || !this.isBot(entity)) continue;
                Minecraft.theWorld.removeEntity(entity);
            }
        }
    }

    @Override
    public void setup() {
        ArrayList<String> arrayList = new ArrayList<String>();
        arrayList.add("Advanced");
        arrayList.add("Watchdog");
        arrayList.add("UUID");
        arrayList.add("Spoof Attack");
        Exodus.INSTANCE.settingsManager.addSetting(new Setting("AntiBot Mode", (Module)this, "Advanced", arrayList));
    }

    public boolean isBot(Entity entity) {
        return entity.isInvisible() && entity.getUniqueID() == null;
    }

    @EventTarget
    public void onReceivePacket(EventPacket eventPacket) {
        String string = Exodus.INSTANCE.settingsManager.getSettingByName("AntiBot Mode").getValString();
        this.setDisplayName("AntiBot \ufffdf" + string);
        if (string.equalsIgnoreCase("Advanced") && eventPacket.getPacket() instanceof S0CPacketSpawnPlayer) {
            S0CPacketSpawnPlayer s0CPacketSpawnPlayer = (S0CPacketSpawnPlayer)eventPacket.getPacket();
            double d = (double)s0CPacketSpawnPlayer.getX() / 32.0;
            double d2 = (double)s0CPacketSpawnPlayer.getY() / 32.0;
            double d3 = (double)s0CPacketSpawnPlayer.getZ() / 32.0;
            double d4 = Minecraft.thePlayer.posX - d;
            double d5 = Minecraft.thePlayer.posY - d2;
            double d6 = Minecraft.thePlayer.posZ - d3;
            double d7 = Math.sqrt(d4 * d4 + d5 * d5 + d6 * d6);
            if (d7 <= 170.0) {
                if (d != Minecraft.thePlayer.posX) {
                    if (d2 != Minecraft.thePlayer.posY) {
                        if (d3 != Minecraft.thePlayer.posZ) {
                            eventPacket.setCancelled(true);
                        }
                    }
                }
            }
        }
    }
}

