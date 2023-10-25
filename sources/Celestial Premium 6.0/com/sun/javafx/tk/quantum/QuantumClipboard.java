/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  javafx.scene.image.Image
 *  javafx.scene.image.PixelReader
 *  javafx.scene.image.WritablePixelFormat
 *  javafx.scene.input.DataFormat
 *  javafx.scene.input.TransferMode
 *  javafx.util.Pair
 */
package com.sun.javafx.tk.quantum;

import com.sun.glass.ui.Application;
import com.sun.glass.ui.ClipboardAssistance;
import com.sun.glass.ui.Pixels;
import com.sun.javafx.tk.ImageLoader;
import com.sun.javafx.tk.PermissionHelper;
import com.sun.javafx.tk.TKClipboard;
import com.sun.javafx.tk.Toolkit;
import com.sun.javafx.tk.quantum.PixelUtils;
import com.sun.prism.Image;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FilePermission;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.ObjectStreamClass;
import java.io.OutputStream;
import java.io.Serializable;
import java.net.MalformedURLException;
import java.net.SocketPermission;
import java.net.URL;
import java.nio.ByteBuffer;
import java.security.AccessControlContext;
import java.security.Permission;
import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javafx.scene.image.PixelReader;
import javafx.scene.image.WritablePixelFormat;
import javafx.scene.input.DataFormat;
import javafx.scene.input.TransferMode;
import javafx.util.Pair;

