package vestige.ui.altmanager;

import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.audio.SoundHandler;
import net.minecraft.client.gui.*;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import vestige.Vestige;
import vestige.font.FontUtil;
import vestige.font.MinecraftFontRenderer;
import vestige.ui.altmanager.components.AltButton;
import vestige.ui.altmanager.components.PasswordField;
import vestige.util.render.ColorUtil;
import vestige.util.render.GLSLSandboxShader;
import vestige.util.sound.AudioUtil;

import java.awt.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;
import java.util.ArrayList;

public class AltLogin extends GuiScreen {

    private final ArrayList<AltButton> altButtons = new ArrayList<>();
    private GLSLSandboxShader backgroundShader;

    private GuiTextField emailField;
    private PasswordField passwordField;

    private AltType currentAltType = AltType.MICROSOFT;

    private AltButton loginButton;
    private AltButton importEmailPassButton;

    public AltLogin() {

    }

    @Override
    public void initGui() {
    	if(Vestige.getInstance().isShaderEnabled()) {
    		try {
                GlStateManager.disableCull();
                this.backgroundShader = new GLSLSandboxShader("/background.fsh");
            } catch (IOException e) {
                e.printStackTrace();
            }
    	}

        addButtons();

        this.emailField = new GuiTextField(10, this.mc.fontRendererObj, width / 2 - 100, 110, 200, 20);
        this.passwordField = new PasswordField(this.mc.fontRendererObj, width / 2 - 100, 150, 200, 20);
    }

    private void addButtons() {
        altButtons.clear();
        
        /*
        altButtons.add(new AltButton("Current Session Type : Microsoft") {
            @Override
            public void onClicked() {
                if(currentAltType == AltType.MOJANG) {
                    currentAltType = AltType.MICROSOFT;
                    setName("Current Session Type : Microsoft");
                } else if(currentAltType == AltType.MICROSOFT) {
                    currentAltType = AltType.MOJANG;
                    setName("Current Session Type : Mojang");
                }
            }
        });
        */

        altButtons.add(importEmailPassButton = new AltButton("Import email:pass") {
            @Override
            public void onClicked() {
                try {
                    String data = (String) Toolkit.getDefaultToolkit().getSystemClipboard().getData(DataFlavor.stringFlavor);
                    if (data.contains(":")) {
                        String[] credentials = data.split(":");
                        emailField.setText(credentials[0]);
                        passwordField.setText(credentials[1]);
                    }
                } catch (UnsupportedFlavorException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        altButtons.add(loginButton = new AltButton("Login with alt") {
            @Override
            public void onClicked() {
                if(!emailField.getText().isEmpty()) {
                    new Alt(emailField.getText(), passwordField.getText(), currentAltType).login();
                }
            }
        });

        altButtons.add(new AltButton("Add alt") {
            @Override
            public void onClicked() {
                if(!emailField.getText().isEmpty()) {
                    Vestige.getInstance().getAltManager().alts.add(new Alt(emailField.getText(), passwordField.getText(), currentAltType));
                    mc.displayGuiScreen(Vestige.getInstance().getAltManager());
                }
            }
        });

        altButtons.add(new AltButton("Back") {
            @Override
            public void onClicked() {
                mc.displayGuiScreen(Vestige.getInstance().getAltManager());
            }
        });
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        MinecraftFontRenderer fr = FontUtil.product_sans;
        ScaledResolution sr = new ScaledResolution(mc);
        
        if(Vestige.getInstance().isShaderEnabled()) {
			GlStateManager.enableAlpha();

			this.backgroundShader.useShader(this.width, this.height, mouseX, mouseY, Vestige.getInstance().getShaderTimeElapsed() / 1000F);

			GL11.glBegin(GL11.GL_QUADS);

			GL11.glVertex2f(-1F, -1F);
			GL11.glVertex2f(-1F, 1F);
			GL11.glVertex2f(1F, 1F);
			GL11.glVertex2f(1F, -1F);

			GL11.glEnd();

			GL20.glUseProgram(0);
		} else {
			mc.getTextureManager().bindTexture(new ResourceLocation("vestige/background.jpg"));
			this.drawModalRectWithCustomSizedTexture(0, 0, 0, 0, this.width, this.height, this.width, this.height);
		}

        String altLogin = "Alt login";
        String status = Vestige.getInstance().getAltManager().getStatus();

        fr.drawStringWithShadow(altLogin, sr.getScaledWidth() / 2 - fr.getStringWidth(altLogin) / 2, 50, -1);
        fr.drawStringWithShadow(status, sr.getScaledWidth() / 2 - fr.getStringWidth(status) / 2, 75, -1);

        String a1 = "Email";
        String a2 = "Password";

        emailField.drawTextBox();
        passwordField.drawTextBox();

        Color gray = new Color(205, 205, 205);

        if(!emailField.isFocused() && emailField.getText().isEmpty()) {
            fr.drawStringWithShadow(a1, sr.getScaledWidth() / 2 - 95, 116, gray.getRGB());
        }
        if(!passwordField.isFocused && passwordField.getText().isEmpty()) {
            fr.drawStringWithShadow(a2, sr.getScaledWidth() / 2 - 95, 156, gray.getRGB());
        }

        float offsetY = sr.getScaledHeight() / 2;

        for(AltButton button : altButtons) {
            String name = button.getName();
            double startX = sr.getScaledWidth() / 2 - fr.getStringWidth(name) / 2;
            double endX = startX + fr.getStringWidth(name);

            Gui.drawRect(sr.getScaledWidth() / 2 - 80, offsetY, sr.getScaledWidth() / 2 + 80, offsetY + 25, 0x80000000);
            fr.drawStringWithShadow(name, startX, offsetY + 8, -1);

            offsetY += 30;
        }
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int button) throws IOException {
        MinecraftFontRenderer fr = FontUtil.product_sans;
        ScaledResolution sr = new ScaledResolution(mc);

        float offsetX = 35;
        float startY = sr.getScaledHeight() - 50;
        float endY = sr.getScaledHeight() - 25;
        float textY = sr.getScaledHeight() - 42;

        float offsetY = sr.getScaledHeight() / 2;

        for(AltButton altButton : altButtons) {
            if(mouseX > sr.getScaledWidth() / 2 - 80 && mouseX < sr.getScaledWidth() / 2 + 80 && mouseY > offsetY && mouseY < offsetY + 25) {
                altButton.onClicked();
                AudioUtil.buttonClick();
            }

            offsetY += 30;
        }

        emailField.mouseClicked(mouseX, mouseY, button);
        passwordField.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    protected void keyTyped(char character, int key) {
        try {
            super.keyTyped(character, key);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (character == '\t') {
            if (!this.emailField.isFocused() && !this.passwordField.isFocused()) {
                this.emailField.setFocused(true);
            } else {
                this.emailField.setFocused(this.passwordField.isFocused());
                this.passwordField.setFocused(!this.emailField.isFocused());
            }
        }
        if (character == '\r' && !emailField.getText().isEmpty()) {
            loginButton.onClicked();
        }

        if(!emailField.isFocused() && !passwordField.isFocused && GuiScreen.isCtrlKeyDown() && key == Keyboard.KEY_V) {
            importEmailPassButton.onClicked();
            loginButton.onClicked();
        }

        this.emailField.textboxKeyTyped(character, key);
        this.passwordField.textboxKeyTyped(character, key);
    }

}
