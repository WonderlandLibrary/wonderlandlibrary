/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.apache.logging.log4j.LogManager
 *  org.apache.logging.log4j.Logger
 *  org.lwjgl.input.Keyboard
 */
package net.minecraft.client.gui;

import io.netty.buffer.Unpooled;
import java.io.IOException;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.resources.I18n;
import net.minecraft.command.server.CommandBlockLogic;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.client.C17PacketCustomPayload;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.input.Keyboard;

public class GuiCommandBlock
extends GuiScreen {
    private GuiButton field_175390_s;
    private GuiTextField previousOutputTextField;
    private final CommandBlockLogic localCommandBlock;
    private GuiTextField commandTextField;
    private GuiButton doneBtn;
    private boolean field_175389_t;
    private static final Logger field_146488_a = LogManager.getLogger();
    private GuiButton cancelBtn;

    @Override
    public void updateScreen() {
        this.commandTextField.updateCursorCounter();
    }

    @Override
    public void drawScreen(int n, int n2, float f) {
        this.drawDefaultBackground();
        this.drawCenteredString(this.fontRendererObj, I18n.format("advMode.setCommand", new Object[0]), width / 2, 20, 0xFFFFFF);
        this.drawString(this.fontRendererObj, I18n.format("advMode.command", new Object[0]), width / 2 - 150, 37, 0xA0A0A0);
        this.commandTextField.drawTextBox();
        int n3 = 75;
        int n4 = 0;
        this.drawString(this.fontRendererObj, I18n.format("advMode.nearestPlayer", new Object[0]), width / 2 - 150, n3 + n4++ * this.fontRendererObj.FONT_HEIGHT, 0xA0A0A0);
        this.drawString(this.fontRendererObj, I18n.format("advMode.randomPlayer", new Object[0]), width / 2 - 150, n3 + n4++ * this.fontRendererObj.FONT_HEIGHT, 0xA0A0A0);
        this.drawString(this.fontRendererObj, I18n.format("advMode.allPlayers", new Object[0]), width / 2 - 150, n3 + n4++ * this.fontRendererObj.FONT_HEIGHT, 0xA0A0A0);
        this.drawString(this.fontRendererObj, I18n.format("advMode.allEntities", new Object[0]), width / 2 - 150, n3 + n4++ * this.fontRendererObj.FONT_HEIGHT, 0xA0A0A0);
        this.drawString(this.fontRendererObj, "", width / 2 - 150, n3 + n4++ * this.fontRendererObj.FONT_HEIGHT, 0xA0A0A0);
        if (this.previousOutputTextField.getText().length() > 0) {
            n3 = n3 + n4 * this.fontRendererObj.FONT_HEIGHT + 16;
            this.drawString(this.fontRendererObj, I18n.format("advMode.previousOutput", new Object[0]), width / 2 - 150, n3, 0xA0A0A0);
            this.previousOutputTextField.drawTextBox();
        }
        super.drawScreen(n, n2, f);
    }

    @Override
    protected void keyTyped(char c, int n) throws IOException {
        this.commandTextField.textboxKeyTyped(c, n);
        this.previousOutputTextField.textboxKeyTyped(c, n);
        boolean bl = this.doneBtn.enabled = this.commandTextField.getText().trim().length() > 0;
        if (n != 28 && n != 156) {
            if (n == 1) {
                this.actionPerformed(this.cancelBtn);
            }
        } else {
            this.actionPerformed(this.doneBtn);
        }
    }

    @Override
    protected void actionPerformed(GuiButton guiButton) throws IOException {
        if (guiButton.enabled) {
            if (guiButton.id == 1) {
                this.localCommandBlock.setTrackOutput(this.field_175389_t);
                this.mc.displayGuiScreen(null);
            } else if (guiButton.id == 0) {
                PacketBuffer packetBuffer = new PacketBuffer(Unpooled.buffer());
                packetBuffer.writeByte(this.localCommandBlock.func_145751_f());
                this.localCommandBlock.func_145757_a(packetBuffer);
                packetBuffer.writeString(this.commandTextField.getText());
                packetBuffer.writeBoolean(this.localCommandBlock.shouldTrackOutput());
                this.mc.getNetHandler().addToSendQueue(new C17PacketCustomPayload("MC|AdvCdm", packetBuffer));
                if (!this.localCommandBlock.shouldTrackOutput()) {
                    this.localCommandBlock.setLastOutput(null);
                }
                this.mc.displayGuiScreen(null);
            } else if (guiButton.id == 4) {
                this.localCommandBlock.setTrackOutput(!this.localCommandBlock.shouldTrackOutput());
                this.func_175388_a();
            }
        }
    }

    @Override
    public void initGui() {
        Keyboard.enableRepeatEvents((boolean)true);
        this.buttonList.clear();
        this.doneBtn = new GuiButton(0, width / 2 - 4 - 150, height / 4 + 120 + 12, 150, 20, I18n.format("gui.done", new Object[0]));
        this.buttonList.add(this.doneBtn);
        this.cancelBtn = new GuiButton(1, width / 2 + 4, height / 4 + 120 + 12, 150, 20, I18n.format("gui.cancel", new Object[0]));
        this.buttonList.add(this.cancelBtn);
        this.field_175390_s = new GuiButton(4, width / 2 + 150 - 20, 150, 20, 20, "O");
        this.buttonList.add(this.field_175390_s);
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

    public GuiCommandBlock(CommandBlockLogic commandBlockLogic) {
        this.localCommandBlock = commandBlockLogic;
    }

    @Override
    public void onGuiClosed() {
        Keyboard.enableRepeatEvents((boolean)false);
    }

    @Override
    protected void mouseClicked(int n, int n2, int n3) throws IOException {
        super.mouseClicked(n, n2, n3);
        this.commandTextField.mouseClicked(n, n2, n3);
        this.previousOutputTextField.mouseClicked(n, n2, n3);
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

