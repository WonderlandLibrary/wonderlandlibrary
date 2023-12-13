/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  javax.annotation.Nullable
 *  javax.annotation.concurrent.ThreadSafe
 */
package com.google.common.util.concurrent;

import com.google.common.annotations.Beta;
import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.Objects;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Lists;
import com.google.common.collect.MapMaker;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.Nullable;
import javax.annotation.concurrent.ThreadSafe;

@Beta
@ThreadSafe
public class CycleDetectingLockFactory {
    private static final ConcurrentMap<Class<? extends Enum>, Map<? extends Enum, LockGraphNode>> lockGraphNodesPerType = new MapMaker().weakKeys().makeMap();
    private static final Logger logger = Logger.getLogger(CycleDetectingLockFactory.class.getName());
    final Policy policy;
    private static final ThreadLocal<ArrayList<LockGraphNode>> acquiredLocks = new ThreadLocal<ArrayList<LockGraphNode>>(){

        @Override
        protected ArrayList<LockGraphNode> initialValue() {
            return Lists.newArrayListWithCapacity(3);
        }
    };

    public static CycleDetectingLockFactory newInstance(Policy policy) {
        return new CycleDetectingLockFactory(policy);
    }

    public ReentrantLock newReentrantLock(String lockName) {
        return this.newReentrantLock(lockName, false);
    }

    public ReentrantLock newReentrantLock(String lockName, boolean fair) {
        return this.policy == Policies.DISABLED ? new ReentrantLock(fair) : new CycleDetectingReentrantLock(new LockGraphNode(lockName), fair);
    }

    public ReentrantReadWriteLock newReentrantReadWriteLock(String lockName) {
        return this.newReentrantReadWriteLock(lockName, false);
    }

    public ReentrantReadWriteLock newReentrantReadWriteLock(String lockName, boolean fair) {
        return this.policy == Policies.DISABLED ? new ReentrantReadWriteLock(fair) : new CycleDetectingReentrantReadWriteLock(new LockGraphNode(lockName), fair);
    }

    public static <E extends Enum<E>> WithExplicitOrdering<E> newInstanceWithExplicitOrdering(Class<E> enumClass, Policy policy) {
        Preconditions.checkNotNull(enumClass);
        Preconditions.checkNotNull(policy);
        Map<? extends Enum, LockGraphNode> lockGraphNodes = CycleDetectingLockFactory.getOrCreateNodes(enumClass);
        return new WithExplicitOrdering<Enum>(policy, lockGraphNodes);
    }

    private static Map<? extends Enum, LockGraphNode> getOrCreateNodes(Class<? extends Enum> clazz) {
        Map<? extends Enum, LockGraphNode> existing = (Map<? extends Enum, LockGraphNode>)lockGraphNodesPerType.get(clazz);
        if (existing != null) {
            return existing;
        }
        Map<? extends Enum, LockGraphNode> created = CycleDetectingLockFactory.createNodes(clazz);
        existing = lockGraphNodesPerType.putIfAbsent(clazz, created);
        return Objects.firstNonNull(existing, created);
    }

    @VisibleForTesting
    static <E extends Enum<E>> Map<E, LockGraphNode> createNodes(Class<E> clazz) {
        int i2;
        EnumMap<E, LockGraphNode> map = Maps.newEnumMap(clazz);
        Enum[] keys = (Enum[])clazz.getEnumConstants();
        int numKeys = keys.length;
        ArrayList<LockGraphNode> nodes = Lists.newArrayListWithCapacity(numKeys);
        for (Enum key : keys) {
            LockGraphNode node = new LockGraphNode(CycleDetectingLockFactory.getLockName(key));
            nodes.add(node);
            map.put(key, node);
        }
        for (i2 = 1; i2 < numKeys; ++i2) {
            ((LockGraphNode)nodes.get(i2)).checkAcquiredLocks(Policies.THROW, nodes.subList(0, i2));
        }
        for (i2 = 0; i2 < numKeys - 1; ++i2) {
            ((LockGraphNode)nodes.get(i2)).checkAcquiredLocks(Policies.DISABLED, nodes.subList(i2 + 1, numKeys));
        }
        return Collections.unmodifiableMap(map);
    }

