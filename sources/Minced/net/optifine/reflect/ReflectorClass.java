// 
// Decompiled by Procyon v0.5.36
// 

package net.optifine.reflect;

import net.optifine.Log;

public class ReflectorClass implements IResolvable
{
    private String targetClassName;
    private boolean checked;
    private Class targetClass;
    
    public ReflectorClass(final String targetClassName) {
        this.targetClassName = null;
        this.checked = false;
        this.targetClass = null;
        this.targetClassName = targetClassName;
        ReflectorResolver.register(this);
    }
    
    public ReflectorClass(final Class targetClass) {
        this.targetClassName = null;
        this.checked = false;
        this.targetClass = null;
        this.targetClass = targetClass;
        this.targetClassName = targetClass.getName();
        this.checked = true;
    }
    
    public Class getTargetClass() {
        if (this.checked) {
            return this.targetClass;
        }
        this.checked = true;
        try {
            this.targetClass = Class.forName(this.targetClassName);
        }
        catch (ClassNotFoundException var2) {
            Log.log("(Reflector) Class not present: " + this.targetClassName);
        }
        catch (Throwable throwable) {
            throwable.printStackTrace();
        }
        return this.targetClass;
    }
    
    public boolean exists() {
        return this.getTargetClass() != null;
    }
    
    public String getTargetClassName() {
        return this.targetClassName;
    }
    
    public boolean isInstance(final Object obj) {
        return this.getTargetClass() != null && this.getTargetClass().isInstance(obj);
    }
    
    public ReflectorField makeField(final String name) {
        return new ReflectorField(this, name);
    }
    
    public ReflectorMethod makeMethod(final String name) {
        return new ReflectorMethod(this, name);
    }
    
    public ReflectorMethod makeMethod(final String name, final Class[] paramTypes) {
        return new ReflectorMethod(this, name, paramTypes);
    }
    
    @Override
    public void resolve() {
        final Class oclass = this.getTargetClass();
    }
}
