/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.thealtening.AltService$EnumAltService
 *  kotlin.TypeCastException
 *  org.lwjgl.input.Keyboard
 */
package net.ccbluex.liquidbounce.ui.client.altmanager.sub.altgenerator;

import com.thealtening.AltService;
import java.io.IOException;
import kotlin.TypeCastException;
import net.ccbluex.liquidbounce.api.minecraft.client.gui.IGuiButton;
import net.ccbluex.liquidbounce.api.minecraft.client.gui.IGuiTextField;
import net.ccbluex.liquidbounce.api.util.WrappedGuiScreen;
import net.ccbluex.liquidbounce.ui.client.altmanager.GuiAltManager;
import net.ccbluex.liquidbounce.ui.font.Fonts;
import net.ccbluex.liquidbounce.utils.ClientUtils;
import net.ccbluex.liquidbounce.utils.MinecraftInstance;
import net.ccbluex.liquidbounce.utils.misc.MiscUtils;
import net.ccbluex.liquidbounce.utils.render.RenderUtils;
import net.mcleaks.Callback;
import net.mcleaks.MCLeaks;
import net.mcleaks.RedeemResponse;
import net.mcleaks.Session;
import org.lwjgl.input.Keyboard;

public final class GuiMCLeaks
extends WrappedGuiScreen {
    private IGuiTextField tokenField;
    private String status;
    private final GuiAltManager prevGui;

    @Override
    public void updateScreen() {
        this.tokenField.updateCursorCounter();
    }

    @Override
    public void initGui() {
        Keyboard.enableRepeatEvents((boolean)true);
        if (MCLeaks.isAltActive()) {
            this.status = "\u00a7aToken active. Using \u00a79" + MCLeaks.getSession().getUsername() + "\u00a7a to login!";
        }
        this.getRepresentedScreen().getButtonList().add(MinecraftInstance.classProvider.createGuiButton(1, this.getRepresentedScreen().getWidth() / 2 - 100, this.getRepresentedScreen().getHeight() / 4 + 65, 200, 20, "Login"));
        this.getRepresentedScreen().getButtonList().add(MinecraftInstance.classProvider.createGuiButton(2, this.getRepresentedScreen().getWidth() / 2 - 100, this.getRepresentedScreen().getHeight() - 54, 98, 20, "Get Token"));
        this.getRepresentedScreen().getButtonList().add(MinecraftInstance.classProvider.createGuiButton(3, this.getRepresentedScreen().getWidth() / 2 + 2, this.getRepresentedScreen().getHeight() - 54, 98, 20, "Back"));
        this.tokenField = MinecraftInstance.classProvider.createGuiTextField(0, Fonts.font40, this.getRepresentedScreen().getWidth() / 2 - 100, this.getRepresentedScreen().getHeight() / 4 + 40, 200, 20);
        this.tokenField.setFocused(true);
        this.tokenField.setMaxStringLength(16);
    }

    @Override
    public void onGuiClosed() {
        Keyboard.enableRepeatEvents((boolean)false);
    }

    @Override
    public void actionPerformed(IGuiButton button) {
        if (!button.getEnabled()) {
            return;
        }
        switch (button.getId()) {
            case 1: {
                if (this.tokenField.getText().length() != 16) {
                    this.status = "\u00a7cThe token has to be 16 characters long!";
                    return;
                }
                button.setEnabled(false);
                button.setDisplayString("Please wait ...");
                MCLeaks.redeem(this.tokenField.getText(), new Callback<Object>(this, button){
                    final /* synthetic */ GuiMCLeaks this$0;
                    final /* synthetic */ IGuiButton $button;

                    public final void done(Object it) {
                        if (it instanceof String) {
                            GuiMCLeaks.access$setStatus$p(this.this$0, "\u00a7c" + it);
                            this.$button.setEnabled(true);
                            this.$button.setDisplayString("Login");
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
                        this.$button.setEnabled(true);
                        this.$button.setDisplayString("Login");
                        GuiMCLeaks.access$getPrevGui$p((GuiMCLeaks)this.this$0).status = GuiMCLeaks.access$getStatus$p(this.this$0);
                        MinecraftInstance.mc.displayGuiScreen(GuiMCLeaks.access$getPrevGui$p(this.this$0).getRepresentedScreen());
                    }
                    {
                        this.this$0 = guiMCLeaks;
                        this.$button = iGuiButton;
                    }
                });
                break;
            }
            case 2: {
                MiscUtils.showURL("https://mcleaks.net/");
                break;
            }
            case 3: {
                MinecraftInstance.mc.displayGuiScreen(this.prevGui.getRepresentedScreen());
            }
        }
    }

    @Override
    public void keyTyped(char typedChar, int keyCode) {
        switch (keyCode) {
            case 1: {
                MinecraftInstance.mc.displayGuiScreen(this.prevGui.getRepresentedScreen());
                break;
            }
            case 15: {
                this.tokenField.setFocused(!this.tokenField.isFocused());
                break;
            }
            case 28: 
            case 156: {
                this.actionPerformed(this.getRepresentedScreen().getButtonList().get(1));
                break;
            }
            default: {
                this.tokenField.textboxKeyTyped(typedChar, keyCode);
            }
        }
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        this.tokenField.mouseClicked(mouseX, mouseY, mouseButton);
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        this.getRepresentedScreen().drawBackground(0);
        RenderUtils.drawRect(30.0f, 30.0f, (float)this.getRepresentedScreen().getWidth() - 30.0f, (float)this.getRepresentedScreen().getHeight() - 30.0f, Integer.MIN_VALUE);
        Fonts.font40.drawCenteredString("MCLeaks", (float)this.getRepresentedScreen().getWidth() / 2.0f, 6.0f, 0xFFFFFF);
        Fonts.font40.drawString("Token:", (float)this.getRepresentedScreen().getWidth() / 2.0f - (float)100, (float)this.getRepresentedScreen().getHeight() / 4.0f + (float)30, 0xA0A0A0);
        String status = this.status;
        if (status != null) {
            Fonts.font40.drawCenteredString(status, (float)this.getRepresentedScreen().getWidth() / 2.0f, 18.0f, 0xFFFFFF);
        }
        this.tokenField.drawTextBox();
        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    public GuiMCLeaks(GuiAltManager prevGui) {
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

