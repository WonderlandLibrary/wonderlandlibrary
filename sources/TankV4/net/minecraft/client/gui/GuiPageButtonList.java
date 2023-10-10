package net.minecraft.client.gui;

import com.google.common.base.Objects;
import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import com.google.common.collect.Lists;
import java.util.Iterator;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.util.IntHashMap;

public class GuiPageButtonList extends GuiListExtended {
   private Gui field_178075_A;
   private final IntHashMap field_178073_v = new IntHashMap();
   private final List field_178074_u = Lists.newArrayList();
   private final List field_178072_w = Lists.newArrayList();
   private final GuiPageButtonList.GuiListEntry[][] field_178078_x;
   private GuiPageButtonList.GuiResponder field_178076_z;
   private int field_178077_y;

   protected int getScrollBarX() {
      return super.getScrollBarX() + 32;
   }

   public GuiListExtended.IGuiListEntry getListEntry(int var1) {
      return this.getListEntry(var1);
   }

   public Gui func_178056_g() {
      return this.field_178075_A;
   }

   private GuiSlider func_178067_a(int var1, int var2, GuiPageButtonList.GuiSlideEntry var3) {
      GuiSlider var4 = new GuiSlider(this.field_178076_z, var3.func_178935_b(), var1, var2, var3.func_178936_c(), var3.func_178943_e(), var3.func_178944_f(), var3.func_178942_g(), var3.func_178945_a());
      var4.visible = var3.func_178934_d();
      return var4;
   }

   private GuiTextField func_178068_a(int var1, int var2, GuiPageButtonList.EditBoxEntry var3) {
      GuiTextField var4 = new GuiTextField(var3.func_178935_b(), Minecraft.fontRendererObj, var1, var2, 150, 20);
      var4.setText(var3.func_178936_c());
      var4.func_175207_a(this.field_178076_z);
      var4.setVisible(var3.func_178934_d());
      var4.func_175205_a(var3.func_178950_a());
      return var4;
   }

   public void func_178071_h() {
      if (this.field_178077_y > 0) {
         this.func_181156_c(this.field_178077_y - 1);
      }

   }

   public GuiPageButtonList(Minecraft var1, int var2, int var3, int var4, int var5, int var6, GuiPageButtonList.GuiResponder var7, GuiPageButtonList.GuiListEntry[]... var8) {
      super(var1, var2, var3, var4, var5, var6);
      this.field_178076_z = var7;
      this.field_178078_x = var8;
      this.field_148163_i = false;
      this.func_178069_s();
      this.func_178055_t();
   }

   private GuiLabel func_178063_a(int var1, int var2, GuiPageButtonList.GuiLabelEntry var3, boolean var4) {
      GuiLabel var5;
      if (var4) {
         var5 = new GuiLabel(Minecraft.fontRendererObj, var3.func_178935_b(), var1, var2, this.width - var1 * 2, 20, -1);
      } else {
         var5 = new GuiLabel(Minecraft.fontRendererObj, var3.func_178935_b(), var1, var2, 150, 20, -1);
      }

      var5.visible = var3.func_178934_d();
      var5.func_175202_a(var3.func_178936_c());
      var5.setCentered();
      return var5;
   }

   public void func_178064_i() {
      if (this.field_178077_y < this.field_178078_x.length - 1) {
         this.func_181156_c(this.field_178077_y + 1);
      }

   }

   public int getSize() {
      return this.field_178074_u.size();
   }

   public int func_178059_e() {
      return this.field_178077_y;
   }

   private Gui func_178058_a(GuiPageButtonList.GuiListEntry var1, int var2, boolean var3) {
      return (Gui)(var1 instanceof GuiPageButtonList.GuiSlideEntry ? this.func_178067_a(this.width / 2 - 155 + var2, 0, (GuiPageButtonList.GuiSlideEntry)var1) : (var1 instanceof GuiPageButtonList.GuiButtonEntry ? this.func_178065_a(this.width / 2 - 155 + var2, 0, (GuiPageButtonList.GuiButtonEntry)var1) : (var1 instanceof GuiPageButtonList.EditBoxEntry ? this.func_178068_a(this.width / 2 - 155 + var2, 0, (GuiPageButtonList.EditBoxEntry)var1) : (var1 instanceof GuiPageButtonList.GuiLabelEntry ? this.func_178063_a(this.width / 2 - 155 + var2, 0, (GuiPageButtonList.GuiLabelEntry)var1, var3) : null))));
   }

