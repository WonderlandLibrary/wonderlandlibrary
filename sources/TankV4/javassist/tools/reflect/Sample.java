package javassist.tools.reflect;

public class Sample {
   private Metaobject _metaobject;
   private static ClassMetaobject _classobject;

   public Object trap(Object[] var1, int var2) throws Throwable {
      Metaobject var3 = this._metaobject;
      return var3 == null ? ClassMetaobject.invoke(this, var2, var1) : var3.trapMethodcall(var2, var1);
   }

   public static Object trapStatic(Object[] var0, int var1) throws Throwable {
      return _classobject.trapMethodcall(var1, var0);
   }

   public static Object trapRead(Object[] var0, String var1) {
      return var0[0] == null ? _classobject.trapFieldRead(var1) : ((Metalevel)var0[0])._getMetaobject().trapFieldRead(var1);
   }

   public static Object trapWrite(Object[] var0, String var1) {
      Metalevel var2 = (Metalevel)var0[0];
      if (var2 == null) {
         _classobject.trapFieldWrite(var1, var0[1]);
      } else {
         var2._getMetaobject().trapFieldWrite(var1, var0[1]);
      }

      return null;
   }
}
