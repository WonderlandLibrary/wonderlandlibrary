package me.AveReborn.util;

public enum Colors {
   BLACK(-16711423),
   BLUE(-16723258),
   DARKBLUE(-15698006),
   GREEN(-9581017),
   DARKGREEN(-11231458),
   WHITE(-65794),
   AQUA(-14163205),
   DARKAQUA(-16548724),
   GREY(-6710887),
   DARKGREY(-12303292),
   RED(-43691),
   DARKRED(-7864320),
   ORANGE(-21931),
   DARKORANGE(-7846912),
   YELLOW(-171),
   DARKYELLOW(-7829504),
   MAGENTA(-43521),
   DARKMAGENTA(-7864184);

   public int c;

   private Colors(int co) {
      this.c = co;
   }
}
