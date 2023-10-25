package org.apache.commons.compress.archivers.zip;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import org.apache.commons.compress.archivers.ArchiveEntry;

public class ZipArchiveEntry extends ZipEntry implements ArchiveEntry {
  public static final int PLATFORM_UNIX = 3;
  
  public static final int PLATFORM_FAT = 0;
  
  private static final int SHORT_MASK = 65535;
  
  private static final int SHORT_SHIFT = 16;
  
  private static final byte[] EMPTY = new byte[0];
  
  private int method = -1;
  
  private long size = -1L;
  
  private int internalAttributes = 0;
  
  private int platform = 0;
  
  private long externalAttributes = 0L;
  
  private LinkedHashMap<ZipShort, ZipExtraField> extraFields = null;
  
  private UnparseableExtraFieldData unparseableExtra = null;
  
  private String name = null;
  
  private byte[] rawName = null;
  
  private GeneralPurposeBit gpb = new GeneralPurposeBit();
  
  public ZipArchiveEntry(String name) {
    super(name);
    setName(name);
  }
  
  public ZipArchiveEntry(ZipEntry entry) throws ZipException {
    super(entry);
    setName(entry.getName());
    byte[] extra = entry.getExtra();
    if (extra != null) {
      setExtraFields(ExtraFieldUtils.parse(extra, true, ExtraFieldUtils.UnparseableExtraField.READ));
    } else {
      setExtra();
    } 
    setMethod(entry.getMethod());
    this.size = entry.getSize();
  }
  
  public ZipArchiveEntry(ZipArchiveEntry entry) throws ZipException {
    this(entry);
    setInternalAttributes(entry.getInternalAttributes());
    setExternalAttributes(entry.getExternalAttributes());
    setExtraFields(entry.getExtraFields(true));
  }
  
  protected ZipArchiveEntry() {
    this("");
  }
  
  public ZipArchiveEntry(File inputFile, String entryName) {
    this((inputFile.isDirectory() && !entryName.endsWith("/")) ? (entryName + "/") : entryName);
    if (inputFile.isFile())
      setSize(inputFile.length()); 
    setTime(inputFile.lastModified());
  }
  
  public Object clone() {
    ZipArchiveEntry e = (ZipArchiveEntry)super.clone();
    e.setInternalAttributes(getInternalAttributes());
    e.setExternalAttributes(getExternalAttributes());
    e.setExtraFields(getExtraFields(true));
    return e;
  }
  
  public int getMethod() {
    return this.method;
  }
  
  public void setMethod(int method) {
    if (method < 0)
      throw new IllegalArgumentException("ZIP compression method can not be negative: " + method); 
    this.method = method;
  }
  
  public int getInternalAttributes() {
    return this.internalAttributes;
  }
  
  public void setInternalAttributes(int value) {
    this.internalAttributes = value;
  }
  
  public long getExternalAttributes() {
    return this.externalAttributes;
  }
  
  public void setExternalAttributes(long value) {
    this.externalAttributes = value;
  }
  
  public void setUnixMode(int mode) {
    setExternalAttributes((mode << 16 | (((mode & 0x80) == 0) ? 1 : 0) | (isDirectory() ? 16 : 0)));
    this.platform = 3;
  }
  
  public int getUnixMode() {
    return (this.platform != 3) ? 0 : (int)(getExternalAttributes() >> 16L & 0xFFFFL);
  }
  
  public boolean isUnixSymlink() {
    return ((getUnixMode() & 0xA000) == 40960);
  }
  
  public int getPlatform() {
    return this.platform;
  }
  
  protected void setPlatform(int platform) {
    this.platform = platform;
  }
  
  public void setExtraFields(ZipExtraField[] fields) {
    this.extraFields = new LinkedHashMap<ZipShort, ZipExtraField>();
    for (ZipExtraField field : fields) {
      if (field instanceof UnparseableExtraFieldData) {
        this.unparseableExtra = (UnparseableExtraFieldData)field;
      } else {
        this.extraFields.put(field.getHeaderId(), field);
      } 
    } 
    setExtra();
  }
  
  public ZipExtraField[] getExtraFields() {
    return getExtraFields(false);
  }
  
  public ZipExtraField[] getExtraFields(boolean includeUnparseable) {
    if (this.extraFields == null) {
      (new ZipExtraField[1])[0] = this.unparseableExtra;
      return (!includeUnparseable || this.unparseableExtra == null) ? new ZipExtraField[0] : new ZipExtraField[1];
    } 
    List<ZipExtraField> result = new ArrayList<ZipExtraField>(this.extraFields.values());
    if (includeUnparseable && this.unparseableExtra != null)
      result.add(this.unparseableExtra); 
    return result.<ZipExtraField>toArray(new ZipExtraField[0]);
  }
  
  public void addExtraField(ZipExtraField ze) {
    if (ze instanceof UnparseableExtraFieldData) {
      this.unparseableExtra = (UnparseableExtraFieldData)ze;
    } else {
      if (this.extraFields == null)
        this.extraFields = new LinkedHashMap<ZipShort, ZipExtraField>(); 
      this.extraFields.put(ze.getHeaderId(), ze);
    } 
    setExtra();
  }
  
  public void addAsFirstExtraField(ZipExtraField ze) {
    if (ze instanceof UnparseableExtraFieldData) {
      this.unparseableExtra = (UnparseableExtraFieldData)ze;
    } else {
      LinkedHashMap<ZipShort, ZipExtraField> copy = this.extraFields;
      this.extraFields = new LinkedHashMap<ZipShort, ZipExtraField>();
      this.extraFields.put(ze.getHeaderId(), ze);
      if (copy != null) {
        copy.remove(ze.getHeaderId());
        this.extraFields.putAll(copy);
      } 
    } 
    setExtra();
  }
  
