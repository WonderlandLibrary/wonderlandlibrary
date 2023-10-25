/*
 * Decompiled with CFR 0.150.
 */
package com.sun.javafx.runtime.async;

import com.sun.javafx.runtime.async.AbstractAsyncOperation;
import com.sun.javafx.runtime.async.AsyncOperationListener;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InterruptedIOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public abstract class AbstractRemoteResource<T>
extends AbstractAsyncOperation<T> {
    protected final String url;
    protected final String method;
    protected final String outboundContent;
    protected int fileSize;
    private Map<String, String> headers = new HashMap<String, String>();
    private Map<String, List<String>> responseHeaders = new HashMap<String, List<String>>();

    protected AbstractRemoteResource(String string, AsyncOperationListener<T> asyncOperationListener) {
        this(string, "GET", asyncOperationListener);
    }

    protected AbstractRemoteResource(String string, String string2, AsyncOperationListener<T> asyncOperationListener) {
        this(string, string2, null, asyncOperationListener);
    }

    protected AbstractRemoteResource(String string, String string2, String string3, AsyncOperationListener<T> asyncOperationListener) {
        super(asyncOperationListener);
        this.url = string;
        this.method = string2;
        this.outboundContent = string3;
    }

    protected abstract T processStream(InputStream var1) throws IOException;

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Override
    public T call() throws IOException {
        URLConnection uRLConnection;
        URL uRL = new URL(this.url);
        ProgressInputStream progressInputStream = null;
        String string = uRL.getProtocol();
        if (string.equals("http") || string.equals("https")) {
            uRLConnection = (HttpURLConnection)uRL.openConnection();
            ((HttpURLConnection)uRLConnection).setRequestMethod(this.method);
            uRLConnection.setDoInput(true);
            for (Map.Entry<String, String> object : this.headers.entrySet()) {
                String string2 = object.getKey();
                String string3 = object.getValue();
                if (string3 == null || string3.equals("")) continue;
                uRLConnection.setRequestProperty(string2, string3);
            }
            if (this.outboundContent != null && this.method.equals("POST")) {
                uRLConnection.setDoOutput(true);
                Object object2 = this.outboundContent.getBytes("utf-8");
                uRLConnection.setRequestProperty("Content-Length", String.valueOf(((Object)object2).length));
                OutputStream outputStream = uRLConnection.getOutputStream();
                outputStream.write((byte[])object2);
                outputStream.close();
            }
            uRLConnection.connect();
            this.fileSize = uRLConnection.getContentLength();
            this.setProgressMax(this.fileSize);
            this.responseHeaders = uRLConnection.getHeaderFields();
            progressInputStream = new ProgressInputStream(uRLConnection.getInputStream());
        } else {
            uRLConnection = uRL.openConnection();
            this.setProgressMax(uRLConnection.getContentLength());
            progressInputStream = new ProgressInputStream(uRLConnection.getInputStream());
        }
        try {
            uRLConnection = this.processStream(progressInputStream);
            return (T)uRLConnection;
        }
        finally {
            ((InputStream)progressInputStream).close();
        }
    }

    public void setHeader(String string, String string2) {
        this.headers.put(string, string2);
    }

    public String getResponseHeader(String string) {
        String string2 = null;
        List<String> list = this.responseHeaders.get(string);
        if (list != null) {
            StringBuilder stringBuilder = new StringBuilder();
            Iterator<String> iterator = list.iterator();
            while (iterator.hasNext()) {
                stringBuilder.append((Object)iterator.next());
                if (!iterator.hasNext()) continue;
                stringBuilder.append(',');
            }
            string2 = stringBuilder.toString();
        }
        return string2;
    }

    protected class ProgressInputStream
    extends BufferedInputStream {
        public ProgressInputStream(InputStream inputStream) {
            super(inputStream);
        }

        @Override
        public synchronized int read() throws IOException {
            if (Thread.currentThread().isInterrupted()) {
                throw new InterruptedIOException();
            }
            int n = super.read();
            AbstractRemoteResource.this.addProgress(1);
            return n;
        }

        @Override
        public synchronized int read(byte[] arrby, int n, int n2) throws IOException {
            if (Thread.currentThread().isInterrupted()) {
                throw new InterruptedIOException();
            }
            int n3 = super.read(arrby, n, n2);
            AbstractRemoteResource.this.addProgress(n3);
            return n3;
        }

        @Override
        public int read(byte[] arrby) throws IOException {
            if (Thread.currentThread().isInterrupted()) {
                throw new InterruptedIOException();
            }
            int n = super.read(arrby);
            AbstractRemoteResource.this.addProgress(n);
            return n;
        }
    }
}

