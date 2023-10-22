package cn.hanabi.modules;

public enum Category {
   COMBAT("Combat"),
   MOVEMENT("Movement"),
   PLAYER("Player"),
   RENDER("Render"),
   WORLD("World"),
   GHOST("Ghost");

   private final String name;

   private Category(String name) {
      this.name = name;
   }

   public String toString() {
      return this.name;
   }
}
