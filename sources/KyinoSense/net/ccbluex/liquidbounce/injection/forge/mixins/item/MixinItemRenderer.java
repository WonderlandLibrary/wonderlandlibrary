/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.entity.AbstractClientPlayer
 *  net.minecraft.client.entity.EntityPlayerSP
 *  net.minecraft.client.renderer.GlStateManager
 *  net.minecraft.client.renderer.ItemRenderer
 *  net.minecraft.client.renderer.RenderHelper
 *  net.minecraft.client.renderer.block.model.ItemCameraTransforms$TransformType
 *  net.minecraft.entity.EntityLivingBase
 *  net.minecraft.init.Items
 *  net.minecraft.item.EnumAction
 *  net.minecraft.item.ItemAxe
 *  net.minecraft.item.ItemBucketMilk
 *  net.minecraft.item.ItemFood
 *  net.minecraft.item.ItemMap
 *  net.minecraft.item.ItemPotion
 *  net.minecraft.item.ItemStack
 *  net.minecraft.item.ItemSword
 *  net.minecraft.util.MathHelper
 *  net.minecraftforge.fml.relauncher.Side
 *  net.minecraftforge.fml.relauncher.SideOnly
 *  org.lwjgl.opengl.GL11
 */
package net.ccbluex.liquidbounce.injection.forge.mixins.item;

import me.report.liquidware.modules.render.Animations;
import me.report.liquidware.modules.render.Camera;
import me.report.liquidware.modules.render.ItemRotate;
import net.ccbluex.liquidbounce.LiquidBounce;
import net.ccbluex.liquidbounce.features.module.modules.combat.KillAura;
import net.ccbluex.liquidbounce.features.module.modules.render.SwingAnimation;
import net.ccbluex.liquidbounce.utils.timer.MSTimer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Items;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemAxe;
import net.minecraft.item.ItemBucketMilk;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemMap;
import net.minecraft.item.ItemPotion;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.util.MathHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import obfuscator.NativeMethod;
import org.lwjgl.opengl.GL11;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@SideOnly(value=Side.CLIENT)
@Mixin(value={ItemRenderer.class})
public abstract class MixinItemRenderer {
    private static int ticks;
    int f3 = 0;
    float delay = 0.0f;
    MSTimer rotationTimer = new MSTimer();
    MSTimer rotateTimer = new MSTimer();
    @Shadow
    private float field_78451_d;
    @Shadow
    private float field_78454_c;
    @Shadow
    @Final
    private Minecraft field_78455_a;
    @Shadow
    private ItemStack field_78453_b;

    @Shadow
    protected abstract void func_178101_a(float var1, float var2);

    @Shadow
    protected abstract void func_178109_a(AbstractClientPlayer var1);

    @Shadow
    protected abstract void func_178110_a(EntityPlayerSP var1, float var2);

    @Shadow
    protected abstract void func_178097_a(AbstractClientPlayer var1, float var2, float var3, float var4);

    @Shadow
    protected abstract void func_178096_b(float var1, float var2);

    @Shadow
    protected abstract void func_178104_a(AbstractClientPlayer var1, float var2);

    @Shadow
    protected abstract void func_178103_d();

    @Shadow
    protected abstract void func_178098_a(float var1, AbstractClientPlayer var2);

    @Shadow
    protected abstract void func_178105_d(float var1);

    @Shadow
    public abstract void func_178099_a(EntityLivingBase var1, ItemStack var2, ItemCameraTransforms.TransformType var3);

    @Shadow
    protected abstract void func_178095_a(AbstractClientPlayer var1, float var2, float var3);

    private void slide(float var10, float var9) {
        GlStateManager.func_179109_b((float)0.56f, (float)-0.52f, (float)-0.71999997f);
        GlStateManager.func_179109_b((float)0.0f, (float)0.0f, (float)0.0f);
        GlStateManager.func_179114_b((float)45.0f, (float)0.0f, (float)1.0f, (float)0.0f);
        float var11 = MathHelper.func_76126_a((float)(var9 * var9 * (float)Math.PI));
        float var12 = MathHelper.func_76126_a((float)(MathHelper.func_76129_c((float)var9) * (float)Math.PI));
        GlStateManager.func_179114_b((float)(var11 * 0.0f), (float)0.0f, (float)1.0f, (float)0.0f);
        GlStateManager.func_179114_b((float)(var12 * 0.0f), (float)0.0f, (float)0.0f, (float)1.0f);
        GlStateManager.func_179114_b((float)(var12 * -40.0f), (float)1.0f, (float)0.0f, (float)0.0f);
        GlStateManager.func_179139_a((double)0.52, (double)0.52, (double)0.52);
    }

    @Overwrite
    @NativeMethod.Obfuscation(flags="+native,+tiger-black")
    public void func_78440_a(float partialTicks) {
        Animations animations = (Animations)LiquidBounce.moduleManager.getModule(Animations.class);
        float f = 1.0f - (this.field_78451_d + (this.field_78454_c - this.field_78451_d) * partialTicks);
        EntityPlayerSP abstractclientplayer = this.field_78455_a.field_71439_g;
        float f1 = abstractclientplayer.func_70678_g(partialTicks);
        GL11.glScaled((double)((Float)Animations.bobbing.get()).floatValue(), (double)((Float)Animations.bobbing.get()).floatValue(), (double)((Float)Animations.bobbing.get()).floatValue());
        float f2 = abstractclientplayer.field_70127_C + (abstractclientplayer.field_70125_A - abstractclientplayer.field_70127_C) * partialTicks;
        float f3 = abstractclientplayer.field_70126_B + (abstractclientplayer.field_70177_z - abstractclientplayer.field_70126_B) * partialTicks;
        this.func_178101_a(f2, f3);
        this.func_178109_a((AbstractClientPlayer)abstractclientplayer);
        this.func_178110_a(abstractclientplayer, partialTicks);
        GlStateManager.func_179091_B();
        GlStateManager.func_179094_E();
        float angle = (1.0f - this.field_78455_a.field_71439_g.field_70733_aJ) * 360.0f;
        if (this.field_78453_b != null) {
            boolean canBlockEverything;
            KillAura killAura = (KillAura)LiquidBounce.moduleManager.getModule(KillAura.class);
            boolean bl = canBlockEverything = LiquidBounce.moduleManager.getModule(Animations.class).getState() && (Boolean)Animations.blockEverything.get() != false && killAura.getTarget() != null && (this.field_78453_b.func_77973_b() instanceof ItemBucketMilk || this.field_78453_b.func_77973_b() instanceof ItemFood || this.field_78453_b.func_77973_b() instanceof ItemPotion || this.field_78453_b.func_77973_b() instanceof ItemAxe || this.field_78453_b.func_77973_b().equals(Items.field_151055_y));
            if (this.field_78453_b.func_77973_b() instanceof ItemMap) {
                this.func_178097_a((AbstractClientPlayer)abstractclientplayer, f2, f, f1);
            } else if (abstractclientplayer.func_71052_bv() > 0 || this.field_78453_b.func_77973_b() instanceof ItemSword && (killAura.getBlockingStatus() || killAura.getFakeBlock()) || this.field_78453_b.func_77973_b() instanceof ItemSword && LiquidBounce.moduleManager.getModule(Animations.class).getState() && ((Boolean)Animations.fakeBlock.get()).booleanValue() && killAura.getTarget() != null || canBlockEverything) {
                EnumAction enumaction = killAura.getBlockingStatus() || canBlockEverything ? EnumAction.BLOCK : this.field_78453_b.func_77975_n();
                block0 : switch (enumaction) {
                    case NONE: {
                        this.func_178096_b(f, 0.0f);
                        break;
                    }
                    case BOW: {
                        this.func_178096_b(f, f1);
                        this.func_178098_a(partialTicks, (AbstractClientPlayer)abstractclientplayer);
                    }
                    case EAT: 
                    case DRINK: {
                        this.func_178104_a((AbstractClientPlayer)abstractclientplayer, partialTicks);
                        this.func_178096_b(f, f1);
                        break;
                    }
                    case BLOCK: {
                        if (animations.getState()) {
                            float var2 = 1.0f - (this.field_78451_d + (this.field_78454_c - this.field_78451_d) * partialTicks);
                            float var4 = this.field_78455_a.field_71439_g.func_70678_g(partialTicks);
                            float var15 = MathHelper.func_76126_a((float)(MathHelper.func_76129_c((float)var4) * (float)Math.PI));
                            float lllllllllllllllllllIlIIllIllIIlI = this.field_78455_a.field_71439_g.func_70678_g(partialTicks);
                            float var777 = this.field_78455_a.field_71439_g.field_70127_C + (this.field_78455_a.field_71439_g.field_70125_A - this.field_78455_a.field_71439_g.field_70127_C) * partialTicks;
                            float var888 = this.field_78455_a.field_71439_g.field_70126_B + (this.field_78455_a.field_71439_g.field_70177_z - this.field_78455_a.field_71439_g.field_70126_B) * partialTicks;
                            switch ((String)Animations.getModeValue.get()) {
                                case "Exhibition": {
                                    float f8 = MathHelper.func_76126_a((float)(MathHelper.func_76129_c((float)this.field_78455_a.field_71439_g.func_70678_g(partialTicks)) * (float)Math.PI));
                                    this.func_178096_b(f, 0.0f);
                                    GL11.glTranslated((double)0.0, (double)0.5, (double)0.0);
                                    GL11.glRotated((double)(-f8 * 40.0f), (double)(f8 / 2.0f), (double)0.0, (double)9.0);
                                    GL11.glRotated((double)(-f8 * 55.0f), (double)0.8f, (double)(f8 / 2.0f), (double)0.0);
                                    this.func_178103_d(-0.05f);
                                    break block0;
                                }
                                case "OldExhibition": {
                                    float var9 = MathHelper.func_76126_a((float)(MathHelper.func_76129_c((float)this.field_78455_a.field_71439_g.func_70678_g(partialTicks)) * (float)Math.PI));
                                    GL11.glTranslated((double)-0.04, (double)0.13, (double)0.0);
                                    this.func_178096_b(f / 2.5f, 0.0f);
                                    GlStateManager.func_179114_b((float)(-var9 * 40.0f / 2.0f), (float)(var9 / 2.0f), (float)1.0f, (float)4.0f);
                                    GlStateManager.func_179114_b((float)(-var9 * 30.0f), (float)1.0f, (float)(var9 / 3.0f), (float)-0.0f);
                                    this.func_178103_d(0.2f);
                                    break block0;
                                }
                                case "Tifality": {
                                    float f8 = MathHelper.func_76126_a((float)(MathHelper.func_76129_c((float)this.field_78455_a.field_71439_g.func_70678_g(partialTicks)) * (float)Math.PI));
                                    this.func_178096_b(f, 0.0f);
                                    GL11.glTranslated((double)0.0, (double)0.25, (double)0.07);
                                    GL11.glRotated((double)(-f8 * 40.0f), (double)(f8 / 2.0f), (double)0.0, (double)9.0);
                                    GL11.glRotated((double)(-f8 * 50.0f), (double)0.8f, (double)(f8 / 2.0f), (double)0.0);
                                    this.func_178103_d(0.2f);
                                    break block0;
                                }
                                case "Slide2": {
                                    this.slide(0.1f, f1);
                                    this.func_178103_d22();
                                    break block0;
                                }
                                case "Swong": {
                                    this.func_178096_b(f / 2.0f, 0.0f);
                                    float Swong = MathHelper.func_76126_a((float)(MathHelper.func_76129_c((float)f1) * (float)Math.PI));
                                    GlStateManager.func_179114_b((float)(-Swong * 40.0f / 2.0f), (float)(Swong / 2.0f), (float)-0.0f, (float)9.0f);
                                    GlStateManager.func_179114_b((float)(-Swong * 30.0f), (float)1.0f, (float)(Swong / 2.0f), (float)-0.0f);
                                    this.func_178103_d();
                                    break block0;
                                }
                                case "Zoom": {
                                    this.Zoom(0.0f, f1);
                                    this.func_178103_d22();
                                    break block0;
                                }
                                case "MeMe": {
                                    float af = 1.0f - (this.field_78451_d + (this.field_78454_c - this.field_78451_d) * partialTicks);
                                    EntityPlayerSP aentityplayersp = this.field_78455_a.field_71439_g;
                                    float af1 = aentityplayersp.func_70678_g(partialTicks);
                                    GL11.glTranslated((double)-0.1f, (double)0.15f, (double)0.0);
                                    GL11.glTranslated((double)0.1f, (double)-0.2f, (double)0.0);
                                    this.avatar(af, af1);
                                    this.func_178103_d();
                                    GlStateManager.func_179109_b((float)0.11f, (float)-0.3f, (float)-0.18f);
                                    break block0;
                                }
                                case "IDK": {
                                    this.func_178096_b(f, 0.0f);
                                    this.doBlockTransformations2();
                                    float lllllllllllllllllllIlIIllIllIllI = MathHelper.func_76126_a((float)(lllllllllllllllllllIlIIllIllIIlI * lllllllllllllllllllIlIIllIllIIlI * (float)Math.PI));
                                    float lllllllllllllllllllIlIIllIllIlIl = MathHelper.func_76126_a((float)(MathHelper.func_76129_c((float)lllllllllllllllllllIlIIllIllIIlI) * (float)Math.PI));
                                    GlStateManager.func_179109_b((float)-0.0f, (float)-0.4f, (float)0.4f);
                                    GlStateManager.func_179114_b((float)(-lllllllllllllllllllIlIIllIllIlIl * 70.0f / 2.0f), (float)-8.0f, (float)-0.0f, (float)20.0f);
                                    GlStateManager.func_179114_b((float)(-lllllllllllllllllllIlIIllIllIlIl * 30.0f), (float)1.5f, (float)-0.1f, (float)-0.1f);
                                    break block0;
                                }
                                case "1.7": {
                                    this.genCustom2(f, f1);
                                    this.func_178103_d22();
                                    break block0;
                                }
                                case "Rotate": {
                                    this.Random();
                                    this.func_178103_d();
                                    break block0;
                                }
                                case "Remix": {
                                    this.func_178096_b(f, 0.83f);
                                    this.func_178103_d22();
                                    float f5 = MathHelper.func_76126_a((float)(MathHelper.func_76129_c((float)f1) * 3.83f));
                                    GlStateManager.func_179109_b((float)-0.5f, (float)0.2f, (float)0.2f);
                                    GlStateManager.func_179114_b((float)(-f5 * 0.0f), (float)0.0f, (float)0.0f, (float)0.0f);
                                    GlStateManager.func_179114_b((float)(-f5 * 43.0f), (float)58.0f, (float)23.0f, (float)45.0f);
                                    break block0;
                                }
                                case "Lucky": {
                                    this.move(-0.3f, f1);
                                    this.func_178103_d22();
                                    break block0;
                                }
                                case "Swank": {
                                    GL11.glTranslated((double)-0.1, (double)0.15, (double)0.0);
                                    this.func_178096_b(var2 / 0.15f, var4);
                                    float cnmmm = MathHelper.func_76126_a((float)(MathHelper.func_76129_c((float)f1) * (float)Math.PI));
                                    GlStateManager.func_179114_b((float)(cnmmm * 30.0f), (float)2.0f, (float)(-cnmmm), (float)9.0f);
                                    GlStateManager.func_179114_b((float)(cnmmm * 35.0f), (float)1.0f, (float)(-cnmmm), (float)-0.0f);
                                    this.func_178103_d2();
                                    break block0;
                                }
                                case "Slide": {
                                    this.Jigsaw(f, f1);
                                    this.func_178103_d2();
                                    break block0;
                                }
                                case "Push": {
                                    this.func_178096_b(f, 0.0f);
                                    this.func_178103_d2();
                                    float var15h = MathHelper.func_76126_a((float)(MathHelper.func_76129_c((float)f1) * (float)Math.PI));
                                    GlStateManager.func_179109_b((float)-0.0f, (float)0.4f, (float)0.3f);
                                    GlStateManager.func_179114_b((float)(-var15h * 35.0f), (float)-8.0f, (float)-0.0f, (float)9.0f);
                                    GlStateManager.func_179114_b((float)(-var15h * 10.0f), (float)1.0f, (float)-0.4f, (float)-0.5f);
                                    break block0;
                                }
                                case "HRotate": {
                                    this.func_178096_b(f, 0.0f);
                                    GlStateManager.func_179114_b((float)this.delay, (float)0.0f, (float)1.0f, (float)0.0f);
                                    this.func_178103_d22();
                                    if (this.rotateTimer.hasTimePassed(1L)) {
                                        this.delay += 1.0f;
                                        this.delay += ((Float)Animations.SpeedRotate.get()).floatValue();
                                        this.rotateTimer.reset();
                                    }
                                    if (!(this.delay > 360.0f)) break;
                                    this.delay = 0.0f;
                                    break block0;
                                }
                                case "Stella": {
                                    this.func_178096_b(-0.1f, f1);
                                    GlStateManager.func_179109_b((float)-0.5f, (float)0.3f, (float)-0.2f);
                                    GlStateManager.func_179114_b((float)32.0f, (float)0.0f, (float)1.0f, (float)0.0f);
                                    GlStateManager.func_179114_b((float)-70.0f, (float)1.0f, (float)0.0f, (float)0.0f);
                                    GlStateManager.func_179114_b((float)40.0f, (float)0.0f, (float)1.0f, (float)0.0f);
                                    break block0;
                                }
                                case "Flux": {
                                    GlStateManager.func_179152_a((float)((Float)Animations.Scale.get()).floatValue(), (float)((Float)Animations.Scale.get()).floatValue(), (float)((Float)Animations.Scale.get()).floatValue());
                                    this.func_178096_b(f, 0.0f);
                                    float var91 = MathHelper.func_76126_a((float)(MathHelper.func_76129_c((float)f1) * (float)Math.PI));
                                    this.func_178103_d(0.2f);
                                    GlStateManager.func_179109_b((float)-0.4f, (float)0.28f, (float)0.0f);
                                    GlStateManager.func_179114_b((float)(-var91 * 35.0f), (float)-8.0f, (float)-0.0f, (float)9.0f);
                                    GlStateManager.func_179114_b((float)(-var91 * 70.0f), (float)1.0f, (float)-0.4f, (float)-0.0f);
                                    GlStateManager.func_179114_b((float)angle, (float)1.0f, (float)0.0f, (float)0.0f);
                                    break block0;
                                }
                                case "Hit1": {
                                    GlStateManager.func_179114_b((float)angle, (float)1.0f, (float)0.0f, (float)0.0f);
                                    break block0;
                                }
                                case "Hit2": {
                                    GlStateManager.func_179114_b((float)angle, (float)0.0f, (float)1.0f, (float)0.0f);
                                    break block0;
                                }
                                case "Hit3": {
                                    GlStateManager.func_179114_b((float)angle, (float)0.0f, (float)0.0f, (float)1.0f);
                                }
                            }
                            break;
                        }
                        this.func_178096_b(f + 0.1f, f1);
                        this.func_178103_d();
                        GlStateManager.func_179109_b((float)-0.5f, (float)0.2f, (float)0.0f);
                    }
                }
            } else if (animations.getState()) {
                if (LiquidBounce.moduleManager.getModule(SwingAnimation.class).getState()) {
                    GlStateManager.func_179109_b((float)((Float)Animations.itemPosX.get()).floatValue(), (float)((Float)Animations.itemPosY.get()).floatValue(), (float)((Float)Animations.itemPosZ.get()).floatValue());
                    this.func_178096_b(f, f1);
                    GlStateManager.func_179152_a((float)((Float)Animations.Scale.get()).floatValue(), (float)((Float)Animations.Scale.get()).floatValue(), (float)((Float)Animations.Scale.get()).floatValue());
                } else {
                    GlStateManager.func_179109_b((float)((Float)Animations.itemPosX.get()).floatValue(), (float)((Float)Animations.itemPosY.get()).floatValue(), (float)((Float)Animations.itemPosZ.get()).floatValue());
                    this.func_178105_d(f1);
                    this.func_178096_b(f, f1);
                    GlStateManager.func_179152_a((float)((Float)Animations.Scale.get()).floatValue(), (float)((Float)Animations.Scale.get()).floatValue(), (float)((Float)Animations.Scale.get()).floatValue());
                }
            } else if (!((String)ItemRotate.swingMethod.get()).equalsIgnoreCase("Swing")) {
                this.func_178105_d(f1);
                this.func_178096_b(f, f1);
            } else if (((Boolean)ItemRotate.RotateItems.get()).booleanValue()) {
                this.func_178096_b(f, f1);
                this.ItemRotate2();
            } else if (((Boolean)ItemRotate.RotateItems.get()).booleanValue() && this.field_78455_a.field_71474_y.field_74313_G.field_74513_e) {
                this.func_178103_d();
                this.ItemRotate1();
            } else {
                this.func_178096_b(f, f1);
            }
            this.func_178099_a((EntityLivingBase)abstractclientplayer, this.field_78453_b, ItemCameraTransforms.TransformType.FIRST_PERSON);
        } else if (!abstractclientplayer.func_82150_aj()) {
            this.func_178095_a((AbstractClientPlayer)abstractclientplayer, f, f1);
        }
        GlStateManager.func_179121_F();
        GlStateManager.func_179101_C();
        RenderHelper.func_74518_a();
    }

