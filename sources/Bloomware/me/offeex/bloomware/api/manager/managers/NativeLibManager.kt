package me.offeex.bloomware.api.manager.managers

import me.offeex.bloomware.Bloomware
import me.offeex.bloomware.api.graphics.GLApi
import me.offeex.bloomware.api.graphics.GLElement
import me.offeex.bloomware.api.graphics.impl.GLImpl
import me.offeex.bloomware.api.graphics.impl.pipelines.ESPPipeline
import me.offeex.bloomware.api.graphics.impl.pipelines.FontRendererPipeline
import me.offeex.bloomware.api.manager.Manager
import me.offeex.bloomware.api.rust.NativeFontRasterizer

object NativeLibManager: Manager() {
    init {
        val file = FileManager.mainPath.resolve(System.mapLibraryName("bloomware_native"))
        val resource = Bloomware::class.java.getResource("/" + System.mapLibraryName("bloomware_native"))!!
        file.writeBytes(resource.readBytes())
        System.load(file.absolutePath)
        setupNativeLibrary()
    }

    private fun setupNativeLibrary() {
        NativeFontRasterizer.nativeSetup(NativeFontRasterizer)
    }
}