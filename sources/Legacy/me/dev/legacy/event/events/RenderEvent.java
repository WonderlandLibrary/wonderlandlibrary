package me.dev.legacy.event.events;

import me.dev.legacy.event.EventStage;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.math.Vec3d;

public class RenderEvent extends EventStage {
    private Vec3d renderPos;
    private Tessellator tessellator;
    private final float partialTicks;

    public void resetTranslation() {
        setTranslation(this.renderPos);
    }

    public Vec3d getRenderPos() {
        return this.renderPos;
    }

    public BufferBuilder getBuffer() {
        return this.tessellator.getBuffer();
    }

    public Tessellator getTessellator() {
        return this.tessellator;
    }

    public RenderEvent(Tessellator paramTessellator, Vec3d paramVec3d, float ticks) {
        this.tessellator = paramTessellator;
        this.renderPos = paramVec3d;
        partialTicks = ticks;
    }

    public void setTranslation(Vec3d paramVec3d) {
        getBuffer().setTranslation(-paramVec3d.x, -paramVec3d.y, -paramVec3d.z);
    }

    public float getPartialTicks(){
        return partialTicks;
    }

}
