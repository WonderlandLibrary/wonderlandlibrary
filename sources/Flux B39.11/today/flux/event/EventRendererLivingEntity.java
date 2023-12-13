package today.flux.event;

import com.darkmagician6.eventapi.events.callables.EventCancellable;
import com.darkmagician6.eventapi.types.EventType;
import net.minecraft.entity.EntityLivingBase;

public class EventRendererLivingEntity extends EventCancellable {
    private EntityLivingBase entity;
    private EventType type;
    private float limbSwing;
    private float limbSwingAmount;
    private float ageInTicks;
    private float rotationYawHead;
    private float rotationPitch;
    private float chestRot;
    private float offset;

    public EventRendererLivingEntity(EntityLivingBase entity, EventType type, float limbSwing, float limbSwingAmount, float ageInTicks, float rotationYawHead, float rotationPitch, float chestRot, float offset) {
        this.entity = entity;
        this.type = type;
        this.limbSwing = limbSwing;
        this.limbSwingAmount = limbSwingAmount;
        this.ageInTicks = ageInTicks;
        this.rotationYawHead = rotationYawHead;
        this.rotationPitch = rotationPitch;
        this.chestRot = chestRot;
        this.offset = offset;
    }

    public EventRendererLivingEntity(EntityLivingBase entity, EventType type) {
        this.entity = entity;
        this.type = type;
        this.limbSwing = 0;
        this.limbSwingAmount = 0;
        this.ageInTicks = 0;
        this.rotationYawHead = 0;
        this.rotationPitch = 0;
        this.chestRot = 0;
        this.offset = 0;
    }

    public EntityLivingBase getEntity() {
        return this.entity;
    }

    public boolean isPre() {
        return this.type == EventType.PRE;
    }

    public boolean isPost() {
        return this.type == EventType.POST;
    }

    public float getLimbSwing() {
        return this.limbSwing;
    }

    public void setLimbSwing(float limbSwing) {
        this.limbSwing = limbSwing;
    }

    public float getLimbSwingAmount() {
        return this.limbSwingAmount;
    }

    public void setLimbSwingAmount(float limbSwingAmount) {
        this.limbSwingAmount = limbSwingAmount;
    }

    public float getAgeInTicks() {
        return this.ageInTicks;
    }

    public void setAgeInTicks(float ageInTicks) {
        this.ageInTicks = ageInTicks;
    }

    public float getRotationYawHead() {
        return this.rotationYawHead;
    }

    public void setRotationYawHead(float rotationYawHead) {
        this.rotationYawHead = rotationYawHead;
    }

    public float getRotationPitch() {
        return this.rotationPitch;
    }

    public void setRotationPitch(float rotationPitch) {
        this.rotationPitch = rotationPitch;
    }

    public float getOffset() {
        return this.offset;
    }

    public void setOffset(float offset) {
        this.offset = offset;
    }

    public float getRotationChest() {
        return this.chestRot;
    }

    public void setRotationChest(float rotationChest) {
        this.chestRot = rotationChest;
    }

}
