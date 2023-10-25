package intent.AquaDev.aqua.alt.design;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mojang.authlib.Agent;
import com.mojang.authlib.exceptions.AuthenticationException;
import com.mojang.authlib.yggdrasil.YggdrasilAuthenticationService;
import com.mojang.authlib.yggdrasil.YggdrasilUserAuthentication;
import com.thealtening.auth.TheAlteningAuthentication;
import com.thealtening.auth.service.AlteningServiceType;
import intent.AquaDev.aqua.Aqua;
import intent.AquaDev.aqua.alt.design.AltTypes;
import intent.AquaDev.aqua.alt.design.AltsSaver;
import intent.AquaDev.aqua.alt.design.Login;
import intent.AquaDev.aqua.altloader.AltLoader;
import intent.AquaDev.aqua.altloader.Api;
import intent.AquaDev.aqua.altloader.Callback;
import io.netty.util.internal.ThreadLocalRandom;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.Proxy;
import java.net.URI;
import java.net.URL;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.util.Session;
import org.lwjgl.input.Keyboard;

public class AltManager
extends GuiScreen {
    private static final AltLoader altLoader = new AltLoader();
    private static final String ALTENING_AUTH_SERVER = "http://authserver.thealtening.com/";
    private static final String ALTENING_SESSION_SERVER = "http://sessionserver.thealtening.com/";
    private static TheAlteningAuthentication alteningAuthentication = TheAlteningAuthentication.mojang();
    public GuiScreen parentScreen;
    private final String status = "";
    public GuiTextField email;
    public GuiTextField password;
    public static String emailName = "";
    public static String passwordName = "";
    public static int i = 0;

    public AltManager(GuiScreen event) {
        this.parentScreen = event;
    }

    public void initGui() {
        Keyboard.enableRepeatEvents((boolean)true);
        this.buttonList.add((Object)new GuiButton(13, 10, height / 4 + 120 + 205, 68, 20, "TheAltening"));
        this.buttonList.add((Object)new GuiButton(12, 10, height / 4 + 120 + 180, 68, 20, "EasyMC"));
        this.buttonList.add((Object)new GuiButton(11, 10, height / 4 + 144 + 130, 68, 20, "Token"));
        this.buttonList.add((Object)new GuiButton(9, 10, height / 4 + 96 + 130, 68, 20, "Add"));
        this.buttonList.add((Object)new GuiButton(3, 10, height / 4 + 96 + 96, 68, 20, "Remove"));
        this.buttonList.add((Object)new GuiButton(1, 10, height / 4 + 96 + 50, 68, 20, "Login"));
        this.buttonList.add((Object)new GuiButton(5, 10, height / 4 + 96 + 73, 68, 20, "Back"));
        this.buttonList.add((Object)new GuiButton(2, 10, height / 4 + 96 + 15, 68, 20, "Clipboard"));
        this.email = new GuiTextField(3, this.fontRendererObj, 10, 50, 200, 20);
        this.password = new GuiTextField(4, this.fontRendererObj, 10, 100, 200, 20);
    }

    protected void keyTyped(char typedChar, int keyCode) throws IOException {
        this.email.textboxKeyTyped(typedChar, keyCode);
        this.password.textboxKeyTyped(typedChar, keyCode);
        if (keyCode == 1) {
            this.mc.displayGuiScreen(this.parentScreen);
        }
    }

    public void onGuiClosed() {
        Keyboard.enableRepeatEvents((boolean)false);
    }

    public void updateScreen() {
        this.email.updateCursorCounter();
        this.password.updateCursorCounter();
    }

    public void mouseClicked(int x, int y, int m) {
        this.email.mouseClicked(x, y, m);
        this.password.mouseClicked(x, y, m);
        try {
            super.mouseClicked(x, y, m);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        ScaledResolution s = new ScaledResolution(this.mc);
        Aqua.INSTANCE.shaderBackground.renderShader();
        boolean i1 = false;
        Aqua.INSTANCE.comfortaa3.drawStringWithShadow("Email:", 10.0f, 36.0f, -1);
        Aqua.INSTANCE.comfortaa3.drawStringWithShadow("Password:", 10.0f, 86.0f, -1);
        Aqua.INSTANCE.comfortaa3.drawStringWithShadow("\u00a73Logged in with \u00a7a" + this.mc.getSession().getUsername(), (float)(width / 2), 20.0f, -1);
        this.email.drawTextBox();
        this.password.drawTextBox();
        int boxY = 26;
        int boxX = 50;
        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    public boolean login1(String email, String password) {
        try {
            YggdrasilAuthenticationService service = new YggdrasilAuthenticationService(Proxy.NO_PROXY, "");
            YggdrasilUserAuthentication auth = new YggdrasilUserAuthentication(service, Agent.MINECRAFT);
            auth.setUsername(email);
            auth.setPassword(password);
            if (auth.canLogIn()) {
                try {
                    auth.logIn();
                    Minecraft.getMinecraft().session = new Session(auth.getSelectedProfile().getName(), auth.getSelectedProfile().getId().toString(), auth.getAuthenticatedToken(), "mojang");
                    return true;
                }
                catch (AuthenticationException authenticationException) {
                    return false;
                }
            }
        }
        catch (Exception exception) {
            exception.printStackTrace();
        }
        return false;
    }

    public void login(String Email, String password) {
        alteningAuthentication.updateService(AlteningServiceType.MOJANG);
        try {
            Minecraft.getMinecraft().session = Login.logIn((String)Email, (String)password);
        }
        catch (Exception e) {
            YggdrasilAuthenticationService authenticationService = new YggdrasilAuthenticationService(Proxy.NO_PROXY, "");
            YggdrasilUserAuthentication authentication = (YggdrasilUserAuthentication)authenticationService.createUserAuthentication(Agent.MINECRAFT);
            authentication.setUsername(Email);
            authentication.setPassword(password);
            try {
                authentication.logIn();
                Minecraft.getMinecraft().session = new Session(authentication.getSelectedProfile().getName(), authentication.getSelectedProfile().getId().toString(), authentication.getAuthenticatedToken(), "mojang");
            }
            catch (Exception e1) {
                Minecraft.getMinecraft().session = new Session(Email, "", "", "mojang");
            }
        }
    }

    public void loginAltening(String Email, String password) {
        alteningAuthentication.updateService(AlteningServiceType.THEALTENING);
        try {
            Minecraft.getMinecraft().session = Login.logIn((String)Email, (String)password);
        }
        catch (Exception e) {
            YggdrasilAuthenticationService authenticationService = new YggdrasilAuthenticationService(Proxy.NO_PROXY, "");
            YggdrasilUserAuthentication authentication = (YggdrasilUserAuthentication)authenticationService.createUserAuthentication(Agent.MINECRAFT);
            authentication.setUsername(Email);
            authentication.setPassword(password);
            try {
                authentication.logIn();
                Minecraft.getMinecraft().session = new Session(authentication.getSelectedProfile().getName(), authentication.getSelectedProfile().getId().toString(), authentication.getAuthenticatedToken(), "mojang");
            }
            catch (Exception e1) {
                Minecraft.getMinecraft().session = new Session(Email, "", "", "mojang");
            }
        }
    }

    protected void actionPerformed(GuiButton button) throws IOException {
        if (button.id == 11) {
            AltManager.loginToken(AltManager.getClipboardString());
        }
        if (button.id == 13) {
            this.loginAltening(AltManager.getClipboardString(), "test");
        }
        if (button.id == 10) {
            // empty if block
        }
        if (button.id == 9 && !this.email.getText().isEmpty() && !this.password.getText().isEmpty()) {
            emailName = this.email.getText();
            passwordName = this.password.getText();
            AltsSaver.AltTypeList.add((Object)new AltTypes(emailName, passwordName));
        }
        if (button.id == 12) {
            String token = GuiScreen.getClipboardString();
            if (token.equals((Object)"")) {
                try {
                    Class oclass = Class.forName((String)"java.awt.Desktop");
                    Object object = oclass.getMethod("getDesktop", new Class[0]).invoke(null, new Object[0]);
                    oclass.getMethod("browse", new Class[]{URI.class}).invoke(object, new Object[]{new URI("https://easymc.io/")});
                }
                catch (Throwable oclass) {
                    // empty catch block
                }
            }
            Api.redeem((String)token, (Callback)new /* Unavailable Anonymous Inner Class!! */);
        }
        if (button.id == 1) {
            if (!this.email.getText().isEmpty() && !this.password.getText().isEmpty()) {
                this.login(this.email.getText().trim(), this.password.getText().trim());
                this.email.setText("");
                this.password.setText("");
            } else if (this.email.getText().isEmpty() && this.password.getText().isEmpty()) {
                this.password.setText("a");
                StringBuilder randomName = new StringBuilder();
                String alphabet = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ123456789_";
                int random = ThreadLocalRandom.current().nextInt(8, 16);
                for (int i = 0; i < random; ++i) {
                    randomName.append(alphabet.charAt(ThreadLocalRandom.current().nextInt(1, alphabet.length())));
                }
                this.email.setText("" + randomName);
            }
        }
        if (button.id == 2) {
            String Copy = AltManager.getClipboardString();
            String[] WomboCombo = Copy.split(":");
            String Email1 = WomboCombo[0];
            String Passwort = WomboCombo[1];
            this.email.writeText(Email1);
            this.password.writeText(Passwort);
        }
        if (button.id == 26) {
            this.mc.displayGuiScreen(this.parentScreen);
        }
        if (button.id == 5) {
            this.mc.displayGuiScreen((GuiScreen)new GuiMainMenu());
        }
    }

    public static boolean loginToken(String token) {
        URL url = null;
        try {
            url = new URL("https://api.minecraftservices.com/minecraft/profile/");
        }
        catch (MalformedURLException e) {
            e.printStackTrace();
        }
        HttpURLConnection conn = null;
        try {
            conn = (HttpURLConnection)url.openConnection();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        conn.setRequestProperty("Authorization", "Bearer " + token);
        conn.setRequestProperty("Content-Type", "application/json");
        try {
            conn.setRequestMethod("GET");
        }
        catch (ProtocolException e) {
            e.printStackTrace();
        }
        BufferedReader in = null;
        try {
            in = new BufferedReader((Reader)new InputStreamReader(conn.getInputStream()));
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        StringBuilder response = new StringBuilder();
        while (true) {
            String output;
            try {
                output = in.readLine();
                if (output == null) {
                    break;
                }
            }
            catch (IOException e) {
                e.printStackTrace();
                return false;
            }
            response.append(output);
        }
        String jsonString = response.toString();
        JsonObject obj = new JsonParser().parse(jsonString).getAsJsonObject();
        String username = obj.get("name").getAsString();
        String uuid = obj.get("id").getAsString();
        Minecraft.getMinecraft().session = new Session(username, uuid, token, "null");
        try {
            in.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        return true;
    }

    static /* synthetic */ AltLoader access$000() {
        return altLoader;
    }

    static /* synthetic */ Minecraft access$100(AltManager x0) {
        return x0.mc;
    }
}
