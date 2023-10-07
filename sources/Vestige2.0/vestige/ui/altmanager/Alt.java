package vestige.ui.altmanager;

import com.mojang.authlib.Agent;
import com.mojang.authlib.exceptions.AuthenticationException;
import com.mojang.authlib.yggdrasil.YggdrasilAuthenticationService;
import com.mojang.authlib.yggdrasil.YggdrasilUserAuthentication;
import fr.litarvan.openauth.microsoft.MicrosoftAuthResult;
import fr.litarvan.openauth.microsoft.MicrosoftAuthenticator;
import lombok.Data;
import net.minecraft.client.Minecraft;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.Session;
import vestige.Vestige;
import vestige.api.event.impl.EventAltSwitched;
import vestige.util.base.IMinecraft;

import java.io.Serializable;
import java.net.Proxy;

@Data
public class Alt implements Serializable, IMinecraft {

    private String email;
    private String password;
    private String username;
    private AltType altType;
    private Session session;

    public Alt(String email, String password, AltType altType) {
        this.email = email;
        this.password = password;
        this.altType = altType;
    }

    public void login() {
        Vestige.getInstance().getAltManager().setStatus("Logging in...");

        if (this.password.equals("")) {
            this.mc.session = new Session(this.email, "", "", "mojang");

            this.username = email;

            Vestige.getInstance().getAltManager().setStatus("Logged in : " + this.username + " : Offline session");
            return;
        }

        if(altType == AltType.MICROSOFT) {
            new Thread(() -> {
                Session session = createMicrosoftSession();

                if(session != null) {
                    username = session.getUsername();
                    mc.session = session;
                    Vestige.getInstance().getAltManager().setStatus("Logged into : " + username);
                } else {
                    Vestige.getInstance().getAltManager().setStatus("Login failed !");
                }
                
                EventAltSwitched event = new EventAltSwitched(this.username);
                Vestige.getInstance().getSessionInfoProcessor().onAltSwitched(event);
            }).start();
        }
    }

    private Session createMicrosoftSession() {
        try {
            MicrosoftAuthenticator authenticator = new MicrosoftAuthenticator();
            MicrosoftAuthResult result = null;

            result = authenticator.loginWithCredentials(email, password);
            return session = new Session(result.getProfile().getName(), result.getProfile().getId(), result.getAccessToken(), "legacy");
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