  public void removeExtraField(ZipShort type) {
    if (this.extraFields == null)
      throw new NoSuchElementException(); 
    if (this.extraFields.remove(type) == null)
      throw new NoSuchElementException(); 
    setExtra();
  }
  
  public void removeUnparseableExtraFieldData() {
    if (this.unparseableExtra == null)
      throw new NoSuchElementException(); 
    this.unparseableExtra = null;
    setExtra();
  }
  
  public ZipExtraField getExtraField(ZipShort type) {
    if (this.extraFields != null)
      return this.extraFields.get(type); 
    return null;
  }
  
  public UnparseableExtraFieldData getUnparseableExtraFieldData() {
    return this.unparseableExtra;
  }
  
  public void setExtra(byte[] extra) throws RuntimeException {
    try {
      ZipExtraField[] local = ExtraFieldUtils.parse(extra, true, ExtraFieldUtils.UnparseableExtraField.READ);
      mergeExtraFields(local, true);
    } catch (ZipException e) {
      throw new RuntimeException("Error parsing extra fields for entry: " + getName() + " - " + e.getMessage(), e);
    } 
  }
  
  protected void setExtra() {
    super.setExtra(ExtraFieldUtils.mergeLocalFileDataData(getExtraFields(true)));
  }
  
  public void setCentralDirectoryExtra(byte[] b) {
    try {
      ZipExtraField[] central = ExtraFieldUtils.parse(b, false, ExtraFieldUtils.UnparseableExtraField.READ);
      mergeExtraFields(central, false);
    } catch (ZipException e) {
      throw new RuntimeException(e.getMessage(), e);
    } 
  }
  
  public byte[] getLocalFileDataExtra() {
    byte[] extra = getExtra();
    return (extra != null) ? extra : EMPTY;
  }
  
  public byte[] getCentralDirectoryExtra() {
    return ExtraFieldUtils.mergeCentralDirectoryData(getExtraFields(true));
  }
  
  public String getName() {
    return (this.name == null) ? super.getName() : this.name;
  }
  
  public boolean isDirectory() {
    return getName().endsWith("/");
  }
  
  protected void setName(String name) {
    if (name != null && getPlatform() == 0 && name.indexOf("/") == -1)
      name = name.replace('\\', '/'); 
    this.name = name;
  }
  
  public long getSize() {
    return this.size;
  }
  
  public void setSize(long size) {
    if (size < 0L)
      throw new IllegalArgumentException("invalid entry size"); 
    this.size = size;
  }
  
  protected void setName(String name, byte[] rawName) {
    setName(name);
    this.rawName = rawName;
  }
  
  public byte[] getRawName() {
    if (this.rawName != null) {
      byte[] b = new byte[this.rawName.length];
      System.arraycopy(this.rawName, 0, b, 0, this.rawName.length);
      return b;
    } 
    return null;
  }
  
  public int hashCode() {
    return getName().hashCode();
  }
  
  public GeneralPurposeBit getGeneralPurposeBit() {
    return this.gpb;
  }
  
  public void setGeneralPurposeBit(GeneralPurposeBit b) {
    this.gpb = b;
  }
  
  private void mergeExtraFields(ZipExtraField[] f, boolean local) throws ZipException {
    if (this.extraFields == null) {
      setExtraFields(f);
    } else {
      for (ZipExtraField element : f) {
        ZipExtraField existing;
        if (element instanceof UnparseableExtraFieldData) {
          existing = this.unparseableExtra;
        } else {
          existing = getExtraField(element.getHeaderId());
        } 
        if (existing == null) {
          addExtraField(element);
        } else if (local) {
          byte[] b = element.getLocalFileDataData();
          existing.parseFromLocalFileData(b, 0, b.length);
        } else {
          byte[] b = element.getCentralDirectoryData();
          existing.parseFromCentralDirectoryData(b, 0, b.length);
        } 
      } 
      setExtra();
    } 
  }
  
  public Date getLastModifiedDate() {
    return new Date(getTime());
  }
  
  public boolean equals(Object obj) {
    if (this == obj)
      return true; 
    if (obj == null || getClass() != obj.getClass())
      return false; 
    ZipArchiveEntry other = (ZipArchiveEntry)obj;
    String myName = getName();
    String otherName = other.getName();
    if (myName == null) {
      if (otherName != null)
        return false; 
    } else if (!myName.equals(otherName)) {
      return false;
    } 
    String myComment = getComment();
    String otherComment = other.getComment();
    if (myComment == null)
      myComment = ""; 
    if (otherComment == null)
      otherComment = ""; 
    return (getTime() == other.getTime() && myComment.equals(otherComment) && getInternalAttributes() == other.getInternalAttributes() && getPlatform() == other.getPlatform() && getExternalAttributes() == other.getExternalAttributes() && getMethod() == other.getMethod() && getSize() == other.getSize() && getCrc() == other.getCrc() && getCompressedSize() == other.getCompressedSize() && Arrays.equals(getCentralDirectoryExtra(), other.getCentralDirectoryExtra()) && Arrays.equals(getLocalFileDataExtra(), other.getLocalFileDataExtra()) && this.gpb.equals(other.gpb));
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\commons\compress\archivers\zip\ZipArchiveEntry.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */