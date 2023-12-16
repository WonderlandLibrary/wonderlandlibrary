// 
// Decompiled by Procyon v0.5.36
// 

package moonsense.utils.screenshot;

import java.io.IOException;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.datatransfer.DataFlavor;
import java.awt.Image;
import java.awt.datatransfer.Transferable;

public class ImageSelection implements Transferable
{
    private Image image;
    
    public ImageSelection(final Image image) {
        this.image = image;
    }
    
    @Override
    public DataFlavor[] getTransferDataFlavors() {
        return new DataFlavor[] { DataFlavor.imageFlavor };
    }
    
    @Override
    public boolean isDataFlavorSupported(final DataFlavor flavor) {
        return DataFlavor.imageFlavor.equals(flavor);
    }
    
    @Override
    public Object getTransferData(final DataFlavor flavor) throws UnsupportedFlavorException, IOException {
        if (!DataFlavor.imageFlavor.equals(flavor)) {
            throw new UnsupportedFlavorException(flavor);
        }
        return this.image;
    }
}
