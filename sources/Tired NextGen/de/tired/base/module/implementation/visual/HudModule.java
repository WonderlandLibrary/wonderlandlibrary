package de.tired.base.module.implementation.visual;

import de.tired.util.animation.Animation;
import de.tired.util.animation.Easings;
import de.tired.base.annotations.ModuleAnnotation;

import de.tired.util.render.RenderUtil;
import de.tired.base.guis.newclickgui.setting.ModeSetting;
import de.tired.base.guis.newclickgui.setting.NumberSetting;
import de.tired.base.guis.newclickgui.setting.impl.BooleanSetting;
import de.tired.base.guis.newclickgui.setting.impl.ColorPickerSetting;
import de.tired.base.guis.newclickgui.setting.impl.TextBoxSetting;
import de.tired.base.font.CustomFont;
import de.tired.base.font.FontManager;
import de.tired.util.render.AnimationUtil;
import de.tired.util.render.ColorUtil;
import de.tired.base.dragging.DragHandler;
import de.tired.base.dragging.Draggable;
import de.tired.base.event.EventTarget;
import de.tired.base.event.events.EventTick;
import de.tired.base.event.events.Render2DEvent;
import de.tired.base.interfaces.FHook;
import de.tired.base.module.Module;
import de.tired.base.module.ModuleCategory;
import de.tired.base.module.ModuleManager;
import de.tired.Tired;
import de.tired.util.render.shaderloader.ShaderManager;
import de.tired.util.render.shaderloader.list.RoundedRectGradient;
import de.tired.util.render.shaderloader.list.RoundedRectShader;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiOverlayDebug;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.util.ArrayList;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;

@ModuleAnnotation(name = "Hud", category = ModuleCategory.RENDER)

public class HudModule extends Module {

    public BooleanSetting rightBar = new BooleanSetting("Bar", this, true);
    public BooleanSetting textshadow = new BooleanSetting("textshadow", this, true);
    public BooleanSetting minecraftFont = new BooleanSetting("minecraftFont", this, true);

    public BooleanSetting arrayListBackground = new BooleanSetting("arrayListBackground", this, true);

    public BooleanSetting arrayListTopBar = new BooleanSetting("arrayListTopBar", this, true);

    public BooleanSetting customSpacing = new BooleanSetting("customSpacing", this, true);

    public NumberSetting spacing = new NumberSetting("spacing", this, 9, 8, 12, 1, () -> customSpacing.getValue());

    public NumberSetting textSize = new NumberSetting("textSize", this, 20, 3, 24, 1, () -> !minecraftFont.getValue());

    public ColorPickerSetting backgroundColor = new ColorPickerSetting("backgroundColor", this, new Color(0, 0, 0, 255), () -> arrayListBackground.getValue());

    public ModeSetting arraylistPosition = new ModeSetting("arraylistPosition", this, new String[]{"Left", "Right"});
    public NumberSetting backgroundAlpha = new NumberSetting("backgroundAlpha", this, 122, 1, 255, 1);
    public NumberSetting offset = new NumberSetting("offset", this, 3, 0, 20, 1);
    public NumberSetting addTextY = new NumberSetting("addTextY", this, 0, 0, 2, 1);

    public static ArrayList<Module> listToUse;
    public TextBoxSetting text = new TextBoxSetting("", this, "Tired");

    private final ModeSetting colorMode = new ModeSetting("ColorMode", this, new String[]{"Static", "CustomFade", "Rainbow", "Tired"});

    private final ModeSetting fontType = new ModeSetting("FontType", this, new String[]{"Confortaa", "SFUIMedium20", "raleWay", "InterLight"});

    private CustomFont arraylistFont = FontManager.raleWay10;

    private final ModeSetting logoMode = new ModeSetting("logoMode", this, new String[]{"NextGen", "Minecraft", "Logo"});

    public ColorPickerSetting staticColor = new ColorPickerSetting("StaticColor", this, new Color(57, 165, 189, 255), () -> colorMode.getValue().equalsIgnoreCase("Static"));

    public ColorPickerSetting fadeColor1 = new ColorPickerSetting("fadeColor1", this, new Color(57, 165, 189, 255), () -> colorMode.getValue().equalsIgnoreCase("CustomFade"));

