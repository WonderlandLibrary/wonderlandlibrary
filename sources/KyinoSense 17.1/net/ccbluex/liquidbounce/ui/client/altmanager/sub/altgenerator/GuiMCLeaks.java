/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.gui.FontRenderer
 *  net.minecraft.client.gui.Gui
 *  net.minecraft.client.gui.GuiButton
 *  net.minecraft.client.gui.GuiScreen
 *  net.minecraft.client.gui.GuiTextField
 *  org.jetbrains.annotations.NotNull
 *  org.lwjgl.input.Keyboard
 */
package net.ccbluex.liquidbounce.ui.client.altmanager.sub.altgenerator;

import com.thealtening.AltService;
import java.io.IOException;
import kotlin.Metadata;
import kotlin.TypeCastException;
import kotlin.jvm.internal.Intrinsics;
import net.ccbluex.liquidbounce.ui.client.altmanager.GuiAltManager;
import net.ccbluex.liquidbounce.ui.font.Fonts;
import net.ccbluex.liquidbounce.utils.ClientUtils;
import net.ccbluex.liquidbounce.utils.misc.MiscUtils;
import net.mcleaks.Callback;
import net.mcleaks.MCLeaks;
import net.mcleaks.RedeemResponse;
import net.mcleaks.Session;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import org.jetbrains.annotations.NotNull;
import org.lwjgl.input.Keyboard;

