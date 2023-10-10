// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.logging.log4j.core.appender.rolling;

import java.util.Collection;
import java.util.concurrent.ArrayBlockingQueue;
import java.io.FileOutputStream;
import org.apache.logging.log4j.core.util.FileUtils;
import org.apache.logging.log4j.core.appender.ConfigurationFactoryData;
import org.apache.logging.log4j.core.appender.rolling.action.Action;
import org.apache.logging.log4j.core.appender.rolling.action.AbstractAction;
import org.apache.logging.log4j.Logger;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.util.Iterator;
import java.io.IOException;
import java.util.Date;
import org.apache.logging.log4j.core.LifeCycle2;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.appender.AbstractManager;
import org.apache.logging.log4j.core.appender.ManagerFactory;
import org.apache.logging.log4j.core.appender.OutputStreamManager;
import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.core.LifeCycle;
import java.io.File;
import org.apache.logging.log4j.core.LoggerContext;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.nio.ByteBuffer;
import org.apache.logging.log4j.core.util.Constants;
import java.io.Serializable;
import org.apache.logging.log4j.core.Layout;
import java.io.OutputStream;
import java.util.concurrent.atomic.AtomicReferenceFieldUpdater;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.CopyOnWriteArrayList;
import org.apache.logging.log4j.core.util.Log4jThreadFactory;
import java.util.concurrent.Semaphore;
import java.nio.file.attribute.FileTime;
import org.apache.logging.log4j.core.appender.FileManager;

public class RollingFileManager extends FileManager
{
    private static RollingFileManagerFactory factory;
    private static final int MAX_TRIES = 3;
    private static final int MIN_DURATION = 100;
    private static final FileTime EPOCH;
    protected long size;
    private long initialTime;
    private volatile PatternProcessor patternProcessor;
    private final Semaphore semaphore;
    private final Log4jThreadFactory threadFactory;
    private volatile TriggeringPolicy triggeringPolicy;
    private volatile RolloverStrategy rolloverStrategy;
    private volatile boolean renameEmptyFiles;
    private volatile boolean initialized;
    private volatile String fileName;
    private final boolean directWrite;
    private final CopyOnWriteArrayList<RolloverListener> rolloverListeners;
    private final ExecutorService asyncExecutor;
    private static final AtomicReferenceFieldUpdater<RollingFileManager, TriggeringPolicy> triggeringPolicyUpdater;
    private static final AtomicReferenceFieldUpdater<RollingFileManager, RolloverStrategy> rolloverStrategyUpdater;
    private static final AtomicReferenceFieldUpdater<RollingFileManager, PatternProcessor> patternProcessorUpdater;
    
    @Deprecated
    protected RollingFileManager(final String fileName, final String pattern, final OutputStream os, final boolean append, final long size, final long initialTime, final TriggeringPolicy triggeringPolicy, final RolloverStrategy rolloverStrategy, final String advertiseURI, final Layout<? extends Serializable> layout, final int bufferSize, final boolean writeHeader) {
        this(fileName, pattern, os, append, size, initialTime, triggeringPolicy, rolloverStrategy, advertiseURI, layout, writeHeader, ByteBuffer.wrap(new byte[Constants.ENCODER_BYTE_BUFFER_SIZE]));
    }
    
    @Deprecated
    protected RollingFileManager(final String fileName, final String pattern, final OutputStream os, final boolean append, final long size, final long initialTime, final TriggeringPolicy triggeringPolicy, final RolloverStrategy rolloverStrategy, final String advertiseURI, final Layout<? extends Serializable> layout, final boolean writeHeader, final ByteBuffer buffer) {
        super((fileName != null) ? fileName : pattern, os, append, false, advertiseURI, layout, writeHeader, buffer);
        this.semaphore = new Semaphore(1);
        this.threadFactory = Log4jThreadFactory.createThreadFactory("RollingFileManager");
        this.rolloverListeners = new CopyOnWriteArrayList<RolloverListener>();
        this.asyncExecutor = new ThreadPoolExecutor(0, Integer.MAX_VALUE, 0L, TimeUnit.MILLISECONDS, new EmptyQueue(), this.threadFactory);
        this.size = size;
        this.initialTime = initialTime;
        this.triggeringPolicy = triggeringPolicy;
        this.rolloverStrategy = rolloverStrategy;
        (this.patternProcessor = new PatternProcessor(pattern)).setPrevFileTime(initialTime);
        this.fileName = fileName;
        this.directWrite = (rolloverStrategy instanceof DirectWriteRolloverStrategy);
    }
    
    @Deprecated
    protected RollingFileManager(final LoggerContext loggerContext, final String fileName, final String pattern, final OutputStream os, final boolean append, final boolean createOnDemand, final long size, final long initialTime, final TriggeringPolicy triggeringPolicy, final RolloverStrategy rolloverStrategy, final String advertiseURI, final Layout<? extends Serializable> layout, final boolean writeHeader, final ByteBuffer buffer) {
        super(loggerContext, (fileName != null) ? fileName : pattern, os, append, false, createOnDemand, advertiseURI, layout, writeHeader, buffer);
        this.semaphore = new Semaphore(1);
        this.threadFactory = Log4jThreadFactory.createThreadFactory("RollingFileManager");
        this.rolloverListeners = new CopyOnWriteArrayList<RolloverListener>();
        this.asyncExecutor = new ThreadPoolExecutor(0, Integer.MAX_VALUE, 0L, TimeUnit.MILLISECONDS, new EmptyQueue(), this.threadFactory);
        this.size = size;
        this.initialTime = initialTime;
        this.triggeringPolicy = triggeringPolicy;
        this.rolloverStrategy = rolloverStrategy;
        (this.patternProcessor = new PatternProcessor(pattern)).setPrevFileTime(initialTime);
        this.fileName = fileName;
        this.directWrite = (rolloverStrategy instanceof DirectWriteRolloverStrategy);
    }
    
    protected RollingFileManager(final LoggerContext loggerContext, final String fileName, final String pattern, final OutputStream os, final boolean append, final boolean createOnDemand, final long size, final long initialTime, final TriggeringPolicy triggeringPolicy, final RolloverStrategy rolloverStrategy, final String advertiseURI, final Layout<? extends Serializable> layout, final String filePermissions, final String fileOwner, final String fileGroup, final boolean writeHeader, final ByteBuffer buffer) {
        super(loggerContext, (fileName != null) ? fileName : pattern, os, append, false, createOnDemand, advertiseURI, layout, filePermissions, fileOwner, fileGroup, writeHeader, buffer);
        this.semaphore = new Semaphore(1);
        this.threadFactory = Log4jThreadFactory.createThreadFactory("RollingFileManager");
        this.rolloverListeners = new CopyOnWriteArrayList<RolloverListener>();
        this.asyncExecutor = new ThreadPoolExecutor(0, Integer.MAX_VALUE, 0L, TimeUnit.MILLISECONDS, new EmptyQueue(), this.threadFactory);
        this.size = size;
        this.initialTime = initialTime;
        (this.patternProcessor = new PatternProcessor(pattern)).setPrevFileTime(initialTime);
        this.triggeringPolicy = triggeringPolicy;
        this.rolloverStrategy = rolloverStrategy;
        this.fileName = fileName;
        this.directWrite = (rolloverStrategy instanceof DirectFileRolloverStrategy);
    }
    
    public void initialize() {
        if (!this.initialized) {
            RollingFileManager.LOGGER.debug("Initializing triggering policy {}", this.triggeringPolicy);
            this.initialized = true;
            if (this.directWrite) {
                final File file = new File(this.getFileName());
                if (file.exists()) {
                    this.size = file.length();
                }
                else {
                    ((DirectFileRolloverStrategy)this.rolloverStrategy).clearCurrentFileName();
                }
            }
            this.triggeringPolicy.initialize(this);
            if (this.triggeringPolicy instanceof LifeCycle) {
                ((LifeCycle)this.triggeringPolicy).start();
            }
            if (this.directWrite) {
                final File file = new File(this.getFileName());
                if (file.exists()) {
                    this.size = file.length();
                }
                else {
                    ((DirectFileRolloverStrategy)this.rolloverStrategy).clearCurrentFileName();
                }
            }
        }
    }
    
    public static RollingFileManager getFileManager(final String fileName, final String pattern, final boolean append, final boolean bufferedIO, final TriggeringPolicy policy, final RolloverStrategy strategy, final String advertiseURI, final Layout<? extends Serializable> layout, final int bufferSize, final boolean immediateFlush, final boolean createOnDemand, final String filePermissions, final String fileOwner, final String fileGroup, final Configuration configuration) {
        if (strategy instanceof DirectWriteRolloverStrategy && fileName != null) {
            RollingFileManager.LOGGER.error("The fileName attribute must not be specified with the DirectWriteRolloverStrategy");
            return null;
        }
        final String name = (fileName == null) ? pattern : fileName;
        return AbstractManager.narrow(RollingFileManager.class, OutputStreamManager.getManager(name, new FactoryData(fileName, pattern, append, bufferedIO, policy, strategy, advertiseURI, layout, bufferSize, immediateFlush, createOnDemand, filePermissions, fileOwner, fileGroup, configuration), RollingFileManager.factory));
    }
    
