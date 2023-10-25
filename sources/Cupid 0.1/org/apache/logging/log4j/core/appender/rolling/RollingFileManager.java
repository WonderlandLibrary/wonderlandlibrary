package org.apache.logging.log4j.core.appender.rolling;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.Serializable;
import java.nio.ByteBuffer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileTime;
import java.util.Collection;
import java.util.Date;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Semaphore;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReferenceFieldUpdater;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.Layout;
import org.apache.logging.log4j.core.LifeCycle;
import org.apache.logging.log4j.core.LifeCycle2;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.core.appender.AbstractManager;
import org.apache.logging.log4j.core.appender.ConfigurationFactoryData;
import org.apache.logging.log4j.core.appender.FileManager;
import org.apache.logging.log4j.core.appender.ManagerFactory;
import org.apache.logging.log4j.core.appender.rolling.action.AbstractAction;
import org.apache.logging.log4j.core.appender.rolling.action.Action;
import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.core.util.Constants;
import org.apache.logging.log4j.core.util.FileUtils;
import org.apache.logging.log4j.core.util.Log4jThreadFactory;

public class RollingFileManager extends FileManager {
  private static RollingFileManagerFactory factory = new RollingFileManagerFactory();
  
  private static final int MAX_TRIES = 3;
  
  private static final int MIN_DURATION = 100;
  
  private static final FileTime EPOCH = FileTime.fromMillis(0L);
  
  protected long size;
  
  private long initialTime;
  
  private volatile PatternProcessor patternProcessor;
  
  private final Semaphore semaphore = new Semaphore(1);
  
  private final Log4jThreadFactory threadFactory = Log4jThreadFactory.createThreadFactory("RollingFileManager");
  
  private volatile TriggeringPolicy triggeringPolicy;
  
  private volatile RolloverStrategy rolloverStrategy;
  
  private volatile boolean renameEmptyFiles;
  
  private volatile boolean initialized;
  
  private volatile String fileName;
  
  private final boolean directWrite;
  
  private final CopyOnWriteArrayList<RolloverListener> rolloverListeners = new CopyOnWriteArrayList<>();
  
  private final ExecutorService asyncExecutor = new ThreadPoolExecutor(0, 2147483647, 0L, TimeUnit.MILLISECONDS, new EmptyQueue(), (ThreadFactory)this.threadFactory);
  
  private static final AtomicReferenceFieldUpdater<RollingFileManager, TriggeringPolicy> triggeringPolicyUpdater = AtomicReferenceFieldUpdater.newUpdater(RollingFileManager.class, TriggeringPolicy.class, "triggeringPolicy");
  
  private static final AtomicReferenceFieldUpdater<RollingFileManager, RolloverStrategy> rolloverStrategyUpdater = AtomicReferenceFieldUpdater.newUpdater(RollingFileManager.class, RolloverStrategy.class, "rolloverStrategy");
  
  private static final AtomicReferenceFieldUpdater<RollingFileManager, PatternProcessor> patternProcessorUpdater = AtomicReferenceFieldUpdater.newUpdater(RollingFileManager.class, PatternProcessor.class, "patternProcessor");
  
  @Deprecated
  protected RollingFileManager(String fileName, String pattern, OutputStream os, boolean append, long size, long initialTime, TriggeringPolicy triggeringPolicy, RolloverStrategy rolloverStrategy, String advertiseURI, Layout<? extends Serializable> layout, int bufferSize, boolean writeHeader) {
    this(fileName, pattern, os, append, size, initialTime, triggeringPolicy, rolloverStrategy, advertiseURI, layout, writeHeader, 
        ByteBuffer.wrap(new byte[Constants.ENCODER_BYTE_BUFFER_SIZE]));
  }
  
  @Deprecated
  protected RollingFileManager(String fileName, String pattern, OutputStream os, boolean append, long size, long initialTime, TriggeringPolicy triggeringPolicy, RolloverStrategy rolloverStrategy, String advertiseURI, Layout<? extends Serializable> layout, boolean writeHeader, ByteBuffer buffer) {
    super((fileName != null) ? fileName : pattern, os, append, false, advertiseURI, layout, writeHeader, buffer);
    this.size = size;
    this.initialTime = initialTime;
    this.triggeringPolicy = triggeringPolicy;
    this.rolloverStrategy = rolloverStrategy;
    this.patternProcessor = new PatternProcessor(pattern);
    this.patternProcessor.setPrevFileTime(initialTime);
    this.fileName = fileName;
    this.directWrite = rolloverStrategy instanceof DirectWriteRolloverStrategy;
  }
  
