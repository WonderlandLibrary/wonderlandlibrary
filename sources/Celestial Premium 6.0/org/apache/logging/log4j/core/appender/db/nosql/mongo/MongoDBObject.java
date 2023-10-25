/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.mongodb.BasicDBList
 *  com.mongodb.BasicDBObject
 */
package org.apache.logging.log4j.core.appender.db.nosql.mongo;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import java.util.Collections;
import org.apache.logging.log4j.core.appender.db.nosql.NoSQLObject;

public final class MongoDBObject
implements NoSQLObject<BasicDBObject> {
    private final BasicDBObject mongoObject = new BasicDBObject();

    @Override
    public void set(String field, Object value) {
        this.mongoObject.append(field, value);
    }

    @Override
    public void set(String field, NoSQLObject<BasicDBObject> value) {
        this.mongoObject.append(field, (Object)value.unwrap());
    }

    @Override
    public void set(String field, Object[] values) {
        BasicDBList list = new BasicDBList();
        Collections.addAll(list, values);
        this.mongoObject.append(field, (Object)list);
    }

    @Override
    public void set(String field, NoSQLObject<BasicDBObject>[] values) {
        BasicDBList list = new BasicDBList();
        for (NoSQLObject<BasicDBObject> value : values) {
            list.add((Object)value.unwrap());
        }
        this.mongoObject.append(field, (Object)list);
    }

    @Override
    public BasicDBObject unwrap() {
        return this.mongoObject;
    }
}

