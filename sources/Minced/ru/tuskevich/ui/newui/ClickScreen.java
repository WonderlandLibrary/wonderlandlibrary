// 
// Decompiled by Procyon v0.5.36
// 

package ru.tuskevich.ui.newui;

import java.io.IOException;
import org.lwjgl.opengl.GL11;
import net.minecraft.client.gui.ScaledResolution;
import ru.tuskevich.modules.Type;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.client.gui.GuiScreen;

public class ClickScreen extends GuiScreen
{
    public List<UIPanel> panels;
    public static float scroll;
    public float blur;
    public boolean dir;
    
    public ClickScreen() {
        this.panels = new ArrayList<UIPanel>();
        int x = 0;
        for (final Type type : Type.values()) {
            this.panels.add(new UIPanel(type, (float)(20 + x), 10.0f, 100.0f));
            x += 115;
        }
    }
    
    @Override
    public void initGui() {
        super.initGui();
        this.dir = true;
    }
    
    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }
    
    @Override
    public void drawScreen(final int mouseX, final int mouseY, final float partialTicks) {
        final ScaledResolution sr = new ScaledResolution(ClickScreen.mc);
        GL11.glPushMatrix();
        this.panels.forEach(panel -> panel.draw(mouseX, mouseY));
        GL11.glPopMatrix();
    }
    
    @Override
    protected void keyTyped(final char typedChar, final int keyCode) throws IOException {
        this.panels.forEach(panel -> panel.keyTyped(typedChar, keyCode));
        super.keyTyped(typedChar, keyCode);
    }
    
    @Override
    protected void mouseClicked(final int mouseX, final int mouseY, final int mouseButton) throws IOException {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        this.panels.forEach(panel -> panel.mouseClicked(mouseX, mouseY, mouseButton));
    }
    
    @Override
    protected void mouseReleased(final int mouseX, final int mouseY, final int state) {
        super.mouseReleased(mouseX, mouseY, state);
        this.panels.forEach(panel -> panel.mouseReleased(mouseX, mouseY, state));
    }
    
    static {
        ClickScreen.scroll = 1.0f;
    }
}
