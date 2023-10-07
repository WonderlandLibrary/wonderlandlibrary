package dev.rise.ui.clickgui.impl;

import dev.rise.Rise;
import dev.rise.config.ConfigHandler;
import dev.rise.config.online.ConfigState;
import dev.rise.config.online.OnlineConfig;
import dev.rise.font.CustomFont;
import dev.rise.font.fontrenderer.TTFFontRenderer;
import dev.rise.module.Module;
import dev.rise.module.enums.Category;
import dev.rise.setting.Setting;
import dev.rise.setting.impl.*;
import dev.rise.ui.clickgui.ClickGUIType;
import dev.rise.ui.mainmenu.MainMenu;
import dev.rise.util.math.MathUtil;
import dev.rise.util.math.TimeUtil;
import dev.rise.util.misc.FileUtil;
import dev.rise.util.render.BlurUtil;
import dev.rise.util.render.ColorUtil;
import dev.rise.util.render.RenderUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import org.apache.commons.lang3.StringUtils;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;
import store.intent.intentguard.annotation.Exclude;
import store.intent.intentguard.annotation.Strategy;

import java.awt.*;
import java.io.File;
import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.time.Year;
import java.util.List;
import java.util.*;
import java.util.concurrent.TimeUnit;

@Exclude({Strategy.NUMBER_OBFUSCATION, Strategy.FLOW_OBFUSCATION})
public final class ClickGUI extends GuiScreen implements ClickGUIType {

    //Font
    private static final TTFFontRenderer icon = CustomFont.FONT_MANAGER.getFont("Icon 18");
    private static final TTFFontRenderer icon2 = CustomFont.FONT_MANAGER.getFont("Icon2 18");

    private float width = 500;
    private float height = 350;

//    private final ScaledResolution sr = new ScaledResolution(mc);
//    private float x = (float) (sr.getScaledWidth() / 2D - width / 2D), y = (float) (sr.getScaledHeight() / 2D - height / 2D), size;

    private float x, y, size;

    private float categoryWidth;
    private static float categoryHeight;

    private Category selectedCat = Category.COMBAT;

    private float moduleWidth;
    private float moduleHeight;

    private float offset;
    private float heightOffset;

    private final String separator = File.separator;

    public static float scrollAmount, lastScrollAmount, lastLastScrollAmount;
    public static float renderScrollAmount;

    private final TimeUtil timer = new TimeUtil();
    private final TimeUtil timer2 = new TimeUtil();
    private BlurUtil blur;

    public static NumberSetting selectedSlider;

    public static float firstModulePosition;
    public static float lastModulePosition;

    private boolean holdingGui, resizingGui;
    private float holdingOffsetX, holdingOffsetY;

    private float renderSelectY;

    private boolean hasEditedSliders;

    private List<String> onlineConfigs;
    private List<String> localConfigs = new ArrayList<>();
    private List<String> onlineFeaturedConfigs = new ArrayList<>();
    private ConfigState configState = ConfigState.NONE;

    public boolean draggingRadar;
    public int oldMouseX;
    public int oldMouseY;

    Color colorModules;
    Color colorCategory;
    Color colorTop;
    Color selectedCatColor;
    Color booleanColor1;
    Color booleanColor2;
    Color settingColor3;
    Color opacityColor;

    int test;
    int customHue;

//    List<OnlineScriptHandler.OnlineScript> scriptList;
//    private ScriptState scriptState = ScriptState.NONE;

    private final static TTFFontRenderer fontBig = CustomFont.FONT_MANAGER.getFont("Light 19");
    private final static TTFFontRenderer fontLarge = CustomFont.FONT_MANAGER.getFont("Light 24");
    private final static TTFFontRenderer fontExtraLarge = CustomFont.FONT_MANAGER.getFont("Light 36");

    double gap;

    double positionXOfScript;
    double positionYOfScript;
    double widthOfScript;
    double heightOfScript;

    public boolean blockScriptEditorOpen;

    private boolean calledFromMenu = false;
//    private final Minecraft mc;

    private long tabSwapTime;

    public ClickGUI() {
        blur = new BlurUtil();
    }

    public ClickGUI(Minecraft mc) {
        this.calledFromMenu = true;
        this.mc = mc;
        blur = new BlurUtil();
    }

    public void initGui() {
//        size = calledFromMenu ? 0.85F : PopOutAnimation.startingSizeValue;
        size = 0.85F;
        holdingGui = false;
        resizingGui = false;
        tabSwapTime = System.currentTimeMillis();

        blur.init();

        hasEditedSliders = false;
        blockScriptEditorOpen = false;

        final RuntimeMXBean runtimeMxBean = ManagementFactory.getRuntimeMXBean();
        final List<String> arguments = runtimeMxBean.getInputArguments();

        int i = 0;
        for (final Object s : arguments.toArray()) {
            i++;
            //Rise.addChatMessage(s + " " + i);
        }

        if (x <= 0 && y <= 0) {
            ScaledResolution sr = new ScaledResolution(mc);

            if (calledFromMenu) {
                x = 150;
                y = 75;
                width = sr.getScaledWidth() - 300;
                height = sr.getScaledHeight() - 150;
            } else {
                x = (float) (sr.getScaledWidth() / 2D - width / 2D);
                y = (float) (sr.getScaledHeight() / 2D - height / 2D);
            }
        }
    }

