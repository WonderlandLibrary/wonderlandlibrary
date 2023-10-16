package com.enjoytheban.ui.login;

import java.io.IOException;
import java.net.Proxy;

import org.lwjgl.input.Keyboard;

import com.enjoytheban.Client;
import com.enjoytheban.management.FileManager;
import com.enjoytheban.utils.Helper;
import com.mojang.authlib.Agent;
import com.mojang.authlib.exceptions.AuthenticationException;
import com.mojang.authlib.yggdrasil.YggdrasilAuthenticationService;
import com.mojang.authlib.yggdrasil.YggdrasilUserAuthentication;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.util.ResourceLocation;

public class GuiAddAlt
        extends GuiScreen
{
    private final GuiAltManager manager;
    private GuiPasswordField password;

    private class AddAltThread
            extends Thread
    {
        private final String password;
        private final String username;

        
        public AddAltThread(String username, String password)
        {
            this.username = username;
            this.password = password;
            GuiAddAlt.this.status = "\2477Waiting...";
        }

        private final void checkAndAddAlt(String username, String password)
        {
            YggdrasilAuthenticationService service = new YggdrasilAuthenticationService(
                    Proxy.NO_PROXY, "");
            YggdrasilUserAuthentication auth = (YggdrasilUserAuthentication)service
                    .createUserAuthentication(Agent.MINECRAFT);
            auth.setUsername(username);
            auth.setPassword(password);
            try
            {
                auth.logIn();
                Client.instance.getAltManager().getAlts()
                        .add(new Alt(username, password));
                FileManager.saveAlts();
                GuiAddAlt.this.status = ("\247aAlt added. (" + username + ")");
            }
            catch (AuthenticationException e)
            {
                GuiAddAlt.this.status = "\247cAlt failed!";
                e.printStackTrace();
            }
        }

        public void run()
        {
            if (this.password.equals(""))
            {
                Client.instance.getAltManager().getAlts().add(new Alt(this.username, ""));
                FileManager.saveAlts();
                GuiAddAlt.this.status = ("\247aAlt added. (" + this.username + " - offline name)");
                return;
            }
            GuiAddAlt.this.status = "\247eTrying alt...";
            checkAndAddAlt(this.username, this.password);
        }
    }

    private String status = "\247eWaiting...";
    private GuiTextField username;
    private GuiTextField combined;
    
    public GuiAddAlt(GuiAltManager manager)
    {
        this.manager = manager;
    }

    protected void actionPerformed(GuiButton button)
    {
        switch (button.id)
        {
            case 0:
                AddAltThread login; if (combined.getText().isEmpty())
                    login = new AddAltThread(username.getText(), password.getText());
                else if (!combined.getText().isEmpty() && combined.getText().contains(":")) {
                    String u = combined.getText().split(":")[0];
                    String p = combined.getText().split(":")[1];
                    login = new AddAltThread(u.replaceAll(" ", ""), p.replaceAll(" ", ""));
                   
                } else
                    login = new AddAltThread(username.getText(), password.getText());

                login.start();
                break;
            case 1:
                mc.displayGuiScreen(manager);
        }
    }

    public void drawScreen(int i, int j, float f)
    {
        drawDefaultBackground();


        Helper.mc.fontRendererObj.drawCenteredString("Add Alt", this.width / 2, 20,
                -1);
        this.username.drawTextBox();
        this.password.drawTextBox();
        combined.drawTextBox();
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
        Helper.mc.fontRendererObj.drawCenteredString(this.status, this.width / 2, 30, -1);
 
        super.drawScreen(i, j, f);
    }

    public void initGui()
    {
        Keyboard.enableRepeatEvents(true);
        this.buttonList.clear();
        this.buttonList.add(new GuiButton(0, this.width / 2 - 100,
                this.height / 4 + 92 + 12, "Login"));
        this.buttonList.add(new GuiButton(1, this.width / 2 - 100,
                this.height / 4 + 116 + 12, "Back"));
        this.username = new GuiTextField(1, this.mc.fontRendererObj, this.width / 2 - 100, 60, 200,
                20);
        this.password = new GuiPasswordField(this.mc.fontRendererObj, this.width / 2 - 100, 100,
                200, 20);
        combined = new GuiTextField(eventButton, mc.fontRendererObj, width / 2 - 100, 140, 200, 20);
        combined.setMaxStringLength(200);
    }

    protected void keyTyped(char par1, int par2)
    {
    	 username.textboxKeyTyped(par1, par2);
    	    password.textboxKeyTyped(par1, par2);
    	    combined.textboxKeyTyped(par1, par2);
    	    if ((par1 == '\t') && ((username.isFocused()) || (combined.isFocused())|| (password.isFocused())))
    	    {
    	      username.setFocused(!username.isFocused());
    	      password.setFocused(!password.isFocused());
    	      combined.setFocused(!combined.isFocused());
    	    }
    	    if (par1 == '\r') {
    	      actionPerformed((GuiButton)buttonList.get(0));
    	    }
    	  }

    protected void mouseClicked(int par1, int par2, int par3)
    {
        try
        {
            super.mouseClicked(par1, par2, par3);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        this.username.mouseClicked(par1, par2, par3);
        this.password.mouseClicked(par1, par2, par3);
        combined.mouseClicked(par1, par2, par3);
    }
}