   public void func_178062_a(char var1, int var2) {
      if (this.field_178075_A instanceof GuiTextField) {
         GuiTextField var3 = (GuiTextField)this.field_178075_A;
         int var6;
         if (!GuiScreen.isKeyComboCtrlV(var2)) {
            if (var2 == 15) {
               var3.setFocused(false);
               int var4 = this.field_178072_w.indexOf(this.field_178075_A);
               if (GuiScreen.isShiftKeyDown()) {
                  if (var4 == 0) {
                     var4 = this.field_178072_w.size() - 1;
                  } else {
                     --var4;
                  }
               } else if (var4 == this.field_178072_w.size() - 1) {
                  var4 = 0;
               } else {
                  ++var4;
               }

               this.field_178075_A = (Gui)this.field_178072_w.get(var4);
               var3 = (GuiTextField)this.field_178075_A;
               var3.setFocused(true);
               int var5 = var3.yPosition + this.slotHeight;
               var6 = var3.yPosition;
               if (var5 > this.bottom) {
                  this.amountScrolled += (float)(var5 - this.bottom);
               } else if (var6 < this.top) {
                  this.amountScrolled = (float)var6;
               }
            } else {
               var3.textboxKeyTyped(var1, var2);
            }
         } else {
            String var14 = GuiScreen.getClipboardString();
            String[] var13 = var14.split(";");
            var6 = this.field_178072_w.indexOf(this.field_178075_A);
            int var7 = var6;
            String[] var11 = var13;
            int var10 = var13.length;

            for(int var9 = 0; var9 < var10; ++var9) {
               String var8 = var11[var9];
               ((GuiTextField)this.field_178072_w.get(var7)).setText(var8);
               if (var7 == this.field_178072_w.size() - 1) {
                  var7 = 0;
               } else {
                  ++var7;
               }

               if (var7 == var6) {
                  break;
               }
            }
         }
      }

   }

   private void func_178055_t() {
      this.field_178074_u.clear();

      for(int var1 = 0; var1 < this.field_178078_x[this.field_178077_y].length; var1 += 2) {
         GuiPageButtonList.GuiListEntry var2 = this.field_178078_x[this.field_178077_y][var1];
         GuiPageButtonList.GuiListEntry var3 = var1 < this.field_178078_x[this.field_178077_y].length - 1 ? this.field_178078_x[this.field_178077_y][var1 + 1] : null;
         Gui var4 = (Gui)this.field_178073_v.lookup(var2.func_178935_b());
         Gui var5 = var3 != null ? (Gui)this.field_178073_v.lookup(var3.func_178935_b()) : null;
         GuiPageButtonList.GuiEntry var6 = new GuiPageButtonList.GuiEntry(var4, var5);
         this.field_178074_u.add(var6);
      }

   }

   public int func_178057_f() {
      return this.field_178078_x.length;
   }

   public GuiPageButtonList.GuiEntry getListEntry(int var1) {
      return (GuiPageButtonList.GuiEntry)this.field_178074_u.get(var1);
   }

   public void func_181155_a(boolean var1) {
      Iterator var3 = this.field_178074_u.iterator();

      while(var3.hasNext()) {
         GuiPageButtonList.GuiEntry var2 = (GuiPageButtonList.GuiEntry)var3.next();
         if (GuiPageButtonList.GuiEntry.access$0(var2) instanceof GuiButton) {
            ((GuiButton)GuiPageButtonList.GuiEntry.access$0(var2)).enabled = var1;
         }

         if (GuiPageButtonList.GuiEntry.access$1(var2) instanceof GuiButton) {
            ((GuiButton)GuiPageButtonList.GuiEntry.access$1(var2)).enabled = var1;
         }
      }

   }

   public int getListWidth() {
      return 400;
   }

   public void func_181156_c(int var1) {
      if (var1 != this.field_178077_y) {
         int var2 = this.field_178077_y;
         this.field_178077_y = var1;
         this.func_178055_t();
         this.func_178060_e(var2, var1);
         this.amountScrolled = 0.0F;
      }

   }

   public boolean mouseClicked(int var1, int var2, int var3) {
      boolean var4 = super.mouseClicked(var1, var2, var3);
      int var5 = this.getSlotIndexFromScreenCoords(var1, var2);
      if (var5 >= 0) {
         GuiPageButtonList.GuiEntry var6 = this.getListEntry(var5);
         if (this.field_178075_A != GuiPageButtonList.GuiEntry.access$2(var6) && this.field_178075_A != null && this.field_178075_A instanceof GuiTextField) {
            ((GuiTextField)this.field_178075_A).setFocused(false);
         }

         this.field_178075_A = GuiPageButtonList.GuiEntry.access$2(var6);
      }

      return var4;
   }

