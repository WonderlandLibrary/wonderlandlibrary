/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.authlib.Agent
 *  com.mojang.authlib.GameProfile
 *  com.mojang.authlib.exceptions.AuthenticationException
 *  com.mojang.authlib.yggdrasil.YggdrasilAuthenticationService
 *  com.mojang.authlib.yggdrasil.YggdrasilUserAuthentication
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.gui.FontRenderer
 *  net.minecraft.client.gui.Gui
 *  net.minecraft.client.gui.GuiButton
 *  net.minecraft.client.gui.GuiScreen
 *  net.minecraft.client.gui.GuiTextField
 *  net.minecraft.util.Session
 *  org.jetbrains.annotations.NotNull
 *  org.lwjgl.input.Keyboard
 */
package net.ccbluex.liquidbounce.ui.client.altmanager.sub.altgenerator;

import com.mojang.authlib.Agent;
import com.mojang.authlib.GameProfile;
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
import kotlin.Metadata;
import kotlin.Unit;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import net.ccbluex.liquidbounce.LiquidBounce;
import net.ccbluex.liquidbounce.event.SessionEvent;
import net.ccbluex.liquidbounce.ui.client.altmanager.GuiAltManager;
import net.ccbluex.liquidbounce.ui.elements.GuiPasswordField;
import net.ccbluex.liquidbounce.ui.font.Fonts;
import net.ccbluex.liquidbounce.ui.font.GameFontRenderer;
import net.ccbluex.liquidbounce.utils.ClientUtils;
import net.ccbluex.liquidbounce.utils.misc.MiscUtils;
import net.mcleaks.MCLeaks;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.util.Session;
import org.jetbrains.annotations.NotNull;
import org.lwjgl.input.Keyboard;

