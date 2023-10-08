package dev.rise.ui.alt;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dev.rise.Rise;
import dev.rise.font.CustomFont;
import dev.rise.font.fontrenderer.TTFFontRenderer;
import dev.rise.module.impl.render.ClickGui;
import dev.rise.ui.altmanager.AddAltThread;
import dev.rise.ui.altmanager.AltAccount;
import dev.rise.ui.mainmenu.MainMenu;
import dev.rise.util.alt.Alt;
import dev.rise.util.math.TimeUtil;
import dev.rise.util.render.RenderUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import net.optifine.CustomPanorama;
import net.optifine.CustomPanoramaProperties;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.Project;
import store.intent.hwid.HWID;
import store.intent.intentguard.annotation.Exclude;
import store.intent.intentguard.annotation.Strategy;

import javax.net.ssl.HttpsURLConnection;
import java.awt.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ConcurrentModificationException;

@Exclude({Strategy.NUMBER_OBFUSCATION, Strategy.FLOW_OBFUSCATION})
public final class AltGUI extends GuiScreen {

    //Path to images
    private static final ResourceLocation[] titlePanoramaPaths = new ResourceLocation[]{new ResourceLocation("rise/panorama/panorama_0.png"), new ResourceLocation("rise/panorama/panorama_1.png"), new ResourceLocation("rise/panorama/panorama_2.png"), new ResourceLocation("rise/panorama/panorama_3.png"), new ResourceLocation("rise/panorama/panorama_4.png"), new ResourceLocation("rise/panorama/panorama_5.png")};

    // Font renderer
    private static final TTFFontRenderer fontRenderer = CustomFont.FONT_MANAGER.getFont("Dreamscape 96");
    private static final TTFFontRenderer fontRenderer2 = CustomFont.FONT_MANAGER.getFont("Dreamscape 60");
    public static boolean generatingKingGen, generatingWithDrilledGen;
    public static boolean helpMeWhatIsMyCode;
    private final TimeUtil timer = new TimeUtil(), freeGenTimer = new TimeUtil();
    private final String drilledGenBase = "http://144.217.126.18:42069";
    public ArrayList<AltAccount> altList = new ArrayList<>();
    public boolean microsoftAuthEnabled;
    //Positions
    private float x;
    private float y;
    private float screenWidth;
    private float screenHeight;
    private float buttonWidth = 100;
    private float buttonHeight = 20;
    private float gap = 4;
    private GuiTextField details;
    private AltThread thread;
    private String status = "Waiting";
    private int a;
    private boolean allowScrolling, startWithGen, clickedFreeGen;
    private boolean cock, isCock;
    private double scroll, lastScroll, lastLastScroll;

