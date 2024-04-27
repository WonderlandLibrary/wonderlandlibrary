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
    private IGuiButton stateButton;
    private IGuiTextField transferCodeField;
    private String status;
    private final GuiAltManager prevGui;
    private static String transferCode;
    private static boolean capeEnabled;
    public static final Companion Companion;

    @Override
    public void initGui() {
        Keyboard.enableRepeatEvents((boolean)true);
        this.stateButton = MinecraftInstance.classProvider.createGuiButton(1, this.getRepresentedScreen().getWidth() / 2 - 100, 105, "Disable Cape");
        this.getRepresentedScreen().getButtonList().add(this.stateButton);
        this.getRepresentedScreen().getButtonList().add(MinecraftInstance.classProvider.createGuiButton(2, this.getRepresentedScreen().getWidth() / 2 - 100, this.getRepresentedScreen().getHeight() / 4 + 96, "Donate to get Cape"));
        this.getRepresentedScreen().getButtonList().add(MinecraftInstance.classProvider.createGuiButton(0, this.getRepresentedScreen().getWidth() / 2 - 100, this.getRepresentedScreen().getHeight() / 4 + 120, "Back"));
        this.transferCodeField = MinecraftInstance.classProvider.createGuiPasswordField(666, Fonts.font40, this.getRepresentedScreen().getWidth() / 2 - 100, 80, 200, 20);
        this.transferCodeField.setFocused(true);
        this.transferCodeField.setMaxStringLength(Integer.MAX_VALUE);
        this.transferCodeField.setText(transferCode);
        super.initGui();
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        this.getRepresentedScreen().drawBackground(0);
        RenderUtils.drawRect(30.0f, 30.0f, (float)this.getRepresentedScreen().getWidth() - 30.0f, (float)this.getRepresentedScreen().getHeight() - 30.0f, Integer.MIN_VALUE);
        Fonts.font35.drawCenteredString("Donator Cape", (float)this.getRepresentedScreen().getWidth() / 2.0f, 36.0f, 0xFFFFFF);
        Fonts.font35.drawCenteredString(this.status, (float)this.getRepresentedScreen().getWidth() / 2.0f, (float)this.getRepresentedScreen().getHeight() / 4.0f + (float)80, 0xFFFFFF);
        this.transferCodeField.drawTextBox();
        Fonts.font40.drawCenteredString("\u00a77Transfer Code:", (float)this.getRepresentedScreen().getWidth() / 2.0f - (float)65, 66.0f, 0xFFFFFF);
        this.stateButton.setDisplayString(capeEnabled ? "Disable Cape" : "Enable Cape");
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
                this.stateButton.setEnabled(false);
                ThreadsKt.thread$default((boolean)false, (boolean)false, null, null, (int)0, (Function0)((Function0)new Function0<Unit>(this){
                    final /* synthetic */ GuiDonatorCape this$0;

                    public final void invoke() {
                        String string;
                        CloseableHttpClient httpClient = HttpClients.createDefault();
                        BasicHeader[] headers = new BasicHeader[]{new BasicHeader("Content-Type", "application/json"), new BasicHeader("Authorization", GuiDonatorCape.access$getTransferCodeField$p(this.this$0).getText())};
                        HttpRequestBase request = GuiDonatorCape.Companion.getCapeEnabled() ? (HttpRequestBase)new HttpDelete("http://capes.liquidbounce.net/api/v1/cape/self") : (HttpRequestBase)new HttpPut("http://capes.liquidbounce.net/api/v1/cape/self");
                        request.setHeaders((Header[])headers);
                        CloseableHttpResponse response = httpClient.execute((HttpUriRequest)request);
                        int statusCode = response.getStatusLine().getStatusCode();
                        if (statusCode == 204) {
                            GuiDonatorCape.Companion.setCapeEnabled(!GuiDonatorCape.Companion.getCapeEnabled());
                            string = GuiDonatorCape.Companion.getCapeEnabled() ? "\u00a7aSuccessfully enabled cape" : "\u00a7aSuccessfully disabled cape";
                        } else {
                            string = "\u00a7cFailed to toggle cape (" + statusCode + ')';
                        }
                        GuiDonatorCape.access$setStatus$p(this.this$0, string);
                        GuiDonatorCape.access$getStateButton$p(this.this$0).setEnabled(true);
                    }
                    {
                        this.this$0 = guiDonatorCape;
                        super(0);
                    }
                }), (int)31, null);
                break;
            }
            case 2: {
                MiscUtils.showURL("https://donate.liquidbounce.net");
            }
        }
    }

    @Override
    public void keyTyped(char typedChar, int keyCode) {
        if (1 == keyCode) {
            MinecraftInstance.mc.displayGuiScreen(this.prevGui.getRepresentedScreen());
            return;
        }
        if (this.transferCodeField.isFocused()) {
            this.transferCodeField.textboxKeyTyped(typedChar, keyCode);
        }
        super.keyTyped(typedChar, keyCode);
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        this.transferCodeField.mouseClicked(mouseX, mouseY, mouseButton);
        super.mouseClicked(mouseX, mouseY, mouseButton);
    }

    @Override
    public void updateScreen() {
        this.transferCodeField.updateCursorCounter();
        super.updateScreen();
    }

    @Override
    public void onGuiClosed() {
        Keyboard.enableRepeatEvents((boolean)false);
        transferCode = this.transferCodeField.getText();
        super.onGuiClosed();
    }

    public GuiDonatorCape(GuiAltManager prevGui) {
        this.prevGui = prevGui;
        this.status = "";
    }

    static {
        Companion = new Companion(null);
        transferCode = "";
        capeEnabled = true;
    }

    public static final /* synthetic */ IGuiTextField access$getTransferCodeField$p(GuiDonatorCape $this) {
        return $this.transferCodeField;
    }

    public static final /* synthetic */ void access$setTransferCodeField$p(GuiDonatorCape $this, IGuiTextField iGuiTextField) {
        $this.transferCodeField = iGuiTextField;
    }

    public static final /* synthetic */ String access$getStatus$p(GuiDonatorCape $this) {
        return $this.status;
    }

    public static final /* synthetic */ void access$setStatus$p(GuiDonatorCape $this, String string) {
        $this.status = string;
    }

    public static final /* synthetic */ IGuiButton access$getStateButton$p(GuiDonatorCape $this) {
        return $this.stateButton;
    }

    public static final /* synthetic */ void access$setStateButton$p(GuiDonatorCape $this, IGuiButton iGuiButton) {
        $this.stateButton = iGuiButton;
    }

    public static final class Companion {
        public final String getTransferCode() {
            return transferCode;
        }

        public final void setTransferCode(String string) {
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

