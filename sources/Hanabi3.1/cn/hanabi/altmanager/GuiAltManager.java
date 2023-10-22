package cn.hanabi.altmanager;

import cn.hanabi.Client;
import cn.hanabi.Hanabi;
import cn.hanabi.utils.ParticleUtils;
import cn.hanabi.utils.RenderUtil;
import com.thealtening.auth.TheAlteningAuthentication;
import com.thealtening.auth.service.AlteningServiceType;
import java.awt.Color;
import java.awt.Desktop;
import java.awt.Toolkit;
import java.awt.Desktop.Action;
import java.awt.datatransfer.DataFlavor;
import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import me.yarukon.BlurBuffer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiMultiplayer;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

public class GuiAltManager extends GuiScreen {
   public static String Api = null;
   private final ResourceLocation background = new ResourceLocation("textures/mainmenubackground.png");
   public Alt selectedAlt = null;
   private GuiButton login;
   private GuiButton remove;
   private GuiButton rename;
   private AltLoginThread loginThread;
   private int offset;
   private static String status;
   private CustomGuiTextField seatchField;
   private CustomGuiTextField add_UserNameField;
   private CustomGuiTextField add_PassWordField;
   private CustomGuiTextField edit_UserNameField;
   private CustomGuiTextField edit_PassWordField;
   private float currentX;
   private float currentY;
   double anim_add = 0.0;
   double anim_import = 0.0;
   double anim_confirm = 0.0;
   double anim1 = 0.0;
   double anim2 = 0.0;
   double anim3 = 0.0;

   public GuiAltManager() {
      status = EnumChatFormatting.GRAY + "Idle...";
   }

   public static int getColor(int red, int green, int blue, int alpha) {
      int color = 0;
      color |= alpha << 24;
      color |= red << 16;
      color |= green << 8;
      color |= blue;
      return color;
   }

   public static int getColor(Color color) {
      return getColor(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha());
   }

   public static int getColor(int brightness) {
      return getColor(brightness, brightness, brightness, 255);
   }

   public static int getColor(int brightness, int alpha) {
      return getColor(brightness, brightness, brightness, alpha);
   }

   public void actionPerformed(GuiButton button) {
      switch (button.id) {
         case 3:
            Hanabi.INSTANCE.mslogin = !Hanabi.INSTANCE.mslogin;
            this.mc.displayGuiScreen(new GuiAltManager());
         case 4:
         case 6:
         default:
            break;
         case 5:
            ArrayList registry = AltManager.registry;
            Random random = new Random();
            if (registry.size() > 1) {
               Alt randomAlt = (Alt)registry.get(random.nextInt(AltManager.registry.size()));
               (this.loginThread = new AltLoginThread(randomAlt)).start();
            } else {
               status = EnumChatFormatting.RED + "There's no any alts!";
            }
            break;
         case 7:
            this.mc.displayGuiScreen((GuiScreen)null);
            break;
         case 8:
            AltManager.registry.clear();

            try {
               Hanabi.INSTANCE.altFileMgr.getFile(Alts.class).loadFile();
            } catch (IOException var6) {
            }

            status = "§bReloaded!";
            break;
         case 9:
            this.mc.displayGuiScreen(new GuiMultiplayer(this));
            break;
         case 10:
            TheAlteningAuthentication.theAltening().updateService(AlteningServiceType.THEALTENING);
            break;
         case 11:
            TheAlteningAuthentication.theAltening().updateService(AlteningServiceType.MOJANG);
            break;
         case 12:
            if (Desktop.isDesktopSupported()) {
               try {
                  URI uri = URI.create("http://anwen.love");
                  Desktop dp = Desktop.getDesktop();
                  if (dp.isSupported(Action.BROWSE)) {
                     dp.browse(uri);
                  }
               } catch (Exception var5) {
                  var5.printStackTrace();
               }
            }
            break;
         case 13:
            Hanabi.INSTANCE.hypixelBypass = !Hanabi.INSTANCE.hypixelBypass;
            this.mc.displayGuiScreen(new GuiAltManager());
            break;
         case 14:
            (new Thread("Ban Check") {
               public void run() {
                  GuiAltManager.status = "§cChecking All Alts...";
                  int size = GuiAltManager.this.getAlts().size();
                  int count = 0;
                  int bannedCount = 0;
                  Iterator var4 = GuiAltManager.this.getAlts().iterator();

                  while(var4.hasNext()) {
                     Alt alt = (Alt)var4.next();
                     if (!alt.getPassword().equals("")) {
                        StringBuilder var10000 = (new StringBuilder()).append(GuiAltManager.status).append(" (");
                        ++count;
                        GuiAltManager.status = var10000.append(Math.round((float)count / (float)size * 100.0F)).append("%)").toString();
                     }
                  }

                  Hanabi.INSTANCE.altFileMgr.saveFiles();
                  GuiAltManager.status = "§aDone! Deleted " + bannedCount + " alt" + (bannedCount > 1 ? "s" : "") + "!";
               }
            }).start();
      }

   }

