package de.violence.mcgui;

import com.mojang.authlib.Agent;
import com.mojang.authlib.yggdrasil.YggdrasilAuthenticationService;
import com.mojang.authlib.yggdrasil.YggdrasilUserAuthentication;
import de.violence.Violence;
import de.violence.mcgui.GuiMainMenu;
import java.awt.Color;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.Proxy.Type;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiMultiplayer;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.util.Session;
import org.lwjgl.input.Keyboard;

public class AccountManager extends GuiScreen {
   private Minecraft mc = Minecraft.getMinecraft();
   public GuiTextField character;
   public GuiTextField proxy;
   public GuiTextField pwd;
   public GuiScreen eventscreen;
   public String status = "-|-";

   public AccountManager(GuiScreen event) {
      this.eventscreen = event;
   }

   public void initGui() {
      Keyboard.enableRepeatEvents(true);
      this.buttonList.add(new GuiButton(1, this.width / 2 - 100, this.height / 4 + 95, "Login"));
      this.buttonList.add(new GuiButton(3, this.width / 2 - 100, this.height / 4 + 115, "Clipboard Login"));
      this.character = new GuiTextField(3, this.fontRendererObj, this.width / 2 - 100, 76, 200, 20);
      this.pwd = new GuiTextField(4, this.fontRendererObj, this.width / 2 - 100, 116, 200, 20);
      this.proxy = new GuiTextField(5, this.fontRendererObj, this.width / 2 - 100, 155, 200, 20);
      this.pwd.setCensored(true);
      this.character.setMaxStringLength(50);
      this.pwd.setMaxStringLength(50);
      if(this.mc.session.getUsername() != null) {
         if(this.mc.session.getSessionType() == Session.Type.MOJANG) {
            this.status = "Logged in with: " + this.mc.session.getUsername();
         } else {
            this.status = "Logged in with cracked-name: " + this.mc.session.getUsername();
         }
      }

   }

   public void onGuiClosed() {
      Keyboard.enableRepeatEvents(false);
   }

   public void updateScreen() {
      this.character.updateCursorCounter();
      this.pwd.updateCursorCounter();
   }

   public void mouseClicked(int x, int y, int m) {
      this.character.mouseClicked(x, y, m);
      this.pwd.mouseClicked(x, y, m);
      this.proxy.mouseClicked(x, y, m);

      try {
         super.mouseClicked(x, y, m);
      } catch (Exception var5) {
         var5.printStackTrace();
      }

   }

   protected void keyTyped(char c, int i) {
      this.character.textboxKeyTyped(c, i);
      this.pwd.textboxKeyTyped(c, i);
      this.proxy.textboxKeyTyped(c, i);
      if(c == 9) {
         if(this.character.isFocused()) {
            this.character.setFocused(false);
            this.pwd.setFocused(true);
         } else {
            this.character.setFocused(true);
            this.pwd.setFocused(false);
         }
      }

      if(c == 13) {
         try {
            this.actionPerformed((GuiButton)this.buttonList.get(0));
         } catch (Exception var4) {
            var4.printStackTrace();
         }
      }

   }

   public void drawScreen(int mouseX, int mouseY, float partialTicks) {
      this.drawDefaultBackground();
      this.character.drawTextBox();
      this.pwd.drawTextBox();
      this.proxy.drawTextBox();
      if(Keyboard.isKeyDown(1)) {
         this.mc.displayGuiScreen(new GuiMainMenu());
      }

      this.drawString(Minecraft.getMinecraft().fontRendererObj, "E-Mail / Username", this.width / 2 - 100, 65, Color.WHITE.getRGB());
      this.drawString(Minecraft.getMinecraft().fontRendererObj, "Password", this.width / 2 - 100, 105, Color.WHITE.getRGB());
      this.drawString(Minecraft.getMinecraft().fontRendererObj, "Socks-Proxy", this.width / 2 - 100, 144, Color.WHITE.getRGB());
      this.drawString(Minecraft.getMinecraft().fontRendererObj, "Status: " + this.status, 2, 2, Color.WHITE.getRGB());
      super.drawScreen(mouseX, mouseY, partialTicks);
   }

