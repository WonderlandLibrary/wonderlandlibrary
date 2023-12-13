// 
// Decompiled by Procyon v0.5.30
// 

package exhibition.gui.click;

import org.lwjgl.input.Keyboard;
import java.io.IOException;
import net.minecraft.client.renderer.GlStateManager;
import exhibition.gui.click.ui.Zeus;
import exhibition.gui.click.ui.UI;
import java.util.ArrayList;
import exhibition.gui.click.ui.Menu;
import exhibition.gui.click.components.MainPanel;
import net.minecraft.client.gui.GuiScreen;

public class ClickGui extends GuiScreen
{
    MainPanel mainPanel;
    public static Menu menu;
    ArrayList<UI> themes;
    
    public ArrayList<UI> getThemes() {
        return this.themes;
    }
    
    public ClickGui() {
        (this.themes = new ArrayList<UI>()).add(new Zeus());
        this.mainPanel = new MainPanel("Exhibition", 50.0f, 50.0f, this.themes.get(0));
    }
    
    @Override
    public void drawScreen(final int mouseX, final int mouseY, final float partialTicks) {
        GlStateManager.pushMatrix();
        this.mainPanel.draw(mouseX, mouseY);
        GlStateManager.popMatrix();
    }
    
    public void mouseReleased(final int mouseX, final int mouseY, final int mouseButton) {
        this.mainPanel.mouseMovedOrUp(mouseX, mouseY, mouseButton);
        super.mouseReleased(mouseX, mouseY, mouseButton);
    }
    
    public void mouseClicked(final int mouseX, final int mouseY, final int clickedButton) {
        try {
            this.mainPanel.mouseClicked(mouseX, mouseY, clickedButton);
            super.mouseClicked(mouseX, mouseY, clickedButton);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    @Override
    public void handleKeyboardInput() throws IOException {
        super.handleKeyboardInput();
        if (Keyboard.getEventKeyState()) {
            this.mainPanel.keyPressed(Keyboard.getEventKey());
        }
    }
    
    @Override
    public void onGuiClosed() {
    }
}
