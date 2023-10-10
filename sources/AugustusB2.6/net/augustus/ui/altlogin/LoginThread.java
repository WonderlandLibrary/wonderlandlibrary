// 
// Decompiled by Procyon v0.5.36
// 

package net.augustus.ui.altlogin;

import net.mcleaks.MCLeaks;
import net.mcleaks.RedeemResponse;
import net.mcleaks.Callback;
import net.augustus.utils.sound.SoundUtil;
import net.minecraft.client.audio.ISound;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.util.ResourceLocation;
import net.augustus.ui.augustusmanager.AugustusSounds;
import java.util.List;
import net.augustus.Augustus;
import com.thealtening.auth.service.AlteningServiceType;
import com.thealtening.api.TheAltening;
import com.mojang.authlib.exceptions.AuthenticationException;
import com.mojang.authlib.Agent;
import com.mojang.authlib.yggdrasil.YggdrasilUserAuthentication;
import com.mojang.authlib.yggdrasil.YggdrasilAuthenticationService;
import java.net.Proxy;
import net.minecraft.util.Session;
import com.thealtening.api.retriever.AsynchronousDataRetriever;
import com.thealtening.api.retriever.BasicDataRetriever;
import com.thealtening.auth.TheAlteningAuthentication;
import java.awt.Color;
import net.augustus.utils.interfaces.MC;

public class LoginThread extends Thread implements MC
{
    private String username;
    private String password;
    private Color color;
    private String status;
    private final TheAlteningAuthentication alteningAuthentication;
    private BasicDataRetriever basicDataRetriever;
    private AsynchronousDataRetriever asynchronousDataRetriever;
    
    public LoginThread(final String username, final String password) {
        super("Login Thread");
        this.color = Color.green;
        this.status = "";
        this.username = username;
        this.password = password;
        this.alteningAuthentication = TheAlteningAuthentication.mojang();
    }
    
    public Session loginSession(final String username, final String password) {
        final YggdrasilAuthenticationService service = new YggdrasilAuthenticationService(Proxy.NO_PROXY, "");
        final YggdrasilUserAuthentication auth = (YggdrasilUserAuthentication)service.createUserAuthentication(Agent.MINECRAFT);
        auth.setUsername(username);
        auth.setPassword(password);
        try {
            auth.logIn();
            return new Session(auth.getSelectedProfile().getName(), auth.getSelectedProfile().getId().toString(), auth.getAuthenticatedToken(), "mojang");
        }
        catch (AuthenticationException localAuthenticationException) {
            localAuthenticationException.printStackTrace();
            return null;
        }
    }
    
    public String generateName(final String apiKey) {
        this.basicDataRetriever = TheAltening.newBasicRetriever(apiKey);
        this.asynchronousDataRetriever = this.basicDataRetriever.toAsync();
        return this.basicDataRetriever.getAccount().getToken();
    }
    
    @Override
    public void run() {
        super.run();
        this.alteningAuthentication.updateService(AlteningServiceType.MOJANG);
        if (this.username.contains("@alt.com")) {
            this.alteningAuthentication.updateService(AlteningServiceType.THEALTENING);
            this.password = "asdf1337";
        }
        else if (this.password.equals("")) {
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
        else if (this.username.contains("api-")) {
            this.alteningAuthentication.updateService(AlteningServiceType.THEALTENING);
            this.username = this.generateName(this.username);
            this.password = "asdf1337";
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
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public Color getColor() {
        return this.color;
    }
    
    public String getStatus() {
        return this.status;
    }
    
    private void loginSuccessSound() {
        final String currentSound = AugustusSounds.currentSound;
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
        final String currentSound = AugustusSounds.currentSound;
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
    
    final class test implements Callback<Object>
    {
        @Override
        public final void done(final Object it) {
            if (it instanceof String) {
                return;
            }
            if (it == null) {
                System.err.println("null cannot be cast to non-null type net.mcleaks.RedeemResponse");
            }
            final RedeemResponse redeemResponse = (RedeemResponse)it;
            MCLeaks.refresh(new net.mcleaks.Session(redeemResponse.getUsername(), redeemResponse.getToken()));
            try {
                LoginThread.this.alteningAuthentication.updateService(AlteningServiceType.MOJANG);
            }
            catch (Exception ex) {}
        }
    }
}
