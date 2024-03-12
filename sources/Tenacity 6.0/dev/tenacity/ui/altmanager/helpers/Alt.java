// 
// Decompiled by Procyon v0.5.36
// 

package dev.tenacity.ui.altmanager.helpers;

import com.mojang.authlib.exceptions.AuthenticationException;
import com.altening.auth.service.AlteningServiceType;
import com.mojang.authlib.Agent;
import com.mojang.authlib.yggdrasil.YggdrasilUserAuthentication;
import com.mojang.authlib.yggdrasil.YggdrasilAuthenticationService;
import java.net.Proxy;
import dev.tenacity.microsoft.MicrosoftLogin;
import java.util.concurrent.CompletableFuture;
import dev.tenacity.ui.notifications.NotificationManager;
import dev.tenacity.ui.notifications.NotificationType;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.OpenOption;
import java.nio.charset.StandardCharsets;
import com.google.gson.GsonBuilder;
import dev.tenacity.Tenacity;
import net.minecraft.util.Session;
import java.util.UUID;
import net.minecraft.util.ResourceLocation;
import dev.tenacity.utils.server.ban.HypixelBan;
import com.google.gson.annotations.SerializedName;
import com.google.gson.annotations.Expose;
import com.altening.auth.TheAlteningAuthentication;
import com.altening.auth.SSLController;
import net.minecraft.client.Minecraft;

public class Alt
{
    public static Minecraft mc;
    public static int stage;
    public static AltType currentLoginMethod;
    private final SSLController ssl;
    private final TheAlteningAuthentication alteningAuthentication;
    @Expose
    @SerializedName("uuid")
    public String uuid;
    @Expose
    @SerializedName("username")
    public String username;
    @Expose
    @SerializedName("email")
    public String email;
    @Expose
    @SerializedName("password")
    public String password;
    @Expose
    @SerializedName("altState")
    public AltState altState;
    @Expose
    @SerializedName("altType")
    public AltType altType;
    @Expose
    @SerializedName("hypixelBan")
    public HypixelBan hypixelBan;
    @Expose
    @SerializedName("favorite")
    public boolean favorite;
    public ResourceLocation head;
    public boolean headTexture;
    public int headTries;
    
    public Alt(final String email, final String password) {
        this.ssl = new SSLController();
        this.alteningAuthentication = TheAlteningAuthentication.mojang();
        this.email = email;
        this.password = password;
    }
    
    public Alt() {
        this.ssl = new SSLController();
        this.alteningAuthentication = TheAlteningAuthentication.mojang();
    }
    
    private void login(final boolean microsoft) {
        Alt.stage = 0;
        if (!microsoft && this.password.isEmpty()) {
            final String uuid = UUID.randomUUID().toString();
            Alt.mc.session = new Session(this.email, uuid, "", "mojang");
            this.username = this.email;
            this.uuid = uuid;
            this.altState = AltState.LOGIN_SUCCESS;
            this.altType = AltType.CRACKED;
            Alt.stage = 2;
            Tenacity.INSTANCE.getAltManager().currentSessionAlt = this;
            return;
        }
        final Session auth = this.createSession(this.email, this.password, microsoft);
        if (auth == null) {
            Alt.stage = 1;
            this.altState = AltState.LOGIN_FAIL;
        }
        else {
            Alt.mc.session = auth;
            this.uuid = auth.getPlayerID();
            this.username = auth.getUsername();
            Alt.stage = 2;
            this.altState = AltState.LOGIN_SUCCESS;
            this.altType = Alt.currentLoginMethod;
            Tenacity.INSTANCE.getAltManager().currentSessionAlt = this;
        }
    }
    
    public void loginAsync() {
        this.loginAsync(this.altType == AltType.MICROSOFT);
    }
    
    public void loginAsync(final boolean microsoft) {
        new Thread(() -> {
            this.login(microsoft);
            new Thread(() -> {
                try {
                    Files.write(AltManagerUtils.altsFile.toPath(), new GsonBuilder().setPrettyPrinting().excludeFieldsWithoutExposeAnnotation().create().toJson((Object)AltManagerUtils.getAlts().toArray(new Alt[0])).getBytes(StandardCharsets.UTF_8), new OpenOption[0]);
                }
                catch (IOException e) {
                    e.printStackTrace();
                }
            }).start();
        }).start();
    }
    
    private Session createSession(final String username, final String password, final boolean microsoft) {
        if (microsoft) {
            NotificationManager.post(NotificationType.INFO, "Alt Manager", "Opening browser to complete Microsoft authentication...", 12.0f);
            final CompletableFuture<Session> future = new CompletableFuture<Session>();
            MicrosoftLogin.LoginData login;
            final CompletableFuture<Session> completableFuture;
            MicrosoftLogin.getRefreshToken(refreshToken -> {
                if (refreshToken != null) {
                    System.out.println("Refresh token: " + refreshToken);
                    login = MicrosoftLogin.login(refreshToken);
                    Alt.currentLoginMethod = AltType.MICROSOFT;
                    completableFuture.complete(new Session(login.username, login.uuid, login.mcToken, "microsoft"));
                }
                return;
            });
            return future.join();
        }
        final YggdrasilAuthenticationService service = new YggdrasilAuthenticationService(Proxy.NO_PROXY, "");
        final YggdrasilUserAuthentication auth = (YggdrasilUserAuthentication)service.createUserAuthentication(Agent.MINECRAFT);
        auth.setUsername(username);
        auth.setPassword(password);
        try {
            if (username.endsWith("@alt.com")) {
                this.ssl.disableCertificateValidation();
                this.alteningAuthentication.updateService(AlteningServiceType.THEALTENING);
                auth.setPassword("sample pass");
            }
            else if (this.alteningAuthentication.getService() == AlteningServiceType.THEALTENING) {
                this.ssl.enableCertificateValidation();
                this.alteningAuthentication.updateService(AlteningServiceType.MOJANG);
            }
            auth.logIn();
            Alt.currentLoginMethod = AltType.MOJANG;
            return new Session(auth.getSelectedProfile().getName(), auth.getSelectedProfile().getId().toString(), auth.getAuthenticatedToken(), "mojang");
        }
        catch (AuthenticationException e) {
            e.printStackTrace();
            return null;
        }
    }
    
    public String getType() {
        return (this.altType == null) ? "Not logged in" : this.altType.getName();
    }
    
    @Override
    public String toString() {
        return "Alt{uuid='" + this.uuid + '\'' + ", username='" + this.username + '\'' + ", email='" + this.email + '\'' + ", password='" + this.password + '\'' + ", altState=" + this.altState + ", altType=" + this.altType + ", headTries=" + this.headTries + '}';
    }
    
    static {
        Alt.mc = Minecraft.getMinecraft();
        Alt.stage = -1;
        Alt.currentLoginMethod = AltType.MOJANG;
    }
    
    public enum AltState
    {
        @Expose
        @SerializedName("1")
        LOGIN_FAIL("p"), 
        @Expose
        @SerializedName("2")
        LOGIN_SUCCESS("o");
        
        private final String icon;
        
        public String getIcon() {
            return this.icon;
        }
        
        private AltState(final String icon) {
            this.icon = icon;
        }
    }
    
    public enum AltType
    {
        @Expose
        @SerializedName("1")
        MICROSOFT("Microsoft"), 
        @Expose
        @SerializedName("2")
        MOJANG("Mojang"), 
        @Expose
        @SerializedName("3")
        CRACKED("Cracked");
        
        private final String name;
        
        public String getName() {
            return this.name;
        }
        
        private AltType(final String name) {
            this.name = name;
        }
    }
}
