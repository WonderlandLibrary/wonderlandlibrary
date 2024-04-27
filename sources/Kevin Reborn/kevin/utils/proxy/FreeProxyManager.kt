package kevin.utils.proxy

import kevin.main.KevinClient
import kevin.utils.ServerUtils
import net.minecraft.client.Minecraft
import java.net.InetSocketAddress
import java.net.Proxy
import java.net.URL
import java.util.*
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.ConcurrentLinkedDeque
import javax.swing.JOptionPane
import javax.swing.UIManager

object FreeProxyManager {
    val proxies = LinkedList<Pair<Proxy, String>>()
    private var initialized = false
    val pings = ConcurrentHashMap<String, String>()
    private fun init() {
        if (proxies.isEmpty() && !initialized) {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName())
            initialized = true
            val sockets = arrayOf(
                "https://raw.githubusercontent.com/TheSpeedX/SOCKS-List/master/socks4.txt",
                "https://raw.githubusercontent.com/TheSpeedX/SOCKS-List/master/socks5.txt",
                "https://raw.githubusercontent.com/zloi-user/hideip.me/main/socks4.txt",
                "https://raw.githubusercontent.com/zloi-user/hideip.me/main/socks5.txt"
            )
            var i = 0

            for (it in sockets) {
                var get = ServerUtils.sendGet(it)
                if (get.second != 0) {
                    get = ServerUtils.sendGet(it.replace("https://raw.githubusercontent.com/", "https://raw.fgit.cf/"))
                }
                if (get.second != 0) continue
                val s = get.first ?: continue
                Minecraft.logger.info("[Proxy] got socks proxies from $it")
                for (s1 in HashSet(s.split("\n"))) {
                    val split = s1.split(":")
                    if (split.size == 1) continue
                    proxies.add(Proxy(Proxy.Type.SOCKS, InetSocketAddress(split[0], split[1].toInt())) to "SOCKET ${if (split.size > 2) split[2] else "unknown country"} ${i++}")
                }
            }
            val https = arrayOf(
                "https://raw.githubusercontent.com/zloi-user/hideip.me/main/http.txt",
                "https://raw.githubusercontent.com/TheSpeedX/SOCKS-List/master/http.txt"
            )
            for (url in https) {
                var http = ServerUtils.sendGet(url)
                if (http.second != 0) {
                    http =
                        ServerUtils.sendGet(url.replace("https://raw.githubusercontent.com/", "https://raw.fgit.cf/"))
                }
                if (http.second == 0) {
                    Minecraft.logger.info("[Proxy] got http proxies from $url")
                    for (s in HashSet(http.first?.split("\n")?: continue)) {
                        val split = s.split(":")
                        if (split.size == 1) continue
                        proxies.add(Proxy(Proxy.Type.HTTP, InetSocketAddress(split[0], split[1].toInt())) to "HTTP ${if (split.size > 2) split[2] else "unknown country"} ${i++}")
                    }
                }
            }
        }
    }

    @JvmOverloads
    fun ping(gui: ProxySelectorSUI? = null) {
        val brr = ArrayList(proxies)
        brr.shuffle()
        val arr = ConcurrentLinkedDeque(brr)
        // I know, it isn't smart to create a lot of threads
        KevinClient.pool.execute {
            while (arr.isNotEmpty()) {
                val proxy = arr.poll()
                KevinClient.pool.execute {
                    ping0(proxy)
                }
                Thread.sleep(10)
            }
        }
    }

    private fun ping0(proxy: Pair<Proxy, String>?, gui: ProxySelectorSUI? = null) {
        proxy ?: return
        val realUrl = URL("https://www.google.com:443")
        try {
            val start = System.nanoTime() / 1000000
            val connection = realUrl.openConnection(proxy.first)
            connection.setRequestProperty("accept", "*/*")
            connection.setRequestProperty("connection", "Keep-Alive")
            connection.setRequestProperty("user-agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/120.0.0.0 Safari/537.36")
            connection.connectTimeout = 3000
            connection.readTimeout = 3000
            connection.connect()
            val end = System.nanoTime() / 1000000
            val ping = end - start
            pings[proxy.second] = "${ping}ms"
        } catch (_: Throwable) {
            pings[proxy.second] = "time out"
        }
        gui?.updateList()
    }

    fun update(gui: GuiProxySelect) {
        val sui = ProxySelectorSUI(gui)
        init()
        if (proxies.isEmpty()) {
            sui.isVisible = false
            JOptionPane.showMessageDialog(
                null,
                "Failed to get proxy list",
                "Proxy Selector",
                JOptionPane.INFORMATION_MESSAGE
            )
            return
        }
        ping(sui)
//        val str = JOptionPane.showInputDialog(
//            null,
//            "Select proxy:\n",
//            "Proxy selector",
//            JOptionPane.PLAIN_MESSAGE,
//            null,
//            proxies.map { "${pings.getOrDefault(it.second, "no ping")}| ${it.second}" }.filter { !it.endsWith("time out", true) }.sorted().toTypedArray(),
//            null
//        ) as String
//        proxies.find { str.endsWith(it.second, true) }?.apply {
//            ProxyManager.proxyType = first.type()
//            gui.textField.text = first.address().toString().run {
//                this.substring(this.indexOf("/") + 1, this.length)
//            }
//        }
    }
}