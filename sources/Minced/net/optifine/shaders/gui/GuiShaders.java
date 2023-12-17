// 
// Decompiled by Procyon v0.5.36
// 

package net.optifine.shaders.gui;

import net.minecraft.client.Minecraft;
import java.util.List;
import net.optifine.shaders.ShadersTex;
import org.lwjgl.Sys;
import java.io.File;
import java.net.URI;
import java.io.IOException;
import java.util.Iterator;
import net.minecraft.src.Config;
import net.minecraft.client.gui.GuiButton;
import net.optifine.Lang;
import net.optifine.shaders.config.EnumShaderOption;
import net.optifine.shaders.Shaders;
import net.minecraft.client.resources.I18n;
import net.optifine.gui.TooltipProvider;
import net.optifine.gui.TooltipProviderEnumShaderOptions;
import net.minecraft.client.settings.GameSettings;
import net.optifine.gui.TooltipManager;
import net.minecraft.client.gui.GuiScreen;
import net.optifine.gui.GuiScreenOF;

public class GuiShaders extends GuiScreenOF
{
    protected GuiScreen parentGui;
    protected String screenTitle;
    private TooltipManager tooltipManager;
    private int updateTimer;
    private GuiSlotShaders shaderList;
    private boolean saved;
    private static float[] QUALITY_MULTIPLIERS;
    private static String[] QUALITY_MULTIPLIER_NAMES;
    private static float QUALITY_MULTIPLIER_DEFAULT;
    private static float[] HAND_DEPTH_VALUES;
    private static String[] HAND_DEPTH_NAMES;
    private static float HAND_DEPTH_DEFAULT;
    public static final int EnumOS_UNKNOWN = 0;
    public static final int EnumOS_WINDOWS = 1;
    public static final int EnumOS_OSX = 2;
    public static final int EnumOS_SOLARIS = 3;
    public static final int EnumOS_LINUX = 4;
    
    public GuiShaders(final GuiScreen par1GuiScreen, final GameSettings par2GameSettings) {
        this.screenTitle = "Shaders";
        this.tooltipManager = new TooltipManager(this, new TooltipProviderEnumShaderOptions());
        this.updateTimer = -1;
        this.saved = false;
        this.parentGui = par1GuiScreen;
    }
    
    @Override
    public void initGui() {
        this.screenTitle = I18n.format("of.options.shadersTitle", new Object[0]);
        if (Shaders.shadersConfig == null) {
            Shaders.loadConfig();
        }
        final int i = 120;
        final int j = 20;
        final int k = this.width - i - 10;
        final int l = 30;
        final int i2 = 20;
        final int j2 = this.width - i - 20;
        (this.shaderList = new GuiSlotShaders(this, j2, this.height, l, this.height - 50, 16)).registerScrollButtons(7, 8);
        this.buttonList.add(new GuiButtonEnumShaderOption(EnumShaderOption.ANTIALIASING, k, 0 * i2 + l, i, j));
        this.buttonList.add(new GuiButtonEnumShaderOption(EnumShaderOption.NORMAL_MAP, k, 1 * i2 + l, i, j));
        this.buttonList.add(new GuiButtonEnumShaderOption(EnumShaderOption.SPECULAR_MAP, k, 2 * i2 + l, i, j));
        this.buttonList.add(new GuiButtonEnumShaderOption(EnumShaderOption.RENDER_RES_MUL, k, 3 * i2 + l, i, j));
        this.buttonList.add(new GuiButtonEnumShaderOption(EnumShaderOption.SHADOW_RES_MUL, k, 4 * i2 + l, i, j));
        this.buttonList.add(new GuiButtonEnumShaderOption(EnumShaderOption.HAND_DEPTH_MUL, k, 5 * i2 + l, i, j));
        this.buttonList.add(new GuiButtonEnumShaderOption(EnumShaderOption.OLD_HAND_LIGHT, k, 6 * i2 + l, i, j));
        this.buttonList.add(new GuiButtonEnumShaderOption(EnumShaderOption.OLD_LIGHTING, k, 7 * i2 + l, i, j));
        final int k2 = Math.min(150, j2 / 2 - 10);
        final int l2 = j2 / 4 - k2 / 2;
        final int i3 = this.height - 25;
        this.buttonList.add(new GuiButton(201, l2, i3, k2 - 22 + 1, j, Lang.get("of.options.shaders.shadersFolder")));
        this.buttonList.add(new GuiButtonDownloadShaders(210, l2 + k2 - 22 - 1, i3));
        this.buttonList.add(new GuiButton(202, j2 / 4 * 3 - k2 / 2, this.height - 25, k2, j, I18n.format("gui.done", new Object[0])));
        this.buttonList.add(new GuiButton(203, k, this.height - 25, i, j, Lang.get("of.options.shaders.shaderOptions")));
        this.updateButtons();
    }
    
