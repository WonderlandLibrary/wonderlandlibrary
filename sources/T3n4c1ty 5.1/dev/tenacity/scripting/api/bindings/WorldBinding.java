package dev.tenacity.scripting.api.bindings;

import dev.tenacity.utils.Utils;
import net.minecraft.entity.EntityLivingBase;
import store.intent.intentguard.annotation.Exclude;
import store.intent.intentguard.annotation.Strategy;

import java.util.List;
import java.util.stream.Collectors;

@Exclude(Strategy.NAME_REMAPPING)
public class WorldBinding implements Utils {

    public void setTimer(float speed) {
        mc.timer.timerSpeed = speed;
    }

    public boolean isSinglePlayer() {
        return mc.isSingleplayer();
    }

    public float timer() {
        return mc.timer.timerSpeed;
    }

    public List<EntityLivingBase> getLivingEntities() {
        return mc.theWorld.getLoadedEntityList().stream().filter(entity -> entity instanceof EntityLivingBase).map(entity -> (EntityLivingBase) entity).collect(Collectors.toList());
    }

}
