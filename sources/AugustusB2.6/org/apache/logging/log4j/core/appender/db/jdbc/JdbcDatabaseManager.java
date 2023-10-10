// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.logging.log4j.core.appender.db.jdbc;

import java.util.concurrent.CountDownLatch;
import org.apache.logging.log4j.core.util.Log4jThread;
import java.util.ArrayList;
import org.apache.logging.log4j.status.StatusLogger;
import java.util.Iterator;
import org.apache.logging.log4j.core.StringLayout;
import java.sql.Timestamp;
import org.apache.logging.log4j.core.config.plugins.convert.TypeConverters;
import java.sql.NClob;
import java.io.Reader;
import java.io.StringReader;
import java.sql.Clob;
import org.apache.logging.log4j.core.config.plugins.convert.DateTypeConverter;
import java.util.Date;
import org.apache.logging.log4j.spi.ThreadContextStack;
import org.apache.logging.log4j.util.ReadOnlyStringMap;
import org.apache.logging.log4j.spi.ThreadContextMap;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.util.IndexedReadOnlyStringMap;
import java.util.Objects;
import org.apache.logging.log4j.util.Strings;
import org.apache.logging.log4j.message.MapMessage;
import java.sql.ResultSetMetaData;
import java.util.HashMap;
import java.sql.DatabaseMetaData;
import org.apache.logging.log4j.core.appender.db.DbAppenderLoggingException;
import java.util.Arrays;
import java.sql.SQLTransactionRollbackException;
import org.apache.logging.log4j.core.util.Closer;
import org.apache.logging.log4j.core.appender.AppenderLoggingException;
import java.sql.Statement;
import java.sql.SQLException;
import org.apache.logging.log4j.core.appender.ManagerFactory;
import java.io.Serializable;
import org.apache.logging.log4j.core.Layout;
import org.apache.logging.log4j.core.appender.db.ColumnMapping;
import org.apache.logging.log4j.core.appender.AbstractManager;
import java.util.Map;
import java.sql.PreparedStatement;
import java.sql.Connection;
import java.util.List;
import org.apache.logging.log4j.core.appender.db.AbstractDatabaseManager;

public final class JdbcDatabaseManager extends AbstractDatabaseManager
{
    private static final JdbcDatabaseManagerFactory INSTANCE;
    private final List<ColumnConfig> columnConfigs;
    private final String sqlStatement;
    private final FactoryData factoryData;
    private volatile Connection connection;
    private volatile PreparedStatement statement;
    private volatile Reconnector reconnector;
    private volatile boolean isBatchSupported;
    private volatile Map<String, ResultSetColumnMetaData> columnMetaData;
    
    private static void appendColumnName(final int i, final String columnName, final StringBuilder sb) {
        if (i > 1) {
            sb.append(',');
        }
        sb.append(columnName);
    }
    
    private static void appendColumnNames(final String sqlVerb, final FactoryData data, final StringBuilder sb) {
        int i = 1;
        final String messagePattern = "Appending {} {}[{}]: {}={} ";
        if (data.columnMappings != null) {
            for (final ColumnMapping colMapping : data.columnMappings) {
                final String columnName = colMapping.getName();
                appendColumnName(i, columnName, sb);
                AbstractManager.logger().trace("Appending {} {}[{}]: {}={} ", sqlVerb, colMapping.getClass().getSimpleName(), i, columnName, colMapping);
                ++i;
            }
        }
        if (data.columnConfigs != null) {
            for (final ColumnConfig colConfig : data.columnConfigs) {
                final String columnName = colConfig.getColumnName();
                appendColumnName(i, columnName, sb);
                AbstractManager.logger().trace("Appending {} {}[{}]: {}={} ", sqlVerb, colConfig.getClass().getSimpleName(), i, columnName, colConfig);
                ++i;
            }
        }
    }
    
    private static JdbcDatabaseManagerFactory getFactory() {
        return JdbcDatabaseManager.INSTANCE;
    }
    
    @Deprecated
    public static JdbcDatabaseManager getJDBCDatabaseManager(final String name, final int bufferSize, final ConnectionSource connectionSource, final String tableName, final ColumnConfig[] columnConfigs) {
        return AbstractDatabaseManager.getManager(name, new FactoryData(bufferSize, null, connectionSource, tableName, columnConfigs, ColumnMapping.EMPTY_ARRAY, false, 5000L, true), (ManagerFactory<JdbcDatabaseManager, FactoryData>)getFactory());
    }
    