   public void drawScreen(int mouseX, int mouseY, float par3) {
      ScaledResolution res = new ScaledResolution(this.mc);
      int w = res.getScaledWidth();
      int h = res.getScaledHeight();
      ScaledResolution sr = new ScaledResolution(this.mc);
      float xDiff = ((float)(mouseX - h / 2) - this.currentX) / (float)sr.getScaleFactor();
      float yDiff = ((float)(mouseY - w / 2) - this.currentY) / (float)sr.getScaleFactor();
      this.currentX += xDiff * 0.3F;
      this.currentY += yDiff * 0.3F;
      GlStateManager.translate(this.currentX / 30.0F, this.currentY / 15.0F, 0.0F);
      if (!Client.onDebug) {
         RenderUtil.drawImage(new ResourceLocation("Client/mainmenu/mainmenu.png"), -30, -30, sr.getScaledWidth() + 60, sr.getScaledHeight() + 60);
      } else {
         RenderUtil.drawImage(new ResourceLocation("Client/mainmenu/scifi.png"), -30, -30, sr.getScaledWidth() + 60, sr.getScaledHeight() + 60);
      }

      ParticleUtils.drawParticles(mouseX, mouseY);
      GlStateManager.translate(-this.currentX / 30.0F, -this.currentY / 15.0F, 0.0F);
      if (Mouse.hasWheel()) {
         int wheel = Mouse.getDWheel();
         if (wheel < 0) {
            this.offset += 26;
            if (this.offset < 0) {
               this.offset = 0;
            }
         } else if (wheel > 0) {
            this.offset -= 26;
            if (this.offset < 0) {
               this.offset = 0;
            }
         }
      }

      BlurBuffer.updateBlurBuffer(true);
      BlurBuffer.blurArea(50.0F, 33.0F, (float)(this.width - 100), (float)(this.height - 83), true);
      RenderUtil.drawRect(50.0F, 33.0F, (float)(this.width - 50), (float)(this.height - 50), (new Color(0, 0, 0, 150)).getRGB());
      this.drawString(this.fontRendererObj, this.mc.getSession().getUsername(), 10, 10, 14540253);
      FontRenderer fontRendererObj = this.fontRendererObj;
      StringBuilder sb = new StringBuilder("Account Manager - ");
      this.drawCenteredString(fontRendererObj, sb.append(AltManager.registry.size()).append(" alts").toString(), this.width / 2, 10, -1);
      this.drawCenteredString(this.fontRendererObj, this.loginThread == null ? status : this.loginThread.getStatus(), this.width / 2, 20, -1);
      this.drawHeader(mouseX, mouseY);
      this.drawAlt(mouseX, mouseY);
      this.drawEditAndAdd(mouseX, mouseY);
      if (Keyboard.isKeyDown(200)) {
         this.offset -= 26;
      } else if (Keyboard.isKeyDown(208)) {
         this.offset += 26;
      }

      if (this.offset < 0) {
         this.offset = 0;
      }

      this.seatchField.drawTextBox();
      this.edit_UserNameField.drawTextBox();
      this.edit_PassWordField.drawTextBox();
      this.add_UserNameField.drawTextBox();
      this.add_PassWordField.drawTextBox();
      super.drawScreen(mouseX, mouseY, par3);
   }

