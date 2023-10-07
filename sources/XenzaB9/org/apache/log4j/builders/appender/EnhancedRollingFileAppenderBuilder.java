// 
// Decompiled by Procyon v0.6.0
// 

package org.apache.log4j.builders.appender;

import org.apache.logging.log4j.status.StatusLogger;
import java.util.concurrent.atomic.AtomicBoolean;
import org.apache.log4j.bridge.AppenderWrapper;
import org.apache.logging.log4j.core.appender.rolling.RolloverStrategy;
import java.io.Serializable;
import org.apache.logging.log4j.core.appender.AbstractOutputStreamAppender;
import org.apache.logging.log4j.core.filter.AbstractFilterable;
import org.apache.logging.log4j.core.appender.RollingFileAppender;
import org.apache.logging.log4j.core.appender.rolling.TimeBasedTriggeringPolicy;
import org.apache.logging.log4j.core.appender.rolling.DefaultRolloverStrategy;
import org.apache.log4j.bridge.LayoutAdapter;
import org.apache.logging.log4j.core.appender.rolling.TriggeringPolicy;
import org.apache.log4j.spi.Filter;
import org.apache.log4j.Layout;
import org.apache.logging.log4j.core.config.Configuration;
import org.apache.log4j.config.PropertiesConfiguration;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import org.apache.log4j.xml.XmlConfiguration;
import org.w3c.dom.Element;
import java.util.Properties;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.log4j.Appender;
import org.apache.log4j.builders.AbstractBuilder;

@Plugin(name = "org.apache.log4j.rolling.RollingFileAppender", category = "Log4j Builder")
public class EnhancedRollingFileAppenderBuilder extends AbstractBuilder<Appender> implements AppenderBuilder<Appender>
{
    private static final String TIME_BASED_ROLLING_POLICY = "org.apache.log4j.rolling.TimeBasedRollingPolicy";
    private static final String FIXED_WINDOW_ROLLING_POLICY = "org.apache.log4j.rolling.FixedWindowRollingPolicy";
    private static final Logger LOGGER;
    private static final String TRIGGERING_TAG = "triggeringPolicy";
    private static final String ROLLING_TAG = "rollingPolicy";
    private static final int DEFAULT_MIN_INDEX = 1;
    private static final int DEFAULT_MAX_INDEX = 7;
    private static final String ACTIVE_FILE_PARAM = "ActiveFileName";
    private static final String FILE_PATTERN_PARAM = "FileNamePattern";
    private static final String MIN_INDEX_PARAM = "MinIndex";
    private static final String MAX_INDEX_PARAM = "MaxIndex";
    
    public EnhancedRollingFileAppenderBuilder() {
    }
    
    public EnhancedRollingFileAppenderBuilder(final String prefix, final Properties properties) {
        super(prefix, properties);
    }
    
