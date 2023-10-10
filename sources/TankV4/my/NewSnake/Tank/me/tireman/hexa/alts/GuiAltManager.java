package my.NewSnake.Tank.me.tireman.hexa.alts;

import java.io.IOException;
import java.util.Iterator;
import my.NewSnake.Tank.Snake;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.util.EnumChatFormatting;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

public class GuiAltManager extends GuiScreen {
   private String status;
   private GuiButton rename;
   private int offset;
   public Alt selectedAlt = null;
   private GuiButton login;
   private AltLoginThread loginThread;
   private GuiButton remove;

   public void actionPerformed(GuiButton var1) throws IOException {
      switch(var1.id) {
      case 0:
         if (this.loginThread == null) {
            this.mc.displayGuiScreen((GuiScreen)null);
         } else if (!this.loginThread.getStatus().equals(EnumChatFormatting.YELLOW + "Attempting to log in") && !this.loginThread.getStatus().equals(EnumChatFormatting.RED + "Do not hit back!" + EnumChatFormatting.YELLOW + " Logging in...")) {
            this.mc.displayGuiScreen((GuiScreen)null);
         } else {
            this.loginThread.setStatus(EnumChatFormatting.RED + "Failed to login! Please try again!" + EnumChatFormatting.YELLOW + " Logging in...");
         }
         break;
      case 1:
         String var4 = this.selectedAlt.getUsername();
         String var3 = this.selectedAlt.getPassword();
         this.loginThread = new AltLoginThread(var4, var3);
         this.loginThread.start();
         break;
      case 2:
         if (this.loginThread != null) {
            this.loginThread = null;
         }

         AltManager var2 = Snake.instance.altManager;
         AltManager.registry.remove(this.selectedAlt);
         this.status = "§aRemoved.";
         this.selectedAlt = null;
         break;
      case 3:
         this.mc.displayGuiScreen(new GuiAddAlt(this));
         break;
      case 4:
         this.mc.displayGuiScreen(new GuiAltLogin(this));
      case 5:
      default:
         break;
      case 6:
         this.mc.displayGuiScreen(new GuiRenameAlt(this));
      }

   }

   protected void mouseClicked(int var1, int var2, int var3) throws IOException {
      if (this.offset < 0) {
         this.offset = 0;
      }

      int var4 = 38 - this.offset;

      for(Iterator var6 = AltManager.registry.iterator(); var6.hasNext(); var4 += 26) {
         Alt var5 = (Alt)var6.next();
         if (var4 != 0) {
            if (var5 == this.selectedAlt) {
               this.actionPerformed((GuiButton)this.buttonList.get(1));
               return;
            }

            this.selectedAlt = var5;
         }
      }

      try {
         super.mouseClicked(var1, var2, var3);
      } catch (IOException var7) {
         var7.printStackTrace();
      }

   }

   public void initGui() {
      this.buttonList.add(new GuiButton(0, width / 2 + 4 + 50, height - 24, 100, 20, "Cancel"));
      this.login = new GuiButton(1, width / 2 - 154, height - 48, 100, 20, "Login");
      this.buttonList.add(this.login);
      this.remove = new GuiButton(2, width / 2 - 154, height - 24, 100, 20, "Remove");
      this.buttonList.add(this.remove);
      this.buttonList.add(new GuiButton(3, width / 2 + 4 + 50, height - 48, 100, 20, "Add"));
      this.buttonList.add(new GuiButton(4, width / 2 - 50, height - 48, 100, 20, "Direct Login"));
      this.rename = new GuiButton(6, width / 2 - 50, height - 24, 100, 20, "Edit");
      this.buttonList.add(this.rename);
      this.login.enabled = false;
      this.remove.enabled = false;
      this.rename.enabled = false;
   }

   public GuiAltManager() {
      this.status = EnumChatFormatting.GRAY + "No alts selected";
   }

