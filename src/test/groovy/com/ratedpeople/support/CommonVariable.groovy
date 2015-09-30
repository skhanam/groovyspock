package com.ratedpeople.support


public abstract class CommonVariable {
	
	public static final String DB_URL = "jdbc:mysql://minerva.ratedcloud.net:3306/"
	public static final String DB_USERNAME = "ratedpeople"
	public static final String DB_PWD = "ratedpeople"
	public static final String DB_DRIVER = "com.mysql.jdbc.Driver"
	
//	public static final String SERVER_URL = System.getProperty('env')
	public static final String SERVER_URL = "http://minerva.ratedcloud.net:8765"
//	public static final String SERVER_URL = "http://davide-devbox.ratedcloud.net:8765"
	public static final String USER_SERVICE_PREFIX = "/api/user/"
	public static final String AUTHORIZATION_SERVER_PREFIX = "/api/uaa/"
	public static final String PAYMENT_SERVICE_PREFIX = "/api/payment/"
	public static final String BILLING_SERVICE_PREFIX = "/api/billing/"
	public static final String RATING_SERVICE_PREFIX = "/api/rating/"
	public static final String HOPROFILE_SERVICE_PREFIX = "/api/hoprofile/"
	public static final String JOB_SERVICE_PREFIX = "/api/chores/"
	public static final String TMPROFILE_SERVICE_PREFIX = "/api/tmprofile/"
	public static final String LOCATION_SERVICE_PREFIX = "/api/location/"
	
	public static final String DEFAULT_MOBILE_PREFIX = "07"
	public static final String DEFAULT_LANDLINE_PREFIX = "02"
	
	/* Common URL section*/
	public static final String DEFAULT_GET_TOKEN_URI = CommonVariable.AUTHORIZATION_SERVER_PREFIX + "oauth/token"
	public static final String DEFAULT_ME_URI = CommonVariable.USER_SERVICE_PREFIX + "v1.0/me"
	
	/* Response Status Code Section */
	public static final String STATUS_201 = "201"
	public static final String STATUS_400 = "400"
	public static final String STATUS_200 = "200"
	public static final String STATUS_401 = "401"
	public static final String STATUS_409 = "409"
	
	/* USER Account and Authorization Section */
	public static final String DEFAULT_CLIENT_CREDENTIAL = "9ecc8459ea5f39f9da55cb4d71a70b5d1e0f0b80:1"
	
	public static final String HO_USER_PREFIX = "ho.test."
	public static final String TM_USER_PREFIX = "tm.test."
	public static final String DEFAULT_TM_USERNAME = "functional.test.user@gid.com"
	public static final String DEFAULT_HO_USERNAME = "ho.functional.test.user@gid.com"
	public static final String DEFAULT_ADMIN_USERNAME = "admin.test.user@gid.com"
	public static final String DEFAULT_PASSWORD = "password"
	
	public static final String DEFAULT_TM_ID = "1"
	public static final String DEFAULT_HO_ID = "2"
	public static final String DEFAULT_ADMIN_ID = "3"
	
	/* Address Details Section */
	public static final String DEFAULT_LINE1 = "some address line1"
	public static final String DEFAULT_LINE2 = "some address line2"
	public static final String DEFAULT_CITY = "London"
	public static final String DEFAULT_COUNTRY = "united kingdom"
	public static final String DEFAULT_POSTCODE = "SE18NW"
	public static final String OUT_OF_AREA_POSTCODE ="M21WL"
	
	public static final String ADDRESS_TYPE_BUSINESS = "BUSINESS"
	public static final String ADDRESS_TYPE_HOME = "HOME"
	
	/* Homeowner Details Section */
	public static final String DEFAULT_HO_FIRSTNAME = "hoprofile"
	public static final String DEFAULT_HO_LASTNAME = "aws"
	
	
	/* tradesman Details Section */
	public static final String DEFAULT_TM_FIRSTNAME = "tmprofile"
	public static final String DEFAULT_TM_LASTNAME = "aws"

	/* User Status */
	public static final String STATUS_ACTIVE = "ACTIVE"
	public static final String STATUS_PENDING = "PENDING"
	public static final String STATUS_BLACKLISTED = "BLACKLISTED"
	
	/* Credit Card Section*/
	public static final String DEFAULT_CC_TOKEN = "83f1c8e83a004ebdb8c8c35362a688ff"
	public static final String DEFAULT_CC_NAME = "davide"
	public static final String DEFAULT_CC_NUMBER = "4000000000000051"
	public static final String DEFAULT_CC_CVV = "123"
	public static final String DEFAULT_CC_EXPIRY_MONTH = "01"
	public static final String DEFAULT_CC_EXPIRY_YEAR = "2016"
	public static final String DEFAULT_CC_TYPE = "visa"
	public static final String DEFAULT_CC_USER_ID = "34"
	public static final String UNIQUE_CC_NUMBER = "4000000000000"
	
	/* Credit Card Validation Error Messages*/
	public static final String CC_VALIDATION = "credit card has invalid characters - credit card number is compulsory - credit card has wrong size -   "
	public static final String CVV_VALIDATION = "ccv has wrong size -  "
	public static final String CARD_ALREADY_EXISTS = "Credit card already exists for this user  "
	public static final String EXPIRY_YEAR_VALIDATION = "year has invalid characters - year has wrong size -   "
	public static final String EXPIRY_MONTH_VALIDATION = "month has invalid characters -   "
	public static final String CARD_TYPE_VALIDATION = "type has invalid characters -   "
	public static final String CC_NAME_VALIDATION = "name on card is compulsory - name on card has invalid characters -   "
	
	/* Bank Details Section */
	private static final String DEFAULT_BENEFICIARY_NAME = "Test Bank Account"
	private static final String DEFAULT_BANKCODE = "110311"
	private static final String DEFAULT_ACCOUNT_NUMBER = "11020088"
	private static final String DEFAULT_BANK_ACCOUNT_TYPE = "PERSONAL"
	private static final String DEFAULT_BANK_COUNTRY = "UK"
	
	/* Pre Auth Section */
	private static final String DEFAULT_SKRILL_TRANSACTION_ID = ""
	private static final String DEFAULT_IP = "12.12.12.12"
	private static final String DEFAULT_CURRENCY = "GBP"
	private static final String DEFAULT_AMOUNT = "1000"
	
	
	/* Job section */
	
	private static final String DEFAULT_TITLE = "CLEANER"
	private static final String DEFAULT_DESCRIPTION = "This is a test job with 30 or more character"
	private static final String DEFAULT_HOURRATE = "12.00"
	private static final String DEFAULT_TRADE_ID = "2"
	
	/* Working area */
	
	private static final String DEFAULT_LONGITUDE = "-0.057477"
	private static final String DEFAULT_LATITUDE = "51.5009274587931"
	private static final String DEFAULT_RADIUS = "1"
	
	
	/* Company details */
	
	private static final String DEFAULT_COMPANY_NAME = "GID Company"
	
	/* Image Section */
	private static final String IMAGE_TYPE_PROFILE = "PROFILE"
	
}
