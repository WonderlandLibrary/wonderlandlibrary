// 
// Decompiled by Procyon v0.5.36
// 

package javassist.bytecode.stackmap;

public interface TypeTag
{
    public static final TypeData TOP = null;
    public static final TypeData INTEGER = new TypeData.BasicType("int", 1);
    public static final TypeData FLOAT = new TypeData.BasicType("float", 2);
    public static final TypeData DOUBLE = new TypeData.BasicType("double", 3);
    public static final TypeData LONG = new TypeData.BasicType("long", 4);
}
