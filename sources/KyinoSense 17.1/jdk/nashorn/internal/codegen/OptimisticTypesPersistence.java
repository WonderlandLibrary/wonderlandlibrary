/*
 * Decompiled with CFR 0.152.
 */
package jdk.nashorn.internal.codegen;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.URL;
import java.nio.file.FileVisitOption;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.security.AccessController;
import java.security.MessageDigest;
import java.security.PrivilegedAction;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Date;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Function;
import java.util.function.IntFunction;
import java.util.function.Predicate;
import java.util.stream.Stream;
import jdk.nashorn.internal.codegen.types.Type;
import jdk.nashorn.internal.runtime.Context;
import jdk.nashorn.internal.runtime.RecompilableScriptFunctionData;
import jdk.nashorn.internal.runtime.Source;
import jdk.nashorn.internal.runtime.logging.DebugLogger;
import jdk.nashorn.internal.runtime.options.Options;

public final class OptimisticTypesPersistence {
    private static final int DEFAULT_MAX_FILES = 0;
    private static final int UNLIMITED_FILES = -1;
    private static final int MAX_FILES = OptimisticTypesPersistence.getMaxFiles();
    private static final int DEFAULT_CLEANUP_DELAY = 20;
    private static final int CLEANUP_DELAY = Math.max(0, Options.getIntProperty("nashorn.typeInfo.cleanupDelaySeconds", 20));
    private static final String DEFAULT_CACHE_SUBDIR_NAME = "com.oracle.java.NashornTypeInfo";
    private static final File baseCacheDir = OptimisticTypesPersistence.createBaseCacheDir();
    private static final File cacheDir = OptimisticTypesPersistence.createCacheDir(baseCacheDir);
    private static final Object[] locks;
    private static final long ERROR_REPORT_THRESHOLD = 60000L;
    private static volatile long lastReportedError;
    private static final AtomicBoolean scheduledCleanup;
    private static final Timer cleanupTimer;

    public static Object getLocationDescriptor(Source source, int functionId, Type[] paramTypes) {
        if (cacheDir == null) {
            return null;
        }
        StringBuilder b = new StringBuilder(48);
        b.append(source.getDigest()).append('-').append(functionId);
        if (paramTypes != null && paramTypes.length > 0) {
            b.append('-');
            for (Type t : paramTypes) {
                b.append(Type.getShortSignatureDescriptor(t));
            }
        }
        return new LocationDescriptor(new File(cacheDir, b.toString()));
    }

    public static void store(Object locationDescriptor, final Map<Integer, Type> optimisticTypes) {
        if (locationDescriptor == null || optimisticTypes.isEmpty()) {
            return;
        }
        final File file = ((LocationDescriptor)locationDescriptor).file;
        AccessController.doPrivileged(new PrivilegedAction<Void>(){

            /*
             * WARNING - Removed try catching itself - possible behaviour change.
             */
            @Override
            public Void run() {
                Object object = OptimisticTypesPersistence.getFileLock(file);
                synchronized (object) {
                    if (!file.exists()) {
                        OptimisticTypesPersistence.scheduleCleanup();
                    }
                    try (FileOutputStream out = new FileOutputStream(file);){
                        out.getChannel().lock();
                        DataOutputStream dout = new DataOutputStream(new BufferedOutputStream(out));
                        Type.writeTypeMap(optimisticTypes, dout);
                        dout.flush();
                    }
                    catch (Exception e) {
                        OptimisticTypesPersistence.reportError("write", file, e);
                    }
                }
                return null;
            }
        });
    }

    public static Map<Integer, Type> load(Object locationDescriptor) {
        if (locationDescriptor == null) {
            return null;
        }
        final File file = ((LocationDescriptor)locationDescriptor).file;
        return AccessController.doPrivileged(new PrivilegedAction<Map<Integer, Type>>(){

            /*
             * Enabled aggressive block sorting
             * Enabled unnecessary exception pruning
             * Enabled aggressive exception aggregation
             */
            @Override
            public Map<Integer, Type> run() {
                try {
                    if (!file.isFile()) {
                        return null;
                    }
                    Object object = OptimisticTypesPersistence.getFileLock(file);
                    synchronized (object) {
                        try (FileInputStream in = new FileInputStream(file);){
                            in.getChannel().lock(0L, Long.MAX_VALUE, true);
                            DataInputStream din = new DataInputStream(new BufferedInputStream(in));
                            Map<Integer, Type> map = Type.readTypeMap(din);
                            return map;
                        }
                    }
                }
                catch (Exception e) {
                    OptimisticTypesPersistence.reportError("read", file, e);
                    return null;
                }
            }
        });
    }

