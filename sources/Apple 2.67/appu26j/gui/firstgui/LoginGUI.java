package appu26j.gui.firstgui;

import java.awt.Color;
import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

import org.lwjgl.input.Keyboard;

import appu26j.Apple;
import fr.litarvan.openauth.microsoft.MicrosoftAuthResult;
import fr.litarvan.openauth.microsoft.MicrosoftAuthenticator;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.main.Main;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Session;

public class LoginGUI extends GuiScreen {
    private boolean crackedLogin = false, flag = false;
    private long time = System.currentTimeMillis();
    private String name = "";

    /**
     * Draws the screen and all the components in it. Args : mouseX, mouseY,
     * renderPartialTicks
     */
    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        GlStateManager.color(1, 1, 1, 1);
        this.mc.getTextureManager().bindTexture(new ResourceLocation("panorama.png"));
        this.drawModalRectWithCustomSizedTexture(0, 0, 0, 0, this.width, this.height, this.width, this.height);
        float width = this.width / 2;
        float height = this.height / 2;
        this.drawRect(width - 150, height - 100, width + 150, height + 100, new Color(0, 0, 25, 75).getRGB());
        this.drawStringWithShadow("Welcome!", width - (this.getStringWidth("Welcome!", 16) / 2), height - 90, 16, -1);
        this.drawStringWithShadow("You are not logged in :(",
                width - (this.getStringWidth("You are not logged in :(", 8) / 2), height - 65, 8, -1);
        this.drawRect(width - 50, height - 5, width + 50, height + 20,
                !this.crackedLogin && this.isInsideBox(mouseX, mouseY, width - 50, height - 5, width + 50, height + 20)
                        ? new Color(0, 0, 25, 128).getRGB()
                        : new Color(0, 0, 25, 75).getRGB());
        this.drawRect(width - 50, height + 25, width + 50, height + 50,
                !this.crackedLogin && this.isInsideBox(mouseX, mouseY, width - 50, height + 25, width + 50, height + 50)
                        ? new Color(0, 0, 25, 128).getRGB()
                        : new Color(0, 0, 25, 75).getRGB());
        this.drawStringWithShadow("Login (Premium)", width - (this.getStringWidth("Login (Premium)", 8) / 2),
                height + 4, -1);
        this.drawStringWithShadow("Login (Cracked)", width - (this.getStringWidth("Login (Cracked)", 8) / 2),
                height + 34, -1);

        if (this.crackedLogin) {
            this.drawRect(0, 0, this.width, this.height, new Color(0, 0, 0, 175).getRGB());
            this.drawString("Name: " + this.name + (this.flag ? (this.name.length() > 15 ? "|" : "_") : ""), 20,
                    height - 16, 32, -1);
            this.drawString("Press the enter key when done", 20, height + 20, new Color(128, 128, 128).getRGB());

            if (System.currentTimeMillis() > (this.time + 350)) {
                this.flag = !this.flag;
                this.time = System.currentTimeMillis();
            }
        }

        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    /**
     * Called when the mouse is clicked. Args : mouseX, mouseY, clickedButton
     */
    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        float width = this.width / 2;
        float height = this.height / 2;

        if (this.isInsideBox(mouseX, mouseY, width - 50, height - 5, width + 50, height + 20) && mouseButton == 0) {
            try {
                boolean fullscreen = this.mc.isFullScreen();

                if (fullscreen) {
                    this.mc.toggleFullscreen();
                }

                MicrosoftAuthenticator microsoftAuthenticator = new MicrosoftAuthenticator();
                MicrosoftAuthResult microsoftAuthResult = microsoftAuthenticator.loginWithWebview();
                this.mc.setSession(new Session(microsoftAuthResult.getProfile().getName(),
                        microsoftAuthResult.getProfile().getId(), microsoftAuthResult.getAccessToken(), "mojang"));
                FileWriter fileWriter = null;

                try {
                    if (!Apple.ACCOUNT.exists()) {
                        Apple.ACCOUNT.createNewFile();
                    }

                    fileWriter = new FileWriter(Apple.ACCOUNT);
                    fileWriter.write(
                            microsoftAuthResult.getRefreshToken() + "\n" + microsoftAuthResult.getProfile().getId()
                                    + "\n" + microsoftAuthResult.getProfile().getName());
                }

                catch (Exception e) {
                    ;
                }

                finally {
                    if (fileWriter != null) {
                        try {
                            fileWriter.close();
                        }

                        catch (Exception e) {
                            ;
                        }
                    }
                }

                this.mc.logger.info("Setting user: " + this.mc.getSession().getUsername());
                this.mc.logger.info("(Session ID is " + this.mc.getSession().getCensoredSessionID() + ")");
                this.mc.displayGuiScreen(Main.firstTimeUser ? new TutorialGUI() : new GuiMainMenu());
                Main.firstTimeUser = false;
                Apple.CLIENT.sendAddUUIDRequestToServer();

                if (fullscreen) {
                    this.mc.toggleFullscreen();
                }
            }

            catch (Exception e) {
                ;
            }
        }

