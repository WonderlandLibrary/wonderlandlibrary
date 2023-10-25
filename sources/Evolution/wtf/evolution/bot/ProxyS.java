package wtf.evolution.bot;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class ProxyS {
    protected final String[] proxyList = new String[]{
//            "https://api.best-proxies.ru/proxylist.txt?key=cfb3d3640c99119b7f9be54d0ddf9d14&type=socks4&limit=0",
//            "https://api.best-proxies.ru/proxylist.txt?key=cfb3d3640c99119b7f9be54d0ddf9d14&type=socks5&limit=0",
//            //socks4
//			"https://raw.githubusercontent.com/jetkai/proxy-list/main/online-proxies/txt/proxies-socks4.txt",
//			"https://raw.githubusercontent.com/saschazesiger/Free-Proxies/master/proxies/socks4.txt",
//			"https://raw.githubusercontent.com/monosans/proxy-list/main/proxies/socks4.txt",
//			"https://raw.githubusercontent.com/ShiftyTR/Proxy-List/master/socks4.txt",
//			"https://api.proxyscrape.com/v2/?request=displayproxies&protocol=socks4",
//			"https://raw.githubusercontent.com/mmpx12/proxy-list/master/socks4.txt",
//			"https://www.proxy-list.download/api/v1/get?type=socks4",
//			"https://openproxylist.xyz/socks4.txt",
//			"https://proxyspace.pro/socks4.txt",
//			//socks5
//			"https://raw.githubusercontent.com/jetkai/proxy-list/main/online-proxies/txt/proxies-socks5.txt",
//			"https://raw.githubusercontent.com/saschazesiger/Free-Proxies/master/proxies/socks5.txt",
//			"https://raw.githubusercontent.com/monosans/proxy-list/main/proxies/socks5.txt",
//			"https://raw.githubusercontent.com/ShiftyTR/Proxy-List/master/socks5.txt",
//			"https://api.proxyscrape.com/v2/?request=displayproxies&protocol=socks5",
//			"https://raw.githubusercontent.com/mmpx12/proxy-list/master/socks5.txt",
//			"https://www.proxy-list.download/api/v1/get?type=socks5",
//			"https://openproxylist.xyz/socks5.txt",
//			"https://proxyspace.pro/socks5.txt",
//            "https://proxy1337.com/proxy/type=socks4&speed=25000&key=fd3f3dad254145eef84ea9ea47e35ebb",
//            "https://proxy1337.com/proxy/type=socks5&speed=25000&key=fd3f3dad254145eef84ea9ea47e35ebb",
//            "https://cdn.discordapp.com/attachments/1014241422997721249/1014370207524982834/socks4.txt",
//            "https://cdn.discordapp.com/attachments/1014241422997721249/1014370233663893504/socks5.txt",
//            "http://212.192.31.28/socks5.txt",
//            "http://212.192.31.28/socks4.txt",
//            "https://api.best-proxies.ru/proxylist.txt?key=75749eb852ddaca288b68618105f9203&type=socks4&limit=0",
//            "https://api.best-proxies.ru/proxylist.txt?key=75749eb852ddaca288b68618105f9203&type=socks5&limit=0"
            "https://proxyx.ru/700.txt"
    };

    public static boolean isPrivate = true;

    public int number;
    public final List<Proxy> proxies = new ArrayList<>();

    public void start() {
        proxies.clear();
        System.out.println("[Scraper] Starting...");

        try {
            new Thread(() -> {
                try {
                    for (String proxyMap : proxyList) {
                        Document proxyList = Jsoup.connect(proxyMap).ignoreHttpErrors(true).get();
                        for (String proxy : proxyList.text().split(" ")) {
                            if (isPrivate) {
                                int port =5841;
                                proxies.add(new Proxy(ProxyType.SOCKS5, new InetSocketAddress(proxy, port)));
                            }
                            else {
                                String[] proxySplit = proxy.split(":");
                                if (proxySplit.length >= 2) {
                                    String ip = proxy;
                                    proxies.add(new Proxy(ProxyType.SOCKS4, new InetSocketAddress(ip.split(":")[0], Integer.parseInt(ip.split(":")[1]))));
                                }
                            }
                        }

                    }
                } catch (Throwable ignored) {

                    ignored.printStackTrace();
                }
            }).run();

            System.out.println("[Scraper] Scraped " + proxies.size() + " proxy's");
        } catch (Exception ignored) {
        }
    }

    public Proxy getProxy() {
        number++;
        if (number > proxies.size() - 1) {
            number = 0;
        }
        return proxies.get(number);
    }
    public ProxyType getProxyType(String url) {
        if (url.contains("socks4")) return ProxyType.SOCKS4;
        if (url.contains("socks5")) return ProxyType.SOCKS5;
        if (url.contains("/http") || url.contains("=http")) return ProxyType.HTTP;
        return null;
    }

    public enum ProxyType {
        SOCKS4,
        SOCKS5,
        HTTP
    }

    public static class Proxy {
        private final ProxyType type;
        private final InetSocketAddress address;

        public Proxy(ProxyType type, InetSocketAddress address) {
            this.type = type;
            this.address = address;
        }

        public ProxyType getType() {
            return type;
        }

        public InetSocketAddress getAddress() {
            return address;
        }
    }
}