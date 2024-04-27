package me.aquavit.liquidsense.ui.client.hud.element.elements;

import com.google.gson.JsonElement;
import me.aquavit.liquidsense.LiquidSense;
import me.aquavit.liquidsense.utils.data.Ranges;
import me.aquavit.liquidsense.utils.render.shader.shaders.RainbowFontShader;
import me.aquavit.liquidsense.utils.render.shader.shaders.RainbowShader;
import me.aquavit.liquidsense.module.Module;
import me.aquavit.liquidsense.ui.client.hud.designer.GuiHudDesigner;
import me.aquavit.liquidsense.ui.client.hud.element.Border;
import me.aquavit.liquidsense.ui.client.hud.element.Element;
import me.aquavit.liquidsense.ui.client.hud.element.ElementInfo;
import me.aquavit.liquidsense.ui.client.hud.element.Side;
import me.aquavit.liquidsense.ui.font.Fonts;
import me.aquavit.liquidsense.utils.render.ColorUtils;
import me.aquavit.liquidsense.utils.render.RenderUtils;
import me.aquavit.liquidsense.utils.client.Replacement;
import me.aquavit.liquidsense.value.*;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.GlStateManager;

import java.awt.*;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@ElementInfo(name = "Arraylist")
public class Arraylist extends Element {

    public Arraylist(){
        super(0.0,5.0,1f,new Side(Side.Horizontal.RIGHT, Side.Vertical.UP));
    }

    private BoolValue alphaSort = new BoolValue("AlphaSort", false);
    private IntegerValue animationXspeedValue = new IntegerValue("ToggleSpeed", 50, 10, 100);
    private BoolValue lowerCaseValue = new BoolValue("LowerCase", true);
    private BoolValue namebreak = new BoolValue("NameBreak", false) {
        @Override
        protected void onChanged(Boolean oldValue, Boolean newValue) {
            if (newValue)
                LiquidSense.moduleManager.getModules().forEach(it -> it.setArrayListName(getBreakName(it.getName())));
            else
                LiquidSense.moduleManager.getModules().forEach(it -> it.setArrayListName(it.getName()));
        }

        @Override
        public void fromJson(JsonElement element) {
            super.fromJson(element);
            onChanged(getValue(), getValue());
        }
    };

    private BoolValue shadowValue = new BoolValue("Shadow",true);
    private FloatValue spaceValue = new FloatValue("Space", 0F, 0F, 5F);

    private BoolValue tags = new BoolValue("Tags", true);
    private Value<String> tagsMode = new ListValue("Tags-Mode", new String[] {"Space","[]", "()", "<>", "-", "Bold", "White"}, "Space").displayable(() -> tags.get());
    private Value<Boolean> tagsArrayColor = new BoolValue("TagsArrayColor", false).displayable(() -> tags.get());
    private IntegerValue textHeightValue = new IntegerValue("TextHeight", 12, 1, 20);
    private IntegerValue textXValue = new IntegerValue("TextX", 2, -5, 10);
    private IntegerValue textYValue = new IntegerValue("TextY", 2, 0, 5);

    private Value<Float> rainbowX = new FloatValue("ShaderRainbow-X", 750F, -2000F, 2000F).displayable(() ->
            colorModeValue.get().equalsIgnoreCase("ShaderRainbow") ||
                    rectColorModeValue.get().equalsIgnoreCase("ShaderRainbow") ||
                    backgroundColorModeValue.get().equalsIgnoreCase("ShaderRainbow"));
    private Value<Float> rainbowY = new FloatValue("ShaderRainbow-Y", -500F, -2000F, 2000F).displayable(() ->
            colorModeValue.get().equalsIgnoreCase("ShaderRainbow") ||
                    rectColorModeValue.get().equalsIgnoreCase("ShaderRainbow") ||
                    backgroundColorModeValue.get().equalsIgnoreCase("ShaderRainbow"));

    public static ListValue colorModeValue = new ListValue("Text-Color", new String[] {"Custom","Random","Rainbow","ShaderRainbow","OneRainbow","Astolfo","Exhibition"}, "OneRainbow");
    private Value<Integer> colorRedValue = new IntegerValue("Text-R", 100, 0, 255).displayable(() -> colorModeValue.get().equalsIgnoreCase("Custom"));
    private Value<Integer> colorGreenValue = new IntegerValue("Text-G", 100, 0, 255).displayable(() -> colorModeValue.get().equalsIgnoreCase("Custom"));
    private Value<Integer> colorBlueValue = new IntegerValue("Text-B", 200, 0, 255).displayable(() -> colorModeValue.get().equalsIgnoreCase("Custom"));
    private Value<Integer> colorAlphaValue = new IntegerValue("Text-A", 255, 0, 255).displayable(() ->
            colorModeValue.get().equalsIgnoreCase("Custom") || colorModeValue.get().equalsIgnoreCase("Rainbow") || colorModeValue.get().equalsIgnoreCase("OneRainbow"));

