package tech.atani.client.feature.theme.impl.element.modulelist;

import com.google.common.base.Supplier;
import com.mojang.realmsclient.gui.ChatFormatting;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.ScaledResolution;
import tech.atani.client.feature.font.storage.FontStorage;
import tech.atani.client.feature.module.Module;
import tech.atani.client.feature.module.impl.hud.ModuleList;
import tech.atani.client.feature.module.storage.ModuleStorage;
import tech.atani.client.feature.theme.impl.element.ModuleListElement;
import tech.atani.client.feature.value.Value;
import tech.atani.client.feature.value.impl.CheckBoxValue;
import tech.atani.client.feature.value.impl.SliderValue;
import tech.atani.client.feature.value.impl.StringBoxValue;
import tech.atani.client.feature.value.storage.ValueStorage;
import tech.atani.client.feature.theme.data.ThemeObjectInfo;
import tech.atani.client.feature.theme.data.enums.ElementType;
import tech.atani.client.feature.theme.data.enums.ThemeObjectType;
import tech.atani.client.utility.math.atomic.AtomicFloat;
import tech.atani.client.utility.render.RenderUtil;
import tech.atani.client.utility.render.animation.advanced.Direction;
import tech.atani.client.utility.render.animation.advanced.impl.DecelerateAnimation;
import tech.atani.client.utility.render.color.ColorUtil;
import tech.atani.client.utility.render.shader.render.ingame.RenderableShaders;

import java.awt.*;
import java.util.Calendar;
import java.util.LinkedHashMap;

@ThemeObjectInfo(name = "Custom", themeObjectType = ThemeObjectType.ELEMENT, elementType = ElementType.MODULE_LIST)
public class CustomModuleList extends ModuleListElement {

    private StringBoxValue arrayListPosition = new StringBoxValue("Module List Position", "Where will the module list be?", this, new String[]{"Left", "Right"});
    private StringBoxValue customColorMode = new StringBoxValue("Custom Color Mode", "How will the modules be colored in custom mode?", this, new String[]{"Static", "Random", "Fade", "Gradient", "Rainbow", "Astolfo Sky"});
    private SliderValue<Integer> red = new SliderValue<>("Red", "What'll be the red of the color?", this, 255, 0, 255, 0, new Supplier[]{() -> (customColorMode.getValue().equalsIgnoreCase("Static") || customColorMode.getValue().equalsIgnoreCase("Random") || customColorMode.getValue().equalsIgnoreCase("Fade") || customColorMode.getValue().equalsIgnoreCase("Gradient"))});
    private SliderValue<Integer> green = new SliderValue<>("Green", "What'll be the green of the color?", this, 255, 0, 255, 0, new Supplier[]{() -> (customColorMode.getValue().equalsIgnoreCase("Static") || customColorMode.getValue().equalsIgnoreCase("Random") || customColorMode.getValue().equalsIgnoreCase("Fade") || customColorMode.getValue().equalsIgnoreCase("Gradient"))});
    private SliderValue<Integer> blue = new SliderValue<>("Blue", "What'll be the blue of the color?", this, 255, 0, 255, 0, new Supplier[]{() -> (customColorMode.getValue().equalsIgnoreCase("Static") || customColorMode.getValue().equalsIgnoreCase("Random") || customColorMode.getValue().equalsIgnoreCase("Fade") || customColorMode.getValue().equalsIgnoreCase("Gradient"))});
    private SliderValue<Integer> red2 = new SliderValue<>("Second Red", "What'll be the red of the second color?", this, 255, 0, 255, 0, new Supplier[]{() -> customColorMode.getValue().equalsIgnoreCase("Gradient")});
    private SliderValue<Integer> green2 = new SliderValue<>("Second Green", "What'll be the green of the second color?", this, 255, 0, 255, 0, new Supplier[]{() -> customColorMode.getValue().equalsIgnoreCase("Gradient")});
    private SliderValue<Integer> blue2 = new SliderValue<>("Second Blue", "What'll be the blue of the second color?", this, 255, 0, 255, 0, new Supplier[]{() -> customColorMode.getValue().equalsIgnoreCase("Gradient")});
    private SliderValue<Float> darkFactor = new SliderValue<>("Dark Factor", "How much will the color be darkened?", this, 0.49F, 0F, 1F, 2, new Supplier[]{() -> customColorMode.getValue().equalsIgnoreCase("Fade")});
    private SliderValue<Float> rectWidth = new SliderValue<>("Rect Width", "How long will be the rect (in addition to string width)?", this, 2f, 0f, 10f, 1);
    private SliderValue<Float> rectHeight = new SliderValue<>("Rect Height", "How big will be the rect (in addition to string height)?", this, 1f, 0f, 10f, 1);
    private CheckBoxValue renderShaders = new CheckBoxValue("Render Shaders", "Render shaders on the module list?", this, true);
    private CheckBoxValue renderBlur = new CheckBoxValue("Render Blur", "Render blur on the module list?", this, true, new Supplier[]{() -> renderShaders.getValue()});
    private CheckBoxValue renderBloom = new CheckBoxValue("Render Bloom", "Render bloom on the module list?", this, true, new Supplier[]{() -> renderShaders.getValue()});
    private CheckBoxValue suffix = new CheckBoxValue("Suffix", "Display module's mode?", this, true);
    private StringBoxValue suffixMode = new StringBoxValue("Suffix Mode", "How will modes be displayed?", this, new String[] {"nm sfx", "nm - sfx", "nm # sfx", "nm (sfx)", "nm [sfx]", "nm {sfx}", "nm - (sfx)", "nm - [sfx]", "nm - {sfx}", "nm # (sfx)", "nm # [sfx]", "nm # {sfx}"}, new Supplier[]{() -> suffix.getValue()});
    private StringBoxValue suffixColor = new StringBoxValue("Suffix Color", "How will modes be colored?", this, new String[] {"Gray", "Dark Gray", "White", "None"});
    private StringBoxValue fontMode = new StringBoxValue("Font", "Which font will render the module name?", this, new String[]{"Minecraft", "Roboto", "Roboto Medium", "Product Sans", "Arial", "SF Regular", "SF Semibold", "Volte Semibold"});
    private SliderValue<Integer> fontSize = new SliderValue<>("Font Size", "How large will the font be?", this, 19, 17, 21, 0, new Supplier[]{() -> !fontMode.getValue().equalsIgnoreCase("Minecraft")});
    private SliderValue<Integer> brightness = new SliderValue<>("Background Brightness", "What will be the brightness of the background?", this, 0, 0, 255, 0);
    private SliderValue<Integer> opacity = new SliderValue<>("Background Opacity", "What will be the opacity of the background?", this, 180, 0, 255, 0);
    private SliderValue<Float> xOffset = new SliderValue<>("X Offset", "How much will the module list offset on X?", this, 0F, 0F, 20F, 1);
    private SliderValue<Float> yOffset = new SliderValue<>("Y Offset", "How much will the module list offset on Y?", this, 0F, 0F, 20F, 1);
    private CheckBoxValue fontShadow = new CheckBoxValue("Font Shadow", "Should the module font use shadows on the text?", this, true);

