package org.apache.commons.compress.archivers;

import java.io.ByteArrayInputStream;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import org.apache.commons.compress.archivers.ar.ArArchiveInputStream;
import org.apache.commons.compress.archivers.ar.ArArchiveOutputStream;
import org.apache.commons.compress.archivers.arj.ArjArchiveInputStream;
import org.apache.commons.compress.archivers.cpio.CpioArchiveInputStream;
import org.apache.commons.compress.archivers.cpio.CpioArchiveOutputStream;
import org.apache.commons.compress.archivers.dump.DumpArchiveInputStream;
import org.apache.commons.compress.archivers.jar.JarArchiveInputStream;
import org.apache.commons.compress.archivers.jar.JarArchiveOutputStream;
import org.apache.commons.compress.archivers.sevenz.SevenZFile;
import org.apache.commons.compress.archivers.tar.TarArchiveInputStream;
import org.apache.commons.compress.archivers.tar.TarArchiveOutputStream;
import org.apache.commons.compress.archivers.zip.ZipArchiveInputStream;
import org.apache.commons.compress.archivers.zip.ZipArchiveOutputStream;
import org.apache.commons.compress.utils.IOUtils;

public class ArchiveStreamFactory {
  public static final String AR = "ar";
  
  public static final String ARJ = "arj";
  
  public static final String CPIO = "cpio";
  
  public static final String DUMP = "dump";
  
  public static final String JAR = "jar";
  
  public static final String TAR = "tar";
  
  public static final String ZIP = "zip";
  
  public static final String SEVEN_Z = "7z";
  
  private String entryEncoding = null;
  
  public String getEntryEncoding() {
    return this.entryEncoding;
  }
  
  public void setEntryEncoding(String entryEncoding) {
    this.entryEncoding = entryEncoding;
  }
  
  public ArchiveInputStream createArchiveInputStream(String archiverName, InputStream in) throws ArchiveException {
    if (archiverName == null)
      throw new IllegalArgumentException("Archivername must not be null."); 
    if (in == null)
      throw new IllegalArgumentException("InputStream must not be null."); 
    if ("ar".equalsIgnoreCase(archiverName))
      return (ArchiveInputStream)new ArArchiveInputStream(in); 
    if ("arj".equalsIgnoreCase(archiverName)) {
      if (this.entryEncoding != null)
        return (ArchiveInputStream)new ArjArchiveInputStream(in, this.entryEncoding); 
      return (ArchiveInputStream)new ArjArchiveInputStream(in);
    } 
    if ("zip".equalsIgnoreCase(archiverName)) {
      if (this.entryEncoding != null)
        return (ArchiveInputStream)new ZipArchiveInputStream(in, this.entryEncoding); 
      return (ArchiveInputStream)new ZipArchiveInputStream(in);
    } 
    if ("tar".equalsIgnoreCase(archiverName)) {
      if (this.entryEncoding != null)
        return (ArchiveInputStream)new TarArchiveInputStream(in, this.entryEncoding); 
      return (ArchiveInputStream)new TarArchiveInputStream(in);
    } 
    if ("jar".equalsIgnoreCase(archiverName))
      return (ArchiveInputStream)new JarArchiveInputStream(in); 
    if ("cpio".equalsIgnoreCase(archiverName)) {
      if (this.entryEncoding != null)
        return (ArchiveInputStream)new CpioArchiveInputStream(in, this.entryEncoding); 
      return (ArchiveInputStream)new CpioArchiveInputStream(in);
    } 
    if ("dump".equalsIgnoreCase(archiverName)) {
      if (this.entryEncoding != null)
        return (ArchiveInputStream)new DumpArchiveInputStream(in, this.entryEncoding); 
      return (ArchiveInputStream)new DumpArchiveInputStream(in);
    } 
    if ("7z".equalsIgnoreCase(archiverName))
      throw new StreamingNotSupportedException("7z"); 
    throw new ArchiveException("Archiver: " + archiverName + " not found.");
  }
  