        if (this.isInsideBox(mouseX, mouseY, width - 50, height + 25, width + 50, height + 50) && mouseButton == 0) {
            this.crackedLogin = true;
        }
    }

    /**
     * Fired when a key is typed (except F11 which toggles full screen). This is the
     * equivalent of KeyListener.keyTyped(KeyEvent e). Args : character (character
     * on the key), keyCode (lwjgl Keyboard key code)
     */
    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException {
        if (keyCode == 1 && this.crackedLogin) {
            this.crackedLogin = false;
        }

        else if (keyCode == 28 && this.crackedLogin) {
            String uuid = this.getUUID(this.name);
            this.mc.setSession(new Session(this.name, uuid, "0", "legacy"));
            FileWriter fileWriter = null;

            try {
                if (!Apple.ACCOUNT.exists()) {
                    Apple.ACCOUNT.createNewFile();
                }

                fileWriter = new FileWriter(Apple.ACCOUNT);
                fileWriter.write("0\n" + uuid + "\n" + this.name);
            }

            catch (Exception e) {
                ;
            }

            finally {
                if (fileWriter != null) {
                    try {
                        fileWriter.close();
                    }

                    catch (Exception e) {
                        ;
                    }
                }
            }

            this.mc.logger.info("Setting user: " + this.mc.getSession().getUsername());
            this.mc.logger.info("(Session ID is " + this.mc.getSession().getCensoredSessionID() + ")");
            this.mc.displayGuiScreen(Main.firstTimeUser ? new TutorialGUI() : new GuiMainMenu());
            Main.firstTimeUser = false;
            Apple.CLIENT.sendAddUUIDRequestToServer();
            this.crackedLogin = false;
        }

        else if (this.crackedLogin) {
            if (keyCode == 14) {
                if (this.name.length() > 0) {
                    this.name = this.name.substring(0, this.name.length() - 1);
                }
            }

            else if (GuiScreen.isKeyComboCtrlV(keyCode)) {
                this.name += GuiScreen.getClipboardString();
            }

            else if (this.name.length() < 16) {
                this.name += this.getChar(typedChar);
            }
        }
    }

    @Override
    public void actionPerformed(GuiButton button) {
        if (button.id == 1) {
            FileWriter fileWriter = null;

            try {
                if (!Apple.ACCOUNT.exists()) {
                    Apple.ACCOUNT.createNewFile();
                }

                fileWriter = new FileWriter(Apple.ACCOUNT);
                fileWriter
                        .write("1\n" + this.mc.getSession().getPlayerID() + "\n" + this.mc.getSession().getUsername());
            }

            catch (Exception e) {
                ;
            }

            finally {
                if (fileWriter != null) {
                    try {
                        fileWriter.close();
                    }

                    catch (Exception e) {
                        ;
                    }
                }
            }

            this.mc.logger.info("Setting user: " + this.mc.getSession().getUsername());
            this.mc.logger.info("(Session ID is " + this.mc.getSession().getCensoredSessionID() + ")");
            this.mc.displayGuiScreen(Main.firstTimeUser ? new TutorialGUI() : new GuiMainMenu());
            Main.firstTimeUser = false;
            Apple.CLIENT.sendAddUUIDRequestToServer();
        }
    }

    @Override
    public void initGui() {
        super.initGui();
        this.buttonList.add(new GuiButton(1, this.width - 65, 5, 60, 20, "Skip"));
        Keyboard.enableRepeatEvents(true);
    }

    @Override
    public void onGuiClosed() {
        super.onGuiClosed();
        Keyboard.enableRepeatEvents(false);
    }

    public String getChar(char letter) {
        char[] validCharacters = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789".toCharArray();

        for (char temp : validCharacters) {
            if (letter == temp) {
                return String.valueOf(letter);
            }
        }

        return "";
    }

    public boolean isInsideBox(int mouseX, int mouseY, float x, float y, float width, float height) {
        return mouseX > x && mouseX < width && mouseY > y && mouseY < height;
    }
    
    public String getUUID(String name) {
        HttpsURLConnection httpsURLConnection = null;
        InputStreamReader inputStreamReader = null;
        BufferedReader bufferedReader = null;
        String uuid = name;

        try {
            httpsURLConnection = (HttpsURLConnection) new URL("https://api.mojang.com/users/profiles/minecraft/" + name)
                    .openConnection();
            httpsURLConnection.setDoInput(true);
            httpsURLConnection.setDoOutput(true);
            httpsURLConnection.connect();
            httpsURLConnection.getInputStream();
            inputStreamReader = new InputStreamReader(httpsURLConnection.getInputStream());
            bufferedReader = new BufferedReader(inputStreamReader);
            String line;

            while ((line = bufferedReader.readLine()) != null) {
                line = line.trim();

                if (line.contains("\"id\" : ")) {
                    uuid = line.substring(8, line.length() - 2);
                }
            }
        }

        catch (Exception e) {
            ;
        }

        finally {
            if (bufferedReader != null) {
                try {
                    bufferedReader.close();
                }

                catch (Exception e) {
                    ;
                }
            }

            if (inputStreamReader != null) {
                try {
                    inputStreamReader.close();
                }

                catch (Exception e) {
                    ;
                }
            }

            if (httpsURLConnection != null) {
                httpsURLConnection.disconnect();
            }
        }

        return uuid;
    }
}
