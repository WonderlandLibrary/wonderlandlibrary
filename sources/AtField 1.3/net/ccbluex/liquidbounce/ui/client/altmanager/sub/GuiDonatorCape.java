/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kotlin.Unit
 *  kotlin.concurrent.ThreadsKt
 *  kotlin.jvm.functions.Function0
 *  kotlin.jvm.internal.DefaultConstructorMarker
 *  org.apache.http.Header
 *  org.apache.http.client.methods.CloseableHttpResponse
 *  org.apache.http.client.methods.HttpDelete
 *  org.apache.http.client.methods.HttpPut
 *  org.apache.http.client.methods.HttpRequestBase
 *  org.apache.http.client.methods.HttpUriRequest
 *  org.apache.http.impl.client.CloseableHttpClient
 *  org.apache.http.impl.client.HttpClients
 *  org.apache.http.message.BasicHeader
 *  org.lwjgl.input.Keyboard
 */
package net.ccbluex.liquidbounce.ui.client.altmanager.sub;

import kotlin.Unit;
import kotlin.concurrent.ThreadsKt;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.internal.DefaultConstructorMarker;
import net.ccbluex.liquidbounce.api.minecraft.client.gui.IGuiButton;
import net.ccbluex.liquidbounce.api.minecraft.client.gui.IGuiTextField;
import net.ccbluex.liquidbounce.api.util.WrappedGuiScreen;
import net.ccbluex.liquidbounce.ui.client.altmanager.GuiAltManager;
import net.ccbluex.liquidbounce.ui.font.Fonts;
import net.ccbluex.liquidbounce.utils.MinecraftInstance;
import net.ccbluex.liquidbounce.utils.misc.MiscUtils;
import net.ccbluex.liquidbounce.utils.render.RenderUtils;
import org.apache.http.Header;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicHeader;
import org.lwjgl.input.Keyboard;

