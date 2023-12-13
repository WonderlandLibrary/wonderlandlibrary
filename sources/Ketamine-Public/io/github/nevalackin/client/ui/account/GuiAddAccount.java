package io.github.nevalackin.client.ui.account;

import io.github.nevalackin.client.account.Account;
import io.github.nevalackin.client.core.KetamineClient;
import io.github.nevalackin.client.notification.NotificationType;
import io.github.nevalackin.client.util.render.DrawUtil;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.util.Session;

import java.io.IOException;
import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.CookiePolicy;

public final class GuiAddAccount extends GuiScreen {

    private final GuiScreen parent;

    public GuiAddAccount(GuiScreen parent) {
        this.parent = parent;
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        // Background
        DrawUtil.glDrawFilledQuad(0, 0, this.width, this.height, 0xFF080808, 0xFF0F0F0F);

        // Title
        this.mc.fontRendererObj.drawStringWithShadow("Add Account", this.width / 2.0 - this.mc.fontRendererObj.getStringWidth("Add Account") / 2.0,
                this.height / 5.0 - 9 / 2.0, 0xFFFFFFFF);

        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    @Override
    public void initGui() {
        final int yBuffer = 2;
        final int xBuffer = 2;
        final int fullWidth = this.width / 4;
        final int width = fullWidth - xBuffer * 2;
        final int left = this.width / 2 - fullWidth / 2;
        final int top = this.height / 3;
        final int height = 20 + yBuffer * 2;


        final int buttonWidth = 120;
        final int buttonLeft = left + (fullWidth - buttonWidth) / 2;

        this.buttonList.add(new GuiButton(0, buttonLeft, top + xBuffer * 2 + height * 2, buttonWidth, 20, "Login with webview"));
        this.buttonList.add(new GuiButton(1, buttonLeft, top + xBuffer * 2 + height * 3, buttonWidth, 20, "Done"));
    }

    static void clearCookieStore() {
        CookieHandler.setDefault(new CookieManager(null, CookiePolicy.ACCEPT_ALL));
    }

    @Override
    protected void actionPerformed(GuiButton button) throws IOException {
        switch (button.id) {
            case 0:
                KetamineClient.getInstance().getNotificationManager().add(NotificationType.INFO,
                        "Logging In",
                        "Opening Microsoft authentication webview.",
                        1000L);

                KetamineClient.getInstance().getMicrosoftAuthenticator().loginWithAsyncWebview()
                        .exceptionally(e -> null)
                        .thenAccept(authResult -> {
                            if (authResult != null) {
                                KetamineClient.getInstance().getNotificationManager().add(NotificationType.SUCCESS,
                                        "Logged In",
                                        String.format("Successfully logged into %s.", authResult.getProfile().getName()),
                                        1500L);
                                Account account = Account.builder()
                                        .username(authResult.getProfile().getName())
                                        .accessToken(authResult.getAccessToken())
                                        .refreshToken(authResult.getRefreshToken())
                                        .working(true).build();
                                this.mc.setSession(new Session(authResult.getProfile().getName(), authResult.getProfile().getId(), authResult.getAccessToken(), "mojang"));
                                KetamineClient.getInstance().getAccountManager().addAccount(account);
                            }
                            else {
                                KetamineClient.getInstance().getNotificationManager().add(NotificationType.ERROR,
                                        "Login Failed",
                                        "Unable to login to account.",
                                        1500L);

                            }
                            clearCookieStore();
                        });
                break;
            case 1:
                this.mc.displayGuiScreen(this.parent);
                break;
        }
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        super.mouseClicked(mouseX, mouseY, mouseButton);
    }

    @Override
    public void updateScreen() {

    }

    @Override
    protected void mouseReleased(int mouseX, int mouseY, int state) {
        super.mouseReleased(mouseX, mouseY, state);
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException {
        if (keyCode == 1) {
            this.mc.displayGuiScreen(this.parent);
        }
    }
}
