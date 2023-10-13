/*
 * Decompiled with CFR 0.150.
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
import java.util.List;
import java.util.concurrent.ConcurrentMap;

public class DefaultHttpDataFactory
implements HttpDataFactory {
    public static final long MINSIZE = 16384L;
    private final boolean useDisk;
    private final boolean checkSize;
    private long minSize;
    private final ConcurrentMap<HttpRequest, List<HttpData>> requestFileDeleteMap = PlatformDependent.newConcurrentHashMap();

    public DefaultHttpDataFactory() {
        this.useDisk = false;
        this.checkSize = true;
        this.minSize = 16384L;
    }

    public DefaultHttpDataFactory(boolean useDisk) {
        this.useDisk = useDisk;
        this.checkSize = false;
    }

    public DefaultHttpDataFactory(long minSize) {
        this.useDisk = false;
        this.checkSize = true;
        this.minSize = minSize;
    }

    private List<HttpData> getList(HttpRequest request) {
        ArrayList list = (ArrayList)this.requestFileDeleteMap.get(request);
        if (list == null) {
            list = new ArrayList();
            this.requestFileDeleteMap.put(request, list);
        }
        return list;
    }

    @Override
    public Attribute createAttribute(HttpRequest request, String name) {
        if (this.useDisk) {
            DiskAttribute attribute = new DiskAttribute(name);
            List<HttpData> fileToDelete = this.getList(request);
            fileToDelete.add(attribute);
            return attribute;
        }
        if (this.checkSize) {
            MixedAttribute attribute = new MixedAttribute(name, this.minSize);
            List<HttpData> fileToDelete = this.getList(request);
            fileToDelete.add(attribute);
            return attribute;
        }
        return new MemoryAttribute(name);
    }

    @Override
    public Attribute createAttribute(HttpRequest request, String name, String value) {
        if (this.useDisk) {
            Attribute attribute;
            try {
                attribute = new DiskAttribute(name, value);
            }
            catch (IOException e) {
                attribute = new MixedAttribute(name, value, this.minSize);
            }
            List<HttpData> fileToDelete = this.getList(request);
            fileToDelete.add(attribute);
            return attribute;
        }
        if (this.checkSize) {
            MixedAttribute attribute = new MixedAttribute(name, value, this.minSize);
            List<HttpData> fileToDelete = this.getList(request);
            fileToDelete.add(attribute);
            return attribute;
        }
        try {
            return new MemoryAttribute(name, value);
        }
        catch (IOException e) {
            throw new IllegalArgumentException(e);
        }
    }

    @Override
    public FileUpload createFileUpload(HttpRequest request, String name, String filename, String contentType, String contentTransferEncoding, Charset charset, long size) {
        if (this.useDisk) {
            DiskFileUpload fileUpload = new DiskFileUpload(name, filename, contentType, contentTransferEncoding, charset, size);
            List<HttpData> fileToDelete = this.getList(request);
            fileToDelete.add(fileUpload);
            return fileUpload;
        }
        if (this.checkSize) {
            MixedFileUpload fileUpload = new MixedFileUpload(name, filename, contentType, contentTransferEncoding, charset, size, this.minSize);
            List<HttpData> fileToDelete = this.getList(request);
            fileToDelete.add(fileUpload);
            return fileUpload;
        }
        return new MemoryFileUpload(name, filename, contentType, contentTransferEncoding, charset, size);
    }

    @Override
    public void removeHttpDataFromClean(HttpRequest request, InterfaceHttpData data) {
        if (data instanceof HttpData) {
            List<HttpData> fileToDelete = this.getList(request);
            fileToDelete.remove(data);
        }
    }

    @Override
    public void cleanRequestHttpDatas(HttpRequest request) {
        List fileToDelete = (List)this.requestFileDeleteMap.remove(request);
        if (fileToDelete != null) {
            for (HttpData data : fileToDelete) {
                data.delete();
            }
            fileToDelete.clear();
        }
    }

    @Override
    public void cleanAllHttpDatas() {
        for (HttpRequest request : this.requestFileDeleteMap.keySet()) {
            List fileToDelete = (List)this.requestFileDeleteMap.get(request);
            if (fileToDelete != null) {
                for (HttpData data : fileToDelete) {
                    data.delete();
                }
                fileToDelete.clear();
            }
            this.requestFileDeleteMap.remove(request);
        }
    }
}

