// 
// Decompiled by Procyon v0.5.36
// 

package com.google.common.base;

import javax.annotation.CheckForNull;
import com.google.common.annotations.GwtIncompatible;

@ElementTypesAreNonnullByDefault
@GwtIncompatible
public enum StandardSystemProperty
{
    JAVA_VERSION("java.version"), 
    JAVA_VENDOR("java.vendor"), 
    JAVA_VENDOR_URL("java.vendor.url"), 
    JAVA_HOME("java.home"), 
    JAVA_VM_SPECIFICATION_VERSION("java.vm.specification.version"), 
    JAVA_VM_SPECIFICATION_VENDOR("java.vm.specification.vendor"), 
    JAVA_VM_SPECIFICATION_NAME("java.vm.specification.name"), 
    JAVA_VM_VERSION("java.vm.version"), 
    JAVA_VM_VENDOR("java.vm.vendor"), 
    JAVA_VM_NAME("java.vm.name"), 
    JAVA_SPECIFICATION_VERSION("java.specification.version"), 
    JAVA_SPECIFICATION_VENDOR("java.specification.vendor"), 
    JAVA_SPECIFICATION_NAME("java.specification.name"), 
    JAVA_CLASS_VERSION("java.class.version"), 
    JAVA_CLASS_PATH("java.class.path"), 
    JAVA_LIBRARY_PATH("java.library.path"), 
    JAVA_IO_TMPDIR("java.io.tmpdir"), 
    JAVA_COMPILER("java.compiler"), 
    @Deprecated
    JAVA_EXT_DIRS("java.ext.dirs"), 
    OS_NAME("os.name"), 
    OS_ARCH("os.arch"), 
    OS_VERSION("os.version"), 
    FILE_SEPARATOR("file.separator"), 
    PATH_SEPARATOR("path.separator"), 
    LINE_SEPARATOR("line.separator"), 
    USER_NAME("user.name"), 
    USER_HOME("user.home"), 
    USER_DIR("user.dir");
    
    private final String key;
    
    private StandardSystemProperty(final String key) {
        this.key = key;
    }
    
    public String key() {
        return this.key;
    }
    
    @CheckForNull
    public String value() {
        return System.getProperty(this.key);
    }
    
    @Override
    public String toString() {
        final String key = this.key();
        final String value = this.value();
        return new StringBuilder(1 + String.valueOf(key).length() + String.valueOf(value).length()).append(key).append("=").append(value).toString();
    }
    
    private static /* synthetic */ StandardSystemProperty[] $values() {
        return new StandardSystemProperty[] { StandardSystemProperty.JAVA_VERSION, StandardSystemProperty.JAVA_VENDOR, StandardSystemProperty.JAVA_VENDOR_URL, StandardSystemProperty.JAVA_HOME, StandardSystemProperty.JAVA_VM_SPECIFICATION_VERSION, StandardSystemProperty.JAVA_VM_SPECIFICATION_VENDOR, StandardSystemProperty.JAVA_VM_SPECIFICATION_NAME, StandardSystemProperty.JAVA_VM_VERSION, StandardSystemProperty.JAVA_VM_VENDOR, StandardSystemProperty.JAVA_VM_NAME, StandardSystemProperty.JAVA_SPECIFICATION_VERSION, StandardSystemProperty.JAVA_SPECIFICATION_VENDOR, StandardSystemProperty.JAVA_SPECIFICATION_NAME, StandardSystemProperty.JAVA_CLASS_VERSION, StandardSystemProperty.JAVA_CLASS_PATH, StandardSystemProperty.JAVA_LIBRARY_PATH, StandardSystemProperty.JAVA_IO_TMPDIR, StandardSystemProperty.JAVA_COMPILER, StandardSystemProperty.JAVA_EXT_DIRS, StandardSystemProperty.OS_NAME, StandardSystemProperty.OS_ARCH, StandardSystemProperty.OS_VERSION, StandardSystemProperty.FILE_SEPARATOR, StandardSystemProperty.PATH_SEPARATOR, StandardSystemProperty.LINE_SEPARATOR, StandardSystemProperty.USER_NAME, StandardSystemProperty.USER_HOME, StandardSystemProperty.USER_DIR };
    }
    
    static {
        $VALUES = $values();
    }
}
