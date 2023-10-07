/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.gui.Gui
 *  net.minecraft.client.gui.GuiButton
 *  net.minecraft.client.gui.GuiScreen
 *  net.minecraft.client.gui.GuiTextField
 *  org.apache.http.Header
 *  org.apache.http.StatusLine
 *  org.apache.http.client.methods.CloseableHttpResponse
 *  org.apache.http.client.methods.HttpDelete
 *  org.apache.http.client.methods.HttpPut
 *  org.apache.http.client.methods.HttpRequestBase
 *  org.apache.http.client.methods.HttpUriRequest
 *  org.apache.http.impl.client.CloseableHttpClient
 *  org.apache.http.impl.client.HttpClients
 *  org.apache.http.message.BasicHeader
 *  org.jetbrains.annotations.NotNull
 *  org.lwjgl.input.Keyboard
 */
package net.ccbluex.liquidbounce.ui.client.altmanager.sub;

import kotlin.Metadata;
import kotlin.Unit;
import kotlin.concurrent.ThreadsKt;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import net.ccbluex.liquidbounce.ui.client.altmanager.GuiAltManager;
import net.ccbluex.liquidbounce.ui.elements.GuiPasswordField;
import net.ccbluex.liquidbounce.ui.font.Fonts;
import net.ccbluex.liquidbounce.ui.font.GameFontRenderer;
import net.ccbluex.liquidbounce.utils.misc.MiscUtils;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import org.apache.http.Header;
import org.apache.http.StatusLine;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicHeader;
import org.jetbrains.annotations.NotNull;
import org.lwjgl.input.Keyboard;