   protected void actionPerformed(GuiButton button) throws IOException {
      InetSocketAddress e;
      if(button.id == 3) {
         if(getClipboardString() == null || getClipboardString().length() < 10) {
            this.status = "§cYour clipboard is empty!";
            return;
         }

         String[] a1 = getClipboardString().contains(":")?getClipboardString().split(":"):(getClipboardString().contains(";")?getClipboardString().split(";"):(getClipboardString().contains(",")?getClipboardString().split(","):getClipboardString().split(" ")));
         if(a1.length != 2) {
            this.status = "§cInvalid clipboard entry!";
            return;
         }

         System.out.println("Logging in...");
         e = null;
         YggdrasilUserAuthentication e2;
         if(this.proxy.getText().length() == 0) {
            e2 = (YggdrasilUserAuthentication)(new YggdrasilAuthenticationService(Proxy.NO_PROXY, "")).createUserAuthentication(Agent.MINECRAFT);
         } else {
            InetSocketAddress e1 = new InetSocketAddress(this.proxy.getText().split(":")[0], Integer.parseInt(this.proxy.getText().split(":")[1]));
            e2 = (YggdrasilUserAuthentication)(new YggdrasilAuthenticationService(new Proxy(Type.SOCKS, e1), "")).createUserAuthentication(Agent.MINECRAFT);
         }

         e2.setUsername(a1[0].trim());
         e2.setPassword(a1[1].trim());

         try {
            e2.logIn();
            this.mc.session = new Session(e2.getSelectedProfile().getName(), e2.getSelectedProfile().getId().toString(), e2.getAuthenticatedToken(), "mojang");
            this.status = "§aSuccesss: Logged in with: " + this.mc.session.getUsername();
            this.mc.displayGuiScreen(new GuiMultiplayer(new GuiMainMenu()));
            Violence.getSocketConnector().writeOutput("ign$" + this.mc.getSession().getUsername());
         } catch (Exception var6) {
            System.out.println("Error!");
            this.status = "§cError! Wrong Password or Username!";
         }
      } else if(this.pwd.getText().trim().isEmpty()) {
         if(!this.character.getText().trim().isEmpty()) {
            this.mc.session = new Session(this.character.getText().trim(), "-", "-", "Legacy");
            this.status = "Succesfully logged in with cracked account: " + this.character.getText().trim();
            Violence.getSocketConnector().writeOutput("ign$" + this.mc.getSession().getUsername());
         } else {
            this.status = "Failed to login!";
         }
      } else if(!this.character.getText().trim().isEmpty()) {
         System.out.println("Logging in...");
         System.out.println("Logging in...");
         YggdrasilUserAuthentication a = null;
         if(this.proxy.getText().length() == 0) {
            a = (YggdrasilUserAuthentication)(new YggdrasilAuthenticationService(Proxy.NO_PROXY, "")).createUserAuthentication(Agent.MINECRAFT);
         } else {
            e = new InetSocketAddress(this.proxy.getText().split(":")[0], Integer.parseInt(this.proxy.getText().split(":")[1]));
            a = (YggdrasilUserAuthentication)(new YggdrasilAuthenticationService(new Proxy(Type.SOCKS, e), "")).createUserAuthentication(Agent.MINECRAFT);
         }

         a.setUsername(this.character.getText().trim());
         a.setPassword(this.pwd.getText().trim());

         try {
            a.logIn();
            this.mc.session = new Session(a.getSelectedProfile().getName(), a.getSelectedProfile().getId().toString(), a.getAuthenticatedToken(), "mojang");
            this.status = "§aSuccesss: Logged in with: " + this.mc.session.getUsername();
            Violence.getSocketConnector().writeOutput("ign$" + this.mc.getSession().getUsername());
            this.mc.displayGuiScreen(new GuiMultiplayer(new GuiMainMenu()));
         } catch (Exception var5) {
            System.out.println("Error!");
            this.status = "§cError! Wrong Password or Username!";
         }
      }

   }
}
