package com.driveu.util;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import com.driveu.cons.TagConstants;

/**
 * 
 * @author niraja
 *
 */
public class ConnectionUtil {

	public static final Logger LOGGER = LogManager.getLogger(ConnectionUtil.class);
	private static Properties properties;

	static {
		properties = ConnectionUtil.loadConfiguredProperty();
	}

	public static Connection connectDB(String DRIVER, String PROTOCOL, String... credential) {
		Connection connection = null;
		try {
			Class.forName(DRIVER).newInstance();
			if (credential.length == 0)
				connection = DriverManager.getConnection(PROTOCOL);
			else
				connection = DriverManager.getConnection(PROTOCOL, credential[0], credential[1]);

		} catch (SQLException | InstantiationException | IllegalAccessException | ClassNotFoundException ex) {
			LOGGER.error("Error in Connection Utility : ", ex);
			throw new RuntimeException(ex);
		}
		return connection;
	}

	public static Properties loadConfiguredProperty() {
		/*
		 * Loading Configurations given by user through db.properties
		 */
		Properties properties = new Properties();
		InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream(TagConstants.DB_PROPERTIES);
		if (is == null) {
			LOGGER.error("Unable to load configuration file. Using system defaults.");
			return null;
		}
		try {
			properties.load(is);
		} catch (IOException e) {
			LOGGER.error("Error occurred while loading properties file ... : " + e.getMessage());
		}
		return properties;
	}

	public static Connection getConnection() {
		return ConnectionUtil.connectDB(properties.getProperty(TagConstants.JDBC_DRIVER),
				properties.getProperty(TagConstants.JDBC_URL),
				new String[] { properties.getProperty(TagConstants.JDBC_USERNAME),
						properties.getProperty(TagConstants.JDBC_PASSEWORD) });
	}

}