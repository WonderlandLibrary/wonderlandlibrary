package my.NewSnake.Tank.me.tireman.hexa.alts;

public final class Alt {
   private final String username;
   private String mask;
   private String password;

   public void setMask(String var1) {
      this.mask = var1;
   }

   public Alt(String var1, String var2, String var3) {
      this.mask = "";
      this.username = var1;
      this.password = var2;
      this.mask = var3;
   }

   public String getPassword() {
      return this.password;
   }

   public Alt(String var1, String var2) {
      this(var1, var2, "");
   }

   public String getMask() {
      return this.mask;
   }

   public void setPassword(String var1) {
      this.password = var1;
   }

   public String getUsername() {
      return this.username;
   }
}
