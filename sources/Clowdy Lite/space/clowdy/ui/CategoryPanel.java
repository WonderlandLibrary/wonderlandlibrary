package space.clowdy.ui;

import com.mojang.blaze3d.matrix.MatrixStack;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.IngameGui;
import space.clowdy.modules.Category;
import space.clowdy.modules.Module;
import space.clowdy.modules.ModuleManager;
import space.clowdy.utils.ColorUtils;
import space.clowdy.utils.GuiUtils;

public class CategoryPanel {
     public Minecraft mcInstance = Minecraft.getInstance();
     public int sells$;
     public int beyond$;
     public Category currentCategory;
     public int blair$;
     public boolean leonard$;
     public boolean initted = true;
     public int occurred$;
     public int european$;
     public List modules = new ArrayList();
     public int debian$;

     public void mouseDragged(int integer1, int integer2, int integer3) {
          if (GuiUtils.isHovered(integer1, integer2, this.sells$, this.european$, this.sells$ + this.blair$, this.european$ + this.debian$)) {
               if (integer3 == 0) {
                    this.occurred$ = integer1 - this.sells$;
                    this.beyond$ = integer2 - this.european$;
                    this.leonard$ = true;
               } else if (integer3 == 1) {
                    this.initted = !this.initted;
               }
          }

          if (this.initted) {
               Iterator var4 = this.modules.iterator();

               while(var4.hasNext()) {
                    ModulePanel janson6 = (ModulePanel)var4.next();
                    janson6.mouseDragged(integer1, integer2, integer3);
               }
          }

     }

     protected void keyPressed(int integer) {
          if (this.initted) {
               Iterator var2 = this.modules.iterator();

               while(var2.hasNext()) {
                    ModulePanel janson4 = (ModulePanel)var2.next();
                    janson4._practice(integer);
               }
          }

     }

     public void render(MatrixStack matrixStack, int integer2, int integer3, float float4) {
          if (this.leonard$) {
               this.sells$ = integer2 - this.occurred$;
               this.european$ = integer3 - this.beyond$;
          }

          IngameGui.fill(matrixStack, this.sells$ + 10 - 2, this.european$, this.sells$ + this.blair$ - 10 + 2, this.european$ + this.debian$, ColorUtils.clientColor.getRGB());
          this.mcInstance.fontRenderer.drawString(matrixStack, this.currentCategory.name(), (float)(this.sells$ + 15), (float)(this.european$ + this.debian$ / 2 - 4), -1);
          int integer6 = this.european$ + this.debian$ + 2;
          Iterator var6 = this.modules.iterator();

          while(var6.hasNext()) {
               ModulePanel janson8 = (ModulePanel)var6.next();
               janson8.x = this.sells$;
               janson8.y = integer6;
               integer6 += this.debian$ + 2;
               janson8.render(matrixStack, integer2, integer3, float4);
          }

     }

     public CategoryPanel(int integer1, int integer2, int integer3, int integer4, Category laquita) {
          this.sells$ = integer1;
          this.european$ = integer2;
          this.blair$ = integer3;
          this.debian$ = integer4;
          this.currentCategory = laquita;
          int integer7 = integer2 + integer4;
          Iterator var7 = ModuleManager.modules.iterator();

          while(var7.hasNext()) {
               Module domingo9 = (Module)var7.next();
               if (domingo9.category == laquita) {
                    this.modules.add(new ModulePanel(integer1, integer7, integer3, integer4, domingo9));
                    integer7 += integer4;
               }
          }

     }

     protected void mouseReleased(int integer1, int integer2, int integer3) {
          this.leonard$ = false;
          if (this.initted) {
               Iterator var4 = this.modules.iterator();

               while(var4.hasNext()) {
                    ModulePanel janson6 = (ModulePanel)var4.next();
                    janson6.mouseReleased(integer1, integer2, integer3);
               }
          }

     }
}
