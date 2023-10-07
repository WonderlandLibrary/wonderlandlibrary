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
import org.apache.logging.log4j.core.appender.RollingFileAppender;
import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.core.appender.rolling.DefaultRolloverStrategy;
import org.apache.logging.log4j.core.appender.rolling.CompositeTriggeringPolicy;
import org.apache.logging.log4j.core.appender.rolling.TriggeringPolicy;
import org.apache.logging.log4j.core.appender.rolling.TimeBasedTriggeringPolicy;
import org.apache.log4j.bridge.LayoutAdapter;
import org.apache.log4j.config.Log4j1Configuration;
import org.apache.log4j.spi.Filter;
import org.apache.log4j.Layout;
import org.apache.log4j.config.PropertiesConfiguration;
import org.apache.log4j.Appender;
import org.apache.log4j.xml.XmlConfiguration;
import org.w3c.dom.Element;
import java.util.Properties;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.log4j.builders.AbstractBuilder;

@Plugin(name = "org.apache.log4j.DailyRollingFileAppender", category = "Log4j Builder")
public class DailyRollingFileAppenderBuilder extends AbstractBuilder implements AppenderBuilder
{
    private static final String DEFAULT_DATE_PATTERN = ".yyyy-MM-dd";
    private static final String DATE_PATTERN_PARAM = "DatePattern";
    private static final Logger LOGGER;
    
    public DailyRollingFileAppenderBuilder() {
    }
    
    public DailyRollingFileAppenderBuilder(final String prefix, final Properties props) {
        super(prefix, props);
    }
    
    @Override
    public Appender parseAppender(final Element appenderElement, final XmlConfiguration config) {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     1: aload_1         /* appenderElement */
        //     2: invokevirtual   org/apache/log4j/builders/appender/DailyRollingFileAppenderBuilder.getNameAttribute:(Lorg/w3c/dom/Element;)Ljava/lang/String;
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
        //    33: new             Ljava/util/concurrent/atomic/AtomicReference;
        //    36: dup            
        //    37: invokespecial   java/util/concurrent/atomic/AtomicReference.<init>:()V
        //    40: astore          level
        //    42: new             Ljava/util/concurrent/atomic/AtomicBoolean;
        //    45: dup            
        //    46: iconst_1       
        //    47: invokespecial   java/util/concurrent/atomic/AtomicBoolean.<init>:(Z)V
        //    50: astore          immediateFlush
        //    52: new             Ljava/util/concurrent/atomic/AtomicBoolean;
        //    55: dup            
        //    56: iconst_1       
        //    57: invokespecial   java/util/concurrent/atomic/AtomicBoolean.<init>:(Z)V
        //    60: astore          append
        //    62: new             Ljava/util/concurrent/atomic/AtomicBoolean;
        //    65: dup            
        //    66: invokespecial   java/util/concurrent/atomic/AtomicBoolean.<init>:()V
        //    69: astore          bufferedIo
        //    71: new             Ljava/util/concurrent/atomic/AtomicInteger;
        //    74: dup            
        //    75: sipush          8192
        //    78: invokespecial   java/util/concurrent/atomic/AtomicInteger.<init>:(I)V
        //    81: astore          bufferSize
        //    83: new             Ljava/util/concurrent/atomic/AtomicReference;
        //    86: dup            
        //    87: ldc             ".yyyy-MM-dd"
        //    89: invokespecial   java/util/concurrent/atomic/AtomicReference.<init>:(Ljava/lang/Object;)V
        //    92: astore          datePattern
        //    94: aload_1         /* appenderElement */
        //    95: invokeinterface org/w3c/dom/Element.getChildNodes:()Lorg/w3c/dom/NodeList;
        //   100: aload_0         /* this */
        //   101: aload           layout
        //   103: aload_2         /* config */
        //   104: aload           filter
        //   106: aload           fileName
        //   108: aload           append
        //   110: aload           bufferedIo
        //   112: aload           bufferSize
        //   114: aload           level
        //   116: aload           datePattern
        //   118: aload           immediateFlush
        //   120: invokedynamic   BootstrapMethod #0, accept:(Lorg/apache/log4j/builders/appender/DailyRollingFileAppenderBuilder;Ljava/util/concurrent/atomic/AtomicReference;Lorg/apache/log4j/xml/XmlConfiguration;Ljava/util/concurrent/atomic/AtomicReference;Ljava/util/concurrent/atomic/AtomicReference;Ljava/util/concurrent/atomic/AtomicBoolean;Ljava/util/concurrent/atomic/AtomicBoolean;Ljava/util/concurrent/atomic/AtomicInteger;Ljava/util/concurrent/atomic/AtomicReference;Ljava/util/concurrent/atomic/AtomicReference;Ljava/util/concurrent/atomic/AtomicBoolean;)Ljava/util/function/Consumer;
        //   125: invokestatic    org/apache/log4j/xml/XmlConfiguration.forEachElement:(Lorg/w3c/dom/NodeList;Ljava/util/function/Consumer;)V
        //   128: aload_0         /* this */
        //   129: aload_3         /* name */
        //   130: aload           layout
        //   132: invokevirtual   java/util/concurrent/atomic/AtomicReference.get:()Ljava/lang/Object;
        //   135: checkcast       Lorg/apache/log4j/Layout;
        //   138: aload           filter
        //   140: invokevirtual   java/util/concurrent/atomic/AtomicReference.get:()Ljava/lang/Object;
        //   143: checkcast       Lorg/apache/log4j/spi/Filter;
        //   146: aload           fileName
        //   148: invokevirtual   java/util/concurrent/atomic/AtomicReference.get:()Ljava/lang/Object;
        //   151: checkcast       Ljava/lang/String;
        //   154: aload           append
        //   156: invokevirtual   java/util/concurrent/atomic/AtomicBoolean.get:()Z
        //   159: aload           immediateFlush
        //   161: invokevirtual   java/util/concurrent/atomic/AtomicBoolean.get:()Z
        //   164: aload           level
        //   166: invokevirtual   java/util/concurrent/atomic/AtomicReference.get:()Ljava/lang/Object;
        //   169: checkcast       Ljava/lang/String;
        //   172: aload           bufferedIo
        //   174: invokevirtual   java/util/concurrent/atomic/AtomicBoolean.get:()Z
        //   177: aload           bufferSize
        //   179: invokevirtual   java/util/concurrent/atomic/AtomicInteger.get:()I
        //   182: aload           datePattern
        //   184: invokevirtual   java/util/concurrent/atomic/AtomicReference.get:()Ljava/lang/Object;
        //   187: checkcast       Ljava/lang/String;
        //   190: aload_2         /* config */
        //   191: invokespecial   org/apache/log4j/builders/appender/DailyRollingFileAppenderBuilder.createAppender:(Ljava/lang/String;Lorg/apache/log4j/Layout;Lorg/apache/log4j/spi/Filter;Ljava/lang/String;ZZLjava/lang/String;ZILjava/lang/String;Lorg/apache/log4j/config/Log4j1Configuration;)Lorg/apache/log4j/Appender;
        //   194: areturn        
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
        final String datePattern2 = this.getProperty("DatePattern", ".yyyy-MM-dd");
        return this.createAppender(name, layout2, filter2, fileName2, append2, immediateFlush2, level2, bufferedIo2, bufferSize2, datePattern2, configuration);
    }
    
    private <T extends Log4j1Configuration> Appender createAppender(final String name, final Layout layout, final Filter filter, final String fileName, final boolean append, boolean immediateFlush, final String level, final boolean bufferedIo, final int bufferSize, final String datePattern, final T configuration) {
        final org.apache.logging.log4j.core.Layout<?> fileLayout = LayoutAdapter.adapt(layout);
        if (bufferedIo) {
            immediateFlush = false;
        }
        final org.apache.logging.log4j.core.Filter fileFilter = AbstractBuilder.buildFilters(level, filter);
        if (fileName == null) {
            DailyRollingFileAppenderBuilder.LOGGER.error("Unable to create DailyRollingFileAppender, no file name provided");
            return null;
        }
        final String filePattern = fileName + "%d{" + datePattern + "}";
        final TriggeringPolicy timePolicy = TimeBasedTriggeringPolicy.newBuilder().withModulate(true).build();
        final TriggeringPolicy policy = CompositeTriggeringPolicy.createPolicy(timePolicy);
        final RolloverStrategy strategy = DefaultRolloverStrategy.newBuilder().withConfig(configuration).withMax(Integer.toString(Integer.MAX_VALUE)).build();
        return AppenderWrapper.adapt(RollingFileAppender.newBuilder().setName(name).setConfiguration(configuration).setLayout((org.apache.logging.log4j.core.Layout<? extends Serializable>)fileLayout).setFilter(fileFilter).withFileName(fileName).withAppend(append).setBufferedIo(bufferedIo).setBufferSize(bufferSize).setImmediateFlush(immediateFlush).withFilePattern(filePattern).withPolicy(policy).withStrategy(strategy).build());
    }
    
    static {
        LOGGER = StatusLogger.getLogger();
    }
}
