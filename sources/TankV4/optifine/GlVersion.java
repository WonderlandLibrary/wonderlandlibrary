package optifine;

public class GlVersion {
   private int major;
   private int minor;
   private int release;
   private String suffix;

   public GlVersion(int var1, int var2) {
      this(var1, var2, 0);
   }

   public GlVersion(int var1, int var2, int var3) {
      this(var1, var2, var3, (String)null);
   }

   public GlVersion(int var1, int var2, int var3, String var4) {
      this.major = var1;
      this.minor = var2;
      this.release = var3;
      this.suffix = var4;
   }

   public int getMajor() {
      return this.major;
   }

   public int getMinor() {
      return this.minor;
   }

   public int getRelease() {
      return this.release;
   }

   public int toInt() {
      return this.minor > 9 ? this.major * 100 + this.minor : (this.release > 9 ? this.major * 100 + this.minor * 10 + 9 : this.major * 100 + this.minor * 10 + this.release);
   }

   public String toString() {
      return this.suffix == null ? this.major + "." + this.minor + "." + this.release : this.major + "." + this.minor + "." + this.release + this.suffix;
   }
}
