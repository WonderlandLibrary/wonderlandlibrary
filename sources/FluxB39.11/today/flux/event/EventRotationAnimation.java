package today.flux.event;

import com.darkmagician6.eventapi.events.Event;
import net.minecraft.entity.EntityLivingBase;

public class EventRotationAnimation implements Event {
    private EntityLivingBase entity;
    private float rotationYawHead, renderYawOffset, renderHeadYaw, renderHeadPitch, partialTicks;

    public EventRotationAnimation(EntityLivingBase entity, float renderYawOffset, float rotationYawHead
            , float renderHeadYaw, float renderHeadPitch, float partialTicks) {
        this.entity = entity;
        this.rotationYawHead = rotationYawHead;
        this.renderYawOffset = renderYawOffset;
        this.renderHeadYaw = renderHeadYaw;
        this.renderHeadPitch = renderHeadPitch;
        this.partialTicks = partialTicks;
    }

    public EntityLivingBase getEntity() {
        return entity;
    }

    public void setEntity(EntityLivingBase entity) {
        this.entity = entity;
    }

    public float getRenderYawOffset() {
        return renderYawOffset;
    }

    public void setRenderYawOffset(float renderYawOffset) {
        this.renderYawOffset = renderYawOffset;
    }

    public float getRotationYawHead() {
        return rotationYawHead;
    }

    public void setRotationYawHead(float rotationYawHead) {
        this.rotationYawHead = rotationYawHead;
    }

    public float getRenderHeadYaw() {
        return renderHeadYaw;
    }

    public void setRenderHeadYaw(float renderHeadYaw) {
        this.renderHeadYaw = renderHeadYaw;
    }

    public float getRenderHeadPitch() {
        return renderHeadPitch;
    }

    public void setRenderHeadPitch(float renderHeadPitch) {
        this.renderHeadPitch = renderHeadPitch;
    }

    public float getPartialTicks() {
        return partialTicks;
    }
}