   private void func_178060_e(int var1, int var2) {
      GuiPageButtonList.GuiListEntry[] var6;
      int var5 = (var6 = this.field_178078_x[var1]).length;

      GuiPageButtonList.GuiListEntry var3;
      int var4;
      for(var4 = 0; var4 < var5; ++var4) {
         var3 = var6[var4];
         if (var3 != null) {
            this.func_178066_a((Gui)this.field_178073_v.lookup(var3.func_178935_b()), false);
         }
      }

      var5 = (var6 = this.field_178078_x[var2]).length;

      for(var4 = 0; var4 < var5; ++var4) {
         var3 = var6[var4];
         if (var3 != null) {
            this.func_178066_a((Gui)this.field_178073_v.lookup(var3.func_178935_b()), true);
         }
      }

   }

   private void func_178069_s() {
      GuiPageButtonList.GuiListEntry[][] var4;
      int var3 = (var4 = this.field_178078_x).length;

      for(int var2 = 0; var2 < var3; ++var2) {
         GuiPageButtonList.GuiListEntry[] var1 = var4[var2];

         for(int var5 = 0; var5 < var1.length; var5 += 2) {
            GuiPageButtonList.GuiListEntry var6 = var1[var5];
            GuiPageButtonList.GuiListEntry var7 = var5 < var1.length - 1 ? var1[var5 + 1] : null;
            Gui var8 = this.func_178058_a(var6, 0, var7 == null);
            Gui var9 = this.func_178058_a(var7, 160, var6 == null);
            GuiPageButtonList.GuiEntry var10 = new GuiPageButtonList.GuiEntry(var8, var9);
            this.field_178074_u.add(var10);
            if (var6 != null && var8 != null) {
               this.field_178073_v.addKey(var6.func_178935_b(), var8);
               if (var8 instanceof GuiTextField) {
                  this.field_178072_w.add((GuiTextField)var8);
               }
            }

            if (var7 != null && var9 != null) {
               this.field_178073_v.addKey(var7.func_178935_b(), var9);
               if (var9 instanceof GuiTextField) {
                  this.field_178072_w.add((GuiTextField)var9);
               }
            }
         }
      }

   }

   private void func_178066_a(Gui var1, boolean var2) {
      if (var1 instanceof GuiButton) {
         ((GuiButton)var1).visible = var2;
      } else if (var1 instanceof GuiTextField) {
         ((GuiTextField)var1).setVisible(var2);
      } else if (var1 instanceof GuiLabel) {
         ((GuiLabel)var1).visible = var2;
      }

   }

   private GuiListButton func_178065_a(int var1, int var2, GuiPageButtonList.GuiButtonEntry var3) {
      GuiListButton var4 = new GuiListButton(this.field_178076_z, var3.func_178935_b(), var1, var2, var3.func_178936_c(), var3.func_178940_a());
      var4.visible = var3.func_178934_d();
      return var4;
   }

   public Gui func_178061_c(int var1) {
      return (Gui)this.field_178073_v.lookup(var1);
   }

   public static class GuiEntry implements GuiListExtended.IGuiListEntry {
      private final Gui field_178029_b;
      private final Gui field_178030_c;
      private Gui field_178028_d;
      private final Minecraft field_178031_a = Minecraft.getMinecraft();

      static Gui access$2(GuiPageButtonList.GuiEntry var0) {
         return var0.field_178028_d;
      }

      public void mouseReleased(int var1, int var2, int var3, int var4, int var5, int var6) {
         this.func_178016_b(this.field_178029_b, var2, var3, var4);
         this.func_178016_b(this.field_178030_c, var2, var3, var4);
      }

      private boolean func_178026_a(Gui var1, int var2, int var3, int var4) {
         if (var1 == null) {
            return false;
         } else if (var1 instanceof GuiButton) {
            return this.func_178023_a((GuiButton)var1, var2, var3, var4);
         } else {
            if (var1 instanceof GuiTextField) {
               this.func_178018_a((GuiTextField)var1, var2, var3, var4);
            }

            return false;
         }
      }

      private void func_178027_a(GuiTextField var1, int var2, boolean var3) {
         var1.yPosition = var2;
         if (!var3) {
            var1.drawTextBox();
         }

      }

      private void func_178019_b(GuiButton var1, int var2, int var3, int var4) {
         var1.mouseReleased(var2, var3);
      }

      private void func_178025_a(GuiLabel var1, int var2, int var3, int var4, boolean var5) {
         var1.field_146174_h = var2;
         if (!var5) {
            var1.drawLabel(this.field_178031_a, var3, var4);
         }

      }

      private void func_178017_a(Gui var1, int var2, int var3, int var4, boolean var5) {
         if (var1 != null) {
            if (var1 instanceof GuiButton) {
               this.func_178024_a((GuiButton)var1, var2, var3, var4, var5);
            } else if (var1 instanceof GuiTextField) {
               this.func_178027_a((GuiTextField)var1, var2, var5);
            } else if (var1 instanceof GuiLabel) {
               this.func_178025_a((GuiLabel)var1, var2, var3, var4, var5);
            }
         }

      }

