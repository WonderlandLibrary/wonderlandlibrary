/*
 * Decompiled with CFR 0.152.
 */
package jdk.nashorn.internal.objects.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.invoke.MethodHandle;
import jdk.internal.dynalink.CallSiteDescriptor;
import jdk.internal.dynalink.linker.LinkRequest;

@Retention(value=RetentionPolicy.RUNTIME)
@Target(value={ElementType.METHOD})
public @interface SpecializedFunction {
    public String name() default "";

    public Class<?> linkLogic() default LinkLogic.Empty.class;

    public boolean isConstructor() default false;

    public boolean isOptimistic() default false;

    public static abstract class LinkLogic {
        public static final LinkLogic EMPTY_INSTANCE = new Empty();

        public static Class<? extends LinkLogic> getEmptyLinkLogicClass() {
            return Empty.class;
        }

        public Class<? extends Throwable> getRelinkException() {
            return null;
        }

        public static boolean isEmpty(Class<? extends LinkLogic> clazz) {
            return clazz == Empty.class;
        }

        public boolean isEmpty() {
            return false;
        }

        public abstract boolean canLink(Object var1, CallSiteDescriptor var2, LinkRequest var3);

        public boolean needsGuard(Object self) {
            return true;
        }

        public boolean needsGuard(Object self, Object ... args2) {
            return true;
        }

        public MethodHandle getGuard() {
            return null;
        }

        public boolean checkLinkable(Object self, CallSiteDescriptor desc, LinkRequest request) {
            return this.canLink(self, desc, request);
        }

        private static final class Empty
        extends LinkLogic {
            private Empty() {
            }

            @Override
            public boolean canLink(Object self, CallSiteDescriptor desc, LinkRequest request) {
                return true;
            }

            @Override
            public boolean isEmpty() {
                return true;
            }
        }
    }
}

