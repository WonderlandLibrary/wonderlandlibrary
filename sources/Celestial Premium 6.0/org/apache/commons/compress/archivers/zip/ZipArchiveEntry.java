/*
 * Decompiled with CFR 0.150.
 */
package org.apache.commons.compress.archivers.zip;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.NoSuchElementException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.zip.ExtraFieldUtils;
import org.apache.commons.compress.archivers.zip.GeneralPurposeBit;
import org.apache.commons.compress.archivers.zip.UnparseableExtraFieldData;
import org.apache.commons.compress.archivers.zip.ZipExtraField;
import org.apache.commons.compress.archivers.zip.ZipShort;

public class ZipArchiveEntry
extends ZipEntry
implements ArchiveEntry {
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
        this.setName(name);
    }

    public ZipArchiveEntry(ZipEntry entry) throws ZipException {
        super(entry);
        this.setName(entry.getName());
        byte[] extra = entry.getExtra();
        if (extra != null) {
            this.setExtraFields(ExtraFieldUtils.parse(extra, true, ExtraFieldUtils.UnparseableExtraField.READ));
        } else {
            this.setExtra();
        }
        this.setMethod(entry.getMethod());
        this.size = entry.getSize();
    }

    public ZipArchiveEntry(ZipArchiveEntry entry) throws ZipException {
        this((ZipEntry)entry);
        this.setInternalAttributes(entry.getInternalAttributes());
        this.setExternalAttributes(entry.getExternalAttributes());
        this.setExtraFields(entry.getExtraFields(true));
    }

    protected ZipArchiveEntry() {
        this("");
    }

    public ZipArchiveEntry(File inputFile, String entryName) {
        this(inputFile.isDirectory() && !entryName.endsWith("/") ? entryName + "/" : entryName);
        if (inputFile.isFile()) {
            this.setSize(inputFile.length());
        }
        this.setTime(inputFile.lastModified());
    }

    public Object clone() {
        ZipArchiveEntry e = (ZipArchiveEntry)super.clone();
        e.setInternalAttributes(this.getInternalAttributes());
        e.setExternalAttributes(this.getExternalAttributes());
        e.setExtraFields(this.getExtraFields(true));
        return e;
    }

    public int getMethod() {
        return this.method;
    }

    public void setMethod(int method) {
        if (method < 0) {
            throw new IllegalArgumentException("ZIP compression method can not be negative: " + method);
        }
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
        this.setExternalAttributes(mode << 16 | ((mode & 0x80) == 0 ? 1 : 0) | (this.isDirectory() ? 16 : 0));
        this.platform = 3;
    }

    public int getUnixMode() {
        return this.platform != 3 ? 0 : (int)(this.getExternalAttributes() >> 16 & 0xFFFFL);
    }

    public boolean isUnixSymlink() {
        return (this.getUnixMode() & 0xA000) == 40960;
    }

    public int getPlatform() {
        return this.platform;
    }

    protected void setPlatform(int platform) {
        this.platform = platform;
    }

    public void setExtraFields(ZipExtraField[] fields) {
        this.extraFields = new LinkedHashMap();
        for (ZipExtraField field : fields) {
            if (field instanceof UnparseableExtraFieldData) {
                this.unparseableExtra = (UnparseableExtraFieldData)field;
                continue;
            }
            this.extraFields.put(field.getHeaderId(), field);
        }
        this.setExtra();
    }

    public ZipExtraField[] getExtraFields() {
        return this.getExtraFields(false);
    }

    public ZipExtraField[] getExtraFields(boolean includeUnparseable) {
        if (this.extraFields == null) {
            ZipExtraField[] arrzipExtraField;
            if (!includeUnparseable || this.unparseableExtra == null) {
                arrzipExtraField = new ZipExtraField[]{};
            } else {
                ZipExtraField[] arrzipExtraField2 = new ZipExtraField[1];
                arrzipExtraField = arrzipExtraField2;
                arrzipExtraField2[0] = this.unparseableExtra;
            }
            return arrzipExtraField;
        }
        ArrayList<ZipExtraField> result = new ArrayList<ZipExtraField>(this.extraFields.values());
        if (includeUnparseable && this.unparseableExtra != null) {
            result.add(this.unparseableExtra);
        }
        return result.toArray(new ZipExtraField[0]);
    }

    public void addExtraField(ZipExtraField ze) {
        if (ze instanceof UnparseableExtraFieldData) {
            this.unparseableExtra = (UnparseableExtraFieldData)ze;
        } else {
            if (this.extraFields == null) {
                this.extraFields = new LinkedHashMap();
            }
            this.extraFields.put(ze.getHeaderId(), ze);
        }
        this.setExtra();
    }

    public void addAsFirstExtraField(ZipExtraField ze) {
        if (ze instanceof UnparseableExtraFieldData) {
            this.unparseableExtra = (UnparseableExtraFieldData)ze;
        } else {
            LinkedHashMap<ZipShort, ZipExtraField> copy = this.extraFields;
            this.extraFields = new LinkedHashMap();
            this.extraFields.put(ze.getHeaderId(), ze);
            if (copy != null) {
                copy.remove(ze.getHeaderId());
                this.extraFields.putAll(copy);
            }
        }
        this.setExtra();
    }

    public void removeExtraField(ZipShort type) {
        if (this.extraFields == null) {
            throw new NoSuchElementException();
        }
        if (this.extraFields.remove(type) == null) {
            throw new NoSuchElementException();
        }
        this.setExtra();
    }

    public void removeUnparseableExtraFieldData() {
        if (this.unparseableExtra == null) {
            throw new NoSuchElementException();
        }
        this.unparseableExtra = null;
        this.setExtra();
    }

    public ZipExtraField getExtraField(ZipShort type) {
        if (this.extraFields != null) {
            return this.extraFields.get(type);
        }
        return null;
    }

    public UnparseableExtraFieldData getUnparseableExtraFieldData() {
        return this.unparseableExtra;
    }

    public void setExtra(byte[] extra) throws RuntimeException {
        try {
            ZipExtraField[] local = ExtraFieldUtils.parse(extra, true, ExtraFieldUtils.UnparseableExtraField.READ);
            this.mergeExtraFields(local, true);
        }
        catch (ZipException e) {
            throw new RuntimeException("Error parsing extra fields for entry: " + this.getName() + " - " + e.getMessage(), e);
        }
    }

    protected void setExtra() {
        super.setExtra(ExtraFieldUtils.mergeLocalFileDataData(this.getExtraFields(true)));
    }

    public void setCentralDirectoryExtra(byte[] b) {
        try {
            ZipExtraField[] central = ExtraFieldUtils.parse(b, false, ExtraFieldUtils.UnparseableExtraField.READ);
            this.mergeExtraFields(central, false);
        }
        catch (ZipException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    public byte[] getLocalFileDataExtra() {
        byte[] extra = this.getExtra();
        return extra != null ? extra : EMPTY;
    }

    public byte[] getCentralDirectoryExtra() {
        return ExtraFieldUtils.mergeCentralDirectoryData(this.getExtraFields(true));
    }

    public String getName() {
        return this.name == null ? super.getName() : this.name;
    }

    public boolean isDirectory() {
        return this.getName().endsWith("/");
    }

    protected void setName(String name) {
        if (name != null && this.getPlatform() == 0 && name.indexOf("/") == -1) {
            name = name.replace('\\', '/');
        }
        this.name = name;
    }

    public long getSize() {
        return this.size;
    }

    public void setSize(long size) {
        if (size < 0L) {
            throw new IllegalArgumentException("invalid entry size");
        }
        this.size = size;
    }

    protected void setName(String name, byte[] rawName) {
        this.setName(name);
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
        return this.getName().hashCode();
    }

    public GeneralPurposeBit getGeneralPurposeBit() {
        return this.gpb;
    }

    public void setGeneralPurposeBit(GeneralPurposeBit b) {
        this.gpb = b;
    }

    private void mergeExtraFields(ZipExtraField[] f, boolean local) throws ZipException {
        if (this.extraFields == null) {
            this.setExtraFields(f);
        } else {
            for (ZipExtraField element : f) {
                byte[] b;
                ZipExtraField existing = element instanceof UnparseableExtraFieldData ? this.unparseableExtra : this.getExtraField(element.getHeaderId());
                if (existing == null) {
                    this.addExtraField(element);
                    continue;
                }
                if (local) {
                    b = element.getLocalFileDataData();
                    existing.parseFromLocalFileData(b, 0, b.length);
                    continue;
                }
                b = element.getCentralDirectoryData();
                existing.parseFromCentralDirectoryData(b, 0, b.length);
            }
            this.setExtra();
        }
    }

    public Date getLastModifiedDate() {
        return new Date(this.getTime());
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || this.getClass() != obj.getClass()) {
            return false;
        }
        ZipArchiveEntry other = (ZipArchiveEntry)obj;
        String myName = this.getName();
        String otherName = other.getName();
        if (myName == null ? otherName != null : !myName.equals(otherName)) {
            return false;
        }
        String myComment = this.getComment();
        String otherComment = other.getComment();
        if (myComment == null) {
            myComment = "";
        }
        if (otherComment == null) {
            otherComment = "";
        }
        return this.getTime() == other.getTime() && myComment.equals(otherComment) && this.getInternalAttributes() == other.getInternalAttributes() && this.getPlatform() == other.getPlatform() && this.getExternalAttributes() == other.getExternalAttributes() && this.getMethod() == other.getMethod() && this.getSize() == other.getSize() && this.getCrc() == other.getCrc() && this.getCompressedSize() == other.getCompressedSize() && Arrays.equals(this.getCentralDirectoryExtra(), other.getCentralDirectoryExtra()) && Arrays.equals(this.getLocalFileDataExtra(), other.getLocalFileDataExtra()) && this.gpb.equals(other.gpb);
    }
}