    @Deprecated
    public static JdbcDatabaseManager getManager(final String name, final int bufferSize, final Layout<? extends Serializable> layout, final ConnectionSource connectionSource, final String tableName, final ColumnConfig[] columnConfigs, final ColumnMapping[] columnMappings) {
        return AbstractDatabaseManager.getManager(name, new FactoryData(bufferSize, layout, connectionSource, tableName, columnConfigs, columnMappings, false, 5000L, true), (ManagerFactory<JdbcDatabaseManager, FactoryData>)getFactory());
    }
    
    @Deprecated
    public static JdbcDatabaseManager getManager(final String name, final int bufferSize, final Layout<? extends Serializable> layout, final ConnectionSource connectionSource, final String tableName, final ColumnConfig[] columnConfigs, final ColumnMapping[] columnMappings, final boolean immediateFail, final long reconnectIntervalMillis) {
        return AbstractDatabaseManager.getManager(name, new FactoryData(bufferSize, null, connectionSource, tableName, columnConfigs, columnMappings, false, 5000L, true), (ManagerFactory<JdbcDatabaseManager, FactoryData>)getFactory());
    }
    
    public static JdbcDatabaseManager getManager(final String name, final int bufferSize, final Layout<? extends Serializable> layout, final ConnectionSource connectionSource, final String tableName, final ColumnConfig[] columnConfigs, final ColumnMapping[] columnMappings, final boolean immediateFail, final long reconnectIntervalMillis, final boolean truncateStrings) {
        return AbstractDatabaseManager.getManager(name, new FactoryData(bufferSize, layout, connectionSource, tableName, columnConfigs, columnMappings, immediateFail, reconnectIntervalMillis, truncateStrings), (ManagerFactory<JdbcDatabaseManager, FactoryData>)getFactory());
    }
    
    private JdbcDatabaseManager(final String name, final String sqlStatement, final List<ColumnConfig> columnConfigs, final FactoryData factoryData) {
        super(name, factoryData.getBufferSize());
        this.sqlStatement = sqlStatement;
        this.columnConfigs = columnConfigs;
        this.factoryData = factoryData;
    }
    
    private void checkConnection() {
        boolean connClosed = true;
        try {
            connClosed = this.isClosed(this.connection);
        }
        catch (SQLException ex) {}
        boolean stmtClosed = true;
        try {
            stmtClosed = this.isClosed(this.statement);
        }
        catch (SQLException ex2) {}
        if (!this.isRunning() || connClosed || stmtClosed) {
            this.closeResources(false);
            if (this.reconnector != null && !this.factoryData.immediateFail) {
                this.reconnector.latch();
                if (this.connection == null) {
                    throw new AppenderLoggingException("Error writing to JDBC Manager '%s': JDBC connection not available [%s]", new Object[] { this.getName(), this.fieldsToString() });
                }
                if (this.statement == null) {
                    throw new AppenderLoggingException("Error writing to JDBC Manager '%s': JDBC statement not available [%s].", new Object[] { this.getName(), this.connection, this.fieldsToString() });
                }
            }
        }
    }
    
    protected void closeResources(final boolean logExceptions) {
        final PreparedStatement tempPreparedStatement = this.statement;
        this.statement = null;
        try {
            Closer.close(tempPreparedStatement);
        }
        catch (Exception e) {
            if (logExceptions) {
                this.logWarn("Failed to close SQL statement logging event or flushing buffer", e);
            }
        }
        final Connection tempConnection = this.connection;
        this.connection = null;
        try {
            Closer.close(tempConnection);
        }
        catch (Exception e2) {
            if (logExceptions) {
                this.logWarn("Failed to close database connection logging event or flushing buffer", e2);
            }
        }
    }
    