   public void drawEditAndAdd(int mouseX, int mouseY) {
      Hanabi.INSTANCE.fontManager.usans25.drawString("Add Alt", (float)(this.width / 2 + 20), 45.0F, (new Color(255, 255, 255)).getRGB());
      String hoverStringAdd = "";
      String hoverStringEdit = "";
      Hanabi.INSTANCE.fontManager.altManagerIcon45.drawString("C", (float)(this.width / 2 + 20), 110.0F, Color.WHITE.getRGB());
      if (this.isRectHovered((float)this.width / 2.0F + 18.0F, 90.0F, (float)this.width / 2.0F + 42.0F, 120.0F, mouseX, mouseY)) {
         this.anim_add = RenderUtil.getAnimationStateSmooth(2.0, this.anim_add, (double)(10.0F / (float)Minecraft.getDebugFPS()));
         hoverStringAdd = "Add";
      } else {
         this.anim_add = RenderUtil.getAnimationStateSmooth(0.0, this.anim_add, (double)(10.0F / (float)Minecraft.getDebugFPS()));
      }

      RenderUtil.drawRect((float)this.width / 2.0F + 18.0F, (float)(125.0 - this.anim_add), (float)this.width / 2.0F + 42.0F, 125.0F, (new Color(47, 116, 253, 255)).getRGB());
      Hanabi.INSTANCE.fontManager.altManagerIcon45.drawString("E", (float)(this.width / 2 + 55), 110.0F, Color.WHITE.getRGB());
      if (this.isRectHovered((float)this.width / 2.0F + 48.0F, 90.0F, (float)this.width / 2.0F + 78.0F, 120.0F, mouseX, mouseY)) {
         this.anim_import = RenderUtil.getAnimationStateSmooth(2.0, this.anim_import, (double)(10.0F / (float)Minecraft.getDebugFPS()));
         hoverStringAdd = "Import from clipboard";
      } else {
         this.anim_import = RenderUtil.getAnimationStateSmooth(0.0, this.anim_import, (double)(10.0F / (float)Minecraft.getDebugFPS()));
      }

      RenderUtil.drawRect((float)this.width / 2.0F + 48.0F, (float)(125.0 - this.anim_import), (float)this.width / 2.0F + 78.0F, 125.0F, (new Color(47, 116, 253, 255)).getRGB());
      Hanabi.INSTANCE.fontManager.wqy16.drawString(hoverStringAdd, (float)(this.width / 2 + 86), 110.0F, Color.WHITE.getRGB());
      Hanabi.INSTANCE.fontManager.usans25.drawString("Edit Alt", (float)(this.width / 2 + 20), 130.0F, (new Color(255, 255, 255)).getRGB());
      Hanabi.INSTANCE.fontManager.altManagerIcon45.drawString("A", (float)(this.width / 2 + 20), 190.0F, Color.WHITE.getRGB());
      if (this.isRectHovered((float)this.width / 2.0F + 18.0F, 180.0F, (float)this.width / 2.0F + 43.0F, 210.0F, mouseX, mouseY)) {
         this.anim_confirm = RenderUtil.getAnimationStateSmooth(2.0, this.anim_confirm, (double)(10.0F / (float)Minecraft.getDebugFPS()));
         hoverStringEdit = "Confirm";
      } else {
         this.anim_confirm = RenderUtil.getAnimationStateSmooth(0.0, this.anim_confirm, (double)(10.0F / (float)Minecraft.getDebugFPS()));
      }

      RenderUtil.drawRect((float)this.width / 2.0F + 18.0F, (float)(200.0 - this.anim_confirm), (float)this.width / 2.0F + 43.0F, 200.0F, (new Color(47, 116, 253, 255)).getRGB());
      Hanabi.INSTANCE.fontManager.wqy16.drawString(hoverStringEdit, (float)(this.width / 2 + 70), 185.0F, Color.WHITE.getRGB());
   }

