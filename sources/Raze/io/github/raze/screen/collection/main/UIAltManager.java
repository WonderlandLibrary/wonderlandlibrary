package io.github.raze.screen.collection.main;

import fr.litarvan.openauth.microsoft.MicrosoftAuthResult;
import fr.litarvan.openauth.microsoft.MicrosoftAuthenticationException;
import fr.litarvan.openauth.microsoft.MicrosoftAuthenticator;
import io.github.raze.screen.system.UICustomButton;
import io.github.raze.utilities.collection.fonts.CFontUtil;
import io.github.raze.utilities.collection.math.RandomUtil;
import io.github.raze.utilities.collection.visual.RoundUtil;
import net.minecraft.client.gui.*;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Session;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class UIAltManager extends GuiScreen {

    private String inputText = "";
    private String statusText = "Status: waiting...";

    private int scrollOffset = 0;

    private final List<UICustomButton> buttonList;

    public UIAltManager() {buttonList = new ArrayList<>();}

    public void initialize() {
        Keyboard.enableRepeatEvents(true);
        super.initialize();
    }

    protected void actionPerformed(UICustomButton button) throws IOException, MicrosoftAuthenticationException {
        switch (button.id) {
            case 1:
                if (!inputText.isEmpty()) {
                    statusText = EnumChatFormatting.GREEN + "Success: logged in as " + inputText + ".";
                    mc.session = new Session(inputText, "", "", "legacy");
                } else {
                    statusText = EnumChatFormatting.RED + "Error: username can't be empty.";
                    return;
                }
                break;
            case 2:
                MicrosoftAuthenticator authenticator = new MicrosoftAuthenticator();
                MicrosoftAuthResult result = authenticator.loginWithWebview();
                mc.session = new Session(result.getProfile().getName(), result.getProfile().getId(), result.getAccessToken(), "legacy");
                statusText = EnumChatFormatting.GREEN + "Success: logged in as " + result.getProfile().getName() + " from the web.";
                break;
            case 3:
                statusText = EnumChatFormatting.GREEN + "Success: logged into an random account.";
                mc.session = new Session(RandomUtil.randomString(10), "", "", "legacy");
                break;
            case 4:
                mc.displayGuiScreen(new UIMainMenu());
                break;
        }
        super.actionPerformed(button);
    }

    public void render(int mouseX, int mouseY, float partialTicks) {
        mc.getTextureManager().bindTexture(new ResourceLocation("raze/background/background.png"));
        Gui.drawModalRectWithCustomSizedTexture(0, 0, 0, 0, width + 100, height + 100, width, height);

        RoundUtil.drawSmoothRoundedRect(
                (float) (width - 220) / 2,
                (float) (height - 145) / 2,
                (float) (width + 220) / 2,
                (float) (height + 125) / 2,
                16,
                new Color(30, 30, 30, 245).getRGB()
        );

        CFontUtil.Jello_Light_40.getRenderer().drawString(
                "Alt Manager",
                (double) (width - CFontUtil.Jello_Light_40.getRenderer().getStringWidth("Alt Manager")) / 2,
                (double) (height - 145) / 2 + 2,
                Color.WHITE
        );

        String scrollingText = "We recommend buying alts from Night Alts: https://discord.gg/ngE6uefpCj";
        int scrollingTextWidth = (int) CFontUtil.Jello_Light_24.getRenderer().getStringWidth(scrollingText);

        if (scrollOffset >= width) {
            scrollOffset = -scrollingTextWidth;
        }
        int scrollSpeed = 2;
        scrollOffset += scrollSpeed;

        int textX = scrollOffset;
        CFontUtil.Jello_Light_24.getRenderer().drawString(
                scrollingText,
                textX,
                height - CFontUtil.Jello_Light_24.getRenderer().getHeight() - 10,
                Color.WHITE
        );

        CFontUtil.Jello_Light_20.getRenderer().drawString (
                mc.session.getUsername(),
                10,
                10,
                Color.WHITE
        );

        CFontUtil.Jello_Light_16.getRenderer().drawString (
                statusText,
                (double) (width - 220) / 2 + 10,
                (double) (height + 125) / 2 - 14,
                Color.WHITE
        );

        boolean isInputFocused = isFocused((width / 2) - 100, (height / 2) - 45, mouseX, mouseY);

        int inputFieldBackgroundColor = isInputFocused ? new Color(75, 75, 75, 250).getRGB() : new Color(45, 45, 45, 250).getRGB();

        RoundUtil.drawSmoothRoundedRect(
                (float) (width / 2) - 100,
                (float) (height / 2) - 45,
                (float) (width / 2) + 100,
                (float) (height / 2) - 25,
                8,
                inputFieldBackgroundColor
        );

        int inputTextX = (width / 2) - 100 + 1;
        int inputTextY = (height / 2) - 45 + 7;

        if (!inputText.isEmpty()) {
            inputTextX += 2;
        }

        int textWidth = (int) CFontUtil.Jello_Regular_16.getRenderer().getStringWidth(inputText);

        CFontUtil.Jello_Regular_16.getRenderer().drawString(
                inputText,
                inputTextX,
                inputTextY,
                Color.WHITE
        );

        if (isInputFocused && System.currentTimeMillis() % 1000 < 500) {
            int caretX = inputTextX + textWidth + 2;
            int caretY1 = (int) ((height / 2) - 45 + 4 + 1.5);
            int caretY2 = (int) ((height / 2) - 45 + 15 - 2 + 1.5);

            Gui.drawRect(caretX, caretY1, caretX + 1, caretY2, Color.lightGray.getRGB());
        }


        buttonList.clear();

        UICustomButton loginBut = new UICustomButton(1,  width / 2 - 100, height / 2 - 20, 99, 20, "Login (Cracked)", new Color(45, 45, 45, 250), new Color(75,75,75, 250), Color.WHITE);
        buttonList.add(loginBut);

        UICustomButton webLoginBut = new UICustomButton(2,  width / 2 + 1, height / 2 - 20, 99, 20, "Web Login", new Color(45, 45, 45, 250), new Color(75,75,75, 250), Color.WHITE);
        buttonList.add(webLoginBut);

        UICustomButton randomUserBut = new UICustomButton(3, width / 2 - 100,  height / 2 + 2, 200, 20, "Random Username", new Color(45, 45, 45, 250), new Color(75,75,75, 250), Color.WHITE);
        buttonList.add(randomUserBut);

        UICustomButton backBut = new UICustomButton(4, width / 2 - 100, height / 2 + 20 + 4, 200, 20, "Back", new Color(45, 45, 45, 250), new Color(75,75,75, 250), Color.WHITE);
        buttonList.add(backBut);


        for (UICustomButton button : buttonList) {
            button.drawButton(mouseX, mouseY);
        }

        super.render(mouseX, mouseY, partialTicks);
    }

    protected void type(char typedChar, int keyCode) throws IOException {
        int mouseX = Mouse.getEventX() * this.width / this.mc.displayWidth;
        int mouseY = this.height - Mouse.getEventY() * this.height / this.mc.displayHeight - 1;

        if (isFocused((width / 2) - 100, (height / 2) - 45, mouseX, mouseY)) {
            if (keyCode == Keyboard.KEY_BACK && inputText.length() > 0) {
                inputText = inputText.substring(0, inputText.length() - 1);
            } else if ((Character.isLetterOrDigit(typedChar) || typedChar == '_') && inputText.length() < 16) {
                inputText += typedChar;
            }
        }

        if (keyCode == Keyboard.KEY_ESCAPE) {
            return;
        }

        super.type(typedChar, keyCode);
    }

    public void click(int mouseX, int mouseY, int mouseButton) throws IOException {
        for (UICustomButton button : buttonList) {
            if (button.mousePressed(mc, mouseX, mouseY)) {
                try {
                    actionPerformed(button);
                } catch (MicrosoftAuthenticationException e) {
                    throw new RuntimeException(e);
                }
                break;
            }
        }

        super.click(mouseX, mouseY, mouseButton);
    }

    private boolean isFocused(int x, int y, int mouseX, int mouseY) {
        return mouseX >= x && mouseY >= y && mouseX < x + 200 && mouseY < y + 20;
    }
}