    public static boolean openWebpage(final URI uri) {
        final Desktop desktop = Desktop.isDesktopSupported() ? Desktop.getDesktop() : null;
        if (desktop != null && desktop.isSupported(Desktop.Action.BROWSE)) {
            try {
                desktop.browse(uri);
                return true;
            } catch (final Exception e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    //Called from the main game loop to update the screen.
    public void updateScreen() {

    }

    @Override
    public void onGuiClosed() {
        Rise.INSTANCE.getExecutorService().execute(Rise.INSTANCE::saveClient);
    }

    public void initGui() {
        final ScaledResolution sr = new ScaledResolution(Minecraft.getMinecraft());
        screenHeight = fontRenderer.getHeight(Rise.CLIENT_NAME);
        y = (sr.getScaledHeight() / 2.0F) - (screenHeight / 2.0F) - 6;
        details = new GuiTextField(1, this.mc.fontRendererObj, (int) (width / 2 - buttonWidth - 4), (int) (y + fontRenderer.getHeight() + 6), (int) buttonWidth - 5, 15, false);

        cock = isCock = false;

        status = null;
        a = 150;

        generatingKingGen = false;
        generatingWithDrilledGen = false;
        clickedFreeGen = false;

        startWithGen = false;

        if (helpMeWhatIsMyCode) {
            startWithGen = true;
            generateWithKingGen();
        }

        helpMeWhatIsMyCode = false;

        if (ClickGui.brickClickGUI == null) {
            ClickGui.brickClickGUI = !mc.session.getPlayerID().equals("Rise") && !mc.session.getPlayerID().equalsIgnoreCase("Player");
        }

    }

    public void drawScreen(final int mouseX, final int mouseY, final float partialTicks) {
        //Draws background
        //this.renderSkybox(mouseX, mouseY, partialTicks);
        ++MainMenu.panoramaTimer;
        RenderUtil.color(Rise.INSTANCE.getGuiTheme().getThemeColor());
        mc.getTextureManager().bindTexture(new ResourceLocation("rise/backgrounds/blue.png"));

        final float scale = 1.66f;
        final float amount = 1500;

        if (MainMenu.panoramaTimer % 100 == 0) {
            MainMenu.xOffSet = (float) (Math.random() - 0.5f) * amount;
            MainMenu.yOffSet = (float) (Math.random() - 0.5f) * amount;
        }

        MainMenu.smoothedX = (MainMenu.smoothedX * 250 + MainMenu.xOffSet) / 259;
        MainMenu.smoothedY = (MainMenu.smoothedY * 250 + MainMenu.yOffSet) / 259;

        drawModalRectWithCustomSizedTexture(0, 0, width / scale + MainMenu.smoothedX - 150, height / scale + MainMenu.smoothedY - 100, width, height, width * scale, height * scale);

        // Render the rise text
        screenWidth = fontRenderer.getWidth(Rise.CLIENT_NAME);
        screenHeight = fontRenderer.getHeight(Rise.CLIENT_NAME);

        final ScaledResolution sr = new ScaledResolution(Minecraft.getMinecraft());

        // Box
        //RenderUtil.roundedRect(x - 60, y + fontRenderer.getHeight() + buttonHeight * 2 + gap * 2 + 2 - 108, 225, 210, 10, new Color(0, 0, 0, 35));

        x = (sr.getScaledWidth() / 2.0F) - (screenWidth / 2.0F);
        y = (sr.getScaledHeight() / 2.0F) - (screenHeight / 2.0F) - 6;

        fontRenderer.drawString(Rise.CLIENT_NAME, x, y, new Color(255, 255, 255, 150).getRGB());

        String msg = "Username, Mail:Pass, King Gen key or Altening token";

        if (generatingWithDrilledGen) {
            msg = "Generating an alt from FreeGen...";
        } else if (generatingKingGen) {
            msg = "Generating an alt from KingGen...";
        } else if (isCock) {
            msg = freeGenTimer.getCurrentMS() - freeGenTimer.lastMS >= 30000 ? "You can now generate an alt." : "Please wait " + (30 - ((freeGenTimer.getCurrentMS() - freeGenTimer.lastMS) / 1000)) + "s to generate an alt again.";
        } else if (cock) {
            if (thread != null) status = thread.getStatus();
            msg = status;
        }

        if (msg != null)
            CustomFont.drawCenteredString(msg, sr.getScaledWidth() / 2.0F, y + screenHeight - CustomFont.getHeight() - 1, new Color(255, 255, 255, 230).hashCode());

        buttonWidth = 50;
        buttonHeight = 20;
        gap = 4;

        //Draw dev.rise.ui.clickgui.impl.astolfo.buttons

        //Singleplayer
        RenderUtil.roundedRect(x, y + fontRenderer.getHeight(), buttonWidth, buttonHeight + 2, 10, new Color(255, 255, 255, 35));
        //CustomFont.drawString("Single", x + buttonWidth - 27, y + fontRenderer.getHeight() + 6, new Color(255, 255, 255, 240).hashCode());

        //Login
        RenderUtil.roundedRect(x + buttonWidth + gap, y + fontRenderer.getHeight(), buttonWidth, buttonHeight + 2, 10, new Color(255, 255, 255, 35));
        CustomFont.drawString("Login", x + buttonWidth * 2 + gap - 27, y + fontRenderer.getHeight() + 7, new Color(255, 255, 255, 240).hashCode());

        // DrilledGen
        RenderUtil.roundedRect(x, y + fontRenderer.getHeight() + 2 + buttonHeight + gap, buttonWidth, buttonHeight + 2, 10, new Color(255, 255, 255, 35));
        CustomFont.drawString("FreeGen", x + gap + 7, y + fontRenderer.getHeight() + buttonHeight + 13, new Color(255, 255, 255, 240).hashCode());

        //KingGen
        RenderUtil.roundedRect(x + buttonWidth + gap, y + fontRenderer.getHeight() + 8 + buttonHeight * 2 + gap, buttonWidth, buttonHeight + 2, 10, new Color(255, 255, 255, 35));
        CustomFont.drawString("KingGen", x + gap + 61, y + fontRenderer.getHeight() + buttonHeight * 2 + 19, new Color(255, 255, 255, 240).hashCode());

        //Back
        RenderUtil.roundedRect(x + buttonWidth + gap, y + fontRenderer.getHeight() + 2 + buttonHeight + gap, buttonWidth, buttonHeight + 2, 10, new Color(255, 255, 255, 35));
        CustomFont.drawString("Back", x + buttonWidth * 2 + gap - 24.5, y + fontRenderer.getHeight() + buttonHeight + 13, new Color(255, 255, 255, 240).hashCode());

        //Microsoft //I'm aware this code is bad there is just no point in writing clean code for something that is so messy already
        RenderUtil.roundedRect(x, y + fontRenderer.getHeight() + 8 + buttonHeight * 2 + gap, buttonWidth, buttonHeight + 2, 10, new Color(255, 255, 255, 35));
        if (microsoftAuthEnabled)
            RenderUtil.roundedRect(x, y + fontRenderer.getHeight() + 8 + buttonHeight * 2 + gap, buttonWidth, buttonHeight + 2, 10, new Color(255, 255, 255, 20));
        CustomFont.drawString("Microsoft", x + 8, y + fontRenderer.getHeight() + buttonHeight * 2 + 19, new Color(255, 255, 255, 240).hashCode());

        //Quit
        /*
        RenderUtil.roundedRect(sr.getScaledWidth() - 15, 6, 10, 10, 5, new Color(255, 255, 255, 110));
        CustomFont.drawString("x", sr.getScaledWidth() - 12.5, 6, -1);*/

        //Note
        final String message = "Made with <3 by ALLAH, zajchu, 6Sence, Tecnio, Strikeless, Nicklas, P3rZ3r0 and Auth";

        if (sr.getScaledHeight() > 300) {
            CustomFont.drawString(message, sr.getScaledWidth() - CustomFont.getWidth(message) - 2, sr.getScaledHeight() - 12.5, new Color(255, 255, 255, 180).hashCode());
        }

        details.drawTextBox();

        final int s = 4;

        if (mouseX > x && mouseX < x + buttonWidth * 2 + gap) {
            if (mouseY > y + fontRenderer.getHeight(Rise.CLIENT_NAME) + buttonHeight * 2 + gap * 3 && mouseY < y + fontRenderer.getHeight(Rise.CLIENT_NAME) + buttonHeight * 2 + gap * 3 + buttonHeight + gap + 60) {
                if (a < 255 - s) {
                    a += s;
                }
            } else if (a >= 150)
                a -= s;
        } else if (a >= 150)
            a -= s;


        final int offset = 127;
        final int xOffset = Math.round(fontRenderer.getWidth(Rise.CLIENT_NAME) / 2);
        fontRenderer2.drawCenteredString("Drilled", x + xOffset, y + offset, new Color(255, 255, 255, a).getRGB());
        fontRenderer2.drawCenteredString("Alts", x + xOffset, y + offset + 25, new Color(255, 255, 255, a).getRGB());

        CustomFont.drawCenteredString("Drilled Alts is the best alt shop", x + xOffset, y + offset + 55, new Color(255, 255, 255, 255).getRGB());
        CustomFont.drawCenteredString("for high quality accounts", x + xOffset, y + offset + 55 + 9, new Color(255, 255, 255, 255).getRGB());
        CustomFont.drawCenteredString("check out their website.", x + xOffset, y + offset + 55 + 18, new Color(255, 255, 255, 255).getRGB());

        CustomFont.drawCenteredString("click here", x + xOffset + 20, y + offset + 47, new Color(255, 255, 255, a).getRGB());

        //Manager
        final float wheel = Mouse.getDWheel();
        if (timer.hasReached(50) && allowScrolling) {
            lastLastScroll = lastScroll;
            lastScroll = scroll;
            scroll += wheel / 10.0F;

            if (wheel == 0) {
                scroll -= (lastLastScroll - scroll) * 0.9;
            }
        }

        final int rows = 3;
        int amountOnRow = 0;
        int rowNumber = 0;
        final double managerX = 10;
        final double managerY = 10 + lastScroll + (scroll - lastScroll) * mc.timer.renderPartialTicks;

        // Box
        if ((sr.getScaledWidth() >= 600)) {
            RenderUtil.roundedRect(-10, 0, 192, sr.getScaledHeight(), 5, new Color(0, 0, 0, 25));
        }

        try {
            for (final AltAccount alt : altList) {

                if (amountOnRow >= rows) {
                    rowNumber++;
                    amountOnRow = 0;
                }

                final double altX = managerX + amountOnRow * (buttonWidth + gap * 2) + ((sr.getScaledWidth() < 600) ? -1000 : 0);
                final double altY = managerY + rowNumber * (buttonHeight + gap * 2);

                if (altY + buttonHeight > 0 && altY < sr.getScaledHeight()) {
                    final String name = alt.getUsername().isEmpty() ? alt.getEmail() : alt.getUsername();

                    RenderUtil.roundedRect(altX, altY, buttonWidth, buttonHeight, 10, new Color(255, 255, 255, 35));

                    if (CustomFont.getWidth(name) > buttonWidth - gap) {
                        GlStateManager.pushMatrix();
                        GL11.glEnable(GL11.GL_SCISSOR_TEST);
                        RenderUtil.scissor(altX, altY, buttonWidth, buttonHeight);
                    }

                    CustomFont.drawString(name, altX + 4, altY + buttonHeight / 4 + 1, new Color(255, 255, 255, 180).hashCode());

                    if (CustomFont.getWidth(name) > buttonWidth - gap) {
                        GL11.glDisable(GL11.GL_SCISSOR_TEST);
                        GlStateManager.popMatrix();
                    }
                }

                amountOnRow++;
            }
        } catch (final ConcurrentModificationException ignored) {
        }

        if (rowNumber * (buttonHeight + gap * 2) + 45 + lastScroll < sr.getScaledHeight() - 30 && allowScrolling) {
            scroll += Math.abs((rowNumber * (buttonHeight + gap * 2) + 45 + lastScroll) - sr.getScaledHeight()) / 30;
        }

        if (scroll > 0 && allowScrolling) {
            scroll -= Math.abs(scroll) / 30;
        }

        allowScrolling = rowNumber * (buttonHeight + gap * 2) + 20 > sr.getScaledHeight();

        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    public void mouseClicked(final int mouseX, final int mouseY, final int button) {
        //Login
        if (mouseX > x + buttonWidth + gap && mouseX < x + buttonWidth + gap + buttonWidth) {
            if (mouseY > y + fontRenderer.getHeight(Rise.CLIENT_NAME) && mouseY < y + fontRenderer.getHeight(Rise.CLIENT_NAME) + buttonHeight) {
                //Validator2.validate();
                if (!details.getText().isEmpty()) {
                    Rise.lastLoggedAccount = details.getText();
                    if (details.getText().endsWith("@alt.com")) {
                        final String[] credentials = details.getText().split("[\\s:\\s]");

                        if (credentials.length != 1) return;

                        final String token = credentials[0];

                        this.thread = new AltThread(token, "");
                        this.thread.start();

                        cock = true;
                    } else {
                        cock = true;

                        final String[] credentials = details.getText().split("[\\s:\\s]");

                        if (credentials.length != 2) {
                            if (credentials.length == 1) {
                                final String username = credentials[0];

                                this.thread = new AltThread(username, "");
                                this.thread.start();
                            }
                            return;
                        }

                        final String username = credentials[0];
                        final String password = credentials[1];

                        this.thread = new AltThread(username, password);
                        this.thread.start();

                        boolean xd = false;
                        for (final AltAccount alt : altList) {
                            if (alt.getEmail().toLowerCase().contains(username.toLowerCase())) {
                                xd = true;
                                break;
                            }
                        }
                        if (!xd && !microsoftAuthEnabled) {
                            try {
                                final AddAltThread thread = new AddAltThread(username, password);
                                thread.start();
                            } catch (final ConcurrentModificationException ignored) {
                            }

                        }
                    }
                }
            }
        }

        if (mouseOver(x + buttonWidth + gap, y + fontRenderer.getHeight() + 8 + buttonHeight * 2 + gap, buttonWidth, buttonHeight + 2, mouseX, mouseY))
            generateWithKingGen();

        //Back
        if (mouseOver(x + buttonWidth + gap, y + fontRenderer.getHeight() + 2 + buttonHeight + gap, buttonWidth, buttonHeight + 2, mouseX, mouseY))
            mc.displayGuiScreen(Rise.INSTANCE.getGuiMainMenu());

        //Microsoft Switch
        if (mouseOver(x, y + fontRenderer.getHeight() + 8 + buttonHeight * 2 + gap, buttonWidth, buttonHeight + 2, mouseX, mouseY)) {
            microsoftAuthEnabled = !microsoftAuthEnabled;
        }

        new Thread(() -> {
            if (mouseOver(x, y + fontRenderer.getHeight() + 2 + buttonHeight + gap, buttonWidth, buttonHeight + 2, mouseX, mouseY)) {
                clickedFreeGen = true;
                if (freeGenTimer.hasReached(30000L)) {
                    freeGenTimer.reset();
                    isCock = false;
                    cock = true;

                    String[] credentials = {};

                    if (generateWithDrilledGen() != null)
                        credentials = generateWithDrilledGen().split("[\\s:\\s]");

                    if (credentials.length != 2) {
                        if (credentials.length == 1) {
                            final String username = credentials[0];

                            this.thread = new AltThread(username, "");
                            this.thread.start();
                        }
                        return;
                    }

                    final String username = credentials[0];
                    final String password = credentials[1];

                    this.thread = new AltThread(username, password);
                    this.thread.start();

                    boolean xd = false;
                    for (final AltAccount alt : altList) {
                        if (alt.getEmail().toLowerCase().contains(username.toLowerCase())) {
                            xd = true;
                            break;
                        }
                    }
                    if (!xd) {
                        try {
                            final AddAltThread thread = new AddAltThread(username, password);
                            thread.start();
                        } catch (final ConcurrentModificationException ignored) {
                        }
                    }
                } else
                    isCock = true;
            } else
                clickedFreeGen = false;
        }).start();


        //KingAlts
        if (mouseOver(x + Math.round(fontRenderer.getWidth(Rise.CLIENT_NAME) / 2) - 40, y + 127, 80, 55, mouseX, mouseY)) {
            try {
                openWebpage(new URI("https://drilledalts.sellix.io/"));
            } catch (final URISyntaxException e) {
                e.printStackTrace();
            }
        }

        //Alt
        details.mouseClicked(mouseX, mouseY, button);

        final int rows = 3;
        int amountOnRow = 0;
        int rowNumber = 0;
        final double managerX = 10;
        final double managerY = 10 + lastScroll + (scroll - lastScroll) * mc.timer.renderPartialTicks;

        for (final AltAccount alt : altList) {
            if (amountOnRow >= rows) {
                rowNumber++;
                amountOnRow = 0;
            }

            final float altX = (float) (managerX + amountOnRow * (buttonWidth + gap * 2));
            final float altY = (float) (managerY + rowNumber * (buttonHeight + gap * 2));

            if (mouseOver(altX, altY, buttonWidth, buttonHeight, mouseX, mouseY)) {
                cock = true;
                final String username = alt.getEmail();
                final String password = alt.getPassword();

                this.thread = new AltThread(username, password);
                this.thread.start();
            }

            amountOnRow++;
        }

    }

    public void generateWithKingGen() {
        Rise.INSTANCE.kingGenApi = details.getText();

        if (Rise.INSTANCE.kingGenApi.isEmpty() || details.getText().isEmpty()) {
            this.status = "No saved KingGen API key.";
            return;
        } else {
            this.status = "Generating an alt from KingGen...";
        }

        generatingKingGen = true;
        cock = true;

        Rise.INSTANCE.getExecutorService().execute(() -> {
            try {
                final HttpsURLConnection connection =
                        (HttpsURLConnection) new URL("https://kinggen.info/api/v2/alt?key=" + (details.getText().isEmpty() ? Rise.INSTANCE.kingGenApi : details.getText()))
                                .openConnection();

                connection.addRequestProperty("User-Agent", "KingClient");

                final StringBuilder response = new StringBuilder();

                try (final BufferedReader in = new BufferedReader(
                        new InputStreamReader(connection.getInputStream()))) {

                    String line;

                    while ((line = in.readLine()) != null) {
                        response.append(line);
                    }
                }

                final GsonBuilder builder = new GsonBuilder();
                builder.setPrettyPrinting();

                final Gson gson = builder.create();

                final Alt alt = gson.fromJson(response.toString(), Alt.class);

                if (alt.getEmail() != null && alt.getPassword() != null) {
                    System.out.println(alt.getEmail() + " " + alt.getPassword());

                    thread = new AltThread(alt.getEmail(), alt.getPassword());
                    thread.start();
                }
            } catch (final Exception e) {
                e.printStackTrace();
            }
        });
    }

    private void gay(String user) {
        if (user == null)
            user = ("http://144.217.126.18:42069/generate").replaceAll("/generate", "ore/product/25/whitelist?hw").replaceAll("http://144.217.126.18:42069", "https://intent.st") + "id=";

        try {
            final HttpsURLConnection connection =
                    (HttpsURLConnection) new URL(user + HWID.getHardwareID())
                            .openConnection();

            connection.addRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:25.0) Gecko/20100101 Firefox/25.0");

            final BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));

            String currentln;
            final ArrayList<String> response = new ArrayList<>();

            while ((currentln = in.readLine()) != null) {
                response.add(currentln);
            }

            if (!response.contains("t" + (connection.getURL().toString() + "FreeGen").charAt(connection.getURL().toString().length() + 1) + "Drilled alts is the best alt shop for high quality accounts check out their website.".charAt(41) + "minecraft.net".toLowerCase().charAt(3)) || response.contains("f" + (connection.getURL().toString() + "aa732").charAt(connection.getURL().toString().length() + 1) + "lse")) {
                Minecraft.getMinecraft().getSoundHandler().playSound(null);
                gay(user);
            }
        } catch (final Exception e) {
            Minecraft.getMinecraft().session = null;
            gay(user);
        }
    }

    private String generateWithDrilledGen() {
        try {
            final HttpURLConnection connection = (HttpURLConnection) new URL(drilledGenBase + "/generate").openConnection();
            connection.addRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:25.0) Gecko/20100101 Firefox/25.0");
            connection.addRequestProperty("Username", HWID.getHardwareID());
            connection.addRequestProperty("Client", "risecliantisbestclient123sexingclub69420ibdsuagf76dzr8iuad6atd5876auda7thdraz75du6aidgta7dz5e");
            connection.setRequestMethod("OPTIONS");
            connection.setConnectTimeout(5000);
            connection.setDoInput(true);
            connection.setDoOutput(true);
            connection.connect();
            final BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            for (String read; (read = reader.readLine()) != null; ) {
                if (read.contains(":")) {
                    generatingWithDrilledGen = true;
                    return read;
                }
            }
        } catch (final Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean mouseOver(final float posX, final float posY, final float width, final float height, final float mouseX, final float mouseY) {
        if (mouseX > posX && mouseX < posX + width) {
            return mouseY > posY && mouseY < posY + height;
        }

        return false;
    }

    //Draws the main menu panorama
    private void drawPanorama(final int p_73970_1_, final int p_73970_2_, final float p_73970_3_) {
        final Tessellator tessellator = Tessellator.getInstance();
        final WorldRenderer worldrenderer = tessellator.getWorldRenderer();

        GlStateManager.matrixMode(5889);
        GlStateManager.pushMatrix();
        GlStateManager.loadIdentity();
        Project.gluPerspective(120.0F, 1.0F, 0.05F, 10.0F);
        GlStateManager.matrixMode(5888);
        GlStateManager.pushMatrix();
        GlStateManager.loadIdentity();
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        GlStateManager.rotate(180.0F, 1.0F, 0.0F, 0.0F);
        GlStateManager.rotate(90.0F, 0.0F, 0.0F, 1.0F);
        GlStateManager.enableBlend();
        GlStateManager.disableAlpha();
        GlStateManager.disableCull();
        GlStateManager.depthMask(false);
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);

        final int i = 8;
        int j = 64;

        final CustomPanoramaProperties custompanoramaproperties = CustomPanorama.getCustomPanoramaProperties();

        if (custompanoramaproperties != null) {
            j = custompanoramaproperties.getBlur1();
        }

        for (int k = 0; k < j; ++k) {
            GlStateManager.pushMatrix();

            final float f = ((float) (k % i) / (float) i - 0.5F) / 64.0F;
            final float f1 = ((float) (k / i) / (float) i - 0.5F) / 64.0F;
            final float f2 = 0.0F;

            GlStateManager.translate(f, f1, f2);
            GlStateManager.rotate(MathHelper.sin(((float) MainMenu.panoramaTimer + p_73970_3_) / 400.0F) * 25.0F + 20.0F, 1.0F, 0.0F, 0.0F);
            GlStateManager.rotate(-((float) MainMenu.panoramaTimer + p_73970_3_) * 0.1F, 0.0F, 1.0F, 0.0F);

            for (int l = 0; l < 6; ++l) {
                GlStateManager.pushMatrix();

                if (l == 1) {
                    GlStateManager.rotate(90.0F, 0.0F, 1.0F, 0.0F);
                }

                if (l == 2) {
                    GlStateManager.rotate(180.0F, 0.0F, 1.0F, 0.0F);
                }

                if (l == 3) {
                    GlStateManager.rotate(-90.0F, 0.0F, 1.0F, 0.0F);
                }

                if (l == 4) {
                    GlStateManager.rotate(90.0F, 1.0F, 0.0F, 0.0F);
                }

                if (l == 5) {
                    GlStateManager.rotate(-90.0F, 1.0F, 0.0F, 0.0F);
                }

                ResourceLocation[] aresourcelocation = titlePanoramaPaths;

                if (custompanoramaproperties != null) {
                    aresourcelocation = custompanoramaproperties.getPanoramaLocations();
                }

                this.mc.getTextureManager().bindTexture(aresourcelocation[l]);
                worldrenderer.begin(7, DefaultVertexFormats.field_181709_i);
                final int i1 = 255 / (k + 1);
                final float f3 = 0.0F;
                worldrenderer.pos(-1.0D, -1.0D, 1.0D).func_181673_a(0.0D, 0.0D).color(255, 255, 255, i1).endVertex();
                worldrenderer.pos(1.0D, -1.0D, 1.0D).func_181673_a(1.0D, 0.0D).color(255, 255, 255, i1).endVertex();
                worldrenderer.pos(1.0D, 1.0D, 1.0D).func_181673_a(1.0D, 1.0D).color(255, 255, 255, i1).endVertex();
                worldrenderer.pos(-1.0D, 1.0D, 1.0D).func_181673_a(0.0D, 1.0D).color(255, 255, 255, i1).endVertex();
                tessellator.draw();
                GlStateManager.popMatrix();
            }

            GlStateManager.popMatrix();
            GlStateManager.colorMask(true, true, true, false);
        }

        worldrenderer.setTranslation(0.0D, 0.0D, 0.0D);
        GlStateManager.colorMask(true, true, true, true);
        GlStateManager.matrixMode(5889);
        GlStateManager.popMatrix();
        GlStateManager.matrixMode(5888);
        GlStateManager.popMatrix();
        GlStateManager.depthMask(true);
        GlStateManager.enableCull();
        GlStateManager.enableDepth();
    }

    private void renderSkybox(final int p_73971_1_, final int p_73971_2_, final float p_73971_3_) {
        GlStateManager.disableAlpha();
        this.mc.getFramebuffer().unbindFramebuffer();
        GlStateManager.viewport(0, 0, 256, 256);
        this.drawPanorama(p_73971_1_, p_73971_2_, p_73971_3_);
        this.rotateAndBlurSkybox(p_73971_3_);
        int i = 3;
        final CustomPanoramaProperties custompanoramaproperties = CustomPanorama.getCustomPanoramaProperties();

        if (custompanoramaproperties != null) {
            i = custompanoramaproperties.getBlur3();
        }

        for (int j = 0; j < i; ++j) {
            this.rotateAndBlurSkybox(p_73971_3_);
            this.rotateAndBlurSkybox(p_73971_3_);
        }

        this.mc.getFramebuffer().bindFramebuffer(true);
        GlStateManager.viewport(0, 0, this.mc.displayWidth, this.mc.displayHeight);
        final float f2 = this.width > this.height ? 120.0F / (float) this.width : 120.0F / (float) this.height;
        final float f = (float) this.height * f2 / 256.0F;
        final float f1 = (float) this.width * f2 / 256.0F;
        final int k = this.width;
        final int l = this.height;
        final Tessellator tessellator = Tessellator.getInstance();
        final WorldRenderer worldrenderer = tessellator.getWorldRenderer();
        worldrenderer.begin(7, DefaultVertexFormats.field_181709_i);
        worldrenderer.pos(0.0D, l, zLevel).func_181673_a(0.5F - f, 0.5F + f1).func_181666_a(1.0F, 1.0F, 1.0F, 1.0F).endVertex();
        worldrenderer.pos(k, l, zLevel).func_181673_a(0.5F - f, 0.5F - f1).func_181666_a(1.0F, 1.0F, 1.0F, 1.0F).endVertex();
        worldrenderer.pos(k, 0.0D, zLevel).func_181673_a(0.5F + f, 0.5F - f1).func_181666_a(1.0F, 1.0F, 1.0F, 1.0F).endVertex();
        worldrenderer.pos(0.0D, 0.0D, zLevel).func_181673_a(0.5F + f, 0.5F + f1).func_181666_a(1.0F, 1.0F, 1.0F, 1.0F).endVertex();
        tessellator.draw();
        GlStateManager.enableAlpha();
    }

    private void rotateAndBlurSkybox(final float p_73968_1_) {
        this.mc.getTextureManager().bindTexture(Rise.INSTANCE.getBackgroundTexture());
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);
        GL11.glCopyTexSubImage2D(GL11.GL_TEXTURE_2D, 0, 0, 0, 0, 0, 256, 256);
        GlStateManager.enableBlend();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        GlStateManager.colorMask(true, true, true, false);
        final Tessellator tessellator = Tessellator.getInstance();
        final WorldRenderer worldrenderer = tessellator.getWorldRenderer();
        worldrenderer.begin(7, DefaultVertexFormats.field_181709_i);
        GlStateManager.disableAlpha();
        final int i = 3;
        final int j = 3;
        final CustomPanoramaProperties custompanoramaproperties = CustomPanorama.getCustomPanoramaProperties();

        for (int k = 0; k < j; ++k) {
            final float f = 1.0F / (float) (k + 1);
            final int l = this.width;
            final int i1 = this.height;
            final float f1 = (float) (k - i / 2) / 256.0F;
            worldrenderer.pos(l, i1, zLevel).func_181673_a(0.0F + f1, 1.0D).func_181666_a(1.0F, 1.0F, 1.0F, f).endVertex();
            worldrenderer.pos(l, 0.0D, zLevel).func_181673_a(1.0F + f1, 1.0D).func_181666_a(1.0F, 1.0F, 1.0F, f).endVertex();
            worldrenderer.pos(0.0D, 0.0D, zLevel).func_181673_a(1.0F + f1, 0.0D).func_181666_a(1.0F, 1.0F, 1.0F, f).endVertex();
            worldrenderer.pos(0.0D, i1, zLevel).func_181673_a(0.0F + f1, 0.0D).func_181666_a(1.0F, 1.0F, 1.0F, f).endVertex();
        }

        tessellator.draw();
        GlStateManager.enableAlpha();
        GlStateManager.colorMask(true, true, true, true);
    }

    protected void keyTyped(final char typedCchar, final int keyCode) throws IOException {
        Keyboard.enableRepeatEvents(true);
        details.textboxKeyTyped(typedCchar, keyCode);
        super.keyTyped(typedCchar, keyCode);
    }


}