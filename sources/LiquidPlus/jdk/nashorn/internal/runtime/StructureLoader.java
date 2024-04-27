/*
 * Decompiled with CFR 0.152.
 */
package jdk.nashorn.internal.runtime;

import java.security.ProtectionDomain;
import jdk.nashorn.internal.codegen.Compiler;
import jdk.nashorn.internal.codegen.CompilerConstants;
import jdk.nashorn.internal.codegen.ObjectClassGenerator;
import jdk.nashorn.internal.runtime.Context;
import jdk.nashorn.internal.runtime.NashornLoader;

final class StructureLoader
extends NashornLoader {
    private static final String SINGLE_FIELD_PREFIX = Compiler.binaryName("jdk/nashorn/internal/scripts") + '.' + CompilerConstants.JS_OBJECT_SINGLE_FIELD_PREFIX.symbolName();
    private static final String DUAL_FIELD_PREFIX = Compiler.binaryName("jdk/nashorn/internal/scripts") + '.' + CompilerConstants.JS_OBJECT_DUAL_FIELD_PREFIX.symbolName();

    StructureLoader(ClassLoader parent) {
        super(parent);
    }

    private static boolean isDualFieldStructure(String name) {
        return name.startsWith(DUAL_FIELD_PREFIX);
    }

    static boolean isSingleFieldStructure(String name) {
        return name.startsWith(SINGLE_FIELD_PREFIX);
    }

    static boolean isStructureClass(String name) {
        return StructureLoader.isDualFieldStructure(name) || StructureLoader.isSingleFieldStructure(name);
    }

    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {
        if (StructureLoader.isDualFieldStructure(name)) {
            return this.generateClass(name, name.substring(DUAL_FIELD_PREFIX.length()), true);
        }
        if (StructureLoader.isSingleFieldStructure(name)) {
            return this.generateClass(name, name.substring(SINGLE_FIELD_PREFIX.length()), false);
        }
        return super.findClass(name);
    }

    private Class<?> generateClass(String name, String descriptor, boolean dualFields) {
        Context context = Context.getContextTrusted();
        byte[] code = new ObjectClassGenerator(context, dualFields).generate(descriptor);
        return this.defineClass(name, code, 0, code.length, new ProtectionDomain(null, this.getPermissions(null)));
    }
}

