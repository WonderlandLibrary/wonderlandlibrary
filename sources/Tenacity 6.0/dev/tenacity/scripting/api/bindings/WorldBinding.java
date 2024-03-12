// 
// Decompiled by Procyon v0.5.36
// 

package dev.tenacity.scripting.api.bindings;

import net.minecraft.entity.Entity;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import net.minecraft.entity.EntityLivingBase;
import java.util.List;
import store.intent.intentguard.annotation.Strategy;
import store.intent.intentguard.annotation.Exclude;
import dev.tenacity.utils.Utils;

@Exclude({ Strategy.NAME_REMAPPING })
public class WorldBinding implements Utils
{
    public void setTimer(final float speed) {
        WorldBinding.mc.timer.timerSpeed = speed;
    }
    
    public boolean isSinglePlayer() {
        return WorldBinding.mc.isSingleplayer();
    }
    
    public float timer() {
        return WorldBinding.mc.timer.timerSpeed;
    }
    
    public List<EntityLivingBase> getLivingEntities() {
        return WorldBinding.mc.theWorld.getLoadedEntityList().stream().filter(entity -> entity instanceof EntityLivingBase).map(entity -> entity).collect((Collector<? super Object, ?, List<EntityLivingBase>>)Collectors.toList());
    }
}
