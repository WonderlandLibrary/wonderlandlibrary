// 
// Decompiled by Procyon v0.6.0
// 

package org.apache.log4j.builders.rolling;

import java.util.concurrent.atomic.AtomicLong;
import org.apache.log4j.config.PropertiesConfiguration;
import org.apache.logging.log4j.core.appender.rolling.SizeBasedTriggeringPolicy;
import org.apache.log4j.xml.XmlConfiguration;
import org.w3c.dom.Element;
import java.util.Properties;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.appender.rolling.TriggeringPolicy;
import org.apache.log4j.builders.AbstractBuilder;

@Plugin(name = "org.apache.log4j.rolling.SizeBasedTriggeringPolicy", category = "Log4j Builder")
public class SizeBasedTriggeringPolicyBuilder extends AbstractBuilder<TriggeringPolicy> implements TriggeringPolicyBuilder
{
    private static final String MAX_SIZE_PARAM = "MaxFileSize";
    private static final long DEFAULT_MAX_SIZE = 10485760L;
    
    public SizeBasedTriggeringPolicyBuilder() {
    }
    
    public SizeBasedTriggeringPolicyBuilder(final String prefix, final Properties props) {
        super(prefix, props);
    }
    
    @Override
    public SizeBasedTriggeringPolicy parse(final Element element, final XmlConfiguration configuration) {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     3: dup            
        //     4: ldc2_w          10485760
        //     7: invokespecial   java/util/concurrent/atomic/AtomicLong.<init>:(J)V
        //    10: astore_3        /* maxSize */
        //    11: aload_1         /* element */
        //    12: invokeinterface org/w3c/dom/Element.getChildNodes:()Lorg/w3c/dom/NodeList;
        //    17: aload_0         /* this */
        //    18: aload_3         /* maxSize */
        //    19: invokedynamic   BootstrapMethod #0, accept:(Lorg/apache/log4j/builders/rolling/SizeBasedTriggeringPolicyBuilder;Ljava/util/concurrent/atomic/AtomicLong;)Ljava/util/function/Consumer;
        //    24: invokestatic    org/apache/log4j/xml/XmlConfiguration.forEachElement:(Lorg/w3c/dom/NodeList;Ljava/util/function/Consumer;)V
        //    27: aload_0         /* this */
        //    28: aload_3         /* maxSize */
        //    29: invokevirtual   java/util/concurrent/atomic/AtomicLong.get:()J
        //    32: invokespecial   org/apache/log4j/builders/rolling/SizeBasedTriggeringPolicyBuilder.createTriggeringPolicy:(J)Lorg/apache/logging/log4j/core/appender/rolling/SizeBasedTriggeringPolicy;
        //    35: areturn        
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
    public SizeBasedTriggeringPolicy parse(final PropertiesConfiguration configuration) {
        final long maxSize2 = this.getLongProperty("MaxFileSize", 10485760L);
        return this.createTriggeringPolicy(maxSize2);
    }
    
    private SizeBasedTriggeringPolicy createTriggeringPolicy(final long maxSize) {
        return SizeBasedTriggeringPolicy.createPolicy(Long.toString(maxSize));
    }
}
