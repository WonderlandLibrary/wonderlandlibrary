// 
// Decompiled by Procyon v0.5.36
// 

package com.sun.jna.platform.win32.COM.tlb.imp;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.io.InputStream;
import java.io.Reader;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.io.IOException;
import com.sun.jna.platform.win32.COM.TypeInfoUtil;
import com.sun.jna.platform.win32.COM.TypeLibUtil;

public abstract class TlbBase
{
    public static final String CR = "\n";
    public static final String CRCR = "\n\n";
    public static final String TAB = "\t";
    public static final String TABTAB = "\t\t";
    protected TypeLibUtil typeLibUtil;
    protected TypeInfoUtil typeInfoUtil;
    protected int index;
    protected StringBuffer templateBuffer;
    protected StringBuffer classBuffer;
    protected String content;
    protected String filename;
    protected String name;
    public static String[] IUNKNOWN_METHODS;
    public static String[] IDISPATCH_METHODS;
    protected String bindingMode;
    
    public TlbBase(final int index, final TypeLibUtil typeLibUtil, final TypeInfoUtil typeInfoUtil) {
        this(index, typeLibUtil, typeInfoUtil, "dispid");
    }
    
    public TlbBase(final int index, final TypeLibUtil typeLibUtil, final TypeInfoUtil typeInfoUtil, final String bindingMode) {
        this.content = "";
        this.filename = "DefaultFilename";
        this.name = "DefaultName";
        this.bindingMode = "dispid";
        this.index = index;
        this.typeLibUtil = typeLibUtil;
        this.typeInfoUtil = typeInfoUtil;
        this.bindingMode = bindingMode;
        final String filename = this.getClassTemplate();
        try {
            this.readTemplateFile(filename);
            this.classBuffer = this.templateBuffer;
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public void logError(final String msg) {
        this.log("ERROR", msg);
    }
    
    public void logInfo(final String msg) {
        this.log("INFO", msg);
    }
    
    public StringBuffer getClassBuffer() {
        return this.classBuffer;
    }
    
    public void createContent(final String content) {
        this.replaceVariable("content", content);
    }
    
    public void setFilename(String filename) {
        if (!filename.endsWith("java")) {
            filename += ".java";
        }
        this.filename = filename;
    }
    
    public String getFilename() {
        return this.filename;
    }
    
    public String getName() {
        return this.name;
    }
    
    public void setName(final String name) {
        this.name = name;
    }
    
    protected void log(final String level, final String msg) {
        final String _msg = level + " " + this.getTime() + " : " + msg;
        System.out.println(_msg);
    }
    
    private String getTime() {
        final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        return sdf.format(new Date());
    }
    
    protected abstract String getClassTemplate();
    
    protected void readTemplateFile(final String filename) throws IOException {
        this.templateBuffer = new StringBuffer();
        BufferedReader reader = null;
        try {
            final InputStream is = this.getClass().getClassLoader().getResourceAsStream(filename);
            reader = new BufferedReader(new InputStreamReader(is));
            String line = null;
            while ((line = reader.readLine()) != null) {
                this.templateBuffer.append(line + "\n");
            }
        }
        finally {
            if (reader != null) {
                reader.close();
            }
        }
    }
    
    protected void replaceVariable(final String name, String value) {
        if (value == null) {
            value = "";
        }
        final Pattern pattern = Pattern.compile("\\$\\{" + name + "\\}");
        final Matcher matcher = pattern.matcher(this.classBuffer);
        final String replacement = value;
        String result = "";
        while (matcher.find()) {
            result = matcher.replaceAll(replacement);
        }
        if (result.length() > 0) {
            this.classBuffer = new StringBuffer(result);
        }
    }
    
    protected void createPackageName(final String packagename) {
        this.replaceVariable("packagename", packagename);
    }
    
    protected void createClassName(final String name) {
        this.replaceVariable("classname", name);
    }
    
    protected boolean isReservedMethod(final String method) {
        for (int i = 0; i < TlbBase.IUNKNOWN_METHODS.length; ++i) {
            if (TlbBase.IUNKNOWN_METHODS[i].equalsIgnoreCase(method)) {
                return true;
            }
        }
        for (int i = 0; i < TlbBase.IDISPATCH_METHODS.length; ++i) {
            if (TlbBase.IDISPATCH_METHODS[i].equalsIgnoreCase(method)) {
                return true;
            }
        }
        return false;
    }
    
    protected boolean isVTableMode() {
        return this.bindingMode.equalsIgnoreCase("vtable");
    }
    
    protected boolean isDispIdMode() {
        return this.bindingMode.equalsIgnoreCase("dispid");
    }
    
    static {
        TlbBase.IUNKNOWN_METHODS = new String[] { "QueryInterface", "AddRef", "Release" };
        TlbBase.IDISPATCH_METHODS = new String[] { "GetTypeInfoCount", "GetTypeInfo", "GetIDsOfNames", "Invoke" };
    }
}