final class QuantumClipboard
implements TKClipboard {
    private ClipboardAssistance systemAssistant;
    private AccessControlContext accessContext = null;
    private boolean isCaching;
    private List<Pair<DataFormat, Object>> dataCache;
    private Set<TransferMode> transferModesCache;
    private javafx.scene.image.Image dragImage = null;
    private double dragOffsetX = 0.0;
    private double dragOffsetY = 0.0;
    private static ClipboardAssistance currentDragboard;
    private static final Pattern findTagIMG;

    private QuantumClipboard() {
    }

    @Override
    public void setSecurityContext(AccessControlContext accessControlContext) {
        if (this.accessContext != null) {
            throw new RuntimeException("Clipboard security context has been already set!");
        }
        this.accessContext = accessControlContext;
    }

    private AccessControlContext getAccessControlContext() {
        if (this.accessContext == null) {
            throw new RuntimeException("Clipboard security context has not been set!");
        }
        return this.accessContext;
    }

    public static QuantumClipboard getClipboardInstance(ClipboardAssistance clipboardAssistance) {
        QuantumClipboard quantumClipboard = new QuantumClipboard();
        quantumClipboard.systemAssistant = clipboardAssistance;
        quantumClipboard.isCaching = false;
        return quantumClipboard;
    }

    static ClipboardAssistance getCurrentDragboard() {
        return currentDragboard;
    }

    static void releaseCurrentDragboard() {
        currentDragboard = null;
    }

    public static QuantumClipboard getDragboardInstance(ClipboardAssistance clipboardAssistance, boolean bl) {
        QuantumClipboard quantumClipboard = new QuantumClipboard();
        quantumClipboard.systemAssistant = clipboardAssistance;
        quantumClipboard.isCaching = true;
        if (bl) {
            currentDragboard = clipboardAssistance;
        }
        return quantumClipboard;
    }

    public static int transferModesToClipboardActions(Set<TransferMode> set) {
        int n = 0;
        block5: for (TransferMode transferMode : set) {
            switch (transferMode) {
                case COPY: {
                    n |= 1;
                    continue block5;
                }
                case MOVE: {
                    n |= 2;
                    continue block5;
                }
                case LINK: {
                    n |= 0x40000000;
                    continue block5;
                }
            }
            throw new IllegalArgumentException("unsupported TransferMode " + set);
        }
        return n;
    }

    public void setSupportedTransferMode(Set<TransferMode> set) {
        if (this.isCaching) {
            this.transferModesCache = set;
        }
        int n = QuantumClipboard.transferModesToClipboardActions(set);
        this.systemAssistant.setSupportedActions(n);
    }

    public static Set<TransferMode> clipboardActionsToTransferModes(int n) {
        EnumSet<TransferMode> enumSet = EnumSet.noneOf(TransferMode.class);
        if ((n & 1) != 0) {
            enumSet.add(TransferMode.COPY);
        }
        if ((n & 2) != 0) {
            enumSet.add(TransferMode.MOVE);
        }
        if ((n & 0x40000000) != 0) {
            enumSet.add(TransferMode.LINK);
        }
        return enumSet;
    }

    @Override
    public Set<TransferMode> getTransferModes() {
        if (this.transferModesCache != null) {
            return EnumSet.copyOf(this.transferModesCache);
        }
        ClipboardAssistance clipboardAssistance = currentDragboard != null ? currentDragboard : this.systemAssistant;
        Set<TransferMode> set = QuantumClipboard.clipboardActionsToTransferModes(clipboardAssistance.getSupportedSourceActions());
        return set;
    }

    @Override
    public void setDragView(javafx.scene.image.Image image) {
        this.dragImage = image;
    }

    @Override
    public void setDragViewOffsetX(double d) {
        this.dragOffsetX = d;
    }

    @Override
    public void setDragViewOffsetY(double d) {
        this.dragOffsetY = d;
    }

    @Override
    public javafx.scene.image.Image getDragView() {
        return this.dragImage;
    }

    @Override
    public double getDragViewOffsetX() {
        return this.dragOffsetX;
    }

    @Override
    public double getDragViewOffsetY() {
        return this.dragOffsetY;
    }

    public void close() {
        this.systemAssistant.close();
    }

    public void flush() {
        if (this.isCaching) {
            this.putContentToPeer(this.dataCache.toArray((T[])new Pair[0]));
        }
        this.clearCache();
        this.clearDragView();
        this.systemAssistant.flush();
    }

    @Override
    public Object getContent(DataFormat dataFormat) {
        ClipboardAssistance clipboardAssistance;
        if (this.dataCache != null) {
            for (Pair<DataFormat, Object> pair : this.dataCache) {
                if (pair.getKey() != dataFormat) continue;
                return pair.getValue();
            }
            return null;
        }
        ClipboardAssistance clipboardAssistance2 = clipboardAssistance = currentDragboard != null ? currentDragboard : this.systemAssistant;
        if (dataFormat == DataFormat.IMAGE) {
            return this.readImage();
        }
        if (dataFormat == DataFormat.URL) {
            return clipboardAssistance.getData("text/uri-list");
        }
        if (dataFormat == DataFormat.FILES) {
            Object object = clipboardAssistance.getData("application/x-java-file-list");
            if (object == null) {
                return Collections.emptyList();
            }
            String[] arrstring = (String[])object;
            ArrayList<File> arrayList = new ArrayList<File>(arrstring.length);
            for (int i = 0; i < arrstring.length; ++i) {
                arrayList.add(new File(arrstring[i]));
            }
            return arrayList;
        }
        for (String string : dataFormat.getIdentifiers()) {
            Object object = clipboardAssistance.getData(string);
            if (object instanceof ByteBuffer) {
                try {
                    ByteBuffer byteBuffer = (ByteBuffer)object;
                    ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(byteBuffer.array());
                    ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream){

                        @Override
                        protected Class<?> resolveClass(ObjectStreamClass objectStreamClass) throws IOException, ClassNotFoundException {
                            return Class.forName(objectStreamClass.getName(), false, Thread.currentThread().getContextClassLoader());
                        }
                    };
                    object = objectInputStream.readObject();
                }
                catch (IOException iOException) {
                }
                catch (ClassNotFoundException classNotFoundException) {
                    // empty catch block
                }
            }
            if (object == null) continue;
            return object;
        }
        return null;
    }

    private static javafx.scene.image.Image convertObjectToImage(Object object) {
        Pixels pixels;
        Object object2;
        if (object instanceof javafx.scene.image.Image) {
            return (javafx.scene.image.Image)object;
        }
        if (object instanceof ByteBuffer) {
            object2 = (ByteBuffer)object;
            try {
                ((ByteBuffer)object2).rewind();
                int n = ((ByteBuffer)object2).getInt();
                int n2 = ((ByteBuffer)object2).getInt();
                pixels = Application.GetApplication().createPixels(n, n2, ((ByteBuffer)object2).slice());
            }
            catch (Exception exception) {
                return null;
            }
        } else if (object instanceof Pixels) {
            pixels = (Pixels)object;
        } else {
            return null;
        }
        object2 = PixelUtils.pixelsToImage(pixels);
        ImageLoader imageLoader = Toolkit.getToolkit().loadPlatformImage(object2);
        return Toolkit.getImageAccessor().fromPlatformImage(imageLoader);
    }

    private javafx.scene.image.Image readImage() {
        ClipboardAssistance clipboardAssistance = currentDragboard != null ? currentDragboard : this.systemAssistant;
        Object object = clipboardAssistance.getData("application/x-java-rawimage");
        if (object == null) {
            String string;
            Object object2 = clipboardAssistance.getData("text/html");
            if (object2 != null && (string = this.parseIMG(object2)) != null) {
                try {
                    SecurityManager securityManager = System.getSecurityManager();
                    if (securityManager != null) {
                        Object object3;
                        AccessControlContext accessControlContext = this.getAccessControlContext();
                        URL uRL = new URL(string);
                        String string2 = uRL.getProtocol();
                        if (string2.equalsIgnoreCase("jar")) {
                            object3 = uRL.getFile();
                            uRL = new URL((String)object3);
                            string2 = uRL.getProtocol();
                        }
                        if (string2.equalsIgnoreCase("file")) {
                            object3 = new FilePermission(uRL.getFile(), "read");
                            securityManager.checkPermission((Permission)object3, accessControlContext);
                        } else if (string2.equalsIgnoreCase("ftp") || string2.equalsIgnoreCase("http") || string2.equalsIgnoreCase("https")) {
                            int n = uRL.getPort();
                            String string3 = n == -1 ? uRL.getHost() : uRL.getHost() + ":" + n;
                            SocketPermission socketPermission = new SocketPermission(string3, "connect");
                            securityManager.checkPermission(socketPermission, accessControlContext);
                        } else {
                            PermissionHelper.checkClipboardPermission(accessControlContext);
                        }
                    }
                    return new javafx.scene.image.Image(string);
                }
                catch (MalformedURLException malformedURLException) {
                    return null;
                }
                catch (SecurityException securityException) {
                    return null;
                }
            }
            return null;
        }
        return QuantumClipboard.convertObjectToImage(object);
    }

    private String parseIMG(Object object) {
        if (object == null) {
            return null;
        }
        if (!(object instanceof String)) {
            return null;
        }
        String string = (String)object;
        Matcher matcher = findTagIMG.matcher(string);
        if (matcher.find()) {
            return matcher.group(1);
        }
        return null;
    }

    private boolean placeImage(javafx.scene.image.Image image) {
        if (image == null) {
            return false;
        }
        String string = image.getUrl();
        if (string == null || PixelUtils.supportedFormatType(string)) {
            Image image2 = (Image)Toolkit.getImageAccessor().getPlatformImage(image);
            Pixels pixels = PixelUtils.imageToPixels(image2);
            if (pixels != null) {
                this.systemAssistant.setData("application/x-java-rawimage", pixels);
                return true;
            }
            return false;
        }
        this.systemAssistant.setData("text/uri-list", string);
        return true;
    }

    @Override
    public Set<DataFormat> getContentTypes() {
        HashSet<DataFormat> hashSet = new HashSet<DataFormat>();
        if (this.dataCache != null) {
            for (Pair<DataFormat, Object> pair : this.dataCache) {
                hashSet.add((DataFormat)pair.getKey());
            }
            return hashSet;
        }
        ClipboardAssistance clipboardAssistance = currentDragboard != null ? currentDragboard : this.systemAssistant;
        String[] arrstring = clipboardAssistance.getMimeTypes();
        if (arrstring == null) {
            return hashSet;
        }
        for (String string : arrstring) {
            if (string.equalsIgnoreCase("application/x-java-rawimage")) {
                hashSet.add(DataFormat.IMAGE);
                continue;
            }
            if (string.equalsIgnoreCase("text/uri-list")) {
                hashSet.add(DataFormat.URL);
                continue;
            }
            if (string.equalsIgnoreCase("application/x-java-file-list")) {
                hashSet.add(DataFormat.FILES);
                continue;
            }
            if (string.equalsIgnoreCase("text/html")) {
                hashSet.add(DataFormat.HTML);
                try {
                    if (this.parseIMG(clipboardAssistance.getData("text/html")) == null) continue;
                    hashSet.add(DataFormat.IMAGE);
                }
                catch (Exception exception) {}
                continue;
            }
            DataFormat dataFormat = DataFormat.lookupMimeType((String)string);
            if (dataFormat == null) {
                dataFormat = new DataFormat(new String[]{string});
            }
            hashSet.add(dataFormat);
        }
        return hashSet;
    }

    @Override
    public boolean hasContent(DataFormat dataFormat) {
        if (this.dataCache != null) {
            for (Pair<DataFormat, Object> pair : this.dataCache) {
                if (pair.getKey() != dataFormat) continue;
                return true;
            }
            return false;
        }
        ClipboardAssistance clipboardAssistance = currentDragboard != null ? currentDragboard : this.systemAssistant;
        String[] arrstring = clipboardAssistance.getMimeTypes();
        if (arrstring == null) {
            return false;
        }
        for (String string : arrstring) {
            if (dataFormat == DataFormat.IMAGE && string.equalsIgnoreCase("application/x-java-rawimage")) {
                return true;
            }
            if (dataFormat == DataFormat.URL && string.equalsIgnoreCase("text/uri-list")) {
                return true;
            }
            if (dataFormat == DataFormat.IMAGE && string.equalsIgnoreCase("text/html") && this.parseIMG(clipboardAssistance.getData("text/html")) != null) {
                return true;
            }
            if (dataFormat == DataFormat.FILES && string.equalsIgnoreCase("application/x-java-file-list")) {
                return true;
            }
            DataFormat dataFormat2 = DataFormat.lookupMimeType((String)string);
            if (dataFormat2 == null || !dataFormat2.equals((Object)dataFormat)) continue;
            return true;
        }
        return false;
    }

    private static ByteBuffer prepareImage(javafx.scene.image.Image image) {
        PixelReader pixelReader = image.getPixelReader();
        int n = (int)image.getWidth();
        int n2 = (int)image.getHeight();
        byte[] arrby = new byte[n * n2 * 4];
        pixelReader.getPixels(0, 0, n, n2, WritablePixelFormat.getByteBgraInstance(), arrby, 0, n * 4);
        ByteBuffer byteBuffer = ByteBuffer.allocate(8 + n * n2 * 4);
        byteBuffer.putInt(n);
        byteBuffer.putInt(n2);
        byteBuffer.put(arrby);
        return byteBuffer;
    }

    private static ByteBuffer prepareOffset(double d, double d2) {
        ByteBuffer byteBuffer = ByteBuffer.allocate(8);
        byteBuffer.rewind();
        byteBuffer.putInt((int)d);
        byteBuffer.putInt((int)d2);
        return byteBuffer;
    }

    private boolean putContentToPeer(Pair<DataFormat, Object> ... arrpair) {
        this.systemAssistant.emptyCache();
        boolean bl = false;
        for (Pair<DataFormat, Object> pair : arrpair) {
            int n;
            Object iOException;
            DataFormat dataFormat = (DataFormat)pair.getKey();
            Object object = pair.getValue();
            if (dataFormat == DataFormat.IMAGE) {
                bl = this.placeImage(QuantumClipboard.convertObjectToImage(object));
                continue;
            }
            if (dataFormat == DataFormat.URL) {
                this.systemAssistant.setData("text/uri-list", object);
                bl = true;
                continue;
            }
            if (dataFormat == DataFormat.RTF) {
                this.systemAssistant.setData("text/rtf", object);
                bl = true;
                continue;
            }
            if (dataFormat == DataFormat.FILES) {
                iOException = (List)object;
                if (iOException.size() == 0) continue;
                iOException4 = new String[iOException.size()];
                n = 0;
                Iterator iterator = iOException.iterator();
                while (iterator.hasNext()) {
                    File file = (File)iterator.next();
                    iOException4[n++] = file.getAbsolutePath();
                }
                this.systemAssistant.setData("application/x-java-file-list", iOException4);
                bl = true;
                continue;
            }
            if (object instanceof Serializable) {
                if (dataFormat != DataFormat.PLAIN_TEXT && dataFormat != DataFormat.HTML || !(object instanceof String)) {
                    try {
                        iOException = new ByteArrayOutputStream();
                        iOException4 = new ObjectOutputStream((OutputStream)iOException);
                        iOException4.writeObject(object);
                        iOException4.close();
                        object = ByteBuffer.wrap(((ByteArrayOutputStream)iOException).toByteArray());
                    }
                    catch (IOException iOException2) {
                        throw new IllegalArgumentException("Could not serialize the data", iOException2);
                    }
                }
            } else if (object instanceof InputStream) {
                iOException = new ByteArrayOutputStream();
                try {
                    iOException4 = (InputStream)object;
                    try {
                        n = ((InputStream)iOException4).read();
                        while (n != -1) {
                            ((ByteArrayOutputStream)iOException).write(n);
                            n = ((InputStream)iOException4).read();
                        }
                    }
                    finally {
                        if (iOException4 != null) {
                            ((InputStream)iOException4).close();
                        }
                    }
                }
                catch (IOException iOException3) {
                    throw new IllegalArgumentException("Could not serialize the data", iOException3);
                }
                object = ByteBuffer.wrap(((ByteArrayOutputStream)iOException).toByteArray());
            } else if (!(object instanceof ByteBuffer)) {
                throw new IllegalArgumentException("Only serializable objects or ByteBuffer can be used as data with data format " + dataFormat);
            }
            for (Object iOException4 : dataFormat.getIdentifiers()) {
                this.systemAssistant.setData((String)iOException4, object);
                bl = true;
            }
        }
        if (this.dragImage != null) {
            ByteBuffer byteBuffer = QuantumClipboard.prepareImage(this.dragImage);
            ByteBuffer byteBuffer2 = QuantumClipboard.prepareOffset(this.dragOffsetX, this.dragOffsetY);
            this.systemAssistant.setData("application/x-java-drag-image", byteBuffer);
            this.systemAssistant.setData("application/x-java-drag-image-offset", byteBuffer2);
        }
        return bl;
    }

    @Override
    public boolean putContent(Pair<DataFormat, Object> ... arrpair) {
        for (Pair<DataFormat, Object> pair : arrpair) {
            Pair<DataFormat, Object> pair2 = (Pair<DataFormat, Object>)pair.getKey();
            Object object = pair.getValue();
            if (pair2 == null) {
                throw new NullPointerException("Clipboard.putContent: null data format");
            }
            if (object != null) continue;
            throw new NullPointerException("Clipboard.putContent: null data");
        }
        boolean bl = false;
        if (this.isCaching) {
            if (this.dataCache == null) {
                this.dataCache = new ArrayList<Pair<DataFormat, Object>>(arrpair.length);
            }
            for (Pair<DataFormat, Object> pair2 : arrpair) {
                this.dataCache.add(pair2);
                bl = true;
            }
        } else {
            bl = this.putContentToPeer(arrpair);
            this.systemAssistant.flush();
        }
        return bl;
    }

    private void clearCache() {
        this.dataCache = null;
        this.transferModesCache = null;
    }

    private void clearDragView() {
        this.dragImage = null;
        this.dragOffsetY = 0.0;
        this.dragOffsetX = 0.0;
    }

    static {
        findTagIMG = Pattern.compile("IMG\\s+SRC=\\\"([^\\\"]+)\\\"", 34);
    }
}

