/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.renderer.GlStateManager
 *  net.minecraft.client.renderer.entity.RenderItem
 *  net.minecraft.client.renderer.texture.TextureManager
 *  net.minecraft.client.renderer.texture.TextureMap
 *  net.minecraft.client.resources.model.IBakedModel
 *  net.minecraft.util.ResourceLocation
 */
package net.dev.important.injection.forge.mixins.render;

import java.awt.Color;
import net.dev.important.Client;
import net.dev.important.modules.module.modules.color.ColorMixer;
import net.dev.important.modules.module.modules.render.EnchantEffect;
import net.dev.important.utils.render.RenderUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.resources.model.IBakedModel;
import net.minecraft.util.ResourceLocation;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value={RenderItem.class})
public abstract class MixinRenderItem {
    @Final
    @Shadow
    private TextureManager field_175057_n;
    @Final
    @Shadow
    private static ResourceLocation field_110798_h;

    @Shadow
    public abstract void func_175035_a(IBakedModel var1, int var2);

    @Inject(method={"renderEffect"}, at={@At(value="HEAD")})
    private void renderEffect(IBakedModel model, CallbackInfo callbackInfo) {
        EnchantEffect enchantEffect = (EnchantEffect)Client.moduleManager.getModule(EnchantEffect.class);
        int rainbowColour = RenderUtils.getRainbowOpaque((Integer)enchantEffect.rainbowSpeedValue.get(), ((Float)enchantEffect.rainbowSatValue.get()).floatValue(), ((Float)enchantEffect.rainbowBrgValue.get()).floatValue(), (int)Minecraft.func_71386_F() % 2 * ((Integer)enchantEffect.rainbowDelayValue.get() * 10));
        int skyColor = RenderUtils.SkyRainbow(0, ((Float)enchantEffect.rainbowSatValue.get()).floatValue(), ((Float)enchantEffect.rainbowBrgValue.get()).floatValue());
        int mixerColor = ColorMixer.getMixedColor(0, (Integer)enchantEffect.rainbowSpeedValue.get()).getRGB();
        int currentColor = new Color((Integer)enchantEffect.redValue.get(), (Integer)enchantEffect.greenValue.get(), (Integer)enchantEffect.blueValue.get()).getRGB();
        GlStateManager.func_179132_a((boolean)false);
        GlStateManager.func_179143_c((int)514);
        GlStateManager.func_179140_f();
        GlStateManager.func_179112_b((int)768, (int)1);
        this.field_175057_n.func_110577_a(field_110798_h);
        GlStateManager.func_179128_n((int)5890);
        GlStateManager.func_179094_E();
        GlStateManager.func_179152_a((float)8.0f, (float)8.0f, (float)8.0f);
        float f = (float)(Minecraft.func_71386_F() % 3000L) / 3000.0f / 8.0f;
        GlStateManager.func_179109_b((float)f, (float)0.0f, (float)0.0f);
        GlStateManager.func_179114_b((float)-50.0f, (float)0.0f, (float)0.0f, (float)1.0f);
        if (enchantEffect.getState()) {
            switch (((String)enchantEffect.modeValue.get()).toLowerCase()) {
                case "custom": {
                    this.func_175035_a(model, currentColor);
                    break;
                }
                case "rainbow": {
                    this.func_175035_a(model, rainbowColour);
                    break;
                }
                case "sky": {
                    this.func_175035_a(model, skyColor);
                }
                case "mixer": {
                    this.func_175035_a(model, mixerColor);
                }
            }
        } else {
            this.func_175035_a(model, -8372020);
        }
        GlStateManager.func_179121_F();
        GlStateManager.func_179094_E();
        GlStateManager.func_179152_a((float)8.0f, (float)8.0f, (float)8.0f);
        float f1 = (float)(Minecraft.func_71386_F() % 4873L) / 4873.0f / 8.0f;
        GlStateManager.func_179109_b((float)(-f1), (float)0.0f, (float)0.0f);
        GlStateManager.func_179114_b((float)10.0f, (float)0.0f, (float)0.0f, (float)1.0f);
        if (enchantEffect.getState()) {
            switch (((String)enchantEffect.modeValue.get()).toLowerCase()) {
                case "custom": {
                    this.func_175035_a(model, currentColor);
                    break;
                }
                case "rainbow": {
                    this.func_175035_a(model, rainbowColour);
                    break;
                }
                case "sky": {
                    this.func_175035_a(model, skyColor);
                }
                case "mixer": {
                    this.func_175035_a(model, mixerColor);
                }
            }
        } else {
            this.func_175035_a(model, -8372020);
        }
        GlStateManager.func_179121_F();
        GlStateManager.func_179128_n((int)5888);
        GlStateManager.func_179112_b((int)770, (int)771);
        GlStateManager.func_179145_e();
        GlStateManager.func_179143_c((int)515);
        GlStateManager.func_179132_a((boolean)true);
        this.field_175057_n.func_110577_a(TextureMap.field_110575_b);
    }
}

