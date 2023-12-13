package koks.gui.clickgui.normal;

import koks.Koks;
import koks.manager.cl.Role;
import koks.manager.module.Module;
import koks.manager.module.impl.gui.ClickGui;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;

import java.io.IOException;
import java.util.ArrayList;

/**
 * @author deleteboys | lmao | kroko
 * @created on 12.09.2020 : 23:59
 */
public class ClickGUI extends GuiScreen {

    private final Minecraft mc = Minecraft.getMinecraft();
    private final FontRenderer fr = Minecraft.getMinecraft().fontRendererObj;
    private ArrayList<DrawCategory> drawCategories = new ArrayList<>();
    private int x, y, width, height;

    public ClickGUI() {
        x = 50;
        y = 50;
        width = 90;
        height = 15;
        for (Module.Category category : Module.Category.values()) {
            drawCategories.add(new DrawCategory(category, x, y, width, height));
            x += 100;
        }
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        ScaledResolution scaledResolution = new ScaledResolution(mc);
        ClickGui clickGui = (ClickGui) Koks.getKoks().moduleManager.getModule(ClickGui.class);
        if (clickGui.fakeCredits.isToggled())
            drawString(fontRendererObj, "§aby " + clickGui.fakeAuthor.getTyped(), scaledResolution.getScaledWidth() - fontRendererObj.getStringWidth("by " + clickGui.fakeAuthor.getTyped()), scaledResolution.getScaledHeight() - fontRendererObj.FONT_HEIGHT, -1);
        for (DrawCategory drawCategory : drawCategories) {
            if (!drawCategory.category.equals(Module.Category.DEBUG) || Koks.getKoks().CLManager.getUser().getRole().equals(Role.Developer))
                drawCategory.drawScreen(mouseX, mouseY, partialTicks);
        }
        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException {
        for (DrawCategory drawCategory : drawCategories) {
            if (!drawCategory.category.equals(Module.Category.DEBUG) || Koks.getKoks().CLManager.getUser().getRole().equals(Role.Developer))
                drawCategory.keyTyped(typedChar, keyCode);
        }
        super.keyTyped(typedChar, keyCode);
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        for (DrawCategory drawCategory : drawCategories) {
            if (!drawCategory.category.equals(Module.Category.DEBUG) || Koks.getKoks().CLManager.getUser().getRole().equals(Role.Developer))
                drawCategory.mouseClicked(mouseX, mouseY, mouseButton);
        }
        super.mouseClicked(mouseX, mouseY, mouseButton);
    }

    @Override
    protected void mouseReleased(int mouseX, int mouseY, int state) {
        for (DrawCategory drawCategory : drawCategories) {
            if (!drawCategory.category.equals(Module.Category.DEBUG) || Koks.getKoks().CLManager.getUser().getRole().equals(Role.Developer))
                drawCategory.mouseReleased(mouseX, mouseY, state);
        }
        super.mouseReleased(mouseX, mouseY, state);
    }

    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }

}