    private void push(float idk, float idc) {
        GlStateManager.func_179109_b((float)((Float)Animations.itemPosX.get()).floatValue(), (float)((Float)Animations.itemPosY.get()).floatValue(), (float)((Float)Animations.itemPosZ.get()).floatValue());
        GlStateManager.func_179109_b((float)0.0f, (float)(idk * -0.6f), (float)0.0f);
        GlStateManager.func_179114_b((float)45.0f, (float)0.0f, (float)1.0f, (float)0.0f);
        float var3 = MathHelper.func_76126_a((float)(idc * idc * (float)Math.PI));
        float var4 = MathHelper.func_76126_a((float)(MathHelper.func_76129_c((float)idc) * (float)Math.PI));
        GlStateManager.func_179114_b((float)(var3 * -10.0f), (float)1.0f, (float)1.0f, (float)1.0f);
        GlStateManager.func_179114_b((float)(var4 * -10.0f), (float)1.0f, (float)1.0f, (float)1.0f);
        GlStateManager.func_179114_b((float)(var4 * -10.0f), (float)1.0f, (float)1.0f, (float)1.0f);
        GlStateManager.func_179152_a((float)((Float)Animations.Scale.get()).floatValue(), (float)((Float)Animations.Scale.get()).floatValue(), (float)((Float)Animations.Scale.get()).floatValue());
    }

