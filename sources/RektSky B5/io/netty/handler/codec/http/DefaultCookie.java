/*
 * Decompiled with CFR 0.152.
 */
package io.netty.handler.codec.http;

import io.netty.handler.codec.http.Cookie;
import java.util.Collections;
import java.util.Set;
import java.util.TreeSet;

public class DefaultCookie
implements Cookie {
    private final String name;
    private String value;
    private String domain;
    private String path;
    private String comment;
    private String commentUrl;
    private boolean discard;
    private Set<Integer> ports = Collections.emptySet();
    private Set<Integer> unmodifiablePorts = this.ports;
    private long maxAge = Long.MIN_VALUE;
    private int version;
    private boolean secure;
    private boolean httpOnly;

    public DefaultCookie(String name, String value) {
        if (name == null) {
            throw new NullPointerException("name");
        }
        if ((name = name.trim()).isEmpty()) {
            throw new IllegalArgumentException("empty name");
        }
        for (int i2 = 0; i2 < name.length(); ++i2) {
            char c2 = name.charAt(i2);
            if (c2 > '\u007f') {
                throw new IllegalArgumentException("name contains non-ascii character: " + name);
            }
            switch (c2) {
                case '\t': 
                case '\n': 
                case '\u000b': 
                case '\f': 
                case '\r': 
                case ' ': 
                case ',': 
                case ';': 
                case '=': {
                    throw new IllegalArgumentException("name contains one of the following prohibited characters: =,; \\t\\r\\n\\v\\f: " + name);
                }
            }
        }
        if (name.charAt(0) == '$') {
            throw new IllegalArgumentException("name starting with '$' not allowed: " + name);
        }
        this.name = name;
        this.setValue(value);
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public String getValue() {
        return this.value;
    }

    @Override
    public void setValue(String value) {
        if (value == null) {
            throw new NullPointerException("value");
        }
        this.value = value;
    }

    @Override
    public String getDomain() {
        return this.domain;
    }

    @Override
    public void setDomain(String domain) {
        this.domain = DefaultCookie.validateValue("domain", domain);
    }

    @Override
    public String getPath() {
        return this.path;
    }

    @Override
    public void setPath(String path) {
        this.path = DefaultCookie.validateValue("path", path);
    }

    @Override
    public String getComment() {
        return this.comment;
    }

    @Override
    public void setComment(String comment) {
        this.comment = DefaultCookie.validateValue("comment", comment);
    }

    @Override
    public String getCommentUrl() {
        return this.commentUrl;
    }

    @Override
    public void setCommentUrl(String commentUrl) {
        this.commentUrl = DefaultCookie.validateValue("commentUrl", commentUrl);
    }

    @Override
    public boolean isDiscard() {
        return this.discard;
    }

    @Override
    public void setDiscard(boolean discard) {
        this.discard = discard;
    }

    @Override
    public Set<Integer> getPorts() {
        if (this.unmodifiablePorts == null) {
            this.unmodifiablePorts = Collections.unmodifiableSet(this.ports);
        }
        return this.unmodifiablePorts;
    }

    @Override
    public void setPorts(int ... ports) {
        if (ports == null) {
            throw new NullPointerException("ports");
        }
        int[] portsCopy = (int[])ports.clone();
        if (portsCopy.length == 0) {
            this.ports = Collections.emptySet();
            this.unmodifiablePorts = this.ports;
        } else {
            TreeSet<Integer> newPorts = new TreeSet<Integer>();
            for (int p2 : portsCopy) {
                if (p2 <= 0 || p2 > 65535) {
                    throw new IllegalArgumentException("port out of range: " + p2);
                }
                newPorts.add(p2);
            }
            this.ports = newPorts;
            this.unmodifiablePorts = null;
        }
    }

    @Override
    public void setPorts(Iterable<Integer> ports) {
        TreeSet<Integer> newPorts = new TreeSet<Integer>();
        for (int p2 : ports) {
            if (p2 <= 0 || p2 > 65535) {
                throw new IllegalArgumentException("port out of range: " + p2);
            }
            newPorts.add(p2);
        }
        if (newPorts.isEmpty()) {
            this.ports = Collections.emptySet();
            this.unmodifiablePorts = this.ports;
        } else {
            this.ports = newPorts;
            this.unmodifiablePorts = null;
        }
    }

    @Override
    public long getMaxAge() {
        return this.maxAge;
    }

    @Override
    public void setMaxAge(long maxAge) {
        this.maxAge = maxAge;
    }

    @Override
    public int getVersion() {
        return this.version;
    }

    @Override
    public void setVersion(int version) {
        this.version = version;
    }

    @Override
    public boolean isSecure() {
        return this.secure;
    }

    @Override
    public void setSecure(boolean secure) {
        this.secure = secure;
    }

    @Override
    public boolean isHttpOnly() {
        return this.httpOnly;
    }

    @Override
    public void setHttpOnly(boolean httpOnly) {
        this.httpOnly = httpOnly;
    }

    public int hashCode() {
        return this.getName().hashCode();
    }

    public boolean equals(Object o2) {
        if (!(o2 instanceof Cookie)) {
            return false;
        }
        Cookie that = (Cookie)o2;
        if (!this.getName().equalsIgnoreCase(that.getName())) {
            return false;
        }
        if (this.getPath() == null) {
            if (that.getPath() != null) {
                return false;
            }
        } else {
            if (that.getPath() == null) {
                return false;
            }
            if (!this.getPath().equals(that.getPath())) {
                return false;
            }
        }
        if (this.getDomain() == null) {
            return that.getDomain() == null;
        }
        if (that.getDomain() == null) {
            return false;
        }
        return this.getDomain().equalsIgnoreCase(that.getDomain());
    }

    @Override
    public int compareTo(Cookie c2) {
        int v2 = this.getName().compareToIgnoreCase(c2.getName());
        if (v2 != 0) {
            return v2;
        }
        if (this.getPath() == null) {
            if (c2.getPath() != null) {
                return -1;
            }
        } else {
            if (c2.getPath() == null) {
                return 1;
            }
            v2 = this.getPath().compareTo(c2.getPath());
            if (v2 != 0) {
                return v2;
            }
        }
        if (this.getDomain() == null) {
            if (c2.getDomain() != null) {
                return -1;
            }
        } else {
            if (c2.getDomain() == null) {
                return 1;
            }
            v2 = this.getDomain().compareToIgnoreCase(c2.getDomain());
            return v2;
        }
        return 0;
    }

    public String toString() {
        StringBuilder buf = new StringBuilder();
        buf.append(this.getName());
        buf.append('=');
        buf.append(this.getValue());
        if (this.getDomain() != null) {
            buf.append(", domain=");
            buf.append(this.getDomain());
        }
        if (this.getPath() != null) {
            buf.append(", path=");
            buf.append(this.getPath());
        }
        if (this.getComment() != null) {
            buf.append(", comment=");
            buf.append(this.getComment());
        }
        if (this.getMaxAge() >= 0L) {
            buf.append(", maxAge=");
            buf.append(this.getMaxAge());
            buf.append('s');
        }
        if (this.isSecure()) {
            buf.append(", secure");
        }
        if (this.isHttpOnly()) {
            buf.append(", HTTPOnly");
        }
        return buf.toString();
    }

    private static String validateValue(String name, String value) {
        if (value == null) {
            return null;
        }
        if ((value = value.trim()).isEmpty()) {
            return null;
        }
        for (int i2 = 0; i2 < value.length(); ++i2) {
            char c2 = value.charAt(i2);
            switch (c2) {
                case '\n': 
                case '\u000b': 
                case '\f': 
                case '\r': 
                case ';': {
                    throw new IllegalArgumentException(name + " contains one of the following prohibited characters: " + ";\\r\\n\\f\\v (" + value + ')');
                }
            }
        }
        return value;
    }
}