    private BoolValue rectTopValue = new BoolValue("TobRect", false);
    private ListValue rectValue = new ListValue("Rect-Mode", new String[] {"None", "Left", "ShortLeft" ,"Right","OutLine"}, "Right");
    private IntegerValue rectWidthValue = new IntegerValue("Rect-Width", 0, -1, 5);
    public static ListValue rectColorModeValue = new ListValue("Rect-Color", new String[] {"Custom", "Random", "Rainbow","ShaderRainbow","OneRainbow","Astolfo","Exhibition"}, "Alpha");
    private Value<Integer> rectColorRedValue = new IntegerValue("Rect-R", 255, 0, 255).displayable(() -> rectColorModeValue.get().equalsIgnoreCase("Custom"));
    private Value<Integer> rectColorGreenValue = new IntegerValue("Rect-G", 255, 0, 255).displayable(() -> rectColorModeValue.get().equalsIgnoreCase("Custom"));
    private Value<Integer> rectColorBlueValue = new IntegerValue("Rect-B", 255, 0, 255).displayable(() -> rectColorModeValue.get().equalsIgnoreCase("Custom"));
    private Value<Integer> rectColorAlpha = new IntegerValue("Rect-A", 255, 0, 255).displayable(() ->
            rectColorModeValue.get().equalsIgnoreCase("Custom") || rectColorModeValue.get().equalsIgnoreCase("Rainbow") || rectColorModeValue.get().equalsIgnoreCase("OneRainbow"));

    public static ListValue backgroundColorModeValue = new ListValue("Background-Color", new String[] {"Custom", "Random", "Rainbow","ShaderRainbow","OneRainbow","Astolfo","Exhibition"}, "Custom");
    private IntegerValue backgroundWidthValue = new IntegerValue("Background-Width", 2, -2, 10);
    private Value<Integer> backgroundColorRedValue = new IntegerValue("Background-R", 0, 0, 255).displayable(() -> backgroundColorModeValue.get().equalsIgnoreCase("Custom"));
    private Value<Integer> backgroundColorGreenValue = new IntegerValue("Background-G", 0, 0, 255).displayable(() -> backgroundColorModeValue.get().equalsIgnoreCase("Custom"));
    private Value<Integer> backgroundColorBlueValue = new IntegerValue("Background-B", 0, 0, 255).displayable(() -> backgroundColorModeValue.get().equalsIgnoreCase("Custom"));
    private Value<Integer> backgroundColorAlphaValue = new IntegerValue("Background-A", 50, 0, 255).displayable(() ->
            backgroundColorModeValue.get().equalsIgnoreCase("Custom") || backgroundColorModeValue.get().equalsIgnoreCase("Rainbow") || backgroundColorModeValue.get().equalsIgnoreCase("OneRainbow"));

    private Value<Float> saturationValue = new FloatValue("Saturation", 0.35f, 0f, 1f).displayable(() ->
            colorModeValue.get().equalsIgnoreCase("OneRainbow") || colorModeValue.get().equalsIgnoreCase("Random") ||
            colorModeValue.get().equalsIgnoreCase("Astolfo") || rectColorModeValue.get().equalsIgnoreCase("OneRainbow") ||
            rectColorModeValue.get().equalsIgnoreCase("Random") || rectColorModeValue.get().equalsIgnoreCase("Astolfo") ||
            backgroundColorModeValue.get().equalsIgnoreCase("OneRainbow") || backgroundColorModeValue.get().equalsIgnoreCase("Random") ||
            backgroundColorModeValue.get().equalsIgnoreCase("Astolfo"));
    private Value<Float> brightnessValue = new FloatValue("Brightness", 1.0f, 0f, 1f).displayable(() ->
            colorModeValue.get().equalsIgnoreCase("Random") || colorModeValue.get().equalsIgnoreCase("Astolfo") ||
            rectColorModeValue.get().equalsIgnoreCase("Random") || rectColorModeValue.get().equalsIgnoreCase("Astolfo") ||
            backgroundColorModeValue.get().equalsIgnoreCase("Random") || backgroundColorModeValue.get().equalsIgnoreCase("Astolfo"));
    private Value<Integer> rainbowOffsetValue = new IntegerValue("RainbowOffset", 40, 10, 100).displayable(() ->
            colorModeValue.get().equalsIgnoreCase("Rainbow") || rectColorModeValue.get().equalsIgnoreCase("Rainbow") ||
            backgroundColorModeValue.get().equalsIgnoreCase("Rainbow"));

    private Value<Integer> oneSpeed = new IntegerValue("One-Speed", 6, 0, 20).displayable(() ->
            colorModeValue.get().equalsIgnoreCase("OneRainbow") ||
                    rectColorModeValue.get().equalsIgnoreCase("OneRainbow") ||
                    backgroundColorModeValue.get().equalsIgnoreCase("OneRainbow"));
    private Value<Integer> oneWidth = new IntegerValue("One-Width", 2, 0, 5).displayable(() ->
            colorModeValue.get().equalsIgnoreCase("OneRainbow") ||
                    rectColorModeValue.get().equalsIgnoreCase("OneRainbow") ||
                    backgroundColorModeValue.get().equalsIgnoreCase("OneRainbow"));
    private Value<Float> oneOffset = new FloatValue("One-Offset", 2f, 1f, 5f).displayable(() ->
            colorModeValue.get().equalsIgnoreCase("OneRainbow") ||
                    rectColorModeValue.get().equalsIgnoreCase("OneRainbow") ||
                    backgroundColorModeValue.get().equalsIgnoreCase("OneRainbow"));

