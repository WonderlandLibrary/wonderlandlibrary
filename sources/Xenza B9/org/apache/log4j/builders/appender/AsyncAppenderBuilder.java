// 
// Decompiled by Procyon v0.6.0
// 

package org.apache.log4j.builders.appender;

import org.apache.logging.log4j.status.StatusLogger;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import org.apache.log4j.bridge.AppenderWrapper;
import org.apache.logging.log4j.core.config.Configuration;
import org.apache.log4j.bridge.FilterAdapter;
import org.apache.logging.log4j.core.appender.AsyncAppender;
import org.apache.logging.log4j.core.config.AppenderRef;
import org.apache.log4j.helpers.OptionConverter;
import org.apache.logging.log4j.Level;
import org.apache.log4j.config.Log4j1Configuration;
import org.apache.log4j.spi.Filter;
import org.apache.log4j.config.PropertiesConfiguration;
import org.apache.log4j.Appender;
import org.apache.log4j.xml.XmlConfiguration;
import org.w3c.dom.Element;
import java.util.Properties;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.log4j.builders.AbstractBuilder;

@Plugin(name = "org.apache.log4j.AsyncAppender", category = "Log4j Builder")
public class AsyncAppenderBuilder extends AbstractBuilder implements AppenderBuilder
{
    private static final Logger LOGGER;
    private static final String BLOCKING_PARAM = "Blocking";
    private static final String INCLUDE_LOCATION_PARAM = "IncludeLocation";
    
    public AsyncAppenderBuilder() {
    }
    
