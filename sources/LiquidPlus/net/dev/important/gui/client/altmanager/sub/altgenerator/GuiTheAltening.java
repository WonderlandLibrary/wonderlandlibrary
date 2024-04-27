/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.authlib.Agent
 *  com.mojang.authlib.exceptions.AuthenticationException
 *  com.mojang.authlib.yggdrasil.YggdrasilAuthenticationService
 *  com.mojang.authlib.yggdrasil.YggdrasilUserAuthentication
 *  net.minecraft.client.gui.FontRenderer
 *  net.minecraft.client.gui.Gui
 *  net.minecraft.client.gui.GuiButton
 *  net.minecraft.client.gui.GuiScreen
 *  net.minecraft.client.gui.GuiTextField
 *  net.minecraft.util.Session
 *  org.jetbrains.annotations.NotNull
 *  org.lwjgl.input.Keyboard
 */
package net.dev.important.gui.client.altmanager.sub.altgenerator;

import com.mojang.authlib.Agent;
import com.mojang.authlib.exceptions.AuthenticationException;
import com.mojang.authlib.yggdrasil.YggdrasilAuthenticationService;
import com.mojang.authlib.yggdrasil.YggdrasilUserAuthentication;
import com.thealtening.AltService;
import com.thealtening.api.TheAltening;
import com.thealtening.api.data.AccountData;
import java.net.Proxy;
import java.util.concurrent.CompletableFuture;
import kotlin.Metadata;
import kotlin.Unit;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import net.dev.important.Client;
import net.dev.important.event.SessionEvent;
import net.dev.important.gui.client.altmanager.GuiAltManager;
import net.dev.important.gui.elements.GuiPasswordField;
import net.dev.important.gui.font.Fonts;
import net.dev.important.gui.font.GameFontRenderer;
import net.dev.important.utils.ClientUtils;
import net.dev.important.utils.misc.MiscUtils;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.util.Session;
import org.jetbrains.annotations.NotNull;
import org.lwjgl.input.Keyboard;

/*
 * Illegal identifiers - consider using --renameillegalidents true
 */
@Metadata(mv={1, 6, 0}, k=1, xi=48, d1={"\u0000H\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0002\b\u0003\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0010\u0007\n\u0002\b\u0003\n\u0002\u0010\f\n\u0002\b\u0007\u0018\u0000 \u001f2\u00020\u0001:\u0001\u001fB\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J\u0010\u0010\r\u001a\u00020\u000e2\u0006\u0010\u000f\u001a\u00020\bH\u0014J \u0010\u0010\u001a\u00020\u000e2\u0006\u0010\u0011\u001a\u00020\u00122\u0006\u0010\u0013\u001a\u00020\u00122\u0006\u0010\u0014\u001a\u00020\u0015H\u0016J\b\u0010\u0016\u001a\u00020\u000eH\u0016J\u0018\u0010\u0017\u001a\u00020\u000e2\u0006\u0010\u0018\u001a\u00020\u00192\u0006\u0010\u001a\u001a\u00020\u0012H\u0014J \u0010\u001b\u001a\u00020\u000e2\u0006\u0010\u0011\u001a\u00020\u00122\u0006\u0010\u0013\u001a\u00020\u00122\u0006\u0010\u001c\u001a\u00020\u0012H\u0014J\b\u0010\u001d\u001a\u00020\u000eH\u0016J\b\u0010\u001e\u001a\u00020\u000eH\u0016R\u000e\u0010\u0005\u001a\u00020\u0006X\u0082.\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\bX\u0082.\u00a2\u0006\u0002\n\u0000R\u000e\u0010\t\u001a\u00020\bX\u0082.\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\n\u001a\u00020\u000bX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\f\u001a\u00020\u0006X\u0082.\u00a2\u0006\u0002\n\u0000\u00a8\u0006 "}, d2={"Lnet/dev/important/gui/client/altmanager/sub/altgenerator/GuiTheAltening;", "Lnet/minecraft/client/gui/GuiScreen;", "prevGui", "Lnet/dev/important/gui/client/altmanager/GuiAltManager;", "(Lnet/dev/important/gui/client/altmanager/GuiAltManager;)V", "apiKeyField", "Lnet/minecraft/client/gui/GuiTextField;", "generateButton", "Lnet/minecraft/client/gui/GuiButton;", "loginButton", "status", "", "tokenField", "actionPerformed", "", "button", "drawScreen", "mouseX", "", "mouseY", "partialTicks", "", "initGui", "keyTyped", "typedChar", "", "keyCode", "mouseClicked", "mouseButton", "onGuiClosed", "updateScreen", "Companion", "LiquidBounce"})
public final class GuiTheAltening
extends GuiScreen {
    @NotNull
    public static final Companion Companion = new Companion(null);
    @NotNull
    private final GuiAltManager prevGui;
    private GuiButton loginButton;
    private GuiButton generateButton;
    private GuiTextField apiKeyField;
    private GuiTextField tokenField;
    @NotNull
    private String status;
    @NotNull
    private static String apiKey = "";

    public GuiTheAltening(@NotNull GuiAltManager prevGui) {
        Intrinsics.checkNotNullParameter((Object)prevGui, "prevGui");
        this.prevGui = prevGui;
        this.status = "";
    }

    public void func_73866_w_() {
        Keyboard.enableRepeatEvents((boolean)true);
        this.loginButton = new GuiButton(2, this.field_146294_l / 2 - 100, 75, "Login");
        GuiButton guiButton = this.loginButton;
        if (guiButton == null) {
            Intrinsics.throwUninitializedPropertyAccessException("loginButton");
            guiButton = null;
        }
        this.field_146292_n.add(guiButton);
        this.generateButton = new GuiButton(1, this.field_146294_l / 2 - 100, 140, "Generate");
        GuiButton guiButton2 = this.generateButton;
        if (guiButton2 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("generateButton");
            guiButton2 = null;
        }
        this.field_146292_n.add(guiButton2);
        this.field_146292_n.add(new GuiButton(3, this.field_146294_l / 2 - 100, this.field_146295_m - 54, 98, 20, "Buy"));
        this.field_146292_n.add(new GuiButton(0, this.field_146294_l / 2 + 2, this.field_146295_m - 54, 98, 20, "Back"));
        this.tokenField = new GuiTextField(666, (FontRenderer)Fonts.font40, this.field_146294_l / 2 - 100, 50, 200, 20);
        GuiTextField guiTextField = this.tokenField;
        if (guiTextField == null) {
            Intrinsics.throwUninitializedPropertyAccessException("tokenField");
            guiTextField = null;
        }
        guiTextField.func_146195_b(true);
        GuiTextField guiTextField2 = this.tokenField;
        if (guiTextField2 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("tokenField");
            guiTextField2 = null;
        }
        guiTextField2.func_146203_f(Integer.MAX_VALUE);
        GameFontRenderer gameFontRenderer = Fonts.font40;
        Intrinsics.checkNotNullExpressionValue((Object)gameFontRenderer, "font40");
        this.apiKeyField = new GuiPasswordField(1337, gameFontRenderer, this.field_146294_l / 2 - 100, 115, 200, 20);
        GuiTextField guiTextField3 = this.apiKeyField;
        if (guiTextField3 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("apiKeyField");
            guiTextField3 = null;
        }
        guiTextField3.func_146203_f(18);
        GuiTextField guiTextField4 = this.apiKeyField;
        if (guiTextField4 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("apiKeyField");
            guiTextField4 = null;
        }
        guiTextField4.func_146180_a(apiKey);
        super.func_73866_w_();
    }

    public void func_73863_a(int mouseX, int mouseY, float partialTicks) {
        this.func_146278_c(0);
        Gui.func_73734_a((int)30, (int)30, (int)(this.field_146294_l - 30), (int)(this.field_146295_m - 30), (int)Integer.MIN_VALUE);
        this.func_73732_a(Fonts.font35, "TheAltening", this.field_146294_l / 2, 6, 0xFFFFFF);
        this.func_73732_a(Fonts.font35, this.status, this.field_146294_l / 2, 18, 0xFFFFFF);
        GuiTextField guiTextField = this.apiKeyField;
        if (guiTextField == null) {
            Intrinsics.throwUninitializedPropertyAccessException("apiKeyField");
            guiTextField = null;
        }
        guiTextField.func_146194_f();
        GuiTextField guiTextField2 = this.tokenField;
        if (guiTextField2 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("tokenField");
            guiTextField2 = null;
        }
        guiTextField2.func_146194_f();
        this.func_73732_a(Fonts.font40, "\u00a77Token:", this.field_146294_l / 2 - 84, 40, 0xFFFFFF);
        this.func_73732_a(Fonts.font40, "\u00a77API-Key:", this.field_146294_l / 2 - 78, 105, 0xFFFFFF);
        super.func_73863_a(mouseX, mouseY, partialTicks);
    }

    protected void func_146284_a(@NotNull GuiButton button) {
        Intrinsics.checkNotNullParameter(button, "button");
        if (!button.field_146124_l) {
            return;
        }
        switch (button.field_146127_k) {
            case 0: {
                this.field_146297_k.func_147108_a((GuiScreen)this.prevGui);
                break;
            }
            case 1: {
                GuiButton guiButton = this.loginButton;
                if (guiButton == null) {
                    Intrinsics.throwUninitializedPropertyAccessException("loginButton");
                    guiButton = null;
                }
                guiButton.field_146124_l = false;
                GuiButton guiButton2 = this.generateButton;
                if (guiButton2 == null) {
                    Intrinsics.throwUninitializedPropertyAccessException("generateButton");
                    guiButton2 = null;
                }
                guiButton2.field_146124_l = false;
                GuiTextField guiTextField = this.apiKeyField;
                if (guiTextField == null) {
                    Intrinsics.throwUninitializedPropertyAccessException("apiKeyField");
                    guiTextField = null;
                }
                String string = guiTextField.func_146179_b();
                Intrinsics.checkNotNullExpressionValue(string, "apiKeyField.text");
                apiKey = string;
                TheAltening altening = new TheAltening(apiKey);
                TheAltening.Asynchronous asynchronous = new TheAltening.Asynchronous(altening);
                this.status = "\u00a7cGenerating account...";
                ((CompletableFuture)((CompletableFuture)asynchronous.getAccountData().thenAccept(arg_0 -> GuiTheAltening.actionPerformed$lambda-0(this, arg_0))).handle((arg_0, arg_1) -> GuiTheAltening.actionPerformed$lambda-1(this, arg_0, arg_1))).whenComplete((arg_0, arg_1) -> GuiTheAltening.actionPerformed$lambda-2(this, arg_0, arg_1));
                break;
            }
            case 2: {
                GuiButton guiButton = this.loginButton;
                if (guiButton == null) {
                    Intrinsics.throwUninitializedPropertyAccessException("loginButton");
                    guiButton = null;
                }
                guiButton.field_146124_l = false;
                GuiButton guiButton3 = this.generateButton;
                if (guiButton3 == null) {
                    Intrinsics.throwUninitializedPropertyAccessException("generateButton");
                    guiButton3 = null;
                }
                guiButton3.field_146124_l = false;
                new Thread(() -> GuiTheAltening.actionPerformed$lambda-3(this)).start();
                break;
            }
            case 3: {
                MiscUtils.showURL("https://thealtening.com/?ref=liquidbounce");
            }
        }
        super.func_146284_a(button);
    }

    protected void func_73869_a(char typedChar, int keyCode) {
        GuiTextField guiTextField;
        if (1 == keyCode) {
            this.field_146297_k.func_147108_a((GuiScreen)this.prevGui);
            return;
        }
        GuiTextField guiTextField2 = this.apiKeyField;
        if (guiTextField2 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("apiKeyField");
            guiTextField2 = null;
        }
        if (guiTextField2.func_146206_l()) {
            GuiTextField guiTextField3 = this.apiKeyField;
            if (guiTextField3 == null) {
                Intrinsics.throwUninitializedPropertyAccessException("apiKeyField");
                guiTextField3 = null;
            }
            guiTextField3.func_146201_a(typedChar, keyCode);
        }
        if ((guiTextField = this.tokenField) == null) {
            Intrinsics.throwUninitializedPropertyAccessException("tokenField");
            guiTextField = null;
        }
        if (guiTextField.func_146206_l()) {
            GuiTextField guiTextField4 = this.tokenField;
            if (guiTextField4 == null) {
                Intrinsics.throwUninitializedPropertyAccessException("tokenField");
                guiTextField4 = null;
            }
            guiTextField4.func_146201_a(typedChar, keyCode);
        }
        super.func_73869_a(typedChar, keyCode);
    }

    protected void func_73864_a(int mouseX, int mouseY, int mouseButton) {
        GuiTextField guiTextField = this.apiKeyField;
        if (guiTextField == null) {
            Intrinsics.throwUninitializedPropertyAccessException("apiKeyField");
            guiTextField = null;
        }
        guiTextField.func_146192_a(mouseX, mouseY, mouseButton);
        GuiTextField guiTextField2 = this.tokenField;
        if (guiTextField2 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("tokenField");
            guiTextField2 = null;
        }
        guiTextField2.func_146192_a(mouseX, mouseY, mouseButton);
        super.func_73864_a(mouseX, mouseY, mouseButton);
    }

    public void func_73876_c() {
        GuiTextField guiTextField = this.apiKeyField;
        if (guiTextField == null) {
            Intrinsics.throwUninitializedPropertyAccessException("apiKeyField");
            guiTextField = null;
        }
        guiTextField.func_146178_a();
        GuiTextField guiTextField2 = this.tokenField;
        if (guiTextField2 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("tokenField");
            guiTextField2 = null;
        }
        guiTextField2.func_146178_a();
        super.func_73876_c();
    }

    public void func_146281_b() {
        Keyboard.enableRepeatEvents((boolean)false);
        GuiTextField guiTextField = this.apiKeyField;
        if (guiTextField == null) {
            Intrinsics.throwUninitializedPropertyAccessException("apiKeyField");
            guiTextField = null;
        }
        String string = guiTextField.func_146179_b();
        Intrinsics.checkNotNullExpressionValue(string, "apiKeyField.text");
        apiKey = string;
        super.func_146281_b();
    }

    /*
     * WARNING - void declaration
     */
    private static final void actionPerformed$lambda-0(GuiTheAltening this$0, AccountData account) {
        GuiButton guiButton;
        Intrinsics.checkNotNullParameter((Object)this$0, "this$0");
        this$0.status = Intrinsics.stringPlus("\u00a7aGenerated account: \u00a7b\u00a7l", account.getUsername());
        try {
            String string;
            GuiTheAltening guiTheAltening;
            this$0.status = "\u00a7cSwitching Alt Service...";
            GuiAltManager.altService.switchService(AltService.EnumAltService.THEALTENING);
            this$0.status = "\u00a7cLogging in...";
            YggdrasilUserAuthentication yggdrasilUserAuthentication = new YggdrasilUserAuthentication(new YggdrasilAuthenticationService(Proxy.NO_PROXY, ""), Agent.MINECRAFT);
            yggdrasilUserAuthentication.setUsername(account.getToken());
            yggdrasilUserAuthentication.setPassword("LiquidPlus");
            GuiTheAltening guiTheAltening2 = this$0;
            try {
                guiTheAltening = guiTheAltening2;
                yggdrasilUserAuthentication.logIn();
                this$0.field_146297_k.field_71449_j = new Session(yggdrasilUserAuthentication.getSelectedProfile().getName(), yggdrasilUserAuthentication.getSelectedProfile().getId().toString(), yggdrasilUserAuthentication.getAuthenticatedToken(), "mojang");
                Client.INSTANCE.getEventManager().callEvent(new SessionEvent());
                this$0.prevGui.status = "\u00a7aYour name is now \u00a7b\u00a7l" + yggdrasilUserAuthentication.getSelectedProfile().getName() + "\u00a7c.";
                this$0.field_146297_k.func_147108_a((GuiScreen)this$0.prevGui);
                string = "";
            }
            catch (AuthenticationException authenticationException) {
                void e;
                guiTheAltening = guiTheAltening2;
                GuiAltManager.altService.switchService(AltService.EnumAltService.MOJANG);
                ClientUtils.getLogger().error("Failed to login.", (Throwable)e);
                string = Intrinsics.stringPlus("\u00a7cFailed to login: ", e.getMessage());
            }
            guiTheAltening.status = string;
        }
        catch (Throwable throwable) {
            this$0.status = "\u00a7cFailed to login. Unknown error.";
            ClientUtils.getLogger().error("Failed to login.", throwable);
        }
        if ((guiButton = this$0.loginButton) == null) {
            Intrinsics.throwUninitializedPropertyAccessException("loginButton");
            guiButton = null;
        }
        guiButton.field_146124_l = true;
        GuiButton guiButton2 = this$0.generateButton;
        if (guiButton2 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("generateButton");
            guiButton2 = null;
        }
        guiButton2.field_146124_l = true;
    }

    private static final Unit actionPerformed$lambda-1(GuiTheAltening this$0, Void $noName_0, Throwable err) {
        Intrinsics.checkNotNullParameter((Object)this$0, "this$0");
        this$0.status = "\u00a7cFailed to generate account.";
        ClientUtils.getLogger().error("Failed to generate account.", err);
        return Unit.INSTANCE;
    }

    private static final void actionPerformed$lambda-2(GuiTheAltening this$0, Unit $noName_0, Throwable $noName_1) {
        Intrinsics.checkNotNullParameter((Object)this$0, "this$0");
        GuiButton guiButton = this$0.loginButton;
        if (guiButton == null) {
            Intrinsics.throwUninitializedPropertyAccessException("loginButton");
            guiButton = null;
        }
        guiButton.field_146124_l = true;
        GuiButton guiButton2 = this$0.generateButton;
        if (guiButton2 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("generateButton");
            guiButton2 = null;
        }
        guiButton2.field_146124_l = true;
    }

    /*
     * WARNING - void declaration
     */
    private static final void actionPerformed$lambda-3(GuiTheAltening this$0) {
        GuiButton guiButton;
        Intrinsics.checkNotNullParameter((Object)this$0, "this$0");
        try {
            String string;
            GuiTheAltening guiTheAltening;
            this$0.status = "\u00a7cSwitching Alt Service...";
            GuiAltManager.altService.switchService(AltService.EnumAltService.THEALTENING);
            this$0.status = "\u00a7cLogging in...";
            YggdrasilUserAuthentication yggdrasilUserAuthentication = new YggdrasilUserAuthentication(new YggdrasilAuthenticationService(Proxy.NO_PROXY, ""), Agent.MINECRAFT);
            GuiTextField guiTextField = this$0.tokenField;
            if (guiTextField == null) {
                Intrinsics.throwUninitializedPropertyAccessException("tokenField");
                guiTextField = null;
            }
            yggdrasilUserAuthentication.setUsername(guiTextField.func_146179_b());
            yggdrasilUserAuthentication.setPassword("LiquidPlus");
            GuiTheAltening guiTheAltening2 = this$0;
            try {
                guiTheAltening = guiTheAltening2;
                yggdrasilUserAuthentication.logIn();
                this$0.field_146297_k.field_71449_j = new Session(yggdrasilUserAuthentication.getSelectedProfile().getName(), yggdrasilUserAuthentication.getSelectedProfile().getId().toString(), yggdrasilUserAuthentication.getAuthenticatedToken(), "mojang");
                Client.INSTANCE.getEventManager().callEvent(new SessionEvent());
                this$0.prevGui.status = "\u00a7aYour name is now \u00a7b\u00a7l" + yggdrasilUserAuthentication.getSelectedProfile().getName() + "\u00a7c.";
                this$0.field_146297_k.func_147108_a((GuiScreen)this$0.prevGui);
                string = "\u00a7aYour name is now \u00a7b\u00a7l" + yggdrasilUserAuthentication.getSelectedProfile().getName() + "\u00a7c.";
            }
            catch (AuthenticationException authenticationException) {
                void e;
                guiTheAltening = guiTheAltening2;
                GuiAltManager.altService.switchService(AltService.EnumAltService.MOJANG);
                ClientUtils.getLogger().error("Failed to login.", (Throwable)e);
                string = Intrinsics.stringPlus("\u00a7cFailed to login: ", e.getMessage());
            }
            guiTheAltening.status = string;
        }
        catch (Throwable throwable) {
            ClientUtils.getLogger().error("Failed to login.", throwable);
            this$0.status = "\u00a7cFailed to login. Unknown error.";
        }
        if ((guiButton = this$0.loginButton) == null) {
            Intrinsics.throwUninitializedPropertyAccessException("loginButton");
            guiButton = null;
        }
        guiButton.field_146124_l = true;
        GuiButton guiButton2 = this$0.generateButton;
        if (guiButton2 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("generateButton");
            guiButton2 = null;
        }
        guiButton2.field_146124_l = true;
    }

    @Metadata(mv={1, 6, 0}, k=1, xi=48, d1={"\u0000\u0014\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\u0005\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002R\u001a\u0010\u0003\u001a\u00020\u0004X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u0005\u0010\u0006\"\u0004\b\u0007\u0010\b\u00a8\u0006\t"}, d2={"Lnet/dev/important/gui/client/altmanager/sub/altgenerator/GuiTheAltening$Companion;", "", "()V", "apiKey", "", "getApiKey", "()Ljava/lang/String;", "setApiKey", "(Ljava/lang/String;)V", "LiquidBounce"})
    public static final class Companion {
        private Companion() {
        }

        @NotNull
        public final String getApiKey() {
            return apiKey;
        }

        public final void setApiKey(@NotNull String string) {
            Intrinsics.checkNotNullParameter(string, "<set-?>");
            apiKey = string;
        }

        public /* synthetic */ Companion(DefaultConstructorMarker $constructor_marker) {
            this();
        }
    }
}