    private void genCustom2(float p_178096_1_, float p_178096_2_) {
        GlStateManager.func_179109_b((float)((Float)Animations.itemPosX.get()).floatValue(), (float)((Float)Animations.itemPosY.get()).floatValue(), (float)((Float)Animations.itemPosZ.get()).floatValue());
        GlStateManager.func_179109_b((float)0.56f, (float)-0.52f, (float)-0.71999997f);
        GlStateManager.func_179109_b((float)0.0f, (float)(p_178096_1_ * -0.6f), (float)0.0f);
        GlStateManager.func_179114_b((float)45.0f, (float)0.0f, (float)1.0f, (float)0.0f);
        float var3 = MathHelper.func_76126_a((float)(p_178096_2_ * p_178096_2_ * (float)Math.PI));
        float var4 = MathHelper.func_76126_a((float)(MathHelper.func_76129_c((float)p_178096_2_) * (float)Math.PI));
        GlStateManager.func_179114_b((float)(var3 * -34.0f), (float)0.0f, (float)1.0f, (float)0.2f);
        GlStateManager.func_179114_b((float)(var4 * -20.7f), (float)0.2f, (float)0.1f, (float)1.0f);
        GlStateManager.func_179114_b((float)(var4 * -68.6f), (float)1.3f, (float)0.1f, (float)0.2f);
        GlStateManager.func_179152_a((float)0.4f, (float)0.4f, (float)0.4f);
    }