    private static String getLockName(Enum<?> rank) {
        return rank.getDeclaringClass().getSimpleName() + "." + rank.name();
    }

    private CycleDetectingLockFactory(Policy policy) {
        this.policy = Preconditions.checkNotNull(policy);
    }

    private void aboutToAcquire(CycleDetectingLock lock) {
        if (!lock.isAcquiredByCurrentThread()) {
            ArrayList<LockGraphNode> acquiredLockList = acquiredLocks.get();
            LockGraphNode node = lock.getLockGraphNode();
            node.checkAcquiredLocks(this.policy, acquiredLockList);
            acquiredLockList.add(node);
        }
    }

    private void lockStateChanged(CycleDetectingLock lock) {
        if (!lock.isAcquiredByCurrentThread()) {
            ArrayList<LockGraphNode> acquiredLockList = acquiredLocks.get();
            LockGraphNode node = lock.getLockGraphNode();
            for (int i2 = acquiredLockList.size() - 1; i2 >= 0; --i2) {
                if (acquiredLockList.get(i2) != node) continue;
                acquiredLockList.remove(i2);
                break;
            }
        }
    }

    private class CycleDetectingReentrantWriteLock
    extends ReentrantReadWriteLock.WriteLock {
        final CycleDetectingReentrantReadWriteLock readWriteLock;

        CycleDetectingReentrantWriteLock(CycleDetectingReentrantReadWriteLock readWriteLock) {
            super(readWriteLock);
            this.readWriteLock = readWriteLock;
        }

        /*
         * WARNING - Removed try catching itself - possible behaviour change.
         */
        @Override
        public void lock() {
            CycleDetectingLockFactory.this.aboutToAcquire(this.readWriteLock);
            try {
                super.lock();
            }
            finally {
                CycleDetectingLockFactory.this.lockStateChanged(this.readWriteLock);
            }
        }

        /*
         * WARNING - Removed try catching itself - possible behaviour change.
         */
        @Override
        public void lockInterruptibly() throws InterruptedException {
            CycleDetectingLockFactory.this.aboutToAcquire(this.readWriteLock);
            try {
                super.lockInterruptibly();
            }
            finally {
                CycleDetectingLockFactory.this.lockStateChanged(this.readWriteLock);
            }
        }

        /*
         * WARNING - Removed try catching itself - possible behaviour change.
         */
        @Override
        public boolean tryLock() {
            CycleDetectingLockFactory.this.aboutToAcquire(this.readWriteLock);
            try {
                boolean bl = super.tryLock();
                return bl;
            }
            finally {
                CycleDetectingLockFactory.this.lockStateChanged(this.readWriteLock);
            }
        }

        /*
         * WARNING - Removed try catching itself - possible behaviour change.
         */
        @Override
        public boolean tryLock(long timeout, TimeUnit unit) throws InterruptedException {
            CycleDetectingLockFactory.this.aboutToAcquire(this.readWriteLock);
            try {
                boolean bl = super.tryLock(timeout, unit);
                return bl;
            }
            finally {
                CycleDetectingLockFactory.this.lockStateChanged(this.readWriteLock);
            }
        }

        /*
         * WARNING - Removed try catching itself - possible behaviour change.
         */
        @Override
        public void unlock() {
            try {
                super.unlock();
            }
            finally {
                CycleDetectingLockFactory.this.lockStateChanged(this.readWriteLock);
            }
        }
    }