    public void addRolloverListener(final RolloverListener listener) {
        this.rolloverListeners.add(listener);
    }
    
    public void removeRolloverListener(final RolloverListener listener) {
        this.rolloverListeners.remove(listener);
    }
    
    @Override
    public String getFileName() {
        if (this.directWrite) {
            this.fileName = ((DirectFileRolloverStrategy)this.rolloverStrategy).getCurrentFileName(this);
        }
        return this.fileName;
    }
    
    @Override
    protected void createParentDir(final File file) {
        if (this.directWrite) {
            file.getParentFile().mkdirs();
        }
    }
    
    public boolean isDirectWrite() {
        return this.directWrite;
    }
    
    public FileExtension getFileExtension() {
        return this.patternProcessor.getFileExtension();
    }
    
    @Override
    protected synchronized void write(final byte[] bytes, final int offset, final int length, final boolean immediateFlush) {
        super.write(bytes, offset, length, immediateFlush);
    }
    
    @Override
    protected synchronized void writeToDestination(final byte[] bytes, final int offset, final int length) {
        this.size += length;
        super.writeToDestination(bytes, offset, length);
    }
    
    public boolean isRenameEmptyFiles() {
        return this.renameEmptyFiles;
    }
    
    public void setRenameEmptyFiles(final boolean renameEmptyFiles) {
        this.renameEmptyFiles = renameEmptyFiles;
    }
    
    public long getFileSize() {
        return this.size + this.byteBuffer.position();
    }
    
    public long getFileTime() {
        return this.initialTime;
    }
    
    public synchronized void checkRollover(final LogEvent event) {
        if (this.triggeringPolicy.isTriggeringEvent(event)) {
            this.rollover();
        }
    }
    
    @Override
    public boolean releaseSub(final long timeout, final TimeUnit timeUnit) {
        RollingFileManager.LOGGER.debug("Shutting down RollingFileManager {}", this.getName());
        boolean stopped = true;
        if (this.triggeringPolicy instanceof LifeCycle2) {
            stopped &= ((LifeCycle2)this.triggeringPolicy).stop(timeout, timeUnit);
        }
        else if (this.triggeringPolicy instanceof LifeCycle) {
            ((LifeCycle)this.triggeringPolicy).stop();
            stopped &= true;
        }
        final boolean status = super.releaseSub(timeout, timeUnit) && stopped;
        this.asyncExecutor.shutdown();
        try {
            final long millis = timeUnit.toMillis(timeout);
            final long waitInterval = (100L < millis) ? millis : 100L;
            for (int count = 1; count <= 3 && !this.asyncExecutor.isTerminated(); ++count) {
                this.asyncExecutor.awaitTermination(waitInterval * count, TimeUnit.MILLISECONDS);
            }
            if (this.asyncExecutor.isTerminated()) {
                RollingFileManager.LOGGER.debug("All asynchronous threads have terminated");
            }
            else {
                this.asyncExecutor.shutdownNow();
                try {
                    this.asyncExecutor.awaitTermination(timeout, timeUnit);
                    if (this.asyncExecutor.isTerminated()) {
                        RollingFileManager.LOGGER.debug("All asynchronous threads have terminated");
                    }
                    else {
                        RollingFileManager.LOGGER.debug("RollingFileManager shutting down but some asynchronous services may not have completed");
                    }
                }
                catch (InterruptedException inner) {
                    RollingFileManager.LOGGER.warn("RollingFileManager stopped but some asynchronous services may not have completed.");
                }
            }
        }
        catch (InterruptedException ie) {
            this.asyncExecutor.shutdownNow();
            try {
                this.asyncExecutor.awaitTermination(timeout, timeUnit);
                if (this.asyncExecutor.isTerminated()) {
                    RollingFileManager.LOGGER.debug("All asynchronous threads have terminated");
                }
            }
            catch (InterruptedException inner2) {
                RollingFileManager.LOGGER.warn("RollingFileManager stopped but some asynchronous services may not have completed.");
            }
            Thread.currentThread().interrupt();
        }
        RollingFileManager.LOGGER.debug("RollingFileManager shutdown completed with status {}", (Object)status);
        return status;
    }
    
