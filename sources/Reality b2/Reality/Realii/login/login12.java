package Reality.Realii.login;

import Reality.Realii.mods.modules.ClientSettings;
import Reality.Realii.utils.cheats.RenderUtills.Pair;
import Reality.Realii.utils.cheats.RenderUtills.Render2;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

import Nyghtfull.serverdate.User;
import Reality.Realii.Client;
import Reality.Realii.guis.font.FontLoaders;
import Reality.Realii.login.utils.EmptyInputBox;
import Reality.Realii.utils.render.RenderUtil;
import checkerv2.idk12;

import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.io.IOException;

public class login12 extends GuiScreen {
    EmptyInputBox username;
    EmptyInputBox password;
    private boolean loginsuccessfully = false;
    int anim = 140;
    

    @Override
    public void initGui() {
        super.initGui();
        username = new EmptyInputBox(4, mc.fontRendererObj, 435, 150, 100, 20);
        password = new EmptyInputBox(4, mc.fontRendererObj, 435, 180, 100, 20);
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        ScaledResolution sr = new ScaledResolution(mc);


        if (loginsuccessfully) {
            mc.displayGuiScreen(new GuiMainMenu());
        }
        this.drawDefaultBackground();
        username.yPosition = 100;
        password.yPosition = username.yPosition + 30;
        int i = 0;
        Color startColor = new Color(ClientSettings.r.getValue().intValue(), ClientSettings.g.getValue().intValue(), ClientSettings.b.getValue().intValue());
        Color endColor = new Color(ClientSettings.r2.getValue().intValue(), ClientSettings.g2.getValue().intValue(), ClientSettings.b2.getValue().intValue());
        Pair<Color, Color> colors = Pair.of(startColor, endColor);
        Color c2 = Render2.interpolateColorsBackAndForth(7, 3 + (i * 20), colors.getFirst(), colors.getSecond(), false);
        //RenderUtils.drawRect(0, 0, sr.getScaledWidth(), sr.getScaledHeight(), new Color(62, 66, 104).getRGB());
        RenderUtil.drawRect(0, 0, 0, sr.getScaledHeight(),new Color(10, 10, 10).getRGB());

        FontLoaders.product22.drawString("Welcome! To Reality Beta 1.4", 405, 45, new Color(255, 255, 255).getRGB());
        FontLoaders.product16.drawString("Input your information to continue", 411, 65, new Color(255, 255, 255).getRGB());


        RenderUtil.drawBorderedRect(username.xPosition, username.yPosition, username.xPosition + username.getWidth(), username.yPosition + 20,2, c2.getRGB(),new Color(255, 255, 255).getRGB());


        if (!username.isFocused() && username.getText().isEmpty()) {
            FontLoaders.product16.drawString("Username", username.xPosition + 25, username.yPosition + 6, new Color(10, 10, 10).getRGB());
        }
        
        RenderUtil.drawBorderedRect(password.xPosition, password.yPosition, password.xPosition + password.getWidth(), password.yPosition + 20,2, c2.getRGB(),new Color(200, 200, 200).getRGB());
        RenderUtil.drawBorderedRect(password.xPosition + 0.5f, password.yPosition + 0.5f, password.xPosition + password.getWidth() - 0.5f, password.yPosition + 20 - 0.5f,2, c2.getRGB(), new Color(255, 255, 255).getRGB());
        if (!password.isFocused() && password.getText().isEmpty()) {
            FontLoaders.product16.drawString("Password", password.xPosition + 25, password.yPosition + 6, new Color(10, 10, 10).getRGB());
        } else {
            String xing = "";
            for (char c : password.getText().toCharArray()) {
                xing = xing + "x";

            }
            FontLoaders.arial22.drawString(xing, password.xPosition + 4, password.yPosition + 6, new Color(10, 10, 10).getRGB());
        }

        username.drawTextBox();
        if (isHovered(password.xPosition, password.yPosition + 30, password.xPosition + password.getWidth(), password.yPosition + 50, mouseX, mouseY)) {
            if (Mouse.isButtonDown(0)) {
                Minecraft.getMinecraft().displayGuiScreen(new GuiMainMenu());
                if(true == true) {
                    return;
                }
                
            }
            RenderUtil.drawBorderedRect(password.xPosition, password.yPosition + 30, password.xPosition + password.getWidth(), password.yPosition + 50,2, c2.getRGB(),c2.getRGB());
            FontLoaders.product16.drawCenteredString("Login", password.xPosition + -1 + password.getWidth() / 2, password.yPosition + 38, new Color(10, 10, 10).getRGB());
        } else {
            RenderUtil.drawBorderedRect(password.xPosition, password.yPosition + 30, password.xPosition + password.getWidth(), password.yPosition + 50,2, c2.getRGB(),c2.getRGB());
            FontLoaders.product16.drawCenteredString("Login", password.xPosition + -1 + password.getWidth() / 2, password.yPosition + 38, new Color(10, 10, 10).getRGB());
        }


        

        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    public boolean contains_(String s, String t) {
        char[] array1 = s.toCharArray();
        char[] array2 = t.toCharArray();
        boolean status = false;

        if (array2.length < array1.length) {
            for (int i = 0; i < array1.length; i++) {
                if (array1[i] == array2[0] && i + array2.length - 1 < array1.length) {
                    int j = 0;
                    while (j < array2.length) {
                        if (array1[i + j] == array2[j]) {
                            j++;
                        } else
                            break;
                    }
                    if (j == array2.length) {
                        status = true;
                        break;
                    }
                }

            }
        }
        return status;
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException {
        switch (keyCode) {
            case Keyboard.KEY_TAB:
                if (username.isFocused()) {
                    // TabÃ©â€�Â®Ã¥Ë†â€¡Ã¦ï¿½Â¢Ã§â€žÂ¦Ã§â€šÂ¹
                    if (keyCode == Keyboard.KEY_TAB) {
                        password.setFocused(true);
                        username.setFocused(false);
                        return;
                    }
                }
                break;
            case Keyboard.KEY_RETURN:
                // TODO: 2021/8/2 f 
                break;
            default:
                if (username.isFocused()) {
                    username.textboxKeyTyped(typedChar, keyCode);
                }
                if (password.isFocused()) {
                    password.textboxKeyTyped(typedChar, keyCode);
                }
                break;
        }
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        username.mouseClicked(mouseX, mouseY, mouseButton);
        password.mouseClicked(mouseX, mouseY, mouseButton);

        super.mouseClicked(mouseX, mouseY, mouseButton);
    }

    public static boolean isHovered(float x, float y, float x2, float y2, int mouseX, int mouseY) {
        return mouseX >= x && mouseX <= x2 && mouseY >= y && mouseY <= y2;
    }
}
