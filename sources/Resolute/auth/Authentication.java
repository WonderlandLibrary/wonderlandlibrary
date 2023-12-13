// 
// Decompiled by Procyon v0.5.36
// 

package vip.Resolute.auth;

import vip.Resolute.auth.util.Encryption;
import vip.Resolute.auth.util.HWID;
import net.minecraft.util.EnumChatFormatting;
import vip.Resolute.ui.notification.Notification;
import vip.Resolute.ui.notification.NotificationType;
import vip.Resolute.Resolute;
import vip.Resolute.auth.util.Security;
import a.a.a.a.a.a.a.a.a.a.a.Mvncentral;
import net.minecraft.client.Minecraft;

public class Authentication extends Thread
{
    public Minecraft mc;
    public static String key;
    public String status;
    public static String username;
    public static String password;
    
    public Authentication(final String username, final String password) {
        this.mc = Minecraft.getMinecraft();
        Authentication.username = username;
        Authentication.password = password;
    }
    
    @Override
    public void run() {
        try {
            Mvncentral.a();
            if (Mvncentral.f(getEncryptedAuthString(Authentication.username, Authentication.password, Authentication.key)) && !Security.wiresharkRunning()) {
                Resolute.instance.setAuthorized(true);
                Resolute.instance.setResoluteName(Authentication.username);
                Resolute.instance.setUUID(Authentication.password);
                LoginScreen.progression = "Authorized!";
                Resolute.getNotificationManager().add(new Notification("Success", "Logged in! Welcome to vip.Resolute, " + Authentication.username + "!", 5000L, NotificationType.SUCCESS));
                this.status = EnumChatFormatting.GREEN + "Logged in! Welcome to vip.Resolute, " + Authentication.username + "!";
            }
            else {
                LoginScreen.progression = "Wrong login credentials!";
                Resolute.getNotificationManager().add(new Notification("Warning", "Invalid Username or Password", 5000L, NotificationType.WARNING));
                Resolute.instance.setAuthorized(false);
            }
        }
        catch (Exception e) {
            LoginScreen.progression = "Error";
            Resolute.getNotificationManager().add(new Notification("Error", "Authentication has failed", 5000L, NotificationType.ERROR));
            System.out.println("Error with authentication");
            e.printStackTrace();
        }
    }
    
    public static String getAuthString(final String inputUsername, final String inputPassword) {
        return inputUsername + "::" + inputPassword + "::" + HWID.hwid;
    }
    
    public static String getEncryptedAuthString(final String inputUsername, final String inputPassword, final String key) {
        final String authString = getAuthString(inputUsername, inputPassword);
        return Encryption.hashMD5(Encryption.encryptAES(authString, key));
    }
    
    public String getStatus() {
        return this.status;
    }
    
    static {
        Authentication.key = "1behk23kbb2kf8o22";
    }
}
