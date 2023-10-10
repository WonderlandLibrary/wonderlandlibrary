package my.NewSnake.Tank.gui.click;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import my.NewSnake.Tank.gui.click.component.Console;
import my.NewSnake.Tank.gui.click.component.window.ModuleWindow;
import my.NewSnake.Tank.gui.click.component.window.Window;
import my.NewSnake.Tank.module.Module;
import my.NewSnake.Tank.module.ModuleManager;
import my.NewSnake.Tank.option.OptionManager;
import my.NewSnake.utils.ClientUtils;
import my.NewSnake.utils.FileUtils;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;

public class ClickGui extends GuiScreen {
   private static final File GUI_DIR = FileUtils.getConfigFile("Gui");
   private static Console console;
   private boolean binding;
   private CopyOnWriteArrayList windows = new CopyOnWriteArrayList();
   private static ClickGui instance;
   private static final float SCALE = 2.0F;

   public static ClickGui getInstance() {
      return instance;
   }

   public void onGuiClosed() {
      ModuleManager.save();
      OptionManager.save();
      super.onGuiClosed();
   }

   protected void mouseClicked(int var1, int var2, int var3) throws IOException {
      int var4 = (int)Math.round((double)((float)Mouse.getX() / 2.0F) * (1920.0D / (double)Display.getWidth()));
      int var5 = (int)Math.round((double)((float)(Display.getHeight() - Mouse.getY()) / 2.0F) * (1920.0D / (double)Display.getWidth()));
      Iterator var7 = this.getWindows().iterator();

      while(var7.hasNext()) {
         Window var6 = (Window)var7.next();
         var6.click(var4, var5, var3);
      }

      this.save();
   }

   public void drawScreen(int var1, int var2, float var3) {
      GlStateManager.pushMatrix();
      ScaledResolution var4 = new ScaledResolution(this.mc);
      float var5 = (float)var4.getScaleFactor() / (float)Math.pow((double)var4.getScaleFactor(), 2.0D) * 2.0F;
      var5 /= (float)(1920.0D / (double)Display.getWidth());
      GlStateManager.scale(var5, var5, var5);
      int var6 = (int)Math.round((double)((float)Mouse.getX() / 2.0F) * (1920.0D / (double)Display.getWidth()));
      int var7 = (int)Math.round((double)((float)(Display.getHeight() - Mouse.getY()) / 2.0F) * (1920.0D / (double)Display.getWidth()));
      Iterator var9 = this.windows.iterator();

      while(var9.hasNext()) {
         Window var8 = (Window)var9.next();
         var8.draw(var6, var7);
      }

      GlStateManager.popMatrix();
      GlStateManager.pushMatrix();
      GlStateManager.scale(1.0F, var5, 1.0F);
      console.draw(var6, var7);
      GlStateManager.popMatrix();
   }

   public CopyOnWriteArrayList getWindows() {
      return this.windows;
   }

   public Window getTopWindow(int var1, int var2) {
      ArrayList var3 = new ArrayList();
      Iterator var5 = this.windows.iterator();

      while(true) {
         ModuleWindow var4;
         do {
            if (!var5.hasNext()) {
               if (!var3.isEmpty()) {
                  var4 = (ModuleWindow)var3.get(var3.size() - 1);
                  this.windows.remove(var4);
                  this.windows.add(var4);
                  return var4;
               }

               return null;
            }

            var4 = (ModuleWindow)var5.next();
         } while(!var4.hovering(var1, var2) && !var4.getHandle().hovering(var1, var2));

         var3.add(var4);
      }
   }

   protected void mouseReleased(int var1, int var2, int var3) {
      int var4 = (int)Math.round((double)((float)Mouse.getX() / 2.0F) * (1920.0D / (double)Display.getWidth()));
      int var5 = (int)Math.round((double)((float)(Display.getHeight() - Mouse.getY()) / 2.0F) * (1920.0D / (double)Display.getWidth()));
      Iterator var7 = this.windows.iterator();

      while(var7.hasNext()) {
         Window var6 = (Window)var7.next();
         var6.release(var4, var5, var3);
      }

      this.save();
   }

