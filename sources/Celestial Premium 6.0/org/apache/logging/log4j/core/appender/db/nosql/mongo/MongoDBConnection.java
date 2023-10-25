/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.mongodb.BasicDBObject
 *  com.mongodb.DB
 *  com.mongodb.DBCollection
 *  com.mongodb.DBObject
 *  com.mongodb.Mongo
 *  com.mongodb.MongoException
 *  com.mongodb.WriteConcern
 *  com.mongodb.WriteResult
 *  org.bson.BSON
 *  org.bson.Transformer
 */
package org.apache.logging.log4j.core.appender.db.nosql.mongo;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.Mongo;
import com.mongodb.MongoException;
import com.mongodb.WriteConcern;
import com.mongodb.WriteResult;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.appender.AppenderLoggingException;
import org.apache.logging.log4j.core.appender.db.nosql.NoSQLConnection;
import org.apache.logging.log4j.core.appender.db.nosql.NoSQLObject;
import org.apache.logging.log4j.core.appender.db.nosql.mongo.MongoDBObject;
import org.apache.logging.log4j.status.StatusLogger;
import org.bson.BSON;
import org.bson.Transformer;

public final class MongoDBConnection
implements NoSQLConnection<BasicDBObject, MongoDBObject> {
    private static final Logger LOGGER = StatusLogger.getLogger();
    private final DBCollection collection;
    private final Mongo mongo;
    private final WriteConcern writeConcern;

    public MongoDBConnection(DB database, WriteConcern writeConcern, String collectionName) {
        this.mongo = database.getMongo();
        this.collection = database.getCollection(collectionName);
        this.writeConcern = writeConcern;
    }

    @Override
    public MongoDBObject createObject() {
        return new MongoDBObject();
    }

    public MongoDBObject[] createList(int length) {
        return new MongoDBObject[length];
    }

    @Override
    public void insertObject(NoSQLObject<BasicDBObject> object) {
        try {
            WriteResult result = this.collection.insert((DBObject)object.unwrap(), this.writeConcern);
            if (result.getError() != null && result.getError().length() > 0) {
                throw new AppenderLoggingException("Failed to write log event to MongoDB due to error: " + result.getError() + ".");
            }
        }
        catch (MongoException e) {
            throw new AppenderLoggingException("Failed to write log event to MongoDB due to error: " + e.getMessage(), e);
        }
    }

    @Override
    public void close() {
        this.mongo.close();
    }

    @Override
    public boolean isClosed() {
        return !this.mongo.getConnector().isOpen();
    }

    static void authenticate(DB database, String username, String password) {
        try {
            if (!database.authenticate(username, password.toCharArray())) {
                LOGGER.error("Failed to authenticate against MongoDB server. Unknown error.");
            }
        }
        catch (MongoException e) {
            LOGGER.error("Failed to authenticate against MongoDB: " + e.getMessage(), (Throwable)e);
        }
        catch (IllegalStateException e) {
            LOGGER.error("Factory-supplied MongoDB database connection already authenticated with differentcredentials but lost connection.");
        }
    }

    static {
        BSON.addDecodingHook(Level.class, (Transformer)new Transformer(){

            public Object transform(Object o) {
                if (o instanceof Level) {
                    return ((Level)((Object)o)).name();
                }
                return o;
            }
        });
    }
}

