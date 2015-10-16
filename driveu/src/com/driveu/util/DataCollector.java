package com.driveu.util;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.zip.GZIPInputStream;

import org.apache.commons.httpclient.DefaultHttpMethodRetryHandler;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.driveu.bean.TagInfo;
import com.driveu.cons.TagConstants;

/**
 * This class is used to collect top 50 popular tags and full information about 
 * each tag like how many questions are asked and how many are unanswered
 * 
 * @author niraja
 *
 */
public class DataCollector {

	private static final Logger logger = Logger.getLogger(DataCollector.class);

	/**
	 * To get data for a particular url through http request
	 * 
	 * @param address
	 * @return
	 */
	public static String fetchUrlData(final String address) {
		byte[] responseBody = null;
		HttpClient client = new HttpClient();
		GetMethod method = new GetMethod(address);
		method.getParams().setParameter(HttpMethodParams.RETRY_HANDLER, new DefaultHttpMethodRetryHandler(5, false));
		boolean isResReceived = false;
		while (!isResReceived) {
			try {
				int statuscode = -1;
				try {
					statuscode = client.executeMethod(method);
				} catch (HttpException e) {
					logger.debug("HttpException in fetching url data for StackOverFlow " + e);
					e.printStackTrace();
				} catch (IOException e) {
					logger.debug("IOException in fetching url data for StackOverFlow " + e);
					e.printStackTrace();
				}
				if (statuscode != HttpStatus.SC_OK) {
					logger.info("Method failed " + method.getStatusLine() + " for the url call in StackOverFlow "
							+ address);
					return null;
				}
				try {
					responseBody = method.getResponseBody();
				} catch (IOException e) {
					logger.debug("IOException in fetching url data for StackOverFlow " + e);
					return null;
				}
				isResReceived = true;
			} finally {
				method.releaseConnection();
			}
		}
		try {
			return getDataIntoString(responseBody);
													
		} catch (IOException e) {
			logger.error(e.getMessage());
		}
		return null;
	}

	/**
	 * To convert gziped byte array to the String format
	 * 
	 * @param responseBody
	 * @return
	 * @throws IOException
	 */
	private static String getDataIntoString(byte[] responseBody) throws IOException {
		GZIPInputStream gis = new GZIPInputStream(new ByteArrayInputStream(responseBody));
		BufferedReader bf = new BufferedReader(new InputStreamReader(gis, TagConstants.ENCODING));
		String outStr = TagConstants.EMPTY_STR;
		String line;
		while ((line = bf.readLine()) != null) {
			outStr += line;
		}
		return outStr;
	}

	/**
	 * 
	 * @param tagData
	 * @return
	 */
	public static Map<String, TagInfo> getTop50PopularTags(String tagData) {
		if (tagData == null | tagData.isEmpty()) {
			logger.error("Input data is null or empty");
			return null;
		}
		Map<String, TagInfo> tagMap = new HashMap<>();
		try {
			JSONObject jsObj = new JSONObject(tagData);
			JSONArray jsArr = jsObj.getJSONArray(TagConstants.ITEM);
			int len = jsArr.length();
			for (int index = 0; index < len; index++) {
				JSONObject tagObj = jsArr.getJSONObject(index);
				if (tagObj.isNull(TagConstants.TAG_NAME)) {
					continue;
				}
				TagInfo tagInfo = new TagInfo();
				tagInfo.setTotalCount(tagObj.getInt(TagConstants.COUNT));
				String tagName = tagObj.getString(TagConstants.TAG_NAME);
				tagInfo.setTagName(tagName);
				tagMap.put(tagName, tagInfo);
			}
		} catch (JSONException e) {
			logger.error(e.getMessage());
		}
		return tagMap;
	}

	public static Map<String, TagInfo> getTagDetailedInfo(Map<String, TagInfo> tagMap) {
		Set<String> tagSet = tagMap.keySet();
		int count = 1;
		for (String tagName : tagSet) {
			count++;
			if ((count % TagConstants.REQ_COUNT) == 0) {
				try {
					Thread.sleep(90000);
					count = 1;
				} catch (InterruptedException e) {
					logger.error(e.getMessage());
				}
			}
			String tagDetailedData = fetchUrlData(
					TagConstants.UNANSWERED_ADDR1 + tagName + TagConstants.UNANSWERED_ADDR2);
			if (tagDetailedData == null || tagDetailedData.isEmpty()) {
				continue;
			}
			try {
				JSONObject jsObj = new JSONObject(tagDetailedData);
				int unAnsweredCount = jsObj.getInt(TagConstants.TOTAL_COUNT);
				TagInfo tagInfo = tagMap.get(tagName);
				tagInfo.setUnansweredCount(unAnsweredCount);
				tagMap.put(tagName, tagInfo);
			} catch (JSONException jsEx) {
				logger.error(jsEx.getMessage());
			}
		}
		return tagMap;
	}

}