   public void drawScreen(int var1, int var2, float var3) {
      if (Mouse.hasWheel()) {
         int var4 = Mouse.getDWheel();
         if (var4 < 0) {
            this.offset += 26;
            if (this.offset < 0) {
               this.offset = 0;
            }
         } else if (var4 > 0) {
            this.offset -= 26;
            if (this.offset < 0) {
               this.offset = 0;
            }
         }
      }

      this.drawDefaultBackground();
      this.drawString(this.fontRendererObj, this.mc.session.getUsername(), 10, 10, -7829368);
      FontRenderer var11 = this.fontRendererObj;
      StringBuilder var5 = new StringBuilder("Account Manager - ");
      this.drawCenteredString(var11, var5.append(AltManager.registry.size()).append(" alts").toString(), width / 2, 10, -1);
      this.drawCenteredString(this.fontRendererObj, this.loginThread == null ? this.status : this.loginThread.getStatus(), width / 2, 20, -1);
      RenderUtils.drawBorderedRect(50.0F, 33.0F, width - 50, height - 50, 1.0F, -16777216, Integer.MIN_VALUE);
      GL11.glPushMatrix();
      this.prepareScissorBox(0.0F, 33.0F, (float)width, (float)(height - 50));
      GL11.glEnable(3089);
      int var6 = 38;
      Iterator var8 = AltManager.registry.iterator();

      while(true) {
         Alt var7;
         do {
            if (!var8.hasNext()) {
               GL11.glDisable(3089);
               GL11.glPopMatrix();
               super.drawScreen(var1, var2, var3);
               if (this.selectedAlt == null) {
                  this.login.enabled = false;
                  this.remove.enabled = false;
                  this.rename.enabled = false;
               } else {
                  this.login.enabled = true;
                  this.remove.enabled = true;
                  this.rename.enabled = true;
               }

               if (Keyboard.isKeyDown(200)) {
                  this.offset -= 26;
                  if (this.offset < 0) {
                     this.offset = 0;
                  }
               } else if (Keyboard.isKeyDown(208)) {
                  this.offset += 26;
                  if (this.offset < 0) {
                     this.offset = 0;
                  }
               }

               return;
            }

            var7 = (Alt)var8.next();
         } while(var6 != 0);

         String var9 = var7.getMask().equals("") ? var7.getUsername() : var7.getMask();
         String var10 = var7.getPassword().equals("") ? "§cCracked" : var7.getPassword().replaceAll(".", "*");
         if (var7 == this.selectedAlt) {
            if (var2 >= var6 - this.offset && Mouse.isButtonDown(0)) {
               RenderUtils.drawBorderedRect(52.0F, (float)(var6 - this.offset - 4), width - 52, var6 - this.offset + 20, 1.0F, -16777216, -2142943931);
            } else if (var2 >= var6 - this.offset) {
               RenderUtils.drawBorderedRect(52.0F, (float)(var6 - this.offset - 4), width - 52, var6 - this.offset + 20, 1.0F, -16777216, -2142088622);
            } else {
               RenderUtils.drawBorderedRect(52.0F, (float)(var6 - this.offset - 4), width - 52, var6 - this.offset + 20, 1.0F, -16777216, -2144259791);
            }
         } else if (var2 >= var6 - this.offset && Mouse.isButtonDown(0)) {
            RenderUtils.drawBorderedRect(52.0F, (float)(var6 - this.offset - 4), width - 52, var6 - this.offset + 20, 1.0F, -16777216, -2146101995);
         } else if (var2 >= var6 - this.offset) {
            RenderUtils.drawBorderedRect(52.0F, (float)(var6 - this.offset - 4), width - 52, var6 - this.offset + 20, 1.0F, -16777216, -2145180893);
         }

         this.drawCenteredString(this.fontRendererObj, var9, width / 2, var6 - this.offset, -1);
         this.drawCenteredString(this.fontRendererObj, var10, width / 2, var6 - this.offset + 10, 5592405);
         var6 += 26;
      }
   }

   public void prepareScissorBox(float var1, float var2, float var3, float var4) {
      ScaledResolution var5 = new ScaledResolution(this.mc);
      int var6 = var5.getScaleFactor();
      GL11.glScissor((int)(var1 * (float)var6), (int)(((float)ScaledResolution.getScaledHeight() - var4) * (float)var6), (int)((var3 - var1) * (float)var6), (int)((var4 - var2) * (float)var6));
   }
}
