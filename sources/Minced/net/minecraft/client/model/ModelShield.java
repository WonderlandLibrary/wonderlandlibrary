// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.client.model;

public class ModelShield extends ModelBase
{
    public ModelRenderer plate;
    public ModelRenderer handle;
    
    public ModelShield() {
        this.textureWidth = 64;
        this.textureHeight = 64;
        (this.plate = new ModelRenderer(this, 0, 0)).addBox(-6.0f, -11.0f, -2.0f, 12, 22, 1, 0.0f);
        (this.handle = new ModelRenderer(this, 26, 0)).addBox(-1.0f, -3.0f, -1.0f, 2, 6, 6, 0.0f);
    }
    
    public void render() {
        this.plate.render(0.0625f);
        this.handle.render(0.0625f);
    }
}
