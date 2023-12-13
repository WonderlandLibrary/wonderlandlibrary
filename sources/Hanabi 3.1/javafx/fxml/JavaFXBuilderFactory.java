package javafx.fxml;

import com.sun.javafx.fxml.builder.JavaFXFontBuilder;
import com.sun.javafx.fxml.builder.JavaFXImageBuilder;
import com.sun.javafx.fxml.builder.JavaFXSceneBuilder;
import com.sun.javafx.fxml.builder.ProxyBuilder;
import com.sun.javafx.fxml.builder.TriangleMeshBuilder;
import com.sun.javafx.fxml.builder.URLBuilder;
import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.ConditionalFeature;
import javafx.application.Platform;
import javafx.beans.NamedArg;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.shape.TriangleMesh;
import javafx.scene.text.Font;
import javafx.util.Builder;
import javafx.util.BuilderFactory;
import sun.reflect.misc.ConstructorUtil;

public final class JavaFXBuilderFactory implements BuilderFactory {
   private final JavaFXBuilder NO_BUILDER;
   private final Map builders;
   private final ClassLoader classLoader;
   private final boolean alwaysUseBuilders;
   private final boolean webSupported;

   public JavaFXBuilderFactory() {
      this(FXMLLoader.getDefaultClassLoader(), false);
   }

   /** @deprecated */
   public JavaFXBuilderFactory(boolean var1) {
      this(FXMLLoader.getDefaultClassLoader(), var1);
   }

   public JavaFXBuilderFactory(ClassLoader var1) {
      this(var1, false);
   }

   /** @deprecated */
   public JavaFXBuilderFactory(ClassLoader var1, boolean var2) {
      this.NO_BUILDER = new JavaFXBuilder();
      this.builders = new HashMap();
      if (var1 == null) {
         throw new NullPointerException();
      } else {
         this.classLoader = var1;
         this.alwaysUseBuilders = var2;
         this.webSupported = Platform.isSupported(ConditionalFeature.WEB);
      }
   }

   public Builder getBuilder(Class var1) {
      Object var2;
      if (var1 == Scene.class) {
         var2 = new JavaFXSceneBuilder();
      } else if (var1 == Font.class) {
         var2 = new JavaFXFontBuilder();
      } else if (var1 == Image.class) {
         var2 = new JavaFXImageBuilder();
      } else if (var1 == URL.class) {
         var2 = new URLBuilder(this.classLoader);
      } else if (var1 == TriangleMesh.class) {
         var2 = new TriangleMeshBuilder();
      } else if (this.scanForConstructorAnnotations(var1)) {
         var2 = new ProxyBuilder(var1);
      } else {
         Builder var3 = null;
         JavaFXBuilder var4 = (JavaFXBuilder)this.builders.get(var1);
         if (var4 != this.NO_BUILDER) {
            if (var4 == null) {
               boolean var5;
               try {
                  ConstructorUtil.getConstructor(var1, new Class[0]);
                  if (this.alwaysUseBuilders) {
                     throw new Exception();
                  }

                  var5 = true;
               } catch (Exception var8) {
                  var5 = false;
               }

               if (!var5 || this.webSupported && var1.getName().equals("javafx.scene.web.WebView")) {
                  try {
                     var4 = this.createTypeBuilder(var1);
                  } catch (ClassNotFoundException var7) {
                  }
               }

               this.builders.put(var1, var4 == null ? this.NO_BUILDER : var4);
            }

            if (var4 != null) {
               var3 = var4.createBuilder();
            }
         }

         var2 = var3;
      }

      return (Builder)var2;
   }

   JavaFXBuilder createTypeBuilder(Class var1) throws ClassNotFoundException {
      JavaFXBuilder var2 = null;
      Class var3 = this.classLoader.loadClass(var1.getName() + "Builder");

      try {
         var2 = new JavaFXBuilder(var3);
      } catch (Exception var5) {
         Logger.getLogger(JavaFXBuilderFactory.class.getName()).log(Level.WARNING, "Failed to instantiate JavaFXBuilder for " + var3, var5);
      }

      if (!this.alwaysUseBuilders) {
         Logger.getLogger(JavaFXBuilderFactory.class.getName()).log(Level.FINER, "class {0} requires a builder.", var1);
      }

      return var2;
   }

   private boolean scanForConstructorAnnotations(Class var1) {
      Constructor[] var2 = ConstructorUtil.getConstructors(var1);
      Constructor[] var3 = var2;
      int var4 = var2.length;

      for(int var5 = 0; var5 < var4; ++var5) {
         Constructor var6 = var3[var5];
         Annotation[][] var7 = var6.getParameterAnnotations();

         for(int var8 = 0; var8 < var6.getParameterTypes().length; ++var8) {
            Annotation[] var9 = var7[var8];
            int var10 = var9.length;

            for(int var11 = 0; var11 < var10; ++var11) {
               Annotation var12 = var9[var11];
               if (var12 instanceof NamedArg) {
                  return true;
               }
            }
         }
      }

      return false;
   }
}
