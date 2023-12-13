// 
// Decompiled by Procyon v0.5.36
// 

package com.elementars.eclient.module.combat;

import java.util.HashMap;
import java.util.HashSet;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import java.util.Iterator;
import net.minecraft.init.MobEffects;
import net.minecraft.potion.PotionEffect;
import net.minecraftforge.event.entity.living.PotionColorCalculationEvent;
import com.elementars.eclient.module.Category;
import java.util.Map;
import net.minecraft.entity.player.EntityPlayer;
import java.util.Set;
import com.elementars.eclient.module.Module;

public class StrengthDetectX extends Module
{
    public static final Set<EntityPlayer> strengthPlayers;
    public static final Map<EntityPlayer, Integer> strMap;
    
    public StrengthDetectX() {
        super("StrengthDetectX", "Hope this works", 0, Category.COMBAT, true);
    }
    
    @Override
    public void onEnable() {
        StrengthDetectX.EVENT_BUS.register((Object)this);
    }
    
    @Override
    public void onDisable() {
        StrengthDetectX.EVENT_BUS.unregister((Object)this);
    }
    
    @SubscribeEvent
    public void onPotionColor(final PotionColorCalculationEvent event) {
        if (event.getEntityLiving() instanceof EntityPlayer) {
            this.sendDebugMessage("Yo this event is being fired");
            boolean hasStrength = false;
            for (final PotionEffect potionEffect : event.getEffects()) {
                if (potionEffect.getPotion() == MobEffects.STRENGTH) {
                    StrengthDetectX.strMap.put((EntityPlayer)event.getEntityLiving(), potionEffect.getAmplifier());
                    this.sendRawDebugMessage(event.getEntityLiving().getName() + " has strength");
                    hasStrength = true;
                    break;
                }
            }
            if (StrengthDetectX.strMap.containsKey(event.getEntityLiving()) && !hasStrength) {
                StrengthDetectX.strMap.remove(event.getEntityLiving());
                this.sendRawDebugMessage(event.getEntityLiving().getName() + " no longer has strength");
            }
        }
    }
    
    static {
        strengthPlayers = new HashSet<EntityPlayer>();
        strMap = new HashMap<EntityPlayer, Integer>();
    }
}
