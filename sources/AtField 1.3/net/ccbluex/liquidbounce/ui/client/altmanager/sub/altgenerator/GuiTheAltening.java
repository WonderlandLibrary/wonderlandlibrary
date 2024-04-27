/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.authlib.Agent
 *  com.mojang.authlib.exceptions.AuthenticationException
 *  com.mojang.authlib.yggdrasil.YggdrasilAuthenticationService
 *  com.mojang.authlib.yggdrasil.YggdrasilUserAuthentication
 *  com.thealtening.AltService$EnumAltService
 *  com.thealtening.api.TheAltening
 *  com.thealtening.api.TheAltening$Asynchronous
 *  com.thealtening.api.data.AccountData
 *  kotlin.Unit
 *  kotlin.jvm.internal.DefaultConstructorMarker
 *  org.lwjgl.input.Keyboard
 */
package net.ccbluex.liquidbounce.ui.client.altmanager.sub.altgenerator;

import com.mojang.authlib.Agent;
import com.mojang.authlib.exceptions.AuthenticationException;
import com.mojang.authlib.yggdrasil.YggdrasilAuthenticationService;
import com.mojang.authlib.yggdrasil.YggdrasilUserAuthentication;
import com.thealtening.AltService;
import com.thealtening.api.TheAltening;
import com.thealtening.api.data.AccountData;
import java.net.Proxy;
import java.util.concurrent.CompletableFuture;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import kotlin.Unit;
import kotlin.jvm.internal.DefaultConstructorMarker;
import net.ccbluex.liquidbounce.LiquidBounce;
import net.ccbluex.liquidbounce.api.minecraft.client.gui.IGuiButton;
import net.ccbluex.liquidbounce.api.minecraft.client.gui.IGuiTextField;
import net.ccbluex.liquidbounce.api.util.WrappedGuiScreen;
import net.ccbluex.liquidbounce.event.SessionEvent;
import net.ccbluex.liquidbounce.ui.client.altmanager.GuiAltManager;
import net.ccbluex.liquidbounce.ui.font.Fonts;
import net.ccbluex.liquidbounce.utils.ClientUtils;
import net.ccbluex.liquidbounce.utils.MinecraftInstance;
import net.ccbluex.liquidbounce.utils.misc.MiscUtils;
import net.ccbluex.liquidbounce.utils.render.RenderUtils;
import org.lwjgl.input.Keyboard;

