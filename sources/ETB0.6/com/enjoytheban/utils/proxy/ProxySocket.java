package com.enjoytheban.utils.proxy;

import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.Socket;

public class ProxySocket {

	public static Socket connectOverProxy(final String proxyAdress, final int proxyPort, final String destAddress,
			final int destPort) throws Exception {
		final Proxy proxy = new Proxy(Proxy.Type.SOCKS, new InetSocketAddress(proxyAdress, proxyPort));
		final Socket returnment = new Socket(proxy);
		returnment.setTcpNoDelay(true);
		returnment.connect(new InetSocketAddress(destAddress, destPort));
		return returnment;
	}

	/*
	 * public static Socket connectOverProxy(final String proxyAdress, final int
	 * proxyPort, final String destAddress, final int destPort) throws Exception {
	 * final String user = "enjoythebanllc"; final String password = "o9ZY9e7w114J";
	 * 
	 * java.net.Authenticator.setDefault(new java.net.Authenticator() { private
	 * PasswordAuthentication authentication = new PasswordAuthentication(user,
	 * password.toCharArray());
	 * 
	 * @Override protected PasswordAuthentication getPasswordAuthentication() {
	 * return authentication; } }); final Proxy proxy = new Proxy(Proxy.Type.SOCKS,
	 * new InetSocketAddress(proxyAdress, proxyPort)); final Socket returnment = new
	 * Socket(proxy); returnment.setTcpNoDelay(true); returnment.connect(new
	 * InetSocketAddress(destAddress, destPort)); return returnment; }
	 */
}
