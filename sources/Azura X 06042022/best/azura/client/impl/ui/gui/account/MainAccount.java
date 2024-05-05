package best.azura.client.impl.ui.gui.account;


import best.azura.client.api.account.AccountData;
import best.azura.client.api.account.AccountType;
import best.azura.client.api.account.BannedData;
import best.azura.client.api.ui.buttons.Button;
import best.azura.client.api.ui.notification.Notification;
import best.azura.client.api.ui.notification.Type;
import best.azura.client.impl.Client;
import best.azura.client.impl.module.impl.render.BlurModule;
import best.azura.client.impl.rpc.DiscordRPCImpl;
import best.azura.client.impl.ui.font.Fonts;
import best.azura.client.impl.ui.gui.impl.buttons.ButtonImpl;
import best.azura.client.impl.ui.gui.MainMultiplayer;
import best.azura.client.impl.ui.gui.ScrollRegion;
import best.azura.client.impl.ui.gui.impl.buttons.TextButton;
import best.azura.client.impl.ui.gui.impl.Window;
import best.azura.client.util.other.PlayTimeUtil;
import best.azura.client.util.render.RenderUtil;
import best.azura.client.util.render.StencilUtil;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;
import wtf.kinggen.KingGen;
import wtf.kinggen.entities.KingGenAccount;
import wtf.kinggen.exceptions.KingGenInvalidApiKeyException;
import wtf.kinggen.exceptions.KingGenOutOfStockException;
import wtf.kinggen.exceptions.KingGenReachedLimitException;

import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.function.Consumer;

import static org.lwjgl.opengl.GL11.*;

public class MainAccount extends GuiScreen {

    private best.azura.client.impl.ui.gui.impl.Window currentWindow = null;
    private final GuiScreen parent;
    private GuiScreen toShow;
    private double animation;
    private long start;
    private final ArrayList<Button> buttons = new ArrayList<>();
    private Button selected;
    private ScrollRegion region;

    public MainAccount(GuiScreen parent) {
        this.parent = parent;
        this.start = System.currentTimeMillis();
        this.animation = 0;
    }