public final class GuiTheAltening
extends WrappedGuiScreen {
    private IGuiButton loginButton;
    private static String apiKey;
    private IGuiButton generateButton;
    private IGuiTextField tokenField;
    private IGuiTextField apiKeyField;
    private final GuiAltManager prevGui;
    public static final Companion Companion;
    private String status;

    public static final void access$setLoginButton$p(GuiTheAltening guiTheAltening, IGuiButton iGuiButton) {
        guiTheAltening.loginButton = iGuiButton;
    }

    public static final GuiAltManager access$getPrevGui$p(GuiTheAltening guiTheAltening) {
        return guiTheAltening.prevGui;
    }

    public static final void access$setApiKey$cp(String string) {
        apiKey = string;
    }

    public static final IGuiTextField access$getTokenField$p(GuiTheAltening guiTheAltening) {
        return guiTheAltening.tokenField;
    }

    public static final IGuiButton access$getGenerateButton$p(GuiTheAltening guiTheAltening) {
        return guiTheAltening.generateButton;
    }

    @Override
    public void keyTyped(char c, int n) {
        if (1 == n) {
            MinecraftInstance.mc.displayGuiScreen(this.prevGui.getRepresentedScreen());
            return;
        }
        if (this.apiKeyField.isFocused()) {
            this.apiKeyField.textboxKeyTyped(c, n);
        }
        if (this.tokenField.isFocused()) {
            this.tokenField.textboxKeyTyped(c, n);
        }
        super.keyTyped(c, n);
    }

    @Override
    public void updateScreen() {
        this.apiKeyField.updateCursorCounter();
        this.tokenField.updateCursorCounter();
        super.updateScreen();
    }

    @Override
    public void mouseClicked(int n, int n2, int n3) {
        this.apiKeyField.mouseClicked(n, n2, n3);
        this.tokenField.mouseClicked(n, n2, n3);
        super.mouseClicked(n, n2, n3);
    }

    @Override
    public void initGui() {
        Keyboard.enableRepeatEvents((boolean)true);
        this.loginButton = MinecraftInstance.classProvider.createGuiButton(2, this.getRepresentedScreen().getWidth() / 2 - 100, 75, "Login");
        this.getRepresentedScreen().getButtonList().add(this.loginButton);
        this.generateButton = MinecraftInstance.classProvider.createGuiButton(1, this.getRepresentedScreen().getWidth() / 2 - 100, 140, "Generate");
        this.getRepresentedScreen().getButtonList().add(this.generateButton);
        this.getRepresentedScreen().getButtonList().add(MinecraftInstance.classProvider.createGuiButton(3, this.getRepresentedScreen().getWidth() / 2 - 100, this.getRepresentedScreen().getHeight() - 54, 98, 20, "Buy"));
        this.getRepresentedScreen().getButtonList().add(MinecraftInstance.classProvider.createGuiButton(0, this.getRepresentedScreen().getWidth() / 2 + 2, this.getRepresentedScreen().getHeight() - 54, 98, 20, "Back"));
        this.tokenField = MinecraftInstance.classProvider.createGuiTextField(666, Fonts.roboto40, this.getRepresentedScreen().getWidth() / 2 - 100, 50, 200, 20);
        this.tokenField.setFocused(true);
        this.tokenField.setMaxStringLength(Integer.MAX_VALUE);
        this.apiKeyField = MinecraftInstance.classProvider.createGuiPasswordField(1337, Fonts.roboto40, this.getRepresentedScreen().getWidth() / 2 - 100, 115, 200, 20);
        this.apiKeyField.setMaxStringLength(18);
        this.apiKeyField.setText(apiKey);
        super.initGui();
    }

    @Override
    public void actionPerformed(IGuiButton iGuiButton) {
        if (!iGuiButton.getEnabled()) {
            return;
        }
        switch (iGuiButton.getId()) {
            case 0: {
                MinecraftInstance.mc.displayGuiScreen(this.prevGui.getRepresentedScreen());
                break;
            }
            case 1: {
                this.loginButton.setEnabled(false);
                this.generateButton.setEnabled(false);
                apiKey = this.apiKeyField.getText();
                TheAltening theAltening = new TheAltening(apiKey);
                TheAltening.Asynchronous asynchronous = new TheAltening.Asynchronous(theAltening);
                this.status = "\u00a7cGenerating account...";
                ((CompletableFuture)((CompletableFuture)asynchronous.getAccountData().thenAccept(new Consumer(this){
                    final GuiTheAltening this$0;

                    public final void accept(AccountData accountData) {
                        GuiTheAltening.access$setStatus$p(this.this$0, "\u00a7aGenerated account: \u00a7b\u00a7l" + accountData.getUsername());
                        try {
                            String string;
                            GuiTheAltening guiTheAltening;
                            GuiTheAltening.access$setStatus$p(this.this$0, "\u00a7cSwitching Alt Service...");
                            GuiAltManager.altService.switchService(AltService.EnumAltService.THEALTENING);
                            GuiTheAltening.access$setStatus$p(this.this$0, "\u00a7cLogging in...");
                            YggdrasilUserAuthentication yggdrasilUserAuthentication = new YggdrasilUserAuthentication(new YggdrasilAuthenticationService(Proxy.NO_PROXY, ""), Agent.MINECRAFT);
                            yggdrasilUserAuthentication.setUsername(accountData.getToken());
                            yggdrasilUserAuthentication.setPassword("AtField");
                            GuiTheAltening guiTheAltening2 = this.this$0;
                            try {
                                guiTheAltening = guiTheAltening2;
                                yggdrasilUserAuthentication.logIn();
                                MinecraftInstance.mc.setSession(MinecraftInstance.classProvider.createSession(yggdrasilUserAuthentication.getSelectedProfile().getName(), yggdrasilUserAuthentication.getSelectedProfile().getId().toString(), yggdrasilUserAuthentication.getAuthenticatedToken(), "mojang"));
                                LiquidBounce.INSTANCE.getEventManager().callEvent(new SessionEvent());
                                GuiTheAltening.access$getPrevGui$p((GuiTheAltening)this.this$0).status = "\u00a7aYour name is now \u00a7b\u00a7l" + yggdrasilUserAuthentication.getSelectedProfile().getName() + "\u00a7c.";
                                MinecraftInstance.mc.displayGuiScreen(GuiTheAltening.access$getPrevGui$p(this.this$0).getRepresentedScreen());
                                string = "";
                            }
                            catch (AuthenticationException authenticationException) {
                                guiTheAltening = guiTheAltening2;
                                GuiAltManager.altService.switchService(AltService.EnumAltService.MOJANG);
                                ClientUtils.getLogger().error("Failed to login.", (Throwable)authenticationException);
                                string = "\u00a7cFailed to login: " + authenticationException.getMessage();
                            }
                            GuiTheAltening.access$setStatus$p(guiTheAltening, string);
                        }
                        catch (Throwable throwable) {
                            GuiTheAltening.access$setStatus$p(this.this$0, "\u00a7cFailed to login. Unknown error.");
                            ClientUtils.getLogger().error("Failed to login.", throwable);
                        }
                        GuiTheAltening.access$getLoginButton$p(this.this$0).setEnabled(true);
                        GuiTheAltening.access$getGenerateButton$p(this.this$0).setEnabled(true);
                    }

                    static {
                    }
                    {
                        this.this$0 = guiTheAltening;
                    }

                    public void accept(Object object) {
                        this.accept((AccountData)object);
                    }
                })).handle(new BiFunction(this){
                    final GuiTheAltening this$0;

                    public Object apply(Object object, Object object2) {
                        this.apply((Void)object, (Throwable)object2);
                        return Unit.INSTANCE;
                    }

                    public final void apply(Void void_, Throwable throwable) {
                        GuiTheAltening.access$setStatus$p(this.this$0, "\u00a7cFailed to generate account.");
                        ClientUtils.getLogger().error("Failed to generate account.", throwable);
                    }

                    static {
                    }
                    {
                        this.this$0 = guiTheAltening;
                    }
                })).whenComplete(new BiConsumer(this){
                    final GuiTheAltening this$0;
                    {
                        this.this$0 = guiTheAltening;
                    }

                    static {
                    }

                    public void accept(Object object, Object object2) {
                        this.accept((Unit)object, (Throwable)object2);
                    }

                    public final void accept(Unit unit, Throwable throwable) {
                        GuiTheAltening.access$getLoginButton$p(this.this$0).setEnabled(true);
                        GuiTheAltening.access$getGenerateButton$p(this.this$0).setEnabled(true);
                    }
                });
                break;
            }
            case 2: {
                this.loginButton.setEnabled(false);
                this.generateButton.setEnabled(false);
                new Thread(new Runnable(this){
                    final GuiTheAltening this$0;

                    static {
                    }
                    {
                        this.this$0 = guiTheAltening;
                    }

                    public final void run() {
                        try {
                            String string;
                            GuiTheAltening guiTheAltening;
                            GuiTheAltening.access$setStatus$p(this.this$0, "\u00a7cSwitching Alt Service...");
                            GuiAltManager.altService.switchService(AltService.EnumAltService.THEALTENING);
                            GuiTheAltening.access$setStatus$p(this.this$0, "\u00a7cLogging in...");
                            YggdrasilUserAuthentication yggdrasilUserAuthentication = new YggdrasilUserAuthentication(new YggdrasilAuthenticationService(Proxy.NO_PROXY, ""), Agent.MINECRAFT);
                            yggdrasilUserAuthentication.setUsername(GuiTheAltening.access$getTokenField$p(this.this$0).getText());
                            yggdrasilUserAuthentication.setPassword("AtField");
                            GuiTheAltening guiTheAltening2 = this.this$0;
                            try {
                                guiTheAltening = guiTheAltening2;
                                yggdrasilUserAuthentication.logIn();
                                MinecraftInstance.mc.setSession(MinecraftInstance.classProvider.createSession(yggdrasilUserAuthentication.getSelectedProfile().getName(), yggdrasilUserAuthentication.getSelectedProfile().getId().toString(), yggdrasilUserAuthentication.getAuthenticatedToken(), "mojang"));
                                LiquidBounce.INSTANCE.getEventManager().callEvent(new SessionEvent());
                                GuiTheAltening.access$getPrevGui$p((GuiTheAltening)this.this$0).status = "\u00a7aYour name is now \u00a7b\u00a7l" + yggdrasilUserAuthentication.getSelectedProfile().getName() + "\u00a7c.";
                                MinecraftInstance.mc.displayGuiScreen(GuiTheAltening.access$getPrevGui$p(this.this$0).getRepresentedScreen());
                                string = "\u00a7aYour name is now \u00a7b\u00a7l" + yggdrasilUserAuthentication.getSelectedProfile().getName() + "\u00a7c.";
                            }
                            catch (AuthenticationException authenticationException) {
                                guiTheAltening = guiTheAltening2;
                                GuiAltManager.altService.switchService(AltService.EnumAltService.MOJANG);
                                ClientUtils.getLogger().error("Failed to login.", (Throwable)authenticationException);
                                string = "\u00a7cFailed to login: " + authenticationException.getMessage();
                            }
                            GuiTheAltening.access$setStatus$p(guiTheAltening, string);
                        }
                        catch (Throwable throwable) {
                            ClientUtils.getLogger().error("Failed to login.", throwable);
                            GuiTheAltening.access$setStatus$p(this.this$0, "\u00a7cFailed to login. Unknown error.");
                        }
                        GuiTheAltening.access$getLoginButton$p(this.this$0).setEnabled(true);
                        GuiTheAltening.access$getGenerateButton$p(this.this$0).setEnabled(true);
                    }
                }).start();
                break;
            }
            case 3: {
                MiscUtils.showURL("https://thealtening.com/?ref=liquidbounce");
                break;
            }
        }
    }

    public static final void access$setStatus$p(GuiTheAltening guiTheAltening, String string) {
        guiTheAltening.status = string;
    }

    @Override
    public void onGuiClosed() {
        Keyboard.enableRepeatEvents((boolean)false);
        apiKey = this.apiKeyField.getText();
        super.onGuiClosed();
    }

    public static final void access$setGenerateButton$p(GuiTheAltening guiTheAltening, IGuiButton iGuiButton) {
        guiTheAltening.generateButton = iGuiButton;
    }

    public static final IGuiButton access$getLoginButton$p(GuiTheAltening guiTheAltening) {
        return guiTheAltening.loginButton;
    }

    public static final String access$getApiKey$cp() {
        return apiKey;
    }

    public static final void access$setTokenField$p(GuiTheAltening guiTheAltening, IGuiTextField iGuiTextField) {
        guiTheAltening.tokenField = iGuiTextField;
    }

    public GuiTheAltening(GuiAltManager guiAltManager) {
        this.prevGui = guiAltManager;
        this.status = "";
    }

    static {
        Companion = new Companion(null);
        apiKey = "";
    }

    public static final String access$getStatus$p(GuiTheAltening guiTheAltening) {
        return guiTheAltening.status;
    }

    @Override
    public void drawScreen(int n, int n2, float f) {
        this.getRepresentedScreen().drawBackground(0);
        RenderUtils.drawRect(30.0f, 30.0f, (float)this.getRepresentedScreen().getWidth() - 30.0f, (float)this.getRepresentedScreen().getHeight() - 30.0f, Integer.MIN_VALUE);
        Fonts.roboto35.drawCenteredString("TheAltening", (float)this.getRepresentedScreen().getWidth() / 2.0f, 6.0f, 0xFFFFFF);
        Fonts.roboto35.drawCenteredString(this.status, (float)this.getRepresentedScreen().getWidth() / 2.0f, 18.0f, 0xFFFFFF);
        this.apiKeyField.drawTextBox();
        this.tokenField.drawTextBox();
        Fonts.roboto40.drawCenteredString("\u00a77Token:", (float)this.getRepresentedScreen().getWidth() / 2.0f - (float)84, 40.0f, 0xFFFFFF);
        Fonts.roboto40.drawCenteredString("\u00a77API-Key:", (float)this.getRepresentedScreen().getWidth() / 2.0f - (float)78, 105.0f, 0xFFFFFF);
        Fonts.roboto40.drawCenteredString("\u00a77Use coupon code 'liquidbounce' for 20% off!", (float)this.getRepresentedScreen().getWidth() / 2.0f, (float)this.getRepresentedScreen().getHeight() - 65.0f, 0xFFFFFF);
        super.drawScreen(n, n2, f);
    }

    public static final class Companion {
        public final String getApiKey() {
            return GuiTheAltening.access$getApiKey$cp();
        }

        private Companion() {
        }

        public final void setApiKey(String string) {
            GuiTheAltening.access$setApiKey$cp(string);
        }

        public Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }
    }
}

