package me.nyan.flush.altmanager;

import com.google.common.io.Files;
import me.nyan.flush.Flush;
import me.nyan.flush.ui.elements.Button;
import me.nyan.flush.ui.elements.TextBox;
import me.nyan.flush.utils.other.MouseUtils;
import me.nyan.flush.utils.render.RenderUtils;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.util.EnumChatFormatting;
import org.apache.commons.lang3.RandomStringUtils;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

import javax.swing.*;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class GuiAltManager extends GuiScreen {
    private final GuiScreen previousScreen;
    private final AltManager altmgr = Flush.getInstance().getAltManager();
    private final ArrayList<Button> buttons = new ArrayList<>();
    private AltLoginThread thread;
    private TextBox username;
    private TextBox password;
    private TextBox combo;
    private AccountInfo selectedAlt;
    private Button loginButton;
    private Button editButton;
    private Button deleteButton;
    private float scroll;
    private float scrollSpeed;

    public GuiAltManager(GuiScreen previousScreen) {
        this.previousScreen = previousScreen;
    }

    @Override
    public void initGui() {
        scroll = 0;
        selectedAlt = null;
        altmgr.load();

        username = new TextBox(30F, 38F, width / 2F - 32, 22);

        password = new TextBox(width / 2f + 4, 38F, width - (width / 2F + 34), 22);
        password.setPassword(true);

        combo = new TextBox(30F, 64F, width - 60, 22);
        Keyboard.enableRepeatEvents(true);

        altmgr.setStatus("Alt Manager");
        buttons.clear();
        buttons.add(new Button("Mojang", width - 60 - 6, 6, 60, 22F));
        buttons.add(new Button("Login", 30F, 92F, width / 2f - 32, 22F));
        buttons.add(new Button("Add Account", width / 2f + 2, 92F, width - (width / 2f + 32), 22F));
        buttons.add(new Button("Clipboard Login", 30F, 118F, width / 2f - 32, 22F));
        buttons.add(new Button("Generate Cracked Account", width / 2f + 2, 118F, width - (width / 2f + width / 4f + 16), 22F));
        buttons.add(new Button("Import alt list", width / 2f + width / 4f - 8, 118F, width - (width / 2f + width / 4f + 22), 22F));
        buttons.add(new Button("Back", 4F, 4F, 60F, 22F));
        loginButton = new Button("Login", width - 198F, (height - 70F), 192F, 18F);
        editButton = new Button("Edit", width - 198F, (height - 48F), 192F, 18F);
        deleteButton = new Button("Delete", width - 198F, (height - 26F), 192F, 18F);
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        for (Button button : buttons) {
            if (button.getName().equalsIgnoreCase("mojang") || button.getName().equalsIgnoreCase("microsoft")) {
                button.setName(altmgr.isMicrosoft() ? "Microsoft" : "Mojang");
                break;
            }
        }
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        float modulesMax = (float) (-height + 146 + altmgr.getAlts().size() * 42);
        if (Mouse.hasWheel()) {
            int wheel = Mouse.getDWheel();
            if (wheel > 0 && scroll > 0) {
                if (scrollSpeed > 0) {
                    scrollSpeed = 0;
                }
                scrollSpeed -= 10;
            } else if (wheel < 0) {
                if (scrollSpeed < 0) {
                    scrollSpeed = 0;
                }
                scrollSpeed += 10;
            } else {
                scrollSpeed -= scrollSpeed / 10F;
            }

            scroll += scrollSpeed;
            scroll = altmgr.getAlts().size() * 42 < height - 146 ? 0 : Math.max(Math.min(scroll, modulesMax), 0);
        }

        drawFlush(mouseX, mouseY);
        drawRect(0.0, 0.0, width, 146.0, -0x56000000);
        Flush.getFont("GoogleSansDisplay", 20).drawXCenteredString(altmgr.getStatus(), width / 2f, 10f, -1, true);
        Flush.getFont("GoogleSansDisplay", 20).drawXCenteredString("Current User", 120f, 6f, -1, true);
        Flush.getFont("GoogleSansDisplay", 20).drawXCenteredString(mc.getSession().getUsername(), 120f, 18f, -1, true);
        GlStateManager.pushMatrix();
        GL11.glEnable(GL11.GL_SCISSOR_TEST);
        RenderUtils.glScissor(width - 204.0, 146.0, width, height);
        if (selectedAlt != null) {
            Date date = selectedAlt.getCreationDate();
            String password = !selectedAlt.isCracked() ? selectedAlt.getPassword().substring(0, selectedAlt.getPassword().length() < 3 ? 0 : 3) +
                    selectedAlt.getPassword().replaceAll("(?s).", "*").substring(selectedAlt.getPassword().length() < 3 ? 0 : 3) : "Cracked account";
            drawRect(width - 204.0, 146.0, width, height, -0x56000000);
            Flush.getFont("GoogleSansDisplay", 26).drawXCenteredString(selectedAlt.toString(), width - 104F, 154f, -1, true);
            Flush.getFont("GoogleSansDisplay", 20).drawXCenteredString(password, width - 104F, 174f, -0x555556, true);
            Flush.getFont("GoogleSansDisplay", 20).drawXCenteredString(
                    "Created the " + dateFormat.format(date) + " at " + timeFormat.format(date),
                    width - 104F, 190f, -1, true);
            if (selectedAlt.getFaceImage() != null) {
                if (selectedAlt.getFaceTexture() == null) {
                    selectedAlt.setFaceTexture(new DynamicTexture(selectedAlt.getFaceImage()));
                }

                RenderUtils.glColor(-1);
                RenderUtils.drawImage(
                        selectedAlt.getFaceTexture(),
                        width - 104 - 64,
                        238.0,
                        128.0,
                        128.0,
                        selectedAlt.getTextureId()
                );
            }

            loginButton.drawButton(mouseX, mouseY);
            editButton.drawButton(mouseX, mouseY);
            deleteButton.drawButton(mouseX, mouseY);
        }
        GL11.glDisable(GL11.GL_SCISSOR_TEST);
        GlStateManager.popMatrix();

        GlStateManager.pushMatrix();
        GL11.glEnable(GL11.GL_SCISSOR_TEST);
        RenderUtils.glScissor(30.0, 146.0, width - 208.0, height);
        GL11.glTranslatef(30f, 146 - scroll, 0f);
        int index = 0;
        for (AccountInfo alt : altmgr.getAlts()) {
            String password = !alt.isCracked() ? alt.getPassword().substring(0, alt.getPassword().length() < 3 ? 0 : 3) +
                    alt.getPassword().replaceAll("(?s).", "*").substring(alt.getPassword().length() < 3 ? 0 : 3) : "Cracked account";
            GlStateManager.pushMatrix();
            GlStateManager.translate(0F, index * 42F, 0F);
            RenderUtils.fillRoundRect(0, 0, width - 238, 38, 3, 0xFF1E1E1E);

            GlStateManager.pushMatrix();
            GlStateManager.translate(alt.getFaceImage() != null ? 32F + 3 : 0F, 0F, 0F);
            Flush.getFont("GoogleSansDisplay", 24).drawString(alt.toString(), 4f, 4f, -1);
            Flush.getFont("GoogleSansDisplay", 18).drawString(password,
                    (10 + Flush.getFont("GoogleSansDisplay", 24).getStringWidth(alt.toString())), 6f, -0x555556);
            Flush.getFont("GoogleSansDisplay", 18).drawString(
                    "Created the " + dateFormat.format(alt.getCreationDate()) + " at " + timeFormat.format(
                            alt.getCreationDate()), 4f, (34 - Flush.getFont("GoogleSansDisplay", 20).getFontHeight()), -1);
            GlStateManager.popMatrix();

            if (alt.getFaceImage() != null) {
                if (alt.getFaceTexture() == null) {
                    alt.setFaceTexture(new DynamicTexture(alt.getFaceImage()));
                }

                RenderUtils.glColor(-1);
                RenderUtils.drawImage(
                        alt.getFaceTexture(),
                        3.0,
                        3.0,
                        32.0,
                        32.0,
                        alt.getTextureId()
                );
            }
            GlStateManager.popMatrix();
            index++;
        }
        GL11.glDisable(GL11.GL_SCISSOR_TEST);
        GlStateManager.popMatrix();

        username.draw();
        password.draw();
        combo.draw();
        if (username.getText().isEmpty()) {
            Flush.getFont("Roboto Light", 18).drawString("Username / Mail", 34f, 43F, -0x555556, true);
        }
        if (password.getText().isEmpty()) {
            Flush.getFont("Roboto Light", 18).drawString("Password", width / 2f + 8, 43F, -0x555556, true);
        }
        if (combo.getText().isEmpty()) {
            Flush.getFont("Roboto Light", 18).drawXCenteredString("Username:Password", width / 2f, 69F, -0x555556, true);
        }
        buttons.forEach(b -> b.drawButton(mouseX, mouseY));
        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    @Override
    public void keyTyped(char character, int key) {
        try {
            super.keyTyped(character, key);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (character == '\r') {
            if (!combo.getText().contains(":")) {
                thread = new AltLoginThread(new AccountInfo(username.getText(), password.getText(), altmgr.getType()));
            } else {
                String[] comboCredentials = combo.getText().split(":", 2);
                thread = new AltLoginThread(new AccountInfo(comboCredentials[0], comboCredentials[1], altmgr.getType()));
            }
            thread.start();
        }

        username.keyTyped(character, key);
        password.keyTyped(character, key);
        combo.keyTyped(character, key);
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        try {
            super.mouseClicked(mouseX, mouseY, mouseButton);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (MouseUtils.hovered(mouseX, mouseY, 30.0, 146.0, width - 208, height)) {
            int index = 0;
            for (AccountInfo alt : altmgr.getAlts()) {
                if (mouseButton == 0 && MouseUtils.hovered(mouseX, mouseY, 30.0, 146 + index * 42 - scroll, width - 208, 146 + index * 42 + 38 - scroll)) {
                    selectedAlt = alt;
                    break;
                }
                index++;
            }
        }
        if (selectedAlt != null) {
            if (loginButton.mouseClicked(mouseX, mouseY, mouseButton)) {
                thread = new AltLoginThread(selectedAlt);
                thread.start();
            }
            if (editButton.mouseClicked(mouseX, mouseY, mouseButton)) {
                mc.displayGuiScreen(new GuiEditAlt(this, selectedAlt));
            }
            if (deleteButton.mouseClicked(mouseX, mouseY, mouseButton)) {
                altmgr.getAlts().remove(selectedAlt);
                selectedAlt = null;
                altmgr.save();
            }
        }
        for (Button button : buttons) {
            if (!button.mouseClicked(mouseX, mouseY, mouseButton)) {
                continue;
            }
            Flush.playClickSound();
            switch (button.getName().toLowerCase()) {
                case "mojang":
                case "microsoft":
                    altmgr.setType(altmgr.isMicrosoft() ? AccountInfo.Type.MOJANG : AccountInfo.Type.MICROSOFT);
                    break;

                case "login":
                    if (!combo.getText().contains(":")) {
                        thread = new AltLoginThread(new AccountInfo(username.getText(), password.getText(), altmgr.getType()));
                    } else {
                        String[] comboCredentials = combo.getText().split(":", 2);
                        thread = new AltLoginThread(new AccountInfo(comboCredentials[0], comboCredentials[1], altmgr.getType()));
                    }
                    thread.start();
                    break;

                case "add account":
                    String[] comboCredentials = combo.getText().split(":", 2);
                    if (combo.getText().contains(":") && comboCredentials.length > 1) {
                        altmgr.addAlt(new AccountInfo(comboCredentials[0], comboCredentials[1], altmgr.getType()));
                        altmgr.setStatus(EnumChatFormatting.GREEN + "Added account \"" + comboCredentials[0] + "\".");
                        combo.setText("");
                        break;
                    }
                    if (!username.getText().isEmpty() && !password.getText().isEmpty() || altmgr.isValidCrackedAlt(username.getText())) {
                        altmgr.addAlt(new AccountInfo(username.getText(), password.getText(), altmgr.getType()));
                        altmgr.setStatus(EnumChatFormatting.GREEN + "Added account \"" + username.getText() + "\".");
                        username.setText("");
                        password.setText("");
                        break;
                    }
                    altmgr.setStatus(EnumChatFormatting.RED + "Failed to add account.");
                    break;

                case "clipboard login":
                    if (getClipboardString().contains(":")) {
                        String[] combo = getClipboardString().replace("\n", "").replace(" ", "")
                                .split(":", 2);
                        thread = new AltLoginThread(new AccountInfo(combo[0], combo[1], altmgr.getType()));
                        thread.start();
                        break;
                    }
                    altmgr.setStatus(EnumChatFormatting.RED + "Invalid clipboard.");
                    break;

                case "generate cracked account":
                    thread = new AltLoginThread(new AccountInfo(Flush.NAME + "_" +
                            RandomStringUtils.randomAlphanumeric(16 - Flush.NAME.length() - 1), null,
                            AccountInfo.Type.MOJANG));
                    thread.start();
                    break;
                case "import alt list":
                    JFileChooser chooser = new JFileChooser();
                    chooser.setDialogTitle(Flush.NAME + " - Select alt list");

                    if (chooser.showOpenDialog(null) != JFileChooser.APPROVE_OPTION) {
                        break;
                    }

                    try {
                        List<String> lines = Files.readLines(chooser.getSelectedFile(), StandardCharsets.UTF_8);
                        for (String line : lines) {
                            String[] args = line.split(":", 2);
                            if (args.length < 2) {
                                continue;
                            }
                            altmgr.addAlt(new AccountInfo(args[0], args[1], altmgr.getType()));
                        }
                        altmgr.save();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    break;
                case "back":
                    mc.displayGuiScreen(previousScreen);
                    break;
            }
        }
        username.mouseClicked(mouseX, mouseY, mouseButton);
        password.mouseClicked(mouseX, mouseY, mouseButton);
        combo.mouseClicked(mouseX, mouseY, mouseButton);
    }

    @Override
    public void onGuiClosed() {
        Keyboard.enableRepeatEvents(false);
    }
}