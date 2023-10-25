/*
 * Decompiled with CFR 0.150.
 */
package org.apache.logging.log4j.core.appender.db.jdbc;

import java.io.Serializable;
import java.io.StringReader;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.NClob;
import java.sql.PreparedStatement;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.SQLTransactionRollbackException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.CountDownLatch;
import org.apache.logging.log4j.core.Layout;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.StringLayout;
import org.apache.logging.log4j.core.appender.AppenderLoggingException;
import org.apache.logging.log4j.core.appender.ManagerFactory;
import org.apache.logging.log4j.core.appender.db.AbstractDatabaseManager;
import org.apache.logging.log4j.core.appender.db.ColumnMapping;
import org.apache.logging.log4j.core.appender.db.DbAppenderLoggingException;
import org.apache.logging.log4j.core.appender.db.jdbc.ColumnConfig;
import org.apache.logging.log4j.core.appender.db.jdbc.ConnectionSource;
import org.apache.logging.log4j.core.config.plugins.convert.DateTypeConverter;
import org.apache.logging.log4j.core.config.plugins.convert.TypeConverters;
import org.apache.logging.log4j.core.util.Closer;
import org.apache.logging.log4j.core.util.Log4jThread;
import org.apache.logging.log4j.message.MapMessage;
import org.apache.logging.log4j.spi.ThreadContextMap;
import org.apache.logging.log4j.spi.ThreadContextStack;
import org.apache.logging.log4j.util.IndexedReadOnlyStringMap;
import org.apache.logging.log4j.util.ReadOnlyStringMap;
import org.apache.logging.log4j.util.Strings;

