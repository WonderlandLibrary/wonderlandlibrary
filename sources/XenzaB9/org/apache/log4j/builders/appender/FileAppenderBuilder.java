// 
// Decompiled by Procyon v0.6.0
// 

package org.apache.log4j.builders.appender;

import org.apache.logging.log4j.status.StatusLogger;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;
import org.apache.log4j.bridge.AppenderWrapper;
import java.io.Serializable;
import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.core.appender.AbstractOutputStreamAppender;
import org.apache.logging.log4j.core.appender.FileAppender;
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

@Plugin(name = "org.apache.log4j.FileAppender", category = "Log4j Builder")
public class FileAppenderBuilder extends AbstractBuilder implements AppenderBuilder
{
    private static final Logger LOGGER;
    
    public FileAppenderBuilder() {
    }
    
    public FileAppenderBuilder(final String prefix, final Properties props) {
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
        //     2: invokevirtual   org/apache/log4j/builders/appender/FileAppenderBuilder.getNameAttribute:(Lorg/w3c/dom/Element;)Ljava/lang/String;
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
        //    83: aload_1         /* appenderElement */
        //    84: invokeinterface org/w3c/dom/Element.getChildNodes:()Lorg/w3c/dom/NodeList;
        //    89: aload_0         /* this */
        //    90: aload           layout
        //    92: aload_2         /* config */
        //    93: aload           filter
        //    95: aload           fileName
        //    97: aload           append
        //    99: aload           bufferedIo
        //   101: aload           bufferSize
        //   103: aload           level
        //   105: aload           immediateFlush
        //   107: invokedynamic   BootstrapMethod #0, accept:(Lorg/apache/log4j/builders/appender/FileAppenderBuilder;Ljava/util/concurrent/atomic/AtomicReference;Lorg/apache/log4j/xml/XmlConfiguration;Ljava/util/concurrent/atomic/AtomicReference;Ljava/util/concurrent/atomic/AtomicReference;Ljava/util/concurrent/atomic/AtomicBoolean;Ljava/util/concurrent/atomic/AtomicBoolean;Ljava/util/concurrent/atomic/AtomicInteger;Ljava/util/concurrent/atomic/AtomicReference;Ljava/util/concurrent/atomic/AtomicBoolean;)Ljava/util/function/Consumer;
        //   112: invokestatic    org/apache/log4j/xml/XmlConfiguration.forEachElement:(Lorg/w3c/dom/NodeList;Ljava/util/function/Consumer;)V
        //   115: aload_0         /* this */
        //   116: aload_3         /* name */
        //   117: aload_2         /* config */
        //   118: aload           layout
        //   120: invokevirtual   java/util/concurrent/atomic/AtomicReference.get:()Ljava/lang/Object;
        //   123: checkcast       Lorg/apache/log4j/Layout;
        //   126: aload           filter
        //   128: invokevirtual   java/util/concurrent/atomic/AtomicReference.get:()Ljava/lang/Object;
        //   131: checkcast       Lorg/apache/log4j/spi/Filter;
        //   134: aload           fileName
        //   136: invokevirtual   java/util/concurrent/atomic/AtomicReference.get:()Ljava/lang/Object;
        //   139: checkcast       Ljava/lang/String;
        //   142: aload           level
        //   144: invokevirtual   java/util/concurrent/atomic/AtomicReference.get:()Ljava/lang/Object;
        //   147: checkcast       Ljava/lang/String;
        //   150: aload           immediateFlush
        //   152: invokevirtual   java/util/concurrent/atomic/AtomicBoolean.get:()Z
        //   155: aload           append
        //   157: invokevirtual   java/util/concurrent/atomic/AtomicBoolean.get:()Z
        //   160: aload           bufferedIo
        //   162: invokevirtual   java/util/concurrent/atomic/AtomicBoolean.get:()Z
        //   165: aload           bufferSize
        //   167: invokevirtual   java/util/concurrent/atomic/AtomicInteger.get:()I
        //   170: invokespecial   org/apache/log4j/builders/appender/FileAppenderBuilder.createAppender:(Ljava/lang/String;Lorg/apache/log4j/config/Log4j1Configuration;Lorg/apache/log4j/Layout;Lorg/apache/log4j/spi/Filter;Ljava/lang/String;Ljava/lang/String;ZZZI)Lorg/apache/log4j/Appender;
        //   173: areturn        
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
        final String fileName2 = this.getProperty("File");
        final boolean append2 = this.getBooleanProperty("Append", true);
        final boolean immediateFlush2 = this.getBooleanProperty("ImmediateFlush", true);
        final boolean bufferedIo2 = this.getBooleanProperty("BufferedIO", false);
        final int bufferSize2 = Integer.parseInt(this.getProperty("BufferSize", "8192"));
        return this.createAppender(name, configuration, layout2, filter2, fileName2, level2, immediateFlush2, append2, bufferedIo2, bufferSize2);
    }
    
    private Appender createAppender(final String name, final Log4j1Configuration configuration, final Layout layout, final Filter filter, final String fileName, final String level, boolean immediateFlush, final boolean append, final boolean bufferedIo, final int bufferSize) {
        final org.apache.logging.log4j.core.Layout<?> fileLayout = LayoutAdapter.adapt(layout);
        if (bufferedIo) {
            immediateFlush = false;
        }
        final org.apache.logging.log4j.core.Filter fileFilter = AbstractBuilder.buildFilters(level, filter);
        if (fileName == null) {
            FileAppenderBuilder.LOGGER.error("Unable to create FileAppender, no file name provided");
            return null;
        }
        return AppenderWrapper.adapt(FileAppender.newBuilder().setName(name).setConfiguration(configuration).setLayout((org.apache.logging.log4j.core.Layout<? extends Serializable>)fileLayout).setFilter(fileFilter).withFileName(fileName).setImmediateFlush(immediateFlush).withAppend(append).setBufferedIo(bufferedIo).setBufferSize(bufferSize).build());
    }
    
    static {
        LOGGER = StatusLogger.getLogger();
    }
}
