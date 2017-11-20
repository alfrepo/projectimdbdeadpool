package de.projectimdbdeadpool.tools;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.ParseException;

import org.joda.time.DateTime;

public class UtilsConvert {

	public static void main(String[] args) {
		System.out.println(toDouble("185,088"));
		System.out.println(toDouble("8.5"));
	}
	
	public static Double toDouble(String stringValue){
		Number n = toNumber(stringValue);
		if(n!=null){
			return n.doubleValue();
		}
		return null;
	}
	
	public static Long toLong(String stringValue){
		Number n = toNumber(stringValue);
		if(n!=null){
			return n.longValue();
		}
		return null;
	}
	
	public static Number toNumber(String stringValue){
		DecimalFormat df = new DecimalFormat();
		DecimalFormatSymbols symbols = new DecimalFormatSymbols();
		symbols.setDecimalSeparator('.');
		symbols.setGroupingSeparator(',');
		df.setDecimalFormatSymbols(symbols);
		try {
			return df.parse(stringValue);
		} catch (ParseException e) {
			return null;
		}
	}
	
	public static final com.google.cloud.Timestamp toTimeStamp(org.joda.time.DateTime dateTime){
		return com.google.cloud.Timestamp.ofTimeMicroseconds(dateTime.getMillis());
	}
	public static final DateTime toDateTime(com.google.cloud.Timestamp timeStamp){
		return new org.joda.time.DateTime(timeStamp.getSeconds()*1000);
	}

}
