package de.tired.base.guis.newclickgui.renderables.setting;

import de.tired.base.guis.newclickgui.abstracts.ClickGUIHandler;
import de.tired.base.guis.newclickgui.setting.impl.TextBoxSetting;
import de.tired.util.misc.RenderableTextfield;

import java.io.IOException;


public class RenderableTextbox extends ClickGUIHandler {

    public TextBoxSetting textBoxSetting;

    private RenderableTextfield renderableTextfield;

    public RenderableTextbox(final TextBoxSetting textBoxSetting) {
        this.textBoxSetting = textBoxSetting;
        renderableTextfield = new RenderableTextfield(textBoxSetting.getName(), 0, 0, width, height);
    }

    @Override
    public void onMouseReleased(int mouseX, int mouseY) {

    }

    @Override
    public void onMouseClicked(int mouseX, int mouseY, int mouseKey) {
        renderableTextfield.mouseClicked();
    }

    public void onKeyTyped(char typedChar, int keyCode) {

        try {
            renderableTextfield.keyTyped(typedChar, keyCode);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, int x, int y) {
        renderableTextfield.y = y + 3;
        renderableTextfield.width = 80;
        renderableTextfield.x = (int) (x + renderableTextfield.width / 2f) / 2;
        renderableTextfield.height = 14;
        renderableTextfield.drawTextBox(mouseX, mouseY);
    }
}
