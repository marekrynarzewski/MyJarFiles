package syncpack;

import java.util.Map;

public interface Rule
{
	void onDataSourcePrepare(DataSource ds);
	void onTablePrepare(DataSource ds, String tableName);
	void onRowPrepare(DataSource ds, String tableName, Map<String, Object> row);
}
