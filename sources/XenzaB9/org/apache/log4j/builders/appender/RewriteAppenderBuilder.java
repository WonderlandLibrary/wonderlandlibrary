// 
// Decompiled by Procyon v0.6.0
// 

package org.apache.log4j.builders.appender;

import org.apache.logging.log4j.status.StatusLogger;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import org.apache.log4j.bridge.AppenderWrapper;
import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.core.appender.rewrite.RewriteAppender;
import org.apache.log4j.bridge.RewritePolicyAdapter;
import org.apache.log4j.bridge.RewritePolicyWrapper;
import org.apache.logging.log4j.core.config.AppenderRef;
import org.apache.log4j.helpers.OptionConverter;
import org.apache.logging.log4j.Level;
import org.apache.log4j.config.Log4j1Configuration;
import org.apache.log4j.spi.Filter;
import org.apache.log4j.builders.BuilderManager;
import org.apache.log4j.rewrite.RewritePolicy;
import org.apache.log4j.config.PropertiesConfiguration;
import org.apache.log4j.Appender;
import org.apache.log4j.xml.XmlConfiguration;
import org.w3c.dom.Element;
import java.util.Properties;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.log4j.builders.AbstractBuilder;

@Plugin(name = "org.apache.log4j.rewrite.RewriteAppender", category = "Log4j Builder")
public class RewriteAppenderBuilder extends AbstractBuilder implements AppenderBuilder
{
    private static final Logger LOGGER;
    private static final String REWRITE_POLICY_TAG = "rewritePolicy";
    
    public RewriteAppenderBuilder() {
    }
    
