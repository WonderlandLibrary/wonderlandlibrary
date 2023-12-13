package org.reflections.vfs;

import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.JarURLConnection;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.jar.JarFile;
import javax.annotation.Nullable;
import org.reflections.Reflections;
import org.reflections.ReflectionsException;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.Utils;

public abstract class Vfs {
   private static List defaultUrlTypes = Lists.newArrayList((Object[])Vfs.DefaultUrlTypes.values());

   public static List getDefaultUrlTypes() {
      return defaultUrlTypes;
   }

   public static void setDefaultURLTypes(List var0) {
      defaultUrlTypes = var0;
   }

   public static void addDefaultURLTypes(Vfs.UrlType var0) {
      defaultUrlTypes.add(0, var0);
   }

   public static Vfs.Dir fromURL(URL var0) {
      return fromURL(var0, defaultUrlTypes);
   }

   public static Vfs.Dir fromURL(URL var0, List var1) {
      Iterator var2 = var1.iterator();

      while(var2.hasNext()) {
         Vfs.UrlType var3 = (Vfs.UrlType)var2.next();

         try {
            if (var3.matches(var0)) {
               Vfs.Dir var4 = var3.createDir(var0);
               if (var4 != null) {
                  return var4;
               }
            }
         } catch (Throwable var5) {
            if (Reflections.log != null) {
               Reflections.log.warn("could not create Dir using " + var3 + " from url " + var0.toExternalForm() + ". skipping.", var5);
            }
         }
      }

      throw new ReflectionsException("could not create Vfs.Dir from url, no matching UrlType was found [" + var0.toExternalForm() + "]\n" + "either use fromURL(final URL url, final List<UrlType> urlTypes) or " + "use the static setDefaultURLTypes(final List<UrlType> urlTypes) or addDefaultURLTypes(UrlType urlType) " + "with your specialized UrlType.");
   }