    private Value<Integer> oneRedValue = new IntegerValue("One-R", 2, 0, 20).displayable(() ->
            colorModeValue.get().equalsIgnoreCase("OneRainbow") ||
                    rectColorModeValue.get().equalsIgnoreCase("OneRainbow") ||
                    backgroundColorModeValue.get().equalsIgnoreCase("OneRainbow"));
    private Value<Integer> oneGreenValue = new IntegerValue("One-G", 8, 0, 20).displayable(() ->
            colorModeValue.get().equalsIgnoreCase("OneRainbow") ||
                    rectColorModeValue.get().equalsIgnoreCase("OneRainbow") ||
                    backgroundColorModeValue.get().equalsIgnoreCase("OneRainbow"));
    private Value<Integer> oneBlueValue = new IntegerValue("One-B", 8, 0, 20).displayable(() ->
            colorModeValue.get().equalsIgnoreCase("OneRainbow") ||
                    rectColorModeValue.get().equalsIgnoreCase("OneRainbow") ||
                    backgroundColorModeValue.get().equalsIgnoreCase("OneRainbow"));

    private Value<Integer> motionRedValue = new IntegerValue("Motion-R", 128, 0, 255).displayable(() ->
            colorModeValue.get().equalsIgnoreCase("OneRainbow") ||
                    rectColorModeValue.get().equalsIgnoreCase("OneRainbow") ||
                    backgroundColorModeValue.get().equalsIgnoreCase("OneRainbow"));
    private Value<Integer> motionGreenValue = new IntegerValue("Motion-G", 40, 0, 255).displayable(() ->
            colorModeValue.get().equalsIgnoreCase("OneRainbow") ||
                    rectColorModeValue.get().equalsIgnoreCase("OneRainbow") ||
                    backgroundColorModeValue.get().equalsIgnoreCase("OneRainbow"));
    private Value<Integer> motionBlueValue = new IntegerValue("Motion-B", 40, 0, 255).displayable(() ->
            colorModeValue.get().equalsIgnoreCase("OneRainbow") ||
                    rectColorModeValue.get().equalsIgnoreCase("OneRainbow") ||
                    backgroundColorModeValue.get().equalsIgnoreCase("OneRainbow"));

    private FontValue fontValue = new FontValue("Font", Fonts.minecraftFont);

    private int x2;
    private float y2 = 0F;
    private List<Module> modules = new java.util.ArrayList<Module>();
    
