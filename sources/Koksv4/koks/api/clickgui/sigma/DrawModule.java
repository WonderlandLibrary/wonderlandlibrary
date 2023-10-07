package koks.api.clickgui.sigma;

import koks.Koks;
import koks.api.Methods;
import koks.api.clickgui.Element;
import koks.api.clickgui.sigma.draw.DrawCheckBox;
import koks.api.clickgui.sigma.draw.DrawColorPicker;
import koks.api.clickgui.sigma.draw.DrawComboBox;
import koks.api.clickgui.sigma.draw.DrawSlider;
import koks.api.font.DirtyFontRenderer;
import koks.api.font.Fonts;
import koks.api.manager.value.Value;
import koks.api.manager.value.ValueManager;
import koks.api.registry.module.Module;
import koks.api.registry.module.ModuleRegistry;
import koks.api.utils.Animation;
import koks.api.utils.RenderUtil;
import koks.module.gui.ClickGUI;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.util.ArrayList;

/**
 * @author kroko
 * @created on 19.02.2021 : 03:11
 */
public class DrawModule implements Methods {
    int x, y, width, height;
    int clickedX, clickedY;
    Module module;
    DirtyFontRenderer fr;

    public final Animation animation = new Animation();

    public final ArrayList<Element> elements = new ArrayList<>();

    public DrawModule(int x, int y, int width, int height, Module module) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.module = module;

        for (Value<?> setting : ValueManager.getInstance().getValues()) {
            if (setting.getObject() == module) {
                if (setting.getValue() instanceof Boolean)
                    elements.add(new DrawCheckBox(setting));
                if (setting.getValue() instanceof String)
                    elements.add(new DrawComboBox(setting));
                if (setting.getValue() instanceof Integer)
                    if (setting.isColorPicker())
                        elements.add(new DrawColorPicker(setting));
                    else
                        elements.add(new DrawSlider(setting));
                if (setting.getValue() instanceof Double)
                    elements.add(new DrawSlider(setting));
            }
        }
    }

    public void onClick(int mouseX, int mouseY, int mouseButton) {
        if (mouseButton == 0) {
            if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT))
                module.setBypass(!module.isBypass());
            else
                module.toggle();
            clickedX = mouseX;
            clickedY = mouseY;
        }
        if (mouseButton == 1 && ValueManager.getInstance().getValues(module.getClass()).size() != 0) {
            final SigmaClickGUI vegaClickGUI = Koks.getKoks().sigmaClickGUI;
            vegaClickGUI.displaySetting = true;
            vegaClickGUI.settingScroll = 0;
            vegaClickGUI.curModule = module;
        }
    }

    public void updateValues(int x, int y) {
        this.y = y;
        this.x = x;
        this.fr = Fonts.arial18;
    }

    public void draw(int mouseX, int mouseY, float partialTicks) {
        GlStateManager.resetColor();
        final RenderUtil renderUtil = RenderUtil.getInstance();
        GuiScreen.drawRect(x, y, x + width, y + height, new Color(21, 23, 24, 255).getRGB());
        if (module.isToggled()) {
            animation.setGoalX(width * height);
            animation.setSpeed(1.5F);
            float radius = animation.getAnimationX();

            if (clickedX < x || clickedX > x + width)
                clickedX = x + width / 2;
            if (clickedY < y || clickedY > y + height)
                clickedY = y + height / 2;

            final ClickGUI clickGUI = ModuleRegistry.getModule(ClickGUI.class);
            final Color customColor = new Color(ValueManager.getInstance().getValue("Color", clickGUI).castInteger());

            GL11.glEnable(GL11.GL_SCISSOR_TEST);
            renderUtil.scissor(x, y, x + width, y + height);
            renderUtil.drawCircle(clickedX, clickedY, radius, customColor.getRGB());
            GL11.glDisable(GL11.GL_SCISSOR_TEST);
        } else {
            animation.setX(0);
        }
        if (module.isBypass()) {
            for (int i = 0; i < module.getName().length(); i++) {
                final char character = module.getName().charAt(i);
                fr.drawString(character + "", x + 5 + fr.getStringWidth(module.getName().substring(0, i)), y + height / 2F - fr.getStringHeight(module.getName()) / 2F, getRainbow(100 * (i + 1), 3000, 0.6f, 1), true);
            }
        } else
            fr.drawString(module.getName(), x + 5, y + height / 2F - fr.getStringHeight(module.getName()) / 2, module.isToggled() ? Color.white : Color.gray, true);
        GlStateManager.resetColor();
    }
}
