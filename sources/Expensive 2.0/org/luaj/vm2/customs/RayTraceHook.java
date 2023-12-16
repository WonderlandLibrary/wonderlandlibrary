package org.luaj.vm2.customs;

import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.util.math.RayTraceResult;

public class RayTraceHook {

    public RayTraceResult result;

    public RayTraceHook(RayTraceResult result) {
        this.result = result;
    }

    public boolean isType(String type) {
        switch (type.toLowerCase()) {
            case "entity":
                return result.getType() == RayTraceResult.Type.ENTITY;
            case "block":
                return result.getType() == RayTraceResult.Type.BLOCK;
            case "miss":
                return result.getType() == RayTraceResult.Type.MISS;
            default:
                return false;
        }
    }

    public EntityHook getEntity() {
        if (result instanceof EntityRayTraceResult e) {
            return new EntityHook(e.getEntity());
        }
        return null;
    }

}

