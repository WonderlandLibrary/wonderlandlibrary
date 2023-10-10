package dev.rise.ui.alt;

import com.mojang.authlib.Agent;
import com.mojang.authlib.exceptions.AuthenticationException;
import com.mojang.authlib.yggdrasil.YggdrasilAuthenticationService;
import com.mojang.authlib.yggdrasil.YggdrasilUserAuthentication;
import dev.rise.Rise;
import dev.rise.util.alt.thealtening.SSLController;
import dev.rise.util.alt.thealtening.TheAlteningAuthentication;
import dev.rise.util.alt.thealtening.service.AlteningServiceType;
import fr.litarvan.openauth.microsoft.MicrosoftAuthResult;
import fr.litarvan.openauth.microsoft.MicrosoftAuthenticationException;
import fr.litarvan.openauth.microsoft.MicrosoftAuthenticator;
import fr.litarvan.openauth.microsoft.model.response.MinecraftProfile;
import net.minecraft.client.Minecraft;
import net.minecraft.util.Session;
import store.intent.intentguard.annotation.Exclude;
import store.intent.intentguard.annotation.Strategy;

import java.net.Proxy;

@Exclude({Strategy.NUMBER_OBFUSCATION, Strategy.FLOW_OBFUSCATION, Strategy.NAME_REMAPPING, Strategy.REFERENCE_OBFUSCATION, Strategy.DEBUG_STRIPPING})
public final class AltThread extends Thread {
    private final Minecraft mc = Minecraft.getMinecraft();

    private final SSLController ssl = new SSLController();
    private final TheAlteningAuthentication serviceSwitch = TheAlteningAuthentication.mojang();

    private final String username, password;
    private String status;

    public AltThread(final String username, final String password) {
        super("Alt Thread");
        this.username = username;
        this.password = password;
        this.status = "Waiting...";
    }

    public Session createSession(final String username, final String password) {
        Rise.lastLoggedAccount = username + ":" + password;
        final YggdrasilAuthenticationService service = new YggdrasilAuthenticationService(Proxy.NO_PROXY, "");
        final YggdrasilUserAuthentication auth = (YggdrasilUserAuthentication) service.createUserAuthentication(Agent.MINECRAFT);

        auth.setUsername(username);
        auth.setPassword(password);

        if (username.contains("@alt.com")) {
            if (this.serviceSwitch.getService() == AlteningServiceType.MOJANG) {
                this.ssl.disableCertificateValidation();
                this.serviceSwitch.updateService(AlteningServiceType.THEALTENING);
            }
        } else {
            if (this.serviceSwitch.getService() == AlteningServiceType.THEALTENING) {
                this.ssl.enableCertificateValidation();
                this.serviceSwitch.updateService(AlteningServiceType.MOJANG);
            }
        }

        if (Rise.INSTANCE.getAltGUI().microsoftAuthEnabled) {
            final MicrosoftAuthenticator authenticator = new MicrosoftAuthenticator();
            try {
                final MicrosoftAuthResult result = authenticator.loginWithCredentials(username, password);
                final MinecraftProfile profile = result.getProfile();
                return new Session(profile.getName(), profile.getId(), result.getAccessToken(), "microsoft");
            } catch (final MicrosoftAuthenticationException e) {
                e.printStackTrace();
                return null;
            }
        } else {
            try {
                auth.logIn();
                return new Session(auth.getSelectedProfile().getName(), auth.getSelectedProfile().getId().toString(), auth.getAuthenticatedToken(), "mojang");
            } catch (final AuthenticationException localAuthenticationException) {
                localAuthenticationException.printStackTrace();
                return null;
            }
        }

    }

    @Override
    public void run() {
        if (this.password.equals("")) {
            this.mc.session = new Session(this.username, "", "", "mojang");
            this.status = "Logged in offline as " + this.username + ".";
            return;
        }
        this.status = "Logging in...";
        final Session auth = createSession(this.username, this.password);
        if (auth == null) {
            this.status = "Login failed!";
        } else {
            this.status = "Logged in as " + auth.getProfile().getName() + ".";
            this.mc.session = auth;
        }

        AltGUI.generatingKingGen = false;
        AltGUI.generatingWithDrilledGen = false;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(final String status) {
        this.status = status;
    }
}