  @Deprecated
  protected RollingFileManager(LoggerContext loggerContext, String fileName, String pattern, OutputStream os, boolean append, boolean createOnDemand, long size, long initialTime, TriggeringPolicy triggeringPolicy, RolloverStrategy rolloverStrategy, String advertiseURI, Layout<? extends Serializable> layout, boolean writeHeader, ByteBuffer buffer) {
    super(loggerContext, (fileName != null) ? fileName : pattern, os, append, false, createOnDemand, advertiseURI, layout, writeHeader, buffer);
    this.size = size;
    this.initialTime = initialTime;
    this.triggeringPolicy = triggeringPolicy;
    this.rolloverStrategy = rolloverStrategy;
    this.patternProcessor = new PatternProcessor(pattern);
    this.patternProcessor.setPrevFileTime(initialTime);
    this.fileName = fileName;
    this.directWrite = rolloverStrategy instanceof DirectWriteRolloverStrategy;
  }
  
  protected RollingFileManager(LoggerContext loggerContext, String fileName, String pattern, OutputStream os, boolean append, boolean createOnDemand, long size, long initialTime, TriggeringPolicy triggeringPolicy, RolloverStrategy rolloverStrategy, String advertiseURI, Layout<? extends Serializable> layout, String filePermissions, String fileOwner, String fileGroup, boolean writeHeader, ByteBuffer buffer) {
    super(loggerContext, (fileName != null) ? fileName : pattern, os, append, false, createOnDemand, advertiseURI, layout, filePermissions, fileOwner, fileGroup, writeHeader, buffer);
    this.size = size;
    this.initialTime = initialTime;
    this.patternProcessor = new PatternProcessor(pattern);
    this.patternProcessor.setPrevFileTime(initialTime);
    this.triggeringPolicy = triggeringPolicy;
    this.rolloverStrategy = rolloverStrategy;
    this.fileName = fileName;
    this.directWrite = rolloverStrategy instanceof DirectFileRolloverStrategy;
  }
  
  public void initialize() {
    if (!this.initialized) {
      LOGGER.debug("Initializing triggering policy {}", this.triggeringPolicy);
      this.initialized = true;
      if (this.directWrite) {
        File file = new File(getFileName());
        if (file.exists()) {
          this.size = file.length();
        } else {
          ((DirectFileRolloverStrategy)this.rolloverStrategy).clearCurrentFileName();
        } 
      } 
      this.triggeringPolicy.initialize(this);
      if (this.triggeringPolicy instanceof LifeCycle)
        ((LifeCycle)this.triggeringPolicy).start(); 
      if (this.directWrite) {
        File file = new File(getFileName());
        if (file.exists()) {
          this.size = file.length();
        } else {
          ((DirectFileRolloverStrategy)this.rolloverStrategy).clearCurrentFileName();
        } 
      } 
    } 
  }
  
  public static RollingFileManager getFileManager(String fileName, String pattern, boolean append, boolean bufferedIO, TriggeringPolicy policy, RolloverStrategy strategy, String advertiseURI, Layout<? extends Serializable> layout, int bufferSize, boolean immediateFlush, boolean createOnDemand, String filePermissions, String fileOwner, String fileGroup, Configuration configuration) {
    if (strategy instanceof DirectWriteRolloverStrategy && fileName != null) {
      LOGGER.error("The fileName attribute must not be specified with the DirectWriteRolloverStrategy");
      return null;
    } 
    String name = (fileName == null) ? pattern : fileName;
    return (RollingFileManager)narrow(RollingFileManager.class, (AbstractManager)getManager(name, new FactoryData(fileName, pattern, append, bufferedIO, policy, strategy, advertiseURI, layout, bufferSize, immediateFlush, createOnDemand, filePermissions, fileOwner, fileGroup, configuration), factory));
  }
  
  public void addRolloverListener(RolloverListener listener) {
    this.rolloverListeners.add(listener);
  }
  
  public void removeRolloverListener(RolloverListener listener) {
    this.rolloverListeners.remove(listener);
  }
  
  public String getFileName() {
    if (this.directWrite)
      this.fileName = ((DirectFileRolloverStrategy)this.rolloverStrategy).getCurrentFileName(this); 
    return this.fileName;
  }
  
