/*
 * Decompiled with CFR 0.152.
 */
package tk.rektsky.ui.clickgui.components.impl;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Locale;
import net.minecraft.client.gui.Gui;
import tk.rektsky.Client;
import tk.rektsky.module.Category;
import tk.rektsky.module.Module;
import tk.rektsky.module.ModulesManager;
import tk.rektsky.ui.clickgui.AlloyColors;
import tk.rektsky.ui.clickgui.components.Component;

public class ModuleComponent
extends Component {
    public Category category;
    public Color color;
    public boolean open = false;
    ArrayList<Module> modules = new ArrayList();

    public ModuleComponent(Category category, Color color, int x2, int y2) {
        super(x2, y2);
        this.category = category;
        this.color = color;
        for (Module m2 : ModulesManager.getModules()) {
            if (!m2.category.equals((Object)category)) continue;
            this.modules.add(m2);
        }
    }

    @Override
    public int getWidth() {
        float offset = 10.0f;
        float w2 = Client.getCasper().getWidth(this.category.getName().toLowerCase(Locale.ROOT)) + offset;
        for (Module m2 : this.modules) {
            if (!(Client.getCasper().getWidth(m2.name) + offset > w2)) continue;
            w2 = Client.getCasper().getWidth(m2.name) + offset;
        }
        return (int)w2;
    }

    @Override
    public int getHeight() {
        return Client.getCasper().FONT_HEIGHT + 10;
    }

    public void draw(int mouseX, int mouseY, int y2) {
        Gui.drawRect(this.x, y2, y2 + this.getWidth(), this.x + this.getHeight(), AlloyColors.ClickGUITop.getRGB());
        Client.getCasper().drawString(this.category.getName().toLowerCase(Locale.ROOT), this.x + 4, y2 + 4, this.color.getRGB());
        if (!this.open) {
            return;
        }
    }

    @Override
    public void onClick(int mouseX, int mouseY, int mouseBtn) {
        if (mouseBtn != 0 && this.isMouseOverObj(mouseX, mouseY)) {
            this.open = !this.open;
        }
    }
}

