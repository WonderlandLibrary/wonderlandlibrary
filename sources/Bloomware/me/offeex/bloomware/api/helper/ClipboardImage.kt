package me.offeex.bloomware.api.helper

import java.awt.Image
import java.awt.datatransfer.DataFlavor
import java.awt.datatransfer.Transferable
import java.awt.datatransfer.UnsupportedFlavorException

data class ClipboardImage(val image: Image) : Transferable {
    override fun getTransferData(flavor: DataFlavor) =
        if (isDataFlavorSupported(flavor)) image else throw UnsupportedFlavorException(flavor)

    override fun isDataFlavorSupported(flavor: DataFlavor) = flavor === DataFlavor.imageFlavor
    override fun getTransferDataFlavors() = arrayOf(DataFlavor.imageFlavor)
}