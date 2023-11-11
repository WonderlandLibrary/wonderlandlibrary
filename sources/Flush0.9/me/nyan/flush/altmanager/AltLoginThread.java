package me.nyan.flush.altmanager;

import com.mojang.authlib.Agent;
import com.mojang.authlib.exceptions.AuthenticationException;
import com.mojang.authlib.yggdrasil.YggdrasilAuthenticationService;
import com.mojang.authlib.yggdrasil.YggdrasilUserAuthentication;
import com.mojang.util.UUIDTypeAdapter;
import me.nyan.flush.Flush;
import fr.litarvan.openauth.microsoft.MicrosoftAuthResult;
import fr.litarvan.openauth.microsoft.MicrosoftAuthenticationException;
import fr.litarvan.openauth.microsoft.MicrosoftAuthenticator;
import net.minecraft.client.Minecraft;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.Session;

import java.io.IOException;
import java.net.Proxy;

public class AltLoginThread extends Thread {
    private final AccountInfo account;
    private final Minecraft mc = Minecraft.getMinecraft();
    private final AltManager altmgr = Flush.getInstance().getAltManager();

    public AltLoginThread(AccountInfo account) {
        super("Alt Login Thread");
        this.account = account;
    }

    private Session createSession(String username, String password) {
        YggdrasilAuthenticationService service = new YggdrasilAuthenticationService(Proxy.NO_PROXY, "");
        YggdrasilUserAuthentication auth = (YggdrasilUserAuthentication) service.createUserAuthentication(Agent.MINECRAFT);
        auth.setUsername(username);
        auth.setPassword(password);
        try {
            auth.logIn();
            return new Session(auth.getSelectedProfile().getName(), auth.getSelectedProfile().getId().toString(), auth.getAuthenticatedToken(), "mojang");
        } catch (AuthenticationException e) {
            return null;
        }
    }

    @Override
    public void run() {
        if (account.isCracked()) {
            if (account.isValidCracked()) {
                mc.setSession(new Session(account.getUsername(), "", "", "mojang"));
                altmgr.setStatus(String.format(EnumChatFormatting.GREEN + "Logged in as %s.", account.getUsername()));
            } else {
                altmgr.setStatus(EnumChatFormatting.RED + "Invalid Username!");
            }
            return;
        }

        altmgr.setStatus(EnumChatFormatting.YELLOW + "Logging in...");

        if (account.getType() == AccountInfo.Type.MICROSOFT) {
            microsoftLogin();
            return;
        }
        mojangLogin();
    }

    private void mojangLogin() {
        Session auth = createSession(account.getUsername(), account.getPassword());
        if (auth == null) {
            altmgr.setStatus(EnumChatFormatting.RED + "Login failed!");
            return;
        }

        altmgr.setStatus(String.format(EnumChatFormatting.GREEN + "Logged in as %s.", auth.getUsername()));
        mc.setSession(auth);

        if (!altmgr.getAlts().contains(account)) {
            return;
        }
        account.setUuid(UUIDTypeAdapter.fromString(auth.getPlayerID()));
        account.setDisplayName(auth.getUsername());
        try {
            account.downloadFace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        altmgr.save();
    }

    private void microsoftLogin() {
        MicrosoftAuthenticator authenticator = new MicrosoftAuthenticator();
        try {
            MicrosoftAuthResult result = authenticator.loginWithCredentials(account.getUsername(), account.getPassword());
            Session session = new Session(result.getProfile().getName(), result.getProfile().getId(), result.getAccessToken(), "msa");

            altmgr.setStatus(String.format(EnumChatFormatting.GREEN + "Logged in as %s.", session.getUsername()));
            mc.setSession(session);

            if (!altmgr.getAlts().contains(account)) {
                return;
            }
            account.setUuid(UUIDTypeAdapter.fromString(session.getPlayerID()));
            account.setDisplayName(session.getUsername());
            account.downloadFace();
            altmgr.save();
        } catch (MicrosoftAuthenticationException e) {
            altmgr.setStatus(EnumChatFormatting.RED + "Login failed!");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}