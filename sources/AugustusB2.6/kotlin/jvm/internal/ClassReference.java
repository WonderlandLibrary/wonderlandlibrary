// 
// Decompiled by Procyon v0.5.36
// 

package kotlin.jvm.internal;

import com.badlogic.gdx.graphics.Pixmap;
import kotlin.reflect.KClass;

public final class ClassReference implements ClassBasedDeclarationContainer, KClass<Object>
{
    private final Class<?> jClass;
    
    @Override
    public final boolean equals(final Object other) {
        return other instanceof ClassReference && Intrinsics.areEqual(Pixmap.getJavaObjectType((KClass<Object>)this), Pixmap.getJavaObjectType((KClass<Object>)other));
    }
    
    @Override
    public final int hashCode() {
        return Pixmap.getJavaObjectType((KClass<Object>)this).hashCode();
    }
    
    @Override
    public final String toString() {
        return this.jClass.toString() + " (Kotlin reflection is not available)";
    }
    
    @Override
    public final Class<?> getJClass() {
        return this.jClass;
    }
    
    public ClassReference(final Class<?> jClass) {
        Intrinsics.checkParameterIsNotNull(jClass, "jClass");
        this.jClass = jClass;
    }
}