  protected void createParentDir(File file) {
    if (this.directWrite)
      file.getParentFile().mkdirs(); 
  }
  
  public boolean isDirectWrite() {
    return this.directWrite;
  }
  
  public FileExtension getFileExtension() {
    return this.patternProcessor.getFileExtension();
  }
  
  protected synchronized void write(byte[] bytes, int offset, int length, boolean immediateFlush) {
    super.write(bytes, offset, length, immediateFlush);
  }
  
  protected synchronized void writeToDestination(byte[] bytes, int offset, int length) {
    this.size += length;
    super.writeToDestination(bytes, offset, length);
  }
  
  public boolean isRenameEmptyFiles() {
    return this.renameEmptyFiles;
  }
  
  public void setRenameEmptyFiles(boolean renameEmptyFiles) {
    this.renameEmptyFiles = renameEmptyFiles;
  }
  
  public long getFileSize() {
    return this.size + this.byteBuffer.position();
  }
  
  public long getFileTime() {
    return this.initialTime;
  }
  
  public synchronized void checkRollover(LogEvent event) {
    if (this.triggeringPolicy.isTriggeringEvent(event))
      rollover(); 
  }
  
  public boolean releaseSub(long timeout, TimeUnit timeUnit) {
    int i;
    LOGGER.debug("Shutting down RollingFileManager {}", getName());
    boolean stopped = true;
    if (this.triggeringPolicy instanceof LifeCycle2) {
      stopped &= ((LifeCycle2)this.triggeringPolicy).stop(timeout, timeUnit);
    } else if (this.triggeringPolicy instanceof LifeCycle) {
      ((LifeCycle)this.triggeringPolicy).stop();
      i = stopped & true;
    } 
    boolean status = (super.releaseSub(timeout, timeUnit) && i != 0);
    this.asyncExecutor.shutdown();
    try {
      long millis = timeUnit.toMillis(timeout);
      long waitInterval = (100L < millis) ? millis : 100L;
      for (int count = 1; count <= 3 && !this.asyncExecutor.isTerminated(); count++)
        this.asyncExecutor.awaitTermination(waitInterval * count, TimeUnit.MILLISECONDS); 
      if (this.asyncExecutor.isTerminated()) {
        LOGGER.debug("All asynchronous threads have terminated");
      } else {
        this.asyncExecutor.shutdownNow();
        try {
          this.asyncExecutor.awaitTermination(timeout, timeUnit);
          if (this.asyncExecutor.isTerminated()) {
            LOGGER.debug("All asynchronous threads have terminated");
          } else {
            LOGGER.debug("RollingFileManager shutting down but some asynchronous services may not have completed");
          } 
        } catch (InterruptedException inner) {
          LOGGER.warn("RollingFileManager stopped but some asynchronous services may not have completed.");
        } 
      } 
    } catch (InterruptedException ie) {
      this.asyncExecutor.shutdownNow();
      try {
        this.asyncExecutor.awaitTermination(timeout, timeUnit);
        if (this.asyncExecutor.isTerminated())
          LOGGER.debug("All asynchronous threads have terminated"); 
      } catch (InterruptedException inner) {
        LOGGER.warn("RollingFileManager stopped but some asynchronous services may not have completed.");
      } 
      Thread.currentThread().interrupt();
    } 
    LOGGER.debug("RollingFileManager shutdown completed with status {}", Boolean.valueOf(status));
    return status;
  }
  
  public synchronized void rollover(Date prevFileTime, Date prevRollTime) {
    getPatternProcessor().setPrevFileTime(prevFileTime.getTime());
    getPatternProcessor().setCurrentFileTime(prevRollTime.getTime());
    rollover();
  }
  
