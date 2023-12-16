// 
// Decompiled by Procyon v0.5.36
// 

package optifine;

import java.util.Enumeration;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import net.minecraft.util.ResourceLocation;
import java.util.ArrayList;
import java.io.File;
import net.minecraft.client.resources.AbstractResourcePack;
import net.minecraft.client.resources.DefaultResourcePack;
import net.minecraft.client.resources.IResourcePack;
import java.util.Collection;
import java.util.Arrays;
import java.util.LinkedHashSet;

public class ResUtils
{
    public static String[] collectFiles(final String prefix, final String suffix) {
        return collectFiles(new String[] { prefix }, new String[] { suffix });
    }
    
    public static String[] collectFiles(final String[] prefixes, final String[] suffixes) {
        final LinkedHashSet setPaths = new LinkedHashSet();
        final IResourcePack[] rps = Config.getResourcePacks();
        for (int paths = 0; paths < rps.length; ++paths) {
            final IResourcePack rp = rps[paths];
            final String[] ps = collectFiles(rp, prefixes, suffixes, null);
            setPaths.addAll(Arrays.asList(ps));
        }
        final String[] var7 = (String[])setPaths.toArray(new String[setPaths.size()]);
        return var7;
    }
    
    public static String[] collectFiles(final IResourcePack rp, final String prefix, final String suffix, final String[] defaultPaths) {
        return collectFiles(rp, new String[] { prefix }, new String[] { suffix }, defaultPaths);
    }
    
    public static String[] collectFiles(final IResourcePack rp, final String[] prefixes, final String[] suffixes) {
        return collectFiles(rp, prefixes, suffixes, null);
    }
    
    public static String[] collectFiles(final IResourcePack rp, final String[] prefixes, final String[] suffixes, final String[] defaultPaths) {
        if (rp instanceof DefaultResourcePack) {
            return collectFilesFixed(rp, defaultPaths);
        }
        if (!(rp instanceof AbstractResourcePack)) {
            return new String[0];
        }
        final AbstractResourcePack arp = (AbstractResourcePack)rp;
        final File tpFile = arp.resourcePackFile;
        return (tpFile == null) ? new String[0] : (tpFile.isDirectory() ? collectFilesFolder(tpFile, "", prefixes, suffixes) : (tpFile.isFile() ? collectFilesZIP(tpFile, prefixes, suffixes) : new String[0]));
    }
    
    private static String[] collectFilesFixed(final IResourcePack rp, final String[] paths) {
        if (paths == null) {
            return new String[0];
        }
        final ArrayList list = new ArrayList();
        for (int pathArr = 0; pathArr < paths.length; ++pathArr) {
            final String path = paths[pathArr];
            final ResourceLocation loc = new ResourceLocation(path);
            if (rp.resourceExists(loc)) {
                list.add(path);
            }
        }
        final String[] var6 = list.toArray(new String[list.size()]);
        return var6;
    }
    
    private static String[] collectFilesFolder(final File tpFile, final String basePath, final String[] prefixes, final String[] suffixes) {
        final ArrayList list = new ArrayList();
        final String prefixAssets = "assets/minecraft/";
        final File[] files = tpFile.listFiles();
        if (files == null) {
            return new String[0];
        }
        for (int names = 0; names < files.length; ++names) {
            final File file = files[names];
            if (file.isFile()) {
                String dirPath = String.valueOf(basePath) + file.getName();
                if (dirPath.startsWith(prefixAssets)) {
                    dirPath = dirPath.substring(prefixAssets.length());
                    if (StrUtils.startsWith(dirPath, prefixes) && StrUtils.endsWith(dirPath, suffixes)) {
                        list.add(dirPath);
                    }
                }
            }
            else if (file.isDirectory()) {
                final String dirPath = String.valueOf(basePath) + file.getName() + "/";
                final String[] names2 = collectFilesFolder(file, dirPath, prefixes, suffixes);
                for (int n = 0; n < names2.length; ++n) {
                    final String name = names2[n];
                    list.add(name);
                }
            }
        }
        final String[] var13 = list.toArray(new String[list.size()]);
        return var13;
    }
    
    private static String[] collectFilesZIP(final File tpFile, final String[] prefixes, final String[] suffixes) {
        final ArrayList list = new ArrayList();
        final String prefixAssets = "assets/minecraft/";
        try {
            final ZipFile e = new ZipFile(tpFile);
            final Enumeration en = e.entries();
            while (en.hasMoreElements()) {
                final ZipEntry names = en.nextElement();
                String name = names.getName();
                if (name.startsWith(prefixAssets)) {
                    name = name.substring(prefixAssets.length());
                    if (!StrUtils.startsWith(name, prefixes) || !StrUtils.endsWith(name, suffixes)) {
                        continue;
                    }
                    list.add(name);
                }
            }
            e.close();
            final String[] names2 = list.toArray(new String[list.size()]);
            return names2;
        }
        catch (IOException var9) {
            var9.printStackTrace();
            return new String[0];
        }
    }
}