public final class JdbcDatabaseManager
extends AbstractDatabaseManager {
    private static final JdbcDatabaseManagerFactory INSTANCE = new JdbcDatabaseManagerFactory();
    private final List<ColumnConfig> columnConfigs;
    private final String sqlStatement;
    private final FactoryData factoryData;
    private volatile Connection connection;
    private volatile PreparedStatement statement;
    private volatile Reconnector reconnector;
    private volatile boolean isBatchSupported;
    private volatile Map<String, ResultSetColumnMetaData> columnMetaData;

    private static void appendColumnName(int i, String columnName, StringBuilder sb) {
        if (i > 1) {
            sb.append(',');
        }
        sb.append(columnName);
    }

    private static void appendColumnNames(String sqlVerb, FactoryData data, StringBuilder sb) {
        String columnName;
        int i = 1;
        String messagePattern = "Appending {} {}[{}]: {}={} ";
        if (data.columnMappings != null) {
            for (ColumnMapping colMapping : data.columnMappings) {
                columnName = colMapping.getName();
                JdbcDatabaseManager.appendColumnName(i, columnName, sb);
                JdbcDatabaseManager.logger().trace("Appending {} {}[{}]: {}={} ", sqlVerb, colMapping.getClass().getSimpleName(), i, columnName, colMapping);
                ++i;
            }
        }
        if (data.columnConfigs != null) {
            for (ColumnConfig colConfig : data.columnConfigs) {
                columnName = colConfig.getColumnName();
                JdbcDatabaseManager.appendColumnName(i, columnName, sb);
                JdbcDatabaseManager.logger().trace("Appending {} {}[{}]: {}={} ", sqlVerb, colConfig.getClass().getSimpleName(), i, columnName, colConfig);
                ++i;
            }
        }
    }

    private static JdbcDatabaseManagerFactory getFactory() {
        return INSTANCE;
    }

    @Deprecated
    public static JdbcDatabaseManager getJDBCDatabaseManager(String name, int bufferSize, ConnectionSource connectionSource, String tableName, ColumnConfig[] columnConfigs) {
        return JdbcDatabaseManager.getManager(name, new FactoryData(bufferSize, null, connectionSource, tableName, columnConfigs, new ColumnMapping[0], false, 5000L, true), JdbcDatabaseManager.getFactory());
    }

    @Deprecated
    public static JdbcDatabaseManager getManager(String name, int bufferSize, Layout<? extends Serializable> layout, ConnectionSource connectionSource, String tableName, ColumnConfig[] columnConfigs, ColumnMapping[] columnMappings) {
        return JdbcDatabaseManager.getManager(name, new FactoryData(bufferSize, layout, connectionSource, tableName, columnConfigs, columnMappings, false, 5000L, true), JdbcDatabaseManager.getFactory());
    }

    @Deprecated
    public static JdbcDatabaseManager getManager(String name, int bufferSize, Layout<? extends Serializable> layout, ConnectionSource connectionSource, String tableName, ColumnConfig[] columnConfigs, ColumnMapping[] columnMappings, boolean immediateFail, long reconnectIntervalMillis) {
        return JdbcDatabaseManager.getManager(name, new FactoryData(bufferSize, null, connectionSource, tableName, columnConfigs, columnMappings, false, 5000L, true), JdbcDatabaseManager.getFactory());
    }

    public static JdbcDatabaseManager getManager(String name, int bufferSize, Layout<? extends Serializable> layout, ConnectionSource connectionSource, String tableName, ColumnConfig[] columnConfigs, ColumnMapping[] columnMappings, boolean immediateFail, long reconnectIntervalMillis, boolean truncateStrings) {
        return JdbcDatabaseManager.getManager(name, new FactoryData(bufferSize, layout, connectionSource, tableName, columnConfigs, columnMappings, immediateFail, reconnectIntervalMillis, truncateStrings), JdbcDatabaseManager.getFactory());
    }

    private JdbcDatabaseManager(String name, String sqlStatement, List<ColumnConfig> columnConfigs, FactoryData factoryData) {
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
        catch (SQLException sQLException) {
            // empty catch block
        }
        boolean stmtClosed = true;
        try {
            stmtClosed = this.isClosed(this.statement);
        }
        catch (SQLException sQLException) {
            // empty catch block
        }
        if (!this.isRunning() || connClosed || stmtClosed) {
            this.closeResources(false);
            if (this.reconnector != null && !this.factoryData.immediateFail) {
                this.reconnector.latch();
                if (this.connection == null) {
                    throw new AppenderLoggingException("Error writing to JDBC Manager '%s': JDBC connection not available [%s]", new Object[]{this.getName(), this.fieldsToString()});
                }
                if (this.statement == null) {
                    throw new AppenderLoggingException("Error writing to JDBC Manager '%s': JDBC statement not available [%s].", new Object[]{this.getName(), this.connection, this.fieldsToString()});
                }
            }
        }
    }

    protected void closeResources(boolean logExceptions) {
        block5: {
            block4: {
                PreparedStatement tempPreparedStatement = this.statement;
                this.statement = null;
                try {
                    Closer.close(tempPreparedStatement);
                }
                catch (Exception e) {
                    if (!logExceptions) break block4;
                    this.logWarn("Failed to close SQL statement logging event or flushing buffer", e);
                }
            }
            Connection tempConnection = this.connection;
            this.connection = null;
            try {
                Closer.close(tempConnection);
            }
            catch (Exception e) {
                if (!logExceptions) break block5;
                this.logWarn("Failed to close database connection logging event or flushing buffer", e);
            }
        }
    }

    protected boolean commitAndClose() {
        block8: {
            boolean closed = true;
            try {
                if (this.connection == null || this.connection.isClosed()) break block8;
                if (this.isBuffered() && this.isBatchSupported && this.statement != null) {
                    int[] result;
                    JdbcDatabaseManager.logger().debug("Executing batch PreparedStatement {}", this.statement);
                    try {
                        result = this.statement.executeBatch();
                    }
                    catch (SQLTransactionRollbackException e) {
                        JdbcDatabaseManager.logger().debug("{} executing batch PreparedStatement {}, retrying.", e, this.statement);
                        result = this.statement.executeBatch();
                    }
                    JdbcDatabaseManager.logger().debug("Batch result: {}", Arrays.toString(result));
                }
                JdbcDatabaseManager.logger().debug("Committing Connection {}", this.connection);
                this.connection.commit();
            }
            catch (SQLException e) {
                throw new DbAppenderLoggingException(e, "Failed to commit transaction logging event or flushing buffer [%s]", this.fieldsToString());
            }
            finally {
                this.closeResources(true);
            }
        }
        return true;
    }

    private boolean commitAndCloseAll() {
        if (this.connection != null || this.statement != null) {
            try {
                this.commitAndClose();
                return true;
            }
            catch (AppenderLoggingException e) {
                Throwable cause = e.getCause();
                Throwable actual = cause == null ? e : cause;
                JdbcDatabaseManager.logger().debug("{} committing and closing connection: {}", actual, actual.getClass().getSimpleName(), e.toString(), e);
            }
        }
        if (this.factoryData.connectionSource != null) {
            this.factoryData.connectionSource.stop();
        }
        return true;
    }

    private void connectAndPrepare() throws SQLException {
        JdbcDatabaseManager.logger().debug("Acquiring JDBC connection from {}", this.getConnectionSource());
        this.connection = this.getConnectionSource().getConnection();
        JdbcDatabaseManager.logger().debug("Acquired JDBC connection {}", this.connection);
        JdbcDatabaseManager.logger().debug("Getting connection metadata {}", this.connection);
        DatabaseMetaData databaseMetaData = this.connection.getMetaData();
        JdbcDatabaseManager.logger().debug("Connection metadata {}", databaseMetaData);
        this.isBatchSupported = databaseMetaData.supportsBatchUpdates();
        JdbcDatabaseManager.logger().debug("Connection supportsBatchUpdates: {}", this.isBatchSupported);
        this.connection.setAutoCommit(false);
        JdbcDatabaseManager.logger().debug("Preparing SQL {}", this.sqlStatement);
        this.statement = this.connection.prepareStatement(this.sqlStatement);
        JdbcDatabaseManager.logger().debug("Prepared SQL {}", this.statement);
        if (this.factoryData.truncateStrings) {
            this.initColumnMetaData();
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    protected void connectAndStart() {
        this.checkConnection();
        JdbcDatabaseManager jdbcDatabaseManager = this;
        synchronized (jdbcDatabaseManager) {
            try {
                this.connectAndPrepare();
            }
            catch (SQLException e) {
                this.reconnectOn(e);
            }
        }
    }

    private Reconnector createReconnector() {
        Reconnector recon = new Reconnector();
        recon.setDaemon(true);
        recon.setPriority(1);
        return recon;
    }

    private String createSqlSelect() {
        StringBuilder sb = new StringBuilder("select ");
        JdbcDatabaseManager.appendColumnNames("SELECT", this.factoryData, sb);
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
        String sqlSelect = this.createSqlSelect();
        JdbcDatabaseManager.logger().debug("Getting SQL metadata for table {}: {}", this.factoryData.tableName, sqlSelect);
        try (PreparedStatement mdStatement = this.connection.prepareStatement(sqlSelect);){
            ResultSetMetaData rsMetaData = mdStatement.getMetaData();
            JdbcDatabaseManager.logger().debug("SQL metadata: {}", rsMetaData);
            if (rsMetaData != null) {
                int columnCount = rsMetaData.getColumnCount();
                this.columnMetaData = new HashMap<String, ResultSetColumnMetaData>(columnCount);
                int i = 0;
                int j = 1;
                while (i < columnCount) {
                    ResultSetColumnMetaData value = new ResultSetColumnMetaData(rsMetaData, j);
                    this.columnMetaData.put(value.getNameKey(), value);
                    ++i;
                    ++j;
                }
            } else {
                JdbcDatabaseManager.logger().warn("{}: truncateStrings is true and ResultSetMetaData is null for statement: {}; manager will not perform truncation.", this.getClass().getSimpleName(), mdStatement);
            }
        }
    }

    private boolean isClosed(Statement statement) throws SQLException {
        return statement == null || statement.isClosed();
    }

    private boolean isClosed(Connection connection) throws SQLException {
        return connection == null || connection.isClosed();
    }

    private void reconnectOn(Exception exception) {
        block4: {
            if (!this.factoryData.retry) {
                throw new AppenderLoggingException("Cannot connect and prepare", exception);
            }
            if (this.reconnector == null) {
                this.reconnector = this.createReconnector();
                try {
                    this.reconnector.reconnect();
                }
                catch (SQLException reconnectEx) {
                    JdbcDatabaseManager.logger().debug("Cannot reestablish JDBC connection to {}: {}; starting reconnector thread {}", this.factoryData, reconnectEx, this.reconnector.getName(), reconnectEx);
                    this.reconnector.start();
                    this.reconnector.latch();
                    if (this.connection != null && this.statement != null) break block4;
                    throw new AppenderLoggingException((Throwable)exception, "Error sending to %s for %s [%s]", new Object[]{this.getName(), this.factoryData, this.fieldsToString()});
                }
            }
        }
    }

    private void setFields(MapMessage<?, ?> mapMessage) throws SQLException {
        IndexedReadOnlyStringMap map = mapMessage.getIndexedReadOnlyStringMap();
        String simpleName = this.statement.getClass().getName();
        int j = 1;
        if (this.factoryData.columnMappings != null) {
            for (ColumnMapping mapping : this.factoryData.columnMappings) {
                if (mapping.getLiteralValue() != null) continue;
                String source = mapping.getSource();
                String key = Strings.isEmpty(source) ? mapping.getName() : source;
                Object value = map.getValue(key);
                if (JdbcDatabaseManager.logger().isTraceEnabled()) {
                    String valueStr = value instanceof String ? "\"" + value + "\"" : Objects.toString(value, null);
                    JdbcDatabaseManager.logger().trace("{} setObject({}, {}) for key '{}' and mapping '{}'", simpleName, j, valueStr, key, mapping.getName());
                }
                this.setStatementObject(j, mapping.getNameKey(), value);
                ++j;
            }
        }
    }

    private void setStatementObject(int j, String nameKey, Object value) throws SQLException {
        if (this.statement == null) {
            throw new AppenderLoggingException("Cannot set a value when the PreparedStatement is null.");
        }
        if (value == null) {
            if (this.columnMetaData == null) {
                throw new AppenderLoggingException("Cannot set a value when the column metadata is null.");
            }
            this.statement.setNull(j, this.columnMetaData.get(nameKey).getType());
        } else {
            this.statement.setObject(j, this.truncate(nameKey, value));
        }
    }

    protected boolean shutdownInternal() {
        if (this.reconnector != null) {
            this.reconnector.shutdown();
            this.reconnector.interrupt();
            this.reconnector = null;
        }
        return this.commitAndCloseAll();
    }

    protected void startupInternal() throws Exception {
    }

    private Object truncate(String nameKey, Object value) {
        if (value != null && this.factoryData.truncateStrings && this.columnMetaData != null) {
            ResultSetColumnMetaData resultSetColumnMetaData = this.columnMetaData.get(nameKey);
            if (resultSetColumnMetaData != null) {
                if (resultSetColumnMetaData.isStringType()) {
                    value = resultSetColumnMetaData.truncate(value.toString());
                }
            } else {
                JdbcDatabaseManager.logger().error("Missing ResultSetColumnMetaData for {}, connection={}, statement={}", nameKey, this.connection, this.statement);
            }
        }
        return value;
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    protected void writeInternal(LogEvent event, Serializable serializable) {
        StringReader reader = null;
        try {
            if (!this.isRunning() || this.isClosed(this.connection) || this.isClosed(this.statement)) {
                throw new AppenderLoggingException("Cannot write logging event; JDBC manager not connected to the database, running=%s, [%s]).", new Object[]{this.isRunning(), this.fieldsToString()});
            }
            this.statement.clearParameters();
            if (serializable instanceof MapMessage) {
                this.setFields((MapMessage)serializable);
            }
            int j = 1;
            if (this.factoryData.columnMappings != null) {
                for (ColumnMapping mapping : this.factoryData.columnMappings) {
                    if (ThreadContextMap.class.isAssignableFrom(mapping.getType()) || ReadOnlyStringMap.class.isAssignableFrom(mapping.getType())) {
                        this.statement.setObject(j++, event.getContextData().toMap());
                        continue;
                    }
                    if (ThreadContextStack.class.isAssignableFrom(mapping.getType())) {
                        this.statement.setObject(j++, event.getContextStack().asList());
                        continue;
                    }
                    if (Date.class.isAssignableFrom(mapping.getType())) {
                        this.statement.setObject(j++, DateTypeConverter.fromMillis(event.getTimeMillis(), mapping.getType().asSubclass(Date.class)));
                        continue;
                    }
                    StringLayout layout = mapping.getLayout();
                    if (layout == null) continue;
                    if (Clob.class.isAssignableFrom(mapping.getType())) {
                        this.statement.setClob(j++, new StringReader((String)layout.toSerializable(event)));
                        continue;
                    }
                    if (NClob.class.isAssignableFrom(mapping.getType())) {
                        this.statement.setNClob(j++, new StringReader((String)layout.toSerializable(event)));
                        continue;
                    }
                    Object value = TypeConverters.convert((String)layout.toSerializable(event), mapping.getType(), null);
                    this.setStatementObject(j++, mapping.getNameKey(), value);
                }
            }
            for (ColumnConfig column : this.columnConfigs) {
                if (column.isEventTimestamp()) {
                    this.statement.setTimestamp(j++, new Timestamp(event.getTimeMillis()));
                    continue;
                }
                if (column.isClob()) {
                    reader = new StringReader(column.getLayout().toSerializable(event));
                    if (column.isUnicode()) {
                        this.statement.setNClob(j++, reader);
                        continue;
                    }
                    this.statement.setClob(j++, reader);
                    continue;
                }
                if (column.isUnicode()) {
                    this.statement.setNString(j++, Objects.toString(this.truncate(column.getColumnNameKey(), column.getLayout().toSerializable(event)), null));
                    continue;
                }
                this.statement.setString(j++, Objects.toString(this.truncate(column.getColumnNameKey(), column.getLayout().toSerializable(event)), null));
            }
            if (this.isBuffered() && this.isBatchSupported) {
                JdbcDatabaseManager.logger().debug("addBatch for {}", this.statement);
                this.statement.addBatch();
            } else {
                int executeUpdate = this.statement.executeUpdate();
                JdbcDatabaseManager.logger().debug("executeUpdate = {} for {}", executeUpdate, this.statement);
                if (executeUpdate == 0) {
                    throw new AppenderLoggingException("No records inserted in database table for log event in JDBC manager [%s].", new Object[]{this.fieldsToString()});
                }
            }
        }
        catch (SQLException e) {
            try {
                throw new DbAppenderLoggingException(e, "Failed to insert record for log event in JDBC manager: %s [%s]", e, this.fieldsToString());
            }
            catch (Throwable throwable) {
                try {
                    if (this.statement != null) {
                        this.statement.clearParameters();
                    }
                }
                catch (SQLException sQLException) {
                    // empty catch block
                }
                Closer.closeSilently(reader);
                throw throwable;
            }
        }
        try {
            if (this.statement != null) {
                this.statement.clearParameters();
            }
        }
        catch (SQLException j) {
            // empty catch block
        }
        Closer.closeSilently(reader);
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    protected void writeThrough(LogEvent event, Serializable serializable) {
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

    private static final class ResultSetColumnMetaData {
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

        public ResultSetColumnMetaData(ResultSetMetaData rsMetaData, int j) throws SQLException {
            this(rsMetaData.getSchemaName(j), rsMetaData.getCatalogName(j), rsMetaData.getTableName(j), rsMetaData.getColumnName(j), rsMetaData.getColumnLabel(j), rsMetaData.getColumnDisplaySize(j), rsMetaData.getColumnType(j), rsMetaData.getColumnTypeName(j), rsMetaData.getColumnClassName(j), rsMetaData.getPrecision(j), rsMetaData.getScale(j));
        }

        private ResultSetColumnMetaData(String schemaName, String catalogName, String tableName, String name, String label, int displaySize, int type, String typeName, String className, int precision, int scale) {
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
            this.isStringType = type == 1 || type == -16 || type == -1 || type == -9 || type == 12;
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

        public String toString() {
            return String.format("ColumnMetaData [schemaName=%s, catalogName=%s, tableName=%s, name=%s, nameKey=%s, label=%s, displaySize=%s, type=%s, typeName=%s, className=%s, precision=%s, scale=%s, isStringType=%s]", this.schemaName, this.catalogName, this.tableName, this.name, this.nameKey, this.label, this.displaySize, this.type, this.typeName, this.className, this.precision, this.scale, this.isStringType);
        }

        public String truncate(String string) {
            return this.precision > 0 ? Strings.left(string, this.precision) : string;
        }
    }

    private final class Reconnector
    extends Log4jThread {
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
            catch (InterruptedException interruptedException) {
                // empty catch block
            }
        }

        void reconnect() throws SQLException {
            JdbcDatabaseManager.this.closeResources(false);
            JdbcDatabaseManager.this.connectAndPrepare();
            JdbcDatabaseManager.this.reconnector = null;
            this.shutdown = true;
            JdbcDatabaseManager.logger().debug("Connection reestablished to {}", JdbcDatabaseManager.this.factoryData);
        }

        @Override
        public void run() {
            while (!this.shutdown) {
                try {
                    Reconnector.sleep(JdbcDatabaseManager.this.factoryData.reconnectIntervalMillis);
                    this.reconnect();
                }
                catch (InterruptedException | SQLException e) {
                    JdbcDatabaseManager.logger().debug("Cannot reestablish JDBC connection to {}: {}", JdbcDatabaseManager.this.factoryData, e.getLocalizedMessage(), e);
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

    private static final class JdbcDatabaseManagerFactory
    implements ManagerFactory<JdbcDatabaseManager, FactoryData> {
        private static final char PARAMETER_MARKER = '?';

        private JdbcDatabaseManagerFactory() {
        }

        @Override
        public JdbcDatabaseManager createManager(String name, FactoryData data) {
            StringBuilder sb = new StringBuilder("insert into ").append(data.tableName).append(" (");
            JdbcDatabaseManager.appendColumnNames("INSERT", data, sb);
            sb.append(") values (");
            int i = 1;
            if (data.columnMappings != null) {
                for (ColumnMapping mapping : data.columnMappings) {
                    String mappingName = mapping.getName();
                    if (Strings.isNotEmpty(mapping.getLiteralValue())) {
                        JdbcDatabaseManager.logger().trace("Adding INSERT VALUES literal for ColumnMapping[{}]: {}={} ", i, mappingName, mapping.getLiteralValue());
                        sb.append(mapping.getLiteralValue());
                    } else if (Strings.isNotEmpty(mapping.getParameter())) {
                        JdbcDatabaseManager.logger().trace("Adding INSERT VALUES parameter for ColumnMapping[{}]: {}={} ", i, mappingName, mapping.getParameter());
                        sb.append(mapping.getParameter());
                    } else {
                        JdbcDatabaseManager.logger().trace("Adding INSERT VALUES parameter marker for ColumnMapping[{}]: {}={} ", i, mappingName, Character.valueOf('?'));
                        sb.append('?');
                    }
                    sb.append(',');
                    ++i;
                }
            }
            int columnConfigsLen = data.columnConfigs == null ? 0 : data.columnConfigs.length;
            ArrayList<ColumnConfig> columnConfigs = new ArrayList<ColumnConfig>(columnConfigsLen);
            if (data.columnConfigs != null) {
                for (ColumnConfig config : data.columnConfigs) {
                    if (Strings.isNotEmpty(config.getLiteralValue())) {
                        sb.append(config.getLiteralValue());
                    } else {
                        sb.append('?');
                        columnConfigs.add(config);
                    }
                    sb.append(',');
                }
            }
            sb.setCharAt(sb.length() - 1, ')');
            String sqlStatement = sb.toString();
            return new JdbcDatabaseManager(name, sqlStatement, columnConfigs, data);
        }
    }

    private static final class FactoryData
    extends AbstractDatabaseManager.AbstractFactoryData {
        private final ConnectionSource connectionSource;
        private final String tableName;
        private final ColumnConfig[] columnConfigs;
        private final ColumnMapping[] columnMappings;
        private final boolean immediateFail;
        private final boolean retry;
        private final long reconnectIntervalMillis;
        private final boolean truncateStrings;

        protected FactoryData(int bufferSize, Layout<? extends Serializable> layout, ConnectionSource connectionSource, String tableName, ColumnConfig[] columnConfigs, ColumnMapping[] columnMappings, boolean immediateFail, long reconnectIntervalMillis, boolean truncateStrings) {
            super(bufferSize, layout);
            this.connectionSource = connectionSource;
            this.tableName = tableName;
            this.columnConfigs = columnConfigs;
            this.columnMappings = columnMappings;
            this.immediateFail = immediateFail;
            this.retry = reconnectIntervalMillis > 0L;
            this.reconnectIntervalMillis = reconnectIntervalMillis;
            this.truncateStrings = truncateStrings;
        }

        public String toString() {
            return String.format("FactoryData [connectionSource=%s, tableName=%s, columnConfigs=%s, columnMappings=%s, immediateFail=%s, retry=%s, reconnectIntervalMillis=%s, truncateStrings=%s]", this.connectionSource, this.tableName, Arrays.toString(this.columnConfigs), Arrays.toString(this.columnMappings), this.immediateFail, this.retry, this.reconnectIntervalMillis, this.truncateStrings);
        }
    }
}

