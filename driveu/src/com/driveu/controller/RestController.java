package com.driveu.controller;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;
import javax.ws.rs.core.Response.Status;

import com.driveu.bean.TagInfo;
import com.driveu.cons.TagConstants;
import com.driveu.dao.TagInfoDAO;
import com.driveu.util.ConnectionUtil;
import com.driveu.util.DBUtil;
import com.driveu.util.DataCollector;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

/**
 * 
 * @author niraja
 *
 */
public class RestController {

	private TagInfoDAO tagInfoDAO = new TagInfoDAO();
	
	private static final Gson gson = new Gson();
	
		@GET
		@Path("/tags")
		@Produces("application/json")
		public String getTagInfoList() throws IOException, SQLException
		{
			Connection con = ConnectionUtil.getConnection();
			List<TagInfo> tagInfoList = (List<TagInfo>) tagInfoDAO.getTagInfo(con);
			if(con != null){
				con.close();
			}
			if (tagInfoList == null) 
			{
				ResponseBuilder builder = Response.status(Status.BAD_REQUEST);
				builder.type("application/json");
				builder.entity("<error>Count Not Found</error>");
				throw new WebApplicationException(builder.build());
			} 
			else 
			{
				return gson.toJson(tagInfoList);
			}
			
		}
		
		@GET
		@Path("/populate")
		@Produces("application/json")
		public String populate() throws IOException, SQLException
		{
			Connection con = ConnectionUtil.getConnection();
			String tagData = DataCollector.fetchUrlData(TagConstants.TAG_50);
			if(tagData == null){
				ResponseBuilder builder = Response.status(Status.BAD_REQUEST);
				builder.type("application/json");
				builder.entity("<error>Could Not Fetch Data from Stackexchange API. Please see logs for more info...</error>");
				throw new WebApplicationException(builder.build());
			}
			Map<String, TagInfo> tagMap = DataCollector.getTop50PopularTags(tagData);
			Map<String, TagInfo> updatedTagMap = DataCollector.getTagDetailedInfo(tagMap);
			DBUtil.checkAndCreateTable(con);
			DBUtil.insertDataIntoDB(updatedTagMap, con);
			if(con != null){
				con.close();
			}
			JsonObject jsonObject = new JsonObject();
			jsonObject.addProperty("message", "Database populated successfully with tag information ....");
			return gson.toJson(jsonObject);
		}
	
}