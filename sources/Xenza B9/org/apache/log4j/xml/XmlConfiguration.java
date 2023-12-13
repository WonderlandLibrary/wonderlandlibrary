// 
// Decompiled by Procyon v0.6.0
// 

package org.apache.log4j.xml;

import org.xml.sax.SAXParseException;
import org.apache.logging.log4j.status.StatusLogger;
import org.apache.log4j.spi.AppenderAttachable;
import org.apache.log4j.bridge.AppenderAdapter;
import java.util.function.IntFunction;
import java.util.stream.IntStream;
import java.util.function.Consumer;
import org.apache.logging.log4j.core.appender.rolling.TriggeringPolicy;
import org.apache.log4j.Layout;
import org.apache.logging.log4j.core.config.LoggerConfig;
import org.apache.logging.log4j.Level;
import org.apache.log4j.bridge.FilterAdapter;
import org.apache.log4j.spi.Filter;
import java.util.concurrent.atomic.AtomicReference;
import org.apache.logging.log4j.util.LoaderUtil;
import org.apache.log4j.rewrite.RewritePolicy;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.apache.log4j.config.PropertySetter;
import org.w3c.dom.Element;
import org.apache.logging.log4j.core.LifeCycle;
import org.apache.logging.log4j.core.config.Configuration;
import java.io.InterruptedIOException;
import org.xml.sax.EntityResolver;
import org.xml.sax.ErrorHandler;
import javax.xml.parsers.DocumentBuilderFactory;
import org.apache.log4j.helpers.OptionConverter;
import javax.xml.parsers.FactoryConfigurationError;
import java.io.IOException;
import org.xml.sax.SAXException;
import org.xml.sax.InputSource;
import org.w3c.dom.Document;
import javax.xml.parsers.DocumentBuilder;
import java.util.HashMap;
import org.apache.logging.log4j.core.config.ConfigurationSource;
import org.apache.logging.log4j.core.LoggerContext;
import java.util.Properties;
import org.apache.log4j.Appender;
import java.util.Map;
import org.apache.logging.log4j.Logger;
import org.apache.log4j.config.Log4j1Configuration;

public class XmlConfiguration extends Log4j1Configuration
{
    private static final Logger LOGGER;
    private static final String CONFIGURATION_TAG = "log4j:configuration";
    private static final String OLD_CONFIGURATION_TAG = "configuration";
    private static final String RENDERER_TAG = "renderer";
    private static final String APPENDER_TAG = "appender";
    public static final String PARAM_TAG = "param";
    public static final String LAYOUT_TAG = "layout";
    private static final String CATEGORY = "category";
    private static final String LOGGER_ELEMENT = "logger";
    private static final String CATEGORY_FACTORY_TAG = "categoryFactory";
    private static final String LOGGER_FACTORY_TAG = "loggerFactory";
    public static final String NAME_ATTR = "name";
    private static final String CLASS_ATTR = "class";
    public static final String VALUE_ATTR = "value";
    private static final String ROOT_TAG = "root";
    private static final String LEVEL_TAG = "level";
    private static final String PRIORITY_TAG = "priority";
    public static final String FILTER_TAG = "filter";
    private static final String ERROR_HANDLER_TAG = "errorHandler";
    public static final String REF_ATTR = "ref";
    private static final String ADDITIVITY_ATTR = "additivity";
    private static final String CONFIG_DEBUG_ATTR = "configDebug";
    private static final String INTERNAL_DEBUG_ATTR = "debug";
    private static final String THRESHOLD_ATTR = "threshold";
    private static final String EMPTY_STR = "";
    private static final Class<?>[] ONE_STRING_PARAM;
    private static final String dbfKey = "javax.xml.parsers.DocumentBuilderFactory";
    private static final String THROWABLE_RENDERER_TAG = "throwableRenderer";
    public static final long DEFAULT_DELAY = 60000L;
    protected static final String TEST_PREFIX = "log4j-test";
    protected static final String DEFAULT_PREFIX = "log4j";
    private Map<String, Appender> appenderMap;
    private Properties props;
    
    public XmlConfiguration(final LoggerContext loggerContext, final ConfigurationSource source, final int monitorIntervalSeconds) {
        super(loggerContext, source, monitorIntervalSeconds);
        this.props = null;
        this.appenderMap = new HashMap<String, Appender>();
    }
    
    public void addAppenderIfAbsent(final Appender appender) {
        this.appenderMap.putIfAbsent(appender.getName(), appender);
    }
    
    public void doConfigure() throws FactoryConfigurationError {
        final ConfigurationSource source = this.getConfigurationSource();
        final ParseAction action = new ParseAction() {
            @Override
            public Document parse(final DocumentBuilder parser) throws SAXException, IOException {
                final InputSource inputSource = new InputSource(source.getInputStream());
                inputSource.setSystemId("dummy://log4j.dtd");
                return parser.parse(inputSource);
            }
            
            @Override
            public String toString() {
                return XmlConfiguration.this.getConfigurationSource().getLocation();
            }
        };
        this.doConfigure(action);
    }
    
