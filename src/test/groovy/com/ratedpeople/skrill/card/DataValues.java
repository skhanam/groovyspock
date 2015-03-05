/**
 * 
 */
package com.ratedpeople.skrill.card;

import java.util.HashMap;
import java.util.Map;
import org.apache.commons.codec.binary.Base64;

/**
 * @author shabana.khanam
 *
 */
public  class DataValues {
	
	
	public static Map<String,String> responseToken = new HashMap<String,String>();
	
	public static Map<String,String> requestValues = new HashMap<String,String>(){ 
		{
	
		put("CREDITCARDNUMBER","4000000000000051");
		put("CV2","123");
		put("EXPIRYYEAR","2016");
		put("EXPIRYMONTH","01");
		put("NAMEONCARD","davide");
		put("CARDTYPE","visa");
		put("STATUS201","201");
		put("STATUS400","400");
		put("USERNAME","davide");
		put("PASSWORD","password");
		put("STATUS200","200");
		put("STATUS401","401");
		put("TOKEN","9ecc8459ea5f39f9da55cb4d71a70b5d1e0f0b80:test12");
		
		// CARD services 
		put("USERSERVICE","/api/user/");
		put("AUTHSERVICE","/api/uaa/");
		put("PAYMENTSERVICE","/api/payment/");
		put("URL","http://minerva.ratedcloud.net:8765");
		put("USERID","34");
		put("CREDITCARDVALIDATION","credit card has wrong size - credit card has invalid characters - credit card number is compulsory -");
		put("USERIDVALIDATION","userId is compulsory - userId has invalid characters -");
		put("CVVVALIDATION","ccv has wrong size - ccv has invalid characters -");
		put("EXPIRYYEARVALIDATION","year has invalid characters - year has wrong size -");
		put("EXPIRYMONTHVALIDATION","month has invalid characters -");
		put("NAMEONCARDVALIDATION","name on card has invalid characters - name on card is compulsory -");
		put("CARDTYPEVALIDATION","credit card has wrong size -");
		put("CARDEXISTS","Credit card already exists for this user");
		put("MERCHANTEXISTS","Merchant with RP user id 1 already exists");
		
		//MERCHANT POST
		put("CHANNELID","6c6dc1da6bc443e08510a36325ada090");
		put("MERCHANT","4656");
		put("RPUSERID","1");
		put("","");
		put("","");
		put("","");
		put("","");
		put("","");
		put("","");
		put("","");
		put("","");
		put("","");
		put("","");
		put("","");
		put("","");
		put("","");
		put("","");
		put("","");
		put("","");
		put("","");
		put("","");
		put("","");
		put("","");
		put("","");
		put("","");
		put("","");
		put("","");
		put("","");
		put("","");
		put("","");
		put("","");
		put("","");
		put("","");
		put("","");
		put("","");
		put("","");
		put("","");
		put("","");
		put("","");
		put("","");
		put("","");
		put("","");
		put("","");
		put("","");
		put("","");
		put("","");
		put("","");
		put("","");
		put("","");
		put("","");
		put("","");
		put("","");
		put("","");
		
		}				
	};
	
//	public static String generateToken(){
//	byte[] gentoken = requestValues.get("TOKEN").bytes.encodeBase64().toString();
//		return gentoken;
//	}
}
