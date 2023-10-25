/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  javax.servlet.DispatcherType
 *  javax.servlet.Filter
 *  javax.servlet.FilterRegistration$Dynamic
 *  javax.servlet.ServletContainerInitializer
 *  javax.servlet.ServletContext
 *  javax.servlet.ServletException
 *  javax.servlet.UnavailableException
 */
package org.apache.logging.log4j.core.web;

import java.util.EnumSet;
import java.util.EventListener;
import java.util.Set;
import javax.servlet.DispatcherType;
import javax.servlet.Filter;
import javax.servlet.FilterRegistration;
import javax.servlet.ServletContainerInitializer;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.UnavailableException;
import org.apache.logging.log4j.core.web.Log4jServletContextListener;
import org.apache.logging.log4j.core.web.Log4jServletFilter;
import org.apache.logging.log4j.core.web.Log4jWebInitializer;
import org.apache.logging.log4j.core.web.Log4jWebInitializerImpl;

public class Log4jServletContainerInitializer
implements ServletContainerInitializer {
    public void onStartup(Set<Class<?>> classes, ServletContext servletContext) throws ServletException {
        if (servletContext.getMajorVersion() > 2) {
            servletContext.log("Log4jServletContainerInitializer starting up Log4j in Servlet 3.0+ environment.");
            Log4jWebInitializer initializer = Log4jWebInitializerImpl.getLog4jWebInitializer(servletContext);
            initializer.initialize();
            initializer.setLoggerContext();
            servletContext.addListener((EventListener)((Object)new Log4jServletContextListener()));
            FilterRegistration.Dynamic filter = servletContext.addFilter("log4jServletFilter", (Filter)new Log4jServletFilter());
            if (filter == null) {
                throw new UnavailableException("In a Servlet 3.0+ application, you must not define a log4jServletFilter in web.xml. Log4j 2 defines this for you automatically.");
            }
            filter.addMappingForUrlPatterns(EnumSet.allOf(DispatcherType.class), false, new String[]{"/*"});
        }
    }
}

