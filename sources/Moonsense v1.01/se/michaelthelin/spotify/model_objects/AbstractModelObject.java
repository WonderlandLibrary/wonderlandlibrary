// 
// Decompiled by Procyon v0.5.36
// 

package se.michaelthelin.spotify.model_objects;

import se.michaelthelin.spotify.model_objects.specification.Cursor;
import se.michaelthelin.spotify.model_objects.specification.PagingCursorbased;
import se.michaelthelin.spotify.model_objects.specification.Paging;
import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import java.lang.reflect.Array;
import java.lang.reflect.ParameterizedType;
import com.google.gson.JsonArray;
import com.google.gson.JsonParser;
import com.google.gson.JsonObject;

public abstract class AbstractModelObject implements IModelObject
{
    protected AbstractModelObject(final Builder builder) {
        assert builder != null;
    }
    
    @Override
    public abstract String toString();
    
    public abstract static class Builder implements IModelObject.Builder
    {
    }
    
    public abstract static class JsonUtil<T> implements IJsonUtil<T>
    {
        @Override
        public boolean hasAndNotNull(final JsonObject jsonObject, final String memberName) {
            return jsonObject.has(memberName) && !jsonObject.get(memberName).isJsonNull();
        }
        
        @Override
        public T createModelObject(final String json) {
            if (json == null) {
                return null;
            }
            return this.createModelObject(JsonParser.parseString(json).getAsJsonObject());
        }
        
        @Override
        public T[] createModelObjectArray(final JsonArray jsonArray) {
            final T[] array = (T[])Array.newInstance((Class<?>)((ParameterizedType)this.getClass().getGenericSuperclass()).getActualTypeArguments()[0], jsonArray.size());
            for (int i = 0; i < jsonArray.size(); ++i) {
                final JsonElement jsonElement = jsonArray.get(i);
                if (jsonElement instanceof JsonNull) {
                    array[i] = null;
                }
                else {
                    final JsonObject jsonObject = jsonElement.getAsJsonObject();
                    array[i] = this.createModelObject(jsonObject);
                }
            }
            return array;
        }
        
        @Override
        public T[] createModelObjectArray(final String json) {
            return this.createModelObjectArray(JsonParser.parseString(json).getAsJsonArray());
        }
        
        @Override
        public T[] createModelObjectArray(final String json, final String key) {
            return this.createModelObjectArray(JsonParser.parseString(json).getAsJsonObject().get(key).getAsJsonArray());
        }
        
        @Override
        public <X> X[] createModelObjectArray(final JsonArray jsonArray, final Class<X> clazz) {
            final X[] array = (X[])Array.newInstance(clazz, jsonArray.size());
            for (int i = 0; i < jsonArray.size(); ++i) {
                final JsonElement jsonElement = jsonArray.get(i);
                final JsonObject jsonObject = jsonElement.getAsJsonObject();
                array[i] = (X)this.createModelObject(jsonObject);
            }
            return array;
        }
        
        @Override
        public Paging<T> createModelObjectPaging(final JsonObject jsonObject) {
            return new Paging.Builder<T>().setHref(this.hasAndNotNull(jsonObject, "href") ? jsonObject.get("href").getAsString() : null).setItems((T[])(this.hasAndNotNull(jsonObject, "items") ? this.createModelObjectArray(jsonObject.getAsJsonArray("items")) : null)).setLimit(this.hasAndNotNull(jsonObject, "limit") ? Integer.valueOf(jsonObject.get("limit").getAsInt()) : null).setNext(this.hasAndNotNull(jsonObject, "next") ? jsonObject.get("next").getAsString() : null).setOffset(this.hasAndNotNull(jsonObject, "offset") ? Integer.valueOf(jsonObject.get("offset").getAsInt()) : null).setPrevious(this.hasAndNotNull(jsonObject, "previous") ? jsonObject.get("previous").getAsString() : null).setTotal(this.hasAndNotNull(jsonObject, "total") ? Integer.valueOf(jsonObject.get("total").getAsInt()) : null).build();
        }
        
        @Override
        public Paging<T> createModelObjectPaging(final String json) {
            return this.createModelObjectPaging(JsonParser.parseString(json).getAsJsonObject());
        }
        
        @Override
        public Paging<T> createModelObjectPaging(final String json, final String key) {
            return this.createModelObjectPaging(JsonParser.parseString(json).getAsJsonObject().get(key).getAsJsonObject());
        }
        
        @Override
        public PagingCursorbased<T> createModelObjectPagingCursorbased(final JsonObject jsonObject) {
            return new PagingCursorbased.Builder<T>().setHref(this.hasAndNotNull(jsonObject, "href") ? jsonObject.get("href").getAsString() : null).setItems((T[])(this.hasAndNotNull(jsonObject, "items") ? this.createModelObjectArray(jsonObject.getAsJsonArray("items")) : null)).setLimit(this.hasAndNotNull(jsonObject, "limit") ? Integer.valueOf(jsonObject.get("limit").getAsInt()) : null).setNext(this.hasAndNotNull(jsonObject, "next") ? jsonObject.get("next").getAsString() : null).setCursors(this.hasAndNotNull(jsonObject, "cursors") ? new Cursor.JsonUtil().createModelObject(jsonObject.getAsJsonObject("cursors")) : null).setTotal(this.hasAndNotNull(jsonObject, "total") ? Integer.valueOf(jsonObject.get("total").getAsInt()) : null).build();
        }
        
        @Override
        public PagingCursorbased<T> createModelObjectPagingCursorbased(final String json) {
            return this.createModelObjectPagingCursorbased(JsonParser.parseString(json).getAsJsonObject());
        }
        
        @Override
        public PagingCursorbased<T> createModelObjectPagingCursorbased(final String json, final String key) {
            return this.createModelObjectPagingCursorbased(JsonParser.parseString(json).getAsJsonObject().get(key).getAsJsonObject());
        }
    }
}
