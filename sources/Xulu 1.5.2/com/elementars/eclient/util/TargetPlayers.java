// 
// Decompiled by Procyon v0.5.36
// 

package com.elementars.eclient.util;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import java.util.concurrent.ConcurrentHashMap;

public class TargetPlayers implements Helper
{
    public static ConcurrentHashMap<String, Integer> targettedplayers;
    
    public static void onAttack(final AttackEntityEvent event) {
        if (event.getTarget() instanceof EntityPlayer) {
            if (((EntityPlayer)event.getTarget()).getHealth() == 0.0f) {
                return;
            }
            TargetPlayers.targettedplayers.put(event.getTarget().getName(), 20);
        }
    }
    
    public static void onUpdate() {
        TargetPlayers.targettedplayers.forEach((name, timeout) -> {
            if (timeout <= 0) {
                TargetPlayers.targettedplayers.remove(name);
            }
            else {
                TargetPlayers.targettedplayers.put(name, timeout - 1);
            }
        });
    }
    
    public static void addTargetedPlayer(final String name) {
        if (name.equalsIgnoreCase(TargetPlayers.mc.player.getName())) {
            return;
        }
        if (TargetPlayers.targettedplayers == null) {
            TargetPlayers.targettedplayers = new ConcurrentHashMap<String, Integer>();
        }
        TargetPlayers.targettedplayers.put(name, 20);
    }
    
    static {
        TargetPlayers.targettedplayers = new ConcurrentHashMap<String, Integer>();
    }
}
