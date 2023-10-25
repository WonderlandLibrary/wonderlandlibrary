/*
 * Decompiled with CFR 0.150.
 */
package org.apache.http.impl.execchain;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;
import org.apache.http.HttpEntity;
import org.apache.http.HttpEntityEnclosingRequest;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.annotation.NotThreadSafe;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.impl.execchain.ConnectionHolder;
import org.apache.http.impl.execchain.RequestEntityExecHandler;
import org.apache.http.impl.execchain.ResponseProxyHandler;

@NotThreadSafe
class Proxies {
    Proxies() {
    }

    static void enhanceEntity(HttpEntityEnclosingRequest request) {
        HttpEntity entity = request.getEntity();
        if (entity != null && !entity.isRepeatable() && !Proxies.isEnhanced(entity)) {
            HttpEntity proxy = (HttpEntity)Proxy.newProxyInstance(HttpEntity.class.getClassLoader(), new Class[]{HttpEntity.class}, (InvocationHandler)new RequestEntityExecHandler(entity));
            request.setEntity(proxy);
        }
    }

    static boolean isEnhanced(HttpEntity entity) {
        if (entity != null && Proxy.isProxyClass(entity.getClass())) {
            InvocationHandler handler = Proxy.getInvocationHandler(entity);
            return handler instanceof RequestEntityExecHandler;
        }
        return false;
    }

    static boolean isRepeatable(HttpRequest request) {
        HttpEntity entity;
        if (request instanceof HttpEntityEnclosingRequest && (entity = ((HttpEntityEnclosingRequest)request).getEntity()) != null) {
            RequestEntityExecHandler handler;
            if (Proxies.isEnhanced(entity) && !(handler = (RequestEntityExecHandler)Proxy.getInvocationHandler(entity)).isConsumed()) {
                return true;
            }
            return entity.isRepeatable();
        }
        return true;
    }

    public static CloseableHttpResponse enhanceResponse(HttpResponse original, ConnectionHolder connHolder) {
        return (CloseableHttpResponse)Proxy.newProxyInstance(ResponseProxyHandler.class.getClassLoader(), new Class[]{CloseableHttpResponse.class}, (InvocationHandler)new ResponseProxyHandler(original, connHolder));
    }
}

