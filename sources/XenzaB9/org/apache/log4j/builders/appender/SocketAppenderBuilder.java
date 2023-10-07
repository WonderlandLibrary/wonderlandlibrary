// 
// Decompiled by Procyon v0.6.0
// 

package org.apache.log4j.builders.appender;

import org.apache.logging.log4j.status.StatusLogger;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import org.apache.log4j.config.PropertiesConfiguration;
import org.apache.log4j.xml.XmlConfiguration;
import org.w3c.dom.Element;
import org.apache.log4j.bridge.AppenderWrapper;
import org.apache.logging.log4j.core.config.Configuration;
import java.io.Serializable;
import org.apache.logging.log4j.core.appender.SocketAppender;
import org.apache.log4j.bridge.LayoutAdapter;
import org.apache.log4j.Appender;
import org.apache.log4j.config.Log4j1Configuration;
import org.apache.log4j.spi.Filter;
import org.apache.log4j.Layout;
import java.util.Properties;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.log4j.builders.AbstractBuilder;

@Plugin(name = "org.apache.log4j.net.SocketAppender", category = "Log4j Builder")
public class SocketAppenderBuilder extends AbstractBuilder implements AppenderBuilder
{
    private static final String HOST_PARAM = "RemoteHost";
    private static final String PORT_PARAM = "Port";
    private static final String RECONNECTION_DELAY_PARAM = "ReconnectionDelay";
    private static final int DEFAULT_PORT = 4560;
    private static final int DEFAULT_RECONNECTION_DELAY = 30000;
    public static final Logger LOGGER;
    
    public SocketAppenderBuilder() {
    }
    
    public SocketAppenderBuilder(final String prefix, final Properties props) {
        super(prefix, props);
    }
    
    private <T extends Log4j1Configuration> Appender createAppender(final String name, final String host, final int port, final Layout layout, final Filter filter, final String level, final boolean immediateFlush, final int reconnectDelayMillis, final T configuration) {
        final org.apache.logging.log4j.core.Layout<?> actualLayout = LayoutAdapter.adapt(layout);
        final org.apache.logging.log4j.core.Filter actualFilter = AbstractBuilder.buildFilters(level, filter);
        return AppenderWrapper.adapt(SocketAppender.newBuilder().setHost(host).setPort(port).setReconnectDelayMillis(reconnectDelayMillis).setName(name).setLayout((org.apache.logging.log4j.core.Layout<? extends Serializable>)actualLayout).setFilter(actualFilter).setConfiguration(configuration).setImmediateFlush(immediateFlush).build());
    }
    
