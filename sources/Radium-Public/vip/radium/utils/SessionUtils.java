package vip.radium.utils;

import com.mojang.authlib.Agent;
import com.mojang.authlib.exceptions.AuthenticationException;
import com.mojang.authlib.yggdrasil.YggdrasilAuthenticationService;
import com.mojang.authlib.yggdrasil.YggdrasilUserAuthentication;
import com.mojang.util.UUIDTypeAdapter;
import com.thealtening.auth.service.AlteningServiceType;
import vip.radium.RadiumClient;
import vip.radium.alt.Alt;
import vip.radium.alt.AltManager;
import vip.radium.gui.alt.impl.GuiAltManager;
import vip.radium.notification.Notification;
import vip.radium.notification.NotificationType;
import net.minecraft.util.Session;

import java.net.Proxy;

public final class SessionUtils {

    public static void switchService(AlteningServiceType service) {
        RadiumClient.getInstance().getAltManager().getAlteningAuth().updateService(service);
    }

    public static Alt importFromClipboard() {
        return AltManager.parseAlt(StringUtils.getTrimmedClipboardContents());
    }

    public static void logIn(Alt alt) {
        new Thread(() -> {
            try {
                GuiAltManager.status = "\247eLogging in...";
                RadiumClient.getInstance().getNotificationManager().add(new Notification("Logging In", 1000, NotificationType.INFO));
                YggdrasilUserAuthentication userAuthentication =
                        (YggdrasilUserAuthentication) new YggdrasilAuthenticationService(Proxy.NO_PROXY, "")
                                .createUserAuthentication(Agent.MINECRAFT);
                userAuthentication.setUsername(alt.getEmail());
                userAuthentication.setPassword(alt.getPassword());
                userAuthentication.logIn();
                String username = userAuthentication.getSelectedProfile().getName();
                alt.setUsername(username);
                alt.setWorking(true);
                Wrapper.getMinecraft().session = new Session(username,
                        UUIDTypeAdapter.fromUUID(userAuthentication.getSelectedProfile().getId()),
                        userAuthentication.getAuthenticatedToken(),
                        userAuthentication.getUserType().getName());
                RadiumClient.getInstance().getNotificationManager().add(new Notification("Login Success", "Logged into account " + username, NotificationType.SUCCESS));
                GuiAltManager.status = "\247aLogged in (" + username + ").";
            } catch (AuthenticationException ignored) {
                GuiAltManager.status = "\247cFailed.";
                RadiumClient.getInstance().getNotificationManager().add(new Notification("Login Failed", 1000, NotificationType.ERROR));
                alt.setWorking(false);
            }
        }).start();
    }
}
