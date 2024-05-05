/*
 * Decompiled with CFR 0.152.
 */
package jdk.internal.dynalink.beans;

import java.lang.invoke.MethodHandle;
import jdk.internal.dynalink.linker.GuardedInvocation;

class GuardedInvocationComponent {
    private final GuardedInvocation guardedInvocation;
    private final Validator validator;

    GuardedInvocationComponent(MethodHandle invocation) {
        this(invocation, null, ValidationType.NONE);
    }

    GuardedInvocationComponent(MethodHandle invocation, MethodHandle guard, ValidationType validationType) {
        this(invocation, guard, null, validationType);
    }

    GuardedInvocationComponent(MethodHandle invocation, MethodHandle guard, Class<?> validatorClass, ValidationType validationType) {
        this(invocation, guard, new Validator(validatorClass, validationType));
    }

    GuardedInvocationComponent(GuardedInvocation guardedInvocation, Class<?> validatorClass, ValidationType validationType) {
        this(guardedInvocation, new Validator(validatorClass, validationType));
    }

    GuardedInvocationComponent replaceInvocation(MethodHandle newInvocation) {
        return this.replaceInvocation(newInvocation, this.guardedInvocation.getGuard());
    }

    GuardedInvocationComponent replaceInvocation(MethodHandle newInvocation, MethodHandle newGuard) {
        return new GuardedInvocationComponent(this.guardedInvocation.replaceMethods(newInvocation, newGuard), this.validator);
    }

    private GuardedInvocationComponent(MethodHandle invocation, MethodHandle guard, Validator validator) {
        this(new GuardedInvocation(invocation, guard), validator);
    }

    private GuardedInvocationComponent(GuardedInvocation guardedInvocation, Validator validator) {
        this.guardedInvocation = guardedInvocation;
        this.validator = validator;
    }

    GuardedInvocation getGuardedInvocation() {
        return this.guardedInvocation;
    }

    Class<?> getValidatorClass() {
        return this.validator.validatorClass;
    }

    ValidationType getValidationType() {
        return this.validator.validationType;
    }

    GuardedInvocationComponent compose(MethodHandle compositeInvocation, MethodHandle otherGuard, Class<?> otherValidatorClass, ValidationType otherValidationType) {
        Validator compositeValidator = this.validator.compose(new Validator(otherValidatorClass, otherValidationType));
        MethodHandle compositeGuard = compositeValidator == this.validator ? this.guardedInvocation.getGuard() : otherGuard;
        return new GuardedInvocationComponent(compositeInvocation, compositeGuard, compositeValidator);
    }

    private static class Validator {
        final Class<?> validatorClass;
        final ValidationType validationType;

        Validator(Class<?> validatorClass, ValidationType validationType) {
            this.validatorClass = validatorClass;
            this.validationType = validationType;
        }

        Validator compose(Validator other) {
            if (other.validationType == ValidationType.NONE) {
                return this;
            }
            block0 : switch (this.validationType) {
                case NONE: {
                    return other;
                }
                case INSTANCE_OF: {
                    switch (other.validationType) {
                        case INSTANCE_OF: {
                            if (this.isAssignableFrom(other)) {
                                return other;
                            }
                            if (!other.isAssignableFrom(this)) break block0;
                            return this;
                        }
                        case EXACT_CLASS: {
                            if (!this.isAssignableFrom(other)) break block0;
                            return other;
                        }
                        case IS_ARRAY: {
                            if (!this.validatorClass.isArray()) break block0;
                            return this;
                        }
                        default: {
                            throw new AssertionError();
                        }
                    }
                }
                case EXACT_CLASS: {
                    switch (other.validationType) {
                        case INSTANCE_OF: {
                            if (!other.isAssignableFrom(this)) break block0;
                            return this;
                        }
                        case EXACT_CLASS: {
                            if (this.validatorClass != other.validatorClass) break block0;
                            return this;
                        }
                        case IS_ARRAY: {
                            if (!this.validatorClass.isArray()) break block0;
                            return this;
                        }
                        default: {
                            throw new AssertionError();
                        }
                    }
                }
                case IS_ARRAY: {
                    switch (other.validationType) {
                        case INSTANCE_OF: 
                        case EXACT_CLASS: {
                            if (!other.validatorClass.isArray()) break block0;
                            return other;
                        }
                        case IS_ARRAY: {
                            return this;
                        }
                        default: {
                            throw new AssertionError();
                        }
                    }
                }
                default: {
                    throw new AssertionError();
                }
            }
            throw new AssertionError((Object)("Incompatible composition " + this + " vs " + other));
        }

        private boolean isAssignableFrom(Validator other) {
            return this.validatorClass.isAssignableFrom(other.validatorClass);
        }

        public String toString() {
            return "Validator[" + (Object)((Object)this.validationType) + (this.validatorClass == null ? "" : " " + this.validatorClass.getName()) + "]";
        }
    }

    static enum ValidationType {
        NONE,
        INSTANCE_OF,
        EXACT_CLASS,
        IS_ARRAY;

    }
}

