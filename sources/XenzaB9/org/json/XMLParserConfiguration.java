// 
// Decompiled by Procyon v0.6.0
// 

package org.json;

import java.util.Collection;
import java.util.HashSet;
import java.util.HashMap;
import java.util.Collections;
import java.util.Set;
import java.util.Map;

public class XMLParserConfiguration
{
    public static final int UNDEFINED_MAXIMUM_NESTING_DEPTH = -1;
    public static final int DEFAULT_MAXIMUM_NESTING_DEPTH = 512;
    public static final XMLParserConfiguration ORIGINAL;
    public static final XMLParserConfiguration KEEP_STRINGS;
    private boolean keepStrings;
    private String cDataTagName;
    private boolean convertNilAttributeToNull;
    private Map<String, XMLXsiTypeConverter<?>> xsiTypeMap;
    private Set<String> forceList;
    private int maxNestingDepth;
    
    public XMLParserConfiguration() {
        this.maxNestingDepth = 512;
        this.keepStrings = false;
        this.cDataTagName = "content";
        this.convertNilAttributeToNull = false;
        this.xsiTypeMap = Collections.emptyMap();
        this.forceList = Collections.emptySet();
    }
    
    @Deprecated
    public XMLParserConfiguration(final boolean keepStrings) {
        this(keepStrings, "content", false);
    }
    
    @Deprecated
    public XMLParserConfiguration(final String cDataTagName) {
        this(false, cDataTagName, false);
    }
    
    @Deprecated
    public XMLParserConfiguration(final boolean keepStrings, final String cDataTagName) {
        this.maxNestingDepth = 512;
        this.keepStrings = keepStrings;
        this.cDataTagName = cDataTagName;
        this.convertNilAttributeToNull = false;
    }
    
    @Deprecated
    public XMLParserConfiguration(final boolean keepStrings, final String cDataTagName, final boolean convertNilAttributeToNull) {
        this.maxNestingDepth = 512;
        this.keepStrings = keepStrings;
        this.cDataTagName = cDataTagName;
        this.convertNilAttributeToNull = convertNilAttributeToNull;
    }
    
    private XMLParserConfiguration(final boolean keepStrings, final String cDataTagName, final boolean convertNilAttributeToNull, final Map<String, XMLXsiTypeConverter<?>> xsiTypeMap, final Set<String> forceList, final int maxNestingDepth) {
        this.maxNestingDepth = 512;
        this.keepStrings = keepStrings;
        this.cDataTagName = cDataTagName;
        this.convertNilAttributeToNull = convertNilAttributeToNull;
        this.xsiTypeMap = Collections.unmodifiableMap((Map<? extends String, ? extends XMLXsiTypeConverter<?>>)xsiTypeMap);
        this.forceList = Collections.unmodifiableSet((Set<? extends String>)forceList);
        this.maxNestingDepth = maxNestingDepth;
    }
    
    @Override
    protected XMLParserConfiguration clone() {
        return new XMLParserConfiguration(this.keepStrings, this.cDataTagName, this.convertNilAttributeToNull, this.xsiTypeMap, this.forceList, this.maxNestingDepth);
    }
    
    public boolean isKeepStrings() {
        return this.keepStrings;
    }
    
    public XMLParserConfiguration withKeepStrings(final boolean newVal) {
        final XMLParserConfiguration newConfig = this.clone();
        newConfig.keepStrings = newVal;
        return newConfig;
    }
    
    public String getcDataTagName() {
        return this.cDataTagName;
    }
    
    public XMLParserConfiguration withcDataTagName(final String newVal) {
        final XMLParserConfiguration newConfig = this.clone();
        newConfig.cDataTagName = newVal;
        return newConfig;
    }
    
    public boolean isConvertNilAttributeToNull() {
        return this.convertNilAttributeToNull;
    }
    
    public XMLParserConfiguration withConvertNilAttributeToNull(final boolean newVal) {
        final XMLParserConfiguration newConfig = this.clone();
        newConfig.convertNilAttributeToNull = newVal;
        return newConfig;
    }
    
    public Map<String, XMLXsiTypeConverter<?>> getXsiTypeMap() {
        return this.xsiTypeMap;
    }
    
    public XMLParserConfiguration withXsiTypeMap(final Map<String, XMLXsiTypeConverter<?>> xsiTypeMap) {
        final XMLParserConfiguration newConfig = this.clone();
        final Map<String, XMLXsiTypeConverter<?>> cloneXsiTypeMap = new HashMap<String, XMLXsiTypeConverter<?>>(xsiTypeMap);
        newConfig.xsiTypeMap = Collections.unmodifiableMap((Map<? extends String, ? extends XMLXsiTypeConverter<?>>)cloneXsiTypeMap);
        return newConfig;
    }
    
    public Set<String> getForceList() {
        return this.forceList;
    }
    
    public XMLParserConfiguration withForceList(final Set<String> forceList) {
        final XMLParserConfiguration newConfig = this.clone();
        final Set<String> cloneForceList = new HashSet<String>(forceList);
        newConfig.forceList = Collections.unmodifiableSet((Set<? extends String>)cloneForceList);
        return newConfig;
    }
    
    public int getMaxNestingDepth() {
        return this.maxNestingDepth;
    }
    
    public XMLParserConfiguration withMaxNestingDepth(final int maxNestingDepth) {
        final XMLParserConfiguration newConfig = this.clone();
        if (maxNestingDepth > -1) {
            newConfig.maxNestingDepth = maxNestingDepth;
        }
        else {
            newConfig.maxNestingDepth = -1;
        }
        return newConfig;
    }
    
    static {
        ORIGINAL = new XMLParserConfiguration();
        KEEP_STRINGS = new XMLParserConfiguration().withKeepStrings(true);
    }
}