    @Override
    protected boolean commitAndClose() {
        final boolean closed = true;
        try {
            if (this.connection != null && !this.connection.isClosed()) {
                if (this.isBuffered() && this.isBatchSupported && this.statement != null) {
                    AbstractManager.logger().debug("Executing batch PreparedStatement {}", this.statement);
                    int[] result;
                    try {
                        result = this.statement.executeBatch();
                    }
                    catch (SQLTransactionRollbackException e) {
                        AbstractManager.logger().debug("{} executing batch PreparedStatement {}, retrying.", e, this.statement);
                        result = this.statement.executeBatch();
                    }
                    AbstractManager.logger().debug("Batch result: {}", Arrays.toString(result));
                }
                AbstractManager.logger().debug("Committing Connection {}", this.connection);
                this.connection.commit();
            }
        }
        catch (SQLException e2) {
            throw new DbAppenderLoggingException(e2, "Failed to commit transaction logging event or flushing buffer [%s]", new Object[] { this.fieldsToString() });
        }
        finally {
            this.closeResources(true);
        }
        return true;
    }
    
    private boolean commitAndCloseAll() {
        Label_0058: {
            if (this.connection == null) {
                if (this.statement == null) {
                    break Label_0058;
                }
            }
            try {
                this.commitAndClose();
                return true;
            }
            catch (AppenderLoggingException e) {
                final Throwable cause = e.getCause();
                final Throwable actual = (cause == null) ? e : cause;
                AbstractManager.logger().debug("{} committing and closing connection: {}", actual, actual.getClass().getSimpleName(), e.toString(), e);
            }
        }
        if (this.factoryData.connectionSource != null) {
            this.factoryData.connectionSource.stop();
        }
        return true;
    }
    
    private void connectAndPrepare() throws SQLException {
        AbstractManager.logger().debug("Acquiring JDBC connection from {}", this.getConnectionSource());
        this.connection = this.getConnectionSource().getConnection();
        AbstractManager.logger().debug("Acquired JDBC connection {}", this.connection);
        AbstractManager.logger().debug("Getting connection metadata {}", this.connection);
        final DatabaseMetaData databaseMetaData = this.connection.getMetaData();
        AbstractManager.logger().debug("Connection metadata {}", databaseMetaData);
        this.isBatchSupported = databaseMetaData.supportsBatchUpdates();
        AbstractManager.logger().debug("Connection supportsBatchUpdates: {}", (Object)this.isBatchSupported);
        this.connection.setAutoCommit(false);
        AbstractManager.logger().debug("Preparing SQL {}", this.sqlStatement);
        this.statement = this.connection.prepareStatement(this.sqlStatement);
        AbstractManager.logger().debug("Prepared SQL {}", this.statement);
        if (this.factoryData.truncateStrings) {
            this.initColumnMetaData();
        }
    }
    
    @Override
    protected void connectAndStart() {
        this.checkConnection();
        synchronized (this) {
            try {
                this.connectAndPrepare();
            }
            catch (SQLException e) {
                this.reconnectOn(e);
            }
        }
    }
    
    private Reconnector createReconnector() {
        final Reconnector recon = new Reconnector();
        recon.setDaemon(true);
        recon.setPriority(1);
        return recon;
    }
    
    private String createSqlSelect() {
        final StringBuilder sb = new StringBuilder("select ");
        appendColumnNames("SELECT", this.factoryData, sb);
        sb.append(" from ");
        sb.append(this.factoryData.tableName);
        sb.append(" where 1=0");
        return sb.toString();
    }
    
    private String fieldsToString() {
        return String.format("columnConfigs=%s, sqlStatement=%s, factoryData=%s, connection=%s, statement=%s, reconnector=%s, isBatchSupported=%s, columnMetaData=%s", this.columnConfigs, this.sqlStatement, this.factoryData, this.connection, this.statement, this.reconnector, this.isBatchSupported, this.columnMetaData);
    }
    
    public ConnectionSource getConnectionSource() {
        return this.factoryData.connectionSource;
    }
    
    public String getSqlStatement() {
        return this.sqlStatement;
    }
    
    public String getTableName() {
        return this.factoryData.tableName;
    }
    
