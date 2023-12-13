/*
 * Decompiled with CFR 0.150.
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
    private TileEntitySign tileSign;
    private int updateCounter;
    private int editLine;
    private GuiButton doneBtn;
    private static final String __OBFID = "CL_00000764";

    public GuiEditSign(TileEntitySign p_i1097_1_) {
        this.tileSign = p_i1097_1_;
    }

    @Override
    public void initGui() {
        this.buttonList.clear();
        Keyboard.enableRepeatEvents((boolean)true);
        this.doneBtn = new GuiButton(0, this.width / 2 - 100, this.height / 4 + 120, I18n.format("gui.done", new Object[0]));
        this.buttonList.add(this.doneBtn);
        this.tileSign.setEditable(false);
    }

    @Override
    public void onGuiClosed() {
        Keyboard.enableRepeatEvents((boolean)false);
        NetHandlerPlayClient var1 = this.mc.getNetHandler();
        if (var1 != null) {
            var1.addToSendQueue(new C12PacketUpdateSign(this.tileSign.getPos(), this.tileSign.signText));
        }
        this.tileSign.setEditable(true);
    }

    @Override
    public void updateScreen() {
        ++this.updateCounter;
    }

    @Override
    protected void actionPerformed(GuiButton button) throws IOException {
        if (button.enabled && button.id == 0) {
            this.tileSign.markDirty();
            this.mc.displayGuiScreen(null);
        }
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException {
        if (keyCode == 200) {
            this.editLine = this.editLine - 1 & 3;
        }
        if (keyCode == 208 || keyCode == 28 || keyCode == 156) {
            this.editLine = this.editLine + 1 & 3;
        }
        String var3 = this.tileSign.signText[this.editLine].getUnformattedText();
        if (keyCode == 14 && var3.length() > 0) {
            var3 = var3.substring(0, var3.length() - 1);
        }
        if (ChatAllowedCharacters.isAllowedCharacter(typedChar) && this.fontRendererObj.getStringWidth(String.valueOf(var3) + typedChar) <= 90) {
            var3 = String.valueOf(var3) + typedChar;
        }
        this.tileSign.signText[this.editLine] = new ChatComponentText(var3);
        if (keyCode == 1) {
            this.actionPerformed(this.doneBtn);
        }
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        this.drawDefaultBackground();
        this.drawCenteredString(this.fontRendererObj, I18n.format("sign.edit", new Object[0]), this.width / 2, 40.0f, 0xFFFFFF);
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
        GlStateManager.pushMatrix();
        GlStateManager.translate(this.width / 2, 0.0f, 50.0f);
        float var4 = 93.75f;
        GlStateManager.scale(-var4, -var4, -var4);
        GlStateManager.rotate(180.0f, 0.0f, 1.0f, 0.0f);
        Block var5 = this.tileSign.getBlockType();
        if (var5 == Blocks.standing_sign) {
            float var6 = (float)(this.tileSign.getBlockMetadata() * 360) / 16.0f;
            GlStateManager.rotate(var6, 0.0f, 1.0f, 0.0f);
            GlStateManager.translate(0.0f, -1.0625f, 0.0f);
        } else {
            int var8 = this.tileSign.getBlockMetadata();
            float var7 = 0.0f;
            if (var8 == 2) {
                var7 = 180.0f;
            }
            if (var8 == 4) {
                var7 = 90.0f;
            }
            if (var8 == 5) {
                var7 = -90.0f;
            }
            GlStateManager.rotate(var7, 0.0f, 1.0f, 0.0f);
            GlStateManager.translate(0.0f, -1.0625f, 0.0f);
        }
        if (this.updateCounter / 6 % 2 == 0) {
            this.tileSign.lineBeingEdited = this.editLine;
        }
        TileEntityRendererDispatcher.instance.renderTileEntityAt(this.tileSign, -0.5, -0.75, -0.5, 0.0f);
        this.tileSign.lineBeingEdited = -1;
        GlStateManager.popMatrix();
        super.drawScreen(mouseX, mouseY, partialTicks);
    }
}

