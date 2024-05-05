package team.massacre.api.ui.alt;

import com.mojang.authlib.Agent;
import com.mojang.authlib.exceptions.AuthenticationException;
import com.mojang.authlib.yggdrasil.YggdrasilAuthenticationService;
import com.mojang.authlib.yggdrasil.YggdrasilUserAuthentication;
import com.thealtening.auth.SSLController;
import com.thealtening.auth.TheAlteningAuthentication;
import com.thealtening.auth.service.AlteningServiceType;
import java.awt.Toolkit;
import java.awt.datatransfer.DataFlavor;
import java.io.IOException;
import java.net.Proxy;
import java.util.Iterator;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiMultiplayer;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Session;
import org.apache.commons.lang3.RandomUtils;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;
import team.massacre.Massacre;
import team.massacre.utils.GuiUtils;

public class GuiAltManager extends GuiScreen {
   public Alt selectedAlt = null;
   private int offset;
   private PasswordField password;
   private AltLoginThread thread;
   private GuiTextField username;
   private SSLController ssl = new SSLController();
   private TheAlteningAuthentication mojang = TheAlteningAuthentication.mojang();

   protected void actionPerformed(GuiButton button) {
      try {
         switch(button.id) {
         case 0:
            String data = (String)Toolkit.getDefaultToolkit().getSystemClipboard().getData(DataFlavor.stringFlavor);
            String[] credentials = data.split(":");
            if (credentials[0].contains("@alt.com")) {
               this.mojang.updateService(AlteningServiceType.THEALTENING);
               this.username.setText(credentials[0]);
               this.password.setText("pass");
            } else {
               this.username.setText(credentials[0]);
               this.password.setText(credentials[1]);
            }

            this.thread = new AltLoginThread(this.username.getText(), this.password.getText());
            this.thread.start();
            break;
         case 1:
            this.username.setText("massacre" + RandomUtils.nextInt(999, 9999999));
            this.password.setText("");
            this.thread = new AltLoginThread(this.username.getText(), this.password.getText());
            this.thread.start();
            break;
         case 2:
            this.mc.displayGuiScreen(new GuiMultiplayer(this));
            break;
         case 3:
            this.mc.displayGuiScreen((GuiScreen)null);
         }
      } catch (Throwable var4) {
      }

   }

