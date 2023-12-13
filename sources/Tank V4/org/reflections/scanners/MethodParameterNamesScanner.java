package org.reflections.scanners;

import com.google.common.base.Joiner;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Iterator;
import javassist.bytecode.LocalVariableAttribute;
import javassist.bytecode.MethodInfo;
import org.reflections.adapters.MetadataAdapter;

public class MethodParameterNamesScanner extends AbstractScanner {
   public void scan(Object var1) {
      MetadataAdapter var2 = this.getMetadataAdapter();
      Iterator var3 = var2.getMethods(var1).iterator();

      while(true) {
         Object var4;
         String var5;
         LocalVariableAttribute var6;
         int var7;
         int var8;
         do {
            do {
               if (!var3.hasNext()) {
                  return;
               }

               var4 = var3.next();
               var5 = var2.getMethodFullKey(var1, var4);
            } while(!this.acceptResult(var5));

            var6 = (LocalVariableAttribute)((MethodInfo)var4).getCodeAttribute().getAttribute("LocalVariableTable");
            var7 = var6.tableLength();
            var8 = Modifier.isStatic(((MethodInfo)var4).getAccessFlags()) ? 0 : 1;
         } while(var8 >= var7);

         ArrayList var9 = new ArrayList(var7 - var8);

         while(var8 < var7) {
            var9.add(((MethodInfo)var4).getConstPool().getUtf8Info(var6.nameIndex(var8++)));
         }

         this.getStore().put(var5, Joiner.on(", ").join((Iterable)var9));
      }
   }
}
