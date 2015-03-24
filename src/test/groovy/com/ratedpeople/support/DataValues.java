/**
 * 
 */
package com.ratedpeople.support;

import java.util.HashMap;
import java.util.Map;

/**
 * @author shabana.khanam
 *
 */
public class DataValues {
		
	private static Map<String,String> requestValues = new HashMap<String,String>();
	
	static { 
		
		// Database values 
		requestValues.put("CONNECTURL","jdbc:mysql://minerva.ratedcloud.net:3306/");
		requestValues.put("DBUSERNAME","admin");
		requestValues.put("DBPWD","123456");
		requestValues.put("DBDRIVER","com.mysql.jdbc.Driver");	
		
		// User Creation Service 
		requestValues.put("HOUSER","ho.test."+System.currentTimeMillis()+"@gid.com");
		requestValues.put("TMUSER","tm.test."+System.currentTimeMillis()+"@gid.com");
				
		//HTTP Response Status
		requestValues.put("STATUS201","201");
		requestValues.put("STATUS400","400");
		requestValues.put("STATUS200","200");
		requestValues.put("STATUS401","401");
		requestValues.put("STATUS409","409");
				
		//Service URI 
		requestValues.put("URL","http://minerva.ratedcloud.net:8765");
		requestValues.put("USERSERVICE","/api/user/");
		requestValues.put("AUTHSERVICE","/api/uaa/");
		requestValues.put("PAYMENTSERVICE","/api/payment/");
		requestValues.put("BILLINGSERVICE","/api/billing/");
			
		//Default Client token
		requestValues.put("CLIENT_ID","9ecc8459ea5f39f9da55cb4d71a70b5d1e0f0b80:1");
		
		//Default User
		requestValues.put("USERNAME","functional.test.user@gid.com");
		requestValues.put("USERNAME_HO","ho.functional.test.user@gid.com");
		requestValues.put("USERIDTM","1");
		requestValues.put("USERIDHO","2");
		requestValues.put("CCTOKEN","83f1c8e83a004ebdb8c8c35362a688ff");
		
		requestValues.put("PASSWORD","password");
	
		//Default Merchant
		requestValues.put("CHANNELID","6c6dc1da6bc443e08510a36325ada090");
		requestValues.put("MERCHANT","4656");
		requestValues.put("RPUSERID","1");
				
		//Credit Card		
		requestValues.put("NAMEONCARD","davide");
		requestValues.put("CREDITCARDNUMBER","4000000000000051");
		requestValues.put("CV2","123");
		requestValues.put("EXPIRYMONTH","01");
		requestValues.put("EXPIRYYEAR","2016");
		requestValues.put("CARDTYPE","visa");
		requestValues.put("CC_USERID","34");
				
		//Credit Card validation Error
		requestValues.put("CREDITCARDVALIDATION","credit card has invalid characters - credit card number is compulsory - credit card has wrong size - ");
		requestValues.put("CVVVALIDATION","ccv has wrong size - ");
		requestValues.put("CARDEXISTS","Credit card already exists for this user");
		requestValues.put("EXPIRYYEARVALIDATION","year has invalid characters - year has wrong size - ");
		requestValues.put("EXPIRYMONTHVALIDATION","month has invalid characters - ");
		requestValues.put("CARDTYPEVALIDATION","type has invalid characters - ");
		requestValues.put("NAMEONCARDVALIDATION","name on card is compulsory - name on card has invalid characters - ");
		
		//Merchant Validation Error
		requestValues.put("USERIDVALIDATION","USER_ID_TM is compulsory - USER_ID_TM has invalid characters -");
		requestValues.put("MERCHANTEXISTS","Merchant with RP user id 1 already exists");	
		
		// Preauth
		requestValues.put("FROMUSERID","1");
		requestValues.put("TOUSERID","1");
		requestValues.put("JOBID","1");
		requestValues.put("TOKEN","");
		requestValues.put("CURRENCY","GBP");
		requestValues.put("SKRILLTRANSACTION","");
		requestValues.put("AMOUNT","1000");
		requestValues.put("FROMUSEREMAIL","davi@tin.it");
		requestValues.put("IP","12.12.12.12");
		/*requestValues.put("JOBID","123");
		requestValues.put("","01");
		requestValues.put("currency","2016");
		requestValues.put("skrillTransaction","visa");
		requestValues.put("amount","34");*/
		
		//bank details
		requestValues.put("BENEFICIARYNAME","Chips and");
		requestValues.put("BANKCODE","110311");
		requestValues.put("ACCOUNTNUMBER","11020088");
		requestValues.put("BANKACCOUNTTYPE","PERSONAL");
		requestValues.put("COUNTRY","UK");
		
		
		
	};
}
