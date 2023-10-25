/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.mongodb.DB
 *  com.mongodb.MongoClient
 *  com.mongodb.ServerAddress
 *  com.mongodb.WriteConcern
 */
package org.apache.logging.log4j.core.appender.db.nosql.mongo;

import com.mongodb.DB;
import com.mongodb.MongoClient;
import com.mongodb.ServerAddress;
import com.mongodb.WriteConcern;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.appender.AbstractAppender;
import org.apache.logging.log4j.core.appender.db.nosql.NoSQLProvider;
import org.apache.logging.log4j.core.appender.db.nosql.mongo.MongoDBConnection;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.config.plugins.PluginAttribute;
import org.apache.logging.log4j.core.config.plugins.PluginFactory;
import org.apache.logging.log4j.core.helpers.NameUtil;
import org.apache.logging.log4j.status.StatusLogger;

@Plugin(name="MongoDb", category="Core", printObject=true)
public final class MongoDBProvider
implements NoSQLProvider<MongoDBConnection> {
    private static final Logger LOGGER = StatusLogger.getLogger();
    private final String collectionName;
    private final DB database;
    private final String description;
    private final WriteConcern writeConcern;

    private MongoDBProvider(DB database, WriteConcern writeConcern, String collectionName, String description) {
        this.database = database;
        this.writeConcern = writeConcern;
        this.collectionName = collectionName;
        this.description = "mongoDb{ " + description + " }";
    }

    @Override
    public MongoDBConnection getConnection() {
        return new MongoDBConnection(this.database, this.writeConcern, this.collectionName);
    }

    @Override
    public String toString() {
        return this.description;
    }

    /*
     * Unable to fully structure code
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     * Lifted jumps to return sites
     */
    @PluginFactory
    public static MongoDBProvider createNoSQLProvider(@PluginAttribute(value="collectionName") String collectionName, @PluginAttribute(value="writeConcernConstant") String writeConcernConstant, @PluginAttribute(value="writeConcernConstantClass") String writeConcernConstantClassName, @PluginAttribute(value="databaseName") String databaseName, @PluginAttribute(value="server") String server, @PluginAttribute(value="port") String port, @PluginAttribute(value="username") String username, @PluginAttribute(value="password") String password, @PluginAttribute(value="factoryClassName") String factoryClassName, @PluginAttribute(value="factoryMethodName") String factoryMethodName) {
        block22: {
            if (factoryClassName == null || factoryClassName.length() <= 0 || factoryMethodName == null || factoryMethodName.length() <= 0) break block22;
            try {
                block24: {
                    block23: {
                        factoryClass = Class.forName(factoryClassName);
                        method = factoryClass.getMethod(factoryMethodName, new Class[0]);
                        object = method.invoke(null, new Object[0]);
                        if (!(object instanceof DB)) break block23;
                        database = (DB)object;
                        break block24;
                    }
                    if (!(object instanceof MongoClient)) ** GOTO lbl16
                    if (databaseName != null && databaseName.length() > 0) {
                        database = ((MongoClient)object).getDB(databaseName);
                    } else {
                        MongoDBProvider.LOGGER.error("The factory method [{}.{}()] returned a MongoClient so the database name is required.", new Object[]{factoryClassName, factoryMethodName});
                        return null;
lbl16:
                        // 1 sources

                        if (object == null) {
                            MongoDBProvider.LOGGER.error("The factory method [{}.{}()] returned null.", new Object[]{factoryClassName, factoryMethodName});
                            return null;
                        }
                        MongoDBProvider.LOGGER.error("The factory method [{}.{}()] returned an unsupported type [{}].", new Object[]{factoryClassName, factoryMethodName, object.getClass().getName()});
                        return null;
                    }
                }
                description = "database=" + database.getName();
                addresses = database.getMongo().getAllAddress();
                if (addresses.size() == 1) {
                    description = description + ", server=" + ((ServerAddress)addresses.get(0)).getHost() + ", port=" + ((ServerAddress)addresses.get(0)).getPort();
                }
                description = description + ", servers=[";
                for (ServerAddress address : addresses) {
                    description = description + " { " + address.getHost() + ", " + address.getPort() + " } ";
                }
                description = description + "]";
            }
            catch (ClassNotFoundException e) {
                MongoDBProvider.LOGGER.error("The factory class [{}] could not be loaded.", new Object[]{factoryClassName, e});
                return null;
            }
            catch (NoSuchMethodException e) {
                MongoDBProvider.LOGGER.error("The factory class [{}] does not have a no-arg method named [{}].", new Object[]{factoryClassName, factoryMethodName, e});
                return null;
            }
            catch (Exception e) {
                MongoDBProvider.LOGGER.error("The factory method [{}.{}()] could not be invoked.", new Object[]{factoryClassName, factoryMethodName, e});
                return null;
            }
        }
        if (databaseName != null && databaseName.length() > 0) {
            description = "database=" + databaseName;
            try {
                if (server != null && server.length() > 0) {
                    portInt = AbstractAppender.parseInt(port, 0);
                    description = description + ", server=" + server;
                    if (portInt > 0) {
                        description = description + ", port=" + portInt;
                        database = new MongoClient(server, portInt).getDB(databaseName);
                    }
                    database = new MongoClient(server).getDB(databaseName);
                }
                database = new MongoClient().getDB(databaseName);
            }
            catch (Exception e) {
                MongoDBProvider.LOGGER.error("Failed to obtain a database instance from the MongoClient at server [{}] and port [{}].", new Object[]{server, port});
                return null;
            }
        } else {
            MongoDBProvider.LOGGER.error("No factory method was provided so the database name is required.");
            return null;
        }
        if (!database.isAuthenticated()) {
            if (username != null && username.length() > 0 && password != null && password.length() > 0) {
                description = description + ", username=" + username + ", passwordHash=" + NameUtil.md5(password + MongoDBProvider.class.getName());
                MongoDBConnection.authenticate(database, username, password);
            } else {
                MongoDBProvider.LOGGER.error("The database is not already authenticated so you must supply a username and password for the MongoDB provider.");
                return null;
            }
        }
        if (writeConcernConstant != null && writeConcernConstant.length() > 0) {
            if (writeConcernConstantClassName != null && writeConcernConstantClassName.length() > 0) {
                try {
                    writeConcernConstantClass = Class.forName(writeConcernConstantClassName);
                    field = writeConcernConstantClass.getField(writeConcernConstant);
                    writeConcern = (WriteConcern)field.get(null);
                    return new MongoDBProvider(database, writeConcern, collectionName, description);
                }
                catch (Exception e) {
                    MongoDBProvider.LOGGER.error("Write concern constant [{}.{}] not found, using default.", new Object[]{writeConcernConstantClassName, writeConcernConstant});
                    writeConcern = WriteConcern.ACKNOWLEDGED;
                    return new MongoDBProvider(database, writeConcern, collectionName, description);
                }
            }
            writeConcern = WriteConcern.valueOf((String)writeConcernConstant);
            if (writeConcern != null) return new MongoDBProvider(database, writeConcern, collectionName, description);
            MongoDBProvider.LOGGER.warn("Write concern constant [{}] not found, using default.", new Object[]{writeConcernConstant});
            writeConcern = WriteConcern.ACKNOWLEDGED;
            return new MongoDBProvider(database, writeConcern, collectionName, description);
        }
        writeConcern = WriteConcern.ACKNOWLEDGED;
        return new MongoDBProvider(database, writeConcern, collectionName, description);
    }
}

