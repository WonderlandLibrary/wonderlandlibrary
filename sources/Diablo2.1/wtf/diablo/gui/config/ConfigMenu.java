package wtf.diablo.gui.config;

import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;
import wtf.diablo.Diablo;
import wtf.diablo.config.Config;
import wtf.diablo.gui.config.impl.ConfigButton;
import wtf.diablo.gui.config.impl.ConfigMenuNav;
import wtf.diablo.module.impl.Render.Hud;
import wtf.diablo.utils.chat.ChatUtil;
import wtf.diablo.utils.font.Fonts;
import wtf.diablo.utils.glstuff.BlurUtils;
import wtf.diablo.utils.render.ColorUtil;
import wtf.diablo.utils.render.RenderUtil;

import java.io.IOException;
import java.util.concurrent.CopyOnWriteArrayList;

public class ConfigMenu extends GuiScreen {

    public GuiScreen lastGUI;
    public CopyOnWriteArrayList<ConfigMenuNav> navButtons = new CopyOnWriteArrayList<>();
    public CopyOnWriteArrayList<ConfigButton> localConfigs = new CopyOnWriteArrayList<>();
    public CopyOnWriteArrayList<ConfigMenuNav> configSettings = new CopyOnWriteArrayList<>();
    public boolean keepKeybinds = false;
    private int selected = 0;
    public boolean focused = true;
    private float scrolled;
    private int anim = 0;
    private ConfigInputBox inputBox = new ConfigInputBox();
    private ConfigMenuNav confirmConfigSave = new ConfigMenuNav(20,62 * 2,20);
    private ConfigMenuNav backButton = new ConfigMenuNav(20,62 * 2,20);

    public ConfigMenu(GuiScreen lastGUI){
        this.lastGUI = lastGUI;
        reloadConfigs();
    }
    public void reloadConfigs(){
        navButtons.clear();
        configSettings.clear();
        localConfigs.clear();
        navButtons.add(new ConfigMenuNav(0,"Local Configs",140,20));
        navButtons.add(new ConfigMenuNav(1,"Online Configs",140,20));
        for(Config config : Diablo.configManager.getConfigs()){
            localConfigs.add(new ConfigButton(this, config, 290, 20));
        }
        configSettings.add(new ConfigMenuNav(10, 140, 18));
        configSettings.add(new ConfigMenuNav(11, 140, 18));
    }
    public void openGUI() {
        mc.displayGuiScreen(this);
    }
    double totalButtonHeight = 0;
    double x,y,width,height;
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        if(anim < 261){
            anim = anim + 10;
            System.out.println("" + anim);
        }


        width = 300;
        height = 200;
        x = getWidth() / 2 - width / 2;

        y = getHeight() / 2 - height / 2;
        Gui.drawRect(x, y - anim + 280, x + width, y + height - anim + 280, 0xFF131313);
        Gui.drawRect(x, y - anim + 280, x + width, y + 5 - anim + 280, ColorUtil.getColor((int) y));

        Gui.drawRect(x, y + 5- anim + 280, x + width, y + 30- anim + 280, 0xFF151515);
        int btn = 0;
        for (ConfigMenuNav button : navButtons) {
            switch (btn) {
                case 0:
                    button.drawButton(x + 5, y + 2.5 + 5 - anim + 280, mouseX, mouseY, selected == 0);
                    break;
                case 1:
                    button.drawButton(x + 150 + 5, y + 5 + 2.5 - anim + 280, mouseX, mouseY, selected == 1);
                    break;
            }
            btn++;
        }
        Gui.drawRect(x + 5, y + 30 - anim + 280, x + width - 5, y + height - 5 - anim + 280, 0xFF232323);
        GlStateManager.pushMatrix();
        GlStateManager.translate(0,scrolled,0);
        GL11.glEnable(GL11.GL_SCISSOR_TEST);
        RenderUtil.prepareScissorBox(x + 5, y + 30, x + width -5, y + height -25 - anim + 280);
        double tempButtonHeight = 0;
        int count = 0;
        if(selected == 0) {
            if(localConfigs.size() > 0) {
                for (ConfigButton configButton : localConfigs) {
                    tempButtonHeight += configButton.drawButton(x + 5, y + 30 + (count * 22) + ((count > 0) ? 1 : 0) - anim + 280, mouseX, (int) (mouseY - scrolled));
                    count++;
                }
            }
            else {
                Fonts.SFReg24.drawStringWithShadow("No Configs Found.", x + (width /2) - (Fonts.SFReg24.getStringWidth("No Configs Found.") / 2), y + height - 5 - (175/2) - (Fonts.SFReg24.getHeight() / 2) - anim + 280,-1);
            }
        }
        else {
            Fonts.SFReg24.drawStringWithShadow("Coming Soon...", x + (width /2) - (Fonts.SFReg24.getStringWidth("Coming Soon...") / 2), y + height - 5 - (175/2) - (Fonts.SFReg24.getHeight() / 2) - anim + 280,-1);
        }
        totalButtonHeight = tempButtonHeight;
        GL11.glDisable(GL11.GL_SCISSOR_TEST);
        GlStateManager.popMatrix();
        RenderUtil.drawGradientRect(x + 5, y + height - 30 - anim + 280, x + width - 5, y + height - 25 - anim + 280, 0xff131313,0x00131313);
        Gui.drawRect(x + 5, y + height - 25 - anim + 280, x + width - 5, y + height - 5 - anim + 280, 0xFF252525);

