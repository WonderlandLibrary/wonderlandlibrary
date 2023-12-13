// 
// Decompiled by Procyon v0.5.36
// 

package net.augustus.clickgui.screens;

import java.nio.file.attribute.FileAttribute;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Paths;
import java.io.File;
import java.util.Iterator;
import net.minecraft.client.renderer.GlStateManager;
import org.lwjgl.input.Mouse;
import net.minecraft.client.gui.Gui;
import java.awt.Color;
import java.io.IOException;
import net.minecraft.client.gui.GuiButton;
import net.augustus.ui.widgets.CustomButton;
import net.augustus.Augustus;
import net.minecraft.client.gui.ScaledResolution;
import java.util.ArrayList;
import net.augustus.ui.widgets.ConfigButton;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.gui.GuiScreen;

public class ConfigGui extends GuiScreen
{
    private GuiTextField createTextField;
    private final GuiScreen parent;
    private ConfigButton selected;
    private ArrayList<ConfigButton> configButtons;
    
    public ConfigGui(final GuiScreen parent) {
        this.configButtons = new ArrayList<ConfigButton>();
        this.parent = parent;
    }
    
    @Override
    public void initGui() {
        super.initGui();
        final ScaledResolution sr = new ScaledResolution(this.mc);
        (this.createTextField = new GuiTextField(1, this.fontRendererObj, sr.getScaledWidth() / 2 - 75, sr.getScaledHeight() - 130, 150, 20)).setMaxStringLength(1377);
        this.buttonList.add(new CustomButton(2, sr.getScaledWidth() / 2 - 50, sr.getScaledHeight() - 105, 100, 20, "Create", Augustus.getInstance().getClientColor()));
        this.buttonList.add(new CustomButton(3, sr.getScaledWidth() / 2 - 50, sr.getScaledHeight() - 80, 100, 20, "Load", Augustus.getInstance().getClientColor()));
        this.buttonList.add(new CustomButton(4, sr.getScaledWidth() / 2 - 50, sr.getScaledHeight() - 55, 100, 20, "Save", Augustus.getInstance().getClientColor()));
        this.buttonList.add(new CustomButton(5, sr.getScaledWidth() / 2 - 50, sr.getScaledHeight() - 30, 100, 20, "Delete", Augustus.getInstance().getClientColor()));
        this.initConfigs();
    }
    
    @Override
    protected void actionPerformed(final GuiButton button) throws IOException {
        if (button.id == 2) {
            this.createConfig();
        }
        else if (button.id == 3) {
            this.loadConfig();
        }
        else if (button.id == 4) {
            this.saveConfigs();
        }
        else if (button.id == 5) {
            this.deleteConfigs();
        }
        super.actionPerformed(button);
    }
    
    @Override
    public void drawScreen(final int mouseX, final int mouseY, final float partialTicks) {
        final ScaledResolution sr = new ScaledResolution(this.mc);
        Gui.drawRect(sr.getScaledWidth() / 2 - 150, 0, sr.getScaledWidth() / 2 + 150, sr.getScaledHeight(), new Color(0, 0, 0, 190).getRGB());
        boolean b = true;
        final float mdw = Mouse.getDWheel() / 10.0f;
        float y = 0.0f;
        if (this.configButtons.size() > 0) {
            if (this.configButtons.get(this.configButtons.size() - 1).yPosition + this.configButtons.get(this.configButtons.size() - 1).getHeight() + mdw < sr.getScaledHeight() - 140 && mdw < 0.0f) {
                b = false;
                y = (float)(55 + (sr.getScaledHeight() - 140 - 55 - this.configButtons.size() * 36) + 2);
            }
            else if (this.configButtons.get(0).yPosition + mdw > 55.0f && mdw > 0.0f) {
                b = false;
                y = 55.0f;
            }
            float yAdd = 0.0f;
            for (final ConfigButton configButton : this.configButtons) {
                if (this.configButtons.size() > 1 && this.configButtons.size() * 36 > sr.getScaledHeight() - 140 - 55) {
                    if (b) {
                        final ConfigButton configButton2 = configButton;
                        configButton2.yPosition += (int)mdw;
                    }
                    else {
                        configButton.yPosition = (int)(y + yAdd);
                    }
                }
                configButton.draw(this.mc, mouseX, mouseY);
                yAdd += 36.0f;
            }
        }
        Gui.drawRect(sr.getScaledWidth() / 2 - 150, 0, sr.getScaledWidth() / 2 + 150, 55, new Color(15, 15, 15, 255).getRGB());
        Gui.drawRect(sr.getScaledWidth() / 2 - 150, sr.getScaledHeight() - 140, sr.getScaledWidth() / 2 + 150, sr.getScaledHeight(), new Color(15, 15, 15, 255).getRGB());
        Gui.drawRect(sr.getScaledWidth() / 2 - 150, 55, sr.getScaledWidth() / 2 - 140, sr.getScaledHeight() - 140, new Color(15, 15, 15, 255).getRGB());
        Gui.drawRect(sr.getScaledWidth() / 2 + 140, 55, sr.getScaledWidth() / 2 + 150, sr.getScaledHeight() - 140, new Color(15, 15, 15, 255).getRGB());
        super.drawScreen(mouseX, mouseY, partialTicks);
        this.createTextField.drawTextBox();
        GlStateManager.pushMatrix();
        GlStateManager.scale(3.0f, 3.0f, 1.0f);
        this.fontRendererObj.drawStringWithShadow("Configs", sr.getScaledWidth() / 2.0f / 3.0f - this.fontRendererObj.getStringWidth("Configs") / 2.0f, 5.0f, Color.gray.getRGB());
        GlStateManager.scale(1.0f, 1.0f, 1.0f);
        GlStateManager.popMatrix();
    }
    
