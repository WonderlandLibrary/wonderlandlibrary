// 
// Decompiled by Procyon v0.5.36
// 

package dev.xulu.clickgui;

import org.lwjgl.input.Mouse;
import java.io.IOException;
import dev.xulu.clickgui.item.Item;
import java.util.Iterator;
import dev.xulu.clickgui.item.Button;
import dev.xulu.clickgui.item.ModuleButton;
import com.elementars.eclient.module.Module;
import com.elementars.eclient.Xulu;
import com.elementars.eclient.module.Category;
import java.util.ArrayList;
import net.minecraft.client.gui.GuiScreen;

public final class ClickGui extends GuiScreen
{
    private static ClickGui clickGui;
    private final ArrayList<Panel> panels;
    
    public ClickGui() {
        this.panels = new ArrayList<Panel>();
        if (this.getPanels().isEmpty()) {
            this.load();
        }
    }
    
    public static ClickGui getClickGui() {
        return (ClickGui.clickGui == null) ? (ClickGui.clickGui = new ClickGui()) : ClickGui.clickGui;
    }
    
    private void load() {
        int x = -84;
        for (final Category c : Category.values()) {
            if (c != Category.HIDDEN) {
                if (c != Category.HUD) {
                    final ArrayList<Panel> panels = this.panels;
                    final String label = Character.toUpperCase(c.name().toLowerCase().charAt(0)) + c.name().toLowerCase().substring(1);
                    x += 90;
                    panels.add(new Panel(label, x, 4, true) {
                        @Override
                        public void setupItems() {
                            for (final Module m : Xulu.MODULE_MANAGER.getModules()) {
                                if (!m.getCategory().equals(c)) {
                                    continue;
                                }
                                this.addButton(new ModuleButton(m, this));
                            }
                        }
                    });
                }
            }
        }
        this.panels.forEach(panel -> panel.getItems().sort((item1, item2) -> item1.getLabel().compareTo(item2.getLabel())));
    }
    
    public void drawScreen(final int mouseX, final int mouseY, final float partialTicks) {
        this.drawDefaultBackground();
        this.panels.forEach(panel -> panel.drawScreen(mouseX, mouseY, partialTicks));
    }
    
    public void mouseClicked(final int mouseX, final int mouseY, final int clickedButton) {
        this.panels.forEach(panel -> panel.mouseClicked(mouseX, mouseY, clickedButton));
    }
    
    public void mouseReleased(final int mouseX, final int mouseY, final int releaseButton) {
        this.panels.forEach(panel -> panel.mouseReleased(mouseX, mouseY, releaseButton));
    }
    
    protected void keyTyped(final char typedChar, final int keyCode) {
        for (final Panel p : this.panels) {
            if (p != null && p.getOpen()) {
                for (final Item e : p.getItems()) {
                    try {
                        if (e.keyTyped(typedChar, keyCode)) {
                            return;
                        }
                        continue;
                    }
                    catch (IOException e2) {
                        e2.printStackTrace();
                    }
                }
            }
        }
        try {
            super.keyTyped(typedChar, keyCode);
        }
        catch (IOException e3) {
            e3.printStackTrace();
        }
    }
    
    public void handleMouseInput() throws IOException {
        final int scrollAmount = 5;
        if (Mouse.getEventDWheel() > 0) {
            for (final Panel p : this.panels) {
                p.setY(p.getY() + scrollAmount);
            }
        }
        if (Mouse.getEventDWheel() < 0) {
            for (final Panel p : this.panels) {
                p.setY(p.getY() - scrollAmount);
            }
        }
        super.handleMouseInput();
    }
    
    public boolean doesGuiPauseGame() {
        return false;
    }
    
    public final ArrayList<Panel> getPanels() {
        return this.panels;
    }
}
