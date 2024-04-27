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
import net.mcleaks.MCLeaks;
import org.lwjgl.input.Keyboard;

public final class GuiTheAltening
extends WrappedGuiScreen {
    private IGuiButton loginButton;
    private IGuiButton generateButton;
    private IGuiTextField apiKeyField;
    private IGuiTextField tokenField;
    private String status;
    private final GuiAltManager prevGui;
    private static String apiKey;
    public static final Companion Companion;

    @Override
    public void initGui() {
        Keyboard.enableRepeatEvents((boolean)true);
        this.loginButton = MinecraftInstance.classProvider.createGuiButton(2, this.getRepresentedScreen().getWidth() / 2 - 100, 75, "Login");
        this.getRepresentedScreen().getButtonList().add(this.loginButton);
        this.generateButton = MinecraftInstance.classProvider.createGuiButton(1, this.getRepresentedScreen().getWidth() / 2 - 100, 140, "Generate");
        this.getRepresentedScreen().getButtonList().add(this.generateButton);
        this.getRepresentedScreen().getButtonList().add(MinecraftInstance.classProvider.createGuiButton(3, this.getRepresentedScreen().getWidth() / 2 - 100, this.getRepresentedScreen().getHeight() - 54, 98, 20, "Buy"));
        this.getRepresentedScreen().getButtonList().add(MinecraftInstance.classProvider.createGuiButton(0, this.getRepresentedScreen().getWidth() / 2 + 2, this.getRepresentedScreen().getHeight() - 54, 98, 20, "Back"));
        this.tokenField = MinecraftInstance.classProvider.createGuiTextField(666, Fonts.font40, this.getRepresentedScreen().getWidth() / 2 - 100, 50, 200, 20);
        this.tokenField.setFocused(true);
        this.tokenField.setMaxStringLength(Integer.MAX_VALUE);
        this.apiKeyField = MinecraftInstance.classProvider.createGuiPasswordField(1337, Fonts.font40, this.getRepresentedScreen().getWidth() / 2 - 100, 115, 200, 20);
        this.apiKeyField.setMaxStringLength(18);
        this.apiKeyField.setText(apiKey);
        super.initGui();
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        this.getRepresentedScreen().drawBackground(0);
        RenderUtils.drawRect(30.0f, 30.0f, (float)this.getRepresentedScreen().getWidth() - 30.0f, (float)this.getRepresentedScreen().getHeight() - 30.0f, Integer.MIN_VALUE);
        Fonts.font35.drawCenteredString("TheAltening", (float)this.getRepresentedScreen().getWidth() / 2.0f, 6.0f, 0xFFFFFF);
        Fonts.font35.drawCenteredString(this.status, (float)this.getRepresentedScreen().getWidth() / 2.0f, 18.0f, 0xFFFFFF);
        this.apiKeyField.drawTextBox();
        this.tokenField.drawTextBox();
        Fonts.font40.drawCenteredString("\u00a77Token:", (float)this.getRepresentedScreen().getWidth() / 2.0f - (float)84, 40.0f, 0xFFFFFF);
        Fonts.font40.drawCenteredString("\u00a77API-Key:", (float)this.getRepresentedScreen().getWidth() / 2.0f - (float)78, 105.0f, 0xFFFFFF);
        Fonts.font40.drawCenteredString("\u00a77Use coupon code 'liquidbounce' for 20% off!", (float)this.getRepresentedScreen().getWidth() / 2.0f, (float)this.getRepresentedScreen().getHeight() - 65.0f, 0xFFFFFF);
        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    @Override
    public void actionPerformed(IGuiButton button) {
        if (!button.getEnabled()) {
            return;
        }
        switch (button.getId()) {
            case 0: {
                MinecraftInstance.mc.displayGuiScreen(this.prevGui.getRepresentedScreen());
                break;
            }
            case 1: {
                this.loginButton.setEnabled(false);
                this.generateButton.setEnabled(false);
                apiKey = this.apiKeyField.getText();
                TheAltening altening = new TheAltening(apiKey);
                TheAltening.Asynchronous asynchronous = new TheAltening.Asynchronous(altening);
                this.status = "\u00a7cGenerating account...";
                ((CompletableFuture)((CompletableFuture)asynchronous.getAccountData().thenAccept(new Consumer<AccountData>(this){
                    final /* synthetic */ GuiTheAltening this$0;

                    /*
                     * WARNING - void declaration
                     */
                    public final void accept(AccountData account) {
                        GuiTheAltening.access$setStatus$p(this.this$0, "\u00a7aGenerated account: \u00a7b\u00a7l" + account.getUsername());
                        try {
                            String string;
                            GuiTheAltening guiTheAltening;
                            GuiTheAltening.access$setStatus$p(this.this$0, "\u00a7cSwitching Alt Service...");
                            GuiAltManager.altService.switchService(AltService.EnumAltService.THEALTENING);
                            GuiTheAltening.access$setStatus$p(this.this$0, "\u00a7cLogging in...");
                            YggdrasilUserAuthentication yggdrasilUserAuthentication = new YggdrasilUserAuthentication(new YggdrasilAuthenticationService(Proxy.NO_PROXY, ""), Agent.MINECRAFT);
                            yggdrasilUserAuthentication.setUsername(account.getToken());
                            yggdrasilUserAuthentication.setPassword("LRQ");
                            GuiTheAltening guiTheAltening2 = this.this$0;
                            try {
                                guiTheAltening = guiTheAltening2;
                                yggdrasilUserAuthentication.logIn();
                                MinecraftInstance.mc.setSession(MinecraftInstance.classProvider.createSession(yggdrasilUserAuthentication.getSelectedProfile().getName(), yggdrasilUserAuthentication.getSelectedProfile().getId().toString(), yggdrasilUserAuthentication.getAuthenticatedToken(), "mojang"));
                                LiquidBounce.INSTANCE.getEventManager().callEvent(new SessionEvent());
                                MCLeaks.remove();
                                GuiTheAltening.access$getPrevGui$p((GuiTheAltening)this.this$0).status = "\u00a7aYour name is now \u00a7b\u00a7l" + yggdrasilUserAuthentication.getSelectedProfile().getName() + "\u00a7c.";
                                MinecraftInstance.mc.displayGuiScreen(GuiTheAltening.access$getPrevGui$p(this.this$0).getRepresentedScreen());
                                string = "";
                            }
                            catch (AuthenticationException authenticationException) {
                                void e;
                                guiTheAltening = guiTheAltening2;
                                GuiAltManager.altService.switchService(AltService.EnumAltService.MOJANG);
                                ClientUtils.getLogger().error("Failed to login.", (Throwable)e);
                                string = "\u00a7cFailed to login: " + e.getMessage();
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
                    {
                        this.this$0 = guiTheAltening;
                    }
                })).handle(new BiFunction<T, Throwable, U>(this){
                    final /* synthetic */ GuiTheAltening this$0;

                    public final void apply(Void $noName_0, Throwable err) {
                        GuiTheAltening.access$setStatus$p(this.this$0, "\u00a7cFailed to generate account.");
                        ClientUtils.getLogger().error("Failed to generate account.", err);
                    }
                    {
                        this.this$0 = guiTheAltening;
                    }
                })).whenComplete(new BiConsumer<Unit, Throwable>(this){
                    final /* synthetic */ GuiTheAltening this$0;

                    public final void accept(Unit $noName_0, Throwable $noName_1) {
                        GuiTheAltening.access$getLoginButton$p(this.this$0).setEnabled(true);
                        GuiTheAltening.access$getGenerateButton$p(this.this$0).setEnabled(true);
                    }
                    {
                        this.this$0 = guiTheAltening;
                    }
                });
                break;
            }
            case 2: {
                this.loginButton.setEnabled(false);
                this.generateButton.setEnabled(false);
                new Thread(new Runnable(this){
                    final /* synthetic */ GuiTheAltening this$0;

                    /*
                     * WARNING - void declaration
                     */
                    public final void run() {
                        try {
                            String string;
                            GuiTheAltening guiTheAltening;
                            GuiTheAltening.access$setStatus$p(this.this$0, "\u00a7cSwitching Alt Service...");
                            GuiAltManager.altService.switchService(AltService.EnumAltService.THEALTENING);
                            GuiTheAltening.access$setStatus$p(this.this$0, "\u00a7cLogging in...");
                            YggdrasilUserAuthentication yggdrasilUserAuthentication = new YggdrasilUserAuthentication(new YggdrasilAuthenticationService(Proxy.NO_PROXY, ""), Agent.MINECRAFT);
                            yggdrasilUserAuthentication.setUsername(GuiTheAltening.access$getTokenField$p(this.this$0).getText());
                            yggdrasilUserAuthentication.setPassword("LRQ");
                            GuiTheAltening guiTheAltening2 = this.this$0;
                            try {
                                guiTheAltening = guiTheAltening2;
                                yggdrasilUserAuthentication.logIn();
                                MinecraftInstance.mc.setSession(MinecraftInstance.classProvider.createSession(yggdrasilUserAuthentication.getSelectedProfile().getName(), yggdrasilUserAuthentication.getSelectedProfile().getId().toString(), yggdrasilUserAuthentication.getAuthenticatedToken(), "mojang"));
                                LiquidBounce.INSTANCE.getEventManager().callEvent(new SessionEvent());
                                MCLeaks.remove();
                                GuiTheAltening.access$getPrevGui$p((GuiTheAltening)this.this$0).status = "\u00a7aYour name is now \u00a7b\u00a7l" + yggdrasilUserAuthentication.getSelectedProfile().getName() + "\u00a7c.";
                                MinecraftInstance.mc.displayGuiScreen(GuiTheAltening.access$getPrevGui$p(this.this$0).getRepresentedScreen());
                                string = "\u00a7aYour name is now \u00a7b\u00a7l" + yggdrasilUserAuthentication.getSelectedProfile().getName() + "\u00a7c.";
                            }
                            catch (AuthenticationException authenticationException) {
                                void e;
                                guiTheAltening = guiTheAltening2;
                                GuiAltManager.altService.switchService(AltService.EnumAltService.MOJANG);
                                ClientUtils.getLogger().error("Failed to login.", (Throwable)e);
                                string = "\u00a7cFailed to login: " + e.getMessage();
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
                    {
                        this.this$0 = guiTheAltening;
                    }
                }).start();
                break;
            }
            case 3: {
                MiscUtils.showURL("https://thealtening.com/?ref=liquidbounce");
            }
        }
    }

    @Override
    public void keyTyped(char typedChar, int keyCode) {
        if (1 == keyCode) {
            MinecraftInstance.mc.displayGuiScreen(this.prevGui.getRepresentedScreen());
            return;
        }
        if (this.apiKeyField.isFocused()) {
            this.apiKeyField.textboxKeyTyped(typedChar, keyCode);
        }
        if (this.tokenField.isFocused()) {
            this.tokenField.textboxKeyTyped(typedChar, keyCode);
        }
        super.keyTyped(typedChar, keyCode);
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        this.apiKeyField.mouseClicked(mouseX, mouseY, mouseButton);
        this.tokenField.mouseClicked(mouseX, mouseY, mouseButton);
        super.mouseClicked(mouseX, mouseY, mouseButton);
    }

    @Override
    public void updateScreen() {
        this.apiKeyField.updateCursorCounter();
        this.tokenField.updateCursorCounter();
        super.updateScreen();
    }

    @Override
    public void onGuiClosed() {
        Keyboard.enableRepeatEvents((boolean)false);
        apiKey = this.apiKeyField.getText();
        super.onGuiClosed();
    }

    public GuiTheAltening(GuiAltManager prevGui) {
        this.prevGui = prevGui;
        this.status = "";
    }

    static {
        Companion = new Companion(null);
        apiKey = "";
    }

    public static final /* synthetic */ String access$getStatus$p(GuiTheAltening $this) {
        return $this.status;
    }

    public static final /* synthetic */ void access$setStatus$p(GuiTheAltening $this, String string) {
        $this.status = string;
    }

    public static final /* synthetic */ GuiAltManager access$getPrevGui$p(GuiTheAltening $this) {
        return $this.prevGui;
    }

    public static final /* synthetic */ IGuiButton access$getLoginButton$p(GuiTheAltening $this) {
        return $this.loginButton;
    }

    public static final /* synthetic */ void access$setLoginButton$p(GuiTheAltening $this, IGuiButton iGuiButton) {
        $this.loginButton = iGuiButton;
    }

    public static final /* synthetic */ IGuiButton access$getGenerateButton$p(GuiTheAltening $this) {
        return $this.generateButton;
    }

    public static final /* synthetic */ void access$setGenerateButton$p(GuiTheAltening $this, IGuiButton iGuiButton) {
        $this.generateButton = iGuiButton;
    }

    public static final /* synthetic */ IGuiTextField access$getTokenField$p(GuiTheAltening $this) {
        return $this.tokenField;
    }

    public static final /* synthetic */ void access$setTokenField$p(GuiTheAltening $this, IGuiTextField iGuiTextField) {
        $this.tokenField = iGuiTextField;
    }

    public static final class Companion {
        public final String getApiKey() {
            return apiKey;
        }

        public final void setApiKey(String string) {
            apiKey = string;
        }

        private Companion() {
        }

        public /* synthetic */ Companion(DefaultConstructorMarker $constructor_marker) {
            this();
        }
    }
}