   public void drawHeader(int mouseX, int mouseY) {
      String hovering = "";
      Hanabi.INSTANCE.fontManager.altManagerIcon45.drawString("A", 65.0F, 55.0F, Color.WHITE.getRGB());
      if (this.isRectHovered(63.0F, 50.0F, 87.0F, 73.0F, mouseX, mouseY)) {
         this.anim1 = RenderUtil.getAnimationStateSmooth(2.0, this.anim1, (double)(10.0F / (float)Minecraft.getDebugFPS()));
         hovering = "Login";
      } else {
         this.anim1 = RenderUtil.getAnimationStateSmooth(0.0, this.anim1, (double)(10.0F / (float)Minecraft.getDebugFPS()));
      }

      RenderUtil.drawRect(63.0F, (float)(73.0 - this.anim1), 87.0F, 73.0F, (new Color(47, 116, 253, 255)).getRGB());
      Hanabi.INSTANCE.fontManager.altManagerIcon45.drawString("B", 100.0F, 55.0F, Color.WHITE.getRGB());
      if (this.isRectHovered(95.0F, 50.0F, 115.0F, 73.0F, mouseX, mouseY)) {
         this.anim2 = RenderUtil.getAnimationStateSmooth(2.0, this.anim2, (double)(10.0F / (float)Minecraft.getDebugFPS()));
         hovering = "Remove";
      } else {
         this.anim2 = RenderUtil.getAnimationStateSmooth(0.0, this.anim2, (double)(10.0F / (float)Minecraft.getDebugFPS()));
      }

      RenderUtil.drawRect(95.0F, (float)(73.0 - this.anim2), 120.0F, 73.0F, (new Color(47, 116, 253, 255)).getRGB());
      Hanabi.INSTANCE.fontManager.wqy16.drawString(hovering, 130.0F, 52.0F, (new Color(200, 200, 200)).getRGB());
   }

   public void drawAlt(int par1, int par2) {
      GL11.glPushMatrix();
      this.prepareScissorBox(0.0F, 76.0F, (float)this.width, (float)(this.height - 50));
      GL11.glEnable(3089);
      int y = 80;
      Iterator var4 = this.getAlts().iterator();

      while(true) {
         Alt alt;
         do {
            if (!var4.hasNext()) {
               GL11.glDisable(3089);
               GL11.glPopMatrix();
               return;
            }

            alt = (Alt)var4.next();
         } while(!this.isAltInArea(y));

         String name;
         if (alt.getMask().equals("")) {
            name = alt.getUsername();
         } else {
            name = alt.getMask();
         }

         String pass;
         if (alt.getPassword().equals("")) {
            pass = "Cracked";
         } else {
            pass = alt.getPassword().replaceAll("\\.", "*");
         }

         if (alt == this.selectedAlt) {
            if (this.isMouseOverAlt(par1, par2, y - this.offset) && Mouse.isButtonDown(0)) {
               RenderUtil.drawRect(60.0F, (float)(y - this.offset - 4), (float)this.width / 2.0F, (float)(y - this.offset + 30), getColor(250, 50));
            } else if (this.isMouseOverAlt(par1, par2, y - this.offset)) {
               RenderUtil.drawRect(60.0F, (float)(y - this.offset - 4), (float)this.width / 2.0F, (float)(y - this.offset + 30), getColor(200, 50));
            } else {
               RenderUtil.drawRect(60.0F, (float)(y - this.offset - 4), (float)this.width / 2.0F, (float)(y - this.offset + 30), getColor(255, 50));
            }
         } else if (this.isMouseOverAlt(par1, par2, y - this.offset) && Mouse.isButtonDown(0)) {
            RenderUtil.drawRect(60.0F, (float)(y - this.offset - 4), (float)this.width / 2.0F, (float)(y - this.offset + 30), -getColor(250, 50));
         } else if (this.isMouseOverAlt(par1, par2, y - this.offset)) {
            RenderUtil.drawRect(60.0F, (float)(y - this.offset - 4), (float)this.width / 2.0F, (float)(y - this.offset + 30), getColor(200, 50));
         }

         Hanabi.INSTANCE.fontManager.wqy18.drawString(name, 75.0F, (float)(y - this.offset), -1);
         Hanabi.INSTANCE.fontManager.wqy13.drawString(alt.getUsername(), 75.0F, (float)(y - this.offset + 10), getColor(110));
         y += 35;
      }
   }

