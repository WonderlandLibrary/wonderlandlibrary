// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.logging.log4j.core.impl;

import org.apache.logging.log4j.core.pattern.PlainTextRenderer;
import org.apache.logging.log4j.core.pattern.TextRenderer;
import java.util.Objects;
import java.io.Serializable;

public final class ExtendedClassInfo implements Serializable
{
    private static final long serialVersionUID = 1L;
    private final boolean exact;
    private final String location;
    private final String version;
    
    public ExtendedClassInfo(final boolean exact, final String location, final String version) {
        this.exact = exact;
        this.location = location;
        this.version = version;
    }
    
    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (!(obj instanceof ExtendedClassInfo)) {
            return false;
        }
        final ExtendedClassInfo other = (ExtendedClassInfo)obj;
        return this.exact == other.exact && Objects.equals(this.location, other.location) && Objects.equals(this.version, other.version);
    }
    
    public boolean getExact() {
        return this.exact;
    }
    
    public String getLocation() {
        return this.location;
    }
    
    public String getVersion() {
        return this.version;
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(this.exact, this.location, this.version);
    }
    
    public void renderOn(final StringBuilder output, final TextRenderer textRenderer) {
        if (!this.exact) {
            textRenderer.render("~", output, "ExtraClassInfo.Inexact");
        }
        textRenderer.render("[", output, "ExtraClassInfo.Container");
        textRenderer.render(this.location, output, "ExtraClassInfo.Location");
        textRenderer.render(":", output, "ExtraClassInfo.ContainerSeparator");
        textRenderer.render(this.version, output, "ExtraClassInfo.Version");
        textRenderer.render("]", output, "ExtraClassInfo.Container");
    }
    
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        this.renderOn(sb, PlainTextRenderer.getInstance());
        return sb.toString();
    }
}
