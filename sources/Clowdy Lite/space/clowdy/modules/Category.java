package space.clowdy.modules;

public enum Category {
     DETECT,
     VISUAL,
     MISC,
     GHOST;

     public static Category[] clone() {
          return (ぬ[])categories.clone();
     }

     public static Category getCategoryByName(String name) {
          return (Category)Enum.valueOf(Category.class, name);
     }
}
