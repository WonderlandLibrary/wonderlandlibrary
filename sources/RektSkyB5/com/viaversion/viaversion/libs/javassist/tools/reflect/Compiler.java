/*
 * Decompiled with CFR 0.152.
 */
package com.viaversion.viaversion.libs.javassist.tools.reflect;

import com.viaversion.viaversion.libs.javassist.ClassPool;
import com.viaversion.viaversion.libs.javassist.CtClass;
import com.viaversion.viaversion.libs.javassist.tools.reflect.CompiledClass;
import com.viaversion.viaversion.libs.javassist.tools.reflect.Reflection;
import java.io.PrintStream;

public class Compiler {
    public static void main(String[] args) throws Exception {
        if (args.length == 0) {
            Compiler.help(System.err);
            return;
        }
        CompiledClass[] entries = new CompiledClass[args.length];
        int n2 = Compiler.parse(args, entries);
        if (n2 < 1) {
            System.err.println("bad parameter.");
            return;
        }
        Compiler.processClasses(entries, n2);
    }

    private static void processClasses(CompiledClass[] entries, int n2) throws Exception {
        int i2;
        Reflection implementor = new Reflection();
        ClassPool pool = ClassPool.getDefault();
        implementor.start(pool);
        for (i2 = 0; i2 < n2; ++i2) {
            CtClass c2 = pool.get(entries[i2].classname);
            if (entries[i2].metaobject != null || entries[i2].classobject != null) {
                String metaobj = entries[i2].metaobject == null ? "com.viaversion.viaversion.libs.javassist.tools.reflect.Metaobject" : entries[i2].metaobject;
                String classobj = entries[i2].classobject == null ? "com.viaversion.viaversion.libs.javassist.tools.reflect.ClassMetaobject" : entries[i2].classobject;
                if (!implementor.makeReflective(c2, pool.get(metaobj), pool.get(classobj))) {
                    System.err.println("Warning: " + c2.getName() + " is reflective.  It was not changed.");
                }
                System.err.println(c2.getName() + ": " + metaobj + ", " + classobj);
                continue;
            }
            System.err.println(c2.getName() + ": not reflective");
        }
        for (i2 = 0; i2 < n2; ++i2) {
            implementor.onLoad(pool, entries[i2].classname);
            pool.get(entries[i2].classname).writeFile();
        }
    }

    private static int parse(String[] args, CompiledClass[] result) {
        int n2 = -1;
        for (int i2 = 0; i2 < args.length; ++i2) {
            String a2 = args[i2];
            if (a2.equals("-m")) {
                if (n2 < 0 || i2 + 1 > args.length) {
                    return -1;
                }
                result[n2].metaobject = args[++i2];
                continue;
            }
            if (a2.equals("-c")) {
                if (n2 < 0 || i2 + 1 > args.length) {
                    return -1;
                }
                result[n2].classobject = args[++i2];
                continue;
            }
            if (a2.charAt(0) == '-') {
                return -1;
            }
            CompiledClass cc = new CompiledClass();
            cc.classname = a2;
            cc.metaobject = null;
            cc.classobject = null;
            result[++n2] = cc;
        }
        return n2 + 1;
    }

    private static void help(PrintStream out) {
        out.println("Usage: java javassist.tools.reflect.Compiler");
        out.println("            (<class> [-m <metaobject>] [-c <class metaobject>])+");
    }
}

