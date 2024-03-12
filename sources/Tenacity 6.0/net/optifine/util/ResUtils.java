// 
// Decompiled by Procyon v0.5.36
// 

package net.optifine.util;

import java.io.InputStream;
import java.io.FileNotFoundException;
import java.util.Properties;
import java.util.Locale;
import java.util.Enumeration;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.List;
import net.minecraft.util.ResourceLocation;
import java.util.ArrayList;
import java.io.File;
import net.minecraft.client.resources.AbstractResourcePack;
import net.minecraft.client.resources.DefaultResourcePack;
import net.minecraft.client.resources.IResourcePack;
import java.util.Set;
import java.util.Collection;
import java.util.Arrays;
import net.minecraft.src.Config;
import java.util.LinkedHashSet;

public class ResUtils
{
    public static String[] collectFiles(final String prefix, final String suffix) {
        return collectFiles(new String[] { prefix }, new String[] { suffix });
    }
    
    public static String[] collectFiles(final String[] prefixes, final String[] suffixes) {
        final Set<String> set = new LinkedHashSet<String>();
        final IResourcePack[] airesourcepack = Config.getResourcePacks();
        for (int i = 0; i < airesourcepack.length; ++i) {
            final IResourcePack iresourcepack = airesourcepack[i];
            final String[] astring = collectFiles(iresourcepack, prefixes, suffixes, null);
            set.addAll(Arrays.asList(astring));
        }
        final String[] astring2 = set.toArray(new String[set.size()]);
        return astring2;
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
            Config.warn("Unknown resource pack type: " + rp);
            return new String[0];
        }
        final AbstractResourcePack abstractresourcepack = (AbstractResourcePack)rp;
        final File file1 = abstractresourcepack.resourcePackFile;
        if (file1 == null) {
            return new String[0];
        }
        if (file1.isDirectory()) {
            return collectFilesFolder(file1, "", prefixes, suffixes);
        }
        if (file1.isFile()) {
            return collectFilesZIP(file1, prefixes, suffixes);
        }
        Config.warn("Unknown resource pack file: " + file1);
        return new String[0];
    }
    
    private static String[] collectFilesFixed(final IResourcePack rp, final String[] paths) {
        if (paths == null) {
            return new String[0];
        }
        final List list = new ArrayList();
        for (int i = 0; i < paths.length; ++i) {
            final String s = paths[i];
            final ResourceLocation resourcelocation = new ResourceLocation(s);
            if (rp.resourceExists(resourcelocation)) {
                list.add(s);
            }
        }
        final String[] astring = list.toArray(new String[list.size()]);
        return astring;
    }
    
    private static String[] collectFilesFolder(final File tpFile, final String basePath, final String[] prefixes, final String[] suffixes) {
        final List list = new ArrayList();
        final String s = "assets/minecraft/";
        final File[] afile = tpFile.listFiles();
        if (afile == null) {
            return new String[0];
        }
        for (int i = 0; i < afile.length; ++i) {
            final File file1 = afile[i];
            if (file1.isFile()) {
                String s2 = basePath + file1.getName();
                if (s2.startsWith(s)) {
                    s2 = s2.substring(s.length());
                    if (StrUtils.startsWith(s2, prefixes) && StrUtils.endsWith(s2, suffixes)) {
                        list.add(s2);
                    }
                }
            }
            else if (file1.isDirectory()) {
                final String s3 = basePath + file1.getName() + "/";
                final String[] astring = collectFilesFolder(file1, s3, prefixes, suffixes);
                for (int j = 0; j < astring.length; ++j) {
                    final String s4 = astring[j];
                    list.add(s4);
                }
            }
        }
        final String[] astring2 = list.toArray(new String[list.size()]);
        return astring2;
    }
    
    private static String[] collectFilesZIP(final File tpFile, final String[] prefixes, final String[] suffixes) {
        final List list = new ArrayList();
        final String s = "assets/minecraft/";
        try {
            final ZipFile zipfile = new ZipFile(tpFile);
            final Enumeration enumeration = zipfile.entries();
            while (enumeration.hasMoreElements()) {
                final ZipEntry zipentry = enumeration.nextElement();
                String s2 = zipentry.getName();
                if (s2.startsWith(s)) {
                    s2 = s2.substring(s.length());
                    if (!StrUtils.startsWith(s2, prefixes) || !StrUtils.endsWith(s2, suffixes)) {
                        continue;
                    }
                    list.add(s2);
                }
            }
            zipfile.close();
            final String[] astring = list.toArray(new String[list.size()]);
            return astring;
        }
        catch (IOException ioexception) {
            ioexception.printStackTrace();
            return new String[0];
        }
    }
    
    private static boolean isLowercase(final String str) {
        return str.equals(str.toLowerCase(Locale.ROOT));
    }
    
    public static Properties readProperties(final String path, final String module) {
        final ResourceLocation resourcelocation = new ResourceLocation(path);
        try {
            final InputStream inputstream = Config.getResourceStream(resourcelocation);
            if (inputstream == null) {
                return null;
            }
            final Properties properties = new PropertiesOrdered();
            properties.load(inputstream);
            inputstream.close();
            Config.dbg("" + module + ": Loading " + path);
            return properties;
        }
        catch (FileNotFoundException var5) {
            return null;
        }
        catch (IOException var6) {
            Config.warn("" + module + ": Error reading " + path);
            return null;
        }
    }
    
    public static Properties readProperties(final InputStream in, final String module) {
        if (in == null) {
            return null;
        }
        try {
            final Properties properties = new PropertiesOrdered();
            properties.load(in);
            in.close();
            return properties;
        }
        catch (FileNotFoundException var3) {
            return null;
        }
        catch (IOException var4) {
            return null;
        }
    }
}
