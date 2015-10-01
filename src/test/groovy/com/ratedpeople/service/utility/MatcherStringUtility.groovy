package com.ratedpeople.service.utility

import java.util.regex.Matcher
import java.util.regex.Pattern

class MatcherStringUtility {


	public static String getMatch(String regex,String text){
		Pattern pattern = Pattern.compile(regex);
		// text contains the full text that you want to extract data
		Matcher matcher = pattern.matcher(text);

		while (matcher.find()) {
			String result = matcher.group(1);
			return result;
		}
		return null;
	}
}
