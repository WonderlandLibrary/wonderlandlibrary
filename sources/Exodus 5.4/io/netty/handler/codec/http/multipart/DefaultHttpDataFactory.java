/*
 * Decompiled with CFR 0.152.
 */
package io.netty.handler.codec.http.multipart;

import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.multipart.Attribute;
import io.netty.handler.codec.http.multipart.DiskAttribute;
import io.netty.handler.codec.http.multipart.DiskFileUpload;
import io.netty.handler.codec.http.multipart.FileUpload;
import io.netty.handler.codec.http.multipart.HttpData;
import io.netty.handler.codec.http.multipart.HttpDataFactory;
import io.netty.handler.codec.http.multipart.InterfaceHttpData;
import io.netty.handler.codec.http.multipart.MemoryAttribute;
import io.netty.handler.codec.http.multipart.MemoryFileUpload;
import io.netty.handler.codec.http.multipart.MixedAttribute;
import io.netty.handler.codec.http.multipart.MixedFileUpload;
import io.netty.util.internal.PlatformDependent;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class DefaultHttpDataFactory
implements HttpDataFactory {
    public static final long MINSIZE = 16384L;
    private final boolean useDisk;
    private final boolean checkSize;
    private long minSize;
    private final Map<HttpRequest, List<HttpData>> requestFileDeleteMap = PlatformDependent.newConcurrentHashMap();

    @Override
    public Attribute createAttribute(HttpRequest httpRequest, String string) {
        if (this.useDisk) {
            DiskAttribute diskAttribute = new DiskAttribute(string);
            List<HttpData> list = this.getList(httpRequest);
            list.add(diskAttribute);
            return diskAttribute;
        }
        if (this.checkSize) {
            MixedAttribute mixedAttribute = new MixedAttribute(string, this.minSize);
            List<HttpData> list = this.getList(httpRequest);
            list.add(mixedAttribute);
            return mixedAttribute;
        }
        return new MemoryAttribute(string);
    }

    public DefaultHttpDataFactory() {
        this.useDisk = false;
        this.checkSize = true;
        this.minSize = 16384L;
    }

    @Override
    public Attribute createAttribute(HttpRequest httpRequest, String string, String string2) {
        if (this.useDisk) {
            Attribute attribute;
            try {
                attribute = new DiskAttribute(string, string2);
            }
            catch (IOException iOException) {
                attribute = new MixedAttribute(string, string2, this.minSize);
            }
            List<HttpData> list = this.getList(httpRequest);
            list.add(attribute);
            return attribute;
        }
        if (this.checkSize) {
            MixedAttribute mixedAttribute = new MixedAttribute(string, string2, this.minSize);
            List<HttpData> list = this.getList(httpRequest);
            list.add(mixedAttribute);
            return mixedAttribute;
        }
        try {
            return new MemoryAttribute(string, string2);
        }
        catch (IOException iOException) {
            throw new IllegalArgumentException(iOException);
        }
    }

    @Override
    public void cleanAllHttpDatas() {
        Iterator<Map.Entry<HttpRequest, List<HttpData>>> iterator = this.requestFileDeleteMap.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<HttpRequest, List<HttpData>> entry = iterator.next();
            iterator.remove();
            List<HttpData> list = entry.getValue();
            if (list == null) continue;
            for (HttpData httpData : list) {
                httpData.delete();
            }
            list.clear();
        }
    }

    private List<HttpData> getList(HttpRequest httpRequest) {
        List<HttpData> list = this.requestFileDeleteMap.get(httpRequest);
        if (list == null) {
            list = new ArrayList<HttpData>();
            this.requestFileDeleteMap.put(httpRequest, list);
        }
        return list;
    }

    public DefaultHttpDataFactory(boolean bl) {
        this.useDisk = bl;
        this.checkSize = false;
    }

    @Override
    public void removeHttpDataFromClean(HttpRequest httpRequest, InterfaceHttpData interfaceHttpData) {
        if (interfaceHttpData instanceof HttpData) {
            List<HttpData> list = this.getList(httpRequest);
            list.remove(interfaceHttpData);
        }
    }

    public DefaultHttpDataFactory(long l) {
        this.useDisk = false;
        this.checkSize = true;
        this.minSize = l;
    }

    @Override
    public void cleanRequestHttpDatas(HttpRequest httpRequest) {
        List<HttpData> list = this.requestFileDeleteMap.remove(httpRequest);
        if (list != null) {
            for (HttpData httpData : list) {
                httpData.delete();
            }
            list.clear();
        }
    }

    @Override
    public FileUpload createFileUpload(HttpRequest httpRequest, String string, String string2, String string3, String string4, Charset charset, long l) {
        if (this.useDisk) {
            DiskFileUpload diskFileUpload = new DiskFileUpload(string, string2, string3, string4, charset, l);
            List<HttpData> list = this.getList(httpRequest);
            list.add(diskFileUpload);
            return diskFileUpload;
        }
        if (this.checkSize) {
            MixedFileUpload mixedFileUpload = new MixedFileUpload(string, string2, string3, string4, charset, l, this.minSize);
            List<HttpData> list = this.getList(httpRequest);
            list.add(mixedFileUpload);
            return mixedFileUpload;
        }
        return new MemoryFileUpload(string, string2, string3, string4, charset, l);
    }
}

