package com.ratedpeople.support

import spock.lang.Specification
import groovy.sql.Sql


class DatabaseHelper {

	def static sqlconn = Sql.newInstance(DataValues.requestValues.get("CONNECTURL"), DataValues.requestValues.get("DBUSERNAME"),DataValues.requestValues.get("DBPWD"), DataValues.requestValues.get("DBDRIVER"))

	static executeQuery(final String query){
		sqlconn.execute(query)
	}
	
	static String select (final String query){
		return sqlconn.rows(query)
	
	}
}
