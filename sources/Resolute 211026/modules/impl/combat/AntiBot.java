// 
// Decompiled by Procyon v0.5.36
// 

package vip.Resolute.modules.impl.combat;

import net.minecraft.client.network.NetworkPlayerInfo;
import java.util.Iterator;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.GuiPlayerTabOverlay;
import net.minecraft.entity.item.EntityArmorStand;
import vip.Resolute.Resolute;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.Entity;
import vip.Resolute.events.impl.EventUpdate;
import net.minecraft.util.MathHelper;
import net.minecraft.network.play.server.S0CPacketSpawnPlayer;
import vip.Resolute.events.impl.EventPacket;
import vip.Resolute.events.Event;
import vip.Resolute.settings.Setting;
import java.util.ArrayList;
import net.minecraft.entity.player.EntityPlayer;
import java.util.List;
import vip.Resolute.settings.impl.BooleanSetting;
import vip.Resolute.settings.impl.ModeSetting;
import vip.Resolute.modules.Module;

public class AntiBot extends Module
{
    public static ModeSetting mode;
    public BooleanSetting botKiller;
    private final String[] strings;
    public static List<EntityPlayer> watchdogBots;
    public static ArrayList bots;
    private static final ArrayList spawnedBots;
    public static boolean enabled;
    
    @Override
    public void onEnable() {
        super.onEnable();
        AntiBot.enabled = true;
        AntiBot.watchdogBots.clear();
    }
    
    @Override
    public void onDisable() {
        super.onDisable();
        AntiBot.enabled = false;
        AntiBot.watchdogBots.clear();
    }
    
    public AntiBot() {
        super("AntiBot", 0, "Removes anticheat bots", Category.COMBAT);
        this.botKiller = new BooleanSetting("Bot Killer", false, () -> AntiBot.mode.is("Hypixel"));
        this.strings = new String[] { "1st Killer - ", "1st Place - ", "You died! Want to play again? Click here!", " - Damage Dealt - ", "1st - ", "Winning Team - ", "Winners: ", "Winner: ", "Winning Team: ", " win the game!", "1st Place: ", "Last team standing!", "Winner #1 (", "Top Survivors", "Winners - " };
        this.addSettings(AntiBot.mode, this.botKiller);
    }
    
    @Override
    public void onEvent(final Event e) {
        this.setSuffix(AntiBot.mode.getMode());
        if (e instanceof EventPacket && AntiBot.mode.is("Advanced") && ((EventPacket)e).getPacket() instanceof S0CPacketSpawnPlayer) {
            final S0CPacketSpawnPlayer packet = ((EventPacket)e).getPacket();
            final double posX = packet.getX() / 32.0;
            final double posY = packet.getY() / 32.0;
            final double posZ = packet.getZ() / 32.0;
            final double diffX = AntiBot.mc.thePlayer.posX - posX;
            final double diffY = AntiBot.mc.thePlayer.posY - posY;
            final double diffZ = AntiBot.mc.thePlayer.posZ - posZ;
            final double dist = MathHelper.sqrt_double(diffX * diffX + diffY * diffY + diffZ * diffZ);
            if (dist <= 17.0 && posY > AntiBot.mc.thePlayer.posY + 1.0 && posX != AntiBot.mc.thePlayer.posX && posY != AntiBot.mc.thePlayer.posY && posZ != AntiBot.mc.thePlayer.posZ) {
                e.setCancelled(true);
            }
        }
        if (e instanceof EventUpdate && e.isPre()) {
            if (AntiBot.mode.is("Hypixel")) {
                if (AntiBot.mc.thePlayer.ticksExisted <= 500) {
                    for (final EntityPlayer entity : AntiBot.mc.theWorld.playerEntities) {
                        if (entity.getDistanceToEntity(AntiBot.mc.thePlayer) <= 17.0f && Math.abs(AntiBot.mc.thePlayer.posY - entity.posY) > 2.0 && !isOnSameTeam(entity) && entity != AntiBot.mc.thePlayer && !AntiBot.watchdogBots.contains(entity) && entity.ticksExisted != 0 && entity.ticksExisted <= 10) {
                            AntiBot.watchdogBots.add(entity);
                            Resolute.addChatMessage("Added bot: " + entity.getGameProfile().getName() + ", Distance: " + entity.getDistanceToEntity(AntiBot.mc.thePlayer) + ", Ticks Existed: " + entity.ticksExisted);
                        }
                    }
                }
                if (this.botKiller.isEnabled()) {
                    if (AntiBot.watchdogBots.isEmpty()) {
                        return;
                    }
                    AntiBot.watchdogBots.forEach(wdBots -> AntiBot.mc.theWorld.removeEntity(wdBots));
                }
            }
            if (AntiBot.mode.is("Hylex")) {
                for (final Entity entity2 : AntiBot.mc.theWorld.loadedEntityList) {
                    if (entity2 instanceof EntityArmorStand) {
                        AntiBot.mc.theWorld.removeEntity(entity2);
                    }
                }
            }
            if (AntiBot.mode.is("TabList")) {
                for (final EntityPlayer player : AntiBot.mc.theWorld.playerEntities) {
                    if (player == AntiBot.mc.thePlayer) {
                        continue;
                    }
                    if (GuiPlayerTabOverlay.getPlayers().contains(player)) {
                        continue;
                    }
                    AntiBot.bots.add(player);
                }
            }
            if (AntiBot.mode.is("Mineplex")) {
                if (AntiBot.mc.thePlayer.ticksExisted % 50 == 0) {
                    AntiBot.spawnedBots.clear();
                }
                for (final Object o : AntiBot.mc.theWorld.loadedEntityList) {
                    final Entity en = (Entity)o;
                    if (en instanceof EntityPlayer && !(en instanceof EntityPlayerSP)) {
                        final String customname = en.getCustomNameTag();
                        if (customname != "" || AntiBot.spawnedBots.contains(en)) {
                            continue;
                        }
                        AntiBot.spawnedBots.add(en);
                    }
                }
                AntiBot.spawnedBots.forEach(mineplexBots -> AntiBot.mc.theWorld.removeEntity(mineplexBots));
            }
        }
    }
    
    public static boolean isOnSameTeam(final EntityLivingBase entity) {
        if (entity.getTeam() != null && AntiBot.mc.thePlayer.getTeam() != null) {
            final char c1 = entity.getDisplayName().getFormattedText().charAt(1);
            final char c2 = AntiBot.mc.thePlayer.getDisplayName().getFormattedText().charAt(1);
            return c1 == c2;
        }
        return false;
    }
    
    public static boolean isInTablist(final EntityLivingBase player) {
        if (AntiBot.mc.isSingleplayer()) {
            return true;
        }
        for (final Object o : AntiBot.mc.getNetHandler().getPlayerInfoMap()) {
            final NetworkPlayerInfo playerInfo = (NetworkPlayerInfo)o;
            if (playerInfo.getGameProfile().getName().equalsIgnoreCase(((EntityPlayer)player).getGameProfile().getName())) {
                return true;
            }
        }
        return false;
    }
    
    static {
        AntiBot.mode = new ModeSetting("Mode", "Hypixel", new String[] { "Mineplex", "Hypixel", "Advanced" });
        AntiBot.watchdogBots = new ArrayList<EntityPlayer>();
        AntiBot.bots = new ArrayList();
        spawnedBots = new ArrayList();
        AntiBot.enabled = false;
    }
}
