package org.apache.logging.log4j.core.appender.db.jdbc;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Objects;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.Filter;
import org.apache.logging.log4j.core.Layout;
import org.apache.logging.log4j.core.appender.AbstractAppender;
import org.apache.logging.log4j.core.appender.db.AbstractDatabaseAppender;
import org.apache.logging.log4j.core.appender.db.ColumnMapping;
import org.apache.logging.log4j.core.config.Property;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.config.plugins.PluginBuilderAttribute;
import org.apache.logging.log4j.core.config.plugins.PluginBuilderFactory;
import org.apache.logging.log4j.core.config.plugins.PluginElement;
import org.apache.logging.log4j.core.config.plugins.validation.constraints.Required;
import org.apache.logging.log4j.core.util.Assert;
import org.apache.logging.log4j.core.util.Booleans;

@Plugin(name = "JDBC", category = "Core", elementType = "appender", printObject = true)
public final class JdbcAppender extends AbstractDatabaseAppender<JdbcDatabaseManager> {
  private final String description;
  
  public static class Builder<B extends Builder<B>> extends AbstractDatabaseAppender.Builder<B> implements org.apache.logging.log4j.core.util.Builder<JdbcAppender> {
    @PluginElement("ConnectionSource")
    @Required(message = "No ConnectionSource provided")
    private ConnectionSource connectionSource;
    
    @PluginBuilderAttribute
    private boolean immediateFail;
    
    @PluginBuilderAttribute
    private int bufferSize;
    
    @PluginBuilderAttribute
    @Required(message = "No table name provided")
    private String tableName;
    
    @PluginElement("ColumnConfigs")
    private ColumnConfig[] columnConfigs;
    
    @PluginElement("ColumnMappings")
    private ColumnMapping[] columnMappings;
    
    @PluginBuilderAttribute
    private boolean truncateStrings = true;
    
    @PluginBuilderAttribute
    private long reconnectIntervalMillis = 5000L;
    
    public JdbcAppender build() {
      if (Assert.isEmpty(this.columnConfigs) && Assert.isEmpty(this.columnMappings)) {
        JdbcAppender.LOGGER.error("Cannot create JdbcAppender without any columns.");
        return null;
      } 
      String managerName = "JdbcManager{name=" + getName() + ", bufferSize=" + this.bufferSize + ", tableName=" + this.tableName + ", columnConfigs=" + Arrays.toString((Object[])this.columnConfigs) + ", columnMappings=" + Arrays.toString((Object[])this.columnMappings) + '}';
      JdbcDatabaseManager manager = JdbcDatabaseManager.getManager(managerName, this.bufferSize, getLayout(), this.connectionSource, this.tableName, this.columnConfigs, this.columnMappings, this.immediateFail, this.reconnectIntervalMillis, this.truncateStrings);
      if (manager == null)
        return null; 
      return new JdbcAppender(getName(), getFilter(), getLayout(), isIgnoreExceptions(), getPropertyArray(), manager);
    }
    
    public long getReconnectIntervalMillis() {
      return this.reconnectIntervalMillis;
    }
    
    public boolean isImmediateFail() {
      return this.immediateFail;
    }
    
    public B setBufferSize(int bufferSize) {
      this.bufferSize = bufferSize;
      return (B)asBuilder();
    }
    
    public B setColumnConfigs(ColumnConfig... columnConfigs) {
      this.columnConfigs = columnConfigs;
      return (B)asBuilder();
    }
    
    public B setColumnMappings(ColumnMapping... columnMappings) {
      this.columnMappings = columnMappings;
      return (B)asBuilder();
    }
    
    public B setConnectionSource(ConnectionSource connectionSource) {
      this.connectionSource = connectionSource;
      return (B)asBuilder();
    }
    
    public void setImmediateFail(boolean immediateFail) {
      this.immediateFail = immediateFail;
    }
    
    public void setReconnectIntervalMillis(long reconnectIntervalMillis) {
      this.reconnectIntervalMillis = reconnectIntervalMillis;
    }
    
    public B setTableName(String tableName) {
      this.tableName = tableName;
      return (B)asBuilder();
    }
    
    public B setTruncateStrings(boolean truncateStrings) {
      this.truncateStrings = truncateStrings;
      return (B)asBuilder();
    }
  }
  
  @Deprecated
  public static <B extends Builder<B>> JdbcAppender createAppender(String name, String ignore, Filter filter, ConnectionSource connectionSource, String bufferSize, String tableName, ColumnConfig[] columnConfigs) {
    Assert.requireNonEmpty(name, "Name cannot be empty");
    Objects.requireNonNull(connectionSource, "ConnectionSource cannot be null");
    Assert.requireNonEmpty(tableName, "Table name cannot be empty");
    Assert.requireNonEmpty(columnConfigs, "ColumnConfigs cannot be empty");
    int bufferSizeInt = AbstractAppender.parseInt(bufferSize, 0);
    boolean ignoreExceptions = Booleans.parseBoolean(ignore, true);
    return ((Builder)((Builder)((Builder)newBuilder()
      .setBufferSize(bufferSizeInt)
      .setColumnConfigs(columnConfigs)
      .setConnectionSource(connectionSource)
      .setTableName(tableName).setName(name)).setIgnoreExceptions(ignoreExceptions)).setFilter(filter))
      .build();
  }
  
  @PluginBuilderFactory
  public static <B extends Builder<B>> B newBuilder() {
    return (B)(new Builder<>()).asBuilder();
  }
  
  private JdbcAppender(String name, Filter filter, Layout<? extends Serializable> layout, boolean ignoreExceptions, Property[] properties, JdbcDatabaseManager manager) {
    super(name, filter, layout, ignoreExceptions, properties, manager);
    this.description = getName() + "{ manager=" + getManager() + " }";
  }
  
  public String toString() {
    return this.description;
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\logging\log4j\core\appender\db\jdbc\JdbcAppender.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */