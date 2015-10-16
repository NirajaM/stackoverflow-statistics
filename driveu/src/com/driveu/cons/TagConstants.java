package com.driveu.cons;

/**
 * This class is used to hold all the constants needed for this project
 * @author niraja
 *
 */
public class TagConstants {

	public static final String DB_PROPERTIES = "db.properties";
	public static final String JDBC_DRIVER = "jdbc.driverClassName";
	public static final String JDBC_URL = "jdbc.url";
	public static final String JDBC_PASSEWORD = "jdbc.password";
	public static final String JDBC_USERNAME = "jdbc.username";
	public static final String TABLE_NAME = "TAG_INFO";
	public static final String ITEM = "items";
	public static final String TAG_NAME = "name";
	public static final String TAGS = "tags";
	public static final String IS_ANSWERED = "is_answered";
	public static final String TOTAL_COUNT = "total";
	public static final String COUNT = "count";
	public static final String TAG_50 = "https://api.stackexchange.com/2.2/tags?pagesize=50&order="
			+ "desc&sort=popular&site=stackoverflow";
	public static final String UNANSWERED_ADDR1 = "https://api.stackexchange.com/2.2/questions/unanswered?order=desc&sort=activity&tagged=";
	public static final String UNANSWERED_ADDR2 = "&site=stackoverflow&filter=total";
	/**
	 * link - https://api.stackexchange.com/docs/throttle
	 * Stackoverflow allows per ip 30 requests per second
	 */
	public static final int REQ_COUNT = 30;
	public static final String EMPTY_STR = "";
	public static final String ENCODING = "UTF-8";

}