    public void updateButtons() {
        final boolean flag = Config.isShaders();
        for (final GuiButton guibutton : this.buttonList) {
            if (guibutton.id != 201 && guibutton.id != 202 && guibutton.id != 210 && guibutton.id != EnumShaderOption.ANTIALIASING.ordinal()) {
                guibutton.enabled = flag;
            }
        }
    }
    
    @Override
    public void handleMouseInput() throws IOException {
        super.handleMouseInput();
        this.shaderList.handleMouseInput();
    }
    
    @Override
    protected void actionPerformed(final GuiButton button) {
        this.actionPerformed(button, false);
    }
    
    @Override
    protected void actionPerformedRightClick(final GuiButton button) {
        this.actionPerformed(button, true);
    }
    
    private void actionPerformed(final GuiButton button, final boolean rightClick) {
        if (button.enabled) {
            if (!(button instanceof GuiButtonEnumShaderOption)) {
                if (!rightClick) {
                    switch (button.id) {
                        case 201: {
                            switch (getOSType()) {
                                case 1: {
                                    final String s = String.format("cmd.exe /C start \"Open file\" \"%s\"", Shaders.shaderPacksDir.getAbsolutePath());
                                    try {
                                        Runtime.getRuntime().exec(s);
                                        return;
                                    }
                                    catch (IOException ioexception) {
                                        ioexception.printStackTrace();
                                        break;
                                    }
                                }
                                case 2: {
                                    try {
                                        Runtime.getRuntime().exec(new String[] { "/usr/bin/open", Shaders.shaderPacksDir.getAbsolutePath() });
                                        return;
                                    }
                                    catch (IOException ioexception2) {
                                        ioexception2.printStackTrace();
                                    }
                                    break;
                                }
                            }
                            boolean flag = false;
                            try {
                                final Class oclass1 = Class.forName("java.awt.Desktop");
                                final Object object1 = oclass1.getMethod("getDesktop", (Class[])new Class[0]).invoke(null, new Object[0]);
                                oclass1.getMethod("browse", URI.class).invoke(object1, new File(GuiShaders.mc.gameDir, "shaderpacks").toURI());
                            }
                            catch (Throwable throwable1) {
                                throwable1.printStackTrace();
                                flag = true;
                            }
                            if (flag) {
                                Config.dbg("Opening via system class!");
                                Sys.openURL("file://" + Shaders.shaderPacksDir.getAbsolutePath());
                            }
                            return;
                        }
                        case 202: {
                            Shaders.storeConfig();
                            this.saved = true;
                            GuiShaders.mc.displayGuiScreen(this.parentGui);
                            return;
                        }
                        case 203: {
                            final GuiShaderOptions guishaderoptions = new GuiShaderOptions(this, Config.getGameSettings());
                            Config.getMinecraft().displayGuiScreen(guishaderoptions);
                            return;
                        }
                        case 210: {
                            try {
                                final Class<?> oclass2 = Class.forName("java.awt.Desktop");
                                final Object object2 = oclass2.getMethod("getDesktop", (Class<?>[])new Class[0]).invoke(null, new Object[0]);
                                oclass2.getMethod("browse", URI.class).invoke(object2, new URI("http://optifine.net/shaderPacks"));
                            }
                            catch (Throwable throwable2) {
                                throwable2.printStackTrace();
                            }
                            break;
                        }
                    }
                    this.shaderList.actionPerformed(button);
                }
            }
            else {
                final GuiButtonEnumShaderOption guibuttonenumshaderoption = (GuiButtonEnumShaderOption)button;
                switch (guibuttonenumshaderoption.getEnumShaderOption()) {
                    case ANTIALIASING: {
                        Shaders.nextAntialiasingLevel(!rightClick);
                        if (this.hasShiftDown()) {
                            Shaders.configAntialiasingLevel = 0;
                        }
                        Shaders.uninit();
                        break;
                    }
                    case NORMAL_MAP: {
                        Shaders.configNormalMap = !Shaders.configNormalMap;
                        if (this.hasShiftDown()) {
                            Shaders.configNormalMap = true;
                        }
                        Shaders.uninit();
                        GuiShaders.mc.scheduleResourcesRefresh();
                        break;
                    }
                    case SPECULAR_MAP: {
                        Shaders.configSpecularMap = !Shaders.configSpecularMap;
                        if (this.hasShiftDown()) {
                            Shaders.configSpecularMap = true;
                        }
                        Shaders.uninit();
                        GuiShaders.mc.scheduleResourcesRefresh();
                        break;
                    }
                    case RENDER_RES_MUL: {
                        Shaders.configRenderResMul = this.getNextValue(Shaders.configRenderResMul, GuiShaders.QUALITY_MULTIPLIERS, GuiShaders.QUALITY_MULTIPLIER_DEFAULT, !rightClick, this.hasShiftDown());
                        Shaders.uninit();
                        Shaders.scheduleResize();
                        break;
                    }
                    case SHADOW_RES_MUL: {
                        Shaders.configShadowResMul = this.getNextValue(Shaders.configShadowResMul, GuiShaders.QUALITY_MULTIPLIERS, GuiShaders.QUALITY_MULTIPLIER_DEFAULT, !rightClick, this.hasShiftDown());
                        Shaders.uninit();
                        Shaders.scheduleResizeShadow();
                        break;
                    }
                    case HAND_DEPTH_MUL: {
                        Shaders.configHandDepthMul = this.getNextValue(Shaders.configHandDepthMul, GuiShaders.HAND_DEPTH_VALUES, GuiShaders.HAND_DEPTH_DEFAULT, !rightClick, this.hasShiftDown());
                        Shaders.uninit();
                        break;
                    }
                    case OLD_HAND_LIGHT: {
                        Shaders.configOldHandLight.nextValue(!rightClick);
                        if (this.hasShiftDown()) {
                            Shaders.configOldHandLight.resetValue();
                        }
                        Shaders.uninit();
                        break;
                    }
                    case OLD_LIGHTING: {
                        Shaders.configOldLighting.nextValue(!rightClick);
                        if (this.hasShiftDown()) {
                            Shaders.configOldLighting.resetValue();
                        }
                        Shaders.updateBlockLightLevel();
                        Shaders.uninit();
                        GuiShaders.mc.scheduleResourcesRefresh();
                        break;
                    }
                    case TWEAK_BLOCK_DAMAGE: {
                        Shaders.configTweakBlockDamage = !Shaders.configTweakBlockDamage;
                        break;
                    }
                    case CLOUD_SHADOW: {
                        Shaders.configCloudShadow = !Shaders.configCloudShadow;
                        break;
                    }
                    case TEX_MIN_FIL_B: {
                        Shaders.configTexMinFilB = (Shaders.configTexMinFilB + 1) % 3;
                        Shaders.configTexMinFilN = (Shaders.configTexMinFilS = Shaders.configTexMinFilB);
                        button.displayString = "Tex Min: " + Shaders.texMinFilDesc[Shaders.configTexMinFilB];
                        ShadersTex.updateTextureMinMagFilter();
                        break;
                    }
                    case TEX_MAG_FIL_N: {
                        Shaders.configTexMagFilN = (Shaders.configTexMagFilN + 1) % 2;
                        button.displayString = "Tex_n Mag: " + Shaders.texMagFilDesc[Shaders.configTexMagFilN];
                        ShadersTex.updateTextureMinMagFilter();
                        break;
                    }
                    case TEX_MAG_FIL_S: {
                        Shaders.configTexMagFilS = (Shaders.configTexMagFilS + 1) % 2;
                        button.displayString = "Tex_s Mag: " + Shaders.texMagFilDesc[Shaders.configTexMagFilS];
                        ShadersTex.updateTextureMinMagFilter();
                        break;
                    }
                    case SHADOW_CLIP_FRUSTRUM: {
                        Shaders.configShadowClipFrustrum = !Shaders.configShadowClipFrustrum;
                        button.displayString = "ShadowClipFrustrum: " + toStringOnOff(Shaders.configShadowClipFrustrum);
                        ShadersTex.updateTextureMinMagFilter();
                        break;
                    }
                }
                guibuttonenumshaderoption.updateButtonText();
            }
        }
    }
    
