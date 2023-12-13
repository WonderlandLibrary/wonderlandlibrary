// 
// Decompiled by Procyon v0.6.0
// 

package org.apache.log4j.builders.appender;

import org.apache.logging.log4j.status.StatusLogger;
import java.util.concurrent.atomic.AtomicBoolean;
import org.apache.logging.log4j.util.Strings;
import org.apache.log4j.bridge.AppenderWrapper;
import java.io.Serializable;
import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.core.appender.SyslogAppender;
import org.apache.logging.log4j.core.net.Facility;
import org.apache.log4j.layout.Log4j1SyslogLayout;
import org.apache.log4j.bridge.LayoutAdapter;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import org.apache.log4j.Layout;
import org.apache.log4j.spi.Filter;
import org.apache.log4j.config.Log4j1Configuration;
import org.apache.logging.log4j.core.net.Protocol;
import org.apache.log4j.config.PropertiesConfiguration;
import org.apache.log4j.Appender;
import org.apache.log4j.xml.XmlConfiguration;
import org.w3c.dom.Element;
import java.util.Properties;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.log4j.builders.AbstractBuilder;

@Plugin(name = "org.apache.log4j.net.SyslogAppender", category = "Log4j Builder")
public class SyslogAppenderBuilder extends AbstractBuilder implements AppenderBuilder
{
    private static final String DEFAULT_HOST = "localhost";
    private static int DEFAULT_PORT;
    private static final String DEFAULT_FACILITY = "LOCAL0";
    private static final Logger LOGGER;
    private static final String FACILITY_PARAM = "Facility";
    private static final String FACILITY_PRINTING_PARAM = "FacilityPrinting";
    private static final String HEADER_PARAM = "Header";
    private static final String PROTOCOL_PARAM = "Protocol";
    private static final String SYSLOG_HOST_PARAM = "SyslogHost";
    
    public SyslogAppenderBuilder() {
    }
    
    public SyslogAppenderBuilder(final String prefix, final Properties props) {
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
        //     2: invokevirtual   org/apache/log4j/builders/appender/SyslogAppenderBuilder.getNameAttribute:(Lorg/w3c/dom/Element;)Ljava/lang/String;
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
        //    31: astore          facility
        //    33: new             Ljava/util/concurrent/atomic/AtomicReference;
        //    36: dup            
        //    37: invokespecial   java/util/concurrent/atomic/AtomicReference.<init>:()V
        //    40: astore          level
        //    42: new             Ljava/util/concurrent/atomic/AtomicReference;
        //    45: dup            
        //    46: invokespecial   java/util/concurrent/atomic/AtomicReference.<init>:()V
        //    49: astore          host
        //    51: new             Ljava/util/concurrent/atomic/AtomicReference;
        //    54: dup            
        //    55: getstatic       org/apache/logging/log4j/core/net/Protocol.TCP:Lorg/apache/logging/log4j/core/net/Protocol;
        //    58: invokespecial   java/util/concurrent/atomic/AtomicReference.<init>:(Ljava/lang/Object;)V
        //    61: astore          protocol
        //    63: new             Ljava/util/concurrent/atomic/AtomicBoolean;
        //    66: dup            
        //    67: iconst_0       
        //    68: invokespecial   java/util/concurrent/atomic/AtomicBoolean.<init>:(Z)V
        //    71: astore          header
        //    73: new             Ljava/util/concurrent/atomic/AtomicBoolean;
        //    76: dup            
        //    77: iconst_0       
        //    78: invokespecial   java/util/concurrent/atomic/AtomicBoolean.<init>:(Z)V
        //    81: astore          facilityPrinting
        //    83: aload_1         /* appenderElement */
        //    84: invokeinterface org/w3c/dom/Element.getChildNodes:()Lorg/w3c/dom/NodeList;
        //    89: aload_0         /* this */
        //    90: aload           layout
        //    92: aload_2         /* config */
        //    93: aload           filter
        //    95: aload           facility
        //    97: aload           facilityPrinting
        //    99: aload           header
        //   101: aload           protocol
        //   103: aload           host
        //   105: aload           level
        //   107: invokedynamic   BootstrapMethod #0, accept:(Lorg/apache/log4j/builders/appender/SyslogAppenderBuilder;Ljava/util/concurrent/atomic/AtomicReference;Lorg/apache/log4j/xml/XmlConfiguration;Ljava/util/concurrent/atomic/AtomicReference;Ljava/util/concurrent/atomic/AtomicReference;Ljava/util/concurrent/atomic/AtomicBoolean;Ljava/util/concurrent/atomic/AtomicBoolean;Ljava/util/concurrent/atomic/AtomicReference;Ljava/util/concurrent/atomic/AtomicReference;Ljava/util/concurrent/atomic/AtomicReference;)Ljava/util/function/Consumer;
        //   112: invokestatic    org/apache/log4j/xml/XmlConfiguration.forEachElement:(Lorg/w3c/dom/NodeList;Ljava/util/function/Consumer;)V
        //   115: aload_0         /* this */
        //   116: aload_3         /* name */
        //   117: aload_2         /* config */
        //   118: aload           layout
        //   120: invokevirtual   java/util/concurrent/atomic/AtomicReference.get:()Ljava/lang/Object;
        //   123: checkcast       Lorg/apache/log4j/Layout;
        //   126: aload           facility
        //   128: invokevirtual   java/util/concurrent/atomic/AtomicReference.get:()Ljava/lang/Object;
        //   131: checkcast       Ljava/lang/String;
        //   134: aload           filter
        //   136: invokevirtual   java/util/concurrent/atomic/AtomicReference.get:()Ljava/lang/Object;
        //   139: checkcast       Lorg/apache/log4j/spi/Filter;
        //   142: aload           host
        //   144: invokevirtual   java/util/concurrent/atomic/AtomicReference.get:()Ljava/lang/Object;
        //   147: checkcast       Ljava/lang/String;
        //   150: aload           level
        //   152: invokevirtual   java/util/concurrent/atomic/AtomicReference.get:()Ljava/lang/Object;
        //   155: checkcast       Ljava/lang/String;
        //   158: aload           protocol
        //   160: invokevirtual   java/util/concurrent/atomic/AtomicReference.get:()Ljava/lang/Object;
        //   163: checkcast       Lorg/apache/logging/log4j/core/net/Protocol;
        //   166: aload           header
        //   168: invokevirtual   java/util/concurrent/atomic/AtomicBoolean.get:()Z
        //   171: aload           facilityPrinting
        //   173: invokevirtual   java/util/concurrent/atomic/AtomicBoolean.get:()Z
        //   176: invokespecial   org/apache/log4j/builders/appender/SyslogAppenderBuilder.createAppender:(Ljava/lang/String;Lorg/apache/log4j/config/Log4j1Configuration;Lorg/apache/log4j/Layout;Ljava/lang/String;Lorg/apache/log4j/spi/Filter;Ljava/lang/String;Ljava/lang/String;Lorg/apache/logging/log4j/core/net/Protocol;ZZ)Lorg/apache/log4j/Appender;
        //   179: areturn        
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
        final Filter filter2 = configuration.parseAppenderFilters(props, filterPrefix, name);
        final Layout layout2 = configuration.parseLayout(layoutPrefix, name, props);
        final String level2 = this.getProperty("Threshold");
        final String facility2 = this.getProperty("Facility", "LOCAL0");
        final boolean facilityPrinting2 = this.getBooleanProperty("FacilityPrinting", false);
        final boolean header2 = this.getBooleanProperty("Header", false);
        final String protocol2 = this.getProperty("Protocol", Protocol.TCP.name());
        final String syslogHost = this.getProperty("SyslogHost", "localhost:" + SyslogAppenderBuilder.DEFAULT_PORT);
        return this.createAppender(name, configuration, layout2, facility2, filter2, syslogHost, level2, Protocol.valueOf(protocol2), header2, facilityPrinting2);
    }
    
