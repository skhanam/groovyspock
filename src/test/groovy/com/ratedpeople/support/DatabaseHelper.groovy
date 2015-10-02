package com.ratedpeople.support

import spock.lang.Specification
import groovy.sql.Sql


public abstract class DatabaseHelper {

	
	protected static final String DB_URL = System.getProperty("DB_URL", CommonVariable.DB_URL);
	
	def static sqlconn = Sql.newInstance(DB_URL, CommonVariable.DB_USERNAME,CommonVariable.DB_PWD, CommonVariable.DB_DRIVER)

	static executeQuery(final String query){
		sqlconn.execute(query)
	}
	
	static String select (final String query){
		return sqlconn.rows(query)
	
	}
}
