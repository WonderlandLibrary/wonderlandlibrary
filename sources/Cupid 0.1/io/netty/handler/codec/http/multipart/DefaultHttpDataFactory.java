package io.netty.handler.codec.http.multipart;

import io.netty.handler.codec.http.HttpRequest;
import io.netty.util.internal.PlatformDependent;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class DefaultHttpDataFactory implements HttpDataFactory {
  public static final long MINSIZE = 16384L;
  
  private final boolean useDisk;
  
  private final boolean checkSize;
  
  private long minSize;
  
  private final Map<HttpRequest, List<HttpData>> requestFileDeleteMap = PlatformDependent.newConcurrentHashMap();
  
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
    List<HttpData> list = this.requestFileDeleteMap.get(request);
    if (list == null) {
      list = new ArrayList<HttpData>();
      this.requestFileDeleteMap.put(request, list);
    } 
    return list;
  }
  
  public Attribute createAttribute(HttpRequest request, String name) {
    if (this.useDisk) {
      Attribute attribute = new DiskAttribute(name);
      List<HttpData> fileToDelete = getList(request);
      fileToDelete.add(attribute);
      return attribute;
    } 
    if (this.checkSize) {
      Attribute attribute = new MixedAttribute(name, this.minSize);
      List<HttpData> fileToDelete = getList(request);
      fileToDelete.add(attribute);
      return attribute;
    } 
    return new MemoryAttribute(name);
  }
  
  public Attribute createAttribute(HttpRequest request, String name, String value) {
    if (this.useDisk) {
      Attribute attribute;
      try {
        attribute = new DiskAttribute(name, value);
      } catch (IOException e) {
        attribute = new MixedAttribute(name, value, this.minSize);
      } 
      List<HttpData> fileToDelete = getList(request);
      fileToDelete.add(attribute);
      return attribute;
    } 
    if (this.checkSize) {
      Attribute attribute = new MixedAttribute(name, value, this.minSize);
      List<HttpData> fileToDelete = getList(request);
      fileToDelete.add(attribute);
      return attribute;
    } 
    try {
      return new MemoryAttribute(name, value);
    } catch (IOException e) {
      throw new IllegalArgumentException(e);
    } 
  }
  
  public FileUpload createFileUpload(HttpRequest request, String name, String filename, String contentType, String contentTransferEncoding, Charset charset, long size) {
    if (this.useDisk) {
      FileUpload fileUpload = new DiskFileUpload(name, filename, contentType, contentTransferEncoding, charset, size);
      List<HttpData> fileToDelete = getList(request);
      fileToDelete.add(fileUpload);
      return fileUpload;
    } 
    if (this.checkSize) {
      FileUpload fileUpload = new MixedFileUpload(name, filename, contentType, contentTransferEncoding, charset, size, this.minSize);
      List<HttpData> fileToDelete = getList(request);
      fileToDelete.add(fileUpload);
      return fileUpload;
    } 
    return new MemoryFileUpload(name, filename, contentType, contentTransferEncoding, charset, size);
  }
  
  public void removeHttpDataFromClean(HttpRequest request, InterfaceHttpData data) {
    if (data instanceof HttpData) {
      List<HttpData> fileToDelete = getList(request);
      fileToDelete.remove(data);
    } 
  }
  
  public void cleanRequestHttpDatas(HttpRequest request) {
    List<HttpData> fileToDelete = this.requestFileDeleteMap.remove(request);
    if (fileToDelete != null) {
      for (HttpData data : fileToDelete)
        data.delete(); 
      fileToDelete.clear();
    } 
  }
  
  public void cleanAllHttpDatas() {
    Iterator<Map.Entry<HttpRequest, List<HttpData>>> i = this.requestFileDeleteMap.entrySet().iterator();
    while (i.hasNext()) {
      Map.Entry<HttpRequest, List<HttpData>> e = i.next();
      i.remove();
      List<HttpData> fileToDelete = e.getValue();
      if (fileToDelete != null) {
        for (HttpData data : fileToDelete)
          data.delete(); 
        fileToDelete.clear();
      } 
    } 
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\io\netty\handler\codec\http\multipart\DefaultHttpDataFactory.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */