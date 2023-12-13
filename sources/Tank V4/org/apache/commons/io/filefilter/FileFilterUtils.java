package org.apache.commons.io.filefilter;

import java.io.File;
import java.io.FileFilter;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import org.apache.commons.io.IOCase;

public class FileFilterUtils {
   private static final IOFileFilter cvsFilter = notFileFilter(and(directoryFileFilter(), nameFileFilter("CVS")));
   private static final IOFileFilter svnFilter = notFileFilter(and(directoryFileFilter(), nameFileFilter(".svn")));

   public static File[] filter(IOFileFilter var0, File... var1) {
      if (var0 == null) {
         throw new IllegalArgumentException("file filter is null");
      } else if (var1 == null) {
         return new File[0];
      } else {
         ArrayList var2 = new ArrayList();
         File[] var3 = var1;
         int var4 = var1.length;

         for(int var5 = 0; var5 < var4; ++var5) {
            File var6 = var3[var5];
            if (var6 == null) {
               throw new IllegalArgumentException("file array contains null");
            }

            if (var0.accept(var6)) {
               var2.add(var6);
            }
         }

         return (File[])var2.toArray(new File[var2.size()]);
      }
   }

   public static File[] filter(IOFileFilter var0, Iterable var1) {
      List var2 = filterList(var0, var1);
      return (File[])var2.toArray(new File[var2.size()]);
   }

   public static List filterList(IOFileFilter var0, Iterable var1) {
      return (List)filter(var0, var1, new ArrayList());
   }

   public static List filterList(IOFileFilter var0, File... var1) {
      File[] var2 = filter(var0, var1);
      return Arrays.asList(var2);
   }

   public static Set filterSet(IOFileFilter var0, File... var1) {
      File[] var2 = filter(var0, var1);
      return new HashSet(Arrays.asList(var2));
   }

   public static Set filterSet(IOFileFilter var0, Iterable var1) {
      return (Set)filter(var0, var1, new HashSet());
   }

   private static Collection filter(IOFileFilter var0, Iterable var1, Collection var2) {
      if (var0 == null) {
         throw new IllegalArgumentException("file filter is null");
      } else {
         if (var1 != null) {
            Iterator var3 = var1.iterator();

            while(var3.hasNext()) {
               File var4 = (File)var3.next();
               if (var4 == null) {
                  throw new IllegalArgumentException("file collection contains null");
               }

               if (var0.accept(var4)) {
                  var2.add(var4);
               }
            }
         }

         return var2;
      }
   }

   public static IOFileFilter prefixFileFilter(String var0) {
      return new PrefixFileFilter(var0);
   }

   public static IOFileFilter prefixFileFilter(String var0, IOCase var1) {
      return new PrefixFileFilter(var0, var1);
   }

   public static IOFileFilter suffixFileFilter(String var0) {
      return new SuffixFileFilter(var0);
   }

   public static IOFileFilter suffixFileFilter(String var0, IOCase var1) {
      return new SuffixFileFilter(var0, var1);
   }

   public static IOFileFilter nameFileFilter(String var0) {
      return new NameFileFilter(var0);
   }

   public static IOFileFilter nameFileFilter(String var0, IOCase var1) {
      return new NameFileFilter(var0, var1);
   }

   public static IOFileFilter directoryFileFilter() {
      return DirectoryFileFilter.DIRECTORY;
   }

   public static IOFileFilter fileFileFilter() {
      return FileFileFilter.FILE;
   }

   /** @deprecated */
   @Deprecated
   public static IOFileFilter andFileFilter(IOFileFilter var0, IOFileFilter var1) {
      return new AndFileFilter(var0, var1);
   }

   /** @deprecated */
   @Deprecated
   public static IOFileFilter orFileFilter(IOFileFilter var0, IOFileFilter var1) {
      return new OrFileFilter(var0, var1);
   }

   public static IOFileFilter and(IOFileFilter... var0) {
      return new AndFileFilter(toList(var0));
   }

   public static IOFileFilter or(IOFileFilter... var0) {
      return new OrFileFilter(toList(var0));
   }

   public static List toList(IOFileFilter... var0) {
      if (var0 == null) {
         throw new IllegalArgumentException("The filters must not be null");
      } else {
         ArrayList var1 = new ArrayList(var0.length);

         for(int var2 = 0; var2 < var0.length; ++var2) {
            if (var0[var2] == null) {
               throw new IllegalArgumentException("The filter[" + var2 + "] is null");
            }

            var1.add(var0[var2]);
         }

         return var1;
      }
   }

   public static IOFileFilter notFileFilter(IOFileFilter var0) {
      return new NotFileFilter(var0);
   }

   public static IOFileFilter trueFileFilter() {
      return TrueFileFilter.TRUE;
   }

   public static IOFileFilter falseFileFilter() {
      return FalseFileFilter.FALSE;
   }

   public static IOFileFilter asFileFilter(FileFilter var0) {
      return new DelegateFileFilter(var0);
   }

   public static IOFileFilter asFileFilter(FilenameFilter var0) {
      return new DelegateFileFilter(var0);
   }

   public static IOFileFilter ageFileFilter(long var0) {
      return new AgeFileFilter(var0);
   }

   public static IOFileFilter ageFileFilter(long var0, boolean var2) {
      return new AgeFileFilter(var0, var2);
   }

   public static IOFileFilter ageFileFilter(Date var0) {
      return new AgeFileFilter(var0);
   }

   public static IOFileFilter ageFileFilter(Date var0, boolean var1) {
      return new AgeFileFilter(var0, var1);
   }

   public static IOFileFilter ageFileFilter(File var0) {
      return new AgeFileFilter(var0);
   }

   public static IOFileFilter ageFileFilter(File var0, boolean var1) {
      return new AgeFileFilter(var0, var1);
   }

   public static IOFileFilter sizeFileFilter(long var0) {
      return new SizeFileFilter(var0);
   }

   public static IOFileFilter sizeFileFilter(long var0, boolean var2) {
      return new SizeFileFilter(var0, var2);
   }

   public static IOFileFilter sizeRangeFileFilter(long var0, long var2) {
      SizeFileFilter var4 = new SizeFileFilter(var0, true);
      SizeFileFilter var5 = new SizeFileFilter(var2 + 1L, false);
      return new AndFileFilter(var4, var5);
   }

   public static IOFileFilter magicNumberFileFilter(String var0) {
      return new MagicNumberFileFilter(var0);
   }

   public static IOFileFilter magicNumberFileFilter(String var0, long var1) {
      return new MagicNumberFileFilter(var0, var1);
   }

   public static IOFileFilter magicNumberFileFilter(byte[] var0) {
      return new MagicNumberFileFilter(var0);
   }

   public static IOFileFilter magicNumberFileFilter(byte[] var0, long var1) {
      return new MagicNumberFileFilter(var0, var1);
   }

   public static IOFileFilter makeCVSAware(IOFileFilter var0) {
      return var0 == null ? cvsFilter : and(var0, cvsFilter);
   }

   public static IOFileFilter makeSVNAware(IOFileFilter var0) {
      return var0 == null ? svnFilter : and(var0, svnFilter);
   }

   public static IOFileFilter makeDirectoryOnly(IOFileFilter var0) {
      return (IOFileFilter)(var0 == null ? DirectoryFileFilter.DIRECTORY : new AndFileFilter(DirectoryFileFilter.DIRECTORY, var0));
   }

   public static IOFileFilter makeFileOnly(IOFileFilter var0) {
      return (IOFileFilter)(var0 == null ? FileFileFilter.FILE : new AndFileFilter(FileFileFilter.FILE, var0));
   }
}
