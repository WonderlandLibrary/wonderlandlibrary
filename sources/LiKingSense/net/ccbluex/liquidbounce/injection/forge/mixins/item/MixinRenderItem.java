/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.renderer.GlStateManager
 *  net.minecraft.client.renderer.RenderItem
 *  net.minecraft.client.renderer.block.model.IBakedModel
 *  net.minecraft.client.renderer.texture.TextureManager
 *  net.minecraft.client.renderer.texture.TextureMap
 *  net.minecraft.client.resources.IResourceManager
 *  net.minecraft.client.resources.IResourceManagerReloadListener
 *  net.minecraft.util.ResourceLocation
 *  net.minecraftforge.fml.relauncher.Side
 *  net.minecraftforge.fml.relauncher.SideOnly
 *  org.spongepowered.asm.mixin.Final
 *  org.spongepowered.asm.mixin.Mixin
 *  org.spongepowered.asm.mixin.Overwrite
 *  org.spongepowered.asm.mixin.Shadow
 */
package net.ccbluex.liquidbounce.injection.forge.mixins.item;

import java.awt.Color;
import net.ccbluex.liquidbounce.LiquidBounce;
import net.ccbluex.liquidbounce.features.module.modules.render.EnchantEffect;
import net.ccbluex.liquidbounce.utils.render.ColorUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.client.resources.IResourceManagerReloadListener;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

@SideOnly(value=Side.CLIENT)
@Mixin(value={RenderItem.class})
public abstract class MixinRenderItem
implements IResourceManagerReloadListener {
    @Final
    @Shadow
    private static final ResourceLocation field_110798_h = new ResourceLocation("textures/misc/enchanted_item_glint.png");
    @Final
    @Shadow
    private TextureManager field_175057_n;

    @Shadow
    public abstract void func_110549_a(IResourceManager var1);

    @Shadow
    protected abstract void func_191965_a(IBakedModel var1, int var2);

    @Overwrite
    private void func_191966_a(IBakedModel p_renderEffect_1_) {
        EnchantEffect effect = (EnchantEffect)LiquidBounce.moduleManager.get(EnchantEffect.class);
        Color color = (Boolean)effect.getRainbow().get() != false ? ColorUtils.rainbow() : new Color((Integer)effect.getRedValue().get(), (Integer)effect.getGreenValue().get(), (Integer)effect.getBlueValue().get(), (Integer)effect.getalphaValue().get());
        GlStateManager.func_179132_a((boolean)false);
        GlStateManager.func_179143_c((int)514);
        GlStateManager.func_179140_f();
        int n = 0;
        this.field_175057_n.func_110577_a(field_110798_h);
        GlStateManager.func_179128_n((int)5890);
        GlStateManager.func_179094_E();
        GlStateManager.func_179152_a((float)8.0f, (float)8.0f, (float)8.0f);
        float f = (float)(Minecraft.func_71386_F() % 3000L) / 3000.0f / 8.0f;
        GlStateManager.func_179109_b((float)f, (float)0.0f, (float)0.0f);
        GlStateManager.func_179114_b((float)-50.0f, (float)0.0f, (float)0.0f, (float)1.0f);
        if (effect.getState()) {
            this.func_191965_a(p_renderEffect_1_, color.getRGB());
        } else {
            this.func_191965_a(p_renderEffect_1_, -8372020);
        }
        GlStateManager.func_179121_F();
        GlStateManager.func_179094_E();
        GlStateManager.func_179152_a((float)8.0f, (float)8.0f, (float)8.0f);
        float f1 = (float)(Minecraft.func_71386_F() % 4873L) / 4873.0f / 8.0f;
        GlStateManager.func_179109_b((float)(-f1), (float)0.0f, (float)0.0f);
        GlStateManager.func_179114_b((float)10.0f, (float)0.0f, (float)0.0f, (float)1.0f);
        if (effect.getState()) {
            this.func_191965_a(p_renderEffect_1_, color.getRGB());
        } else {
            this.func_191965_a(p_renderEffect_1_, -8372020);
        }
        GlStateManager.func_179121_F();
        GlStateManager.func_179128_n((int)5888);
        int n2 = 0;
        GlStateManager.func_179145_e();
        GlStateManager.func_179143_c((int)515);
        GlStateManager.func_179132_a((boolean)true);
        this.field_175057_n.func_110577_a(TextureMap.field_110575_b);
    }
}