    @Override
    public void initGui() {
        DiscordRPCImpl.updateNewPresence("Account Manager", "Switching to a new account");
        this.region = new ScrollRegion(mc.displayWidth / 2 - 500, mc.displayHeight / 2 - 400, 1000, 720);
        start = System.currentTimeMillis();
        animation = 0;

        buttons.clear();
        int count = 0;
        for (AccountData data : Client.INSTANCE.getAccountManager().getData()) {
            ButtonImpl b;
            buttons.add(b = new ButtonImpl("", mc.displayWidth / 2 - 470, mc.displayHeight / 2 - 380 + count * 78, 940, 70, 5));
            b.accountData = data;
            count++;
        }
        String[] strings = new String[]{"Direct login", "Add", "Login", "Remove", "Back", "KingAlts"};
        int calcWidth = 0;
        for (String s : strings) {
            int width = Fonts.INSTANCE.arial20.getStringWidth(s);
            buttons.add(new ButtonImpl(s, mc.displayWidth / 2 - 470 + calcWidth, mc.displayHeight / 2 + 350, width + 40, 40, 3));
            calcWidth += width + 40 + 5;
        }
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {

        if (toShow != null) {
            float anim = Math.min(1, (System.currentTimeMillis() - start) / 500f);
            animation = -1 * Math.pow(anim - 1, 6) + 1;
            animation = 1 - animation;
        } else {
            float anim = Math.min(1, (System.currentTimeMillis() - start) / 500f);
            animation = -1 * Math.pow(anim - 1, 6) + 1;
        }

        if (toShow != null && animation == 0) {
            mc.displayGuiScreen(toShow);
            return;
        }

        GlStateManager.pushMatrix();
        glEnable(GL_BLEND);
        drawDefaultBackground();

        RenderUtil.INSTANCE.scaleFix(1.0);
        final ScaledResolution sr = new ScaledResolution(mc);
        final boolean blur = Client.INSTANCE.getModuleManager().getModule(BlurModule.class).isEnabled() && BlurModule.blurMenu.getObject();
        if (blur) {
            GL11.glPushMatrix();
            RenderUtil.INSTANCE.invertScaleFix(1.0);
            StencilUtil.initStencilToWrite();
            GlStateManager.scale(1.0 / sr.getScaleFactor(), 1.0 / sr.getScaleFactor(), 1);
        }
        RenderUtil.INSTANCE.drawRoundedRect(mc.displayWidth / 2.0 - 500, mc.displayHeight / 2.0 - 400, 1000, 800, 10, new Color(0, 0, 0, 170));
        if (blur) {
            GlStateManager.scale(sr.getScaleFactor(), sr.getScaleFactor(), 1);
            StencilUtil.readStencilBuffer(1);
            BlurModule.blurShader.blur();
            StencilUtil.uninitStencilBuffer();
            RenderUtil.INSTANCE.scaleFix(1.0);
            GL11.glPopMatrix();
        }

        region.prepare();
        region.render(mouseX, mouseY);
        int count = 0;
        int calcHeight = 0;
        try {
            for (Button button : buttons) {
                if (button instanceof ButtonImpl) {
                    ButtonImpl button1 = (ButtonImpl) button;

                    if (button1.accountData != null) {
                        button1.y = mc.displayHeight / 2 - 380 + count * 78 + region.mouse;
                        button1.width = 940;
                        button1.x = mc.displayWidth / 2 - 470;
                        button1.height = 70;
                        button1.roundness = 5;
                        button1.animation = animation;
                        button1.selected = selected == button;
                        button1.draw(mouseX, mouseY);
                        count++;
                        calcHeight += 78;
                    }
                }
            }
        } catch (Exception ignored) {
        }
        if (calcHeight > region.height) {
            region.offset = region.height - calcHeight - 20;
        }
        region.end();

        for (Button button : buttons) {
            if (button instanceof ButtonImpl) {
                ButtonImpl button1 = (ButtonImpl) button;
                if (button1.accountData != null) {
                    if (button1.selected) {
                        final AccountData data = button1.accountData;
                        if (data.getBanned() == null || data.getBanned().isEmpty()) continue;
                        RenderUtil.INSTANCE.drawRoundedRect(region.x - 430, region.y, 420, 420, 10, new Color(0, 0, 0, 170));
                        int height = 0;
                        for (BannedData bannedData : data.getBanned()) {
                            final String s = bannedData.getPlayTime() == -1 || bannedData.getPlayTime() == 0 || PlayTimeUtil.format(bannedData.getPlayTime()).isEmpty() ? "" : " after " + PlayTimeUtil.format(bannedData.getPlayTime()), ipBase = bannedData.getServerIp();
                            final String[] baseIp = (ipBase.endsWith(".") ? ipBase.substring(0, ipBase.length() - 1) : ipBase).split("\\.");
                            final String ip = baseIp.length >= 2 ? baseIp[baseIp.length - 2] + "." + baseIp[baseIp.length - 1] : ipBase;
                            Fonts.INSTANCE.arial12.drawStringWithShadow("Banned from " + ip + " on " + bannedData.getBannedDate() + s, region.x - 427, region.y + height, -1);
                            height += Fonts.INSTANCE.arial12.FONT_HEIGHT;
                        }
                    }
                    continue;
                }
                button1.animation = animation;
                button.draw(mouseX, mouseY);
            }
        }

        if (currentWindow != null && currentWindow.hidden && currentWindow.animation == 0) {
            currentWindow = null;
        }

        if (currentWindow != null) {
            RenderUtil.INSTANCE.drawRect(0, 0, mc.displayWidth, mc.displayHeight, new Color(0, 0, 0, (int) (100 * currentWindow.animation)));
            currentWindow.draw(mouseX, mouseY);
        }

        GlStateManager.popMatrix();
        glDisable(GL_BLEND);

    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {

        if (currentWindow != null) {
            final Button button = currentWindow.clickedButton(mouseX, mouseY);
            if (button == null) return;
            if (button instanceof ButtonImpl) {
                ButtonImpl button1 = (ButtonImpl) button;
                switch (button1.text) {
                    case "Microsoft":
                        currentWindow.hide();
                        ButtonImpl microsoftAccountButton;
                        buttons.add(microsoftAccountButton = new ButtonImpl("d", 0, 0, 0, 0, 5));

                        try {
                            Client.INSTANCE.getMicrosoftAuthenticator().loginWithAsyncWebview().thenAccept(microsoftAuthResult1 -> {
                                AccountData microsoftAccountData;
                                Client.INSTANCE.getAccountManager().addAccount(microsoftAccountData = new AccountData(AccountType.MICROSOFT, microsoftAuthResult1.getProfile().getName() + ":" + microsoftAuthResult1.getProfile().getId() + ":" + microsoftAuthResult1.getAccessToken()));
                                microsoftAccountButton.accountData = microsoftAccountData;
                            });
                        } catch (Exception ignore) {
                        }
                        break;
                    case "Add":
                        currentWindow.hide();
                        ButtonImpl b2;
                        buttons.add(b2 = new ButtonImpl("d", 0, 0, 0, 0, 5));

                        String addUserName, addPassword;

                        addUserName = ((TextButton) currentWindow.buttons.stream().filter(button2 -> button2 instanceof TextButton && ((TextButton) button2).text.equalsIgnoreCase("E-Mail / Name")).findAny().get()).fontText;
                        addPassword = ((TextButton) currentWindow.buttons.stream().filter(button2 -> button2 instanceof TextButton && ((TextButton) button2).text.equalsIgnoreCase("Password")).findAny().get()).fontText;

                        AccountData data;
                        assert addPassword != null;
                        if (addPassword.equals("")) {
                            Client.INSTANCE.getAccountManager().addAccount(data = new AccountData(AccountType.CRACKED, addUserName));
                        } else {
                            Client.INSTANCE.getAccountManager().addAccount(data = new AccountData(AccountType.MICROSOFT_CREDENTIALS, addUserName + ":" + addPassword));
                        }
                        b2.accountData = data;
                        break;
                    case "Remove":
                        currentWindow.hide();
                        Client.INSTANCE.getAccountManager().getData().remove(((ButtonImpl) selected).accountData);
                        buttons.remove(selected);
                        selected = null;
                        break;
                    case "Login":

                        String username, password;

                        username = ((TextButton) currentWindow.buttons.stream().filter(button2 -> button2 instanceof TextButton && ((TextButton) button2).text.equalsIgnoreCase("E-Mail / Name")).findAny().get()).fontText;
                        password = ((TextButton) currentWindow.buttons.stream().filter(button2 -> button2 instanceof TextButton && ((TextButton) button2).text.equalsIgnoreCase("Password")).findAny().get()).fontText;

                        AccountData accountData;

                        if (password.isEmpty()) {
                            accountData = new AccountData(AccountType.CRACKED, username);
                        } else {
                            accountData = new AccountData(AccountType.MICROSOFT_CREDENTIALS, username + ":" + password);
                        }

                        accountData.login();
                        break;
                    case "Generate":
                        Client.INSTANCE.getNotificationManager().addToQueue(new Notification("Alt Manager","INFO: KingGen does not work as of right now!",3000, Type.INFO));
                        break;
                    case "Cancel":
                        currentWindow.hide();
                        break;
                }
                return;
            }
        }

        String clicked = "null";
        for (final Button button : buttons) {
            if (button instanceof ButtonImpl) {
                ButtonImpl button1 = (ButtonImpl) button;
                if (button1.hovered && button1.accountData != null && region.isHovered()) {
                    selected = button;
                    return;
                } else if (button1.hovered) {
                    clicked = button1.text;
                }
                button.mouseClicked();
            }
        }

        switch (clicked) {
            case "Back":
                toShow = parent;
                start = System.currentTimeMillis();
                break;
            case "Add":
                currentWindow = new best.azura.client.impl.ui.gui.impl.Window("Add account", 300, 300);
                currentWindow.buttons.add(new TextButton("E-Mail / Name", mc.displayWidth / 2 - 140, mc.displayHeight / 2 - 70, 280, 40, false));
                currentWindow.buttons.add(new TextButton("Password", mc.displayWidth / 2 - 140, mc.displayHeight / 2 - 20, 280, 40, true));
                currentWindow.buttons.add(new ButtonImpl("Add", mc.displayWidth / 2 - 140, mc.displayHeight / 2 + 100, 90, 40, 5));
                currentWindow.buttons.add(new ButtonImpl("Microsoft", mc.displayWidth / 2 - 45, mc.displayHeight / 2 + 100, 100, 40, 5));
                currentWindow.buttons.add(new ButtonImpl("Cancel", mc.displayWidth / 2 + 60, mc.displayHeight / 2 + 100, 80, 40, 5));
                break;
            case "Remove":
                if (selected != null) {
                    currentWindow = new best.azura.client.impl.ui.gui.impl.Window("Confirm removing account", 300, 200);
                    MainMultiplayer.button(currentWindow, mc.displayWidth / 2, mc.displayHeight / 2);
                }
                break;
            case "Login":
                if (selected != null) {
                    if (selected instanceof ButtonImpl) {
                        ButtonImpl button1 = (ButtonImpl) selected;
                        button1.accountData.login();
                        selected = null;
                    }
                }
                break;
            case "Direct login":
                currentWindow = new best.azura.client.impl.ui.gui.impl.Window("Direct login", 300, 300);
                currentWindow.buttons.add(new TextButton("E-Mail / Name", mc.displayWidth / 2 - 140, mc.displayHeight / 2 - 70, 280, 40, false));
                currentWindow.buttons.add(new TextButton("Password", mc.displayWidth / 2 - 140, mc.displayHeight / 2 - 20, 280, 40, true));
                currentWindow.buttons.add(new ButtonImpl("Login", mc.displayWidth / 2 - 140, mc.displayHeight / 2 + 100, 100, 40, 5));
                currentWindow.buttons.add(new ButtonImpl("Cancel", mc.displayWidth / 2 + 40, mc.displayHeight / 2 + 100, 100, 40, 5));
                break;
            case "KingAlts":
                currentWindow = new Window("KingAlts Generator", 300, 300);
                TextButton apiKeyButton = new TextButton("API-KEY", mc.displayWidth / 2 - 140, mc.displayHeight / 2 - 70, 280, 40, true);
                currentWindow.buttons.add(apiKeyButton);

                apiKeyButton.fontText = Client.INSTANCE.getKingAltsGenerator().getApiKey();

                currentWindow.buttons.add(new ButtonImpl("Generate", mc.displayWidth / 2 - 140, mc.displayHeight / 2 + 100, 100, 40, 5));
                currentWindow.buttons.add(new ButtonImpl("Cancel", mc.displayWidth / 2 + 40, mc.displayHeight / 2 + 100, 100, 40, 5));
                break;
            case "null":
                selected = null;
                break;
        }

    }

    @Override
    public void handleMouseInput() throws IOException {
        super.handleMouseInput();
        region.handleMouseInput();
    }

    @Override
    public void onTick() {
        region.onTick();
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException {
        super.keyTyped(typedChar, keyCode);
        if (currentWindow != null) {
            currentWindow.keyTyped(typedChar, keyCode);
        }

        if (currentWindow == null) {
            if (keyCode == Keyboard.KEY_V && GuiScreen.isCtrlKeyDown()) {
                String clipboard = GuiScreen.getClipboardString();
                if (clipboard.contains("\n") && clipboard.split("\n").length >= 1) {
                    for (String s : clipboard.split("\n")) {
                        if (s.isEmpty()) continue;
                        AccountData data;
                        if (s.contains("\\n")) continue;
                        if (s.contains("\n")) continue;
                        if (Client.INSTANCE.getAccountManager().getData().stream().anyMatch(d -> d.getData().equals(s)))
                            continue;
                        if (s.split(":").length >= 2) {
                            Client.INSTANCE.getAccountManager().addAccount(data = new AccountData(AccountType.MICROSOFT_CREDENTIALS, s));
                        } else continue;
                        ButtonImpl button;
                        buttons.add(button = new ButtonImpl("d", 0, 0, 0, 0, 5));
                        button.accountData = data;
                    }
                } else {
                    AccountData data;
                    if (clipboard.split(":").length >= 2) {
                        Client.INSTANCE.getAccountManager().addAccount(data = new AccountData(AccountType.MICROSOFT_CREDENTIALS, clipboard));
                    } else return;
                    ButtonImpl button;
                    buttons.add(button = new ButtonImpl("d", 0, 0, 0, 0, 5));
                    button.accountData = data;
                }
            }
        } else if (currentWindow.text.equalsIgnoreCase("Direct login")) {
            if (keyCode == Keyboard.KEY_V && GuiScreen.isCtrlKeyDown()) {
                String clipboard = GuiScreen.getClipboardString();

                if (clipboard.split(":").length >= 2) {
                    ((TextButton) currentWindow.buttons.stream().filter(button -> button instanceof TextButton && ((TextButton) button).text.equalsIgnoreCase("E-Mail / Name")).findAny().get()).fontText = clipboard.split(":")[0];
                    ((TextButton) currentWindow.buttons.stream().filter(button -> button instanceof TextButton && ((TextButton) button).text.equalsIgnoreCase("Password")).findAny().get()).fontText = clipboard.split(":")[1];

                    new AccountData(AccountType.MICROSOFT_CREDENTIALS, clipboard).login();
                } else return;
            }
        }

        if (selected != null && selected instanceof ButtonImpl) {
            ButtonImpl button1 = (ButtonImpl) selected;
            if (keyCode == Keyboard.KEY_DELETE && button1.accountData != null) {
                buttons.remove(selected);
                Client.INSTANCE.getAccountManager().getData().remove(button1.accountData);
                selected = null;
            }
        }
    }

}
