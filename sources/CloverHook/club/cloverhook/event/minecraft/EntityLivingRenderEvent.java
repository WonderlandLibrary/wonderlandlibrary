package club.cloverhook.event.minecraft;

import me.hippo.systems.lwjeb.event.Cancelable;
import net.minecraft.entity.EntityLivingBase;

public class EntityLivingRenderEvent extends Cancelable {

    private boolean isPre;
    private boolean isPost;
    private EntityLivingBase entityLivingBase;

    public EntityLivingRenderEvent(boolean pre, EntityLivingBase entityLivingBase) {
        this.entityLivingBase = entityLivingBase;
        isPre = pre;
        isPost = !pre;
    }

    public boolean isPre() {
        return isPre;
    }
    public boolean isPost() {
        return isPost;
    }
    public EntityLivingBase getEntity() {
        return entityLivingBase;
    }
}
