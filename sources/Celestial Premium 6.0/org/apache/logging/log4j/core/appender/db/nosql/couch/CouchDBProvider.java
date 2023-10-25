/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  org.lightcouch.CouchDbClient
 *  org.lightcouch.CouchDbProperties
 */
package org.apache.logging.log4j.core.appender.db.nosql.couch;

import java.lang.reflect.Method;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.appender.AbstractAppender;
import org.apache.logging.log4j.core.appender.db.nosql.NoSQLProvider;
import org.apache.logging.log4j.core.appender.db.nosql.couch.CouchDBConnection;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.config.plugins.PluginAttribute;
import org.apache.logging.log4j.core.config.plugins.PluginFactory;
import org.apache.logging.log4j.core.helpers.NameUtil;
import org.apache.logging.log4j.core.helpers.Strings;
import org.apache.logging.log4j.status.StatusLogger;
import org.lightcouch.CouchDbClient;
import org.lightcouch.CouchDbProperties;

@Plugin(name="CouchDb", category="Core", printObject=true)
public final class CouchDBProvider
implements NoSQLProvider<CouchDBConnection> {
    private static final int HTTP = 80;
    private static final int HTTPS = 443;
    private static final Logger LOGGER = StatusLogger.getLogger();
    private final CouchDbClient client;
    private final String description;

    private CouchDBProvider(CouchDbClient client, String description) {
        this.client = client;
        this.description = "couchDb{ " + description + " }";
    }

    @Override
    public CouchDBConnection getConnection() {
        return new CouchDBConnection(this.client);
    }

    @Override
    public String toString() {
        return this.description;
    }

    @PluginFactory
    public static CouchDBProvider createNoSQLProvider(@PluginAttribute(value="databaseName") String databaseName, @PluginAttribute(value="protocol") String protocol, @PluginAttribute(value="server") String server, @PluginAttribute(value="port") String port, @PluginAttribute(value="username") String username, @PluginAttribute(value="password") String password, @PluginAttribute(value="factoryClassName") String factoryClassName, @PluginAttribute(value="factoryMethodName") String factoryMethodName) {
        String description;
        CouchDbClient client;
        if (factoryClassName != null && factoryClassName.length() > 0 && factoryMethodName != null && factoryMethodName.length() > 0) {
            try {
                Class<?> factoryClass = Class.forName(factoryClassName);
                Method method = factoryClass.getMethod(factoryMethodName, new Class[0]);
                Object object = method.invoke(null, new Object[0]);
                if (object instanceof CouchDbClient) {
                    client = (CouchDbClient)object;
                    description = "uri=" + client.getDBUri();
                }
                if (object instanceof CouchDbProperties) {
                    CouchDbProperties properties = (CouchDbProperties)object;
                    client = new CouchDbClient(properties);
                    description = "uri=" + client.getDBUri() + ", username=" + properties.getUsername() + ", passwordHash=" + NameUtil.md5(password + CouchDBProvider.class.getName()) + ", maxConnections=" + properties.getMaxConnections() + ", connectionTimeout=" + properties.getConnectionTimeout() + ", socketTimeout=" + properties.getSocketTimeout();
                }
                if (object == null) {
                    LOGGER.error("The factory method [{}.{}()] returned null.", factoryClassName, factoryMethodName);
                    return null;
                }
                LOGGER.error("The factory method [{}.{}()] returned an unsupported type [{}].", factoryClassName, factoryMethodName, object.getClass().getName());
                return null;
            }
            catch (ClassNotFoundException e) {
                LOGGER.error("The factory class [{}] could not be loaded.", factoryClassName, e);
                return null;
            }
            catch (NoSuchMethodException e) {
                LOGGER.error("The factory class [{}] does not have a no-arg method named [{}].", factoryClassName, factoryMethodName, e);
                return null;
            }
            catch (Exception e) {
                LOGGER.error("The factory method [{}.{}()] could not be invoked.", factoryClassName, factoryMethodName, e);
                return null;
            }
        } else if (databaseName != null && databaseName.length() > 0) {
            if (protocol != null && protocol.length() > 0) {
                if (!(protocol = protocol.toLowerCase()).equals("http") && !protocol.equals("https")) {
                    LOGGER.error("Only protocols [http] and [https] are supported, [{}] specified.", protocol);
                    return null;
                }
            } else {
                protocol = "http";
                LOGGER.warn("No protocol specified, using default port [http].");
            }
            int portInt = AbstractAppender.parseInt(port, protocol.equals("https") ? 443 : 80);
            if (Strings.isEmpty(server)) {
                server = "localhost";
                LOGGER.warn("No server specified, using default server localhost.");
            }
            if (Strings.isEmpty(username) || Strings.isEmpty(password)) {
                LOGGER.error("You must provide a username and password for the CouchDB provider.");
                return null;
            }
            client = new CouchDbClient(databaseName, false, protocol, server, portInt, username, password);
            description = "uri=" + client.getDBUri() + ", username=" + username + ", passwordHash=" + NameUtil.md5(password + CouchDBProvider.class.getName());
        } else {
            LOGGER.error("No factory method was provided so the database name is required.");
            return null;
        }
        return new CouchDBProvider(client, description);
    }
}