   public void drawScreen(int par1, int par2, float par3) {
      new ScaledResolution(this.mc);
      this.mc.getTextureManager().bindTexture(new ResourceLocation("massacre/background2.png"));
      Gui.drawScaledCustomSizeModalRect(0, 0, 0.0F, 0.0F, this.width, this.height, this.width, this.height, (float)this.width, (float)this.height);
      FontRenderer fontRendererObj = this.fontRendererObj;
      new AltManager();
      if (this.thread != null) {
         Massacre.INSTANCE.getFontManager().getLatoRegularMainMenu().drawCenteredString(this.thread.getStatus(), this.width / 2, this.height / 2 - 64, -1);
      }

      Massacre.INSTANCE.getFontManager().getLatoRegularMainMenu().drawCenteredString("Username: " + this.mc.getSession().getUsername(), this.width / 2, this.height / 2 - 52, -1);
      GL11.glPushMatrix();
      this.prepareScissorBox(0.0F, 33.0F, (float)this.width, (float)(this.height - 50));
      GL11.glEnable(3089);
      int y2 = 38;
      new AltManager();
      Iterator var9 = AltManager.registry.iterator();

      while(true) {
         Alt alt2;
         do {
            if (!var9.hasNext()) {
               GL11.glDisable(3089);
               GL11.glPopMatrix();
               super.drawScreen(par1, par2, par3);
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

            alt2 = (Alt)var9.next();
         } while(!this.isAltInArea(y2));

         String name = alt2.getMask().equals("") ? alt2.getUsername() : alt2.getMask();
         String pass = alt2.getPassword().equals("") ? "§cCracked" : alt2.getPassword().replaceAll(".", "*");
         if (alt2 == this.selectedAlt) {
            if (this.isMouseOverAlt(par1, par2, y2 - this.offset) && Mouse.isButtonDown(0)) {
               GuiUtils.drawBorderedRect(52.0F, (float)(y2 - this.offset - 4), (float)(this.width - 52), (float)(y2 - this.offset + 20), 1.0F, -16777216, -2142943931);
            } else if (this.isMouseOverAlt(par1, par2, y2 - this.offset)) {
               GuiUtils.drawBorderedRect(52.0F, (float)(y2 - this.offset - 4), (float)(this.width - 52), (float)(y2 - this.offset + 20), 1.0F, -16777216, -2142088622);
            } else {
               GuiUtils.drawBorderedRect(52.0F, (float)(y2 - this.offset - 4), (float)(this.width - 52), (float)(y2 - this.offset + 20), 1.0F, -16777216, -2144259791);
            }
         } else if (this.isMouseOverAlt(par1, par2, y2 - this.offset) && Mouse.isButtonDown(0)) {
            GuiUtils.drawBorderedRect(52.0F, (float)(y2 - this.offset - 4), (float)(this.width - 52), (float)(y2 - this.offset + 20), 1.0F, -16777216, -2146101995);
         } else if (this.isMouseOverAlt(par1, par2, y2 - this.offset)) {
            GuiUtils.drawBorderedRect(52.0F, (float)(y2 - this.offset - 4), (float)(this.width - 52), (float)(y2 - this.offset + 20), 1.0F, -16777216, -2145180893);
         }

         this.drawCenteredString(this.fontRendererObj, name, this.width / 2, y2 - this.offset, -1);
         this.drawCenteredString(this.fontRendererObj, pass, this.width / 2, y2 - this.offset + 10, 5592405);
         y2 += 26;
      }
   }

   protected void keyTyped(char character, int key) {
      try {
         super.keyTyped(character, key);
      } catch (IOException var4) {
         var4.printStackTrace();
      }

      if (character == '\t') {
         if (!this.username.isFocused() && !this.password.isFocused()) {
            this.username.setFocused(true);
         } else {
            this.username.setFocused(this.password.isFocused());
            this.password.setFocused(!this.username.isFocused());
         }
      }

      if (character == '\r') {
         this.actionPerformed((GuiButton)this.buttonList.get(0));
      }

      this.username.textboxKeyTyped(character, key);
      this.password.textboxKeyTyped(character, key);
   }

   public void initGui() {
      int var3 = this.height / 4 + 24;
      this.buttonList.add(new GuiButton(0, this.width / 2 - 50, this.height / 2 - 40, 100, 20, "Clipboard"));
      this.buttonList.add(new GuiButton(1, this.width / 2 - 50, this.height / 2 - 16, 100, 20, "Offline Account"));
      this.buttonList.add(new GuiButton(2, this.width / 2 - 50, this.height / 2 + 8, 100, 20, "Multiplayer"));
      this.buttonList.add(new GuiButton(3, this.width / 2 - 50, this.height / 2 + 32, 100, 20, "Cancel"));
      this.username = new GuiTextField(var3, this.mc.fontRendererObj, this.width / 2 - 100, 60, 200, 20);
      this.password = new PasswordField(this.mc.fontRendererObj, this.width / 2 - 100, 100, 200, 20);
      this.username.setFocused(true);
      Keyboard.enableRepeatEvents(true);
   }

   private boolean isAltInArea(int y2) {
      return y2 - this.offset <= this.height - 50;
   }

   public void updateScreen() {
      this.username.updateCursorCounter();
      this.password.updateCursorCounter();
   }

   private boolean isMouseOverAlt(int x2, int y2, int y1) {
      return x2 >= 52 && y2 >= y1 - 4 && x2 <= this.width - 52 && y2 <= y1 + 20 && x2 >= 0 && y2 >= 33 && x2 <= this.width && y2 <= this.height - 50;
   }

   protected void mouseClicked(int x2, int y2, int button) {
      try {
         super.mouseClicked(x2, y2, button);
      } catch (IOException var5) {
         var5.printStackTrace();
      }

      this.username.mouseClicked(x2, y2, button);
      this.password.mouseClicked(x2, y2, button);
   }

   public void onGuiClosed() {
      Keyboard.enableRepeatEvents(false);
   }

   private Session createSession(String username, String password) {
      YggdrasilAuthenticationService service = new YggdrasilAuthenticationService(Proxy.NO_PROXY, "");
      YggdrasilUserAuthentication auth = (YggdrasilUserAuthentication)service.createUserAuthentication(Agent.MINECRAFT);
      auth.setUsername(username);
      auth.setPassword(password);

      try {
         if (username.endsWith("@alt.com")) {
            this.ssl.disableCertificateValidation();
            this.mojang.updateService(AlteningServiceType.THEALTENING);
         } else if (this.mojang.getService() == AlteningServiceType.THEALTENING) {
            this.ssl.enableCertificateValidation();
            this.mojang.updateService(AlteningServiceType.MOJANG);
         }

         auth.logIn();
         return new Session(auth.getSelectedProfile().getName(), auth.getSelectedProfile().getId().toString(), auth.getAuthenticatedToken(), "mojang");
      } catch (AuthenticationException var6) {
         var6.printStackTrace();
         return null;
      }
   }

   public void prepareScissorBox(float x2, float y2, float x22, float y22) {
      ScaledResolution scale = new ScaledResolution(this.mc);
      int factor = scale.getScaleFactor();
      GL11.glScissor((int)(x2 * (float)factor), (int)(((float)scale.getScaledHeight() - y22) * (float)factor), (int)((x22 - x2) * (float)factor), (int)((y22 - y2) * (float)factor));
   }
}
