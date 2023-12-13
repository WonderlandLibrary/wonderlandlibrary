// 
// Decompiled by Procyon v0.6.0
// 

package net.augustus.modules.combat;

import net.augustus.events.EventReadPacket;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.client.gui.GuiPlayerTabOverlay;
import net.minecraft.client.Minecraft;
import net.lenni0451.eventapi.reflection.EventTarget;
import java.util.Iterator;
import java.util.ArrayList;
import net.minecraft.entity.Entity;
import net.augustus.events.EventTick;
import net.augustus.modules.Categorys;
import java.awt.Color;
import net.augustus.settings.BooleanValue;
import net.augustus.settings.DoubleValue;
import net.augustus.settings.StringValue;
import net.minecraft.entity.player.EntityPlayer;
import java.util.List;
import net.augustus.modules.Module;

public class AntiBot extends Module
{
    public static List<EntityPlayer> bots;
    public StringValue mode;
    public DoubleValue ticksExisted;
    public BooleanValue checkTab;
    
    public AntiBot() {
        super("AntiBot", Color.BLUE, Categorys.COMBAT);
        this.mode = new StringValue(1, "Mode", this, "Mineplex", new String[] { "Mineplex", "Custom" });
        this.ticksExisted = new DoubleValue(1, "TicksExisted", this, 20.0, 1.0, 100.0, 0);
        this.checkTab = new BooleanValue(2, "CheckTab", this, false);
    }
    
    @Override
    public void onEnable() {
        super.onEnable();
        AntiBot.bots.clear();
    }
    
    @EventTarget
    public void onEventTick(final EventTick eventTick) {
        this.setDisplayName(super.getName() + " §8" + this.mode.getSelected());
        final List<EntityPlayer> tab = getTabPlayerList();
        final String selected;
        final String var2 = selected = this.mode.getSelected();
        switch (selected) {
            case "Mineplex": {
                if (AntiBot.mc.thePlayer.ticksExisted > 110) {
                    for (final Entity entity : AntiBot.mc.theWorld.loadedEntityList) {
                        if (entity instanceof EntityPlayer && entity != AntiBot.mc.thePlayer && entity.getCustomNameTag() == "" && !AntiBot.bots.contains(entity)) {
                            AntiBot.bots.add((EntityPlayer)entity);
                        }
                    }
                    break;
                }
                AntiBot.bots = new ArrayList<EntityPlayer>();
                break;
            }
            case "Custom": {
                AntiBot.bots.clear();
                for (final Entity entity : AntiBot.mc.theWorld.loadedEntityList) {
                    if (entity instanceof EntityPlayer) {
                        if (this.checkTab.getBoolean() && !tab.contains(entity)) {
                            AntiBot.bots.add((EntityPlayer)entity);
                        }
                        if (entity.ticksExisted >= this.ticksExisted.getValue() || entity == AntiBot.mc.thePlayer || AntiBot.bots.contains(entity)) {
                            continue;
                        }
                        AntiBot.bots.add((EntityPlayer)entity);
                    }
                }
                break;
            }
        }
    }
    
    public static List<EntityPlayer> getTabPlayerList() {
        final NetHandlerPlayClient var4 = Minecraft.getMinecraft().thePlayer.sendQueue;
        final List<EntityPlayer> list = new ArrayList<EntityPlayer>();
        final List<NetworkPlayerInfo> players = GuiPlayerTabOverlay.field_175252_a.sortedCopy(var4.getPlayerInfoMap());
        for (final NetworkPlayerInfo info : players) {
            final NetworkPlayerInfo o = info;
            if (info == null) {
                continue;
            }
            list.add(Minecraft.getMinecraft().theWorld.getPlayerEntityByName(info.getGameProfile().getName()));
        }
        return list;
    }
    
    @EventTarget
    public void onEventReadPacket(final EventReadPacket eventReadPacket) {
    }
    
    static {
        AntiBot.bots = new ArrayList<EntityPlayer>();
    }
}