    @Override
    public Appender parseAppender(final Element appenderElement, final XmlConfiguration config) {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     1: aload_1         /* appenderElement */
        //     2: invokevirtual   org/apache/log4j/builders/appender/SocketAppenderBuilder.getNameAttribute:(Lorg/w3c/dom/Element;)Ljava/lang/String;
        //     5: astore_3        /* name */
        //     6: new             Ljava/util/concurrent/atomic/AtomicReference;
        //     9: dup            
        //    10: ldc             "localhost"
        //    12: invokespecial   java/util/concurrent/atomic/AtomicReference.<init>:(Ljava/lang/Object;)V
        //    15: astore          host
        //    17: new             Ljava/util/concurrent/atomic/AtomicInteger;
        //    20: dup            
        //    21: sipush          4560
        //    24: invokespecial   java/util/concurrent/atomic/AtomicInteger.<init>:(I)V
        //    27: astore          port
        //    29: new             Ljava/util/concurrent/atomic/AtomicInteger;
        //    32: dup            
        //    33: sipush          30000
        //    36: invokespecial   java/util/concurrent/atomic/AtomicInteger.<init>:(I)V
        //    39: astore          reconnectDelay
        //    41: new             Ljava/util/concurrent/atomic/AtomicReference;
        //    44: dup            
        //    45: invokespecial   java/util/concurrent/atomic/AtomicReference.<init>:()V
        //    48: astore          layout
        //    50: new             Ljava/util/concurrent/atomic/AtomicReference;
        //    53: dup            
        //    54: invokespecial   java/util/concurrent/atomic/AtomicReference.<init>:()V
        //    57: astore          filter
        //    59: new             Ljava/util/concurrent/atomic/AtomicReference;
        //    62: dup            
        //    63: invokespecial   java/util/concurrent/atomic/AtomicReference.<init>:()V
        //    66: astore          level
        //    68: new             Ljava/util/concurrent/atomic/AtomicBoolean;
        //    71: dup            
        //    72: iconst_1       
        //    73: invokespecial   java/util/concurrent/atomic/AtomicBoolean.<init>:(Z)V
        //    76: astore          immediateFlush
        //    78: aload_1         /* appenderElement */
        //    79: invokeinterface org/w3c/dom/Element.getChildNodes:()Lorg/w3c/dom/NodeList;
        //    84: aload_0         /* this */
        //    85: aload           layout
        //    87: aload_2         /* config */
        //    88: aload           filter
        //    90: aload           host
        //    92: aload           port
        //    94: aload           reconnectDelay
        //    96: aload           level
        //    98: aload           immediateFlush
        //   100: invokedynamic   BootstrapMethod #0, accept:(Lorg/apache/log4j/builders/appender/SocketAppenderBuilder;Ljava/util/concurrent/atomic/AtomicReference;Lorg/apache/log4j/xml/XmlConfiguration;Ljava/util/concurrent/atomic/AtomicReference;Ljava/util/concurrent/atomic/AtomicReference;Ljava/util/concurrent/atomic/AtomicInteger;Ljava/util/concurrent/atomic/AtomicInteger;Ljava/util/concurrent/atomic/AtomicReference;Ljava/util/concurrent/atomic/AtomicBoolean;)Ljava/util/function/Consumer;
        //   105: invokestatic    org/apache/log4j/xml/XmlConfiguration.forEachElement:(Lorg/w3c/dom/NodeList;Ljava/util/function/Consumer;)V
        //   108: aload_0         /* this */
        //   109: aload_3         /* name */
        //   110: aload           host
        //   112: invokevirtual   java/util/concurrent/atomic/AtomicReference.get:()Ljava/lang/Object;
        //   115: checkcast       Ljava/lang/String;
        //   118: aload           port
        //   120: invokevirtual   java/util/concurrent/atomic/AtomicInteger.get:()I
        //   123: aload           layout
        //   125: invokevirtual   java/util/concurrent/atomic/AtomicReference.get:()Ljava/lang/Object;
        //   128: checkcast       Lorg/apache/log4j/Layout;
        //   131: aload           filter
        //   133: invokevirtual   java/util/concurrent/atomic/AtomicReference.get:()Ljava/lang/Object;
        //   136: checkcast       Lorg/apache/log4j/spi/Filter;
        //   139: aload           level
        //   141: invokevirtual   java/util/concurrent/atomic/AtomicReference.get:()Ljava/lang/Object;
        //   144: checkcast       Ljava/lang/String;
        //   147: aload           immediateFlush
        //   149: invokevirtual   java/util/concurrent/atomic/AtomicBoolean.get:()Z
        //   152: aload           reconnectDelay
        //   154: invokevirtual   java/util/concurrent/atomic/AtomicInteger.get:()I
        //   157: aload_2         /* config */
        //   158: invokespecial   org/apache/log4j/builders/appender/SocketAppenderBuilder.createAppender:(Ljava/lang/String;Ljava/lang/String;ILorg/apache/log4j/Layout;Lorg/apache/log4j/spi/Filter;Ljava/lang/String;ZILorg/apache/log4j/config/Log4j1Configuration;)Lorg/apache/log4j/Appender;
        //   161: areturn        
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
        return this.createAppender(name, this.getProperty("RemoteHost"), this.getIntegerProperty("Port", 4560), configuration.parseLayout(layoutPrefix, name, props), configuration.parseAppenderFilters(props, filterPrefix, name), this.getProperty("Threshold"), this.getBooleanProperty("ImmediateFlush"), this.getIntegerProperty("ReconnectionDelay", 30000), configuration);
    }
    
    static {
        LOGGER = StatusLogger.getLogger();
    }
}
