package me.aquavit.liquidsense.ui.client.gui.elements;

import net.minecraft.client.gui.FontRenderer;

public class GuiPasswordField extends GuiUsernameField {
    public GuiPasswordField(int componentId, FontRenderer fontrendererObj, int x, int y, int par5Width, int par6Height) {
        super(componentId, fontrendererObj, x, y, par5Width, par6Height);
    }

    @Override
    public void drawTextBox() {
        String realText = this.getText();
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < ((CharSequence) getText()).length(); ++i) {
            stringBuilder.append('*');
        }

        this.setText(stringBuilder.toString());
        super.drawTextBox();
        this.setText(realText);
    }
}