    @Override
    public void onEnable() {
        ModuleList moduleList = ModuleStorage.getInstance().getByClass(ModuleList.class);
        for(Value value : ValueStorage.getInstance().getValues(this, false))
            ValueStorage.getInstance().addLinkedValues(moduleList, value);
    }

    @Override
    public void onDisable() {
        ModuleList moduleList = ModuleStorage.getInstance().getByClass(ModuleList.class);
        for(Value value : ValueStorage.getInstance().getValues(this, false))
            ValueStorage.getInstance().removeLinkedValues(moduleList, value);
    }

    private LinkedHashMap<Module, Color> moduleColorHashMap = new LinkedHashMap<>();
    final Calendar calendar = Calendar.getInstance();

    @Override
    public void onDraw(ScaledResolution scaledResolution, float partialTicks, AtomicFloat leftY, AtomicFloat rightY, LinkedHashMap<Module, DecelerateAnimation> moduleHashMap) {
        switch (this.arrayListPosition.getValue()) {
            case "Left":
                leftY.set(leftY.get() + yOffset.getValue());
                break;
            case "Right":
                rightY.set(rightY.get() + yOffset.getValue());
                break;
        }
        RenderableShaders.renderAndRun(renderShaders.getValue() && renderBloom.getValue(), renderShaders.getValue() && renderBlur.getValue(), () -> {
            FontRenderer fontRenderer;
            switch (fontMode.getValue()) {
                case "Roboto":
                    fontRenderer = FontStorage.getInstance().findFont("Roboto", this.fontSize.getValue());
                    break;
                case "Roboto Medium":
                    fontRenderer = FontStorage.getInstance().findFont("Roboto Medium", this.fontSize.getValue());
                    break;
                case "Product Sans":
                    fontRenderer = FontStorage.getInstance().findFont("Product Sans", this.fontSize.getValue());
                    break;
                case "Arial":
                    fontRenderer = FontStorage.getInstance().findFont("Arial", this.fontSize.getValue());
                    break;
                case "SF Regular":
                    fontRenderer = FontStorage.getInstance().findFont("SF Regular", this.fontSize.getValue());
                    break;
                case "SF Semibold":
                    fontRenderer = FontStorage.getInstance().findFont("SF Semibold", this.fontSize.getValue());
                    break;
                case "Volte Semibold":
                    fontRenderer = FontStorage.getInstance().findFont("Volte Semibold", this.fontSize.getValue());
                    break;
                default:
                    fontRenderer = mc.fontRendererObj;
                    break;
            }
            float moduleY = arrayListPosition.is("Left") ? leftY.get() : rightY.get();
            int counter = 0;
            for (Module module : moduleHashMap.keySet()) {
                float moduleHeight = fontRenderer.FONT_HEIGHT + rectHeight.getValue();
                if (!moduleHashMap.get(module).finished(Direction.BACKWARDS)) {
                    int color = 0;
                    switch (this.customColorMode.getValue()) {
                        case "Static":
                            color = new Color(red.getValue(), green.getValue(), blue.getValue()).getRGB();
                            break;
                        case "Random":
                            if(!moduleColorHashMap.containsKey(module)) {
                                int baseHue = 15;
                                int minValue = 150;
                                int maxValue = 255;
                                int alpha = 255;
                                moduleColorHashMap.put(module, ColorUtil.generateRandomTonedColor(baseHue, minValue, maxValue, alpha));
                            }
                            color = moduleColorHashMap.get(module).getRGB();
                            break;
                        case "Fade": {
                            int firstColor = new Color(red.getValue(), green.getValue(), blue.getValue()).getRGB();
                            color = ColorUtil.fadeBetween(firstColor, ColorUtil.darken(firstColor, darkFactor.getValue()), counter * 150L);
                            break;
                        }
                        case "Gradient": {
                            int firstColor = new Color(red.getValue(), green.getValue(), blue.getValue()).getRGB();
                            int secondColor = new Color(red2.getValue(), green2.getValue(), blue2.getValue()).getRGB();
                            color = ColorUtil.fadeBetween(firstColor, secondColor, counter * 150L);
                            break;
                        }
                        case "Rainbow":
                            color = ColorUtil.getRainbow(3000, (int) (counter * 150L));
                            break;
                        case "Astolfo Sky":
                            color = ColorUtil.blendRainbowColours(counter * 150L);
                            break;
                    }
                    String name = getModuleName(module, true);
                    float rectWidth = (fontRenderer.getStringWidthInt(name) + this.rectWidth.getValue());
                    float moduleX = this.arrayListPosition.getValue().equalsIgnoreCase("Left") ? (0 - rectWidth + (float) (moduleHashMap.get(module).getOutput() * rectWidth) + xOffset.getValue()) : scaledResolution.getScaledWidth() - ((float) (moduleHashMap.get(module).getOutput() * rectWidth) + xOffset.getValue());
                    RenderUtil.drawRect(moduleX, moduleY, rectWidth, moduleHeight, new Color(brightness.getValue(), brightness.getValue(), brightness.getValue(), opacity.getValue()).getRGB());

                    if (!fontShadow.getValue()) {
                        fontRenderer.drawTotalCenteredString(name, moduleX + rectWidth / 2, moduleY + moduleHeight / 2 + 0.5f, color);
                    } else {
                        fontRenderer.drawTotalCenteredStringWithShadow(name, moduleX + rectWidth / 2, moduleY + moduleHeight / 2 + 0.5f, color);
                    }

                    moduleY += moduleHeight;
                    counter++;
                }
            }
        });
    }

