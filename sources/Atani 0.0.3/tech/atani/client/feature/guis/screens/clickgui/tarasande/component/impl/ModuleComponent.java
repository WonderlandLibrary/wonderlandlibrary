package tech.atani.client.feature.guis.screens.clickgui.tarasande.component.impl;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiScreen;
import tech.atani.client.feature.guis.screens.clickgui.tarasande.component.Component;
import tech.atani.client.feature.guis.screens.clickgui.tarasande.window.Window;
import tech.atani.client.feature.module.Module;
import tech.atani.client.feature.value.storage.ValueStorage;
import tech.atani.client.utility.render.RenderUtil;
import tech.atani.client.utility.render.color.ColorUtil;
import tech.atani.client.utility.render.shader.shaders.RoundedShader;

import java.awt.*;

public class ModuleComponent extends Component {

    private final Module module;
    private GuiScreen parent;

    public ModuleComponent(Module module, float posX, float posY, float width, float height, GuiScreen parent) {
        super(posX, posY, width, height);
        this.module = module;
        this.parent = parent;
    }

    @Override
    public boolean shouldHide() {
        return module.shouldHide();
    }

    @Override
    public void drawScreen(int mouseX, int mouseY) {
        FontRenderer normal = Minecraft.getMinecraft().fontRendererObj;
        Color tarasande = new Color(TARASANDE);
        RenderUtil.drawRect(getPosX() + getAddX(), getPosY(), getBaseWidth(), getFinalHeight(), ColorUtil.setAlpha(tarasande, 180).getRGB());
        RenderUtil.drawRect(getPosX() + getAddX() + 1, getPosY() + 1, getBaseWidth() - 2, getFinalHeight() - 2, new Color(0, 0, 0, 100).getRGB());
        RenderUtil.scaleStart(getPosX() + 3, getPosY() + 3, 1.1f);
        normal.drawStringWithShadow(module.getName(), getPosX() + 3f, getPosY() + 3f, -1);
        RenderUtil.scaleEnd();
        RenderUtil.scaleStart(getPosX() + 3, getPosY() + getBaseHeight() - 1.1f - normal.FONT_HEIGHT + 2, 0.6f);
        normal.drawStringWithShadow(module.getDescription(), getPosX() + 3, getPosY() + getBaseHeight() - 1.1f - normal.FONT_HEIGHT + 2, new Color(180, 180, 180).getRGB());
        RenderUtil.scaleEnd();
        RoundedShader.drawRoundOutline(getPosX() + getBaseWidth() - 13 - 6, getPosY() + getBaseHeight() - 10 - 1 - 6,12, 12, 5.7f, 0.5f, module.isEnabled() ? tarasande  : ColorUtil.setAlpha(tarasande, 60), new Color(-1));

    }

    @Override
    public void mouseClick(int mouseX, int mouseY, int mouseButton) {
        if(RenderUtil.isHovered(mouseX, mouseY, this.getPosX(), this.getPosY(), this.getBaseWidth(), this.getBaseHeight())) {
            switch (mouseButton) {
                case 0:
                    module.toggle();
                    break;
                case 1:
                    if(!ValueStorage.getInstance().getValues(module).isEmpty())
                        Minecraft.getMinecraft().displayGuiScreen(new Window(getModule(), parent));
                    break;
            }
        }
    }

    @Override
    public float getFinalHeight() {
        return this.getBaseHeight();
    }

    public Module getModule() {
        return module;
    }
}
