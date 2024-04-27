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
package net.dev.important.injection.forge.mixins.item;

import net.dev.important.Client;
import net.dev.important.modules.module.modules.combat.KillAura;
import net.dev.important.modules.module.modules.render.Animations;
import net.dev.important.modules.module.modules.render.AntiBlind;
import net.dev.important.utils.timer.MSTimer;
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
    float delay = 0.0f;
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

    private void func_178096_b(float p_178096_1_, float p_178096_2_) {
        GlStateManager.func_179109_b((float)0.56f, (float)-0.52f, (float)-0.71999997f);
        GlStateManager.func_179109_b((float)0.0f, (float)(p_178096_1_ * -0.6f), (float)0.0f);
        GlStateManager.func_179114_b((float)45.0f, (float)0.0f, (float)1.0f, (float)0.0f);
        float var3 = MathHelper.func_76126_a((float)(p_178096_2_ * p_178096_2_ * (float)Math.PI));
        float var4 = MathHelper.func_76126_a((float)(MathHelper.func_76129_c((float)p_178096_2_) * (float)Math.PI));
        GlStateManager.func_179114_b((float)(var3 * -20.0f), (float)0.0f, (float)1.0f, (float)0.0f);
        GlStateManager.func_179114_b((float)(var4 * -20.0f), (float)0.0f, (float)0.0f, (float)1.0f);
        GlStateManager.func_179114_b((float)(var4 * -80.0f), (float)1.0f, (float)0.0f, (float)0.0f);
        GlStateManager.func_179152_a((float)((Float)Animations.Scale.get()).floatValue(), (float)((Float)Animations.Scale.get()).floatValue(), (float)((Float)Animations.Scale.get()).floatValue());
    }

    private void test(float i, float i2) {
        GlStateManager.func_179109_b((float)0.56f, (float)-0.52f, (float)-0.71999997f);
        GlStateManager.func_179109_b((float)0.0f, (float)(i * -0.6f), (float)0.0f);
        GlStateManager.func_179114_b((float)45.0f, (float)0.0f, (float)1.0f, (float)0.0f);
        float var3 = MathHelper.func_76126_a((float)(i2 * i2 * (float)Math.PI));
        float var4 = MathHelper.func_76126_a((float)(MathHelper.func_76129_c((float)i2) * (float)Math.PI));
        float var5 = MathHelper.func_76123_f((float)((float)MathHelper.func_76128_c((double)i2) * (float)Math.PI));
        GlStateManager.func_179114_b((float)(var3 * -20.0f), (float)0.0f, (float)1.0f, (float)0.0f);
        GlStateManager.func_179114_b((float)(var4 * -20.0f), (float)0.0f, (float)0.0f, (float)1.0f);
        GlStateManager.func_179114_b((float)(var5 * -80.0f), (float)1.0f, (float)0.0f, (float)0.0f);
        GlStateManager.func_179152_a((float)((Float)Animations.Scale.get()).floatValue(), (float)((Float)Animations.Scale.get()).floatValue(), (float)((Float)Animations.Scale.get()).floatValue());
    }

    private void tap2(float var2, float swing) {
        GlStateManager.func_179109_b((float)0.56f, (float)-0.52f, (float)-0.71999997f);
        float var3 = MathHelper.func_76126_a((float)(swing * swing * (float)Math.PI));
        float var4 = MathHelper.func_76126_a((float)(MathHelper.func_76129_c((float)swing) * (float)Math.PI));
        GlStateManager.func_179109_b((float)0.56f, (float)-0.42f, (float)-0.71999997f);
        GlStateManager.func_179109_b((float)(0.1f * var4), (float)-0.0f, (float)(-0.21999997f * var4));
        GlStateManager.func_179109_b((float)0.0f, (float)(var2 * -0.15f), (float)0.0f);
        GlStateManager.func_179114_b((float)(var3 * 45.0f), (float)0.0f, (float)1.0f, (float)0.0f);
        GlStateManager.func_179152_a((float)((Float)Animations.Scale.get()).floatValue(), (float)((Float)Animations.Scale.get()).floatValue(), (float)((Float)Animations.Scale.get()).floatValue());
    }

    private void avatar(float equipProgress, float swingProgress) {
        GlStateManager.func_179109_b((float)0.56f, (float)-0.52f, (float)-0.71999997f);
        GlStateManager.func_179109_b((float)0.0f, (float)0.0f, (float)0.0f);
        GlStateManager.func_179114_b((float)45.0f, (float)0.0f, (float)1.0f, (float)0.0f);
        float f = MathHelper.func_76126_a((float)(swingProgress * swingProgress * (float)Math.PI));
        float f2 = MathHelper.func_76126_a((float)(MathHelper.func_76129_c((float)swingProgress) * (float)Math.PI));
        GlStateManager.func_179114_b((float)(f * -20.0f), (float)0.0f, (float)1.0f, (float)0.0f);
        GlStateManager.func_179114_b((float)(f2 * -20.0f), (float)0.0f, (float)0.0f, (float)1.0f);
        GlStateManager.func_179114_b((float)(f2 * -40.0f), (float)1.0f, (float)0.0f, (float)0.0f);
        GlStateManager.func_179152_a((float)((Float)Animations.Scale.get()).floatValue(), (float)((Float)Animations.Scale.get()).floatValue(), (float)((Float)Animations.Scale.get()).floatValue());
    }

    private void tap1(float tap1, float tap2) {
        GlStateManager.func_179109_b((float)0.56f, (float)-0.52f, (float)-0.71999997f);
        GlStateManager.func_179109_b((float)0.0f, (float)(tap1 * -0.6f), (float)0.0f);
        GlStateManager.func_179114_b((float)45.0f, (float)0.0f, (float)1.0f, (float)0.0f);
        float var3 = MathHelper.func_76126_a((float)(tap2 * tap2 * (float)Math.PI));
        float var4 = MathHelper.func_76126_a((float)(MathHelper.func_76129_c((float)tap2) * (float)Math.PI));
        GlStateManager.func_179114_b((float)(var3 * -40.0f), (float)0.0f, (float)1.0f, (float)0.0f);
        GlStateManager.func_179114_b((float)(var4 * 0.0f), (float)0.0f, (float)0.0f, (float)1.0f);
        GlStateManager.func_179114_b((float)(var4 * 0.0f), (float)1.0f, (float)0.0f, (float)0.0f);
        GlStateManager.func_179152_a((float)((Float)Animations.Scale.get()).floatValue(), (float)((Float)Animations.Scale.get()).floatValue(), (float)((Float)Animations.Scale.get()).floatValue());
    }

    private void stab(float var10, float var9) {
        GlStateManager.func_179109_b((float)0.56f, (float)-0.52f, (float)-0.71999997f);
        GlStateManager.func_179109_b((float)0.0f, (float)0.0f, (float)0.0f);
        GlStateManager.func_179114_b((float)45.0f, (float)0.0f, (float)1.0f, (float)0.0f);
        float var11 = MathHelper.func_76126_a((float)(var9 * var9 * (float)Math.PI));
        float var12 = MathHelper.func_76126_a((float)(MathHelper.func_76129_c((float)var9) * (float)Math.PI));
        GlStateManager.func_179114_b((float)(var11 * 20.0f), (float)1.0f, (float)0.0f, (float)0.0f);
        GlStateManager.func_179114_b((float)(var12 * 0.0f), (float)0.0f, (float)0.0f, (float)0.0f);
        GlStateManager.func_179114_b((float)(var12 * -10.0f), (float)1.0f, (float)0.0f, (float)-4.0f);
        GlStateManager.func_179152_a((float)((Float)Animations.Scale.get()).floatValue(), (float)((Float)Animations.Scale.get()).floatValue(), (float)((Float)Animations.Scale.get()).floatValue());
    }

    private void slide(float var10, float var9) {
        GlStateManager.func_179109_b((float)0.56f, (float)-0.52f, (float)-0.71999997f);
        GlStateManager.func_179109_b((float)0.0f, (float)0.0f, (float)0.0f);
        GlStateManager.func_179114_b((float)45.0f, (float)0.0f, (float)1.0f, (float)0.0f);
        float var11 = MathHelper.func_76126_a((float)(var9 * var9 * (float)Math.PI));
        float var12 = MathHelper.func_76126_a((float)(MathHelper.func_76129_c((float)var9) * (float)Math.PI));
        GlStateManager.func_179114_b((float)(var11 * 0.0f), (float)0.0f, (float)1.0f, (float)0.0f);
        GlStateManager.func_179114_b((float)(var12 * 0.0f), (float)0.0f, (float)0.0f, (float)1.0f);
        GlStateManager.func_179114_b((float)(var12 * -40.0f), (float)1.0f, (float)0.0f, (float)0.0f);
        GlStateManager.func_179152_a((float)((Float)Animations.Scale.get()).floatValue(), (float)((Float)Animations.Scale.get()).floatValue(), (float)((Float)Animations.Scale.get()).floatValue());
    }

    private void slide2(float var10, float var9) {
        GlStateManager.func_179109_b((float)0.56f, (float)-0.52f, (float)-0.71999997f);
        GlStateManager.func_179109_b((float)0.0f, (float)0.0f, (float)0.0f);
        GlStateManager.func_179114_b((float)45.0f, (float)0.0f, (float)1.0f, (float)0.0f);
        float var11 = MathHelper.func_76126_a((float)(var9 * var9 * (float)Math.PI));
        float var12 = MathHelper.func_76126_a((float)(MathHelper.func_76129_c((float)var9) * (float)Math.PI));
        GlStateManager.func_179114_b((float)(var11 * 0.0f), (float)0.0f, (float)1.0f, (float)0.0f);
        GlStateManager.func_179114_b((float)(var12 * 0.0f), (float)0.0f, (float)0.0f, (float)1.0f);
        GlStateManager.func_179114_b((float)(var12 * -80.0f), (float)1.0f, (float)0.0f, (float)0.0f);
        GlStateManager.func_179152_a((float)((Float)Animations.Scale.get()).floatValue(), (float)((Float)Animations.Scale.get()).floatValue(), (float)((Float)Animations.Scale.get()).floatValue());
    }

    private void jello(float var11, float var12) {
        GlStateManager.func_179109_b((float)0.56f, (float)-0.52f, (float)-0.71999997f);
        GlStateManager.func_179114_b((float)48.57f, (float)0.0f, (float)0.24f, (float)0.14f);
        float var13 = MathHelper.func_76126_a((float)(var12 * var12 * (float)Math.PI));
        float var14 = MathHelper.func_76126_a((float)(MathHelper.func_76129_c((float)var12) * (float)Math.PI));
        GlStateManager.func_179114_b((float)(var13 * -35.0f), (float)0.0f, (float)0.0f, (float)0.0f);
        GlStateManager.func_179114_b((float)(var14 * 0.0f), (float)0.0f, (float)0.0f, (float)0.0f);
        GlStateManager.func_179114_b((float)(var14 * 20.0f), (float)1.0f, (float)1.0f, (float)1.0f);
        GlStateManager.func_179152_a((float)((Float)Animations.Scale.get()).floatValue(), (float)((Float)Animations.Scale.get()).floatValue(), (float)((Float)Animations.Scale.get()).floatValue());
    }

    private void continuity(float var11, float var10) {
        GlStateManager.func_179109_b((float)0.56f, (float)-0.52f, (float)-0.71999997f);
        GlStateManager.func_179109_b((float)0.0f, (float)0.0f, (float)0.0f);
        GlStateManager.func_179114_b((float)45.0f, (float)0.0f, (float)1.0f, (float)0.0f);
        float var12 = -MathHelper.func_76126_a((float)(var10 * var10 * (float)Math.PI));
        float var13 = MathHelper.func_76134_b((float)(MathHelper.func_76129_c((float)var10) * (float)Math.PI));
        float var14 = MathHelper.func_76135_e((float)(MathHelper.func_76129_c((float)var11) * (float)Math.PI));
        GlStateManager.func_179114_b((float)(var12 * var14 * 30.0f), (float)0.0f, (float)1.0f, (float)0.0f);
        GlStateManager.func_179114_b((float)(var13 * 0.0f), (float)0.0f, (float)0.0f, (float)1.0f);
        GlStateManager.func_179114_b((float)(var13 * 20.0f), (float)1.0f, (float)0.0f, (float)0.0f);
        GlStateManager.func_179152_a((float)((Float)Animations.Scale.get()).floatValue(), (float)((Float)Animations.Scale.get()).floatValue(), (float)((Float)Animations.Scale.get()).floatValue());
    }

    private void poke(float var5, float var6) {
        GlStateManager.func_179109_b((float)0.56f, (float)-0.52f, (float)-0.71999997f);
        GlStateManager.func_179114_b((float)45.0f, (float)0.0f, (float)1.0f, (float)0.0f);
        float var7 = MathHelper.func_76126_a((float)(var6 * var6 * (float)Math.PI));
        float var8 = MathHelper.func_76126_a((float)(MathHelper.func_76129_c((float)var6) * (float)Math.PI));
        GlStateManager.func_179109_b((float)0.56f, (float)-0.42f, (float)-0.71999997f);
        GlStateManager.func_179109_b((float)(0.1f * var8), (float)-0.0f, (float)(-0.21999997f * var8));
        GlStateManager.func_179109_b((float)0.0f, (float)(var5 * -0.15f), (float)0.0f);
        GlStateManager.func_179114_b((float)(var7 * 0.0f), (float)0.0f, (float)1.0f, (float)0.0f);
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
        GlStateManager.func_179152_a((float)((Float)Animations.Scale.get()).floatValue(), (float)((Float)Animations.Scale.get()).floatValue(), (float)((Float)Animations.Scale.get()).floatValue());
    }

    private void strange(float lul, float lol) {
        GlStateManager.func_179109_b((float)0.56f, (float)-0.52f, (float)-0.71999997f);
        GlStateManager.func_179114_b((float)45.0f, (float)0.0f, (float)1.0f, (float)0.0f);
        float var26 = MathHelper.func_76126_a((float)(lol * lul * (float)Math.PI));
        float var27 = MathHelper.func_76134_b((float)(MathHelper.func_76133_a((double)lul) * (float)Math.PI));
        float var28 = MathHelper.func_76135_e((float)(MathHelper.func_76129_c((float)lul) * (float)Math.PI));
        GlStateManager.func_179114_b((float)(var26 * var27), (float)0.0f, (float)1.0f, (float)0.0f);
        GlStateManager.func_179114_b((float)(var28 * 15.0f), (float)0.0f, (float)0.0f, (float)1.0f);
        GlStateManager.func_179114_b((float)(var27 * 10.0f), (float)1.0f, (float)0.0f, (float)0.0f);
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
        GlStateManager.func_179152_a((float)((Float)Animations.Scale.get()).floatValue(), (float)((Float)Animations.Scale.get()).floatValue(), (float)((Float)Animations.Scale.get()).floatValue());
    }

    private void ETB(float equipProgress, float swingProgress) {
        GlStateManager.func_179109_b((float)0.56f, (float)-0.52f, (float)-0.71999997f);
        GlStateManager.func_179109_b((float)0.0f, (float)(equipProgress * -0.6f), (float)0.0f);
        GlStateManager.func_179114_b((float)45.0f, (float)0.0f, (float)1.0f, (float)0.0f);
        float var3 = MathHelper.func_76126_a((float)(swingProgress * swingProgress * (float)Math.PI));
        float var4 = MathHelper.func_76126_a((float)(MathHelper.func_76129_c((float)swingProgress) * (float)Math.PI));
        GlStateManager.func_179114_b((float)(var3 * -34.0f), (float)0.0f, (float)1.0f, (float)0.2f);
        GlStateManager.func_179114_b((float)(var4 * -20.7f), (float)0.2f, (float)0.1f, (float)1.0f);
        GlStateManager.func_179114_b((float)(var4 * -68.6f), (float)1.3f, (float)0.1f, (float)0.2f);
        GlStateManager.func_179152_a((float)((Float)Animations.Scale.get()).floatValue(), (float)((Float)Animations.Scale.get()).floatValue(), (float)((Float)Animations.Scale.get()).floatValue());
    }

    private void sigmaold(float p_178096_1_, float p_178096_2_) {
        GlStateManager.func_179109_b((float)0.56f, (float)-0.52f, (float)-0.71999997f);
        GlStateManager.func_179109_b((float)0.0f, (float)(p_178096_1_ * -0.6f), (float)0.0f);
        GlStateManager.func_179114_b((float)25.0f, (float)0.0f, (float)1.0f, (float)0.0f);
        float var3 = MathHelper.func_76126_a((float)(p_178096_2_ * p_178096_2_ * (float)Math.PI));
        float var4 = MathHelper.func_76126_a((float)(MathHelper.func_76129_c((float)p_178096_2_) * (float)Math.PI));
        GlStateManager.func_179114_b((float)(var3 * -15.0f), (float)0.0f, (float)1.0f, (float)0.2f);
        GlStateManager.func_179114_b((float)(var4 * -10.0f), (float)0.2f, (float)0.1f, (float)1.0f);
        GlStateManager.func_179114_b((float)(var4 * -30.0f), (float)1.3f, (float)0.1f, (float)0.2f);
        GlStateManager.func_179152_a((float)((Float)Animations.Scale.get()).floatValue(), (float)((Float)Animations.Scale.get()).floatValue(), (float)((Float)Animations.Scale.get()).floatValue());
    }

    private void push1(float idk, float idc) {
        GlStateManager.func_179109_b((float)0.56f, (float)-0.52f, (float)-0.71999997f);
        GlStateManager.func_179109_b((float)0.0f, (float)(idk * -0.6f), (float)0.0f);
        GlStateManager.func_179114_b((float)45.0f, (float)0.0f, (float)1.0f, (float)0.0f);
        float var3 = MathHelper.func_76126_a((float)(idc * idc * (float)Math.PI));
        float var4 = MathHelper.func_76126_a((float)(MathHelper.func_76129_c((float)idc) * (float)Math.PI));
        GlStateManager.func_179114_b((float)(var3 * -10.0f), (float)1.0f, (float)1.0f, (float)1.0f);
        GlStateManager.func_179114_b((float)(var4 * -10.0f), (float)1.0f, (float)1.0f, (float)1.0f);
        GlStateManager.func_179114_b((float)(var4 * -10.0f), (float)1.0f, (float)1.0f, (float)1.0f);
        GlStateManager.func_179152_a((float)((Float)Animations.Scale.get()).floatValue(), (float)((Float)Animations.Scale.get()).floatValue(), (float)((Float)Animations.Scale.get()).floatValue());
    }

    private void push2(float idk, float idc) {
        GlStateManager.func_179109_b((float)0.56f, (float)-0.52f, (float)-0.71999997f);
        GlStateManager.func_179109_b((float)0.0f, (float)(idk * -0.6f), (float)0.0f);
        GlStateManager.func_179114_b((float)45.0f, (float)0.0f, (float)1.0f, (float)0.0f);
        float var3 = MathHelper.func_76126_a((float)(idc * idc * (float)Math.PI));
        float var4 = MathHelper.func_76126_a((float)(MathHelper.func_76129_c((float)idc) * (float)Math.PI));
        GlStateManager.func_179114_b((float)(var3 * -10.0f), (float)2.0f, (float)2.0f, (float)2.0f);
        GlStateManager.func_179114_b((float)(var4 * -10.0f), (float)2.0f, (float)2.0f, (float)0.0f);
        GlStateManager.func_179114_b((float)(var4 * -10.0f), (float)2.0f, (float)2.0f, (float)0.0f);
        GlStateManager.func_179152_a((float)((Float)Animations.Scale.get()).floatValue(), (float)((Float)Animations.Scale.get()).floatValue(), (float)((Float)Animations.Scale.get()).floatValue());
    }

    private void up(float idk, float idc) {
        GlStateManager.func_179109_b((float)0.56f, (float)-0.52f, (float)-0.71999997f);
        GlStateManager.func_179109_b((float)0.0f, (float)(idk * -0.6f), (float)0.0f);
        GlStateManager.func_179114_b((float)45.0f, (float)0.0f, (float)1.0f, (float)0.0f);
        float var3 = MathHelper.func_76126_a((float)(idc * idc * (float)Math.PI));
        float var4 = MathHelper.func_76126_a((float)(MathHelper.func_76129_c((float)idc) * (float)Math.PI));
        GlStateManager.func_179114_b((float)(var3 * -20.0f), (float)0.0f, (float)1.0f, (float)1.0f);
        GlStateManager.func_179114_b((float)(var4 * -10.0f), (float)1.0f, (float)0.0f, (float)0.0f);
        GlStateManager.func_179114_b((float)(var3 * -20.0f), (float)0.0f, (float)1.0f, (float)0.0f);
        GlStateManager.func_179114_b((float)(var4 * -10.0f), (float)1.0f, (float)0.0f, (float)1.0f);
        GlStateManager.func_179152_a((float)((Float)Animations.Scale.get()).floatValue(), (float)((Float)Animations.Scale.get()).floatValue(), (float)((Float)Animations.Scale.get()).floatValue());
    }

    private void func_178103_d() {
        GlStateManager.func_179109_b((float)-0.5f, (float)0.2f, (float)0.0f);
        GlStateManager.func_179114_b((float)30.0f, (float)0.0f, (float)1.0f, (float)0.0f);
        GlStateManager.func_179114_b((float)-80.0f, (float)1.0f, (float)0.0f, (float)0.0f);
        GlStateManager.func_179114_b((float)60.0f, (float)0.0f, (float)1.0f, (float)0.0f);
    }

    @Overwrite
    public void func_78440_a(float partialTicks) {
        float f = 1.0f - (this.field_78451_d + (this.field_78454_c - this.field_78451_d) * partialTicks);
        EntityPlayerSP abstractclientplayer = this.field_78455_a.field_71439_g;
        float f1 = abstractclientplayer.func_70678_g(partialTicks);
        float f2 = abstractclientplayer.field_70127_C + (abstractclientplayer.field_70125_A - abstractclientplayer.field_70127_C) * partialTicks;
        float f3 = abstractclientplayer.field_70126_B + (abstractclientplayer.field_70177_z - abstractclientplayer.field_70126_B) * partialTicks;
        if (Client.moduleManager.getModule(Animations.class).getState()) {
            GL11.glTranslated((double)((Float)Animations.itemPosX.get()).doubleValue(), (double)((Float)Animations.itemPosY.get()).doubleValue(), (double)((Float)Animations.itemPosZ.get()).doubleValue());
        }
        this.func_178101_a(f2, f3);
        this.func_178109_a((AbstractClientPlayer)abstractclientplayer);
        this.func_178110_a(abstractclientplayer, partialTicks);
        GlStateManager.func_179091_B();
        GlStateManager.func_179094_E();
        if (Client.moduleManager.getModule(Animations.class).getState()) {
            GL11.glTranslated((double)((Float)Animations.itemPosX.get()).doubleValue(), (double)((Float)Animations.itemPosY.get()).doubleValue(), (double)((Float)Animations.itemPosZ.get()).doubleValue());
        }
        if (this.field_78453_b != null) {
            boolean canBlockEverything;
            KillAura killAura = (KillAura)Client.moduleManager.getModule(KillAura.class);
            boolean bl = canBlockEverything = Client.moduleManager.getModule(Animations.class).getState() && (Boolean)Animations.blockEverything.get() != false && killAura.getTarget() != null && (this.field_78453_b.func_77973_b() instanceof ItemBucketMilk || this.field_78453_b.func_77973_b() instanceof ItemFood || this.field_78453_b.func_77973_b() instanceof ItemPotion || this.field_78453_b.func_77973_b() instanceof ItemAxe || this.field_78453_b.func_77973_b().equals(Items.field_151055_y));
            if (this.field_78453_b.func_77973_b() instanceof ItemMap) {
                this.func_178097_a((AbstractClientPlayer)abstractclientplayer, f2, f, f1);
            } else if (abstractclientplayer.func_71052_bv() > 0 || this.field_78453_b.func_77973_b() instanceof ItemSword && (killAura.getBlockingStatus() || killAura.getFakeBlock()) || this.field_78453_b.func_77973_b() instanceof ItemSword && Client.moduleManager.getModule(Animations.class).getState() && ((Boolean)Animations.fakeBlock.get()).booleanValue() && killAura.getTarget() != null || canBlockEverything) {
                EnumAction enumaction = killAura.getBlockingStatus() || canBlockEverything ? EnumAction.BLOCK : this.field_78453_b.func_77975_n();
                block0 : switch (enumaction) {
                    case NONE: {
                        this.func_178096_b(f, 0.0f);
                        break;
                    }
                    case EAT: 
                    case DRINK: {
                        if (Client.moduleManager.getModule(Animations.class).getState()) {
                            if (((String)Animations.swingMethod.get()).equalsIgnoreCase("Swing") || ((String)Animations.swingMethod.get()).equalsIgnoreCase("Default")) {
                                this.func_178104_a((AbstractClientPlayer)abstractclientplayer, partialTicks);
                            }
                            this.func_178096_b(f, f1);
                            if (!((Boolean)Animations.RotateItems.get()).booleanValue()) break;
                            this.ItemRotate();
                            break;
                        }
                        this.func_178104_a((AbstractClientPlayer)abstractclientplayer, partialTicks);
                        this.func_178096_b(f, f1);
                        break;
                    }
                    case BLOCK: {
                        if (Client.moduleManager.getModule(Animations.class).getState()) {
                            String z;
                            GL11.glTranslated((double)((Float)Animations.blockPosX.get()).doubleValue(), (double)((Float)Animations.blockPosY.get()).doubleValue(), (double)((Float)Animations.blockPosZ.get()).doubleValue());
                            switch (z = (String)Animations.Sword.get()) {
                                case "Normal": {
                                    this.func_178096_b(f + 0.1f, f1);
                                    if (((Boolean)Animations.RotateItems.get()).booleanValue()) {
                                        this.ItemRotate();
                                    }
                                    this.func_178103_d();
                                    GlStateManager.func_179109_b((float)-0.5f, (float)0.2f, (float)0.0f);
                                    if (!((Boolean)Animations.RotateItems.get()).booleanValue()) break;
                                    this.ItemRotate1();
                                    break block0;
                                }
                                case "SlideDown1": {
                                    this.func_178096_b(0.2f, f1);
                                    if (((Boolean)Animations.RotateItems.get()).booleanValue()) {
                                        this.ItemRotate();
                                    }
                                    this.func_178103_d();
                                    if (!((Boolean)Animations.RotateItems.get()).booleanValue()) break;
                                    this.ItemRotate1();
                                    break block0;
                                }
                                case "SlideDown2": {
                                    this.slide2(0.1f, f1);
                                    if (((Boolean)Animations.RotateItems.get()).booleanValue()) {
                                        this.ItemRotate();
                                    }
                                    this.func_178103_d();
                                    if (!((Boolean)Animations.RotateItems.get()).booleanValue()) break;
                                    this.ItemRotate1();
                                    break block0;
                                }
                                case "Minecraft": {
                                    this.func_178096_b(f, ((Float)Animations.mcSwordPos.get()).floatValue());
                                    if (((Boolean)Animations.RotateItems.get()).booleanValue()) {
                                        this.ItemRotate();
                                    }
                                    this.func_178103_d();
                                    if (!((Boolean)Animations.RotateItems.get()).booleanValue()) break;
                                    this.ItemRotate1();
                                    break block0;
                                }
                                case "Avatar": {
                                    this.avatar(f, f1);
                                    if (((Boolean)Animations.RotateItems.get()).booleanValue()) {
                                        this.ItemRotate();
                                    }
                                    this.func_178103_d();
                                    if (!((Boolean)Animations.RotateItems.get()).booleanValue()) break;
                                    this.ItemRotate1();
                                    break block0;
                                }
                                case "Tap2": {
                                    this.tap2(0.0f, f1);
                                    if (((Boolean)Animations.RotateItems.get()).booleanValue()) {
                                        this.ItemRotate();
                                    }
                                    GlStateManager.func_179152_a((float)2.0f, (float)2.0f, (float)2.0f);
                                    this.func_178103_d();
                                    if (!((Boolean)Animations.RotateItems.get()).booleanValue()) break;
                                    this.ItemRotate1();
                                    break block0;
                                }
                                case "Poke": {
                                    this.poke(0.1f, f1);
                                    GlStateManager.func_179152_a((float)2.5f, (float)2.5f, (float)2.5f);
                                    GL11.glTranslated((double)1.2, (double)-0.5, (double)0.5);
                                    if (((Boolean)Animations.RotateItems.get()).booleanValue()) {
                                        this.ItemRotate();
                                    }
                                    this.func_178103_d();
                                    if (!((Boolean)Animations.RotateItems.get()).booleanValue()) break;
                                    this.ItemRotate1();
                                    break block0;
                                }
                                case "Slide": {
                                    this.slide(0.1f, f1);
                                    if (((Boolean)Animations.RotateItems.get()).booleanValue()) {
                                        this.ItemRotate();
                                    }
                                    this.func_178103_d();
                                    if (!((Boolean)Animations.RotateItems.get()).booleanValue()) break;
                                    this.ItemRotate1();
                                    break block0;
                                }
                                case "Push1": {
                                    this.push1(0.1f, f1);
                                    if (((Boolean)Animations.RotateItems.get()).booleanValue()) {
                                        this.ItemRotate();
                                    }
                                    this.func_178103_d();
                                    if (!((Boolean)Animations.RotateItems.get()).booleanValue()) break;
                                    this.ItemRotate1();
                                    break block0;
                                }
                                case "Up": {
                                    this.up(f, f1);
                                    if (((Boolean)Animations.RotateItems.get()).booleanValue()) {
                                        this.ItemRotate();
                                    }
                                    this.func_178103_d();
                                    if (!((Boolean)Animations.RotateItems.get()).booleanValue()) break;
                                    this.ItemRotate1();
                                    break block0;
                                }
                                case "Shield": {
                                    this.jello(0.0f, f1);
                                    if (((Boolean)Animations.RotateItems.get()).booleanValue()) {
                                        this.ItemRotate();
                                    }
                                    this.func_178103_d();
                                    if (!((Boolean)Animations.RotateItems.get()).booleanValue()) break;
                                    this.ItemRotate1();
                                    break block0;
                                }
                                case "Akrien": {
                                    this.func_178096_b(f1, 0.0f);
                                    if (((Boolean)Animations.RotateItems.get()).booleanValue()) {
                                        this.ItemRotate();
                                    }
                                    this.func_178103_d();
                                    if (!((Boolean)Animations.RotateItems.get()).booleanValue()) break;
                                    this.ItemRotate1();
                                    break block0;
                                }
                                case "VisionFX": {
                                    this.continuity(0.1f, f1);
                                    if (((Boolean)Animations.RotateItems.get()).booleanValue()) {
                                        this.ItemRotate();
                                    }
                                    this.func_178103_d();
                                    if (!((Boolean)Animations.RotateItems.get()).booleanValue()) break;
                                    this.ItemRotate1();
                                    break block0;
                                }
                                case "Strange": {
                                    this.strange(f1 + 0.2f, 0.1f);
                                    if (((Boolean)Animations.RotateItems.get()).booleanValue()) {
                                        this.ItemRotate();
                                    }
                                    this.func_178103_d();
                                    if (!((Boolean)Animations.RotateItems.get()).booleanValue()) break;
                                    this.ItemRotate1();
                                    break block0;
                                }
                                case "Lucky": {
                                    this.move(-0.3f, f1);
                                    if (((Boolean)Animations.RotateItems.get()).booleanValue()) {
                                        this.ItemRotate();
                                    }
                                    this.func_178103_d();
                                    if (!((Boolean)Animations.RotateItems.get()).booleanValue()) break;
                                    this.ItemRotate1();
                                    break block0;
                                }
                                case "ETB": {
                                    this.ETB(f, f1);
                                    if (((Boolean)Animations.RotateItems.get()).booleanValue()) {
                                        this.ItemRotate();
                                    }
                                    this.func_178103_d();
                                    if (!((Boolean)Animations.RotateItems.get()).booleanValue()) break;
                                    this.ItemRotate1();
                                    break block0;
                                }
                                case "Swong": {
                                    this.func_178096_b(f / 2.0f, 0.0f);
                                    GlStateManager.func_179114_b((float)(-MathHelper.func_76126_a((float)(MathHelper.func_76129_c((float)this.field_78455_a.field_71439_g.func_70678_g(partialTicks)) * (float)Math.PI)) * 40.0f / 2.0f), (float)(MathHelper.func_76129_c((float)this.field_78455_a.field_71439_g.func_70678_g(partialTicks)) / 2.0f), (float)-0.0f, (float)9.0f);
                                    GlStateManager.func_179114_b((float)(-MathHelper.func_76129_c((float)this.field_78455_a.field_71439_g.func_70678_g(partialTicks)) * 30.0f), (float)1.0f, (float)(MathHelper.func_76129_c((float)this.field_78455_a.field_71439_g.func_70678_g(partialTicks)) / 2.0f), (float)-0.0f);
                                    if (((Boolean)Animations.RotateItems.get()).booleanValue()) {
                                        this.ItemRotate();
                                    }
                                    this.func_178103_d();
                                    if (!((Boolean)Animations.RotateItems.get()).booleanValue()) break;
                                    this.ItemRotate1();
                                    break block0;
                                }
                                case "SigmaOld": {
                                    float var15 = MathHelper.func_76126_a((float)(MathHelper.func_76129_c((float)f1) * (float)Math.PI));
                                    this.sigmaold(f * 0.5f, 0.0f);
                                    GlStateManager.func_179114_b((float)(-var15 * 55.0f / 2.0f), (float)-8.0f, (float)-0.0f, (float)9.0f);
                                    GlStateManager.func_179114_b((float)(-var15 * 45.0f), (float)1.0f, (float)(var15 / 2.0f), (float)-0.0f);
                                    if (((Boolean)Animations.RotateItems.get()).booleanValue()) {
                                        this.ItemRotate();
                                    }
                                    this.func_178103_d();
                                    GL11.glTranslated((double)1.2, (double)0.3, (double)0.5);
                                    GL11.glTranslatef((float)-1.0f, (float)(this.field_78455_a.field_71439_g.func_70093_af() ? -0.1f : -0.2f), (float)0.2f);
                                    GlStateManager.func_179152_a((float)1.2f, (float)1.2f, (float)1.2f);
                                    if (!((Boolean)Animations.RotateItems.get()).booleanValue()) break;
                                    this.ItemRotate1();
                                    break block0;
                                }
                                case "SmoothFloat": {
                                    this.func_178096_b(0.0f, 0.95f);
                                    GlStateManager.func_179114_b((float)this.delay, (float)1.0f, (float)0.0f, (float)2.0f);
                                    if (this.rotateTimer.hasTimePassed(1L)) {
                                        this.delay += 1.0f;
                                        this.delay += ((Float)Animations.SpeedRotate.get()).floatValue();
                                        this.rotateTimer.reset();
                                    }
                                    if (this.delay > 360.0f) {
                                        this.delay = 0.0f;
                                    }
                                    if (((Boolean)Animations.RotateItems.get()).booleanValue()) {
                                        this.ItemRotate();
                                    }
                                    this.func_178103_d();
                                    GlStateManager.func_179114_b((float)this.delay, (float)0.0f, (float)1.0f, (float)0.0f);
                                    if (!((Boolean)Animations.RotateItems.get()).booleanValue()) break;
                                    this.ItemRotate1();
                                    break block0;
                                }
                                case "Rotate360": {
                                    this.func_178096_b(0.0f, 0.95f);
                                    if (((Boolean)Animations.RotateItems.get()).booleanValue()) {
                                        this.ItemRotate();
                                    }
                                    this.func_178103_d();
                                    GlStateManager.func_179114_b((float)this.delay, (float)1.0f, (float)0.0f, (float)2.0f);
                                    if (this.rotateTimer.hasTimePassed(1L)) {
                                        this.delay += 1.0f;
                                        this.delay += ((Float)Animations.SpeedRotate.get()).floatValue();
                                        this.rotateTimer.reset();
                                    }
                                    if (this.delay > 360.0f) {
                                        this.delay = 0.0f;
                                    }
                                    if (!((Boolean)Animations.RotateItems.get()).booleanValue()) break;
                                    this.ItemRotate1();
                                    break block0;
                                }
                                case "Reverse": {
                                    this.func_178096_b(f1, f1);
                                    if (((Boolean)Animations.RotateItems.get()).booleanValue()) {
                                        this.ItemRotate();
                                    }
                                    this.func_178103_d();
                                    if (!((Boolean)Animations.RotateItems.get()).booleanValue()) break;
                                    this.ItemRotate1();
                                    break block0;
                                }
                                case "Zoom": {
                                    this.Zoom(0.0f, f1);
                                    if (((Boolean)Animations.RotateItems.get()).booleanValue()) {
                                        this.ItemRotate();
                                    }
                                    this.func_178103_d();
                                    if (!((Boolean)Animations.RotateItems.get()).booleanValue()) break;
                                    this.ItemRotate1();
                                    break block0;
                                }
                                case "Move": {
                                    this.test(f, f1);
                                    if (((Boolean)Animations.RotateItems.get()).booleanValue()) {
                                        this.ItemRotate();
                                    }
                                    this.func_178103_d();
                                    if (!((Boolean)Animations.RotateItems.get()).booleanValue()) break;
                                    this.ItemRotate1();
                                    break block0;
                                }
                                case "Tap1": {
                                    this.tap1(f, f1);
                                    if (((Boolean)Animations.RotateItems.get()).booleanValue()) {
                                        this.ItemRotate();
                                    }
                                    this.func_178103_d();
                                    if (!((Boolean)Animations.RotateItems.get()).booleanValue()) break;
                                    this.ItemRotate1();
                                    break block0;
                                }
                                case "Stab": {
                                    this.stab(0.1f, f1);
                                    if (((Boolean)Animations.RotateItems.get()).booleanValue()) {
                                        this.ItemRotate();
                                    }
                                    this.func_178103_d();
                                    if (!((Boolean)Animations.RotateItems.get()).booleanValue()) break;
                                    this.ItemRotate1();
                                    break block0;
                                }
                                case "Push2": {
                                    this.push2(0.1f, f1);
                                    if (((Boolean)Animations.RotateItems.get()).booleanValue()) {
                                        this.ItemRotate();
                                    }
                                    this.func_178103_d();
                                    if (!((Boolean)Animations.RotateItems.get()).booleanValue()) break;
                                    this.ItemRotate1();
                                    break block0;
                                }
                                case "Jello": {
                                    this.func_178096_b(0.0f, 0.0f);
                                    this.func_178103_d();
                                    int alpha2 = (int)Math.min(255L, (System.currentTimeMillis() % 255L > 127L ? Math.abs(Math.abs(System.currentTimeMillis()) % 255L - 255L) : System.currentTimeMillis() % 255L) * 2L);
                                    float f5 = (double)f1 > 0.5 ? 1.0f - f1 : f1;
                                    GlStateManager.func_179109_b((float)0.3f, (float)-0.0f, (float)0.4f);
                                    GlStateManager.func_179114_b((float)0.0f, (float)0.0f, (float)0.0f, (float)1.0f);
                                    GlStateManager.func_179109_b((float)0.0f, (float)0.5f, (float)0.0f);
                                    GlStateManager.func_179114_b((float)90.0f, (float)1.0f, (float)0.0f, (float)-1.0f);
                                    GlStateManager.func_179109_b((float)0.6f, (float)0.5f, (float)0.0f);
                                    GlStateManager.func_179114_b((float)-90.0f, (float)1.0f, (float)0.0f, (float)-1.0f);
                                    GlStateManager.func_179114_b((float)-10.0f, (float)1.0f, (float)0.0f, (float)-1.0f);
                                    GlStateManager.func_179114_b((float)(-f5 * 10.0f), (float)10.0f, (float)10.0f, (float)-9.0f);
                                    GlStateManager.func_179114_b((float)10.0f, (float)-1.0f, (float)0.0f, (float)0.0f);
                                    GlStateManager.func_179137_b((double)0.0, (double)0.0, (double)-0.5);
                                    GlStateManager.func_179114_b((float)(this.field_78455_a.field_71439_g.field_82175_bq ? (float)(-alpha2) / 5.0f : 1.0f), (float)1.0f, (float)-0.0f, (float)1.0f);
                                    GlStateManager.func_179137_b((double)0.0, (double)0.0, (double)0.5);
                                    break block0;
                                }
                            }
                            break;
                        }
                        this.func_178096_b(f + 0.1f, f1);
                        this.func_178103_d();
                        GlStateManager.func_179109_b((float)-0.5f, (float)0.2f, (float)0.0f);
                        break;
                    }
                    case BOW: {
                        if (!Client.moduleManager.getModule(Animations.class).getState()) break;
                        this.func_178096_b(f, f1);
                        if (((Boolean)Animations.RotateItems.get()).booleanValue()) {
                            this.ItemRotate();
                        }
                        if (((String)Animations.swingMethod.get()).equalsIgnoreCase("Swing") || ((String)Animations.swingMethod.get()).equalsIgnoreCase("Cancel")) {
                            this.func_178098_a(partialTicks, (AbstractClientPlayer)abstractclientplayer);
                        }
                        if (!((Boolean)Animations.RotateItems.get()).booleanValue()) break;
                        this.ItemRotate1();
                    }
                }
            } else if (!((String)Animations.swingMethod.get()).equalsIgnoreCase("Swing")) {
                this.func_178105_d(f1);
                this.func_178096_b(f, f1);
            } else if (((Boolean)Animations.RotateItems.get()).booleanValue()) {
                this.func_178096_b(f, f1);
                this.ItemRotate();
            } else if (((Boolean)Animations.RotateItems.get()).booleanValue() && this.field_78455_a.field_71474_y.field_74313_G.field_74513_e) {
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
        if (Client.moduleManager.getModule(Animations.class).getState()) {
            GL11.glTranslated((double)(-((Float)Animations.itemPosX.get()).doubleValue()), (double)(-((Float)Animations.itemPosY.get()).doubleValue()), (double)(-((Float)Animations.itemPosZ.get()).doubleValue()));
        }
    }

    private void ItemRotate() {
        if (((String)Animations.transformFirstPersonRotate.get()).equalsIgnoreCase("Rotate1")) {
            GlStateManager.func_179114_b((float)this.delay, (float)0.0f, (float)1.0f, (float)0.0f);
        }
        if (((String)Animations.transformFirstPersonRotate.get()).equalsIgnoreCase("Rotate2")) {
            GlStateManager.func_179114_b((float)this.delay, (float)1.0f, (float)1.0f, (float)0.0f);
        }
        if (((String)Animations.transformFirstPersonRotate.get()).equalsIgnoreCase("Custom")) {
            GlStateManager.func_179114_b((float)this.delay, (float)((Integer)Animations.customRotate1.get()).floatValue(), (float)((Integer)Animations.customRotate2.get()).floatValue(), (float)((Integer)Animations.customRotate3.get()).floatValue());
        }
        if (this.rotateTimer.hasTimePassed(1L)) {
            this.delay += 1.0f;
            this.delay += ((Float)Animations.SpeedRotate.get()).floatValue();
            this.rotateTimer.reset();
        }
        if (this.delay > 360.0f) {
            this.delay = 0.0f;
        }
    }

    private void ItemRotate1() {
        if (((String)Animations.doBlockTransformationsRotate.get()).equalsIgnoreCase("Rotate1")) {
            GlStateManager.func_179114_b((float)this.delay, (float)0.0f, (float)1.0f, (float)0.0f);
        }
        if (((String)Animations.doBlockTransformationsRotate.get()).equalsIgnoreCase("Rotate2")) {
            GlStateManager.func_179114_b((float)this.delay, (float)1.0f, (float)1.0f, (float)0.0f);
        }
        if (((String)Animations.transformFirstPersonRotate.get()).equalsIgnoreCase("Custom")) {
            GlStateManager.func_179114_b((float)this.delay, (float)((Integer)Animations.customRotate1.get()).floatValue(), (float)((Integer)Animations.customRotate2.get()).floatValue(), (float)((Integer)Animations.customRotate3.get()).floatValue());
        }
        if (this.rotateTimer.hasTimePassed(1L)) {
            this.delay += 1.0f;
            this.delay += ((Float)Animations.SpeedRotate.get()).floatValue();
            this.rotateTimer.reset();
        }
        if (this.delay > 360.0f) {
            this.delay = 0.0f;
        }
    }

    @Inject(method={"renderFireInFirstPerson"}, at={@At(value="HEAD")}, cancellable=true)
    private void renderFireInFirstPerson(CallbackInfo callbackInfo) {
        AntiBlind antiBlind = (AntiBlind)Client.moduleManager.getModule(AntiBlind.class);
        if (antiBlind.getState() && ((Boolean)antiBlind.getFireEffect().get()).booleanValue()) {
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

