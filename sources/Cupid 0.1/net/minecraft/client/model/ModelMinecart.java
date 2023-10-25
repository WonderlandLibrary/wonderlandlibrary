package net.minecraft.client.model;

import net.minecraft.entity.Entity;

public class ModelMinecart extends ModelBase {
  public ModelRenderer[] sideModels = new ModelRenderer[7];
  
  public ModelMinecart() {
    this.sideModels[0] = new ModelRenderer(this, 0, 10);
    this.sideModels[1] = new ModelRenderer(this, 0, 0);
    this.sideModels[2] = new ModelRenderer(this, 0, 0);
    this.sideModels[3] = new ModelRenderer(this, 0, 0);
    this.sideModels[4] = new ModelRenderer(this, 0, 0);
    this.sideModels[5] = new ModelRenderer(this, 44, 10);
    int i = 20;
    int j = 8;
    int k = 16;
    int l = 4;
    this.sideModels[0].addBox((-i / 2), (-k / 2), -1.0F, i, k, 2, 0.0F);
    this.sideModels[0].setRotationPoint(0.0F, l, 0.0F);
    this.sideModels[5].addBox((-i / 2 + 1), (-k / 2 + 1), -1.0F, i - 2, k - 2, 1, 0.0F);
    this.sideModels[5].setRotationPoint(0.0F, l, 0.0F);
    this.sideModels[1].addBox((-i / 2 + 2), (-j - 1), -1.0F, i - 4, j, 2, 0.0F);
    this.sideModels[1].setRotationPoint((-i / 2 + 1), l, 0.0F);
    this.sideModels[2].addBox((-i / 2 + 2), (-j - 1), -1.0F, i - 4, j, 2, 0.0F);
    this.sideModels[2].setRotationPoint((i / 2 - 1), l, 0.0F);
    this.sideModels[3].addBox((-i / 2 + 2), (-j - 1), -1.0F, i - 4, j, 2, 0.0F);
    this.sideModels[3].setRotationPoint(0.0F, l, (-k / 2 + 1));
    this.sideModels[4].addBox((-i / 2 + 2), (-j - 1), -1.0F, i - 4, j, 2, 0.0F);
    this.sideModels[4].setRotationPoint(0.0F, l, (k / 2 - 1));
    (this.sideModels[0]).rotateAngleX = 1.5707964F;
    (this.sideModels[1]).rotateAngleY = 4.712389F;
    (this.sideModels[2]).rotateAngleY = 1.5707964F;
    (this.sideModels[3]).rotateAngleY = 3.1415927F;
    (this.sideModels[5]).rotateAngleX = -1.5707964F;
  }
  
  public void render(Entity entityIn, float p_78088_2_, float p_78088_3_, float p_78088_4_, float p_78088_5_, float p_78088_6_, float scale) {
    (this.sideModels[5]).rotationPointY = 4.0F - p_78088_4_;
    for (int i = 0; i < 6; i++)
      this.sideModels[i].render(scale); 
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\net\minecraft\client\model\ModelMinecart.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */