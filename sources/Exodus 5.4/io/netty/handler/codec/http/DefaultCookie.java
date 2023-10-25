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
    private String value;
    private Set<Integer> ports = Collections.emptySet();
    private String domain;
    private String commentUrl;
    private String comment;
    private boolean discard;
    private String path;
    private boolean secure;
    private final String name;
    private int version;
    private Set<Integer> unmodifiablePorts = this.ports;
    private long maxAge = Long.MIN_VALUE;
    private boolean httpOnly;

    @Override
    public void setVersion(int n) {
        this.version = n;
    }

    @Override
    public void setValue(String string) {
        if (string == null) {
            throw new NullPointerException("value");
        }
        this.value = string;
    }

    public int hashCode() {
        return this.getName().hashCode();
    }

    @Override
    public void setPorts(Iterable<Integer> iterable) {
        TreeSet<Integer> treeSet = new TreeSet<Integer>();
        for (int n : iterable) {
            if (n <= 0 || n > 65535) {
                throw new IllegalArgumentException("port out of range: " + n);
            }
            treeSet.add(n);
        }
        if (treeSet.isEmpty()) {
            this.ports = Collections.emptySet();
            this.unmodifiablePorts = this.ports;
        } else {
            this.ports = treeSet;
            this.unmodifiablePorts = null;
        }
    }

    @Override
    public boolean isHttpOnly() {
        return this.httpOnly;
    }

    public boolean equals(Object object) {
        if (!(object instanceof Cookie)) {
            return false;
        }
        Cookie cookie = (Cookie)object;
        if (!this.getName().equalsIgnoreCase(cookie.getName())) {
            return false;
        }
        if (this.getPath() == null) {
            if (cookie.getPath() != null) {
                return false;
            }
        } else {
            if (cookie.getPath() == null) {
                return false;
            }
            if (!this.getPath().equals(cookie.getPath())) {
                return false;
            }
        }
        if (this.getDomain() == null) {
            return cookie.getDomain() == null;
        }
        if (cookie.getDomain() == null) {
            return false;
        }
        return this.getDomain().equalsIgnoreCase(cookie.getDomain());
    }

    @Override
    public String getValue() {
        return this.value;
    }

    @Override
    public void setComment(String string) {
        this.comment = DefaultCookie.validateValue("comment", string);
    }

    private static String validateValue(String string, String string2) {
        if (string2 == null) {
            return null;
        }
        if ((string2 = string2.trim()).isEmpty()) {
            return null;
        }
        for (int i = 0; i < string2.length(); ++i) {
            char c = string2.charAt(i);
            switch (c) {
                case '\n': 
                case '\u000b': 
                case '\f': 
                case '\r': 
                case ';': {
                    throw new IllegalArgumentException(string + " contains one of the following prohibited characters: " + ";\\r\\n\\f\\v (" + string2 + ')');
                }
            }
        }
        return string2;
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(this.getName());
        stringBuilder.append('=');
        stringBuilder.append(this.getValue());
        if (this.getDomain() != null) {
            stringBuilder.append(", domain=");
            stringBuilder.append(this.getDomain());
        }
        if (this.getPath() != null) {
            stringBuilder.append(", path=");
            stringBuilder.append(this.getPath());
        }
        if (this.getComment() != null) {
            stringBuilder.append(", comment=");
            stringBuilder.append(this.getComment());
        }
        if (this.getMaxAge() >= 0L) {
            stringBuilder.append(", maxAge=");
            stringBuilder.append(this.getMaxAge());
            stringBuilder.append('s');
        }
        if (this.isSecure()) {
            stringBuilder.append(", secure");
        }
        if (this.isHttpOnly()) {
            stringBuilder.append(", HTTPOnly");
        }
        return stringBuilder.toString();
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public boolean isSecure() {
        return this.secure;
    }

    @Override
    public boolean isDiscard() {
        return this.discard;
    }

    @Override
    public String getComment() {
        return this.comment;
    }

    @Override
    public void setPath(String string) {
        this.path = DefaultCookie.validateValue("path", string);
    }

    @Override
    public int getVersion() {
        return this.version;
    }

    @Override
    public void setDiscard(boolean bl) {
        this.discard = bl;
    }

    public DefaultCookie(String string, String string2) {
        if (string == null) {
            throw new NullPointerException("name");
        }
        if ((string = string.trim()).isEmpty()) {
            throw new IllegalArgumentException("empty name");
        }
        for (int i = 0; i < string.length(); ++i) {
            char c = string.charAt(i);
            if (c > '\u007f') {
                throw new IllegalArgumentException("name contains non-ascii character: " + string);
            }
            switch (c) {
                case '\t': 
                case '\n': 
                case '\u000b': 
                case '\f': 
                case '\r': 
                case ' ': 
                case ',': 
                case ';': 
                case '=': {
                    throw new IllegalArgumentException("name contains one of the following prohibited characters: =,; \\t\\r\\n\\v\\f: " + string);
                }
            }
        }
        if (string.charAt(0) == '$') {
            throw new IllegalArgumentException("name starting with '$' not allowed: " + string);
        }
        this.name = string;
        this.setValue(string2);
    }

    @Override
    public String getDomain() {
        return this.domain;
    }

    @Override
    public void setSecure(boolean bl) {
        this.secure = bl;
    }

    @Override
    public void setDomain(String string) {
        this.domain = DefaultCookie.validateValue("domain", string);
    }

    @Override
    public void setMaxAge(long l) {
        this.maxAge = l;
    }

    @Override
    public void setPorts(int ... nArray) {
        if (nArray == null) {
            throw new NullPointerException("ports");
        }
        int[] nArray2 = (int[])nArray.clone();
        if (nArray2.length == 0) {
            this.ports = Collections.emptySet();
            this.unmodifiablePorts = this.ports;
        } else {
            TreeSet<Integer> treeSet = new TreeSet<Integer>();
            for (int n : nArray2) {
                if (n <= 0 || n > 65535) {
                    throw new IllegalArgumentException("port out of range: " + n);
                }
                treeSet.add(n);
            }
            this.ports = treeSet;
            this.unmodifiablePorts = null;
        }
    }

    @Override
    public void setCommentUrl(String string) {
        this.commentUrl = DefaultCookie.validateValue("commentUrl", string);
    }

    @Override
    public void setHttpOnly(boolean bl) {
        this.httpOnly = bl;
    }

    @Override
    public Set<Integer> getPorts() {
        if (this.unmodifiablePorts == null) {
            this.unmodifiablePorts = Collections.unmodifiableSet(this.ports);
        }
        return this.unmodifiablePorts;
    }

    @Override
    public int compareTo(Cookie cookie) {
        int n = this.getName().compareToIgnoreCase(cookie.getName());
        if (n != 0) {
            return n;
        }
        if (this.getPath() == null) {
            if (cookie.getPath() != null) {
                return -1;
            }
        } else {
            if (cookie.getPath() == null) {
                return 1;
            }
            n = this.getPath().compareTo(cookie.getPath());
            if (n != 0) {
                return n;
            }
        }
        if (this.getDomain() == null) {
            if (cookie.getDomain() != null) {
                return -1;
            }
        } else {
            if (cookie.getDomain() == null) {
                return 1;
            }
            n = this.getDomain().compareToIgnoreCase(cookie.getDomain());
            return n;
        }
        return 0;
    }

    @Override
    public String getCommentUrl() {
        return this.commentUrl;
    }

    @Override
    public long getMaxAge() {
        return this.maxAge;
    }

    @Override
    public String getPath() {
        return this.path;
    }
}