    public ColorPickerSetting fadeColor2 = new ColorPickerSetting("fadeColor2", this, new Color(57, 165, 189, 255), () -> colorMode.getValue().equalsIgnoreCase("CustomFade"));

    public NumberSetting rainbowTime = new NumberSetting("rainbowTime", this, 3, 1, 6, .1, () -> colorMode.getValue().equalsIgnoreCase("Rainbow"));

    public NumberSetting fadeSpeed = new NumberSetting("fadeSpeed", this, 3, 1, 22, .1, () -> colorMode.getValue().equalsIgnoreCase("CustomFade"));

    public BooleanSetting seperateTextColor = new BooleanSetting("seperateTextColor", this, true);

    private ModeSetting colorModeText = new ModeSetting("colorModeText", this, new String[]{"StaticC", "CustomFadeC", "RainbowC", "TiredC"}, () -> seperateTextColor.getValue());

    public ColorPickerSetting textColor = new ColorPickerSetting("textColor", this, new Color(57, 165, 189, 255), () -> colorModeText.getValue().equalsIgnoreCase("StaticC") && seperateTextColor.getValue());

    public ColorPickerSetting fadeColor1Text = new ColorPickerSetting("fadeColor1Text", this, new Color(57, 165, 189, 255), () -> colorModeText.getValue().equalsIgnoreCase("CustomFadeC") && seperateTextColor.getValue());

    public ColorPickerSetting fadeColor2Text = new ColorPickerSetting("fadeColor2Text", this, new Color(57, 165, 189, 255), () -> colorModeText.getValue().equalsIgnoreCase("CustomFadeC") && seperateTextColor.getValue());

    public NumberSetting rainbowTimeText = new NumberSetting("rainbowTimeText", this, 3, 1, 6, .1, () -> colorModeText.getValue().equalsIgnoreCase("RainbowC") && seperateTextColor.getValue());

    public NumberSetting fadeSpeedText = new NumberSetting("fadeSpeedText", this, 3, 1, 22, .1, () -> colorModeText.getValue().equalsIgnoreCase("CustomFadeC") && seperateTextColor.getValue());

    public float animationX;

    public float borderSize;

    private int size;

    public CustomFont font;

    private String mode;
    float bps;

    public HudModule() {

    }

    public static HudModule getInstance() {
        return ModuleManager.getInstance(HudModule.class);
    }

    public Draggable drag = DragHandler.setupDrag(this, "logo", 33, 33, false);

    @EventTarget
    public void onTick(EventTick eventTick) {
    }

    @EventTarget
    public void onRender(Render2DEvent e) {
        int lastSize = this.size;
        this.size = textSize.getValueInt();

        String lastMode = this.mode;
        this.mode = fontType.getValue();
        if (size != lastSize || !Objects.equals(mode, lastMode)) {
            switch (fontType.getValue()) {
                case "InterLight": {
                    //   this.lastFont = this.arraylistFont;
                    arraylistFont = new CustomFont(FontManager.getFont(FontManager.locationMap, FontManager.interLightLocation, textSize.getValueInt()), true, true);
                    break;
                }
                case "Confortaa":
                    //    this.lastFont = this.arraylistFont;
                    arraylistFont = new CustomFont(FontManager.getFont(FontManager.locationMap, FontManager.confortaaLocation, textSize.getValueInt()), true, true);
                    break;
                case "SFUIMedium20":
                    //     this.lastFont = this.arraylistFont;
                    arraylistFont = new CustomFont(FontManager.getFont(FontManager.locationMap, FontManager.SFMediumLocation, textSize.getValueInt()), true, true);
                    break;
                case "raleWay":
                    //    this.lastFont = this.arraylistFont;
                    arraylistFont = new CustomFont(FontManager.getFont(FontManager.locationMap, FontManager.raleWay, textSize.getValueInt()), true, true);
                    break;
            }
        }


        final ScaledResolution sr = new ScaledResolution(MC);

        float rat = Minecraft.getMinecraft().timer.ticksPerSecond * Minecraft.getMinecraft().timer.timerSpeed;
        this.bps = (float) (MC.thePlayer.getDistance(MC.thePlayer.lastTickPosX, MC.thePlayer.posY, MC.thePlayer.lastTickPosZ) * rat);

        this.font = FontManager.futuraNormal;

        borderSize = rightBar.getValue() ? 1 : 0;
        FontManager.raleWay20.drawString("FPS:" + Minecraft.getDebugFPS(), 2, sr.getScaledHeight() - 21, Integer.MAX_VALUE);

        FontManager.raleWay20.drawString(Tired.INSTANCE.CLIENT_NAME + " B" + Tired.INSTANCE.VERSION, 2, sr.getScaledHeight() - 10, Integer.MAX_VALUE);

        ModuleManager.sortedModList.sort(minecraftFont.getValue() ? (m1, m2) -> MC.fontRendererObj.getStringWidth(m2.getNameWithSuffix()) - MC.fontRendererObj.getStringWidth(m1.getNameWithSuffix()) : (m1, m2) -> font.getStringWidth(m2.getNameWithSuffix()) - font.getStringWidth(m1.getNameWithSuffix()));
        listToUse = ModuleManager.sortedModList;


        animationX = (float) AnimationUtil.getAnimationState(animationX, MC.gameSettings.showDebugProfilerChart ? GuiOverlayDebug.y2 : offset.getValue(), 120);


        int texColor = ColorUtil.fade(textColor.ColorPickerC.brighter(), 100, 2 + 10).getRGB();

        Color firstColor = ColorUtil.interpolateColorsBackAndForth(12, 0, new Color(84, 51, 158).brighter(), new Color(104, 127, 203), true);
        Color secondColor = ColorUtil.interpolateColorsBackAndForth(12, 90, new Color(84, 51, 158), new Color(104, 127, 203).darker(), false);


        float width = logoMode.getValue().equalsIgnoreCase("Minecraft") ? MC.fontRendererObj.getStringWidth("TIRED") : FontManager.raleWay30.getStringWidth("TIRED") + FontManager.raleWay20.getStringWidth("Nextgen.") + 10;
        drag.setObjectWidth(width);
        drag.setObjectHeight(logoMode.getValue().equalsIgnoreCase("Minecraft") ? 10 : 30);
        if (logoMode.getValue().equalsIgnoreCase("Logo")) {
            drag.setObjectWidth(129);
            drag.setObjectHeight(50);
            RenderUtil.instance.doRenderShadow(Color.BLACK, drag.getXPosition(), drag.getYPosition() - 5, 50, 50, 22);
            ShaderManager.shaderBy(RoundedRectGradient.class).drawRound(drag.getXPosition(), drag.getYPosition() - 5, 50, 50, 18, firstColor, firstColor, secondColor, secondColor);
            FontManager.logo40.drawString("A", drag.getXPosition() + 2, drag.getYPosition() + 6, Color.WHITE.getRGB());
            FontManager.raleWay30.drawString("NextGen.", drag.getXPosition() + 58, drag.getYPosition() + 15, Integer.MAX_VALUE);
        }
        if (logoMode.getValue().equalsIgnoreCase("Nextgen")) {

            FontManager.raleWay30.drawString("TIRED", drag.getXPosition(), drag.getYPosition(), Integer.MIN_VALUE);
            RenderUtil.instance.applyGradientHorizontal(drag.getXPosition(), drag.getYPosition(), width, 20, 1f, firstColor, secondColor, () -> {
                FontManager.raleWay30.drawString("TIRED", drag.getXPosition(), drag.getYPosition(), texColor);
            });

            FontManager.raleWay20.drawString("Nextgen.", FontManager.raleWay30.getStringWidth("TIRED") + 6 + drag.getXPosition(), drag.getYPosition() + 7, Integer.MAX_VALUE);

        }
        if (logoMode.getValue().equalsIgnoreCase("Minecraft")) {
            MC.fontRendererObj.drawStringWithShadow("T", drag.getXPosition(), drag.getYPosition(), firstColor.getRGB());
            MC.fontRendererObj.drawStringWithShadow("ired", drag.getXPosition() + MC.fontRendererObj.getStringWidth("T") + 1, drag.getYPosition(), -1);
        }


    }