        btn = 0;
        for (ConfigMenuNav button : this.configSettings) {
            switch (btn) {
                case 0:
                    button.drawButton(x + 6, y + height - 24 + 0 - anim + 280, mouseX, mouseY, this.keepKeybinds ? "Disable Keybinds" : "Enable Keybinds",false);
                    break;
                case 1:
                    button.drawButton(x + 150 + 4, y + height - 24 - anim + 280, mouseX, mouseY, "Create Config",false);
                    break;
            }
            btn++;
        }
        if(!focused){
            Gui.drawRect(x,y - anim + 280, x + width, y + height - anim + 280, 0x80000000);
            Gui.drawRect(x + (width / 2) - 70, y + (height / 2) - 60 - anim + 280, x + (width /2) + 70, y + (height / 2) + 60 - anim + 280, 0xFF232323);
            Fonts.SFReg24.drawStringWithShadow("Create Config", x + (width / 2) - 62, y + (height / 2) - 52 - anim + 280, - 1);

            Fonts.SFReg18.drawStringWithShadow("Config Name", x + (width / 2) - 62, y + (height / 2) - 34 - anim + 280,-1);
            inputBox.drawScreen(mouseX,mouseY, x + (width / 2) - 62, y + (height / 2) - 25 - anim + 280) ;
            confirmConfigSave.drawButton(x + (width / 2) - 62, y + (height / 2) + 60 - 20 - 5 - 20 - 2 - anim + 280, mouseX,mouseY,"Confirm",false);
            backButton.drawButton(x + (width / 2) - 62, y + (height / 2) + 60 - 20 - 5 - anim + 280, mouseX,mouseY,"Back",false);

        }
        if(Hud.blurOn.getValue()){
            BlurUtils.drawWholeScreen();
        }
    }

    protected void keyTyped(char typedChar, int keyCode) throws IOException {
        if(keyCode == Keyboard.KEY_ESCAPE){
            mc.displayGuiScreen(lastGUI);
        }else
        super.keyTyped(typedChar, keyCode);
        inputBox.keyTyped(typedChar,keyCode);
    }

    public void onGuiClosed(){
        anim = 0;
    }

    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        if(!focused) {
            inputBox.mouseClicked(mouseX, mouseY, mouseButton);
            if(this.backButton.buttonClick(mouseX,mouseY,mouseButton)){
                focused = true;
                inputBox.focused = false;
            }
            if(this.confirmConfigSave.buttonClick(mouseX,mouseY,mouseButton)) {
                try {
                    if (inputBox.data.length() > 1) {
                        Diablo.configManager.saveConfig(inputBox.data);
                        focused = true;
                        inputBox.data = "";
                        reloadConfigs();
                    } else {
                        ChatUtil.log("Invalid Config Name. [-1]");
                    }
                }catch(Exception e){
                    ChatUtil.log("Invalid Config Name. [0]");
                }
            }
        }
        else {
            for (ConfigMenuNav button : navButtons) {
                if (button.buttonClick(mouseX, mouseY, mouseButton)) {
                    buttonClickEvent(button);
                }
            }
            if (selected == 0) {
                for (ConfigButton button : localConfigs) {
                    if (RenderUtil.isHovered(mouseX, mouseY, x + 5, y + 30, x + width - 5, y + height - 25))
                        button.buttonClick(mouseX, mouseY, mouseButton);
                }
            }
            for (ConfigMenuNav settingsBtns : this.configSettings) {
                if (settingsBtns.buttonClick(mouseX, mouseY, mouseButton)) {
                    switch (settingsBtns.buttonId) {
                        case 10:
                            this.keepKeybinds = !keepKeybinds;
                            break;
                        case 11:
                            focused = false;
                            break;
                    }
                }
            }
        }
    }

    private void buttonClickEvent(ConfigMenuNav button) {
        switch(button.buttonId){
            case 0:
                selected = 0;
                break;
            case 1:
                selected = 1;
                break;
        }
    }

    protected void mouseReleased(int mouseX, int mouseY, int state) {

    }

    public void handleMouseInput() throws IOException {
        super.handleMouseInput();
        int i = Mouse.getEventDWheel();
        if (i != 0)
        {
            if (i > 1)
            {
                i = 1;
            }

            if (i < -1)
            {
                i = -1;
            }

            if (!isShiftKeyDown())
            {
                i *= 7;
            }
            if(scrolled >= 0 && i >= 0)
                return;
            if(-scrolled >= totalButtonHeight - (32 * 3) && i <= 0)
                return;
            scrolled += i;
        }
    }
}
