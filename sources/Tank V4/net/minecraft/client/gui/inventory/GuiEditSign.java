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

public class GuiEditSign extends GuiScreen {
   private int editLine;
   private GuiButton doneBtn;
   private int updateCounter;
   private TileEntitySign tileSign;

   public void onGuiClosed() {
      Keyboard.enableRepeatEvents(false);
      NetHandlerPlayClient var1 = this.mc.getNetHandler();
      if (var1 != null) {
         var1.addToSendQueue(new C12PacketUpdateSign(this.tileSign.getPos(), this.tileSign.signText));
      }

      this.tileSign.setEditable(true);
   }

   public void initGui() {
      this.buttonList.clear();
      Keyboard.enableRepeatEvents(true);
      this.buttonList.add(this.doneBtn = new GuiButton(0, width / 2 - 100, height / 4 + 120, I18n.format("gui.done")));
      this.tileSign.setEditable(false);
   }

   public void drawScreen(int var1, int var2, float var3) {
      this.drawDefaultBackground();
      this.drawCenteredString(this.fontRendererObj, I18n.format("sign.edit"), width / 2, 40, 16777215);
      GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
      GlStateManager.pushMatrix();
      GlStateManager.translate((float)(width / 2), 0.0F, 50.0F);
      float var4 = 93.75F;
      GlStateManager.scale(-var4, -var4, -var4);
      GlStateManager.rotate(180.0F, 0.0F, 1.0F, 0.0F);
      Block var5 = this.tileSign.getBlockType();
      if (var5 == Blocks.standing_sign) {
         float var6 = (float)(this.tileSign.getBlockMetadata() * 360) / 16.0F;
         GlStateManager.rotate(var6, 0.0F, 1.0F, 0.0F);
         GlStateManager.translate(0.0F, -1.0625F, 0.0F);
      } else {
         int var8 = this.tileSign.getBlockMetadata();
         float var7 = 0.0F;
         if (var8 == 2) {
            var7 = 180.0F;
         }

         if (var8 == 4) {
            var7 = 90.0F;
         }

         if (var8 == 5) {
            var7 = -90.0F;
         }

         GlStateManager.rotate(var7, 0.0F, 1.0F, 0.0F);
         GlStateManager.translate(0.0F, -1.0625F, 0.0F);
      }

      if (this.updateCounter / 6 % 2 == 0) {
         this.tileSign.lineBeingEdited = this.editLine;
      }

      TileEntityRendererDispatcher.instance.renderTileEntityAt(this.tileSign, -0.5D, -0.75D, -0.5D, 0.0F);
      this.tileSign.lineBeingEdited = -1;
      GlStateManager.popMatrix();
      super.drawScreen(var1, var2, var3);
   }

   public void updateScreen() {
      ++this.updateCounter;
   }

   public GuiEditSign(TileEntitySign var1) {
      this.tileSign = var1;
   }

   protected void keyTyped(char var1, int var2) throws IOException {
      if (var2 == 200) {
         this.editLine = this.editLine - 1 & 3;
      }

      if (var2 == 208 || var2 == 28 || var2 == 156) {
         this.editLine = this.editLine + 1 & 3;
      }

      String var3 = this.tileSign.signText[this.editLine].getUnformattedText();
      if (var2 == 14 && var3.length() > 0) {
         var3 = var3.substring(0, var3.length() - 1);
      }

      if (ChatAllowedCharacters.isAllowedCharacter(var1) && this.fontRendererObj.getStringWidth(var3 + var1) <= 90) {
         var3 = var3 + var1;
      }

      this.tileSign.signText[this.editLine] = new ChatComponentText(var3);
      if (var2 == 1) {
         this.actionPerformed(this.doneBtn);
      }

   }

   protected void actionPerformed(GuiButton var1) throws IOException {
      if (var1.enabled && var1.id == 0) {
         this.tileSign.markDirty();
         this.mc.displayGuiScreen((GuiScreen)null);
      }

   }
}
