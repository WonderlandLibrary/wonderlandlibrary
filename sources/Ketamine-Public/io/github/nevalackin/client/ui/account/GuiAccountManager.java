package io.github.nevalackin.client.ui.account;

import fr.litarvan.openauth.microsoft.MicrosoftAuthResult;
import io.github.nevalackin.client.account.Account;
import io.github.nevalackin.client.account.AccountManager;
import io.github.nevalackin.client.notification.NotificationType;
import io.github.nevalackin.client.core.KetamineClient;
import io.github.nevalackin.client.util.render.DrawUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.resources.DefaultPlayerSkin;
import net.minecraft.util.Session;
import org.lwjgl.input.Mouse;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

import static org.lwjgl.opengl.GL11.*;

public final class GuiAccountManager extends GuiScreen {

    private final GuiScreen parent;

    private Account selectedAccount;

    private GuiButton loginButton, deleteButton;

    private boolean hasScrollbar;
    private int scrollOffset;

    private long lastClick;

    public GuiAccountManager(GuiScreen parent) {
        this.parent = parent;
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        final AccountManager accountManager = KetamineClient.getInstance().getAccountManager();

        this.deleteButton.enabled = this.loginButton.enabled = this.selectedAccount != null;

        // For smooth shadows
        glDisable(GL_ALPHA_TEST);
        // Background
        DrawUtil.glDrawFilledQuad(0, 0, this.width, this.height, 0xFF080808, 0xFF0F0F0F);
        // Top
        DrawUtil.glDrawFilledQuad(0, 0, this.width, 30, 0xFF080808, 0xFF111111);
        // Top shadow
        DrawUtil.glDrawFilledQuad(0, 30, this.width, 4, 0x96000000, 0);
        // Bottom
        DrawUtil.glDrawFilledQuad(0, this.height - 58, this.width, 58, 0xFF090909);
        // Bottom shadow
        DrawUtil.glDrawFilledQuad(0, this.height - 62, this.width, 4, 0, 0x96000000);
        // Restore to normal
        glEnable(GL_ALPHA_TEST);
        // Draw buttons
        super.drawScreen(mouseX, mouseY, partialTicks);

        // Username
        this.mc.fontRendererObj.drawStringWithShadow(String.format("\2477Username: \247A%s", this.mc.getSession().getUsername()), 4, 4, 0xFFFFFFFF);
        // Alt manager title and alt count
        final String title = String.format("Alt Manager \2478[\2477%s\2478]", accountManager.getAccounts().size());
        this.mc.fontRendererObj.drawStringWithShadow(title, this.width / 2.0 - this.fontRendererObj.getStringWidth(title) / 2.0,
                                                     30 / 2.0 - 9 / 2.0, 0xFFFFFFFF);

        final Collection<Account> accounts = accountManager.getAccounts();

        int top = 30;

        final int xBuffer = 2;
        final int yBuffer = 2;
        final int headSize = 32;
        final int accountEntryHeight = headSize + yBuffer * 2;
        final int left = this.width / 3;

        final int uncutHeight = accounts.size() * accountEntryHeight;
        final int maxHeight = this.height - top - 58;

        this.hasScrollbar = uncutHeight > maxHeight;

        if (!this.hasScrollbar) {
            this.scrollOffset = 0;
        }

        if (this.hasScrollbar) {
            final int scrollAmount = -Mouse.getDWheel() / 5;

            this.scrollOffset = Math.max(0, Math.min(uncutHeight - maxHeight, this.scrollOffset + scrollAmount));

            DrawUtil.glScissorBox(left, top, this.width / 1.5, this.height - top - 58, new ScaledResolution(this.mc));

            if (this.scrollOffset > 0) {
                glTranslated(0, -this.scrollOffset, 0);
            }
        }

        for (final Account account : accounts) {
            final String username = account.getUsername();
            this.mc.fontRendererObj.drawStringWithShadow(username, left + accountEntryHeight, top + yBuffer * 2, 0xFFFFFFFF);

            if (this.selectedAccount == account) {
                DrawUtil.glDrawOutlinedQuad(left + 1, top + 1, Math.max(left, accountEntryHeight + xBuffer + this.mc.fontRendererObj.getStringWidth(username)) - 2, accountEntryHeight - 2, 1, 0xFF434343);
            }

            DrawUtil.glDrawPlayerFace(left + xBuffer, top + yBuffer, headSize, headSize, DefaultPlayerSkin.getDefaultSkinLegacy());

            if (account.isWorking()) {
                final int iconSize = 16;

                if (account.isBanned()) {
                    if (account.getUnbanDate() == -1337) {
                        this.mc.fontRendererObj.drawStringWithShadow("Sec Alert",
                                                                     left + headSize + xBuffer * 2,
                                                                     top + yBuffer * 2 + yBuffer + 9 * 2, 0xFFFF2059);
                    } else {
                        final Date timeRemaining = new Date(account.getUnbanDate() - System.currentTimeMillis());
                        final SimpleDateFormat hypixelDateFormat = new SimpleDateFormat("D'd' H'h' m'm' s's'");
                        this.mc.fontRendererObj.drawStringWithShadow(hypixelDateFormat.format(timeRemaining),
                                                                     left + headSize + xBuffer * 2,
                                                                     top + yBuffer * 2 + yBuffer + 9 * 2, 0xFFFFFF59);
                    }

                    DrawUtil.glDrawWarningImage(left * 2.0 - iconSize - xBuffer, top + yBuffer, iconSize, iconSize, 0xFFFFFF00);
                } else {
                    DrawUtil.glDrawCheckmarkImage(left * 2.0 - iconSize - xBuffer, top + yBuffer, iconSize, iconSize, 0xFF00FF59);
                }
            }

            top += accountEntryHeight;
        }

        if (this.hasScrollbar) {
            if (this.scrollOffset > 0) {
                glTranslated(0, this.scrollOffset, 0);
            }

            DrawUtil.glEndScissor();
        }
    }

