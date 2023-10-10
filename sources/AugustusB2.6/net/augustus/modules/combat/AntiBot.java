// 
// Decompiled by Procyon v0.5.36
// 

package net.augustus.modules.combat;

import net.augustus.events.EventReadPacket;
import net.lenni0451.eventapi.reflection.EventTarget;
import java.util.Iterator;
import java.util.ArrayList;
import net.minecraft.entity.Entity;
import net.augustus.events.EventTick;
import net.augustus.modules.Categorys;
import java.awt.Color;
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
    
    public AntiBot() {
        super("AntiBot", Color.BLUE, Categorys.COMBAT);
        this.mode = new StringValue(1, "Mode", this, "Mineplex", new String[] { "Mineplex", "Custom" });
        this.ticksExisted = new DoubleValue(1, "TicksExisted", this, 20.0, 1.0, 100.0, 0);
    }
    
    @Override
    public void onEnable() {
        super.onEnable();
        AntiBot.bots.clear();
    }
    
    @EventTarget
    public void onEventTick(final EventTick eventTick) {
        this.setDisplayName(super.getName() + " §8" + this.mode.getSelected());
        final String selected = this.mode.getSelected();
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
                    if (entity instanceof EntityPlayer && entity.ticksExisted < this.ticksExisted.getValue() && entity != AntiBot.mc.thePlayer) {
                        AntiBot.bots.add((EntityPlayer)entity);
                    }
                }
                break;
            }
        }
    }
    
    @EventTarget
    public void onEventReadPacket(final EventReadPacket eventReadPacket) {
    }
    
    static {
        AntiBot.bots = new ArrayList<EntityPlayer>();
    }
}
