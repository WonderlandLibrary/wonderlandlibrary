package tech.atani.client.feature.account.thread;

import fr.litarvan.openauth.microsoft.MicrosoftAuthResult;
import fr.litarvan.openauth.microsoft.MicrosoftAuthenticator;
import net.minecraft.client.Minecraft;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.Session;
import tech.atani.client.feature.account.Account;

import java.util.UUID;

public final class AltLoginThread
extends Thread {
    private final Account account;
    private String status;
    private Minecraft mc = Minecraft.getMinecraft();

    public AltLoginThread(Account account) {
        super("Alt Login Thread");
        this.account = account;
        this.status = (Object)((Object)EnumChatFormatting.GRAY) + "Waiting...";
    }

    private Session createSession(Account account) {
        try {
            if(account.isCracked()) {
                return new Session(account.getName(), UUID.randomUUID().toString(), "real token :0", "mojang");
            } else {
                System.out.println(account.getName() + " " + account.getPassword());
                MicrosoftAuthenticator microsoftAuthenticator = new MicrosoftAuthenticator();
                MicrosoftAuthResult microsoftAuthResult = microsoftAuthenticator.loginWithCredentials(account.getName(), account.getPassword());
                return new Session(microsoftAuthResult.getProfile().getName(), microsoftAuthResult.getProfile().getId(), microsoftAuthResult.getAccessToken(), "mojang");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public String getStatus() {
        return this.status;
    }

    @Override
    public void run() {
        this.status = (Object)((Object)EnumChatFormatting.YELLOW) + "Logging in...";
        Session auth = this.createSession(account);
        if (auth == null) {
            this.status = (Object)((Object)EnumChatFormatting.RED) + "Login failed!";
        } else {
            this.status = (Object)((Object)EnumChatFormatting.GREEN) + "Logged in. (" + auth.getUsername() + ")";
            this.mc.session = auth;
        }
    }

    public void setStatus(String status) {
        this.status = status;
    }
}

