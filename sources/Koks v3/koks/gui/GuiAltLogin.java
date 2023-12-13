package koks.gui;

import koks.Koks;
import koks.api.util.GLSLSandboxShader;
import koks.manager.file.impl.AlteningToken;
import koks.api.interfaces.Wrapper;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;

import java.awt.*;
import java.awt.datatransfer.DataFlavor;
import java.io.IOException;

/**
 * @author kroko
 * @created on 06.10.2020 : 20:19
 */
public class GuiAltLogin extends GuiScreen implements Wrapper {

    private GLSLSandboxShader shader;

    public GuiScreen oldScreen;

    public GuiAltLogin(GuiScreen oldScreen) {
        this.oldScreen = oldScreen;
        try {
            this.shader = new GLSLSandboxShader("/alts.fsh");
        } catch (IOException e) {
            throw new IllegalStateException("Failed to load the Shader");
        }
    }

    public GuiTextField email, password;

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        GlStateManager.enableAlpha();
        GlStateManager.disableCull();

        this.shader.useShader(this.width, this.height, mouseX, mouseY, (System.currentTimeMillis() - Koks.getKoks().initTime) / 1000F);

        GL11.glBegin(GL11.GL_QUADS);

        GL11.glVertex2f(-1f, -1f);
        GL11.glVertex2f(-1f, 1f);
        GL11.glVertex2f(1f, 1f);
        GL11.glVertex2f(1f, -1f);

        GL11.glEnd();

        GL20.glUseProgram(0);

        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glEnable(GL11.GL_ALPHA_TEST);

        ScaledResolution sr = new ScaledResolution(mc);

        drawRect(0, sr.getScaledHeight() / 10, sr.getScaledWidth(), sr.getScaledHeight(), Integer.MIN_VALUE);

        drawHorizontalLine(0, sr.getScaledWidth(), sr.getScaledHeight() / 10 - 1, Color.black.getRGB());

        email.drawTextBox();
        password.drawTextBox();
        drawString(fontRendererObj, loginUtil.status, 5, 5, Color.white.getRGB());

        if (email.getText().isEmpty()) {
            String email = "Email / TheAltening";
            drawString(fontRendererObj, email, sr.getScaledWidth() / 2 - fontRendererObj.getStringWidth(email) / 2, sr.getScaledHeight() / 3 + 6, Color.gray.getRGB());
        }

        if (password.getText().isEmpty()) {
            String password = "Password";
            drawString(fontRendererObj, password, sr.getScaledWidth() / 2 - fontRendererObj.getStringWidth(password) / 2, sr.getScaledHeight() / 3 + 33, Color.gray.getRGB());
        }

        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    @Override
    protected void actionPerformed(GuiButton button) throws IOException {
        switch (button.id) {
            case 0:
                mc.displayGuiScreen(oldScreen);
                break;
            case 1:


                if (!Koks.getKoks().alteningApiKey.equals("") && Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {
                    loginUtil.generate(Koks.getKoks().alteningApiKey);
                } else {
                    if (password.getText().isEmpty() && email.getText().isEmpty()) {
                        try {
                            String clipboard = (String) Toolkit.getDefaultToolkit().getSystemClipboard().getData(DataFlavor.stringFlavor);
                            String[] args = clipboard.split(":");
                            if (args.length == 1) {
                                if (args[0].contains("@alt")) {
                                    loginUtil.login(clipboard);
                                } else if (args[0].startsWith("api-")) {
                                    Koks.getKoks().alteningApiKey = clipboard;
                                    Koks.getKoks().fileManager.writeFile(AlteningToken.class);
                                    loginUtil.status = "§aUpdated Altening API Token";
                                } else {
                                    if (Koks.getKoks().alteningApiKey != null) {
                                        loginUtil.generate(Koks.getKoks().alteningApiKey);
                                    }
                                }
                            } else if (args.length == 2) {
                                if (args[0].contains("@") && clipboard.contains(":")) {
                                    loginUtil.login(args[0], args[1]);
                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else if (!password.getText().isEmpty() && !email.getText().isEmpty()) {
                        loginUtil.login(email.getText(), password.getText());
                    } else if (!email.getText().isEmpty() && password.getText().isEmpty()) {
                        if (email.getText().startsWith("api-")) {
                            Koks.getKoks().alteningApiKey = email.getText();
                            Koks.getKoks().fileManager.writeFile(AlteningToken.class);
                            loginUtil.status = "§aUpdated Altening API Token";
                        } else if (email.getText().contains("@alt.com")) {
                            loginUtil.login(email.getText());
                        }

                    }
                }
                break;
        }

        super.actionPerformed(button);
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException {
        Keyboard.enableRepeatEvents(true);
        if (email.isFocused()) {
            email.textboxKeyTyped(typedChar, keyCode);
            if (keyCode == Keyboard.KEY_RETURN) {
                password.setFocused(true);
                email.setFocused(false);
            } else if (keyCode == Keyboard.KEY_DOWN) {
                password.setFocused(true);
                email.setFocused(false);
            } else if (keyCode == Keyboard.KEY_ESCAPE) {
                email.setFocused(false);
            }
        } else if (password.isFocused()) {
            password.textboxKeyTyped(typedChar, keyCode);
            if (keyCode == Keyboard.KEY_RETURN) {
                actionPerformed(buttonList.get(1));
            } else if (keyCode == Keyboard.KEY_UP) {
                password.setFocused(false);
                email.setFocused(true);
            } else if (keyCode == Keyboard.KEY_ESCAPE) {
                password.setFocused(false);
            }
        } else {
            if (keyCode == Keyboard.KEY_ESCAPE) {
                mc.displayGuiScreen(oldScreen);
            } else if (keyCode == Keyboard.KEY_RETURN) {
                actionPerformed(buttonList.get(1));
            }
        }
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        ScaledResolution sr = new ScaledResolution(mc);
        if (mouseX > sr.getScaledWidth() / 2 - 90 - 2 && mouseX < sr.getScaledWidth() / 2 + 90 + 2 && mouseY > sr.getScaledHeight() / 3 && mouseY < sr.getScaledHeight() / 3 + 21) {
            email.setFocused(true);
            password.setFocused(false);
        } else if (mouseX > sr.getScaledWidth() / 2 - 90 - 2 && mouseX < sr.getScaledWidth() / 2 + 90 + 2 && mouseY > sr.getScaledHeight() / 3 + 27 && mouseY < sr.getScaledHeight() / 3 + 27 + 21) {
            email.setFocused(false);
            password.setFocused(true);
        } else {
            email.setFocused(false);
            password.setFocused(false);
        }
        super.mouseClicked(mouseX, mouseY, mouseButton);
    }

    @Override
    public void initGui() {

        ScaledResolution sr = new ScaledResolution(mc);

        buttonList.add(new GuiButton(0, sr.getScaledWidth() / 2 + 5, sr.getScaledHeight() / 2 + 55, 50, 20, "Back"));
        buttonList.add(new GuiButton(1, sr.getScaledWidth() / 2 - 55, sr.getScaledHeight() / 2 + 55, 50, 20, "Login"));

        email = new GuiTextField(187, mc.fontRendererObj, sr.getScaledWidth() / 2 - 180 / 2, sr.getScaledHeight() / 3, 180, 20);
        email.setFocused(false);

        password = new GuiTextField(1337, mc.fontRendererObj, sr.getScaledWidth() / 2 - 180 / 2, sr.getScaledHeight() / 3 + 27, 180, 20);
        password.setFocused(false);

        super.initGui();
    }
}
