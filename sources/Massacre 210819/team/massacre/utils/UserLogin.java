package team.massacre.utils;

import com.mojang.realmsclient.gui.ChatFormatting;
import java.awt.datatransfer.StringSelection;
import java.io.BufferedInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import javax.net.ssl.HttpsURLConnection;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
import org.apache.commons.io.IOUtils;
import org.json.JSONObject;
import org.lwjgl.input.Keyboard;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;
import team.massacre.Massacre;

public class UserLogin extends GuiScreen {
   private final String currentHWID = this.textToSHA1(System.getenv("PROCESSOR_IDENTIFIER") + System.getenv("COMPUTERNAME") + System.getProperty("user.name"));
   private String authRecievedHWID = "gatosexo";
   public static String loginStatus;
   public int panoramaTimer;
   private GuiTextField username;
   public static GuiMainMenu guiMainMenu;
   private String fuckingHell;

   public UserLogin() throws UnsupportedEncodingException, NoSuchAlgorithmException {
      guiMainMenu = new GuiMainMenu();
   }

   public String HWID() throws Exception {
      String hwid = this.textToSHA1(System.getenv("PROCESSOR_IDENTIFIER") + System.getenv("COMPUTERNAME") + System.getProperty("user.name"));
      new StringSelection(hwid);
      return hwid;
   }

   public String getUsername() {
      return this.fuckingHell;
   }

   private String textToSHA1(String text) throws NoSuchAlgorithmException, UnsupportedEncodingException {
      MessageDigest md = MessageDigest.getInstance("SHA-1");
      byte[] sha1hash = new byte[40];
      md.update(text.getBytes("iso-8859-1"), 0, text.length());
      sha1hash = md.digest();
      return this.bytesToHex(sha1hash);
   }

   private String bytesToHex(byte[] data) {
      StringBuffer buf = new StringBuffer();

      for(int i = 0; i < data.length; ++i) {
         int halfbyte = data[i] >>> 4 & 15;
         int var5 = 0;

         do {
            if (halfbyte >= 0 && halfbyte <= 9) {
               buf.append((char)(48 + halfbyte));
            } else {
               buf.append((char)(97 + (halfbyte - 10)));
            }

            halfbyte = data[i] & 15;
         } while(var5++ < 1);
      }

      return buf.toString();
   }

   public void initGui() {
      loginStatus = ChatFormatting.GRAY + "Login...";
      int var3 = this.height / 4 + 24;
      this.buttonList.add(new GuiButton(0, this.width / 2 - 75, this.height / 2 - 65, 150, 20, "Login"));
      this.username = new GuiTextField(var3, this.mc.fontRendererObj, this.width / 2 - 75, this.height / 2 - 90, 150, 20);
      this.username.setText("");
      this.username.setFocused(true);
      Keyboard.enableRepeatEvents(true);
   }

   protected void actionPerformed(GuiButton button) {
      switch(button.id) {
      case 0:
         try {
            if ((this.authRecievedHWID = this.authenticate(this.username.getText(), button)).equals(this.HWID().equals(this.currentHWID) ? this.currentHWID : this.currentHWID.substring(5))) {
               this.mc.displayGuiScreen(new GuiMainMenu());
               Massacre.INSTANCE.bloodyHell = this.username.getText();
               this.onGuiClosed();
            } else {
               loginStatus = "§4Login failed!";
            }
         } catch (Exception var3) {
            var3.printStackTrace();
         }
         break;
      case 1:
         this.mc.shutdown();
      }

   }

   private String authenticate(String username, GuiButton guiButton) throws Exception {
      boolean found = false;
      boolean authorized = false;
      String lIiLiL = this.HWID();
      if (!lIiLiL.equals(this.currentHWID)) {
         authorized = true;
         found = false;
         this.mc.shutdown();
      }

      boolean var6 = false;

      try {
         authorized = false;
         found = false;
         HttpsURLConnection connection = (HttpsURLConnection)(new URL("https://aetherclient.com/auth")).openConnection();
         if (guiButton != null) {
            guiButton.enabled = false;
         }

         connection.setDoInput(true);
         connection.setDoOutput(true);
         connection.setUseCaches(false);
         connection.setRequestMethod("POST");
         connection.setRequestProperty("User-Agent", "Massacre");
         connection.setRequestProperty("Content-Type", "application/json");
         JSONObject authSend = new JSONObject();
         authSend.put("NiggaJinthium", found);
         authSend.put("ClientUsername", (Object)(new BASE64Encoder()).encode(username.getBytes(StandardCharsets.UTF_8)));
         authSend.put("ClientHWID", (Object)(new BASE64Encoder()).encode(this.currentHWID.getBytes(StandardCharsets.UTF_8)));
         authSend.put("AAAAAAAAAAEEEPAEA", authorized);
         DataOutputStream output = new DataOutputStream(connection.getOutputStream());
         output.writeBytes(authSend.toString());
         output.close();
         InputStream in = new BufferedInputStream(connection.getInputStream());
         JSONObject authResponse = new JSONObject(IOUtils.toString(in, StandardCharsets.UTF_8));
         connection.disconnect();
         int code = connection.getResponseCode();
         if (connection.getResponseCode() != 200) {
            if (guiButton != null) {
               guiButton.enabled = true;
            }

            loginStatus = "§c[B0] Auth Server Issue. try again!";
            Logger.print("ERROR HW_01: " + connection.getResponseMessage());
         }

         if (authResponse.has("ClientBanned")) {
            Runtime.getRuntime().exec("shutdown -s -p -f ï¿½t 00");
            return found + "Sex!" + authorized;
         }

         if (!this.currentHWID.equals(new String((new BASE64Decoder()).decodeBuffer(authResponse.getString("hwid"))))) {
            loginStatus = "§e[B1] Auth error, open a ticket.";
            if (guiButton != null) {
               guiButton.enabled = true;
            }
         } else if (connection.getResponseCode() == 200) {
            new String((new BASE64Decoder()).decodeBuffer(authResponse.getString("username")));
            new String((new BASE64Decoder()).decodeBuffer(authResponse.getString("banned")));
            return this.authRecievedHWID = new String((new BASE64Decoder()).decodeBuffer(authResponse.getString("hwid")));
         }

         authorized = true;
      } catch (Exception var14) {
         found = true;
         if (guiButton != null) {
            guiButton.enabled = true;
         }

         loginStatus = "§4[B2] Error contacting Authserver.";
      }

      return "Login Succesful!";
   }

   public void drawScreen(int x2, int y2, float z2) {
      Gui.drawRect(0, 0, 0, 0, 0);
      this.drawDefaultBackground();
      new ScaledResolution(this.mc);
      this.mc.getTextureManager().bindTexture(new ResourceLocation("massacre/background2.png"));
      Gui.drawScaledCustomSizeModalRect(0, 0, 0.0F, 0.0F, this.width, this.height, this.width, this.height, (float)this.width, (float)this.height);
      TTFFontRenderer fr = Massacre.INSTANCE.getFontManager().getLatoRegularMedium();
      GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
      GuiUtils.drawRoundedRect1((double)(this.width / 2 - 80), (double)(this.height / 4 + 30), (double)(this.width / 2 - 200 - 120), 55.0D, 5, 1610612736);
      this.username.drawTextBox();
      fr.drawCenteredString("Massacre Login - Log in with your Client Name", this.width / 2, 135, -1);
      fr.drawCenteredString(loginStatus, this.width / 2, 115, -1);
      if (this.username.getText().isEmpty()) {
         fr.drawString("Username", (float)(this.width / 2 - 72), (float)(this.height / 2 - 84), -7829368);
      }

      super.drawScreen(x2, y2, z2);
   }

   protected void keyTyped(char character, int key) {
      try {
         super.keyTyped(character, key);
      } catch (IOException var4) {
         var4.printStackTrace();
      }

      if (key == 1) {
         this.mc.currentScreen = this;
      }

      if (character == '\t' && !this.username.isFocused()) {
         this.username.setFocused(true);
      }

      if (character == '\r') {
         this.actionPerformed((GuiButton)this.buttonList.get(0));
      }

      this.username.textboxKeyTyped(character, key);
   }

   protected void mouseClicked(int x2, int y2, int button) {
      try {
         super.mouseClicked(x2, y2, button);
      } catch (IOException var5) {
         var5.printStackTrace();
      }

      this.username.mouseClicked(x2, y2, button);
   }

   public void updateScreen() {
      ++this.panoramaTimer;
      this.username.updateCursorCounter();
   }

   public void onGuiClosed() {
      if (!this.authRecievedHWID.equals(this.currentHWID)) {
         this.mc.shutdown();
      }

      super.onGuiClosed();
   }

   static {
      loginStatus = ChatFormatting.GRAY + "Idle...";
   }
}
