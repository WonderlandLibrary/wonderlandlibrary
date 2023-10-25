/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  org.lightcouch.CouchDbClient
 *  org.lightcouch.Response
 */
package org.apache.logging.log4j.core.appender.db.nosql.couch;

import java.util.Map;
import org.apache.logging.log4j.core.appender.AppenderLoggingException;
import org.apache.logging.log4j.core.appender.db.nosql.NoSQLConnection;
import org.apache.logging.log4j.core.appender.db.nosql.NoSQLObject;
import org.apache.logging.log4j.core.appender.db.nosql.couch.CouchDBObject;
import org.lightcouch.CouchDbClient;
import org.lightcouch.Response;

public final class CouchDBConnection
implements NoSQLConnection<Map<String, Object>, CouchDBObject> {
    private final CouchDbClient client;
    private boolean closed = false;

    public CouchDBConnection(CouchDbClient client) {
        this.client = client;
    }

    @Override
    public CouchDBObject createObject() {
        return new CouchDBObject();
    }

    public CouchDBObject[] createList(int length) {
        return new CouchDBObject[length];
    }

    @Override
    public void insertObject(NoSQLObject<Map<String, Object>> object) {
        try {
            Response response = this.client.save(object.unwrap());
            if (response.getError() != null && response.getError().length() > 0) {
                throw new AppenderLoggingException("Failed to write log event to CouchDB due to error: " + response.getError() + ".");
            }
        }
        catch (Exception e) {
            throw new AppenderLoggingException("Failed to write log event to CouchDB due to error: " + e.getMessage(), e);
        }
    }

    @Override
    public synchronized void close() {
        this.closed = true;
        this.client.shutdown();
    }

    @Override
    public synchronized boolean isClosed() {
        return this.closed;
    }
}

