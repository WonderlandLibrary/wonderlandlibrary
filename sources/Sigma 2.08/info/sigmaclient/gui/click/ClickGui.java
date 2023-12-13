package info.sigmaclient.gui.click;

import info.sigmaclient.Client;
import info.sigmaclient.gui.click.components.MainPanel;
import info.sigmaclient.gui.click.ui.Sigma;
import info.sigmaclient.gui.click.ui.Menu;
import info.sigmaclient.gui.click.ui.UI;
import info.sigmaclient.management.animate.Opacity;
import info.sigmaclient.util.RenderingUtil;
import info.sigmaclient.util.render.Colors;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.optifine.IFileDownloadListener;
import org.lwjgl.input.Keyboard;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class ClickGui extends GuiScreen {

    private MainPanel mainPanel;
    public static Menu menu;

    public List<UI> getThemes() {
        return themes;
    }

    private List<UI> themes;

    public ClickGui() {
        (themes = new CopyOnWriteArrayList<>()).add(new Sigma());
        mainPanel = new MainPanel("Sigma", 50, 50, themes.get(0));
        themes.get(0).mainConstructor(this, mainPanel);
    }

    private Opacity opacity = new Opacity(0);

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        super.drawScreen(mouseX, mouseY, partialTicks);
        ScaledResolution res = new ScaledResolution(mc, mc.displayWidth, mc.displayHeight);
        opacity.interpolate(100);
        RenderingUtil.rectangle(0, 0, res.getScaledWidth(), res.getScaledHeight(), Colors.getColor(0, (int) opacity.getOpacity()));
        mainPanel.draw(mouseX, mouseY);
    }

    @Override
    public void mouseReleased(int mouseX, int mouseY, int mouseButton) {
        mainPanel.mouseMovedOrUp(mouseX, mouseY, mouseButton);
        super.mouseReleased(mouseX, mouseY, mouseButton);
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int clickedButton) {
        try {
            mainPanel.mouseClicked(mouseX, mouseY, clickedButton);
            super.mouseClicked(mouseX, mouseY, clickedButton);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void handleKeyboardInput() throws IOException {
        super.handleKeyboardInput();
        if (Keyboard.getEventKeyState()) {
            mainPanel.keyPressed(Keyboard.getEventKey());
        }
    }

    @Override
    public void onGuiClosed() {
        opacity.setOpacity(0);
        themes.get(0).onClose();
    }

}
