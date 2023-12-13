// 
// Decompiled by Procyon v0.5.30
// 

package exhibition.module.impl.combat;

import java.util.HashMap;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.client.gui.GuiPlayerTabOverlay;
import java.util.ArrayList;
import exhibition.event.RegisterEvent;
import java.util.Iterator;
import net.minecraft.entity.Entity;
import exhibition.event.impl.EventMotion;
import net.minecraft.util.MathHelper;
import net.minecraft.network.play.server.S0CPacketSpawnPlayer;
import exhibition.event.impl.EventPacket;
import exhibition.event.Event;
import exhibition.util.misc.ChatUtil;
import exhibition.module.data.Options;
import exhibition.module.data.Setting;
import exhibition.module.data.ModuleData;
import net.minecraft.entity.player.EntityPlayer;
import java.util.List;
import exhibition.util.Timer;
import exhibition.module.Module;

public class AntiBot extends Module
{
    public static String MODE;
    private String DEAD;
    private Timer timer;
    private static List<EntityPlayer> invalid;
    
    public AntiBot(final ModuleData data) {
        super(data);
        this.DEAD = "DEAD";
        this.timer = new Timer();
        ((HashMap<String, Setting<Boolean>>)this.settings).put(this.DEAD, new Setting<Boolean>(this.DEAD, true, "Removes dead bodies from the game."));
        ((HashMap<String, Setting<Options>>)this.settings).put(AntiBot.MODE, new Setting<Options>(AntiBot.MODE, new Options("Mode", "Hypixel", new String[] { "Hypixel", "Packet" }), "Check method for bots."));
    }
    
    public static List<EntityPlayer> getInvalid() {
        return AntiBot.invalid;
    }
    
    @Override
    public void onEnable() {
        AntiBot.invalid.clear();
    }
    
    @Override
    public void onDisable() {
        AntiBot.invalid.clear();
        if (AntiBot.mc.getCurrentServerData().serverIP.toLowerCase().contains("hypixel") || AntiBot.mc.getCurrentServerData().serverIP.toLowerCase().contains("mineplex")) {
            ChatUtil.printChat("§4[§cE§4]§8 AntiBot was kept enabled for your protection.");
            this.toggle();
        }
    }
    
    @RegisterEvent(events = { EventPacket.class, EventMotion.class })
    @Override
    public void onEvent(final Event event) {
        final String currentSetting = ((HashMap<K, Setting<Options>>)this.settings).get(AntiBot.MODE).getValue().getSelected();
        if (event instanceof EventPacket && currentSetting.equalsIgnoreCase("Packet")) {
            final EventPacket ep = (EventPacket)event;
            if (ep.isIncoming() && ep.getPacket() instanceof S0CPacketSpawnPlayer) {
                final S0CPacketSpawnPlayer packet = (S0CPacketSpawnPlayer)ep.getPacket();
                final double entX = packet.func_148942_f() / 32;
                final double entY = packet.func_148949_g() / 32;
                final double entZ = packet.func_148946_h() / 32;
                final double posX = AntiBot.mc.thePlayer.posX;
                final double posY = AntiBot.mc.thePlayer.posY;
                final double posZ = AntiBot.mc.thePlayer.posZ;
                final double var7 = posX - entX;
                final double var8 = posY - entY;
                final double var9 = posZ - entZ;
                final float distance = MathHelper.sqrt_double(var7 * var7 + var8 * var8 + var9 * var9);
                if (distance <= 17.0f && entY > AntiBot.mc.thePlayer.posY + 1.0 && AntiBot.mc.thePlayer.posX != entX && AntiBot.mc.thePlayer.posY != entY && AntiBot.mc.thePlayer.posZ != entZ) {
                    ep.setCancelled(true);
                }
            }
        }
        if (event instanceof EventMotion) {
            final EventMotion em = (EventMotion)event;
            this.setSuffix(currentSetting);
            if (em.isPre()) {
                if (AntiBot.mc.getIntegratedServer() == null) {
                    if (AntiBot.mc.getCurrentServerData().serverIP.toLowerCase().contains("hypixel") && !currentSetting.equals("Hypixel")) {
                        ((HashMap<K, Setting<Options>>)this.settings).get(AntiBot.MODE).getValue().setSelected("Hypixel");
                        ChatUtil.printChat("§4[§cE§4]§8 AntiBot has been set to the proper mode.");
                    }
                    else if (AntiBot.mc.getCurrentServerData().serverIP.toLowerCase().contains("mineplex") && !currentSetting.equals("Packet")) {
                        ((HashMap<K, Setting<Options>>)this.settings).get(AntiBot.MODE).getValue().setSelected("Packet");
                        ChatUtil.printChat("§4[§cE§4]§8 AntiBot has been set to the proper mode.");
                    }
                }
                if (((HashMap<K, Setting<Boolean>>)this.settings).get(this.DEAD).getValue()) {
                    for (final Object o : AntiBot.mc.theWorld.loadedEntityList) {
                        if (o instanceof EntityPlayer) {
                            final EntityPlayer ent = (EntityPlayer)o;
                            assert ent != AntiBot.mc.thePlayer;
                            if (!ent.isPlayerSleeping()) {
                                continue;
                            }
                            AntiBot.mc.theWorld.removeEntity(ent);
                        }
                    }
                }
            }
            if (em.isPre() && !currentSetting.equalsIgnoreCase("Packet")) {
                if (!AntiBot.invalid.isEmpty() && this.timer.delay(1000.0f)) {
                    AntiBot.invalid.clear();
                    this.timer.reset();
                }
                for (final Object o : AntiBot.mc.theWorld.getLoadedEntityList()) {
                    if (o instanceof EntityPlayer) {
                        final EntityPlayer ent = (EntityPlayer)o;
                        if (ent == AntiBot.mc.thePlayer || AntiBot.mc.thePlayer.getDistanceToEntity(ent) >= 10.0f || AntiBot.invalid.contains(ent)) {
                            continue;
                        }
                        final String s = currentSetting;
                        switch (s) {
                            case "Hypixel": {
                                final String str = ent.getDisplayName().getFormattedText();
                                if (str.equalsIgnoreCase(ent.getName() + "§r") || str.contains("NPC") || !getTabPlayerList().contains(ent)) {
                                    this.timer.reset();
                                    AntiBot.invalid.add(ent);
                                    continue;
                                }
                                continue;
                            }
                        }
                    }
                }
            }
        }
    }
    
    public static List<EntityPlayer> getTabPlayerList() {
        final NetHandlerPlayClient var4 = AntiBot.mc.thePlayer.sendQueue;
        final List<EntityPlayer> list = new ArrayList<EntityPlayer>();
        final List players = GuiPlayerTabOverlay.field_175252_a.sortedCopy((Iterable)var4.func_175106_d());
        for (final Object o : players) {
            final NetworkPlayerInfo info = (NetworkPlayerInfo)o;
            if (info == null) {
                continue;
            }
            list.add(AntiBot.mc.theWorld.getPlayerEntityByName(info.getGameProfile().getName()));
        }
        return list;
    }
    
    static {
        AntiBot.MODE = "MODE";
        AntiBot.invalid = new ArrayList<EntityPlayer>();
    }
}
