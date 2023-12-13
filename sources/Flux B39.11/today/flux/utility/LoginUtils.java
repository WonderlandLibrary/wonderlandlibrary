package today.flux.utility;

import com.mojang.authlib.Agent;
import com.mojang.authlib.UserType;
import com.mojang.authlib.exceptions.AuthenticationException;
import com.mojang.authlib.exceptions.AuthenticationUnavailableException;
import com.mojang.authlib.yggdrasil.YggdrasilAuthenticationService;
import com.mojang.authlib.yggdrasil.YggdrasilUserAuthentication;
import net.minecraft.client.Minecraft;
import net.minecraft.util.Session;
import today.flux.Flux;
import today.flux.gui.altMgr.Alt;
import today.flux.gui.altMgr.GuiAltMgr;

import java.net.Proxy;

public class LoginUtils {
	public static String login(String email, String password) {
		return login(email, password, false);
	}
	
    public static String login(String email, String password, boolean autoRemove) {

        System.out.println("Logging in with [" + email + ":********]");
        final YggdrasilAuthenticationService authenticationService = new YggdrasilAuthenticationService(Proxy.NO_PROXY,
                "");
        final YggdrasilUserAuthentication authentication = (YggdrasilUserAuthentication) authenticationService
                .createUserAuthentication(Agent.MINECRAFT);
        authentication.setUsername(email);
        authentication.setPassword(password);
        try {
            authentication.logIn();
            Minecraft.getMinecraft().session = new Session(authentication.getSelectedProfile().getName(), authentication.getSelectedProfile().getId().toString(), authentication.getAuthenticatedToken(), UserType.MOJANG.getName());

            Flux.INSTANCE.currentAlt = new String[]{email, password};
            return null;
        } catch (AuthenticationUnavailableException e2) {
            return "Cannot contact authentication server!";
        } catch (AuthenticationException e) {
            if (e.getMessage().contains("Invalid username or password.")
                    || e.getMessage().toLowerCase().contains("account migrated")) {
            	
				if (autoRemove) {
					Alt curAlt = null;
					for (Alt alt : GuiAltMgr.alts) {
						if (alt.getEmail().equalsIgnoreCase(email)) {
							curAlt = alt;
							break;
						}
					}
					
					if (curAlt != null) {
                        GuiAltMgr.alts.remove(curAlt);
					}
				}
				
                return "Wrong password!";
            } else {
                return "Cannot contact authentication server!";
            }
        } catch (NullPointerException e3) {
            return "Wrong password!";
        }
    }

    public static Session getSession(String email, String password) {

        System.out.println("Logging in with [" + email + ":********]");
        final YggdrasilAuthenticationService authenticationService = new YggdrasilAuthenticationService(Proxy.NO_PROXY,
                "");
        final YggdrasilUserAuthentication authentication = (YggdrasilUserAuthentication) authenticationService.createUserAuthentication(Agent.MINECRAFT);
        authentication.setUsername(email);
        authentication.setPassword(password);
        try {
            authentication.logIn();
            return new Session(authentication.getSelectedProfile().getName(), authentication.getSelectedProfile().getId().toString(), authentication.getAuthenticatedToken(), UserType.MOJANG.getName());
        } catch (AuthenticationUnavailableException e2) {

        } catch (AuthenticationException e) {

        } catch (NullPointerException e3) {

        }
        return null;
    }

    public static void loginToken(String accessToken, String name, String uuid, String clientToken) {
        System.out.println("Logging in with :" + accessToken);

        try {
            Minecraft.getMinecraft().session = new Session(name, uuid, accessToken, UserType.MOJANG.getName());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void changeCrackedName(final String newName) {
        Minecraft.getMinecraft().session = new Session(newName, "", "", UserType.LEGACY.getName());
    }

    public static String removeSpecialChar(String input) {
        return input.replaceAll("\\p{C}", "");
    }
}
