/*
 * Decompiled with CFR 0.152.
 */
package jdk.internal.dynalink.beans;

import java.lang.annotation.Annotation;
import java.lang.reflect.AccessibleObject;
import sun.reflect.CallerSensitive;

public class CallerSensitiveDetector {
    private static final DetectionStrategy DETECTION_STRATEGY = CallerSensitiveDetector.getDetectionStrategy();

    static boolean isCallerSensitive(AccessibleObject ao) {
        return DETECTION_STRATEGY.isCallerSensitive(ao);
    }

    private static DetectionStrategy getDetectionStrategy() {
        try {
            return new PrivilegedDetectionStrategy();
        }
        catch (Throwable t) {
            return new UnprivilegedDetectionStrategy();
        }
    }

    private static class UnprivilegedDetectionStrategy
    extends DetectionStrategy {
        private static final String CALLER_SENSITIVE_ANNOTATION_STRING = "@sun.reflect.CallerSensitive()";

        private UnprivilegedDetectionStrategy() {
        }

        @Override
        boolean isCallerSensitive(AccessibleObject o) {
            for (Annotation a : o.getAnnotations()) {
                if (!String.valueOf(a).equals(CALLER_SENSITIVE_ANNOTATION_STRING)) continue;
                return true;
            }
            return false;
        }
    }

    private static class PrivilegedDetectionStrategy
    extends DetectionStrategy {
        private static final Class<? extends Annotation> CALLER_SENSITIVE_ANNOTATION_CLASS = CallerSensitive.class;

        private PrivilegedDetectionStrategy() {
        }

        @Override
        boolean isCallerSensitive(AccessibleObject ao) {
            return ao.getAnnotation(CALLER_SENSITIVE_ANNOTATION_CLASS) != null;
        }
    }

    private static abstract class DetectionStrategy {
        private DetectionStrategy() {
        }

        abstract boolean isCallerSensitive(AccessibleObject var1);
    }
}