    @Override
    public void onGuiClosed() {
        super.onGuiClosed();
        if (!this.saved) {
            Shaders.storeConfig();
        }
    }
    
    @Override
    public void drawScreen(final int mouseX, final int mouseY, final float partialTicks) {
        this.drawDefaultBackground();
        this.shaderList.drawScreen(mouseX, mouseY, partialTicks);
        if (this.updateTimer <= 0) {
            this.shaderList.updateList();
            this.updateTimer += 20;
        }
        this.drawCenteredString(this.fontRenderer, this.screenTitle + " ", this.width / 2, 15, 16777215);
        final String s = "OpenGL: " + Shaders.glVersionString + ", " + Shaders.glVendorString + ", " + Shaders.glRendererString;
        final int i = this.fontRenderer.getStringWidth(s);
        if (i < this.width - 5) {
            this.drawCenteredString(this.fontRenderer, s, this.width / 2, this.height - 40, 8421504);
        }
        else {
            this.drawString(this.fontRenderer, s, 5, this.height - 40, 8421504);
        }
        super.drawScreen(mouseX, mouseY, partialTicks);
        this.tooltipManager.drawTooltips(mouseX, mouseY, this.buttonList);
    }
    
    @Override
    public void updateScreen() {
        super.updateScreen();
        --this.updateTimer;
    }
    
