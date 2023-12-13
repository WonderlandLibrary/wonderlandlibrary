package cn.hanabi.gui.superskidder.material.clickgui;

public class Category {
   public cn.hanabi.modules.Category moduleType;
   public boolean needRemove;
   public AnimationUtils rollAnim2 = new AnimationUtils();
   float anim;
   public boolean show;
   public float modsY2 = 0.0F;
   public float modsY3 = 0.0F;

   public Category(cn.hanabi.modules.Category mt, int i, boolean b) {
      this.moduleType = mt;
      this.anim = (float)i;
      this.show = b;
   }
}
