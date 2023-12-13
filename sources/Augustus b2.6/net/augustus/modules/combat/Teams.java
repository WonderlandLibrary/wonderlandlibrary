// 
// Decompiled by Procyon v0.5.36
// 

package net.augustus.modules.combat;

import net.minecraft.scoreboard.ScorePlayerTeam;
import net.minecraft.entity.EntityLivingBase;
import net.lenni0451.eventapi.reflection.EventTarget;
import java.util.Iterator;
import net.minecraft.entity.Entity;
import net.augustus.events.EventTick;
import net.augustus.modules.Categorys;
import java.awt.Color;
import net.minecraft.entity.player.EntityPlayer;
import java.util.ArrayList;
import net.augustus.settings.StringValue;
import net.augustus.modules.Module;

public class Teams extends Module
{
    private final StringValue modes;
    private ArrayList<EntityPlayer> teammates;
    
    public Teams() {
        super("Teams", Color.red, Categorys.COMBAT);
        this.modes = new StringValue(1, "Modes", this, "Color", new String[] { "Name", "Color" });
        this.teammates = new ArrayList<EntityPlayer>();
    }
    
    @EventTarget
    public void onEventTick(final EventTick eventTick) {
        this.teammates = new ArrayList<EntityPlayer>();
        for (final Entity entity : Teams.mc.theWorld.loadedEntityList) {
            if (this.isTeammate(entity)) {
                this.teammates.add((EntityPlayer)entity);
            }
        }
    }
    
    private boolean isTeammate(final Entity entity) {
        if (entity instanceof EntityLivingBase && entity instanceof EntityPlayer) {
            final ScorePlayerTeam entityTeam = Teams.mc.theWorld.getScoreboard().getPlayersTeam(entity.getName());
            final ScorePlayerTeam myTeam = Teams.mc.theWorld.getScoreboard().getPlayersTeam(Teams.mc.thePlayer.getName());
            return (entityTeam != null && myTeam != null && entityTeam.getColorPrefix().equals(myTeam.getColorPrefix()) && this.modes.getSelected().equals("Color")) || (entityTeam != null && myTeam != null && entityTeam.getTeamName().equals(myTeam.getTeamName()) && this.modes.getSelected().equals("Name"));
        }
        return false;
    }
    
    public ArrayList<EntityPlayer> getTeammates() {
        return this.teammates;
    }
    
    public void setTeammates(final ArrayList<EntityPlayer> teammates) {
        this.teammates = teammates;
    }
}