   public void initGui() {
      this.buttonList.add(new CustomGuiButton(4, this.width / 2 - 122, this.height - 48, 100, 20, "Direct Login"));
      this.buttonList.add(new CustomGuiButton(5, this.width / 2 - 122, this.height - 24, 100, 20, "Random"));
      this.buttonList.add(new CustomGuiButton(7, this.width / 2 - 200, this.height - 24, 70, 20, "Back"));
      this.buttonList.add(new CustomGuiButton(8, this.width / 2 - 200, this.height - 48, 70, 20, "Reload"));
      this.buttonList.add(new CustomGuiButton(9, this.width / 2 + 122, this.height - 48, 70, 20, "MultPlayer"));
      this.buttonList.add(new CustomGuiButton(10, this.width / 2 - 388, this.height - 48, 68, 20, "Altening"));
      this.buttonList.add(new CustomGuiButton(11, this.width / 2 - 388, this.height - 24, 68, 20, "Mojang"));
      this.buttonList.add(new CustomGuiButton(13, this.width / 2 + 122, this.height - 24, 70, 20, "Hyp Bypass :" + Hanabi.INSTANCE.hypixelBypass));
      this.buttonList.add(new CustomGuiButton(3, this.width / 2 + 16, this.height - 48, 100, 20, "MS Login: " + Hanabi.INSTANCE.mslogin));
      this.buttonList.add(new CustomGuiButton(14, this.width / 2 + 16, this.height - 24, 100, 20, "Ban Checker"));
      this.seatchField = new CustomGuiTextField(99998, this.mc.fontRendererObj, "Search Alt", 65, this.height - 14 - 60, 200, 14);
      this.add_UserNameField = new CustomGuiTextField(99997, this.mc.fontRendererObj, "Username", this.width / 2 + 20, 60, this.width - (this.width / 2 + 80), 14);
      this.add_PassWordField = new CustomGuiTextField(99996, this.mc.fontRendererObj, "Password", this.width / 2 + 20, 80, this.width - (this.width / 2 + 80), 14);
      this.edit_UserNameField = new CustomGuiTextField(99995, this.mc.fontRendererObj, "Select an alt to edit", this.width / 2 + 20, 145, this.width - (this.width / 2 + 80), 14);
      this.edit_PassWordField = new CustomGuiTextField(99994, this.mc.fontRendererObj, "", this.width / 2 + 20, 165, this.width - (this.width / 2 + 80), 14);
   }

   protected void keyTyped(char par1, int par2) {
      this.seatchField.textboxKeyTyped(par1, par2);
      this.add_UserNameField.textboxKeyTyped(par1, par2);
      this.add_PassWordField.textboxKeyTyped(par1, par2);
      this.edit_UserNameField.textboxKeyTyped(par1, par2);
      this.edit_PassWordField.textboxKeyTyped(par1, par2);
      if ((par1 == '\t' || par1 == '\r') && this.seatchField.isFocused()) {
         this.seatchField.setFocused(!this.seatchField.isFocused());
      }

      try {
         super.keyTyped(par1, par2);
      } catch (IOException var4) {
         var4.printStackTrace();
      }

   }

   private boolean isAltInArea(int y) {
      return y - this.offset <= this.height - 50;
   }

   private boolean isMouseOverAlt(int x, int y, int y1) {
      return x >= 52 && y >= y1 - 4 && (float)x <= (float)this.width / 2.0F && y <= y1 + 30 && y >= 33 && x <= this.width && y <= this.height - 50;
   }

   public boolean isRectHovered(float f, float y, float g, float y2, int mouseX, int mouseY) {
      return (float)mouseX >= f && (float)mouseX <= g && (float)mouseY >= y && (float)mouseY <= y2;
   }

