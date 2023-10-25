package net.minecraft.client.gui;

import java.io.IOException;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.IChatComponent;
import net.minecraft.world.WorldSettings;

public class GuiShareToLan
extends GuiScreen {
    private final GuiScreen field_146598_a;
    private GuiButton field_146596_f;
    private GuiButton field_146597_g;
    private String field_146599_h = "survival";
    private boolean field_146600_i;

    public GuiShareToLan(GuiScreen p_i1055_1_) {
        this.field_146598_a = p_i1055_1_;
    }

    public void initGui() {
        this.buttonList.clear();
        this.buttonList.add((Object)new GuiButton(101, width / 2 - 155, height - 28, 150, 20, I18n.format((String)"lanServer.start", (Object[])new Object[0])));
        this.buttonList.add((Object)new GuiButton(102, width / 2 + 5, height - 28, 150, 20, I18n.format((String)"gui.cancel", (Object[])new Object[0])));
        this.field_146597_g = new GuiButton(104, width / 2 - 155, 100, 150, 20, I18n.format((String)"selectWorld.gameMode", (Object[])new Object[0]));
        this.buttonList.add((Object)this.field_146597_g);
        this.field_146596_f = new GuiButton(103, width / 2 + 5, 100, 150, 20, I18n.format((String)"selectWorld.allowCommands", (Object[])new Object[0]));
        this.buttonList.add((Object)this.field_146596_f);
        this.func_146595_g();
    }

    private void func_146595_g() {
        this.field_146597_g.displayString = I18n.format((String)"selectWorld.gameMode", (Object[])new Object[0]) + " " + I18n.format((String)("selectWorld.gameMode." + this.field_146599_h), (Object[])new Object[0]);
        this.field_146596_f.displayString = I18n.format((String)"selectWorld.allowCommands", (Object[])new Object[0]) + " ";
        this.field_146596_f.displayString = this.field_146600_i ? this.field_146596_f.displayString + I18n.format((String)"options.on", (Object[])new Object[0]) : this.field_146596_f.displayString + I18n.format((String)"options.off", (Object[])new Object[0]);
    }

    protected void actionPerformed(GuiButton button) throws IOException {
        if (button.id == 102) {
            this.mc.displayGuiScreen(this.field_146598_a);
        } else if (button.id == 104) {
            this.field_146599_h = this.field_146599_h.equals((Object)"spectator") ? "creative" : (this.field_146599_h.equals((Object)"creative") ? "adventure" : (this.field_146599_h.equals((Object)"adventure") ? "survival" : "spectator"));
            this.func_146595_g();
        } else if (button.id == 103) {
            this.field_146600_i = !this.field_146600_i;
            this.func_146595_g();
        } else if (button.id == 101) {
            this.mc.displayGuiScreen((GuiScreen)null);
            String s = this.mc.getIntegratedServer().shareToLAN(WorldSettings.GameType.getByName((String)this.field_146599_h), this.field_146600_i);
            Object ichatcomponent = s != null ? new ChatComponentTranslation("commands.publish.started", new Object[]{s}) : new ChatComponentText("commands.publish.failed");
            this.mc.ingameGUI.getChatGUI().printChatMessage((IChatComponent)ichatcomponent);
        }
    }

    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        this.drawDefaultBackground();
        this.drawCenteredString(this.fontRendererObj, I18n.format((String)"lanServer.title", (Object[])new Object[0]), width / 2, 50, 0xFFFFFF);
        this.drawCenteredString(this.fontRendererObj, I18n.format((String)"lanServer.otherPlayers", (Object[])new Object[0]), width / 2, 82, 0xFFFFFF);
        super.drawScreen(mouseX, mouseY, partialTicks);
    }
}
