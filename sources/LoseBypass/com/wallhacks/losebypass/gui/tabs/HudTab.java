/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.lwjgl.input.Mouse
 *  org.lwjgl.opengl.GL11
 */
package com.wallhacks.losebypass.gui.tabs;

import com.wallhacks.losebypass.LoseBypass;
import com.wallhacks.losebypass.gui.ClickGui;
import com.wallhacks.losebypass.gui.HudEditor;
import com.wallhacks.losebypass.gui.components.HudModuleComponent;
import com.wallhacks.losebypass.gui.components.TextEditComponent;
import com.wallhacks.losebypass.gui.components.settings.SettingComponent;
import com.wallhacks.losebypass.gui.tabs.ClickGuiTab;
import com.wallhacks.losebypass.manager.SystemManager;
import com.wallhacks.losebypass.systems.hud.HudComponent;
import com.wallhacks.losebypass.systems.hud.HudSettings;
import com.wallhacks.losebypass.systems.setting.Setting;
import com.wallhacks.losebypass.utils.Animation;
import com.wallhacks.losebypass.utils.GuiUtil;
import com.wallhacks.losebypass.utils.MC;
import com.wallhacks.losebypass.utils.MathUtil;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.function.Consumer;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

public class HudTab
extends ClickGuiTab {
    public static final HudSettings hudSettings = new HudSettings();
    public static HudEditor hudEditor = new HudEditor();
    private final Animation animation = new Animation(1.0f, 0.015f);
    double scroll = 0.0;
    double smoothScroll = 0.0;
    double height;
    ArrayList<SettingComponent> settings = new ArrayList();
    private ArrayList<HudModuleComponent> components = new ArrayList();
    Runnable submitText = new Runnable(){

        @Override
        public void run() {
            try {
                HudTab.hudSettings.fontSize.setValue(MathUtil.clamp(Integer.parseInt(HudTab.this.hudsize.getText()), (int)HudTab.hudSettings.fontSize.getMin(), (int)HudTab.hudSettings.fontSize.getMax()));
                LoseBypass.fontManager.setHudFont((Integer)HudTab.hudSettings.fontSize.getValue());
                return;
            }
            catch (NumberFormatException e) {
                HudTab.this.hudsize.setText(HudTab.hudSettings.fontSize.getValueString());
            }
        }
    };
    TextEditComponent hudsize;

    public HudTab() {
        super("Hud", new ResourceLocation("textures/icons/hud.png"));
        this.hudsize = new TextEditComponent(HudTab.hudSettings.fontSize.getValueString(), LoseBypass.fontManager.getThickFont(), this.submitText, true);
        this.hudsize.setOffset(5, 5);
        LoseBypass.fontManager.setHudFont((Integer)HudTab.hudSettings.fontSize.getValue());
        hudSettings.getSettings().forEach((Consumer<Setting<?>>)((Consumer<Setting>)setting -> this.settings.add(GuiUtil.getComponent(setting, true))));
        SystemManager.getHudComponents().forEach(component -> this.components.add(new HudModuleComponent((HudComponent)component)));
    }

    @Override
    public void keyTyped(char typed, int keycode) {
        this.hudsize.keyTyped(typed, keycode);
    }

    @Override
    public void drawTab(int mouseX, int mouseY, int click, int posX, int posY, double deltaTime) {
        GuiUtil.setup(ClickGui.background2());
        GuiUtil.corner(posX + 432, posY + 392, 8.0, 0, 90);
        GuiUtil.corner(posX + 432, posY + 8, 8.0, 90, 180);
        GL11.glVertex2i((int)(posX + 230), (int)posY);
        GL11.glVertex2i((int)(posX + 230), (int)(posY + 400));
        GuiUtil.finish();
        LoseBypass.fontManager.getThickFont().drawString("HudSettings", posX + 305, posY + 8, -1);
        GuiUtil.rounded(posX + 235, posY + 22, posX + 435, posY + 24, -1, 1);
        GuiUtil.rounded(posX + 235, posY + 30, posX + 435, posY + 50, ClickGui.background5(), 5);
        GuiUtil.drawRect(posX + 300, posY + 30, posX + 370, posY + 50, ClickGui.background3());
        if (mouseX > posX + 370 && mouseX < posX + 435 && mouseY > posY + 30 && mouseY < posY + 50) {
            GuiUtil.setup(ClickGui.background5());
        } else {
            GuiUtil.setup(ClickGui.background4());
        }
        GuiUtil.corner(posX + 430, posY + 45, 5.0, 0, 90);
        GuiUtil.corner(posX + 430, posY + 35, 5.0, 90, 180);
        GL11.glVertex2i((int)(posX + 370), (int)(posY + 30));
        GL11.glVertex2i((int)(posX + 370), (int)(posY + 50));
        GuiUtil.finish();
        LoseBypass.fontManager.drawString("FontSize:", posX + 249, posY + 37, -1);
        LoseBypass.fontManager.drawString("+", posX + 397, posY + 37, -1);
        this.hudsize.updatePosition(posX + 300, posY + 30, posX + 370, posY + 50);
        this.hudsize.drawString();
        if (click == 0) {
            this.hudsize.mouseClicked(mouseX, mouseY);
        }
        int offset = posY + 55;
        for (SettingComponent component : this.settings) {
            if (component.getSetting() == HudTab.hudSettings.backgroundMode) {
                LoseBypass.fontManager.drawString("Background", posX + 310, offset + 5, -1);
                GuiUtil.rounded(posX + 235, offset + 18, posX + 435, offset + 20, -1, 1);
                offset += 22;
            }
            if (!component.visible()) continue;
            offset += component.drawComponent(posX + 235, offset, deltaTime, click, mouseX, mouseY, false);
        }
        boolean hoveringHide = false;
        if (mouseX > posX + 235 && mouseX < posX + 435 && mouseY > posY + 375 && mouseY < posY + 395) {
            hoveringHide = true;
        }
        GuiUtil.rounded(posX + 235, posY + 375, posX + 435, posY + 395, hoveringHide ? ClickGui.mainColor2() : ClickGui.mainColor(), 5);
        LoseBypass.fontManager.getThickFont().drawString("Edit HUD", posX + 315, posY + 381, -1);
        if (click == 0 && hoveringHide) {
            SystemManager.editingHud = true;
            MC.mc.displayGuiScreen(hudEditor);
        }
        if (mouseX > posX && mouseY > posY && mouseY < posX + 230 && mouseX < posX + 440) {
            this.scroll = -((double)Mouse.getDWheel() * 0.3) + this.scroll;
        }
        this.scroll = Math.max(0.0, Math.min(this.scroll, this.height - 380.0));
        this.smoothScroll = MathUtil.lerp(this.smoothScroll, this.scroll, deltaTime * 0.02);
        this.animation.update(0.0f, deltaTime);
        this.height = -this.animation.value() * 50.0f;
        GL11.glEnable((int)3089);
        Iterator<HudModuleComponent> iterator = this.components.iterator();
        while (true) {
            if (!iterator.hasNext()) {
                GL11.glDisable((int)3089);
                return;
            }
            HudModuleComponent component = iterator.next();
            ClickGui.maxOffset = 380;
            ClickGui.minOffset = posY + 10;
            GuiUtil.glScissor(posX, ClickGui.minOffset, 440, ClickGui.maxOffset);
            this.height += (double)component.drawComponent(posX + 10, this.height + 10.0 + (double)posY, (int)this.smoothScroll, deltaTime, click, mouseX, mouseY);
        }
    }
}

