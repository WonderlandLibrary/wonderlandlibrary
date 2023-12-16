// 
// Decompiled by Procyon v0.5.36
// 

package moonsense.cosmetics.obj.handler;

import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

public abstract class OBJCosmeticHandler
{
    private final String name;
    private final CosmeticsType type;
    
    public OBJCosmeticHandler(final String name, final CosmeticsType type) {
        this.name = name;
        this.type = type;
    }
    
    public abstract void render(final Entity p0, final ModelRenderer p1);
    
    public String getName() {
        return this.name;
    }
    
    public CosmeticsType getType() {
        return this.type;
    }
    
    public enum CosmeticsType
    {
        HAT("HAT", 0), 
        BODY("BODY", 1);
        
        private CosmeticsType(final String name, final int ordinal) {
        }
    }
}