    private void ItemRotate2() {
        if (((String)ItemRotate.transformFirstPersonRotate.get()).equalsIgnoreCase("Rotate1")) {
            GlStateManager.func_179114_b((float)this.delay, (float)0.0f, (float)1.0f, (float)0.0f);
        }
        if (((String)ItemRotate.transformFirstPersonRotate.get()).equalsIgnoreCase("Rotate2")) {
            GlStateManager.func_179114_b((float)this.delay, (float)1.0f, (float)1.0f, (float)0.0f);
        }
        if (((String)ItemRotate.transformFirstPersonRotate.get()).equalsIgnoreCase("Custom")) {
            GlStateManager.func_179114_b((float)this.delay, (float)((Integer)ItemRotate.customRotate1.get()).floatValue(), (float)((Integer)ItemRotate.customRotate2.get()).floatValue(), (float)((Integer)ItemRotate.customRotate3.get()).floatValue());
        }
        if (this.rotateTimer.hasTimePassed(1L)) {
            this.delay += 1.0f;
            this.delay += ((Float)ItemRotate.SpeedRotate.get()).floatValue();
            this.rotateTimer.reset();
        }
        if (this.delay > 360.0f) {
            this.delay = 0.0f;
        }
    }

    private void ItemRotate1() {
        if (((String)ItemRotate.doBlockTransformationsRotate.get()).equalsIgnoreCase("Rotate1")) {
            GlStateManager.func_179114_b((float)this.delay, (float)0.0f, (float)1.0f, (float)0.0f);
        }
        if (((String)ItemRotate.doBlockTransformationsRotate.get()).equalsIgnoreCase("Rotate2")) {
            GlStateManager.func_179114_b((float)this.delay, (float)1.0f, (float)1.0f, (float)0.0f);
        }
        if (((String)ItemRotate.transformFirstPersonRotate.get()).equalsIgnoreCase("Custom")) {
            GlStateManager.func_179114_b((float)this.delay, (float)((Integer)ItemRotate.customRotate1.get()).floatValue(), (float)((Integer)ItemRotate.customRotate2.get()).floatValue(), (float)((Integer)ItemRotate.customRotate3.get()).floatValue());
        }
        if (this.rotateTimer.hasTimePassed(1L)) {
            this.delay += 1.0f;
            this.delay += ((Float)ItemRotate.SpeedRotate.get()).floatValue();
            this.rotateTimer.reset();
        }
        if (this.delay > 360.0f) {
            this.delay = 0.0f;
        }
    }

    private void Random() {
        GlStateManager.func_179137_b((double)0.7, (double)-0.4f, (double)-0.8f);
        GlStateManager.func_179114_b((float)50.0f, (float)1.0f, (float)0.0f, (float)0.0f);
        GlStateManager.func_179114_b((float)50.0f, (float)0.0f, (float)0.0f, (float)-1.0f);
        GlStateManager.func_179114_b((float)((float)(++ticks) * 0.2f * ((Integer)Animations.Speed.get()).floatValue()), (float)0.0f, (float)0.0f, (float)1.0f);
        GlStateManager.func_179114_b((float)-25.0f, (float)1.0f, (float)0.0f, (float)0.0f);
        GlStateManager.func_179139_a((double)0.4, (double)0.4, (double)0.4);
    }

    private void transformFirstPersonItem1(float equipProgress, float swingProgress) {
        GlStateManager.func_179109_b((float)((Float)Animations.itemPosX.get()).floatValue(), (float)((Float)Animations.itemPosY.get()).floatValue(), (float)((Float)Animations.itemPosZ.get()).floatValue());
        GlStateManager.func_179109_b((float)0.56f, (float)-0.52f, (float)-0.71999997f);
        GlStateManager.func_179109_b((float)0.0f, (float)(equipProgress * -0.6f), (float)0.0f);
        GlStateManager.func_179114_b((float)45.0f, (float)0.0f, (float)1.0f, (float)0.0f);
        float f = MathHelper.func_76126_a((float)(swingProgress * swingProgress * (float)Math.PI));
        float f1 = MathHelper.func_76126_a((float)(MathHelper.func_76129_c((float)swingProgress) * (float)Math.PI));
        GlStateManager.func_179114_b((float)(f * -20.0f), (float)0.0f, (float)1.0f, (float)0.0f);
        GlStateManager.func_179114_b((float)(f1 * -20.0f), (float)0.0f, (float)0.0f, (float)1.0f);
        GlStateManager.func_179114_b((float)(f1 * -80.0f), (float)1.0f, (float)0.0f, (float)0.0f);
        GlStateManager.func_179152_a((float)0.4f, (float)0.4f, (float)0.4f);
        GlStateManager.func_179152_a((float)((Float)Animations.Scale.get()).floatValue(), (float)((Float)Animations.Scale.get()).floatValue(), (float)((Float)Animations.Scale.get()).floatValue());
    }

