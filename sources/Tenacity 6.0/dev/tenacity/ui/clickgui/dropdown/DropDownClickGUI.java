package dev.tenacity.ui.clickgui.dropdown;

import dev.tenacity.Tenacity;
import dev.tenacity.module.Module;
import dev.tenacity.module.ModuleCategory;
import dev.tenacity.module.ModuleRepository;
import dev.tenacity.module.impl.render.ClickGUIModule;
import dev.tenacity.setting.impl.BooleanSetting;
import dev.tenacity.ui.clickgui.dropdown.component.CategoryPanelComponent;
import dev.tenacity.util.render.RenderUtil;
import dev.tenacity.util.render.animation.AbstractAnimation;
import dev.tenacity.util.render.animation.AnimationDirection;
import dev.tenacity.util.render.animation.impl.EaseBackInAnimation;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.entity.Render;
import org.lwjgl.input.Keyboard;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public final class DropDownClickGUI extends GuiScreen {

    private CategoryPanelComponent[] categoryPanels;

    private final AbstractAnimation scaleAnimation = new EaseBackInAnimation(200, 1, 1, AnimationDirection.FORWARDS);

    @Override
    public void initGui() {
        scaleAnimation.setDirection(AnimationDirection.FORWARDS);
        if(categoryPanels == null) {
            int length = ModuleCategory.values().length;
            categoryPanels = new CategoryPanelComponent[length];
            for(int i = 0; i < length; i++) {
                categoryPanels[i] = new CategoryPanelComponent(ModuleCategory.values()[i]);
            }
        }

        for (CategoryPanelComponent categoryPanel : categoryPanels)
            categoryPanel.initGUI();
    }

    @Override
    public void drawScreen(final int mouseX, final int mouseY, final float partialTicks) {
        final ScaledResolution sr = new ScaledResolution(mc);

        RenderUtil.scaleStart(sr.getScaledWidth() / 2f, sr.getScaledHeight() / 2f, scaleAnimation.getOutput().floatValue());

        if (ClickGUIModule.cat.isEnabled())
            RenderUtil.drawImage(Tenacity.cat, sr.getScaledWidth() - 500, sr.getScaledHeight() - 490, 512, 512);
        for (CategoryPanelComponent categoryPanel : categoryPanels)
            categoryPanel.drawScreen(mouseX, mouseY);

        RenderUtil.scaleEnd();

        // Once the closing animation has been finished, hide the gui.
        if(scaleAnimation.isFinished(AnimationDirection.BACKWARDS))
            mc.displayGuiScreen(null);
    }

    @Override
    protected void keyTyped(final char typedChar, final int keyCode) throws IOException {
        for (CategoryPanelComponent categoryPanel : categoryPanels)
            categoryPanel.keyTyped(typedChar, keyCode);

        // Starting the closing process / animation
        if(keyCode == Keyboard.KEY_ESCAPE)
            scaleAnimation.setDirection(AnimationDirection.BACKWARDS);
    }

    @Override
    protected void mouseClicked(final int mouseX, final int mouseY, final int mouseButton) throws IOException {
        for (CategoryPanelComponent categoryPanel : categoryPanels)
            categoryPanel.mouseClicked(mouseX, mouseY, mouseButton);
    }

    @Override
    protected void mouseReleased(final int mouseX, final int mouseY, final int state) {
        for (CategoryPanelComponent categoryPanel : categoryPanels)
            categoryPanel.mouseReleased(mouseX, mouseY, state);
    }

    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }
}
