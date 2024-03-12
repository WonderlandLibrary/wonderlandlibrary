// 
// Decompiled by Procyon v0.5.36
// 

package dev.tenacity.ui.altmanager.helpers;

import java.awt.image.BufferedImage;
import net.minecraft.client.renderer.texture.DynamicTexture;
import javax.imageio.ImageIO;
import java.net.URL;
import java.util.Iterator;
import dev.tenacity.Tenacity;
import dev.tenacity.microsoft.MicrosoftLogin;
import net.minecraft.util.Session;
import java.util.concurrent.CompletableFuture;
import dev.tenacity.ui.notifications.NotificationManager;
import dev.tenacity.ui.notifications.NotificationType;
import dev.tenacity.utils.objects.TextField;
import dev.tenacity.utils.misc.Multithreading;
import java.nio.file.OpenOption;
import java.nio.charset.StandardCharsets;
import com.google.gson.GsonBuilder;
import java.util.function.Consumer;
import java.util.Collection;
import java.util.ArrayList;
import java.util.Arrays;
import com.google.gson.Gson;
import java.nio.file.Files;
import java.io.IOException;
import java.io.File;
import dev.tenacity.utils.time.TimerUtil;
import java.util.List;
import dev.tenacity.utils.Utils;

public class AltManagerUtils implements Utils
{
    private static List<Alt> alts;
    private final TimerUtil timerUtil;
    public static File altsFile;
    
    public AltManagerUtils() {
        this.timerUtil = new TimerUtil();
        if (!AltManagerUtils.altsFile.exists()) {
            try {
                if (AltManagerUtils.altsFile.getParentFile().mkdirs()) {
                    AltManagerUtils.altsFile.createNewFile();
                }
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
        try {
            final byte[] content = Files.readAllBytes(AltManagerUtils.altsFile.toPath());
            (AltManagerUtils.alts = new ArrayList<Alt>((Collection<? extends Alt>)Arrays.asList((Object[])new Gson().fromJson(new String(content), (Class)Alt[].class)))).forEach(this::getHead);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public void writeAltsToFile() {
        if (this.timerUtil.hasTimeElapsed(15000L, true)) {
            new Thread(() -> {
                try {
                    if (!AltManagerUtils.altsFile.exists() && AltManagerUtils.altsFile.getParentFile().mkdirs()) {
                        AltManagerUtils.altsFile.createNewFile();
                    }
                    Files.write(AltManagerUtils.altsFile.toPath(), new GsonBuilder().setPrettyPrinting().excludeFieldsWithoutExposeAnnotation().create().toJson((Object)AltManagerUtils.alts.toArray(new Alt[0])).getBytes(StandardCharsets.UTF_8), new OpenOption[0]);
                }
                catch (IOException e) {
                    e.printStackTrace();
                }
            }).start();
        }
    }
    
    public static void removeAlt(final Alt alt) {
        if (alt != null) {
            AltManagerUtils.alts.remove(alt);
        }
    }
    
    public static void writeAlts() {
        Multithreading.runAsync(() -> {
            try {
                Files.write(AltManagerUtils.altsFile.toPath(), new GsonBuilder().setPrettyPrinting().excludeFieldsWithoutExposeAnnotation().create().toJson((Object)AltManagerUtils.alts.toArray(new Alt[0])).getBytes(StandardCharsets.UTF_8), new OpenOption[0]);
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        });
    }
    
    public void login(final TextField username, final TextField password) {
        String usernameS;
        String passwordS;
        if (username.getText().contains(":")) {
            final String[] combo = username.getText().split(":");
            usernameS = combo[0];
            passwordS = combo[1];
        }
        else {
            usernameS = username.getText();
            passwordS = password.getText();
        }
        final boolean microsoft = Alt.currentLoginMethod == Alt.AltType.MICROSOFT;
        if (usernameS.isEmpty() && passwordS.isEmpty()) {
            return;
        }
        this.loginWithString(usernameS, passwordS, false);
    }
    
    public void microsoftLoginAsync(final String email, final String password) {
        this.microsoftLoginAsync(null, email, password);
    }
    
    public void microsoftLoginAsync(Alt alt, final String email, final String password) {
        NotificationManager.post(NotificationType.INFO, "Alt Manager", "Opening browser to complete Microsoft authentication...", 12.0f);
        if (alt == null) {
            alt = new Alt(email, password);
        }
        final Alt finalAlt = alt;
        final CompletableFuture<Session> future;
        MicrosoftLogin.LoginData login;
        final CompletableFuture<Session> completableFuture;
        final Session auth;
        final Alt currentSessionAlt;
        Multithreading.runAsync(() -> {
            future = new CompletableFuture<Session>();
            MicrosoftLogin.getRefreshToken(refreshToken -> {
                if (refreshToken != null) {
                    login = MicrosoftLogin.login(refreshToken);
                    completableFuture.complete(new Session(login.username, login.uuid, login.mcToken, "microsoft"));
                }
                return;
            });
            auth = future.join();
            if (auth != null) {
                AltManagerUtils.mc.session = auth;
                currentSessionAlt.uuid = auth.getPlayerID();
                currentSessionAlt.altType = Alt.AltType.MICROSOFT;
                currentSessionAlt.username = auth.getUsername();
                if (auth.getUsername() == null) {
                    NotificationManager.post(NotificationType.WARNING, "Alt Manager", "Please set an username on your Minecraft account!", 12.0f);
                }
                Alt.stage = 2;
                currentSessionAlt.altState = Alt.AltState.LOGIN_SUCCESS;
                getAlts().add(currentSessionAlt);
                writeAlts();
                Tenacity.INSTANCE.getAltManager().currentSessionAlt = currentSessionAlt;
                Tenacity.INSTANCE.getAltManager().getAltPanel().refreshAlts();
            }
            else {
                Alt.stage = 1;
                currentSessionAlt.altState = Alt.AltState.LOGIN_FAIL;
            }
        });
    }
    
    public void loginWithString(final String username, final String password, final boolean microsoft) {
        for (final Alt alt : AltManagerUtils.alts) {
            if (alt.email.equals(username) && alt.password.equals(password)) {
                Alt.stage = 0;
                alt.loginAsync(microsoft);
                return;
            }
        }
        final Alt alt2 = new Alt(username, password);
        AltManagerUtils.alts.add(alt2);
        Alt.stage = 0;
        alt2.loginAsync(microsoft);
    }
    
    public void getHead(final Alt alt) {
        if (alt.uuid == null || alt.head != null || alt.headTexture || alt.headTries > 5) {
            return;
        }
        final URL input;
        BufferedImage image;
        DynamicTexture texture;
        Multithreading.runAsync(() -> {
            ++alt.headTries;
            try {
                new URL("https://visage.surgeplay.com/bust/160/" + alt.uuid);
                image = ImageIO.read(input);
                alt.headTexture = true;
                AltManagerUtils.mc.addScheduledTask(() -> {
                    texture = new DynamicTexture(image);
                    alt.head = AltManagerUtils.mc.getTextureManager().getDynamicTextureLocation("HEAD-" + alt.uuid, texture);
                });
            }
            catch (IOException e) {
                alt.headTexture = false;
            }
        });
    }
    
    public static List<Alt> getAlts() {
        return AltManagerUtils.alts;
    }
    
    static {
        AltManagerUtils.alts = new ArrayList<Alt>();
        AltManagerUtils.altsFile = new File(Tenacity.DIRECTORY, "Alts.json");
    }
}
