package tech.atani.client.feature.guis.screens.mainmenu.atani.guis;

import java.io.IOException;
import java.net.Proxy;

import org.lwjgl.input.Keyboard;

import com.mojang.authlib.Agent;
import com.mojang.authlib.exceptions.AuthenticationException;
import com.mojang.authlib.yggdrasil.YggdrasilAuthenticationService;
import com.mojang.authlib.yggdrasil.YggdrasilUserAuthentication;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import tech.atani.client.feature.account.Account;
import tech.atani.client.feature.account.storage.AccountStorage;

public class AtaniAddAlt extends GuiScreen
{
    private final AtaniAltManager manager;
    private GuiPasswordField password;
    private String status;
    private GuiTextField username;
    private GuiTextField combined;
    protected static Minecraft mc = Minecraft.getMinecraft();
    
    public AtaniAddAlt(final AtaniAltManager manager) {
        this.status = "\u00A7eWaiting...";
        this.manager = manager;
    }
    
    @Override
    protected void actionPerformed(final GuiButton button) {
        switch (button.id) {
            case 0: {
                AddAltThread login;
                if (this.combined.getText().isEmpty()) {
                    login = new AddAltThread(this.username.getText(), this.password.getText());
                }
                else if (!this.combined.getText().isEmpty() && this.combined.getText().contains(":")) {
                    final String u = this.combined.getText().split(":")[0];
                    final String p = this.combined.getText().split(":")[1];
                    login = new AddAltThread(u.replaceAll(" ", ""), p.replaceAll(" ", ""));
                }
                else {
                    login = new AddAltThread(this.username.getText(), this.password.getText());
                }
                login.start();
                break;
            }
            case 1: {
                mc.displayGuiScreen(this.manager);
                break;
            }
        }
    }
    
    @Override
    public void drawScreen(final int i, final int j, final float f) {
        this.drawDefaultBackground();
        mc.fontRendererObj.drawCenteredString("Add Alt", this.width / 2, 20, -1);
        this.username.drawTextBox();
        this.password.drawTextBox();
        this.combined.drawTextBox();
        if (this.username.getText().isEmpty()) {
            mc.fontRendererObj.drawStringWithShadow("Username / E-Mail", (float)(this.width / 2 - 96), 66.0f, -7829368);
        }
        if (this.password.getText().isEmpty()) {
            mc.fontRendererObj.drawStringWithShadow("Password", (float)(this.width / 2 - 96), 106.0f, -7829368);
        }
        if (this.combined.getText().isEmpty()) {
            mc.fontRendererObj.drawStringWithShadow("Email:Password", (float)(this.width / 2 - 96), 146.0f, -7829368);
        }
        mc.fontRendererObj.drawCenteredString(this.status, this.width / 2, 30, -1);
        super.drawScreen(i, j, f);
    }
    
    @Override
    public void initGui() {
        Keyboard.enableRepeatEvents(true);
        this.buttonList.clear();
        this.buttonList.add(new GuiButton(0, this.width / 2 - 100, this.height / 4 + 92 + 12, "Login"));
        this.buttonList.add(new GuiButton(1, this.width / 2 - 100, this.height / 4 + 116 + 12, "Back"));
        this.username = new GuiTextField(1, mc.fontRendererObj, this.width / 2 - 100, 60, 200, 20);
        this.password = new GuiPasswordField(mc.fontRendererObj, this.width / 2 - 100, 100, 200, 20);
        (this.combined = new GuiTextField(this.eventButton, mc.fontRendererObj, this.width / 2 - 100, 140, 200, 20)).setMaxStringLength(200);
    }
    
    @Override
    protected void keyTyped(final char par1, final int par2) {
        this.username.textboxKeyTyped(par1, par2);
        this.password.textboxKeyTyped(par1, par2);
        this.combined.textboxKeyTyped(par1, par2);
        if (par1 == '\t' && (this.username.isFocused() || this.combined.isFocused() || this.password.isFocused())) {
            this.username.setFocused(!this.username.isFocused());
            this.password.setFocused(!this.password.isFocused());
            this.combined.setFocused(!this.combined.isFocused());
        }
        if (par1 == '\r') {
            this.actionPerformed(this.buttonList.get(0));
        }
    }
    
    @Override
    protected void mouseClicked(final int par1, final int par2, final int par3) {
        try {
            super.mouseClicked(par1, par2, par3);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        this.username.mouseClicked(par1, par2, par3);
        this.password.mouseClicked(par1, par2, par3);
        this.combined.mouseClicked(par1, par2, par3);
    }
    
    static /* synthetic */ void access$0(final AtaniAddAlt guiAddAlt, final String status) {
        guiAddAlt.status = status;
    }
    
    private class AddAltThread extends Thread
    {
        private final String password;
        private final String username;
        
        public AddAltThread(final String username, final String password) {
            this.username = username;
            this.password = password;
            AtaniAddAlt.access$0(AtaniAddAlt.this, "\u00A77Waiting...");
        }
        
        private final void checkAndAddAlt(final String username, final String password) {
            final YggdrasilAuthenticationService service = new YggdrasilAuthenticationService(Proxy.NO_PROXY, "");
            final YggdrasilUserAuthentication auth = (YggdrasilUserAuthentication)service.createUserAuthentication(Agent.MINECRAFT);
            auth.setUsername(username);
            auth.setPassword(password);
            try {
                auth.logIn();
                AccountStorage.getInstance().add(new Account(username, password, password == "" || password == null));
                AtaniAddAlt.access$0(AtaniAddAlt.this, "\u00A7aAlt added. (" + username + ")");
            }
            catch (AuthenticationException e) {
                AtaniAddAlt.access$0(AtaniAddAlt.this, "\u00A7cAlt failed!");
                e.printStackTrace();
            }
        }
        
        @Override
        public void run() {
            if (this.password.equals("")) {
                AccountStorage.getInstance().add(new Account(username, "", true));
                //ConfigManager.saveAlts();
                AtaniAddAlt.access$0(AtaniAddAlt.this, "\u00A7aAlt added. (" + this.username + " - offline name)");
                return;
            }
            AtaniAddAlt.access$0(AtaniAddAlt.this, "\u00A7eTrying alt...");
            this.checkAndAddAlt(this.username, this.password);
        }
    }
}