   private ModuleWindow getWindow(String var1) {
      Iterator var3 = this.windows.iterator();

      while(var3.hasNext()) {
         ModuleWindow var2 = (ModuleWindow)var3.next();
         if (((Module.Category)var2.getParent()).name().equalsIgnoreCase(var1)) {
            return var2;
         }
      }

      return null;
   }

   protected void mouseClickMove(int var1, int var2, int var3, long var4) {
      int var6 = (int)Math.round((double)((float)Mouse.getX() / 2.0F) * (1920.0D / (double)Display.getWidth()));
      int var7 = (int)Math.round((double)((float)(Display.getHeight() - Mouse.getY()) / 2.0F) * (1920.0D / (double)Display.getWidth()));
      Iterator var9 = this.windows.iterator();

      while(var9.hasNext()) {
         Window var8 = (Window)var9.next();
         if (var8.getHandle() != null) {
            var8.getHandle().drag(var6, var7, var3);
         }
      }

      this.save();
   }

   public boolean isBinding() {
      return this.binding;
   }

   public void save() {
      ArrayList var1 = new ArrayList();
      Iterator var3 = this.windows.iterator();

      while(var3.hasNext()) {
         ModuleWindow var2 = (ModuleWindow)var3.next();
         String var4 = ((Module.Category)var2.getParent()).name();
         String var5 = "" + (int)var2.getX();
         String var6 = "" + (int)var2.getY();
         String var7 = Boolean.toString(var2.isExtended());
         var1.add(String.format("%s:%s:%s:%s", var4, var5, var6, var7));
      }

      FileUtils.write(GUI_DIR, var1, true);
   }

   public static void start() {
      instance = new ClickGui();
   }

   protected void keyTyped(char var1, int var2) throws IOException {
      if (!this.binding && !console.keyType(var2, var1)) {
         super.keyTyped(var1, var2);
      }

      Iterator var4 = this.windows.iterator();

      while(var4.hasNext()) {
         Window var3 = (Window)var4.next();
         var3.keyPress(var2, var1);
      }

   }

   public void load() {
      List var1 = FileUtils.read(GUI_DIR);
      Iterator var3 = var1.iterator();

      while(var3.hasNext()) {
         String var2 = (String)var3.next();
         String[] var4 = var2.split(":");
         String var5 = var4[0];
         String var6 = var4[1];
         String var7 = var4[2];
         String var8 = var4[3];
         ModuleWindow var9 = this.getWindow(var5);
         int var10 = Integer.parseInt(var6);
         int var11 = Integer.parseInt(var7);
         Boolean var12 = Boolean.parseBoolean(var8);
         var9.setStartOffset(new double[]{0.0D, 0.0D});
         var9.setDragging(true);
         if (var9.getHandle() != null) {
            var9.getHandle().drag(var10, var11, 0);
         }

         var9.setDragging(false);
         var9.setExtended(var12);
      }

   }

   private ClickGui() {
      double var1 = 20.0D;
      double var3 = 50.0D;
      double var5 = 0.0D;
      Module.Category[] var7;
      int var8 = (var7 = Module.Category.values()).length;

      for(int var9 = 0; var9 < var8; ++var9) {
         Module.Category var10 = var7[var9];
         var5 = Math.max((double)ClientUtils.clientFont().getStringWidth(var10.name()) * 1.1D, var5);
         Iterator var12 = ModuleManager.getModules().iterator();

         while(var12.hasNext()) {
            Module var11 = (Module)var12.next();
            if (var11.getCategory().equals(var10)) {
               var5 = Math.max(var5, (double)ClientUtils.clientFont().getStringWidth(var11.getDisplayName()));
            }
         }

         ModuleWindow var13 = new ModuleWindow(var10, var1, 50.0D, var5);
         this.windows.add(var13);
         var1 += 100.0D;
      }

      this.load();
      this.save();
      console = new Console();
   }

   public void setBinding(boolean var1) {
      this.binding = var1;
   }
}
