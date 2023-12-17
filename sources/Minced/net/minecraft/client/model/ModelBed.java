// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.client.model;

public class ModelBed extends ModelBase
{
    public ModelRenderer headPiece;
    public ModelRenderer footPiece;
    public ModelRenderer[] legs;
    
    public ModelBed() {
        this.legs = new ModelRenderer[4];
        this.textureWidth = 64;
        this.textureHeight = 64;
        (this.headPiece = new ModelRenderer(this, 0, 0)).addBox(0.0f, 0.0f, 0.0f, 16, 16, 6, 0.0f);
        (this.footPiece = new ModelRenderer(this, 0, 22)).addBox(0.0f, 0.0f, 0.0f, 16, 16, 6, 0.0f);
        this.legs[0] = new ModelRenderer(this, 50, 0);
        this.legs[1] = new ModelRenderer(this, 50, 6);
        this.legs[2] = new ModelRenderer(this, 50, 12);
        this.legs[3] = new ModelRenderer(this, 50, 18);
        this.legs[0].addBox(0.0f, 6.0f, -16.0f, 3, 3, 3);
        this.legs[1].addBox(0.0f, 6.0f, 0.0f, 3, 3, 3);
        this.legs[2].addBox(-16.0f, 6.0f, -16.0f, 3, 3, 3);
        this.legs[3].addBox(-16.0f, 6.0f, 0.0f, 3, 3, 3);
        this.legs[0].rotateAngleX = 1.5707964f;
        this.legs[1].rotateAngleX = 1.5707964f;
        this.legs[2].rotateAngleX = 1.5707964f;
        this.legs[3].rotateAngleX = 1.5707964f;
        this.legs[0].rotateAngleZ = 0.0f;
        this.legs[1].rotateAngleZ = 1.5707964f;
        this.legs[2].rotateAngleZ = 4.712389f;
        this.legs[3].rotateAngleZ = 3.1415927f;
    }
    
    public int getModelVersion() {
        return 51;
    }
    
    public void render() {
        this.headPiece.render(0.0625f);
        this.footPiece.render(0.0625f);
        this.legs[0].render(0.0625f);
        this.legs[1].render(0.0625f);
        this.legs[2].render(0.0625f);
        this.legs[3].render(0.0625f);
    }
    
    public void preparePiece(final boolean p_193769_1_) {
        this.headPiece.showModel = p_193769_1_;
        this.footPiece.showModel = !p_193769_1_;
        this.legs[0].showModel = !p_193769_1_;
        this.legs[1].showModel = p_193769_1_;
        this.legs[2].showModel = !p_193769_1_;
        this.legs[3].showModel = p_193769_1_;
    }
}
