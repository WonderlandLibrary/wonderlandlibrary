/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.lwjgl.input.Keyboard
 */
package net.minecraft.client.gui.inventory;

import java.io.IOException;
import net.minecraft.block.Block;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.client.resources.I18n;
import net.minecraft.init.Blocks;
import net.minecraft.network.play.client.C12PacketUpdateSign;
import net.minecraft.tileentity.TileEntitySign;
import net.minecraft.util.ChatAllowedCharacters;
import net.minecraft.util.ChatComponentText;
import org.lwjgl.input.Keyboard;

public class GuiEditSign
extends GuiScreen {
    private GuiButton doneBtn;
    private TileEntitySign tileSign;
    private int editLine;
    private int updateCounter;

    public GuiEditSign(TileEntitySign tileEntitySign) {
        this.tileSign = tileEntitySign;
    }

    @Override
    public void updateScreen() {
        ++this.updateCounter;
    }

    @Override
    protected void actionPerformed(GuiButton guiButton) throws IOException {
        if (guiButton.enabled && guiButton.id == 0) {
            this.tileSign.markDirty();
            this.mc.displayGuiScreen(null);
        }
    }

    @Override
    protected void keyTyped(char c, int n) throws IOException {
        if (n == 200) {
            this.editLine = this.editLine - 1 & 3;
        }
        if (n == 208 || n == 28 || n == 156) {
            this.editLine = this.editLine + 1 & 3;
        }
        String string = this.tileSign.signText[this.editLine].getUnformattedText();
        if (n == 14 && string.length() > 0) {
            string = string.substring(0, string.length() - 1);
        }
        if (ChatAllowedCharacters.isAllowedCharacter(c) && this.fontRendererObj.getStringWidth(String.valueOf(string) + c) <= 90) {
            string = String.valueOf(string) + c;
        }
        this.tileSign.signText[this.editLine] = new ChatComponentText(string);
        if (n == 1) {
            this.actionPerformed(this.doneBtn);
        }
    }

    @Override
    public void drawScreen(int n, int n2, float f) {
        this.drawDefaultBackground();
        this.drawCenteredString(this.fontRendererObj, I18n.format("sign.edit", new Object[0]), width / 2, 40, 0xFFFFFF);
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
        GlStateManager.pushMatrix();
        GlStateManager.translate(width / 2, 0.0f, 50.0f);
        float f2 = 93.75f;
        GlStateManager.scale(-f2, -f2, -f2);
        GlStateManager.rotate(180.0f, 0.0f, 1.0f, 0.0f);
        Block block = this.tileSign.getBlockType();
        if (block == Blocks.standing_sign) {
            float f3 = (float)(this.tileSign.getBlockMetadata() * 360) / 16.0f;
            GlStateManager.rotate(f3, 0.0f, 1.0f, 0.0f);
            GlStateManager.translate(0.0f, -1.0625f, 0.0f);
        } else {
            int n3 = this.tileSign.getBlockMetadata();
            float f4 = 0.0f;
            if (n3 == 2) {
                f4 = 180.0f;
            }
            if (n3 == 4) {
                f4 = 90.0f;
            }
            if (n3 == 5) {
                f4 = -90.0f;
            }
            GlStateManager.rotate(f4, 0.0f, 1.0f, 0.0f);
            GlStateManager.translate(0.0f, -1.0625f, 0.0f);
        }
        if (this.updateCounter / 6 % 2 == 0) {
            this.tileSign.lineBeingEdited = this.editLine;
        }
        TileEntityRendererDispatcher.instance.renderTileEntityAt(this.tileSign, -0.5, -0.75, -0.5, 0.0f);
        this.tileSign.lineBeingEdited = -1;
        GlStateManager.popMatrix();
        super.drawScreen(n, n2, f);
    }

    @Override
    public void initGui() {
        this.buttonList.clear();
        Keyboard.enableRepeatEvents((boolean)true);
        this.doneBtn = new GuiButton(0, width / 2 - 100, height / 4 + 120, I18n.format("gui.done", new Object[0]));
        this.buttonList.add(this.doneBtn);
        this.tileSign.setEditable(false);
    }

    @Override
    public void onGuiClosed() {
        Keyboard.enableRepeatEvents((boolean)false);
        NetHandlerPlayClient netHandlerPlayClient = this.mc.getNetHandler();
        if (netHandlerPlayClient != null) {
            netHandlerPlayClient.addToSendQueue(new C12PacketUpdateSign(this.tileSign.getPos(), this.tileSign.signText));
        }
        this.tileSign.setEditable(true);
    }
}