    @Override
    public boolean shouldAnimate() {
        return true;
    }

    @Override
    public FontRenderer getFontRenderer() {
        FontRenderer fontRenderer = null;
        switch (fontMode.getValue()) {
            case "Roboto":
                fontRenderer = FontStorage.getInstance().findFont("Roboto", this.fontSize.getValue());
                break;
            case "Roboto Medium":
                fontRenderer = FontStorage.getInstance().findFont("Roboto Medium", this.fontSize.getValue());
                break;
            case "Product Sans":
                fontRenderer = FontStorage.getInstance().findFont("Product Sans", this.fontSize.getValue());
                break;
            case "Arial":
                fontRenderer = FontStorage.getInstance().findFont("Arial", this.fontSize.getValue());
                break;
            case "SF Regular":
                fontRenderer = FontStorage.getInstance().findFont("SF Regular", this.fontSize.getValue());
                break;
            case "SF Semibold":
                fontRenderer = FontStorage.getInstance().findFont("SF Semibold", this.fontSize.getValue());
                break;
            case "Volte Semibold":
                fontRenderer = FontStorage.getInstance().findFont("Volte Semibold", this.fontSize.getValue());
                break;
            default:
                fontRenderer = mc.fontRendererObj;
                break;
        }
        return fontRenderer;
    }

    public String getModuleName(Module module, boolean colors) {
        String name = module.getName();
        if(module.getSuffix() != null && suffix.getValue()) {
            ChatFormatting chatFormatting = null;
            if(colors) {
                switch(this.suffixColor.getValue()) {
                    case "White":
                        chatFormatting = ChatFormatting.WHITE;
                        break;
                    case "Gray":
                        chatFormatting = ChatFormatting.GRAY;
                        break;
                    case "Dark Gray":
                        chatFormatting = ChatFormatting.DARK_GRAY;
                        break;
                    case "None":
                        chatFormatting = ChatFormatting.RESET;
                        break;
                }
            }
            name = suffixMode.getValue().replace("nm", module.getName() + (chatFormatting == null ? "" : chatFormatting.toString())).replace("sfx",  module.getSuffix());
        }
        return name;
    }

}
