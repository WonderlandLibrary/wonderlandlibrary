/*
 * Decompiled with CFR 0.152.
 */
package tk.rektsky.ui.clickgui.components.impl;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Locale;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import tk.rektsky.Client;
import tk.rektsky.module.Category;
import tk.rektsky.module.Module;
import tk.rektsky.module.ModulesManager;
import tk.rektsky.ui.clickgui.AlloyColors;
import tk.rektsky.ui.clickgui.components.Component;
import tk.rektsky.ui.clickgui.components.impl.ModuleComponent;

public class CategoryComponent
extends Component {
    public Category category;
    public Color color;
    public ArrayList<ModuleComponent> modules = new ArrayList();
    public boolean open = false;

    public CategoryComponent(Color color, Category cat, int x2, int y2) {
        super(x2, y2);
        for (Module m2 : ModulesManager.getModules()) {
            if (m2.category != cat) continue;
            this.modules.add(new ModuleComponent(cat, color, x2, -1));
        }
        this.category = cat;
        this.color = color;
    }

    @Override
    public void draw(int mouseX, int mouseY) {
        Gui.drawRect(this.x, this.y, this.x + this.getWidth(), this.y + this.getHeight(), AlloyColors.ClickGUITop.getRGB());
        Client.getCasperBold().drawString(this.category.getName().toLowerCase(Locale.ROOT), this.x + 4, this.y + 5, this.color.getRGB());
        Minecraft.getMinecraft().getTextureManager().bindTexture(this.category.getIcon());
        Minecraft.getMinecraft().fontRendererObj.drawString("", -1, -1, this.color.getRGB());
        Gui.drawModalRectWithCustomSizedTexture(this.x + this.getWidth() - 20, this.y + 4, 16.0f, 16.0f, 16, 16, 16.0f, 16.0f);
        int oy = this.y;
        if (this.open) {
            for (ModuleComponent module : this.modules) {
                module.draw(mouseX, mouseY, oy);
                oy += module.getHeight();
            }
        }
    }

    @Override
    public void onClick(int mouseX, int mouseY, int mouseBtn) {
        if (mouseBtn != 0 && this.isMouseOverObj(mouseX, mouseY)) {
            this.open = !this.open;
        }
        for (ModuleComponent module : this.modules) {
            module.onClick(mouseX, mouseY, mouseBtn);
        }
    }

    @Override
    public int getWidth() {
        return (int)(Client.getCasperBold().getWidth(this.category.getName().toLowerCase(Locale.ROOT)) + 20.0f + 16.0f);
    }

    @Override
    public int getHeight() {
        return Client.getCasperBold().FONT_HEIGHT + 15;
    }
}