    public void drawScreen(final int mouseX, final int mouseY, final float partialTicks) {
        GlStateManager.pushMatrix();

        final ScaledResolution sr = new ScaledResolution(mc);

//        final boolean canPopUp = Objects.requireNonNull(Rise.INSTANCE.getModuleManager().getModule("PopOutAnimation")).isEnabled() && PopOutAnimation.clickGuiValue;
//
//        if (canPopUp && !calledFromMenu) {
//            size = (float) MathUtil.lerp(size, 1, PopOutAnimation.speedValue / (Minecraft.getDebugFPS() / 20D));
//
//            GlStateManager.translate((x - x * size) + (width / 2F - width / 2F * size), (y - y * size) + (height / 2F - height / 2F * size), 0);
//            GlStateManager.scale(size, size, 1);
//        } else if (calledFromMenu) {
//            size = (float) MathUtil.lerp(size, 1, 0.5 / (Minecraft.getDebugFPS() / 20D));
//
//            GlStateManager.translate((x - x * size) + (width / 2F - width / 2F * size), (y - y * size) + (height / 2F - height / 2F * size), 0);
//            GlStateManager.scale(size, size, 1);
//        } else {
//            size = 1;
//        }

        // fuck you, always animated
            size = (float) MathUtil.lerp(size, 1, 0.5 / (Minecraft.getDebugFPS() / 20D));

            GlStateManager.translate((x - x * size) + (width / 2F - width / 2F * size), (y - y * size) + (height / 2F - height / 2F * size), 0);
            GlStateManager.scale(size, size, 1);

        if (resizingGui) {

            width = 415;
            height = 290;

            while (width < mouseX - x) {
                width += 1;
            }

            while (height < mouseY - y) {
                height += 1;
            }
        }

        categoryWidth = 85;
        categoryHeight = 20;

        boolean darkmode = ((BooleanSetting) Objects.requireNonNull(Rise.INSTANCE.getModuleManager().getSetting("ClickGui", "Dark Mode"))).isEnabled();

        // main background
        colorModules = colorTop = darkmode ? new Color(23, 26, 33, 255) : new Color(39, 42, 48, 255);
        colorCategory = opacityColor = darkmode ? new Color(18, 20, 25, 255) : new Color(38, 39, 44, 255);
        selectedCatColor = booleanColor2 = settingColor3 = darkmode ? new Color(94, 129, 172, 255) : new Color(68, 134, 240, 255);
        booleanColor1 = ColorUtil.darker(selectedCatColor, 0.85);

        final ModeSetting theme = (ModeSetting) Rise.INSTANCE.getModuleManager().getSetting("ClickGui", "Theme");

        customHue = 0;

        switch (Objects.requireNonNull(theme).getMode()) {
            case "Rural Amethyst":
                customHue = 265;
                break;

            case "Alyssum Pink":
                customHue = 330;
                break;

            case "Sweet Grape Vine":
                customHue = 130;
                selectedCatColor = new Color(25, 91, 197, 255);
                break;

            case "Orchid Aqua":
                customHue = 200;
                break;

            case "Disco":
                customHue = (test++);
                if (test > 359) test = 0;
                break;
        }

        if (!theme.is("Deep Blue Rise")) {
            colorModules = changeHue(colorModules, customHue / 360f);
            colorCategory = changeHue(colorCategory, customHue / 360f);
            colorTop = changeHue(colorTop, customHue / 360f);
            selectedCatColor = changeHue(selectedCatColor, customHue / 360f);
            booleanColor1 = changeHue(booleanColor1, customHue / 360f);
            booleanColor2 = changeHue(booleanColor2, customHue / 360f);
            settingColor3 = changeHue(settingColor3, customHue / 360f);
            opacityColor = changeHue(opacityColor, customHue / 360f);
        }

        if (((BooleanSetting) Objects.requireNonNull(Rise.INSTANCE.getModuleManager().getSetting("ClickGui", "Transparency"))).isEnabled()) {
            colorCategory = new Color(opacityColor.getRed(), opacityColor.getGreen(), opacityColor.getBlue(), 220);
        }

        // cope
        if (((BooleanSetting) Objects.requireNonNull(Rise.INSTANCE.getModuleManager().getSetting("ClickGui", "Shadow"))).isEnabled() || calledFromMenu) {
            RenderUtil.roundedRect(x - 30, y - 30, width + 60, height + 60, 60, new Color(0, 0, 0, 3));
            RenderUtil.roundedRect(x - 20, y - 20, width + 40, height + 40, 50, new Color(0, 0, 0, 3));
            RenderUtil.roundedRect(x - 18, y - 18, width + 36, height + 36, 50, new Color(0, 0, 0, 3));
            RenderUtil.roundedRect(x - 16, y - 16, width + 32, height + 32, 45, new Color(0, 0, 0, 5));
            RenderUtil.roundedRect(x - 14, y - 14, width + 28, height + 28, 45, new Color(0, 0, 0, 5));
            RenderUtil.roundedRect(x - 12, y - 12, width + 24, height + 24, 40, new Color(0, 0, 0, 6));
            RenderUtil.roundedRect(x - 10, y - 10, width + 20, height + 20, 40, new Color(0, 0, 0, 6));
            RenderUtil.roundedRect(x - 8, y - 8, width + 16, height + 16, 35, new Color(0, 0, 0, 7));
            RenderUtil.roundedRect(x - 6, y - 6, width + 12, height + 12, 35, new Color(0, 0, 0, 7));
            RenderUtil.roundedRect(x - 4, y - 4, width + 8, height + 8, 30, new Color(0, 0, 0, 8));
            RenderUtil.roundedRect(x - 2, y - 2, width + 4, height + 4, 30, new Color(0, 0, 0, 8));
        }

        // Background
        RenderUtil.roundedRectCustom(x + categoryWidth, y + categoryHeight, width - categoryWidth, height - categoryHeight, 10, colorModules, false, false, false, true);

        // Category background
        RenderUtil.roundedRectCustom(x, y, categoryWidth, height, 10, colorCategory, true, false, true, false);

        // Above
        RenderUtil.roundedRectCustom(x + categoryWidth, y, width - categoryWidth, categoryHeight, 10, colorTop, false, true, false, false);

        //Logo
        CustomFont.drawStringBig(Rise.CLIENT_NAME, x + 16, y + 4, new Color(237, 237, 237).getRGB());
        CustomFont.drawString(Rise.CLIENT_VERSION, x + 50, y + 5, new Color(237, 237, 237).getRGB());

        // Handle the selected category.
        int i = 0;
        for (final Category category : Category.values()) {
            if (category == selectedCat) {
                if (timer2.hasReached(1000 / 120)) {
                    timer2.reset();
                    renderSelectY = MathUtil.lerp(renderSelectY, categoryHeight * (i + 1) + 8, 0.15F);
                }

                RenderUtil.rect(x, y + renderSelectY, categoryWidth, categoryHeight, selectedCatColor);
            }

            ++i;
        }

        int amount = 0;
        for (final Category c : Category.values()) {

            final Color color = new Color(237, 237, 237);

            switch (c) {
                case COMBAT: {
                    icon.drawString("a", x + 5, y + categoryHeight * (amount + 1) + categoryHeight / 2 + 3.5F, color.hashCode());
                    break;
                }
                case MOVEMENT: {
                    icon.drawString("b", x + 5, y + categoryHeight * (amount + 1) + categoryHeight / 2 + 3.5F, color.hashCode());
                    break;
                }
                case PLAYER: {
                    icon.drawString("c", x + 5, y + categoryHeight * (amount + 1) + categoryHeight / 2 + 3.5F, color.hashCode());
                    break;
                }
                case RENDER: {
                    icon2.drawString("D", x + 5, y + categoryHeight * (amount + 1) + categoryHeight / 2 + 3.5F, color.hashCode());
                    break;
                }
                case LEGIT: {
                    icon.drawString("f", x + 5, y + categoryHeight * (amount + 1) + categoryHeight / 2 + 3.5F, color.hashCode());
                    break;
                }
                case OTHER: {
                    icon.drawString("e", x + 5, y + categoryHeight * (amount + 1) + categoryHeight / 2 + 3.5F, color.hashCode());
                    break;
                }
                case SCRIPTS: {
                    icon2.drawString("H", x + 5, y + categoryHeight * (amount + 1) + categoryHeight / 2 + 3.5F, color.hashCode());
                    break;
                }
                case CONFIGS: {
                    icon2.drawString("G", x + 5, y + categoryHeight * (amount + 1) + categoryHeight / 2 + 3.5F, color.hashCode());
                    break;
                }
                case INFO: {
                    CustomFont.drawString("i", x + 7.5, y + categoryHeight * (amount + 1) + categoryHeight / 2 + 3.5F, color.hashCode());
                    break;
                }
            }

            CustomFont.drawString(StringUtils.capitalize(c.name().toLowerCase()), x + 19, y + categoryHeight * (amount + 1) + categoryHeight / 2 + 3.5F, color.hashCode());

            ++amount;
        }

        GlStateManager.pushMatrix();
        GL11.glEnable(GL11.GL_SCISSOR_TEST);
        RenderUtil.scissor((x - (categoryWidth - categoryWidth * size)) + categoryWidth, (y + (height / 2F) - (height / 2F) * size) + categoryHeight * size, width - categoryWidth * size, ((height - categoryHeight) * size));

        moduleWidth = width - categoryWidth;
        moduleHeight = 20;
        offset = 5;

        //Modules
        heightOffset = 0;
        firstModulePosition = 9999999;
        amount = 0;

        switch (selectedCat) {
            case SCRIPTS:

            /*
            for (final Script script : Rise.INSTANCE.getScriptManager().getScripts()) {
                script.sizeInGui = moduleHeight;

                if (firstModulePosition == 9999999)
                    firstModulePosition = categoryHeight + heightOffset + offset * amount + renderScrollAmount;*/
                //Settings
                /*
                if (script.expanded) {
                    script.sizeInGui = categoryHeight;


                    for (final Setting s : script.getSettings()) {

                        if (!s.isHidden()) {


                            script.sizeInGui += 12;
                            updateRainbow(theme);
                        }
                    }
                }*/


                /*
                renderScript(x + categoryWidth + offset, y + categoryHeight + heightOffset + offset * amount + renderScrollAmount, moduleWidth - offset * 2, script.sizeInGui, script);
                lastModulePosition = categoryHeight + heightOffset + offset * amount + renderScrollAmount + script.sizeInGui;

                heightOffset += script.sizeInGui;

                amount++;

                if (script.isEnabled()) updateRainbow(theme);

            }*/

                // who the hell thought this looked good enough?
//                CustomFont.drawString("Coming soon...", x + categoryWidth + offset - 2, y + categoryHeight + heightOffset, new Color(237, 237, 237).getRGB());

                float animProgressLight = Math.round(MathHelper.clamp_float(System.currentTimeMillis() - tabSwapTime, 0, 1000)) / 1000F;
                float animProgressDark = Math.round(MathHelper.clamp_float(System.currentTimeMillis() - tabSwapTime - 250, 0, 1000)) / 1000F;

                int light = new Color(lerp(colorModules.getRed(), 230, animProgressLight) / 255F, lerp(colorModules.getGreen(), 230, animProgressLight) / 255F, lerp(colorModules.getBlue(), 230, animProgressLight) / 255F, 200 / 255F).hashCode();
                int darkest = new Color(lerp(colorModules.getRed(), 150, animProgressDark) / 255F, lerp(colorModules.getGreen(), 150, animProgressDark) / 255F, lerp(colorModules.getBlue(), 150, animProgressDark) / 255F, 150 / 255F).hashCode() ;


                CustomFont.drawCenteredMedium("This is a work in progress feature...", x + categoryWidth + moduleWidth / 2, y + height / 2 - 20, light);
                CustomFont.drawCenteredString("Scripts will be released in Rise 6 in the near future.", x + categoryWidth + moduleWidth / 2, y + height / 2 + 5, darkest);
//                CustomFont.drawCenteredString("Leave the development team suggestions and feedback for Rise 6 in our Discord server.", x + categoryWidth + moduleWidth / 2, y + height / 2 + 23, darkest);

                break;

            default:
                for (final Module m : Rise.INSTANCE.getModuleManager().getModuleList()) {
                    if (m.isHidden()) continue;
                    m.sizeInGui = moduleHeight;

                    final Category c = m.getModuleInfo().category();

                    if (c == selectedCat) {

                        if (c != Category.CONFIGS && c != Category.INFO) {

                            if (firstModulePosition == 9999999)
                                firstModulePosition = categoryHeight + heightOffset + offset * amount + renderScrollAmount;

                            //Settings
                            if (m.expanded) {
                                m.sizeInGui = categoryHeight;

                                for (final Setting s : m.getSettings()) {

                                    if (!s.isHidden()) {

                                        float fontWidth = CustomFont.getWidth(s.name) + 5;

                                        float settingsX = x + categoryWidth + offset + 4;
                                        float settingsY = y + categoryHeight + heightOffset + +offset * amount + m.sizeInGui + renderScrollAmount;

                                        if (s instanceof NoteSetting) {

                                            if (m.getSettings().indexOf(s) != 0) {
                                                m.sizeInGui += 6;
                                                settingsY += 6;
                                            }

                                            CustomFont.drawString(s.name, settingsX, settingsY, new Color(125, 125,   125).getRGB());
                                        } else {
                                            CustomFont.drawString(s.name, settingsX, settingsY, new Color(237, 237, 237).getRGB());
                                        }

                                        if (s instanceof BooleanSetting) {
                                            RenderUtil.circle(settingsX + fontWidth, settingsY + 1.5, 7, false, booleanColor1);

                                            if (((BooleanSetting) s).isEnabled()) {
                                                RenderUtil.circle(settingsX + fontWidth + 1.25, settingsY + 1.5 + 1.25, 4.5, true, booleanColor2);
                                            }
                                        }

                                        if (s instanceof NumberSetting) {
                                            final NumberSetting numberSetting = ((NumberSetting) s);

                                            if (selectedSlider == s) {

                                                final double percent = (double) (mouseX - (settingsX + fontWidth)) / (double) (100);
                                                double value = numberSetting.minimum - percent * (numberSetting.minimum - numberSetting.maximum);

                                                if (value > numberSetting.maximum) value = numberSetting.maximum;
                                                if (value < numberSetting.minimum) value = numberSetting.minimum;

                                                numberSetting.value = value;

                                                if (numberSetting.getIncrement() != 0)
                                                    numberSetting.value = round(value, (float) numberSetting.increment);
                                                else numberSetting.value = value;

                                                hasEditedSliders = true;
                                            }

                                            numberSetting.percentage = (((NumberSetting) s).value - ((NumberSetting) s).minimum) / (((NumberSetting) s).maximum - ((NumberSetting) s).minimum);

                                            RenderUtil.rect(settingsX + fontWidth, settingsY + 3.5, 100, 2, booleanColor1);
                                            RenderUtil.roundedRect(settingsX + fontWidth + numberSetting.renderPercentage * 100, settingsY + 2, 5, 5, 5, settingColor3);

                                            String value = String.valueOf((float) round((numberSetting.renderPercentage * (numberSetting.getMaximum() - numberSetting.getMinimum()) + numberSetting.getMinimum()), (float) numberSetting.increment));

                                            if (numberSetting.increment == 1) {
                                                value = value.replace(".0", "");
                                            }

                                            if (numberSetting.getReplacements() != null) {
                                                for (final String replacement : numberSetting.getReplacements()) {
                                                    final String[] split = replacement.split("-");
                                                    value = value.replace(split[0], split[1]);
                                                }
                                            }

                                            CustomFont.drawString(value, settingsX + fontWidth + 109, settingsY, new Color(237, 237, 237, 235).hashCode());
                                        }

                                        if (s instanceof ModeSetting) {
                                            CustomFont.drawString(((ModeSetting) s).getModes().get(((ModeSetting) s).index), settingsX + fontWidth, settingsY, new Color(237, 237, 237, 255).getRGB());
                                        }

                                        m.sizeInGui += 12;
                                        updateRainbow(theme);
                                    }
                                }
                            }

                            final float startModuleRenderY = y + categoryHeight;
                            final float moduleRenderY = startModuleRenderY + heightOffset + offset * amount + renderScrollAmount;

                            if (moduleRenderY > startModuleRenderY - m.sizeInGui) {
                                if (moduleRenderY < startModuleRenderY + height) {
                                    renderModule(x + categoryWidth + offset, moduleRenderY, moduleWidth - offset * 2, m.sizeInGui, m);
                                }
                            }
                            lastModulePosition = categoryHeight + heightOffset + offset * amount + renderScrollAmount + m.sizeInGui;

                            heightOffset += m.sizeInGui;

                            amount++;

                            if (m.isEnabled()) updateRainbow(theme);
                        }
                    }
                }
                break;

            /**
             * @author Hazsi
             * @since 04/09/2022
             */
            case CONFIGS:

                switch (configState) {
                    case NONE: {
                        configState = ConfigState.LOADING;

                        Rise.INSTANCE.getOnlineConfigExecutor().execute(() -> {
                            final List<String> list = OnlineConfig.getAvailableConfigs();

                            if (list != null) {
                                Collections.sort(list);
                                onlineConfigs = list;

                                configState = ConfigState.DONE;
                            } else {
                                configState = ConfigState.FAILED;
                            }
                        });

                        localConfigs.clear();
                        onlineFeaturedConfigs.clear();

                        if (!FileUtil.exists("Config\\")) {
                            return;
                        }
                        final File configFolder = FileUtil.getFileOrPath("Config\\");

                        if (configFolder.listFiles() == null || Objects.requireNonNull(configFolder.listFiles()).length < 1) {
                            return;
                        } else {
                            for (File file : Objects.requireNonNull(configFolder.listFiles())) {
                                long lastMod = 0;

                                try {
                                    lastMod = Files.readAttributes(Paths.get(file.getPath()), BasicFileAttributes.class).lastModifiedTime().toMillis();
                                } catch (Exception ignored) {}

                                // have to use / as it must be a character no user can enter in a file name on ANY OS, otherwise it would break.
                                localConfigs.add(file.getName().replace(".txt", "") + "/" + lastMod);
                            }
                        }
                        break;
                    }

                    case LOADING: {
                        float animProgress = Math.round(MathHelper.clamp_float(System.currentTimeMillis() - tabSwapTime - 1000, 0, 1000)) / 1000F;
                        CustomFont.drawCenteredMedium("Downloading online configs...", x + categoryWidth + moduleWidth / 2, y + height / 2 - 10, new Color(lerp(colorModules.getRed(), 237, animProgress) / 255F, lerp(colorModules.getGreen(), 237, animProgress) / 255F, lerp(colorModules.getBlue(), 237, animProgress) / 255F, 150 / 255F).getRGB());
                        break;
                    }

                    case FAILED: {
                        CustomFont.drawCenteredMedium("Failed to download online configs.", x + categoryWidth + moduleWidth / 2, y + height / 2 - 10, new Color(237, 237, 237, 150).getRGB());
                        break;
                    }

                    case DONE: {
                        if (onlineConfigs != null) {

                            firstModulePosition = categoryHeight + heightOffset + offset * amount + renderScrollAmount;

                            boolean draw = !onlineFeaturedConfigs.isEmpty();

                            int column3 = 0;
                            int row3 = 0;
                            if (draw) CustomFont.drawStringMedium("Featured configs", x + categoryWidth + offset + 5, y + categoryHeight + renderScrollAmount + 5, new Color(237, 237, 237, 150).getRGB());

                            // render featured configs
                            for (final String config : onlineFeaturedConfigs) {
                                final String[] split = config.split("-");
                                final String configName = split[0];

                                if (split.length > 1) {
                                    final String configDate = split[1];

                                    String amountOfDays = "A long time ago";
                                    if (configDate.contains("/")) {
                                        final long days = days(configDate);
                                        amountOfDays = days + ((days == 1) ? " day ago" : " days ago");
                                        if (days <= 0) amountOfDays = "Today";
                                    }

                                    String author = "Unknown", server = "Unknown";

                                    if (split.length > 2) {
                                        if (!Objects.equals(split[2], "")) author = split[2];

                                        if (split.length > 3) {
                                            if (!Objects.equals(split[3], "")) server = split[3];
                                        }
                                    }

                                    // Don't remove the width check. It's important.
                                    if (width > 550) CustomFont.drawString(amountOfDays, x + categoryWidth + offset + (column3 + 1) * (moduleWidth/3 - 3) - CustomFont.getWidth(amountOfDays) - 10, y + categoryHeight + row3 * 90 + renderScrollAmount + 75, new Color(237, 237, 237, 150).getRGB());
                                    if (width > 550) CustomFont.drawString(server, x + categoryWidth + offset + (column3 + 1) * (moduleWidth/3 - 3) - CustomFont.getWidth(server) - 10, y + categoryHeight + row3 * 90 + renderScrollAmount + 90, new Color(237, 237, 237, 100).getRGB());

                                    CustomFont.drawString(author, x + categoryWidth + offset + column3 * (moduleWidth/3 - 3) + 10, y + categoryHeight + row3 * 90 + renderScrollAmount + 90, new Color(237, 237, 237, 100).getRGB());
                                    RenderUtil.roundedRect(x + categoryWidth + offset + column3 * (moduleWidth/3 - 3) + 5, y + categoryHeight + row3 * 90 + renderScrollAmount + 25, moduleWidth / 3 - 12, 80, 5, new Color(255, 255, 255, 10));
                                    CustomFont.drawString(configName, x + categoryWidth + offset + column3 * (moduleWidth/3 - 3) + 10, y + categoryHeight + row3 * 90 + renderScrollAmount + 75, booleanColor2.hashCode());
                                }


                                lastModulePosition = (float) (categoryHeight + heightOffset + offset * amount + renderScrollAmount + moduleHeight);

                                heightOffset += moduleHeight;

                                if (column3 == 2) {
                                    column3 = 0;
                                    row3++;
                                } else {
                                    column3++;
                                }
                            }

                            double onlineOffset = (Math.ceil(onlineFeaturedConfigs.size() / 3D) * 90) + 35;

                            int column = 0;
                            int row = 0;
                            if (draw) CustomFont.drawStringMedium(onlineConfigs.size() + " online configs", x + categoryWidth + offset + 5, y + categoryHeight + onlineOffset + renderScrollAmount + 5, new Color(237, 237, 237, 150).getRGB());

                            // render online configs
                            Iterator<String> iterator = onlineConfigs.iterator();

                            while (iterator.hasNext()) {
                                String config = iterator.next();

                                if (firstModulePosition == 9999999)
                                    firstModulePosition = categoryHeight + heightOffset + offset * amount + renderScrollAmount;

                                final String[] split = config.split("-");
                                final String configName = split[0];

                                if (split.length > 1) {
                                    final String configDate = split[1];

                                    String amountOfDays = "A long time ago";
                                    if (configDate.contains("/")) {
                                        final long days = days(configDate);
                                        amountOfDays = days + ((days == 1) ? " day ago" : " days ago");
                                        if (days <= 0) amountOfDays = "Today";
                                    }

                                    String author = "Unknown", server = "Unknown";

                                    if (split.length > 2) {
                                        if (!Objects.equals(split[2], "")) author = split[2];

                                        if (split.length > 3) {
                                            if (!Objects.equals(split[3], "")) server = split[3];

                                            // featured config
                                            if (split.length > 4) {
                                                iterator.remove();
                                                onlineFeaturedConfigs.add(config);
                                                continue;
                                            }
                                        }
                                    }

                                    if (draw) {
                                        if (width > 600) CustomFont.drawString(amountOfDays, x + categoryWidth + offset + (column + 1) * (moduleWidth/3 - 3) - CustomFont.getWidth(amountOfDays) - 10, y + categoryHeight + row * 45 + onlineOffset + renderScrollAmount + 31, new Color(237, 237, 237, 150).getRGB());
                                        if (width > 600) CustomFont.drawString(server, x + categoryWidth + offset + (column + 1) * (moduleWidth/3 - 3) - CustomFont.getWidth(server) - 10, y + categoryHeight + row * 45 + renderScrollAmount + onlineOffset + 45, new Color(237, 237, 237, 100).getRGB());

                                        CustomFont.drawString(author, x + categoryWidth + offset + column * (moduleWidth/3 - 3) + 10, y + categoryHeight + row * 45 + renderScrollAmount + onlineOffset + 45, new Color(237, 237, 237, 100).getRGB());
                                        RenderUtil.roundedRect(x + categoryWidth + offset + column * (moduleWidth/3 - 3) + 5, y + categoryHeight + row * 45 + renderScrollAmount + onlineOffset + 25, moduleWidth / 3 - 12, 35, 5, new Color(255, 255, 255, 10));
                                        CustomFont.drawString(configName, x + categoryWidth + offset + column * (moduleWidth/3 - 3) + 10, y + categoryHeight + row * 45 + renderScrollAmount + onlineOffset + 31, booleanColor2.hashCode());

                                    }
                                }


                                lastModulePosition = (float) (categoryHeight + heightOffset + offset * amount + renderScrollAmount + moduleHeight + onlineOffset);

                                heightOffset += moduleHeight;

                                if (column == 2) {
                                    column = 0;
                                    row++;
                                } else {
                                    column++;
                                }
                            }

                            heightOffset += onlineOffset;

                            double localOffset = (Math.ceil(onlineConfigs.size() / 3D) * 45) + onlineOffset;

                            if (draw) {
                                CustomFont.drawStringMedium(localConfigs.size() + " local configs", x + categoryWidth + offset + 5, y + categoryHeight + renderScrollAmount + localOffset + 35, new Color(237, 237, 237, 150).getRGB());
                                RenderUtil.roundedRect(x + width - 99, y + categoryHeight + renderScrollAmount + localOffset + 31, 90, 18, 5, new Color(255, 255, 255, 10));
                                CustomFont.drawCenteredString("Open config folder", x + width - 53, y + categoryHeight + renderScrollAmount + localOffset + 35.5, new Color(237, 237, 237, 150).getRGB());
                            }

                            int row2 = 0;

                            // render local configs
                            for (String config : localConfigs) {

                                // have to use / as it must be a character no user can enter in a file name on ANY OS, otherwise it would break.
                                String name = config.split("/")[0];

                                if (draw) {
                                    RenderUtil.roundedRect(x + categoryWidth + offset + 5, y + categoryHeight + row2 * 25 + renderScrollAmount + localOffset + 55, moduleWidth - 18, 20, 5, new Color(255, 255, 255, 10));
                                    CustomFont.drawString(name, x + categoryWidth + offset + 10, y + categoryHeight + row2 * 25 + renderScrollAmount + localOffset + 60, booleanColor2.hashCode());
                                }

                                if (config.split("/").length > 0) {
                                    long dateRaw = Long.parseLong(config.split("/")[1]);
                                    dateRaw = Math.round((System.currentTimeMillis() - dateRaw) / (1000D * 60 * 60 * 24));
                                    String date = dateRaw + (dateRaw == 1 ? " day ago" : " days ago");
                                    if (date.contains("0 days ago")) date = "Today";

                                    if (draw) CustomFont.drawString(String.valueOf(date), x + moduleWidth + offset - CustomFont.getWidth(String.valueOf(date)) + 67, y + categoryHeight + row2 * 25 + renderScrollAmount + localOffset + 60, new Color(237, 237, 237, 150).getRGB());

                                }

                                lastModulePosition = categoryHeight + heightOffset + offset + renderScrollAmount + moduleHeight + 25;
                                heightOffset += moduleHeight;

                                row2++;
                            }

                            lastModulePosition += 25;

                            if (draw) CustomFont.drawCenteredString("If you'd like to submit a config, post it in #public-configs in our Discord.", x + width/2 + 37.5, y + categoryHeight + row2 * 25 + renderScrollAmount + localOffset + 70, new Color(150, 150, 150, 100).getRGB());
                        }
                        break;
                    }
                }
                break;
        }

        if (selectedCat == Category.INFO) {
            final float off = 14;

            final long milliseconds = System.currentTimeMillis() - Rise.timeJoinedServer;

            final long hours = TimeUnit.MILLISECONDS.toHours(milliseconds);
            final long minutes = TimeUnit.MILLISECONDS.toMinutes(milliseconds);
            final long seconds = TimeUnit.MILLISECONDS.toSeconds(milliseconds);

            String time = "Unavailable";

            if (!mc.isIntegratedServerRunning() && !calledFromMenu) {
                if (minutes < 1)
                    time = seconds + "s";
                else if (hours < 1)
                    time = minutes + "m, " + (seconds - minutes * 60) + "s";
                else
                    time = hours + "h, " + (minutes - hours * 60) + "m, " + ((seconds - minutes * 60 - hours) + hours) + "s";
            }

//            CustomFont.drawString("Time on server: " + time, x + categoryWidth + offset, y + categoryHeight + heightOffset + offset, -1);
//            CustomFont.drawString("Kills: " + Rise.totalKills, x + categoryWidth + offset, y + categoryHeight + heightOffset + offset + off, -1);
//            CustomFont.drawString("Deaths: " + Rise.totalDeaths, x + categoryWidth + offset, y + categoryHeight + heightOffset + offset + off * 2, -1);
//            CustomFont.drawString("Distance ran: " + Math.round(Rise.distanceRan), x + categoryWidth + offset, y + categoryHeight + heightOffset + offset + off * 3, -1);
//            CustomFont.drawString("Distance flown: " + Math.round(Rise.distanceFlew), x + categoryWidth + offset, y + categoryHeight + heightOffset + offset + off * 4, -1);
//            CustomFont.drawString("Distance jumped: " + Math.round(Rise.distanceJumped), x + categoryWidth + offset, y + categoryHeight + heightOffset + offset + off * 5, -1);
//            CustomFont.drawString("Modules on: " + Rise.amountOfModulesOn + " / " + Rise.INSTANCE.getModuleManager().getModuleList().length, x + categoryWidth + offset, y + categoryHeight + heightOffset + offset + off * 6, -1);
//            CustomFont.drawString("Times saved from void: " + Rise.amountOfVoidSaves, x + categoryWidth + offset, y + categoryHeight + heightOffset + offset + off * 7, -1);
//            CustomFont.drawString("Configs loaded: " + Rise.amountOfConfigsLoaded, x + categoryWidth + offset, y + categoryHeight + heightOffset + offset + off * 8, -1);

            double left = x + categoryWidth + moduleWidth / 2 - 145, right = left + 170;
            double posY = y + (height / 2) - 155;

            // i have to do this AWFUL code because our font renderer folds to opacity.
            float animProgress = Math.round(Math.min(1000, System.currentTimeMillis() - tabSwapTime)) / 1000F;
            int light = new Color(lerp(colorModules.getRed(), 230, animProgress) / 255F, lerp(colorModules.getGreen(), 230, animProgress) / 255F, lerp(colorModules.getBlue(), 230, animProgress) / 255F, 200 / 255F).hashCode();
            int dark = new Color(lerp(colorModules.getRed(), 200, animProgress) / 255F, lerp(colorModules.getGreen(), 200, animProgress) / 255F, lerp(colorModules.getBlue(), 200, animProgress) / 255F, 150 / 255F).hashCode();
            // left
            CustomFont.drawStringHuge(Rise.CLIENT_NAME, left, posY + 50, light);
            CustomFont.drawStringBig(Rise.CLIENT_VERSION, left + 65, posY + 45, dark);

            animProgress = Math.round(MathHelper.clamp_float(System.currentTimeMillis() - tabSwapTime - 250, 0, 1000)) / 1000F;
            light = new Color(lerp(colorModules.getRed(), 230, animProgress) / 255F, lerp(colorModules.getGreen(), 230, animProgress) / 255F, lerp(colorModules.getBlue(), 230, animProgress) / 255F, 200 / 255F).hashCode();
            dark = new Color(lerp(colorModules.getRed(), 200, animProgress) / 255F, lerp(colorModules.getGreen(), 200, animProgress) / 255F, lerp(colorModules.getBlue(), 200, animProgress) / 255F, 150 / 255F).hashCode();
            int darkest = new Color(lerp(colorModules.getRed(), 150, animProgress) / 255F, lerp(colorModules.getGreen(), 150, animProgress) / 255F, lerp(colorModules.getBlue(), 150, animProgress) / 255F, 150 / 255F).hashCode() ;


            CustomFont.drawStringMedium("Registered to " + Rise.intentAccount.username, left, posY + 110, light);
            CustomFont.drawStringMedium(" (" + Rise.intentAccount.client_uid + ")", left + CustomFont.getWidthMedium("Registered to " + Rise.intentAccount.username), posY + 110, darkest);
            CustomFont.drawStringMedium(Rise.intentAccount.discord_tag == null ? "Discord not linked" : Rise.intentAccount.discord_tag, left, posY + 130, light);

            CustomFont.drawStringMedium("Written by Alan32, Tecnio,", left, posY + 170, dark);
            CustomFont.drawStringMedium("Strikeless, Nicklas, Auth,", left, posY + 190, dark);
            CustomFont.drawStringMedium("and Hazsi", left, posY + 210, dark);

            CustomFont.drawStringMedium("intent.store", left, posY + 250, light);
            CustomFont.drawStringMedium("riseclient.com", left, posY + 270, light);

            // right
            animProgress = Math.round(MathHelper.clamp_float(System.currentTimeMillis() - tabSwapTime - 500, 0, 1000)) / 1000F;
            light = new Color(lerp(colorModules.getRed(), 230, animProgress) / 255F, lerp(colorModules.getGreen(), 230, animProgress) / 255F, lerp(colorModules.getBlue(), 230, animProgress) / 255F, 200 / 255F).hashCode();
            dark = new Color(lerp(colorModules.getRed(), 200, animProgress) / 255F, lerp(colorModules.getGreen(), 200, animProgress) / 255F, lerp(colorModules.getBlue(), 200, animProgress) / 255F, 150 / 255F).hashCode();

            CustomFont.drawStringMedium("Session statistics", right, posY + 50, light);
            CustomFont.drawString(time.equals("Unavailable") ? "Playtime information unavailable" : time + " playtime", right, posY + 75, light);
            CustomFont.drawString("Kills: " + Rise.totalKills, right, posY + 90, dark);
            CustomFont.drawString("Deaths: " + Rise.totalKills, right, posY + 105, dark);
            CustomFont.drawString("Distance ran: " + Math.round(Rise.distanceRan), right, posY + 120, dark);
            CustomFont.drawString("Distance flown: " + Math.round(Rise.distanceFlew), right, posY + 135, dark);
            CustomFont.drawString("Distance jumped: " + Math.round(Rise.distanceJumped), right, posY + 150, dark);
            CustomFont.drawString("Times saved from void: " + Rise.amountOfVoidSaves, right, posY + 165, dark);

            CustomFont.drawStringMedium("General statistics", right, posY + 200, light);
            CustomFont.drawString(Rise.amountOfModulesOn + " out of " + Rise.INSTANCE.getModuleManager().getModuleList().length + " modules enabled", right, posY + 225, dark);
            CustomFont.drawString(Rise.amountOfConfigsLoaded + " configs loaded", right, posY + 240, dark);

            if (Rise.devMode) CustomFont.drawStringMedium("Developer Mode", right, posY + 270, light);
        }

        GL11.glDisable(GL11.GL_SCISSOR_TEST);
        GlStateManager.popMatrix();

        //Selected category
        if (!(selectedCat.equals(Category.SCRIPTS)/* || selectedCat.equals(Category.CONFIGS) || selectedCat.equals(Category.INFO)*/)) {
            final String categoryName = StringUtils.capitalize(selectedCat.name().toLowerCase());
            CustomFont.drawCenteredString(categoryName, x + categoryWidth + (width - categoryWidth) / 2, y + 5.5, new Color(237, 237, 237, 150).getRGB());
        }

        renderScrollAmount = lastScrollAmount + (scrollAmount - lastScrollAmount) * mc.timer.renderPartialTicks;

        if (timer.hasReached(1000 / 100)) {
            timer.reset();

            for (final Module m : Rise.INSTANCE.getModuleManager().getModuleList()) {
                for (final Setting s : m.getSettings()) {
                    if (s instanceof NumberSetting) {
                        final NumberSetting numberSetting = ((NumberSetting) s);

                        if (hasEditedSliders) {
                            numberSetting.renderPercentage = (numberSetting.renderPercentage * 4 + numberSetting.percentage) / 5;
                        } else {
                            numberSetting.renderPercentage = (numberSetting.renderPercentage * 9 + numberSetting.percentage) / 10;
                        }

                    }
                }

                //Grey out
                if (!(m.isEnabled() || m.isExpanded() && m.settings.size() > 0)) {
                    m.clickGuiOpacity += 6;

                    if (m.clickGuiOpacity > 90) {
                        m.clickGuiOpacity = 90;
                    }
                } else {
                    m.clickGuiOpacity -= 6;

                    if (m.clickGuiOpacity < 1) {
                        m.clickGuiOpacity = 1;
                    }
                }
            }

            if (firstModulePosition > categoryHeight && lastModulePosition > height - categoryHeight) {
                scrollAmount *= 0.8;
            }

            if (lastModulePosition < height - categoryHeight && firstModulePosition < categoryHeight) {
                scrollAmount += ((height - categoryHeight) - lastModulePosition) * 0.14;
            }
        }

        if (holdingGui) {
            x = mouseX + holdingOffsetX;
            y = mouseY + holdingOffsetY;
        }


        switch (selectedCat) {
            case SCRIPTS:
                //Renders open scripts folder button
//                RenderUtil.roundedRect(x + categoryWidth + moduleWidth - 84, y + 3, 81, categoryHeight - 3 * 2, 5, new Color(255, 255, 255, 10));
//                CustomFont.drawString("Open scripts folder", x + categoryWidth + moduleWidth - 81, y + 5, new Color(237, 237, 237, 220).hashCode());
                break;

            case CONFIGS:
                //Renders open configs folder button
//                RenderUtil.roundedRect(x + categoryWidth + moduleWidth - 84, y + 3, 81, categoryHeight - 3 * 2, 5, new Color(255, 255, 255, 10));
//                CustomFont.drawString("Open configs folder", x + categoryWidth + moduleWidth - 82.5, y + 5, new Color(237, 237, 237, 220).hashCode());

                // moved to config tab rendering (dynamic position)
                break;
        }

        if (draggingRadar) {
            int x = mouseX + oldMouseX;
            int y = mouseY + oldMouseY;

            if (mouseX < (sr.getScaledWidth() / 2f) + 17.5 && mouseX > (sr.getScaledWidth() / 2f) - 17.5) {
                x = sr.getScaledWidth() / 2 - 35;
                RenderUtil.rect(sr.getScaledWidth() / 2f - 0.5, 0, 0.5, sr.getScaledHeight(), Color.GREEN);
            }

            if (mouseY < (sr.getScaledHeight() / 2f) + 17.5 && mouseY > (sr.getScaledHeight() / 2f) - 17.5) {
                y = sr.getScaledHeight() / 2 - 35;
                RenderUtil.rect(0, sr.getScaledHeight() / 2f - 0.5, sr.getScaledWidth(), 0.5, Color.RED);
            }

//            if(y > (sr.getScaledHeight() / 2) - 35 || y < (sr.getScaledHeight() / 2) + 35)
//                y = sr.getScaledHeight() / 2;

            if (x >= sr.getScaledWidth() - 70) {
                x = sr.getScaledWidth() - 70;
            } else if (x <= 0) {
                x = 0;
            }

            if (y >= sr.getScaledHeight() - 70) {
                y = sr.getScaledHeight() - 70;
            } else if (y <= 0) {
                y = 0;
            }


            ((NumberSetting) Objects.requireNonNull(Rise.INSTANCE.getModuleManager().getSetting("Radar", "Radar X"))).setValue(x);
            ((NumberSetting) Objects.requireNonNull(Rise.INSTANCE.getModuleManager().getSetting("Radar", "Radar Y"))).setValue(y);
        }

        amount = 0;
        heightOffset = 0;

//        System.out.println(y + " " + categoryHeight + " " + heightOffset + " " + offset + " " + amount + " " + renderScrollAmount + " " + moduleHeight);
        for (final Module m : Rise.INSTANCE.getModuleManager().getModuleList()) {
            if (m.isHidden()) continue;
            m.sizeInGui = moduleHeight;

            if (m.expanded) {
                m.sizeInGui = categoryHeight;

                for (final Setting ignored : m.getSettings()) {
                    m.sizeInGui += (ignored instanceof NoteSetting && m.getSettings().indexOf(ignored) != 0) ? 18 : 12;
                }
            }

            if (m.getModuleInfo().category() == selectedCat) {
                if (m.expanded) {
                    m.sizeInGui = categoryHeight;

                    for (final Setting s : m.getSettings()) {

                        if (!s.isHidden()) {
                            m.sizeInGui += (s instanceof NoteSetting && m.getSettings().indexOf(s) != 0) ? 18 : 12;
                        }
                    }
                }

                if (mouseOver(x + categoryWidth, y + categoryHeight + heightOffset + offset * amount + renderScrollAmount, moduleWidth, moduleHeight, mouseX, mouseY)) {
                    m.descOpacityInGui += 2;
//                    System.out.println( "[HOVER] " + m.getModuleInfo().name() + " " + mouseX + " " + mouseY + " " + y + " " + categoryHeight + " " + heightOffset + " " + offset + " " + amount + " " + renderScrollAmount + " " + moduleHeight);
//                    System.out.println(m.sizeInGui);
                } else m.descOpacityInGui -= 2;

                m.descOpacityInGui = MathHelper.clamp_float(m.descOpacityInGui, 0, 150);

                heightOffset += m.sizeInGui;

                amount++;
            }
        }

        Rise.INSTANCE.getNotificationManager().draw();

        GlStateManager.popMatrix();
    }

//    public void drawScript(final OnlineScriptHandler.OnlineScript script, final double x, final double y, final double width, final double height) {
//        if (script != null) {
//            RenderUtil.roundedRect(x, y, width, height, 9, new Color(255, 255, 255, 10));
//
//            final String scriptName = StringUtils.capitalize(script.getName());
//            final String scriptAuthor = script.getAuthor();
//
//            fontBig.drawString(scriptName, (float) x + 5, (float) y + 5, new Color(237, 237, 237, 237).getRGB());
//            CustomFont.drawString("by " + scriptAuthor, (float) x + 5, (float) y + 16, new Color(237, 237, 237, 177).getRGB());
//
//            RenderUtil.imageCentered(new ResourceLocation("rise/icon/downloadIcon.png"), (int) (x + width - 10), (float) (y + height - 10), 14 / 1.2f, 13 / 1.2f);
//        }
//    }