    public synchronized void rollover(final Date prevFileTime, final Date prevRollTime) {
        this.getPatternProcessor().setPrevFileTime(prevFileTime.getTime());
        this.getPatternProcessor().setCurrentFileTime(prevRollTime.getTime());
        this.rollover();
    }
    
    public synchronized void rollover() {
        if (!this.hasOutputStream() && !this.isCreateOnDemand() && !this.isDirectWrite()) {
            return;
        }
        final String currentFileName = this.fileName;
        if (this.rolloverListeners.size() > 0) {
            for (final RolloverListener listener : this.rolloverListeners) {
                try {
                    listener.rolloverTriggered(currentFileName);
                }
                catch (Exception ex) {
                    RollingFileManager.LOGGER.warn("Rollover Listener {} failed with {}: {}", listener.getClass().getSimpleName(), ex.getClass().getName(), ex.getMessage());
                }
            }
        }
        final boolean interrupted = Thread.interrupted();
        try {
            if (interrupted) {
                RollingFileManager.LOGGER.warn("RollingFileManager cleared thread interrupted state, continue to rollover");
            }
            if (this.rollover(this.rolloverStrategy)) {
                try {
                    this.size = 0L;
                    this.initialTime = System.currentTimeMillis();
                    this.createFileAfterRollover();
                }
                catch (IOException e) {
                    this.logError("Failed to create file after rollover", e);
                }
            }
        }
        finally {
            if (interrupted) {
                Thread.currentThread().interrupt();
            }
        }
        if (this.rolloverListeners.size() > 0) {
            for (final RolloverListener listener2 : this.rolloverListeners) {
                try {
                    listener2.rolloverComplete(currentFileName);
                }
                catch (Exception ex2) {
                    RollingFileManager.LOGGER.warn("Rollover Listener {} failed with {}: {}", listener2.getClass().getSimpleName(), ex2.getClass().getName(), ex2.getMessage());
                }
            }
        }
    }
    
    protected void createFileAfterRollover() throws IOException {
        this.setOutputStream(this.createOutputStream());
    }
    
    public PatternProcessor getPatternProcessor() {
        return this.patternProcessor;
    }
    
    public void setTriggeringPolicy(final TriggeringPolicy triggeringPolicy) {
        triggeringPolicy.initialize(this);
        final TriggeringPolicy policy = this.triggeringPolicy;
        int count = 0;
        boolean policyUpdated = false;
        do {
            ++count;
        } while (!(policyUpdated = RollingFileManager.triggeringPolicyUpdater.compareAndSet(this, this.triggeringPolicy, triggeringPolicy)) && count < 3);
        if (policyUpdated) {
            if (triggeringPolicy instanceof LifeCycle) {
                ((LifeCycle)triggeringPolicy).start();
            }
            if (policy instanceof LifeCycle) {
                ((LifeCycle)policy).stop();
            }
        }
        else if (triggeringPolicy instanceof LifeCycle) {
            ((LifeCycle)triggeringPolicy).stop();
        }
    }
    
    public void setRolloverStrategy(final RolloverStrategy rolloverStrategy) {
        RollingFileManager.rolloverStrategyUpdater.compareAndSet(this, this.rolloverStrategy, rolloverStrategy);
    }
    
    public void setPatternProcessor(final PatternProcessor patternProcessor) {
        RollingFileManager.patternProcessorUpdater.compareAndSet(this, this.patternProcessor, patternProcessor);
    }
    
    public <T extends TriggeringPolicy> T getTriggeringPolicy() {
        return (T)this.triggeringPolicy;
    }
    
    Semaphore getSemaphore() {
        return this.semaphore;
    }
    
    public RolloverStrategy getRolloverStrategy() {
        return this.rolloverStrategy;
    }
    
