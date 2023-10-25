package net.optifine.shaders.gui;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.src.Config;
import net.optifine.Lang;
import net.optifine.gui.GuiScreenOF;
import net.optifine.gui.TooltipManager;
import net.optifine.gui.TooltipProvider;
import net.optifine.gui.TooltipProviderEnumShaderOptions;
import net.optifine.shaders.Shaders;
import net.optifine.shaders.ShadersTex;
import net.optifine.shaders.config.EnumShaderOption;
import net.optifine.shaders.gui.GuiButtonDownloadShaders;
import net.optifine.shaders.gui.GuiButtonEnumShaderOption;
import net.optifine.shaders.gui.GuiShaderOptions;
import net.optifine.shaders.gui.GuiShaders;
import net.optifine.shaders.gui.GuiSlotShaders;
import org.lwjgl.Sys;

public class GuiShaders
extends GuiScreenOF {
    protected GuiScreen parentGui;
    protected String screenTitle = "Shaders";
    private TooltipManager tooltipManager = new TooltipManager((GuiScreen)this, (TooltipProvider)new TooltipProviderEnumShaderOptions());
    private int updateTimer = -1;
    private GuiSlotShaders shaderList;
    private boolean saved = false;
    private static float[] QUALITY_MULTIPLIERS = new float[]{0.5f, 0.6f, 0.6666667f, 0.75f, 0.8333333f, 0.9f, 1.0f, 1.1666666f, 1.3333334f, 1.5f, 1.6666666f, 1.8f, 2.0f};
    private static String[] QUALITY_MULTIPLIER_NAMES = new String[]{"0.5x", "0.6x", "0.66x", "0.75x", "0.83x", "0.9x", "1x", "1.16x", "1.33x", "1.5x", "1.66x", "1.8x", "2x"};
    private static float QUALITY_MULTIPLIER_DEFAULT = 1.0f;
    private static float[] HAND_DEPTH_VALUES = new float[]{0.0625f, 0.125f, 0.25f};
    private static String[] HAND_DEPTH_NAMES = new String[]{"0.5x", "1x", "2x"};
    private static float HAND_DEPTH_DEFAULT = 0.125f;
    public static final int EnumOS_UNKNOWN = 0;
    public static final int EnumOS_WINDOWS = 1;
    public static final int EnumOS_OSX = 2;
    public static final int EnumOS_SOLARIS = 3;
    public static final int EnumOS_LINUX = 4;

    public GuiShaders(GuiScreen par1GuiScreen, GameSettings par2GameSettings) {
        this.parentGui = par1GuiScreen;
    }

    public void initGui() {
        this.screenTitle = I18n.format((String)"of.options.shadersTitle", (Object[])new Object[0]);
        if (Shaders.shadersConfig == null) {
            Shaders.loadConfig();
        }
        int i = 120;
        int j = 20;
        int k = width - i - 10;
        int l = 30;
        int i1 = 20;
        int j1 = width - i - 20;
        this.shaderList = new GuiSlotShaders(this, j1, height, l, height - 50, 16);
        this.shaderList.registerScrollButtons(7, 8);
        this.buttonList.add((Object)new GuiButtonEnumShaderOption(EnumShaderOption.ANTIALIASING, k, 0 * i1 + l, i, j));
        this.buttonList.add((Object)new GuiButtonEnumShaderOption(EnumShaderOption.NORMAL_MAP, k, 1 * i1 + l, i, j));
        this.buttonList.add((Object)new GuiButtonEnumShaderOption(EnumShaderOption.SPECULAR_MAP, k, 2 * i1 + l, i, j));
        this.buttonList.add((Object)new GuiButtonEnumShaderOption(EnumShaderOption.RENDER_RES_MUL, k, 3 * i1 + l, i, j));
        this.buttonList.add((Object)new GuiButtonEnumShaderOption(EnumShaderOption.SHADOW_RES_MUL, k, 4 * i1 + l, i, j));
        this.buttonList.add((Object)new GuiButtonEnumShaderOption(EnumShaderOption.HAND_DEPTH_MUL, k, 5 * i1 + l, i, j));
        this.buttonList.add((Object)new GuiButtonEnumShaderOption(EnumShaderOption.OLD_HAND_LIGHT, k, 6 * i1 + l, i, j));
        this.buttonList.add((Object)new GuiButtonEnumShaderOption(EnumShaderOption.OLD_LIGHTING, k, 7 * i1 + l, i, j));
        int k1 = Math.min((int)150, (int)(j1 / 2 - 10));
        int l1 = j1 / 4 - k1 / 2;
        int i2 = height - 25;
        this.buttonList.add((Object)new GuiButton(201, l1, i2, k1 - 22 + 1, j, Lang.get((String)"of.options.shaders.shadersFolder")));
        this.buttonList.add((Object)new GuiButtonDownloadShaders(210, l1 + k1 - 22 - 1, i2));
        this.buttonList.add((Object)new GuiButton(202, j1 / 4 * 3 - k1 / 2, height - 25, k1, j, I18n.format((String)"gui.done", (Object[])new Object[0])));
        this.buttonList.add((Object)new GuiButton(203, k, height - 25, i, j, Lang.get((String)"of.options.shaders.shaderOptions")));
        this.updateButtons();
    }

    public void updateButtons() {
        boolean flag = Config.isShaders();
        for (GuiButton guibutton : this.buttonList) {
            if (guibutton.id == 201 || guibutton.id == 202 || guibutton.id == 210 || guibutton.id == EnumShaderOption.ANTIALIASING.ordinal()) continue;
            guibutton.enabled = flag;
        }
    }

    public void handleMouseInput() throws IOException {
        super.handleMouseInput();
        this.shaderList.handleMouseInput();
    }

    protected void actionPerformed(GuiButton button) {
        this.actionPerformed(button, false);
    }

    protected void actionPerformedRightClick(GuiButton button) {
        this.actionPerformed(button, true);
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    private void actionPerformed(GuiButton button, boolean rightClick) {
        if (!button.enabled) return;
        if (!(button instanceof GuiButtonEnumShaderOption)) {
            if (rightClick) return;
            switch (button.id) {
                case 201: {
                    switch (GuiShaders.getOSType()) {
                        case 1: {
                            String s = String.format((String)"cmd.exe /C start \"Open file\" \"%s\"", (Object[])new Object[]{Shaders.shaderPacksDir.getAbsolutePath()});
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
                                Runtime.getRuntime().exec(new String[]{"/usr/bin/open", Shaders.shaderPacksDir.getAbsolutePath()});
                                return;
                            }
                            catch (IOException ioexception1) {
                                ioexception1.printStackTrace();
                            }
                        }
                    }
                    boolean flag = false;
                    try {
                        Class oclass1 = Class.forName((String)"java.awt.Desktop");
                        Object object1 = oclass1.getMethod("getDesktop", new Class[0]).invoke(null, new Object[0]);
                        oclass1.getMethod("browse", new Class[]{URI.class}).invoke(object1, new Object[]{new File(this.mc.mcDataDir, "shaderpacks").toURI()});
                    }
                    catch (Throwable throwable1) {
                        throwable1.printStackTrace();
                        flag = true;
                    }
                    if (!flag) return;
                    Config.dbg((String)"Opening via system class!");
                    Sys.openURL((String)("file://" + Shaders.shaderPacksDir.getAbsolutePath()));
                    return;
                }
                case 202: {
                    Shaders.storeConfig();
                    this.saved = true;
                    this.mc.displayGuiScreen(this.parentGui);
                    return;
                }
                case 203: {
                    GuiShaderOptions guishaderoptions = new GuiShaderOptions((GuiScreen)this, Config.getGameSettings());
                    Config.getMinecraft().displayGuiScreen((GuiScreen)guishaderoptions);
                    return;
                }
                case 210: {
                    try {
                        Class oclass = Class.forName((String)"java.awt.Desktop");
                        Object object = oclass.getMethod("getDesktop", new Class[0]).invoke(null, new Object[0]);
                        oclass.getMethod("browse", new Class[]{URI.class}).invoke(object, new Object[]{new URI("http://optifine.net/shaderPacks")});
                    }
                    catch (Throwable throwable) {
                        throwable.printStackTrace();
                    }
                }
                default: {
                    this.shaderList.actionPerformed(button);
                    return;
                }
            }
        }
        GuiButtonEnumShaderOption guibuttonenumshaderoption = (GuiButtonEnumShaderOption)button;
        switch (1.$SwitchMap$net$optifine$shaders$config$EnumShaderOption[guibuttonenumshaderoption.getEnumShaderOption().ordinal()]) {
            case 1: {
                Shaders.nextAntialiasingLevel((!rightClick ? 1 : 0) != 0);
                if (this.hasShiftDown()) {
                    Shaders.configAntialiasingLevel = 0;
                }
                Shaders.uninit();
                break;
            }
            case 2: {
                boolean bl = Shaders.configNormalMap = !Shaders.configNormalMap;
                if (this.hasShiftDown()) {
                    Shaders.configNormalMap = true;
                }
                Shaders.uninit();
                this.mc.scheduleResourcesRefresh();
                break;
            }
            case 3: {
                boolean bl = Shaders.configSpecularMap = !Shaders.configSpecularMap;
                if (this.hasShiftDown()) {
                    Shaders.configSpecularMap = true;
                }
                Shaders.uninit();
                this.mc.scheduleResourcesRefresh();
                break;
            }
            case 4: {
                Shaders.configRenderResMul = this.getNextValue(Shaders.configRenderResMul, QUALITY_MULTIPLIERS, QUALITY_MULTIPLIER_DEFAULT, !rightClick, this.hasShiftDown());
                Shaders.uninit();
                Shaders.scheduleResize();
                break;
            }
            case 5: {
                Shaders.configShadowResMul = this.getNextValue(Shaders.configShadowResMul, QUALITY_MULTIPLIERS, QUALITY_MULTIPLIER_DEFAULT, !rightClick, this.hasShiftDown());
                Shaders.uninit();
                Shaders.scheduleResizeShadow();
                break;
            }
            case 6: {
                Shaders.configHandDepthMul = this.getNextValue(Shaders.configHandDepthMul, HAND_DEPTH_VALUES, HAND_DEPTH_DEFAULT, !rightClick, this.hasShiftDown());
                Shaders.uninit();
                break;
            }
            case 7: {
                Shaders.configOldHandLight.nextValue(!rightClick);
                if (this.hasShiftDown()) {
                    Shaders.configOldHandLight.resetValue();
                }
                Shaders.uninit();
                break;
            }
            case 8: {
                Shaders.configOldLighting.nextValue(!rightClick);
                if (this.hasShiftDown()) {
                    Shaders.configOldLighting.resetValue();
                }
                Shaders.updateBlockLightLevel();
                Shaders.uninit();
                this.mc.scheduleResourcesRefresh();
                break;
            }
            case 9: {
                Shaders.configTweakBlockDamage = !Shaders.configTweakBlockDamage;
                break;
            }
            case 10: {
                Shaders.configCloudShadow = !Shaders.configCloudShadow;
                break;
            }
            case 11: {
                Shaders.configTexMinFilN = Shaders.configTexMinFilS = (Shaders.configTexMinFilB = (Shaders.configTexMinFilB + 1) % 3);
                button.displayString = "Tex Min: " + Shaders.texMinFilDesc[Shaders.configTexMinFilB];
                ShadersTex.updateTextureMinMagFilter();
                break;
            }
            case 12: {
                Shaders.configTexMagFilN = (Shaders.configTexMagFilN + 1) % 2;
                button.displayString = "Tex_n Mag: " + Shaders.texMagFilDesc[Shaders.configTexMagFilN];
                ShadersTex.updateTextureMinMagFilter();
                break;
            }
            case 13: {
                Shaders.configTexMagFilS = (Shaders.configTexMagFilS + 1) % 2;
                button.displayString = "Tex_s Mag: " + Shaders.texMagFilDesc[Shaders.configTexMagFilS];
                ShadersTex.updateTextureMinMagFilter();
                break;
            }
            case 14: {
                Shaders.configShadowClipFrustrum = !Shaders.configShadowClipFrustrum;
                button.displayString = "ShadowClipFrustrum: " + GuiShaders.toStringOnOff(Shaders.configShadowClipFrustrum);
                ShadersTex.updateTextureMinMagFilter();
            }
        }
        guibuttonenumshaderoption.updateButtonText();
    }

    public void onGuiClosed() {
        super.onGuiClosed();
        if (!this.saved) {
            Shaders.storeConfig();
        }
    }

    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        this.drawDefaultBackground();
        this.shaderList.drawScreen(mouseX, mouseY, partialTicks);
        if (this.updateTimer <= 0) {
            this.shaderList.updateList();
            this.updateTimer += 20;
        }
        this.drawCenteredString(this.fontRendererObj, this.screenTitle + " ", width / 2, 15, 0xFFFFFF);
        String s = "OpenGL: " + Shaders.glVersionString + ", " + Shaders.glVendorString + ", " + Shaders.glRendererString;
        int i = this.fontRendererObj.getStringWidth(s);
        if (i < width - 5) {
            this.drawCenteredString(this.fontRendererObj, s, width / 2, height - 40, 0x808080);
        } else {
            this.drawString(this.fontRendererObj, s, 5, height - 40, 0x808080);
        }
        super.drawScreen(mouseX, mouseY, partialTicks);
        this.tooltipManager.drawTooltips(mouseX, mouseY, this.buttonList);
    }

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
        String s = Lang.getOn();
        String s1 = Lang.getOff();
        return value ? s : s1;
    }

    public static String toStringAa(int value) {
        return value == 2 ? "FXAA 2x" : (value == 4 ? "FXAA 4x" : Lang.getOff());
    }

    public static String toStringValue(float val, float[] values, String[] names) {
        int i = GuiShaders.getValueIndex(val, values);
        return names[i];
    }

    private float getNextValue(float val, float[] values, float valDef, boolean forward, boolean reset) {
        if (reset) {
            return valDef;
        }
        int i = GuiShaders.getValueIndex(val, values);
        if (forward) {
            if (++i >= values.length) {
                i = 0;
            }
        } else if (--i < 0) {
            i = values.length - 1;
        }
        return values[i];
    }

    public static int getValueIndex(float val, float[] values) {
        for (int i = 0; i < values.length; ++i) {
            float f = values[i];
            if (!(f >= val)) continue;
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
        String s = System.getProperty((String)"os.name").toLowerCase();
        return s.contains((CharSequence)"win") ? 1 : (s.contains((CharSequence)"mac") ? 2 : (s.contains((CharSequence)"solaris") ? 3 : (s.contains((CharSequence)"sunos") ? 3 : (s.contains((CharSequence)"linux") ? 4 : (s.contains((CharSequence)"unix") ? 4 : 0)))));
    }

    public boolean hasShiftDown() {
        return GuiShaders.isShiftKeyDown();
    }
}