  public synchronized void rollover() {
    if (!hasOutputStream() && !isCreateOnDemand() && !isDirectWrite())
      return; 
    String currentFileName = this.fileName;
    if (this.rolloverListeners.size() > 0)
      for (RolloverListener listener : this.rolloverListeners) {
        try {
          listener.rolloverTriggered(currentFileName);
        } catch (Exception ex) {
          LOGGER.warn("Rollover Listener {} failed with {}: {}", listener.getClass().getSimpleName(), ex
              .getClass().getName(), ex.getMessage());
        } 
      }  
    boolean interrupted = Thread.interrupted();
    try {
      if (interrupted)
        LOGGER.warn("RollingFileManager cleared thread interrupted state, continue to rollover"); 
      if (rollover(this.rolloverStrategy))
        try {
          this.size = 0L;
          this.initialTime = System.currentTimeMillis();
          createFileAfterRollover();
        } catch (IOException e) {
          logError("Failed to create file after rollover", e);
        }  
    } finally {
      if (interrupted)
        Thread.currentThread().interrupt(); 
    } 
    if (this.rolloverListeners.size() > 0)
      for (RolloverListener listener : this.rolloverListeners) {
        try {
          listener.rolloverComplete(currentFileName);
        } catch (Exception ex) {
          LOGGER.warn("Rollover Listener {} failed with {}: {}", listener.getClass().getSimpleName(), ex
              .getClass().getName(), ex.getMessage());
        } 
      }  
  }
  
  protected void createFileAfterRollover() throws IOException {
    setOutputStream(createOutputStream());
  }
  
  public PatternProcessor getPatternProcessor() {
    return this.patternProcessor;
  }
  
  public void setTriggeringPolicy(TriggeringPolicy triggeringPolicy) {
    triggeringPolicy.initialize(this);
    TriggeringPolicy policy = this.triggeringPolicy;
    int count = 0;
    boolean policyUpdated = false;
    do {
      count++;
    } while (!(policyUpdated = triggeringPolicyUpdater.compareAndSet(this, this.triggeringPolicy, triggeringPolicy)) && count < 3);
    if (policyUpdated) {
      if (triggeringPolicy instanceof LifeCycle)
        ((LifeCycle)triggeringPolicy).start(); 
      if (policy instanceof LifeCycle)
        ((LifeCycle)policy).stop(); 
    } else if (triggeringPolicy instanceof LifeCycle) {
      ((LifeCycle)triggeringPolicy).stop();
    } 
  }
  
  public void setRolloverStrategy(RolloverStrategy rolloverStrategy) {
    rolloverStrategyUpdater.compareAndSet(this, this.rolloverStrategy, rolloverStrategy);
  }
  