    public static Color changeHue(Color c, final float hue) {

        // Get saturation and brightness.
        final float[] hsbVals = new float[3];
        Color.RGBtoHSB(c.getRed(), c.getGreen(), c.getBlue(), hsbVals);

        // Pass .5 (= 180 degrees) as HUE
        c = new Color(Color.HSBtoRGB(hue, hsbVals[1], hsbVals[2]));

        return c;
    }

    public static void updateScroll() {
        lastLastScrollAmount = lastScrollAmount;
        lastScrollAmount = scrollAmount;
        MainMenu mainMenu = (MainMenu) Rise.INSTANCE.getGuiMainMenu();

        if (lastModulePosition - renderScrollAmount > (mainMenu.drawingClickGui ? mainMenu.getClickGUI().height : Rise.INSTANCE.getClickGUI().height) - categoryHeight) {

            final float wheel = Mouse.getDWheel();
            scrollAmount += wheel / 10.0F;

            if (wheel == 0) {
                scrollAmount -= (lastLastScrollAmount - scrollAmount) * 0.6;
            }

        } else {
            scrollAmount = 0;
        }
    }

    public void renderModule(final float x, final float y, final float width, final float height, final Module m) {
        //Module background
        RenderUtil.roundedRect(x, y, width, height, 5, new Color(255, 255, 255, 10));

        //Module name
        CustomFont.drawString(m.getModuleInfo().name(), x + 4, y + 6, ((m.isEnabled()) ? booleanColor2 : new Color(237, 237, 237)).getRGB());

        //Switch
        RenderUtil.roundedRect(x + width - 15, y + 8, 10, 5, 5, new Color(255, 255, 255, 255));
        RenderUtil.circle(x + width - ((m.isEnabled()) ? 10 : 17), y + 7, 7, booleanColor1);

        if (m.clickGuiOpacity != 1)
            RenderUtil.roundedRect(x, y, width, height, 5, new Color(39, 42, 48, Math.round(m.clickGuiOpacity)));

        //Module description
        if (m.descOpacityInGui > 1)
            CustomFont.drawStringSmall(m.getModuleInfo().description(), x + (CustomFont.getWidth(m.getModuleInfo().name())) + 8, y + 7.5, new Color(175, 175, 175, Math.round(m.descOpacityInGui)).getRGB());

    }

//    public void renderScript(final float x, final float y, final float width, final float height, final Script script) {
//        //Module background
//        RenderUtil.roundedRect(x, y, width, height, 5, new Color(255, 255, 255, 10));
//
//        //Module name
//        CustomFont.drawString(script.getName(), x + 4, y + 6, ((script.isEnabled()) ? booleanColor2 : new Color(237, 237, 237)).getRGB());
//
//        //Switch
//        RenderUtil.roundedRect(x + width - 15, y + 8, 10, 5, 5, new Color(255, 255, 255, 255));
//        RenderUtil.circle(x + width - ((script.isEnabled()) ? 10 : 17), y + 7, 7, booleanColor1);
//
//        if (script.clickGuiOpacity != 1)
//            RenderUtil.roundedRect(x, y, width, height, 5, new Color(39, 42, 48, Math.round(script.clickGuiOpacity)));
//    }