    private class CycleDetectingReentrantReadLock
    extends ReentrantReadWriteLock.ReadLock {
        final CycleDetectingReentrantReadWriteLock readWriteLock;

        CycleDetectingReentrantReadLock(CycleDetectingReentrantReadWriteLock readWriteLock) {
            super(readWriteLock);
            this.readWriteLock = readWriteLock;
        }

        /*
         * WARNING - Removed try catching itself - possible behaviour change.
         */
        @Override
        public void lock() {
            CycleDetectingLockFactory.this.aboutToAcquire(this.readWriteLock);
            try {
                super.lock();
            }
            finally {
                CycleDetectingLockFactory.this.lockStateChanged(this.readWriteLock);
            }
        }

        /*
         * WARNING - Removed try catching itself - possible behaviour change.
         */
        @Override
        public void lockInterruptibly() throws InterruptedException {
            CycleDetectingLockFactory.this.aboutToAcquire(this.readWriteLock);
            try {
                super.lockInterruptibly();
            }
            finally {
                CycleDetectingLockFactory.this.lockStateChanged(this.readWriteLock);
            }
        }

        /*
         * WARNING - Removed try catching itself - possible behaviour change.
         */
        @Override
        public boolean tryLock() {
            CycleDetectingLockFactory.this.aboutToAcquire(this.readWriteLock);
            try {
                boolean bl = super.tryLock();
                return bl;
            }
            finally {
                CycleDetectingLockFactory.this.lockStateChanged(this.readWriteLock);
            }
        }

        /*
         * WARNING - Removed try catching itself - possible behaviour change.
         */
        @Override
        public boolean tryLock(long timeout, TimeUnit unit) throws InterruptedException {
            CycleDetectingLockFactory.this.aboutToAcquire(this.readWriteLock);
            try {
                boolean bl = super.tryLock(timeout, unit);
                return bl;
            }
            finally {
                CycleDetectingLockFactory.this.lockStateChanged(this.readWriteLock);
            }
        }

        /*
         * WARNING - Removed try catching itself - possible behaviour change.
         */
        @Override
        public void unlock() {
            try {
                super.unlock();
            }
            finally {
                CycleDetectingLockFactory.this.lockStateChanged(this.readWriteLock);
            }
        }
    }

    final class CycleDetectingReentrantReadWriteLock
    extends ReentrantReadWriteLock
    implements CycleDetectingLock {
        private final CycleDetectingReentrantReadLock readLock;
        private final CycleDetectingReentrantWriteLock writeLock;
        private final LockGraphNode lockGraphNode;

        private CycleDetectingReentrantReadWriteLock(LockGraphNode lockGraphNode, boolean fair) {
            super(fair);
            this.readLock = new CycleDetectingReentrantReadLock(this);
            this.writeLock = new CycleDetectingReentrantWriteLock(this);
            this.lockGraphNode = Preconditions.checkNotNull(lockGraphNode);
        }

        @Override
        public ReentrantReadWriteLock.ReadLock readLock() {
            return this.readLock;
        }

        @Override
        public ReentrantReadWriteLock.WriteLock writeLock() {
            return this.writeLock;
        }

        @Override
        public LockGraphNode getLockGraphNode() {
            return this.lockGraphNode;
        }

        @Override
        public boolean isAcquiredByCurrentThread() {
            return this.isWriteLockedByCurrentThread() || this.getReadHoldCount() > 0;
        }
    }

