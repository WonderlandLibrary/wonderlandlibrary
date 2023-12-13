// 
// Decompiled by Procyon v0.6.0
// 

package org.apache.log4j.builders.appender;

import org.apache.logging.log4j.status.StatusLogger;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;
import org.apache.logging.log4j.core.appender.rolling.RolloverStrategy;
import org.apache.log4j.bridge.AppenderWrapper;
import java.io.Serializable;
import org.apache.logging.log4j.core.appender.AbstractOutputStreamAppender;
import org.apache.logging.log4j.core.appender.RollingFileAppender;
import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.core.appender.rolling.DefaultRolloverStrategy;
import org.apache.logging.log4j.core.appender.rolling.CompositeTriggeringPolicy;
import org.apache.logging.log4j.core.appender.rolling.TriggeringPolicy;
import org.apache.logging.log4j.core.appender.rolling.SizeBasedTriggeringPolicy;
import org.apache.log4j.bridge.LayoutAdapter;
import org.apache.log4j.spi.Filter;
import org.apache.log4j.Layout;
import org.apache.log4j.config.Log4j1Configuration;
import org.apache.log4j.config.PropertiesConfiguration;
import org.apache.log4j.Appender;
import org.apache.log4j.xml.XmlConfiguration;
import org.w3c.dom.Element;
import java.util.Properties;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.log4j.builders.AbstractBuilder;

@Plugin(name = "org.apache.log4j.RollingFileAppender", category = "Log4j Builder")
public class RollingFileAppenderBuilder extends AbstractBuilder implements AppenderBuilder
{
    private static final String DEFAULT_MAX_SIZE = "10 MB";
    private static final String DEFAULT_MAX_BACKUPS = "1";
    private static final Logger LOGGER;
    
    public RollingFileAppenderBuilder() {
    }
    
    public RollingFileAppenderBuilder(final String prefix, final Properties properties) {
        super(prefix, properties);
    }
    
