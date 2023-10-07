// 
// Decompiled by Procyon v0.6.0
// 

package net.augustus.ui.altlogin;

import net.augustus.utils.sound.SoundUtil;
import net.minecraft.client.audio.ISound;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.util.ResourceLocation;
import net.augustus.ui.augustusmanager.AugustusSounds;
import java.util.List;
import net.augustus.Augustus;
import fr.litarvan.openauth.microsoft.MicrosoftAuthResult;
import fr.litarvan.openauth.microsoft.MicrosoftAuthenticator;
import net.minecraft.util.Session;
import java.awt.Color;
import net.augustus.utils.interfaces.MC;

public class LoginThread extends Thread implements MC
{
    private String username;
    private String password;
    private Color color;
    private String status;
    private boolean gen;
    
    public LoginThread(final String username, final String password, final boolean isGen) {
        super("Login Thread");
        this.color = Color.green;
        this.status = "";
        this.username = username;
        this.password = password;
        this.gen = isGen;
    }
    
    public Session loginSession(final String username, final String password) {
        try {
            final MicrosoftAuthenticator authenticator = new MicrosoftAuthenticator();
            final MicrosoftAuthResult result = authenticator.loginWithCredentials(username, password);
            return new Session(result.getProfile().getName(), result.getProfile().getId(), result.getAccessToken(), "microsoft");
        }
        catch (final Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    
    @Override
    public void run() {
        super.run();
        if (this.gen) {
            this.status = "Generated cracked alt - (" + this.username + ")";
            this.loginSuccessSound();
            LoginThread.mc.setSession(new Session(this.username, "", "", "mojang"));
            return;
        }
        if (this.password.equals("")) {
            if (this.username.contains(":")) {
                final String[] s = this.username.split(":");
                this.username = s[0];
                this.password = s[1];
            }
            else if (!this.username.equals("")) {
                this.status = "Logged in - (" + this.username + ") Cracked Login";
                this.loginSuccessSound();
                LoginThread.mc.setSession(new Session(this.username, "", "", "mojang"));
                return;
            }
        }
        try {
            this.status = "Logging in...";
            this.color = Color.green;
            final Session session = this.loginSession(this.username, this.password);
            if (session != null) {
                LoginThread.mc.setSession(session);
                this.status = "Logged in! (" + session.getUsername() + ")";
                this.color = Color.green;
                this.loginSuccessSound();
                final List<String> lastAlts = Augustus.getInstance().getLastAlts();
                if (!lastAlts.contains(this.username + ":" + this.password)) {
                    lastAlts.add(this.username + ":" + this.password);
                    Augustus.getInstance().setLastAlts(lastAlts);
                }
            }
            else {
                this.color = Color.red;
                this.status = "Login Failed!";
                this.loginFailedSound();
            }
        }
        catch (final Exception var3) {
            var3.printStackTrace();
        }
    }
    
    public Color getColor() {
        return this.color;
    }
    
    public String getStatus() {
        return this.status;
    }
    
    private void loginSuccessSound() {
        final String currentSound;
        final String var1 = currentSound = AugustusSounds.currentSound;
        switch (currentSound) {
            case "Vanilla": {
                LoginThread.mc.getSoundHandler().playSound(PositionedSoundRecord.create(new ResourceLocation("gui.button.press"), 1.0f));
                break;
            }
            case "Sigma": {
                SoundUtil.play(SoundUtil.loginSuccessful);
                break;
            }
        }
    }
    
    private void loginFailedSound() {
        final String currentSound;
        final String var1 = currentSound = AugustusSounds.currentSound;
        switch (currentSound) {
            case "Vanilla": {
                LoginThread.mc.getSoundHandler().playSound(PositionedSoundRecord.create(new ResourceLocation("gui.button.press"), 1.0f));
                break;
            }
            case "Sigma": {
                SoundUtil.play(SoundUtil.loginFailed);
                break;
            }
        }
    }
}
