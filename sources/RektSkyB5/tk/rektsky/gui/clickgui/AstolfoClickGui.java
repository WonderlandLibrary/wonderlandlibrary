/*
 * Decompiled with CFR 0.152.
 */
package tk.rektsky.gui.clickgui;

import cc.hyperium.utils.HyperiumFontRenderer;
import java.io.IOException;
import java.util.ArrayList;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
import tk.rektsky.Client;
import tk.rektsky.gui.clickgui.components.AstolfoButton;
import tk.rektsky.gui.clickgui.components.AstolfoCategoryPanel;
import tk.rektsky.gui.clickgui.components.AstolfoModuleButton;
import tk.rektsky.module.Category;
import tk.rektsky.module.ModulesManager;
import tk.rektsky.module.impl.rektsky.ClickGUI;
import tk.rektsky.ui.clickgui.AlloyColors;

public class AstolfoClickGui
extends GuiScreen {
    public ArrayList<AstolfoCategoryPanel> categoryPanels = new ArrayList();
    private int baseY = 0;
    public static HyperiumFontRenderer categoryNameFont = new HyperiumFontRenderer(HyperiumFontRenderer.getCasperBold(), 19.0f);
    public static HyperiumFontRenderer otherFont = new HyperiumFontRenderer(HyperiumFontRenderer.getDefaultFont(), 16.0f);
    public static HyperiumFontRenderer settingsFont = new HyperiumFontRenderer(HyperiumFontRenderer.getDefaultFont(), 14.0f);

    @Override
    public void initGui() {
        if (ModulesManager.getModuleByClass(ClickGUI.class).blur.getValue().booleanValue()) {
            this.mc.entityRenderer.loadShader(new ResourceLocation("shaders/post/blur.json"));
        }
        this.baseY = 0;
    }

    public AstolfoClickGui() {
        int count = 4;
        for (Category cat : Category.values()) {
            switch (cat) {
                case COMBAT: {
                    this.categoryPanels.add(new AstolfoCategoryPanel(count, 4.0f, 100.0f, 18.0f, cat, AlloyColors.COMBAT));
                    break;
                }
                case MOVEMENT: {
                    this.categoryPanels.add(new AstolfoCategoryPanel(count, 4.0f, 100.0f, 18.0f, cat, AlloyColors.MOVEMENT));
                    break;
                }
                case PLAYER: {
                    this.categoryPanels.add(new AstolfoCategoryPanel(count, 4.0f, 100.0f, 18.0f, cat, AlloyColors.PLAYER));
                    break;
                }
                case WORLD: {
                    this.categoryPanels.add(new AstolfoCategoryPanel(count, 4.0f, 100.0f, 18.0f, cat, AlloyColors.WORLD));
                    break;
                }
                case EXPLOIT: {
                    this.categoryPanels.add(new AstolfoCategoryPanel(count, 4.0f, 100.0f, 18.0f, cat, AlloyColors.EXPLOIT));
                    break;
                }
                case REKTSKY: {
                    this.categoryPanels.add(new AstolfoCategoryPanel(count, 4.0f, 100.0f, 18.0f, cat, AlloyColors.REKTSKY));
                    break;
                }
                case RENDER: {
                    this.categoryPanels.add(new AstolfoCategoryPanel(count, 4.0f, 100.0f, 18.0f, cat, AlloyColors.RENDER));
                }
            }
            count += 120;
        }
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        if (ModulesManager.getModuleByClass(ClickGUI.class).gradient.getValue().booleanValue()) {
            this.drawGradientRect(0, 0, this.width, this.height, 5853884, -1084665156);
        }
        this.mc.fontRendererObj.drawString(Client.A1, -1000, -1000, -1);
        GlStateManager.translate(0.0f, this.baseY, 0.0f);
        for (AstolfoCategoryPanel catPanel : this.categoryPanels) {
            catPanel.drawPanel(mouseX, mouseY);
        }
        for (AstolfoCategoryPanel categoryPanel : this.categoryPanels) {
            for (AstolfoModuleButton moduleButton : categoryPanel.moduleButtons) {
                if (!((float)mouseX > moduleButton.x) || !((float)mouseX < moduleButton.x + moduleButton.width) || !((float)mouseY > moduleButton.y) || !((float)mouseY < moduleButton.y + moduleButton.height)) continue;
                Gui.drawRect(mouseX, mouseY, mouseX + (int)otherFont.getWidth(moduleButton.module.description) + 6, mouseY + AstolfoClickGui.otherFont.FONT_HEIGHT + 6, -2013265920);
                otherFont.drawString(moduleButton.module.description, mouseX + 3, mouseY + 3, -1);
            }
        }
        GlStateManager.translate(0.0f, -this.baseY, 0.0f);
        otherFont.drawString("Modules marked with Yellow means it isn't bypassing Verus/Useless in BlocksMC!", 0.0f, 0.0f, -1);
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        Client.executorService.execute(() -> {
            for (AstolfoCategoryPanel catPan : this.categoryPanels) {
                catPan.mouseAction(mouseX, mouseY, true, mouseButton);
                if (!catPan.category.isEnabled()) continue;
                for (AstolfoModuleButton modPan : catPan.moduleButtons) {
                    modPan.mouseAction(mouseX, mouseY, true, mouseButton);
                    if (!modPan.extended) continue;
                    for (AstolfoButton pan : modPan.astolfoButtons) {
                        pan.mouseAction(mouseX, mouseY, true, mouseButton);
                    }
                }
            }
        });
    }

    @Override
    protected void mouseReleased(int mouseX, int mouseY, int state) {
        Client.executorService.execute(() -> {
            for (AstolfoCategoryPanel catPan : this.categoryPanels) {
                catPan.mouseAction(mouseX, mouseY, false, state);
                if (!catPan.category.isEnabled()) continue;
                for (AstolfoModuleButton modPan : catPan.moduleButtons) {
                    modPan.mouseAction(mouseX, mouseY, false, state);
                    if (!modPan.extended) continue;
                    for (AstolfoButton pan : modPan.astolfoButtons) {
                        pan.mouseAction(mouseX, mouseY, false, state);
                    }
                }
            }
        });
    }

    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }

    @Override
    public void closeGui() {
        this.mc.entityRenderer.stopUseShader();
        if (ModulesManager.getModuleByClass(ClickGUI.class).blur.getValue().booleanValue()) {
            // empty if block
        }
        try {
            ModulesManager.getModuleByClass(ClickGUI.class).rawSetToggled(false);
        }
        catch (Exception exception) {
            // empty catch block
        }
    }
}