    public RewriteAppenderBuilder(final String prefix, final Properties props) {
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
        //     2: invokevirtual   org/apache/log4j/builders/appender/RewriteAppenderBuilder.getNameAttribute:(Lorg/w3c/dom/Element;)Ljava/lang/String;
        //     5: astore_3        /* name */
        //     6: new             Ljava/util/concurrent/atomic/AtomicReference;
        //     9: dup            
        //    10: new             Ljava/util/ArrayList;
        //    13: dup            
        //    14: invokespecial   java/util/ArrayList.<init>:()V
        //    17: invokespecial   java/util/concurrent/atomic/AtomicReference.<init>:(Ljava/lang/Object;)V
        //    20: astore          appenderRefs
        //    22: new             Ljava/util/concurrent/atomic/AtomicReference;
        //    25: dup            
        //    26: invokespecial   java/util/concurrent/atomic/AtomicReference.<init>:()V
        //    29: astore          rewritePolicyHolder
        //    31: new             Ljava/util/concurrent/atomic/AtomicReference;
        //    34: dup            
        //    35: invokespecial   java/util/concurrent/atomic/AtomicReference.<init>:()V
        //    38: astore          level
        //    40: new             Ljava/util/concurrent/atomic/AtomicReference;
        //    43: dup            
        //    44: invokespecial   java/util/concurrent/atomic/AtomicReference.<init>:()V
        //    47: astore          filter
        //    49: aload_1         /* appenderElement */
        //    50: invokeinterface org/w3c/dom/Element.getChildNodes:()Lorg/w3c/dom/NodeList;
        //    55: aload_0         /* this */
        //    56: aload_2         /* config */
        //    57: aload           appenderRefs
        //    59: aload           rewritePolicyHolder
        //    61: aload           filter
        //    63: aload           level
        //    65: invokedynamic   BootstrapMethod #0, accept:(Lorg/apache/log4j/builders/appender/RewriteAppenderBuilder;Lorg/apache/log4j/xml/XmlConfiguration;Ljava/util/concurrent/atomic/AtomicReference;Ljava/util/concurrent/atomic/AtomicReference;Ljava/util/concurrent/atomic/AtomicReference;Ljava/util/concurrent/atomic/AtomicReference;)Ljava/util/function/Consumer;
        //    70: invokestatic    org/apache/log4j/xml/XmlConfiguration.forEachElement:(Lorg/w3c/dom/NodeList;Ljava/util/function/Consumer;)V
        //    73: aload_0         /* this */
        //    74: aload_3         /* name */
        //    75: aload           level
        //    77: invokevirtual   java/util/concurrent/atomic/AtomicReference.get:()Ljava/lang/Object;
        //    80: checkcast       Ljava/lang/String;
        //    83: aload           appenderRefs
        //    85: invokevirtual   java/util/concurrent/atomic/AtomicReference.get:()Ljava/lang/Object;
        //    88: checkcast       Ljava/util/List;
        //    91: getstatic       org/apache/logging/log4j/util/Strings.EMPTY_ARRAY:[Ljava/lang/String;
        //    94: invokeinterface java/util/List.toArray:([Ljava/lang/Object;)[Ljava/lang/Object;
        //    99: checkcast       [Ljava/lang/String;
        //   102: aload           rewritePolicyHolder
        //   104: invokevirtual   java/util/concurrent/atomic/AtomicReference.get:()Ljava/lang/Object;
        //   107: checkcast       Lorg/apache/log4j/rewrite/RewritePolicy;
        //   110: aload           filter
        //   112: invokevirtual   java/util/concurrent/atomic/AtomicReference.get:()Ljava/lang/Object;
        //   115: checkcast       Lorg/apache/log4j/spi/Filter;
        //   118: aload_2         /* config */
        //   119: invokespecial   org/apache/log4j/builders/appender/RewriteAppenderBuilder.createAppender:(Ljava/lang/String;Ljava/lang/String;[Ljava/lang/String;Lorg/apache/log4j/rewrite/RewritePolicy;Lorg/apache/log4j/spi/Filter;Lorg/apache/log4j/config/Log4j1Configuration;)Lorg/apache/log4j/Appender;
        //   122: areturn        
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
        final String policyPrefix = appenderPrefix + ".rewritePolicy";
        final String className = this.getProperty(policyPrefix);
        final RewritePolicy policy = configuration.getBuilderManager().parse(className, policyPrefix, props, configuration, BuilderManager.INVALID_REWRITE_POLICY);
        final String level2 = this.getProperty("Threshold");
        if (appenderRef == null) {
            RewriteAppenderBuilder.LOGGER.error("No appender references configured for RewriteAppender {}", name);
            return null;
        }
        final Appender appender = configuration.parseAppender(props, appenderRef);
        if (appender == null) {
            RewriteAppenderBuilder.LOGGER.error("Cannot locate Appender {}", appenderRef);
            return null;
        }
        return this.createAppender(name, level2, new String[] { appenderRef }, policy, filter2, configuration);
    }
    
    private <T extends Log4j1Configuration> Appender createAppender(final String name, final String level, final String[] appenderRefs, final RewritePolicy policy, final Filter filter, final T configuration) {
        if (appenderRefs.length == 0) {
            RewriteAppenderBuilder.LOGGER.error("No appender references configured for RewriteAppender {}", name);
            return null;
        }
        final Level logLevel = OptionConverter.convertLevel(level, Level.TRACE);
        final AppenderRef[] refs = new AppenderRef[appenderRefs.length];
        int index = 0;
        for (final String appenderRef : appenderRefs) {
            refs[index++] = AppenderRef.createAppenderRef(appenderRef, logLevel, null);
        }
        final org.apache.logging.log4j.core.Filter rewriteFilter = AbstractBuilder.buildFilters(level, filter);
        org.apache.logging.log4j.core.appender.rewrite.RewritePolicy rewritePolicy;
        if (policy instanceof RewritePolicyWrapper) {
            rewritePolicy = ((RewritePolicyWrapper)policy).getPolicy();
        }
        else {
            rewritePolicy = new RewritePolicyAdapter(policy);
        }
        return AppenderWrapper.adapt(RewriteAppender.createAppender(name, "true", refs, configuration, rewritePolicy, rewriteFilter));
    }
    
    static {
        LOGGER = StatusLogger.getLogger();
    }
}
