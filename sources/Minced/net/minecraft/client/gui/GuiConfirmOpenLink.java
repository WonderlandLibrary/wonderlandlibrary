// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.client.gui;

import java.io.IOException;
import net.minecraft.client.resources.I18n;

public class GuiConfirmOpenLink extends GuiYesNo
{
    private final String openLinkWarning;
    private final String copyLinkButtonText;
    private final String linkText;
    private boolean showSecurityWarning;
    
    public GuiConfirmOpenLink(final GuiYesNoCallback parentScreenIn, final String linkTextIn, final int parentButtonClickedIdIn, final boolean trusted) {
        super(parentScreenIn, I18n.format(trusted ? "chat.link.confirmTrusted" : "chat.link.confirm", new Object[0]), linkTextIn, parentButtonClickedIdIn);
        this.showSecurityWarning = true;
        this.confirmButtonText = I18n.format(trusted ? "chat.link.open" : "gui.yes", new Object[0]);
        this.cancelButtonText = I18n.format(trusted ? "gui.cancel" : "gui.no", new Object[0]);
        this.copyLinkButtonText = I18n.format("chat.copy", new Object[0]);
        this.openLinkWarning = I18n.format("chat.link.warning", new Object[0]);
        this.linkText = linkTextIn;
    }
    
    @Override
    public void initGui() {
        super.initGui();
        this.buttonList.clear();
        this.buttonList.add(new GuiButton(0, this.width / 2 - 50 - 105, this.height / 6 + 96, 100, 20, this.confirmButtonText));
        this.buttonList.add(new GuiButton(2, this.width / 2 - 50, this.height / 6 + 96, 100, 20, this.copyLinkButtonText));
        this.buttonList.add(new GuiButton(1, this.width / 2 - 50 + 105, this.height / 6 + 96, 100, 20, this.cancelButtonText));
    }
    
    @Override
    protected void actionPerformed(final GuiButton button) throws IOException {
        if (button.id == 2) {
            this.copyLinkToClipboard();
        }
        this.parentScreen.confirmClicked(button.id == 0, this.parentButtonClickedId);
    }
    
    public void copyLinkToClipboard() {
        GuiScreen.setClipboardString(this.linkText);
    }
    
    @Override
    public void drawScreen(final int mouseX, final int mouseY, final float partialTicks) {
        super.drawScreen(mouseX, mouseY, partialTicks);
        if (this.showSecurityWarning) {
            this.drawCenteredString(this.fontRenderer, this.openLinkWarning, this.width / 2, 110, 16764108);
        }
    }
    
    public void disableSecurityWarning() {
        this.showSecurityWarning = false;
    }
}