    private void initColumnMetaData() throws SQLException {
        final String sqlSelect = this.createSqlSelect();
        AbstractManager.logger().debug("Getting SQL metadata for table {}: {}", this.factoryData.tableName, sqlSelect);
        try (final PreparedStatement mdStatement = this.connection.prepareStatement(sqlSelect)) {
            final ResultSetMetaData rsMetaData = mdStatement.getMetaData();
            AbstractManager.logger().debug("SQL metadata: {}", rsMetaData);
            if (rsMetaData != null) {
                final int columnCount = rsMetaData.getColumnCount();
                this.columnMetaData = new HashMap<String, ResultSetColumnMetaData>(columnCount);
                for (int i = 0, j = 1; i < columnCount; ++i, ++j) {
                    final ResultSetColumnMetaData value = new ResultSetColumnMetaData(rsMetaData, j);
                    this.columnMetaData.put(value.getNameKey(), value);
                }
            }
            else {
                AbstractManager.logger().warn("{}: truncateStrings is true and ResultSetMetaData is null for statement: {}; manager will not perform truncation.", this.getClass().getSimpleName(), mdStatement);
            }
        }
    }
    
    private boolean isClosed(final Statement statement) throws SQLException {
        return statement == null || statement.isClosed();
    }
    
    private boolean isClosed(final Connection connection) throws SQLException {
        return connection == null || connection.isClosed();
    }
    
    private void reconnectOn(final Exception exception) {
        if (!this.factoryData.retry) {
            throw new AppenderLoggingException("Cannot connect and prepare", exception);
        }
        if (this.reconnector == null) {
            this.reconnector = this.createReconnector();
            try {
                this.reconnector.reconnect();
            }
            catch (SQLException reconnectEx) {
                AbstractManager.logger().debug("Cannot reestablish JDBC connection to {}: {}; starting reconnector thread {}", this.factoryData, reconnectEx, this.reconnector.getName(), reconnectEx);
                this.reconnector.start();
                this.reconnector.latch();
                if (this.connection == null || this.statement == null) {
                    throw new AppenderLoggingException(exception, "Error sending to %s for %s [%s]", new Object[] { this.getName(), this.factoryData, this.fieldsToString() });
                }
            }
        }
    }
    
    private void setFields(final MapMessage<?, ?> mapMessage) throws SQLException {
        final IndexedReadOnlyStringMap map = mapMessage.getIndexedReadOnlyStringMap();
        final String simpleName = this.statement.getClass().getName();
        int j = 1;
        if (this.factoryData.columnMappings != null) {
            for (final ColumnMapping mapping : this.factoryData.columnMappings) {
                if (mapping.getLiteralValue() == null) {
                    final String source = mapping.getSource();
                    final String key = Strings.isEmpty(source) ? mapping.getName() : source;
                    final Object value = map.getValue(key);
                    if (AbstractManager.logger().isTraceEnabled()) {
                        final String valueStr = (value instanceof String) ? ("\"" + value + "\"") : Objects.toString(value, null);
                        AbstractManager.logger().trace("{} setObject({}, {}) for key '{}' and mapping '{}'", simpleName, j, valueStr, key, mapping.getName());
                    }
                    this.setStatementObject(j, mapping.getNameKey(), value);
                    ++j;
                }
            }
        }
    }
    
    private void setStatementObject(final int j, final String nameKey, final Object value) throws SQLException {
        if (this.statement == null) {
            throw new AppenderLoggingException("Cannot set a value when the PreparedStatement is null.");
        }
        if (value == null) {
            if (this.columnMetaData == null) {
                throw new AppenderLoggingException("Cannot set a value when the column metadata is null.");
            }
            this.statement.setNull(j, this.columnMetaData.get(nameKey).getType());
        }
        else {
            this.statement.setObject(j, this.truncate(nameKey, value));
        }
    }
    
    @Override
    protected boolean shutdownInternal() {
        if (this.reconnector != null) {
            this.reconnector.shutdown();
            this.reconnector.interrupt();
            this.reconnector = null;
        }
        return this.commitAndCloseAll();
    }
    
    @Override
    protected void startupInternal() throws Exception {
    }
    
    private Object truncate(final String nameKey, Object value) {
        if (value != null && this.factoryData.truncateStrings && this.columnMetaData != null) {
            final ResultSetColumnMetaData resultSetColumnMetaData = this.columnMetaData.get(nameKey);
            if (resultSetColumnMetaData != null) {
                if (resultSetColumnMetaData.isStringType()) {
                    value = resultSetColumnMetaData.truncate(value.toString());
                }
            }
            else {
                AbstractManager.logger().error("Missing ResultSetColumnMetaData for {}, connection={}, statement={}", nameKey, this.connection, this.statement);
            }
        }
        return value;
    }
    
