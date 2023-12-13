package wtf.diablo.gui.clickGuiAlternate.impl.configs;

import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import wtf.diablo.Diablo;
import wtf.diablo.config.Config;
import wtf.diablo.config.ConfigManager;
import wtf.diablo.gui.clickGui.impl.configs.ConfigSelect;
import wtf.diablo.utils.font.Fonts;
import wtf.diablo.utils.render.RenderUtil;

import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.CopyOnWriteArrayList;

public class ConfigTab {

    public ArrayList<ConfigSelect> buttons = new ArrayList<>();
    public CopyOnWriteArrayList<ConfigButton> configButtons = new CopyOnWriteArrayList<>();

    public double x, y, offsetX, offsetY;
    public boolean dragging, collapsed;
    public int width = 100, barHeight = 15;
    private double totalHeight;
    private double scrolled = 0;
    private double fixedHeight = 150;
    public Color color;

    public ConfigTab(int x) {
        this.x = x;
        this.y = 5;
        this.color = new Color(178,89,132);
        update();
    }

    public void update() {
        configButtons.clear();
        buttons.clear();
        int count = 0;
        for (Config config : Diablo.configManager.getConfigs()) {
            buttons.add(new ConfigSelect(this, config, count));
            count++;
        }
        int c2 = 0;
        configButtons.add(new ConfigButton("Load",c2,this,count));
        c2++;
        configButtons.add(new ConfigButton("Save",c2,this,count));
        c2++;
        configButtons.add(new ConfigButton("Delete",c2,this,count));
    }
    public double settingsHeight = 0;

    public void drawScreen(int mouseX, int mouseY, float partialTicks){
        if (dragging) {
            x = mouseX + offsetX;
            y = mouseY + offsetY;
        }
        int mainColor = this.color.getRGB();

        Gui.drawRect(x, y, x + width,y + barHeight + fixedHeight,mainColor);
        if(fixedHeight > 1)
            RenderUtil.drawRoundedRect(x,y + barHeight,width,fixedHeight,5, 0xFF131313);

        Fonts.SFReg18.drawStringWithShadow("Configs", x + 17, y + 0.5 + ((barHeight - Fonts.SFReg18.getHeight()) / 2f), -1);
        settingsHeight = 0;

        fixedHeight = 0;
        if(!collapsed) {
            for (ConfigSelect b : buttons) {
                settingsHeight += b.drawScreen(mouseX, mouseY, partialTicks, settingsHeight);
            }
            for(ConfigButton but : configButtons) {
                settingsHeight += but.drawScreen(mouseX,mouseY,partialTicks,settingsHeight);
            }
        }
        if(!collapsed) {
            RenderUtil.drawOutlinedRoundedRect(x, y, width, settingsHeight + barHeight, 5, 4, mainColor);
            GlStateManager.pushMatrix();
            // if(fixedHeight > 1)
            RenderUtil.drawOutlinedRoundedRect(x + 1, y + barHeight - 0.5, width - 2, settingsHeight , 5, 3, 0xFF333333);
            GlStateManager.popMatrix();
        }
        totalHeight = settingsHeight;
    }

    public void keyTyped(char typedChar, int keyCode) throws IOException {
        for (ConfigSelect b : buttons) {
            b.keyTyped(typedChar, keyCode);
        }
    }

    public void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        if (RenderUtil.isHovered(mouseX, mouseY, x, y, x + width, y + barHeight)) {
            if (mouseButton == 0) {
                dragging = true;
                offsetX = x - mouseX;
                offsetY = y - mouseY;
            } else if (mouseButton == 1) {
                collapsed = !collapsed;
            }
        }
        if (!collapsed) {
            String selected = null;
            for (ConfigSelect b : buttons) { if(b.selected) selected = b.config.getName(); b.mouseClicked(mouseX, mouseY, mouseButton); }
            for (ConfigButton b : configButtons) {
                if (b.mouseClicked(mouseX, mouseY, mouseButton)) {
                    if (selected != null) {
                        buttonClick(b.num, selected);
                    }
                }
            }
        }
    }

    private void buttonClick(int num, String configName) {
        switch (num) {
            case 0:
                Diablo.configManager.loadConfig(configName,false);
                break;
            case 1:
                Diablo.configManager.saveConfig(configName);
                break;
        }
        update();
    }

    public void mouseReleased(int mouseX, int mouseY, int state)
    {
        dragging = false;
        offsetY = 0;
        offsetX = 0;
        for(ConfigSelect b : buttons){
            b.mouseReleased(mouseX, mouseY, state);
        }
    }

    public void scroll(int i, int lastMouseX, int lastMouseY) {
        if(scrolled >= 0 && i >= 0)
            return;
        if(-scrolled >= totalHeight - fixedHeight / 2 && i <= 0)
            return;
        if(RenderUtil.isHovered(lastMouseX,lastMouseY, x,y,x + width,y + fixedHeight + barHeight)) {
            scrolled += i;
        }
    }

    public void onOpenGUI() {
        if(!collapsed) {
            fixedHeight = 0;
        }
        for(ConfigSelect button : buttons){
            button.onOpenGUI();
        }
    }
}