      public GuiEntry(Gui var1, Gui var2) {
         this.field_178029_b = var1;
         this.field_178030_c = var2;
      }

      private boolean func_178023_a(GuiButton var1, int var2, int var3, int var4) {
         boolean var5 = var1.mousePressed(this.field_178031_a, var2, var3);
         if (var5) {
            this.field_178028_d = var1;
         }

         return var5;
      }

      public Gui func_178022_a() {
         return this.field_178029_b;
      }

      static Gui access$1(GuiPageButtonList.GuiEntry var0) {
         return var0.field_178030_c;
      }

      public boolean mousePressed(int var1, int var2, int var3, int var4, int var5, int var6) {
         boolean var7 = this.func_178026_a(this.field_178029_b, var2, var3, var4);
         boolean var8 = this.func_178026_a(this.field_178030_c, var2, var3, var4);
         return var7 || var8;
      }

      static Gui access$0(GuiPageButtonList.GuiEntry var0) {
         return var0.field_178029_b;
      }

      private void func_178018_a(GuiTextField var1, int var2, int var3, int var4) {
         var1.mouseClicked(var2, var3, var4);
         if (var1.isFocused()) {
            this.field_178028_d = var1;
         }

      }

      private void func_178024_a(GuiButton var1, int var2, int var3, int var4, boolean var5) {
         var1.yPosition = var2;
         if (!var5) {
            var1.drawButton(this.field_178031_a, var3, var4);
         }

      }

      public void setSelected(int var1, int var2, int var3) {
         this.func_178017_a(this.field_178029_b, var3, 0, 0, true);
         this.func_178017_a(this.field_178030_c, var3, 0, 0, true);
      }

      private void func_178016_b(Gui var1, int var2, int var3, int var4) {
         if (var1 != null && var1 instanceof GuiButton) {
            this.func_178019_b((GuiButton)var1, var2, var3, var4);
         }

      }

      public Gui func_178021_b() {
         return this.field_178030_c;
      }

      public void drawEntry(int var1, int var2, int var3, int var4, int var5, int var6, int var7, boolean var8) {
         this.func_178017_a(this.field_178029_b, var3, var6, var7, false);
         this.func_178017_a(this.field_178030_c, var3, var6, var7, false);
      }
   }

   public static class GuiLabelEntry extends GuiPageButtonList.GuiListEntry {
      public GuiLabelEntry(int var1, String var2, boolean var3) {
         super(var1, var2, var3);
      }
   }

   public static class EditBoxEntry extends GuiPageButtonList.GuiListEntry {
      private final Predicate field_178951_a;

      public EditBoxEntry(int var1, String var2, boolean var3, Predicate var4) {
         super(var1, var2, var3);
         this.field_178951_a = (Predicate)Objects.firstNonNull(var4, Predicates.alwaysTrue());
      }

      public Predicate func_178950_a() {
         return this.field_178951_a;
      }
   }

   public static class GuiSlideEntry extends GuiPageButtonList.GuiListEntry {
      private final GuiSlider.FormatHelper field_178949_a;
      private final float field_178947_b;
      private final float field_178948_c;
      private final float field_178946_d;

      public float func_178942_g() {
         return this.field_178946_d;
      }

      public float func_178944_f() {
         return this.field_178948_c;
      }

      public float func_178943_e() {
         return this.field_178947_b;
      }

      public GuiSlider.FormatHelper func_178945_a() {
         return this.field_178949_a;
      }

      public GuiSlideEntry(int var1, String var2, boolean var3, GuiSlider.FormatHelper var4, float var5, float var6, float var7) {
         super(var1, var2, var3);
         this.field_178949_a = var4;
         this.field_178947_b = var5;
         this.field_178948_c = var6;
         this.field_178946_d = var7;
      }
   }

   public static class GuiListEntry {
      private final String field_178937_b;
      private final boolean field_178938_c;
      private final int field_178939_a;

      public GuiListEntry(int var1, String var2, boolean var3) {
         this.field_178939_a = var1;
         this.field_178937_b = var2;
         this.field_178938_c = var3;
      }

      public boolean func_178934_d() {
         return this.field_178938_c;
      }

      public String func_178936_c() {
         return this.field_178937_b;
      }

      public int func_178935_b() {
         return this.field_178939_a;
      }
   }

   public interface GuiResponder {
      void func_175321_a(int var1, boolean var2);

      void func_175319_a(int var1, String var2);

      void onTick(int var1, float var2);
   }

   public static class GuiButtonEntry extends GuiPageButtonList.GuiListEntry {
      private final boolean field_178941_a;

      public boolean func_178940_a() {
         return this.field_178941_a;
      }

      public GuiButtonEntry(int var1, String var2, boolean var3, boolean var4) {
         super(var1, var2, var3);
         this.field_178941_a = var4;
      }
   }
}