    public Minecraft getMc() {
        return GuiShaders.mc;
    }
    
    public void drawCenteredString(final String text, final int x, final int y, final int color) {
        this.drawCenteredString(this.fontRenderer, text, x, y, color);
    }
    
    public static String toStringOnOff(final boolean value) {
        final String s = Lang.getOn();
        final String s2 = Lang.getOff();
        return value ? s : s2;
    }
    
    public static String toStringAa(final int value) {
        if (value == 2) {
            return "FXAA 2x";
        }
        return (value == 4) ? "FXAA 4x" : Lang.getOff();
    }
    
    public static String toStringValue(final float val, final float[] values, final String[] names) {
        final int i = getValueIndex(val, values);
        return names[i];
    }
    
    private float getNextValue(final float val, final float[] values, final float valDef, final boolean forward, final boolean reset) {
        if (reset) {
            return valDef;
        }
        int i = getValueIndex(val, values);
        if (forward) {
            if (++i >= values.length) {
                i = 0;
            }
        }
        else if (--i < 0) {
            i = values.length - 1;
        }
        return values[i];
    }
    
    public static int getValueIndex(final float val, final float[] values) {
        for (int i = 0; i < values.length; ++i) {
            final float f = values[i];
            if (f >= val) {
                return i;
            }
        }
        return values.length - 1;
    }
    
    public static String toStringQuality(final float val) {
        return toStringValue(val, GuiShaders.QUALITY_MULTIPLIERS, GuiShaders.QUALITY_MULTIPLIER_NAMES);
    }
    
    public static String toStringHandDepth(final float val) {
        return toStringValue(val, GuiShaders.HAND_DEPTH_VALUES, GuiShaders.HAND_DEPTH_NAMES);
    }
    
    public static int getOSType() {
        final String s = System.getProperty("os.name").toLowerCase();
        if (s.contains("win")) {
            return 1;
        }
        if (s.contains("mac")) {
            return 2;
        }
        if (s.contains("solaris")) {
            return 3;
        }
        if (s.contains("sunos")) {
            return 3;
        }
        if (s.contains("linux")) {
            return 4;
        }
        return s.contains("unix") ? 4 : 0;
    }
    
    public boolean hasShiftDown() {
        return GuiScreen.isShiftKeyDown();
    }
    
    static {
        GuiShaders.QUALITY_MULTIPLIERS = new float[] { 0.5f, 0.6f, 0.6666667f, 0.75f, 0.8333333f, 0.9f, 1.0f, 1.1666666f, 1.3333334f, 1.5f, 1.6666666f, 1.8f, 2.0f };
        GuiShaders.QUALITY_MULTIPLIER_NAMES = new String[] { "0.5x", "0.6x", "0.66x", "0.75x", "0.83x", "0.9x", "1x", "1.16x", "1.33x", "1.5x", "1.66x", "1.8x", "2x" };
        GuiShaders.QUALITY_MULTIPLIER_DEFAULT = 1.0f;
        GuiShaders.HAND_DEPTH_VALUES = new float[] { 0.0625f, 0.125f, 0.25f };
        GuiShaders.HAND_DEPTH_NAMES = new String[] { "0.5x", "1x", "2x" };
        GuiShaders.HAND_DEPTH_DEFAULT = 0.125f;
    }
}
