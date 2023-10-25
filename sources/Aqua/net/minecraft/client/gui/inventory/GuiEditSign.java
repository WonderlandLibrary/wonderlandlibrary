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
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C12PacketUpdateSign;
import net.minecraft.tileentity.TileEntity;
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

    public GuiEditSign(TileEntitySign teSign) {
        this.tileSign = teSign;
    }

    public void initGui() {
        this.buttonList.clear();
        Keyboard.enableRepeatEvents((boolean)true);
        this.doneBtn = new GuiButton(0, width / 2 - 100, height / 4 + 120, I18n.format((String)"gui.done", (Object[])new Object[0]));
        this.buttonList.add((Object)this.doneBtn);
        this.tileSign.setEditable(false);
    }

    public void onGuiClosed() {
        Keyboard.enableRepeatEvents((boolean)false);
        NetHandlerPlayClient nethandlerplayclient = this.mc.getNetHandler();
        if (nethandlerplayclient != null) {
            nethandlerplayclient.addToSendQueue((Packet)new C12PacketUpdateSign(this.tileSign.getPos(), this.tileSign.signText));
        }
        this.tileSign.setEditable(true);
    }

    public void updateScreen() {
        ++this.updateCounter;
    }

    protected void actionPerformed(GuiButton button) throws IOException {
        if (button.enabled && button.id == 0) {
            this.tileSign.markDirty();
            this.mc.displayGuiScreen((GuiScreen)null);
        }
    }

    protected void keyTyped(char typedChar, int keyCode) throws IOException {
        if (keyCode == 200) {
            this.editLine = this.editLine - 1 & 3;
        }
        if (keyCode == 208 || keyCode == 28 || keyCode == 156) {
            this.editLine = this.editLine + 1 & 3;
        }
        String s = this.tileSign.signText[this.editLine].getUnformattedText();
        if (keyCode == 14 && s.length() > 0) {
            s = s.substring(0, s.length() - 1);
        }
        if (ChatAllowedCharacters.isAllowedCharacter((char)typedChar) && this.fontRendererObj.getStringWidth(s + typedChar) <= 90) {
            s = s + typedChar;
        }
        this.tileSign.signText[this.editLine] = new ChatComponentText(s);
        if (keyCode == 1) {
            this.actionPerformed(this.doneBtn);
        }
    }

    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        this.drawDefaultBackground();
        this.drawCenteredString(this.fontRendererObj, I18n.format((String)"sign.edit", (Object[])new Object[0]), width / 2, 40, 0xFFFFFF);
        GlStateManager.color((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
        GlStateManager.pushMatrix();
        GlStateManager.translate((float)(width / 2), (float)0.0f, (float)50.0f);
        float f = 93.75f;
        GlStateManager.scale((float)(-f), (float)(-f), (float)(-f));
        GlStateManager.rotate((float)180.0f, (float)0.0f, (float)1.0f, (float)0.0f);
        Block block = this.tileSign.getBlockType();
        if (block == Blocks.standing_sign) {
            float f1 = (float)(this.tileSign.getBlockMetadata() * 360) / 16.0f;
            GlStateManager.rotate((float)f1, (float)0.0f, (float)1.0f, (float)0.0f);
            GlStateManager.translate((float)0.0f, (float)-1.0625f, (float)0.0f);
        } else {
            int i = this.tileSign.getBlockMetadata();
            float f2 = 0.0f;
            if (i == 2) {
                f2 = 180.0f;
            }
            if (i == 4) {
                f2 = 90.0f;
            }
            if (i == 5) {
                f2 = -90.0f;
            }
            GlStateManager.rotate((float)f2, (float)0.0f, (float)1.0f, (float)0.0f);
            GlStateManager.translate((float)0.0f, (float)-1.0625f, (float)0.0f);
        }
        if (this.updateCounter / 6 % 2 == 0) {
            this.tileSign.lineBeingEdited = this.editLine;
        }
        TileEntityRendererDispatcher.instance.renderTileEntityAt((TileEntity)this.tileSign, -0.5, -0.75, -0.5, 0.0f);
        this.tileSign.lineBeingEdited = -1;
        GlStateManager.popMatrix();
        super.drawScreen(mouseX, mouseY, partialTicks);
    }
}
