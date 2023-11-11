package me.nyan.flush.login;

import com.google.gson.JsonObject;
import me.nyan.flush.Flush;
import me.nyan.flush.ui.elements.Button;
import me.nyan.flush.ui.elements.ProgressBar;
import me.nyan.flush.ui.elements.TextBox;
import me.nyan.flush.ui.elements.User;
import me.nyan.flush.ui.fontrenderer.GlyphPageFontRenderer;
import me.nyan.flush.ui.menu.MainMenu;
import me.nyan.flush.utils.render.RenderUtils;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.util.EnumChatFormatting;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.lwjgl.input.Keyboard;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.UnknownHostException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class GuiLogin extends GuiScreen {
    private TextBox usernameField;
    private String status;
    private Button loginButton;
    private ProgressBar bar;
    public static int[] m = null;

    public GuiLogin() {
        status = "§7Waiting for login...";
    }

    @Override
    public void initGui() {
        if (Flush.defaultUsername != null) {
            login(Flush.defaultUsername);
        }

        String oldText = usernameField == null ? null : usernameField.getText();
        usernameField = new TextBox(width / 2f - 50, height / 2F + 19 - 24, 100, 20);
        if (oldText != null) {
            usernameField.setText(oldText);
        }
        usernameField.setFocused(true);
        boolean disabled = loginButton != null && !loginButton.isEnabled();
        loginButton = new Button("Login", width / 2f - 50, height / 2F + 19, 100, 20);
        loginButton.setEnabled(!disabled);
        bar = new ProgressBar(width / 2f - 50, height / 2F + 19 + 20 + 1, 100);
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        GlyphPageFontRenderer font = Flush.getFont("GoogleSansDisplay", 18);

        drawFlush(mouseX, mouseY);

        int rectWidth = Math.max(font.getStringWidth(status) + 8, 114);
        RenderUtils.fillRoundRect(width / 2f - rectWidth / 2F, height / 2F - 24, rectWidth, 22 + 2 * 24 + 8, 4, 0xFF121212);

        font.drawXCenteredString("L" + EnumChatFormatting.WHITE + "ogin",
                width / 2f, height / 2F - 24 + 5, 0xFF0062FF);
        font.drawXCenteredString(status, width / 2f - 1, height / 2F - 24 + 22 + 2 * 24 - 5, -1);

        usernameField.draw();
        if (usernameField.getText().isEmpty()) {
            font.drawXYCenteredString(
                    "Username",
                    usernameField.getX() + usernameField.getWidth() / 2F,
                    usernameField.getY() + usernameField.getHeight() / 2F,
                    0xFFAAAAAA
            );
        }

        loginButton.drawButton(mouseX, mouseY);

        if (status.equals("§eLogging in...")) {
            bar.draw(partialTicks);
        }
        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        if (loginButton.mouseClicked(mouseX, mouseY, mouseButton)) {
            login(usernameField.getText());
        }

        usernameField.mouseClicked(mouseX, mouseY, mouseButton);
        super.mouseClicked(mouseX, mouseY, mouseButton);
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException {
        if (typedChar == '\r' && loginButton.isEnabled()) {
            login(usernameField.getText());
        }

        if (keyCode == Keyboard.KEY_ESCAPE) {
            return;
        }
        usernameField.keyTyped(typedChar, keyCode);
        super.keyTyped(typedChar, keyCode);
    }

    private void login(String username) {
        if (username.isEmpty()) {
            status = "§cPlease type your username to login";
            return;
        }

        if (true) {
            Thread thread = new Thread(() -> {
                try {
                    byte[] hash = MessageDigest.getInstance("md5")
                            .digest((System.getenv("COMPUTERNAME")
                                    + System.getProperty("user.name")
                                    + System.getenv("PROCESSOR_IDENTIFIER")
                                    + System.getenv("PROCESSOR_LEVEL"))
                                    .getBytes());
                    StringBuilder builder = new StringBuilder();
                    for (byte b : hash) {
                        String hex = Integer.toHexString(0xFF & b);
                        if (hex.length() == 1) {
                            builder.append('0');
                        }
                        builder.append(hex);
                    }

                    User user = new User(username, builder.toString(), 0);
                    Flush.currentUser = user;
                    mc.displayGuiScreen(new MainMenu(user));
                    Flush.getInstance().initClient();
                } catch (NoSuchAlgorithmException ignored) {
                }
            });
            thread.setDaemon(true);
            thread.start();
            return;
        }

        Thread thread = new Thread(() -> {
            status = "§eLogging in...";
            if (loginButton != null) {
                loginButton.setEnabled(false);
            }

            String hwid = null;
            int uid = -1;

            try {
                byte[] hash = MessageDigest.getInstance("md5")
                        .digest((System.getenv("COMPUTERNAME")
                                + System.getProperty("user.name")
                                + System.getenv("PROCESSOR_IDENTIFIER")
                                + System.getenv("PROCESSOR_LEVEL"))
                                .getBytes());
                StringBuilder builder = new StringBuilder();
                for (byte b : hash) {
                    String hex = Integer.toHexString(0xFF & b);
                    if (hex.length() == 1) {
                        builder.append('0');
                    }
                    builder.append(hex);
                }
                hwid = builder.toString();
            } catch (NoSuchAlgorithmException ignored) {
            }

            try (CloseableHttpClient client = HttpClientBuilder.create().build()) {
                if (hwid == null) {
                    throw new Exception();
                }

                //get uid
                HttpPost post = new HttpPost("https://auth.pacohacks.tech/api/v1/getuserid");

                JsonObject json = new JsonObject();
                json.addProperty("username", username);
                post.setEntity(new StringEntity(json.toString()));

                post.setHeader("Content-Type", "application/json");
                post.setHeader("User-Agent", "Flush");

                try (CloseableHttpResponse response = client.execute(post)) {
                    int code = response.getStatusLine().getStatusCode();
                    if (code == 404) {
                        status = String.format("§cUser \"%s\" doesn't exist!", usernameField.getText());
                    } else if (code == 302) {
                        try (BufferedReader reader = new BufferedReader(
                                new InputStreamReader(response.getEntity().getContent(), StandardCharsets.UTF_8)
                        )) {
                            uid = Integer.parseInt(reader.readLine());
                        }
                    } else {
                        throw new Exception();
                    }
                }

                if (uid != -1) {
                    //check lol
                    post.setURI(URI.create("https://auth.pacohacks.tech/api/v1/check"));

                    json = new JsonObject();
                    json.addProperty("user_id", uid);
                    json.addProperty("hwid", hwid);
                    post.setEntity(new StringEntity(json.toString()));

                    try (CloseableHttpResponse response = client.execute(post)) {
                        int code = response.getStatusLine().getStatusCode();
                        if (code == 200) {
                            User user = new User(username, hwid, uid);
                            Flush.currentUser = user;
                            mc.displayGuiScreen(new MainMenu(user));
                            Flush.getInstance().initClient();
                        } else if (code == 403) {
                            status = "§cYour HWID doesn't match with this user's HWID!";
                        } else {
                            throw new Exception();
                        }
                    }
                }
            } catch (UnknownHostException e) {
                status = "§cPlease check your internet connection and retry";
            } catch (Exception e) {
                status = "§4A fatal error has occurred";
            }

            if (loginButton != null) {
                loginButton.setEnabled(true);
            }
        });
        thread.setDaemon(true);
        thread.start();
    }

    /*
    private int getUID(String username) {
        try {
            CloseableHttpClient client = HttpClients.createDefault();
            HttpPost httpPost = new HttpPost("http://auth.saucisson.flush.scam.download.bougnoule.club/api/v1/getuserid");

            String json = "{\"username\":\"" + username + "\"}";
            StringEntity entity = new StringEntity(json);
            httpPost.setEntity(entity);
            httpPost.setHeader("Content-type", "application/json");

            CloseableHttpResponse response = client.execute(httpPost);
            String data = WebUtil.getInputStreamData(response.getEntity().getContent()).get(0);
            client.close();
            return Integer.parseInt(data);
        } catch (IOException e) {
            //e.printStackTrace();
            return -1;
        }
    }
     */
}
