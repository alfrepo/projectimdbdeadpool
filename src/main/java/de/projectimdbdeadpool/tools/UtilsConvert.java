package de.projectimdbdeadpool.tools;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.ParseException;

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

}
