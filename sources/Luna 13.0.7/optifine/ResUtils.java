package optifine;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.LinkedHashSet;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import net.minecraft.client.resources.AbstractResourcePack;
import net.minecraft.client.resources.DefaultResourcePack;
import net.minecraft.client.resources.IResourcePack;
import net.minecraft.util.ResourceLocation;

public class ResUtils
{
  public ResUtils() {}
  
  public static String[] collectFiles(String prefix, String suffix)
  {
    return collectFiles(new String[] { prefix }, new String[] { suffix });
  }
  
  public static String[] collectFiles(String[] prefixes, String[] suffixes)
  {
    LinkedHashSet setPaths = new LinkedHashSet();
    IResourcePack[] rps = Config.getResourcePacks();
    for (int paths = 0; paths < rps.length; paths++)
    {
      IResourcePack rp = rps[paths];
      String[] ps = collectFiles(rp, prefixes, suffixes, (String[])null);
      setPaths.addAll(Arrays.asList(ps));
    }
    String[] var7 = (String[])setPaths.toArray(new String[setPaths.size()]);
    return var7;
  }
  
  public static String[] collectFiles(IResourcePack rp, String prefix, String suffix, String[] defaultPaths)
  {
    return collectFiles(rp, new String[] { prefix }, new String[] { suffix }, defaultPaths);
  }
  
  public static String[] collectFiles(IResourcePack rp, String[] prefixes, String[] suffixes)
  {
    return collectFiles(rp, prefixes, suffixes, (String[])null);
  }
  
  public static String[] collectFiles(IResourcePack rp, String[] prefixes, String[] suffixes, String[] defaultPaths)
  {
    if ((rp instanceof DefaultResourcePack)) {
      return collectFilesFixed(rp, defaultPaths);
    }
    if (!(rp instanceof AbstractResourcePack)) {
      return new String[0];
    }
    AbstractResourcePack arp = (AbstractResourcePack)rp;
    File tpFile = arp.resourcePackFile;
    return tpFile.isFile() ? collectFilesZIP(tpFile, prefixes, suffixes) : tpFile.isDirectory() ? collectFilesFolder(tpFile, "", prefixes, suffixes) : tpFile == null ? new String[0] : new String[0];
  }
  
  private static String[] collectFilesFixed(IResourcePack rp, String[] paths)
  {
    if (paths == null) {
      return new String[0];
    }
    ArrayList list = new ArrayList();
    for (int pathArr = 0; pathArr < paths.length; pathArr++)
    {
      String path = paths[pathArr];
      ResourceLocation loc = new ResourceLocation(path);
      if (rp.resourceExists(loc)) {
        list.add(path);
      }
    }
    String[] var6 = (String[])(String[])list.toArray(new String[list.size()]);
    return var6;
  }
  
  private static String[] collectFilesFolder(File tpFile, String basePath, String[] prefixes, String[] suffixes)
  {
    ArrayList list = new ArrayList();
    String prefixAssets = "assets/minecraft/";
    File[] files = tpFile.listFiles();
    if (files == null) {
      return new String[0];
    }
    for (int names = 0; names < files.length; names++)
    {
      File file = files[names];
      if (file.isFile())
      {
        String dirPath = basePath + file.getName();
        if (dirPath.startsWith(prefixAssets))
        {
          dirPath = dirPath.substring(prefixAssets.length());
          if ((StrUtils.startsWith(dirPath, prefixes)) && (StrUtils.endsWith(dirPath, suffixes))) {
            list.add(dirPath);
          }
        }
      }
      else if (file.isDirectory())
      {
        String dirPath = basePath + file.getName() + "/";
        String[] names1 = collectFilesFolder(file, dirPath, prefixes, suffixes);
        for (int n = 0; n < names1.length; n++)
        {
          String name = names1[n];
          list.add(name);
        }
      }
    }
    String[] var13 = (String[])(String[])list.toArray(new String[list.size()]);
    return var13;
  }
  
  private static String[] collectFilesZIP(File tpFile, String[] prefixes, String[] suffixes)
  {
    ArrayList list = new ArrayList();
    String prefixAssets = "assets/minecraft/";
    try
    {
      ZipFile e = new ZipFile(tpFile);
      Enumeration en = e.entries();
      while (en.hasMoreElements())
      {
        ZipEntry names = (ZipEntry)en.nextElement();
        String name = names.getName();
        if (name.startsWith(prefixAssets))
        {
          name = name.substring(prefixAssets.length());
          if ((StrUtils.startsWith(name, prefixes)) && (StrUtils.endsWith(name, suffixes))) {
            list.add(name);
          }
        }
      }
      e.close();
      return (String[])(String[])list.toArray(new String[list.size()]);
    }
    catch (IOException var9)
    {
      var9.printStackTrace();
    }
    return new String[0];
  }
}
