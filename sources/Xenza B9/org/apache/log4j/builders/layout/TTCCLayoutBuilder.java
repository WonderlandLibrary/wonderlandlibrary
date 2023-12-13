// 
// Decompiled by Procyon v0.6.0
// 

package org.apache.log4j.builders.layout;

import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.atomic.AtomicBoolean;
import org.apache.log4j.bridge.LayoutWrapper;
import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.core.layout.PatternLayout;
import org.apache.log4j.config.Log4j1Configuration;
import org.apache.log4j.config.PropertiesConfiguration;
import org.apache.log4j.xml.XmlConfiguration;
import org.w3c.dom.Element;
import java.util.Properties;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.log4j.Layout;
import org.apache.log4j.builders.AbstractBuilder;

@Plugin(name = "org.apache.log4j.TTCCLayout", category = "Log4j Builder")
public class TTCCLayoutBuilder extends AbstractBuilder<Layout> implements LayoutBuilder
{
    private static final String THREAD_PRINTING_PARAM = "ThreadPrinting";
    private static final String CATEGORY_PREFIXING_PARAM = "CategoryPrefixing";
    private static final String CONTEXT_PRINTING_PARAM = "ContextPrinting";
    private static final String DATE_FORMAT_PARAM = "DateFormat";
    private static final String TIMEZONE_FORMAT = "TimeZone";
    
    public TTCCLayoutBuilder() {
    }
    
    public TTCCLayoutBuilder(final String prefix, final Properties props) {
        super(prefix, props);
    }
    
    @Override
    public Layout parse(final Element layoutElement, final XmlConfiguration config) {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     3: dup            
        //     4: getstatic       java/lang/Boolean.TRUE:Ljava/lang/Boolean;
        //     7: invokevirtual   java/lang/Boolean.booleanValue:()Z
        //    10: invokespecial   java/util/concurrent/atomic/AtomicBoolean.<init>:(Z)V
        //    13: astore_3        /* threadPrinting */
        //    14: new             Ljava/util/concurrent/atomic/AtomicBoolean;
        //    17: dup            
        //    18: getstatic       java/lang/Boolean.TRUE:Ljava/lang/Boolean;
        //    21: invokevirtual   java/lang/Boolean.booleanValue:()Z
        //    24: invokespecial   java/util/concurrent/atomic/AtomicBoolean.<init>:(Z)V
        //    27: astore          categoryPrefixing
        //    29: new             Ljava/util/concurrent/atomic/AtomicBoolean;
        //    32: dup            
        //    33: getstatic       java/lang/Boolean.TRUE:Ljava/lang/Boolean;
        //    36: invokevirtual   java/lang/Boolean.booleanValue:()Z
        //    39: invokespecial   java/util/concurrent/atomic/AtomicBoolean.<init>:(Z)V
        //    42: astore          contextPrinting
        //    44: new             Ljava/util/concurrent/atomic/AtomicReference;
        //    47: dup            
        //    48: ldc             "RELATIVE"
        //    50: invokespecial   java/util/concurrent/atomic/AtomicReference.<init>:(Ljava/lang/Object;)V
        //    53: astore          dateFormat
        //    55: new             Ljava/util/concurrent/atomic/AtomicReference;
        //    58: dup            
        //    59: invokespecial   java/util/concurrent/atomic/AtomicReference.<init>:()V
        //    62: astore          timezone
        //    64: aload_1         /* layoutElement */
        //    65: ldc             "param"
        //    67: invokeinterface org/w3c/dom/Element.getElementsByTagName:(Ljava/lang/String;)Lorg/w3c/dom/NodeList;
        //    72: aload_0         /* this */
        //    73: aload_3         /* threadPrinting */
        //    74: aload           categoryPrefixing
        //    76: aload           contextPrinting
        //    78: aload           dateFormat
        //    80: aload           timezone
        //    82: invokedynamic   BootstrapMethod #0, accept:(Lorg/apache/log4j/builders/layout/TTCCLayoutBuilder;Ljava/util/concurrent/atomic/AtomicBoolean;Ljava/util/concurrent/atomic/AtomicBoolean;Ljava/util/concurrent/atomic/AtomicBoolean;Ljava/util/concurrent/atomic/AtomicReference;Ljava/util/concurrent/atomic/AtomicReference;)Ljava/util/function/Consumer;
        //    87: invokestatic    org/apache/log4j/xml/XmlConfiguration.forEachElement:(Lorg/w3c/dom/NodeList;Ljava/util/function/Consumer;)V
        //    90: aload_0         /* this */
        //    91: aload_3         /* threadPrinting */
        //    92: invokevirtual   java/util/concurrent/atomic/AtomicBoolean.get:()Z
        //    95: aload           categoryPrefixing
        //    97: invokevirtual   java/util/concurrent/atomic/AtomicBoolean.get:()Z
        //   100: aload           contextPrinting
        //   102: invokevirtual   java/util/concurrent/atomic/AtomicBoolean.get:()Z
        //   105: aload           dateFormat
        //   107: invokevirtual   java/util/concurrent/atomic/AtomicReference.get:()Ljava/lang/Object;
        //   110: checkcast       Ljava/lang/String;
        //   113: aload           timezone
        //   115: invokevirtual   java/util/concurrent/atomic/AtomicReference.get:()Ljava/lang/Object;
        //   118: checkcast       Ljava/lang/String;
        //   121: aload_2         /* config */
        //   122: invokespecial   org/apache/log4j/builders/layout/TTCCLayoutBuilder.createLayout:(ZZZLjava/lang/String;Ljava/lang/String;Lorg/apache/log4j/config/Log4j1Configuration;)Lorg/apache/log4j/Layout;
        //   125: areturn        
        //    MethodParameters:
        //  Name           Flags  
        //  -------------  -----
        //  layoutElement  
        //  config         
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
    public Layout parse(final PropertiesConfiguration config) {
        final boolean threadPrinting2 = this.getBooleanProperty("ThreadPrinting", true);
        final boolean categoryPrefixing2 = this.getBooleanProperty("CategoryPrefixing", true);
        final boolean contextPrinting2 = this.getBooleanProperty("ContextPrinting", true);
        final String dateFormat2 = this.getProperty("DateFormat", "RELATIVE");
        final String timezone2 = this.getProperty("TimeZone");
        return this.createLayout(threadPrinting2, categoryPrefixing2, contextPrinting2, dateFormat2, timezone2, config);
    }
    
    private Layout createLayout(final boolean threadPrinting, final boolean categoryPrefixing, final boolean contextPrinting, final String dateFormat, final String timezone, final Log4j1Configuration config) {
        final StringBuilder sb = new StringBuilder();
        if (dateFormat != null) {
            if ("RELATIVE".equalsIgnoreCase(dateFormat)) {
                sb.append("%r ");
            }
            else if (!"NULL".equalsIgnoreCase(dateFormat)) {
                sb.append("%d{").append(dateFormat).append("}");
                if (timezone != null) {
                    sb.append("{").append(timezone).append("}");
                }
                sb.append(" ");
            }
        }
        if (threadPrinting) {
            sb.append("[%t] ");
        }
        sb.append("%p ");
        if (categoryPrefixing) {
            sb.append("%c ");
        }
        if (contextPrinting) {
            sb.append("%notEmpty{%ndc }");
        }
        sb.append("- %m%n");
        return LayoutWrapper.adapt(PatternLayout.newBuilder().withPattern(sb.toString()).withConfiguration(config).build());
    }
}
