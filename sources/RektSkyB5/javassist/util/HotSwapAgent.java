/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.sun.tools.attach.VirtualMachine
 */
package javassist.util;

import com.sun.tools.attach.VirtualMachine;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.instrument.ClassDefinition;
import java.lang.instrument.Instrumentation;
import java.lang.instrument.UnmodifiableClassException;
import java.lang.management.ManagementFactory;
import java.util.jar.Attributes;
import java.util.jar.JarEntry;
import java.util.jar.JarOutputStream;
import java.util.jar.Manifest;
import java.util.zip.ZipOutputStream;
import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.NotFoundException;

public class HotSwapAgent {
    private static Instrumentation instrumentation = null;

    public Instrumentation instrumentation() {
        return instrumentation;
    }

    public static void premain(String agentArgs, Instrumentation inst) throws Throwable {
        HotSwapAgent.agentmain(agentArgs, inst);
    }

    public static void agentmain(String agentArgs, Instrumentation inst) throws Throwable {
        if (!inst.isRedefineClassesSupported()) {
            throw new RuntimeException("this JVM does not support redefinition of classes");
        }
        instrumentation = inst;
    }

    public static void redefine(Class<?> oldClass, CtClass newClass) throws NotFoundException, IOException, CannotCompileException {
        Class[] old = new Class[]{oldClass};
        CtClass[] newClasses = new CtClass[]{newClass};
        HotSwapAgent.redefine(old, newClasses);
    }

    public static void redefine(Class<?>[] oldClasses, CtClass[] newClasses) throws NotFoundException, IOException, CannotCompileException {
        HotSwapAgent.startAgent();
        ClassDefinition[] defs = new ClassDefinition[oldClasses.length];
        for (int i2 = 0; i2 < oldClasses.length; ++i2) {
            defs[i2] = new ClassDefinition(oldClasses[i2], newClasses[i2].toBytecode());
        }
        try {
            instrumentation.redefineClasses(defs);
        }
        catch (ClassNotFoundException e2) {
            throw new NotFoundException(e2.getMessage(), e2);
        }
        catch (UnmodifiableClassException e3) {
            throw new CannotCompileException(e3.getMessage(), e3);
        }
    }

    private static void startAgent() throws NotFoundException {
        if (instrumentation != null) {
            return;
        }
        try {
            File agentJar = HotSwapAgent.createJarFile();
            String nameOfRunningVM = ManagementFactory.getRuntimeMXBean().getName();
            String pid = nameOfRunningVM.substring(0, nameOfRunningVM.indexOf(64));
            VirtualMachine vm = VirtualMachine.attach((String)pid);
            vm.loadAgent(agentJar.getAbsolutePath(), null);
            vm.detach();
        }
        catch (Exception e2) {
            throw new NotFoundException("hotswap agent", e2);
        }
        for (int sec = 0; sec < 10; ++sec) {
            if (instrumentation != null) {
                return;
            }
            try {
                Thread.sleep(1000L);
                continue;
            }
            catch (InterruptedException e3) {
                Thread.currentThread().interrupt();
                break;
            }
        }
        throw new NotFoundException("hotswap agent (timeout)");
    }

    public static File createAgentJarFile(String fileName) throws IOException, CannotCompileException, NotFoundException {
        return HotSwapAgent.createJarFile(new File(fileName));
    }

    private static File createJarFile() throws IOException, CannotCompileException, NotFoundException {
        File jar = File.createTempFile("agent", ".jar");
        jar.deleteOnExit();
        return HotSwapAgent.createJarFile(jar);
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    private static File createJarFile(File jar) throws IOException, CannotCompileException, NotFoundException {
        Manifest manifest = new Manifest();
        Attributes attrs = manifest.getMainAttributes();
        attrs.put(Attributes.Name.MANIFEST_VERSION, "1.0");
        attrs.put(new Attributes.Name("Premain-Class"), HotSwapAgent.class.getName());
        attrs.put(new Attributes.Name("Agent-Class"), HotSwapAgent.class.getName());
        attrs.put(new Attributes.Name("Can-Retransform-Classes"), "true");
        attrs.put(new Attributes.Name("Can-Redefine-Classes"), "true");
        try (ZipOutputStream jos = null;){
            jos = new JarOutputStream((OutputStream)new FileOutputStream(jar), manifest);
            String cname = HotSwapAgent.class.getName();
            JarEntry e2 = new JarEntry(cname.replace('.', '/') + ".class");
            ((JarOutputStream)jos).putNextEntry(e2);
            ClassPool pool = ClassPool.getDefault();
            CtClass clazz = pool.get(cname);
            jos.write(clazz.toBytecode());
            jos.closeEntry();
        }
        return jar;
    }
}

