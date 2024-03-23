package dev.zprestige.prestige.client.module.impl.misc;

import dev.zprestige.prestige.api.mixin.IPlayerInteractEntityC2SPacket;
import dev.zprestige.prestige.client.event.EventListener;
import dev.zprestige.prestige.client.event.impl.PacketSendEvent;
import dev.zprestige.prestige.client.module.Category;
import dev.zprestige.prestige.client.module.Module;
import net.minecraft.entity.Entity;
import net.minecraft.entity.decoration.EndCrystalEntity;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.network.packet.c2s.play.PlayerInteractEntityC2SPacket;

public class CrystalOptimizer extends Module {

    public CrystalOptimizer() {
        super("Crystal Optimizer", Category.Misc, "Does not wait for server-side confirmation when breaking crystals");
    }

    @EventListener
    public void event(PacketSendEvent event) {
        if (event.getPacket() instanceof PlayerInteractEntityC2SPacket packet && ((IPlayerInteractEntityC2SPacket)packet).getType().getType() == PlayerInteractEntityC2SPacket.InteractType.ATTACK) {
            Entity entity = getMc().world.getEntityById(((IPlayerInteractEntityC2SPacket)packet).getEntityId());
            if (entity instanceof EndCrystalEntity && getMc().player.getStatusEffect(StatusEffects.WEAKNESS) == null) {
                entity.kill();
                entity.setRemoved(Entity.RemovalReason.KILLED);
                entity.onRemoved();
            }
        }
    }
}
