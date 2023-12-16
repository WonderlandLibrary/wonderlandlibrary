// 
// Decompiled by Procyon v0.5.36
// 

package moonsense.account;

import fr.litarvan.openauth.microsoft.MicrosoftAuthResult;
import fr.litarvan.openauth.microsoft.MicrosoftAuthenticationException;
import net.minecraft.util.Session;
import net.minecraft.client.Minecraft;
import fr.litarvan.openauth.microsoft.MicrosoftAuthenticator;
import moonsense.MoonsenseClient;

public class AccountLoginThread extends Thread
{
    private String email;
    private String password;
    private String status;
    
    public AccountLoginThread(final String email, final String password) {
        this.status = "&eWaiting for login...";
        this.email = email;
        this.password = password;
    }
    
    @Override
    public void run() {
        if (MoonsenseClient.INSTANCE.getSocketClient().isRunning()) {
            MoonsenseClient.INSTANCE.getSocketClient().updateSelf(false);
        }
        try {
            final MicrosoftAuthenticator a = new MicrosoftAuthenticator();
            try {
                final MicrosoftAuthResult res = a.loginWithCredentials(this.email, this.password);
                Minecraft.getMinecraft().session = new Session(res.getProfile().getName(), res.getProfile().getId(), res.getAccessToken(), "legacy");
                Account account = MoonsenseClient.INSTANCE.getAccountManager().getAccountByName(res.getProfile().getName());
                account = ((account == null) ? new Account(res.getProfile().getName(), this.email, this.password) : account);
                account.setUsername(res.getProfile().getName());
                this.status = String.format("&aLogged in as %s.", res.getProfile().getName());
                if (!MoonsenseClient.INSTANCE.getAccountManager().getAccounts().contains(account)) {
                    MoonsenseClient.INSTANCE.getAccountManager().getAccounts().add(account);
                }
                MoonsenseClient.INSTANCE.getAccountManager().setLastAlt(account);
                MoonsenseClient.INSTANCE.getAccountManager().save();
                if (MoonsenseClient.INSTANCE.getSocketClient().isRunning()) {
                    MoonsenseClient.INSTANCE.getSocketClient().updateUserWithPacket(res.getProfile().getName(), true);
                }
            }
            catch (MicrosoftAuthenticationException e) {
                e.printStackTrace();
                this.status = "&4Login failed.";
                if (Minecraft.getMinecraft().session.getToken() != null && !Minecraft.getMinecraft().session.getToken().equalsIgnoreCase("0") && MoonsenseClient.INSTANCE.getSocketClient().isRunning()) {
                    MoonsenseClient.INSTANCE.getSocketClient().updateSelf(true);
                }
            }
        }
        catch (NullPointerException e2) {
            this.status = "&4Unknown error.";
            if (Minecraft.getMinecraft().session.getToken() != null && !Minecraft.getMinecraft().session.getToken().equalsIgnoreCase("0") && MoonsenseClient.INSTANCE.getSocketClient().isRunning()) {
                MoonsenseClient.INSTANCE.getSocketClient().updateSelf(true);
            }
        }
    }
    
    public String getStatus() {
        return this.status;
    }
}