    private void parseRollingPolicy(final Element element, final XmlConfiguration configuration, final AtomicReference<String> rollingPolicyClassName, final AtomicReference<String> activeFileName, final AtomicReference<String> fileNamePattern, final AtomicInteger minIndex, final AtomicInteger maxIndex) {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     1: aload_2         /* configuration */
        //     2: aload_1         /* element */
        //     3: ldc             "class"
        //     5: invokeinterface org/w3c/dom/Element.getAttribute:(Ljava/lang/String;)Ljava/lang/String;
        //    10: aload_0         /* this */
        //    11: invokevirtual   org/apache/log4j/builders/appender/EnhancedRollingFileAppenderBuilder.getProperties:()Ljava/util/Properties;
        //    14: invokevirtual   org/apache/log4j/xml/XmlConfiguration.subst:(Ljava/lang/String;Ljava/util/Properties;)Ljava/lang/String;
        //    17: invokevirtual   java/util/concurrent/atomic/AtomicReference.set:(Ljava/lang/Object;)V
        //    20: aload_1         /* element */
        //    21: invokeinterface org/w3c/dom/Element.getChildNodes:()Lorg/w3c/dom/NodeList;
        //    26: aload_0         /* this */
        //    27: aload           activeFileName
        //    29: aload           fileNamePattern
        //    31: aload           minIndex
        //    33: aload           maxIndex
        //    35: invokedynamic   BootstrapMethod #0, accept:(Lorg/apache/log4j/builders/appender/EnhancedRollingFileAppenderBuilder;Ljava/util/concurrent/atomic/AtomicReference;Ljava/util/concurrent/atomic/AtomicReference;Ljava/util/concurrent/atomic/AtomicInteger;Ljava/util/concurrent/atomic/AtomicInteger;)Ljava/util/function/Consumer;
        //    40: invokestatic    org/apache/log4j/xml/XmlConfiguration.forEachElement:(Lorg/w3c/dom/NodeList;Ljava/util/function/Consumer;)V
        //    43: return         
        //    MethodParameters:
        //  Name                    Flags  
        //  ----------------------  -----
        //  element                 
        //  configuration           
        //  rollingPolicyClassName  
        //  activeFileName          
        //  fileNamePattern         
        //  minIndex                
        //  maxIndex                
        //    Signature:
        //  (Lorg/w3c/dom/Element;Lorg/apache/log4j/xml/XmlConfiguration;Ljava/util/concurrent/atomic/AtomicReference<Ljava/lang/String;>;Ljava/util/concurrent/atomic/AtomicReference<Ljava/lang/String;>;Ljava/util/concurrent/atomic/AtomicReference<Ljava/lang/String;>;Ljava/util/concurrent/atomic/AtomicInteger;Ljava/util/concurrent/atomic/AtomicInteger;)V
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
    public Appender parseAppender(final Element element, final XmlConfiguration configuration) {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     1: aload_1         /* element */
        //     2: invokevirtual   org/apache/log4j/builders/appender/EnhancedRollingFileAppenderBuilder.getNameAttribute:(Lorg/w3c/dom/Element;)Ljava/lang/String;
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
        //    87: invokespecial   java/util/concurrent/atomic/AtomicReference.<init>:()V
        //    90: astore          rollingPolicyClassName
        //    92: new             Ljava/util/concurrent/atomic/AtomicReference;
        //    95: dup            
        //    96: invokespecial   java/util/concurrent/atomic/AtomicReference.<init>:()V
        //    99: astore          activeFileName
        //   101: new             Ljava/util/concurrent/atomic/AtomicReference;
        //   104: dup            
        //   105: invokespecial   java/util/concurrent/atomic/AtomicReference.<init>:()V
        //   108: astore          fileNamePattern
        //   110: new             Ljava/util/concurrent/atomic/AtomicInteger;
        //   113: dup            
        //   114: iconst_1       
        //   115: invokespecial   java/util/concurrent/atomic/AtomicInteger.<init>:(I)V
        //   118: astore          minIndex
        //   120: new             Ljava/util/concurrent/atomic/AtomicInteger;
        //   123: dup            
        //   124: bipush          7
        //   126: invokespecial   java/util/concurrent/atomic/AtomicInteger.<init>:(I)V
        //   129: astore          maxIndex
        //   131: new             Ljava/util/concurrent/atomic/AtomicReference;
        //   134: dup            
        //   135: invokespecial   java/util/concurrent/atomic/AtomicReference.<init>:()V
        //   138: astore          triggeringPolicy
        //   140: aload_1         /* element */
        //   141: invokeinterface org/w3c/dom/Element.getChildNodes:()Lorg/w3c/dom/NodeList;
        //   146: aload_0         /* this */
        //   147: aload_2         /* configuration */
        //   148: aload           rollingPolicyClassName
        //   150: aload           activeFileName
        //   152: aload           fileNamePattern
        //   154: aload           minIndex
        //   156: aload           maxIndex
        //   158: aload           triggeringPolicy
        //   160: aload           layout
        //   162: aload           filter
        //   164: aload           fileName
        //   166: aload           append
        //   168: aload           bufferedIo
        //   170: aload           bufferSize
        //   172: aload           level
        //   174: aload           immediateFlush
        //   176: invokedynamic   BootstrapMethod #1, accept:(Lorg/apache/log4j/builders/appender/EnhancedRollingFileAppenderBuilder;Lorg/apache/log4j/xml/XmlConfiguration;Ljava/util/concurrent/atomic/AtomicReference;Ljava/util/concurrent/atomic/AtomicReference;Ljava/util/concurrent/atomic/AtomicReference;Ljava/util/concurrent/atomic/AtomicInteger;Ljava/util/concurrent/atomic/AtomicInteger;Ljava/util/concurrent/atomic/AtomicReference;Ljava/util/concurrent/atomic/AtomicReference;Ljava/util/concurrent/atomic/AtomicReference;Ljava/util/concurrent/atomic/AtomicReference;Ljava/util/concurrent/atomic/AtomicBoolean;Ljava/util/concurrent/atomic/AtomicBoolean;Ljava/util/concurrent/atomic/AtomicInteger;Ljava/util/concurrent/atomic/AtomicReference;Ljava/util/concurrent/atomic/AtomicBoolean;)Ljava/util/function/Consumer;
        //   181: invokestatic    org/apache/log4j/xml/XmlConfiguration.forEachElement:(Lorg/w3c/dom/NodeList;Ljava/util/function/Consumer;)V
        //   184: aload_0         /* this */
        //   185: aload_3         /* name */
        //   186: aload           layout
        //   188: invokevirtual   java/util/concurrent/atomic/AtomicReference.get:()Ljava/lang/Object;
        //   191: checkcast       Lorg/apache/log4j/Layout;
        //   194: aload           filter
        //   196: invokevirtual   java/util/concurrent/atomic/AtomicReference.get:()Ljava/lang/Object;
        //   199: checkcast       Lorg/apache/log4j/spi/Filter;
        //   202: aload           fileName
        //   204: invokevirtual   java/util/concurrent/atomic/AtomicReference.get:()Ljava/lang/Object;
        //   207: checkcast       Ljava/lang/String;
        //   210: aload           level
        //   212: invokevirtual   java/util/concurrent/atomic/AtomicReference.get:()Ljava/lang/Object;
        //   215: checkcast       Ljava/lang/String;
        //   218: aload           immediateFlush
        //   220: invokevirtual   java/util/concurrent/atomic/AtomicBoolean.get:()Z
        //   223: aload           append
        //   225: invokevirtual   java/util/concurrent/atomic/AtomicBoolean.get:()Z
        //   228: aload           bufferedIo
        //   230: invokevirtual   java/util/concurrent/atomic/AtomicBoolean.get:()Z
        //   233: aload           bufferSize
        //   235: invokevirtual   java/util/concurrent/atomic/AtomicInteger.get:()I
        //   238: aload           rollingPolicyClassName
        //   240: invokevirtual   java/util/concurrent/atomic/AtomicReference.get:()Ljava/lang/Object;
        //   243: checkcast       Ljava/lang/String;
        //   246: aload           activeFileName
        //   248: invokevirtual   java/util/concurrent/atomic/AtomicReference.get:()Ljava/lang/Object;
        //   251: checkcast       Ljava/lang/String;
        //   254: aload           fileNamePattern
        //   256: invokevirtual   java/util/concurrent/atomic/AtomicReference.get:()Ljava/lang/Object;
        //   259: checkcast       Ljava/lang/String;
        //   262: aload           minIndex
        //   264: invokevirtual   java/util/concurrent/atomic/AtomicInteger.get:()I
        //   267: aload           maxIndex
        //   269: invokevirtual   java/util/concurrent/atomic/AtomicInteger.get:()I
        //   272: aload           triggeringPolicy
        //   274: invokevirtual   java/util/concurrent/atomic/AtomicReference.get:()Ljava/lang/Object;
        //   277: checkcast       Lorg/apache/logging/log4j/core/appender/rolling/TriggeringPolicy;
        //   280: aload_2         /* configuration */
        //   281: invokespecial   org/apache/log4j/builders/appender/EnhancedRollingFileAppenderBuilder.createAppender:(Ljava/lang/String;Lorg/apache/log4j/Layout;Lorg/apache/log4j/spi/Filter;Ljava/lang/String;Ljava/lang/String;ZZZILjava/lang/String;Ljava/lang/String;Ljava/lang/String;IILorg/apache/logging/log4j/core/appender/rolling/TriggeringPolicy;Lorg/apache/logging/log4j/core/config/Configuration;)Lorg/apache/log4j/Appender;
        //   284: areturn        
        //    MethodParameters:
        //  Name           Flags  
        //  -------------  -----
        //  element        
        //  configuration  
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
        final String level2 = this.getProperty("Threshold");
        final String fileName2 = this.getProperty("File");
        final boolean append2 = this.getBooleanProperty("Append", true);
        final boolean immediateFlush2 = this.getBooleanProperty("ImmediateFlush", true);
        final boolean bufferedIo2 = this.getBooleanProperty("BufferedIO", false);
        final int bufferSize2 = Integer.parseInt(this.getProperty("BufferSize", "8192"));
        final String rollingPolicyClassName3 = this.getProperty("rollingPolicy");
        final int minIndex3 = this.getIntegerProperty("rollingPolicy.MinIndex", 1);
        final int maxIndex3 = this.getIntegerProperty("rollingPolicy.MaxIndex", 7);
        final String activeFileName3 = this.getProperty("rollingPolicy.ActiveFileName");
        final String fileNamePattern3 = this.getProperty("rollingPolicy.FileNamePattern");
        final TriggeringPolicy triggeringPolicy2 = configuration.parseTriggeringPolicy(props, appenderPrefix + "." + "triggeringPolicy");
        return this.createAppender(name, layout2, filter2, fileName2, level2, immediateFlush2, append2, bufferedIo2, bufferSize2, rollingPolicyClassName3, activeFileName3, fileNamePattern3, minIndex3, maxIndex3, triggeringPolicy2, configuration);
    }
    
    private Appender createAppender(final String name, final Layout layout, final Filter filter, final String fileName, final String level, final boolean immediateFlush, final boolean append, final boolean bufferedIo, final int bufferSize, final String rollingPolicyClassName, final String activeFileName, final String fileNamePattern, final int minIndex, final int maxIndex, final TriggeringPolicy triggeringPolicy, final Configuration configuration) {
        final org.apache.logging.log4j.core.Layout<?> fileLayout = LayoutAdapter.adapt(layout);
        final boolean actualImmediateFlush = !bufferedIo && immediateFlush;
        final org.apache.logging.log4j.core.Filter fileFilter = AbstractBuilder.buildFilters(level, filter);
        if (rollingPolicyClassName == null) {
            EnhancedRollingFileAppenderBuilder.LOGGER.error("Unable to create RollingFileAppender, no rolling policy provided.");
            return null;
        }
        final String actualFileName = (activeFileName != null) ? activeFileName : fileName;
        if (actualFileName == null) {
            EnhancedRollingFileAppenderBuilder.LOGGER.error("Unable to create RollingFileAppender, no file name provided.");
            return null;
        }
        if (fileNamePattern == null) {
            EnhancedRollingFileAppenderBuilder.LOGGER.error("Unable to create RollingFileAppender, no file name pattern provided.");
            return null;
        }
        final DefaultRolloverStrategy.Builder rolloverStrategyBuilder = DefaultRolloverStrategy.newBuilder();
        switch (rollingPolicyClassName) {
            case "org.apache.log4j.rolling.FixedWindowRollingPolicy": {
                rolloverStrategyBuilder.withMin(Integer.toString(minIndex)).withMax(Integer.toString(maxIndex));
                break;
            }
            case "org.apache.log4j.rolling.TimeBasedRollingPolicy": {
                break;
            }
            default: {
                EnhancedRollingFileAppenderBuilder.LOGGER.warn("Unsupported rolling policy: {}", rollingPolicyClassName);
                break;
            }
        }
        TriggeringPolicy actualTriggeringPolicy;
        if (triggeringPolicy != null) {
            actualTriggeringPolicy = triggeringPolicy;
        }
        else {
            if (!rollingPolicyClassName.equals("org.apache.log4j.rolling.TimeBasedRollingPolicy")) {
                EnhancedRollingFileAppenderBuilder.LOGGER.error("Unable to create RollingFileAppender, no triggering policy provided.");
                return null;
            }
            actualTriggeringPolicy = TimeBasedTriggeringPolicy.newBuilder().build();
        }
        return AppenderWrapper.adapt(RollingFileAppender.newBuilder().withAppend(append).setBufferedIo(bufferedIo).setBufferSize(bufferedIo ? bufferSize : 0).setConfiguration(configuration).withFileName(actualFileName).withFilePattern(fileNamePattern).setFilter(fileFilter).setImmediateFlush(actualImmediateFlush).setLayout((org.apache.logging.log4j.core.Layout<? extends Serializable>)fileLayout).setName(name).withPolicy(actualTriggeringPolicy).withStrategy(rolloverStrategyBuilder.build()).build());
    }
    
    static {
        LOGGER = StatusLogger.getLogger();
    }
}
