package xyz.northclient.login;

import com.mojang.realmsclient.gui.ChatFormatting;
import fr.litarvan.openauth.microsoft.MicrosoftAuthResult;
import fr.litarvan.openauth.microsoft.MicrosoftAuthenticator;
import net.minecraft.client.Minecraft;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.Session;

public final class AltLoginThread
        extends Thread {
    private final String password;
    private String status;
    private final String username;
    private Minecraft mc = Minecraft.getMinecraft();

    public AltLoginThread(String username, String password) {
        super("Alt Login Thread");
        this.username = username;
        this.password = password;
        this.status = (Object)((Object)EnumChatFormatting.YELLOW) + "Logging in...";
    }

    private Session createSession(String username, String password) {
        try {
            MicrosoftAuthenticator authenticator = new MicrosoftAuthenticator();
            MicrosoftAuthResult result = authenticator.loginWithCredentials(username,password);
            System.out.println(username);

            return new Session(result.getProfile().getName(),result.getProfile().getId(), result.getAccessToken(), "microsoft");
        }catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public String getStatus() {
        return this.status;
    }

    @Override
    public void run() {
        if (this.password.equals("")) {
            this.mc.session = new Session(this.username, "", "", "mojang");
            this.status = (Object)((Object)EnumChatFormatting.WHITE) + "Logged in," + EnumChatFormatting.GREEN + " (" + this.username + ")" + ChatFormatting.WHITE + " - cracked alt";
            return;
        }
        this.status = (Object)((Object)EnumChatFormatting.YELLOW) + "Logging in...";
        Session auth = this.createSession(this.username, this.password);
        if (auth == null) {
            this.status = (Object)((Object)EnumChatFormatting.RED) + "Login Failed! Invalid Password or Email!";
        } else {
            this.status = (Object)((Object)EnumChatFormatting.WHITE) + "Logged in," + EnumChatFormatting.GREEN + " (" + auth.getUsername() + ")";
            this.mc.session = auth;
        }
    }

    public void setStatus(String status) {
        this.status = status;
    }
}

