/*
 * Decompiled with CFR 0.152.
 */
package jdk.nashorn.internal.runtime;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.security.AccessControlException;
import java.security.AccessController;
import java.security.PrivilegedActionException;
import java.security.PrivilegedExceptionAction;
import java.util.Iterator;
import java.util.Map;
import java.util.ServiceLoader;
import jdk.nashorn.internal.codegen.OptimisticTypesPersistence;
import jdk.nashorn.internal.codegen.types.Type;
import jdk.nashorn.internal.runtime.Context;
import jdk.nashorn.internal.runtime.FunctionInitializer;
import jdk.nashorn.internal.runtime.ScriptEnvironment;
import jdk.nashorn.internal.runtime.Source;
import jdk.nashorn.internal.runtime.StoredScript;
import jdk.nashorn.internal.runtime.logging.DebugLogger;
import jdk.nashorn.internal.runtime.logging.Loggable;
import jdk.nashorn.internal.runtime.logging.Logger;
import jdk.nashorn.internal.runtime.options.Options;

@Logger(name="codestore")
public abstract class CodeStore
implements Loggable {
    public static final String NASHORN_PROVIDE_CODE_STORE = "nashorn.provideCodeStore";
    private DebugLogger log;

    protected CodeStore() {
    }

    @Override
    public DebugLogger initLogger(Context context) {
        this.log = context.getLogger(this.getClass());
        return this.log;
    }

    @Override
    public DebugLogger getLogger() {
        return this.log;
    }

    public static CodeStore newCodeStore(Context context) {
        Class<CodeStore> baseClass = CodeStore.class;
        try {
            ServiceLoader<CodeStore> services;
            Iterator<CodeStore> iterator2;
            SecurityManager sm = System.getSecurityManager();
            if (sm != null) {
                sm.checkPermission(new RuntimePermission(NASHORN_PROVIDE_CODE_STORE));
            }
            if ((iterator2 = (services = ServiceLoader.load(baseClass)).iterator()).hasNext()) {
                CodeStore store = iterator2.next();
                store.initLogger(context).info("using code store provider ", store.getClass().getCanonicalName());
                return store;
            }
        }
        catch (AccessControlException e) {
            context.getLogger(CodeStore.class).warning("failed to load code store provider ", e);
        }
        try {
            DirectoryCodeStore store = new DirectoryCodeStore(context);
            store.initLogger(context);
            return store;
        }
        catch (IOException e) {
            context.getLogger(CodeStore.class).warning("failed to create cache directory ", e);
            return null;
        }
    }

    public StoredScript store(String functionKey, Source source, String mainClassName, Map<String, byte[]> classBytes, Map<Integer, FunctionInitializer> initializers, Object[] constants, int compilationId) {
        return this.store(functionKey, source, this.storedScriptFor(source, mainClassName, classBytes, initializers, constants, compilationId));
    }

    public abstract StoredScript store(String var1, Source var2, StoredScript var3);

    public abstract StoredScript load(Source var1, String var2);

    public StoredScript storedScriptFor(Source source, String mainClassName, Map<String, byte[]> classBytes, Map<Integer, FunctionInitializer> initializers, Object[] constants, int compilationId) {
        for (Object constant : constants) {
            if (constant instanceof Serializable) continue;
            this.getLogger().warning("cannot store ", source, " non serializable constant ", constant);
            return null;
        }
        return new StoredScript(compilationId, mainClassName, classBytes, initializers, constants);
    }

    public static String getCacheKey(Object functionId, Type[] paramTypes) {
        StringBuilder b = new StringBuilder().append(functionId);
        if (paramTypes != null && paramTypes.length > 0) {
            b.append('-');
            for (Type t : paramTypes) {
                b.append(Type.getShortSignatureDescriptor(t));
            }
        }
        return b.toString();
    }

    public static class DirectoryCodeStore
    extends CodeStore {
        private static final int DEFAULT_MIN_SIZE = 1000;
        private final File dir;
        private final boolean readOnly;
        private final int minSize;

        public DirectoryCodeStore(Context context) throws IOException {
            this(context, Options.getStringProperty("nashorn.persistent.code.cache", "nashorn_code_cache"), false, 1000);
        }

        public DirectoryCodeStore(Context context, String path, boolean readOnly, int minSize) throws IOException {
            this.dir = DirectoryCodeStore.checkDirectory(path, context.getEnv(), readOnly);
            this.readOnly = readOnly;
            this.minSize = minSize;
        }

        private static File checkDirectory(final String path, final ScriptEnvironment env, final boolean readOnly) throws IOException {
            try {
                return AccessController.doPrivileged(new PrivilegedExceptionAction<File>(){

                    @Override
                    public File run() throws IOException {
                        File dir = new File(path, DirectoryCodeStore.getVersionDir(env)).getAbsoluteFile();
                        if (readOnly) {
                            if (!dir.exists() || !dir.isDirectory()) {
                                throw new IOException("Not a directory: " + dir.getPath());
                            }
                            if (!dir.canRead()) {
                                throw new IOException("Directory not readable: " + dir.getPath());
                            }
                        } else {
                            if (!dir.exists() && !dir.mkdirs()) {
                                throw new IOException("Could not create directory: " + dir.getPath());
                            }
                            if (!dir.isDirectory()) {
                                throw new IOException("Not a directory: " + dir.getPath());
                            }
                            if (!dir.canRead() || !dir.canWrite()) {
                                throw new IOException("Directory not readable or writable: " + dir.getPath());
                            }
                        }
                        return dir;
                    }
                });
            }
            catch (PrivilegedActionException e) {
                throw (IOException)e.getException();
            }
        }

        private static String getVersionDir(ScriptEnvironment env) throws IOException {
            try {
                String versionDir = OptimisticTypesPersistence.getVersionDirName();
                return env._optimistic_types ? versionDir + "_opt" : versionDir;
            }
            catch (Exception e) {
                throw new IOException(e);
            }
        }

        @Override
        public StoredScript load(final Source source, final String functionKey) {
            if (this.belowThreshold(source)) {
                return null;
            }
            final File file = this.getCacheFile(source, functionKey);
            try {
                return AccessController.doPrivileged(new PrivilegedExceptionAction<StoredScript>(){

                    @Override
                    public StoredScript run() throws IOException, ClassNotFoundException {
                        if (!file.exists()) {
                            return null;
                        }
                        try (ObjectInputStream in = new ObjectInputStream(new BufferedInputStream(new FileInputStream(file)));){
                            StoredScript storedScript = (StoredScript)in.readObject();
                            DirectoryCodeStore.this.getLogger().info("loaded ", source, "-", functionKey);
                            StoredScript storedScript2 = storedScript;
                            return storedScript2;
                        }
                    }
                });
            }
            catch (PrivilegedActionException e) {
                this.getLogger().warning("failed to load ", source, "-", functionKey, ": ", e.getException());
                return null;
            }
        }

        @Override
        public StoredScript store(final String functionKey, final Source source, final StoredScript script) {
            if (this.readOnly || script == null || this.belowThreshold(source)) {
                return null;
            }
            final File file = this.getCacheFile(source, functionKey);
            try {
                return AccessController.doPrivileged(new PrivilegedExceptionAction<StoredScript>(){

                    @Override
                    public StoredScript run() throws IOException {
                        try (ObjectOutputStream out = new ObjectOutputStream(new BufferedOutputStream(new FileOutputStream(file)));){
                            out.writeObject(script);
                        }
                        DirectoryCodeStore.this.getLogger().info("stored ", source, "-", functionKey);
                        return script;
                    }
                });
            }
            catch (PrivilegedActionException e) {
                this.getLogger().warning("failed to store ", script, "-", functionKey, ": ", e.getException());
                return null;
            }
        }

        private File getCacheFile(Source source, String functionKey) {
            return new File(this.dir, source.getDigest() + '-' + functionKey);
        }

        private boolean belowThreshold(Source source) {
            if (source.getLength() < this.minSize) {
                this.getLogger().info("below size threshold ", source);
                return true;
            }
            return false;
        }
    }
}

