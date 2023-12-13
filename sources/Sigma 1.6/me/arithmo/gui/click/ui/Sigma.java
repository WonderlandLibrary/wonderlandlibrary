/*
 * Decompiled with CFR 0_122.
 * 
 * Could not load the following classes:
 *  org.lwjgl.opengl.GL11
 */
package me.arithmo.gui.click.ui;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import me.arithmo.Client;
import me.arithmo.gui.click.ClickGui;
import me.arithmo.gui.click.components.Button;
import me.arithmo.gui.click.components.CategoryButton;
import me.arithmo.gui.click.components.CategoryPanel;
import me.arithmo.gui.click.components.Checkbox;
import me.arithmo.gui.click.components.ColorPreview;
import me.arithmo.gui.click.components.DropdownBox;
import me.arithmo.gui.click.components.DropdownButton;
import me.arithmo.gui.click.components.MainPanel;
import me.arithmo.gui.click.components.RGBSlider;
import me.arithmo.gui.click.components.SLButton;
import me.arithmo.gui.click.components.Slider;
import me.arithmo.gui.click.ui.UI;
import me.arithmo.management.ColorManager;
import me.arithmo.management.ColorObject;
import me.arithmo.management.FontManager;
import me.arithmo.management.animate.Expand;
import me.arithmo.management.animate.Rotate;
import me.arithmo.management.animate.Translate;
import me.arithmo.management.command.impl.Color;
import me.arithmo.management.keybinding.Keybind;
import me.arithmo.module.Module;
import me.arithmo.module.data.ModuleData;
import me.arithmo.module.data.Setting;
import me.arithmo.module.data.SettingsMap;
import me.arithmo.util.MathUtils;
import me.arithmo.util.RenderingUtil;
import me.arithmo.util.StringConversions;
import me.arithmo.util.render.Colors;
import me.arithmo.util.render.TTFFontRenderer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.shader.ShaderGroup;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

