/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.apache.tools.ant.BuildException
 *  org.apache.tools.ant.Location
 *  org.apache.tools.ant.Task
 *  org.apache.tools.ant.types.FileSet
 *  org.apache.tools.ant.types.Path
 *  org.apache.tools.ant.types.Reference
 *  org.apache.tools.ant.types.ResourceCollection
 */
package lombok.delombok.ant;

import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Location;
import org.apache.tools.ant.Task;
import org.apache.tools.ant.types.FileSet;
import org.apache.tools.ant.types.Path;
import org.apache.tools.ant.types.Reference;
import org.apache.tools.ant.types.ResourceCollection;

class Tasks {
    Tasks() {
    }

    public static class Delombok
    extends Task {
        private File fromDir;
        private File toDir;
        private Path classpath;
        private Path sourcepath;
        private Path modulepath;
        private boolean verbose;
        private String encoding;
        private Path path;
        private List<Format> formatOptions = new ArrayList<Format>();
        private static ClassLoader shadowLoader;

        public void setClasspath(Path classpath) {
            if (this.classpath == null) {
                this.classpath = classpath;
            } else {
                this.classpath.append(classpath);
            }
        }

        public Path createClasspath() {
            if (this.classpath == null) {
                this.classpath = new Path(this.getProject());
            }
            return this.classpath.createPath();
        }

        public void setClasspathRef(Reference r2) {
            this.createClasspath().setRefid(r2);
        }

        public void setSourcepath(Path sourcepath) {
            if (this.sourcepath == null) {
                this.sourcepath = sourcepath;
            } else {
                this.sourcepath.append(sourcepath);
            }
        }

        public Path createSourcepath() {
            if (this.sourcepath == null) {
                this.sourcepath = new Path(this.getProject());
            }
            return this.sourcepath.createPath();
        }

        public void setSourcepathRef(Reference r2) {
            this.createSourcepath().setRefid(r2);
        }

        public void setModulepath(Path modulepath) {
            if (this.modulepath == null) {
                this.modulepath = modulepath;
            } else {
                this.modulepath.append(modulepath);
            }
        }

        public Path createModulepath() {
            if (this.modulepath == null) {
                this.modulepath = new Path(this.getProject());
            }
            return this.modulepath.createPath();
        }

        public void setModulepathRef(Reference r2) {
            this.createModulepath().setRefid(r2);
        }

        public void setFrom(File dir) {
            this.fromDir = dir;
        }

        public void setTo(File dir) {
            this.toDir = dir;
        }

        public void setVerbose(boolean verbose) {
            this.verbose = verbose;
        }

        public void setEncoding(String encoding) {
            this.encoding = encoding;
        }

        public void addFileset(FileSet set) {
            if (this.path == null) {
                this.path = new Path(this.getProject());
            }
            this.path.add((ResourceCollection)set);
        }

        public void addFormat(Format format) {
            this.formatOptions.add(format);
        }

        public static Class<?> shadowLoadClass(String name) {
            try {
                if (shadowLoader == null) {
                    try {
                        Class.forName("lombok.core.LombokNode");
                        shadowLoader = Delombok.class.getClassLoader();
                    }
                    catch (ClassNotFoundException classNotFoundException) {
                        Class<?> launcherMain = Class.forName("lombok.launch.Main");
                        Method m2 = launcherMain.getDeclaredMethod("getShadowClassLoader", new Class[0]);
                        m2.setAccessible(true);
                        shadowLoader = (ClassLoader)m2.invoke(null, new Object[0]);
                    }
                }
                return Class.forName(name, true, shadowLoader);
            }
            catch (Throwable t2) {
                if (t2 instanceof InvocationTargetException) {
                    t2 = t2.getCause();
                }
                if (t2 instanceof RuntimeException) {
                    throw (RuntimeException)t2;
                }
                if (t2 instanceof Error) {
                    throw (Error)t2;
                }
                throw new RuntimeException(t2);
            }
        }

        public void execute() throws BuildException {
            Location loc = this.getLocation();
            try {
                Object instance = Delombok.shadowLoadClass("lombok.delombok.ant.DelombokTaskImpl").getConstructor(new Class[0]).newInstance(new Object[0]);
                Field[] fieldArray = ((Object)((Object)this)).getClass().getDeclaredFields();
                int n2 = fieldArray.length;
                int n3 = 0;
                while (n3 < n2) {
                    Field selfField = fieldArray[n3];
                    if (!selfField.isSynthetic() && !Modifier.isStatic(selfField.getModifiers())) {
                        Field otherField = instance.getClass().getDeclaredField(selfField.getName());
                        otherField.setAccessible(true);
                        if (selfField.getName().equals("formatOptions")) {
                            ArrayList<String> rep = new ArrayList<String>();
                            for (Format f2 : this.formatOptions) {
                                if (f2.getValue() == null) {
                                    throw new BuildException("'value' property required for <format>");
                                }
                                rep.add(f2.getValue());
                            }
                            otherField.set(instance, rep);
                        } else {
                            otherField.set(instance, selfField.get((Object)this));
                        }
                    }
                    ++n3;
                }
                Method m2 = instance.getClass().getMethod("execute", Location.class);
                m2.invoke(instance, loc);
            }
            catch (Throwable t2) {
                if (t2 instanceof InvocationTargetException) {
                    t2 = t2.getCause();
                }
                if (t2 instanceof RuntimeException) {
                    throw (RuntimeException)t2;
                }
                if (t2 instanceof Error) {
                    throw (Error)t2;
                }
                throw new RuntimeException(t2);
            }
        }
    }

    public static class Format {
        private String value;

        public int hashCode() {
            int result = 1;
            result = 31 * result + (this.value == null ? 0 : this.value.hashCode());
            return result;
        }

        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null) {
                return false;
            }
            if (this.getClass() != obj.getClass()) {
                return false;
            }
            Format other = (Format)obj;
            return !(this.value == null ? other.value != null : !this.value.equals(other.value));
        }

        public String toString() {
            return "FormatOption [value=" + this.value + "]";
        }

        public String getValue() {
            return this.value;
        }

        public void setValue(String value) {
            this.value = value;
        }
    }
}

