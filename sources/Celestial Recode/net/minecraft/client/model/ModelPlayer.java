package net.minecraft.client.model;

import java.awt.Color;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.EnumHandSide;
import net.minecraft.util.math.MathHelper;
import ru.smertnix.celestial.Celestial;
import ru.smertnix.celestial.feature.impl.player.AutoEat;
import ru.smertnix.celestial.feature.impl.visual.CustomModel;
import ru.smertnix.celestial.utils.render.ClientHelper;
import ru.smertnix.celestial.utils.render.ColorUtils;
import ru.smertnix.celestial.utils.render.RenderUtils;


public class ModelPlayer extends ModelBiped {
    public ModelRenderer bipedLeftArmwear;

    public ModelRenderer bipedRightArmwear;

    public ModelRenderer bipedLeftLegwear;

    public ModelRenderer bipedRightLegwear;

    public ModelRenderer bipedBodyWear;

    private final ModelRenderer bipedCape;

    private final ModelRenderer bipedDeadmau5Head;

    private final boolean smallArms;

    private final ModelRenderer body;

    private ModelRenderer eye;

    private final ModelRenderer left_leg;

    private final ModelRenderer right_leg;

    private final ModelRenderer RightLeg;

    private final ModelRenderer LeftLeg;

    private final ModelRenderer Body;

    private final ModelRenderer RightArm;

    private final ModelRenderer Head;

    private final ModelRenderer LeftArm;

    private final ModelRenderer bone;

    private final ModelRenderer cube_r1;

    private final ModelRenderer cube_r2;

    private final ModelRenderer cube_r3;

    private final ModelRenderer cube_r4;

    private final ModelRenderer head7;

    private final ModelRenderer left_horn;

    private final ModelRenderer right_horn;

    private final ModelRenderer body7;

    private final ModelRenderer left_wing;

    private final ModelRenderer right_wing;

    private final ModelRenderer left_arm7;

    private final ModelRenderer right_arm7;

    private final ModelRenderer left_leg7;

    private final ModelRenderer left_leg1;

    private final ModelRenderer bone2;

    private final ModelRenderer bone3;

    private final ModelRenderer bone7;

    private final ModelRenderer right_leg7;

    private final ModelRenderer right_leg3;

    private final ModelRenderer bone4;

    private final ModelRenderer bone5;

    private final ModelRenderer bone6;

    ModelRenderer head;

    ModelRenderer nose;

    ModelRenderer ear1;

    ModelRenderer ear2;

    ModelRenderer bodyfront;

    ModelRenderer bodyback;

    ModelRenderer leg1;

    ModelRenderer foot1;

    ModelRenderer leg2;

    ModelRenderer foot2;

    ModelRenderer leg3;

    ModelRenderer foot3;

    ModelRenderer leg4;

    ModelRenderer foot4;

    ModelRenderer tail;

    ModelRenderer Back;

    ModelRenderer Nose;

    ModelRenderer RightEar;

    ModelRenderer RightWhiskers;

    ModelRenderer LeftEar;

    ModelRenderer UpperTail;

    ModelRenderer chinBody;

    ModelRenderer RightEye;

    ModelRenderer LowerTail;

    ModelRenderer RightRearFoot;

    ModelRenderer LeftRearFoot;

    ModelRenderer RightFrontLeg;

    ModelRenderer RightFrontFoot;

    ModelRenderer LeftFrontLeg;

    ModelRenderer LeftFrontFoot;

    ModelRenderer Chin;

    ModelRenderer LeftWhiskers;

    ModelRenderer chinHead;

    ModelRenderer LeftEye;

    public ModelRenderer fredbody;

    public ModelRenderer torso;

    public ModelRenderer armRight;

    public ModelRenderer crotch;

    public ModelRenderer legRight;

    public ModelRenderer legLeft;

    public ModelRenderer armLeft;

    public ModelRenderer fredhead;

    public ModelRenderer armRightpad;

    public ModelRenderer armRight2;

    public ModelRenderer armRightpad2;

    public ModelRenderer handRight;

    public ModelRenderer legRightpad;

    public ModelRenderer legRight2;

    public ModelRenderer legRightpad2;

    public ModelRenderer footRight;

    public ModelRenderer legLeftpad;

    public ModelRenderer legLeft2;

    public ModelRenderer legLeftpad2;

    public ModelRenderer footLeft;

    public ModelRenderer armLeftpad;

    public ModelRenderer armLeft2;

    public ModelRenderer armLeftpad2;

    public ModelRenderer handLeft;

    public ModelRenderer jaw;

    public ModelRenderer frednose;

    public ModelRenderer earRight;

    public ModelRenderer earLeft;

    public ModelRenderer hat;

    public ModelRenderer earRightpad;

    public ModelRenderer earRightpad_1;

    public ModelRenderer hat2;

    ModelRenderer Agarrador_2;

    ModelRenderer Agarrador_3;

    ModelRenderer Cuerpo;

    ModelRenderer Pie_3;

    ModelRenderer Agarrador_1;

    ModelRenderer Pantalon_1;

    ModelRenderer Cabeza;

    ModelRenderer Pierna_2;

    ModelRenderer Pitillo_1;

    ModelRenderer Pierna_1;

    ModelRenderer Pitillo_2;

    ModelRenderer Pie_1;

    ModelRenderer Pie_4;

    ModelRenderer Brazo_1;

    ModelRenderer Pie_2;

    ModelRenderer Pantalon_2;

    ModelRenderer Pantalon_3;

    ModelRenderer Brazo_2;

    ModelRenderer Brazo_3;

    ModelRenderer Brazo_4;

    ModelRenderer Guante_1;

    ModelRenderer Guante_2;

    ModelRenderer Mano_1;

    ModelRenderer Mano_2;

    private final ModelRenderer bb_main;

    private final ModelRenderer Rleg;

    private final ModelRenderer Lleg;

    private final ModelRenderer Larm;

    private final ModelRenderer Rarm;

    private final ModelRenderer sonicHead;

    private final ModelRenderer rabbitBone;

    private final ModelRenderer rabbitRleg;

    private final ModelRenderer rabbitLarm;

    private final ModelRenderer rabbitRarm;

    private final ModelRenderer rabbitLleg;

    private final ModelRenderer rabbitHead;

    public ModelPlayer(float modelSize, boolean smallArmsIn) {
        super(modelSize, 0.0F, 64, 64);
        this.smallArms = smallArmsIn;
        this.bipedDeadmau5Head = new ModelRenderer(this, 24, 0);
        this.bipedDeadmau5Head.addBox(-3.0F, -6.0F, -1.0F, 6, 6, 1, modelSize);
        this.bipedCape = new ModelRenderer(this, 0, 0);
        this.bipedCape.setTextureSize(64, 32);
        this.bipedCape.addBox(-5.0F, 0.0F, -1.0F, 10, 16, 1, modelSize);
        if (smallArmsIn) {
            this.bipedLeftArm = new ModelRenderer(this, 32, 48);
            this.bipedLeftArm.addBox(-1.0F, -2.0F, -2.0F, 3, 12, 4, modelSize);
            this.bipedLeftArm.setRotationPoint(5.0F, 2.5F, 0.0F);
            this.bipedRightArm = new ModelRenderer(this, 40, 16);
            this.bipedRightArm.addBox(-2.0F, -2.0F, -2.0F, 3, 12, 4, modelSize);
            this.bipedRightArm.setRotationPoint(-5.0F, 2.5F, 0.0F);
            this.bipedLeftArmwear = new ModelRenderer(this, 48, 48);
            this.bipedLeftArmwear.addBox(-1.0F, -2.0F, -2.0F, 3, 12, 4, modelSize + 0.25F);
            this.bipedLeftArmwear.setRotationPoint(5.0F, 2.5F, 0.0F);
            this.bipedRightArmwear = new ModelRenderer(this, 40, 32);
            this.bipedRightArmwear.addBox(-2.0F, -2.0F, -2.0F, 3, 12, 4, modelSize + 0.25F);
            this.bipedRightArmwear.setRotationPoint(-5.0F, 2.5F, 10.0F);
        } else {
            this.bipedLeftArm = new ModelRenderer(this, 32, 48);
            this.bipedLeftArm.addBox(-1.0F, -2.0F, -2.0F, 4, 12, 4, modelSize);
            this.bipedLeftArm.setRotationPoint(5.0F, 2.0F, 0.0F);
            this.bipedLeftArmwear = new ModelRenderer(this, 48, 48);
            this.bipedLeftArmwear.addBox(-1.0F, -2.0F, -2.0F, 4, 12, 4, modelSize + 0.25F);
            this.bipedLeftArmwear.setRotationPoint(5.0F, 2.0F, 0.0F);
            this.bipedRightArmwear = new ModelRenderer(this, 40, 32);
            this.bipedRightArmwear.addBox(-3.0F, -2.0F, -2.0F, 4, 12, 4, modelSize + 0.25F);
            this.bipedRightArmwear.setRotationPoint(-5.0F, 2.0F, 10.0F);
        }
        this.bipedLeftLeg = new ModelRenderer(this, 16, 48);
        this.bipedLeftLeg.addBox(-2.0F, 0.0F, -2.0F, 4, 12, 4, modelSize);
        this.bipedLeftLeg.setRotationPoint(1.9F, 12.0F, 0.0F);
        this.bipedLeftLegwear = new ModelRenderer(this, 0, 48);
        this.bipedLeftLegwear.addBox(-2.0F, 0.0F, -2.0F, 4, 12, 4, modelSize + 0.25F);
        this.bipedLeftLegwear.setRotationPoint(1.9F, 12.0F, 0.0F);
        this.bipedRightLegwear = new ModelRenderer(this, 0, 32);
        this.bipedRightLegwear.addBox(-2.0F, 0.0F, -2.0F, 4, 12, 4, modelSize + 0.25F);
        this.bipedRightLegwear.setRotationPoint(-1.9F, 12.0F, 0.0F);
        this.bipedBodyWear = new ModelRenderer(this, 16, 32);
        this.bipedBodyWear.addBox(-4.0F, 0.0F, -2.0F, 8, 12, 4, modelSize + 0.25F);
        this.bipedBodyWear.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.body = new ModelRenderer(this);
        this.body.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.body.setTextureOffset(34, 8).addBox(-4.0F, 6.0F, -3.0F, 8, 12, 6);
        this.body.setTextureOffset(15, 10).addBox(-3.0F, 9.0F, 3.0F, 6, 8, 3);
        this.body.setTextureOffset(26, 0).addBox(-3.0F, 5.0F, -3.0F, 6, 1, 6);
        this.eye = new ModelRenderer(this);
        this.eye.setTextureOffset(0, 10).addBox(-3.0F, 7.0F, -4.0F, 6, 4, 1);
        this.left_leg = new ModelRenderer(this);
        this.left_leg.setRotationPoint(-2.0F, 18.0F, 0.0F);
        this.left_leg.setTextureOffset(0, 0).addBox(2.9F, 0.0F, -1.5F, 3, 6, 3, 0.0F);
        this.right_leg = new ModelRenderer(this);
        this.right_leg.setRotationPoint(2.0F, 18.0F, 0.0F);
        this.right_leg.setTextureOffset(13, 0).addBox(-5.9F, 0.0F, -1.5F, 3, 6, 3);
        this.RightLeg = new ModelRenderer(this);
        this.RightLeg.setRotationPoint(-2.0F, 14.0F, 0.0F);
        this.RightLeg.cubeList.add(new ModelBox(this.RightLeg, 0, 36, -2.0F, 0.0F, -2.0F, 4, 10, 4, 0.0F, false));
        this.LeftLeg = new ModelRenderer(this);
        this.LeftLeg.setRotationPoint(2.0F, 14.0F, 0.0F);
        this.LeftLeg.cubeList.add(new ModelBox(this.LeftLeg, 24, 24, -2.0F, 0.0F, -2.0F, 4, 10, 4, 0.0F, false));
        this.Body = new ModelRenderer(this);
        this.Body.setRotationPoint(0.0F, 24.0F, 0.0F);
        setRotationAngle(this.Body, 0.2618F, 0.0F, 0.0F);
        this.Body.cubeList.add(new ModelBox(this.Body, 0, 18, -4.0F, -23.1486F, 0.5266F, 8, 14, 4, 0.0F, false));
        this.RightArm = new ModelRenderer(this);
        this.RightArm.setRotationPoint(0.0F, 24.0F, 0.0F);
        setRotationAngle(this.RightArm, -1.309F, 0.0F, 0.0F);
        this.RightArm.cubeList.add(new ModelBox(this.RightArm, 36, 0, -7.0F, -4.5F, -23.25F, 3, 12, 3, 0.0F, false));
        this.RightArm.cubeList.add(new ModelBox(this.RightArm, 16, 36, -6.0F, 5.75F, -25.25F, 1, 2, 5, 0.0F, false));
        this.RightArm.cubeList.add(new ModelBox(this.RightArm, 31, 15, -6.0F, 5.75F, -30.25F, 1, 2, 5, 0.0F, false));
        this.RightArm.cubeList.add(new ModelBox(this.RightArm, 0, 0, -6.0F, 8.75F, -28.25F, 1, 1, 3, 0.0F, false));
        this.RightArm.cubeList.add(new ModelBox(this.RightArm, 24, 18, -6.0F, 7.75F, -29.25F, 1, 1, 5, 0.0F, false));
        this.Head = new ModelRenderer(this);
        this.Head.setRotationPoint(0.0F, 1.0F, -3.0F);
        this.Head.cubeList.add(new ModelBox(this.Head, 0, 0, -5.0F, -9.75F, -5.0F, 10, 10, 8, 0.0F, false));
        this.LeftArm = new ModelRenderer(this);
        this.LeftArm.setRotationPoint(4.0F, 3.0F, -3.0F);
        this.LeftArm.cubeList.add(new ModelBox(this.LeftArm, 37, 37, 0.0F, -1.75F, -1.5F, 3, 12, 3, 0.0F, false));
        (this.bone = new ModelRenderer(this)).setRotationPoint(0.0F, 24.0F, 0.0F);
        this.bone.setTextureOffset(0, 0).addCube(-8.0F, -12.0F, -6.0F, 16.0F, 7.0F, 12.0F, 0.0F, false);
        this.bone.setTextureOffset(44, 0).addCube(7.0F, -8.0F, -13.0F, 2.0F, 3.0F, 7.0F, 0.0F, false);
        this.bone.setTextureOffset(40, 40).addCube(-9.0F, -8.0F, -13.0F, 2.0F, 3.0F, 7.0F, 0.0F, false);
        this.bone.setTextureOffset(44, 27).addCube(-10.0F, -9.0F, -16.0F, 6.0F, 5.0F, 3.0F, 0.0F, false);
        this.bone.setTextureOffset(44, 19).addCube(4.0F, -9.0F, -16.0F, 6.0F, 5.0F, 3.0F, 0.0F, false);
        this.bone.setTextureOffset(32, 55).addCube(2.0F, -5.0F, -16.0F, 2.0F, 1.0F, 3.0F, 0.0F, false);
        this.bone.setTextureOffset(31, 19).addCube(-4.0F, -5.0F, -16.0F, 2.0F, 1.0F, 3.0F, 0.0F, false);
        this.bone.setTextureOffset(0, 41).addCube(-4.0F, -9.0F, -16.0F, 3.0F, 2.0F, 3.0F, 0.0F, false);
        this.bone.setTextureOffset(20, 39).addCube(1.0F, -9.0F, -16.0F, 3.0F, 2.0F, 3.0F, 0.0F, false);
        this.bone.setTextureOffset(0, 35).addCube(-6.0F, -8.0F, -9.0F, 3.0F, 3.0F, 3.0F, 0.0F, false);
        this.bone.setTextureOffset(22, 22).addCube(3.0F, -8.0F, -9.0F, 3.0F, 3.0F, 3.0F, 0.0F, false);
        (this.cube_r1 = new ModelRenderer(this)).setRotationPoint(0.0F, 0.0F, 0.0F);
        this.bone.addChild(this.cube_r1);
        setRotationAngle(this.cube_r1, 0.0F, 0.0F, 0.3491F);
        this.cube_r1.setTextureOffset(51, 39).addCube(-11.5F, -1.8F, 4.0F, 2.0F, 6.0F, 2.0F, 0.0F, false);
        this.cube_r1.setTextureOffset(8, 51).addCube(-11.5F, -1.8F, 0.0F, 2.0F, 6.0F, 2.0F, 0.0F, false);
        this.cube_r1.setTextureOffset(24, 55).addCube(-11.5F, -1.8F, -6.0F, 2.0F, 6.0F, 2.0F, 0.0F, false);
        this.cube_r1.setTextureOffset(0, 35).addCube(-12.5F, -5.8F, -6.0F, 4.0F, 4.0F, 12.0F, 0.0F, false);
        this.cube_r1.setTextureOffset(22, 23).addCube(3.3F, -13.6F, -6.0F, 5.0F, 4.0F, 12.0F, 0.0F, false);
        (this.cube_r2 = new ModelRenderer(this)).setRotationPoint(0.0F, 0.0F, 0.0F);
        this.bone.addChild(this.cube_r2);
        setRotationAngle(this.cube_r2, 0.0F, 0.0F, -0.3491F);
        this.cube_r2.setTextureOffset(52, 52).addCube(9.5F, -1.8F, 4.0F, 2.0F, 6.0F, 2.0F, 0.0F, false);
        this.cube_r2.setTextureOffset(16, 55).addCube(9.5F, -1.8F, 0.0F, 2.0F, 6.0F, 2.0F, 0.0F, false);
        this.cube_r2.setTextureOffset(0, 51).addCube(9.5F, -1.8F, -6.0F, 2.0F, 6.0F, 2.0F, 0.0F, false);
        this.cube_r2.setTextureOffset(20, 39).addCube(8.5F, -5.8F, -6.0F, 4.0F, 4.0F, 12.0F, 0.0F, false);
        this.cube_r2.setTextureOffset(0, 19).addCube(-8.3F, -13.6F, -6.0F, 5.0F, 4.0F, 12.0F, 0.0F, false);
        (this.cube_r3 = new ModelRenderer(this)).setRotationPoint(0.0F, 0.0F, 0.0F);
        this.bone.addChild(this.cube_r3);
        setRotationAngle(this.cube_r3, 0.0F, 0.0F, 0.1309F);
        this.cube_r3.setTextureOffset(0, 0).addCube(1.3F, -18.0F, -6.7F, 3.0F, 6.0F, 3.0F, 0.0F, false);
        (this.cube_r4 = new ModelRenderer(this)).setRotationPoint(0.0F, 0.0F, 0.0F);
        this.bone.addChild(this.cube_r4);
        setRotationAngle(this.cube_r4, 0.0F, 0.0F, -0.1309F);
        this.cube_r4.setTextureOffset(0, 19).addCube(-4.3F, -18.0F, -6.7F, 3.0F, 6.0F, 3.0F, 0.0F, false);
        this.head7 = new ModelRenderer(this);
        this.head7.setRotationPoint(0.0F, -6.0F, -1.0F);
        this.head7.setTextureOffset(0, 0).addCube(-4.0F, -4.0F, -3.0F, 8.0F, 8.0F, 8.0F, 0.3F, false);
        this.left_horn = new ModelRenderer(this);
        this.left_horn.setRotationPoint(-8.0F, 8.0F, 0.0F);
        this.head7.addChild(this.left_horn);
        setRotationAngle(this.left_horn, -0.3927F, 0.3927F, -0.5236F);
        this.left_horn.setTextureOffset(32, 8).addCube(13.4346F, -5.2071F, 2.7071F, 6.0F, 2.0F, 2.0F, 0.1F, false);
        this.left_horn.setTextureOffset(0, 0).addCube(17.4346F, -10.4071F, 2.7071F, 2.0F, 5.0F, 2.0F, 0.1F, false);
        this.right_horn = new ModelRenderer(this);
        this.right_horn.setRotationPoint(8.0F, 8.0F, 0.0F);
        this.head7.addChild(this.right_horn);
        setRotationAngle(this.right_horn, -0.3927F, -0.3927F, 0.5236F);
        this.right_horn.setTextureOffset(32, 8).addCube(-19.4346F, -5.2071F, 2.7071F, 6.0F, 2.0F, 2.0F, 0.1F, true);
        this.right_horn.setTextureOffset(0, 0).addCube(-19.4346F, -10.4071F, 2.7071F, 2.0F, 5.0F, 2.0F, 0.1F, true);
        this.body7 = new ModelRenderer(this);
        this.body7.setRotationPoint(0.5F, -0.1F, -3.5F);
        setRotationAngle(this.body7, 0.1745F, 0.0F, 0.0F);
        this.body7.setTextureOffset(0, 16).addCube(-4.5F, -1.7028F, 1.4696F, 8.0F, 12.0F, 4.0F, 0.0F, false);
        this.left_wing = new ModelRenderer(this);
        this.left_wing.setRotationPoint(8.25F, -2.0F, 10.0F);
        this.body7.addChild(this.left_wing);
        setRotationAngle(this.left_wing, 0.0873F, -0.829F, 0.1745F);
        this.left_wing.setTextureOffset(40, 12).addCube(-7.0072F, -0.5972F, 0.7515F, 12.0F, 13.0F, 0.0F, 0.0F, false);
        this.right_wing = new ModelRenderer(this);
        this.right_wing.setRotationPoint(-9.25F, -2.0F, 10.0F);
        this.body7.addChild(this.right_wing);
        setRotationAngle(this.right_wing, 0.0873F, 0.829F, -0.1745F);
        this.right_wing.setTextureOffset(40, 12).addCube(-4.9928F, -0.5972F, 0.7515F, 12.0F, 13.0F, 0.0F, 0.0F, true);
        this.left_arm7 = new ModelRenderer(this);
        this.left_arm7.setRotationPoint(5.4F, -1.25F, -2.0F);
        setRotationAngle(this.left_arm7, 0.0F, 0.0F, -0.2182F);
        this.left_arm7.setTextureOffset(24, 16).addCube(-1.1F, -1.05F, 0.0F, 4.0F, 14.0F, 4.0F, 0.0F, false);
        this.right_arm7 = new ModelRenderer(this);
        this.right_arm7.setRotationPoint(-5.4F, -1.25F, -2.0F);
        setRotationAngle(this.right_arm7, 0.0F, 0.0F, 0.2182F);
        this.right_arm7.setTextureOffset(24, 16).addCube(-2.9F, -1.05F, 0.0F, 4.0F, 14.0F, 4.0F, 0.0F, true);
        this.left_leg7 = new ModelRenderer(this);
        this.left_leg7.setRotationPoint(3.0F, 10.0F, 0.0F);
        this.left_leg7.setTextureOffset(48, 22).addCube(-3.25F, -2.25F, -1.0F, 4.0F, 9.0F, 4.0F, 0.0F, false);
        this.left_leg1 = new ModelRenderer(this);
        this.left_leg1.setRotationPoint(-1.7F, -0.1F, -3.55F);
        this.left_leg7.addChild(this.left_leg1);
        setRotationAngle(this.left_leg1, -0.5236F, 0.0F, 0.0F);
        this.left_leg1.setTextureOffset(34, 34).addCube(0.95F, 4.6F, 8.0511F, 3.0F, 5.0F, 3.0F, 0.0F, false);
        this.bone2 = new ModelRenderer(this);
        this.bone2.setRotationPoint(1.4F, 15.0F, 0.25F);
        this.left_leg1.addChild(this.bone2);
        setRotationAngle(this.bone2, 0.5236F, 0.0F, 0.0F);
        this.bone2.setTextureOffset(26, 0).addCube(-0.7F, -1.15F, 9.3F, 4.0F, 2.0F, 4.0F, 0.0F, false);
        this.bone2.setTextureOffset(40, 0).addCube(-0.7F, -1.15F, 7.3F, 4.0F, 2.0F, 2.0F, 0.0F, false);
        this.bone3 = new ModelRenderer(this);
        this.bone3.setRotationPoint(-1.0F, 0.0F, -2.0F);
        this.left_leg1.addChild(this.bone3);
        setRotationAngle(this.bone3, 0.0F, -0.0873F, -0.2618F);
        this.bone7 = new ModelRenderer(this);
        this.bone7.setRotationPoint(1.9F, 12.0F, 0.25F);
        this.bone3.addChild(this.bone7);
        this.bone7.setTextureOffset(16, 34).addCube(-0.7911F, -10.1159F, 8.0029F, 4.0F, 4.0F, 5.0F, 0.0F, false);
        this.bone7.setTextureOffset(0, 32).addCube(-0.7911F, -15.1159F, 4.0029F, 4.0F, 9.0F, 4.0F, 0.0F, false);
        this.right_leg7 = new ModelRenderer(this);
        this.right_leg7.setRotationPoint(-3.0F, 10.0F, 0.0F);
        this.right_leg7.setTextureOffset(48, 22).addCube(-0.75F, -2.25F, -1.0F, 4.0F, 9.0F, 4.0F, 0.0F, true);
        this.right_leg3 = new ModelRenderer(this);
        this.right_leg3.setRotationPoint(1.7F, -0.1F, -3.55F);
        this.right_leg7.addChild(this.right_leg3);
        setRotationAngle(this.right_leg3, -0.5236F, 0.0F, 0.0F);
        this.right_leg3.setTextureOffset(34, 34).addCube(-3.95F, 4.6F, 8.0511F, 3.0F, 5.0F, 3.0F, 0.0F, true);
        this.bone4 = new ModelRenderer(this);
        this.bone4.setRotationPoint(-1.4F, 15.0F, 0.25F);
        this.right_leg3.addChild(this.bone4);
        setRotationAngle(this.bone4, 0.5236F, 0.0F, 0.0F);
        this.bone4.setTextureOffset(26, 0).addCube(-3.3F, -1.15F, 9.3F, 4.0F, 2.0F, 4.0F, 0.0F, true);
        this.bone4.setTextureOffset(40, 0).addCube(-3.3F, -1.15F, 7.3F, 4.0F, 2.0F, 2.0F, 0.0F, true);
        this.bone5 = new ModelRenderer(this);
        this.bone5.setRotationPoint(1.0F, 0.0F, -2.0F);
        this.right_leg3.addChild(this.bone5);
        setRotationAngle(this.bone5, 0.0F, 0.0873F, 0.2618F);
        this.bone6 = new ModelRenderer(this);
        this.bone6.setRotationPoint(-1.9F, 12.0F, 0.25F);
        this.bone5.addChild(this.bone6);
        this.bone6.setTextureOffset(16, 34).addCube(-3.2089F, -10.1159F, 8.0029F, 4.0F, 4.0F, 5.0F, 0.0F, true);
        this.bone6.setTextureOffset(0, 32).addCube(-3.2089F, -15.1159F, 4.0029F, 4.0F, 9.0F, 4.0F, 0.0F, true);
        (this.head = new ModelRenderer(this, 29, 5)).addBox(-4.0F, -4.0F, -6.0F, 8, 7, 6);
        this.head.setRotationPoint(0.0F, 14.0F, -5.0F);
        this.head.setTextureSize(64, 64);
        this.head.mirror = true;
        setRotationAngle(this.head, 0.0F, 0.0F, 0.0F);
        (this.nose = new ModelRenderer(this, 45, 20)).addBox(-2.0F, -0.5F, -7.5F, 4, 3, 2);
        this.nose.setRotationPoint(0.0F, 14.0F, -5.0F);
        this.nose.setTextureSize(64, 64);
        this.nose.mirror = true;
        setRotationAngle(this.nose, 0.0F, 0.0F, 0.0F);
        (this.ear1 = new ModelRenderer(this, 45, 27)).addBox(1.5F, -6.0F, -4.0F, 4, 4, 2);
        this.ear1.setRotationPoint(0.0F, 14.0F, -5.0F);
        this.ear1.setTextureSize(64, 64);
        this.ear1.mirror = true;
        setRotationAngle(this.ear1, 0.0F, -0.1745329F, 0.0F);
        (this.ear2 = new ModelRenderer(this, 45, 34)).addBox(-5.5F, -6.0F, -4.0F, 4, 4, 2);
        this.ear2.setRotationPoint(0.0F, 14.0F, -5.0F);
        this.ear2.setTextureSize(64, 64);
        this.ear2.mirror = true;
        setRotationAngle(this.ear2, 0.0F, 0.1745329F, 0.0F);
        (this.bodyfront = new ModelRenderer(this, 2, 45)).addBox(0.0F, 0.0F, 0.0F, 9, 8, 9);
        this.bodyfront.setRotationPoint(-4.5F, 11.0F, -6.0F);
        this.bodyfront.setTextureSize(64, 64);
        this.bodyfront.mirror = true;
        setRotationAngle(this.bodyfront, 0.0872665F, 0.0F, 0.0F);
        (this.bodyback = new ModelRenderer(this, 2, 26)).addBox(0.0F, 0.0F, 0.0F, 10, 8, 10);
        this.bodyback.setRotationPoint(-5.0F, 10.0F, 3.0F);
        this.bodyback.setTextureSize(64, 64);
        this.bodyback.mirror = true;
        setRotationAngle(this.bodyback, -0.0872665F, 0.0F, 0.0F);
        (this.leg1 = new ModelRenderer(this, 44, 50)).addBox(0.0F, 0.0F, -2.0F, 4, 8, 4);
        this.leg1.setRotationPoint(1.0F, 16.0F, -5.0F);
        this.leg1.setTextureSize(64, 64);
        this.leg1.mirror = true;
        setRotationAngle(this.leg1, 0.0F, 0.0F, 0.0F);
        (this.foot1 = new ModelRenderer(this, 47, 43)).addBox(0.0F, 6.0F, -3.0F, 4, 2, 1);
        this.foot1.setRotationPoint(1.0F, 16.0F, -5.0F);
        this.foot1.setTextureSize(64, 64);
        this.foot1.mirror = true;
        setRotationAngle(this.foot1, 0.0F, 0.0F, 0.0F);
        (this.leg2 = new ModelRenderer(this, 44, 50)).addBox(-4.0F, 0.0F, -2.0F, 4, 8, 4);
        this.leg2.setRotationPoint(-1.0F, 16.0F, -5.0F);
        this.leg2.setTextureSize(64, 64);
        this.leg2.mirror = true;
        setRotationAngle(this.leg2, 0.0F, 0.0F, 0.0F);
        (this.foot2 = new ModelRenderer(this, 47, 43)).addBox(-4.0F, 6.0F, -3.0F, 4, 2, 1);
        this.foot2.setRotationPoint(-1.0F, 16.0F, -5.0F);
        this.foot2.setTextureSize(64, 64);
        this.foot2.mirror = true;
        setRotationAngle(this.foot2, 0.0F, 0.0F, 0.0F);
        (this.leg3 = new ModelRenderer(this, 44, 50)).addBox(0.0F, 0.0F, -2.0F, 4, 8, 4);
        this.leg3.setRotationPoint(1.5F, 16.0F, 9.0F);
        this.leg3.setTextureSize(64, 64);
        this.leg3.mirror = true;
        setRotationAngle(this.leg3, 0.0F, 0.0F, 0.0F);
        (this.foot3 = new ModelRenderer(this, 47, 43)).addBox(0.0F, 6.0F, -3.0F, 4, 2, 1);
        this.foot3.setRotationPoint(1.5F, 16.0F, 9.0F);
        this.foot3.setTextureSize(64, 64);
        this.foot3.mirror = true;
        setRotationAngle(this.foot3, 0.0F, 0.0F, 0.0F);
        (this.leg4 = new ModelRenderer(this, 44, 50)).addBox(-4.0F, 0.0F, -2.0F, 4, 8, 4);
        this.leg4.setRotationPoint(-1.5F, 16.0F, 9.0F);
        this.leg4.setTextureSize(64, 64);
        this.leg4.mirror = true;
        setRotationAngle(this.leg4, 0.0F, 0.0F, 0.0F);
        (this.foot4 = new ModelRenderer(this, 47, 43)).addBox(-4.0F, 6.0F, -3.0F, 4, 2, 1);
        this.foot4.setRotationPoint(-1.5F, 16.0F, 9.0F);
        this.foot4.setTextureSize(64, 64);
        this.foot4.mirror = true;
        setRotationAngle(this.foot4, 0.0F, 0.0F, 0.0F);
        (this.tail = new ModelRenderer(this, 2, 3)).addBox(-2.0F, -2.0F, 0.0F, 4, 5, 17);
        this.tail.setRotationPoint(0.0F, 14.0F, 11.0F);
        this.tail.setTextureSize(64, 64);
        this.tail.mirror = true;
        setRotationAngle(this.tail, -0.1745329F, 0.0F, 0.0F);
        this.textureWidth = 64;
        this.textureHeight = 32;
        (this.Back = new ModelRenderer(this, 0, 14)).addBox(-3.0F, 0.3F, 3.3F, 6, 6, 3);
        this.Back.setRotationPoint(0.0F, 18.0F, -2.0F);
        this.Back.setTextureSize(64, 32);
        this.Back.mirror = true;
        setRotationAngle(this.Back, 0.2443461F, 0.0F, 0.0F);
        (this.Nose = new ModelRenderer(this, 46, 24)).addBox(-2.0F, -3.0F, -3.0F, 4, 4, 2);
        this.Nose.setRotationPoint(0.0F, 18.0F, -3.0F);
        this.Nose.setTextureSize(64, 32);
        this.Nose.mirror = true;
        setRotationAngle(this.Nose, 0.3687912F, 0.0F, 0.0F);
        (this.RightEar = new ModelRenderer(this, 48, 8)).addBox(0.5F, -6.0F, -0.6F, 1, 4, 3);
        this.RightEar.setRotationPoint(0.0F, 18.0F, -3.0F);
        this.RightEar.setTextureSize(64, 32);
        this.RightEar.mirror = true;
        setRotationAngle(this.RightEar, 0.8726646F, 2.094395F, 0.0F);
        (this.RightWhiskers = new ModelRenderer(this, 18, 19)).addBox(-5.0F, -1.4F, -4.04F, 4, 3, 1);
        this.RightWhiskers.setRotationPoint(0.0F, 18.0F, -3.0F);
        this.RightWhiskers.setTextureSize(64, 32);
        this.RightWhiskers.mirror = true;
        setRotationAngle(this.RightWhiskers, -0.0872665F, 0.1745329F, 0.0F);
        (this.LeftEar = new ModelRenderer(this, 48, 0)).addBox(0.5F, -6.0F, -2.4F, 1, 4, 3);
        this.LeftEar.setRotationPoint(0.0F, 18.0F, -3.0F);
        this.LeftEar.setTextureSize(64, 32);
        this.LeftEar.mirror = true;
        setRotationAngle(this.LeftEar, -0.8726646F, 1.047198F, 0.0F);
        (this.UpperTail = new ModelRenderer(this, 14, 23)).addBox(-1.5F, 1.5F, 2.6F, 3, 3, 4);
        this.UpperTail.setRotationPoint(0.0F, 22.2F, 4.066667F);
        this.UpperTail.setTextureSize(64, 32);
        this.UpperTail.mirror = true;
        setRotationAngle(this.UpperTail, 0.7330383F, 0.0F, 0.0F);
        (this.chinBody = new ModelRenderer(this, 0, 0)).addBox(-3.5F, -2.0F, -1.0F, 7, 7, 7);
        this.chinBody.setRotationPoint(0.0F, 18.0F, -2.0F);
        this.chinBody.setTextureSize(64, 32);
        this.chinBody.mirror = true;
        setRotationAngle(this.chinBody, -0.0872665F, 0.0F, 0.0F);
        (this.LowerTail = new ModelRenderer(this, 0, 23)).addBox(-1.0F, -1.0F, 0.0F, 2, 2, 5);
        this.LowerTail.setRotationPoint(0.0F, 22.2F, 4.066667F);
        this.LowerTail.setTextureSize(64, 32);
        this.LowerTail.mirror = true;
        setRotationAngle(this.LowerTail, -0.1047198F, 0.0F, 0.0F);
        (this.RightRearFoot = new ModelRenderer(this, 28, 9)).addBox(-1.5F, 0.0F, -3.0F, 2, 1, 4);
        this.RightRearFoot.setRotationPoint(-3.0F, 23.0F, 3.0F);
        this.RightRearFoot.setTextureSize(64, 32);
        this.RightRearFoot.mirror = true;
        setRotationAngle(this.RightRearFoot, 0.0F, 0.0698132F, 0.0F);
        (this.LeftRearFoot = new ModelRenderer(this, 28, 0)).addBox(-0.5F, 0.0F, -3.0F, 2, 1, 4);
        this.LeftRearFoot.setRotationPoint(3.0F, 23.0F, 3.0F);
        this.LeftRearFoot.setTextureSize(64, 32);
        this.LeftRearFoot.mirror = true;
        setRotationAngle(this.LeftRearFoot, 0.0F, -0.0698132F, 0.0F);
        (this.RightFrontLeg = new ModelRenderer(this, 40, 9)).addBox(-1.0F, 0.0F, -1.0F, 2, 4, 2);
        this.RightFrontLeg.setRotationPoint(-3.0F, 20.0F, -3.0F);
        this.RightFrontLeg.setTextureSize(64, 32);
        this.RightFrontLeg.mirror = true;
        setRotationAngle(this.RightFrontLeg, -0.1858931F, 0.0F, 0.0F);
        (this.RightFrontFoot = new ModelRenderer(this, 28, 14)).addBox(-1.0F, 3.0F, -2.8F, 2, 1, 3);
        this.RightFrontFoot.setRotationPoint(-3.0F, 20.0F, -3.0F);
        this.RightFrontFoot.setTextureSize(64, 32);
        this.RightFrontFoot.mirror = true;
        setRotationAngle(this.RightFrontFoot, 0.0F, -0.0698132F, 0.0F);
        (this.LeftFrontLeg = new ModelRenderer(this, 40, 0)).addBox(-1.0F, 0.0F, -1.0F, 2, 4, 2);
        this.LeftFrontLeg.setRotationPoint(3.0F, 20.0F, -3.0F);
        this.LeftFrontLeg.setTextureSize(64, 32);
        this.LeftFrontLeg.mirror = true;
        setRotationAngle(this.LeftFrontLeg, -0.1858931F, 0.0F, 0.0F);
        (this.LeftFrontFoot = new ModelRenderer(this, 28, 5)).addBox(-1.0F, 3.0F, -2.8F, 2, 1, 3);
        this.LeftFrontFoot.setRotationPoint(3.0F, 20.0F, -3.0F);
        this.LeftFrontFoot.setTextureSize(64, 32);
        this.LeftFrontFoot.mirror = true;
        setRotationAngle(this.LeftFrontFoot, 0.0F, 0.0698132F, 0.0F);
        (this.Chin = new ModelRenderer(this, 46, 18)).addBox(-2.0F, -1.3F, -4.04F, 4, 3, 2);
        this.Chin.setRotationPoint(0.0F, 18.0F, -3.0F);
        this.Chin.setTextureSize(64, 32);
        this.Chin.mirror = true;
        setRotationAngle(this.Chin, -0.1115358F, 0.0F, 0.0F);
        (this.LeftWhiskers = new ModelRenderer(this, 18, 14)).addBox(1.0F, -1.4F, -4.04F, 4, 3, 1);
        this.LeftWhiskers.setRotationPoint(0.0F, 18.0F, -3.0F);
        this.LeftWhiskers.setTextureSize(64, 32);
        this.LeftWhiskers.mirror = true;
        setRotationAngle(this.LeftWhiskers, -0.0872665F, -0.1745329F, 0.0F);
        (this.chinHead = new ModelRenderer(this, 28, 21)).addBox(-2.5F, -3.0F, -2.4F, 5, 5, 4);
        this.chinHead.setRotationPoint(0.0F, 18.0F, -3.0F);
        this.chinHead.setTextureSize(64, 32);
        this.chinHead.mirror = true;
        setRotationAngle(this.chinHead, 0.0F, 0.0F, 0.0F);
        (this.LeftEye = new ModelRenderer(this, 0, 0)).addBox(-2.0F, -1.8F, -3.4F, 2, 2, 1);
        this.LeftEye.setRotationPoint(0.0F, 18.0F, -3.0F);
        this.LeftEye.setTextureSize(64, 32);
        this.LeftEye.mirror = true;
        setRotationAngle(this.LeftEye, 0.0F, -1.047198F, 0.0F);
        (this.RightEye = new ModelRenderer(this, 0, 3)).addBox(0.0F, -1.8F, -3.4F, 2, 2, 1);
        this.RightEye.setRotationPoint(0.0F, 18.0F, -3.0F);
        this.RightEye.setTextureSize(64, 32);
        this.RightEye.mirror = true;
        setRotationAngle(this.RightEye, 0.0F, 1.047198F, 0.0F);
        this.textureWidth = 64;
        this.textureHeight = 64;
        (this.rabbitBone = new ModelRenderer(this)).setRotationPoint(0.0F, 24.0F, 0.0F);
        this.rabbitBone.cubeList.add(new ModelBox(this.rabbitBone, 28, 45, -5.0F, -13.0F, -5.0F, 10, 11, 8, 0.0F, false));
        (this.rabbitRleg = new ModelRenderer(this)).setRotationPoint(-3.0F, -2.0F, -1.0F);
        this.rabbitBone.addChild(this.rabbitRleg);
        this.rabbitRleg.cubeList.add(new ModelBox(this.rabbitRleg, 0, 0, -2.0F, 0.0F, -2.0F, 4, 2, 4, 0.0F, false));
        (this.rabbitLarm = new ModelRenderer(this)).setRotationPoint(5.0F, -13.0F, -1.0F);
        setRotationAngle(this.rabbitLarm, 0.0F, 0.0F, -0.0873F);
        this.rabbitBone.addChild(this.rabbitLarm);
        this.rabbitLarm.cubeList.add(new ModelBox(this.rabbitLarm, 0, 0, 0.0F, 0.0F, -2.0F, 2, 8, 4, 0.0F, false));
        (this.rabbitRarm = new ModelRenderer(this)).setRotationPoint(-5.0F, -13.0F, -1.0F);
        setRotationAngle(this.rabbitRarm, 0.0F, 0.0F, 0.0873F);
        this.rabbitBone.addChild(this.rabbitRarm);
        this.rabbitRarm.cubeList.add(new ModelBox(this.rabbitRarm, 0, 0, -2.0F, 0.0F, -2.0F, 2, 8, 4, 0.0F, false));
        (this.rabbitLleg = new ModelRenderer(this)).setRotationPoint(3.0F, -2.0F, -1.0F);
        this.rabbitBone.addChild(this.rabbitLleg);
        this.rabbitLleg.cubeList.add(new ModelBox(this.rabbitLleg, 0, 0, -2.0F, 0.0F, -2.0F, 4, 2, 4, 0.0F, false));
        (this.rabbitHead = new ModelRenderer(this)).setRotationPoint(0.0F, -14.0F, -1.0F);
        this.rabbitBone.addChild(this.rabbitHead);
        this.rabbitHead.cubeList.add(new ModelBox(this.rabbitHead, 0, 0, -3.0F, 0.0F, -4.0F, 6, 1, 6, 0.0F, false));
        this.rabbitHead.cubeList.add(new ModelBox(this.rabbitHead, 56, 0, -5.0F, -9.0F, -5.0F, 2, 3, 2, 0.0F, false));
        this.rabbitHead.cubeList.add(new ModelBox(this.rabbitHead, 56, 0, 3.0F, -9.0F, -5.0F, 2, 3, 2, 0.0F, true));
        this.rabbitHead.cubeList.add(new ModelBox(this.rabbitHead, 0, 45, -4.0F, -11.0F, -4.0F, 8, 11, 8, 0.0F, false));
        this.rabbitHead.cubeList.add(new ModelBox(this.rabbitHead, 46, 0, 1.0F, -20.0F, 0.0F, 3, 9, 1, 0.0F, false));
        this.rabbitHead.cubeList.add(new ModelBox(this.rabbitHead, 46, 0, -4.0F, -20.0F, 0.0F, 3, 9, 1, 0.0F, false));
        this.textureWidth = 64;
        this.textureHeight = 64;
        (this.bb_main = new ModelRenderer(this)).setRotationPoint(0.0F, 24.0F, 0.0F);
        this.bb_main.cubeList.add(new ModelBox(this.bb_main, 0, 0, -3.0F, -17.0F, -2.0F, 5, 7, 5, 0.0F, false));
        this.bb_main.cubeList.add(new ModelBox(this.bb_main, 0, 22, -3.0F, -16.0F, -3.0F, 5, 5, 1, 0.0F, false));
        this.bb_main.cubeList.add(new ModelBox(this.bb_main, 0, 0, -3.0F, -16.0F, 3.0F, 5, 5, 1, 0.0F, false));
        this.bb_main.cubeList.add(new ModelBox(this.bb_main, 0, 0, 0.0F, -15.0F, 3.0F, 1, 1, 3, 0.0F, false));
        this.bb_main.cubeList.add(new ModelBox(this.bb_main, 0, 0, -3.0F, -14.0F, 4.0F, 1, 1, 2, 0.0F, false));
        this.bb_main.cubeList.add(new ModelBox(this.bb_main, 0, 0, -1.0F, -11.0F, 3.0F, 1, 1, 2, 0.0F, false));
        this.bb_main.cubeList.add(new ModelBox(this.bb_main, 0, 0, 0.0F, -14.0F, 6.0F, 1, 1, 1, 0.0F, false));
        this.bb_main.cubeList.add(new ModelBox(this.bb_main, 0, 0, -3.0F, -13.0F, 6.0F, 1, 1, 1, 0.0F, false));
        this.bb_main.cubeList.add(new ModelBox(this.bb_main, 0, 0, -1.0F, -12.0F, 5.0F, 1, 1, 1, 0.0F, false));
        this.bb_main.cubeList.add(new ModelBox(this.bb_main, 0, 0, -4.0F, -16.0F, -2.0F, 1, 5, 5, 0.0F, false));
        this.bb_main.cubeList.add(new ModelBox(this.bb_main, 0, 0, 2.0F, -16.0F, -2.0F, 1, 5, 5, 0.0F, false));
        (this.Rleg = new ModelRenderer(this)).setRotationPoint(-3.0F, 14.0F, 0.5F);
        this.Rleg.cubeList.add(new ModelBox(this.Rleg, 53, 54, -1.5F, 7.0F, -1.0F, 2, 1, 2, 0.0F, false));
        this.Rleg.cubeList.add(new ModelBox(this.Rleg, 0, 0, -1.0F, -1.0F, -0.5F, 1, 8, 1, 0.0F, false));
        this.Rleg.cubeList.add(new ModelBox(this.Rleg, 52, 58, -1.5F, 8.0F, -3.0F, 2, 2, 4, 0.0F, true));
        this.Rleg.cubeList.add(new ModelBox(this.Rleg, 52, 58, -2.0F, 9.0F, -2.0F, 0, 1, 1, 0.0F, false));
        (this.Lleg = new ModelRenderer(this)).setRotationPoint(2.0F, 14.0F, 0.5F);
        this.Lleg.cubeList.add(new ModelBox(this.Lleg, 53, 54, -0.5F, 7.0F, -1.0F, 2, 1, 2, 0.0F, false));
        this.Lleg.cubeList.add(new ModelBox(this.Lleg, 0, 0, 0.0F, -1.0F, -0.5F, 1, 8, 1, 0.0F, false));
        this.Lleg.cubeList.add(new ModelBox(this.Lleg, 52, 58, -0.5F, 8.0F, -3.0F, 2, 2, 4, 0.0F, false));
        this.Lleg.cubeList.add(new ModelBox(this.Lleg, 52, 58, 1.5F, 9.0F, -2.0F, 0, 1, 1, 0.0F, false));
        (this.Larm = new ModelRenderer(this)).setRotationPoint(3.0F, 9.0F, 0.5F);
        setRotationAngle(this.Larm, 0.0F, 0.0F, -0.0873F);
        this.Larm.cubeList.add(new ModelBox(this.Larm, 0, 60, -0.5F, 8.0F, -1.0F, 2, 2, 2, 0.0F, false));
        this.Larm.cubeList.add(new ModelBox(this.Larm, 60, 17, 0.0F, 0.0F, -0.5F, 1, 8, 1, 0.0F, false));
        (this.Rarm = new ModelRenderer(this)).setRotationPoint(-4.0F, 9.0F, 0.5F);
        setRotationAngle(this.Rarm, 0.0F, 0.0F, 0.0873F);
        this.Rarm.cubeList.add(new ModelBox(this.Rarm, 60, 17, -1.0F, 0.0F, -0.5F, 1, 8, 1, 0.0F, false));
        this.Rarm.cubeList.add(new ModelBox(this.Rarm, 0, 60, -1.5F, 8.0F, -1.0F, 2, 2, 2, 0.0F, false));
        (this.sonicHead = new ModelRenderer(this)).setRotationPoint(-0.5F, 7.0F, 0.0F);
        this.sonicHead.cubeList.add(new ModelBox(this.sonicHead, 36, 3, -3.5F, -7.0F, -3.0F, 7, 7, 7, 0.0F, false));
        this.sonicHead.cubeList.add(new ModelBox(this.sonicHead, 60, 0, -0.5F, -3.0F, -4.0F, 1, 1, 1, 0.0F, false));
        this.sonicHead.cubeList.add(new ModelBox(this.sonicHead, 0, 0, -3.5F, -4.0F, 6.0F, 3, 3, 3, 0.0F, false));
        this.sonicHead.cubeList.add(new ModelBox(this.sonicHead, 0, 0, -3.5F, -5.0F, 4.0F, 3, 3, 2, 0.0F, false));
        this.sonicHead.cubeList.add(new ModelBox(this.sonicHead, 0, 0, 0.5F, -6.0F, 6.0F, 3, 3, 3, 0.0F, false));
        this.sonicHead.cubeList.add(new ModelBox(this.sonicHead, 0, 0, 0.5F, -7.0F, 4.0F, 3, 3, 2, 0.0F, false));
        this.sonicHead.cubeList.add(new ModelBox(this.sonicHead, 0, 0, -2.5F, -9.0F, 3.0F, 3, 3, 2, 0.0F, false));
        this.sonicHead.cubeList.add(new ModelBox(this.sonicHead, 0, 0, -2.5F, -10.0F, 5.0F, 3, 3, 3, 0.0F, false));
        this.textureWidth = 128;
        this.textureHeight = 64;
        (this.Agarrador_2 = new ModelRenderer(this, 29, 10)).addBox(0.0F, 0.0F, 0.0F, 2, 2, 2);
        this.Agarrador_2.setRotationPoint(-1.0F, 4.0F, -10.0F);
        this.Agarrador_2.setTextureSize(64, 32);
        this.Agarrador_2.mirror = true;
        setRotationAngle(this.Agarrador_2, 0.0F, 0.0F, 0.0F);
        (this.Agarrador_3 = new ModelRenderer(this, 29, 10)).addBox(0.0F, 0.0F, 0.0F, 2, 2, 3);
        this.Agarrador_3.setRotationPoint(-1.0F, 6.0F, -10.0F);
        this.Agarrador_3.setTextureSize(64, 32);
        this.Agarrador_3.mirror = true;
        setRotationAngle(this.Agarrador_3, 0.0F, 0.0F, 0.0F);
        (this.Cuerpo = new ModelRenderer(this, 43, 40)).addBox(0.0F, 0.0F, 0.0F, 4, 4, 3);
        this.Cuerpo.setRotationPoint(-2.0F, 8.0F, -5.0F);
        this.Cuerpo.setTextureSize(64, 32);
        this.Cuerpo.mirror = true;
        setRotationAngle(this.Cuerpo, 0.0F, 0.0F, 0.0F);
        (this.Pie_3 = new ModelRenderer(this, 0, 0)).addBox(0.0F, 0.0F, 0.0F, 3, 1, 3);
        this.Pie_3.setRotationPoint(0.0F, 21.0F, -4.0F);
        this.Pie_3.setTextureSize(64, 32);
        this.Pie_3.mirror = true;
        setRotationAngle(this.Pie_3, 0.0F, 0.1047198F, 0.0F);
        (this.Agarrador_1 = new ModelRenderer(this, 29, 10)).addBox(0.0F, 0.0F, 0.0F, 2, 2, 3);
        this.Agarrador_1.setRotationPoint(-1.0F, 2.0F, -10.0F);
        this.Agarrador_1.setTextureSize(64, 32);
        this.Agarrador_1.mirror = true;
        setRotationAngle(this.Agarrador_1, 0.0F, 0.0F, 0.0F);
        (this.Pantalon_1 = new ModelRenderer(this, 1, 27)).addBox(0.0F, 0.0F, 0.0F, 2, 2, 2);
        this.Pantalon_1.setRotationPoint(0.8F, 15.0F, -4.0F);
        this.Pantalon_1.setTextureSize(64, 32);
        this.Pantalon_1.mirror = true;
        setRotationAngle(this.Pantalon_1, 0.0F, 0.0698132F, 0.0F);
        (this.Cabeza = new ModelRenderer(this, 11, 20)).addBox(0.0F, 0.0F, 0.0F, 8, 7, 8);
        this.Cabeza.setRotationPoint(-4.0F, 1.0F, -7.0F);
        this.Cabeza.setTextureSize(64, 32);
        this.Cabeza.mirror = true;
        setRotationAngle(this.Cabeza, 0.0F, 0.0F, 0.0F);
        (this.Pierna_2 = new ModelRenderer(this, 30, 0)).addBox(0.0F, 0.0F, 0.0F, 1, 4, 1);
        this.Pierna_2.setRotationPoint(-2.3F, 17.0F, -3.3F);
        this.Pierna_2.setTextureSize(64, 32);
        this.Pierna_2.mirror = true;
        setRotationAngle(this.Pierna_2, 0.0F, 0.0F, 0.0F);
        (this.Pitillo_1 = new ModelRenderer(this, 46, 0)).addBox(0.0F, 0.0F, 0.0F, 2, 2, 2);
        this.Pitillo_1.setRotationPoint(0.0F, -1.0F, -6.0F);
        this.Pitillo_1.setTextureSize(64, 32);
        this.Pitillo_1.mirror = true;
        setRotationAngle(this.Pitillo_1, 0.0F, 0.0F, 0.0F);
        (this.Pierna_1 = new ModelRenderer(this, 30, 0)).addBox(0.0F, 0.0F, 0.0F, 1, 4, 1);
        this.Pierna_1.setRotationPoint(1.3F, 17.0F, -3.3F);
        this.Pierna_1.setTextureSize(64, 32);
        this.Pierna_1.mirror = true;
        setRotationAngle(this.Pierna_1, 0.0F, 0.0F, 0.0F);
        (this.Pitillo_2 = new ModelRenderer(this, 54, 0)).addBox(0.0F, 0.0F, 0.0F, 2, 4, 2);
        this.Pitillo_2.setRotationPoint(0.0F, -1.0F, -8.0F);
        this.Pitillo_2.setTextureSize(64, 32);
        this.Pitillo_2.mirror = true;
        setRotationAngle(this.Pitillo_2, 1.53589F, 0.0F, 0.0F);
        (this.Pie_1 = new ModelRenderer(this, 0, 0)).addBox(0.0F, 0.0F, 0.0F, 3, 2, 6);
        this.Pie_1.setRotationPoint(0.0F, 22.0F, -4.0F);
        this.Pie_1.setTextureSize(64, 32);
        this.Pie_1.mirror = true;
        setRotationAngle(this.Pie_1, 0.0F, 0.1047198F, 0.0F);
        (this.Pie_4 = new ModelRenderer(this, 0, 0)).addBox(0.0F, 0.0F, 0.0F, 3, 1, 3);
        this.Pie_4.setRotationPoint(-3.0F, 21.0F, -4.0F);
        this.Pie_4.setTextureSize(64, 32);
        this.Pie_4.mirror = true;
        setRotationAngle(this.Pie_4, 0.0F, -0.1047198F, 0.0F);
        (this.Brazo_1 = new ModelRenderer(this, 23, 0)).addBox(0.0F, 0.0F, 0.0F, 1, 4, 1);
        this.Brazo_1.setRotationPoint(1.8F, 8.0F, -4.0F);
        this.Brazo_1.setTextureSize(64, 32);
        this.Brazo_1.mirror = true;
        setRotationAngle(this.Brazo_1, 0.0F, 0.0F, -0.7853982F);
        (this.Pie_2 = new ModelRenderer(this, 0, 0)).addBox(0.0F, 0.0F, 0.0F, 3, 2, 6);
        this.Pie_2.setRotationPoint(-3.0F, 22.0F, -4.0F);
        this.Pie_2.setTextureSize(64, 32);
        this.Pie_2.mirror = true;
        setRotationAngle(this.Pie_2, 0.0F, -0.1047198F, 0.0F);
        (this.Pantalon_2 = new ModelRenderer(this, 0, 13)).addBox(0.0F, 0.0F, 0.0F, 2, 2, 2);
        this.Pantalon_2.setRotationPoint(-2.8F, 15.0F, -4.0F);
        this.Pantalon_2.setTextureSize(64, 32);
        this.Pantalon_2.mirror = true;
        setRotationAngle(this.Pantalon_2, 0.0F, 0.0F, 0.0F);
        (this.Pantalon_3 = new ModelRenderer(this, 0, 10)).addBox(0.0F, 0.0F, 0.0F, 6, 3, 4);
        this.Pantalon_3.setRotationPoint(-3.0F, 12.0F, -5.0F);
        this.Pantalon_3.setTextureSize(64, 32);
        this.Pantalon_3.mirror = true;
        setRotationAngle(this.Pantalon_3, 0.0F, 0.0F, 0.0F);
        (this.Brazo_2 = new ModelRenderer(this, 23, 0)).addBox(0.0F, 0.0F, 0.0F, 1, 4, 1);
        this.Brazo_2.setRotationPoint(4.0F, 10.0F, -4.0F);
        this.Brazo_2.setTextureSize(64, 32);
        this.Brazo_2.mirror = true;
        setRotationAngle(this.Brazo_2, 0.0F, 0.0F, -0.0174533F);
        (this.Brazo_3 = new ModelRenderer(this, 23, 0)).addBox(0.0F, 0.0F, 0.0F, 1, 4, 1);
        this.Brazo_3.setRotationPoint(-2.0F, 8.0F, -4.0F);
        this.Brazo_3.setTextureSize(64, 32);
        this.Brazo_3.mirror = true;
        setRotationAngle(this.Brazo_3, 0.0F, 0.0F, 0.9773844F);
        (this.Brazo_4 = new ModelRenderer(this, 23, 0)).addBox(0.0F, 0.0F, 0.0F, 1, 4, 1);
        this.Brazo_4.setRotationPoint(-5.0F, 10.0F, -4.0F);
        this.Brazo_4.setTextureSize(64, 32);
        this.Brazo_4.mirror = true;
        setRotationAngle(this.Brazo_4, 0.0F, 0.0F, 0.0698132F);
        (this.Guante_1 = new ModelRenderer(this, 71, 0)).addBox(0.0F, 0.0F, 0.0F, 3, 1, 3);
        this.Guante_1.setRotationPoint(3.2F, 14.0F, -5.0F);
        this.Guante_1.setTextureSize(64, 32);
        this.Guante_1.mirror = true;
        setRotationAngle(this.Guante_1, 0.0F, 0.0F, 0.0F);
        (this.Guante_2 = new ModelRenderer(this, 71, 0)).addBox(0.0F, 0.0F, 0.0F, 3, 1, 3);
        this.Guante_2.setRotationPoint(-6.4F, 14.0F, -5.0F);
        this.Guante_2.setTextureSize(64, 32);
        this.Guante_2.mirror = true;
        setRotationAngle(this.Guante_2, 0.0F, 0.0F, 0.0F);
        (this.Mano_1 = new ModelRenderer(this, 61, 27)).addBox(0.0F, 0.0F, 0.0F, 3, 3, 2);
        this.Mano_1.setRotationPoint(2.866667F, 15.0F, -4.0F);
        this.Mano_1.setTextureSize(64, 32);
        this.Mano_1.mirror = true;
        setRotationAngle(this.Mano_1, 0.0F, 0.3839724F, 0.0F);
        (this.Mano_2 = new ModelRenderer(this, 61, 33)).addBox(0.0F, 0.0F, 0.0F, 3, 3, 2);
        this.Mano_2.setRotationPoint(-6.0F, 15.0F, -5.0F);
        this.Mano_2.setTextureSize(64, 32);
        this.Mano_2.mirror = true;
        setRotationAngle(this.Mano_2, 0.0F, -0.3839724F, 0.0F);
        this.textureWidth = 100;
        this.textureHeight = 80;
        (this.footRight = new ModelRenderer(this, 22, 39)).setRotationPoint(0.0F, 8.0F, 0.0F);
        this.footRight.addBox(-2.5F, 0.0F, -6.0F, 5, 3, 8, 0.0F);
        setRotationAngle(this.footRight, -0.034906585F, 0.0F, 0.0F);
        (this.earRight = new ModelRenderer(this, 8, 0)).setRotationPoint(-4.5F, -5.5F, 0.0F);
        this.earRight.addBox(-1.0F, -3.0F, -0.5F, 2, 3, 1, 0.0F);
        setRotationAngle(this.earRight, 0.05235988F, 0.0F, -1.0471976F);
        (this.legLeftpad = new ModelRenderer(this, 48, 39)).setRotationPoint(0.0F, 0.5F, 0.0F);
        this.legLeftpad.addBox(-3.0F, 0.0F, -3.0F, 6, 9, 6, 0.0F);
        (this.earRightpad_1 = new ModelRenderer(this, 40, 39)).setRotationPoint(0.0F, -1.0F, 0.0F);
        this.earRightpad_1.addBox(-2.0F, -5.0F, -1.0F, 4, 4, 2, 0.0F);
        (this.legLeft = new ModelRenderer(this, 54, 10)).setRotationPoint(3.3F, 12.5F, 0.0F);
        this.legLeft.addBox(-1.0F, 0.0F, -1.0F, 2, 10, 2, 0.0F);
        (this.armRightpad2 = new ModelRenderer(this, 0, 26)).setRotationPoint(0.0F, 0.5F, 0.0F);
        this.armRightpad2.addBox(-2.5F, 0.0F, -2.5F, 5, 7, 5, 0.0F);
        (this.handLeft = new ModelRenderer(this, 58, 56)).setRotationPoint(0.0F, 8.0F, 0.0F);
        this.handLeft.addBox(-2.0F, 0.0F, -2.5F, 4, 4, 5, 0.0F);
        setRotationAngle(this.handLeft, 0.0F, 0.0F, 0.05235988F);
        (this.armLeft = new ModelRenderer(this, 62, 10)).setRotationPoint(6.5F, -8.0F, 0.0F);
        this.armLeft.addBox(-1.0F, 0.0F, -1.0F, 2, 10, 2, 0.0F);
        setRotationAngle(this.armLeft, 0.0F, 0.0F, -0.2617994F);
        (this.legRight = new ModelRenderer(this, 90, 8)).setRotationPoint(-3.3F, 12.5F, 0.0F);
        this.legRight.addBox(-1.0F, 0.0F, -1.0F, 2, 10, 2, 0.0F);
        (this.armLeft2 = new ModelRenderer(this, 90, 48)).setRotationPoint(0.0F, 9.6F, 0.0F);
        this.armLeft2.addBox(-1.0F, 0.0F, -1.0F, 2, 8, 2, 0.0F);
        setRotationAngle(this.armLeft2, -0.17453292F, 0.0F, 0.0F);
        (this.legRight2 = new ModelRenderer(this, 20, 35)).setRotationPoint(0.0F, 9.6F, 0.0F);
        this.legRight2.addBox(-1.0F, 0.0F, -1.0F, 2, 8, 2, 0.0F);
        setRotationAngle(this.legRight2, 0.034906585F, 0.0F, 0.0F);
        (this.armLeftpad2 = new ModelRenderer(this, 0, 58)).setRotationPoint(0.0F, 0.5F, 0.0F);
        this.armLeftpad2.addBox(-2.5F, 0.0F, -2.5F, 5, 7, 5, 0.0F);
        (this.legLeft2 = new ModelRenderer(this, 72, 48)).setRotationPoint(0.0F, 9.6F, 0.0F);
        this.legLeft2.addBox(-1.0F, 0.0F, -1.0F, 2, 8, 2, 0.0F);
        setRotationAngle(this.legLeft2, 0.034906585F, 0.0F, 0.0F);
        (this.hat = new ModelRenderer(this, 70, 24)).setRotationPoint(0.0F, -8.4F, 0.0F);
        this.hat.addBox(-3.0F, -0.5F, -3.0F, 6, 1, 6, 0.0F);
        setRotationAngle(this.hat, -0.017453292F, 0.0F, 0.0F);
        (this.earRightpad = new ModelRenderer(this, 85, 0)).setRotationPoint(0.0F, -1.0F, 0.0F);
        this.earRightpad.addBox(-2.0F, -5.0F, -1.0F, 4, 4, 2, 0.0F);
        (this.crotch = new ModelRenderer(this, 56, 0)).setRotationPoint(0.0F, 9.5F, 0.0F);
        this.crotch.addBox(-5.5F, 0.0F, -3.5F, 11, 3, 7, 0.0F);
        (this.torso = new ModelRenderer(this, 8, 0)).setRotationPoint(0.0F, 0.0F, 0.0F);
        this.torso.addBox(-6.0F, -9.0F, -4.0F, 12, 18, 8, 0.0F);
        setRotationAngle(this.torso, 0.017453292F, 0.0F, 0.0F);
        (this.armRight2 = new ModelRenderer(this, 90, 20)).setRotationPoint(0.0F, 9.6F, 0.0F);
        this.armRight2.addBox(-1.0F, 0.0F, -1.0F, 2, 8, 2, 0.0F);
        setRotationAngle(this.armRight2, -0.17453292F, 0.0F, 0.0F);
        (this.handRight = new ModelRenderer(this, 20, 26)).setRotationPoint(0.0F, 8.0F, 0.0F);
        this.handRight.addBox(-2.0F, 0.0F, -2.5F, 4, 4, 5, 0.0F);
        setRotationAngle(this.handRight, 0.0F, 0.0F, -0.05235988F);
        (this.fredbody = new ModelRenderer(this, 0, 0)).setRotationPoint(0.0F, -9.0F, 0.0F);
        this.fredbody.addBox(-1.0F, -14.0F, -1.0F, 2, 24, 2, 0.0F);
        (this.fredhead = new ModelRenderer(this, 39, 22)).setRotationPoint(0.0F, -13.0F, -0.5F);
        this.fredhead.addBox(-5.5F, -8.0F, -4.5F, 11, 8, 9, 0.0F);
        (this.legRightpad = new ModelRenderer(this, 73, 33)).setRotationPoint(0.0F, 0.5F, 0.0F);
        this.legRightpad.addBox(-3.0F, 0.0F, -3.0F, 6, 9, 6, 0.0F);
        (this.frednose = new ModelRenderer(this, 17, 67)).setRotationPoint(0.0F, -2.0F, -4.5F);
        this.frednose.addBox(-4.0F, -2.0F, -3.0F, 8, 4, 3, 0.0F);
        (this.legLeftpad2 = new ModelRenderer(this, 16, 50)).setRotationPoint(0.0F, 0.5F, 0.0F);
        this.legLeftpad2.addBox(-2.5F, 0.0F, -3.0F, 5, 7, 6, 0.0F);
        (this.armRightpad = new ModelRenderer(this, 70, 10)).setRotationPoint(0.0F, 0.5F, 0.0F);
        this.armRightpad.addBox(-2.5F, 0.0F, -2.5F, 5, 9, 5, 0.0F);
        (this.armLeftpad = new ModelRenderer(this, 38, 54)).setRotationPoint(0.0F, 0.5F, 0.0F);
        this.armLeftpad.addBox(-2.5F, 0.0F, -2.5F, 5, 9, 5, 0.0F);
        (this.hat2 = new ModelRenderer(this, 78, 61)).setRotationPoint(0.0F, 0.1F, 0.0F);
        this.hat2.addBox(-2.0F, -4.0F, -2.0F, 4, 4, 4, 0.0F);
        setRotationAngle(this.hat2, -0.017453292F, 0.0F, 0.0F);
        (this.legRightpad2 = new ModelRenderer(this, 0, 39)).setRotationPoint(0.0F, 0.5F, 0.0F);
        this.legRightpad2.addBox(-2.5F, 0.0F, -3.0F, 5, 7, 6, 0.0F);
        (this.jaw = new ModelRenderer(this, 49, 65)).setRotationPoint(0.0F, 0.5F, 0.0F);
        this.jaw.addBox(-5.0F, 0.0F, -4.5F, 10, 3, 9, 0.0F);
        setRotationAngle(this.jaw, 0.08726646F, 0.0F, 0.0F);
        (this.armRight = new ModelRenderer(this, 48, 0)).setRotationPoint(-6.5F, -8.0F, 0.0F);
        this.armRight.addBox(-1.0F, 0.0F, -1.0F, 2, 10, 2, 0.0F);
        setRotationAngle(this.armRight, 0.0F, 0.0F, 0.2617994F);
        (this.footLeft = new ModelRenderer(this, 72, 50)).setRotationPoint(0.0F, 8.0F, 0.0F);
        this.footLeft.addBox(-2.5F, 0.0F, -6.0F, 5, 3, 8, 0.0F);
        setRotationAngle(this.footLeft, -0.034906585F, 0.0F, 0.0F);
        (this.earLeft = new ModelRenderer(this, 40, 0)).setRotationPoint(4.5F, -5.5F, 0.0F);
        this.earLeft.addBox(-1.0F, -3.0F, -0.5F, 2, 3, 1, 0.0F);
        setRotationAngle(this.earLeft, 0.05235988F, 0.0F, 1.0471976F);
        this.legRight2.addChild(this.footRight);
        this.fredhead.addChild(this.earRight);
        this.legLeft.addChild(this.legLeftpad);
        this.earLeft.addChild(this.earRightpad_1);
        this.fredbody.addChild(this.legLeft);
        this.armRight2.addChild(this.armRightpad2);
        this.armLeft2.addChild(this.handLeft);
        this.fredbody.addChild(this.armLeft);
        this.fredbody.addChild(this.legRight);
        this.armLeft.addChild(this.armLeft2);
        this.legRight.addChild(this.legRight2);
        this.armLeft2.addChild(this.armLeftpad2);
        this.legLeft.addChild(this.legLeft2);
        this.fredhead.addChild(this.hat);
        this.earRight.addChild(this.earRightpad);
        this.fredbody.addChild(this.crotch);
        this.fredbody.addChild(this.torso);
        this.armRight.addChild(this.armRight2);
        this.armRight2.addChild(this.handRight);
        this.fredbody.addChild(this.fredhead);
        this.legRight.addChild(this.legRightpad);
        this.fredhead.addChild(this.frednose);
        this.legLeft2.addChild(this.legLeftpad2);
        this.armRight.addChild(this.armRightpad);
        this.armLeft.addChild(this.armLeftpad);
        this.hat.addChild(this.hat2);
        this.legRight2.addChild(this.legRightpad2);
        this.fredhead.addChild(this.jaw);
        this.fredbody.addChild(this.armRight);
        this.legLeft2.addChild(this.footLeft);
        this.fredhead.addChild(this.earLeft);
    }

    public void render(Entity entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
        GlStateManager.pushMatrix();
            super.render(entityIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
            if (this.isChild) {
                float f = 2.0F;
                GlStateManager.scale(0.5F, 0.5F, 0.5F);
                GlStateManager.translate(0.0F, 24.0F * scale, 0.0F);
                this.bipedLeftLegwear.render(scale);
                this.bipedRightLegwear.render(scale);
                this.bipedLeftArmwear.render(scale);
                this.bipedRightArmwear.render(scale);
                this.bipedBodyWear.render(scale);
            } else {
                if (entityIn.isSneaking())
                    GlStateManager.translate(0.0F, 0.2F, 0.0F);
                this.bipedLeftLegwear.render(scale);
                this.bipedRightLegwear.render(scale);
                this.bipedLeftArmwear.render(scale);
                this.bipedRightArmwear.render(scale);
                this.bipedBodyWear.render(scale);
            }
        GlStateManager.popMatrix();
    }

    public void setRotationAngle(ModelRenderer modelRenderer, float x, float y, float z) {
        modelRenderer.rotateAngleX = x;
        modelRenderer.rotateAngleY = y;
        modelRenderer.rotateAngleZ = z;
    }

    public void renderDeadmau5Head(float scale) {
        copyModelAngles(this.bipedHead, this.bipedDeadmau5Head);
        this.bipedDeadmau5Head.rotationPointX = 0.0F;
        this.bipedDeadmau5Head.rotationPointY = 0.0F;
        this.bipedDeadmau5Head.render(scale);
    }

    public void renderCape(float scale) {
        if (!Celestial.instance.featureManager.getFeature(CustomModel.class).isEnabled())
            this.bipedCape.render(scale);
    }

    public void setRotationAngles(float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scaleFactor, Entity entityIn) {
        super.setRotationAngles(limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scaleFactor, entityIn);
        copyModelAngles(this.bipedLeftLeg, this.bipedLeftLegwear);
        copyModelAngles(this.bipedRightLeg, this.bipedRightLegwear);
        copyModelAngles(this.bipedLeftArm, this.bipedLeftArmwear);
        copyModelAngles(this.bipedRightArm, this.bipedRightArmwear);
        copyModelAngles(this.bipedBody, this.bipedBodyWear);
    }

    public void setInvisible(boolean invisible) {
        super.setInvisible(invisible);
        this.bipedLeftArmwear.showModel = invisible;
        this.bipedRightArmwear.showModel = invisible;
        this.bipedLeftLegwear.showModel = invisible;
        this.bipedRightLegwear.showModel = invisible;
        this.bipedBodyWear.showModel = invisible;
        this.bipedCape.showModel = invisible;
        this.bipedDeadmau5Head.showModel = invisible;
    }

    @Override
    public void postRenderArm(float scale, EnumHandSide side) {
        ModelRenderer modelrenderer = this.getArmForSide(side);
        if (this.smallArms) {
            float f = 0.5f * (float) (side == EnumHandSide.RIGHT ? 1 : -1);
            modelrenderer.rotationPointX += f;
            modelrenderer.postRender(scale);
            modelrenderer.rotationPointX -= f;
        } else {
            modelrenderer.postRender(scale);
        }
    }

    protected float rotlerpRad(float p_205060_1_, float p_205060_2_, float p_205060_3_) {
        float f = (p_205060_2_ - p_205060_1_) % 6.2831855F;
        if (f < -3.1415927F)
            f += 6.2831855F;
        if (f >= 3.1415927F)
            f -= 6.2831855F;
        return p_205060_1_ + p_205060_3_ * f;
    }
}
