/*
 * Decompiled with CFR 0.150.
 */
package shadersmod.client;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.resources.I18n;
import shadersmod.client.EnumShaderOption;
import shadersmod.client.GuiShaders;
import shadersmod.client.Shaders;

public class GuiButtonEnumShaderOption
extends GuiButton {
    private EnumShaderOption enumShaderOption = null;

    public GuiButtonEnumShaderOption(EnumShaderOption enumShaderOption, int x, int y, int widthIn, int heightIn) {
        super(enumShaderOption.ordinal(), x, y, widthIn, heightIn, GuiButtonEnumShaderOption.getButtonText(enumShaderOption));
        this.enumShaderOption = enumShaderOption;
    }

    public EnumShaderOption getEnumShaderOption() {
        return this.enumShaderOption;
    }

    private static String getButtonText(EnumShaderOption eso) {
        String nameText = String.valueOf(I18n.format(eso.getResourceKey(), new Object[0])) + ": ";
        switch (NamelessClass895471824.$SwitchMap$shadersmod$client$EnumShaderOption[eso.ordinal()]) {
            case 1: {
                return String.valueOf(nameText) + GuiShaders.toStringAa(Shaders.configAntialiasingLevel);
            }
            case 2: {
                return String.valueOf(nameText) + GuiShaders.toStringOnOff(Shaders.configNormalMap);
            }
            case 3: {
                return String.valueOf(nameText) + GuiShaders.toStringOnOff(Shaders.configSpecularMap);
            }
            case 4: {
                return String.valueOf(nameText) + GuiShaders.toStringQuality(Shaders.configRenderResMul);
            }
            case 5: {
                return String.valueOf(nameText) + GuiShaders.toStringQuality(Shaders.configShadowResMul);
            }
            case 6: {
                return String.valueOf(nameText) + GuiShaders.toStringHandDepth(Shaders.configHandDepthMul);
            }
            case 7: {
                return String.valueOf(nameText) + GuiShaders.toStringOnOff(Shaders.configCloudShadow);
            }
            case 8: {
                return String.valueOf(nameText) + Shaders.configOldLighting.getUserValue();
            }
            case 9: {
                return String.valueOf(nameText) + GuiShaders.toStringOnOff(Shaders.configShadowClipFrustrum);
            }
            case 10: {
                return String.valueOf(nameText) + GuiShaders.toStringOnOff(Shaders.configTweakBlockDamage);
            }
        }
        return String.valueOf(nameText) + Shaders.getEnumShaderOption(eso);
    }

    public void updateButtonText() {
        this.displayString = GuiButtonEnumShaderOption.getButtonText(this.enumShaderOption);
    }

    static class NamelessClass895471824 {
        static final int[] $SwitchMap$shadersmod$client$EnumShaderOption = new int[EnumShaderOption.values().length];

        static {
            try {
                NamelessClass895471824.$SwitchMap$shadersmod$client$EnumShaderOption[EnumShaderOption.ANTIALIASING.ordinal()] = 1;
            }
            catch (NoSuchFieldError noSuchFieldError) {
                // empty catch block
            }
            try {
                NamelessClass895471824.$SwitchMap$shadersmod$client$EnumShaderOption[EnumShaderOption.NORMAL_MAP.ordinal()] = 2;
            }
            catch (NoSuchFieldError noSuchFieldError) {
                // empty catch block
            }
            try {
                NamelessClass895471824.$SwitchMap$shadersmod$client$EnumShaderOption[EnumShaderOption.SPECULAR_MAP.ordinal()] = 3;
            }
            catch (NoSuchFieldError noSuchFieldError) {
                // empty catch block
            }
            try {
                NamelessClass895471824.$SwitchMap$shadersmod$client$EnumShaderOption[EnumShaderOption.RENDER_RES_MUL.ordinal()] = 4;
            }
            catch (NoSuchFieldError noSuchFieldError) {
                // empty catch block
            }
            try {
                NamelessClass895471824.$SwitchMap$shadersmod$client$EnumShaderOption[EnumShaderOption.SHADOW_RES_MUL.ordinal()] = 5;
            }
            catch (NoSuchFieldError noSuchFieldError) {
                // empty catch block
            }
            try {
                NamelessClass895471824.$SwitchMap$shadersmod$client$EnumShaderOption[EnumShaderOption.HAND_DEPTH_MUL.ordinal()] = 6;
            }
            catch (NoSuchFieldError noSuchFieldError) {
                // empty catch block
            }
            try {
                NamelessClass895471824.$SwitchMap$shadersmod$client$EnumShaderOption[EnumShaderOption.CLOUD_SHADOW.ordinal()] = 7;
            }
            catch (NoSuchFieldError noSuchFieldError) {
                // empty catch block
            }
            try {
                NamelessClass895471824.$SwitchMap$shadersmod$client$EnumShaderOption[EnumShaderOption.OLD_LIGHTING.ordinal()] = 8;
            }
            catch (NoSuchFieldError noSuchFieldError) {
                // empty catch block
            }
            try {
                NamelessClass895471824.$SwitchMap$shadersmod$client$EnumShaderOption[EnumShaderOption.SHADOW_CLIP_FRUSTRUM.ordinal()] = 9;
            }
            catch (NoSuchFieldError noSuchFieldError) {
                // empty catch block
            }
            try {
                NamelessClass895471824.$SwitchMap$shadersmod$client$EnumShaderOption[EnumShaderOption.TWEAK_BLOCK_DAMAGE.ordinal()] = 10;
            }
            catch (NoSuchFieldError noSuchFieldError) {
                // empty catch block
            }
        }

        NamelessClass895471824() {
        }
    }
}

