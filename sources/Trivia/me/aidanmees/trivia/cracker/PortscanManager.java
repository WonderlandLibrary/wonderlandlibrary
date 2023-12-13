package me.aidanmees.trivia.cracker;

import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import me.aidanmees.trivia.client.main.trivia;
import me.aidanmees.trivia.cracker.gui.GuitriviaAccHacker;
import me.aidanmees.trivia.gui.ScreenPos;
import net.minecraft.client.Minecraft;

public class PortscanManager {

	
	 ExecutorService es;
	    private Integer Scanned_Ports = Integer.valueOf(0);
	    public static List<Integer> ports;
	    public boolean isChecking = false;
	    public boolean AlwaysTrue = true;
	    
	    
	    public PortscanManager() {
	        this.es = Executors.newFixedThreadPool(250);
	        this.Scanned_Ports = 0;
	    }
	    
	    
	    public void run(String ServerIP) {
	    	isChecking = true;
	        this.Portscan(ServerIP);
	    }
	    
	   
	    public void onStop() {
	    	isChecking = false;
	        this.es.shutdownNow();
	    }
	    
	   
	    public String GetInfo() {
	        return "Scanned ports: " + this.Scanned_Ports + "/65535!";
	    }
	    
	  
	    public String GetName() {
	        return "Portscan";
	    }
	    
	    public Future<Boolean> portIsOpen(ExecutorService es2,  final String ip2,  final int port,  final int timeout) {
	    	 return es2.submit(new Callable<Boolean>(){

	        
	        	 @Override
	             public Boolean call() {
	                 try {
	                     Socket socket = new Socket();
	                     socket.connect(new InetSocketAddress(ip2, port), timeout);
	                     socket.close();
	                     trivia.getUIRenderer().addToQueue("Discovered open port: " + port + AlwaysTrue, ScreenPos.LEFTUP);
                         System.out.println("Discovered open port: " + port);
	                    Object object = ports;
	                    synchronized (object) {
	                        ports.add(port);
	                    }
	                    object = PortscanManager.this.Scanned_Ports;
	                    synchronized (object) {
	                        Integer n = PortscanManager.this.Scanned_Ports;
	                        Integer n2 = PortscanManager.this.Scanned_Ports = PortscanManager.this.Scanned_Ports + 1;
	                    }
	                    return true;
	                }
	                        catch (Exception ex)
	                        {
	                         
	                          synchronized (PortscanManager.this.Scanned_Ports)
	                          {
	                        	  Integer localInteger1 = PortscanManager.this.Scanned_Ports; Integer localInteger2 = PortscanManager.this.Scanned_Ports = Integer.valueOf(PortscanManager.this.Scanned_Ports.intValue() + 1);
	                          }
	                        }
	                        return Boolean.valueOf(false);
	                      }
	                    });
	            }
	    public void Portscan(String ServerIP) {
	      
	       
	        String serverIP = ServerIP;
	        String ip2 = serverIP.split(":")[0];
	        int timeout = 2000;
	        ArrayList<Future<Boolean>> futures = new ArrayList<Future<Boolean>>();
	        for (int port = 25500; port <= 255800; ++port) {
	            futures.add(this.portIsOpen(this.es, ip2, port, 2000));
	        }
	        this.es.shutdown();
	        int openPorts = 0;
	        for (Future f2 : futures) {
	            try {
	            	isChecking = true;
	                if (!((Boolean)f2.get()).booleanValue()) continue;
	                ++openPorts;
	            }
	            catch (InterruptedException e) {
	            	isChecking = false;
	                e.printStackTrace();
	            }
	            catch (ExecutionException e2) {
	            	isChecking = false;
	                e2.printStackTrace();
	            }
	        }
	        System.out.println("There are " + openPorts + " open ports on host " + ip2 + " (probed with a timeout of " + 2000 + "ms)");
	        
	    }
	    public void onUpdate(){
	    	
	    }
	}