    @Override
    protected void writeInternal(final LogEvent event, final Serializable serializable) {
        StringReader reader = null;
        try {
            if (!this.isRunning() || this.isClosed(this.connection) || this.isClosed(this.statement)) {
                throw new AppenderLoggingException("Cannot write logging event; JDBC manager not connected to the database, running=%s, [%s]).", new Object[] { this.isRunning(), this.fieldsToString() });
            }
            this.statement.clearParameters();
            if (serializable instanceof MapMessage) {
                this.setFields((MapMessage<?, ?>)serializable);
            }
            int j = 1;
            if (this.factoryData.columnMappings != null) {
                for (final ColumnMapping mapping : this.factoryData.columnMappings) {
                    if (ThreadContextMap.class.isAssignableFrom(mapping.getType()) || ReadOnlyStringMap.class.isAssignableFrom(mapping.getType())) {
                        this.statement.setObject(j++, event.getContextData().toMap());
                    }
                    else if (ThreadContextStack.class.isAssignableFrom(mapping.getType())) {
                        this.statement.setObject(j++, event.getContextStack().asList());
                    }
                    else if (Date.class.isAssignableFrom(mapping.getType())) {
                        this.statement.setObject(j++, DateTypeConverter.fromMillis(event.getTimeMillis(), (Class<Object>)mapping.getType().asSubclass(Date.class)));
                    }
                    else {
                        final StringLayout layout = mapping.getLayout();
                        if (layout != null) {
                            if (Clob.class.isAssignableFrom(mapping.getType())) {
                                this.statement.setClob(j++, new StringReader(layout.toSerializable(event)));
                            }
                            else if (NClob.class.isAssignableFrom(mapping.getType())) {
                                this.statement.setNClob(j++, new StringReader(layout.toSerializable(event)));
                            }
                            else {
                                final Object value = TypeConverters.convert((String)layout.toSerializable(event), mapping.getType(), (Object)null);
                                this.setStatementObject(j++, mapping.getNameKey(), value);
                            }
                        }
                    }
                }
            }
            for (final ColumnConfig column : this.columnConfigs) {
                if (column.isEventTimestamp()) {
                    this.statement.setTimestamp(j++, new Timestamp(event.getTimeMillis()));
                }
                else if (column.isClob()) {
                    reader = new StringReader(column.getLayout().toSerializable(event));
                    if (column.isUnicode()) {
                        this.statement.setNClob(j++, reader);
                    }
                    else {
                        this.statement.setClob(j++, reader);
                    }
                }
                else if (column.isUnicode()) {
                    this.statement.setNString(j++, Objects.toString(this.truncate(column.getColumnNameKey(), column.getLayout().toSerializable(event)), null));
                }
                else {
                    this.statement.setString(j++, Objects.toString(this.truncate(column.getColumnNameKey(), column.getLayout().toSerializable(event)), null));
                }
            }
            if (this.isBuffered() && this.isBatchSupported) {
                AbstractManager.logger().debug("addBatch for {}", this.statement);
                this.statement.addBatch();
            }
            else {
                final int executeUpdate = this.statement.executeUpdate();
                AbstractManager.logger().debug("executeUpdate = {} for {}", (Object)executeUpdate, this.statement);
                if (executeUpdate == 0) {
                    throw new AppenderLoggingException("No records inserted in database table for log event in JDBC manager [%s].", new Object[] { this.fieldsToString() });
                }
            }
        }
        catch (SQLException e) {
            throw new DbAppenderLoggingException(e, "Failed to insert record for log event in JDBC manager: %s [%s]", new Object[] { e, this.fieldsToString() });
        }
        finally {
            try {
                if (this.statement != null) {
                    this.statement.clearParameters();
                }
            }
            catch (SQLException ex) {}
            Closer.closeSilently(reader);
        }
    }
    
    @Override
    protected void writeThrough(final LogEvent event, final Serializable serializable) {
        this.connectAndStart();
        try {
            try {
                this.writeInternal(event, serializable);
            }
            finally {
                this.commitAndClose();
            }
        }
        catch (DbAppenderLoggingException e) {
            this.reconnectOn(e);
            try {
                this.writeInternal(event, serializable);
            }
            finally {
                this.commitAndClose();
            }
        }
    }
    
