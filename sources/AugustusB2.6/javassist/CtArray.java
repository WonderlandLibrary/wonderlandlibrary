// 
// Decompiled by Procyon v0.5.36
// 

package javassist;

final class CtArray extends CtClass
{
    protected ClassPool pool;
    private CtClass[] interfaces;
    
    CtArray(final String name, final ClassPool cp) {
        super(name);
        this.interfaces = null;
        this.pool = cp;
    }
    
    public ClassPool getClassPool() {
        return this.pool;
    }
    
    public boolean isArray() {
        return true;
    }
    
    public int getModifiers() {
        int mod = 16;
        try {
            mod |= (this.getComponentType().getModifiers() & 0x7);
        }
        catch (NotFoundException ex) {}
        return mod;
    }
    
    public CtClass[] getInterfaces() throws NotFoundException {
        if (this.interfaces == null) {
            this.interfaces = new CtClass[] { this.pool.get("java.lang.Cloneable"), this.pool.get("java.io.Serializable") };
        }
        return this.interfaces;
    }
    
    public boolean subtypeOf(final CtClass clazz) throws NotFoundException {
        if (super.subtypeOf(clazz)) {
            return true;
        }
        final String cname = clazz.getName();
        return cname.equals("java.lang.Object") || cname.equals("java.lang.Cloneable") || cname.equals("java.io.Serializable") || (clazz.isArray() && this.getComponentType().subtypeOf(clazz.getComponentType()));
    }
    
    public CtClass getComponentType() throws NotFoundException {
        final String name = this.getName();
        return this.pool.get(name.substring(0, name.length() - 2));
    }
    
    public CtClass getSuperclass() throws NotFoundException {
        return this.pool.get("java.lang.Object");
    }
    
    public CtMethod[] getMethods() {
        try {
            return this.getSuperclass().getMethods();
        }
        catch (NotFoundException e) {
            return super.getMethods();
        }
    }
    
    public CtMethod getMethod(final String name, final String desc) throws NotFoundException {
        return this.getSuperclass().getMethod(name, desc);
    }
    
    public CtConstructor[] getConstructors() {
        try {
            return this.getSuperclass().getConstructors();
        }
        catch (NotFoundException e) {
            return super.getConstructors();
        }
    }
}
