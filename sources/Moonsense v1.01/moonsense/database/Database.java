// 
// Decompiled by Procyon v0.5.36
// 

package moonsense.database;

import java.sql.PreparedStatement;
import java.sql.Connection;
import java.util.Iterator;
import java.sql.SQLException;
import moonsense.MoonsenseClient;
import java.sql.DriverManager;
import com.google.common.collect.Lists;
import java.util.List;

public class Database
{
    public static final Database INSTANCE;
    private final String host = "8.tcp.ngrok.io:13391";
    private final String database = "melonclient";
    private final String url = "jdbc:mysql://8.tcp.ngrok.io:13391/melonclient";
    protected final List<Connection> activeConnections;
    
    static {
        INSTANCE = new Database();
    }
    
    public Database() {
        this.activeConnections = (List<Connection>)Lists.newArrayList();
    }
    
    public Connection initConnection() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            return new Connection(DriverManager.getConnection("jdbc:mysql://8.tcp.ngrok.io:13391/melonclient", "melonclient-use", "ReadOnly:P"));
        }
        catch (ClassNotFoundException | SQLException ex2) {
            final Exception ex;
            final Exception e = ex;
            e.printStackTrace();
            MoonsenseClient.error("Connection failed!", new Object[0]);
            return null;
        }
    }
    
    public void close() {
        final Iterator<Connection> connectionIterator = this.activeConnections.iterator();
        MoonsenseClient.info("Trying to close {} connections!", this.activeConnections.size());
        while (connectionIterator.hasNext()) {
            final Connection con = connectionIterator.next();
            try {
                con.connection.close();
                connectionIterator.remove();
            }
            catch (SQLException throwables) {
                throwables.printStackTrace();
                MoonsenseClient.error("Error closing connection! Skipping...", new Object[0]);
            }
        }
        MoonsenseClient.info("Connections closed! {} remain!", this.activeConnections.size());
    }
    
    public static class Connection
    {
        public final java.sql.Connection connection;
        
        public Connection(final java.sql.Connection connection) {
            this.connection = connection;
            Database.INSTANCE.activeConnections.add(this);
        }
        
        public PreparedStatement prepareStatement(final String statement) throws SQLException {
            return this.connection.prepareStatement(statement);
        }
    }
}