    private void tap(float var2, float swingProgress) {
        GlStateManager.func_179109_b((float)((Float)Animations.itemPosX.get()).floatValue(), (float)((Float)Animations.itemPosY.get()).floatValue(), (float)((Float)Animations.itemPosZ.get()).floatValue());
        float smooth = swingProgress * 0.8f - swingProgress * swingProgress * 0.8f;
        GlStateManager.func_179109_b((float)0.56f, (float)-0.52f, (float)-0.71999997f);
        GlStateManager.func_179109_b((float)0.0f, (float)(var2 * -0.15f), (float)0.0f);
        GlStateManager.func_179114_b((float)45.0f, (float)0.0f, (float)1.0f, (float)0.0f);
        float var3 = MathHelper.func_76126_a((float)(swingProgress * swingProgress * (float)Math.PI));
        float var4 = MathHelper.func_76126_a((float)(MathHelper.func_76129_c((float)swingProgress) * (float)Math.PI));
        GlStateManager.func_179114_b((float)(smooth * -90.0f), (float)0.0f, (float)1.0f, (float)0.0f);
        GlStateManager.func_179152_a((float)0.37f, (float)0.37f, (float)0.37f);
        GlStateManager.func_179152_a((float)((Float)Animations.Scale.get()).floatValue(), (float)((Float)Animations.Scale.get()).floatValue(), (float)((Float)Animations.Scale.get()).floatValue());
    }

    private void func_178096_b(float p_178096_1_, float p_178096_2_) {
        GlStateManager.func_179109_b((float)((Float)Animations.itemPosX.get()).floatValue(), (float)((Float)Animations.itemPosY.get()).floatValue(), (float)((Float)Animations.itemPosZ.get()).floatValue());
        GlStateManager.func_179109_b((float)0.56f, (float)-0.52f, (float)-0.71999997f);
        GlStateManager.func_179109_b((float)0.0f, (float)(p_178096_1_ * -0.6f), (float)0.0f);
        GlStateManager.func_179114_b((float)45.0f, (float)0.0f, (float)1.0f, (float)0.0f);
        float var3 = MathHelper.func_76126_a((float)(p_178096_2_ * p_178096_2_ * (float)Math.PI));
        float var4 = MathHelper.func_76126_a((float)(MathHelper.func_76129_c((float)p_178096_2_) * (float)Math.PI));
        GlStateManager.func_179114_b((float)(var3 * -20.0f), (float)0.0f, (float)1.0f, (float)0.0f);
        GlStateManager.func_179114_b((float)(var4 * -20.0f), (float)0.0f, (float)0.0f, (float)1.0f);
        GlStateManager.func_179114_b((float)(var4 * -80.0f), (float)1.0f, (float)0.0f, (float)0.0f);
        GlStateManager.func_179152_a((float)0.4f, (float)0.4f, (float)0.4f);
        GlStateManager.func_179152_a((float)((Float)Animations.Scale.get()).floatValue(), (float)((Float)Animations.Scale.get()).floatValue(), (float)((Float)Animations.Scale.get()).floatValue());
    }

    private void func_178103_d22() {
        GlStateManager.func_179109_b((float)-0.5f, (float)0.2f, (float)0.0f);
        GlStateManager.func_179114_b((float)30.0f, (float)0.0f, (float)1.0f, (float)0.0f);
        GlStateManager.func_179114_b((float)-80.0f, (float)1.0f, (float)0.0f, (float)0.0f);
        GlStateManager.func_179114_b((float)60.0f, (float)0.0f, (float)1.0f, (float)0.0f);
    }

    private void func_178103_d(float qq) {
        GlStateManager.func_179109_b((float)-0.5f, (float)qq, (float)0.0f);
        GlStateManager.func_179114_b((float)30.0f, (float)0.0f, (float)1.0f, (float)0.0f);
        GlStateManager.func_179114_b((float)-80.0f, (float)1.0f, (float)0.0f, (float)0.0f);
        GlStateManager.func_179114_b((float)60.0f, (float)0.0f, (float)1.0f, (float)0.0f);
        GlStateManager.func_179152_a((float)((Float)Animations.Scale.get()).floatValue(), (float)((Float)Animations.Scale.get()).floatValue(), (float)((Float)Animations.Scale.get()).floatValue());
    }

    private void move(float test1, float test2) {
        GlStateManager.func_179109_b((float)0.56f, (float)-0.52f, (float)-0.71999997f);
        GlStateManager.func_179114_b((float)45.0f, (float)0.0f, (float)1.0f, (float)0.0f);
        float var30 = MathHelper.func_76126_a((float)(test2 * MathHelper.func_76129_c((float)test1) * (float)Math.PI));
        float var31 = MathHelper.func_76134_b((float)(MathHelper.func_76129_c((float)test2) * (float)Math.PI));
        float var29 = -MathHelper.func_76135_e((float)(MathHelper.func_76129_c((float)test1) * test2 * (float)Math.PI));
        GlStateManager.func_179114_b((float)(var30 * var29 * -90.0f), (float)0.0f, (float)1.0f, (float)0.0f);
        GlStateManager.func_179114_b((float)(var29 * var31 * 5.0f), (float)0.0f, (float)0.0f, (float)1.0f);
        GlStateManager.func_179114_b((float)(var31 * 5.0f), (float)1.0f, (float)0.0f, (float)0.0f);
        GlStateManager.func_179139_a((double)0.4, (double)0.4, (double)0.4);
    }

    private void func_178096_A(float p_178096_1_, float p_178096_2_) {
        GlStateManager.func_179109_b((float)((Float)Animations.itemPosX.get()).floatValue(), (float)((Float)Animations.itemPosY.get()).floatValue(), (float)((Float)Animations.itemPosZ.get()).floatValue());
        GlStateManager.func_179109_b((float)0.62f, (float)-0.66f, (float)-0.71999997f);
        GlStateManager.func_179109_b((float)0.0f, (float)(p_178096_1_ * -0.6f), (float)0.0f);
        GlStateManager.func_179114_b((float)45.0f, (float)0.0f, (float)1.0f, (float)0.0f);
        float var3 = MathHelper.func_76126_a((float)(p_178096_2_ * p_178096_2_ * (float)Math.PI));
        float var4 = MathHelper.func_76126_a((float)(MathHelper.func_76129_c((float)p_178096_2_) * (float)Math.PI));
        GlStateManager.func_179114_b((float)(var3 * -20.0f), (float)0.0f, (float)1.0f, (float)0.0f);
        GlStateManager.func_179114_b((float)(var4 * -20.0f), (float)0.0f, (float)0.0f, (float)1.0f);
        GlStateManager.func_179114_b((float)(var4 * -80.0f), (float)1.0f, (float)0.0f, (float)0.0f);
        GlStateManager.func_179152_a((float)0.4f, (float)0.4f, (float)0.4f);
        GlStateManager.func_179152_a((float)((Float)Animations.Scale.get()).floatValue(), (float)((Float)Animations.Scale.get()).floatValue(), (float)((Float)Animations.Scale.get()).floatValue());
    }

