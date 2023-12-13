/*
 * Decompiled with CFR 0.152.
 */
package org.apache.http.message;

import java.io.Serializable;
import org.apache.http.Header;
import org.apache.http.HeaderElement;
import org.apache.http.ParseException;
import org.apache.http.annotation.Contract;
import org.apache.http.annotation.ThreadingBehavior;
import org.apache.http.message.BasicHeaderValueParser;
import org.apache.http.message.BasicLineFormatter;
import org.apache.http.util.Args;

@Contract(threading=ThreadingBehavior.IMMUTABLE)
public class BasicHeader
implements Header,
Cloneable,
Serializable {
    private static final HeaderElement[] EMPTY_HEADER_ELEMENTS = new HeaderElement[0];
    private static final long serialVersionUID = -5427236326487562174L;
    private final String name;
    private final String value;

    public BasicHeader(String name, String value) {
        this.name = Args.notNull(name, "Name");
        this.value = value;
    }

    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    @Override
    public HeaderElement[] getElements() throws ParseException {
        if (this.getValue() != null) {
            return BasicHeaderValueParser.parseElements(this.getValue(), null);
        }
        return EMPTY_HEADER_ELEMENTS;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public String getValue() {
        return this.value;
    }

    public String toString() {
        return BasicLineFormatter.INSTANCE.formatHeader(null, this).toString();
    }
}

