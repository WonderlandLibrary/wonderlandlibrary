package xyz.northclient.features.events;

import lombok.AllArgsConstructor;
import lombok.Getter;
import net.minecraft.client.renderer.entity.RendererLivingEntity;
import net.minecraft.entity.EntityLivingBase;
import xyz.northclient.features.Event;

@Getter
@AllArgsConstructor
public class EntityRenderEvent extends Event {
    private final EntityLivingBase entity;
    private final RendererLivingEntity<?> renderer;
    private final float partialTicks;
    private final double x, y, z;
}
