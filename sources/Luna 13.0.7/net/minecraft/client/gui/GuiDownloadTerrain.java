package net.minecraft.client.gui;

import java.io.IOException;
import java.util.List;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.client.resources.I18n;
import net.minecraft.network.play.client.C00PacketKeepAlive;
import space.lunaclient.luna.Luna;
import space.lunaclient.luna.api.element.Element;
import space.lunaclient.luna.impl.elements.world.Scaffold;
import space.lunaclient.luna.impl.managers.ElementManager;

public class GuiDownloadTerrain
  extends GuiScreen
{
  private NetHandlerPlayClient netHandlerPlayClient;
  private int progress;
  private static final String __OBFID = "CL_00000708";
  
  public GuiDownloadTerrain(NetHandlerPlayClient p_i45023_1_)
  {
    this.netHandlerPlayClient = p_i45023_1_;
  }
  
  protected void keyTyped(char typedChar, int keyCode)
    throws IOException
  {}
  
  public void initGui()
  {
    this.buttonList.clear();
  }
  
  public void updateScreen()
  {
    this.progress += 1;
    if (this.progress % 20 == 0) {
      this.netHandlerPlayClient.addToSendQueue(new C00PacketKeepAlive());
    }
  }
  
  public void drawScreen(int mouseX, int mouseY, float partialTicks)
  {
    drawBackground(0);
    drawCenteredString(this.fontRendererObj, I18n.format("multiplayer.downloadingTerrain", new Object[0]), width / 2, height / 2 - 50, 16777215);
    if (Luna.INSTANCE.ELEMENT_MANAGER.getElement(Scaffold.class).isToggled()) {
      Luna.INSTANCE.ELEMENT_MANAGER.getElement(Scaffold.class).toggle();
    }
    super.drawScreen(mouseX, mouseY, partialTicks);
  }
  
  public boolean doesGuiPauseGame()
  {
    return false;
  }
}