    private void doConfigure(final ParseAction action) throws FactoryConfigurationError {
        DocumentBuilderFactory dbf;
        try {
            XmlConfiguration.LOGGER.debug("System property is : {}", OptionConverter.getSystemProperty("javax.xml.parsers.DocumentBuilderFactory", null));
            dbf = DocumentBuilderFactory.newInstance();
            XmlConfiguration.LOGGER.debug("Standard DocumentBuilderFactory search succeded.");
            XmlConfiguration.LOGGER.debug("DocumentBuilderFactory is: " + dbf.getClass().getName());
        }
        catch (final FactoryConfigurationError fce) {
            final Exception e = fce.getException();
            XmlConfiguration.LOGGER.debug("Could not instantiate a DocumentBuilderFactory.", e);
            throw fce;
        }
        try {
            dbf.setValidating(true);
            final DocumentBuilder docBuilder = dbf.newDocumentBuilder();
            docBuilder.setErrorHandler(new SAXErrorHandler());
            docBuilder.setEntityResolver(new Log4jEntityResolver());
            final Document doc = action.parse(docBuilder);
            this.parse(doc.getDocumentElement());
        }
        catch (final Exception e2) {
            if (e2 instanceof InterruptedException || e2 instanceof InterruptedIOException) {
                Thread.currentThread().interrupt();
            }
            XmlConfiguration.LOGGER.error("Could not parse " + action.toString() + ".", e2);
        }
    }
    
    @Override
    public Configuration reconfigure() {
        try {
            final ConfigurationSource source = this.getConfigurationSource().resetInputStream();
            if (source == null) {
                return null;
            }
            final XmlConfigurationFactory factory = new XmlConfigurationFactory();
            final XmlConfiguration config = (XmlConfiguration)factory.getConfiguration(this.getLoggerContext(), source);
            return (config == null || config.getState() != LifeCycle.State.INITIALIZING) ? null : config;
        }
        catch (final IOException ex) {
            XmlConfiguration.LOGGER.error("Cannot locate file {}: {}", this.getConfigurationSource(), ex);
            return null;
        }
    }
    
    private void parseUnrecognizedElement(final Object instance, final Element element, final Properties props) throws Exception {
        boolean recognized = false;
        if (instance instanceof UnrecognizedElementHandler) {
            recognized = ((UnrecognizedElementHandler)instance).parseUnrecognizedElement(element, props);
        }
        if (!recognized) {
            XmlConfiguration.LOGGER.warn("Unrecognized element {}", element.getNodeName());
        }
    }
    
    private void quietParseUnrecognizedElement(final Object instance, final Element element, final Properties props) {
        try {
            this.parseUnrecognizedElement(instance, element, props);
        }
        catch (final Exception ex) {
            if (ex instanceof InterruptedException || ex instanceof InterruptedIOException) {
                Thread.currentThread().interrupt();
            }
            XmlConfiguration.LOGGER.error("Error in extension content: ", ex);
        }
    }
    
    public String subst(final String value, final Properties props) {
        try {
            return OptionConverter.substVars(value, props);
        }
        catch (final IllegalArgumentException e) {
            XmlConfiguration.LOGGER.warn("Could not perform variable substitution.", e);
            return value;
        }
    }
    
    public void setParameter(final Element elem, final PropertySetter propSetter, final Properties props) {
        final String name = this.subst(elem.getAttribute("name"), props);
        String value = elem.getAttribute("value");
        value = this.subst(OptionConverter.convertSpecialChars(value), props);
        propSetter.setProperty(name, value);
    }
    
    public Object parseElement(final Element element, final Properties props, final Class expectedClass) throws Exception {
        final String clazz = this.subst(element.getAttribute("class"), props);
        final Object instance = OptionConverter.instantiateByClassName(clazz, expectedClass, null);
        if (instance != null) {
            final PropertySetter propSetter = new PropertySetter(instance);
            final NodeList children = element.getChildNodes();
            for (int length = children.getLength(), loop = 0; loop < length; ++loop) {
                final Node currentNode = children.item(loop);
                if (currentNode.getNodeType() == 1) {
                    final Element currentElement = (Element)currentNode;
                    final String tagName = currentElement.getTagName();
                    if (tagName.equals("param")) {
                        this.setParameter(currentElement, propSetter, props);
                    }
                    else {
                        this.parseUnrecognizedElement(instance, currentElement, props);
                    }
                }
            }
            return instance;
        }
        return null;
    }
    
    private Appender findAppenderByName(final Document doc, final String appenderName) {
        Appender appender = this.appenderMap.get(appenderName);
        if (appender != null) {
            return appender;
        }
        Element element = null;
        final NodeList list = doc.getElementsByTagName("appender");
        for (int t = 0; t < list.getLength(); ++t) {
            final Node node = list.item(t);
            final NamedNodeMap map = node.getAttributes();
            final Node attrNode = map.getNamedItem("name");
            if (appenderName.equals(attrNode.getNodeValue())) {
                element = (Element)node;
                break;
            }
        }
        if (element == null) {
            XmlConfiguration.LOGGER.error("No appender named [{}] could be found.", appenderName);
            return null;
        }
        appender = this.parseAppender(element);
        if (appender != null) {
            this.appenderMap.put(appenderName, appender);
        }
        return appender;
    }
    
    public Appender findAppenderByReference(final Element appenderRef) {
        final String appenderName = this.subst(appenderRef.getAttribute("ref"));
        final Document doc = appenderRef.getOwnerDocument();
        return this.findAppenderByName(doc, appenderName);
    }
    
    public Appender parseAppender(final Element appenderElement) {
        final String className = this.subst(appenderElement.getAttribute("class"));
        XmlConfiguration.LOGGER.debug("Class name: [" + className + ']');
        Appender appender = this.manager.parseAppender(className, appenderElement, this);
        if (appender == null) {
            appender = this.buildAppender(className, appenderElement);
        }
        return appender;
    }
    
    private Appender buildAppender(final String className, final Element appenderElement) {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     1: invokestatic    org/apache/logging/log4j/util/LoaderUtil.newInstanceOf:(Ljava/lang/String;)Ljava/lang/Object;
        //     4: checkcast       Lorg/apache/log4j/Appender;
        //     7: astore_3        /* appender */
        //     8: new             Lorg/apache/log4j/config/PropertySetter;
        //    11: dup            
        //    12: aload_3         /* appender */
        //    13: invokespecial   org/apache/log4j/config/PropertySetter.<init>:(Ljava/lang/Object;)V
        //    16: astore          propSetter
        //    18: aload_3         /* appender */
        //    19: aload_0         /* this */
        //    20: aload_2         /* appenderElement */
        //    21: ldc             "name"
        //    23: invokeinterface org/w3c/dom/Element.getAttribute:(Ljava/lang/String;)Ljava/lang/String;
        //    28: invokespecial   org/apache/log4j/xml/XmlConfiguration.subst:(Ljava/lang/String;)Ljava/lang/String;
        //    31: invokeinterface org/apache/log4j/Appender.setName:(Ljava/lang/String;)V
        //    36: new             Ljava/util/concurrent/atomic/AtomicReference;
        //    39: dup            
        //    40: invokespecial   java/util/concurrent/atomic/AtomicReference.<init>:()V
        //    43: astore          filterChain
        //    45: aload_2         /* appenderElement */
        //    46: invokeinterface org/w3c/dom/Element.getChildNodes:()Lorg/w3c/dom/NodeList;
        //    51: aload_0         /* this */
        //    52: aload           propSetter
        //    54: aload_3         /* appender */
        //    55: aload           filterChain
        //    57: invokedynamic   BootstrapMethod #0, accept:(Lorg/apache/log4j/xml/XmlConfiguration;Lorg/apache/log4j/config/PropertySetter;Lorg/apache/log4j/Appender;Ljava/util/concurrent/atomic/AtomicReference;)Ljava/util/function/Consumer;
        //    62: invokestatic    org/apache/log4j/xml/XmlConfiguration.forEachElement:(Lorg/w3c/dom/NodeList;Ljava/util/function/Consumer;)V
        //    65: aload           filterChain
        //    67: invokevirtual   java/util/concurrent/atomic/AtomicReference.get:()Ljava/lang/Object;
        //    70: checkcast       Lorg/apache/log4j/spi/Filter;
        //    73: astore          head
        //    75: aload           head
        //    77: ifnull          88
        //    80: aload_3         /* appender */
        //    81: aload           head
        //    83: invokeinterface org/apache/log4j/Appender.addFilter:(Lorg/apache/log4j/spi/Filter;)V
        //    88: aload           propSetter
        //    90: invokevirtual   org/apache/log4j/config/PropertySetter.activate:()V
        //    93: aload_3         /* appender */
        //    94: areturn        
        //    95: astore_3        /* appender */
        //    96: aload_3         /* ex */
        //    97: invokevirtual   org/apache/log4j/xml/XmlConfiguration$ConsumerException.getCause:()Ljava/lang/Throwable;
        //   100: astore          t
        //   102: aload           t
        //   104: instanceof      Ljava/lang/InterruptedException;
        //   107: ifne            118
        //   110: aload           t
        //   112: instanceof      Ljava/io/InterruptedIOException;
        //   115: ifeq            124
        //   118: invokestatic    java/lang/Thread.currentThread:()Ljava/lang/Thread;
        //   121: invokevirtual   java/lang/Thread.interrupt:()V
        //   124: getstatic       org/apache/log4j/xml/XmlConfiguration.LOGGER:Lorg/apache/logging/log4j/Logger;
        //   127: ldc             "Could not create an Appender. Reported error follows."
        //   129: aload           t
        //   131: invokeinterface org/apache/logging/log4j/Logger.error:(Ljava/lang/String;Ljava/lang/Throwable;)V
        //   136: goto            171
        //   139: astore_3        /* oops */
        //   140: aload_3         /* oops */
        //   141: instanceof      Ljava/lang/InterruptedException;
        //   144: ifne            154
        //   147: aload_3         /* oops */
        //   148: instanceof      Ljava/io/InterruptedIOException;
        //   151: ifeq            160
        //   154: invokestatic    java/lang/Thread.currentThread:()Ljava/lang/Thread;
        //   157: invokevirtual   java/lang/Thread.interrupt:()V
        //   160: getstatic       org/apache/log4j/xml/XmlConfiguration.LOGGER:Lorg/apache/logging/log4j/Logger;
        //   163: ldc             "Could not create an Appender. Reported error follows."
        //   165: aload_3         /* oops */
        //   166: invokeinterface org/apache/logging/log4j/Logger.error:(Ljava/lang/String;Ljava/lang/Throwable;)V
        //   171: aconst_null    
        //   172: areturn        
        //    MethodParameters:
        //  Name             Flags  
        //  ---------------  -----
        //  className        
        //  appenderElement  
        //    StackMapTable: 00 08 FF 00 58 00 07 07 01 67 07 01 8E 07 01 8B 07 01 9C 07 01 90 07 01 AE 07 01 AF 00 00 FF 00 06 00 03 07 01 67 07 01 8E 07 01 8B 00 01 07 01 B0 FD 00 16 07 01 B0 07 01 B1 05 FF 00 0E 00 03 07 01 67 07 01 8E 07 01 8B 00 01 07 01 5D FC 00 0E 07 01 5D 05 FA 00 0A
        //    Exceptions:
        //  Try           Handler
        //  Start  End    Start  End    Type                                                     
        //  -----  -----  -----  -----  ---------------------------------------------------------
        //  0      94     95     139    Lorg/apache/log4j/xml/XmlConfiguration$ConsumerException;
        //  0      94     139    171    Ljava/lang/Exception;
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
    
    public RewritePolicy parseRewritePolicy(final Element rewritePolicyElement) {
        final String className2 = this.subst(rewritePolicyElement.getAttribute("class"));
        XmlConfiguration.LOGGER.debug("Class name: [" + className2 + ']');
        RewritePolicy policy = this.manager.parseRewritePolicy(className2, rewritePolicyElement, this);
        if (policy == null) {
            policy = this.buildRewritePolicy(className2, rewritePolicyElement);
        }
        return policy;
    }
    
    private RewritePolicy buildRewritePolicy(final String className, final Element element) {
        try {
            final RewritePolicy policy = LoaderUtil.newInstanceOf(className);
            final PropertySetter propSetter2 = new PropertySetter(policy);
            forEachElement(element.getChildNodes(), currentElement -> {
                if (currentElement.getTagName().equalsIgnoreCase("param")) {
                    this.setParameter(currentElement, propSetter);
                }
                return;
            });
            propSetter2.activate();
            return policy;
        }
        catch (final ConsumerException ex2) {
            final Throwable t2 = ex2.getCause();
            if (t2 instanceof InterruptedException || t2 instanceof InterruptedIOException) {
                Thread.currentThread().interrupt();
            }
            XmlConfiguration.LOGGER.error("Could not create an RewritePolicy. Reported error follows.", t2);
        }
        catch (final Exception oops2) {
            if (oops2 instanceof InterruptedException || oops2 instanceof InterruptedIOException) {
                Thread.currentThread().interrupt();
            }
            XmlConfiguration.LOGGER.error("Could not create an RewritePolicy. Reported error follows.", oops2);
        }
        return null;
    }
    
    private void parseErrorHandler(final Element element, final Appender appender) {
        final org.apache.log4j.spi.ErrorHandler eh = (org.apache.log4j.spi.ErrorHandler)OptionConverter.instantiateByClassName(this.subst(element.getAttribute("class")), org.apache.log4j.spi.ErrorHandler.class, null);
        if (eh != null) {
            eh.setAppender(appender);
            final PropertySetter propSetter2 = new PropertySetter(eh);
            forEachElement(element.getChildNodes(), currentElement -> {
                final String tagName = currentElement.getTagName();
                if (tagName.equals("param")) {
                    this.setParameter(currentElement, propSetter);
                }
                return;
            });
            propSetter2.activate();
            appender.setErrorHandler(eh);
        }
    }
    
    public void addFilter(final AtomicReference<Filter> ref, final Element filterElement) {
        final Filter value = this.parseFilters(filterElement);
        ref.accumulateAndGet(value, FilterAdapter::addFilter);
    }
    
    public Filter parseFilters(final Element filterElement) {
        final String className2 = this.subst(filterElement.getAttribute("class"));
        XmlConfiguration.LOGGER.debug("Class name: [" + className2 + ']');
        Filter filter = this.manager.parseFilter(className2, filterElement, this);
        if (filter == null) {
            filter = this.buildFilter(className2, filterElement);
        }
        return filter;
    }
    
    private Filter buildFilter(final String className, final Element filterElement) {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     1: invokestatic    org/apache/logging/log4j/util/LoaderUtil.newInstanceOf:(Ljava/lang/String;)Ljava/lang/Object;
        //     4: checkcast       Lorg/apache/log4j/spi/Filter;
        //     7: astore_3        /* filter */
        //     8: new             Lorg/apache/log4j/config/PropertySetter;
        //    11: dup            
        //    12: aload_3         /* filter */
        //    13: invokespecial   org/apache/log4j/config/PropertySetter.<init>:(Ljava/lang/Object;)V
        //    16: astore          propSetter
        //    18: aload_2         /* filterElement */
        //    19: invokeinterface org/w3c/dom/Element.getChildNodes:()Lorg/w3c/dom/NodeList;
        //    24: aload_0         /* this */
        //    25: aload           propSetter
        //    27: invokedynamic   BootstrapMethod #4, accept:(Lorg/apache/log4j/xml/XmlConfiguration;Lorg/apache/log4j/config/PropertySetter;)Ljava/util/function/Consumer;
        //    32: invokestatic    org/apache/log4j/xml/XmlConfiguration.forEachElement:(Lorg/w3c/dom/NodeList;Ljava/util/function/Consumer;)V
        //    35: aload           propSetter
        //    37: invokevirtual   org/apache/log4j/config/PropertySetter.activate:()V
        //    40: aload_3         /* filter */
        //    41: areturn        
        //    42: astore_3        /* filter */
        //    43: aload_3         /* ex */
        //    44: invokevirtual   org/apache/log4j/xml/XmlConfiguration$ConsumerException.getCause:()Ljava/lang/Throwable;
        //    47: astore          t
        //    49: aload           t
        //    51: instanceof      Ljava/lang/InterruptedException;
        //    54: ifne            65
        //    57: aload           t
        //    59: instanceof      Ljava/io/InterruptedIOException;
        //    62: ifeq            71
        //    65: invokestatic    java/lang/Thread.currentThread:()Ljava/lang/Thread;
        //    68: invokevirtual   java/lang/Thread.interrupt:()V
        //    71: getstatic       org/apache/log4j/xml/XmlConfiguration.LOGGER:Lorg/apache/logging/log4j/Logger;
        //    74: ldc             "Could not create an Filter. Reported error follows."
        //    76: aload           t
        //    78: invokeinterface org/apache/logging/log4j/Logger.error:(Ljava/lang/String;Ljava/lang/Throwable;)V
        //    83: goto            118
        //    86: astore_3        /* oops */
        //    87: aload_3         /* oops */
        //    88: instanceof      Ljava/lang/InterruptedException;
        //    91: ifne            101
        //    94: aload_3         /* oops */
        //    95: instanceof      Ljava/io/InterruptedIOException;
        //    98: ifeq            107
        //   101: invokestatic    java/lang/Thread.currentThread:()Ljava/lang/Thread;
        //   104: invokevirtual   java/lang/Thread.interrupt:()V
        //   107: getstatic       org/apache/log4j/xml/XmlConfiguration.LOGGER:Lorg/apache/logging/log4j/Logger;
        //   110: ldc             "Could not create an Filter. Reported error follows."
        //   112: aload_3         /* oops */
        //   113: invokeinterface org/apache/logging/log4j/Logger.error:(Ljava/lang/String;Ljava/lang/Throwable;)V
        //   118: aconst_null    
        //   119: areturn        
        //    MethodParameters:
        //  Name           Flags  
        //  -------------  -----
        //  className      FINAL
        //  filterElement  FINAL
        //    StackMapTable: 00 07 6A 07 01 B0 FD 00 16 07 01 B0 07 01 B1 05 FF 00 0E 00 03 07 01 67 07 01 8E 07 01 8B 00 01 07 01 5D FC 00 0E 07 01 5D 05 FA 00 0A
        //    Exceptions:
        //  Try           Handler
        //  Start  End    Start  End    Type                                                     
        //  -----  -----  -----  -----  ---------------------------------------------------------
        //  0      41     42     86     Lorg/apache/log4j/xml/XmlConfiguration$ConsumerException;
        //  0      41     86     118    Ljava/lang/Exception;
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
    
    private void parseCategory(final Element loggerElement) {
        final String catName = this.subst(loggerElement.getAttribute("name"));
        final boolean additivity = OptionConverter.toBoolean(this.subst(loggerElement.getAttribute("additivity")), true);
        LoggerConfig loggerConfig = this.getLogger(catName);
        if (loggerConfig == null) {
            loggerConfig = new LoggerConfig(catName, Level.ERROR, additivity);
            this.addLogger(catName, loggerConfig);
        }
        else {
            loggerConfig.setAdditive(additivity);
        }
        this.parseChildrenOfLoggerElement(loggerElement, loggerConfig, false);
    }
    
    private void parseRoot(final Element rootElement) {
        final LoggerConfig root = this.getRootLogger();
        this.parseChildrenOfLoggerElement(rootElement, root, true);
    }
    
    private void parseChildrenOfLoggerElement(final Element catElement, final LoggerConfig loggerConfig, final boolean isRoot) {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     3: dup            
        //     4: aload_2         /* loggerConfig */
        //     5: invokespecial   org/apache/log4j/config/PropertySetter.<init>:(Ljava/lang/Object;)V
        //     8: astore          propSetter
        //    10: aload_2         /* loggerConfig */
        //    11: invokevirtual   org/apache/logging/log4j/core/config/LoggerConfig.getAppenderRefs:()Ljava/util/List;
        //    14: invokeinterface java/util/List.clear:()V
        //    19: aload_1         /* catElement */
        //    20: invokeinterface org/w3c/dom/Element.getChildNodes:()Lorg/w3c/dom/NodeList;
        //    25: aload_0         /* this */
        //    26: aload_2         /* loggerConfig */
        //    27: iload_3         /* isRoot */
        //    28: aload           propSetter
        //    30: invokedynamic   BootstrapMethod #5, accept:(Lorg/apache/log4j/xml/XmlConfiguration;Lorg/apache/logging/log4j/core/config/LoggerConfig;ZLorg/apache/log4j/config/PropertySetter;)Ljava/util/function/Consumer;
        //    35: invokestatic    org/apache/log4j/xml/XmlConfiguration.forEachElement:(Lorg/w3c/dom/NodeList;Ljava/util/function/Consumer;)V
        //    38: aload           propSetter
        //    40: invokevirtual   org/apache/log4j/config/PropertySetter.activate:()V
        //    43: return         
        //    MethodParameters:
        //  Name          Flags  
        //  ------------  -----
        //  catElement    
        //  loggerConfig  
        //  isRoot        
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
    
    public Layout parseLayout(final Element layoutElement) {
        final String className2 = this.subst(layoutElement.getAttribute("class"));
        XmlConfiguration.LOGGER.debug("Parsing layout of class: \"{}\"", className2);
        Layout layout = this.manager.parseLayout(className2, layoutElement, this);
        if (layout == null) {
            layout = this.buildLayout(className2, layoutElement);
        }
        return layout;
    }
    
    private Layout buildLayout(final String className, final Element layout_element) {
        try {
            final Layout layout = LoaderUtil.newInstanceOf(className);
            final PropertySetter propSetter4 = new PropertySetter(layout);
            forEachElement(layout_element.getChildNodes(), currentElement -> {
                final String tagName = currentElement.getTagName();
                if (tagName.equals("param")) {
                    this.setParameter(currentElement, propSetter);
                }
                else {
                    try {
                        this.parseUnrecognizedElement(layout, currentElement, this.props);
                    }
                    catch (final Exception ex3) {
                        throw new ConsumerException(ex3);
                    }
                }
                return;
            });
            propSetter4.activate();
            return layout;
        }
        catch (final Exception e) {
            final Throwable cause = e.getCause();
            if (e instanceof InterruptedException || e instanceof InterruptedIOException || cause instanceof InterruptedException || cause instanceof InterruptedIOException) {
                Thread.currentThread().interrupt();
            }
            XmlConfiguration.LOGGER.error("Could not create the Layout. Reported error follows.", e);
            return null;
        }
    }
    
    public TriggeringPolicy parseTriggeringPolicy(final Element policyElement) {
        final String className2 = this.subst(policyElement.getAttribute("class"));
        XmlConfiguration.LOGGER.debug("Parsing triggering policy of class: \"{}\"", className2);
        return this.manager.parseTriggeringPolicy(className2, policyElement, this);
    }
    
    private void parseLevel(final Element element, final LoggerConfig logger, final boolean isRoot) {
        String catName = logger.getName();
        if (isRoot) {
            catName = "root";
        }
        final String priStr = this.subst(element.getAttribute("value"));
        XmlConfiguration.LOGGER.debug("Level value for {} is [{}].", catName, priStr);
        if ("inherited".equalsIgnoreCase(priStr) || "null".equalsIgnoreCase(priStr)) {
            if (isRoot) {
                XmlConfiguration.LOGGER.error("Root level cannot be inherited. Ignoring directive.");
            }
            else {
                logger.setLevel(null);
            }
        }
        else {
            final String className2 = this.subst(element.getAttribute("class"));
            org.apache.log4j.Level level;
            if ("".equals(className2)) {
                level = OptionConverter.toLevel(priStr, XmlConfiguration.DEFAULT_LEVEL);
            }
            else {
                level = OptionConverter.toLevel(className2, priStr, XmlConfiguration.DEFAULT_LEVEL);
            }
            logger.setLevel((level != null) ? level.getVersion2Level() : null);
        }
        XmlConfiguration.LOGGER.debug("{} level set to {}", catName, logger.getLevel());
    }
    
    private void setParameter(final Element element, final PropertySetter propSetter) {
        final String name = this.subst(element.getAttribute("name"));
        String value = element.getAttribute("value");
        value = this.subst(OptionConverter.convertSpecialChars(value));
        propSetter.setProperty(name, value);
    }
    
    private void parse(final Element element) {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     1: invokeinterface org/w3c/dom/Element.getTagName:()Ljava/lang/String;
        //     6: astore_2        /* rootElementName */
        //     7: aload_2         /* rootElementName */
        //     8: ldc             "log4j:configuration"
        //    10: invokevirtual   java/lang/String.equals:(Ljava/lang/Object;)Z
        //    13: ifne            59
        //    16: aload_2         /* rootElementName */
        //    17: ldc             "configuration"
        //    19: invokevirtual   java/lang/String.equals:(Ljava/lang/Object;)Z
        //    22: ifeq            48
        //    25: getstatic       org/apache/log4j/xml/XmlConfiguration.LOGGER:Lorg/apache/logging/log4j/Logger;
        //    28: ldc             "The <configuration> element has been deprecated."
        //    30: invokeinterface org/apache/logging/log4j/Logger.warn:(Ljava/lang/String;)V
        //    35: getstatic       org/apache/log4j/xml/XmlConfiguration.LOGGER:Lorg/apache/logging/log4j/Logger;
        //    38: ldc             "Use the <log4j:configuration> element instead."
        //    40: invokeinterface org/apache/logging/log4j/Logger.warn:(Ljava/lang/String;)V
        //    45: goto            59
        //    48: getstatic       org/apache/log4j/xml/XmlConfiguration.LOGGER:Lorg/apache/logging/log4j/Logger;
        //    51: ldc             "DOM element is - not a <log4j:configuration> element."
        //    53: invokeinterface org/apache/logging/log4j/Logger.error:(Ljava/lang/String;)V
        //    58: return         
        //    59: aload_0         /* this */
        //    60: aload_1         /* element */
        //    61: ldc             "debug"
        //    63: invokeinterface org/w3c/dom/Element.getAttribute:(Ljava/lang/String;)Ljava/lang/String;
        //    68: invokespecial   org/apache/log4j/xml/XmlConfiguration.subst:(Ljava/lang/String;)Ljava/lang/String;
        //    71: astore_3        /* debugAttrib */
        //    72: getstatic       org/apache/log4j/xml/XmlConfiguration.LOGGER:Lorg/apache/logging/log4j/Logger;
        //    75: new             Ljava/lang/StringBuilder;
        //    78: dup            
        //    79: invokespecial   java/lang/StringBuilder.<init>:()V
        //    82: ldc             "debug attribute= \""
        //    84: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //    87: aload_3         /* debugAttrib */
        //    88: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //    91: ldc             "\"."
        //    93: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //    96: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
        //    99: invokeinterface org/apache/logging/log4j/Logger.debug:(Ljava/lang/String;)V
        //   104: ldc             "error"
        //   106: astore          status
        //   108: aload_3         /* debugAttrib */
        //   109: ldc             ""
        //   111: invokevirtual   java/lang/String.equals:(Ljava/lang/Object;)Z
        //   114: ifne            146
        //   117: aload_3         /* debugAttrib */
        //   118: ldc             "null"
        //   120: invokevirtual   java/lang/String.equals:(Ljava/lang/Object;)Z
        //   123: ifne            146
        //   126: aload_3         /* debugAttrib */
        //   127: iconst_1       
        //   128: invokestatic    org/apache/log4j/helpers/OptionConverter.toBoolean:(Ljava/lang/String;Z)Z
        //   131: ifeq            139
        //   134: ldc             "debug"
        //   136: goto            141
        //   139: ldc             "error"
        //   141: astore          status
        //   143: goto            156
        //   146: getstatic       org/apache/log4j/xml/XmlConfiguration.LOGGER:Lorg/apache/logging/log4j/Logger;
        //   149: ldc             "Ignoring debug attribute."
        //   151: invokeinterface org/apache/logging/log4j/Logger.debug:(Ljava/lang/String;)V
        //   156: aload_0         /* this */
        //   157: aload_1         /* element */
        //   158: ldc             "configDebug"
        //   160: invokeinterface org/w3c/dom/Element.getAttribute:(Ljava/lang/String;)Ljava/lang/String;
        //   165: invokespecial   org/apache/log4j/xml/XmlConfiguration.subst:(Ljava/lang/String;)Ljava/lang/String;
        //   168: astore          confDebug
        //   170: aload           confDebug
        //   172: ldc             ""
        //   174: invokevirtual   java/lang/String.equals:(Ljava/lang/Object;)Z
        //   177: ifne            228
        //   180: aload           confDebug
        //   182: ldc             "null"
        //   184: invokevirtual   java/lang/String.equals:(Ljava/lang/Object;)Z
        //   187: ifne            228
        //   190: getstatic       org/apache/log4j/xml/XmlConfiguration.LOGGER:Lorg/apache/logging/log4j/Logger;
        //   193: ldc             "The \"configDebug\" attribute is deprecated."
        //   195: invokeinterface org/apache/logging/log4j/Logger.warn:(Ljava/lang/String;)V
        //   200: getstatic       org/apache/log4j/xml/XmlConfiguration.LOGGER:Lorg/apache/logging/log4j/Logger;
        //   203: ldc             "Use the \"debug\" attribute instead."
        //   205: invokeinterface org/apache/logging/log4j/Logger.warn:(Ljava/lang/String;)V
        //   210: aload           confDebug
        //   212: iconst_1       
        //   213: invokestatic    org/apache/log4j/helpers/OptionConverter.toBoolean:(Ljava/lang/String;Z)Z
        //   216: ifeq            224
        //   219: ldc             "debug"
        //   221: goto            226
        //   224: ldc             "error"
        //   226: astore          status
        //   228: new             Lorg/apache/logging/log4j/core/config/status/StatusConfiguration;
        //   231: dup            
        //   232: invokespecial   org/apache/logging/log4j/core/config/status/StatusConfiguration.<init>:()V
        //   235: aload           status
        //   237: invokevirtual   org/apache/logging/log4j/core/config/status/StatusConfiguration.withStatus:(Ljava/lang/String;)Lorg/apache/logging/log4j/core/config/status/StatusConfiguration;
        //   240: astore          statusConfig
        //   242: aload           statusConfig
        //   244: invokevirtual   org/apache/logging/log4j/core/config/status/StatusConfiguration.initialize:()V
        //   247: aload_0         /* this */
        //   248: aload_1         /* element */
        //   249: ldc             "threshold"
        //   251: invokeinterface org/w3c/dom/Element.getAttribute:(Ljava/lang/String;)Ljava/lang/String;
        //   256: invokespecial   org/apache/log4j/xml/XmlConfiguration.subst:(Ljava/lang/String;)Ljava/lang/String;
        //   259: astore          threshold
        //   261: aload           threshold
        //   263: ifnull          294
        //   266: aload           threshold
        //   268: invokevirtual   java/lang/String.trim:()Ljava/lang/String;
        //   271: getstatic       org/apache/logging/log4j/Level.ALL:Lorg/apache/logging/log4j/Level;
        //   274: invokestatic    org/apache/log4j/helpers/OptionConverter.convertLevel:(Ljava/lang/String;Lorg/apache/logging/log4j/Level;)Lorg/apache/logging/log4j/Level;
        //   277: astore          level
        //   279: aload_0         /* this */
        //   280: aload           level
        //   282: getstatic       org/apache/logging/log4j/core/Filter$Result.NEUTRAL:Lorg/apache/logging/log4j/core/Filter$Result;
        //   285: getstatic       org/apache/logging/log4j/core/Filter$Result.DENY:Lorg/apache/logging/log4j/core/Filter$Result;
        //   288: invokestatic    org/apache/logging/log4j/core/filter/ThresholdFilter.createFilter:(Lorg/apache/logging/log4j/Level;Lorg/apache/logging/log4j/core/Filter$Result;Lorg/apache/logging/log4j/core/Filter$Result;)Lorg/apache/logging/log4j/core/filter/ThresholdFilter;
        //   291: invokevirtual   org/apache/log4j/xml/XmlConfiguration.addFilter:(Lorg/apache/logging/log4j/core/Filter;)V
        //   294: aload_1         /* element */
        //   295: invokeinterface org/w3c/dom/Element.getChildNodes:()Lorg/w3c/dom/NodeList;
        //   300: aload_0         /* this */
        //   301: invokedynamic   BootstrapMethod #7, accept:(Lorg/apache/log4j/xml/XmlConfiguration;)Ljava/util/function/Consumer;
        //   306: invokestatic    org/apache/log4j/xml/XmlConfiguration.forEachElement:(Lorg/w3c/dom/NodeList;Ljava/util/function/Consumer;)V
        //   309: return         
        //    MethodParameters:
        //  Name     Flags  
        //  -------  -----
        //  element  
        //    StackMapTable: 00 0A FC 00 30 07 01 8E 0A FD 00 4F 07 01 8E 07 01 8E 41 07 01 8E 04 09 FC 00 43 07 01 8E 41 07 01 8E 01 FD 00 41 07 01 F6 07 01 8E
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
    
    private String subst(final String value) {
        return this.getStrSubstitutor().replace(value);
    }
    
    public static void forEachElement(final NodeList list, final Consumer<Element> consumer) {
        IntStream.range(0, list.getLength()).mapToObj((IntFunction<?>)list::item).filter(node -> node.getNodeType() == 1).forEach(node -> consumer.accept(node));
    }
    
    static {
        LOGGER = StatusLogger.getLogger();
        ONE_STRING_PARAM = new Class[] { String.class };
    }
    
    private static class SAXErrorHandler implements ErrorHandler
    {
        private static final Logger LOGGER;
        
        @Override
        public void error(final SAXParseException ex) {
            emitMessage("Continuable parsing error ", ex);
        }
        
        @Override
        public void fatalError(final SAXParseException ex) {
            emitMessage("Fatal parsing error ", ex);
        }
        
        @Override
        public void warning(final SAXParseException ex) {
            emitMessage("Parsing warning ", ex);
        }
        
        private static void emitMessage(final String msg, final SAXParseException ex) {
            SAXErrorHandler.LOGGER.warn("{} {} and column {}", msg, ex.getLineNumber(), ex.getColumnNumber());
            SAXErrorHandler.LOGGER.warn(ex.getMessage(), ex.getException());
        }
        
        static {
            LOGGER = StatusLogger.getLogger();
        }
    }
    
    private static class ConsumerException extends RuntimeException
    {
        ConsumerException(final Exception ex) {
            super(ex);
        }
    }
    
    private interface ParseAction
    {
        Document parse(final DocumentBuilder parser) throws SAXException, IOException;
    }
}
