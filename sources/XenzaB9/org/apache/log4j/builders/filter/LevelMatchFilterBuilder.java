// 
// Decompiled by Procyon v0.6.0
// 

package org.apache.log4j.builders.filter;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;
import org.apache.log4j.bridge.FilterWrapper;
import org.apache.logging.log4j.core.filter.LevelMatchFilter;
import org.apache.log4j.helpers.OptionConverter;
import org.apache.logging.log4j.Level;
import org.apache.log4j.config.PropertiesConfiguration;
import org.apache.log4j.xml.XmlConfiguration;
import org.w3c.dom.Element;
import java.util.Properties;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.log4j.spi.Filter;
import org.apache.log4j.builders.AbstractBuilder;

@Plugin(name = "org.apache.log4j.varia.LevelMatchFilter", category = "Log4j Builder")
public class LevelMatchFilterBuilder extends AbstractBuilder<Filter> implements FilterBuilder
{
    private static final String LEVEL = "LevelToMatch";
    private static final String ACCEPT_ON_MATCH = "AcceptOnMatch";
    
    public LevelMatchFilterBuilder() {
    }
    
    public LevelMatchFilterBuilder(final String prefix, final Properties props) {
        super(prefix, props);
    }
    
    @Override
    public Filter parse(final Element filterElement, final XmlConfiguration config) {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     3: dup            
        //     4: invokespecial   java/util/concurrent/atomic/AtomicReference.<init>:()V
        //     7: astore_3        /* level */
        //     8: new             Ljava/util/concurrent/atomic/AtomicBoolean;
        //    11: dup            
        //    12: invokespecial   java/util/concurrent/atomic/AtomicBoolean.<init>:()V
        //    15: astore          acceptOnMatch
        //    17: aload_1         /* filterElement */
        //    18: ldc             "param"
        //    20: invokeinterface org/w3c/dom/Element.getElementsByTagName:(Ljava/lang/String;)Lorg/w3c/dom/NodeList;
        //    25: aload_0         /* this */
        //    26: aload_3         /* level */
        //    27: aload           acceptOnMatch
        //    29: invokedynamic   BootstrapMethod #0, accept:(Lorg/apache/log4j/builders/filter/LevelMatchFilterBuilder;Ljava/util/concurrent/atomic/AtomicReference;Ljava/util/concurrent/atomic/AtomicBoolean;)Ljava/util/function/Consumer;
        //    34: invokestatic    org/apache/log4j/xml/XmlConfiguration.forEachElement:(Lorg/w3c/dom/NodeList;Ljava/util/function/Consumer;)V
        //    37: aload_0         /* this */
        //    38: aload_3         /* level */
        //    39: invokevirtual   java/util/concurrent/atomic/AtomicReference.get:()Ljava/lang/Object;
        //    42: checkcast       Ljava/lang/String;
        //    45: aload           acceptOnMatch
        //    47: invokevirtual   java/util/concurrent/atomic/AtomicBoolean.get:()Z
        //    50: invokespecial   org/apache/log4j/builders/filter/LevelMatchFilterBuilder.createFilter:(Ljava/lang/String;Z)Lorg/apache/log4j/spi/Filter;
        //    53: areturn        
        //    MethodParameters:
        //  Name           Flags  
        //  -------------  -----
        //  filterElement  
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
    public Filter parse(final PropertiesConfiguration config) {
        final String level2 = this.getProperty("LevelToMatch");
        final boolean acceptOnMatch2 = this.getBooleanProperty("AcceptOnMatch");
        return this.createFilter(level2, acceptOnMatch2);
    }
    
    private Filter createFilter(final String level, final boolean acceptOnMatch) {
        Level lvl = Level.ERROR;
        if (level != null) {
            lvl = OptionConverter.toLevel(level, org.apache.log4j.Level.ERROR).getVersion2Level();
        }
        final org.apache.logging.log4j.core.Filter.Result onMatch = acceptOnMatch ? org.apache.logging.log4j.core.Filter.Result.ACCEPT : org.apache.logging.log4j.core.Filter.Result.DENY;
        return FilterWrapper.adapt(LevelMatchFilter.newBuilder().setLevel(lvl).setOnMatch(onMatch).setOnMismatch(org.apache.logging.log4j.core.Filter.Result.NEUTRAL).build());
    }
}
