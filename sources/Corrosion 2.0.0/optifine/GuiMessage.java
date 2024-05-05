package optifine;

import com.google.common.collect.Lists;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiOptionButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;

public class GuiMessage extends GuiScreen {
    private GuiScreen parentScreen;
    private String messageLine1;
    private String messageLine2;
    private final List listLines2 = Lists.newArrayList();
    protected String confirmButtonText;
    private int ticksUntilEnable;

    public GuiMessage(GuiScreen p_i48_1_, String p_i48_2_, String p_i48_3_) {
        this.parentScreen = p_i48_1_;
        this.messageLine1 = p_i48_2_;
        this.messageLine2 = p_i48_3_;
        this.confirmButtonText = I18n.format("gui.done");
    }

    public void initGui() {
        this.buttonList.add(new GuiOptionButton(0, this.width / 2 - 74, this.height / 6 + 96, this.confirmButtonText));
        this.listLines2.clear();
        this.listLines2.addAll(this.fontRendererObj.listFormattedStringToWidth(this.messageLine2, this.width - 50));
    }

    protected void actionPerformed(GuiButton button) throws IOException {
        Config.getMinecraft().displayGuiScreen(this.parentScreen);
    }

    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        this.drawDefaultBackground();
        this.drawCenteredString(this.fontRendererObj, this.messageLine1, this.width / 2, 70, 16777215);
        int i = 90;

        for(Iterator var5 = this.listLines2.iterator(); var5.hasNext(); i += this.fontRendererObj.FONT_HEIGHT) {
            Object s = var5.next();
            this.drawCenteredString(this.fontRendererObj, (String)s, this.width / 2, i, 16777215);
        }

        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    public void setButtonDelay(int p_setButtonDelay_1_) {
        this.ticksUntilEnable = p_setButtonDelay_1_;

        GuiButton guibutton;
        for(Iterator var2 = this.buttonList.iterator(); var2.hasNext(); guibutton.enabled = false) {
            guibutton = (GuiButton)var2.next();
        }

    }

    public void updateScreen() {
        super.updateScreen();
        GuiButton guibutton;
        if (--this.ticksUntilEnable == 0) {
            for(Iterator var1 = this.buttonList.iterator(); var1.hasNext(); guibutton.enabled = true) {
                guibutton = (GuiButton)var1.next();
            }
        }

    }
}