    public void renderModule(final float x, final float y, final float width, final float height, final String n) {
        //Module background
        RenderUtil.roundedRect(x, y, width, height, 5, new Color(255, 255, 255, 10));

        //Module name
        CustomFont.drawString(n, x + 4, y + 6, booleanColor2.hashCode());
    }

    public void mouseClicked(final int mouseX, final int mouseY, final int button) {
        final int grabSize = 10;

//        if (blockScriptEditorOpen) {
//            GuiBlockScript.mouseClicked(mouseX, mouseY, button);
//            return;
//        }

        if (mouseOver(x + width - grabSize + 3, y + height - grabSize + 3, grabSize, grabSize, mouseX, mouseY) && !calledFromMenu) {
            resizingGui = true;
            return;
        }

        switch (selectedCat) {
            case SCRIPTS:
                //Open scripts folder
//                if (mouseOver(x + categoryWidth + moduleWidth - 84, y + 3, 81, categoryHeight - 3 * 2, mouseX, mouseY)) {
//                    try {
//                        Desktop.getDesktop().open(new File("rise" + separator + "Script"));
//                    } catch (final Exception ignored) {
//                    }
//                }
                break;

            case CONFIGS:
                try {
                    //Open configs folder
                    if (mouseOver(x + width - 99, (float) (y + categoryHeight + renderScrollAmount + (Math.ceil(onlineConfigs.size() / 3D) * 45) + 30), 90, 20, mouseX, mouseY)) {
                        Desktop.getDesktop().open(new File("rise" + separator + "Config"));
                        mc.getSoundHandler().playSound(PositionedSoundRecord.create(new ResourceLocation("gui.button.press"), 1.0F));
                    }
                } catch (final Exception ignored) {
                }

                break;

//            case STORE:
//                if (scriptState == ScriptState.DONE) {
//                    int columns = 0, rows = 0;
//                    for (final OnlineScriptHandler.OnlineScript script : scriptList) {
//
//                        if (mouseOver((float) (positionXOfScript + (columns * (widthOfScript + gap))), (float) (positionYOfScript + (rows * (heightOfScript + gap))), (float) (widthOfScript), (float) (heightOfScript), mouseX, mouseY)) {
//
//                            //Mouse over download button
//                            if (mouseOver((float) ((float) (positionXOfScript + (columns * (widthOfScript + gap))) + widthOfScript - (14 / 1.2f) - 10), (float) ((float) (positionYOfScript + (rows * (heightOfScript + gap))) + heightOfScript - (13 / 1.2f) - 10), (14 / 1.2f) * 2, (13 / 1.2f) * 2, mouseX, mouseY)) {
//
//                                if (FileUtil.exists("Script" + separator + script.getName() + ".js")) {
//                                    Rise.INSTANCE.getNotificationManager().registerNotification("A script with that name already exists in your script folder.");
//                                } else {
//                                    try {
//                                        //Creates the script
//                                        Files.write(Paths.get("rise" + separator + "Script" + separator + script.getName() + ".js"), Collections.singleton(script.getCode()));
//                                    } catch (final Exception ignored) {
//                                        Rise.INSTANCE.getNotificationManager().registerNotification("Failed to download script.");
//                                    }
//
//                                    Rise.INSTANCE.getScriptManager().reloadScripts();
//                                    Rise.INSTANCE.getNotificationManager().registerNotification("Successfully downloaded " + script.getName() + ".");
//
//                                }
//                            }
//                        }
//
//                        columns++;
//                        if (columns >= 2) {
//                            columns = 0;
//                            rows++;
//                        }
//
//                    }
//                }
//                break;
        }

        int amount = 0;
        for (final Category c : Category.values()) {
            if (mouseOver(x, y + categoryHeight * (amount + 1) + 8, categoryWidth, categoryHeight, mouseX, mouseY) && selectedCat != c) {
                if (c == Category.CONFIGS) {
                    configState = ConfigState.NONE;
                    onlineConfigs = null;
                }

//                if (c == Category.STORE) {
//                    scriptState = ScriptState.NONE;
//                    scriptList = null;
//
//                    if (button == 1) {
//                        blockScriptEditorOpen = true;
//                        GuiBlockScript.onInit();
//                    }
//                }
                
                selectedCat = c;
                renderScrollAmount = lastScrollAmount = lastLastScrollAmount = scrollAmount = 0;
                tabSwapTime = System.currentTimeMillis();

                mc.getSoundHandler().playSound(PositionedSoundRecord.create(new ResourceLocation("gui.button.press"), 1.0F));

                for (final Module m : Rise.INSTANCE.getModuleManager().getModuleList()) {
                    for (final Setting s : m.getSettings()) {
                        if (s instanceof NumberSetting) {
                            final NumberSetting numberSetting = ((NumberSetting) s);
                            numberSetting.renderPercentage = 0;
                        }
                    }
                }

            }

            ++amount;
        }

        //Modules
        heightOffset = 0;
        amount = 0;

        if (mouseOver(x + categoryWidth, y + categoryHeight, width - categoryWidth, height - categoryHeight, mouseX, mouseY)) {
//            System.out.println(y + " " + categoryHeight + " " + heightOffset + " " + offset + " " + amount + " " + renderScrollAmount + " " + moduleHeight);
            for (final Module m : Rise.INSTANCE.getModuleManager().getModuleList()) {
                if (m.isHidden()) continue;
                m.sizeInGui = moduleHeight;

                if (m.expanded) {
                    m.sizeInGui = categoryHeight;

                    for (final Setting ignored : m.getSettings()) {
                        m.sizeInGui += (ignored instanceof NoteSetting && m.getSettings().indexOf(ignored) != 0) ? 18 : 12;
                    }
                }

                if (m.getModuleInfo().category() == selectedCat) {
                    if (m.expanded) {
                        m.sizeInGui = categoryHeight;

                        for (final Setting s : m.getSettings()) {

                            if (!s.isHidden()) {

                                final float settingsX = x + categoryWidth + offset + 4;
                                final float settingsY = y + categoryHeight + heightOffset + offset * amount + m.sizeInGui + renderScrollAmount;

                                if (mouseOver(settingsX, settingsY, width - categoryWidth - offset * 3, 11, mouseX, mouseY)) {
                                    if (s instanceof BooleanSetting) {
                                        if (button == 0) ((BooleanSetting) s).toggle();
                                    }
                                    if (s instanceof NumberSetting) {
                                        if (button == 0) selectedSlider = (NumberSetting) s;
                                    }
                                    if (s instanceof ModeSetting) {
                                        if (button == 0) ((ModeSetting) s).cycle(true);
                                        if (button == 1) ((ModeSetting) s).cycle(false);
                                    }
                                }

                                m.sizeInGui += (s instanceof NoteSetting && m.getSettings().indexOf(s) != 0) ? 18 : 12;
                            }
                        }
                    }

                    if (mouseOver(x + categoryWidth, y + categoryHeight + heightOffset + offset * amount + renderScrollAmount, moduleWidth, moduleHeight, mouseX, mouseY)) {
                        if (button == 0 && !m.getModuleInfo().name().equals("ClickGui")) {
                            if (calledFromMenu) m.toggleNoEvent(); else m.toggleModule();
                            mc.getSoundHandler().playSound(PositionedSoundRecord.create(new ResourceLocation("gui.button.press"), 1.0F));
                        }
                        if (button == 1) {
                            m.expanded = !m.expanded;
                        }
                    }

                    heightOffset += m.sizeInGui;

                    amount++;
                }
            }

            if (selectedCat == Category.CONFIGS && onlineConfigs != null) {

                int column3 = 0, row3 = 0;

                for (final String config : onlineFeaturedConfigs) {
                    if (mouseOver(x + categoryWidth + offset + column3 * (moduleWidth/3 - 3) + 5, (float) (y + categoryHeight + row3 * 90 + renderScrollAmount + 25), moduleWidth / 3 - 12, 35, mouseX, mouseY)) {
                        OnlineConfig.loadConfig(config.split("-")[0] + ".txt");
                    }

                    if (column3 == 2) {
                        column3 = 0;
                        row3++;
                    } else {
                        column3++;
                    }
                }

                int row = 0, column = 0;
                double onlineOffset = (Math.ceil(onlineFeaturedConfigs.size() / 3D) * 90) + 35;

                for (final String onlineConfig : onlineConfigs) {
                    if (mouseOver(x + categoryWidth + offset + column * (moduleWidth/3 - 3) + 5, (float) (y + categoryHeight + row * 45 + renderScrollAmount + onlineOffset + 25), moduleWidth / 3 - 12, 35, mouseX, mouseY)) {;
                        OnlineConfig.loadConfig(onlineConfig.split("-")[0] + ".txt");
                    }

                    if (column == 2) {
                        column = 0;
                        row++;
                    } else {
                        column++;
                    }
                }

                double localOffset = (Math.ceil(onlineConfigs.size() / 3D) * 45) + onlineOffset;
                int row2 = 0;

                for (final String localConfig : localConfigs) {
                    if (mouseOver(x + categoryWidth + offset + 5, (float) (y + categoryHeight + (row2) * 25 + renderScrollAmount + localOffset + 55), moduleWidth - 10, 20,  mouseX, mouseY)) {
                        ConfigHandler.load(localConfig.split("/")[0]);
                    }
                    row2++;
                }
            }

//            if (selectedCat == Category.SCRIPTS) {
//                amount = 0;
//                for (final Script script : Rise.INSTANCE.getScriptManager().getScripts()) {
//                    if (mouseOver(x + categoryWidth, y + categoryHeight + moduleHeight * amount + offset * amount + renderScrollAmount, moduleWidth, moduleHeight, mouseX, mouseY)) {
//                        if (button == 0) {
//                            script.toggleScript();
//                            mc.getSoundHandler().playSound(PositionedSoundRecord.create(new ResourceLocation("gui.button.press"), 1.0F));
//                        }
//                        if (button == 1) {
//                            script.expanded = !script.expanded;
//                        }
//                    }
//
//                    heightOffset += 12;
//                    ++amount;
//                }
//            }
        }

        if (mouseOver(x, y, width, categoryHeight, mouseX, mouseY) && !calledFromMenu) {
            holdingGui = true;
            holdingOffsetX = x - mouseX;
            holdingOffsetY = y - mouseY;
        }

        final int radarX = (int) ((NumberSetting) Objects.requireNonNull(Rise.INSTANCE.getModuleManager().getSetting("Radar", "Radar X"))).getValue();
        final int radarY = (int) ((NumberSetting) Objects.requireNonNull(Rise.INSTANCE.getModuleManager().getSetting("Radar", "Radar Y"))).getValue();

        if (mouseOver(radarX, radarY, 70, 70, mouseX, mouseY)) {
            oldMouseX = radarX - mouseX;
            oldMouseY = radarY - mouseY;
            draggingRadar = true;
        }

    }

