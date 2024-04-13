package dev.tenacity.ui.auth;

import dev.tenacity.util.auth.MicrosoftAuth;
import dev.tenacity.util.auth.SessionManager;
import fr.litarvan.openauth.microsoft.MicrosoftAuthResult;
import fr.litarvan.openauth.microsoft.MicrosoftAuthenticationException;
import fr.litarvan.openauth.microsoft.MicrosoftAuthenticator;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.util.Session;

import java.awt.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static dev.tenacity.ui.IScreen.sr;

class PCIYWB extends GuiScreen {
    private String status = null;
    private String cause = null;
    private ExecutorService executor = null;
    private CompletableFuture<Void> task = null;
        @Override
        public void drawScreen(int mouseX, int mouseY, float partialTicks) {
            this.drawDefaultBackground();
            super.drawScreen(mouseX, mouseY, partialTicks);
            GuiLogin.drawCenteredString(this.mc.fontRendererObj, "Please continue in your web browser", (int) (this.width / 2), (int) (sr.getScaledHeight() / 2 - 65), -1);
        }
        @Override
        public void initGui() {
            int textY = (int) (sr.getScaledHeight() / 2 - 65);
            int buttonY = textY + 30;
            int buttonWidth = 120;
            int buttonHeight = 20;
            int buttonX = (this.width / 2) - (buttonWidth / 2);

            this.buttonList.add(new GuiButton(0, buttonX, buttonY, buttonWidth, buttonHeight, "Done"));
            this.buttonList.add(new GuiButton(1, buttonX, buttonY + buttonHeight + 5, buttonWidth, buttonHeight, "Cancel"));
            super.initGui();

            if (task == null) {
                if (executor == null) {
                    executor = Executors.newSingleThreadExecutor();
                }
                task = MicrosoftAuth.acquireMSAuthCode(executor)
                        .thenComposeAsync(msAuthCode -> {
                            return MicrosoftAuth.acquireMSAccessToken(msAuthCode, executor);
                        })
                        .thenComposeAsync(msAccessToken -> {
                            return MicrosoftAuth.acquireXboxAccessToken(msAccessToken, executor);
                        })
                        .thenComposeAsync(xboxAccessToken -> {
                            return MicrosoftAuth.acquireXboxXstsToken(xboxAccessToken, executor);
                        })
                        .thenComposeAsync(xboxXstsData -> {
                            return MicrosoftAuth.acquireMCAccessToken(
                                    xboxXstsData.get("Token"), xboxXstsData.get("uhs"), executor
                            );
                        })
                        .thenComposeAsync(mcToken -> {
                            return MicrosoftAuth.login(mcToken, executor);
                        })
                        .thenAccept(session -> {
                            SessionManager.setSession(session);
                        })
                        .thenAccept(session -> {
                        })
                        .exceptionally(error -> {
                            status = String.format("&c%s&r", error.getMessage());
                            cause = String.format("&c&o%s&r", error.getCause().getMessage());
                            return null;
                        });
            }
        }

        @Override
        protected void actionPerformed(GuiButton button) {
            if (button.id == 0) {
                mc.displayGuiScreen(new AltManagerGui());
            }
            if (button.id == 1) {
                SessionChanger.getInstance().setUserOffline("Tenacity");
                mc.displayGuiScreen(new AltManagerGui());
            }
        }
    }