    public AsyncAppenderBuilder(final String prefix, final Properties props) {
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
        //     2: invokevirtual   org/apache/log4j/builders/appender/AsyncAppenderBuilder.getNameAttribute:(Lorg/w3c/dom/Element;)Ljava/lang/String;
        //     5: astore_3        /* name */
        //     6: new             Ljava/util/concurrent/atomic/AtomicReference;
        //     9: dup            
        //    10: new             Ljava/util/ArrayList;
        //    13: dup            
        //    14: invokespecial   java/util/ArrayList.<init>:()V
        //    17: invokespecial   java/util/concurrent/atomic/AtomicReference.<init>:(Ljava/lang/Object;)V
        //    20: astore          appenderRefs
        //    22: new             Ljava/util/concurrent/atomic/AtomicBoolean;
        //    25: dup            
        //    26: invokespecial   java/util/concurrent/atomic/AtomicBoolean.<init>:()V
        //    29: astore          blocking
        //    31: new             Ljava/util/concurrent/atomic/AtomicBoolean;
        //    34: dup            
        //    35: invokespecial   java/util/concurrent/atomic/AtomicBoolean.<init>:()V
        //    38: astore          includeLocation
        //    40: new             Ljava/util/concurrent/atomic/AtomicReference;
        //    43: dup            
        //    44: ldc             "trace"
        //    46: invokespecial   java/util/concurrent/atomic/AtomicReference.<init>:(Ljava/lang/Object;)V
        //    49: astore          level
        //    51: new             Ljava/util/concurrent/atomic/AtomicInteger;
        //    54: dup            
        //    55: sipush          1024
        //    58: invokespecial   java/util/concurrent/atomic/AtomicInteger.<init>:(I)V
        //    61: astore          bufferSize
        //    63: new             Ljava/util/concurrent/atomic/AtomicReference;
        //    66: dup            
        //    67: invokespecial   java/util/concurrent/atomic/AtomicReference.<init>:()V
        //    70: astore          filter
        //    72: aload_1         /* appenderElement */
        //    73: invokeinterface org/w3c/dom/Element.getChildNodes:()Lorg/w3c/dom/NodeList;
        //    78: aload_0         /* this */
        //    79: aload_2         /* config */
        //    80: aload           appenderRefs
        //    82: aload           filter
        //    84: aload           bufferSize
        //    86: aload           blocking
        //    88: aload           includeLocation
        //    90: aload           level
        //    92: invokedynamic   BootstrapMethod #0, accept:(Lorg/apache/log4j/builders/appender/AsyncAppenderBuilder;Lorg/apache/log4j/xml/XmlConfiguration;Ljava/util/concurrent/atomic/AtomicReference;Ljava/util/concurrent/atomic/AtomicReference;Ljava/util/concurrent/atomic/AtomicInteger;Ljava/util/concurrent/atomic/AtomicBoolean;Ljava/util/concurrent/atomic/AtomicBoolean;Ljava/util/concurrent/atomic/AtomicReference;)Ljava/util/function/Consumer;
        //    97: invokestatic    org/apache/log4j/xml/XmlConfiguration.forEachElement:(Lorg/w3c/dom/NodeList;Ljava/util/function/Consumer;)V
        //   100: aload_0         /* this */
        //   101: aload_3         /* name */
        //   102: aload           level
        //   104: invokevirtual   java/util/concurrent/atomic/AtomicReference.get:()Ljava/lang/Object;
        //   107: checkcast       Ljava/lang/String;
        //   110: aload           appenderRefs
        //   112: invokevirtual   java/util/concurrent/atomic/AtomicReference.get:()Ljava/lang/Object;
        //   115: checkcast       Ljava/util/List;
        //   118: getstatic       org/apache/logging/log4j/util/Strings.EMPTY_ARRAY:[Ljava/lang/String;
        //   121: invokeinterface java/util/List.toArray:([Ljava/lang/Object;)[Ljava/lang/Object;
        //   126: checkcast       [Ljava/lang/String;
        //   129: aload           blocking
        //   131: invokevirtual   java/util/concurrent/atomic/AtomicBoolean.get:()Z
        //   134: aload           bufferSize
        //   136: invokevirtual   java/util/concurrent/atomic/AtomicInteger.get:()I
        //   139: aload           includeLocation
        //   141: invokevirtual   java/util/concurrent/atomic/AtomicBoolean.get:()Z
        //   144: aload           filter
        //   146: invokevirtual   java/util/concurrent/atomic/AtomicReference.get:()Ljava/lang/Object;
        //   149: checkcast       Lorg/apache/log4j/spi/Filter;
        //   152: aload_2         /* config */
        //   153: invokespecial   org/apache/log4j/builders/appender/AsyncAppenderBuilder.createAppender:(Ljava/lang/String;Ljava/lang/String;[Ljava/lang/String;ZIZLorg/apache/log4j/spi/Filter;Lorg/apache/log4j/config/Log4j1Configuration;)Lorg/apache/log4j/Appender;
        //   156: areturn        
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
        final String appenderRef = this.getProperty("appender-ref");
        final Filter filter2 = configuration.parseAppenderFilters(props, filterPrefix, name);
        final boolean blocking2 = this.getBooleanProperty("Blocking");
        final boolean includeLocation2 = this.getBooleanProperty("IncludeLocation");
        final String level2 = this.getProperty("Threshold");
        final int bufferSize2 = this.getIntegerProperty("BufferSize", 1024);
        if (appenderRef == null) {
            AsyncAppenderBuilder.LOGGER.error("No appender references configured for AsyncAppender {}", name);
            return null;
        }
        final Appender appender = configuration.parseAppender(props, appenderRef);
        if (appender == null) {
            AsyncAppenderBuilder.LOGGER.error("Cannot locate Appender {}", appenderRef);
            return null;
        }
        return this.createAppender(name, level2, new String[] { appenderRef }, blocking2, bufferSize2, includeLocation2, filter2, configuration);
    }
    
    private <T extends Log4j1Configuration> Appender createAppender(final String name, final String level, final String[] appenderRefs, final boolean blocking, final int bufferSize, final boolean includeLocation, final Filter filter, final T configuration) {
        if (appenderRefs.length == 0) {
            AsyncAppenderBuilder.LOGGER.error("No appender references configured for AsyncAppender {}", name);
            return null;
        }
        final Level logLevel = OptionConverter.convertLevel(level, Level.TRACE);
        final AppenderRef[] refs = new AppenderRef[appenderRefs.length];
        int index = 0;
        for (final String appenderRef : appenderRefs) {
            refs[index++] = AppenderRef.createAppenderRef(appenderRef, logLevel, null);
        }
        final AsyncAppender.Builder builder = AsyncAppender.newBuilder();
        builder.setFilter(FilterAdapter.adapt(filter));
        return AppenderWrapper.adapt(builder.setName(name).setAppenderRefs(refs).setBlocking(blocking).setBufferSize(bufferSize).setIncludeLocation(includeLocation).setConfiguration(configuration).build());
    }
    
    static {
        LOGGER = StatusLogger.getLogger();
    }
}
