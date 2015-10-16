package com.driveu.util;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.log4j.Logger;

import com.driveu.bean.TagInfo;
import com.driveu.cons.TagConstants;
import com.driveu.query.PersistenceQuery;

/**
 * 
 * @author niraja
 *
 */
public class DBUtil {

	private static final Logger LOGGER = Logger.getLogger(DBUtil.class);
	
	public static boolean insertDataIntoDB(Map<String, TagInfo> updatedTagMap, Connection con) {
		Set<Entry<String, TagInfo>>  entrySet = updatedTagMap.entrySet();
		Iterator<Entry<String, TagInfo>> iter = entrySet.iterator();
		while(iter.hasNext()){
			Entry<String, TagInfo> entryTagInfo = iter.next();
			TagInfo tagInfo = entryTagInfo.getValue();
			if(tagInfo == null){
				continue;
			}
			try {
				Statement st = (Statement) con.createStatement();
				String insertQuery = PersistenceQuery.INSERT_SCRIPT + "(\""+  tagInfo.getTagName() + "\","
					+	tagInfo.getTotalCount() + "," +  tagInfo.getUnansweredCount() + ")";
				st.executeUpdate(insertQuery);
			} catch (SQLException e) {
				LOGGER.error("error : " + e.getMessage());
			}
		}
		return false;
	}
	
	public static void checkAndCreateTable( Connection con){
		ResultSet resultSet;
		DatabaseMetaData metadata;
		try {
			metadata = (DatabaseMetaData) con.getMetaData();
			resultSet = metadata.getTables(null, null, TagConstants.TABLE_NAME, new String[] {"TABLE"});
			Statement st = (Statement) con.createStatement();
			if(resultSet.next()){
				st.execute(PersistenceQuery.DROP_TABLE);
			}
			st.execute(PersistenceQuery.CREATE_TABLE);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
