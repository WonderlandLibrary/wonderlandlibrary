/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  io.netty.buffer.Unpooled
 */
package net.minecraft.client.gui.inventory;

import io.netty.buffer.Unpooled;
import java.io.IOException;
import javax.annotation.Nullable;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.resources.I18n;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.client.CPacketCustomPayload;
import net.minecraft.tileentity.CommandBlockBaseLogic;
import net.minecraft.util.ITabCompleter;
import net.minecraft.util.TabCompleter;
import net.minecraft.util.math.BlockPos;
import org.lwjgl.input.Keyboard;

public class GuiEditCommandBlockMinecart
extends GuiScreen
implements ITabCompleter {
    private GuiTextField commandField;
    private GuiTextField previousEdit;
    private final CommandBlockBaseLogic commandBlockLogic;
    private GuiButton doneButton;
    private GuiButton cancelButton;
    private GuiButton outputButton;
    private boolean trackOutput;
    private TabCompleter tabCompleter;

    public GuiEditCommandBlockMinecart(CommandBlockBaseLogic p_i46595_1_) {
        this.commandBlockLogic = p_i46595_1_;
    }

    @Override
    public void updateScreen() {
        this.commandField.updateCursorCounter();
    }

    @Override
    public void initGui() {
        Keyboard.enableRepeatEvents(true);
        this.buttonList.clear();
        this.doneButton = this.addButton(new GuiButton(0, this.width / 2 - 4 - 150, this.height / 4 + 120 + 12, 150, 20, I18n.format("gui.done", new Object[0])));
        this.cancelButton = this.addButton(new GuiButton(1, this.width / 2 + 4, this.height / 4 + 120 + 12, 150, 20, I18n.format("gui.cancel", new Object[0])));
        this.outputButton = this.addButton(new GuiButton(4, this.width / 2 + 150 - 20, 150, 20, 20, "O"));
        this.commandField = new GuiTextField(2, this.fontRendererObj, this.width / 2 - 150, 50, 300, 20);
        this.commandField.setMaxStringLength(32500);
        this.commandField.setFocused(true);
        this.commandField.setText(this.commandBlockLogic.getCommand());
        this.previousEdit = new GuiTextField(3, this.fontRendererObj, this.width / 2 - 150, 150, 276, 20);
        this.previousEdit.setMaxStringLength(32500);
        this.previousEdit.setEnabled(false);
        this.previousEdit.setText("-");
        this.trackOutput = this.commandBlockLogic.shouldTrackOutput();
        this.updateCommandOutput();
        this.doneButton.enabled = !this.commandField.getText().trim().isEmpty();
        this.tabCompleter = new TabCompleter(this.commandField, true){

            @Override
            @Nullable
            public BlockPos getTargetBlockPos() {
                return GuiEditCommandBlockMinecart.this.commandBlockLogic.getPosition();
            }
        };
    }

    @Override
    public void onGuiClosed() {
        Keyboard.enableRepeatEvents(false);
    }

    @Override
    protected void actionPerformed(GuiButton button) throws IOException {
        if (button.enabled) {
            if (button.id == 1) {
                this.commandBlockLogic.setTrackOutput(this.trackOutput);
                this.mc.displayGuiScreen(null);
            } else if (button.id == 0) {
                PacketBuffer packetbuffer = new PacketBuffer(Unpooled.buffer());
                packetbuffer.writeByte(this.commandBlockLogic.getCommandBlockType());
                this.commandBlockLogic.fillInInfo(packetbuffer);
                packetbuffer.writeString(this.commandField.getText());
                packetbuffer.writeBoolean(this.commandBlockLogic.shouldTrackOutput());
                this.mc.getConnection().sendPacket(new CPacketCustomPayload("MC|AdvCmd", packetbuffer));
                if (!this.commandBlockLogic.shouldTrackOutput()) {
                    this.commandBlockLogic.setLastOutput(null);
                }
                this.mc.displayGuiScreen(null);
            } else if (button.id == 4) {
                this.commandBlockLogic.setTrackOutput(!this.commandBlockLogic.shouldTrackOutput());
                this.updateCommandOutput();
            }
        }
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException {
        this.tabCompleter.resetRequested();
        if (keyCode == 15) {
            this.tabCompleter.complete();
        } else {
            this.tabCompleter.resetDidComplete();
        }
        this.commandField.textboxKeyTyped(typedChar, keyCode);
        this.previousEdit.textboxKeyTyped(typedChar, keyCode);
        boolean bl = this.doneButton.enabled = !this.commandField.getText().trim().isEmpty();
        if (keyCode != 28 && keyCode != 156) {
            if (keyCode == 1) {
                this.actionPerformed(this.cancelButton);
            }
        } else {
            this.actionPerformed(this.doneButton);
        }
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        this.commandField.mouseClicked(mouseX, mouseY, mouseButton);
        this.previousEdit.mouseClicked(mouseX, mouseY, mouseButton);
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        this.drawDefaultBackground();
        this.drawCenteredString(this.fontRendererObj, I18n.format("advMode.setCommand", new Object[0]), this.width / 2, 20, 0xFFFFFF);
        this.drawString(this.fontRendererObj, I18n.format("advMode.command", new Object[0]), this.width / 2 - 150, 40, 0xA0A0A0);
        this.commandField.drawTextBox();
        int i = 75;
        int j = 0;
        this.drawString(this.fontRendererObj, I18n.format("advMode.nearestPlayer", new Object[0]), this.width / 2 - 140, i + j++ * this.fontRendererObj.FONT_HEIGHT, 0xA0A0A0);
        this.drawString(this.fontRendererObj, I18n.format("advMode.randomPlayer", new Object[0]), this.width / 2 - 140, i + j++ * this.fontRendererObj.FONT_HEIGHT, 0xA0A0A0);
        this.drawString(this.fontRendererObj, I18n.format("advMode.allPlayers", new Object[0]), this.width / 2 - 140, i + j++ * this.fontRendererObj.FONT_HEIGHT, 0xA0A0A0);
        this.drawString(this.fontRendererObj, I18n.format("advMode.allEntities", new Object[0]), this.width / 2 - 140, i + j++ * this.fontRendererObj.FONT_HEIGHT, 0xA0A0A0);
        this.drawString(this.fontRendererObj, I18n.format("advMode.self", new Object[0]), this.width / 2 - 140, i + j++ * this.fontRendererObj.FONT_HEIGHT, 0xA0A0A0);
        if (!this.previousEdit.getText().isEmpty()) {
            i = i + j * this.fontRendererObj.FONT_HEIGHT + 20;
            this.drawString(this.fontRendererObj, I18n.format("advMode.previousOutput", new Object[0]), this.width / 2 - 150, i, 0xA0A0A0);
            this.previousEdit.drawTextBox();
        }
        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    private void updateCommandOutput() {
        if (this.commandBlockLogic.shouldTrackOutput()) {
            this.outputButton.displayString = "O";
            if (this.commandBlockLogic.getLastOutput() != null) {
                this.previousEdit.setText(this.commandBlockLogic.getLastOutput().getUnformattedText());
            }
        } else {
            this.outputButton.displayString = "X";
            this.previousEdit.setText("-");
        }
    }

    @Override
    public void setCompletions(String ... newCompletions) {
        this.tabCompleter.setCompletions(newCompletions);
    }
}

