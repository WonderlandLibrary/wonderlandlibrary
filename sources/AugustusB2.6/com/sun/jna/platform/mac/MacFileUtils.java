// 
// Decompiled by Procyon v0.5.36
// 

package com.sun.jna.platform.mac;

import com.sun.jna.Structure;
import com.sun.jna.Native;
import com.sun.jna.ptr.PointerByReference;
import com.sun.jna.Library;
import java.util.List;
import java.io.IOException;
import com.sun.jna.ptr.ByteByReference;
import java.util.ArrayList;
import java.io.File;
import com.sun.jna.platform.FileUtils;

public class MacFileUtils extends FileUtils
{
    @Override
    public boolean hasTrash() {
        return true;
    }
    
    @Override
    public void moveToTrash(final File... files) throws IOException {
        final List<String> failed = new ArrayList<String>();
        for (final File src : files) {
            final FileManager.FSRef fsref = new FileManager.FSRef();
            int status = FileManager.INSTANCE.FSPathMakeRefWithOptions(src.getAbsolutePath(), 1, fsref, null);
            if (status != 0) {
                failed.add(src + " (FSRef: " + status + ")");
            }
            else {
                status = FileManager.INSTANCE.FSMoveObjectToTrashSync(fsref, null, 0);
                if (status != 0) {
                    failed.add(src + " (" + status + ")");
                }
            }
        }
        if (failed.size() > 0) {
            throw new IOException("The following files could not be trashed: " + failed);
        }
    }
    
    public interface FileManager extends Library
    {
        public static final FileManager INSTANCE = Native.load("CoreServices", FileManager.class);
        public static final int kFSFileOperationDefaultOptions = 0;
        public static final int kFSFileOperationsOverwrite = 1;
        public static final int kFSFileOperationsSkipSourcePermissionErrors = 2;
        public static final int kFSFileOperationsDoNotMoveAcrossVolumes = 4;
        public static final int kFSFileOperationsSkipPreflight = 8;
        public static final int kFSPathDefaultOptions = 0;
        public static final int kFSPathMakeRefDoNotFollowLeafSymlink = 1;
        
        int FSRefMakePath(final FSRef p0, final byte[] p1, final int p2);
        
        int FSPathMakeRef(final String p0, final int p1, final ByteByReference p2);
        
        int FSPathMakeRefWithOptions(final String p0, final int p1, final FSRef p2, final ByteByReference p3);
        
        int FSPathMoveObjectToTrashSync(final String p0, final PointerByReference p1, final int p2);
        
        int FSMoveObjectToTrashSync(final FSRef p0, final FSRef p1, final int p2);
        
        @FieldOrder({ "hidden" })
        public static class FSRef extends Structure
        {
            public byte[] hidden;
            
            public FSRef() {
                this.hidden = new byte[80];
            }
        }
    }
}