    private Appender createAppender(final String name, final Log4j1Configuration configuration, final Layout layout, final String facility, final Filter filter, final String syslogHost, final String level, final Protocol protocol, final boolean header, final boolean facilityPrinting) {
        final AtomicReference<String> host2 = new AtomicReference<String>();
        final AtomicInteger port = new AtomicInteger();
        this.resolveSyslogHost(syslogHost, host2, port);
        final org.apache.logging.log4j.core.Layout<? extends Serializable> messageLayout = (org.apache.logging.log4j.core.Layout<? extends Serializable>)LayoutAdapter.adapt(layout);
        final Log4j1SyslogLayout appenderLayout = Log4j1SyslogLayout.newBuilder().setHeader(header).setFacility(Facility.toFacility(facility)).setFacilityPrinting(facilityPrinting).setMessageLayout(messageLayout).build();
        final org.apache.logging.log4j.core.Filter fileFilter = AbstractBuilder.buildFilters(level, filter);
        return AppenderWrapper.adapt(SyslogAppender.newSyslogAppenderBuilder().setName(name).setConfiguration(configuration).setLayout(appenderLayout).setFilter(fileFilter).setPort(port.get()).setProtocol(protocol).setHost(host2.get()).build());
    }
    
    private void resolveSyslogHost(final String syslogHost, final AtomicReference<String> host, final AtomicInteger port) {
        final String[] parts = (syslogHost != null) ? syslogHost.split(":") : Strings.EMPTY_ARRAY;
        if (parts.length == 1) {
            host.set(parts[0]);
            port.set(SyslogAppenderBuilder.DEFAULT_PORT);
        }
        else if (parts.length == 2) {
            host.set(parts[0]);
            port.set(Integer.parseInt(parts[1].trim()));
        }
        else {
            SyslogAppenderBuilder.LOGGER.warn("Invalid {} setting: {}. Using default.", "SyslogHost", syslogHost);
            host.set("localhost");
            port.set(SyslogAppenderBuilder.DEFAULT_PORT);
        }
    }
    
    static {
        SyslogAppenderBuilder.DEFAULT_PORT = 514;
        LOGGER = StatusLogger.getLogger();
    }
}
