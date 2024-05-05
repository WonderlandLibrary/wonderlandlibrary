package team.massacre.api.module;

public enum Category {
   COMBAT("Combat"),
   MOVEMENT("Movement"),
   PLAYER("Player"),
   MISCELLANEOUS("Miscellaneous"),
   RENDER("Render"),
   WORLD("World");

   public String render;
   public int elementIndex;

   private Category(String render) {
      this.render = render;
   }
}