@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, xi=2, d1={"\u0000D\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0003\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0010\u0007\n\u0002\b\u0003\n\u0002\u0010\f\n\u0002\b\u0007\u0018\u0000 \u001d2\u00020\u0001:\u0001\u001dB\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J\u0010\u0010\u000b\u001a\u00020\f2\u0006\u0010\r\u001a\u00020\u0006H\u0014J \u0010\u000e\u001a\u00020\f2\u0006\u0010\u000f\u001a\u00020\u00102\u0006\u0010\u0011\u001a\u00020\u00102\u0006\u0010\u0012\u001a\u00020\u0013H\u0016J\b\u0010\u0014\u001a\u00020\fH\u0016J\u0018\u0010\u0015\u001a\u00020\f2\u0006\u0010\u0016\u001a\u00020\u00172\u0006\u0010\u0018\u001a\u00020\u0010H\u0014J \u0010\u0019\u001a\u00020\f2\u0006\u0010\u000f\u001a\u00020\u00102\u0006\u0010\u0011\u001a\u00020\u00102\u0006\u0010\u001a\u001a\u00020\u0010H\u0014J\b\u0010\u001b\u001a\u00020\fH\u0016J\b\u0010\u001c\u001a\u00020\fH\u0016R\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0006X\u0082.\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\bX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\t\u001a\u00020\nX\u0082.\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u001e"}, d2={"Lnet/ccbluex/liquidbounce/ui/client/altmanager/sub/GuiDonatorCape;", "Lnet/minecraft/client/gui/GuiScreen;", "prevGui", "Lnet/ccbluex/liquidbounce/ui/client/altmanager/GuiAltManager;", "(Lnet/ccbluex/liquidbounce/ui/client/altmanager/GuiAltManager;)V", "stateButton", "Lnet/minecraft/client/gui/GuiButton;", "status", "", "transferCodeField", "Lnet/minecraft/client/gui/GuiTextField;", "actionPerformed", "", "button", "drawScreen", "mouseX", "", "mouseY", "partialTicks", "", "initGui", "keyTyped", "typedChar", "", "keyCode", "mouseClicked", "mouseButton", "onGuiClosed", "updateScreen", "Companion", "KyinoClient"})
public final class GuiDonatorCape
extends GuiScreen {
    private GuiButton stateButton;
    private GuiTextField transferCodeField;
    private String status;
    private final GuiAltManager prevGui;
    @NotNull
    private static String transferCode;
    private static boolean capeEnabled;
    public static final Companion Companion;

    public void func_73866_w_() {
        Keyboard.enableRepeatEvents((boolean)true);
        GuiButton guiButton = this.stateButton = new GuiButton(1, this.field_146294_l / 2 - 100, 105, "Disable Cape");
        if (guiButton == null) {
            Intrinsics.throwUninitializedPropertyAccessException("stateButton");
        }
        this.field_146292_n.add(guiButton);
        this.field_146292_n.add(new GuiButton(2, this.field_146294_l / 2 - 100, this.field_146295_m / 4 + 96, "Donate to get Cape"));
        this.field_146292_n.add(new GuiButton(0, this.field_146294_l / 2 - 100, this.field_146295_m / 4 + 120, "Back"));
        GameFontRenderer gameFontRenderer = Fonts.font40;
        Intrinsics.checkExpressionValueIsNotNull((Object)gameFontRenderer, "Fonts.font40");
        GuiTextField guiTextField = this.transferCodeField = (GuiTextField)new GuiPasswordField(666, gameFontRenderer, this.field_146294_l / 2 - 100, 80, 200, 20);
        if (guiTextField == null) {
            Intrinsics.throwUninitializedPropertyAccessException("transferCodeField");
        }
        guiTextField.func_146195_b(true);
        GuiTextField guiTextField2 = this.transferCodeField;
        if (guiTextField2 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("transferCodeField");
        }
        guiTextField2.func_146203_f(Integer.MAX_VALUE);
        GuiTextField guiTextField3 = this.transferCodeField;
        if (guiTextField3 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("transferCodeField");
        }
        guiTextField3.func_146180_a(transferCode);
        super.func_73866_w_();
    }

    public void func_73863_a(int mouseX, int mouseY, float partialTicks) {
        this.func_146278_c(0);
        Gui.func_73734_a((int)30, (int)30, (int)(this.field_146294_l - 30), (int)(this.field_146295_m - 30), (int)Integer.MIN_VALUE);
        this.func_73732_a(Fonts.font35, "Donator Cape", this.field_146294_l / 2, 36, 0xFFFFFF);
        this.func_73732_a(Fonts.font35, this.status, this.field_146294_l / 2, this.field_146295_m / 4 + 80, 0xFFFFFF);
        GuiTextField guiTextField = this.transferCodeField;
        if (guiTextField == null) {
            Intrinsics.throwUninitializedPropertyAccessException("transferCodeField");
        }
        guiTextField.func_146194_f();
        this.func_73732_a(Fonts.font40, "\u00a77Transfer Code:", this.field_146294_l / 2 - 65, 66, 0xFFFFFF);
        if (this.stateButton == null) {
            Intrinsics.throwUninitializedPropertyAccessException("stateButton");
        }
        this.stateButton.field_146126_j = capeEnabled ? "Disable Cape" : "Enable Cape";
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
                if (this.stateButton == null) {
                    Intrinsics.throwUninitializedPropertyAccessException("stateButton");
                }
                this.stateButton.field_146124_l = false;
                ThreadsKt.thread$default(false, false, null, null, 0, new Function0<Unit>(this){
                    final /* synthetic */ GuiDonatorCape this$0;

                    public final void invoke() {
                        String string;
                        CloseableHttpResponse response;
                        CloseableHttpClient httpClient = HttpClients.createDefault();
                        BasicHeader[] headers = new BasicHeader[]{new BasicHeader("Content-Type", "application/json"), new BasicHeader("Authorization", GuiDonatorCape.access$getTransferCodeField$p(this.this$0).func_146179_b())};
                        HttpRequestBase request = GuiDonatorCape.Companion.getCapeEnabled() ? (HttpRequestBase)new HttpDelete("http://capes.liquidbounce.net/api/v1/cape/self") : (HttpRequestBase)new HttpPut("http://capes.liquidbounce.net/api/v1/cape/self");
                        request.setHeaders((Header[])headers);
                        CloseableHttpResponse closeableHttpResponse = response = httpClient.execute((HttpUriRequest)request);
                        Intrinsics.checkExpressionValueIsNotNull(closeableHttpResponse, "response");
                        StatusLine statusLine = closeableHttpResponse.getStatusLine();
                        Intrinsics.checkExpressionValueIsNotNull(statusLine, "response.statusLine");
                        int statusCode = statusLine.getStatusCode();
                        if (statusCode == 204) {
                            GuiDonatorCape.Companion.setCapeEnabled(!GuiDonatorCape.Companion.getCapeEnabled());
                            string = GuiDonatorCape.Companion.getCapeEnabled() ? "\u00a7aSuccessfully enabled cape" : "\u00a7aSuccessfully disabled cape";
                        } else {
                            string = "\u00a7cFailed to toggle cape (" + statusCode + ')';
                        }
                        GuiDonatorCape.access$setStatus$p(this.this$0, string);
                        GuiDonatorCape.access$getStateButton$p((GuiDonatorCape)this.this$0).field_146124_l = true;
                    }
                    {
                        this.this$0 = guiDonatorCape;
                        super(0);
                    }
                }, 31, null);
                break;
            }
            case 2: {
                MiscUtils.showURL("https://donate.liquidbounce.net");
            }
        }
        super.func_146284_a(button);
    }

    protected void func_73869_a(char typedChar, int keyCode) {
        if (1 == keyCode) {
            this.field_146297_k.func_147108_a((GuiScreen)this.prevGui);
            return;
        }
        GuiTextField guiTextField = this.transferCodeField;
        if (guiTextField == null) {
            Intrinsics.throwUninitializedPropertyAccessException("transferCodeField");
        }
        if (guiTextField.func_146206_l()) {
            GuiTextField guiTextField2 = this.transferCodeField;
            if (guiTextField2 == null) {
                Intrinsics.throwUninitializedPropertyAccessException("transferCodeField");
            }
            guiTextField2.func_146201_a(typedChar, keyCode);
        }
        super.func_73869_a(typedChar, keyCode);
    }

    protected void func_73864_a(int mouseX, int mouseY, int mouseButton) {
        GuiTextField guiTextField = this.transferCodeField;
        if (guiTextField == null) {
            Intrinsics.throwUninitializedPropertyAccessException("transferCodeField");
        }
        guiTextField.func_146192_a(mouseX, mouseY, mouseButton);
        super.func_73864_a(mouseX, mouseY, mouseButton);
    }

    public void func_73876_c() {
        GuiTextField guiTextField = this.transferCodeField;
        if (guiTextField == null) {
            Intrinsics.throwUninitializedPropertyAccessException("transferCodeField");
        }
        guiTextField.func_146178_a();
        super.func_73876_c();
    }

    public void func_146281_b() {
        Keyboard.enableRepeatEvents((boolean)false);
        GuiTextField guiTextField = this.transferCodeField;
        if (guiTextField == null) {
            Intrinsics.throwUninitializedPropertyAccessException("transferCodeField");
        }
        String string = guiTextField.func_146179_b();
        Intrinsics.checkExpressionValueIsNotNull(string, "transferCodeField.text");
        transferCode = string;
        super.func_146281_b();
    }

    public GuiDonatorCape(@NotNull GuiAltManager prevGui) {
        Intrinsics.checkParameterIsNotNull((Object)prevGui, "prevGui");
        this.prevGui = prevGui;
        this.status = "";
    }

    static {
        Companion = new Companion(null);
        transferCode = "";
        capeEnabled = true;
    }

    public static final /* synthetic */ GuiTextField access$getTransferCodeField$p(GuiDonatorCape $this) {
        GuiTextField guiTextField = $this.transferCodeField;
        if (guiTextField == null) {
            Intrinsics.throwUninitializedPropertyAccessException("transferCodeField");
        }
        return guiTextField;
    }

    public static final /* synthetic */ void access$setTransferCodeField$p(GuiDonatorCape $this, GuiTextField guiTextField) {
        $this.transferCodeField = guiTextField;
    }

    public static final /* synthetic */ String access$getStatus$p(GuiDonatorCape $this) {
        return $this.status;
    }

    public static final /* synthetic */ void access$setStatus$p(GuiDonatorCape $this, String string) {
        $this.status = string;
    }

    public static final /* synthetic */ GuiButton access$getStateButton$p(GuiDonatorCape $this) {
        GuiButton guiButton = $this.stateButton;
        if (guiButton == null) {
            Intrinsics.throwUninitializedPropertyAccessException("stateButton");
        }
        return guiButton;
    }

    public static final /* synthetic */ void access$setStateButton$p(GuiDonatorCape $this, GuiButton guiButton) {
        $this.stateButton = guiButton;
    }

    @Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, xi=2, d1={"\u0000\u001c\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0002\b\u0005\n\u0002\u0010\u000e\n\u0002\b\u0005\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002R\u001a\u0010\u0003\u001a\u00020\u0004X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u0005\u0010\u0006\"\u0004\b\u0007\u0010\bR\u001a\u0010\t\u001a\u00020\nX\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u000b\u0010\f\"\u0004\b\r\u0010\u000e\u00a8\u0006\u000f"}, d2={"Lnet/ccbluex/liquidbounce/ui/client/altmanager/sub/GuiDonatorCape$Companion;", "", "()V", "capeEnabled", "", "getCapeEnabled", "()Z", "setCapeEnabled", "(Z)V", "transferCode", "", "getTransferCode", "()Ljava/lang/String;", "setTransferCode", "(Ljava/lang/String;)V", "KyinoClient"})
    public static final class Companion {
        @NotNull
        public final String getTransferCode() {
            return transferCode;
        }

        public final void setTransferCode(@NotNull String string) {
            Intrinsics.checkParameterIsNotNull(string, "<set-?>");
            transferCode = string;
        }

        public final boolean getCapeEnabled() {
            return capeEnabled;
        }

        public final void setCapeEnabled(boolean bl) {
            capeEnabled = bl;
        }

        private Companion() {
        }

        public /* synthetic */ Companion(DefaultConstructorMarker $constructor_marker) {
            this();
        }
    }
}