   public static Vfs.Dir fromURL(URL var0, Vfs.UrlType... var1) {
      return fromURL(var0, (List)Lists.newArrayList((Object[])var1));
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

   public static Iterable findFiles(Collection var0, Predicate var1) {
      Object var2 = new ArrayList();
      Iterator var3 = var0.iterator();

      while(var3.hasNext()) {
         URL var4 = (URL)var3.next();

         try {
            var2 = Iterables.concat((Iterable)var2, Iterables.filter(new Iterable(var4) {
               final URL val$url;

               {
                  this.val$url = var1;
               }

               public Iterator iterator() {
                  return Vfs.fromURL(this.val$url).getFiles().iterator();
               }
            }, var1));
         } catch (Throwable var6) {
            if (Reflections.log != null) {
               Reflections.log.error("could not findFiles for url. continuing. [" + var4 + "]", var6);
            }
         }
      }

      return (Iterable)var2;
   }

   @Nullable
   public static java.io.File getFile(URL var0) {
      java.io.File var1;
      String var2;
      try {
         var2 = var0.toURI().getSchemeSpecificPart();
         if ((var1 = new java.io.File(var2)).exists()) {
            return var1;
         }
      } catch (URISyntaxException var6) {
      }

      try {
         var2 = URLDecoder.decode(var0.getPath(), "UTF-8");
         if (var2.contains(".jar!")) {
            var2 = var2.substring(0, var2.lastIndexOf(".jar!") + 4);
         }

         if ((var1 = new java.io.File(var2)).exists()) {
            return var1;
         }
      } catch (UnsupportedEncodingException var5) {
      }

      try {
         var2 = var0.toExternalForm();
         if (var2.startsWith("jar:")) {
            var2 = var2.substring(4);
         }

         if (var2.startsWith("wsjar:")) {
            var2 = var2.substring(6);
         }

         if (var2.startsWith("file:")) {
            var2 = var2.substring(5);
         }

         if (var2.contains(".jar!")) {
            var2 = var2.substring(0, var2.indexOf(".jar!") + 4);
         }

         if ((var1 = new java.io.File(var2)).exists()) {
            return var1;
         }

         var2 = var2.replace("%20", " ");
         if ((var1 = new java.io.File(var2)).exists()) {
            return var1;
         }
      } catch (Exception var4) {
      }

      return null;
   }

   public static enum DefaultUrlTypes implements Vfs.UrlType {
      jarFile {
         public boolean matches(URL var1) {
            return var1.getProtocol().equals("file") && var1.toExternalForm().contains(".jar");
         }

         public Vfs.Dir createDir(URL var1) throws Exception {
            return new ZipDir(new JarFile(Vfs.getFile(var1)));
         }
      },
      jarUrl {
         public boolean matches(URL var1) {
            return "jar".equals(var1.getProtocol()) || "zip".equals(var1.getProtocol()) || "wsjar".equals(var1.getProtocol());
         }

         public Vfs.Dir createDir(URL var1) throws Exception {
            try {
               URLConnection var2 = var1.openConnection();
               if (var2 instanceof JarURLConnection) {
                  return new ZipDir(((JarURLConnection)var2).getJarFile());
               }
            } catch (Throwable var3) {
            }

            java.io.File var4 = Vfs.getFile(var1);
            return var4 != null ? new ZipDir(new JarFile(var4)) : null;
         }
      },
      directory {
         public boolean matches(URL var1) {
            return var1.getProtocol().equals("file") && !var1.toExternalForm().contains(".jar") && Vfs.getFile(var1).isDirectory();
         }

         public Vfs.Dir createDir(URL var1) throws Exception {
            return new SystemDir(Vfs.getFile(var1));
         }
      },
      jboss_vfs {
         public boolean matches(URL var1) {
            return var1.getProtocol().equals("vfs");
         }

         public Vfs.Dir createDir(URL var1) throws Exception {
            Object var2 = var1.openConnection().getContent();
            Class var3 = ClasspathHelper.contextClassLoader().loadClass("org.jboss.vfs.VirtualFile");
            java.io.File var4 = (java.io.File)var3.getMethod("getPhysicalFile").invoke(var2);
            String var5 = (String)var3.getMethod("getName").invoke(var2);
            java.io.File var6 = new java.io.File(var4.getParentFile(), var5);
            if (!var6.exists() || !var6.canRead()) {
               var6 = var4;
            }

            return (Vfs.Dir)(var6.isDirectory() ? new SystemDir(var6) : new ZipDir(new JarFile(var6)));
         }
      },
      jboss_vfsfile {
         public boolean matches(URL var1) throws Exception {
            return "vfszip".equals(var1.getProtocol()) || "vfsfile".equals(var1.getProtocol());
         }

         public Vfs.Dir createDir(URL var1) throws Exception {
            return (new UrlTypeVFS()).createDir(var1);
         }
      },
      bundle {
         public boolean matches(URL var1) throws Exception {
            return var1.getProtocol().startsWith("bundle");
         }

         public Vfs.Dir createDir(URL var1) throws Exception {
            return Vfs.fromURL((URL)ClasspathHelper.contextClassLoader().loadClass("org.eclipse.core.runtime.FileLocator").getMethod("resolve", URL.class).invoke((Object)null, var1));
         }
      },
      jarInputStream {
         public boolean matches(URL var1) throws Exception {
            return var1.toExternalForm().contains(".jar");
         }

         public Vfs.Dir createDir(URL var1) throws Exception {
            return new JarInputDir(var1);
         }
      };

      private static final Vfs.DefaultUrlTypes[] $VALUES = new Vfs.DefaultUrlTypes[]{jarFile, jarUrl, directory, jboss_vfs, jboss_vfsfile, bundle, jarInputStream};

      private DefaultUrlTypes() {
      }

      DefaultUrlTypes(Object var3) {
         this();
      }
   }

   public interface UrlType {
      boolean matches(URL var1) throws Exception;

      Vfs.Dir createDir(URL var1) throws Exception;
   }

   public interface File {
      String getName();

      String getRelativePath();

      InputStream openInputStream() throws IOException;
   }

   public interface Dir {
      String getPath();

      Iterable getFiles();

      void close();
   }
}
