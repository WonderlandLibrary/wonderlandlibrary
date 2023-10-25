package net.optifine.shaders.gui;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.resources.I18n;
import net.optifine.shaders.Shaders;
import net.optifine.shaders.config.EnumShaderOption;
import net.optifine.shaders.gui.GuiButtonEnumShaderOption;
import net.optifine.shaders.gui.GuiShaders;

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
        String s = I18n.format((String)eso.getResourceKey(), (Object[])new Object[0]) + ": ";
        switch (1.$SwitchMap$net$optifine$shaders$config$EnumShaderOption[eso.ordinal()]) {
            case 1: {
                return s + GuiShaders.toStringAa((int)Shaders.configAntialiasingLevel);
            }
            case 2: {
                return s + GuiShaders.toStringOnOff((boolean)Shaders.configNormalMap);
            }
            case 3: {
                return s + GuiShaders.toStringOnOff((boolean)Shaders.configSpecularMap);
            }
            case 4: {
                return s + GuiShaders.toStringQuality((float)Shaders.configRenderResMul);
            }
            case 5: {
                return s + GuiShaders.toStringQuality((float)Shaders.configShadowResMul);
            }
            case 6: {
                return s + GuiShaders.toStringHandDepth((float)Shaders.configHandDepthMul);
            }
            case 7: {
                return s + GuiShaders.toStringOnOff((boolean)Shaders.configCloudShadow);
            }
            case 8: {
                return s + Shaders.configOldHandLight.getUserValue();
            }
            case 9: {
                return s + Shaders.configOldLighting.getUserValue();
            }
            case 10: {
                return s + GuiShaders.toStringOnOff((boolean)Shaders.configShadowClipFrustrum);
            }
            case 11: {
                return s + GuiShaders.toStringOnOff((boolean)Shaders.configTweakBlockDamage);
            }
        }
        return s + Shaders.getEnumShaderOption((EnumShaderOption)eso);
    }

    public void updateButtonText() {
        this.displayString = GuiButtonEnumShaderOption.getButtonText(this.enumShaderOption);
    }
}
