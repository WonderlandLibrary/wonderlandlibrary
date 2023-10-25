package net.minecraft.client.gui;

import java.io.IOException;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.client.resources.I18n;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C00PacketKeepAlive;

public class GuiDownloadTerrain extends GuiScreen {
  private NetHandlerPlayClient netHandlerPlayClient;
  
  private int progress;
  
  public GuiDownloadTerrain(NetHandlerPlayClient netHandler) {
    this.netHandlerPlayClient = netHandler;
  }
  
  protected void keyTyped(char typedChar, int keyCode) throws IOException {}
  
  public void initGui() {
    this.buttonList.clear();
  }
  
  public void updateScreen() {
    this.progress++;
    if (this.progress % 20 == 0)
      this.netHandlerPlayClient.addToSendQueue((Packet)new C00PacketKeepAlive()); 
  }
  
  public void drawScreen(int mouseX, int mouseY, float partialTicks) {
    drawBackground(0);
    drawCenteredString(this.fontRendererObj, I18n.format("multiplayer.downloadingTerrain", new Object[0]), this.width / 2, this.height / 2 - 50, 16777215);
    super.drawScreen(mouseX, mouseY, partialTicks);
  }
  
  public boolean doesGuiPauseGame() {
    return false;
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\net\minecraft\client\gui\GuiDownloadTerrain.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */