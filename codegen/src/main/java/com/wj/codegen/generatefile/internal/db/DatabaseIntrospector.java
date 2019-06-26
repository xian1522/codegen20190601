package com.wj.codegen.generatefile.internal.db;

import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.TreeMap;

import com.wj.codegen.api.IntrospectedColumn;
import com.wj.codegen.api.JavaTypeResolver;
import com.wj.codegen.config.Context;
import com.wj.codegen.config.FullyQualifiedTable;
import com.wj.codegen.config.GeneratedKey;
import com.wj.codegen.config.IntrospectedTable;
import com.wj.codegen.config.PropertyRegistry;
import com.wj.codegen.config.TableConfiguration;
import com.wj.codegen.generatefile.internal.ObjectFactory;
import com.wj.codegen.javabean.FullyQualifiedJavaType;
import com.wj.codegen.util.JavaBeansUtil;
import com.wj.codegen.util.StringUtil;

public class DatabaseIntrospector {
	
	private DatabaseMetaData databaseMetaData;
	
	private JavaTypeResolver javaTypeResolver;
	
	private List<String> warnings;
	
	private Context context;
	
	public DatabaseIntrospector(Context context, DatabaseMetaData databaseMetaData,
			JavaTypeResolver javaTypeResolver, List<String> warnings) {
		this.context = context;
		this.databaseMetaData = databaseMetaData;
		this.javaTypeResolver = javaTypeResolver;
		this.warnings = warnings;
	}
	
	public List<IntrospectedTable> introspectTables(TableConfiguration tc) throws SQLException{
		
		Map<ActualTableName,List<IntrospectedColumn>> columns = getColumns(tc);
		
		if(columns.isEmpty()) {
			warnings.add("columns is empty");
			return null;
		}
		//去除不需要生成代码的表字段..
		removeIgnoredColumns(tc,columns);
		//额外表字段处理..
		calculateExtraColumnInformation(tc,columns);
		//表字段覆写..
		applyColumnOverrides(tc,columns);
		
		calculateIdentityColumns(tc,columns);
		
		List<IntrospectedTable> introspectedTables = calculateIntrospectedTables(tc,columns);
		
		return introspectedTables;
	}
	
	
	private Map<ActualTableName,List<IntrospectedColumn>> getColumns(TableConfiguration tc)throws SQLException{
		String localCatalog = null;
		String localSchema = null;
		String localTableName = null;
		
		boolean delimitIdentifiers = tc.isDelimitIdentifiers()
				|| StringUtil.stringContainsSpace(tc.getCatalog())
				|| StringUtil.stringContainsSpace(tc.getSchema())
				|| StringUtil.stringContainsSpace(tc.getTableName());
		
		if(delimitIdentifiers) {
			localCatalog = tc.getCatalog();
			localSchema = tc.getSchema();
			localTableName = tc.getTableName();
		}else if(databaseMetaData.storesLowerCaseIdentifiers()) {
			localCatalog = tc.getCatalog() == null ? null : tc.getCatalog().toLowerCase();
			localSchema = tc.getSchema() == null ? null : tc.getSchema().toLowerCase();
			localTableName = tc.getTableName() == null ? null : tc.getTableName().toLowerCase();
		}else if(databaseMetaData.storesUpperCaseIdentifiers()) {
			localCatalog = tc.getCatalog() == null ? null :tc.getCatalog().toUpperCase();
			localSchema = tc.getSchema() == null ? null : tc.getSchema().toUpperCase();
			localTableName = tc.getTableName() == null ? null : tc.getTableName().toUpperCase();
		}else {
			localCatalog = tc.getCatalog();
			localSchema = tc.getSchema();
			localTableName = tc.getTableName();
		}
		
		if(tc.isWildcardEscapingEnabled()) {
			String escapeString = databaseMetaData.getSearchStringEscape();
			
			StringBuilder sb = new StringBuilder();
			StringTokenizer st;
			if(localSchema != null) {
				st = new StringTokenizer(localSchema,"_%",true);
				while(st.hasMoreTokens()) {
					String token = st.nextToken();
					if(token.equals("%") || token.equals("_")) {
						sb.append(escapeString);
					}
					sb.append(token);
				}
				localSchema = sb.toString();
			}
			
			sb.setLength(0);
			st = new StringTokenizer(localTableName,"_%",true);
			while(st.hasMoreTokens()) {
				String token = st.nextToken();
				if(token.equals("_")||token.equals("%")) {
					sb.append(escapeString);
				}
				sb.append(token);
			}
			localTableName = sb.toString();
		}
		
		Map<ActualTableName,List<IntrospectedColumn>> answer = new HashMap<ActualTableName,List<IntrospectedColumn>>();
		
		ResultSet rs = databaseMetaData.getColumns(localCatalog, localSchema, localTableName, "%");
		
		boolean supportsIsAutoIncrement = false;
		boolean supportsIsGeneratedColumn = false;
		ResultSetMetaData rsmd = rs.getMetaData();
		int colCount = rsmd.getColumnCount();
		for(int i=1; i<colCount; i++) {
			if("IS_AUTOINCREMENT".equals(rsmd.getColumnName(i))){
				supportsIsAutoIncrement = true;
			}
			if("IS_GENERATEDCOLUMN".equals(rsmd.getColumnName(i))) {
				supportsIsGeneratedColumn = true;
			}
		}
		
		while(rs.next()) {
			IntrospectedColumn introspectedColumn = ObjectFactory.createIntrospectedColumn(context);
			
			introspectedColumn.setTableAilas(tc.getAlias());
			introspectedColumn.setJdbcType(rs.getInt("DATA_TYPE"));
			introspectedColumn.setLength(rs.getInt("COLUMN_SIZE"));
			introspectedColumn.setActualColumnName(rs.getString("COLUMN_NAME"));
			introspectedColumn.setNullable(rs.getInt("NULLABLE") == DatabaseMetaData.columnNullable);
			introspectedColumn.setScale(rs.getInt("DECIMAL_DIGITS"));
			introspectedColumn.setRemarks(rs.getString("REMARKS"));
			introspectedColumn.setDefalultValue(rs.getString("COLUMN_DEF"));
			
			if(supportsIsAutoIncrement) {
				introspectedColumn.setAutoIncrement("YES".equals(rs.getString("IS_AUTOINCREMENT")));
			}
			if(supportsIsGeneratedColumn) {
				introspectedColumn.setGeneratedColumn("YES".equals(rs.getString("IS_GENERATEDCOLUMN")));
			}
			
			ActualTableName atn = new ActualTableName(rs.getString("TABLE_CAT"),rs.getString("TABLE_SCHEM"),
					rs.getString("TABLE_NAME"));
			
			List<IntrospectedColumn> columns = answer.get(atn);
			if(columns == null) {
				columns = new ArrayList<IntrospectedColumn>();
				answer.put(atn, columns);
			}
			columns.add(introspectedColumn);
			
		}
		closeResultSet(rs);
		
		if(answer.size() > 1 
				&& StringUtil.stringContainSQLWildcard(localSchema)
				&& StringUtil.stringContainSQLWildcard(localTableName)) {
			// 待处理警告
		}
		
		return answer;
	}
	
	private void closeResultSet(ResultSet rs) {
		if(rs != null) {
			try {
				rs.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	
	private void removeIgnoredColumns(TableConfiguration tc,Map<ActualTableName,List<IntrospectedColumn>>columns) {
		
	}

	private void calculateExtraColumnInformation(TableConfiguration tc,Map<ActualTableName,List<IntrospectedColumn>>columns) {
		for(Map.Entry<ActualTableName, List<IntrospectedColumn>> entry : columns.entrySet()) {
			for(IntrospectedColumn introspectedColumn : entry.getValue()) {
				
				String calculateColumnName = introspectedColumn.getActualColumnName();
				introspectedColumn.setJavaProperty(JavaBeansUtil.getCamelCaseString(calculateColumnName,false));
				
				FullyQualifiedJavaType fullyQualifiedJavaType = javaTypeResolver.calculateJavaType(introspectedColumn);
				if(fullyQualifiedJavaType != null) {
					introspectedColumn.setFullQualifiedJavaType(fullyQualifiedJavaType);
					introspectedColumn.setJdbcTypeName(javaTypeResolver.calculateJdbcTypeName(introspectedColumn));
				}
			}
		}
	}
	
	private void applyColumnOverrides(TableConfiguration tc,Map<ActualTableName,List<IntrospectedColumn>> columns) {
		
	}

	private void calculateIdentityColumns(TableConfiguration tc,Map<ActualTableName,List<IntrospectedColumn>> columns) {
		GeneratedKey gk = tc.getGeneratedKey();
		if(gk == null) {
			return;
		}
		for(Map.Entry<ActualTableName, List<IntrospectedColumn>> entry : columns.entrySet()) {
			for(IntrospectedColumn column : entry.getValue()) {
				if(isMatchdColumn(column, gk)) {
					if(gk.isIdentity() || gk.isJdbcStandard()) {
						column.setIdentity(true);
						column.setSequnceColumn(false);
					}else {
						column.setIdentity(false);
						column.setSequnceColumn(true);
					}
				}
			}
		}
	}
	
	private boolean isMatchdColumn(IntrospectedColumn column,GeneratedKey gk) {
		if(column.isColumnNameDelimited()) {
			return column.getActualColumnName().equals(gk.getColumn());
		}else {
			return column.getActualColumnName().equalsIgnoreCase(gk.getColumn());
		}
	}
	
	private List<IntrospectedTable> calculateIntrospectedTables(TableConfiguration tc,
				Map<ActualTableName,List<IntrospectedColumn>>columns){
		boolean delimitIdentifiers = tc.isDelimitIdentifiers()
				||StringUtil.stringContainsSpace(tc.getCatalog())
				||StringUtil.stringContainsSpace(tc.getSchema())
				||StringUtil.stringContainsSpace(tc.getTableName());
		
		List<IntrospectedTable> answer = new ArrayList<IntrospectedTable>();
		for(Map.Entry<ActualTableName, List<IntrospectedColumn>> entry : columns.entrySet()) {
			ActualTableName atn = entry.getKey();
			
			FullyQualifiedTable table = new  FullyQualifiedTable(
					StringUtil.stringHasValue(tc.getCatalog())?atn.getCatalog():null,
					StringUtil.stringHasValue(tc.getSchema())?atn.getSchema():null,
					atn.getTableName(),
					tc.getDomainObjectName(),
					tc.getAlias(),
					StringUtil.isTrue(tc.getProperty(PropertyRegistry.TABLE_IGNORE_QUALIFIERS_AT_RUNTIME)),
					tc.getProperty(PropertyRegistry.TABLE_RUNTIME_CATALOG),
					tc.getProperty(PropertyRegistry.TABLE_RUNTIME_SCHEMA),
					tc.getProperty(PropertyRegistry.TABLE_RUNTIME_TABLE_NAME),
					delimitIdentifiers,context);
			
			IntrospectedTable introspectedTable = ObjectFactory.createIntrospectedTable(tc, table, context);
			
			for(IntrospectedColumn column : entry.getValue()) {
				introspectedTable.addColumn(column);
			}
			
			this.calculatePrimaryKey(table, introspectedTable);
			this.enhanceIntrospectedTable(introspectedTable);
			
			answer.add(introspectedTable);
		}
		
		return answer;
	}
	
	private void calculatePrimaryKey(FullyQualifiedTable table,IntrospectedTable introspectedTable) {
		ResultSet rs = null;
		
		try {
			rs = databaseMetaData.getPrimaryKeys(table.getIntrospectedCatalog(), 
					table.getIntrospectedSchema(), table.getIntrospectedTableName());
		} catch (SQLException e) {
			this.closeResultSet(rs);
			warnings.add("getPrimaryKeys error");
			return;
		}
		
		try {
			Map<Short,String> keyColumns = new TreeMap<Short,String>();
			while(rs.next()) {
				String columnName = rs.getString("COLUMN_NAME");
				short keyseq = rs.getShort("KEY_SEQ");
				keyColumns.put(keyseq, columnName);
			}
			
			for(String columnName : keyColumns.values()) {
				introspectedTable.addPrimaryKeyColumn(columnName);
			}
		} catch (SQLException e) {
		}finally {
			this.closeResultSet(rs);
		}
	}
	
	private void enhanceIntrospectedTable(IntrospectedTable introspectedTable) {
		
		FullyQualifiedTable table = introspectedTable.getFullyQualifiedTable();
		try {
			ResultSet rs = databaseMetaData.getTables(table.getIntrospectedCatalog(), table.getIntrospectedSchema(), 
					table.getIntrospectedTableName(), null);
			if(rs.next()) {
				String remarks = rs.getString("REMARKS");
				String tableType = rs.getString("TABLE_TYPE");
				introspectedTable.setRemark(remarks);
				introspectedTable.setTableType(tableType);
			}
			this.closeResultSet(rs);
		} catch (SQLException e) {
			warnings.add("enhanceIntrospectedTable error");
		}
		
	}
}
