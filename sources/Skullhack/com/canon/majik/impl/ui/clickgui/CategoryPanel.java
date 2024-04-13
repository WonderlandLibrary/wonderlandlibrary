package com.canon.majik.impl.ui.clickgui;

import com.canon.majik.api.core.Initializer;
import com.canon.majik.api.utils.render.RenderUtils;
import com.canon.majik.api.utils.render.animate.Animation;
import com.canon.majik.api.utils.render.animate.Easing;
import com.canon.majik.impl.modules.api.Category;
import com.canon.majik.impl.modules.api.Module;
import com.canon.majik.impl.modules.impl.client.ClickGui;
import net.minecraft.client.Minecraft;

import java.util.ArrayList;
import java.util.List;

public class CategoryPanel {
    private final Category category;
    private final int x;
    private final int y;
    private final int width;
    private final int height;
    private final Minecraft mc;
    private final List<ModuleButton> moduleButtons = new ArrayList<>();
    Animation animation = new Animation(1650, false, Easing.LINEAR);
    private boolean open = true;

    public CategoryPanel(Category category, int x, int y, int width, int height, Minecraft mc) {
        this.category = category;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.mc = mc;

        for (Module module : Initializer.moduleManager.getModules()) {
            if (module.getCategory() == this.category) {
                moduleButtons.add(new ModuleButton(module, x, y, width, height, mc));
            }
        }
    }

    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        animation.setState(open);
        int offset = (int) (height);
        RenderUtils.invokeScissor(x, y, (x+width), (float) (500 * animation.getAnimationFactor()));
        for (ModuleButton moduleButton : this.moduleButtons) {
            offset += moduleButton.drawScreen(mouseX, mouseY, partialTicks, offset);
        }
        RenderUtils.drawVLine(x, y + height - 3, (int)(offset) - height + 3, ClickGui.instance.color.getValue().getRGB());
        RenderUtils.drawVLine(x + width - 1, y + height - 3, (int)(offset) - height + 3, ClickGui.instance.color.getValue().getRGB());
        RenderUtils.drawHLine(x, y + (int)(offset-1), width, ClickGui.instance.color.getValue().getRGB());
        RenderUtils.releaseScissor();
        RenderUtils.rect(x, y, width, height, ClickGui.instance.color.getValue().getRGB());
        if(ClickGui.instance.cfont.getValue()){
            Initializer.CFont.drawStringWithShadow(category.name(),
                    x + width / 2f - Initializer.CFont.getStringWidth(category.name()) / 2f,
                    y + height / 2f - Initializer.CFont.getHeight() / 2f,
                    -1);
        }else {
            mc.fontRenderer.drawStringWithShadow(category.name(),
                    x + width / 2f - mc.fontRenderer.getStringWidth(category.name()) / 2f,
                    y + height / 2f - mc.fontRenderer.FONT_HEIGHT / 2f,
                    -1);
        }
    }

    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        if (this.open) moduleButtons.forEach(moduleButton -> moduleButton.mouseClicked(mouseX, mouseY, mouseButton));

        if (bounding(mouseX, mouseY) && mouseButton == 1) {
            this.open = !this.open;
        }
    }

    public void keyTyped(char typedChar, int keyCode) {
        if (this.open) moduleButtons.forEach(moduleButton -> moduleButton.keyTyped(typedChar, keyCode));
    }


    public void mouseReleased(int mouseX, int mouseY, int state) {
        if (this.open)
            moduleButtons.forEach(moduleButton -> moduleButton.mouseReleased(mouseX, mouseY, state));
    }

    public boolean bounding(int mouseX, int mouseY) {
        if (mouseX < this.x) return false;
        if (mouseX > this.x + this.width) return false;
        if (mouseY < this.y) return false;
        return mouseY <= this.y + this.height;
    }
}
