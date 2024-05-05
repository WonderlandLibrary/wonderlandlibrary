package vip.radium.gui.alt;

import vip.radium.utils.Wrapper;
import vip.radium.utils.render.RenderingUtils;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import org.lwjgl.input.Keyboard;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class GuiAltScreen extends GuiScreen {

    public static String status;
    protected final GuiScreen parent;

    protected final List<GuiTextField> textFields = new ArrayList<>();

    public GuiAltScreen(GuiScreen parent) {
        this.parent = parent;
    }

    @Override
    public void initGui() {
        if (!textFields.isEmpty())
            Keyboard.enableRepeatEvents(true);
    }

    @Override
    public void onGuiClosed() {
        if (!textFields.isEmpty())
            Keyboard.enableRepeatEvents(false);
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException {
        super.keyTyped(typedChar, keyCode);

        textFields.forEach(textField -> textField.textboxKeyTyped(typedChar, keyCode));
    }

    @Override
    public void updateScreen() {
        textFields.forEach(GuiTextField::updateCursorCounter);
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        super.mouseClicked(mouseX, mouseY, mouseButton);

        textFields.forEach(textField -> textField.mouseClicked(mouseX, mouseY, mouseButton));
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        RenderingUtils.drawGuiBackground(width, height);

        Wrapper.getMinecraftFontRenderer().drawStringWithShadow(status, width / 2.0F -
                (Wrapper.getMinecraftFontRenderer().getWidth(status) / 2.0f), 2, -1);

        textFields.forEach(GuiTextField::drawTextBox);

        super.drawScreen(mouseX, mouseY, partialTicks);
    }
}