    public void drawArraylist(boolean rect) {

        final ArrayList<Module> toggled = new ArrayList<>();


        for (Module module : listToUse) {


            int height;

            if (!customSpacing.getValue()) {
                if (minecraftFont.getValue())
                    height = MC.fontRendererObj.FONT_HEIGHT + 2;
                else
                    height = fontType.getValue().equalsIgnoreCase("Confortaa") ? arraylistFont.getStringHeight(module.name) + 4 : arraylistFont.getStringHeight(module.name) + 2;
            } else {
                height = spacing.getValueInt();
            }
            module.slideAnimation.update();
            module.yAnimation.update();

            if (!module.isState())
                module.animation = 0;

            if (!minecraftFont.getValue()) {
                switch (arraylistPosition.getValue()) {
                    case "Left":
                        module.slideAnimation.animate(!module.isState() ? -arraylistFont.getStringWidth(module.name) - 10 : 4, .0, Easings.BACK_IN4);
                        break;
                    case "Right":
                        module.slideAnimation.animate(module.isState() ? arraylistFont.getStringWidth(module.name) + 2 : -4, .0, Easings.BACK_IN4);
                        break;
                }
            } else {
                switch (arraylistPosition.getValue()) {
                    case "Left":
                        module.slideAnimation.animate(!module.isState() ? -MC.fontRendererObj.getStringWidth(module.name) - 10 : 4, .0, Easings.BACK_IN4);
                        break;
                    case "Right":
                        module.slideAnimation.animate(module.isState() ? MC.fontRendererObj.getStringWidth(module.name) + 2 : -4, .0, Easings.BACK_IN4);
                        break;
                }
            }

            if (!module.state) {
                module.slideAnimation.setValue(-arraylistFont.getStringWidth(module.name) - 30);
            }

            boolean noRender = false;
            if (!minecraftFont.getValue()) {
                switch (arraylistPosition.getValue()) {
                    case "Left":
                        noRender = Math.round(module.slideAnimation.getValue()) <= -arraylistFont.getStringWidth(module.name) + offset.getValueInt();
                        break;
                    case "Right":
                        noRender = Math.round(module.slideAnimation.getValue()) <= -2;
                        break;
                }
            } else {
                switch (arraylistPosition.getValue()) {
                    case "Left":
                        noRender = Math.round(module.slideAnimation.getValue()) <= -MC.fontRendererObj.getStringWidth(module.name) - 6;
                        break;
                    case "Right":
                        noRender = Math.round(module.slideAnimation.getValue()) <= -2;
                        break;
                }
            }


            if (noRender)
                module.yAnimation.animate(0, .05, Easings.NONE);
            else
                module.yAnimation.animate(height, .05, Easings.NONE);


            if (module.yAnimation.getValue() != 0.0 || !noRender)
                toggled.add(module);
            else
                toggled.remove(module);

            if (!minecraftFont.getValue())
                toggled.sort((m1, m2) -> arraylistFont.getStringWidth(m2.name) - arraylistFont.getStringWidth(m1.name));
            else
                toggled.sort((m1, m2) -> MC.fontRendererObj.getStringWidth(m2.name) - MC.fontRendererObj.getStringWidth(m1.name));
        }

        AtomicInteger yAxis = new AtomicInteger(arraylistPosition.getValue().equalsIgnoreCase("Left") ? 22 : 2);

        if (arrayListTopBar.getValue() && arraylistPosition.getValue().equalsIgnoreCase("Right"))
            yAxis = new AtomicInteger(3);

        if (arrayListTopBar.getValue() && arraylistPosition.getValue().equalsIgnoreCase("Left"))
            yAxis = new AtomicInteger(24);

        yAxis.addAndGet(offset.getValueInt());

        for (int i = 0; i < toggled.size(); i++) {
            final Module mod = toggled.get(i);
            final Animation slideAnimation = mod.slideAnimation, yAnimation = mod.yAnimation;

            final ScaledResolution resolution = new ScaledResolution(MC);

            float positionX;

            Color color = Color.RED;

            switch (colorMode.getValue()) {
                case "Static":
                    color = staticColor.ColorPickerC;
                    break;
                case "CustomFade":
                    color = ColorUtil.interpolateColorsBackAndForth(fadeSpeed.getValueInt(), yAxis.get(), fadeColor1.ColorPickerC, fadeColor2.ColorPickerC, false);
                    break;
                case "Rainbow":
                    color = ColorUtil.rainbow((long) (System.nanoTime() * rainbowTime.getValue()), yAxis.get() / 4, .8f);
                    break;
                case "Tired":
                    color = ColorUtil.interpolateColorsBackAndForth(20, yAxis.get(), new Color(84, 51, 158).brighter(), new Color(104, 127, 203), true);
                    break;

            }

            Color textColorC = Color.RED;

            switch (colorModeText.getValue()) {
                case "StaticC":
                    textColorC = textColor.ColorPickerC;
                    break;
                case "CustomFadeC":
                    textColorC = ColorUtil.interpolateColorsBackAndForth(fadeSpeedText.getValueInt(), yAxis.get(), fadeColor1Text.ColorPickerC, fadeColor2Text.ColorPickerC, false);
                    break;
                case "RainbowC":
                    textColorC = ColorUtil.rainbow((long) (System.nanoTime() * rainbowTimeText.getValue()), yAxis.get(), .8f);
                    break;
                case "TiredC":
                    textColorC = ColorUtil.interpolateColorsBackAndForth(20, yAxis.get(), new Color(84, 51, 158).brighter(), new Color(104, 127, 203), true);
                    break;

            }

            if (arraylistPosition.getValue().equalsIgnoreCase("Left"))
                positionX = slideAnimation.getValue() + offset.getValueInt();
            else
                positionX = resolution.getScaledWidth() - slideAnimation.getValue() - offset.getValueInt();

            if (mod.animation < 0.1F)
                mod.animation += 0.21 * RenderUtil.instance.delta;
            if (mod.animation > 0.1F)
                mod.animation = 0.1F;

            final ScaledResolution scaledResolution = new ScaledResolution(MC);


            GL11.glPushMatrix();
            if (arraylistPosition.getValue().equalsIgnoreCase("Right")) {
                GL11.glTranslatef(!minecraftFont.getValue() ? scaledResolution.getScaledWidth() - (MC.fontRendererObj.getStringWidth(mod.getName()) + 4) / 2f : scaledResolution.getScaledWidth() - (arraylistFont.getStringWidth(mod.getName()) + 3) / 2f, yAxis.get() - 3 / 2 - 1, 0);
                GL11.glScalef(0.9F + mod.animation, 0.9F + mod.animation, 0);
                GL11.glTranslatef(-(!minecraftFont.getValue() ? scaledResolution.getScaledWidth() - (MC.fontRendererObj.getStringWidth(mod.getName()) + 4) / 2f : scaledResolution.getScaledWidth() - (arraylistFont.getStringWidth(mod.getName()) + 3) / 2f), -(yAxis.get() - 3 / 2 - 1), 0);
            } else {
                GL11.glTranslatef(!minecraftFont.getValue() ? (MC.fontRendererObj.getStringWidth(mod.getName()) + 4) / 2f : (arraylistFont.getStringWidth(mod.getName()) + 3) / 2f, yAxis.get() - 3 / 2f - 1, 0);
                GL11.glScalef(0.9F + mod.animation, 0.9F + mod.animation, 0);
                GL11.glTranslatef(-(!minecraftFont.getValue() ? (MC.fontRendererObj.getStringWidth(mod.getName()) + 4) / 2f : (arraylistFont.getStringWidth(mod.getName()) + 3) / 2f), -(yAxis.get() - 3 / 2f - 1), 0);
            }
            int height;


            if (!customSpacing.getValue()) {
                if (minecraftFont.getValue())
                    height = MC.fontRendererObj.FONT_HEIGHT + 2;
                else
                    height = fontType.getValue().equalsIgnoreCase("Confortaa") ? arraylistFont.getStringHeight(mod.name) + 4 : arraylistFont.getStringHeight(mod.name) + 2;
            } else {
                height = spacing.getValueInt();
            }

            if (rect) {
                if (arrayListTopBar.getValue()) {
                    if (i == 0) {
                        if (minecraftFont.getValue())
                            RenderUtil.instance.drawRect2(arraylistPosition.getValue().equalsIgnoreCase("Left") ? positionX - 3 : positionX - 1, yAxis.get() - 3, arraylistPosition.getValue().equalsIgnoreCase("Left") ? MC.fontRendererObj.getStringWidth(mod.getName()) + 5 : MC.fontRendererObj.getStringWidth(mod.getName()) + 4, 1, color.getRGB());
                        else
                            RenderUtil.instance.drawRect2(arraylistPosition.getValue().equalsIgnoreCase("Left") ? positionX - 2 : positionX - 1, yAxis.get() - 3, arraylistPosition.getValue().equalsIgnoreCase("Left") ? arraylistFont.getStringWidth(mod.getName()) + 4 : arraylistFont.getStringWidth(mod.getName()) + 3, 1, color.getRGB());
                    }
                }


                if (arrayListBackground.getValue()) {
                    if (minecraftFont.getValue()) {
                        final Color c = new Color(backgroundColor.ColorPickerC.getRed(), backgroundColor.ColorPickerC.getGreen(), backgroundColor.ColorPickerC.getBlue(), backgroundAlpha.getValueInt());
                        //   if (!rect)
                        //       RenderProcessor.renderBlurredShadow(c, arraylistPosition.getValue().equalsIgnoreCase("Left") ? positionX - 2 : positionX - 1, yAxis.get() - 2, MC.fontRendererObj.getStringWidth(mod.getName()) + 4, MC.fontRendererObj.FONT_HEIGHT + 2, 22);
                        RenderUtil.instance.drawRect2(arraylistPosition.getValue().equalsIgnoreCase("Left") ? positionX - 2 : positionX - 1, yAxis.get() - 2, MC.fontRendererObj.getStringWidth(mod.getName()) + 4, height, new Color(backgroundColor.ColorPickerC.getRed(), backgroundColor.ColorPickerC.getGreen(), backgroundColor.ColorPickerC.getBlue(), backgroundAlpha.getValueInt()).getRGB());
                    } else {
                        RenderUtil.instance.drawRect2(positionX - 1, yAxis.get() - 2, arraylistFont.getStringWidth(mod.getName()) + 3, height, new Color(backgroundColor.ColorPickerC.getRed(), backgroundColor.ColorPickerC.getGreen(), backgroundColor.ColorPickerC.getBlue(), backgroundAlpha.getValueInt()).getRGB());
                    }
                }

            } else {
                drawString(mod.name, positionX, yAxis.get() - 2 + height / 2f - 2 + addTextY.getValueInt(), seperateTextColor.getValue() ? textColorC.getRGB() : color.getRGB());

            }
            GlStateManager.popMatrix();
            if (rect) {
                if (rightBar.getValue()) {
                    if (minecraftFont.getValue()) {
                        RenderUtil.instance.drawRect2(arraylistPosition.getValue().equalsIgnoreCase("Left") ? 1 + offset.getValueInt() : (resolution.getScaledWidth()) - offset.getValueInt(), yAxis.get() - 2, 1, height, color.getRGB());
                    } else {
                        RenderUtil.instance.drawRect2(arraylistPosition.getValue().equalsIgnoreCase("Left") ? 1 + offset.getValueInt() + 1 : (resolution.getScaledWidth() - 1) - offset.getValueInt(), yAxis.get() - 2, 1, height, color.getRGB());
                    }
                }
            }


            yAxis.addAndGet(Math.round(mod.yAnimation.getValue()));
        }

    }

    private void drawString(final String text, float x, float y, int color) {
        if (!textshadow.getValue()) {
            if (minecraftFont.getValue())
                MC.fontRendererObj.drawString(text, x + 1, y - 1, color);
            else {
                arraylistFont.drawString(text, x, y + 1, color);
            }
        } else {
            if (minecraftFont.getValue())
                MC.fontRendererObj.drawStringWithShadow(text, x + 1, y - 1, color);
            else {
                arraylistFont.drawStringWithShadow2(text, x, y - 2, color);
            }
        }
    }


    @Override
    public void onState() {
    }

    @Override
    public void onUndo() {

    }
}