  public void setPatternProcessor(PatternProcessor patternProcessor) {
    patternProcessorUpdater.compareAndSet(this, this.patternProcessor, patternProcessor);
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
  
  private boolean rollover(RolloverStrategy strategy) {
    boolean releaseRequired = false;
    try {
      this.semaphore.acquire();
      releaseRequired = true;
    } catch (InterruptedException e) {
      logError("Thread interrupted while attempting to check rollover", e);
      return false;
    } 
    boolean success = true;
    try {
      RolloverDescription descriptor = strategy.rollover(this);
      if (descriptor != null) {
        writeFooter();
        closeOutputStream();
        if (descriptor.getSynchronous() != null) {
          LOGGER.debug("RollingFileManager executing synchronous {}", descriptor.getSynchronous());
          try {
            success = descriptor.getSynchronous().execute();
          } catch (Exception ex) {
            success = false;
            logError("Caught error in synchronous task", ex);
          } 
        } 
        if (success && descriptor.getAsynchronous() != null) {
          LOGGER.debug("RollingFileManager executing async {}", descriptor.getAsynchronous());
          this.asyncExecutor.execute((Runnable)new AsyncAction(descriptor.getAsynchronous(), this));
          releaseRequired = false;
        } 
        return true;
      } 
      return false;
    } finally {
      if (releaseRequired)
        this.semaphore.release(); 
    } 
  }
  
  private static class AsyncAction extends AbstractAction {
    private final Action action;
    
    private final RollingFileManager manager;
    
    public AsyncAction(Action act, RollingFileManager manager) {
      this.action = act;
      this.manager = manager;
    }
    
    public boolean execute() throws IOException {
      try {
        return this.action.execute();
      } finally {
        this.manager.semaphore.release();
      } 
    }
    
    public void close() {
      this.action.close();
    }
    
    public boolean isComplete() {
      return this.action.isComplete();
    }
    
    public String toString() {
      StringBuilder builder = new StringBuilder();
      builder.append(super.toString());
      builder.append("[action=");
      builder.append(this.action);
      builder.append(", manager=");
      builder.append(this.manager);
      builder.append(", isComplete()=");
      builder.append(isComplete());
      builder.append(", isInterrupted()=");
      builder.append(isInterrupted());
      builder.append("]");
      return builder.toString();
    }
  }
  
  private static class FactoryData extends ConfigurationFactoryData {
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
    
    public FactoryData(String fileName, String pattern, boolean append, boolean bufferedIO, TriggeringPolicy policy, RolloverStrategy strategy, String advertiseURI, Layout<? extends Serializable> layout, int bufferSize, boolean immediateFlush, boolean createOnDemand, String filePermissions, String fileOwner, String fileGroup, Configuration configuration) {
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
    
    public String toString() {
      StringBuilder builder = new StringBuilder();
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
  
  public void updateData(Object data) {
    FactoryData factoryData = (FactoryData)data;
    setRolloverStrategy(factoryData.getRolloverStrategy());
    setPatternProcessor(new PatternProcessor(factoryData.getPattern(), getPatternProcessor()));
    setTriggeringPolicy(factoryData.getTriggeringPolicy());
  }
  
  private static class RollingFileManagerFactory implements ManagerFactory<RollingFileManager, FactoryData> {
    private RollingFileManagerFactory() {}
    
    public RollingFileManager createManager(String name, RollingFileManager.FactoryData data) {
      long size = 0L;
      File file = null;
      if (data.fileName != null) {
        file = new File(data.fileName);
        try {
          FileUtils.makeParentDirs(file);
          boolean created = data.createOnDemand ? false : file.createNewFile();
          RollingFileManager.LOGGER.trace("New file '{}' created = {}", name, Boolean.valueOf(created));
        } catch (IOException ioe) {
          RollingFileManager.LOGGER.error("Unable to create file " + name, ioe);
          return null;
        } 
        size = data.append ? file.length() : 0L;
      } 
      try {
        int actualSize = data.bufferedIO ? data.bufferSize : Constants.ENCODER_BYTE_BUFFER_SIZE;
        ByteBuffer buffer = ByteBuffer.wrap(new byte[actualSize]);
        OutputStream os = (data.createOnDemand || data.fileName == null) ? null : new FileOutputStream(data.fileName, data.append);
        long initialTime = (file == null || !file.exists()) ? 0L : RollingFileManager.initialFileTime(file);
        boolean writeHeader = (file != null && file.exists() && file.length() == 0L);
        RollingFileManager rm = new RollingFileManager(data.getLoggerContext(), data.fileName, data.pattern, os, data.append, data.createOnDemand, size, initialTime, data.policy, data.strategy, data.advertiseURI, data.layout, data.filePermissions, data.fileOwner, data.fileGroup, writeHeader, buffer);
        if (os != null && rm.isAttributeViewEnabled())
          rm.defineAttributeView(file.toPath()); 
        return rm;
      } catch (IOException ex) {
        RollingFileManager.LOGGER.error("RollingFileManager (" + name + ") " + ex, ex);
        return null;
      } 
    }
  }
  
  private static long initialFileTime(File file) {
    Path path = file.toPath();
    if (Files.exists(path, new java.nio.file.LinkOption[0]))
      try {
        BasicFileAttributes attrs = Files.readAttributes(path, BasicFileAttributes.class, new java.nio.file.LinkOption[0]);
        FileTime fileTime = attrs.creationTime();
        if (fileTime.compareTo(EPOCH) > 0) {
          LOGGER.debug("Returning file creation time for {}", file.getAbsolutePath());
          return fileTime.toMillis();
        } 
        LOGGER.info("Unable to obtain file creation time for " + file.getAbsolutePath());
      } catch (Exception ex) {
        LOGGER.info("Unable to calculate file creation time for " + file.getAbsolutePath() + ": " + ex.getMessage());
      }  
    return file.lastModified();
  }
  
  private static class EmptyQueue extends ArrayBlockingQueue<Runnable> {
    private static final long serialVersionUID = 1L;
    
    EmptyQueue() {
      super(1);
    }
    
    public int remainingCapacity() {
      return 0;
    }
    
    public boolean add(Runnable runnable) {
      throw new IllegalStateException("Queue is full");
    }
    
    public void put(Runnable runnable) throws InterruptedException {
      throw new InterruptedException("Unable to insert into queue");
    }
    
    public boolean offer(Runnable runnable, long timeout, TimeUnit timeUnit) throws InterruptedException {
      Thread.sleep(timeUnit.toMillis(timeout));
      return false;
    }
    
    public boolean addAll(Collection<? extends Runnable> collection) {
      if (collection.size() > 0)
        throw new IllegalArgumentException("Too many items in collection"); 
      return false;
    }
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\logging\log4j\core\appender\rolling\RollingFileManager.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */