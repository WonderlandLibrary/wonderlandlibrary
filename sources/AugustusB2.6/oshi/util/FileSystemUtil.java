// 
// Decompiled by Procyon v0.5.36
// 

package oshi.util;

import java.util.Iterator;
import java.nio.file.FileSystem;
import java.util.ArrayList;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.PathMatcher;
import java.util.List;
import oshi.annotation.concurrent.ThreadSafe;

@ThreadSafe
public final class FileSystemUtil
{
    private static final String GLOB_PREFIX = "glob:";
    private static final String REGEX_PREFIX = "regex:";
    
    private FileSystemUtil() {
    }
    
    public static boolean isFileStoreExcluded(final String path, final String volume, final List<PathMatcher> pathIncludes, final List<PathMatcher> pathExcludes, final List<PathMatcher> volumeIncludes, final List<PathMatcher> volumeExcludes) {
        final Path p = Paths.get(path, new String[0]);
        final Path v = Paths.get(volume, new String[0]);
        return !matches(p, pathIncludes) && !matches(v, volumeIncludes) && (matches(p, pathExcludes) || matches(v, volumeExcludes));
    }
    
    public static List<PathMatcher> loadAndParseFileSystemConfig(final String configPropertyName) {
        final String config = GlobalConfig.get(configPropertyName, "");
        return parseFileSystemConfig(config);
    }
    
    public static List<PathMatcher> parseFileSystemConfig(final String config) {
        final FileSystem fs = FileSystems.getDefault();
        final List<PathMatcher> patterns = new ArrayList<PathMatcher>();
        for (String item : config.split(",")) {
            if (item.length() > 0) {
                if (!item.startsWith("glob:") && !item.startsWith("regex:")) {
                    item = "glob:" + item;
                }
                patterns.add(fs.getPathMatcher(item));
            }
        }
        return patterns;
    }
    
    public static boolean matches(final Path text, final List<PathMatcher> patterns) {
        for (final PathMatcher pattern : patterns) {
            if (pattern.matches(text)) {
                return true;
            }
        }
        return false;
    }
}
