package net.minecraft.client.gui;

import net.minecraft.util.IProgressUpdate;

public class GuiScreenWorking extends GuiScreen implements IProgressUpdate {
   private int progress;
   private String field_146589_f = "";
   private String field_146591_a = "";
   private boolean doneWorking;

   public void setLoadingProgress(int var1) {
      this.progress = var1;
   }

   public void setDoneWorking() {
      this.doneWorking = true;
   }

   public void displaySavingString(String var1) {
      this.resetProgressAndMessage(var1);
   }

   public void displayLoadingString(String var1) {
      this.field_146589_f = var1;
      this.setLoadingProgress(0);
   }

   public void resetProgressAndMessage(String var1) {
      this.field_146591_a = var1;
      this.displayLoadingString("Working...");
   }

   public void drawScreen(int var1, int var2, float var3) {
      if (this.doneWorking) {
         if (!this.mc.func_181540_al()) {
            this.mc.displayGuiScreen((GuiScreen)null);
         }
      } else {
         this.drawDefaultBackground();
         this.drawCenteredString(this.fontRendererObj, this.field_146591_a, width / 2, 70, 16777215);
         this.drawCenteredString(this.fontRendererObj, this.field_146589_f + " " + this.progress + "%", width / 2, 90, 16777215);
         super.drawScreen(var1, var2, var3);
      }

   }
}
