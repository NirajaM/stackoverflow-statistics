package com.driveu.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.driveu.bean.TagInfo;
import com.driveu.query.PersistenceQuery;

/**
 * 
 * @author niraja
 *
 */
public class TagInfoDAO {

	public List<TagInfo> getTagInfo(Connection con)
	{
		List<TagInfo> tagInfoList = new ArrayList<TagInfo>();
		try
		{		
			Statement st = (Statement) con.createStatement();
			ResultSet rs = st.executeQuery(PersistenceQuery.FETCH_DATA);
			while (rs.next())
			{
				TagInfo tagInfo = new TagInfo();
				tagInfo.setTagName(rs.getString(1));
				tagInfo.setTotalCount(rs.getInt(2));
				tagInfo.setUnansweredCount(rs.getInt(3));
				tagInfoList.add(tagInfo);
			}
			rs.close();
			rs = null;
			con.close();
			con = null;
		} 
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return tagInfoList;
	}
	
}