    @Override
    public void initGui() {
        final int xBuffer = 2;
        final int yBuffer = 2;
        final int unbufferedWidth = this.width / 6;
        final int buttonWidth = this.width / 6 - xBuffer * 2;
        final int buttonHeight = 20;
        final int unbufferedHeight = buttonHeight + yBuffer * 2;

        final int left = this.width / 3;
        final int top = this.height - 50;

        this.buttonList.add(loginButton = new GuiButton(0, left + xBuffer, top + yBuffer, buttonWidth, buttonHeight, "Re-Login"));
        //this.buttonList.add(new GuiButton(1, left + unbufferedWidth + xBuffer, top + yBuffer, buttonWidth, buttonHeight, "Add"));
        this.buttonList.add(deleteButton = new GuiButton(2, left + xBuffer, top + yBuffer + unbufferedHeight, buttonWidth, buttonHeight, "Delete"));
        this.buttonList.add(new GuiButton(3, left + unbufferedWidth + xBuffer, top + yBuffer + unbufferedHeight, buttonWidth, buttonHeight, "Done"));

        final int directLoginWidth = Math.max(buttonWidth / 2, this.mc.fontRendererObj.getStringWidth("Add") + xBuffer * 2);
        this.buttonList.add(new GuiButton(4, left - directLoginWidth - xBuffer, top + yBuffer + unbufferedHeight,
                                          directLoginWidth,
                                          buttonHeight, "Add"));

        final int notWorkingWidth = Math.max(buttonWidth / 2, this.mc.fontRendererObj.getStringWidth("Filter Working") + xBuffer * 2);
        final int bannedWidth = Math.max(buttonWidth / 2, this.mc.fontRendererObj.getStringWidth("Filter Unbanned") + xBuffer * 2);

        this.buttonList.add(new GuiButton(6, left * 2 + xBuffer, top + yBuffer + unbufferedHeight,
                                          notWorkingWidth, buttonHeight, "Filter Working"));

        this.buttonList.add(new GuiButton(7, left * 2 + xBuffer, top + yBuffer,
                                          bannedWidth, buttonHeight, "Filter Unbanned"));
    }