public class Sigma
extends UI {
    public Expand expand = new Expand(0.0f, 0.0f, 0.0f, 0.0f);
    public Expand setExpand = new Expand(200.0f, 175.0f, 0.0f, 0.0f);
    private ResourceLocation gear = new ResourceLocation("textures/gear.png");
    private ResourceLocation gear2 = new ResourceLocation("textures/gear2.png");
    private ResourceLocation darrow = new ResourceLocation("textures/downarrow.png");
    private ResourceLocation uparrow = new ResourceLocation("textures/uparrow.png");
    private ResourceLocation msgo = new ResourceLocation("textures/msgo.png");
    private ResourceLocation others = new ResourceLocation("textures/others.png");
    private ResourceLocation xmark = new ResourceLocation("textures/xmark.png");
    private ResourceLocation combat = new ResourceLocation("textures/combat.png");
    private ResourceLocation player = new ResourceLocation("textures/player.png");
    private ResourceLocation visuals = new ResourceLocation("textures/visuals.png");
    private ResourceLocation movement = new ResourceLocation("textures/movement.png");
    private ResourceLocation colors = new ResourceLocation("textures/colors.png");
    private ResourceLocation msgo2 = new ResourceLocation("textures/msgo2.png");
    private ResourceLocation others2 = new ResourceLocation("textures/others2.png");
    private ResourceLocation xmark2 = new ResourceLocation("textures/xmark2.png");
    private ResourceLocation combat2 = new ResourceLocation("textures/combat2.png");
    private ResourceLocation player2 = new ResourceLocation("textures/player2.png");
    private ResourceLocation visuals2 = new ResourceLocation("textures/visuals2.png");
    private ResourceLocation movement2 = new ResourceLocation("textures/movement2.png");
    private ResourceLocation colors2 = new ResourceLocation("textures/colors2.png");

    @Override
    public void mainConstructor(ClickGui p0, MainPanel panel) {
        float x = 0.0f;
        float y = 0.0f;
        panel.typeButton.add(new CategoryButton(panel, "Combat", x - 45.0f, y - 80.0f));
        panel.typeButton.add(new CategoryButton(panel, "Player", x + 45.0f, y - 80.0f));
        panel.typeButton.add(new CategoryButton(panel, "Movement", x - 45.0f, y));
        panel.typeButton.add(new CategoryButton(panel, "Visuals", x + 45.0f, y));
        panel.typeButton.add(new CategoryButton(panel, "Other", x - 45.0f, y + 80.0f));
        panel.typeButton.add(new CategoryButton(panel, "Colors", x + 45.0f, y + 80.0f));
    }

    @Override
    public void onClose() {
        this.g.entityRenderer.theShaderGroup = null;
        this.expand.setExpandX(0.0f);
        this.expand.setExpandY(0.0f);
        this.setExpand.setExpandX(0.0f);
        this.setExpand.setExpandY(0.0f);
    }

    @Override
    public void mainPanelDraw(MainPanel panel, int p0, int p1) {
        int speedy;
        if (this.expand.getExpandX() == 0.0f) {
            if (panel.currentPanel != null) {
                panel.currentPanel.settingModule = null;
                panel.currentPanel = null;
            }
            this.g.entityRenderer.func_175069_a(EntityRenderer.shaderResourceLocations[18]);
        }
        ScaledResolution res = new ScaledResolution(this.g, this.g.displayWidth, this.g.displayHeight);
        float x = res.getScaledWidth() / 2;
        float y = res.getScaledHeight() / 2;
        GL11.glPushMatrix();
        GL11.glScissor((int)((int)(x - this.expand.getExpandX() / 2.0f) * 2), (int)((int)(y - this.expand.getExpandY() / 2.0f) * 2), (int)((int)this.expand.getExpandX() * 2), (int)((int)this.expand.getExpandY() * 2));
        GL11.glEnable((int)3089);
        int speedx = (int)((double)((float)(panel.currentPanel != null ? 400 : 200) - this.expand.getExpandX()) * (panel.currentPanel != null ? 0.22 : 0.4));
        if (speedx < 2 && (float)(panel.currentPanel != null ? 400 : 200) - this.expand.getExpandX() > 0.0f) {
            speedx = 8;
        }
        if ((speedy = (int)((double)((float)(panel.currentPanel != null ? 350 : 300) - this.expand.getExpandY()) * 0.4)) < 3 && (float)(panel.currentPanel != null ? 350 : 300) - this.expand.getExpandY() > 0.0f) {
            int n = speedy = panel.currentPanel != null ? 6 : 12;
        }
        if (panel.currentPanel != null) {
            this.expand.interpolate(300.0f, 400.0f, speedx, speedy);
        } else {
            this.expand.interpolate(200.0f, 300.0f, speedx, speedy);
        }
        RenderingUtil.drawRoundedRect(x - 99.0f, y - 149.0f, x + 99.0f, y + 149.0f, Colors.getColor(232, 245), Colors.getColor(232, 245));
        TTFFontRenderer font = Client.fm.getFont("SFR 14");
        font.drawCenteredString("Sigma", x, y - 140.0f, Colors.getColor(45));
        for (CategoryButton button : panel.typeButton) {
            button.draw(p0, p1);
        }
        if (panel.currentPanel != null) {
            panel.currentPanel.draw(p0, p1);
            boolean h = (float)p0 >= x + 125.0f && (float)p0 <= x + 125.0f + 16.0f && (float)p1 >= y - 165.0f && (float)p1 <= y - 165.0f + 16.0f;
            this.g.fontRendererObj.drawStringWithShadow("", 300.0f, 300.0f, -1);
            GlStateManager.pushMatrix();
            GlStateManager.enableBlend();
            GL11.glScissor((int)((int)(x - this.expand.getExpandX() / 2.0f) * 2), (int)((int)(y - this.expand.getExpandY() / 2.0f) * 2), (int)((int)this.expand.getExpandX() * 2), (int)((int)this.expand.getExpandY() * 2));
            GL11.glEnable((int)3089);
            this.g.getTextureManager().bindTexture(h ? this.xmark2 : this.xmark);
            this.drawModalRectWithCustomSizedTexture(x + 125.0f, y - 165.0f, 0.0f, 0.0f, 16.0f, 16.0f, 16.0f, 16.0f);
            GL11.glDisable((int)3089);
            GlStateManager.disableBlend();
            GlStateManager.popMatrix();
        } else {
            boolean h = (float)p0 >= x + 78.0f && (float)p0 <= x + 78.0f + 16.0f && (float)p1 >= y - 145.0f && (float)p1 <= y - 145.0f + 16.0f;
            GlStateManager.enableBlend();
            this.g.getTextureManager().bindTexture(h ? this.xmark2 : this.xmark);
            this.drawModalRectWithCustomSizedTexture(x + 78.0f, y - 145.0f, 0.0f, 0.0f, 16.0f, 16.0f, 16.0f, 16.0f);
            GlStateManager.disableBlend();
        }
        GL11.glDisable((int)3089);
        GL11.glPopMatrix();
    }

    @Override
    public void mainPanelKeyPress(MainPanel panel, int key) {
    }

    @Override
    public void panelConstructor(MainPanel mainPanel, float x, float y) {
    }

    @Override
    public void panelMouseClicked(MainPanel mainPanel, int p1, int p2, int p3) {
        boolean h;
        ScaledResolution res = new ScaledResolution(this.g, this.g.displayWidth, this.g.displayHeight);
        float x = res.getScaledWidth() / 2;
        float y = res.getScaledHeight() / 2;
        if (mainPanel.currentPanel != null) {
            h = (float)p1 >= x + 125.0f && (float)p1 <= x + 125.0f + 16.0f && (float)p2 >= y - 165.0f && (float)p2 <= y - 165.0f + 16.0f;
        } else {
            boolean bl = h = (float)p1 >= x + 78.0f && (float)p1 <= x + 78.0f + 16.0f && (float)p2 >= y - 145.0f && (float)p2 <= y - 145.0f + 16.0f;
        }
        if (h && p3 == 0) {
            if (mainPanel.currentPanel == null) {
                this.g.displayGuiScreen(null);
                this.expand.setExpandX(0.0f);
                this.expand.setExpandY(0.0f);
            } else if (mainPanel.currentPanel.settingModule == null) {
                this.expand.setExpandX(0.0f);
                this.expand.setExpandY(0.0f);
                mainPanel.currentPanel.visible = false;
                mainPanel.currentPanel = null;
            } else {
                mainPanel.currentPanel.settingModule = null;
            }
        }
        if (mainPanel.currentPanel == null) {
            for (CategoryButton button : mainPanel.typeButton) {
                button.mouseClicked(p1, p2, p3);
            }
        } else {
            mainPanel.currentPanel.mouseClicked(p1, p2, p3);
        }
    }

    @Override
    public void panelMouseMovedOrUp(MainPanel mainPanel, int p1, int p2, int p3) {
        for (CategoryButton button : mainPanel.typeButton) {
            button.mouseReleased(p1, p2, p3);
        }
    }

    @Override
    public void categoryButtonConstructor(CategoryButton p0, MainPanel p1) {
        ScaledResolution res = new ScaledResolution(this.g, this.g.displayWidth, this.g.displayHeight);
        float x = res.getScaledWidth() / 2;
        float y = res.getScaledHeight() / 2;
        p0.categoryPanel = new CategoryPanel(p0.name, p0, x, y);
    }

    @Override
    public void categoryButtonMouseClicked(CategoryButton p0, MainPanel p1, int p2, int p3, int p4) {
        boolean h;
        ScaledResolution res = new ScaledResolution(this.g, this.g.displayWidth, this.g.displayHeight);
        float x = res.getScaledWidth() / 2;
        float y = res.getScaledHeight() / 2;
        boolean bl = h = (float)p2 >= p0.x + x - 28.0f && (float)p3 >= p0.y + y - 36.0f && (float)p2 <= p0.x + x + 28.0f && (float)p3 <= p0.y + y + 10.0f;
        if (h && p4 == 0) {
            this.expand.setExpandX(1.0f);
            this.expand.setExpandY(1.0f);
            p1.currentPanel = p0.categoryPanel;
            p0.categoryPanel.visible = true;
        }
        p0.categoryPanel.mouseClicked(p2, p3, p4);
    }

    @Override
    public void categoryButtonDraw(CategoryButton p0, float p2, float p3) {
        ScaledResolution res = new ScaledResolution(this.g, this.g.displayWidth, this.g.displayHeight);
        float x = res.getScaledWidth() / 2;
        float y = res.getScaledHeight() / 2;
        boolean h = p2 >= p0.x + x - 28.0f && p3 >= p0.y + y - 36.0f && p2 <= p0.x + x + 28.0f && p3 <= p0.y + y + 10.0f;
        int color = !h ? Colors.getColor(32) : Colors.getColor(232, 60, 40);
        Client.fm.getFont("SFM 12").drawCenteredString(p0.name, p0.x + x, p0.y + y + 2.0f, color);
        GlStateManager.enableBlend();
        switch (p0.name) {
            case "Combat": {
                this.g.getTextureManager().bindTexture(h ? this.combat2 : this.combat);
                this.drawModalRectWithCustomSizedTexture(p0.x + x - 14.0f, p0.y + y - 36.0f, 0.0f, 0.0f, 32.0f, 32.0f, 32.0f, 32.0f);
                break;
            }
            case "Movement": {
                this.g.getTextureManager().bindTexture(h ? this.movement2 : this.movement);
                this.drawModalRectWithCustomSizedTexture(p0.x + x - 14.0f, p0.y + y - 36.0f, 0.0f, 0.0f, 32.0f, 32.0f, 32.0f, 32.0f);
                break;
            }
            case "Player": {
                this.g.getTextureManager().bindTexture(h ? this.player2 : this.player);
                this.drawModalRectWithCustomSizedTexture(p0.x + x - 16.0f, p0.y + y - 36.0f, 0.0f, -5.0f, 32.0f, 32.0f, 32.0f, 32.0f);
                break;
            }
            case "Visuals": {
                this.g.getTextureManager().bindTexture(h ? this.visuals2 : this.visuals);
                this.drawModalRectWithCustomSizedTexture(p0.x + x - 16.0f, p0.y + y - 36.0f, 0.0f, -4.0f, 32.0f, 32.0f, 32.0f, 32.0f);
                break;
            }
            case "Colors": {
                this.g.getTextureManager().bindTexture(h ? this.colors2 : this.colors);
                this.drawModalRectWithCustomSizedTexture(p0.x + x - 16.0f, p0.y + y - 36.0f, 0.0f, 0.0f, 32.0f, 32.0f, 32.0f, 32.0f);
                break;
            }
            case "Other": {
                this.g.getTextureManager().bindTexture(h ? this.others2 : this.others);
                this.drawModalRectWithCustomSizedTexture(p0.x + x - 16.0f, p0.y + y - 36.0f, 0.0f, 0.0f, 32.0f, 32.0f, 32.0f, 32.0f);
            }
        }
        GlStateManager.disableBlend();
    }

    private List<Setting> getSettings(Module mod) {
        ArrayList<Setting> settings = new ArrayList<Setting>();
        settings.addAll(mod.getSettings().values());
        if (settings.isEmpty()) {
            return null;
        }
        return settings;
    }

    private List<Module> getModules(ModuleData.Type type) {
        ArrayList<Module> modulesInType = new ArrayList<Module>();
        for (Module mod : Client.getModuleManager().getArray()) {
            if (mod.getType() != type) continue;
            modulesInType.add(mod);
        }
        if (modulesInType.isEmpty()) {
            return null;
        }
        modulesInType.sort(Comparator.comparing(Module::getName));
        return modulesInType;
    }

    @Override
    public void categoryPanelConstructor(CategoryPanel categoryPanel, CategoryButton categoryButton, float x, float y) {
        int rowCount;
        int meme;
        ScaledResolution res = new ScaledResolution(this.g, this.g.displayWidth, this.g.displayHeight);
        int yOff = 0;
        if (categoryButton.name.equalsIgnoreCase("Combat")) {
            rowCount = 0;
            for (Module module : this.getModules(ModuleData.Type.Combat)) {
                categoryPanel.buttons.add(new Button(categoryPanel, module.getName(), 85 * rowCount, yOff, module));
                if (++rowCount == 3) {
                    yOff += 42;
                    rowCount = 0;
                }
                meme = 0;
                for (Setting setting : module.getSettings().values()) {
                    if (!setting.getValue().getClass().equals(Boolean.class)) continue;
                    categoryPanel.checkboxes.add(new Checkbox(categoryPanel, setting.getName(), 0.0f, meme, setting, module));
                    meme += 14;
                }
                for (Setting setting : module.getSettings().values()) {
                    if (!(setting.getValue() instanceof Number)) continue;
                    categoryPanel.sliders.add(new Slider(categoryPanel, 0.0f, meme, setting, module));
                    meme += 22;
                }
            }
        }
        if (categoryButton.name.equalsIgnoreCase("Movement")) {
            rowCount = 0;
            for (Module module : this.getModules(ModuleData.Type.Movement)) {
                categoryPanel.buttons.add(new Button(categoryPanel, module.getName(), 85 * rowCount, yOff, module));
                if (++rowCount == 3) {
                    yOff += 42;
                    rowCount = 0;
                }
                meme = 0;
                for (Setting setting : module.getSettings().values()) {
                    if (!setting.getValue().getClass().equals(Boolean.class)) continue;
                    categoryPanel.checkboxes.add(new Checkbox(categoryPanel, setting.getName(), 0.0f, meme, setting, module));
                    meme += 14;
                }
                for (Setting setting : module.getSettings().values()) {
                    if (!(setting.getValue() instanceof Number)) continue;
                    categoryPanel.sliders.add(new Slider(categoryPanel, 0.0f, meme, setting, module));
                    meme += 22;
                }
            }
        }
        if (categoryButton.name.equalsIgnoreCase("Player")) {
            rowCount = 0;
            for (Module module : this.getModules(ModuleData.Type.Player)) {
                categoryPanel.buttons.add(new Button(categoryPanel, module.getName(), 85 * rowCount, yOff, module));
                if (++rowCount == 3) {
                    yOff += 42;
                    rowCount = 0;
                }
                meme = 0;
                for (Setting setting : module.getSettings().values()) {
                    if (!setting.getValue().getClass().equals(Boolean.class)) continue;
                    categoryPanel.checkboxes.add(new Checkbox(categoryPanel, setting.getName(), 0.0f, meme, setting, module));
                    meme += 14;
                }
                for (Setting setting : module.getSettings().values()) {
                    if (!(setting.getValue() instanceof Number)) continue;
                    categoryPanel.sliders.add(new Slider(categoryPanel, 0.0f, meme, setting, module));
                    meme += 22;
                }
            }
        }
        if (categoryButton.name.equalsIgnoreCase("Visuals")) {
            rowCount = 0;
            for (Module module : this.getModules(ModuleData.Type.Visuals)) {
                categoryPanel.buttons.add(new Button(categoryPanel, module.getName(), 85 * rowCount, yOff, module));
                if (++rowCount == 3) {
                    yOff += 42;
                    rowCount = 0;
                }
                meme = 0;
                for (Setting setting : module.getSettings().values()) {
                    if (!setting.getValue().getClass().equals(Boolean.class)) continue;
                    categoryPanel.checkboxes.add(new Checkbox(categoryPanel, setting.getName(), 0.0f, meme, setting, module));
                    meme += 14;
                }
                for (Setting setting : module.getSettings().values()) {
                    if (!(setting.getValue() instanceof Number)) continue;
                    categoryPanel.sliders.add(new Slider(categoryPanel, 0.0f, meme, setting, module));
                    meme += 22;
                }
            }
        }
        if (categoryButton.name.equalsIgnoreCase("Other")) {
            rowCount = 0;
            for (Module module : this.getModules(ModuleData.Type.Other)) {
                categoryPanel.buttons.add(new Button(categoryPanel, module.getName(), 85 * rowCount, yOff, module));
                if (++rowCount == 3) {
                    yOff += 42;
                    rowCount = 0;
                }
                meme = 0;
                for (Setting setting : module.getSettings().values()) {
                    if (!setting.getValue().getClass().equals(Boolean.class)) continue;
                    categoryPanel.checkboxes.add(new Checkbox(categoryPanel, setting.getName(), 0.0f, meme, setting, module));
                    meme += 14;
                }
                for (Setting setting : module.getSettings().values()) {
                    if (!(setting.getValue() instanceof Number)) continue;
                    categoryPanel.sliders.add(new Slider(categoryPanel, 0.0f, meme, setting, module));
                    meme += 22;
                }
            }
        }
        if (categoryButton.name.equalsIgnoreCase("Colors")) {
            categoryPanel.colorPreviews.add(new ColorPreview(Client.cm.getHudColor(), "Hud Color", 10.0f, 0.0f, categoryButton));
        }
    }

    @Override
    public void categoryPanelMouseClicked(CategoryPanel categoryPanel, int p1, int p2, int p3) {
        if (this.expand.getExpandX() > 150.0f && categoryPanel.categoryButton.panel.currentPanel == categoryPanel) {
            for (Button button : categoryPanel.buttons) {
                button.mouseClicked(p1, p2, p3);
            }
            if (categoryPanel.settingModule != null) {
                for (Checkbox checkbox : categoryPanel.checkboxes) {
                    if (checkbox.module != categoryPanel.settingModule) continue;
                    checkbox.mouseClicked(p1, p2, p3);
                }
                for (Slider slider : categoryPanel.sliders) {
                    if (slider.module != categoryPanel.settingModule) continue;
                    slider.mouseClicked(p1, p2, p3);
                }
            }
            for (ColorPreview colorPreview : categoryPanel.colorPreviews) {
                for (RGBSlider slidr : colorPreview.sliders) {
                    slidr.mouseClicked(p1, p2, p3);
                }
            }
        }
    }

    @Override
    public void categoryPanelDraw(CategoryPanel categoryPanel, float x, float y) {
        ScaledResolution res = new ScaledResolution(this.g, this.g.displayWidth, this.g.displayHeight);
        float x1 = res.getScaledWidth() / 2;
        float y1 = res.getScaledHeight() / 2;
        RenderingUtil.drawRoundedRect(x1 - 148.0f, y1 - 173.0f, x1 + 148.0f, y1 + 173.0f, Colors.getColor(232), Colors.getColor(232));
        Client.fm.getFont("SFR 12").drawCenteredString(categoryPanel.headerString, x1, y1 - 160.0f, Colors.getColor(0));
        if (categoryPanel.settingModule != null) {
            this.setExpand.interpolate(this.setExpand.getX(), 310.0f, 0, 25);
        } else {
            this.setExpand.interpolate(this.setExpand.getX(), 0.0f, 0, 32);
        }
        for (ColorPreview colorPreview : categoryPanel.colorPreviews) {
            colorPreview.draw(x, y);
        }
        for (Button button : categoryPanel.buttons) {
            button.draw(x, y);
        }
        GL11.glPushMatrix();
        GL11.glScissor((int)((int)(x1 - 146.0f) * 2), (int)((int)(y1 - 170.0f) * 2), (int)584, (int)((int)this.setExpand.getExpandY() * 2));
        GL11.glEnable((int)3089);
        RenderingUtil.drawRoundedRect(x1 - 145.0f, y1 - 138.0f, x1 + 145.0f, y1 + 170.0f, Colors.getColor(215), Colors.getColor(215));
        for (Slider slider : categoryPanel.sliders) {
            if (slider.module != categoryPanel.settingModule) continue;
            slider.draw(x, y);
        }
        for (Checkbox checkbox : categoryPanel.checkboxes) {
            if (checkbox.module != categoryPanel.settingModule) continue;
            checkbox.draw(x, y);
        }
        if (categoryPanel.settingModule != null) {
            Client.fm.getFont("SFR 10").drawString(categoryPanel.settingModule.getName(), x1 - 140.0f, y1 - 133.0f, Colors.getColor(32));
        }
        GL11.glDisable((int)3089);
        GL11.glPopMatrix();
    }

    @Override
    public void categoryPanelMouseMovedOrUp(CategoryPanel categoryPanel, int x, int y, int button) {
        if (categoryPanel.settingModule != null) {
            for (Slider slider : categoryPanel.sliders) {
                if (slider.module != categoryPanel.settingModule) continue;
                slider.mouseReleased(x, y, button);
            }
        }
        for (ColorPreview colorPreview : categoryPanel.colorPreviews) {
            for (RGBSlider slidr : colorPreview.sliders) {
                slidr.mouseReleased(x, y, button);
            }
        }
    }

    @Override
    public void buttonContructor(Button p0, CategoryPanel panel) {
    }

    @Override
    public void buttonMouseClicked(Button p0, int p2, int p3, int p4, CategoryPanel panel) {
        boolean setHovered;
        ScaledResolution res = new ScaledResolution(this.g, this.g.displayWidth, this.g.displayHeight);
        float x1 = (float)(res.getScaledWidth() / 2 - 120) + p0.x;
        float y1 = res.getScaledHeight() / 2 - 130;
        boolean hovered = (float)p2 >= x1 + 51.0f && (float)p3 >= y1 + p0.y - 4.0f && (float)p2 <= x1 + 71.0f && (float)p3 <= y1 + p0.y + 5.0f;
        boolean bl = setHovered = (float)p2 >= x1 + 60.0f && (float)p3 >= y1 + p0.y + 10.5f && (float)p2 <= x1 + 70.0f && (float)p3 <= y1 + p0.y + 20.5f;
        if (panel.settingModule == null) {
            if (hovered && p4 == 0) {
                p0.module.toggle();
            } else if (setHovered && p4 == 0 && this.getSettings(p0.module) != null) {
                panel.settingModule = p0.module;
            } else if (p4 == 1) {
                panel.settingModule = null;
            }
        }
    }

    @Override
    public void buttonDraw(Button p0, float p2, float p3, CategoryPanel panel) {
        boolean hovered;
        ScaledResolution res = new ScaledResolution(this.g, this.g.displayWidth, this.g.displayHeight);
        float x1 = (float)(res.getScaledWidth() / 2 - 120) + p0.x;
        float y1 = res.getScaledHeight() / 2 - 130;
        RenderingUtil.rectangleBordered(x1 + 51.0f, y1 + p0.y - 4.0f, x1 + 71.0f, y1 + p0.y + 5.0f, 1.0, Colors.getColor(140), Colors.getColor(165));
        if (p0.module.isEnabled()) {
            RenderingUtil.rectangle(x1 + 55.0f, y1 + p0.y - 2.0f, x1 + 56.0f, y1 + p0.y + 3.0f, Colors.getColor(50, 200, 65));
        } else {
            RenderingUtil.drawCircle(x1 + 66.0f, y1 + p0.y + 0.6f, 2.3f, 64, Colors.getColor(200, 50, 65));
            RenderingUtil.drawCircle(0.0f, 0.0f, 0.0f, 3, Colors.getColor(165));
        }
        p0.translate.interpolate(p0.module.isEnabled() ? 10.0f : 0.0f, 0.0f, 2);
        float offset = p0.translate.getX();
        RenderingUtil.rectangle(x1 + 53.0f + offset, y1 + p0.y - 2.0f, x1 + 59.0f + offset, y1 + p0.y + 3.0f, Colors.getColor(165));
        Client.fm.getFont("SFM 8").drawString(p0.name, x1, y1 + p0.y - 1.0f, p0.module.isEnabled() ? Colors.getColor(32) : Colors.getColor(110));
        String meme = !p0.module.getKeybind().getKeyStr().equalsIgnoreCase("None") ? p0.module.getKeybind().getKeyStr() : "None";
        Client.fm.getFont("SFM 7").drawString("Bind", x1 + 2.0f, y1 + p0.y + 14.0f, Colors.getColor(32));
        Client.fm.getFont("SFR 7").drawString(meme, x1 + 2.0f, y1 + p0.y + 20.0f, Colors.getColor(32));
        if (this.getSettings(p0.module) != null) {
            Client.fm.getFont("SFM 6").drawString("Settings", x1 + 36.0f, y1 + p0.y + 15.0f, Colors.getColor(32));
            GlStateManager.pushMatrix();
            GlStateManager.bindTexture(0);
            GlStateManager.enableBlend();
            boolean h = p2 >= x1 + 60.0f && p3 >= y1 + p0.y + 10.5f && p2 <= x1 + 70.0f && p3 <= y1 + p0.y + 20.5f;
            p0.rotate.interpolate(h ? 360.0f : 0.0f, h ? 3 : 4);
            if (p0.rotate.getAngle() >= 360.0f) {
                p0.rotate.setAngle(0);
            }
            GlStateManager.translate(x1 + 65.0f, y1 + p0.y + 15.5f, 0.0f);
            GlStateManager.rotate(p0.rotate.getAngle(), 0.0f, 0.0f, 1.0f);
            this.g.getTextureManager().bindTexture(h ? this.gear2 : this.gear);
            this.drawModalRectWithCustomSizedTexture(-5.0f, -5.0f, 0.0f, 0.0f, 10.0f, 10.0f, 10.0f, 10.0f);
            GlStateManager.disableBlend();
            GlStateManager.popMatrix();
        }
        boolean bl = hovered = p2 >= x1 && p3 >= y1 + p0.y - 1.0f && p2 <= x1 + 66.0f && p3 <= y1 + p0.y + 20.0f;
        if (hovered) {
            GlStateManager.pushMatrix();
            GlStateManager.enableBlend();
            boolean h = p2 >= x1 + 60.0f && p3 >= y1 + p0.y + 10.5f && p2 <= x1 + 70.0f && p3 <= y1 + p0.y + 20.5f;
            GlStateManager.translate((double)(x1 - p0.x - 20.0f), (double)y1 + 296.5, 0.0);
            GlStateManager.rotate(90.0f, 0.0f, 0.0f, 1.0f);
            this.g.getTextureManager().bindTexture(this.uparrow);
            this.drawModalRectWithCustomSizedTexture(-5.0f, -5.0f, 0.0f, 0.0f, 10.0f, 10.0f, 10.0f, 10.0f);
            GlStateManager.popMatrix();
            Client.fm.getFont("SFM 8").drawString(p0.module.getDescription(), x1 - p0.x - 15.0f, y1 + 295.0f, Colors.getColor(32));
        }
    }

    @Override
    public void buttonKeyPressed(Button button, int key) {
    }

    @Override
    public void checkBoxMouseClicked(Checkbox p0, int p2, int p3, int p4, CategoryPanel panel) {
        ScaledResolution res = new ScaledResolution(this.g, this.g.displayWidth, this.g.displayHeight);
        float x1 = res.getScaledWidth() / 2 - 135;
        float y1 = (float)(res.getScaledHeight() / 2 - 120) + p0.y;
        if (p0.panel.settingModule == p0.module) {
            boolean hovered;
            boolean bl = hovered = (float)p2 >= x1 + 61.0f && (float)p3 >= y1 - 1.0f && (float)p2 <= x1 + 81.0f && (float)p3 <= y1 + 8.0f;
            if (hovered && p4 == 0) {
                boolean xd = (Boolean)p0.setting.getValue();
                p0.setting.setValue(!xd);
                Module.saveSettings();
            }
        }
    }

    @Override
    public void checkBoxDraw(Checkbox p0, float p2, float p3, CategoryPanel panel) {
        ScaledResolution res = new ScaledResolution(this.g, this.g.displayWidth, this.g.displayHeight);
        float x1 = res.getScaledWidth() / 2 - 135;
        float y1 = (float)(res.getScaledHeight() / 2 - 120) + p0.y;
        boolean enabled = p0.enabled = ((Boolean)p0.setting.getValue()).booleanValue();
        RenderingUtil.rectangle(0.0, 0.0, 0.0, 0.0, 0);
        RenderingUtil.rectangleBordered(x1 + 61.0f, y1 - 1.0f, x1 + 81.0f, y1 + 8.0f, 1.0, Colors.getColor(140), Colors.getColor(165));
        if (enabled) {
            RenderingUtil.rectangle(x1 + 65.0f, y1 + 1.0f, x1 + 66.0f, y1 + 6.0f, Colors.getColor(50, 200, 65));
        } else {
            RenderingUtil.drawCircle(x1 + 76.0f, y1 + 3.5f, 2.0f, 64, Colors.getColor(200, 50, 65));
        }
        p0.translate.interpolate(enabled ? 10.0f : 0.0f, 0.0f, 2);
        float offset = p0.translate.getX();
        RenderingUtil.rectangle(x1 + 63.0f + offset, y1 + 1.0f, x1 + 69.0f + offset, y1 + 6.0f, Colors.getColor(165));
        String xd = "" + p0.setting.getName().charAt(0) + p0.setting.getName().toLowerCase().substring(1);
        Client.fm.getFont("SFR 10").drawString(xd, x1 + 8.0f, y1, Colors.getColor(32));
        RenderingUtil.rectangle(0.0, 0.0, 0.0, 0.0, 0);
        RenderingUtil.drawRoundedRect(x1 + 90.0f, y1 - 1.0f, x1 + 265.0f, y1 + 8.0f, 0, Colors.getColor(200));
        Client.fm.getFont("SFR 8").drawString(p0.setting.getDesc(), x1 + 92.0f, y1 + 2.0f, Colors.getColor(32));
    }

    @Override
    public void dropDownContructor(DropdownBox p0, float x, float u, CategoryPanel panel) {
    }

    @Override
    public void dropDownMouseClicked(DropdownBox p0, int x, int u, int mouse, CategoryPanel panel) {
    }

    @Override
    public void dropDownDraw(DropdownBox p0, float x, float y, CategoryPanel panel) {
    }

    @Override
    public void dropDownButtonMouseClicked(DropdownButton p0, DropdownBox p1, int x, int y, int mouse) {
    }

    @Override
    public void dropDownButtonDraw(DropdownButton p0, DropdownBox p1, float x, float y) {
    }

    @Override
    public void SliderContructor(Slider p0, CategoryPanel panel) {
        p0.dragX = ((Number)p0.setting.getValue()).doubleValue() * 120.0 / p0.setting.getMax();
        p0.dragging = false;
    }

    @Override
    public void SliderMouseClicked(Slider slider, int mouseX, int mouseY, int mouse, CategoryPanel panel) {
        ScaledResolution res = new ScaledResolution(this.g, this.g.displayWidth, this.g.displayHeight);
        float xOff = res.getScaledWidth() / 2 - 125;
        float yOff = (float)(res.getScaledHeight() / 2 - 120) + slider.y + 12.0f;
        if (panel.visible && (float)mouseX >= xOff && (float)mouseY >= yOff && (float)mouseX <= xOff + 120.0f && (float)mouseY <= yOff + 2.0f && mouse == 0) {
            slider.dragging = true;
            slider.lastDragX = (double)mouseX - slider.dragX;
        }
    }

    @Override
    public void SliderMouseMovedOrUp(Slider p0, int mouseX, int mouseY, int mouse, CategoryPanel panel) {
        if (mouse == 0) {
            Module.saveSettings();
            p0.dragging = false;
        }
    }

    private String getValue(double value) {
        return MathUtils.isInteger(value) ? "" + (int)value + "" : "" + value + "";
    }

    @Override
    public void SliderDraw(Slider p0, float p2, float p3, CategoryPanel panel) {
        Object newValue;
        ScaledResolution res = new ScaledResolution(this.g, this.g.displayWidth, this.g.displayHeight);
        float x1 = res.getScaledWidth() / 2 - 135;
        float y1 = (float)(res.getScaledHeight() / 2 - 120) + p0.y;
        String xd = "" + p0.setting.getName().charAt(0) + p0.setting.getName().toLowerCase().substring(1) + ": " + p0.setting.getValue();
        Client.fm.getFont("SFR 10").drawString(xd, x1 + 8.0f, y1, Colors.getColor(32));
        RenderingUtil.rectangle(0.0, 0.0, 0.0, 0.0, 0);
        RenderingUtil.drawRoundedRect(x1 + 90.0f, y1 - 1.0f, x1 + 265.0f, y1 + 8.0f, 0, Colors.getColor(200));
        Client.fm.getFont("SFR 8").drawString(p0.setting.getDesc(), x1 + 92.0f, y1 + 2.0f, Colors.getColor(32));
        RenderingUtil.rectangle(0.0, 0.0, 0.0, 0.0, 0);
        double fraction = p0.dragX / 120.0;
        double value = MathUtils.getIncremental(fraction * p0.setting.getMax(), p0.setting.getInc());
        float sliderX = (float)(((Number)p0.setting.getValue()).doubleValue() * 120.0 / p0.setting.getMax());
        float xOff = res.getScaledWidth() / 2 - 125;
        float yOff = (float)(res.getScaledHeight() / 2 - 120) + p0.y + 12.0f;
        RenderingUtil.drawRoundedRect(p0.x + xOff, yOff, p0.x + xOff + 120.0f, yOff + 2.0f, Colors.getColor(0), Colors.getColor(120));
        RenderingUtil.drawRoundedRect(p0.x + xOff, yOff, p0.x + xOff + sliderX, yOff + 2.0f, Colors.getColor(0), Colors.getColor(160));
        RenderingUtil.drawRoundedRect(p0.x + xOff + sliderX - 1.5f, yOff - 1.5f, p0.x + xOff + sliderX + 1.5f, yOff + 3.5f, 0, Colors.getColor(180));
        RenderingUtil.drawRoundedRect(p0.x + xOff + sliderX - 1.0f, yOff - 1.0f, p0.x + xOff + sliderX + 1.0f, yOff + 3.0f, 0, Colors.getColor(200));
        if (p0.dragging) {
            p0.dragX = (double)p2 - p0.lastDragX;
            newValue = StringConversions.castNumber(this.getValue(value), p0.setting.getInc());
            p0.setting.setValue(newValue);
        }
        if (((Number)p0.setting.getValue()).doubleValue() <= p0.setting.getMin()) {
            newValue = StringConversions.castNumber(this.getValue(p0.setting.getMin()), p0.setting.getInc());
            p0.setting.setValue(newValue);
        }
        if (((Number)p0.setting.getValue()).doubleValue() >= p0.setting.getMax()) {
            newValue = StringConversions.castNumber(this.getValue(p0.setting.getMax()), p0.setting.getInc());
            p0.setting.setValue(newValue);
        }
        if (p0.dragX <= 0.0) {
            p0.dragX = 0.0;
        }
        if (p0.dragX >= 120.0) {
            p0.dragX = 120.0;
        }
    }

    @Override
    public void categoryButtonMouseReleased(CategoryButton categoryButton, int x, int y, int button) {
        categoryButton.categoryPanel.mouseReleased(x, y, button);
    }

    @Override
    public void slButtonDraw(SLButton slButton, float x, float y, MainPanel panel) {
    }

    @Override
    public void slButtonMouseClicked(SLButton slButton, float x, float y, int button, MainPanel panel) {
    }

    @Override
    public void colorConstructor(ColorPreview colorPreview, float x, float y) {
        int i = 0;
        for (RGBSlider.Colors xd : RGBSlider.Colors.values()) {
            colorPreview.sliders.add(new RGBSlider(x + 3.0f, y + (float)i, colorPreview, xd));
            i += 10;
        }
    }

    @Override
    public void colorPrewviewDraw(ColorPreview colorPreview, float x, float y) {
        ScaledResolution res = new ScaledResolution(this.g, this.g.displayWidth, this.g.displayHeight);
        float x1 = (float)(res.getScaledWidth() / 2 - 120) + colorPreview.x;
        float y1 = (float)(res.getScaledHeight() / 2 - 130) + colorPreview.y;
        RenderingUtil.rectangleBordered(x1 - 5.0f, y1 - 5.0f, x1 + 100.0f, y1 + 40.0f, 1.5, Colors.getColor(150), Colors.getColor(165));
        Client.fm.getFont("SFR 8").drawString(colorPreview.colorName, x1, y1 - 11.0f, Colors.getColor(32));
        for (RGBSlider slider : colorPreview.sliders) {
            slider.draw(x, y);
        }
    }

    @Override
    public void rgbSliderDraw(RGBSlider slider, float x, float y) {
        ScaledResolution res = new ScaledResolution(this.g, this.g.displayWidth, this.g.displayHeight);
        float xOff = slider.x + (float)(res.getScaledWidth() / 2) - 120.0f;
        float yOff = slider.y + (float)(res.getScaledHeight() / 2) - 130.0f;
        double fraction = slider.dragX / 90.0;
        double value = MathUtils.getIncremental(fraction * 255.0, 1.0);
        ColorObject cO = slider.colorPreview.colorObject;
        int faggotNiggerColor = Colors.getColor(cO.red, cO.green, cO.blue, 255);
        int faggotNiggerColor2 = Colors.getColor(cO.red, cO.green, cO.blue, 120);
        RenderingUtil.rectangle(xOff, yOff, xOff + 90.0f, yOff + 6.0f, Colors.getColor(32));
        switch (slider.rgba) {
            case ALPHA: {
                faggotNiggerColor = Colors.getColor(ColorManager.hudColor.red, ColorManager.hudColor.green, ColorManager.hudColor.blue, 255);
                faggotNiggerColor2 = Colors.getColor(ColorManager.hudColor.red, ColorManager.hudColor.green, ColorManager.hudColor.blue, 120);
            }
        }
        this.g.fontRendererObj.drawString("", 100.0f, 100.0f, -1);
        RenderingUtil.rectangle(xOff, yOff, (double)xOff + 90.0 * fraction, yOff + 6.0f, Colors.getColor(0));
        RenderingUtil.drawGradient(xOff, yOff, (double)xOff + 90.0 * fraction, yOff + 6.0f, faggotNiggerColor, faggotNiggerColor2);
        String current = "R";
        switch (slider.rgba) {
            case BLUE: {
                current = "B";
                break;
            }
            case GREEN: {
                current = "G";
                break;
            }
            case ALPHA: {
                current = "A";
            }
        }
        Client.fm.getFont("SFR 6").drawString(current, xOff - 6.0f, yOff + 2.5f, -1);
        float textX = xOff + 30.0f - Client.fm.getFont("SFR 6").getWidth(Integer.toString((int)value)) / 2.0f;
        Client.fm.getFont("SFR 6").drawString(Integer.toString((int)value), textX, yOff + 5.0f, -1);
        double newValue = 0.0;
        if (slider.dragging) {
            slider.dragX = (double)x - slider.lastDragX;
            if (value <= 255.0 && value >= 0.0) {
                newValue = value;
            }
            switch (slider.rgba) {
                case RED: {
                    slider.colorPreview.colorObject.setRed((int)newValue);
                    break;
                }
                case GREEN: {
                    slider.colorPreview.colorObject.setGreen((int)newValue);
                    break;
                }
                case BLUE: {
                    slider.colorPreview.colorObject.setBlue((int)newValue);
                    break;
                }
                case ALPHA: {
                    slider.colorPreview.colorObject.setAlpha((int)newValue);
                }
            }
        }
        if (slider.dragX <= 0.0) {
            slider.dragX = 0.0;
        }
        if (slider.dragX >= 90.0) {
            slider.dragX = 90.0;
        }
    }

    @Override
    public void rgbSliderClick(RGBSlider slider, float x, float y, int mouse) {
        ScaledResolution res = new ScaledResolution(this.g, this.g.displayWidth, this.g.displayHeight);
        float xOff = slider.x + (float)(res.getScaledWidth() / 2) - 120.0f;
        float yOff = slider.y + (float)(res.getScaledHeight() / 2) - 130.0f;
        if (x >= xOff && y >= yOff && x <= xOff + 90.0f && y <= yOff + 6.0f && mouse == 0) {
            slider.dragging = true;
            slider.lastDragX = (double)x - slider.dragX;
        }
    }

    @Override
    public void rgbSliderMovedOrUp(RGBSlider slider, float x, float y, int mouse) {
        if (mouse == 0) {
            Color.saveStatus();
            slider.dragging = false;
        }
    }

    private void drawModalRectWithCustomSizedTexture(float x, float y, float u, float v, float width, float height, float textureWidth, float textureHeight) {
        GlStateManager.pushMatrix();
        float var8 = 1.0f / textureWidth;
        float var9 = 1.0f / textureHeight;
        Tessellator var10 = Tessellator.getInstance();
        WorldRenderer var11 = var10.getWorldRenderer();
        boolean var1 = false;
        var11.startDrawingQuads();
        var11.addVertexWithUV(x, y + height, 0.0, u * var8, (v + height) * var9);
        var11.addVertexWithUV(x + width, y + height, 0.0, (u + width) * var8, (v + height) * var9);
        var11.addVertexWithUV(x + width, y, 0.0, (u + width) * var8, v * var9);
        var11.addVertexWithUV(x, y, 0.0, u * var8, v * var9);
        var10.draw();
        GlStateManager.popMatrix();
    }

}

