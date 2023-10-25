package club.async.account;

import com.mojang.authlib.Agent;
import com.mojang.authlib.exceptions.AuthenticationException;
import com.mojang.authlib.yggdrasil.YggdrasilAuthenticationService;
import com.mojang.authlib.yggdrasil.YggdrasilUserAuthentication;

import net.minecraft.client.Minecraft;
import net.minecraft.util.Session;

import java.net.Proxy;

public class AccountThread extends Thread {

    private Account account;
    private String status = "Idle...";
    private String type = "";

    public AccountThread(Account account) {
        this(account, "mojang");
    }

    public AccountThread(Account account, String type) {
        this.account = account;
        this.type = type;
    }

    @Override
    public void run() {

        Session session = null;

        try {
            if (!account.getPassword().isEmpty()) {
                if(type.equalsIgnoreCase("mojang")) {
                    YggdrasilAuthenticationService service = new YggdrasilAuthenticationService(Proxy.NO_PROXY, "");
                    YggdrasilUserAuthentication auth = (YggdrasilUserAuthentication) service.createUserAuthentication(Agent.MINECRAFT);
                    auth.setUsername(account.getUsername());
                    auth.setPassword(account.getPassword());
                    try {
                        auth.logIn();
                        session = new Session(auth.getSelectedProfile().getName(), auth.getSelectedProfile().getId().toString(), auth.getAuthenticatedToken(), "mojang");
                    } catch (AuthenticationException localAuthenticationException) {
                        status = "Failed to authenticate";
                        localAuthenticationException.printStackTrace();
                    }
                } else if(type.equalsIgnoreCase("microsoft")){

                }
            } else {
                session = new Session(account.getUsername(), "", "", "mojang");
            }

            if(session != null) {
                Minecraft.getMinecraft().session = session;
                status = "Success!";
            } else {
                status = "Failed to authenticate";
            }
        } catch (Exception e) {
            status = "Failed to login";
        }

        super.run();
    }

    public String getStatus() {
        return status;
    }
}