    private void Jigsaw(float var2, float swingProgress) {
        GlStateManager.func_179109_b((float)((Float)Animations.itemPosX.get()).floatValue(), (float)((Float)Animations.itemPosY.get()).floatValue(), (float)((Float)Animations.itemPosZ.get()).floatValue());
        float smooth = swingProgress * 0.8f - swingProgress * swingProgress * 0.8f;
        GlStateManager.func_179109_b((float)0.56f, (float)-0.52f, (float)-0.71999997f);
        GlStateManager.func_179109_b((float)0.0f, (float)(var2 * -0.6f), (float)0.0f);
        GlStateManager.func_179114_b((float)45.0f, (float)0.0f, (float)(2.0f + smooth * 0.5f), (float)(smooth * 3.0f));
        float var3 = MathHelper.func_76126_a((float)(swingProgress * swingProgress * (float)Math.PI));
        float var4 = MathHelper.func_76126_a((float)(MathHelper.func_76129_c((float)swingProgress) * (float)Math.PI));
        GlStateManager.func_179114_b((float)0.0f, (float)0.0f, (float)1.0f, (float)0.0f);
        GlStateManager.func_179152_a((float)0.37f, (float)0.37f, (float)0.37f);
        GlStateManager.func_179152_a((float)((Float)Animations.Scale.get()).floatValue(), (float)((Float)Animations.Scale.get()).floatValue(), (float)((Float)Animations.Scale.get()).floatValue());
    }

    private void doBlockTransformations2() {
        GlStateManager.func_179109_b((float)((Float)Animations.itemPosX.get()).floatValue(), (float)((Float)Animations.itemPosY.get()).floatValue(), (float)((Float)Animations.itemPosZ.get()).floatValue());
        GlStateManager.func_179109_b((float)-0.5f, (float)0.2f, (float)0.0f);
        GlStateManager.func_179114_b((float)30.0f, (float)0.0f, (float)1.0f, (float)0.0f);
        GlStateManager.func_179114_b((float)-80.0f, (float)1.0f, (float)0.0f, (float)0.0f);
        GlStateManager.func_179114_b((float)60.0f, (float)0.0f, (float)1.0f, (float)0.0f);
        GlStateManager.func_179139_a((double)0.89, (double)0.89, (double)0.89);
    }

    private void avatar(float equipProgress, float swingProgress) {
        GlStateManager.func_179109_b((float)((Float)Animations.itemPosX.get()).floatValue(), (float)((Float)Animations.itemPosY.get()).floatValue(), (float)((Float)Animations.itemPosZ.get()).floatValue());
        GlStateManager.func_179109_b((float)0.56f, (float)-0.52f, (float)-0.71999997f);
        GlStateManager.func_179109_b((float)0.0f, (float)0.0f, (float)0.0f);
        GlStateManager.func_179114_b((float)45.0f, (float)0.0f, (float)1.0f, (float)0.0f);
        float f = MathHelper.func_76126_a((float)(swingProgress * swingProgress * (float)Math.PI));
        float f1 = MathHelper.func_76126_a((float)(MathHelper.func_76129_c((float)swingProgress) * (float)Math.PI));
        GlStateManager.func_179114_b((float)(f * -20.0f), (float)0.0f, (float)1.0f, (float)0.0f);
        GlStateManager.func_179114_b((float)(f1 * -20.0f), (float)0.0f, (float)0.0f, (float)1.0f);
        GlStateManager.func_179114_b((float)(f1 * -40.0f), (float)1.0f, (float)0.0f, (float)0.0f);
        GlStateManager.func_179152_a((float)0.4f, (float)0.4f, (float)0.4f);
        GlStateManager.func_179152_a((float)((Float)Animations.Scale.get()).floatValue(), (float)((Float)Animations.Scale.get()).floatValue(), (float)((Float)Animations.Scale.get()).floatValue());
    }

    private void emilio(float equipProgress, float swingProgress) {
        GlStateManager.func_179109_b((float)((Float)Animations.itemPosX.get()).floatValue(), (float)((Float)Animations.itemPosY.get()).floatValue(), (float)((Float)Animations.itemPosZ.get()).floatValue());
        float smooth = swingProgress * 0.78f - swingProgress * swingProgress * 0.78f;
        GlStateManager.func_179152_a((float)1.7f, (float)1.7f, (float)1.7f);
        GlStateManager.func_179114_b((float)48.0f, (float)0.0f, (float)-0.6f, (float)0.0f);
        GlStateManager.func_179109_b((float)-0.3f, (float)0.4f, (float)0.0f);
        GlStateManager.func_179109_b((float)0.0f, (float)0.08f, (float)0.0f);
        GlStateManager.func_179109_b((float)0.56f, (float)-0.489f, (float)-0.71999997f);
        GlStateManager.func_179109_b((float)0.0f, (float)0.0f, (float)0.0f);
        GlStateManager.func_179114_b((float)52.0f, (float)0.0f, (float)(180.0f + smooth * 0.5f), (float)(smooth * 20.0f));
        float f = MathHelper.func_76126_a((float)(swingProgress * swingProgress * (float)Math.PI));
        float f2 = MathHelper.func_76126_a((float)(MathHelper.func_76129_c((float)swingProgress) * (float)Math.PI));
        GlStateManager.func_179114_b((float)(f2 * -51.3f), (float)2.0f, (float)0.0f, (float)0.0f);
        GlStateManager.func_179109_b((float)0.0f, (float)-0.2f, (float)0.0f);
        GlStateManager.func_179152_a((float)((Float)Animations.Scale.get()).floatValue(), (float)((Float)Animations.Scale.get()).floatValue(), (float)((Float)Animations.Scale.get()).floatValue());
    }

