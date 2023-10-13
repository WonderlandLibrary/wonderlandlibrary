/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  org.lwjgl.Sys
 */
package shadersmod.client;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.settings.GameSettings;
import optifine.Config;
import optifine.Lang;
import org.lwjgl.Sys;
import shadersmod.client.EnumShaderOption;
import shadersmod.client.GuiButtonEnumShaderOption;
import shadersmod.client.GuiShaderOptions;
import shadersmod.client.GuiSlotShaders;
import shadersmod.client.Shaders;
import shadersmod.client.ShadersTex;

public class GuiShaders
extends GuiScreen {
    protected GuiScreen parentGui;
    protected String screenTitle = "Shaders";
    private int updateTimer = -1;
    private GuiSlotShaders shaderList;
    private static float[] QUALITY_MULTIPLIERS = new float[]{0.5f, 0.70710677f, 1.0f, 1.4142135f, 2.0f};
    private static String[] QUALITY_MULTIPLIER_NAMES = new String[]{"0.5x", "0.7x", "1x", "1.5x", "2x"};
    private static float[] HAND_DEPTH_VALUES = new float[]{0.0625f, 0.125f, 0.25f};
    private static String[] HAND_DEPTH_NAMES = new String[]{"0.5x", "1x", "2x"};
    public static final int EnumOS_UNKNOWN = 0;
    public static final int EnumOS_WINDOWS = 1;
    public static final int EnumOS_OSX = 2;
    public static final int EnumOS_SOLARIS = 3;
    public static final int EnumOS_LINUX = 4;

    public GuiShaders(GuiScreen par1GuiScreen, GameSettings par2GameSettings) {
        this.parentGui = par1GuiScreen;
    }

    @Override
    public void initGui() {
        this.screenTitle = I18n.format("of.options.shadersTitle", new Object[0]);
        if (Shaders.shadersConfig == null) {
            Shaders.loadConfig();
        }
        int btnWidth = 120;
        int btnHeight = 20;
        int btnX = this.width - btnWidth - 10;
        int baseY = 30;
        int stepY = 20;
        int shaderListWidth = this.width - btnWidth - 20;
        this.shaderList = new GuiSlotShaders(this, shaderListWidth, this.height, baseY, this.height - 50, 16);
        this.shaderList.registerScrollButtons(7, 8);
        this.buttonList.add(new GuiButtonEnumShaderOption(EnumShaderOption.ANTIALIASING, btnX, 0 * stepY + baseY, btnWidth, btnHeight));
        this.buttonList.add(new GuiButtonEnumShaderOption(EnumShaderOption.NORMAL_MAP, btnX, 1 * stepY + baseY, btnWidth, btnHeight));
        this.buttonList.add(new GuiButtonEnumShaderOption(EnumShaderOption.SPECULAR_MAP, btnX, 2 * stepY + baseY, btnWidth, btnHeight));
        this.buttonList.add(new GuiButtonEnumShaderOption(EnumShaderOption.RENDER_RES_MUL, btnX, 3 * stepY + baseY, btnWidth, btnHeight));
        this.buttonList.add(new GuiButtonEnumShaderOption(EnumShaderOption.SHADOW_RES_MUL, btnX, 4 * stepY + baseY, btnWidth, btnHeight));
        this.buttonList.add(new GuiButtonEnumShaderOption(EnumShaderOption.HAND_DEPTH_MUL, btnX, 5 * stepY + baseY, btnWidth, btnHeight));
        this.buttonList.add(new GuiButtonEnumShaderOption(EnumShaderOption.OLD_LIGHTING, btnX, 6 * stepY + baseY, btnWidth, btnHeight));
        int btnFolderWidth = Math.min(150, shaderListWidth / 2 - 10);
        this.buttonList.add(new GuiButton(201, shaderListWidth / 4 - btnFolderWidth / 2, this.height - 25, btnFolderWidth, btnHeight, Lang.get("of.options.shaders.shadersFolder")));
        this.buttonList.add(new GuiButton(202, shaderListWidth / 4 * 3 - btnFolderWidth / 2, this.height - 25, btnFolderWidth, btnHeight, I18n.format("gui.done", new Object[0])));
        this.buttonList.add(new GuiButton(203, btnX, this.height - 25, btnWidth, btnHeight, Lang.get("of.options.shaders.shaderOptions")));
        this.updateButtons();
    }

    public void updateButtons() {
        boolean shaderActive = Config.isShaders();
        for (GuiButton button : this.buttonList) {
            if (button.id == 201 || button.id == 202 || button.id == EnumShaderOption.ANTIALIASING.ordinal()) continue;
            button.enabled = shaderActive;
        }
    }

    @Override
    public void handleMouseInput() throws IOException {
        super.handleMouseInput();
        this.shaderList.func_178039_p();
    }

    @Override
    protected void actionPerformed(GuiButton button) {
        if (button.enabled) {
            if (button instanceof GuiButtonEnumShaderOption) {
                GuiButtonEnumShaderOption var12 = (GuiButtonEnumShaderOption)button;
                switch (NamelessClass1647571870.$SwitchMap$shadersmod$client$EnumShaderOption[var12.getEnumShaderOption().ordinal()]) {
                    case 1: {
                        Shaders.nextAntialiasingLevel();
                        Shaders.uninit();
                        break;
                    }
                    case 2: {
                        Shaders.configNormalMap = !Shaders.configNormalMap;
                        this.mc.func_175603_A();
                        break;
                    }
                    case 3: {
                        Shaders.configSpecularMap = !Shaders.configSpecularMap;
                        this.mc.func_175603_A();
                        break;
                    }
                    case 4: {
                        float var13 = Shaders.configRenderResMul;
                        float[] var15 = QUALITY_MULTIPLIERS;
                        String[] names = QUALITY_MULTIPLIER_NAMES;
                        int index = GuiShaders.getValueIndex(var13, var15);
                        if (GuiShaders.isShiftKeyDown()) {
                            if (--index < 0) {
                                index = var15.length - 1;
                            }
                        } else if (++index >= var15.length) {
                            index = 0;
                        }
                        Shaders.configRenderResMul = var15[index];
                        Shaders.scheduleResize();
                        break;
                    }
                    case 5: {
                        float var13 = Shaders.configShadowResMul;
                        float[] var15 = QUALITY_MULTIPLIERS;
                        String[] names = QUALITY_MULTIPLIER_NAMES;
                        int index = GuiShaders.getValueIndex(var13, var15);
                        if (GuiShaders.isShiftKeyDown()) {
                            if (--index < 0) {
                                index = var15.length - 1;
                            }
                        } else if (++index >= var15.length) {
                            index = 0;
                        }
                        Shaders.configShadowResMul = var15[index];
                        Shaders.scheduleResizeShadow();
                        break;
                    }
                    case 6: {
                        float var13 = Shaders.configHandDepthMul;
                        float[] var15 = HAND_DEPTH_VALUES;
                        String[] names = HAND_DEPTH_NAMES;
                        int index = GuiShaders.getValueIndex(var13, var15);
                        if (GuiShaders.isShiftKeyDown()) {
                            if (--index < 0) {
                                index = var15.length - 1;
                            }
                        } else if (++index >= var15.length) {
                            index = 0;
                        }
                        Shaders.configHandDepthMul = var15[index];
                        break;
                    }
                    case 7: {
                        Shaders.configCloudShadow = !Shaders.configCloudShadow;
                        break;
                    }
                    case 8: {
                        Shaders.configOldLighting.nextValue();
                        Shaders.updateBlockLightLevel();
                        this.mc.func_175603_A();
                        break;
                    }
                    case 9: {
                        Shaders.configTweakBlockDamage = !Shaders.configTweakBlockDamage;
                        break;
                    }
                    case 10: {
                        Shaders.configTexMinFilN = Shaders.configTexMinFilS = (Shaders.configTexMinFilB = (Shaders.configTexMinFilB + 1) % 3);
                        button.displayString = "Tex Min: " + Shaders.texMinFilDesc[Shaders.configTexMinFilB];
                        ShadersTex.updateTextureMinMagFilter();
                        break;
                    }
                    case 11: {
                        Shaders.configTexMagFilN = (Shaders.configTexMagFilN + 1) % 2;
                        button.displayString = "Tex_n Mag: " + Shaders.texMagFilDesc[Shaders.configTexMagFilN];
                        ShadersTex.updateTextureMinMagFilter();
                        break;
                    }
                    case 12: {
                        Shaders.configTexMagFilS = (Shaders.configTexMagFilS + 1) % 2;
                        button.displayString = "Tex_s Mag: " + Shaders.texMagFilDesc[Shaders.configTexMagFilS];
                        ShadersTex.updateTextureMinMagFilter();
                        break;
                    }
                    case 13: {
                        Shaders.configShadowClipFrustrum = !Shaders.configShadowClipFrustrum;
                        button.displayString = "ShadowClipFrustrum: " + GuiShaders.toStringOnOff(Shaders.configShadowClipFrustrum);
                        ShadersTex.updateTextureMinMagFilter();
                    }
                }
                var12.updateButtonText();
            } else {
                switch (button.id) {
                    case 201: {
                        switch (GuiShaders.getOSType()) {
                            case 1: {
                                String gbeso = String.format("cmd.exe /C start \"Open file\" \"%s\"", Shaders.shaderpacksdir.getAbsolutePath());
                                try {
                                    Runtime.getRuntime().exec(gbeso);
                                    return;
                                }
                                catch (IOException var9) {
                                    var9.printStackTrace();
                                    break;
                                }
                            }
                            case 2: {
                                try {
                                    Runtime.getRuntime().exec(new String[]{"/usr/bin/open", Shaders.shaderpacksdir.getAbsolutePath()});
                                    return;
                                }
                                catch (IOException var10) {
                                    var10.printStackTrace();
                                }
                            }
                        }
                        boolean var11 = false;
                        try {
                            Class<?> val = Class.forName("java.awt.Desktop");
                            Object var14 = val.getMethod("getDesktop", new Class[0]).invoke(null, new Object[0]);
                            val.getMethod("browse", URI.class).invoke(var14, new File(this.mc.mcDataDir, Shaders.shaderpacksdirname).toURI());
                        }
                        catch (Throwable var8) {
                            var8.printStackTrace();
                            var11 = true;
                        }
                        if (!var11) break;
                        Config.dbg("Opening via system class!");
                        Sys.openURL((String)("file://" + Shaders.shaderpacksdir.getAbsolutePath()));
                        break;
                    }
                    case 202: {
                        new File(Shaders.shadersdir, "current.cfg");
                        try {
                            Shaders.storeConfig();
                        }
                        catch (Exception var8) {
                            // empty catch block
                        }
                        this.mc.displayGuiScreen(this.parentGui);
                        break;
                    }
                    case 203: {
                        GuiShaderOptions values = new GuiShaderOptions(this, Config.getGameSettings());
                        Config.getMinecraft().displayGuiScreen(values);
                        break;
                    }
                    default: {
                        this.shaderList.actionPerformed(button);
                    }
                }
            }
        }
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        this.drawDefaultBackground();
        this.shaderList.drawScreen(mouseX, mouseY, partialTicks);
        if (this.updateTimer <= 0) {
            this.shaderList.updateList();
            this.updateTimer += 20;
        }
        this.drawCenteredString(this.fontRendererObj, String.valueOf(this.screenTitle) + " ", this.width / 2, 15.0f, 0xFFFFFF);
        String info = "OpenGL: " + Shaders.glVersionString + ", " + Shaders.glVendorString + ", " + Shaders.glRendererString;
        int infoWidth = this.fontRendererObj.getStringWidth(info);
        if (infoWidth < this.width - 5) {
            this.drawCenteredString(this.fontRendererObj, info, this.width / 2, this.height - 40, 0x808080);
        } else {
            this.drawString(this.fontRendererObj, info, 5, this.height - 40, 0x808080);
        }
        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    @Override
    public void updateScreen() {
        super.updateScreen();
        --this.updateTimer;
    }

    public Minecraft getMc() {
        return this.mc;
    }

    public void drawCenteredString(String text, int x, int y, int color) {
        this.drawCenteredString(this.fontRendererObj, text, x, y, color);
    }

    public static String toStringOnOff(boolean value) {
        String on = Lang.getOn();
        String off = Lang.getOff();
        return value ? on : off;
    }

    public static String toStringAa(int value) {
        return value == 2 ? "FXAA 2x" : (value == 4 ? "FXAA 4x" : Lang.getOff());
    }

    public static String toStringValue(float val, float[] values, String[] names) {
        int index = GuiShaders.getValueIndex(val, values);
        return names[index];
    }

    public static int getValueIndex(float val, float[] values) {
        for (int i = 0; i < values.length; ++i) {
            float value = values[i];
            if (!(value >= val)) continue;
            return i;
        }
        return values.length - 1;
    }

    public static String toStringQuality(float val) {
        return GuiShaders.toStringValue(val, QUALITY_MULTIPLIERS, QUALITY_MULTIPLIER_NAMES);
    }

    public static String toStringHandDepth(float val) {
        return GuiShaders.toStringValue(val, HAND_DEPTH_VALUES, HAND_DEPTH_NAMES);
    }

    public static int getOSType() {
        String osName = System.getProperty("os.name").toLowerCase();
        return osName.contains("win") ? 1 : (osName.contains("mac") ? 2 : (osName.contains("solaris") ? 3 : (osName.contains("sunos") ? 3 : (osName.contains("linux") ? 4 : (osName.contains("unix") ? 4 : 0)))));
    }

    static class NamelessClass1647571870 {
        static final int[] $SwitchMap$shadersmod$client$EnumShaderOption = new int[EnumShaderOption.values().length];

        static {
            try {
                NamelessClass1647571870.$SwitchMap$shadersmod$client$EnumShaderOption[EnumShaderOption.ANTIALIASING.ordinal()] = 1;
            }
            catch (NoSuchFieldError noSuchFieldError) {
                // empty catch block
            }
            try {
                NamelessClass1647571870.$SwitchMap$shadersmod$client$EnumShaderOption[EnumShaderOption.NORMAL_MAP.ordinal()] = 2;
            }
            catch (NoSuchFieldError noSuchFieldError) {
                // empty catch block
            }
            try {
                NamelessClass1647571870.$SwitchMap$shadersmod$client$EnumShaderOption[EnumShaderOption.SPECULAR_MAP.ordinal()] = 3;
            }
            catch (NoSuchFieldError noSuchFieldError) {
                // empty catch block
            }
            try {
                NamelessClass1647571870.$SwitchMap$shadersmod$client$EnumShaderOption[EnumShaderOption.RENDER_RES_MUL.ordinal()] = 4;
            }
            catch (NoSuchFieldError noSuchFieldError) {
                // empty catch block
            }
            try {
                NamelessClass1647571870.$SwitchMap$shadersmod$client$EnumShaderOption[EnumShaderOption.SHADOW_RES_MUL.ordinal()] = 5;
            }
            catch (NoSuchFieldError noSuchFieldError) {
                // empty catch block
            }
            try {
                NamelessClass1647571870.$SwitchMap$shadersmod$client$EnumShaderOption[EnumShaderOption.HAND_DEPTH_MUL.ordinal()] = 6;
            }
            catch (NoSuchFieldError noSuchFieldError) {
                // empty catch block
            }
            try {
                NamelessClass1647571870.$SwitchMap$shadersmod$client$EnumShaderOption[EnumShaderOption.CLOUD_SHADOW.ordinal()] = 7;
            }
            catch (NoSuchFieldError noSuchFieldError) {
                // empty catch block
            }
            try {
                NamelessClass1647571870.$SwitchMap$shadersmod$client$EnumShaderOption[EnumShaderOption.OLD_LIGHTING.ordinal()] = 8;
            }
            catch (NoSuchFieldError noSuchFieldError) {
                // empty catch block
            }
            try {
                NamelessClass1647571870.$SwitchMap$shadersmod$client$EnumShaderOption[EnumShaderOption.TWEAK_BLOCK_DAMAGE.ordinal()] = 9;
            }
            catch (NoSuchFieldError noSuchFieldError) {
                // empty catch block
            }
            try {
                NamelessClass1647571870.$SwitchMap$shadersmod$client$EnumShaderOption[EnumShaderOption.TEX_MIN_FIL_B.ordinal()] = 10;
            }
            catch (NoSuchFieldError noSuchFieldError) {
                // empty catch block
            }
            try {
                NamelessClass1647571870.$SwitchMap$shadersmod$client$EnumShaderOption[EnumShaderOption.TEX_MAG_FIL_N.ordinal()] = 11;
            }
            catch (NoSuchFieldError noSuchFieldError) {
                // empty catch block
            }
            try {
                NamelessClass1647571870.$SwitchMap$shadersmod$client$EnumShaderOption[EnumShaderOption.TEX_MAG_FIL_S.ordinal()] = 12;
            }
            catch (NoSuchFieldError noSuchFieldError) {
                // empty catch block
            }
            try {
                NamelessClass1647571870.$SwitchMap$shadersmod$client$EnumShaderOption[EnumShaderOption.SHADOW_CLIP_FRUSTRUM.ordinal()] = 13;
            }
            catch (NoSuchFieldError noSuchFieldError) {
                // empty catch block
            }
        }

        NamelessClass1647571870() {
        }
    }
}

