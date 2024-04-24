package me.offeex.bloomware.api.gui.screen.streamermode

import java.awt.*
import javax.swing.JPanel

class StreamerWindow : JPanel() {
    private var toDraw = listOf<String>()
    private val _font: Font
    private val metrics: FontMetrics

    init {
        background = Color.black
        this.isFocusable = true
        this.preferredSize = Dimension(600, 480)
        _font = Font("Verdana", Font.PLAIN, 20)
        metrics = getFontMetrics(_font)
    }

    fun setStrings(strings: List<String>) {
        toDraw = strings
        this.repaint()
    }

    public override fun paintComponent(g: Graphics) {
        super.paintComponent(g)
        render(g)
    }

    fun render(g: Graphics) {
        val g2d = g as Graphics2D
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON)
        g.setColor(Color.WHITE)
        g.setFont(_font)
        var offset: Int = 40
        for (s in toDraw) {
            g.drawString(s, (width - metrics.stringWidth(s)) / 2, offset)
            offset += 20
        }
        Toolkit.getDefaultToolkit().sync()
    }
}