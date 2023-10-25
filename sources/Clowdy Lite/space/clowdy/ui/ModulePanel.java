package space.clowdy.ui;

import com.mojang.blaze3d.matrix.MatrixStack;
import java.awt.Color;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.IngameGui;
import space.clowdy.modules.Module;
import space.clowdy.utils.ColorUtils;
import space.clowdy.utils.GuiUtils;

public class ModulePanel {
     public int width;
     public int y;
     public Minecraft mc = Minecraft.getInstance();
     public int height;
     public Module module;
     public int x;
     public boolean binding;

     public void mouseDragged(int integer1, int integer2, int integer3) {
          if (GuiUtils.isHovered(integer1, integer2, this.x, this.y, this.x + this.width, this.y + this.height)) {
               if (integer3 == 0) {
                    this.module.toggle();
               } else if (integer3 == 2) {
                    this.binding = !this.binding;
               }
          }

     }

     public void render(MatrixStack matrixStack, int integer2, int integer3, float float4) {
          IngameGui.fill(matrixStack, this.x + 10, this.y, this.x + this.width - 10, this.y + this.height, (new Color(1056964608, true)).hashCode());
          IngameGui.fill(matrixStack, this.x + 10, this.y, (int)((double)(this.x + 10) + 2.5D), this.y + this.height, ColorUtils.clientColor.getRGB());
          this.mc.fontRenderer.drawString(matrixStack, this.binding ? "{Press}" : this.module.name, (float)(this.x + this.width / 2 - this.mc.fontRenderer.getStringWidth(!this.binding ? this.module.name : "{Press}") / 2), (float)(this.y + this.height / 2 - 4), this.module.isEnabled() ? ColorUtils.clientColor.getRGB() : ColorUtils.useless);
     }

     protected void mouseReleased(int integer1, int integer2, int integer3) {
     }

     public ModulePanel(int integer1, int integer2, int integer3, int integer4, Module domingo) {
          this.x = integer1;
          this.y = integer2;
          this.width = integer3;
          this.height = integer4;
          this.module = domingo;
     }

     protected void _practice(int integer) {
          if (this.binding) {
               this.module.key = integer;
               this.binding = false;
          }

     }
}
