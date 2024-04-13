package wtf.dawn.alts;

import com.mojang.authlib.Agent;
import com.mojang.authlib.AuthenticationService;
import com.mojang.authlib.UserAuthentication;
import com.mojang.authlib.yggdrasil.YggdrasilAuthenticationService;
import net.minecraft.client.Minecraft;
import net.minecraft.util.Session;

import java.util.UUID;

public class SessionManager {

    private final UserAuthentication auth;

    public SessionManager() {
        UUID uuid_random = UUID.randomUUID();
        AuthenticationService authService = new YggdrasilAuthenticationService(Minecraft.getMinecraft().getProxy(), uuid_random.toString());
        auth = authService.createUserAuthentication(Agent.MINECRAFT);
        authService.createMinecraftSessionService();
    }

    private void setSession(Session session) {
        Minecraft.getMinecraft().session = session;
    }

    public void offlineLogin(String p1) {
        this.auth.logOut();
        Session session = new Session(p1, p1, "69", "legacy");
        setSession(session);
    }


}
