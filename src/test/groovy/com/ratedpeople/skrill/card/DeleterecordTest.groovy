package com.ratedpeople.skrill.card

import spock.lang.Specification
import groovy.sql.Sql


//@Grab('mysql:mysql-connector-java:5.1.22')
class DeleterecordTest extends Specification  {

	def USER_ID ="34"
	def sqlconn = Sql.newInstance('jdbc:mysql://minerva.ratedcloud.net:3306/', 'admin','123456', 'com.mysql.jdbc.Driver')
//	def CREDITCARDID;

	def "test connection and see if records exists for credit card"(){

		when:
			sqlconn.eachRow('SELECT id FROM payment.credit_card')
			{ row ->
				String userid =  row.'id'
				println "credit card id  : "+userid

			}
		then:

		{	
			sqlconn.execute('delete  from payment.credit_card where user_id=1')
			println "you are in delete block"
			}
		

	}
	

		def "test connection and see if records exists for merchant"(){
		
				when:
					sqlconn.eachRow('SELECT merchant FROM payment.merchant'){ row ->
						println row.'rp_user_id'
						println "merchant details  :"+row.'rp_user_id'
					}
				then:
					sqlconn.execute('delete  from payment.merchant where rp_user_id=1'){
						row -> 	println row
						
					}
		

			}
			

	
	
}