    public void mouseReleased(final int mouseX, final int mouseY, final int state) {
        selectedSlider = null;
        holdingGui = resizingGui = false;
        draggingRadar = false;
        //if (blockScriptEditorOpen) GuiBlockScript.releasedMouseButton();
        super.mouseReleased(mouseX, mouseY, state);
    }

    public boolean mouseOver(final float posX, final float posY, final float width, final float height, final float mouseX, final float mouseY) {
        return mouseX > posX && mouseX < posX + width && mouseY > posY && mouseY < posY + height;
    }

    public void onGuiClosed() {
        selectedSlider = null;
        holdingGui = resizingGui = false;
        if (!calledFromMenu) Objects.requireNonNull(Rise.INSTANCE.getModuleManager().getModule("ClickGui")).toggleModule();
    }

//    private static double round(final double value, final int places) {
//        if (places < 0) throw new IllegalArgumentException();
//
//        BigDecimal bd = new BigDecimal(Double.toString(value));
//        bd = bd.setScale(places, RoundingMode.HALF_UP);
//        return bd.doubleValue();
//    }

    private static double round(final double value, final float places) {
        if (places < 0) throw new IllegalArgumentException();

        final double precision = 1 / places;
        return Math.round(value * precision) / precision;
    }

    private long days(final String date) {
        // creating the date 1 with sample input date.
        final Date date1 = new Date(Year.now().getValue(), Calendar.getInstance().get(Calendar.MONTH) + 1, Calendar.getInstance().get(Calendar.DATE));

        // creating the date 2 with sample input date.
        final String[] split = date.split("/");

        final Date date2 = new Date(Integer.parseInt(split[2]), Integer.parseInt(split[1]), Integer.parseInt(split[0]));

        // getting milliseconds for both dates
        final long date1InMs = date1.getTime();
        final long date2InMs = date2.getTime();

        // getting the diff between two dates.
        long timeDiff = 0;
        if (date1InMs > date2InMs) {
            timeDiff = date1InMs - date2InMs;
        } else {
            timeDiff = date2InMs - date1InMs;
        }

        // print diff in days
        return (int) (timeDiff / (1000 * 60 * 60 * 24));
    }

    private void updateRainbow(final ModeSetting theme) {
        if (theme.is("Disco")) {
            customHue = customHue > 360 ? 0 : customHue + 9;
            colorModules = changeHue(colorModules, customHue / 360f);
            booleanColor1 = changeHue(booleanColor1, customHue / 360f);
            booleanColor2 = changeHue(booleanColor2, customHue / 360f);
            settingColor3 = changeHue(settingColor3, customHue / 360f);
        }
    }
    
    private float lerp(double start, double stop, double progress) {
        return (float) (start * (1 - progress) + (stop * progress));
    }
}
