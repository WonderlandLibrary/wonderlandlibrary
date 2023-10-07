/*
 * Decompiled with CFR 0_122.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.Ordering
 *  com.mojang.authlib.GameProfile
 */
package me.arithmo.module.impl.other;

import com.google.common.collect.Ordering;
import com.mojang.authlib.GameProfile;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import me.arithmo.event.Event;
import me.arithmo.event.RegisterEvent;
import me.arithmo.event.impl.EventMotion;
import me.arithmo.event.impl.EventPacket;
import me.arithmo.management.notifications.Notifications;
import me.arithmo.module.Module;
import me.arithmo.module.data.ModuleData;
import me.arithmo.module.data.Options;
import me.arithmo.module.data.Setting;
import me.arithmo.module.data.SettingsMap;
import me.arithmo.util.Timer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.GuiPlayerTabOverlay;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S0CPacketSpawnPlayer;
import net.minecraft.util.IChatComponent;
import net.minecraft.util.MathHelper;

public class AntiBot
extends Module {
    private static String HYPIXEL = "MODE";
    private String DEAD = "DEAD";
    private Timer timer = new Timer();
    private static List<EntityPlayer> invalid = new ArrayList<EntityPlayer>();

    public AntiBot(ModuleData data) {
        super(data);
        this.settings.put(this.DEAD, new Setting<Boolean>(this.DEAD, true, "Removes dead bodies from the game."));
        this.settings.put(HYPIXEL, new Setting<Options>(HYPIXEL, new Options("Mode", "Hypixel", new String[]{"Hypixel", "Packet"}), "Check method for bots."));
    }

    public static List<EntityPlayer> getInvalid() {
        return invalid;
    }

    @Override
    public void onEnable() {
        invalid.clear();
    }

    @Override
    public void onDisable() {
        invalid.clear();
    }

    @RegisterEvent(events={EventPacket.class, EventMotion.class})
    public void onEvent(Event event) {
        double posX;
        double var11;
        double var9;
        EventPacket ep;
        double entY;
        S0CPacketSpawnPlayer packet;
        double var7;
        double posZ;
        double posY;
        float distance;
        double entZ;
        double entX;
        String currentSetting = ((Options)((Setting)this.settings.get(HYPIXEL)).getValue()).getSelected();
        if (event instanceof EventPacket && currentSetting.equalsIgnoreCase("Packet") && (ep = (EventPacket)event).isIncoming() && ep.getPacket() instanceof S0CPacketSpawnPlayer && (distance = MathHelper.sqrt_double((var7 = (posX = AntiBot.mc.thePlayer.posX) - (entX = (double)((packet = (S0CPacketSpawnPlayer)ep.getPacket()).func_148942_f() / 32))) * var7 + (var9 = (posY = AntiBot.mc.thePlayer.posY) - (entY = (double)(packet.func_148949_g() / 32))) * var9 + (var11 = (posZ = AntiBot.mc.thePlayer.posZ) - (entZ = (double)(packet.func_148946_h() / 32))) * var11)) <= 17.0f && entY > AntiBot.mc.thePlayer.posY + 1.0 && AntiBot.mc.thePlayer.posX != entX && AntiBot.mc.thePlayer.posY != entY && AntiBot.mc.thePlayer.posZ != entZ) {
            Notifications.getManager().post("Bot Removal", "Removed bot at \u00a7c" + (int)distance + "\u00a7fm away.", 1500, Notifications.Type.INFO);
            ep.setCancelled(true);
        }
        if (event instanceof EventMotion) {
            EventMotion em = (EventMotion)event;
            this.setSuffix(currentSetting);
            if (em.isPre() && !currentSetting.equalsIgnoreCase("Packet")) {
                EntityPlayer ent;
                if (!invalid.isEmpty() && this.timer.delay(5000.0f)) {
                    invalid.clear();
                    this.timer.reset();
                }
                if (((Boolean)((Setting)this.settings.get(this.DEAD)).getValue()).booleanValue()) {
                    for (Object o : AntiBot.mc.theWorld.loadedEntityList) {
                        if (!(o instanceof EntityPlayer)) continue;
                        ent = (EntityPlayer)o;
                        assert (ent != AntiBot.mc.thePlayer);
                        if (!ent.isPlayerSleeping()) continue;
                        AntiBot.mc.theWorld.removeEntity(ent);
                    }
                }
                for (Object o : AntiBot.mc.theWorld.getLoadedEntityList()) {
                    if (!(o instanceof EntityPlayer) || (ent = (EntityPlayer)o) == AntiBot.mc.thePlayer || AntiBot.mc.thePlayer.getDistanceToEntity(ent) >= 5.0f) continue;
                    switch (currentSetting) {
                        case "Hypixel": {
                            String str = ent.getDisplayName().getFormattedText();
                            if ((!str.equalsIgnoreCase(ent.getName() + "\u00a7r") && !str.contains("NPC") || AntiBot.mc.thePlayer.getDisplayName().getFormattedText().equalsIgnoreCase(AntiBot.mc.thePlayer.getName() + "\u00a7r")) && (AntiBot.getPlayerList().contains(ent) || invalid.contains(ent))) break;
                            this.timer.reset();
                            invalid.add(ent);
                            break;
                        }
                        case "Mineplex": {
                            double y = ent.posY;
                            if (y <= AntiBot.mc.thePlayer.posY || ent.onGround) break;
                            this.timer.reset();
                            invalid.add(ent);
                            break;
                        }
                    }
                }
            }
        }
    }

    public static List<EntityPlayer> getPlayerList() {
        NetHandlerPlayClient var4 = AntiBot.mc.thePlayer.sendQueue;
        ArrayList<EntityPlayer> list = new ArrayList<EntityPlayer>();
        List players = GuiPlayerTabOverlay.field_175252_a.sortedCopy((Iterable)var4.func_175106_d());
        for (Object o : players) {
            NetworkPlayerInfo info = (NetworkPlayerInfo)o;
            if (info == null) continue;
            list.add(AntiBot.mc.theWorld.getPlayerEntityByName(info.getGameProfile().getName()));
        }
        return list;
    }
}

