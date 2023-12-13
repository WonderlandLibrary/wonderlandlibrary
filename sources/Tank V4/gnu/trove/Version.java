package gnu.trove;

public class Version {
   public static void main(String[] var0) {
      System.out.println(getVersion());
   }

   public static String getVersion() {
      String var0 = Version.class.getPackage().getImplementationVersion();
      return var0 != null ? "trove4j version " + var0 : "Sorry no Implementation-Version manifest attribute available";
   }
}
