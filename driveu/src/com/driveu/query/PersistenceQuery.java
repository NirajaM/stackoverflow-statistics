package com.driveu.query;

/**
 * 
 * @author niraja
 *
 */
public class PersistenceQuery {

	public static final String CREATE_TABLE = "CREATE TABLE TAG_INFO ( TAG_NAME varchar(50) not null," +
			"TOTAL_COUNT int, UNANSWERED_COUNT int , CONSTRAINT PK_TAG PRIMARY KEY (TAG_NAME))";
	public static final String INSERT_SCRIPT = "INSERT INTO TAG_INFO ( TAG_NAME, TOTAL_COUNT,"
			+ " UNANSWERED_COUNT) VALUES " ;
	public static final String  FETCH_DATA = "SELECT * FROM TAG_INFO";
	public static final String  DROP_TABLE = "DROP TABLE TAG_INFO";
}