    private void Zoom(float p_178096_1_, float p_178096_2_) {
        GlStateManager.func_179109_b((float)0.56f, (float)-0.52f, (float)-0.71999997f);
        GlStateManager.func_179109_b((float)0.0f, (float)(p_178096_1_ * -0.6f), (float)0.0f);
        GlStateManager.func_179114_b((float)45.0f, (float)0.0f, (float)1.0f, (float)0.0f);
        float var3 = MathHelper.func_76126_a((float)(p_178096_2_ * p_178096_2_ * (float)Math.PI));
        float var4 = MathHelper.func_76126_a((float)(MathHelper.func_76129_c((float)p_178096_2_) * (float)Math.PI));
        GlStateManager.func_179114_b((float)(var3 * -20.0f), (float)0.0f, (float)0.0f, (float)0.0f);
        GlStateManager.func_179114_b((float)(var4 * -20.0f), (float)0.0f, (float)0.0f, (float)0.0f);
        GlStateManager.func_179114_b((float)(var4 * -20.0f), (float)0.0f, (float)0.0f, (float)0.0f);
        GlStateManager.func_179139_a((double)0.5, (double)0.5, (double)0.5);
    }

    private void ETB(float p_178096_1_, float p_178096_2_) {
        GlStateManager.func_179109_b((float)0.56f, (float)-0.52f, (float)-0.71999997f);
        GlStateManager.func_179109_b((float)0.0f, (float)(p_178096_1_ * -0.6f), (float)0.0f);
        GlStateManager.func_179114_b((float)45.0f, (float)0.0f, (float)1.0f, (float)0.0f);
        float var3 = MathHelper.func_76126_a((float)(p_178096_2_ * p_178096_2_ * (float)Math.PI));
        float var4 = MathHelper.func_76126_a((float)(MathHelper.func_76129_c((float)p_178096_2_) * (float)Math.PI));
        GlStateManager.func_179114_b((float)(var3 * -34.0f), (float)0.0f, (float)1.0f, (float)0.2f);
        GlStateManager.func_179114_b((float)(var4 * -20.7f), (float)0.2f, (float)0.1f, (float)1.0f);
        GlStateManager.func_179114_b((float)(var4 * -68.6f), (float)1.3f, (float)0.1f, (float)0.2f);
        GlStateManager.func_179152_a((float)0.4f, (float)0.4f, (float)0.4f);
    }

    private void func_178103_d2() {
        GlStateManager.func_179109_b((float)((Float)Animations.itemPosX.get()).floatValue(), (float)((Float)Animations.itemPosY.get()).floatValue(), (float)((Float)Animations.itemPosZ.get()).floatValue());
        GlStateManager.func_179109_b((float)-0.5f, (float)0.2f, (float)0.0f);
        GlStateManager.func_179114_b((float)30.0f, (float)0.0f, (float)1.0f, (float)0.0f);
        GlStateManager.func_179114_b((float)-80.0f, (float)1.0f, (float)0.0f, (float)0.0f);
        GlStateManager.func_179114_b((float)60.0f, (float)0.0f, (float)1.0f, (float)0.0f);
        GlStateManager.func_179152_a((float)((Float)Animations.Scale.get()).floatValue(), (float)((Float)Animations.Scale.get()).floatValue(), (float)((Float)Animations.Scale.get()).floatValue());
    }

    private void genCustom(float p_178096_1_, float p_178096_2_) {
        GlStateManager.func_179109_b((float)((Float)Animations.itemPosX.get()).floatValue(), (float)((Float)Animations.itemPosY.get()).floatValue(), (float)((Float)Animations.itemPosZ.get()).floatValue());
        GlStateManager.func_179109_b((float)0.56f, (float)-0.52f, (float)-0.71999997f);
        GlStateManager.func_179109_b((float)0.0f, (float)(p_178096_1_ * -0.6f), (float)0.0f);
        GlStateManager.func_179114_b((float)25.0f, (float)0.0f, (float)1.0f, (float)0.0f);
        float var3 = MathHelper.func_76126_a((float)(p_178096_2_ * p_178096_2_ * (float)Math.PI));
        float var4 = MathHelper.func_76126_a((float)(MathHelper.func_76129_c((float)p_178096_2_) * (float)Math.PI));
        GlStateManager.func_179114_b((float)(var3 * -15.0f), (float)0.0f, (float)1.0f, (float)0.2f);
        GlStateManager.func_179114_b((float)(var4 * -10.0f), (float)0.2f, (float)0.1f, (float)1.0f);
        GlStateManager.func_179114_b((float)(var4 * -30.0f), (float)1.3f, (float)0.1f, (float)0.2f);
        GlStateManager.func_179152_a((float)0.4f, (float)0.4f, (float)0.4f);
        GlStateManager.func_179152_a((float)((Float)Animations.Scale.get()).floatValue(), (float)((Float)Animations.Scale.get()).floatValue(), (float)((Float)Animations.Scale.get()).floatValue());
    }

    @Inject(method={"renderFireInFirstPerson"}, at={@At(value="HEAD")}, cancellable=true)
    private void renderFireInFirstPerson(CallbackInfo callbackInfo) {
        Camera camera = (Camera)LiquidBounce.moduleManager.getModule(Camera.class);
        if (camera.getState() && ((Boolean)camera.getFireEffect().get()).booleanValue() && ((Boolean)camera.getAntiBlindValue().get()).booleanValue()) {
            GlStateManager.func_179131_c((float)1.0f, (float)1.0f, (float)1.0f, (float)0.9f);
            GlStateManager.func_179143_c((int)519);
            GlStateManager.func_179132_a((boolean)false);
            GlStateManager.func_179147_l();
            GlStateManager.func_179120_a((int)770, (int)771, (int)1, (int)0);
            GlStateManager.func_179131_c((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
            GlStateManager.func_179084_k();
            GlStateManager.func_179132_a((boolean)true);
            GlStateManager.func_179143_c((int)515);
            callbackInfo.cancel();
        }
    }
}

