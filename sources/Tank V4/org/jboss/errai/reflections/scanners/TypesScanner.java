package org.jboss.errai.reflections.scanners;

import com.google.common.collect.Lists;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import org.jboss.errai.reflections.ReflectionsException;
import org.jboss.errai.reflections.serializers.JavaCodeSerializer;
import org.jboss.errai.reflections.util.Utils;
import org.jboss.errai.reflections.vfs.Vfs;

public class TypesScanner extends AbstractScanner {
   private static final List javaCodeSerializerInterfaces = Lists.newArrayList((Object[])(JavaCodeSerializer.IElement.class.getName(), JavaCodeSerializer.IPackage.class.getName(), JavaCodeSerializer.IClass.class.getName(), JavaCodeSerializer.IField.class.getName(), JavaCodeSerializer.IMethod.class.getName()));

   public boolean acceptsInput(String var1) {
      return var1.endsWith(".class");
   }

   public void scan(Vfs.File var1) {
      InputStream var2 = null;

      try {
         var2 = var1.openInputStream();
         Object var3 = this.getMetadataAdapter().createClassObject(var2);
         this.scan(var3, var1);
      } catch (IOException var5) {
         throw new ReflectionsException("could not create class file from " + var1.getName(), var5);
      }

      Utils.close(var2);
   }

   private void scan(Object param1, Vfs.File param2) {
      // $FF: Couldn't be decompiled
   }

   public void scan(Object var1) {
      throw new UnsupportedOperationException("should not get here");
   }
}