    final class CycleDetectingReentrantLock
    extends ReentrantLock
    implements CycleDetectingLock {
        private final LockGraphNode lockGraphNode;

        private CycleDetectingReentrantLock(LockGraphNode lockGraphNode, boolean fair) {
            super(fair);
            this.lockGraphNode = Preconditions.checkNotNull(lockGraphNode);
        }

        @Override
        public LockGraphNode getLockGraphNode() {
            return this.lockGraphNode;
        }

        @Override
        public boolean isAcquiredByCurrentThread() {
            return this.isHeldByCurrentThread();
        }

        /*
         * WARNING - Removed try catching itself - possible behaviour change.
         */
        @Override
        public void lock() {
            CycleDetectingLockFactory.this.aboutToAcquire(this);
            try {
                super.lock();
            }
            finally {
                CycleDetectingLockFactory.this.lockStateChanged(this);
            }
        }

        /*
         * WARNING - Removed try catching itself - possible behaviour change.
         */
        @Override
        public void lockInterruptibly() throws InterruptedException {
            CycleDetectingLockFactory.this.aboutToAcquire(this);
            try {
                super.lockInterruptibly();
            }
            finally {
                CycleDetectingLockFactory.this.lockStateChanged(this);
            }
        }

        /*
         * WARNING - Removed try catching itself - possible behaviour change.
         */
        @Override
        public boolean tryLock() {
            CycleDetectingLockFactory.this.aboutToAcquire(this);
            try {
                boolean bl = super.tryLock();
                return bl;
            }
            finally {
                CycleDetectingLockFactory.this.lockStateChanged(this);
            }
        }

        /*
         * WARNING - Removed try catching itself - possible behaviour change.
         */
        @Override
        public boolean tryLock(long timeout, TimeUnit unit) throws InterruptedException {
            CycleDetectingLockFactory.this.aboutToAcquire(this);
            try {
                boolean bl = super.tryLock(timeout, unit);
                return bl;
            }
            finally {
                CycleDetectingLockFactory.this.lockStateChanged(this);
            }
        }

        /*
         * WARNING - Removed try catching itself - possible behaviour change.
         */
        @Override
        public void unlock() {
            try {
                super.unlock();
            }
            finally {
                CycleDetectingLockFactory.this.lockStateChanged(this);
            }
        }
    }

    private static class LockGraphNode {
        final Map<LockGraphNode, ExampleStackTrace> allowedPriorLocks = new MapMaker().weakKeys().makeMap();
        final Map<LockGraphNode, PotentialDeadlockException> disallowedPriorLocks = new MapMaker().weakKeys().makeMap();
        final String lockName;

        LockGraphNode(String lockName) {
            this.lockName = Preconditions.checkNotNull(lockName);
        }

        String getLockName() {
            return this.lockName;
        }

        void checkAcquiredLocks(Policy policy, List<LockGraphNode> acquiredLocks) {
            int size = acquiredLocks.size();
            for (int i2 = 0; i2 < size; ++i2) {
                this.checkAcquiredLock(policy, acquiredLocks.get(i2));
            }
        }

        void checkAcquiredLock(Policy policy, LockGraphNode acquiredLock) {
            Preconditions.checkState(this != acquiredLock, "Attempted to acquire multiple locks with the same rank " + acquiredLock.getLockName());
            if (this.allowedPriorLocks.containsKey(acquiredLock)) {
                return;
            }
            PotentialDeadlockException previousDeadlockException = this.disallowedPriorLocks.get(acquiredLock);
            if (previousDeadlockException != null) {
                PotentialDeadlockException exception = new PotentialDeadlockException(acquiredLock, this, previousDeadlockException.getConflictingStackTrace());
                policy.handlePotentialDeadlock(exception);
                return;
            }
            Set<LockGraphNode> seen = Sets.newIdentityHashSet();
            ExampleStackTrace path = acquiredLock.findPathTo(this, seen);
            if (path == null) {
                this.allowedPriorLocks.put(acquiredLock, new ExampleStackTrace(acquiredLock, this));
            } else {
                PotentialDeadlockException exception = new PotentialDeadlockException(acquiredLock, this, path);
                this.disallowedPriorLocks.put(acquiredLock, exception);
                policy.handlePotentialDeadlock(exception);
            }
        }

        @Nullable
        private ExampleStackTrace findPathTo(LockGraphNode node, Set<LockGraphNode> seen) {
            if (!seen.add(this)) {
                return null;
            }
            ExampleStackTrace found = this.allowedPriorLocks.get(node);
            if (found != null) {
                return found;
            }
            for (Map.Entry<LockGraphNode, ExampleStackTrace> entry : this.allowedPriorLocks.entrySet()) {
                LockGraphNode preAcquiredLock = entry.getKey();
                found = preAcquiredLock.findPathTo(node, seen);
                if (found == null) continue;
                ExampleStackTrace path = new ExampleStackTrace(preAcquiredLock, this);
                path.setStackTrace(entry.getValue().getStackTrace());
                path.initCause(found);
                return path;
            }
            return null;
        }
    }

