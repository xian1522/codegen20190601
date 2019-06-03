package com.wj.codegen.generatefile.internal.db;

import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import com.wj.codegen.api.IntrospectedColumn;
import com.wj.codegen.api.JavaTypeResolver;
import com.wj.codegen.config.Context;
import com.wj.codegen.config.IntrospectedTable;
import com.wj.codegen.config.TableConfiguration;
import com.wj.codegen.generatefile.internal.ObjectFactory;
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
		
		return null;
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
		}
		
		return answer;
	}
	
}
