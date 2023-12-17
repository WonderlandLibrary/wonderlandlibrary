// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.client.gui.inventory;

import java.io.IOException;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketCustomPayload;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.PacketBuffer;
import io.netty.buffer.Unpooled;
import javax.annotation.Nullable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.client.resources.I18n;
import org.lwjgl.input.Keyboard;
import net.minecraft.util.TabCompleter;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.tileentity.CommandBlockBaseLogic;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.util.ITabCompleter;
import net.minecraft.client.gui.GuiScreen;

public class GuiEditCommandBlockMinecart extends GuiScreen implements ITabCompleter
{
    private GuiTextField commandField;
    private GuiTextField previousEdit;
    private final CommandBlockBaseLogic commandBlockLogic;
    private GuiButton doneButton;
    private GuiButton cancelButton;
    private GuiButton outputButton;
    private boolean trackOutput;
    private TabCompleter tabCompleter;
    
    public GuiEditCommandBlockMinecart(final CommandBlockBaseLogic p_i46595_1_) {
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
        (this.commandField = new GuiTextField(2, this.fontRenderer, this.width / 2 - 150, 50, 300, 20)).setMaxStringLength(32500);
        this.commandField.setFocused(true);
        this.commandField.setText(this.commandBlockLogic.getCommand());
        (this.previousEdit = new GuiTextField(3, this.fontRenderer, this.width / 2 - 150, 150, 276, 20)).setMaxStringLength(32500);
        this.previousEdit.setEnabled(false);
        this.previousEdit.setText("-");
        this.trackOutput = this.commandBlockLogic.shouldTrackOutput();
        this.updateCommandOutput();
        this.doneButton.enabled = !this.commandField.getText().trim().isEmpty();
        this.tabCompleter = new TabCompleter(this.commandField, true) {
            @Nullable
            @Override
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
    protected void actionPerformed(final GuiButton button) throws IOException {
        if (button.enabled) {
            if (button.id == 1) {
                this.commandBlockLogic.setTrackOutput(this.trackOutput);
                GuiEditCommandBlockMinecart.mc.displayGuiScreen(null);
            }
            else if (button.id == 0) {
                final PacketBuffer packetbuffer = new PacketBuffer(Unpooled.buffer());
                packetbuffer.writeByte(this.commandBlockLogic.getCommandBlockType());
                this.commandBlockLogic.fillInInfo(packetbuffer);
                packetbuffer.writeString(this.commandField.getText());
                packetbuffer.writeBoolean(this.commandBlockLogic.shouldTrackOutput());
                GuiEditCommandBlockMinecart.mc.getConnection().sendPacket(new CPacketCustomPayload("MC|AdvCmd", packetbuffer));
                if (!this.commandBlockLogic.shouldTrackOutput()) {
                    this.commandBlockLogic.setLastOutput(null);
                }
                GuiEditCommandBlockMinecart.mc.displayGuiScreen(null);
            }
            else if (button.id == 4) {
                this.commandBlockLogic.setTrackOutput(!this.commandBlockLogic.shouldTrackOutput());
                this.updateCommandOutput();
            }
        }
    }
    
    @Override
    protected void keyTyped(final char typedChar, final int keyCode) throws IOException {
        this.tabCompleter.resetRequested();
        if (keyCode == 15) {
            this.tabCompleter.complete();
        }
        else {
            this.tabCompleter.resetDidComplete();
        }
        this.commandField.textboxKeyTyped(typedChar, keyCode);
        this.previousEdit.textboxKeyTyped(typedChar, keyCode);
        this.doneButton.enabled = !this.commandField.getText().trim().isEmpty();
        if (keyCode != 28 && keyCode != 156) {
            if (keyCode == 1) {
                this.actionPerformed(this.cancelButton);
            }
        }
        else {
            this.actionPerformed(this.doneButton);
        }
    }
    
    @Override
    protected void mouseClicked(final int mouseX, final int mouseY, final int mouseButton) throws IOException {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        this.commandField.mouseClicked(mouseX, mouseY, mouseButton);
        this.previousEdit.mouseClicked(mouseX, mouseY, mouseButton);
    }
    
    @Override
    public void drawScreen(final int mouseX, final int mouseY, final float partialTicks) {
        this.drawDefaultBackground();
        this.drawCenteredString(this.fontRenderer, I18n.format("advMode.setCommand", new Object[0]), this.width / 2, 20, 16777215);
        this.drawString(this.fontRenderer, I18n.format("advMode.command", new Object[0]), this.width / 2 - 150, 40, 10526880);
        this.commandField.drawTextBox();
        int i = 75;
        int j = 0;
        this.drawString(this.fontRenderer, I18n.format("advMode.nearestPlayer", new Object[0]), this.width / 2 - 140, i + j++ * this.fontRenderer.FONT_HEIGHT, 10526880);
        this.drawString(this.fontRenderer, I18n.format("advMode.randomPlayer", new Object[0]), this.width / 2 - 140, i + j++ * this.fontRenderer.FONT_HEIGHT, 10526880);
        this.drawString(this.fontRenderer, I18n.format("advMode.allPlayers", new Object[0]), this.width / 2 - 140, i + j++ * this.fontRenderer.FONT_HEIGHT, 10526880);
        this.drawString(this.fontRenderer, I18n.format("advMode.allEntities", new Object[0]), this.width / 2 - 140, i + j++ * this.fontRenderer.FONT_HEIGHT, 10526880);
        this.drawString(this.fontRenderer, I18n.format("advMode.self", new Object[0]), this.width / 2 - 140, i + j++ * this.fontRenderer.FONT_HEIGHT, 10526880);
        if (!this.previousEdit.getText().isEmpty()) {
            i = i + j * this.fontRenderer.FONT_HEIGHT + 20;
            this.drawString(this.fontRenderer, I18n.format("advMode.previousOutput", new Object[0]), this.width / 2 - 150, i, 10526880);
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
        }
        else {
            this.outputButton.displayString = "X";
            this.previousEdit.setText("-");
        }
    }
    
    @Override
    public void setCompletions(final String... newCompletions) {
        this.tabCompleter.setCompletions(newCompletions);
    }
}