    private boolean rollover(final RolloverStrategy strategy) {
        boolean releaseRequired = false;
        try {
            this.semaphore.acquire();
            releaseRequired = true;
        }
        catch (InterruptedException e) {
            this.logError("Thread interrupted while attempting to check rollover", e);
            return false;
        }
        boolean success = true;
        try {
            final RolloverDescription descriptor = strategy.rollover(this);
            if (descriptor != null) {
                this.writeFooter();
                this.closeOutputStream();
                if (descriptor.getSynchronous() != null) {
                    RollingFileManager.LOGGER.debug("RollingFileManager executing synchronous {}", descriptor.getSynchronous());
                    try {
                        success = descriptor.getSynchronous().execute();
                    }
                    catch (Exception ex) {
                        success = false;
                        this.logError("Caught error in synchronous task", ex);
                    }
                }
                if (success && descriptor.getAsynchronous() != null) {
                    RollingFileManager.LOGGER.debug("RollingFileManager executing async {}", descriptor.getAsynchronous());
                    this.asyncExecutor.execute(new AsyncAction(descriptor.getAsynchronous(), this));
                    releaseRequired = false;
                }
                return true;
            }
            return false;
        }
        finally {
            if (releaseRequired) {
                this.semaphore.release();
            }
        }
    }
    
    @Override
    public void updateData(final Object data) {
        final FactoryData factoryData = (FactoryData)data;
        this.setRolloverStrategy(factoryData.getRolloverStrategy());
        this.setPatternProcessor(new PatternProcessor(factoryData.getPattern(), this.getPatternProcessor()));
        this.setTriggeringPolicy(factoryData.getTriggeringPolicy());
    }
    
    private static long initialFileTime(final File file) {
        final Path path = file.toPath();
        if (Files.exists(path, new LinkOption[0])) {
            try {
                final BasicFileAttributes attrs = Files.readAttributes(path, BasicFileAttributes.class, new LinkOption[0]);
                final FileTime fileTime = attrs.creationTime();
                if (fileTime.compareTo(RollingFileManager.EPOCH) > 0) {
                    RollingFileManager.LOGGER.debug("Returning file creation time for {}", file.getAbsolutePath());
                    return fileTime.toMillis();
                }
                RollingFileManager.LOGGER.info("Unable to obtain file creation time for " + file.getAbsolutePath());
            }
            catch (Exception ex) {
                RollingFileManager.LOGGER.info("Unable to calculate file creation time for " + file.getAbsolutePath() + ": " + ex.getMessage());
            }
        }
        return file.lastModified();
    }
    
    static {
        RollingFileManager.factory = new RollingFileManagerFactory();
        EPOCH = FileTime.fromMillis(0L);
        triggeringPolicyUpdater = AtomicReferenceFieldUpdater.newUpdater(RollingFileManager.class, TriggeringPolicy.class, "triggeringPolicy");
        rolloverStrategyUpdater = AtomicReferenceFieldUpdater.newUpdater(RollingFileManager.class, RolloverStrategy.class, "rolloverStrategy");
        patternProcessorUpdater = AtomicReferenceFieldUpdater.newUpdater(RollingFileManager.class, PatternProcessor.class, "patternProcessor");
    }
    
    private static class AsyncAction extends AbstractAction
    {
        private final Action action;
        private final RollingFileManager manager;
        
        public AsyncAction(final Action act, final RollingFileManager manager) {
            this.action = act;
            this.manager = manager;
        }
        
        @Override
        public boolean execute() throws IOException {
            try {
                return this.action.execute();
            }
            finally {
                this.manager.semaphore.release();
            }
        }
        
        @Override
        public void close() {
            this.action.close();
        }
        
        @Override
        public boolean isComplete() {
            return this.action.isComplete();
        }
        
        @Override
        public String toString() {
            final StringBuilder builder = new StringBuilder();
            builder.append(super.toString());
            builder.append("[action=");
            builder.append(this.action);
            builder.append(", manager=");
            builder.append(this.manager);
            builder.append(", isComplete()=");
            builder.append(this.isComplete());
            builder.append(", isInterrupted()=");
            builder.append(this.isInterrupted());
            builder.append("]");
            return builder.toString();
        }
    }
    
    private static class FactoryData extends ConfigurationFactoryData
    {
        private final String fileName;
        private final String pattern;
        private final boolean append;
        private final boolean bufferedIO;
        private final int bufferSize;
        private final boolean immediateFlush;
        private final boolean createOnDemand;
        private final TriggeringPolicy policy;
        private final RolloverStrategy strategy;
        private final String advertiseURI;
        private final Layout<? extends Serializable> layout;
        private final String filePermissions;
        private final String fileOwner;
        private final String fileGroup;
        
