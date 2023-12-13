package slavikcodd3r.rainbow.gui.hacktools;

import java.io.IOException;
import net.minecraft.client.gui.GuiButton;
import org.lwjgl.input.Keyboard;
import net.minecraft.client.gui.GuiScreen;

public class GuiHackTools extends GuiScreen
{
    private GuiScreen parentScreen;
    
    public GuiHackTools(final GuiScreen guiscreen) {
        this.parentScreen = guiscreen;
    }
    
    @Override
    public void onGuiClosed() {
        Keyboard.enableRepeatEvents(false);
    }
    
    @Override
    protected void actionPerformed(final GuiButton guibutton) {
        if (!guibutton.enabled) {
            return;
        }
        if (guibutton.id == 0) {
            this.mc.displayGuiScreen(this.parentScreen);
        }
        else if (guibutton.id == 1) {
            final GuiSubdomainBrute ps = new GuiSubdomainBrute();
            ps.setVisible(true);
        }
        else if (guibutton.id == 2) {
            this.mc.displayGuiScreen(new GuiOfflineName(this.parentScreen));
        }
        else if (guibutton.id == 3) {
            this.mc.displayGuiScreen(new GuiBungeeIpNick(this.parentScreen));
        }
        else if (guibutton.id == 4) {
            final GuiHttpPostBrute ps2 = new GuiHttpPostBrute();
            ps2.setVisible(true);
        }
        else if (guibutton.id == 5) {
            final GuiHttpGetBrute ps3 = new GuiHttpGetBrute();
            ps3.setVisible(true);
        }
        else if (guibutton.id == 7) {
            this.mc.displayGuiScreen(new GuiSpoofUUID(this.parentScreen));
        }
        else if (guibutton.id == 6) {
            this.mc.displayGuiScreen(new GuiProxy(this.parentScreen));
        }
    }
    
    @Override
    protected void mouseClicked(final int i, final int j, final int k) throws IOException {
        super.mouseClicked(i, j, k);
    }
    
    @Override
    public void initGui() {
        Keyboard.enableRepeatEvents(true);
        this.buttonList.clear();        
        this.buttonList.add(new GuiButton(6, this.width / 2 - 100, this.height / 4 - 48 + 12, "Proxy"));
        this.buttonList.add(new GuiButton(5, this.width / 2 - 100, this.height / 4 - 24 + 12, "HTTP(S) GET Method Brute"));
        this.buttonList.add(new GuiButton(7, this.width / 2 - 100, this.height / 4 + 0 + 12, "Spoof UUID"));
        this.buttonList.add(new GuiButton(4, this.width / 2 - 100, this.height / 4 + 24 + 12, "HTTP(S) POST Method Accounts Brute"));
        this.buttonList.add(new GuiButton(3, this.width / 2 - 100, this.height / 4 + 48 + 12, "Bungee Offline UUID Spoof"));
        this.buttonList.add(new GuiButton(2, this.width / 2 - 100, this.height / 4 + 72 + 12, "Offline Name"));
        this.buttonList.add(new GuiButton(1, this.width / 2 - 100, this.height / 4 + 96 + 12, "Subdomain Brute"));
        this.buttonList.add(new GuiButton(0, this.width / 2 - 100, this.height / 4 + 120 + 12, "Cancel"));
    }
    
    @Override
    public void drawScreen(final int i, final int j, final float f) {
        this.drawDefaultBackground();
        super.drawScreen(i, j, f);
    }
}