    @Override
    public Border drawElement() {
        final FontRenderer fontRenderer = fontValue.get();

        final int delta = RenderUtils.deltaTime;

        for (Module module : LiquidSense.moduleManager.getModules()) {
            if (!module.getArray() || (!module.getState() && module.getSlide() == 2F)) continue;
            String displayString = Replacement.multiReplace(
                    tagsArrayColor.get() ? module.getColorlessTagName() :
                            (tags.get() && tagsMode.get().equalsIgnoreCase("Space") ? module.getTagName() :
                                    (tags.get() && tagsMode.get().equalsIgnoreCase("[]") ? module.getTagName2() :
                                            (tags.get() && tagsMode.get().equalsIgnoreCase("()") ? module.getTagName3() :
                                                    (tags.get() && tagsMode.get().equalsIgnoreCase("<>") ? module.getTagName4() :
                                                            (tags.get() && tagsMode.get().equalsIgnoreCase("-") ? module.getTagName5() :
                                                                    (tags.get() && tagsMode.get().equalsIgnoreCase("Bold") ? module.getTagName6() :
                                                                            (tags.get() && tagsMode.get().equalsIgnoreCase("White") ? module.getTagName7() :
                                                                                    module.getArrayListName()))))))));

            if (lowerCaseValue.get())
                displayString = displayString.toLowerCase();

            int width = fontRenderer.getStringWidth(displayString);
            float deltaX = Math.abs((float) width - module.getSlide());

            if (module.getState()) {
                if (module.getSlide() < width) {
                    module.setSlide(module.getSlide() + Math.abs(((float)width - module.getSlideStep()) / (float)animationXspeedValue.get()) * (float)delta);
                    module.setSlideStep((float)width - deltaX);
                }
            } else if (module.getSlide() > 0) {
                module.setSlide(module.getSlide() - Math.abs(((float)width - module.getSlideStep()) / (float)animationXspeedValue.get()) * (float)delta);
                module.setSlideStep(Math.abs((float)-1 + deltaX));
            }
            module.setSlide(Ranges.coerceIn(module.getSlide(), 0.0f, (float) width));
            module.setSlideStep(Ranges.coerceIn(module.getSlideStep(), 0.0f, (float)width));
        }

        String colorMode = colorModeValue.get();
        String rectColorMode = rectColorModeValue.get();
        String backgroundColorMode = backgroundColorModeValue.get();
        int customColor = new Color(colorRedValue.get(), colorGreenValue.get(), colorBlueValue.get(), colorAlphaValue.get()).getRGB();
        int rectCustomColor = new Color(rectColorRedValue.get(), rectColorGreenValue.get(), rectColorBlueValue.get(), rectColorAlpha.get()).getRGB();

        float space = spaceValue.get();
        int textHeight = textHeightValue.get();
        int textY = textYValue.get();
        int textX = textXValue.get();
        String rectMode = rectValue.get();

        int backgroundCustomColor = new Color(backgroundColorRedValue.get(), backgroundColorGreenValue.get(), backgroundColorBlueValue.get(), backgroundColorAlphaValue.get()).getRGB();

        boolean textShadow = shadowValue.get();
        float textSpacer = (float) textHeight + space;

        float saturation = saturationValue.get();
        float brightness = brightnessValue.get();
        int[] counter = new int[]{0};
        int cou = 0;

        Side side = getSide();

        switch (side.getHorizontal()){
            case MIDDLE:
            case RIGHT:{
                int index = 0;
                for (Module module : modules){
                    String displayString = Replacement.multiReplace(
                            tagsArrayColor.get() ? module.getColorlessTagName() :
                                    (tags.get() && tagsMode.get().equalsIgnoreCase("Space") ? module.getTagName() :
                                            (tags.get() && tagsMode.get().equalsIgnoreCase("[]") ? module.getTagName2() :
                                                    (tags.get() && tagsMode.get().equalsIgnoreCase("()") ? module.getTagName3() :
                                                            (tags.get() && tagsMode.get().equalsIgnoreCase("<>") ? module.getTagName4() :
                                                                    (tags.get() && tagsMode.get().equalsIgnoreCase("-") ? module.getTagName5() :
                                                                            (tags.get() && tagsMode.get().equalsIgnoreCase("Bold") ? module.getTagName6() :
                                                                                    (tags.get() && tagsMode.get().equalsIgnoreCase("White") ? module.getTagName7() :
                                                                                            module.getArrayListName()))))))));

                    if (lowerCaseValue.get())
                        displayString = displayString.toLowerCase();

                    float xPos = -module.getSlide() - 4;
                    float yPos = ((side.getVertical() == Side.Vertical.DOWN) ? -textSpacer : textSpacer) * ((side.getVertical() == Side.Vertical.DOWN) ? index + 1 : index);
                    int moduleColor = Color.getHSBColor(module.getHue(), saturation, brightness).getRGB();
                    int rainbowCol =
                            ColorUtils.rainbow(System.nanoTime() * ((long)oneSpeed.get()) * 1.0f, (float)(counter[0] * oneWidth.get()) * 1.0f, oneOffset.get() * 1.0E8f, saturation, 1.0f).getRGB();

                    Color col = new Color(rainbowCol);
                    counter[0] = counter[0] + 1;

                    int astolfo = ColorUtils.Astolfo(counter[0] * 100, saturationValue.get(), brightnessValue.get());
                    Color exhibition = new Color(Color.HSBtoRGB((float)((double)mc.thePlayer.ticksExisted / 50.0 + Math.sin((double)index / 50.0 * 1.6)) % 1.0f, 0.5f, 1.0f));
                    int width = fontRenderer.getStringWidth(displayString) + 1;

                    if (module.getState()) {
                        if (module.getSlide() < (float)width) module.getTranslate().translate(xPos, yPos);
                    } else if (!module.getState()) module.getTranslate().translate(xPos, -25.0f);

                    boolean backgroundRectRainbow = backgroundColorMode.equalsIgnoreCase("ShaderRainbow");
                    if (backgroundRectRainbow) {
                        RainbowShader.INSTANCE.setStrengthX(rainbowX.get() == 0.0f ? 0.0f : 1.0f / rainbowX.get());
                        RainbowShader.INSTANCE.setStrengthY(rainbowY.get() == 0.0f ? 0.0f : 1.0f / rainbowY.get());
                        RainbowShader.INSTANCE.setOffset((System.currentTimeMillis() % 10000) / 10000.0f);
                        RainbowShader.INSTANCE.startShader();
                    }
                    RenderUtils.drawRect(
                            xPos - (rectMode.equalsIgnoreCase("right") ? 2 + backgroundWidthValue.get() : backgroundWidthValue.get()),
                            module.getTranslate().getY() - (index == 0 ? 1 : 0),
                            rectMode.equalsIgnoreCase("right") ? -1F : 1F,
                            module.getTranslate().getY() + textHeight,
                            backgroundRectRainbow ? 16777216 :
                                    (backgroundColorMode.equalsIgnoreCase("Rainbow") ? ColorUtils.rainbow(rainbowOffsetValue.get() * 10000000L * (long)index, backgroundColorAlphaValue.get()).getRGB() :
                                            (backgroundColorMode.equalsIgnoreCase("Random") ? moduleColor :
                                                    (backgroundColorMode.equalsIgnoreCase("Astolfo") ? astolfo :
                                                            (backgroundColorMode.equalsIgnoreCase("Exhibition") ? exhibition.getRGB() :
                                                                    (backgroundColorMode.equalsIgnoreCase("OneRainbow") ? new Color(col.getRed() / oneRedValue.get() + motionRedValue.get(),
                                                                            col.getGreen() / oneGreenValue.get() + motionGreenValue.get(),
                                                                            col.getBlue() / oneBlueValue.get() + motionBlueValue.get(), colorAlphaValue.get()).getRGB() :
                                                                            backgroundCustomColor))))));

                    if (backgroundRectRainbow)
                        RainbowShader.INSTANCE.stopShader();

                    boolean shaderfont = colorMode.equalsIgnoreCase("ShaderRainbow");
                    if (shaderfont) {
                        RainbowFontShader.INSTANCE.setStrengthX(rainbowX.get() == 0.0f ? 0.0f : 1.0f / rainbowX.get());
                        RainbowFontShader.INSTANCE.setStrengthY(rainbowY.get() == 0.0f ? 0.0f : 1.0f / rainbowY.get());
                        RainbowFontShader.INSTANCE.setOffset((System.currentTimeMillis() % 10000) / 10000.0f);
                        RainbowFontShader.INSTANCE.startShader();
                    }

                    fontRenderer.drawString(
                            displayString, xPos + textX - (rectMode.equalsIgnoreCase("right") ? 1 + backgroundWidthValue.get() : -1 + backgroundWidthValue.get()),
                            module.getTranslate().getY() + (float)textY,
                            shaderfont ? 0 : (colorMode.equalsIgnoreCase("Rainbow") ? ColorUtils.rainbow(rainbowOffsetValue.get() * 10000000L * (long)index, colorAlphaValue.get()).getRGB() :
                            (colorMode.equalsIgnoreCase("Random") ? moduleColor :
                                    (colorMode.equalsIgnoreCase("Astolfo") ? astolfo :
                                            (colorMode.equalsIgnoreCase("Exhibition") ? exhibition.getRGB() :
                                                    (colorMode.equalsIgnoreCase("OneRainbow") ? new Color(col.getRed() / oneRedValue.get() + motionRedValue.get(),
                                                            col.getGreen() / oneGreenValue.get() + motionGreenValue.get(),
                                                            col.getBlue() / oneBlueValue.get() + motionBlueValue.get(), colorAlphaValue.get()).getRGB() :
                                                            customColor)))))
                            , textShadow);

                    if (shaderfont)
                        RainbowFontShader.INSTANCE.stopShader();

                    boolean rectRainbow = rectColorMode.equalsIgnoreCase("ShaderRainbow");
                    if (rectRainbow) {
                        RainbowShader.INSTANCE.setStrengthX(rainbowX.get() == 0.0f ? 0.0f : 1.0f / rainbowX.get());
                        RainbowShader.INSTANCE.setStrengthY(rainbowY.get() == 0.0f ? 0.0f : 1.0f / rainbowY.get());
                        RainbowShader.INSTANCE.setOffset((System.currentTimeMillis() % 10000) / 10000.0f);
                        RainbowShader.INSTANCE.startShader();
                    }

                    int rectColor = rectRainbow ? 0 : (rectColorMode.equalsIgnoreCase("Rainbow") ? ColorUtils.rainbow(rainbowOffsetValue.get() * 10000000L * (long)index, rectColorAlpha.get()).getRGB() :
                            (rectColorMode.equalsIgnoreCase("Random") ? moduleColor :
                                    (rectColorMode.equalsIgnoreCase("Astolfo") ? astolfo :
                                            (rectColorMode.equalsIgnoreCase("Exhibition") ? exhibition.getRGB() :
                                                    (rectColorMode.equalsIgnoreCase("OneRainbow") ? new Color(col.getRed() / oneRedValue.get() + motionRedValue.get(),
                                                            col.getGreen() / oneGreenValue.get() + motionGreenValue.get(),
                                                            col.getBlue() / oneBlueValue.get() + motionBlueValue.get(), colorAlphaValue.get()).getRGB() :
                                                            rectCustomColor)))));
                    if (rectTopValue.get()) {
                        if (module == modules.get(0))
                            RenderUtils.drawRect(xPos - backgroundWidthValue.get(), module.getTranslate().getY() - 1.77F, 1F, module.getTranslate().getY() - 1, rectColor);
                    }

                    switch (rectMode.toLowerCase()) {
                        case "left":
                            RenderUtils.drawRect(xPos - 1 - backgroundWidthValue.get(), module.getTranslate().getY() + ((index == 1) ? 0.25f : 0f) - 0.25F,
                                    xPos - rectWidthValue.get() - backgroundWidthValue.get(), module.getTranslate().getY() + textHeight, rectColor);
                            break;
                        case "shortleft":
                            RenderUtils.drawRect(xPos - 2 - backgroundWidthValue.get(), module.getTranslate().getY() + 2.5F,
                                    xPos - rectWidthValue.get() - 1 - backgroundWidthValue.get(), module.getTranslate().getY() + textHeight - 2.5F, rectColor);
                            break;
                        case "right":
                            RenderUtils.drawRect(-2F, module.getTranslate().getY() - 2F,
                                    rectWidthValue.get() - 1F, module.getTranslate().getY() + textHeight, rectColor);
                            break;
                        case "outline":{

                            RenderUtils.drawRect(xPos - 1 - backgroundWidthValue.get(),
                                    module.getTranslate().getY(),
                                    xPos - rectWidthValue.get()  - backgroundWidthValue.get(),
                                    module.getTranslate().getY() + textHeight + 0.5F,
                                    rectColor);

                            // Junction
                            if (module != modules.get(0)) {
                                String tags = updateTags(modules.get(index - 1));

                                RenderUtils.drawRect(
                                        xPos - 1 - backgroundWidthValue.get() - (fontRenderer.getStringWidth(tags) - fontRenderer.getStringWidth(displayString)),
                                        module.getTranslate().getY(),
                                        xPos - rectWidthValue.get() - 1 - backgroundWidthValue.get(),
                                        module.getTranslate().getY() + 1,
                                        rectColor);
                            }

                            // Bottom
                            if (module == modules.get(modules.size() - 1)) {
                                RenderUtils.drawRect(
                                        xPos - 1 - backgroundWidthValue.get(),
                                        module.getTranslate().getY() + textHeight,
                                        rectWidthValue.get() + 0f,
                                        module.getTranslate().getY() + textHeight + 1,
                                        rectColor
                                );
                            }
                            break;
                        }

                    }
                    if (rectRainbow) RainbowShader.INSTANCE.stopShader();
                    counter[0]++;
                    cou++;
                    index++;

                }
                break;
            }
            case LEFT:{
                int index = 0;
                for (Module module : modules) {
                    String displayString = Replacement.multiReplace(
                            tagsArrayColor.get() ? module.getColorlessTagName() :
                                    (tags.get() && tagsMode.get().equalsIgnoreCase("Space") ? module.getTagName() :
                                            (tags.get() && tagsMode.get().equalsIgnoreCase("[]") ? module.getTagName2() :
                                                    (tags.get() && tagsMode.get().equalsIgnoreCase("()") ? module.getTagName3() :
                                                            (tags.get() && tagsMode.get().equalsIgnoreCase("<>") ? module.getTagName4() :
                                                                    (tags.get() && tagsMode.get().equalsIgnoreCase("-") ? module.getTagName5() :
                                                                            (tags.get() && tagsMode.get().equalsIgnoreCase("Bold") ? module.getTagName6() :
                                                                                    (tags.get() && tagsMode.get().equalsIgnoreCase("White") ? module.getTagName7() :
                                                                                            module.getArrayListName()))))))));

                    if (lowerCaseValue.get())
                        displayString = displayString.toLowerCase();

                    float width = fontRenderer.getStringWidth(displayString);
                    float xPos = -(width - module.getSlide()) + (rectMode.equalsIgnoreCase("left") ? 5 : 2);
                    float yPos = ((side.getVertical() == Side.Vertical.DOWN) ? -textSpacer : textSpacer) *
                    (side.getVertical() == Side.Vertical.DOWN ? index + 1 : index);
                    int moduleColor = Color.getHSBColor(module.getHue(), saturation, brightness).getRGB();
                    int rainbowCol =
                            ColorUtils.rainbow(System.nanoTime() * ((long)oneSpeed.get()) * 1.0f, (float)(counter[0] * oneWidth.get()) * 1.0f, oneOffset.get() * 1.0E8f, saturation, 1.0f).getRGB();

                    Color col = new Color(rainbowCol);
                    counter[0] = counter[0] + 1;

                    int astolfo = ColorUtils.Astolfo(counter[0] * 100, saturationValue.get(), brightnessValue.get());
                    Color exhibition = new Color(Color.HSBtoRGB((float)((double)mc.thePlayer.ticksExisted / 50.0 + Math.sin((double)index / 50.0 * 1.6)) % 1.0f, 0.5f, 1.0f));

                    boolean backgroundRectRainbow = backgroundColorMode.equalsIgnoreCase("ShaderRainbow");
                    if (backgroundRectRainbow) {
                        RainbowShader.INSTANCE.setStrengthX(rainbowX.get() == 0.0f ? 0.0f : 1.0f / rainbowX.get());
                        RainbowShader.INSTANCE.setStrengthY(rainbowY.get() == 0.0f ? 0.0f : 1.0f / rainbowY.get());
                        RainbowShader.INSTANCE.setOffset((System.currentTimeMillis() % 10000) / 10000.0f);
                        RainbowShader.INSTANCE.startShader();
                    }
                    RenderUtils.drawRect(
                            0F,
                            yPos - (index == 0 ? 1F : 0F),
                            xPos + width + (rectMode.equalsIgnoreCase("right") ? 5 : 2)  + backgroundWidthValue.get(),
                            yPos + textHeight,
                            backgroundRectRainbow ? 16777216 :
                                    (backgroundColorMode.equalsIgnoreCase("Rainbow") ? ColorUtils.rainbow(rainbowOffsetValue.get() * 10000000L * (long)index, backgroundColorAlphaValue.get()).getRGB() :
                                            (backgroundColorMode.equalsIgnoreCase("Random") ? moduleColor :
                                                    (backgroundColorMode.equalsIgnoreCase("Astolfo") ? astolfo :
                                                            (backgroundColorMode.equalsIgnoreCase("Exhibition") ? exhibition.getRGB() :
                                                                    (backgroundColorMode.equalsIgnoreCase("OneRainbow") ? new Color(col.getRed() / oneRedValue.get() + motionRedValue.get(),
                                                                            col.getGreen() / oneGreenValue.get() + motionGreenValue.get(),
                                                                            col.getBlue() / oneBlueValue.get() + motionBlueValue.get(), colorAlphaValue.get()).getRGB() :
                                                                            backgroundCustomColor))))));

                    if (backgroundRectRainbow)
                        RainbowShader.INSTANCE.stopShader();

                    boolean shaderfont = colorMode.equalsIgnoreCase("ShaderRainbow");
                    if (shaderfont) {
                        RainbowFontShader.INSTANCE.setStrengthX(rainbowX.get() == 0.0f ? 0.0f : 1.0f / rainbowX.get());
                        RainbowFontShader.INSTANCE.setStrengthY(rainbowY.get() == 0.0f ? 0.0f : 1.0f / rainbowY.get());
                        RainbowFontShader.INSTANCE.setOffset((System.currentTimeMillis() % 10000) / 10000.0f);
                        RainbowFontShader.INSTANCE.startShader();
                    }

                    fontRenderer.drawString(
                            displayString, xPos + textX, yPos + textY,
                            shaderfont ? 0 : (colorMode.equalsIgnoreCase("Rainbow") ? ColorUtils.rainbow(rainbowOffsetValue.get() * 10000000L * (long)index, colorAlphaValue.get()).getRGB() :
                                    (colorMode.equalsIgnoreCase("Random") ? moduleColor :
                                            (colorMode.equalsIgnoreCase("Astolfo") ? astolfo :
                                                    (colorMode.equalsIgnoreCase("Exhibition") ? exhibition.getRGB() :
                                                            (colorMode.equalsIgnoreCase("OneRainbow") ? new Color(col.getRed() / oneRedValue.get() + motionRedValue.get(),
                                                                    col.getGreen() / oneGreenValue.get() + motionGreenValue.get(),
                                                                    col.getBlue() / oneBlueValue.get() + motionBlueValue.get(), colorAlphaValue.get()).getRGB() :
                                                                    customColor)))))
                            , textShadow);

                    if (shaderfont)
                        RainbowFontShader.INSTANCE.stopShader();

                    boolean rectRainbow = rectColorMode.equalsIgnoreCase("ShaderRainbow");
                    if (rectRainbow) {
                        RainbowShader.INSTANCE.setStrengthX(rainbowX.get() == 0.0f ? 0.0f : 1.0f / rainbowX.get());
                        RainbowShader.INSTANCE.setStrengthY(rainbowY.get() == 0.0f ? 0.0f : 1.0f / rainbowY.get());
                        RainbowShader.INSTANCE.setOffset((System.currentTimeMillis() % 10000) / 10000.0f);
                        RainbowShader.INSTANCE.startShader();
                    }

                    int rectColor = rectRainbow ? 0 : (rectColorMode.equalsIgnoreCase("Rainbow") ? ColorUtils.rainbow(rainbowOffsetValue.get() * 10000000L * (long)index, rectColorAlpha.get()).getRGB() :
                            (rectColorMode.equalsIgnoreCase("Random") ? moduleColor :
                                    (rectColorMode.equalsIgnoreCase("Astolfo") ? astolfo :
                                            (rectColorMode.equalsIgnoreCase("Exhibition") ? exhibition.getRGB() :
                                                    (rectColorMode.equalsIgnoreCase("OneRainbow") ? new Color(col.getRed() / oneRedValue.get() + motionRedValue.get(),
                                                            col.getGreen() / oneGreenValue.get() + motionGreenValue.get(),
                                                            col.getBlue() / oneBlueValue.get() + motionBlueValue.get(), colorAlphaValue.get()).getRGB() :
                                                            rectCustomColor)))));
                    switch (rectMode.toLowerCase()) {
                        case "left":
                            RenderUtils.drawRect(0F, yPos - 1, rectWidthValue.get() + 3F, yPos + textHeight, rectColor);
                            break;
                        case "right":
                            RenderUtils.drawRect(xPos + width + 2 + backgroundWidthValue.get(), yPos - 1, xPos + width + 2 + 3 + rectWidthValue.get() + backgroundWidthValue.get(), yPos + textHeight, rectColor);
                            break;
                    }
                    if (rectRainbow) RainbowShader.INSTANCE.stopShader();
                    counter[0]++;
                    cou++;
                    index++;
                }
                break;
            }
        }

        if (mc.currentScreen instanceof GuiHudDesigner) {
            int x2 = Integer.MIN_VALUE;

            if (modules.isEmpty())
                return side.getHorizontal() == Side.Horizontal.LEFT ?
                    new Border(0F, -1F, 20F, 20F)
                :
                    new Border(0F, -1F, -20F, 20F);

            for (Module module : modules) {
                switch (side.getHorizontal()) {
                    case RIGHT:
                    case MIDDLE:{
                        int xPos = -((int)module.getSlide()) - 2;
                        if (x2 != Integer.MIN_VALUE && xPos >= x2) break;
                        x2 = xPos;
                        break;
                    }

                    case LEFT: {
                        int xPos = (int)module.getSlide() + 14;
                        if (x2 != Integer.MIN_VALUE && xPos <= x2) break;
                        x2 = xPos;
                        break;
                    }

                }
            }
            y2 = (side.getVertical() == Side.Vertical.DOWN ? -textSpacer : textSpacer) * (float)modules.size();
            return new Border(-1.0f, -1.0f, (float)x2 - 7.0f, y2);
        }
        GlStateManager.resetColor();
        return null;
    }