    private static void reLoginAccount(final Account account) {
        KetamineClient.getInstance().getNotificationManager().add(NotificationType.INFO,
                "Re-Logging",
                String.format("Logging in with refresh token %s.", account.getUsername()),
                1000L);

        MicrosoftAuthResult authResult = null;

        try {
            authResult = KetamineClient.getInstance().getMicrosoftAuthenticator().loginWithRefreshToken(account.getRefreshToken());
        } catch (Exception ignored) {
        }

        if (authResult != null) {
            KetamineClient.getInstance().getNotificationManager().add(NotificationType.SUCCESS,
                    "Logged In",
                    String.format("Successfully logged into %s.", authResult.getProfile().getName()),
                    1500L);

            Minecraft.getMinecraft().setSession(new Session(authResult.getProfile().getName(), authResult.getProfile().getId(), authResult.getAccessToken(), "mojang"));

            account.setUsername(account.getUsername());
            account.setWorking(true);
            account.setAccessToken(authResult.getAccessToken());
            account.setRefreshToken(authResult.getRefreshToken());
        } else {
            KetamineClient.getInstance().getNotificationManager().add(NotificationType.ERROR,
                    "Re-Log Failed",
                    String.format("Unable to login to account %s.", account.getUsername()),
                    1500L);
        }

        GuiAddAccount.clearCookieStore();
    }

    @Override
    protected void actionPerformed(GuiButton button) throws IOException {
        final AccountManager accountManager = KetamineClient.getInstance().getAccountManager();

        switch (button.id) {
            case 0:
                if (this.selectedAccount != null) {
                    reLoginAccount(this.selectedAccount);
                }
                break;
            case 2:
                accountManager.deleteAccount(this.selectedAccount);
                KetamineClient.getInstance().getNotificationManager().add(NotificationType.SUCCESS,
                                                                          "Deleted Account",
                                                                          String.format("Successfully deleted %s", this.selectedAccount.getUsername()),
                                                                          1000L);
                this.selectedAccount = null;
                break;
            case 3:
                this.mc.displayGuiScreen(this.parent);
                break;
            case 4:
                this.mc.displayGuiScreen(new GuiAddAccount(this));
                break;
            case 6:
                final Collection<Account> workingAccounts = accountManager.getWorkingAccounts();
                final List<Account> notWorking = new ArrayList<>();

                accountManager.getAccounts().forEach(account -> {
                    if (!workingAccounts.contains(account)) {
                        notWorking.add(account);
                    }
                });

                notWorking.forEach(accountManager::deleteAccount);
                break;
            case 7:
                final Collection<Account> unbannedAccounts = accountManager.getUnbannedAccounts();
                final List<Account> banned = new ArrayList<>();

                accountManager.getAccounts().forEach(account -> {
                    if (!unbannedAccounts.contains(account)) {
                        banned.add(account);
                    }
                });

                banned.forEach(accountManager::deleteAccount);
                break;
        }
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        int top = 30;

        final int accountEntryHeight = 36;
        final int left = this.width / 3;

        boolean selectedAccount = false;

        if (mouseY > top && mouseY < this.height - 58 && mouseX > left) {
            mouseY += this.scrollOffset;
            for (final Account account : KetamineClient.getInstance().getAccountManager().getAccounts()) {
                if (mouseY > top && mouseY < top + accountEntryHeight &&
                    mouseX < left + Math.max(left, accountEntryHeight + this.mc.fontRendererObj.getStringWidth(account.getUsername()) + 2)) {
                    this.selectedAccount = account;
                    selectedAccount = true;
                    break;
                }

                top += accountEntryHeight;
            }
            mouseY -= this.scrollOffset;
        }

        if (mouseButton == 0 && selectedAccount) {
            if (System.currentTimeMillis() - this.lastClick < 100L) {
                reLoginAccount(this.selectedAccount);
            }

            this.lastClick = System.currentTimeMillis();
        }

        if (!selectedAccount && mouseY < this.height - 58) {
            this.selectedAccount = null;
        }

        super.mouseClicked(mouseX, mouseY, mouseButton);
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
