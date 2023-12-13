package info.sigmaclient.gui.screen.impl.mainmenu;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import info.sigmaclient.Client;
import info.sigmaclient.gui.altmanager.GuiAltManager;
import info.sigmaclient.gui.screen.component.GuiMenuButton;
import info.sigmaclient.gui.screen.component.particles.ParticleManager;
import info.sigmaclient.management.users.impl.Staff;
import info.sigmaclient.management.users.impl.Upgraded;
import info.sigmaclient.util.render.Colors;
import info.sigmaclient.util.render.TTFFontRenderer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.*;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.ResourceLocation;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class GuiModdedMainMenu extends ClientMainMenu {

    private ParticleManager particles;
    private ResourceLocation text = new ResourceLocation("textures/big.png");

    private String SESSION_STATUS = "Sessions status \2477- \247fChecking...";
    private String WEBSITE_STATUS = "Website status \2477- \247fChecking...";
    private String AUTH_STATUS = "Auth status \2477- \247fChecking...";

    @Override
    public void initGui() {
        super.initGui();
        particles = new ParticleManager();
        SESSION_STATUS = "Sessions status \2477- \247fChecking...";
        WEBSITE_STATUS = "Website status \2477- \247fChecking...";
        AUTH_STATUS = "Auth status \2477- \247fChecking...";
        String strSSP = I18n.format("Singleplayer");
        String strSMP = I18n.format("Multiplayer");
        String strOptions = I18n.format("Options");
        String strQuit = I18n.format("Exit");
        String strLang = I18n.format("Language");
        String strAccounts = "Accounts";
        int initHeight = this.height / 2 - 50;
        int objHeight = 50;
        int objWidth = 50;
        int xMid = width / 2 - 55;
        buttonList.add(new GuiMenuButton(0, xMid - 90, initHeight, objWidth, objHeight, strSSP));
        buttonList.add(new GuiMenuButton(1, xMid - 30, initHeight, objWidth, objHeight, strSMP));
        buttonList.add(new GuiMenuButton(2, xMid + 30, initHeight, objWidth, objHeight, strOptions));
        buttonList.add(new GuiMenuButton(3, xMid + 90, initHeight, objWidth, objHeight, strLang));
        buttonList.add(new GuiMenuButton(4, xMid - 65, initHeight + 75, objWidth, objHeight, strAccounts));
        buttonList.add(new GuiMenuButton(5, xMid + 65, initHeight + 75, objWidth, objHeight, strQuit));
        GuiButton guiButton = new GuiButton(6, xMid + 15, initHeight + 100, 70, 20, "Authenticate");
        this.buttonList.add(guiButton);
        usernameField = new GuiTextField(this.eventButton, this.mc.fontRendererObj, xMid + 15, initHeight + 80, 70, 14);
        usernameField.setEnabled(!(Client.um.getUser() instanceof Upgraded || Client.um.getUser() instanceof Staff));
        guiButton.enabled = !(Client.um.getUser() instanceof Upgraded || Client.um.getUser() instanceof Staff);
        new Thread(() -> {
            try {
                URL authURL = new URL("https://authserver.mojang.com/");
                BufferedReader reader = new BufferedReader(new InputStreamReader(authURL.openStream()));
                StringBuilder str = new StringBuilder();
                str.append(reader.readLine());
                Gson gson = new Gson();
                JsonObject array = gson.fromJson(str.toString(), JsonObject.class);
                if (array.get("Status").getAsString().equals("OK")) {
                    AUTH_STATUS = "Auth status \2477- \247aOnline";
                }
            } catch (Exception e) {
                AUTH_STATUS = "Auth status \2477- \247cDOWN";
            }
            try {
                URL authURL = new URL("https://sessionserver.mojang.com/");
                BufferedReader reader = new BufferedReader(new InputStreamReader(authURL.openStream()));
                StringBuilder str = new StringBuilder();
                str.append(reader.readLine());
                Gson gson = new Gson();
                JsonObject array = gson.fromJson(str.toString(), JsonObject.class);
                if (array.get("Status").getAsString().equals("OK")) {
                    SESSION_STATUS = "Session status \2477- \247aOnline";
                }
            } catch (Exception e) {
                SESSION_STATUS = "Session status \2477- \247cDOWN";
            }
            try {
                URL authURL = new URL("https://sessionserver.mojang.com/");
                BufferedReader reader = new BufferedReader(new InputStreamReader(authURL.openStream()));
                StringBuilder str = new StringBuilder();
                str.append(reader.readLine());
                Gson gson = new Gson();
                JsonObject array = gson.fromJson(str.toString(), JsonObject.class);
                if (array.get("Status").getAsString().equals("OK")) {
                    WEBSITE_STATUS = "Stop decrypting strings.";
                }
            } catch (Exception e) {
                WEBSITE_STATUS = "Stop decrypting strings.";
            }
        }).start();
    }

    private GuiTextField usernameField;

    @Override
    protected void keyTyped(final char par1, final int par2) {
        usernameField.textboxKeyTyped(par1, par2);
        if ((par1 == '\t' || par1 == '\r') && usernameField.isFocused()) {
            usernameField.setFocused(!usernameField.isFocused());
        }
        try {
            super.keyTyped(par1, par2);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void mouseClicked(final int par1, final int par2, final int par3) {
        if(usernameField.isEnabled())
            usernameField.mouseClicked(par1, par2, par3);
        try {
            super.mouseClicked(par1, par2, par3);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void actionPerformed(GuiButton button) throws IOException {
        if (button.id == 0) {
            mc.displayGuiScreen(new GuiSelectWorld(this));
        } else if (button.id == 1) {
            mc.displayGuiScreen(new GuiMultiplayer(this));
        } else if (button.id == 2) {
            mc.displayGuiScreen(new GuiOptions(this, mc.gameSettings));
        } else if (button.id == 3) {
            mc.displayGuiScreen(new GuiLanguage(this, mc.gameSettings, mc.getLanguageManager()));
        } else if (button.id == 4) {
            mc.displayGuiScreen(new GuiAltManager());
        } else if (button.id == 5) {
            mc.shutdown();
        } else if (button.id == 6) {
            if (!usernameField.getText().isEmpty()) {
                Client.um.checkUserStatus(usernameField.getText());
            }
        }
    }

    private ResourceLocation background = new ResourceLocation("textures/mainmenubackground.png");

    private float currentX, targetX, currentY, targetY;

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        ScaledResolution sr = new ScaledResolution(Minecraft.getMinecraft(), Minecraft.getMinecraft().displayWidth, Minecraft.getMinecraft().displayHeight);
        int w = sr.getScaledWidth();
        int h = sr.getScaledHeight();

        GlStateManager.pushMatrix();
        float xDiff = ((mouseX - sr.getScaledWidth() / 2) - currentX) / sr.getScaleFactor();
        float yDiff = ((mouseY - sr.getScaledHeight() / 2) - currentY) / sr.getScaleFactor();

        currentX += xDiff * 0.3F;
        currentY += yDiff * 0.3F;
        GlStateManager.translate(currentX / 100, currentY / 100, 0);
        mc.getTextureManager().bindTexture(background);
        drawScaledCustomSizeModalRect(-50, -50, 0, 0, w + 90, h + 90, w + 90, h + 90, w + 90, h + 90);
        GlStateManager.bindTexture(0);

        GlStateManager.translate(-currentX / 100, -currentY / 100, 0);
        GlStateManager.translate(currentX / 50, currentY / 50, 0);

        GlStateManager.enableBlend();
        mc.getTextureManager().bindTexture(text);
        drawModalRectWithCustomSizedTexture(w / 2 - 80, height / 2 - 110, 0, 0, 150, 195 / 4, 150, 195 / 4);
        GlStateManager.disableBlend();
        GlStateManager.translate(-currentX / 50, -currentY / 50, 0);
        GlStateManager.pushMatrix();
        GlStateManager.translate(currentX / 15, currentY / 15, 0);
        particles.render(mouseX, mouseY);
        GlStateManager.translate(-currentX / 15, -currentY / 15, 0);
        GlStateManager.popMatrix();
        GlStateManager.translate(currentX / 50, currentY / 50, 0);
        for (Object o : this.buttonList) {
            GuiButton button = (GuiButton) o;
            button.drawButton(mc, mouseX, mouseY);
        }
        int initHeight = this.height / 2 - 50;
        int xMid = width / 2 - 55;
        usernameField.drawTextBox();
        if (usernameField.getText().isEmpty() && !usernameField.isFocused())
            mc.fontRendererObj.drawStringWithShadow("\2477Username", xMid + 18, initHeight + 83, Colors.getColor(0));
        GlStateManager.popMatrix();

        TTFFontRenderer font = Client.fm.getFont("SFL 10");

        String str = "Hello \247a" + Client.um.getUser().getName() + "\247f!";
        font.drawStringWithShadow(str, 2, height - 20, -1);


        String version = Client.outdated ? "\247cYour client is \2474\247lOutdated\247r\247c. Please download the latest version on discord." : "You are using the latest version.";
        font.drawStringWithShadow(version, 2, height - 10, -1);

        float widthS = font.getWidth(SESSION_STATUS);
        font.drawStringWithShadow(SESSION_STATUS, width - widthS - 2, height - 10, -1);

        float widthA = font.getWidth(AUTH_STATUS);
        font.drawStringWithShadow(AUTH_STATUS, width - widthA - 2, height - 22, -1);
        String add = "\247a\247l+ \247r";
        String change = "\2476\247l~ \247r";
        String fix = "\247b\247l> \247r";
        String rem = "\247c\247l- \247r";
        List<String> changeLog = Arrays.asList(change + "Fixed Authentication Bug(s)", rem + "Removed Herobrine", add + "Added Toggle Blur (InGame Pause)", change + "Changed a lot of stuff.", rem + "Forgot to remember what I did.");
        TTFFontRenderer tFont = Client.fm.getFont("SFL 8");
        changeLog.sort(Comparator.comparingDouble(tFont::getWidth));
        Collections.reverse(changeLog);
        tFont.drawStringWithShadow("--- " + Client.version + " ChangeLog ---", width - tFont.getWidth("--- " + Client.version + " ChangeLog ---"),3,-1);
        int y = 13;
        for(String string : changeLog) {
            float strWidth = tFont.getWidth(string);
            tFont.drawStringWithShadow(string, width - strWidth, y, -1);
            y += 10;
        }

        tFont.drawStringWithShadow("Original design by Louise.", 1, 3, -1);
    }
}
