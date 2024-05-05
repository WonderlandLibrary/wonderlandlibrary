package me.AveReborn.ui.altManager;

import me.AveReborn.Client;
import me.AveReborn.ui.altManager.AltLogin;
import me.AveReborn.ui.altManager.GuiAltManager;
import me.AveReborn.util.ClientUtil;
import me.AveReborn.util.Colors;
import me.AveReborn.util.FlatColors;
import me.AveReborn.util.RenderUtil;
import me.AveReborn.util.fontRenderer.UnicodeFontRenderer;
import me.AveReborn.util.handler.MouseInputHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;

public class GuiAltSlot {
   private String username;
   private String password;
   public int x;
   public int y;
   public int WIDTH;
   private ScaledResolution res;
   public static final int HEIGHT = 25;
   private boolean clicked;
   public boolean selected;
   public float opacity;
   public int MIN_HEIGHT;
   public int MAX_HEIGHT;
   private float animation_one;
   private float animation_two;
   private float animation_three;
   private MouseInputHandler handler = new MouseInputHandler(0);

   public GuiAltSlot(String username, String password) {
      this.username = username;
      this.password = password;
      this.x = 12;
   }

   private void update() {
      this.res = new ScaledResolution(Minecraft.getMinecraft());
   }

   public void drawScreen(int mouseX, int mouseY) {
      if(this.y <= this.MAX_HEIGHT - 25) {
         if(this.y >= this.MIN_HEIGHT) {
            FontRenderer font = Minecraft.getMinecraft().fontRendererObj;
            this.update();
            int lightGray = -15066598;
            byte stripeWidth = 2;
            int var10001 = this.y + 1;
            int var10002 = this.WIDTH - 2;
            int var10003 = this.y + 25 - 1;
            ClientUtil var10004 = ClientUtil.INSTANCE;
            Gui.drawRect(this.x, var10001, var10002, var10003, ClientUtil.reAlpha(251658240, 0.025F * this.opacity));
            Gui.drawRect(this.x - stripeWidth, this.y + 1, this.x, this.y - 1 + 25, FlatColors.BLUE.c);
            if(this.isHovering(mouseX, mouseY)) {
               var10001 = this.y + 1;
               var10002 = this.WIDTH - 2;
               var10003 = this.y + 25 - 1;
               var10004 = ClientUtil.INSTANCE;
               Gui.drawRect(this.x, var10001, var10002, var10003, ClientUtil.reAlpha(251658240, 0.2F * this.opacity));
            }

            String text = this.username;
            String password = this.password;
            UnicodeFontRenderer var10000 = Client.fontManager.tahoma20;
            String var17 = text + (password.equalsIgnoreCase("OfflineAccountName")?" (Cracked)":" (Premium)");
            float var20 = (float)(this.WIDTH - 110) / 2.0F;
            float var21 = (float)(this.y + 10 - font.FONT_HEIGHT / 2);
            var10004 = ClientUtil.INSTANCE;
            var10000.drawString(var17, var20, var21, ClientUtil.reAlpha(Colors.WHITE.c, this.opacity));
            int boxX = this.x + this.WIDTH - 10;
            int boxY = this.y + 1;
            byte boxSize = 25;
            byte size = 5;
            float var15 = (float)boxX;
            float var18 = (float)boxY;
            var20 = (float)(boxX + boxSize - 1);
            var21 = (float)(boxY + boxSize - 1);
            var10004 = ClientUtil.INSTANCE;
            RenderUtil.drawRect(var15, var18, var20, var21, ClientUtil.reAlpha(FlatColors.RED.c, 0.7F));
            Client.fontManager.tahoma17.drawString("DEL", (float)boxX + (float)((boxSize - size) / 2) - 5.0F, (float)(boxY + boxSize - 1 - 16), -1);
            boolean hoveringCross = mouseX >= boxX && mouseX <= boxX + boxSize && mouseY >= boxY && mouseY <= boxY + boxSize && mouseY < this.res.getScaledHeight() - 35;
            RenderUtil var19 = RenderUtil.INSTANCE;
            this.animation_one = (float)RenderUtil.getAnimationState((double)this.animation_one, (double)(hoveringCross?1:0), 5.0D);
            var15 = (float)boxX;
            var18 = (float)boxY;
            var20 = (float)(boxX + boxSize - 1);
            var21 = (float)(boxY + boxSize - 1);
            var10004 = ClientUtil.INSTANCE;
            RenderUtil.drawRect(var15, var18, var20, var21, ClientUtil.reAlpha(Colors.BLACK.c, 0.25F * this.animation_one));
            boolean size1 = true;
            var15 = (float)(boxX + boxSize);
            var18 = (float)boxY;
            var20 = (float)((boxX += boxSize + 1) + boxSize - 2 + 25);
            var21 = (float)(boxY + boxSize - 1);
            var10004 = ClientUtil.INSTANCE;
            RenderUtil.drawRect(var15, var18, var20, var21, ClientUtil.reAlpha(FlatColors.GREEN.c, 0.7F));
            Client.fontManager.tahoma17.drawString("LOGIN", (float)(boxX += boxSize + 1) + (float)boxSize - 41.0F, (float)(boxY + boxSize - 1 - 16), -1);
            boolean hoveringLogin = mouseX >= boxX - 25 && mouseX <= boxX + boxSize && mouseY >= boxY && mouseY <= boxY + boxSize;
            var19 = RenderUtil.INSTANCE;
            this.animation_two = (float)RenderUtil.getAnimationState((double)this.animation_two, (double)(hoveringLogin?1:0), 5.0D);
            int var16 = boxX - 27;
            var10002 = boxX + boxSize - 3;
            var10003 = boxY + boxSize - 1;
            var10004 = ClientUtil.INSTANCE;
            Gui.drawRect(var16, boxY, var10002, var10003, ClientUtil.reAlpha(Colors.BLACK.c, 0.25F * this.animation_two));
            if(this.handler.canExcecute()) {
               if(hoveringCross) {
                  GuiAltManager.toDelete.add(new GuiAltSlot(this.username, this.password));
               } else if(hoveringLogin) {
                  AltLogin.login(this.username, this.password);
               }
            }

         }
      }
   }

   public boolean isHovering(int mouseX, int mouseY) {
      return this.y > this.MAX_HEIGHT - 25?false:mouseY > this.y && mouseY <= this.y + 25 && mouseX >= this.x && mouseX <= this.WIDTH && mouseY <= this.MAX_HEIGHT - 25 && mouseY >= this.MIN_HEIGHT + 25;
   }

   public String getUsername() {
      return this.username;
   }

   public String getPassword() {
      return this.password;
   }

   public void setUsername(String username) {
      this.username = username;
   }

   public void setPassword(String password) {
      this.password = password;
   }
}