@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, xi=2, d1={"\u0000H\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0002\b\u0003\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0010\u0007\n\u0002\b\u0003\n\u0002\u0010\f\n\u0002\b\u0007\u0018\u0000 \u001f2\u00020\u0001:\u0001\u001fB\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J\u0010\u0010\r\u001a\u00020\u000e2\u0006\u0010\u000f\u001a\u00020\bH\u0014J \u0010\u0010\u001a\u00020\u000e2\u0006\u0010\u0011\u001a\u00020\u00122\u0006\u0010\u0013\u001a\u00020\u00122\u0006\u0010\u0014\u001a\u00020\u0015H\u0016J\b\u0010\u0016\u001a\u00020\u000eH\u0016J\u0018\u0010\u0017\u001a\u00020\u000e2\u0006\u0010\u0018\u001a\u00020\u00192\u0006\u0010\u001a\u001a\u00020\u0012H\u0014J \u0010\u001b\u001a\u00020\u000e2\u0006\u0010\u0011\u001a\u00020\u00122\u0006\u0010\u0013\u001a\u00020\u00122\u0006\u0010\u001c\u001a\u00020\u0012H\u0014J\b\u0010\u001d\u001a\u00020\u000eH\u0016J\b\u0010\u001e\u001a\u00020\u000eH\u0016R\u000e\u0010\u0005\u001a\u00020\u0006X\u0082.\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\bX\u0082.\u00a2\u0006\u0002\n\u0000R\u000e\u0010\t\u001a\u00020\bX\u0082.\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\n\u001a\u00020\u000bX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\f\u001a\u00020\u0006X\u0082.\u00a2\u0006\u0002\n\u0000\u00a8\u0006 "}, d2={"Lnet/ccbluex/liquidbounce/ui/client/altmanager/sub/altgenerator/GuiTheAltening;", "Lnet/minecraft/client/gui/GuiScreen;", "prevGui", "Lnet/ccbluex/liquidbounce/ui/client/altmanager/GuiAltManager;", "(Lnet/ccbluex/liquidbounce/ui/client/altmanager/GuiAltManager;)V", "apiKeyField", "Lnet/minecraft/client/gui/GuiTextField;", "generateButton", "Lnet/minecraft/client/gui/GuiButton;", "loginButton", "status", "", "tokenField", "actionPerformed", "", "button", "drawScreen", "mouseX", "", "mouseY", "partialTicks", "", "initGui", "keyTyped", "typedChar", "", "keyCode", "mouseClicked", "mouseButton", "onGuiClosed", "updateScreen", "Companion", "KyinoClient"})
public final class GuiTheAltening
extends GuiScreen {
    private GuiButton loginButton;
    private GuiButton generateButton;
    private GuiTextField apiKeyField;
    private GuiTextField tokenField;
    private String status;
    private final GuiAltManager prevGui;
    @NotNull
    private static String apiKey;
    public static final Companion Companion;

    public void func_73866_w_() {
        Keyboard.enableRepeatEvents((boolean)true);
        GuiButton guiButton = this.loginButton = new GuiButton(2, this.field_146294_l / 2 - 100, 75, "Login");
        if (guiButton == null) {
            Intrinsics.throwUninitializedPropertyAccessException("loginButton");
        }
        this.field_146292_n.add(guiButton);
        GuiButton guiButton2 = this.generateButton = new GuiButton(1, this.field_146294_l / 2 - 100, 140, "Generate");
        if (guiButton2 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("generateButton");
        }
        this.field_146292_n.add(guiButton2);
        this.field_146292_n.add(new GuiButton(3, this.field_146294_l / 2 - 100, this.field_146295_m - 54, 98, 20, "Buy"));
        this.field_146292_n.add(new GuiButton(0, this.field_146294_l / 2 + 2, this.field_146295_m - 54, 98, 20, "Back"));
        GuiTextField guiTextField = this.tokenField = new GuiTextField(666, (FontRenderer)Fonts.font40, this.field_146294_l / 2 - 100, 50, 200, 20);
        if (guiTextField == null) {
            Intrinsics.throwUninitializedPropertyAccessException("tokenField");
        }
        guiTextField.func_146195_b(true);
        GuiTextField guiTextField2 = this.tokenField;
        if (guiTextField2 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("tokenField");
        }
        guiTextField2.func_146203_f(Integer.MAX_VALUE);
        GameFontRenderer gameFontRenderer = Fonts.font40;
        Intrinsics.checkExpressionValueIsNotNull((Object)gameFontRenderer, "Fonts.font40");
        GuiTextField guiTextField3 = this.apiKeyField = (GuiTextField)new GuiPasswordField(1337, gameFontRenderer, this.field_146294_l / 2 - 100, 115, 200, 20);
        if (guiTextField3 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("apiKeyField");
        }
        guiTextField3.func_146203_f(18);
        GuiTextField guiTextField4 = this.apiKeyField;
        if (guiTextField4 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("apiKeyField");
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
        }
        guiTextField.func_146194_f();
        GuiTextField guiTextField2 = this.tokenField;
        if (guiTextField2 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("tokenField");
        }
        guiTextField2.func_146194_f();
        this.func_73732_a(Fonts.font40, "\u00a77Token:", this.field_146294_l / 2 - 84, 40, 0xFFFFFF);
        this.func_73732_a(Fonts.font40, "\u00a77API-Key:", this.field_146294_l / 2 - 78, 105, 0xFFFFFF);
        this.func_73732_a(Fonts.font40, "\u00a77Use coupon code 'liquidbounce' for 20% off!", this.field_146294_l / 2, this.field_146295_m - 65, 0xFFFFFF);
        super.func_73863_a(mouseX, mouseY, partialTicks);
    }

    protected void func_146284_a(@NotNull GuiButton button) {
        Intrinsics.checkParameterIsNotNull(button, "button");
        if (!button.field_146124_l) {
            return;
        }
        switch (button.field_146127_k) {
            case 0: {
                this.field_146297_k.func_147108_a((GuiScreen)this.prevGui);
                break;
            }
            case 1: {
                if (this.loginButton == null) {
                    Intrinsics.throwUninitializedPropertyAccessException("loginButton");
                }
                this.loginButton.field_146124_l = false;
                if (this.generateButton == null) {
                    Intrinsics.throwUninitializedPropertyAccessException("generateButton");
                }
                this.generateButton.field_146124_l = false;
                GuiTextField guiTextField = this.apiKeyField;
                if (guiTextField == null) {
                    Intrinsics.throwUninitializedPropertyAccessException("apiKeyField");
                }
                String string = guiTextField.func_146179_b();
                Intrinsics.checkExpressionValueIsNotNull(string, "apiKeyField.text");
                apiKey = string;
                TheAltening altening = new TheAltening(apiKey);
                TheAltening.Asynchronous asynchronous = new TheAltening.Asynchronous(altening);
                this.status = "\u00a7cGenerating account...";
                ((CompletableFuture)((CompletableFuture)asynchronous.getAccountData().thenAccept((Consumer)new Consumer<AccountData>(this){
                    final /* synthetic */ GuiTheAltening this$0;

                    /*
                     * WARNING - void declaration
                     */
                    public final void accept(AccountData account) {
                        StringBuilder stringBuilder = new StringBuilder().append("\u00a7aGenerated account: \u00a7b\u00a7l");
                        AccountData accountData = account;
                        Intrinsics.checkExpressionValueIsNotNull(accountData, "account");
                        GuiTheAltening.access$setStatus$p(this.this$0, stringBuilder.append(accountData.getUsername()).toString());
                        try {
                            String string;
                            GuiTheAltening guiTheAltening;
                            GuiTheAltening.access$setStatus$p(this.this$0, "\u00a7cSwitching Alt Service...");
                            GuiAltManager.altService.switchService(AltService.EnumAltService.THEALTENING);
                            GuiTheAltening.access$setStatus$p(this.this$0, "\u00a7cLogging in...");
                            YggdrasilUserAuthentication yggdrasilUserAuthentication = new YggdrasilUserAuthentication(new YggdrasilAuthenticationService(Proxy.NO_PROXY, ""), Agent.MINECRAFT);
                            yggdrasilUserAuthentication.setUsername(account.getToken());
                            yggdrasilUserAuthentication.setPassword("KyinoClient");
                            GuiTheAltening guiTheAltening2 = this.this$0;
                            try {
                                guiTheAltening = guiTheAltening2;
                                yggdrasilUserAuthentication.logIn();
                                Minecraft minecraft = this.this$0.field_146297_k;
                                GameProfile gameProfile = yggdrasilUserAuthentication.getSelectedProfile();
                                Intrinsics.checkExpressionValueIsNotNull(gameProfile, "yggdrasilUserAuthentication.selectedProfile");
                                String string2 = gameProfile.getName();
                                GameProfile gameProfile2 = yggdrasilUserAuthentication.getSelectedProfile();
                                Intrinsics.checkExpressionValueIsNotNull(gameProfile2, "yggdrasilUserAuthenticat\u2026         .selectedProfile");
                                minecraft.field_71449_j = new Session(string2, gameProfile2.getId().toString(), yggdrasilUserAuthentication.getAuthenticatedToken(), "mojang");
                                LiquidBounce.INSTANCE.getEventManager().callEvent(new SessionEvent());
                                MCLeaks.remove();
                                GuiAltManager guiAltManager = GuiTheAltening.access$getPrevGui$p(this.this$0);
                                StringBuilder stringBuilder2 = new StringBuilder().append("\u00a7aYour name is now \u00a7b\u00a7l");
                                GameProfile gameProfile3 = yggdrasilUserAuthentication.getSelectedProfile();
                                Intrinsics.checkExpressionValueIsNotNull(gameProfile3, "yggdrasilUserAuthentication.selectedProfile");
                                guiAltManager.status = stringBuilder2.append(gameProfile3.getName()).append("\u00a7c.").toString();
                                this.this$0.field_146297_k.func_147108_a((GuiScreen)GuiTheAltening.access$getPrevGui$p(this.this$0));
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
                        GuiTheAltening.access$getLoginButton$p((GuiTheAltening)this.this$0).field_146124_l = true;
                        GuiTheAltening.access$getGenerateButton$p((GuiTheAltening)this.this$0).field_146124_l = true;
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
                        GuiTheAltening.access$getLoginButton$p((GuiTheAltening)this.this$0).field_146124_l = true;
                        GuiTheAltening.access$getGenerateButton$p((GuiTheAltening)this.this$0).field_146124_l = true;
                    }
                    {
                        this.this$0 = guiTheAltening;
                    }
                });
                break;
            }
            case 2: {
                if (this.loginButton == null) {
                    Intrinsics.throwUninitializedPropertyAccessException("loginButton");
                }
                this.loginButton.field_146124_l = false;
                if (this.generateButton == null) {
                    Intrinsics.throwUninitializedPropertyAccessException("generateButton");
                }
                this.generateButton.field_146124_l = false;
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
                            yggdrasilUserAuthentication.setUsername(GuiTheAltening.access$getTokenField$p(this.this$0).func_146179_b());
                            yggdrasilUserAuthentication.setPassword("KyinoClient");
                            GuiTheAltening guiTheAltening2 = this.this$0;
                            try {
                                guiTheAltening = guiTheAltening2;
                                yggdrasilUserAuthentication.logIn();
                                Minecraft minecraft = this.this$0.field_146297_k;
                                GameProfile gameProfile = yggdrasilUserAuthentication.getSelectedProfile();
                                Intrinsics.checkExpressionValueIsNotNull(gameProfile, "yggdrasilUserAuthentication.selectedProfile");
                                String string2 = gameProfile.getName();
                                GameProfile gameProfile2 = yggdrasilUserAuthentication.getSelectedProfile();
                                Intrinsics.checkExpressionValueIsNotNull(gameProfile2, "yggdrasilUserAuthenticat\u2026         .selectedProfile");
                                minecraft.field_71449_j = new Session(string2, gameProfile2.getId().toString(), yggdrasilUserAuthentication.getAuthenticatedToken(), "mojang");
                                LiquidBounce.INSTANCE.getEventManager().callEvent(new SessionEvent());
                                MCLeaks.remove();
                                GuiAltManager guiAltManager = GuiTheAltening.access$getPrevGui$p(this.this$0);
                                StringBuilder stringBuilder = new StringBuilder().append("\u00a7aYour name is now \u00a7b\u00a7l");
                                GameProfile gameProfile3 = yggdrasilUserAuthentication.getSelectedProfile();
                                Intrinsics.checkExpressionValueIsNotNull(gameProfile3, "yggdrasilUserAuthentication.selectedProfile");
                                guiAltManager.status = stringBuilder.append(gameProfile3.getName()).append("\u00a7c.").toString();
                                this.this$0.field_146297_k.func_147108_a((GuiScreen)GuiTheAltening.access$getPrevGui$p(this.this$0));
                                StringBuilder stringBuilder2 = new StringBuilder().append("\u00a7aYour name is now \u00a7b\u00a7l");
                                GameProfile gameProfile4 = yggdrasilUserAuthentication.getSelectedProfile();
                                Intrinsics.checkExpressionValueIsNotNull(gameProfile4, "yggdrasilUserAuthentication.selectedProfile");
                                string = stringBuilder2.append(gameProfile4.getName()).append("\u00a7c.").toString();
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
                        GuiTheAltening.access$getLoginButton$p((GuiTheAltening)this.this$0).field_146124_l = true;
                        GuiTheAltening.access$getGenerateButton$p((GuiTheAltening)this.this$0).field_146124_l = true;
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
        super.func_146284_a(button);
    }

    protected void func_73869_a(char typedChar, int keyCode) {
        if (1 == keyCode) {
            this.field_146297_k.func_147108_a((GuiScreen)this.prevGui);
            return;
        }
        GuiTextField guiTextField = this.apiKeyField;
        if (guiTextField == null) {
            Intrinsics.throwUninitializedPropertyAccessException("apiKeyField");
        }
        if (guiTextField.func_146206_l()) {
            GuiTextField guiTextField2 = this.apiKeyField;
            if (guiTextField2 == null) {
                Intrinsics.throwUninitializedPropertyAccessException("apiKeyField");
            }
            guiTextField2.func_146201_a(typedChar, keyCode);
        }
        GuiTextField guiTextField3 = this.tokenField;
        if (guiTextField3 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("tokenField");
        }
        if (guiTextField3.func_146206_l()) {
            GuiTextField guiTextField4 = this.tokenField;
            if (guiTextField4 == null) {
                Intrinsics.throwUninitializedPropertyAccessException("tokenField");
            }
            guiTextField4.func_146201_a(typedChar, keyCode);
        }
        super.func_73869_a(typedChar, keyCode);
    }

    protected void func_73864_a(int mouseX, int mouseY, int mouseButton) {
        GuiTextField guiTextField = this.apiKeyField;
        if (guiTextField == null) {
            Intrinsics.throwUninitializedPropertyAccessException("apiKeyField");
        }
        guiTextField.func_146192_a(mouseX, mouseY, mouseButton);
        GuiTextField guiTextField2 = this.tokenField;
        if (guiTextField2 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("tokenField");
        }
        guiTextField2.func_146192_a(mouseX, mouseY, mouseButton);
        super.func_73864_a(mouseX, mouseY, mouseButton);
    }

    public void func_73876_c() {
        GuiTextField guiTextField = this.apiKeyField;
        if (guiTextField == null) {
            Intrinsics.throwUninitializedPropertyAccessException("apiKeyField");
        }
        guiTextField.func_146178_a();
        GuiTextField guiTextField2 = this.tokenField;
        if (guiTextField2 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("tokenField");
        }
        guiTextField2.func_146178_a();
        super.func_73876_c();
    }

    public void func_146281_b() {
        Keyboard.enableRepeatEvents((boolean)false);
        GuiTextField guiTextField = this.apiKeyField;
        if (guiTextField == null) {
            Intrinsics.throwUninitializedPropertyAccessException("apiKeyField");
        }
        String string = guiTextField.func_146179_b();
        Intrinsics.checkExpressionValueIsNotNull(string, "apiKeyField.text");
        apiKey = string;
        super.func_146281_b();
    }

    public GuiTheAltening(@NotNull GuiAltManager prevGui) {
        Intrinsics.checkParameterIsNotNull((Object)prevGui, "prevGui");
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

    public static final /* synthetic */ GuiButton access$getLoginButton$p(GuiTheAltening $this) {
        GuiButton guiButton = $this.loginButton;
        if (guiButton == null) {
            Intrinsics.throwUninitializedPropertyAccessException("loginButton");
        }
        return guiButton;
    }

    public static final /* synthetic */ void access$setLoginButton$p(GuiTheAltening $this, GuiButton guiButton) {
        $this.loginButton = guiButton;
    }

    public static final /* synthetic */ GuiButton access$getGenerateButton$p(GuiTheAltening $this) {
        GuiButton guiButton = $this.generateButton;
        if (guiButton == null) {
            Intrinsics.throwUninitializedPropertyAccessException("generateButton");
        }
        return guiButton;
    }

    public static final /* synthetic */ void access$setGenerateButton$p(GuiTheAltening $this, GuiButton guiButton) {
        $this.generateButton = guiButton;
    }

    public static final /* synthetic */ GuiTextField access$getTokenField$p(GuiTheAltening $this) {
        GuiTextField guiTextField = $this.tokenField;
        if (guiTextField == null) {
            Intrinsics.throwUninitializedPropertyAccessException("tokenField");
        }
        return guiTextField;
    }

    public static final /* synthetic */ void access$setTokenField$p(GuiTheAltening $this, GuiTextField guiTextField) {
        $this.tokenField = guiTextField;
    }

    @Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, xi=2, d1={"\u0000\u0014\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\u0005\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002R\u001a\u0010\u0003\u001a\u00020\u0004X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u0005\u0010\u0006\"\u0004\b\u0007\u0010\b\u00a8\u0006\t"}, d2={"Lnet/ccbluex/liquidbounce/ui/client/altmanager/sub/altgenerator/GuiTheAltening$Companion;", "", "()V", "apiKey", "", "getApiKey", "()Ljava/lang/String;", "setApiKey", "(Ljava/lang/String;)V", "KyinoClient"})
    public static final class Companion {
        @NotNull
        public final String getApiKey() {
            return apiKey;
        }

        public final void setApiKey(@NotNull String string) {
            Intrinsics.checkParameterIsNotNull(string, "<set-?>");
            apiKey = string;
        }

        private Companion() {
        }

        public /* synthetic */ Companion(DefaultConstructorMarker $constructor_marker) {
            this();
        }
    }
}

