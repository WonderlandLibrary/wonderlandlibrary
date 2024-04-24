package me.crimp.claudius.mod.modules.render;

import me.crimp.claudius.event.events.DeathEvent;
import me.crimp.claudius.mod.modules.Module;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class DeathEffect extends Module {
    public DeathEffect() {
        super("DeathEffect", "Summons Lighting When Other People Die", Category.Render, false, false);
    }
    public static DeathEffect INSTANCE = new DeathEffect();


    @SubscribeEvent
    public void onDeath(DeathEvent event) {
        if (!event.getPlayer().equals(mc.player) && this.enabled.getValue()) {
            mc.world.spawnEntity(new EntityLightningBolt(mc.world,  event.getPlayer().posX,  event.getPlayer().posY,  event.getPlayer().posZ, true));
        }
    }
}
