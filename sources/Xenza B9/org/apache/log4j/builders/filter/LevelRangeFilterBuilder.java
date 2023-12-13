// 
// Decompiled by Procyon v0.6.0
// 

package org.apache.log4j.builders.filter;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;
import org.apache.log4j.bridge.FilterWrapper;
import org.apache.logging.log4j.core.filter.LevelRangeFilter;
import org.apache.log4j.helpers.OptionConverter;
import org.apache.logging.log4j.Level;
import org.apache.log4j.config.PropertiesConfiguration;
import org.apache.log4j.xml.XmlConfiguration;
import org.w3c.dom.Element;
import java.util.Properties;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.log4j.spi.Filter;
import org.apache.log4j.builders.AbstractBuilder;

@Plugin(name = "org.apache.log4j.varia.LevelRangeFilter", category = "Log4j Builder")
public class LevelRangeFilterBuilder extends AbstractBuilder<Filter> implements FilterBuilder
{
    private static final String LEVEL_MAX = "LevelMax";
    private static final String LEVEL_MIN = "LevelMin";
    private static final String ACCEPT_ON_MATCH = "AcceptOnMatch";
    
    public LevelRangeFilterBuilder() {
    }
    
    public LevelRangeFilterBuilder(final String prefix, final Properties props) {
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
        //     7: astore_3        /* levelMax */
        //     8: new             Ljava/util/concurrent/atomic/AtomicReference;
        //    11: dup            
        //    12: invokespecial   java/util/concurrent/atomic/AtomicReference.<init>:()V
        //    15: astore          levelMin
        //    17: new             Ljava/util/concurrent/atomic/AtomicBoolean;
        //    20: dup            
        //    21: invokespecial   java/util/concurrent/atomic/AtomicBoolean.<init>:()V
        //    24: astore          acceptOnMatch
        //    26: aload_1         /* filterElement */
        //    27: ldc             "param"
        //    29: invokeinterface org/w3c/dom/Element.getElementsByTagName:(Ljava/lang/String;)Lorg/w3c/dom/NodeList;
        //    34: aload_0         /* this */
        //    35: aload_3         /* levelMax */
        //    36: aload           levelMin
        //    38: aload           acceptOnMatch
        //    40: invokedynamic   BootstrapMethod #0, accept:(Lorg/apache/log4j/builders/filter/LevelRangeFilterBuilder;Ljava/util/concurrent/atomic/AtomicReference;Ljava/util/concurrent/atomic/AtomicReference;Ljava/util/concurrent/atomic/AtomicBoolean;)Ljava/util/function/Consumer;
        //    45: invokestatic    org/apache/log4j/xml/XmlConfiguration.forEachElement:(Lorg/w3c/dom/NodeList;Ljava/util/function/Consumer;)V
        //    48: aload_0         /* this */
        //    49: aload_3         /* levelMax */
        //    50: invokevirtual   java/util/concurrent/atomic/AtomicReference.get:()Ljava/lang/Object;
        //    53: checkcast       Ljava/lang/String;
        //    56: aload           levelMin
        //    58: invokevirtual   java/util/concurrent/atomic/AtomicReference.get:()Ljava/lang/Object;
        //    61: checkcast       Ljava/lang/String;
        //    64: aload           acceptOnMatch
        //    66: invokevirtual   java/util/concurrent/atomic/AtomicBoolean.get:()Z
        //    69: invokespecial   org/apache/log4j/builders/filter/LevelRangeFilterBuilder.createFilter:(Ljava/lang/String;Ljava/lang/String;Z)Lorg/apache/log4j/spi/Filter;
        //    72: areturn        
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
        final String levelMax2 = this.getProperty("LevelMax");
        final String levelMin2 = this.getProperty("LevelMin");
        final boolean acceptOnMatch2 = this.getBooleanProperty("AcceptOnMatch");
        return this.createFilter(levelMax2, levelMin2, acceptOnMatch2);
    }
    
    private Filter createFilter(final String levelMax, final String levelMin, final boolean acceptOnMatch) {
        Level max = Level.OFF;
        Level min = Level.ALL;
        if (levelMax != null) {
            max = OptionConverter.toLevel(levelMax, org.apache.log4j.Level.OFF).getVersion2Level();
        }
        if (levelMin != null) {
            min = OptionConverter.toLevel(levelMin, org.apache.log4j.Level.ALL).getVersion2Level();
        }
        final org.apache.logging.log4j.core.Filter.Result onMatch = acceptOnMatch ? org.apache.logging.log4j.core.Filter.Result.ACCEPT : org.apache.logging.log4j.core.Filter.Result.NEUTRAL;
        return FilterWrapper.adapt(LevelRangeFilter.createFilter(max, min, onMatch, org.apache.logging.log4j.core.Filter.Result.DENY));
    }
}
