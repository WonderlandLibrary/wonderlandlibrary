// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.client.gui.inventory;

import net.minecraft.block.Block;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.init.Blocks;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatAllowedCharacters;
import java.io.IOException;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C12PacketUpdateSign;
import net.minecraft.client.resources.I18n;
import org.lwjgl.input.Keyboard;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.tileentity.TileEntitySign;
import net.minecraft.client.gui.GuiScreen;

public class GuiEditSign extends GuiScreen
{
    private TileEntitySign tileSign;
    private int updateCounter;
    private int editLine;
    private GuiButton doneBtn;
    private static final String __OBFID = "CL_00000764";
    
    public GuiEditSign(final TileEntitySign p_i1097_1_) {
        this.tileSign = p_i1097_1_;
    }
    
    @Override
    public void initGui() {
        this.buttonList.clear();
        Keyboard.enableRepeatEvents(true);
        this.buttonList.add(this.doneBtn = new GuiButton(0, this.width / 2 - 100, this.height / 4 + 120, I18n.format("gui.done", new Object[0])));
        this.tileSign.setEditable(false);
    }
    
    @Override
    public void onGuiClosed() {
        Keyboard.enableRepeatEvents(false);
        final NetHandlerPlayClient var1 = this.mc.getNetHandler();
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
    protected void actionPerformed(final GuiButton button) throws IOException {
        if (button.enabled && button.id == 0) {
            this.tileSign.markDirty();
            this.mc.displayGuiScreen(null);
        }
    }
    
    @Override
    protected void keyTyped(final char typedChar, final int keyCode) throws IOException {
        if (keyCode == 200) {
            this.editLine = (this.editLine - 1 & 0x3);
        }
        if (keyCode == 208 || keyCode == 28 || keyCode == 156) {
            this.editLine = (this.editLine + 1 & 0x3);
        }
        String var3 = this.tileSign.signText[this.editLine].getUnformattedText();
        if (keyCode == 14 && var3.length() > 0) {
            var3 = var3.substring(0, var3.length() - 1);
        }
        if (ChatAllowedCharacters.isAllowedCharacter(typedChar) && this.fontRendererObj.getStringWidth(var3 + typedChar) <= 90) {
            var3 += typedChar;
        }
        this.tileSign.signText[this.editLine] = new ChatComponentText(var3);
        if (keyCode == 1) {
            this.actionPerformed(this.doneBtn);
        }
    }
    
    @Override
    public void drawScreen(final int mouseX, final int mouseY, final float partialTicks) {
        this.drawDefaultBackground();
        this.drawCenteredString(this.fontRendererObj, I18n.format("sign.edit", new Object[0]), this.width / 2, 40, 16777215);
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
        GlStateManager.pushMatrix();
        GlStateManager.translate(this.width / 2, 0.0f, 50.0f);
        final float var4 = 93.75f;
        GlStateManager.scale(-var4, -var4, -var4);
        GlStateManager.rotate(180.0f, 0.0f, 1.0f, 0.0f);
        final Block var5 = this.tileSign.getBlockType();
        if (var5 == Blocks.standing_sign) {
            final float var6 = this.tileSign.getBlockMetadata() * 360 / 16.0f;
            GlStateManager.rotate(var6, 0.0f, 1.0f, 0.0f);
            GlStateManager.translate(0.0f, -1.0625f, 0.0f);
        }
        else {
            final int var7 = this.tileSign.getBlockMetadata();
            float var8 = 0.0f;
            if (var7 == 2) {
                var8 = 180.0f;
            }
            if (var7 == 4) {
                var8 = 90.0f;
            }
            if (var7 == 5) {
                var8 = -90.0f;
            }
            GlStateManager.rotate(var8, 0.0f, 1.0f, 0.0f);
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
