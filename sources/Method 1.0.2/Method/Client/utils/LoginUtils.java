package Method.Client.utils;

import Method.Client.utils.system.Wrapper;
import com.mojang.authlib.Agent;
import com.mojang.authlib.exceptions.AuthenticationException;
import com.mojang.authlib.exceptions.AuthenticationUnavailableException;
import com.mojang.authlib.yggdrasil.YggdrasilAuthenticationService;
import com.mojang.authlib.yggdrasil.YggdrasilUserAuthentication;
import net.minecraft.client.Minecraft;
import net.minecraft.util.Session;

import java.net.Proxy;

public class LoginUtils {

    public static String loginAlt(String email, String password) {
        YggdrasilAuthenticationService authenticationService = new YggdrasilAuthenticationService(Proxy.NO_PROXY, "");
        YggdrasilUserAuthentication authentication = (YggdrasilUserAuthentication) authenticationService.createUserAuthentication(Agent.MINECRAFT);
        authentication.setUsername(email);
        authentication.setPassword(password);
        String displayText = "Logged [License]: " + Wrapper.INSTANCE.mc().getSession().getUsername();

        try {
            authentication.logIn();
            Minecraft.getMinecraft().session = new Session(authentication.getSelectedProfile().getName(), authentication.getSelectedProfile().getId().toString(), authentication.getAuthenticatedToken(), "mojang");

        } catch (AuthenticationUnavailableException e) {
            displayText = "Cannot contact authentication server!";
        } catch (AuthenticationException e) {
            if (e.getMessage().contains("Invalid username or password.") || e.getMessage().toLowerCase().contains("account migrated")) {
                displayText = "Wrong password!";
            } else {
                displayText = "Cannot contact authentication server!";
            }
        } catch (NullPointerException e) {
            displayText = "Wrong password!";
        }

        return displayText;
    }

    public static String getName(String email, String password) {
        YggdrasilAuthenticationService authenticationService = new YggdrasilAuthenticationService(Proxy.NO_PROXY, "");
        YggdrasilUserAuthentication authentication = (YggdrasilUserAuthentication) authenticationService.createUserAuthentication(Agent.MINECRAFT);
        authentication.setUsername(email);
        authentication.setPassword(password);
        try {
            authentication.logIn();
            return authentication.getSelectedProfile().getName();
        } catch (Exception e) {
            return null;
        }
    }

    public static void changeCrackedName(String name) {
        Minecraft.getMinecraft().session = new Session(name, "", "", "mojang");
    }


}
