package space.clowdy.ui;

import com.mojang.blaze3d.matrix.MatrixStack;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.util.text.StringTextComponent;
import space.clowdy.modules.Category;

public class ModuleScreen extends Screen {
     public List categoryPanels = new ArrayList();

     public void mouseMoved(double double1, double double2) {
          super.mouseMoved(double1, double2);
     }

     public void render(MatrixStack matrixStack, int integer2, int integer3, float float4) {
          this.renderBackground(matrixStack);
          Iterator var5 = this.categoryPanels.iterator();

          while(var5.hasNext()) {
               CategoryPanel jacleen7 = (CategoryPanel)var5.next();
               jacleen7.render(matrixStack, integer2, integer3, float4);
          }

          super.render(matrixStack, integer2, integer3, float4);
     }

     public boolean keyPressed(int integer1, int integer2, int integer3) {
          Iterator var4 = this.categoryPanels.iterator();

          while(var4.hasNext()) {
               CategoryPanel jacleen6 = (CategoryPanel)var4.next();
               jacleen6.keyPressed(integer1);
          }

          return super.keyPressed(integer1, integer2, integer3);
     }

     public void init() {
     }

     public boolean mouseReleased(double double1, double double2, int integer) {
          Iterator var6 = this.categoryPanels.iterator();

          while(var6.hasNext()) {
               CategoryPanel jacleen8 = (CategoryPanel)var6.next();
               jacleen8.mouseReleased((int)double1, (int)double2, integer);
          }

          return super.mouseReleased(double1, double2, integer);
     }

     public ModuleScreen() {
          super(new StringTextComponent("4x1"));
          int integer2 = 10;
          int integer3 = 10;
          Category[] var3 = Category.clone();
          int var4 = var3.length;

          for(int var5 = 0; var5 < var4; ++var5) {
               Category laquita7 = var3[var5];
               this.categoryPanels.add(new CategoryPanel(integer3, integer2, 100, 14, laquita7));
               integer3 += 90;
          }

     }

     public boolean mouseDragged(double double1, double double2, int integer) {
          Iterator var6 = this.categoryPanels.iterator();

          while(var6.hasNext()) {
               CategoryPanel jacleen8 = (CategoryPanel)var6.next();
               jacleen8.mouseDragged((int)double1, (int)double2, integer);
          }

          return super.mouseClicked(double1, double2, integer);
     }

     public void closeScreen() {
          super.closeScreen();
     }

     public boolean mouseDragged(double double1, double double2, int integer, double double4, double double5) {
          return super.mouseDragged(double1, double2, integer, double4, double5);
     }
}
