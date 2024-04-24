package me.offeex.bloomware.api.gui.screen.streamermode

import me.offeex.bloomware.Bloomware
import me.offeex.bloomware.client.module.client.StreamerMode
import java.awt.Image
import java.awt.event.WindowAdapter
import java.awt.event.WindowEvent
import javax.imageio.ImageIO
import javax.swing.JFrame

class StreamerWindowFrame : JFrame() {
    private val window = StreamerWindow()

    init {
        initUI()
    }

    private fun initUI() {
        this.add(window)
        this.isResizable = true
        pack()
        title = "Bloomware - Streamer Mode"
        setLocationRelativeTo(null)
        addWindowListener(object : WindowAdapter() {
            override fun windowClosing(e: WindowEvent) {
                StreamerMode.disable()
            }
        })
        val image: Image =
            ImageIO.read(Bloomware.javaClass.getResourceAsStream("/assets/bloomware/elements/tray/icon16x16.png"))
        this.iconImage = image
    }

    fun setStrings(strings: List<String>) {
        window.setStrings(strings)
    }
}