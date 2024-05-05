package space.lunaclient.luna.impl.gui.alt;

import com.mojang.realmsclient.gui.ChatFormatting;
import java.io.IOException;
import java.util.List;
import java.util.Random;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Session;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;
import space.lunaclient.luna.Luna;
import space.lunaclient.luna.impl.managers.CustomFileManager;
import space.lunaclient.luna.util.RenderUtils;

public class GuiAltManager
  extends GuiScreen
{
  private static Minecraft mc = ;
  private GuiButton login;
  private GuiButton remove;
  private GuiButton rename;
  private GuiButton random;
  private AltLoginThread loginThread;
  private int offset;
  Alt selectedAlt = null;
  private String status = "§fWaiting...";
  private static final ResourceLocation background = new ResourceLocation(Luna.BACKGROUND);
  
  public GuiAltManager()
  {
    CustomFileManager.saveAlts();
  }
  
  public void actionPerformed(GuiButton button)
  {
    switch (button.id)
    {
    case 0: 
      if (this.loginThread == null) {
        mc.displayGuiScreen(null);
      } else if ((!this.loginThread.getStatus().equals("§7Logging in...")) && 
        (!this.loginThread.getStatus().equals("§1Do not hit back! §7Logging in..."))) {
        mc.displayGuiScreen(null);
      } else {
        this.loginThread.setStatus("§1Do not hit back! §eLogging in...");
      }
      break;
    case 1: 
      String user = this.selectedAlt.getUsername();
      String pass = this.selectedAlt.getPassword();
      this.loginThread = new AltLoginThread(user, pass);
      this.loginThread.start();
      break;
    case 2: 
      if (this.loginThread != null) {
        this.loginThread = null;
      }
      Luna.INSTANCE.ALT_MANAGER.getAlts().remove(this.selectedAlt);
      this.status = "§4Removed.";
      this.selectedAlt = null;
      Luna.INSTANCE.FILE_MANAGER.saveFiles();
      break;
    case 3: 
      mc.displayGuiScreen(new GuiAddAlt(this));
      break;
    case 4: 
      mc.displayGuiScreen(new GuiAltLogin(this));
      break;
    case 5: 
      if ((!Luna.INSTANCE.ALT_MANAGER.getAlts().isEmpty()) || (Luna.INSTANCE.ALT_MANAGER.getAlts().size() >= 1))
      {
        Alt randomAlt = (Alt)Luna.INSTANCE.ALT_MANAGER.getAlts().get(new Random().nextInt(Luna.INSTANCE.ALT_MANAGER.getAlts().size()));
        String user1 = randomAlt.getUsername();
        String pass1 = randomAlt.getPassword();
        this.loginThread = new AltLoginThread(user1, pass1);
        this.loginThread.start();
      }
      else
      {
        this.status = (ChatFormatting.RED + "Alt list is empty! Can't use a random alt if there's no alts to choose from.");
      }
      break;
    case 6: 
      mc.displayGuiScreen(new GuiRenameAlt(this));
      break;
    case 7: 
      Alt lastAlt = Luna.INSTANCE.ALT_MANAGER.getLastAlt();
      if (lastAlt == null)
      {
        if (this.loginThread == null) {
          this.status = "§eThere is no last used alt!";
        } else {
          this.loginThread.setStatus("§eThere is no last used alt!");
        }
      }
      else
      {
        String user2 = lastAlt.getUsername();
        String pass2 = lastAlt.getPassword();
        this.loginThread = new AltLoginThread(user2, pass2);
        this.loginThread.start();
      }
      break;
    }
  }
  
  public void drawScreen(int par1, int par2, float par3)
  {
    GuiAltLogin.time = 100;
    if (Mouse.hasWheel())
    {
      int wheel = Mouse.getDWheel();
      if (wheel < 0)
      {
        this.offset += 26;
        if (this.offset < 0) {
          this.offset = 0;
        }
      }
      else if (wheel > 0)
      {
        this.offset -= 26;
        if (this.offset < 0) {
          this.offset = 0;
        }
      }
    }
    drawDefaultBackground();
    renderBackground(width, height);
    drawString(this.fontRendererObj, mc.session.getUsername(), 10, 10, 14540253);
    drawCenteredString(this.fontRendererObj, "Account Manager - " + Luna.INSTANCE.ALT_MANAGER
      .getAlts().size() + " alts - " + Luna.sponsorList[0], width / 2, 10, -1);
    
    drawCenteredString(this.fontRendererObj, this.loginThread == null ? this.status : this.loginThread.getStatus(), width / 2, 20, -1);
    
    RenderUtils.drawBorderedRectNameTag(50.0F, 33.0F, width - 50, height - 50, 1.0F, -16777216, Integer.MIN_VALUE);
    
    GL11.glPushMatrix();
    prepareScissorBox(width, height - 50);
    GL11.glEnable(3089);
    int y = 38;
    for (Alt alt : Luna.INSTANCE.ALT_MANAGER.getAlts()) {
      if (isAltInArea(y))
      {
        String name;
        String name;
        if (alt.getMask().equals("")) {
          name = alt.getUsername();
        } else {
          name = alt.getMask();
        }
        String pass;
        String pass;
        if (alt.getPassword().equals("")) {
          pass = "§cCracked";
        } else {
          pass = alt.getPassword().replaceAll(".", "*");
        }
        if (alt == this.selectedAlt)
        {
          if ((isMouseOverAlt(par1, par2, y - this.offset)) && (Mouse.isButtonDown(0))) {
            RenderUtils.drawBorderedRectNameTag(52.0F, y - this.offset - 4, width - 52, y - this.offset + 20, 1.0F, -16777216, -2142943931);
          } else if (isMouseOverAlt(par1, par2, y - this.offset)) {
            RenderUtils.drawBorderedRectNameTag(52.0F, y - this.offset - 4, width - 52, y - this.offset + 20, 1.0F, -16777216, -2142088622);
          } else {
            RenderUtils.drawBorderedRectNameTag(52.0F, y - this.offset - 4, width - 52, y - this.offset + 20, 1.0F, -16777216, -2144259791);
          }
        }
        else if ((isMouseOverAlt(par1, par2, y - this.offset)) && (Mouse.isButtonDown(0))) {
          RenderUtils.drawBorderedRectNameTag(52.0F, y - this.offset - 4, width - 52, y - this.offset + 20, 1.0F, -16777216, -2146101995);
        } else if (isMouseOverAlt(par1, par2, y - this.offset)) {
          RenderUtils.drawBorderedRectNameTag(52.0F, y - this.offset - 4, width - 52, y - this.offset + 20, 1.0F, -16777216, -2145180893);
        }
        drawCenteredString(this.fontRendererObj, name, width / 2, y - this.offset, -1);
        drawCenteredString(this.fontRendererObj, pass, width / 2, y - this.offset + 10, 5592405);
        y += 26;
      }
    }
    GL11.glDisable(3089);
    GL11.glPopMatrix();
    super.drawScreen(par1, par2, par3);
    if (this.selectedAlt == null)
    {
      this.login.enabled = false;
      this.remove.enabled = false;
      this.rename.enabled = false;
    }
    else
    {
      this.login.enabled = true;
      this.remove.enabled = true;
      this.rename.enabled = true;
    }
    this.random.enabled = (!Luna.INSTANCE.ALT_MANAGER.getAlts().isEmpty());
    if (Keyboard.isKeyDown(200))
    {
      this.offset -= 26;
      if (this.offset < 0) {
        this.offset = 0;
      }
    }
    else if (Keyboard.isKeyDown(208))
    {
      this.offset += 26;
      if (this.offset < 0) {
        this.offset = 0;
      }
    }
  }
  
  public void initGui()
  {
    this.buttonList.add(new GuiButton(0, width / 2 + 4 + 76, height - 24, 75, 20, "Cancel"));
    this.buttonList.add(this.login = new GuiButton(1, width / 2 - 154, height - 48, 100, 20, "Login"));
    this.buttonList.add(this.remove = new GuiButton(2, width / 2 - 74, height - 24, 70, 20, "Remove"));
    this.buttonList.add(new GuiButton(3, width / 2 + 4 + 50, height - 48, 100, 20, "Add"));
    this.buttonList.add(new GuiButton(4, width / 2 - 50, height - 48, 100, 20, "Direct Login"));
    this.buttonList.add(this.random = new GuiButton(5, width / 2 - 154, height - 24, 70, 20, "Random"));
    this.buttonList.add(this.rename = new GuiButton(6, width / 2 + 4, height - 24, 70, 20, "Rename"));
    this.login.enabled = false;
    this.remove.enabled = false;
    this.rename.enabled = false;
    this.random.enabled = false;
  }
  
  private boolean isAltInArea(int y)
  {
    return y - this.offset <= height - 50;
  }
  
  private boolean isMouseOverAlt(int x, int y, int y1)
  {
    return (x >= 52) && (y >= y1 - 4) && (x <= width - 52) && (y <= y1 + 20) && (y >= 33) && (x <= width) && (y <= height - 50);
  }
  
  protected void mouseClicked(int par1, int par2, int par3)
  {
    if (this.offset < 0) {
      this.offset = 0;
    }
    int y = 38 - this.offset;
    for (Alt alt : Luna.INSTANCE.ALT_MANAGER.getAlts())
    {
      if (isMouseOverAlt(par1, par2, y))
      {
        if (alt == this.selectedAlt)
        {
          actionPerformed((GuiButton)this.buttonList.get(1));
          return;
        }
        this.selectedAlt = alt;
      }
      y += 26;
    }
    try
    {
      super.mouseClicked(par1, par2, par3);
    }
    catch (IOException e)
    {
      e.printStackTrace();
    }
  }
  
  private void prepareScissorBox(float x2, float y2)
  {
    int factor = RenderUtils.getScaledRes().getScaleFactor();
    GL11.glScissor((int)(0.0F * factor), (int)((RenderUtils.getScaledRes().getScaledHeight() - y2) * factor), (int)((x2 - 0.0F) * factor), (int)((y2 - 33.0F) * factor));
  }
  
  private void renderBackground(int par1, int par2)
  {
    GL11.glDisable(2929);
    GL11.glDepthMask(false);
    OpenGlHelper.glBlendFunc(770, 771, 1, 0);
    GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
    GL11.glDisable(3008);
    mc.getTextureManager().bindTexture(background);
    Tessellator var3 = Tessellator.getInstance();
    var3.getWorldRenderer().startDrawingQuads();
    var3.getWorldRenderer().addVertexWithUV(0.0D, par2, -90.0D, 0.0D, 1.0D);
    var3.getWorldRenderer().addVertexWithUV(par1, par2, -90.0D, 1.0D, 1.0D);
    var3.getWorldRenderer().addVertexWithUV(par1, 0.0D, -90.0D, 1.0D, 0.0D);
    var3.getWorldRenderer().addVertexWithUV(0.0D, 0.0D, -90.0D, 0.0D, 0.0D);
    var3.draw();
    GL11.glDepthMask(true);
    GL11.glEnable(2929);
    GL11.glEnable(3008);
    GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
  }
}
