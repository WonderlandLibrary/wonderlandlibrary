package net.minecraft.realms;

import java.util.Iterator;
import java.util.List;
import net.minecraft.util.IChatComponent;

public class DisconnectedRealmsScreen extends RealmsScreen {
   private IChatComponent reason;
   private String title;
   private final RealmsScreen parent;
   private List lines;
   private int textHeight;

   public void init() {
      Realms.setConnectedToRealms(false);
      this.buttonsClear();
      this.lines = this.fontSplit(this.reason.getFormattedText(), this.width() - 50);
      this.textHeight = this.lines.size() * this.fontLineHeight();
      this.buttonsAdd(newButton(0, this.width() / 2 - 100, this.height() / 2 + this.textHeight / 2 + this.fontLineHeight(), getLocalizedString("gui.back")));
   }

   public void keyPressed(char var1, int var2) {
      if (var2 == 1) {
         Realms.setScreen(this.parent);
      }

   }

   public void render(int var1, int var2, float var3) {
      this.renderBackground();
      this.drawCenteredString(this.title, this.width() / 2, this.height() / 2 - this.textHeight / 2 - this.fontLineHeight() * 2, 11184810);
      int var4 = this.height() / 2 - this.textHeight / 2;
      if (this.lines != null) {
         for(Iterator var6 = this.lines.iterator(); var6.hasNext(); var4 += this.fontLineHeight()) {
            String var5 = (String)var6.next();
            this.drawCenteredString(var5, this.width() / 2, var4, 16777215);
         }
      }

      super.render(var1, var2, var3);
   }

   public void buttonClicked(RealmsButton var1) {
      if (var1.id() == 0) {
         Realms.setScreen(this.parent);
      }

   }

   public DisconnectedRealmsScreen(RealmsScreen var1, String var2, IChatComponent var3) {
      this.parent = var1;
      this.title = getLocalizedString(var2);
      this.reason = var3;
   }
}
