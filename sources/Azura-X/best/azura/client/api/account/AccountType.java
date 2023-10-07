package best.azura.client.api.account;

import best.azura.client.api.ui.notification.Notification;
import best.azura.client.api.ui.notification.Type;
import best.azura.client.impl.Client;
import com.google.gson.annotations.SerializedName;
import com.mojang.authlib.Agent;
import com.mojang.authlib.exceptions.AuthenticationException;
import com.mojang.authlib.yggdrasil.YggdrasilAuthenticationService;
import com.mojang.authlib.yggdrasil.YggdrasilUserAuthentication;
import fr.litarvan.openauth.microsoft.MicrosoftAuthResult;
import net.minecraft.client.Minecraft;
import net.minecraft.util.Session;

import java.net.Proxy;

public enum AccountType {

    @SerializedName("MOJANG")
    MOJANG {
        @Override
        String login(String data) {
            Client.INSTANCE.getNotificationManager().addToQueue(new Notification("Alt Manager","WARNING: Mojang accounts are no longer supported!",3000, Type.WARNING));
            return "";
        }
    },
    @SerializedName("CRACKED")
    CRACKED {
        @Override
        String login(String data) {
            Minecraft.getMinecraft().setSession(new Session(data, "", "-", "legacy"));
            if (Minecraft.getMinecraft().getSession().getUsername() != null)
                Client.INSTANCE.getNotificationManager().addToQueue(new Notification("Alt Manager","Logged in as " +
                        Minecraft.getMinecraft().getSession().getUsername(),3000,Type.SUCCESS));
            return Minecraft.getMinecraft().getSession().getUsername();
        }
    },
    @SerializedName("MICROSOFT")
    MICROSOFT {
        @Override
        String login(String data) {
            String[] args = data.split(":");
            try {
                Minecraft.getMinecraft().setSession(new Session(args[0], args[1], args[2], "mojang"));

                if (Minecraft.getMinecraft().getSession().getUsername() != null)
                    Client.INSTANCE.getNotificationManager().addToQueue(new Notification("Alt Manager","Logged in as " +
                            Minecraft.getMinecraft().getSession().getUsername(),3000,Type.SUCCESS));
                return Minecraft.getMinecraft().getSession().getUsername();
            } catch (Exception ignore) {}
            return "";
        }
    },
    @SerializedName("MICROSOFT_CREDENTIALS")
    MICROSOFT_CREDENTIALS {
        @Override
        String login(String data) {
            try {
                String[] args = data.split(":");
                MicrosoftAuthResult result = Client.INSTANCE.getMicrosoftAuthenticator().loginWithCredentials(args[0], args[1]);
                Minecraft.getMinecraft().setSession(new Session(result.getProfile().getName(), result.getProfile().getId(), result.getAccessToken(), "mojang"));
                if (Minecraft.getMinecraft().getSession().getUsername() != null)
                    Client.INSTANCE.getNotificationManager().addToQueue(new Notification("Alt Manager","Logged in as " +
                            Minecraft.getMinecraft().getSession().getUsername(),3000,Type.SUCCESS));
                return Minecraft.getMinecraft().getSession().getUsername();
            } catch (Exception ignore) {}
            return "";
        }
    };

    String login(String data) {return "";}

}