        public FactoryData(final String fileName, final String pattern, final boolean append, final boolean bufferedIO, final TriggeringPolicy policy, final RolloverStrategy strategy, final String advertiseURI, final Layout<? extends Serializable> layout, final int bufferSize, final boolean immediateFlush, final boolean createOnDemand, final String filePermissions, final String fileOwner, final String fileGroup, final Configuration configuration) {
            super(configuration);
            this.fileName = fileName;
            this.pattern = pattern;
            this.append = append;
            this.bufferedIO = bufferedIO;
            this.bufferSize = bufferSize;
            this.policy = policy;
            this.strategy = strategy;
            this.advertiseURI = advertiseURI;
            this.layout = layout;
            this.immediateFlush = immediateFlush;
            this.createOnDemand = createOnDemand;
            this.filePermissions = filePermissions;
            this.fileOwner = fileOwner;
            this.fileGroup = fileGroup;
        }
        
        public TriggeringPolicy getTriggeringPolicy() {
            return this.policy;
        }
        
        public RolloverStrategy getRolloverStrategy() {
            return this.strategy;
        }
        
        public String getPattern() {
            return this.pattern;
        }
        
        @Override
        public String toString() {
            final StringBuilder builder = new StringBuilder();
            builder.append(super.toString());
            builder.append("[pattern=");
            builder.append(this.pattern);
            builder.append(", append=");
            builder.append(this.append);
            builder.append(", bufferedIO=");
            builder.append(this.bufferedIO);
            builder.append(", bufferSize=");
            builder.append(this.bufferSize);
            builder.append(", policy=");
            builder.append(this.policy);
            builder.append(", strategy=");
            builder.append(this.strategy);
            builder.append(", advertiseURI=");
            builder.append(this.advertiseURI);
            builder.append(", layout=");
            builder.append(this.layout);
            builder.append(", filePermissions=");
            builder.append(this.filePermissions);
            builder.append(", fileOwner=");
            builder.append(this.fileOwner);
            builder.append("]");
            return builder.toString();
        }
    }
    
    private static class RollingFileManagerFactory implements ManagerFactory<RollingFileManager, FactoryData>
    {
        @Override
        public RollingFileManager createManager(final String name, final FactoryData data) {
            long size = 0L;
            File file = null;
            if (data.fileName != null) {
                file = new File(data.fileName);
                try {
                    FileUtils.makeParentDirs(file);
                    final boolean created = !data.createOnDemand && file.createNewFile();
                    RollingFileManager.LOGGER.trace("New file '{}' created = {}", name, created);
                }
                catch (IOException ioe) {
                    RollingFileManager.LOGGER.error("Unable to create file " + name, ioe);
                    return null;
                }
                size = (data.append ? file.length() : 0L);
            }
            try {
                final int actualSize = data.bufferedIO ? data.bufferSize : Constants.ENCODER_BYTE_BUFFER_SIZE;
                final ByteBuffer buffer = ByteBuffer.wrap(new byte[actualSize]);
                final OutputStream os = (data.createOnDemand || data.fileName == null) ? null : new FileOutputStream(data.fileName, data.append);
                final long initialTime = (file == null || !file.exists()) ? 0L : initialFileTime(file);
                final boolean writeHeader = file != null && file.exists() && file.length() == 0L;
                final RollingFileManager rm = new RollingFileManager(data.getLoggerContext(), data.fileName, data.pattern, os, data.append, data.createOnDemand, size, initialTime, data.policy, data.strategy, data.advertiseURI, data.layout, data.filePermissions, data.fileOwner, data.fileGroup, writeHeader, buffer);
                if (os != null && rm.isAttributeViewEnabled()) {
                    rm.defineAttributeView(file.toPath());
                }
                return rm;
            }
            catch (IOException ex) {
                RollingFileManager.LOGGER.error("RollingFileManager (" + name + ") " + ex, ex);
                return null;
            }
        }
    }
    
    private static class EmptyQueue extends ArrayBlockingQueue<Runnable>
    {
        private static final long serialVersionUID = 1L;
        
        EmptyQueue() {
            super(1);
        }
        
        @Override
        public int remainingCapacity() {
            return 0;
        }
        
        @Override
        public boolean add(final Runnable runnable) {
            throw new IllegalStateException("Queue is full");
        }
        
        @Override
        public void put(final Runnable runnable) throws InterruptedException {
            throw new InterruptedException("Unable to insert into queue");
        }
        
        @Override
        public boolean offer(final Runnable runnable, final long timeout, final TimeUnit timeUnit) throws InterruptedException {
            Thread.sleep(timeUnit.toMillis(timeout));
            return false;
        }
        
        @Override
        public boolean addAll(final Collection<? extends Runnable> collection) {
            if (collection.size() > 0) {
                throw new IllegalArgumentException("Too many items in collection");
            }
            return false;
        }
    }
}
