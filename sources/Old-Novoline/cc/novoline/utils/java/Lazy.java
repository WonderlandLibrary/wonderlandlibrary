package cc.novoline.utils.java;

import java.lang.ref.WeakReference;
import java.util.Objects;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.function.Supplier;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * A wrapper for a value initialized once needed.
 *
 * @param <T> type of wrapped value
 * @author JarvisCraft
 */
public interface Lazy<T> extends Supplier<T> {

    /**
     * Gets the value wrapped initializing it once requested.
     *
     * @return value wrapped by this lazy
     */
    @Override
    T get();

    /**
     * Tests if the value of this lazy was initialized.
     *
     * @return {@code true} if this lazy's value was initialize and {@code false} otherwise
     */
    boolean isInitialized();

    ///////////////////////////////////////////////////////////////////////////
    // Static factories
    ///////////////////////////////////////////////////////////////////////////

    /**
     * Creates a new lazy creating its value using the given supplier.
     *
     * @param valueSupplier supplier of the value to be called once needed
     * @param <T>           type of value wrapped
     * @return created lazy
     * @apiNote might be thread-unsafe
     */
    static <T> Lazy<T> create(@NotNull Supplier<T> valueSupplier) {
        return new SimpleLazy<>(valueSupplier);
    }

    /**
     * Creates a new thread-safe lazy creating its value using the given supplier.
     *
     * @param valueSupplier supplier of the value to be called once needed
     * @param <T>           type of value wrapped
     * @return created lazy
     */
    static <T> Lazy<T> createThreadSafe(@NotNull Supplier<T> valueSupplier) {
        return new DoubleCheckedLazy<>(valueSupplier);
    }

    /**
     * Creates a new weak lazy creating its value using the given supplier.
     *
     * @param valueSupplier supplier of the value to be called once needed
     * @param <T>           type of value wrapped
     * @return created lazy
     * @apiNote might be thread-unsafe
     * @apiNote weak lazy stores the value wrapped in weak reference and so it may be GCed
     * and so the new one might be recomputed using the value supplier
     */
    static <T> Lazy<T> createWeak(@NotNull Supplier<T> valueSupplier) {
        return new SimpleWeakLazy<>(valueSupplier);
    }

    /**
     * Creates a new weak thread-safe lazy creating its value using the given supplier.
     *
     * @param valueSupplier supplier of the value to be called once needed
     * @param <T>           type of value wrapped
     * @return created lazy
     * @apiNote weak lazy stores the value wrapped in weak reference and so it may be GCed
     * and so the new one might be recomputed using the value supplier
     */
    static <T> Lazy<T> createWeakThreadSafe(@NotNull Supplier<T> valueSupplier) {
        return new LockedWeakLazy<>(valueSupplier);
    }

    /**
     * Non-thread-safe (using double-checked locking) lazy getting its value from the specified value supplier.
     *
     * @param <T> type of wrapped value
     */
    class SimpleLazy<T> implements Lazy<T> {

        /**
         * Supplier used for creation of the value
         */
        protected Supplier<T> valueSupplier;

        protected SimpleLazy(@NotNull Supplier<T> valueSupplier) {
            this.valueSupplier = valueSupplier;
        }

        /**
         * The value stored
         */
        T value;

        @Override
        public T get() {
            if (valueSupplier != null) {
                value = valueSupplier.get();
                valueSupplier = null;
            }

            return value;
        }

        @Override
        public boolean isInitialized() {
            return valueSupplier == null;
        }

        @Nullable
        public Supplier<T> getValueSupplier() {
            return valueSupplier;
        }

        public void setValueSupplier(@Nullable Supplier<T> valueSupplier) {
            this.valueSupplier = valueSupplier;
        }

        public T getValue() {
            return value;
        }