    @Override
    protected void keyTyped(final char typedChar, final int keyCode) throws IOException {
        this.createTextField.textboxKeyTyped(typedChar, keyCode);
        if (keyCode == 1) {
            this.mc.displayGuiScreen(this.parent);
        }
    }
    
    @Override
    protected void mouseClicked(final int mouseX, final int mouseY, final int mouseButton) throws IOException {
        this.createTextField.mouseClicked(mouseX, mouseY, mouseButton);
        super.mouseClicked(mouseX, mouseY, mouseButton);
        if (mouseButton == 1 || mouseButton == 0) {
            for (final ConfigButton configButton : this.configButtons) {
                if (this.mouseOver(mouseX, mouseY, configButton.xPosition, configButton.yPosition, configButton.getButtonWidth(), configButton.getHeight())) {
                    (this.selected = configButton).setSelected(true);
                }
                else {
                    configButton.setSelected(false);
                }
            }
        }
    }
    
    @Override
    public void updateScreen() {
        this.createTextField.updateCursorCounter();
        super.updateScreen();
    }
    
    private void deleteConfigs() {
        if (this.selected != null) {
            final File file = new File("augustus/configs/" + this.selected.displayString + ".json");
            file.delete();
        }
        this.initConfigs();
    }
    
    private void createConfig() {
        if (!this.createTextField.getText().equals("")) {
            Augustus.getInstance().getConverter().configSaver(this.createTextField.getText());
            this.createTextField.setText("");
        }
        this.initConfigs();
    }
    
    private void saveConfigs() {
        if (this.selected != null) {
            Augustus.getInstance().getConverter().configSaver(this.selected.displayString);
        }
    }
    
    private void loadConfig() {
        if (this.selected != null) {
            Augustus.getInstance().getConverter().configLoader(this.selected.displayString);
        }
    }
    
    private void initConfigs() {
        final ScaledResolution sr = new ScaledResolution(this.mc);
        this.configButtons = new ArrayList<ConfigButton>();
        if (!Files.exists(Paths.get("augustus/configs", new String[0]), new LinkOption[0])) {
            try {
                Files.createDirectories(Paths.get("augustus/configs", new String[0]), (FileAttribute<?>[])new FileAttribute[0]);
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
        final File f = new File("augustus/configs");
        final File[] fileArray = f.listFiles();
        int yAdd = 0;
        assert fileArray != null;
        for (final File file : fileArray) {
            if (file.getName().contains(".json")) {
                String name = "";
                for (int i = 0; i < file.getName().length() - 5; ++i) {
                    name += file.getName().charAt(i);
                }
                final String[] s = Augustus.getInstance().getConverter().configReader(name);
                this.configButtons.add(new ConfigButton(6, sr.getScaledWidth() / 2 - 140, 55 + yAdd, 280, 35, name, s[1], s[2], Color.gray));
                yAdd += 36;
            }
        }
    }
    
    public boolean mouseOver(final double mouseX, final double mouseY, final double posX, final double posY, final double width, final double height) {
        return mouseX >= posX && mouseX <= posX + width && mouseY >= posY && mouseY <= posY + height;
    }
}
