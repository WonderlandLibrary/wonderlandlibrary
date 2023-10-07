// 
// Decompiled by Procyon v0.6.0
// 

package org.apache.log4j.builders.appender;

import org.apache.logging.log4j.status.StatusLogger;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;
import org.apache.log4j.bridge.AppenderWrapper;
import org.apache.logging.log4j.core.config.Configuration;
import java.io.Serializable;
import org.apache.logging.log4j.core.appender.ConsoleAppender;
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

@Plugin(name = "org.apache.log4j.ConsoleAppender", category = "Log4j Builder")
public class ConsoleAppenderBuilder extends AbstractBuilder implements AppenderBuilder
{
    private static final String SYSTEM_OUT = "System.out";
    private static final String SYSTEM_ERR = "System.err";
    private static final String TARGET_PARAM = "Target";
    private static final String FOLLOW_PARAM = "Follow";
    private static final Logger LOGGER;
    
    public ConsoleAppenderBuilder() {
    }
    
    public ConsoleAppenderBuilder(final String prefix, final Properties props) {
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
        //     2: invokevirtual   org/apache/log4j/builders/appender/ConsoleAppenderBuilder.getNameAttribute:(Lorg/w3c/dom/Element;)Ljava/lang/String;
        //     5: astore_3        /* name */
        //     6: new             Ljava/util/concurrent/atomic/AtomicReference;
        //     9: dup            
        //    10: ldc             "System.out"
        //    12: invokespecial   java/util/concurrent/atomic/AtomicReference.<init>:(Ljava/lang/Object;)V
        //    15: astore          target
        //    17: new             Ljava/util/concurrent/atomic/AtomicReference;
        //    20: dup            
        //    21: invokespecial   java/util/concurrent/atomic/AtomicReference.<init>:()V
        //    24: astore          layout
        //    26: new             Ljava/util/concurrent/atomic/AtomicReference;
        //    29: dup            
        //    30: invokespecial   java/util/concurrent/atomic/AtomicReference.<init>:()V
        //    33: astore          filter
        //    35: new             Ljava/util/concurrent/atomic/AtomicReference;
        //    38: dup            
        //    39: invokespecial   java/util/concurrent/atomic/AtomicReference.<init>:()V
        //    42: astore          level
        //    44: new             Ljava/util/concurrent/atomic/AtomicBoolean;
        //    47: dup            
        //    48: invokespecial   java/util/concurrent/atomic/AtomicBoolean.<init>:()V
        //    51: astore          follow
        //    53: new             Ljava/util/concurrent/atomic/AtomicBoolean;
        //    56: dup            
        //    57: iconst_1       
        //    58: invokespecial   java/util/concurrent/atomic/AtomicBoolean.<init>:(Z)V
        //    61: astore          immediateFlush
        //    63: aload_1         /* appenderElement */
        //    64: invokeinterface org/w3c/dom/Element.getChildNodes:()Lorg/w3c/dom/NodeList;
        //    69: aload_0         /* this */
        //    70: aload           layout
        //    72: aload_2         /* config */
        //    73: aload           filter
        //    75: aload           target
        //    77: aload           level
        //    79: aload           follow
        //    81: aload           immediateFlush
        //    83: invokedynamic   BootstrapMethod #0, accept:(Lorg/apache/log4j/builders/appender/ConsoleAppenderBuilder;Ljava/util/concurrent/atomic/AtomicReference;Lorg/apache/log4j/xml/XmlConfiguration;Ljava/util/concurrent/atomic/AtomicReference;Ljava/util/concurrent/atomic/AtomicReference;Ljava/util/concurrent/atomic/AtomicReference;Ljava/util/concurrent/atomic/AtomicBoolean;Ljava/util/concurrent/atomic/AtomicBoolean;)Ljava/util/function/Consumer;
        //    88: invokestatic    org/apache/log4j/xml/XmlConfiguration.forEachElement:(Lorg/w3c/dom/NodeList;Ljava/util/function/Consumer;)V
        //    91: aload_0         /* this */
        //    92: aload_3         /* name */
        //    93: aload           layout
        //    95: invokevirtual   java/util/concurrent/atomic/AtomicReference.get:()Ljava/lang/Object;
        //    98: checkcast       Lorg/apache/log4j/Layout;
        //   101: aload           filter
        //   103: invokevirtual   java/util/concurrent/atomic/AtomicReference.get:()Ljava/lang/Object;
        //   106: checkcast       Lorg/apache/log4j/spi/Filter;
        //   109: aload           level
        //   111: invokevirtual   java/util/concurrent/atomic/AtomicReference.get:()Ljava/lang/Object;
        //   114: checkcast       Ljava/lang/String;
        //   117: aload           target
        //   119: invokevirtual   java/util/concurrent/atomic/AtomicReference.get:()Ljava/lang/Object;
        //   122: checkcast       Ljava/lang/String;
        //   125: aload           immediateFlush
        //   127: invokevirtual   java/util/concurrent/atomic/AtomicBoolean.get:()Z
        //   130: aload           follow
        //   132: invokevirtual   java/util/concurrent/atomic/AtomicBoolean.get:()Z
        //   135: aload_2         /* config */
        //   136: invokespecial   org/apache/log4j/builders/appender/ConsoleAppenderBuilder.createAppender:(Ljava/lang/String;Lorg/apache/log4j/Layout;Lorg/apache/log4j/spi/Filter;Ljava/lang/String;Ljava/lang/String;ZZLorg/apache/log4j/config/Log4j1Configuration;)Lorg/apache/log4j/Appender;
        //   139: areturn        
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
        final String level2 = this.getProperty("Threshold");
        final String target2 = this.getProperty("Target");
        final boolean follow2 = this.getBooleanProperty("Follow");
        final boolean immediateFlush2 = this.getBooleanProperty("ImmediateFlush");
        return this.createAppender(name, layout2, filter2, level2, target2, immediateFlush2, follow2, configuration);
    }
    
    private <T extends Log4j1Configuration> Appender createAppender(final String name, final Layout layout, final Filter filter, final String level, final String target, final boolean immediateFlush, final boolean follow, final T configuration) {
        final org.apache.logging.log4j.core.Layout<?> consoleLayout = LayoutAdapter.adapt(layout);
        final org.apache.logging.log4j.core.Filter consoleFilter = AbstractBuilder.buildFilters(level, filter);
        final ConsoleAppender.Target consoleTarget = "System.err".equals(target) ? ConsoleAppender.Target.SYSTEM_ERR : ConsoleAppender.Target.SYSTEM_OUT;
        return AppenderWrapper.adapt(ConsoleAppender.newBuilder().setName(name).setTarget(consoleTarget).setFollow(follow).setLayout((org.apache.logging.log4j.core.Layout<? extends Serializable>)consoleLayout).setFilter(consoleFilter).setConfiguration(configuration).setImmediateFlush(immediateFlush).build());
    }
    
    static {
        LOGGER = StatusLogger.getLogger();
    }
}
