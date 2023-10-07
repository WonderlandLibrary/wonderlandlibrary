// 
// Decompiled by Procyon v0.6.0
// 

package org.apache.log4j.builders.rolling;

import java.util.List;
import java.util.Collections;
import org.apache.log4j.config.PropertiesConfiguration;
import org.apache.logging.log4j.core.appender.rolling.CompositeTriggeringPolicy;
import org.apache.log4j.xml.XmlConfiguration;
import org.w3c.dom.Element;
import java.util.Properties;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.appender.rolling.TriggeringPolicy;
import org.apache.log4j.builders.AbstractBuilder;

@Plugin(name = "org.apache.log4j.rolling.CompositeTriggeringPolicy", category = "Log4j Builder")
public class CompositeTriggeringPolicyBuilder extends AbstractBuilder<TriggeringPolicy> implements TriggeringPolicyBuilder
{
    private static final TriggeringPolicy[] EMPTY_TRIGGERING_POLICIES;
    private static final String POLICY_TAG = "triggeringPolicy";
    
    public CompositeTriggeringPolicyBuilder() {
    }
    
    public CompositeTriggeringPolicyBuilder(final String prefix, final Properties props) {
        super(prefix, props);
    }
    
    @Override
    public CompositeTriggeringPolicy parse(final Element element, final XmlConfiguration configuration) {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     3: dup            
        //     4: invokespecial   java/util/ArrayList.<init>:()V
        //     7: astore_3        /* policies */
        //     8: aload_1         /* element */
        //     9: invokeinterface org/w3c/dom/Element.getChildNodes:()Lorg/w3c/dom/NodeList;
        //    14: aload_2         /* configuration */
        //    15: aload_3         /* policies */
        //    16: invokedynamic   BootstrapMethod #0, accept:(Lorg/apache/log4j/xml/XmlConfiguration;Ljava/util/List;)Ljava/util/function/Consumer;
        //    21: invokestatic    org/apache/log4j/xml/XmlConfiguration.forEachElement:(Lorg/w3c/dom/NodeList;Ljava/util/function/Consumer;)V
        //    24: aload_0         /* this */
        //    25: aload_3         /* policies */
        //    26: invokespecial   org/apache/log4j/builders/rolling/CompositeTriggeringPolicyBuilder.createTriggeringPolicy:(Ljava/util/List;)Lorg/apache/logging/log4j/core/appender/rolling/CompositeTriggeringPolicy;
        //    29: areturn        
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
    public CompositeTriggeringPolicy parse(final PropertiesConfiguration configuration) {
        return this.createTriggeringPolicy(Collections.emptyList());
    }
    
    private CompositeTriggeringPolicy createTriggeringPolicy(final List<TriggeringPolicy> policies) {
        return CompositeTriggeringPolicy.createPolicy((TriggeringPolicy[])policies.toArray(CompositeTriggeringPolicyBuilder.EMPTY_TRIGGERING_POLICIES));
    }
    
    static {
        EMPTY_TRIGGERING_POLICIES = new TriggeringPolicy[0];
    }
}
