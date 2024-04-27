/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.thealtening.AltService$EnumAltService
 *  kotlin.Unit
 *  kotlin.concurrent.ThreadsKt
 *  kotlin.jvm.functions.Function0
 *  org.lwjgl.input.Keyboard
 */
package net.ccbluex.liquidbounce.ui.client.altmanager.sub;

import com.thealtening.AltService;
import kotlin.Unit;
import kotlin.concurrent.ThreadsKt;
import kotlin.jvm.functions.Function0;
import net.ccbluex.liquidbounce.api.minecraft.client.gui.IGuiButton;
import net.ccbluex.liquidbounce.api.minecraft.client.gui.IGuiTextField;
import net.ccbluex.liquidbounce.api.util.WrappedGuiScreen;
import net.ccbluex.liquidbounce.ui.client.altmanager.GuiAltManager;
import net.ccbluex.liquidbounce.ui.client.altmanager.sub.GuiSessionLogin$WhenMappings;
import net.ccbluex.liquidbounce.ui.font.Fonts;
import net.ccbluex.liquidbounce.utils.ClientUtils;
import net.ccbluex.liquidbounce.utils.MinecraftInstance;
import net.ccbluex.liquidbounce.utils.login.LoginUtils;
import net.ccbluex.liquidbounce.utils.render.RenderUtils;
import net.mcleaks.MCLeaks;
import org.lwjgl.input.Keyboard;

public final class GuiSessionLogin
extends WrappedGuiScreen {
    private IGuiButton loginButton;
    private IGuiTextField sessionTokenField;
    private String status;
    private final GuiAltManager prevGui;

    @Override
    public void initGui() {
        Keyboard.enableRepeatEvents((boolean)true);
        this.loginButton = MinecraftInstance.classProvider.createGuiButton(1, this.getRepresentedScreen().getWidth() / 2 - 100, this.getRepresentedScreen().getHeight() / 4 + 96, "Login");
        this.getRepresentedScreen().getButtonList().add(this.loginButton);
        this.getRepresentedScreen().getButtonList().add(MinecraftInstance.classProvider.createGuiButton(0, this.getRepresentedScreen().getWidth() / 2 - 100, this.getRepresentedScreen().getHeight() / 4 + 120, "Back"));
        this.sessionTokenField = MinecraftInstance.classProvider.createGuiTextField(666, Fonts.font40, this.getRepresentedScreen().getWidth() / 2 - 100, 80, 200, 20);
        this.sessionTokenField.setFocused(true);
        this.sessionTokenField.setMaxStringLength(Integer.MAX_VALUE);
        super.initGui();
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        this.getRepresentedScreen().drawBackground(0);
        RenderUtils.drawRect(30.0f, 30.0f, (float)this.getRepresentedScreen().getWidth() - 30.0f, (float)this.getRepresentedScreen().getHeight() - 30.0f, Integer.MIN_VALUE);
        Fonts.font35.drawCenteredString("Session Login", (float)this.getRepresentedScreen().getWidth() / 2.0f, 36.0f, 0xFFFFFF);
        Fonts.font35.drawCenteredString(this.status, (float)this.getRepresentedScreen().getWidth() / 2.0f, (float)this.getRepresentedScreen().getHeight() / 4.0f + 80.0f, 0xFFFFFF);
        this.sessionTokenField.drawTextBox();
        Fonts.font40.drawCenteredString("\u00a77Session Token:", (float)this.getRepresentedScreen().getWidth() / 2.0f - 65.0f, 66.0f, 0xFFFFFF);
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
                this.status = "\u00a7aLogging in...";
                ThreadsKt.thread$default((boolean)false, (boolean)false, null, null, (int)0, (Function0)((Function0)new Function0<Unit>(this){
                    final /* synthetic */ GuiSessionLogin this$0;

                    public final void invoke() {
                        String string;
                        LoginUtils.LoginResult loginResult = LoginUtils.loginSessionId(GuiSessionLogin.access$getSessionTokenField$p(this.this$0).getText());
                        GuiSessionLogin guiSessionLogin = this.this$0;
                        switch (GuiSessionLogin$WhenMappings.$EnumSwitchMapping$0[loginResult.ordinal()]) {
                            case 1: {
                                if (GuiAltManager.altService.getCurrentService() != AltService.EnumAltService.MOJANG) {
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
                                string = "\u00a7cYour name is now \u00a7f\u00a7l" + MinecraftInstance.mc.getSession().getUsername() + "\u00a7c";
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
                        GuiSessionLogin.access$getLoginButton$p(this.this$0).setEnabled(true);
                    }
                    {
                        this.this$0 = guiSessionLogin;
                        super(0);
                    }
                }), (int)31, null);
            }
        }
    }

    @Override
    public void keyTyped(char typedChar, int keyCode) {
        if (1 == keyCode) {
            MinecraftInstance.mc.displayGuiScreen(this.prevGui.getRepresentedScreen());
            return;
        }
        if (this.sessionTokenField.isFocused()) {
            this.sessionTokenField.textboxKeyTyped(typedChar, keyCode);
        }
        super.keyTyped(typedChar, keyCode);
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        this.sessionTokenField.mouseClicked(mouseX, mouseY, mouseButton);
        super.mouseClicked(mouseX, mouseY, mouseButton);
    }

    @Override
    public void updateScreen() {
        this.sessionTokenField.updateCursorCounter();
        super.updateScreen();
    }

    @Override
    public void onGuiClosed() {
        Keyboard.enableRepeatEvents((boolean)false);
        super.onGuiClosed();
    }

    public GuiSessionLogin(GuiAltManager prevGui) {
        this.prevGui = prevGui;
        this.status = "";
    }

    public static final /* synthetic */ IGuiTextField access$getSessionTokenField$p(GuiSessionLogin $this) {
        return $this.sessionTokenField;
    }

    public static final /* synthetic */ void access$setSessionTokenField$p(GuiSessionLogin $this, IGuiTextField iGuiTextField) {
        $this.sessionTokenField = iGuiTextField;
    }

    public static final /* synthetic */ String access$getStatus$p(GuiSessionLogin $this) {
        return $this.status;
    }

    public static final /* synthetic */ void access$setStatus$p(GuiSessionLogin $this, String string) {
        $this.status = string;
    }

    public static final /* synthetic */ IGuiButton access$getLoginButton$p(GuiSessionLogin $this) {
        return $this.loginButton;
    }

    public static final /* synthetic */ void access$setLoginButton$p(GuiSessionLogin $this, IGuiButton iGuiButton) {
        $this.loginButton = iGuiButton;
    }
}

