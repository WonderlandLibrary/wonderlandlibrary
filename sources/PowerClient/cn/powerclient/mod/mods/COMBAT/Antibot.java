/*
 * Decompiled with CFR 0_132 Helper by Lightcolour E-mail wyy-666@hotmail.com.
 */
package me.AveReborn.mod.mods.COMBAT;

import com.darkmagician6.eventapi.EventTarget;
import com.google.common.collect.Ordering;
import com.mojang.authlib.GameProfile;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;
import me.AveReborn.Value;
import me.AveReborn.events.EventPacket;
import me.AveReborn.events.EventUpdate;
import me.AveReborn.mod.Category;
import me.AveReborn.mod.Mod;
import me.AveReborn.ui.ClientNotification;
import me.AveReborn.util.ChatType;
import me.AveReborn.util.ClientUtil;
import me.AveReborn.util.timeUtils.TimeHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.GuiIngame;
import net.minecraft.client.gui.GuiPlayerTabOverlay;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S0CPacketSpawnPlayer;
import net.minecraft.util.IChatComponent;

public class Antibot
extends Mod {
    public static ArrayList<Entity> invalid = new ArrayList();
    public TimeHelper timer = new TimeHelper();
    public Value<Boolean> remove = new Value<Boolean>("Antibot_Remove", false);
    public Value<Double> delay = new Value<Double>("Antibot_Delay", 20.0, 10.0, 60.0, 1.0);
    public Value<String> atb_mode = new Value("Antibot", "Mode", 1);

    public Antibot() {
        super("Antibot", Category.COMBAT);
        this.atb_mode.mode.add("Basic");
        this.atb_mode.mode.add("Watchdog");
    }

    @EventTarget
    public void onUpdate(EventUpdate event) {
        Entity ent;
        if (this.atb_mode.isCurrentMode("Basic")) {
            this.setDisplayName("Basic");
            for (Object entity : this.mc.theWorld.loadedEntityList) {
                ent = (Entity)entity;
                if (ent == Minecraft.thePlayer || !ent.isInvisible() || ent.ticksExisted < 105) continue;
                ent.ticksExisted = -1;
                ent.isDead = true;
                ent.setInvisible(true);
            }
        }
        if (this.atb_mode.isCurrentMode("Watchdog")) {
            this.setDisplayName("Watchdog");
            for (Object obj : this.mc.theWorld.loadedEntityList) {
                if (!(obj instanceof EntityPlayer)) continue;
                ent = (EntityPlayer)obj;
                String str = ent.getDisplayName().getFormattedText();
                if (ent.getDistanceToEntity(Minecraft.thePlayer) > 10.0f || ent == Minecraft.thePlayer || invalid.contains(ent) || ent.isDead || this.getTabPlayerList().contains(ent)) continue;
                if (this.remove.getValueState().booleanValue()) {
                    this.mc.theWorld.removeEntity(ent);
                    this.timer.reset();
                    continue;
                }
                invalid.add(ent);
                this.timer.reset();
            }
            if (!invalid.isEmpty() && this.timer.delay(this.delay.getValueState() * 1000.0)) {
                ClientUtil.sendClientMessage("Antibot AUTO CLEAR!", ClientNotification.Type.INFO);
                invalid.clear();
                this.timer.reset();
            }
        }
    }

    @EventTarget
    public void onReceivePacket(EventPacket event) {
        double posX;
        double difZ;
        double difX;
        double difY;
        double posY;
        S0CPacketSpawnPlayer lol;
        double posZ;
        double dist;
        if (this.atb_mode.isCurrentMode("Watchdog") && event.getPacket() instanceof S0CPacketSpawnPlayer && (dist = Math.sqrt((difX = Minecraft.thePlayer.posX - (posX = (double)(lol = (S0CPacketSpawnPlayer)event.getPacket()).getX() / 32.0)) * difX + (difY = Minecraft.thePlayer.posY - (posY = (double)lol.getY() / 32.0)) * difY + (difZ = Minecraft.thePlayer.posZ - (posZ = (double)lol.getZ() / 32.0)) * difZ)) <= 17.0 && posX != Minecraft.thePlayer.posX && posY != Minecraft.thePlayer.posY && posZ != Minecraft.thePlayer.posZ) {
            lol = (S0CPacketSpawnPlayer)event.getPacket();
            ClientUtil.sendChatMessage("Hypixel bot Detected " + this.mc.getNetHandler().getPlayerInfo(lol.getPlayer()).getDisplayName().getUnformattedText(), ChatType.WARN);
            event.setCancelled(true);
        }
    }

    @Override
    public void onDisable() {
        if (!invalid.isEmpty()) {
            invalid.clear();
        }
        super.onDisable();
    }

    public List<EntityPlayer> getTabPlayerList() {
        NetHandlerPlayClient nhpc = Minecraft.thePlayer.sendQueue;
        ArrayList<EntityPlayer> list = new ArrayList<EntityPlayer>();
        new net.minecraft.client.gui.GuiPlayerTabOverlay(this.mc, this.mc.ingameGUI);
        List<NetworkPlayerInfo> players = GuiPlayerTabOverlay.field_175252_a.sortedCopy(nhpc.getPlayerInfoMap());
        for (NetworkPlayerInfo o2 : players) {
            NetworkPlayerInfo info = o2;
            if (info == null) continue;
            list.add(this.mc.theWorld.getPlayerEntityByName(info.getGameProfile().getName()));
        }
        return list;
    }
}