    static {
        INSTANCE = new JdbcDatabaseManagerFactory();
    }
    
    private static final class FactoryData extends AbstractFactoryData
    {
        private final ConnectionSource connectionSource;
        private final String tableName;
        private final ColumnConfig[] columnConfigs;
        private final ColumnMapping[] columnMappings;
        private final boolean immediateFail;
        private final boolean retry;
        private final long reconnectIntervalMillis;
        private final boolean truncateStrings;
        
        protected FactoryData(final int bufferSize, final Layout<? extends Serializable> layout, final ConnectionSource connectionSource, final String tableName, final ColumnConfig[] columnConfigs, final ColumnMapping[] columnMappings, final boolean immediateFail, final long reconnectIntervalMillis, final boolean truncateStrings) {
            super(bufferSize, layout);
            this.connectionSource = connectionSource;
            this.tableName = tableName;
            this.columnConfigs = columnConfigs;
            this.columnMappings = columnMappings;
            this.immediateFail = immediateFail;
            this.retry = (reconnectIntervalMillis > 0L);
            this.reconnectIntervalMillis = reconnectIntervalMillis;
            this.truncateStrings = truncateStrings;
        }
        
        @Override
        public String toString() {
            return String.format("FactoryData [connectionSource=%s, tableName=%s, columnConfigs=%s, columnMappings=%s, immediateFail=%s, retry=%s, reconnectIntervalMillis=%s, truncateStrings=%s]", this.connectionSource, this.tableName, Arrays.toString(this.columnConfigs), Arrays.toString(this.columnMappings), this.immediateFail, this.retry, this.reconnectIntervalMillis, this.truncateStrings);
        }
    }
    
    private static final class JdbcDatabaseManagerFactory implements ManagerFactory<JdbcDatabaseManager, FactoryData>
    {
        private static final char PARAMETER_MARKER = '?';
        
        @Override
        public JdbcDatabaseManager createManager(final String name, final FactoryData data) {
            final StringBuilder sb = new StringBuilder("insert into ").append(data.tableName).append(" (");
            appendColumnNames("INSERT", data, sb);
            sb.append(") values (");
            int i = 1;
            if (data.columnMappings != null) {
                for (final ColumnMapping mapping : data.columnMappings) {
                    final String mappingName = mapping.getName();
                    if (Strings.isNotEmpty(mapping.getLiteralValue())) {
                        AbstractManager.logger().trace("Adding INSERT VALUES literal for ColumnMapping[{}]: {}={} ", (Object)i, mappingName, mapping.getLiteralValue());
                        sb.append(mapping.getLiteralValue());
                    }
                    else if (Strings.isNotEmpty(mapping.getParameter())) {
                        AbstractManager.logger().trace("Adding INSERT VALUES parameter for ColumnMapping[{}]: {}={} ", (Object)i, mappingName, mapping.getParameter());
                        sb.append(mapping.getParameter());
                    }
                    else {
                        AbstractManager.logger().trace("Adding INSERT VALUES parameter marker for ColumnMapping[{}]: {}={} ", (Object)i, mappingName, '?');
                        sb.append('?');
                    }
                    sb.append(',');
                    ++i;
                }
            }
            final int columnConfigsLen = (data.columnConfigs == null) ? 0 : data.columnConfigs.length;
            final List<ColumnConfig> columnConfigs = new ArrayList<ColumnConfig>(columnConfigsLen);
            if (data.columnConfigs != null) {
                for (final ColumnConfig config : data.columnConfigs) {
                    if (Strings.isNotEmpty(config.getLiteralValue())) {
                        sb.append(config.getLiteralValue());
                    }
                    else {
                        sb.append('?');
                        columnConfigs.add(config);
                    }
                    sb.append(',');
                }
            }
            sb.setCharAt(sb.length() - 1, ')');
            final String sqlStatement = sb.toString();
            return new JdbcDatabaseManager(name, sqlStatement, columnConfigs, data, null);
        }
    }
    
    private final class Reconnector extends Log4jThread
    {
        private final CountDownLatch latch;
        private volatile boolean shutdown;
        
        private Reconnector() {
            super("JdbcDatabaseManager-Reconnector");
            this.latch = new CountDownLatch(1);
        }
        