public final class GuiDonatorCape
extends WrappedGuiScreen {
    public static final Companion Companion = new Companion(null);
    private IGuiTextField transferCodeField;
    private IGuiButton stateButton;
    private static String transferCode = "";
    private String status;
    private static boolean capeEnabled = true;
    private final GuiAltManager prevGui;

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
                this.stateButton.setEnabled(false);
                ThreadsKt.thread$default((boolean)false, (boolean)false, null, null, (int)0, (Function0)new Function0(this){
                    final GuiDonatorCape this$0;
                    {
                        this.this$0 = guiDonatorCape;
                        super(0);
                    }

                    public final void invoke() {
                        String string;
                        CloseableHttpClient closeableHttpClient = HttpClients.createDefault();
                        BasicHeader[] basicHeaderArray = new BasicHeader[]{new BasicHeader("Content-Type", "application/json"), new BasicHeader("Authorization", GuiDonatorCape.access$getTransferCodeField$p(this.this$0).getText())};
                        HttpRequestBase httpRequestBase = GuiDonatorCape.Companion.getCapeEnabled() ? (HttpRequestBase)new HttpDelete("http://capes.liquidbounce.net/api/v1/cape/self") : (HttpRequestBase)new HttpPut("http://capes.liquidbounce.net/api/v1/cape/self");
                        httpRequestBase.setHeaders((Header[])basicHeaderArray);
                        CloseableHttpResponse closeableHttpResponse = closeableHttpClient.execute((HttpUriRequest)httpRequestBase);
                        int n = closeableHttpResponse.getStatusLine().getStatusCode();
                        if (n == 204) {
                            GuiDonatorCape.Companion.setCapeEnabled(!GuiDonatorCape.Companion.getCapeEnabled());
                            string = GuiDonatorCape.Companion.getCapeEnabled() ? "\u00a7aSuccessfully enabled cape" : "\u00a7aSuccessfully disabled cape";
                        } else {
                            string = "\u00a7cFailed to toggle cape (" + n + ')';
                        }
                        GuiDonatorCape.access$setStatus$p(this.this$0, string);
                        GuiDonatorCape.access$getStateButton$p(this.this$0).setEnabled(true);
                    }

                    public Object invoke() {
                        this.invoke();
                        return Unit.INSTANCE;
                    }

                    static {
                    }
                }, (int)31, null);
                break;
            }
            case 2: {
                MiscUtils.showURL("https://donate.liquidbounce.net");
                break;
            }
        }
    }

    @Override
    public void mouseClicked(int n, int n2, int n3) {
        this.transferCodeField.mouseClicked(n, n2, n3);
        super.mouseClicked(n, n2, n3);
    }

    public static final boolean access$getCapeEnabled$cp() {
        return capeEnabled;
    }

    @Override
    public void updateScreen() {
        this.transferCodeField.updateCursorCounter();
        super.updateScreen();
    }

    @Override
    public void keyTyped(char c, int n) {
        if (1 == n) {
            MinecraftInstance.mc.displayGuiScreen(this.prevGui.getRepresentedScreen());
            return;
        }
        if (this.transferCodeField.isFocused()) {
            this.transferCodeField.textboxKeyTyped(c, n);
        }
        super.keyTyped(c, n);
    }

    public static final String access$getTransferCode$cp() {
        return transferCode;
    }

    public static final void access$setCapeEnabled$cp(boolean bl) {
        capeEnabled = bl;
    }

    public static final IGuiTextField access$getTransferCodeField$p(GuiDonatorCape guiDonatorCape) {
        return guiDonatorCape.transferCodeField;
    }

    @Override
    public void drawScreen(int n, int n2, float f) {
        this.getRepresentedScreen().drawBackground(0);
        RenderUtils.drawRect(30.0f, 30.0f, (float)this.getRepresentedScreen().getWidth() - 30.0f, (float)this.getRepresentedScreen().getHeight() - 30.0f, Integer.MIN_VALUE);
        Fonts.roboto35.drawCenteredString("Donator Cape", (float)this.getRepresentedScreen().getWidth() / 2.0f, 36.0f, 0xFFFFFF);
        Fonts.roboto35.drawCenteredString(this.status, (float)this.getRepresentedScreen().getWidth() / 2.0f, (float)this.getRepresentedScreen().getHeight() / 4.0f + (float)80, 0xFFFFFF);
        this.transferCodeField.drawTextBox();
        Fonts.roboto40.drawCenteredString("\u00a77Transfer Code:", (float)this.getRepresentedScreen().getWidth() / 2.0f - (float)65, 66.0f, 0xFFFFFF);
        this.stateButton.setDisplayString(capeEnabled ? "Disable Cape" : "Enable Cape");
        super.drawScreen(n, n2, f);
    }

    @Override
    public void initGui() {
        Keyboard.enableRepeatEvents((boolean)true);
        this.stateButton = MinecraftInstance.classProvider.createGuiButton(1, this.getRepresentedScreen().getWidth() / 2 - 100, 105, "Disable Cape");
        this.getRepresentedScreen().getButtonList().add(this.stateButton);
        this.getRepresentedScreen().getButtonList().add(MinecraftInstance.classProvider.createGuiButton(2, this.getRepresentedScreen().getWidth() / 2 - 100, this.getRepresentedScreen().getHeight() / 4 + 96, "Donate to get Cape"));
        this.getRepresentedScreen().getButtonList().add(MinecraftInstance.classProvider.createGuiButton(0, this.getRepresentedScreen().getWidth() / 2 - 100, this.getRepresentedScreen().getHeight() / 4 + 120, "Back"));
        this.transferCodeField = MinecraftInstance.classProvider.createGuiPasswordField(666, Fonts.roboto40, this.getRepresentedScreen().getWidth() / 2 - 100, 80, 200, 20);
        this.transferCodeField.setFocused(true);
        this.transferCodeField.setMaxStringLength(Integer.MAX_VALUE);
        this.transferCodeField.setText(transferCode);
        super.initGui();
    }

    public static final void access$setTransferCode$cp(String string) {
        transferCode = string;
    }

    public GuiDonatorCape(GuiAltManager guiAltManager) {
        this.prevGui = guiAltManager;
        this.status = "";
    }

    public static final void access$setTransferCodeField$p(GuiDonatorCape guiDonatorCape, IGuiTextField iGuiTextField) {
        guiDonatorCape.transferCodeField = iGuiTextField;
    }

    public static final String access$getStatus$p(GuiDonatorCape guiDonatorCape) {
        return guiDonatorCape.status;
    }

    public static final void access$setStateButton$p(GuiDonatorCape guiDonatorCape, IGuiButton iGuiButton) {
        guiDonatorCape.stateButton = iGuiButton;
    }

    @Override
    public void onGuiClosed() {
        Keyboard.enableRepeatEvents((boolean)false);
        transferCode = this.transferCodeField.getText();
        super.onGuiClosed();
    }

    public static final void access$setStatus$p(GuiDonatorCape guiDonatorCape, String string) {
        guiDonatorCape.status = string;
    }

    public static final IGuiButton access$getStateButton$p(GuiDonatorCape guiDonatorCape) {
        return guiDonatorCape.stateButton;
    }

    public static final class Companion {
        public final void setTransferCode(String string) {
            GuiDonatorCape.access$setTransferCode$cp(string);
        }

        public final void setCapeEnabled(boolean bl) {
            GuiDonatorCape.access$setCapeEnabled$cp(bl);
        }

        private Companion() {
        }

        public final boolean getCapeEnabled() {
            return GuiDonatorCape.access$getCapeEnabled$cp();
        }

        public Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        public final String getTransferCode() {
            return GuiDonatorCape.access$getTransferCode$cp();
        }
    }
}

