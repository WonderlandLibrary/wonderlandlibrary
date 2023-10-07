package best.azura.client.impl.events;

import best.azura.scripting.event.NamedEvent;
import net.minecraft.client.model.ModelRenderer;

import java.util.List;

public class EventRenderPlayer implements NamedEvent {
    private final String target, entityName;
    private String mode;
    public List<ModelRenderer> boxList;
    public float bodyRotationX, bodyRotationY, bodyRotationZ, bodyRotationYaw, bodyRotationPitch;

    public EventRenderPlayer(String target, String entityName, String mode, List<ModelRenderer> boxList) {
        this.target = target;
        this.entityName = entityName;
        this.mode = mode;
        this.boxList = boxList;
    }

    public EventRenderPlayer(String target, String entityName, String mode, List<ModelRenderer> boxList, float bodyRotationX, float bodyRotationY,
                             float bodyRotationZ, float bodyRotationYaw, float bodyRotationPitch) {
        this(target, entityName, mode, boxList);
        this.bodyRotationX = bodyRotationX;
        this.bodyRotationY = bodyRotationY;
        this.bodyRotationZ = bodyRotationZ;
        this.bodyRotationYaw = bodyRotationYaw;
        this.bodyRotationPitch = bodyRotationPitch;
    }

    public String getEntityName() {
        return entityName;
    }

    public String getTarget() {
        return target;
    }

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    @Override
    public String name() {
        return "renderPlayer";
    }
}