        public void latch() {
            try {
                this.latch.await();
            }
            catch (InterruptedException ex) {}
        }
        
        void reconnect() throws SQLException {
            JdbcDatabaseManager.this.closeResources(false);
            JdbcDatabaseManager.this.connectAndPrepare();
            JdbcDatabaseManager.this.reconnector = null;
            this.shutdown = true;
            AbstractManager.logger().debug("Connection reestablished to {}", JdbcDatabaseManager.this.factoryData);
        }
        
        @Override
        public void run() {
            while (!this.shutdown) {
                try {
                    Thread.sleep(JdbcDatabaseManager.this.factoryData.reconnectIntervalMillis);
                    this.reconnect();
                }
                catch (InterruptedException ex) {}
                catch (SQLException e) {
                    AbstractManager.logger().debug("Cannot reestablish JDBC connection to {}: {}", JdbcDatabaseManager.this.factoryData, e.getLocalizedMessage(), e);
                }
                finally {
                    this.latch.countDown();
                }
            }
        }
        
        public void shutdown() {
            this.shutdown = true;
        }
        
        @Override
        public String toString() {
            return String.format("Reconnector [latch=%s, shutdown=%s]", this.latch, this.shutdown);
        }
    }
    
    private static final class ResultSetColumnMetaData
    {
        private final String schemaName;
        private final String catalogName;
        private final String tableName;
        private final String name;
        private final String nameKey;
        private final String label;
        private final int displaySize;
        private final int type;
        private final String typeName;
        private final String className;
        private final int precision;
        private final int scale;
        private final boolean isStringType;
        
        public ResultSetColumnMetaData(final ResultSetMetaData rsMetaData, final int j) throws SQLException {
            this(rsMetaData.getSchemaName(j), rsMetaData.getCatalogName(j), rsMetaData.getTableName(j), rsMetaData.getColumnName(j), rsMetaData.getColumnLabel(j), rsMetaData.getColumnDisplaySize(j), rsMetaData.getColumnType(j), rsMetaData.getColumnTypeName(j), rsMetaData.getColumnClassName(j), rsMetaData.getPrecision(j), rsMetaData.getScale(j));
        }
        
        private ResultSetColumnMetaData(final String schemaName, final String catalogName, final String tableName, final String name, final String label, final int displaySize, final int type, final String typeName, final String className, final int precision, final int scale) {
            this.schemaName = schemaName;
            this.catalogName = catalogName;
            this.tableName = tableName;
            this.name = name;
            this.nameKey = ColumnMapping.toKey(name);
            this.label = label;
            this.displaySize = displaySize;
            this.type = type;
            this.typeName = typeName;
            this.className = className;
            this.precision = precision;
            this.scale = scale;
            this.isStringType = (type == 1 || type == -16 || type == -1 || type == -9 || type == 12);
        }
        
        public String getCatalogName() {
            return this.catalogName;
        }
        
        public String getClassName() {
            return this.className;
        }
        
        public int getDisplaySize() {
            return this.displaySize;
        }
        
        public String getLabel() {
            return this.label;
        }
        
        public String getName() {
            return this.name;
        }
        
        public String getNameKey() {
            return this.nameKey;
        }
        
        public int getPrecision() {
            return this.precision;
        }
        
        public int getScale() {
            return this.scale;
        }
        
        public String getSchemaName() {
            return this.schemaName;
        }
        
        public String getTableName() {
            return this.tableName;
        }
        
        public int getType() {
            return this.type;
        }
        
        public String getTypeName() {
            return this.typeName;
        }
        
        public boolean isStringType() {
            return this.isStringType;
        }
        
        @Override
        public String toString() {
            return String.format("ColumnMetaData [schemaName=%s, catalogName=%s, tableName=%s, name=%s, nameKey=%s, label=%s, displaySize=%s, type=%s, typeName=%s, className=%s, precision=%s, scale=%s, isStringType=%s]", this.schemaName, this.catalogName, this.tableName, this.name, this.nameKey, this.label, this.displaySize, this.type, this.typeName, this.className, this.precision, this.scale, this.isStringType);
        }
        
        public String truncate(final String string) {
            return (this.precision > 0) ? Strings.left(string, this.precision) : string;
        }
    }
}
