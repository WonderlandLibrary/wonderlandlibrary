/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.gui.FontRenderer
 *  net.minecraft.client.gui.Gui
 *  net.minecraft.client.gui.GuiButton
 *  net.minecraft.client.gui.GuiScreen
 *  net.minecraft.client.gui.GuiTextField
 *  net.minecraft.util.Session
 *  org.jetbrains.annotations.NotNull
 *  org.lwjgl.input.Keyboard
 */
package net.ccbluex.liquidbounce.ui.client.altmanager.sub;

import com.thealtening.AltService;
import kotlin.Metadata;
import kotlin.Unit;
import kotlin.concurrent.ThreadsKt;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.internal.Intrinsics;
import net.ccbluex.liquidbounce.ui.client.altmanager.GuiAltManager;
import net.ccbluex.liquidbounce.ui.client.altmanager.sub.GuiSessionLogin$WhenMappings;
import net.ccbluex.liquidbounce.ui.font.Fonts;
import net.ccbluex.liquidbounce.utils.ClientUtils;
import net.ccbluex.liquidbounce.utils.login.LoginUtils;
import net.mcleaks.MCLeaks;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.util.Session;
import org.jetbrains.annotations.NotNull;
import org.lwjgl.input.Keyboard;

@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, xi=2, d1={"\u0000D\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0003\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0010\u0007\n\u0002\b\u0003\n\u0002\u0010\f\n\u0002\b\u0006\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J\u0010\u0010\u000b\u001a\u00020\f2\u0006\u0010\r\u001a\u00020\u0006H\u0014J \u0010\u000e\u001a\u00020\f2\u0006\u0010\u000f\u001a\u00020\u00102\u0006\u0010\u0011\u001a\u00020\u00102\u0006\u0010\u0012\u001a\u00020\u0013H\u0016J\b\u0010\u0014\u001a\u00020\fH\u0016J\u0018\u0010\u0015\u001a\u00020\f2\u0006\u0010\u0016\u001a\u00020\u00172\u0006\u0010\u0018\u001a\u00020\u0010H\u0014J \u0010\u0019\u001a\u00020\f2\u0006\u0010\u000f\u001a\u00020\u00102\u0006\u0010\u0011\u001a\u00020\u00102\u0006\u0010\u001a\u001a\u00020\u0010H\u0014J\b\u0010\u001b\u001a\u00020\fH\u0016J\b\u0010\u001c\u001a\u00020\fH\u0016R\u000e\u0010\u0005\u001a\u00020\u0006X\u0082.\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\bX\u0082.\u00a2\u0006\u0002\n\u0000R\u000e\u0010\t\u001a\u00020\nX\u0082\u000e\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u001d"}, d2={"Lnet/ccbluex/liquidbounce/ui/client/altmanager/sub/GuiSessionLogin;", "Lnet/minecraft/client/gui/GuiScreen;", "prevGui", "Lnet/ccbluex/liquidbounce/ui/client/altmanager/GuiAltManager;", "(Lnet/ccbluex/liquidbounce/ui/client/altmanager/GuiAltManager;)V", "loginButton", "Lnet/minecraft/client/gui/GuiButton;", "sessionTokenField", "Lnet/minecraft/client/gui/GuiTextField;", "status", "", "actionPerformed", "", "button", "drawScreen", "mouseX", "", "mouseY", "partialTicks", "", "initGui", "keyTyped", "typedChar", "", "keyCode", "mouseClicked", "mouseButton", "onGuiClosed", "updateScreen", "KyinoClient"})
public final class GuiSessionLogin
extends GuiScreen {
    private GuiButton loginButton;
    private GuiTextField sessionTokenField;
    private String status;
    private final GuiAltManager prevGui;

    public void func_73866_w_() {
        Keyboard.enableRepeatEvents((boolean)true);
        GuiButton guiButton = this.loginButton = new GuiButton(1, this.field_146294_l / 2 - 100, this.field_146295_m / 4 + 96, "Login");
        if (guiButton == null) {
            Intrinsics.throwUninitializedPropertyAccessException("loginButton");
        }
        this.field_146292_n.add(guiButton);
        this.field_146292_n.add(new GuiButton(0, this.field_146294_l / 2 - 100, this.field_146295_m / 4 + 120, "Back"));
        GuiTextField guiTextField = this.sessionTokenField = new GuiTextField(666, (FontRenderer)Fonts.font40, this.field_146294_l / 2 - 100, 80, 200, 20);
        if (guiTextField == null) {
            Intrinsics.throwUninitializedPropertyAccessException("sessionTokenField");
        }
        guiTextField.func_146195_b(true);
        GuiTextField guiTextField2 = this.sessionTokenField;
        if (guiTextField2 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("sessionTokenField");
        }
        guiTextField2.func_146203_f(Integer.MAX_VALUE);
        if (this.sessionTokenField == null) {
            Intrinsics.throwUninitializedPropertyAccessException("sessionTokenField");
        }
        super.func_73866_w_();
    }

    public void func_73863_a(int mouseX, int mouseY, float partialTicks) {
        this.func_146278_c(0);
        Gui.func_73734_a((int)30, (int)30, (int)(this.field_146294_l - 30), (int)(this.field_146295_m - 30), (int)Integer.MIN_VALUE);
        this.func_73732_a(Fonts.font35, "Session Login", this.field_146294_l / 2, 36, 0xFFFFFF);
        this.func_73732_a(Fonts.font35, this.status, this.field_146294_l / 2, this.field_146295_m / 4 + 80, 0xFFFFFF);
        GuiTextField guiTextField = this.sessionTokenField;
        if (guiTextField == null) {
            Intrinsics.throwUninitializedPropertyAccessException("sessionTokenField");
        }
        guiTextField.func_146194_f();
        this.func_73732_a(Fonts.font40, "\u00a77Session Token:", this.field_146294_l / 2 - 65, 66, 0xFFFFFF);
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
                this.status = "\u00a7aLogging in...";
                ThreadsKt.thread$default(false, false, null, null, 0, new Function0<Unit>(this){
                    final /* synthetic */ GuiSessionLogin this$0;

                    public final void invoke() {
                        String string;
                        String string2 = GuiSessionLogin.access$getSessionTokenField$p(this.this$0).func_146179_b();
                        Intrinsics.checkExpressionValueIsNotNull(string2, "sessionTokenField.text");
                        LoginUtils.LoginResult loginResult = LoginUtils.loginSessionId(string2);
                        GuiSessionLogin guiSessionLogin = this.this$0;
                        switch (GuiSessionLogin$WhenMappings.$EnumSwitchMapping$0[loginResult.ordinal()]) {
                            case 1: {
                                AltService altService = GuiAltManager.altService;
                                Intrinsics.checkExpressionValueIsNotNull(altService, "GuiAltManager.altService");
                                if (altService.getCurrentService() != AltService.EnumAltService.MOJANG) {
                                    GuiSessionLogin guiSessionLogin2 = guiSessionLogin;
                                    try {
                                        guiSessionLogin = guiSessionLogin2;
                                        GuiAltManager.altService.switchService(AltService.EnumAltService.MOJANG);
                                    }
                                    catch (NoSuchFieldException e) {
                                        guiSessionLogin = guiSessionLogin2;
                                        ClientUtils.getLogger().error("Something went wrong while trying to switch alt service.", (Throwable)e);
                                    }
                                    catch (IllegalAccessException e) {
                                        guiSessionLogin = guiSessionLogin2;
                                        ClientUtils.getLogger().error("Something went wrong while trying to switch alt service.", (Throwable)e);
                                    }
                                }
                                MCLeaks.remove();
                                StringBuilder stringBuilder = new StringBuilder().append("\u00a7cYour name is now \u00a7f\u00a7l");
                                Session session = this.this$0.field_146297_k.field_71449_j;
                                Intrinsics.checkExpressionValueIsNotNull(session, "mc.session");
                                string = stringBuilder.append(session.func_111285_a()).append("\u00a7c").toString();
                                break;
                            }
                            case 2: {
                                string = "\u00a7cFailed to parse Session ID!";
                                break;
                            }
                            case 3: {
                                string = "\u00a7cInvalid Session ID!";
                                break;
                            }
                            default: {
                                string = "";
                            }
                        }
                        GuiSessionLogin.access$setStatus$p(guiSessionLogin, string);
                        GuiSessionLogin.access$getLoginButton$p((GuiSessionLogin)this.this$0).field_146124_l = true;
                    }
                    {
                        this.this$0 = guiSessionLogin;
                        super(0);
                    }
                }, 31, null);
            }
        }
        super.func_146284_a(button);
    }

    protected void func_73869_a(char typedChar, int keyCode) {
        if (1 == keyCode) {
            this.field_146297_k.func_147108_a((GuiScreen)this.prevGui);
            return;
        }
        GuiTextField guiTextField = this.sessionTokenField;
        if (guiTextField == null) {
            Intrinsics.throwUninitializedPropertyAccessException("sessionTokenField");
        }
        if (guiTextField.func_146206_l()) {
            GuiTextField guiTextField2 = this.sessionTokenField;
            if (guiTextField2 == null) {
                Intrinsics.throwUninitializedPropertyAccessException("sessionTokenField");
            }
            guiTextField2.func_146201_a(typedChar, keyCode);
        }
        super.func_73869_a(typedChar, keyCode);
    }

    protected void func_73864_a(int mouseX, int mouseY, int mouseButton) {
        GuiTextField guiTextField = this.sessionTokenField;
        if (guiTextField == null) {
            Intrinsics.throwUninitializedPropertyAccessException("sessionTokenField");
        }
        guiTextField.func_146192_a(mouseX, mouseY, mouseButton);
        super.func_73864_a(mouseX, mouseY, mouseButton);
    }

    public void func_73876_c() {
        GuiTextField guiTextField = this.sessionTokenField;
        if (guiTextField == null) {
            Intrinsics.throwUninitializedPropertyAccessException("sessionTokenField");
        }
        guiTextField.func_146178_a();
        super.func_73876_c();
    }

    public void func_146281_b() {
        Keyboard.enableRepeatEvents((boolean)false);
        super.func_146281_b();
    }

    public GuiSessionLogin(@NotNull GuiAltManager prevGui) {
        Intrinsics.checkParameterIsNotNull((Object)prevGui, "prevGui");
        this.prevGui = prevGui;
        this.status = "";
    }

    public static final /* synthetic */ GuiTextField access$getSessionTokenField$p(GuiSessionLogin $this) {
        GuiTextField guiTextField = $this.sessionTokenField;
        if (guiTextField == null) {
            Intrinsics.throwUninitializedPropertyAccessException("sessionTokenField");
        }
        return guiTextField;
    }

    public static final /* synthetic */ void access$setSessionTokenField$p(GuiSessionLogin $this, GuiTextField guiTextField) {
        $this.sessionTokenField = guiTextField;
    }

    public static final /* synthetic */ String access$getStatus$p(GuiSessionLogin $this) {
        return $this.status;
    }

    public static final /* synthetic */ void access$setStatus$p(GuiSessionLogin $this, String string) {
        $this.status = string;
    }

    public static final /* synthetic */ GuiButton access$getLoginButton$p(GuiSessionLogin $this) {
        GuiButton guiButton = $this.loginButton;
        if (guiButton == null) {
            Intrinsics.throwUninitializedPropertyAccessException("loginButton");
        }
        return guiButton;
    }

    public static final /* synthetic */ void access$setLoginButton$p(GuiSessionLogin $this, GuiButton guiButton) {
        $this.loginButton = guiButton;
    }
}