   protected void mouseClicked(int par1, int par2, int par3) {
      this.seatchField.mouseClicked(par1, par2, par3);
      this.add_UserNameField.mouseClicked(par1, par2, par3);
      this.add_PassWordField.mouseClicked(par1, par2, par3);
      this.edit_UserNameField.mouseClicked(par1, par2, par3);
      this.edit_PassWordField.mouseClicked(par1, par2, par3);
      if (this.isRectHovered((float)this.width / 2.0F + 18.0F, 298.0F, (float)this.width / 2.0F + 43.0F, 326.0F, par1, par2) && this.selectedAlt != null) {
         this.selectedAlt.setUsername(this.edit_UserNameField.getText());
         this.selectedAlt.setPassword(this.edit_PassWordField.getText());
      }

      AddAltThread data;
      if (this.isRectHovered((float)this.width / 2.0F + 18.0F, 90.0F, (float)this.width / 2.0F + 42.0F, 120.0F, par1, par2)) {
         data = new AddAltThread(this.add_UserNameField.getText(), this.add_PassWordField.getText());
         data.start();
         this.add_UserNameField.setText("");
         this.add_PassWordField.setText("");
      }

      if (this.isRectHovered((float)this.width / 2.0F + 48.0F, 90.0F, (float)this.width / 2.0F + 78.0F, 120.0F, par1, par2)) {
         data = null;

         String data;
         try {
            data = (String)Toolkit.getDefaultToolkit().getSystemClipboard().getData(DataFlavor.stringFlavor);
         } catch (Exception var9) {
            return;
         }

         if (data.contains(":")) {
            String[] credentials = data.split(":");
            this.add_UserNameField.setText(credentials[0]);
            this.add_PassWordField.setText(credentials[1]);
         }
      }

      if (this.offset < 0) {
         this.offset = 0;
      }

      int y = 80 - this.offset;
      if (this.isRectHovered(63.0F, 50.0F, 87.0F, 73.0F, par1, par2)) {
         (this.loginThread = new AltLoginThread(this.selectedAlt)).start();
         Hanabi.INSTANCE.altFileMgr.saveFiles();
      }

      if (this.isRectHovered(95.0F, 50.0F, 115.0F, 73.0F, par1, par2)) {
         if (this.loginThread != null) {
            this.loginThread = null;
         }

         AltManager.registry.remove(this.selectedAlt);
         status = "§aRemoved.";

         try {
            Hanabi.INSTANCE.altFileMgr.getFile(Alts.class).saveFile();
         } catch (Exception var8) {
         }

         this.selectedAlt = null;
      }

      for(Iterator var12 = this.getAlts().iterator(); var12.hasNext(); y += 35) {
         Alt alt = (Alt)var12.next();
         if (this.isMouseOverAlt(par1, par2, y)) {
            if (alt == this.selectedAlt) {
               this.actionPerformed((GuiButton)this.buttonList.get(0));
               return;
            }

            this.selectedAlt = alt;
         }
      }

      try {
         super.mouseClicked(par1, par2, par3);
      } catch (IOException var7) {
         var7.printStackTrace();
      }

      if (this.isRectHovered((float)this.width / 2.0F + 18.0F, 180.0F, (float)this.width / 2.0F + 43.0F, 210.0F, par1, par2) && this.selectedAlt != null) {
         this.edit_UserNameField.setText(this.selectedAlt.getUsername());
         this.edit_PassWordField.setText(this.selectedAlt.getPassword());
      }

   }

   public static void setStatus(String status) {
      GuiAltManager.status = status;
   }

   private List getAlts() {
      List altList = new ArrayList();
      Iterator var2 = AltManager.registry.iterator();

      while(true) {
         Alt alt;
         do {
            if (!var2.hasNext()) {
               return altList;
            }

            alt = (Alt)var2.next();
         } while(!this.seatchField.getText().isEmpty() && !alt.getMask().toLowerCase().contains(this.seatchField.getText().toLowerCase()) && !alt.getUsername().toLowerCase().contains(this.seatchField.getText().toLowerCase()));

         altList.add(alt);
      }
   }

   public void prepareScissorBox(float x, float y, float x2, float y2) {
      ScaledResolution scale = new ScaledResolution(this.mc);
      int factor = scale.getScaleFactor();
      GL11.glScissor((int)(x * (float)factor), (int)(((float)scale.getScaledHeight() - y2) * (float)factor), (int)((x2 - x) * (float)factor), (int)((y2 - y) * (float)factor));
   }
}
