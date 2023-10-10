// 
// Decompiled by Procyon v0.5.36
// 

package com.sun.jna.platform.win32.COM.tlb;

import com.sun.jna.platform.win32.COM.tlb.imp.TlbCoClass;
import com.sun.jna.platform.win32.COM.tlb.imp.TlbDispInterface;
import com.sun.jna.platform.win32.COM.tlb.imp.TlbInterface;
import com.sun.jna.platform.win32.COM.tlb.imp.TlbEnum;
import com.sun.jna.platform.win32.COM.tlb.imp.TlbBase;
import java.io.IOException;
import java.io.OutputStream;
import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.FileNotFoundException;
import com.sun.jna.platform.win32.OaIdl;
import com.sun.jna.platform.win32.COM.tlb.imp.TlbCmdlineArgs;
import java.io.File;
import com.sun.jna.platform.win32.COM.TypeLibUtil;
import com.sun.jna.platform.win32.COM.tlb.imp.TlbConst;

public class TlbImp implements TlbConst
{
    private TypeLibUtil typeLibUtil;
    private File comRootDir;
    private File outputDir;
    private TlbCmdlineArgs cmdlineArgs;
    
    public static void main(final String[] args) {
        new TlbImp(args);
    }
    
    public TlbImp(final String[] args) {
        this.cmdlineArgs = new TlbCmdlineArgs(args);
        if (this.cmdlineArgs.isTlbId()) {
            final String clsid = this.cmdlineArgs.getRequiredParam("tlb.id");
            final int majorVersion = this.cmdlineArgs.getIntParam("tlb.major.version");
            final int minorVersion = this.cmdlineArgs.getIntParam("tlb.minor.version");
            this.typeLibUtil = new TypeLibUtil(clsid, majorVersion, minorVersion);
            this.startCOM2Java();
        }
        else if (this.cmdlineArgs.isTlbFile()) {
            final String file = this.cmdlineArgs.getRequiredParam("tlb.file");
            this.typeLibUtil = new TypeLibUtil(file);
            this.startCOM2Java();
        }
        else {
            this.cmdlineArgs.showCmdHelp();
        }
    }
    
    public void startCOM2Java() {
        try {
            this.createDir();
            final String bindingMode = this.cmdlineArgs.getBindingMode();
            final int typeInfoCount = this.typeLibUtil.getTypeInfoCount();
            for (int i = 0; i < typeInfoCount; ++i) {
                final OaIdl.TYPEKIND typekind = this.typeLibUtil.getTypeInfoType(i);
                if (typekind.value == 0) {
                    this.createCOMEnum(i, this.getPackageName(), this.typeLibUtil);
                }
                else if (typekind.value == 1) {
                    logInfo("'TKIND_RECORD' objects are currently not supported!");
                }
                else if (typekind.value == 2) {
                    logInfo("'TKIND_MODULE' objects are currently not supported!");
                }
                else if (typekind.value == 3) {
                    this.createCOMInterface(i, this.getPackageName(), this.typeLibUtil);
                }
                else if (typekind.value == 4) {
                    this.createCOMDispInterface(i, this.getPackageName(), this.typeLibUtil);
                }
                else if (typekind.value == 5) {
                    this.createCOMCoClass(i, this.getPackageName(), this.typeLibUtil, bindingMode);
                }
                else if (typekind.value == 6) {
                    logInfo("'TKIND_ALIAS' objects are currently not supported!");
                }
                else if (typekind.value == 7) {
                    logInfo("'TKIND_UNION' objects are currently not supported!");
                }
            }
            logInfo(typeInfoCount + " files sucessfully written to: " + this.comRootDir.toString());
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private void createDir() throws FileNotFoundException {
        final String _outputDir = this.cmdlineArgs.getParam("output.dir");
        final String path = "_jnaCOM_" + System.currentTimeMillis() + "\\myPackage\\" + this.typeLibUtil.getName().toLowerCase() + "\\";
        if (_outputDir != null) {
            this.comRootDir = new File(_outputDir + "\\" + path);
        }
        else {
            final String tmp = System.getProperty("java.io.tmpdir");
            this.comRootDir = new File(tmp + "\\" + path);
        }
        if (this.comRootDir.exists()) {
            this.comRootDir.delete();
        }
        if (this.comRootDir.mkdirs()) {
            logInfo("Output directory sucessfully created.");
            return;
        }
        throw new FileNotFoundException("Output directory NOT sucessfully created to: " + this.comRootDir.toString());
    }
    
    private String getPackageName() {
        return "myPackage." + this.typeLibUtil.getName().toLowerCase();
    }
    
    private void writeTextFile(final String filename, final String str) throws IOException {
        final String file = this.comRootDir + File.separator + filename;
        final BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(file));
        bos.write(str.getBytes());
        bos.close();
    }
    
    private void writeTlbClass(final TlbBase tlbBase) throws IOException {
        final StringBuffer classBuffer = tlbBase.getClassBuffer();
        this.writeTextFile(tlbBase.getFilename(), classBuffer.toString());
    }
    
    private void createCOMEnum(final int index, final String packagename, final TypeLibUtil typeLibUtil) throws IOException {
        final TlbEnum tlbEnum = new TlbEnum(index, packagename, typeLibUtil);
        this.writeTlbClass(tlbEnum);
    }
    
    private void createCOMInterface(final int index, final String packagename, final TypeLibUtil typeLibUtil) throws IOException {
        final TlbInterface tlbInterface = new TlbInterface(index, packagename, typeLibUtil);
        this.writeTlbClass(tlbInterface);
    }
    
    private void createCOMDispInterface(final int index, final String packagename, final TypeLibUtil typeLibUtil) throws IOException {
        final TlbDispInterface tlbDispatch = new TlbDispInterface(index, packagename, typeLibUtil);
        this.writeTlbClass(tlbDispatch);
    }
    
    private void createCOMCoClass(final int index, final String packagename, final TypeLibUtil typeLibUtil, final String bindingMode) throws IOException {
        final TlbCoClass tlbCoClass = new TlbCoClass(index, this.getPackageName(), typeLibUtil, bindingMode);
        this.writeTlbClass(tlbCoClass);
    }
    
    public static void logInfo(final String msg) {
        System.out.println(msg);
    }
}