    @Override
    public void updateElement() {
        modules = LiquidSense.moduleManager.getModules().stream().filter(it -> it.getArray() && it.getSlide() > 0).sorted(Comparator.comparing(it -> -fontValue.get().getStringWidth(
                Replacement.multiReplace(
                        lowerCaseValue.get() && tagsArrayColor.get() ? it.getColorlessTagName().toLowerCase() :
                                (lowerCaseValue.get() && tags.get() && tagsMode.get().equalsIgnoreCase("Space") ? it.getTagName().toLowerCase() :
                                        (lowerCaseValue.get() && tags.get() && tagsMode.get().equalsIgnoreCase("[]") ? it.getTagName2().toLowerCase() :
                                                (lowerCaseValue.get() && tags.get() && tagsMode.get().equalsIgnoreCase("()") ? it.getTagName3().toLowerCase() :
                                                        (lowerCaseValue.get() && tags.get() && tagsMode.get().equalsIgnoreCase("<>") ? it.getTagName4().toLowerCase() :
                                                                (lowerCaseValue.get() && tags.get() && tagsMode.get().equalsIgnoreCase("-") ? it.getTagName5().toLowerCase() :
                                                                        (lowerCaseValue.get() && tags.get() && tagsMode.get().equalsIgnoreCase("Bold") ? it.getTagName6().toLowerCase() :
                                                                                (lowerCaseValue.get() && tags.get() && tagsMode.get().equalsIgnoreCase("White") ? it.getTagName7().toLowerCase() :
                                                                                        (lowerCaseValue.get() ? it.getArrayListName().toLowerCase() :
                                                                                                (tagsArrayColor.get() ? it.getColorlessTagName() :
                                                                                                        (tags.get() && tagsMode.get().equalsIgnoreCase("Space") ? it.getTagName() :
                                                                                                                (tags.get() && tagsMode.get().equalsIgnoreCase("[]") ? it.getTagName2() :
                                                                                                                        (tags.get() && tagsMode.get().equalsIgnoreCase("()") ? it.getTagName3() :
                                                                                                                                ( tags.get() && tagsMode.get().equalsIgnoreCase("<>") ? it.getTagName4() :
                                                                                                                                        (tags.get() && tagsMode.get().equalsIgnoreCase("-") ? it.getTagName5() :
                                                                                                                                                (tags.get() && tagsMode.get().equalsIgnoreCase("Bold") ? it.getTagName6() :
                                                                                                                                                        (tags.get() && tagsMode.get().equalsIgnoreCase("White") ? it.getTagName7() :
                                                                                                                                                                it.getArrayListName())))))))))))))))))))).collect(Collectors.toList());


        if (alphaSort.get()) modules = LiquidSense.moduleManager.getModules().stream().filter(it -> it.getArray() && it.getSlide() >0).sorted(Comparator.comparing(it -> -fontValue.get().getStringWidth(String.valueOf(0)))).collect(Collectors.toList());
    }

