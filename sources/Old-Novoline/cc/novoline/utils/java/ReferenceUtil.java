package cc.novoline.utils.java;

import org.checkerframework.checker.nullness.qual.NonNull;

import java.lang.ref.WeakReference;

/**
 * Utility for reference-related features.
 *
 * @author JarvisCraft
 */
public final class ReferenceUtil {

    /**
     * Stub of a {@link WeakReference} actually referencing {@code null}
     */
    @NonNull
    private static final WeakReference<?> WEAK_REFERENCE_STUB = new WeakReference<>(null);

    private ReferenceUtil() {
        throw new java.lang.UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }

    /**
     * Gets a {@link WeakReference} stub singleton.
     *
     * @param <T> type of value intended to be referenced
     * @return {@link WeakReference} stub singleton
     */
    @SuppressWarnings("unchecked")
    public static <T> WeakReference<T> weakReferenceStub() {
        return (WeakReference<T>) WEAK_REFERENCE_STUB;
    }

}