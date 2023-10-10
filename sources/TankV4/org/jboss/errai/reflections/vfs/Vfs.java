package org.jboss.errai.reflections.vfs;

import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import org.jboss.errai.reflections.ReflectionsException;
import org.jboss.errai.reflections.util.Utils;

public abstract class Vfs {
   private static List defaultUrlTypes = Lists.newArrayList((Object[])Vfs.DefaultUrlTypes.values());

   public static List getDefaultUrlTypes() {
      return defaultUrlTypes;
   }

   public static void setDefaultURLTypes(List var0) {
      defaultUrlTypes = var0;
   }

   public static void addDefaultURLTypes(Vfs.UrlType var0) {
      defaultUrlTypes.add(var0);
   }

   public static Vfs.Dir fromURL(URL var0) {
      return fromURL(var0, defaultUrlTypes);
   }

   public static Vfs.Dir fromURL(URL var0, List var1) {
      Iterator var2 = var1.iterator();

      Vfs.UrlType var3;
      do {
         if (!var2.hasNext()) {
            throw new ReflectionsException("could not create Vfs.Dir from url, no matching UrlType was found [" + var0.toExternalForm() + "]\n" + "either use fromURL(final URL url, final List<UrlType> urlTypes) or " + "use the static setDefaultURLTypes(final List<UrlType> urlTypes) or addDefaultURLTypes(UrlType urlType) " + "with your specialized UrlType.");
         }

         var3 = (Vfs.UrlType)var2.next();
      } while(!var3.matches(var0));

      try {
         return var3.createDir(var0);
      } catch (Exception var5) {
         throw new ReflectionsException("could not create Dir using " + var3.getClass().getName() + " from url " + var0.toExternalForm());
      }
   }

   public static Vfs.Dir fromURL(URL var0, Vfs.UrlType... var1) {
      return fromURL(var0, (List)Lists.newArrayList((Object[])var1));
   }

   public static Iterable findFiles(Collection var0, Predicate var1) {
      Object var2 = new ArrayList();

      Iterable var5;
      for(Iterator var3 = var0.iterator(); var3.hasNext(); var2 = Iterables.concat((Iterable)var2, var5)) {
         URL var4 = (URL)var3.next();
         var5 = Iterables.filter(fromURL(var4).getFiles(), var1);
      }

      return (Iterable)var2;
   }

   public static Iterable findFiles(Collection var0, String var1, Predicate var2) {
      Predicate var3 = new Predicate(var1, var2) {
         final String val$packagePrefix;
         final Predicate val$nameFilter;

         {
            this.val$packagePrefix = var1;
            this.val$nameFilter = var2;
         }

         public boolean apply(Vfs.File var1) {
            String var2 = var1.getRelativePath();
            if (!var2.startsWith(this.val$packagePrefix)) {
               return false;
            } else {
               String var3 = var2.substring(var2.indexOf(this.val$packagePrefix) + this.val$packagePrefix.length());
               return !Utils.isEmpty(var3) && this.val$nameFilter.apply(var3.substring(1));
            }
         }

         public boolean apply(Object var1) {
            return this.apply((Vfs.File)var1);
         }
      };
      return findFiles(var0, var3);
   }

   public static String normalizePath(URL var0) {
      try {
         return var0.toURI().getPath();
      } catch (URISyntaxException var5) {
         String var2 = var0.getPath();
         String var3 = "";
         if (var2.startsWith("file:")) {
            var2 = var2.substring(5);
         }

         int var4 = var2.indexOf(33);
         if (var4 != -1) {
            var3 = var2.substring(var4 + 1);
            var2 = var2.substring(0, var4);
            if (var3.equals("/")) {
               var3 = "";
            }
         }

         return var2;
      }
   }

   public static enum DefaultUrlTypes implements Vfs.UrlType {
      jarfile {
         public boolean matches(URL var1) {
            return var1.getProtocol().equals("file") && var1.toExternalForm().endsWith(".jar");
         }

         public Vfs.Dir createDir(URL var1) {
            return new ZipDir(var1);
         }
      },
      jarUrl {
         public boolean matches(URL var1) {
            return var1.toExternalForm().contains(".jar!");
         }

         public Vfs.Dir createDir(URL var1) {
            return new ZipDir(var1);
         }
      },
      directory {
         public boolean matches(URL var1) {
            return var1.getProtocol().equals("file") && (new java.io.File(Vfs.normalizePath(var1))).isDirectory();
         }

         public Vfs.Dir createDir(URL var1) {
            return new SystemDir(var1);
         }
      },
      vfsfile {
         public boolean matches(URL var1) {
            return var1.getProtocol().equals("vfsfile") && var1.toExternalForm().endsWith(".jar");
         }

         public Vfs.Dir createDir(URL var1) {
            return new ZipDir(var1.toString().replaceFirst("vfsfile:", "file:"));
         }
      },
      vfszip {
         public boolean matches(URL var1) {
            return var1.getProtocol().equals("vfszip") && var1.toExternalForm().endsWith(".jar");
         }

         public Vfs.Dir createDir(URL var1) {
            return new ZipDir(var1.toString().replaceFirst("vfszip:", "file:"));
         }
      };

      private static final Vfs.DefaultUrlTypes[] $VALUES = new Vfs.DefaultUrlTypes[]{jarfile, jarUrl, directory, vfsfile, vfszip};

      private DefaultUrlTypes() {
      }

      DefaultUrlTypes(Object var3) {
         this();
      }
   }

   public interface UrlType {
      boolean matches(URL var1);

      Vfs.Dir createDir(URL var1);
   }

   public interface File {
      String getName();

      String getRelativePath();

      String getFullPath();

      InputStream openInputStream() throws IOException;
   }

   public interface Dir {
      String getPath();

      Iterable getFiles();

      void close();
   }
}