    private String updateTags(Module it) {
        String string;
        if (!tags.get()) {
            string = it.getArrayListName();
        } else if (tagsArrayColor.get()) {
            switch (tagsMode.get()) {
                case "[]": {
                    string = it.getTagName2();
                    break;
                }
                case "()": {
                    string = it.getTagName3();
                    break;
                }
                case "<>": {
                    string = it.getTagName4();
                    break;
                }
                case "-": {
                    string = it.getTagName5();
                    break;
                }
                case "Bold": {
                    string = it.getTagName6();
                    break;
                }
                case "White": {
                    string = it.getTagName7();
                    break;
                }
                default: {
                    string = it.getColorlessTagName();
                    break;
                }
            }
        } else {
            switch (tagsMode.get()) {
                case "[]": {
                    string = it.getTagName2();
                    break;
                }
                case "()": {
                    string = it.getTagName3();
                    break;
                }
                case "<>": {
                    string = it.getTagName4();
                    break;
                }
                case "-": {
                    string = it.getTagName5();
                    break;
                }
                case "Bold": {
                    string = it.getTagName6();
                    break;
                }
                case "White": {
                    string = it.getTagName7();
                    break;
                }
                default: {
                    string = it.getTagName();
                }
            }
        }
        String displayString = Replacement.multiReplace(string);
        if (lowerCaseValue.get()) displayString = displayString.toLowerCase();
        return displayString;
    }


    private String getBreakName(String string) {
        StringBuilder outPut = new StringBuilder();
        if (!string.isEmpty()) outPut.append(string.charAt(0));
        for (int i = 1; i < string.length(); ++i) {
            if (Character.isUpperCase(string.charAt(i)) && i < string.length() - 1 &&
                    (Character.isLowerCase(string.charAt(i + 1)) || Character.isLowerCase(string.charAt(i - 1))))
                outPut.append(" ");
            outPut.append(string.charAt(i));
        }
        return outPut.toString();
    }
}