        public void setValue(T value) {
            this.value = value;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof SimpleLazy)) return false;
            SimpleLazy<?> that = (SimpleLazy<?>) o;
            return Objects.equals(valueSupplier, that.valueSupplier) && Objects.equals(value, that.value);
        }

        @Override
        public int hashCode() {
            return Objects.hash(valueSupplier, value);
        }

        @Override
        public String toString() {
            return "SimpleLazy{" + "valueSupplier=" + valueSupplier + ", value=" + value + '}';
        }
    }

    /**
     * Thread-safe (using double-checked locking) lazy getting its value from the specified value supplier.
     *
     * @param <T> type of wrapped value
     */
    class DoubleCheckedLazy<T> implements Lazy<T> {

        /**
         * Mutex used for synchronizations
         */
        @NotNull
        protected final Object mutex;

        /**
         * Supplier used for creation of the value
         */
        @Nullable
        protected volatile Supplier<T> valueSupplier;

        /**
         * The value stored
         */
        protected volatile T value;

        protected DoubleCheckedLazy(@NotNull Supplier<T> valueSupplier) {
            mutex = new Object[0];
            this.valueSupplier = valueSupplier;
        }

        @Override
        public T get() {
            if (valueSupplier != null) synchronized (mutex) {
                if (valueSupplier != null) {
                    // noinspection ConstantConditions
                    value = valueSupplier.get();
                    valueSupplier = null;
                }
            }

            return value;
        }

        @Override
        public boolean isInitialized() {
            if (valueSupplier == null) return true;

            synchronized (mutex) {
                return valueSupplier == null;
            }
        }

        @NotNull
        public Object getMutex() {
            return mutex;
        }

        @Nullable
        public Supplier<T> getValueSupplier() {
            return valueSupplier;
        }

        public void setValueSupplier(@Nullable Supplier<T> valueSupplier) {
            this.valueSupplier = valueSupplier;
        }

        public T getValue() {
            return value;
        }

        public void setValue(T value) {
            this.value = value;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof DoubleCheckedLazy)) return false;
            DoubleCheckedLazy<?> that = (DoubleCheckedLazy<?>) o;
            return mutex.equals(that.mutex) && Objects.equals(valueSupplier, that.valueSupplier) && Objects.equals(value, that.value);
        }

        @Override
        public int hashCode() {
            return Objects.hash(mutex, valueSupplier, value);
        }

        @Override
        public String toString() {
            return "DoubleCheckedLazy{" + "mutex=" + mutex + ", valueSupplier=" + valueSupplier + ", value=" + value + '}';
        }

    }

    /**
     * Non-thread-safe (using double-checked locking) weak lazy getting its value from the specified value supplier.
     *
     * @param <T> type of wrapped value
     * @apiNote weak lazy stores the value wrapped in weak reference and so it may be GCed
     * and so the new one might be recomputed using the value supplier
     */
    class SimpleWeakLazy<@NotNull T> implements Lazy<T> {

        /**
         * Supplier used for creation of the value
         */
        @NotNull
        protected final Supplier<T> valueSupplier;

        /**
         * The value stored wrapped in {@link WeakReference}
         */
        @NotNull
        protected WeakReference<T> value = ReferenceUtil.weakReferenceStub();

        protected SimpleWeakLazy(@NotNull Supplier<T> valueSupplier) {
            this.valueSupplier = valueSupplier;
        }

        @Override
        public T get() {
            if (value.get() == null) {
                T value = valueSupplier.get();
                this.value = new WeakReference<>(value);

                return value;
            }

            return value.get();
        }

        @Override
        public boolean isInitialized() {
            return value.get() != null;
        }

        @NotNull
        public Supplier<T> getValueSupplier() {
            return valueSupplier;
        }

        @NotNull
        public WeakReference<T> getValue() {
            return value;
        }

        public void setValue(@NotNull WeakReference<T> value) {
            this.value = value;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof SimpleWeakLazy)) return false;
            SimpleWeakLazy<?> that = (SimpleWeakLazy<?>) o;
            return valueSupplier.equals(that.valueSupplier) && value.equals(that.value);
        }

        @Override
        public int hashCode() {
            return Objects.hash(valueSupplier, value);
        }

        @Override
        public String toString() {
            return "SimpleWeakLazy{" + "valueSupplier=" + valueSupplier + ", value=" + value + '}';
        }

    }

    /**
     * Thread-safe (using read-write-lock) weak lazy getting its value from the specified value supplier.
     *
     * @param <T> type of wrapped value
     * @apiNote weak lazy stores the value wrapped in weak reference and so it may be GCed
     * and so the new one might be recomputed using the value supplier
     */
    class LockedWeakLazy<@NotNull T> implements Lazy<T> {

        /**
         * Mutex used for synchronizations
         */
        @NotNull
        protected final Lock readLock, writeLock;

        /**
         * Supplier used for creation of the value
         */
        @NotNull
        protected final Supplier<T> valueSupplier;

        /**
         * The value stored wrapped in {@link WeakReference}
         */
        @NotNull
        protected volatile WeakReference<T> value = ReferenceUtil.weakReferenceStub();

        protected LockedWeakLazy(@NotNull Supplier<T> valueSupplier) {
            this.valueSupplier = valueSupplier;

            {
                ReentrantReadWriteLock lock = new ReentrantReadWriteLock();
                readLock = lock.readLock();
                writeLock = lock.writeLock();
            }
        }

        @Override
        public T get() {
            readLock.lock();
            try {
                T value = this.value.get();

                if (value == null) {
                    writeLock.lock();

                    try {
                        this.value = new WeakReference<>(value = valueSupplier.get());
                    } finally {
                        writeLock.unlock();
                    }
                }

                return value;
            } finally {
                readLock.unlock();
            }
        }

        @Override
        public boolean isInitialized() {
            readLock.lock();
            try {
                return value.get() != null;
            } finally {
                readLock.unlock();
            }
        }

        @NotNull
        public Lock getReadLock() {
            return readLock;
        }

        @NotNull
        public Lock getWriteLock() {
            return writeLock;
        }

        @NotNull
        public Supplier<T> getValueSupplier() {
            return valueSupplier;
        }

        @NotNull
        public WeakReference<T> getValue() {
            return value;
        }

        public void setValue(@NotNull WeakReference<T> value) {
            this.value = value;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof LockedWeakLazy)) return false;
            LockedWeakLazy<?> that = (LockedWeakLazy<?>) o;
            return readLock.equals(that.readLock) && writeLock.equals(that.writeLock) && valueSupplier.equals(that.valueSupplier) && value.equals(that.value);
        }

        @Override
        public int hashCode() {
            return Objects.hash(readLock, writeLock, valueSupplier, value);
        }

        @Override
        public String toString() {
            return "LockedWeakLazy{" + "readLock=" + readLock + ", writeLock=" + writeLock + ", valueSupplier=" + valueSupplier + ", value=" + value + '}';
        }
    }
}