/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.gui.FontRenderer
 *  net.minecraft.client.renderer.texture.TextureManager
 *  net.minecraft.util.ResourceLocation
 */
package net.dev.important.injection.forge.mixins.accessors;

import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.util.ResourceLocation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(value={FontRenderer.class})
public interface FontRendererAccessor {
    @Accessor
    public ResourceLocation getLocationFontTexture();

    @Accessor
    public TextureManager getRenderEngine();

    @Accessor
    public float getPosX();

    @Accessor
    public float getPosY();

    @Accessor
    public void setPosX(float var1);

    @Accessor
    public void setPosY(float var1);

    @Accessor
    public float getRed();

    @Accessor(value="green")
    public float getBlue();

    @Accessor(value="blue")
    public float getGreen();

    @Accessor
    public float getAlpha();

    @Accessor
    public void setStrikethroughStyle(boolean var1);

    @Accessor
    public void setUnderlineStyle(boolean var1);

    @Accessor
    public void setItalicStyle(boolean var1);

    @Accessor
    public void setRandomStyle(boolean var1);

    @Accessor
    public void setBoldStyle(boolean var1);

    @Accessor
    public boolean isRandomStyle();

    @Accessor
    public boolean isItalicStyle();

    @Accessor
    public boolean isBoldStyle();

    @Accessor
    public boolean isStrikethroughStyle();

    @Accessor
    public boolean isUnderlineStyle();

    @Accessor
    public byte[] getGlyphWidth();

    @Accessor
    public void setTextColor(int var1);

    @Accessor
    public int[] getColorCode();

    @Invoker
    public int callRenderString(String var1, float var2, float var3, int var4, boolean var5);
}