@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, xi=2, d1={"\u0000D\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0010\u0007\n\u0002\b\u0003\n\u0002\u0010\f\n\u0002\b\u0006\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J\u0010\u0010\t\u001a\u00020\n2\u0006\u0010\u000b\u001a\u00020\fH\u0014J \u0010\r\u001a\u00020\n2\u0006\u0010\u000e\u001a\u00020\u000f2\u0006\u0010\u0010\u001a\u00020\u000f2\u0006\u0010\u0011\u001a\u00020\u0012H\u0016J\b\u0010\u0013\u001a\u00020\nH\u0016J\u0018\u0010\u0014\u001a\u00020\n2\u0006\u0010\u0015\u001a\u00020\u00162\u0006\u0010\u0017\u001a\u00020\u000fH\u0014J \u0010\u0018\u001a\u00020\n2\u0006\u0010\u000e\u001a\u00020\u000f2\u0006\u0010\u0010\u001a\u00020\u000f2\u0006\u0010\u0019\u001a\u00020\u000fH\u0014J\b\u0010\u001a\u001a\u00020\nH\u0016J\b\u0010\u001b\u001a\u00020\nH\u0016R\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0010\u0010\u0005\u001a\u0004\u0018\u00010\u0006X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\bX\u0082.\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u001c"}, d2={"Lnet/ccbluex/liquidbounce/ui/client/altmanager/sub/altgenerator/GuiMCLeaks;", "Lnet/minecraft/client/gui/GuiScreen;", "prevGui", "Lnet/ccbluex/liquidbounce/ui/client/altmanager/GuiAltManager;", "(Lnet/ccbluex/liquidbounce/ui/client/altmanager/GuiAltManager;)V", "status", "", "tokenField", "Lnet/minecraft/client/gui/GuiTextField;", "actionPerformed", "", "button", "Lnet/minecraft/client/gui/GuiButton;", "drawScreen", "mouseX", "", "mouseY", "partialTicks", "", "initGui", "keyTyped", "typedChar", "", "keyCode", "mouseClicked", "mouseButton", "onGuiClosed", "updateScreen", "KyinoClient"})
public final class GuiMCLeaks
extends GuiScreen {
    private GuiTextField tokenField;
    private String status;
    private final GuiAltManager prevGui;

    public void func_73876_c() {
        GuiTextField guiTextField = this.tokenField;
        if (guiTextField == null) {
            Intrinsics.throwUninitializedPropertyAccessException("tokenField");
        }
        guiTextField.func_146178_a();
    }

    public void func_73866_w_() {
        Keyboard.enableRepeatEvents((boolean)true);
        if (MCLeaks.isAltActive()) {
            StringBuilder stringBuilder = new StringBuilder().append("\u00a7aToken active. Using \u00a79");
            Session session = MCLeaks.getSession();
            Intrinsics.checkExpressionValueIsNotNull(session, "MCLeaks.getSession()");
            this.status = stringBuilder.append(session.getUsername()).append("\u00a7a to login!").toString();
        }
        this.field_146292_n.add(new GuiButton(1, this.field_146294_l / 2 - 100, this.field_146295_m / 4 + 65, 200, 20, "Login"));
        this.field_146292_n.add(new GuiButton(2, this.field_146294_l / 2 - 100, this.field_146295_m - 54, 98, 20, "Get Token"));
        this.field_146292_n.add(new GuiButton(3, this.field_146294_l / 2 + 2, this.field_146295_m - 54, 98, 20, "Back"));
        GuiTextField guiTextField = this.tokenField = new GuiTextField(0, (FontRenderer)Fonts.font40, this.field_146294_l / 2 - 100, this.field_146295_m / 4 + 40, 200, 20);
        if (guiTextField == null) {
            Intrinsics.throwUninitializedPropertyAccessException("tokenField");
        }
        guiTextField.func_146195_b(true);
        GuiTextField guiTextField2 = this.tokenField;
        if (guiTextField2 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("tokenField");
        }
        guiTextField2.func_146203_f(16);
    }

    public void func_146281_b() {
        Keyboard.enableRepeatEvents((boolean)false);
    }

    protected void func_146284_a(@NotNull GuiButton button) {
        Intrinsics.checkParameterIsNotNull(button, "button");
        if (!button.field_146124_l) {
            return;
        }
        switch (button.field_146127_k) {
            case 1: {
                GuiTextField guiTextField = this.tokenField;
                if (guiTextField == null) {
                    Intrinsics.throwUninitializedPropertyAccessException("tokenField");
                }
                if (guiTextField.func_146179_b().length() != 16) {
                    this.status = "\u00a7cThe token has to be 16 characters long!";
                    return;
                }
                button.field_146124_l = false;
                button.field_146126_j = "Please wait ...";
                GuiTextField guiTextField2 = this.tokenField;
                if (guiTextField2 == null) {
                    Intrinsics.throwUninitializedPropertyAccessException("tokenField");
                }
                MCLeaks.redeem(guiTextField2.func_146179_b(), new Callback<Object>(this, button){
                    final /* synthetic */ GuiMCLeaks this$0;
                    final /* synthetic */ GuiButton $button;

                    public final void done(Object it) {
                        if (it instanceof String) {
                            GuiMCLeaks.access$setStatus$p(this.this$0, "\u00a7c" + it);
                            this.$button.field_146124_l = true;
                            this.$button.field_146126_j = "Login";
                            return;
                        }
                        Object object = it;
                        if (object == null) {
                            throw new TypeCastException("null cannot be cast to non-null type net.mcleaks.RedeemResponse");
                        }
                        RedeemResponse redeemResponse = (RedeemResponse)object;
                        MCLeaks.refresh(new Session(redeemResponse.getUsername(), redeemResponse.getToken()));
                        try {
                            GuiAltManager.altService.switchService(AltService.EnumAltService.MOJANG);
                        }
                        catch (Exception e) {
                            ClientUtils.getLogger().error("Failed to change alt service to Mojang.", (Throwable)e);
                        }
                        GuiMCLeaks.access$setStatus$p(this.this$0, "\u00a7aYour token was redeemed successfully!");
                        this.$button.field_146124_l = true;
                        this.$button.field_146126_j = "Login";
                        GuiMCLeaks.access$getPrevGui$p((GuiMCLeaks)this.this$0).status = GuiMCLeaks.access$getStatus$p(this.this$0);
                        this.this$0.field_146297_k.func_147108_a((GuiScreen)GuiMCLeaks.access$getPrevGui$p(this.this$0));
                    }
                    {
                        this.this$0 = guiMCLeaks;
                        this.$button = guiButton;
                    }
                });
                break;
            }
            case 2: {
                MiscUtils.showURL("https://mcleaks.net/");
                break;
            }
            case 3: {
                this.field_146297_k.func_147108_a((GuiScreen)this.prevGui);
            }
        }
    }

    protected void func_73869_a(char typedChar, int keyCode) {
        switch (keyCode) {
            case 1: {
                this.field_146297_k.func_147108_a((GuiScreen)this.prevGui);
                break;
            }
            case 15: {
                GuiTextField guiTextField = this.tokenField;
                if (guiTextField == null) {
                    Intrinsics.throwUninitializedPropertyAccessException("tokenField");
                }
                GuiTextField guiTextField2 = this.tokenField;
                if (guiTextField2 == null) {
                    Intrinsics.throwUninitializedPropertyAccessException("tokenField");
                }
                guiTextField.func_146195_b(!guiTextField2.func_146206_l());
                break;
            }
            case 28: 
            case 156: {
                Object e = this.field_146292_n.get(1);
                Intrinsics.checkExpressionValueIsNotNull(e, "buttonList[1]");
                this.func_146284_a((GuiButton)e);
                break;
            }
            default: {
                GuiTextField guiTextField = this.tokenField;
                if (guiTextField == null) {
                    Intrinsics.throwUninitializedPropertyAccessException("tokenField");
                }
                guiTextField.func_146201_a(typedChar, keyCode);
            }
        }
    }

    protected void func_73864_a(int mouseX, int mouseY, int mouseButton) throws IOException {
        super.func_73864_a(mouseX, mouseY, mouseButton);
        GuiTextField guiTextField = this.tokenField;
        if (guiTextField == null) {
            Intrinsics.throwUninitializedPropertyAccessException("tokenField");
        }
        guiTextField.func_146192_a(mouseX, mouseY, mouseButton);
    }

    public void func_73863_a(int mouseX, int mouseY, float partialTicks) {
        this.func_146278_c(0);
        Gui.func_73734_a((int)30, (int)30, (int)(this.field_146294_l - 30), (int)(this.field_146295_m - 30), (int)Integer.MIN_VALUE);
        this.func_73732_a(Fonts.font40, "MCLeaks", this.field_146294_l / 2, 6, 0xFFFFFF);
        this.func_73731_b(Fonts.font40, "Token:", this.field_146294_l / 2 - 100, this.field_146295_m / 4 + 30, 0xA0A0A0);
        if (this.status != null) {
            this.func_73732_a(Fonts.font40, this.status, this.field_146294_l / 2, 18, 0xFFFFFF);
        }
        GuiTextField guiTextField = this.tokenField;
        if (guiTextField == null) {
            Intrinsics.throwUninitializedPropertyAccessException("tokenField");
        }
        guiTextField.func_146194_f();
        super.func_73863_a(mouseX, mouseY, partialTicks);
    }

    public GuiMCLeaks(@NotNull GuiAltManager prevGui) {
        Intrinsics.checkParameterIsNotNull((Object)prevGui, "prevGui");
        this.prevGui = prevGui;
    }

    public static final /* synthetic */ String access$getStatus$p(GuiMCLeaks $this) {
        return $this.status;
    }

    public static final /* synthetic */ void access$setStatus$p(GuiMCLeaks $this, String string) {
        $this.status = string;
    }

    public static final /* synthetic */ GuiAltManager access$getPrevGui$p(GuiMCLeaks $this) {
        return $this.prevGui;
    }
}