    private static interface CycleDetectingLock {
        public LockGraphNode getLockGraphNode();

        public boolean isAcquiredByCurrentThread();
    }

    @Beta
    public static final class PotentialDeadlockException
    extends ExampleStackTrace {
        private final ExampleStackTrace conflictingStackTrace;

        private PotentialDeadlockException(LockGraphNode node1, LockGraphNode node2, ExampleStackTrace conflictingStackTrace) {
            super(node1, node2);
            this.conflictingStackTrace = conflictingStackTrace;
            this.initCause(conflictingStackTrace);
        }

        public ExampleStackTrace getConflictingStackTrace() {
            return this.conflictingStackTrace;
        }

        @Override
        public String getMessage() {
            StringBuilder message = new StringBuilder(super.getMessage());
            for (Throwable t2 = this.conflictingStackTrace; t2 != null; t2 = t2.getCause()) {
                message.append(", ").append(t2.getMessage());
            }
            return message.toString();
        }
    }

    private static class ExampleStackTrace
    extends IllegalStateException {
        static final StackTraceElement[] EMPTY_STACK_TRACE = new StackTraceElement[0];
        static Set<String> EXCLUDED_CLASS_NAMES = ImmutableSet.of(CycleDetectingLockFactory.class.getName(), ExampleStackTrace.class.getName(), LockGraphNode.class.getName());

        ExampleStackTrace(LockGraphNode node1, LockGraphNode node2) {
            super(node1.getLockName() + " -> " + node2.getLockName());
            StackTraceElement[] origStackTrace = this.getStackTrace();
            int n2 = origStackTrace.length;
            for (int i2 = 0; i2 < n2; ++i2) {
                if (WithExplicitOrdering.class.getName().equals(origStackTrace[i2].getClassName())) {
                    this.setStackTrace(EMPTY_STACK_TRACE);
                    break;
                }
                if (EXCLUDED_CLASS_NAMES.contains(origStackTrace[i2].getClassName())) continue;
                this.setStackTrace(Arrays.copyOfRange(origStackTrace, i2, n2));
                break;
            }
        }
    }

    @Beta
    public static final class WithExplicitOrdering<E extends Enum<E>>
    extends CycleDetectingLockFactory {
        private final Map<E, LockGraphNode> lockGraphNodes;

        @VisibleForTesting
        WithExplicitOrdering(Policy policy, Map<E, LockGraphNode> lockGraphNodes) {
            super(policy);
            this.lockGraphNodes = lockGraphNodes;
        }

        public ReentrantLock newReentrantLock(E rank) {
            return this.newReentrantLock(rank, false);
        }

        public ReentrantLock newReentrantLock(E rank, boolean fair) {
            return this.policy == Policies.DISABLED ? new ReentrantLock(fair) : new CycleDetectingReentrantLock(this.lockGraphNodes.get(rank), fair);
        }

        public ReentrantReadWriteLock newReentrantReadWriteLock(E rank) {
            return this.newReentrantReadWriteLock(rank, false);
        }

        public ReentrantReadWriteLock newReentrantReadWriteLock(E rank, boolean fair) {
            return this.policy == Policies.DISABLED ? new ReentrantReadWriteLock(fair) : new CycleDetectingReentrantReadWriteLock(this.lockGraphNodes.get(rank), fair);
        }
    }

    @Beta
    public static enum Policies implements Policy
    {
        THROW{

            @Override
            public void handlePotentialDeadlock(PotentialDeadlockException e2) {
                throw e2;
            }
        }
        ,
        WARN{

            @Override
            public void handlePotentialDeadlock(PotentialDeadlockException e2) {
                logger.log(Level.SEVERE, "Detected potential deadlock", e2);
            }
        }
        ,
        DISABLED{

            @Override
            public void handlePotentialDeadlock(PotentialDeadlockException e2) {
            }
        };

    }

    @Beta
    @ThreadSafe
    public static interface Policy {
        public void handlePotentialDeadlock(PotentialDeadlockException var1);
    }
}

