package com.enjoytheban.ui.login;

import java.io.IOException;

import org.lwjgl.input.Keyboard;

import com.enjoytheban.utils.Helper;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.util.ResourceLocation;

public class GuiAltLogin
        extends GuiScreen {
    private GuiPasswordField password;
    private final GuiScreen previousScreen;
    private AltLoginThread thread;
    private GuiTextField username, combined;
    public GuiAltLogin(GuiScreen previousScreen) {
        this.previousScreen = previousScreen;
    }

    protected void actionPerformed(GuiButton button) {
        switch (button.id) {
        case 1: 
            mc.displayGuiScreen(previousScreen);
            break;
          case 0:
              if (combined.getText().isEmpty())
              thread = new AltLoginThread(username.getText(), password.getText());
              else if (!combined.getText().isEmpty() && combined.getText().contains(":")) {
                  String u = combined.getText().split(":")[0];
                  String p = combined.getText().split(":")[1];
                  thread = new AltLoginThread(u.replaceAll(" ", ""), p.replaceAll(" ", ""));
              } else
                  thread = new AltLoginThread(username.getText(), password.getText());
              thread.start();
          }
        }

    public void drawScreen(int x, int y, float z) {
        drawDefaultBackground();
        this.username.drawTextBox();
        this.password.drawTextBox();
        this.combined.drawTextBox();
        Helper.mc.fontRendererObj.drawCenteredString("Alt Login", this.width / 2, 20,
                -1);
        Helper.mc.fontRendererObj.drawCenteredString(this.thread == null ? "§eWaiting..." :
                this.thread.getStatus(), this.width / 2, 29, -1);
        if (this.username.getText().isEmpty()) {
        	Helper.mc.fontRendererObj.drawStringWithShadow("Username / E-Mail", this.width / 2 - 96,
                    66, -7829368);
        }
        if (this.password.getText().isEmpty()) {
            Helper.mc.fontRendererObj.drawStringWithShadow("Password", this.width / 2 - 96, 106,
                    -7829368);
        }
        if (combined.getText().isEmpty()) {
        	Helper.mc.fontRendererObj.drawStringWithShadow("Email:Password", width / 2 - 96, 146, -7829368);
          }
        super.drawScreen(x, y, z);
    }

    public void initGui() {
        int var3 = this.height / 4 + 24;
        this.buttonList.add(new GuiButton(0, this.width / 2 - 100, var3 + 72 + 12, "Login"));
        this.buttonList.add(new GuiButton(1, this.width / 2 - 100, var3 + 72 + 12 + 24, "Back"));
        this.username = new GuiTextField(1, this.mc.fontRendererObj, this.width / 2 - 100, 60, 200, 20);
        this.password = new GuiPasswordField(this.mc.fontRendererObj, this.width / 2 - 100, 100, 200, 20);
        this.combined = new GuiTextField(var3, mc.fontRendererObj, width / 2 - 100, 140, 200, 20);
        this.username.setFocused(true);
        username.setMaxStringLength(200);
        password.func_146203_f(200);
        combined.setMaxStringLength(200);
        Keyboard.enableRepeatEvents(true);
    }

    protected void keyTyped(char character, int key)
    {
      try
      {
        super.keyTyped(character, key);
      }
      catch (IOException e)
      {
        e.printStackTrace();
      }
        if ((character == '\t') && ((username.isFocused()) || (combined.isFocused())|| (password.isFocused())))
        {
            username.setFocused(!username.isFocused());
            password.setFocused(!password.isFocused());
            combined.setFocused(!combined.isFocused());
        }
      if (character == '\r') {
        actionPerformed((GuiButton)buttonList.get(0));
      }
      username.textboxKeyTyped(character, key);
      password.textboxKeyTyped(character, key);
      combined.textboxKeyTyped(character, key);
    }

    protected void mouseClicked(int x, int y, int button) {
        try {
            super.mouseClicked(x, y, button);
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.username.mouseClicked(x, y, button);
        this.password.mouseClicked(x, y, button);
        this.combined.mouseClicked(x, y, button);
    }

    public void onGuiClosed() {
        Keyboard.enableRepeatEvents(false);
    }

    public void updateScreen() {
        this.username.updateCursorCounter();
        this.password.updateCursorCounter();
        this.combined.updateCursorCounter();
    }
}