  public ArchiveOutputStream createArchiveOutputStream(String archiverName, OutputStream out) throws ArchiveException {
    if (archiverName == null)
      throw new IllegalArgumentException("Archivername must not be null."); 
    if (out == null)
      throw new IllegalArgumentException("OutputStream must not be null."); 
    if ("ar".equalsIgnoreCase(archiverName))
      return (ArchiveOutputStream)new ArArchiveOutputStream(out); 
    if ("zip".equalsIgnoreCase(archiverName)) {
      ZipArchiveOutputStream zip = new ZipArchiveOutputStream(out);
      if (this.entryEncoding != null)
        zip.setEncoding(this.entryEncoding); 
      return (ArchiveOutputStream)zip;
    } 
    if ("tar".equalsIgnoreCase(archiverName)) {
      if (this.entryEncoding != null)
        return (ArchiveOutputStream)new TarArchiveOutputStream(out, this.entryEncoding); 
      return (ArchiveOutputStream)new TarArchiveOutputStream(out);
    } 
    if ("jar".equalsIgnoreCase(archiverName))
      return (ArchiveOutputStream)new JarArchiveOutputStream(out); 
    if ("cpio".equalsIgnoreCase(archiverName)) {
      if (this.entryEncoding != null)
        return (ArchiveOutputStream)new CpioArchiveOutputStream(out, this.entryEncoding); 
      return (ArchiveOutputStream)new CpioArchiveOutputStream(out);
    } 
    if ("7z".equalsIgnoreCase(archiverName))
      throw new StreamingNotSupportedException("7z"); 
    throw new ArchiveException("Archiver: " + archiverName + " not found.");
  }
  
  public ArchiveInputStream createArchiveInputStream(InputStream in) throws ArchiveException {
    if (in == null)
      throw new IllegalArgumentException("Stream must not be null."); 
    if (!in.markSupported())
      throw new IllegalArgumentException("Mark is not supported."); 
    byte[] signature = new byte[12];
    in.mark(signature.length);
    try {
      int signatureLength = IOUtils.readFully(in, signature);
      in.reset();
      if (ZipArchiveInputStream.matches(signature, signatureLength)) {
        if (this.entryEncoding != null)
          return (ArchiveInputStream)new ZipArchiveInputStream(in, this.entryEncoding); 
        return (ArchiveInputStream)new ZipArchiveInputStream(in);
      } 
      if (JarArchiveInputStream.matches(signature, signatureLength))
        return (ArchiveInputStream)new JarArchiveInputStream(in); 
      if (ArArchiveInputStream.matches(signature, signatureLength))
        return (ArchiveInputStream)new ArArchiveInputStream(in); 
      if (CpioArchiveInputStream.matches(signature, signatureLength))
        return (ArchiveInputStream)new CpioArchiveInputStream(in); 
      if (ArjArchiveInputStream.matches(signature, signatureLength))
        return (ArchiveInputStream)new ArjArchiveInputStream(in); 
      if (SevenZFile.matches(signature, signatureLength))
        throw new StreamingNotSupportedException("7z"); 
      byte[] dumpsig = new byte[32];
      in.mark(dumpsig.length);
      signatureLength = IOUtils.readFully(in, dumpsig);
      in.reset();
      if (DumpArchiveInputStream.matches(dumpsig, signatureLength))
        return (ArchiveInputStream)new DumpArchiveInputStream(in); 
      byte[] tarheader = new byte[512];
      in.mark(tarheader.length);
      signatureLength = IOUtils.readFully(in, tarheader);
      in.reset();
      if (TarArchiveInputStream.matches(tarheader, signatureLength)) {
        if (this.entryEncoding != null)
          return (ArchiveInputStream)new TarArchiveInputStream(in, this.entryEncoding); 
        return (ArchiveInputStream)new TarArchiveInputStream(in);
      } 
      if (signatureLength >= 512) {
        TarArchiveInputStream tais = null;
        try {
          tais = new TarArchiveInputStream(new ByteArrayInputStream(tarheader));
          if (tais.getNextTarEntry().isCheckSumOK())
            return (ArchiveInputStream)new TarArchiveInputStream(in); 
        } catch (Exception e) {
        
        } finally {
          IOUtils.closeQuietly((Closeable)tais);
        } 
      } 
    } catch (IOException e) {
      throw new ArchiveException("Could not use reset and mark operations.", e);
    } 
    throw new ArchiveException("No Archiver found for the stream signature");
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\commons\compress\archivers\ArchiveStreamFactory.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */