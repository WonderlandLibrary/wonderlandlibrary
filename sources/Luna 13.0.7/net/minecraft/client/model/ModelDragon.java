package net.minecraft.client.model;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.boss.EntityDragon;

public class ModelDragon
  extends ModelBase
{
  private ModelRenderer head;
  private ModelRenderer spine;
  private ModelRenderer jaw;
  private ModelRenderer body;
  private ModelRenderer rearLeg;
  private ModelRenderer frontLeg;
  private ModelRenderer rearLegTip;
  private ModelRenderer frontLegTip;
  private ModelRenderer rearFoot;
  private ModelRenderer frontFoot;
  private ModelRenderer wing;
  private ModelRenderer wingTip;
  private float partialTicks;
  private static final String __OBFID = "CL_00000870";
  
  public ModelDragon(float p_i46360_1_)
  {
    this.textureWidth = 256;
    this.textureHeight = 256;
    setTextureOffset("body.body", 0, 0);
    setTextureOffset("wing.skin", -56, 88);
    setTextureOffset("wingtip.skin", -56, 144);
    setTextureOffset("rearleg.main", 0, 0);
    setTextureOffset("rearfoot.main", 112, 0);
    setTextureOffset("rearlegtip.main", 196, 0);
    setTextureOffset("head.upperhead", 112, 30);
    setTextureOffset("wing.bone", 112, 88);
    setTextureOffset("head.upperlip", 176, 44);
    setTextureOffset("jaw.jaw", 176, 65);
    setTextureOffset("frontleg.main", 112, 104);
    setTextureOffset("wingtip.bone", 112, 136);
    setTextureOffset("frontfoot.main", 144, 104);
    setTextureOffset("neck.box", 192, 104);
    setTextureOffset("frontlegtip.main", 226, 138);
    setTextureOffset("body.scale", 220, 53);
    setTextureOffset("head.scale", 0, 0);
    setTextureOffset("neck.scale", 48, 0);
    setTextureOffset("head.nostril", 112, 0);
    float var2 = -16.0F;
    this.head = new ModelRenderer(this, "head");
    this.head.addBox("upperlip", -6.0F, -1.0F, -8.0F + var2, 12, 5, 16);
    this.head.addBox("upperhead", -8.0F, -8.0F, 6.0F + var2, 16, 16, 16);
    this.head.mirror = true;
    this.head.addBox("scale", -5.0F, -12.0F, 12.0F + var2, 2, 4, 6);
    this.head.addBox("nostril", -5.0F, -3.0F, -6.0F + var2, 2, 2, 4);
    this.head.mirror = false;
    this.head.addBox("scale", 3.0F, -12.0F, 12.0F + var2, 2, 4, 6);
    this.head.addBox("nostril", 3.0F, -3.0F, -6.0F + var2, 2, 2, 4);
    this.jaw = new ModelRenderer(this, "jaw");
    this.jaw.setRotationPoint(0.0F, 4.0F, 8.0F + var2);
    this.jaw.addBox("jaw", -6.0F, 0.0F, -16.0F, 12, 4, 16);
    this.head.addChild(this.jaw);
    this.spine = new ModelRenderer(this, "neck");
    this.spine.addBox("box", -5.0F, -5.0F, -5.0F, 10, 10, 10);
    this.spine.addBox("scale", -1.0F, -9.0F, -3.0F, 2, 4, 6);
    this.body = new ModelRenderer(this, "body");
    this.body.setRotationPoint(0.0F, 4.0F, 8.0F);
    this.body.addBox("body", -12.0F, 0.0F, -16.0F, 24, 24, 64);
    this.body.addBox("scale", -1.0F, -6.0F, -10.0F, 2, 6, 12);
    this.body.addBox("scale", -1.0F, -6.0F, 10.0F, 2, 6, 12);
    this.body.addBox("scale", -1.0F, -6.0F, 30.0F, 2, 6, 12);
    this.wing = new ModelRenderer(this, "wing");
    this.wing.setRotationPoint(-12.0F, 5.0F, 2.0F);
    this.wing.addBox("bone", -56.0F, -4.0F, -4.0F, 56, 8, 8);
    this.wing.addBox("skin", -56.0F, 0.0F, 2.0F, 56, 0, 56);
    this.wingTip = new ModelRenderer(this, "wingtip");
    this.wingTip.setRotationPoint(-56.0F, 0.0F, 0.0F);
    this.wingTip.addBox("bone", -56.0F, -2.0F, -2.0F, 56, 4, 4);
    this.wingTip.addBox("skin", -56.0F, 0.0F, 2.0F, 56, 0, 56);
    this.wing.addChild(this.wingTip);
    this.frontLeg = new ModelRenderer(this, "frontleg");
    this.frontLeg.setRotationPoint(-12.0F, 20.0F, 2.0F);
    this.frontLeg.addBox("main", -4.0F, -4.0F, -4.0F, 8, 24, 8);
    this.frontLegTip = new ModelRenderer(this, "frontlegtip");
    this.frontLegTip.setRotationPoint(0.0F, 20.0F, -1.0F);
    this.frontLegTip.addBox("main", -3.0F, -1.0F, -3.0F, 6, 24, 6);
    this.frontLeg.addChild(this.frontLegTip);
    this.frontFoot = new ModelRenderer(this, "frontfoot");
    this.frontFoot.setRotationPoint(0.0F, 23.0F, 0.0F);
    this.frontFoot.addBox("main", -4.0F, 0.0F, -12.0F, 8, 4, 16);
    this.frontLegTip.addChild(this.frontFoot);
    this.rearLeg = new ModelRenderer(this, "rearleg");
    this.rearLeg.setRotationPoint(-16.0F, 16.0F, 42.0F);
    this.rearLeg.addBox("main", -8.0F, -4.0F, -8.0F, 16, 32, 16);
    this.rearLegTip = new ModelRenderer(this, "rearlegtip");
    this.rearLegTip.setRotationPoint(0.0F, 32.0F, -4.0F);
    this.rearLegTip.addBox("main", -6.0F, -2.0F, 0.0F, 12, 32, 12);
    this.rearLeg.addChild(this.rearLegTip);
    this.rearFoot = new ModelRenderer(this, "rearfoot");
    this.rearFoot.setRotationPoint(0.0F, 31.0F, 4.0F);
    this.rearFoot.addBox("main", -9.0F, 0.0F, -20.0F, 18, 6, 24);
    this.rearLegTip.addChild(this.rearFoot);
  }
  
  public void setLivingAnimations(EntityLivingBase p_78086_1_, float p_78086_2_, float p_78086_3_, float p_78086_4_)
  {
    this.partialTicks = p_78086_4_;
  }
  
  public void render(Entity p_78088_1_, float p_78088_2_, float p_78088_3_, float p_78088_4_, float p_78088_5_, float p_78088_6_, float p_78088_7_)
  {
    GlStateManager.pushMatrix();
    EntityDragon var8 = (EntityDragon)p_78088_1_;
    float var9 = var8.prevAnimTime + (var8.animTime - var8.prevAnimTime) * this.partialTicks;
    this.jaw.rotateAngleX = ((float)(Math.sin(var9 * 3.1415927F * 2.0F) + 1.0D) * 0.2F);
    float var10 = (float)(Math.sin(var9 * 3.1415927F * 2.0F - 1.0F) + 1.0D);
    var10 = (var10 * var10 * 1.0F + var10 * 2.0F) * 0.05F;
    GlStateManager.translate(0.0F, var10 - 2.0F, -3.0F);
    GlStateManager.rotate(var10 * 2.0F, 1.0F, 0.0F, 0.0F);
    float var11 = -30.0F;
    float var13 = 0.0F;
    float var14 = 1.5F;
    double[] var15 = var8.getMovementOffsets(6, this.partialTicks);
    float var16 = updateRotations(var8.getMovementOffsets(5, this.partialTicks)[0] - var8.getMovementOffsets(10, this.partialTicks)[0]);
    float var17 = updateRotations(var8.getMovementOffsets(5, this.partialTicks)[0] + var16 / 2.0F);
    var11 += 2.0F;
    float var18 = var9 * 3.1415927F * 2.0F;
    var11 = 20.0F;
    float var12 = -12.0F;
    for (int var19 = 0; var19 < 5; var19++)
    {
      double[] var20 = var8.getMovementOffsets(5 - var19, this.partialTicks);
      float var21 = (float)Math.cos(var19 * 0.45F + var18) * 0.15F;
      this.spine.rotateAngleY = (updateRotations(var20[0] - var15[0]) * 3.1415927F / 180.0F * var14);
      this.spine.rotateAngleX = (var21 + (float)(var20[1] - var15[1]) * 3.1415927F / 180.0F * var14 * 5.0F);
      this.spine.rotateAngleZ = (-updateRotations(var20[0] - var17) * 3.1415927F / 180.0F * var14);
      this.spine.rotationPointY = var11;
      this.spine.rotationPointZ = var12;
      this.spine.rotationPointX = var13;
      var11 = (float)(var11 + Math.sin(this.spine.rotateAngleX) * 10.0D);
      var12 = (float)(var12 - Math.cos(this.spine.rotateAngleY) * Math.cos(this.spine.rotateAngleX) * 10.0D);
      var13 = (float)(var13 - Math.sin(this.spine.rotateAngleY) * Math.cos(this.spine.rotateAngleX) * 10.0D);
      this.spine.render(p_78088_7_);
    }
    this.head.rotationPointY = var11;
    this.head.rotationPointZ = var12;
    this.head.rotationPointX = var13;
    double[] var22 = var8.getMovementOffsets(0, this.partialTicks);
    this.head.rotateAngleY = (updateRotations(var22[0] - var15[0]) * 3.1415927F / 180.0F * 1.0F);
    this.head.rotateAngleZ = (-updateRotations(var22[0] - var17) * 3.1415927F / 180.0F * 1.0F);
    this.head.render(p_78088_7_);
    GlStateManager.pushMatrix();
    GlStateManager.translate(0.0F, 1.0F, 0.0F);
    GlStateManager.rotate(-var16 * var14 * 1.0F, 0.0F, 0.0F, 1.0F);
    GlStateManager.translate(0.0F, -1.0F, 0.0F);
    this.body.rotateAngleZ = 0.0F;
    this.body.render(p_78088_7_);
    for (int var23 = 0; var23 < 2; var23++)
    {
      GlStateManager.enableCull();
      float var21 = var9 * 3.1415927F * 2.0F;
      this.wing.rotateAngleX = (0.125F - (float)Math.cos(var21) * 0.2F);
      this.wing.rotateAngleY = 0.25F;
      this.wing.rotateAngleZ = ((float)(Math.sin(var21) + 0.125D) * 0.8F);
      this.wingTip.rotateAngleZ = (-(float)(Math.sin(var21 + 2.0F) + 0.5D) * 0.75F);
      this.rearLeg.rotateAngleX = (1.0F + var10 * 0.1F);
      this.rearLegTip.rotateAngleX = (0.5F + var10 * 0.1F);
      this.rearFoot.rotateAngleX = (0.75F + var10 * 0.1F);
      this.frontLeg.rotateAngleX = (1.3F + var10 * 0.1F);
      this.frontLegTip.rotateAngleX = (-0.5F - var10 * 0.1F);
      this.frontFoot.rotateAngleX = (0.75F + var10 * 0.1F);
      this.wing.render(p_78088_7_);
      this.frontLeg.render(p_78088_7_);
      this.rearLeg.render(p_78088_7_);
      GlStateManager.scale(-1.0F, 1.0F, 1.0F);
      if (var23 == 0) {
        GlStateManager.cullFace(1028);
      }
    }
    GlStateManager.popMatrix();
    GlStateManager.cullFace(1029);
    GlStateManager.disableCull();
    float var24 = -(float)Math.sin(var9 * 3.1415927F * 2.0F) * 0.0F;
    var18 = var9 * 3.1415927F * 2.0F;
    var11 = 10.0F;
    var12 = 60.0F;
    var13 = 0.0F;
    var15 = var8.getMovementOffsets(11, this.partialTicks);
    for (int var25 = 0; var25 < 12; var25++)
    {
      var22 = var8.getMovementOffsets(12 + var25, this.partialTicks);
      var24 = (float)(var24 + Math.sin(var25 * 0.45F + var18) * 0.05000000074505806D);
      this.spine.rotateAngleY = ((updateRotations(var22[0] - var15[0]) * var14 + 180.0F) * 3.1415927F / 180.0F);
      this.spine.rotateAngleX = (var24 + (float)(var22[1] - var15[1]) * 3.1415927F / 180.0F * var14 * 5.0F);
      this.spine.rotateAngleZ = (updateRotations(var22[0] - var17) * 3.1415927F / 180.0F * var14);
      this.spine.rotationPointY = var11;
      this.spine.rotationPointZ = var12;
      this.spine.rotationPointX = var13;
      var11 = (float)(var11 + Math.sin(this.spine.rotateAngleX) * 10.0D);
      var12 = (float)(var12 - Math.cos(this.spine.rotateAngleY) * Math.cos(this.spine.rotateAngleX) * 10.0D);
      var13 = (float)(var13 - Math.sin(this.spine.rotateAngleY) * Math.cos(this.spine.rotateAngleX) * 10.0D);
      this.spine.render(p_78088_7_);
    }
    GlStateManager.popMatrix();
  }
  
  private float updateRotations(double p_78214_1_)
  {
    while (p_78214_1_ >= 180.0D) {
      p_78214_1_ -= 360.0D;
    }
    while (p_78214_1_ < -180.0D) {
      p_78214_1_ += 360.0D;
    }
    return (float)p_78214_1_;
  }
}
