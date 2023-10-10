// 
// Decompiled by Procyon v0.5.36
// 

package oshi.util;

import org.slf4j.LoggerFactory;
import java.io.InputStream;
import java.net.URL;
import java.util.Collections;
import java.util.stream.Collector;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.LinkedHashSet;
import java.util.Properties;
import java.util.Iterator;
import java.util.HashMap;
import java.util.Map;
import java.io.IOException;
import java.util.ArrayList;
import java.nio.file.Files;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;
import java.io.File;
import java.util.List;
import org.slf4j.Logger;
import oshi.annotation.concurrent.ThreadSafe;

@ThreadSafe
public final class FileUtil
{
    private static final Logger LOG;
    private static final String READING_LOG = "Reading file {}";
    private static final String READ_LOG = "Read {}";
    
    private FileUtil() {
    }
    
    public static List<String> readFile(final String filename) {
        return readFile(filename, true);
    }
    
    public static List<String> readFile(final String filename, final boolean reportError) {
        if (new File(filename).canRead()) {
            if (FileUtil.LOG.isDebugEnabled()) {
                FileUtil.LOG.debug("Reading file {}", filename);
            }
            try {
                return Files.readAllLines(Paths.get(filename, new String[0]), StandardCharsets.UTF_8);
            }
            catch (IOException e) {
                if (reportError) {
                    FileUtil.LOG.error("Error reading file {}. {}", filename, e.getMessage());
                }
                else {
                    FileUtil.LOG.debug("Error reading file {}. {}", filename, e.getMessage());
                }
                return new ArrayList<String>();
            }
        }
        if (reportError) {
            FileUtil.LOG.warn("File not found or not readable: {}", filename);
        }
        return new ArrayList<String>();
    }
    
    public static byte[] readAllBytes(final String filename) {
        return readAllBytes(filename, true);
    }
    
    public static byte[] readAllBytes(final String filename, final boolean reportError) {
        if (new File(filename).canRead()) {
            if (FileUtil.LOG.isDebugEnabled()) {
                FileUtil.LOG.debug("Reading file {}", filename);
            }
            try {
                return Files.readAllBytes(Paths.get(filename, new String[0]));
            }
            catch (IOException e) {
                if (reportError) {
                    FileUtil.LOG.error("Error reading file {}. {}", filename, e.getMessage());
                }
                else {
                    FileUtil.LOG.debug("Error reading file {}. {}", filename, e.getMessage());
                }
                return new byte[0];
            }
        }
        if (reportError) {
            FileUtil.LOG.warn("File not found or not readable: {}", filename);
        }
        return new byte[0];
    }
    
    public static long getLongFromFile(final String filename) {
        if (FileUtil.LOG.isDebugEnabled()) {
            FileUtil.LOG.debug("Reading file {}", filename);
        }
        final List<String> read = readFile(filename, false);
        if (!read.isEmpty()) {
            if (FileUtil.LOG.isTraceEnabled()) {
                FileUtil.LOG.trace("Read {}", read.get(0));
            }
            return ParseUtil.parseLongOrDefault(read.get(0), 0L);
        }
        return 0L;
    }
    
    public static long getUnsignedLongFromFile(final String filename) {
        if (FileUtil.LOG.isDebugEnabled()) {
            FileUtil.LOG.debug("Reading file {}", filename);
        }
        final List<String> read = readFile(filename, false);
        if (!read.isEmpty()) {
            if (FileUtil.LOG.isTraceEnabled()) {
                FileUtil.LOG.trace("Read {}", read.get(0));
            }
            return ParseUtil.parseUnsignedLongOrDefault(read.get(0), 0L);
        }
        return 0L;
    }
    
    public static int getIntFromFile(final String filename) {
        if (FileUtil.LOG.isDebugEnabled()) {
            FileUtil.LOG.debug("Reading file {}", filename);
        }
        try {
            final List<String> read = readFile(filename, false);
            if (!read.isEmpty()) {
                if (FileUtil.LOG.isTraceEnabled()) {
                    FileUtil.LOG.trace("Read {}", read.get(0));
                }
                return Integer.parseInt(read.get(0));
            }
        }
        catch (NumberFormatException ex) {
            FileUtil.LOG.warn("Unable to read value from {}. {}", filename, ex.getMessage());
        }
        return 0;
    }
    
    public static String getStringFromFile(final String filename) {
        if (FileUtil.LOG.isDebugEnabled()) {
            FileUtil.LOG.debug("Reading file {}", filename);
        }
        final List<String> read = readFile(filename, false);
        if (!read.isEmpty()) {
            if (FileUtil.LOG.isTraceEnabled()) {
                FileUtil.LOG.trace("Read {}", read.get(0));
            }
            return read.get(0);
        }
        return "";
    }
    
    public static Map<String, String> getKeyValueMapFromFile(final String filename, final String separator) {
        final Map<String, String> map = new HashMap<String, String>();
        if (FileUtil.LOG.isDebugEnabled()) {
            FileUtil.LOG.debug("Reading file {}", filename);
        }
        final List<String> lines = readFile(filename, false);
        for (final String line : lines) {
            final String[] parts = line.split(separator);
            if (parts.length == 2) {
                map.put(parts[0], parts[1].trim());
            }
        }
        return map;
    }
    
    public static Properties readPropertiesFromFilename(final String propsFilename) {
        final Properties archProps = new Properties();
        for (final ClassLoader loader : Stream.of(new ClassLoader[] { Thread.currentThread().getContextClassLoader(), ClassLoader.getSystemClassLoader(), FileUtil.class.getClassLoader() }).collect((Collector<? super ClassLoader, ?, LinkedHashSet<ClassLoader>>)Collectors.toCollection((Supplier<R>)LinkedHashSet::new))) {
            if (readPropertiesFromClassLoader(propsFilename, archProps, loader)) {
                return archProps;
            }
        }
        FileUtil.LOG.warn("Failed to load default configuration");
        return archProps;
    }
    
    private static boolean readPropertiesFromClassLoader(final String propsFilename, final Properties archProps, final ClassLoader loader) {
        if (loader == null) {
            return false;
        }
        try {
            final List<URL> resources = Collections.list(loader.getResources(propsFilename));
            if (resources.isEmpty()) {
                FileUtil.LOG.debug("No {} file found from ClassLoader {}", propsFilename, loader);
                return false;
            }
            if (resources.size() > 1) {
                FileUtil.LOG.warn("Configuration conflict: there is more than one {} file on the classpath", propsFilename);
                return true;
            }
            final InputStream in = resources.get(0).openStream();
            try {
                if (in != null) {
                    archProps.load(in);
                }
                if (in != null) {
                    in.close();
                }
            }
            catch (Throwable t) {
                if (in != null) {
                    try {
                        in.close();
                    }
                    catch (Throwable exception) {
                        t.addSuppressed(exception);
                    }
                }
                throw t;
            }
            return true;
        }
        catch (IOException e) {
            return false;
        }
    }
    
    public static String readSymlinkTarget(final File file) {
        try {
            return Files.readSymbolicLink(Paths.get(file.getAbsolutePath(), new String[0])).toString();
        }
        catch (IOException e) {
            return null;
        }
    }
    
    static {
        LOG = LoggerFactory.getLogger(FileUtil.class);
    }
}
