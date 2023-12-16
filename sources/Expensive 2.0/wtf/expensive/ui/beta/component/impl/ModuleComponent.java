package wtf.expensive.ui.beta.component.impl;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.util.text.TextFormatting;
import org.lwjgl.glfw.GLFW;
import wtf.expensive.managment.Managment;
import wtf.expensive.modules.Function;
import wtf.expensive.modules.settings.Setting;
import wtf.expensive.modules.settings.imp.*;
import wtf.expensive.ui.beta.ClickGui;
import wtf.expensive.util.ClientUtil;
import wtf.expensive.util.font.Fonts;
import wtf.expensive.util.math.KeyMappings;
import wtf.expensive.util.render.ColorUtil;
import wtf.expensive.util.render.RenderUtil;
import wtf.expensive.util.render.SmartScissor;
import wtf.expensive.util.render.animation.AnimationMath;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

import static wtf.expensive.util.render.RenderUtil.reAlphaInt;

public class ModuleComponent extends Component {

    public Function function;

    public List<Component> components = new ArrayList<>();

    public ModuleComponent(Function function) {
        this.function = function;
        for (Setting setting : function.getSettingList()) {
            switch (setting.getType()) {
                case BOOLEAN_OPTION -> components.add(new BooleanComponent((BooleanOption) setting));
                case SLIDER_SETTING -> components.add(new SliderComponent((SliderSetting) setting));
                case MODE_SETTING -> components.add(new ModeComponent((ModeSetting) setting));
                case COLOR_SETTING -> components.add(new ColorComponent((ColorSetting) setting));
                case MULTI_BOX_SETTING -> components.add(new ListComponent((MultiBoxSetting) setting));
                case BIND_SETTING -> components.add(new BindComponent((BindSetting) setting));
                case TEXT_SETTING -> components.add(new TextComponent((TextSetting) setting));
            }
        }
    }

    public float animationToggle;
    public static ModuleComponent binding;

    @Override
    public void drawComponent(MatrixStack matrixStack, int mouseX, int mouseY) {

        float totalHeight = 2;
        for (Component component : components) {
            if (component.s != null && component.s.visible()) {
                totalHeight += component.height;
            }
        }

        float off = 2f;

        components.forEach(c -> {
            c.function = function;
            c.parent = parent;
        });

        animationToggle = AnimationMath.lerp(animationToggle, function.state ? 1 : 0, 10);
        RenderUtil.Render2D.drawShadow(x, y, width, height + totalHeight, 10, new Color(17, 18, 21).getRGB());
        RenderUtil.Render2D.drawRoundedCorner(x, y, width, height + totalHeight, 3.5f, new Color(17, 18, 21).getRGB(), RenderUtil.Render2D.Corner.ALL);

        RenderUtil.Render2D.drawRect(x + 5, y + 20, width - 10, 0.5f, new Color(26, 28, 33).getRGB());
        Fonts.gilroy[14].drawString(matrixStack, function.name, x + 7.5f, y + 9f, new Color(57, 66, 72).getRGB());

        String key = ClientUtil.getKey(function.bind);

        if (binding == this && key != null) {
            RenderUtil.Render2D.drawRoundedCorner(x + width - 20 - Fonts.gilroy[14].getWidth(key) + 5, y + 5, 10 + Fonts.gilroy[14].getWidth(key), 10, 2, new Color(17, 18, 21).brighter().getRGB());
            Fonts.gilroy[14].drawCenteredString(matrixStack, key, x + width - 20 - Fonts.gilroy[14].getWidth(key) + 5 + (10 + Fonts.gilroy[14].getWidth(key)) / 2, y + 9, -1);
        }

        int color = ColorUtil.interpolateColor(RenderUtil.IntColor.rgba(26, 29, 33, 255), RenderUtil.IntColor.rgba(74, 166, 218, 255), animationToggle);
        RenderUtil.Render2D.drawShadow(x + 5, y + 23 + off, 10, 10, 8, reAlphaInt(color, 50));
        RenderUtil.Render2D.drawRoundedRect(x + 5, y + 23 + off, 10, 10, 2f, color);
        SmartScissor.push();

        SmartScissor.setFromComponentCoordinates(x + 5, y + 23 + off, 10 * animationToggle, 10);
        Fonts.icons[12].drawString(matrixStack, "A", x + 7, y + 28 + off, -1);
        SmartScissor.unset();
        SmartScissor.pop();

        Fonts.gilroy[14].drawString(matrixStack, "Включен", x + 18f, y + 27f + off, -1);

        float offsetY = 0;
        for (Component component : components) {
            if (component.s != null && component.s.visible()) {
                component.setPosition(x, y + height + offsetY, width, 20);
                component.drawComponent(matrixStack, mouseX, mouseY);
                offsetY += component.height;
            }
        }

    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        if (RenderUtil.isInRegion(mouseX, mouseY, x + 5, y + 20, width - 10, 17) && mouseButton <= 1) {
            function.toggle();
        }

        if (binding == this && mouseButton > 2) {
            function.bind = -100 + mouseButton;
            Managment.NOTIFICATION_MANAGER.add("Модуль " + TextFormatting.GRAY + binding.function.name + TextFormatting.WHITE + " был забинжен на кнопку " + ClientUtil.getKey(-100 + mouseButton), "Module", 5);
            binding = null;
        }

        if (RenderUtil.isInRegion(mouseX, mouseY, x + 5, y, width - 10, 20)) {
            if (mouseButton == 2) {
                ClickGui.typing = false;
                binding = this;
            }
        }
        components.forEach(component -> component.mouseClicked(mouseX, mouseY, mouseButton));
    }

    @Override
    public void mouseReleased(int mouseX, int mouseY, int mouseButton) {
        components.forEach(component -> component.mouseReleased(mouseX, mouseY, mouseButton));
    }

    @Override
    public void keyTyped(int keyCode, int scanCode, int modifiers) {
        components.forEach(component -> component.keyTyped(keyCode, scanCode, modifiers));
    }

    @Override
    public void charTyped(char codePoint, int modifiers) {
        components.forEach(component -> component.charTyped(codePoint, modifiers));
    }
}