    private static void reportError(String msg, File file, Exception e) {
        long now = System.currentTimeMillis();
        if (now - lastReportedError > 60000L) {
            OptimisticTypesPersistence.reportError(String.format("Failed to %s %s", msg, file), e);
            lastReportedError = now;
        }
    }

    private static void reportError(String msg, Exception e) {
        OptimisticTypesPersistence.getLogger().warning(msg, "\n", OptimisticTypesPersistence.exceptionToString(e));
    }

    private static String exceptionToString(Exception e) {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw, false);
        e.printStackTrace(pw);
        pw.flush();
        return sw.toString();
    }

    private static File createBaseCacheDir() {
        if (MAX_FILES == 0 || Options.getBooleanProperty("nashorn.typeInfo.disabled")) {
            return null;
        }
        try {
            return OptimisticTypesPersistence.createBaseCacheDirPrivileged();
        }
        catch (Exception e) {
            OptimisticTypesPersistence.reportError("Failed to create cache dir", e);
            return null;
        }
    }

    private static File createBaseCacheDirPrivileged() {
        return AccessController.doPrivileged(new PrivilegedAction<File>(){

            @Override
            public File run() {
                File dir;
                String explicitDir = System.getProperty("nashorn.typeInfo.cacheDir");
                if (explicitDir != null) {
                    dir = new File(explicitDir);
                } else {
                    File systemCacheDir = OptimisticTypesPersistence.getSystemCacheDir();
                    dir = new File(systemCacheDir, OptimisticTypesPersistence.DEFAULT_CACHE_SUBDIR_NAME);
                    if (OptimisticTypesPersistence.isSymbolicLink(dir)) {
                        return null;
                    }
                }
                return dir;
            }
        });
    }

    private static File createCacheDir(File baseDir) {
        if (baseDir == null) {
            return null;
        }
        try {
            return OptimisticTypesPersistence.createCacheDirPrivileged(baseDir);
        }
        catch (Exception e) {
            OptimisticTypesPersistence.reportError("Failed to create cache dir", e);
            return null;
        }
    }

    private static File createCacheDirPrivileged(final File baseDir) {
        return AccessController.doPrivileged(new PrivilegedAction<File>(){

            @Override
            public File run() {
                String versionDirName;
                try {
                    versionDirName = OptimisticTypesPersistence.getVersionDirName();
                }
                catch (Exception e) {
                    OptimisticTypesPersistence.reportError("Failed to calculate version dir name", e);
                    return null;
                }
                File versionDir = new File(baseDir, versionDirName);
                if (OptimisticTypesPersistence.isSymbolicLink(versionDir)) {
                    return null;
                }
                versionDir.mkdirs();
                if (versionDir.isDirectory()) {
                    OptimisticTypesPersistence.getLogger().info("Optimistic type persistence directory is " + versionDir);
                    return versionDir;
                }
                OptimisticTypesPersistence.getLogger().warning("Could not create optimistic type persistence directory " + versionDir);
                return null;
            }
        });
    }

    private static File getSystemCacheDir() {
        String os = System.getProperty("os.name", "generic");
        if ("Mac OS X".equals(os)) {
            return new File(new File(System.getProperty("user.home"), "Library"), "Caches");
        }
        if (os.startsWith("Windows")) {
            return new File(System.getProperty("java.io.tmpdir"));
        }
        return new File(System.getProperty("user.home"), ".cache");
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public static String getVersionDirName() throws Exception {
        URL url = OptimisticTypesPersistence.class.getResource("anchor.properties");
        String protocol = url.getProtocol();
        if (!protocol.equals("jar")) {
            if (!protocol.equals("file")) throw new AssertionError();
            String fileStr = url.getFile();
            String className = OptimisticTypesPersistence.class.getName();
            int packageNameLen = className.lastIndexOf(46);
            String dirStr = fileStr.substring(0, fileStr.length() - packageNameLen - 1);
            File dir = new File(dirStr);
            return "dev-" + new SimpleDateFormat("yyyyMMdd-HHmmss").format(new Date(OptimisticTypesPersistence.getLastModifiedClassFile(dir, 0L)));
        }
        String jarUrlFile = url.getFile();
        String filePath = jarUrlFile.substring(0, jarUrlFile.indexOf(33));
        URL file = new URL(filePath);
        try (InputStream in = file.openStream();){
            byte[] buf = new byte[131072];
            MessageDigest digest = MessageDigest.getInstance("SHA-1");
            while (true) {
                int l;
                if ((l = in.read(buf)) == -1) {
                    String string = Base64.getUrlEncoder().withoutPadding().encodeToString(digest.digest());
                    return string;
                }
                digest.update(buf, 0, l);
                continue;
                break;
            }
        }
    }

    private static long getLastModifiedClassFile(File dir, long max) {
        long currentMax = max;
        for (File f : dir.listFiles()) {
            long lastModified;
            if (f.getName().endsWith(".class")) {
                lastModified = f.lastModified();
                if (lastModified <= currentMax) continue;
                currentMax = lastModified;
                continue;
            }
            if (!f.isDirectory() || (lastModified = OptimisticTypesPersistence.getLastModifiedClassFile(f, currentMax)) <= currentMax) continue;
            currentMax = lastModified;
        }
        return currentMax;
    }

    private static boolean isSymbolicLink(File file) {
        if (Files.isSymbolicLink(file.toPath())) {
            OptimisticTypesPersistence.getLogger().warning("Directory " + file + " is a symlink");
            return true;
        }
        return false;
    }

    private static Object[] createLockArray() {
        Object[] lockArray = new Object[Runtime.getRuntime().availableProcessors() * 2];
        for (int i = 0; i < lockArray.length; ++i) {
            lockArray[i] = new Object();
        }
        return lockArray;
    }

    private static Object getFileLock(File file) {
        return locks[(file.hashCode() & Integer.MAX_VALUE) % locks.length];
    }

    private static DebugLogger getLogger() {
        try {
            return Context.getContext().getLogger(RecompilableScriptFunctionData.class);
        }
        catch (Exception e) {
            e.printStackTrace();
            return DebugLogger.DISABLED_LOGGER;
        }
    }

    private static void scheduleCleanup() {
        if (MAX_FILES != -1 && scheduledCleanup.compareAndSet(false, true)) {
            cleanupTimer.schedule(new TimerTask(){

                @Override
                public void run() {
                    scheduledCleanup.set(false);
                    try {
                        OptimisticTypesPersistence.doCleanup();
                    }
                    catch (IOException iOException) {
                        // empty catch block
                    }
                }
            }, TimeUnit.SECONDS.toMillis(CLEANUP_DELAY));
        }
    }

    private static void doCleanup() throws IOException {
        Path[] files = OptimisticTypesPersistence.getAllRegularFilesInLastModifiedOrder();
        int nFiles = files.length;
        int filesToDelete = Math.max(0, nFiles - MAX_FILES);
        int filesDeleted = 0;
        for (int i = 0; i < nFiles && filesDeleted < filesToDelete; ++i) {
            try {
                Files.deleteIfExists(files[i]);
                ++filesDeleted;
            }
            catch (Exception exception) {
                // empty catch block
            }
            files[i] = null;
        }
    }

    private static Path[] getAllRegularFilesInLastModifiedOrder() throws IOException {
        try (Stream<Path> filesStream = Files.walk(baseCacheDir.toPath(), new FileVisitOption[0]);){
            Path[] pathArray = (Path[])filesStream.filter(new Predicate<Path>(){

                @Override
                public boolean test(Path path) {
                    return !Files.isDirectory(path, new LinkOption[0]);
                }
            }).map(new Function<Path, PathAndTime>(){

                @Override
                public PathAndTime apply(Path path) {
                    return new PathAndTime(path);
                }
            }).sorted().map(new Function<PathAndTime, Path>(){

                @Override
                public Path apply(PathAndTime pathAndTime) {
                    return pathAndTime.path;
                }
            }).toArray((IntFunction<A[]>)new IntFunction<Path[]>(){

                @Override
                public Path[] apply(int length) {
                    return new Path[length];
                }
            });
            return pathArray;
        }
    }

    private static int getMaxFiles() {
        String str = Options.getStringProperty("nashorn.typeInfo.maxFiles", null);
        if (str == null) {
            return 0;
        }
        if ("unlimited".equals(str)) {
            return -1;
        }
        return Math.max(0, Integer.parseInt(str));
    }

    static {
        Object[] objectArray = locks = cacheDir == null ? null : OptimisticTypesPersistence.createLockArray();
        if (baseCacheDir == null || MAX_FILES == -1) {
            scheduledCleanup = null;
            cleanupTimer = null;
        } else {
            scheduledCleanup = new AtomicBoolean();
            cleanupTimer = new Timer(true);
        }
    }

    private static class PathAndTime
    implements Comparable<PathAndTime> {
        private final Path path;
        private final long time;

        PathAndTime(Path path) {
            this.path = path;
            this.time = PathAndTime.getTime(path);
        }

        @Override
        public int compareTo(PathAndTime other) {
            return Long.compare(this.time, other.time);
        }

        private static long getTime(Path path) {
            try {
                return Files.getLastModifiedTime(path, new LinkOption[0]).toMillis();
            }
            catch (IOException e) {
                return -1L;
            }
        }
    }

    private static final class LocationDescriptor {
        private final File file;

        LocationDescriptor(File file) {
            this.file = file;
        }
    }
}