    @Override
    public Appender parseAppender(final Element appenderElement, final XmlConfiguration config) {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     1: aload_1         /* appenderElement */
        //     2: invokevirtual   org/apache/log4j/builders/appender/RollingFileAppenderBuilder.getNameAttribute:(Lorg/w3c/dom/Element;)Ljava/lang/String;
        //     5: astore_3        /* name */
        //     6: new             Ljava/util/concurrent/atomic/AtomicReference;
        //     9: dup            
        //    10: invokespecial   java/util/concurrent/atomic/AtomicReference.<init>:()V
        //    13: astore          layout
        //    15: new             Ljava/util/concurrent/atomic/AtomicReference;
        //    18: dup            
        //    19: invokespecial   java/util/concurrent/atomic/AtomicReference.<init>:()V
        //    22: astore          filter
        //    24: new             Ljava/util/concurrent/atomic/AtomicReference;
        //    27: dup            
        //    28: invokespecial   java/util/concurrent/atomic/AtomicReference.<init>:()V
        //    31: astore          fileName
        //    33: new             Ljava/util/concurrent/atomic/AtomicBoolean;
        //    36: dup            
        //    37: iconst_1       
        //    38: invokespecial   java/util/concurrent/atomic/AtomicBoolean.<init>:(Z)V
        //    41: astore          immediateFlush
        //    43: new             Ljava/util/concurrent/atomic/AtomicBoolean;
        //    46: dup            
        //    47: iconst_1       
        //    48: invokespecial   java/util/concurrent/atomic/AtomicBoolean.<init>:(Z)V
        //    51: astore          append
        //    53: new             Ljava/util/concurrent/atomic/AtomicBoolean;
        //    56: dup            
        //    57: invokespecial   java/util/concurrent/atomic/AtomicBoolean.<init>:()V
        //    60: astore          bufferedIo
        //    62: new             Ljava/util/concurrent/atomic/AtomicInteger;
        //    65: dup            
        //    66: sipush          8192
        //    69: invokespecial   java/util/concurrent/atomic/AtomicInteger.<init>:(I)V
        //    72: astore          bufferSize
        //    74: new             Ljava/util/concurrent/atomic/AtomicReference;
        //    77: dup            
        //    78: ldc             "10 MB"
        //    80: invokespecial   java/util/concurrent/atomic/AtomicReference.<init>:(Ljava/lang/Object;)V
        //    83: astore          maxSize
        //    85: new             Ljava/util/concurrent/atomic/AtomicReference;
        //    88: dup            
        //    89: ldc             "1"
        //    91: invokespecial   java/util/concurrent/atomic/AtomicReference.<init>:(Ljava/lang/Object;)V
        //    94: astore          maxBackups
        //    96: new             Ljava/util/concurrent/atomic/AtomicReference;
        //    99: dup            
        //   100: invokespecial   java/util/concurrent/atomic/AtomicReference.<init>:()V
        //   103: astore          level
        //   105: aload_1         /* appenderElement */
        //   106: invokeinterface org/w3c/dom/Element.getChildNodes:()Lorg/w3c/dom/NodeList;
        //   111: aload_0         /* this */
        //   112: aload           layout
        //   114: aload_2         /* config */
        //   115: aload           filter
        //   117: aload           fileName
        //   119: aload           append
        //   121: aload           bufferedIo
        //   123: aload           bufferSize
        //   125: aload           maxBackups
        //   127: aload           maxSize
        //   129: aload           level
        //   131: aload           immediateFlush
        //   133: invokedynamic   BootstrapMethod #0, accept:(Lorg/apache/log4j/builders/appender/RollingFileAppenderBuilder;Ljava/util/concurrent/atomic/AtomicReference;Lorg/apache/log4j/xml/XmlConfiguration;Ljava/util/concurrent/atomic/AtomicReference;Ljava/util/concurrent/atomic/AtomicReference;Ljava/util/concurrent/atomic/AtomicBoolean;Ljava/util/concurrent/atomic/AtomicBoolean;Ljava/util/concurrent/atomic/AtomicInteger;Ljava/util/concurrent/atomic/AtomicReference;Ljava/util/concurrent/atomic/AtomicReference;Ljava/util/concurrent/atomic/AtomicReference;Ljava/util/concurrent/atomic/AtomicBoolean;)Ljava/util/function/Consumer;
        //   138: invokestatic    org/apache/log4j/xml/XmlConfiguration.forEachElement:(Lorg/w3c/dom/NodeList;Ljava/util/function/Consumer;)V
        //   141: aload_0         /* this */
        //   142: aload_3         /* name */
        //   143: aload_2         /* config */
        //   144: aload           layout
        //   146: invokevirtual   java/util/concurrent/atomic/AtomicReference.get:()Ljava/lang/Object;
        //   149: checkcast       Lorg/apache/log4j/Layout;
        //   152: aload           filter
        //   154: invokevirtual   java/util/concurrent/atomic/AtomicReference.get:()Ljava/lang/Object;
        //   157: checkcast       Lorg/apache/log4j/spi/Filter;
        //   160: aload           append
        //   162: invokevirtual   java/util/concurrent/atomic/AtomicBoolean.get:()Z
        //   165: aload           bufferedIo
        //   167: invokevirtual   java/util/concurrent/atomic/AtomicBoolean.get:()Z
        //   170: aload           bufferSize
        //   172: invokevirtual   java/util/concurrent/atomic/AtomicInteger.get:()I
        //   175: aload           immediateFlush
        //   177: invokevirtual   java/util/concurrent/atomic/AtomicBoolean.get:()Z
        //   180: aload           fileName
        //   182: invokevirtual   java/util/concurrent/atomic/AtomicReference.get:()Ljava/lang/Object;
        //   185: checkcast       Ljava/lang/String;
        //   188: aload           level
        //   190: invokevirtual   java/util/concurrent/atomic/AtomicReference.get:()Ljava/lang/Object;
        //   193: checkcast       Ljava/lang/String;
        //   196: aload           maxSize
        //   198: invokevirtual   java/util/concurrent/atomic/AtomicReference.get:()Ljava/lang/Object;
        //   201: checkcast       Ljava/lang/String;
        //   204: aload           maxBackups
        //   206: invokevirtual   java/util/concurrent/atomic/AtomicReference.get:()Ljava/lang/Object;
        //   209: checkcast       Ljava/lang/String;
        //   212: invokespecial   org/apache/log4j/builders/appender/RollingFileAppenderBuilder.createAppender:(Ljava/lang/String;Lorg/apache/log4j/config/Log4j1Configuration;Lorg/apache/log4j/Layout;Lorg/apache/log4j/spi/Filter;ZZIZLjava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)Lorg/apache/log4j/Appender;
        //   215: areturn        
        //    MethodParameters:
        //  Name             Flags  
        //  ---------------  -----
        //  appenderElement  FINAL
        //  config           FINAL
        // 
        // The error that occurred was:
        // 
        // java.lang.NullPointerException
        //     at com.strobel.decompiler.languages.java.ast.NameVariables.generateNameForVariable(NameVariables.java:252)
        //     at com.strobel.decompiler.languages.java.ast.NameVariables.assignNamesToVariables(NameVariables.java:185)
        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.nameVariables(AstMethodBodyBuilder.java:1482)
        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.populateVariables(AstMethodBodyBuilder.java:1411)
        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:210)
        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:93)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createMethodBody(AstBuilder.java:868)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createMethod(AstBuilder.java:761)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addTypeMembers(AstBuilder.java:638)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeCore(AstBuilder.java:605)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeNoCache(AstBuilder.java:195)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createType(AstBuilder.java:162)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addType(AstBuilder.java:137)
        //     at com.strobel.decompiler.languages.java.JavaLanguage.buildAst(JavaLanguage.java:71)
        //     at com.strobel.decompiler.languages.java.JavaLanguage.decompileType(JavaLanguage.java:59)
        //     at com.strobel.decompiler.DecompilerDriver.decompileType(DecompilerDriver.java:333)
        //     at com.strobel.decompiler.DecompilerDriver.decompileJar(DecompilerDriver.java:254)
        //     at com.strobel.decompiler.DecompilerDriver.main(DecompilerDriver.java:144)
        // 
        throw new IllegalStateException("An error occurred while decompiling this method.");
    }
    
    @Override
    public Appender parseAppender(final String name, final String appenderPrefix, final String layoutPrefix, final String filterPrefix, final Properties props, final PropertiesConfiguration configuration) {
        final Layout layout2 = configuration.parseLayout(layoutPrefix, name, props);
        final Filter filter2 = configuration.parseAppenderFilters(props, filterPrefix, name);
        final String fileName2 = this.getProperty("File");
        final String level2 = this.getProperty("Threshold");
        final boolean append2 = this.getBooleanProperty("Append", true);
        final boolean immediateFlush2 = this.getBooleanProperty("ImmediateFlush", true);
        final boolean bufferedIo2 = this.getBooleanProperty("BufferedIO", false);
        final int bufferSize2 = this.getIntegerProperty("BufferSize", 8192);
        final String maxSize2 = this.getProperty("MaxFileSize", "10 MB");
        final String maxBackups2 = this.getProperty("MaxBackupIndex", "1");
        return this.createAppender(name, configuration, layout2, filter2, append2, bufferedIo2, bufferSize2, immediateFlush2, fileName2, level2, maxSize2, maxBackups2);
    }
    
    private Appender createAppender(final String name, final Log4j1Configuration config, final Layout layout, final Filter filter, final boolean append, final boolean bufferedIo, final int bufferSize, boolean immediateFlush, final String fileName, final String level, final String maxSize, final String maxBackups) {
        final org.apache.logging.log4j.core.Layout<?> fileLayout = LayoutAdapter.adapt(layout);
        if (!bufferedIo) {
            immediateFlush = false;
        }
        final org.apache.logging.log4j.core.Filter fileFilter = AbstractBuilder.buildFilters(level, filter);
        if (fileName == null) {
            RollingFileAppenderBuilder.LOGGER.error("Unable to create RollingFileAppender, no file name provided");
            return null;
        }
        final String filePattern = fileName + ".%i";
        final SizeBasedTriggeringPolicy sizePolicy = SizeBasedTriggeringPolicy.createPolicy(maxSize);
        final CompositeTriggeringPolicy policy = CompositeTriggeringPolicy.createPolicy(sizePolicy);
        final RolloverStrategy strategy = DefaultRolloverStrategy.newBuilder().withConfig(config).withMax(maxBackups).build();
        return AppenderWrapper.adapt(RollingFileAppender.newBuilder().setName(name).setConfiguration(config).setLayout((org.apache.logging.log4j.core.Layout<? extends Serializable>)fileLayout).setFilter(fileFilter).withAppend(append).setBufferedIo(bufferedIo).setBufferSize(bufferSize).setImmediateFlush(immediateFlush).withFileName(fileName).withFilePattern(filePattern).withPolicy(policy).withStrategy(strategy).build());
    }
    
    static {
        LOGGER = StatusLogger.getLogger();
    }
}
