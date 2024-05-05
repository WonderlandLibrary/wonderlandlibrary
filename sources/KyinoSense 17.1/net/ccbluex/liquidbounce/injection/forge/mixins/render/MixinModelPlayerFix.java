/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.model.ModelBase
 *  net.minecraft.client.model.ModelBiped
 *  net.minecraft.client.model.ModelBox
 *  net.minecraft.client.model.ModelPlayer
 *  net.minecraft.client.model.ModelRenderer
 *  net.minecraft.client.renderer.GlStateManager
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.player.EntityPlayer
 */
package net.ccbluex.liquidbounce.injection.forge.mixins.render;

import me.report.liquidware.modules.render.CustomModel;
import net.ccbluex.liquidbounce.LiquidBounce;
import net.ccbluex.liquidbounce.event.UpdateModelEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.model.ModelBox;
import net.minecraft.client.model.ModelPlayer;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value={ModelPlayer.class})
public class MixinModelPlayerFix
extends ModelBiped {
    public ModelRenderer left_leg;
    public ModelRenderer right_leg;
    public ModelRenderer body;
    public ModelRenderer eye;
    public ModelRenderer rabbitBone;
    public ModelRenderer rabbitRleg;
    public ModelRenderer rabbitLarm;
    public ModelRenderer rabbitRarm;
    public ModelRenderer rabbitLleg;
    public ModelRenderer rabbitHead;
    public ModelRenderer fredhead;
    public ModelRenderer armLeft;
    public ModelRenderer legRight;
    public ModelRenderer legLeft;
    public ModelRenderer armRight;
    public ModelRenderer fredbody;
    public ModelRenderer armLeftpad2;
    public ModelRenderer torso;
    public ModelRenderer earRightpad_1;
    public ModelRenderer armRightpad2;
    public ModelRenderer legLeftpad;
    public ModelRenderer hat;
    public ModelRenderer legLeftpad2;
    public ModelRenderer armRight2;
    public ModelRenderer legRight2;
    public ModelRenderer earRightpad;
    public ModelRenderer armLeft2;
    public ModelRenderer frednose;
    public ModelRenderer earLeft;
    public ModelRenderer footRight;
    public ModelRenderer legRightpad2;
    public ModelRenderer legRightpad;
    public ModelRenderer armLeftpad;
    public ModelRenderer legLeft2;
    public ModelRenderer footLeft;
    public ModelRenderer hat2;
    public ModelRenderer armRightpad;
    public ModelRenderer earRight;
    public ModelRenderer crotch;
    public ModelRenderer jaw;
    public ModelRenderer handRight;
    public ModelRenderer handLeft;
    @Shadow
    private boolean field_178735_y;
    @Shadow
    public ModelRenderer field_178734_a;
    @Shadow
    public ModelRenderer field_178732_b;
    @Shadow
    public ModelRenderer field_178733_c;
    @Shadow
    public ModelRenderer field_178731_d;
    @Shadow
    public ModelRenderer field_178730_v;

    @ModifyConstant(method={"<init>"}, constant={@Constant(floatValue=2.5f)})
    private float fixAlexArmHeight(float original) {
        return 2.0f;
    }

    @Overwrite
    public void func_178718_a(float scale) {
        if (this.field_178735_y) {
            this.field_178723_h.field_78800_c += 0.5f;
            this.field_178723_h.func_78794_c(scale);
            this.field_178723_h.field_78798_e -= 0.5f;
        } else {
            this.field_178723_h.func_78794_c(scale);
        }
    }

    @Inject(method={"render"}, at={@At(value="HEAD")}, cancellable=true)
    public void renderHook(Entity entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scale, CallbackInfo ci) {
        CustomModel customModel = (CustomModel)LiquidBounce.moduleManager.getModule(CustomModel.class);
        if (customModel.getState()) {
            ci.cancel();
            this.renderCustom(entityIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
        }
    }

    public void setRotationAngle(ModelRenderer modelRenderer, float x, float y, float z) {
        modelRenderer.field_78795_f = x;
        modelRenderer.field_78796_g = y;
        modelRenderer.field_78808_h = z;
    }

    public void generatemodel() {
        ModelRenderer earLeft;
        ModelRenderer footLeft;
        ModelRenderer armRight3;
        ModelRenderer jaw;
        ModelRenderer legRightpad2;
        ModelRenderer hat2;
        ModelRenderer armLeftpad3;
        ModelRenderer armRightpad3;
        ModelRenderer legLeftpad2;
        ModelRenderer frednose;
        ModelRenderer legRightpad;
        ModelRenderer fredhead;
        ModelRenderer fredbody;
        ModelRenderer handRight;
        ModelRenderer armRight2;
        ModelRenderer torso;
        ModelRenderer crotch;
        ModelRenderer earRightpad;
        ModelRenderer hat;
        ModelRenderer legLeft2;
        ModelRenderer armLeftpad2;
        ModelRenderer legRight2;
        ModelRenderer armLeft2;
        ModelRenderer legRight;
        ModelRenderer armLeft;
        ModelRenderer handLeft;
        ModelRenderer armRightpad2;
        ModelRenderer legLeft;
        ModelRenderer earRightpad_1;
        ModelRenderer legLeftpad;
        ModelRenderer earRight;
        ModelRenderer footRight;
        this.body = new ModelRenderer((ModelBase)this);
        this.body.func_78793_a(0.0f, 0.0f, 0.0f);
        this.body.func_78784_a(34, 8).func_78789_a(-4.0f, 6.0f, -3.0f, 8, 12, 6);
        this.body.func_78784_a(15, 10).func_78789_a(-3.0f, 9.0f, 3.0f, 6, 8, 3);
        this.body.func_78784_a(26, 0).func_78789_a(-3.0f, 5.0f, -3.0f, 6, 1, 6);
        this.eye = new ModelRenderer((ModelBase)this);
        this.eye.func_78784_a(0, 10).func_78789_a(-3.0f, 7.0f, -4.0f, 6, 4, 1);
        this.left_leg = new ModelRenderer((ModelBase)this);
        this.left_leg.func_78793_a(-2.0f, 18.0f, 0.0f);
        this.left_leg.func_78784_a(0, 0).func_78790_a(2.9f, 0.0f, -1.5f, 3, 6, 3, 0.0f);
        this.right_leg = new ModelRenderer((ModelBase)this);
        this.right_leg.func_78793_a(2.0f, 18.0f, 0.0f);
        this.right_leg.func_78784_a(13, 0).func_78789_a(-5.9f, 0.0f, -1.5f, 3, 6, 3);
        this.rabbitBone = new ModelRenderer((ModelBase)this);
        this.rabbitBone.func_78793_a(0.0f, 24.0f, 0.0f);
        this.rabbitBone.field_78804_l.add(new ModelBox(this.rabbitBone, 28, 45, -5.0f, -13.0f, -5.0f, 10, 11, 8, 0.0f, false));
        this.rabbitRleg = new ModelRenderer((ModelBase)this);
        this.rabbitRleg.func_78793_a(-3.0f, -2.0f, -1.0f);
        this.rabbitBone.func_78792_a(this.rabbitRleg);
        this.rabbitRleg.field_78804_l.add(new ModelBox(this.rabbitRleg, 0, 0, -2.0f, 0.0f, -2.0f, 4, 2, 4, 0.0f, false));
        this.rabbitLarm = new ModelRenderer((ModelBase)this);
        this.rabbitLarm.func_78793_a(5.0f, -13.0f, -1.0f);
        this.setRotationAngle(this.rabbitLarm, 0.0f, 0.0f, -0.0873f);
        this.rabbitBone.func_78792_a(this.rabbitLarm);
        this.rabbitLarm.field_78804_l.add(new ModelBox(this.rabbitLarm, 0, 0, 0.0f, 0.0f, -2.0f, 2, 8, 4, 0.0f, false));
        this.rabbitRarm = new ModelRenderer((ModelBase)this);
        this.rabbitRarm.func_78793_a(-5.0f, -13.0f, -1.0f);
        this.setRotationAngle(this.rabbitRarm, 0.0f, 0.0f, 0.0873f);
        this.rabbitBone.func_78792_a(this.rabbitRarm);
        this.rabbitRarm.field_78804_l.add(new ModelBox(this.rabbitRarm, 0, 0, -2.0f, 0.0f, -2.0f, 2, 8, 4, 0.0f, false));
        this.rabbitLleg = new ModelRenderer((ModelBase)this);
        this.rabbitLleg.func_78793_a(3.0f, -2.0f, -1.0f);
        this.rabbitBone.func_78792_a(this.rabbitLleg);
        this.rabbitLleg.field_78804_l.add(new ModelBox(this.rabbitLleg, 0, 0, -2.0f, 0.0f, -2.0f, 4, 2, 4, 0.0f, false));
        this.rabbitHead = new ModelRenderer((ModelBase)this);
        this.rabbitHead.func_78793_a(0.0f, -14.0f, -1.0f);
        this.rabbitBone.func_78792_a(this.rabbitHead);
        this.rabbitHead.field_78804_l.add(new ModelBox(this.rabbitHead, 0, 0, -3.0f, 0.0f, -4.0f, 6, 1, 6, 0.0f, false));
        this.rabbitHead.field_78804_l.add(new ModelBox(this.rabbitHead, 56, 0, -5.0f, -9.0f, -5.0f, 2, 3, 2, 0.0f, false));
        this.rabbitHead.field_78804_l.add(new ModelBox(this.rabbitHead, 56, 0, 3.0f, -9.0f, -5.0f, 2, 3, 2, 0.0f, true));
        this.rabbitHead.field_78804_l.add(new ModelBox(this.rabbitHead, 0, 45, -4.0f, -11.0f, -4.0f, 8, 11, 8, 0.0f, false));
        this.rabbitHead.field_78804_l.add(new ModelBox(this.rabbitHead, 46, 0, 1.0f, -20.0f, 0.0f, 3, 9, 1, 0.0f, false));
        this.rabbitHead.field_78804_l.add(new ModelBox(this.rabbitHead, 46, 0, -4.0f, -20.0f, 0.0f, 3, 9, 1, 0.0f, false));
        this.field_78090_t = 100;
        this.field_78089_u = 80;
        this.footRight = footRight = new ModelRenderer((ModelBase)this, 22, 39);
        footRight.func_78793_a(0.0f, 8.0f, 0.0f);
        this.footRight.func_78790_a(-2.5f, 0.0f, -6.0f, 5, 3, 8, 0.0f);
        this.setRotationAngle(this.footRight, (float)(-Math.PI) / 90, 0.0f, 0.0f);
        this.earRight = earRight = new ModelRenderer((ModelBase)this, 8, 0);
        earRight.func_78793_a(-4.5f, -5.5f, 0.0f);
        this.earRight.func_78790_a(-1.0f, -3.0f, -0.5f, 2, 3, 1, 0.0f);
        this.setRotationAngle(this.earRight, 0.05235988f, 0.0f, -1.0471976f);
        this.legLeftpad = legLeftpad = new ModelRenderer((ModelBase)this, 48, 39);
        legLeftpad.func_78793_a(0.0f, 0.5f, 0.0f);
        this.legLeftpad.func_78790_a(-3.0f, 0.0f, -3.0f, 6, 9, 6, 0.0f);
        this.earRightpad_1 = earRightpad_1 = new ModelRenderer((ModelBase)this, 40, 39);
        earRightpad_1.func_78793_a(0.0f, -1.0f, 0.0f);
        this.earRightpad_1.func_78790_a(-2.0f, -5.0f, -1.0f, 4, 4, 2, 0.0f);
        this.legLeft = legLeft = new ModelRenderer((ModelBase)this, 54, 10);
        legLeft.func_78793_a(3.3f, 12.5f, 0.0f);
        this.legLeft.func_78790_a(-1.0f, 0.0f, -1.0f, 2, 10, 2, 0.0f);
        this.armRightpad2 = armRightpad2 = new ModelRenderer((ModelBase)this, 0, 26);
        armRightpad2.func_78793_a(0.0f, 0.5f, 0.0f);
        this.armRightpad2.func_78790_a(-2.5f, 0.0f, -2.5f, 5, 7, 5, 0.0f);
        this.handLeft = handLeft = new ModelRenderer((ModelBase)this, 58, 56);
        handLeft.func_78793_a(0.0f, 8.0f, 0.0f);
        this.handLeft.func_78790_a(-2.0f, 0.0f, -2.5f, 4, 4, 5, 0.0f);
        this.setRotationAngle(this.handLeft, 0.0f, 0.0f, 0.05235988f);
        this.armLeft = armLeft = new ModelRenderer((ModelBase)this, 62, 10);
        armLeft.func_78793_a(6.5f, -8.0f, 0.0f);
        this.armLeft.func_78790_a(-1.0f, 0.0f, -1.0f, 2, 10, 2, 0.0f);
        this.setRotationAngle(this.armLeft, 0.0f, 0.0f, -0.2617994f);
        this.legRight = legRight = new ModelRenderer((ModelBase)this, 90, 8);
        legRight.func_78793_a(-3.3f, 12.5f, 0.0f);
        this.legRight.func_78790_a(-1.0f, 0.0f, -1.0f, 2, 10, 2, 0.0f);
        this.armLeft2 = armLeft2 = new ModelRenderer((ModelBase)this, 90, 48);
        armLeft2.func_78793_a(0.0f, 9.6f, 0.0f);
        this.armLeft2.func_78790_a(-1.0f, 0.0f, -1.0f, 2, 8, 2, 0.0f);
        this.setRotationAngle(this.armLeft2, -0.17453292f, 0.0f, 0.0f);
        this.legRight2 = legRight2 = new ModelRenderer((ModelBase)this, 20, 35);
        legRight2.func_78793_a(0.0f, 9.6f, 0.0f);
        this.legRight2.func_78790_a(-1.0f, 0.0f, -1.0f, 2, 8, 2, 0.0f);
        this.setRotationAngle(this.legRight2, (float)Math.PI / 90, 0.0f, 0.0f);
        this.armLeftpad2 = armLeftpad2 = new ModelRenderer((ModelBase)this, 0, 58);
        armLeftpad2.func_78793_a(0.0f, 0.5f, 0.0f);
        this.armLeftpad2.func_78790_a(-2.5f, 0.0f, -2.5f, 5, 7, 5, 0.0f);
        this.legLeft2 = legLeft2 = new ModelRenderer((ModelBase)this, 72, 48);
        legLeft2.func_78793_a(0.0f, 9.6f, 0.0f);
        this.legLeft2.func_78790_a(-1.0f, 0.0f, -1.0f, 2, 8, 2, 0.0f);
        this.setRotationAngle(this.legLeft2, (float)Math.PI / 90, 0.0f, 0.0f);
        this.hat = hat = new ModelRenderer((ModelBase)this, 70, 24);
        hat.func_78793_a(0.0f, -8.4f, 0.0f);
        this.hat.func_78790_a(-3.0f, -0.5f, -3.0f, 6, 1, 6, 0.0f);
        this.setRotationAngle(this.hat, (float)(-Math.PI) / 180, 0.0f, 0.0f);
        this.earRightpad = earRightpad = new ModelRenderer((ModelBase)this, 85, 0);
        earRightpad.func_78793_a(0.0f, -1.0f, 0.0f);
        this.earRightpad.func_78790_a(-2.0f, -5.0f, -1.0f, 4, 4, 2, 0.0f);
        this.crotch = crotch = new ModelRenderer((ModelBase)this, 56, 0);
        crotch.func_78793_a(0.0f, 9.5f, 0.0f);
        this.crotch.func_78790_a(-5.5f, 0.0f, -3.5f, 11, 3, 7, 0.0f);
        this.torso = torso = new ModelRenderer((ModelBase)this, 8, 0);
        torso.func_78793_a(0.0f, 0.0f, 0.0f);
        this.torso.func_78790_a(-6.0f, -9.0f, -4.0f, 12, 18, 8, 0.0f);
        this.setRotationAngle(this.torso, (float)Math.PI / 180, 0.0f, 0.0f);
        this.armRight2 = armRight2 = new ModelRenderer((ModelBase)this, 90, 20);
        armRight2.func_78793_a(0.0f, 9.6f, 0.0f);
        this.armRight2.func_78790_a(-1.0f, 0.0f, -1.0f, 2, 8, 2, 0.0f);
        this.setRotationAngle(this.armRight2, -0.17453292f, 0.0f, 0.0f);
        this.handRight = handRight = new ModelRenderer((ModelBase)this, 20, 26);
        handRight.func_78793_a(0.0f, 8.0f, 0.0f);
        this.handRight.func_78790_a(-2.0f, 0.0f, -2.5f, 4, 4, 5, 0.0f);
        this.setRotationAngle(this.handRight, 0.0f, 0.0f, -0.05235988f);
        this.fredbody = fredbody = new ModelRenderer((ModelBase)this, 0, 0);
        fredbody.func_78793_a(0.0f, -9.0f, 0.0f);
        this.fredbody.func_78790_a(-1.0f, -14.0f, -1.0f, 2, 24, 2, 0.0f);
        this.fredhead = fredhead = new ModelRenderer((ModelBase)this, 39, 22);
        fredhead.func_78793_a(0.0f, -13.0f, -0.5f);
        this.fredhead.func_78790_a(-5.5f, -8.0f, -4.5f, 11, 8, 9, 0.0f);
        this.legRightpad = legRightpad = new ModelRenderer((ModelBase)this, 73, 33);
        legRightpad.func_78793_a(0.0f, 0.5f, 0.0f);
        this.legRightpad.func_78790_a(-3.0f, 0.0f, -3.0f, 6, 9, 6, 0.0f);
        this.frednose = frednose = new ModelRenderer((ModelBase)this, 17, 67);
        frednose.func_78793_a(0.0f, -2.0f, -4.5f);
        this.frednose.func_78790_a(-4.0f, -2.0f, -3.0f, 8, 4, 3, 0.0f);
        this.legLeftpad2 = legLeftpad2 = new ModelRenderer((ModelBase)this, 16, 50);
        legLeftpad2.func_78793_a(0.0f, 0.5f, 0.0f);
        this.legLeftpad2.func_78790_a(-2.5f, 0.0f, -3.0f, 5, 7, 6, 0.0f);
        this.armRightpad = armRightpad3 = new ModelRenderer((ModelBase)this, 70, 10);
        armRightpad3.func_78793_a(0.0f, 0.5f, 0.0f);
        this.armRightpad.func_78790_a(-2.5f, 0.0f, -2.5f, 5, 9, 5, 0.0f);
        this.armLeftpad = armLeftpad3 = new ModelRenderer((ModelBase)this, 38, 54);
        armLeftpad3.func_78793_a(0.0f, 0.5f, 0.0f);
        this.armLeftpad.func_78790_a(-2.5f, 0.0f, -2.5f, 5, 9, 5, 0.0f);
        this.hat2 = hat2 = new ModelRenderer((ModelBase)this, 78, 61);
        hat2.func_78793_a(0.0f, 0.1f, 0.0f);
        this.hat2.func_78790_a(-2.0f, -4.0f, -2.0f, 4, 4, 4, 0.0f);
        this.setRotationAngle(this.hat2, (float)(-Math.PI) / 180, 0.0f, 0.0f);
        this.legRightpad2 = legRightpad2 = new ModelRenderer((ModelBase)this, 0, 39);
        legRightpad2.func_78793_a(0.0f, 0.5f, 0.0f);
        this.legRightpad2.func_78790_a(-2.5f, 0.0f, -3.0f, 5, 7, 6, 0.0f);
        this.jaw = jaw = new ModelRenderer((ModelBase)this, 49, 65);
        jaw.func_78793_a(0.0f, 0.5f, 0.0f);
        this.jaw.func_78790_a(-5.0f, 0.0f, -4.5f, 10, 3, 9, 0.0f);
        this.setRotationAngle(this.jaw, 0.08726646f, 0.0f, 0.0f);
        this.armRight = armRight3 = new ModelRenderer((ModelBase)this, 48, 0);
        armRight3.func_78793_a(-6.5f, -8.0f, 0.0f);
        this.armRight.func_78790_a(-1.0f, 0.0f, -1.0f, 2, 10, 2, 0.0f);
        this.setRotationAngle(this.armRight, 0.0f, 0.0f, 0.2617994f);
        this.footLeft = footLeft = new ModelRenderer((ModelBase)this, 72, 50);
        footLeft.func_78793_a(0.0f, 8.0f, 0.0f);
        this.footLeft.func_78790_a(-2.5f, 0.0f, -6.0f, 5, 3, 8, 0.0f);
        this.setRotationAngle(this.footLeft, (float)(-Math.PI) / 90, 0.0f, 0.0f);
        this.earLeft = earLeft = new ModelRenderer((ModelBase)this, 40, 0);
        earLeft.func_78793_a(4.5f, -5.5f, 0.0f);
        this.earLeft.func_78790_a(-1.0f, -3.0f, -0.5f, 2, 3, 1, 0.0f);
        this.setRotationAngle(this.earLeft, 0.05235988f, 0.0f, 1.0471976f);
        this.legRight2.func_78792_a(this.footRight);
        this.fredhead.func_78792_a(this.earRight);
        this.legLeft.func_78792_a(this.legLeftpad);
        this.earLeft.func_78792_a(this.earRightpad_1);
        this.fredbody.func_78792_a(this.legLeft);
        this.armRight2.func_78792_a(this.armRightpad2);
        this.armLeft2.func_78792_a(this.handLeft);
        this.fredbody.func_78792_a(this.armLeft);
        this.fredbody.func_78792_a(this.legRight);
        this.armLeft.func_78792_a(this.armLeft2);
        this.legRight.func_78792_a(this.legRight2);
        this.armLeft2.func_78792_a(this.armLeftpad2);
        this.legLeft.func_78792_a(this.legLeft2);
        this.fredhead.func_78792_a(this.hat);
        this.earRight.func_78792_a(this.earRightpad);
        this.fredbody.func_78792_a(this.crotch);
        this.fredbody.func_78792_a(this.torso);
        this.armRight.func_78792_a(this.armRight2);
        this.armRight2.func_78792_a(this.handRight);
        this.fredbody.func_78792_a(this.fredhead);
        this.legRight.func_78792_a(this.legRightpad);
        this.fredhead.func_78792_a(this.frednose);
        this.legLeft2.func_78792_a(this.legLeftpad2);
        this.armRight.func_78792_a(this.armRightpad);
        this.armLeft.func_78792_a(this.armLeftpad);
        this.hat.func_78792_a(this.hat2);
        this.legRight2.func_78792_a(this.legRightpad2);
        this.fredhead.func_78792_a(this.jaw);
        this.fredbody.func_78792_a(this.armRight);
        this.legLeft2.func_78792_a(this.footLeft);
        this.fredhead.func_78792_a(this.earLeft);
    }

    public void renderCustom(Entity entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
        if (this.left_leg == null) {
            this.generatemodel();
        }
        CustomModel customModel = (CustomModel)LiquidBounce.moduleManager.getModule(CustomModel.class);
        GlStateManager.func_179094_E();
        if (!((Boolean)customModel.getOnlySelf().getValue()).booleanValue() || entityIn == Minecraft.func_71410_x().field_71439_g) {
            if (customModel.getState() && ((String)customModel.getMode2().get()).contains("Rabbit")) {
                GlStateManager.func_179094_E();
                GlStateManager.func_179139_a((double)1.25, (double)1.25, (double)1.25);
                GlStateManager.func_179137_b((double)0.0, (double)-0.3, (double)0.0);
                this.rabbitHead.field_78795_f = this.field_78116_c.field_78795_f;
                this.rabbitHead.field_78796_g = this.field_78116_c.field_78796_g;
                this.rabbitHead.field_78808_h = this.field_78116_c.field_78808_h;
                this.rabbitLarm.field_78795_f = this.field_178724_i.field_78795_f;
                this.rabbitLarm.field_78796_g = this.field_178724_i.field_78796_g;
                this.rabbitLarm.field_78808_h = this.field_178724_i.field_78808_h;
                this.rabbitRarm.field_78795_f = this.field_178723_h.field_78795_f;
                this.rabbitRarm.field_78796_g = this.field_178723_h.field_78796_g;
                this.rabbitRarm.field_78808_h = this.field_178723_h.field_78808_h;
                this.rabbitRleg.field_78795_f = this.field_178721_j.field_78795_f;
                this.rabbitRleg.field_78796_g = this.field_178721_j.field_78796_g;
                this.rabbitRleg.field_78808_h = this.field_178721_j.field_78808_h;
                this.rabbitLleg.field_78795_f = this.field_178722_k.field_78795_f;
                this.rabbitLleg.field_78796_g = this.field_178722_k.field_78796_g;
                this.rabbitLleg.field_78808_h = this.field_178722_k.field_78808_h;
                this.rabbitBone.func_78785_a(scale);
                GlStateManager.func_179121_F();
            } else if (customModel.getState() && ((String)customModel.getMode2().get()).contains("Freddy")) {
                this.fredhead.field_78795_f = this.field_78116_c.field_78795_f;
                this.fredhead.field_78796_g = this.field_78116_c.field_78796_g;
                this.fredhead.field_78808_h = this.field_78116_c.field_78808_h;
                this.armLeft.field_78795_f = this.field_178724_i.field_78795_f;
                this.armLeft.field_78796_g = this.field_178724_i.field_78796_g;
                this.armLeft.field_78808_h = this.field_178724_i.field_78808_h;
                this.legRight.field_78795_f = this.field_178721_j.field_78795_f;
                this.legRight.field_78796_g = this.field_178721_j.field_78796_g;
                this.legRight.field_78808_h = this.field_178721_j.field_78808_h;
                this.legLeft.field_78795_f = this.field_178722_k.field_78795_f;
                this.legLeft.field_78796_g = this.field_178722_k.field_78796_g;
                this.legLeft.field_78808_h = this.field_178722_k.field_78808_h;
                this.armRight.field_78795_f = this.field_178723_h.field_78795_f;
                this.armRight.field_78796_g = this.field_178723_h.field_78796_g;
                this.armRight.field_78808_h = this.field_178723_h.field_78808_h;
                GlStateManager.func_179094_E();
                GlStateManager.func_179139_a((double)0.75, (double)0.65, (double)0.75);
                GlStateManager.func_179137_b((double)0.0, (double)0.85, (double)0.0);
                this.fredbody.func_78785_a(scale);
                GlStateManager.func_179121_F();
            }
        } else {
            super.func_78088_a(entityIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
            if (this.field_78091_s) {
                float f = 2.0f;
                GlStateManager.func_179152_a((float)0.5f, (float)0.5f, (float)0.5f);
                GlStateManager.func_179109_b((float)0.0f, (float)(24.0f * scale), (float)0.0f);
                this.field_178733_c.func_78785_a(scale);
                this.field_178731_d.func_78785_a(scale);
                this.field_178734_a.func_78785_a(scale);
                this.field_178732_b.func_78785_a(scale);
                this.field_178730_v.func_78785_a(scale);
            } else {
                if (entityIn.func_70093_af()) {
                    GlStateManager.func_179109_b((float)0.0f, (float)0.2f, (float)0.0f);
                }
                this.field_178733_c.func_78785_a(scale);
                this.field_178731_d.func_78785_a(scale);
                this.field_178734_a.func_78785_a(scale);
                this.field_178732_b.func_78785_a(scale);
                this.field_178730_v.func_78785_a(scale);
            }
        }
        GlStateManager.func_179121_F();
    }

    @Inject(method={"setRotationAngles"}, at={@At(value="RETURN")})
    private void revertSwordAnimation(float p_setRotationAngles_1_, float p_setRotationAngles_2_, float p_setRotationAngles_3_, float p_setRotationAngles_4_, float p_setRotationAngles_5_, float p_setRotationAngles_6_, Entity p_setRotationAngles_7_, CallbackInfo callbackInfo) {
        LiquidBounce.eventManager.callEvent(new UpdateModelEvent((EntityPlayer)p_setRotationAngles_7_, (ModelPlayer)this));
    }
}

