package net.minecraft.client.gui;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import java.io.IOException;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.resources.I18n;
import net.minecraft.command.server.CommandBlockLogic;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.client.C17PacketCustomPayload;
import net.minecraft.util.IChatComponent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.input.Keyboard;

public class GuiCommandBlock
extends GuiScreen {
    private static final Logger field_146488_a = LogManager.getLogger();
    private GuiTextField commandTextField;
    private GuiTextField previousOutputTextField;
    private final CommandBlockLogic localCommandBlock;
    private GuiButton doneBtn;
    private GuiButton cancelBtn;
    private GuiButton field_175390_s;
    private boolean field_175389_t;

    public GuiCommandBlock(CommandBlockLogic p_i45032_1_) {
        this.localCommandBlock = p_i45032_1_;
    }

    public void updateScreen() {
        this.commandTextField.updateCursorCounter();
    }

    public void initGui() {
        Keyboard.enableRepeatEvents((boolean)true);
        this.buttonList.clear();
        this.doneBtn = new GuiButton(0, width / 2 - 4 - 150, height / 4 + 120 + 12, 150, 20, I18n.format((String)"gui.done", (Object[])new Object[0]));
        this.buttonList.add((Object)this.doneBtn);
        this.cancelBtn = new GuiButton(1, width / 2 + 4, height / 4 + 120 + 12, 150, 20, I18n.format((String)"gui.cancel", (Object[])new Object[0]));
        this.buttonList.add((Object)this.cancelBtn);
        this.field_175390_s = new GuiButton(4, width / 2 + 150 - 20, 150, 20, 20, "O");
        this.buttonList.add((Object)this.field_175390_s);
        this.commandTextField = new GuiTextField(2, this.fontRendererObj, width / 2 - 150, 50, 300, 20);
        this.commandTextField.setMaxStringLength(Short.MAX_VALUE);
        this.commandTextField.setFocused(true);
        this.commandTextField.setText(this.localCommandBlock.getCommand());
        this.previousOutputTextField = new GuiTextField(3, this.fontRendererObj, width / 2 - 150, 150, 276, 20);
        this.previousOutputTextField.setMaxStringLength(Short.MAX_VALUE);
        this.previousOutputTextField.setEnabled(false);
        this.previousOutputTextField.setText("-");
        this.field_175389_t = this.localCommandBlock.shouldTrackOutput();
        this.func_175388_a();
        this.doneBtn.enabled = this.commandTextField.getText().trim().length() > 0;
    }

    public void onGuiClosed() {
        Keyboard.enableRepeatEvents((boolean)false);
    }

    protected void actionPerformed(GuiButton button) throws IOException {
        if (button.enabled) {
            if (button.id == 1) {
                this.localCommandBlock.setTrackOutput(this.field_175389_t);
                this.mc.displayGuiScreen((GuiScreen)null);
            } else if (button.id == 0) {
                PacketBuffer packetbuffer = new PacketBuffer(Unpooled.buffer());
                packetbuffer.writeByte(this.localCommandBlock.func_145751_f());
                this.localCommandBlock.func_145757_a((ByteBuf)packetbuffer);
                packetbuffer.writeString(this.commandTextField.getText());
                packetbuffer.writeBoolean(this.localCommandBlock.shouldTrackOutput());
                this.mc.getNetHandler().addToSendQueue((Packet)new C17PacketCustomPayload("MC|AdvCdm", packetbuffer));
                if (!this.localCommandBlock.shouldTrackOutput()) {
                    this.localCommandBlock.setLastOutput((IChatComponent)null);
                }
                this.mc.displayGuiScreen((GuiScreen)null);
            } else if (button.id == 4) {
                this.localCommandBlock.setTrackOutput(!this.localCommandBlock.shouldTrackOutput());
                this.func_175388_a();
            }
        }
    }

    protected void keyTyped(char typedChar, int keyCode) throws IOException {
        this.commandTextField.textboxKeyTyped(typedChar, keyCode);
        this.previousOutputTextField.textboxKeyTyped(typedChar, keyCode);
        boolean bl = this.doneBtn.enabled = this.commandTextField.getText().trim().length() > 0;
        if (keyCode != 28 && keyCode != 156) {
            if (keyCode == 1) {
                this.actionPerformed(this.cancelBtn);
            }
        } else {
            this.actionPerformed(this.doneBtn);
        }
    }

    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        this.commandTextField.mouseClicked(mouseX, mouseY, mouseButton);
        this.previousOutputTextField.mouseClicked(mouseX, mouseY, mouseButton);
    }

    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        this.drawDefaultBackground();
        this.drawCenteredString(this.fontRendererObj, I18n.format((String)"advMode.setCommand", (Object[])new Object[0]), width / 2, 20, 0xFFFFFF);
        this.drawString(this.fontRendererObj, I18n.format((String)"advMode.command", (Object[])new Object[0]), width / 2 - 150, 37, 0xA0A0A0);
        this.commandTextField.drawTextBox();
        int i = 75;
        int j = 0;
        int n = j++;
        this.drawString(this.fontRendererObj, I18n.format((String)"advMode.nearestPlayer", (Object[])new Object[0]), width / 2 - 150, i + n * FontRenderer.FONT_HEIGHT, 0xA0A0A0);
        int n2 = j++;
        this.drawString(this.fontRendererObj, I18n.format((String)"advMode.randomPlayer", (Object[])new Object[0]), width / 2 - 150, i + n2 * FontRenderer.FONT_HEIGHT, 0xA0A0A0);
        int n3 = j++;
        this.drawString(this.fontRendererObj, I18n.format((String)"advMode.allPlayers", (Object[])new Object[0]), width / 2 - 150, i + n3 * FontRenderer.FONT_HEIGHT, 0xA0A0A0);
        int n4 = j++;
        this.drawString(this.fontRendererObj, I18n.format((String)"advMode.allEntities", (Object[])new Object[0]), width / 2 - 150, i + n4 * FontRenderer.FONT_HEIGHT, 0xA0A0A0);
        int n5 = j++;
        this.drawString(this.fontRendererObj, "", width / 2 - 150, i + n5 * FontRenderer.FONT_HEIGHT, 0xA0A0A0);
        if (this.previousOutputTextField.getText().length() > 0) {
            i = i + j * FontRenderer.FONT_HEIGHT + 16;
            this.drawString(this.fontRendererObj, I18n.format((String)"advMode.previousOutput", (Object[])new Object[0]), width / 2 - 150, i, 0xA0A0A0);
            this.previousOutputTextField.drawTextBox();
        }
        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    private void func_175388_a() {
        if (this.localCommandBlock.shouldTrackOutput()) {
            this.field_175390_s.displayString = "O";
            if (this.localCommandBlock.getLastOutput() != null) {
                this.previousOutputTextField.setText(this.localCommandBlock.getLastOutput().getUnformattedText());
            }
        } else {
            this.field_175390_s.displayString = "X";
            this.previousOutputTextField.setText("-");
        }
    }
}
