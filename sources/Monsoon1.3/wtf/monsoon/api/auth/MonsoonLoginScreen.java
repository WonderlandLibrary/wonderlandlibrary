package wtf.monsoon.api.auth;


import java.awt.Color;
import java.io.IOException;

import org.lwjgl.input.Keyboard;

import wtf.monsoon.Monsoon;
import wtf.monsoon.api.util.render.ColorUtil;
import wtf.monsoon.api.util.render.DrawUtil;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;

public final class MonsoonLoginScreen extends GuiScreen {
    private GuiScreen previousScreen;
    private Authentication thread;
    private GuiTextField username;
    private PasswordField password;

    public MonsoonLoginScreen() {
        //this.previousScreen = previousScreen;
    }

    @Override
    protected void actionPerformed(GuiButton button) {
        switch (button.id) {
            case 0: {
                this.thread = new Authentication(this.username.getText(), this.password.getText());
                this.thread.start();
            }
        }
    }

    @Override
    public void drawScreen(int x2, int y2, float z2) {
        Gui.drawRect(0,0,this.width, this.height, new Color(54, 57, 63).getRGB());


        Gui.drawRect(this.width / 2 - 150, this.height / 2 - 100,this.width / 2 + 150, this.height / 2 + 100, new Color(30, 30, 30).getRGB());
        DrawUtil.drawHollowRect(this.width / 2 - 150, this.height / 2 - 100,300, 200, ColorUtil.colorLerpv2(new Color(0,140,255), new Color(0,255,255), 0.5f).getRGB());
        DrawUtil.draw2DImage(new ResourceLocation("monsoon/monsoon.png"), this.width / 2 - 1, this.height / 2 - 65, 150, 150, Color.white);

        Monsoon.INSTANCE.getLargeFont().drawString("Monsoon Login",this.width / 2 - 145, this.height / 2 - 93,-1);

        GlStateManager.pushMatrix();
        GlStateManager.scale(0.9, 0.9, 0.9);
        Monsoon.INSTANCE.getFont().drawString("Version: " + Monsoon.INSTANCE.version, 2, 2, -1);
        Monsoon.INSTANCE.getFont().drawString("Build Type: " + Monsoon.INSTANCE.type, 2, 2 + Monsoon.INSTANCE.getFont().getHeight() + 2, -1);
        Monsoon.INSTANCE.getFont().drawString("Modules: " + Monsoon.INSTANCE.manager.modules.size(), 2, 2 + (Monsoon.INSTANCE.getFont().getHeight() * 2) + 2, -1);
        //Monsoon.INSTANCE.getFont().drawString("Commands: " + Monsoon.commandManager.commands.size(), 2, 2 + (Monsoon.getFont().getHeight() * 3) + 2, -1);
        Monsoon.INSTANCE.getFont().drawString("Made with \u2764 by the Monsoon team", 2, 2 + (Monsoon.INSTANCE.getFont().getHeight() * 4) + 2, -1);
        GlStateManager.popMatrix();

        this.username.drawTextBox();
        this.password.drawTextBox();
        super.drawScreen(x2, y2, z2);
    }

    @Override
    public void initGui() {
        int var3 = height / 4 + 24;
        this.buttonList.add(new GuiButton(0, width / 2 - 145, this.height / 2 + 25, 150, 20, "Login"));
        this.username = new GuiTextField(var3, this.mc.fontRendererObj, width / 2 - 145, this.height / 2 - 25, 150, 20);
        this.password = new PasswordField(this.mc.fontRendererObj, width / 2 - 145, this.height / 2 , 150, 20);
        this.username.setFocused(true);
        Keyboard.enableRepeatEvents(true);
    }

    @Override
    protected void keyTyped(char character, int key) {
        try {
            super.keyTyped(character, key);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        if (character == '\t') {
            if (!this.username.isFocused() && !this.password.isFocused()) {
                this.username.setFocused(true);
            } else {
                this.username.setFocused(this.password.isFocused());
                this.password.setFocused(!this.username.isFocused());
            }
        }
        if (character == '\r') {
            this.actionPerformed((GuiButton)this.buttonList.get(0));
        }
        this.username.textboxKeyTyped(character, key);
        this.password.textboxKeyTyped(character, key);
    }

    @Override
    protected void mouseClicked(int x2, int y2, int button) {
        try {
            super.mouseClicked(x2, y2, button);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        this.username.mouseClicked(x2, y2, button);
        this.password.mouseClicked(x2, y2, button);
    }

    @Override
    public void onGuiClosed() {
        Keyboard.enableRepeatEvents(false);
    }

    @Override
    public void updateScreen() {
        this.username.updateCursorCounter();
        this.password.updateCursorCounter();
    }
}

