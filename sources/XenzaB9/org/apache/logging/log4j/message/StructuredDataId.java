// 
// Decompiled by Procyon v0.6.0
// 

package org.apache.logging.log4j.message;

import org.apache.logging.log4j.util.Strings;
import org.apache.logging.log4j.util.StringBuilderFormattable;
import java.io.Serializable;

public class StructuredDataId implements Serializable, StringBuilderFormattable
{
    public static final StructuredDataId TIME_QUALITY;
    public static final StructuredDataId ORIGIN;
    public static final StructuredDataId META;
    public static final String RESERVED = "-1";
    private static final long serialVersionUID = -8252896346202183738L;
    private static final int MAX_LENGTH = 32;
    private static final String AT_SIGN = "@";
    private final String name;
    private final String enterpriseNumber;
    private final String[] required;
    private final String[] optional;
    
    public StructuredDataId(final String name) {
        this(name, null, null, 32);
    }
    
    public StructuredDataId(final String name, final int maxLength) {
        this(name, null, null, maxLength);
    }
    
    public StructuredDataId(final String name, final String[] required, final String[] optional) {
        this(name, required, optional, 32);
    }
    
    public StructuredDataId(final String name, final String[] required, final String[] optional, int maxLength) {
        int index = -1;
        if (name != null) {
            if (maxLength <= 0) {
                maxLength = 32;
            }
            if (name.length() > maxLength) {
                throw new IllegalArgumentException(String.format("Length of id %s exceeds maximum of %d characters", name, maxLength));
            }
            index = name.indexOf("@");
        }
        if (index > 0) {
            this.name = name.substring(0, index);
            this.enterpriseNumber = name.substring(index + 1).trim();
        }
        else {
            this.name = name;
            this.enterpriseNumber = "-1";
        }
        this.required = required;
        this.optional = optional;
    }
    
    public StructuredDataId(final String name, final String enterpriseNumber, final String[] required, final String[] optional) {
        this(name, enterpriseNumber, required, optional, 32);
    }
    
    @Deprecated
    public StructuredDataId(final String name, final int enterpriseNumber, final String[] required, final String[] optional) {
        this(name, String.valueOf(enterpriseNumber), required, optional, 32);
    }
    
    public StructuredDataId(final String name, final String enterpriseNumber, final String[] required, final String[] optional, final int maxLength) {
        if (name == null) {
            throw new IllegalArgumentException("No structured id name was supplied");
        }
        if (name.contains("@")) {
            throw new IllegalArgumentException("Structured id name cannot contain an " + Strings.quote("@"));
        }
        if ("-1".equals(enterpriseNumber)) {
            throw new IllegalArgumentException("No enterprise number was supplied");
        }
        this.name = name;
        this.enterpriseNumber = enterpriseNumber;
        final String id = name + "@" + enterpriseNumber;
        if (maxLength > 0 && id.length() > maxLength) {
            throw new IllegalArgumentException("Length of id exceeds maximum of " + maxLength + " characters: " + id);
        }
        this.required = required;
        this.optional = optional;
    }
    
    @Deprecated
    public StructuredDataId(final String name, final int enterpriseNumber, final String[] required, final String[] optional, final int maxLength) {
        this(name, String.valueOf(enterpriseNumber), required, optional, maxLength);
    }
    
    public StructuredDataId makeId(final StructuredDataId id) {
        if (id == null) {
            return this;
        }
        return this.makeId(id.getName(), id.getEnterpriseNumber());
    }
    
    public StructuredDataId makeId(final String defaultId, final String anEnterpriseNumber) {
        if ("-1".equals(anEnterpriseNumber)) {
            return this;
        }
        String id;
        String[] req;
        String[] opt;
        if (this.name != null) {
            id = this.name;
            req = this.required;
            opt = this.optional;
        }
        else {
            id = defaultId;
            req = null;
            opt = null;
        }
        return new StructuredDataId(id, anEnterpriseNumber, req, opt);
    }
    
    @Deprecated
    public StructuredDataId makeId(final String defaultId, final int anEnterpriseNumber) {
        return this.makeId(defaultId, String.valueOf(anEnterpriseNumber));
    }
    
    public String[] getRequired() {
        return this.required;
    }
    
    public String[] getOptional() {
        return this.optional;
    }
    
    public String getName() {
        return this.name;
    }
    
    public String getEnterpriseNumber() {
        return this.enterpriseNumber;
    }
    
    public boolean isReserved() {
        return "-1".equals(this.enterpriseNumber);
    }
    
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder(this.name.length() + 10);
        this.formatTo(sb);
        return sb.toString();
    }
    
    @Override
    public void formatTo(final StringBuilder buffer) {
        if (this.isReserved()) {
            buffer.append(this.name);
        }
        else {
            buffer.append(this.name).append("@").append(this.enterpriseNumber);
        }
    }
    
    static {
        TIME_QUALITY = new StructuredDataId("timeQuality", null, new String[] { "tzKnown", "isSynced", "syncAccuracy" });
        ORIGIN = new StructuredDataId("origin", null, new String[] { "ip", "enterpriseId", "software", "swVersion" });
        META = new StructuredDataId("meta", null, new String[] { "sequenceId", "sysUpTime", "language" });
    }
}
