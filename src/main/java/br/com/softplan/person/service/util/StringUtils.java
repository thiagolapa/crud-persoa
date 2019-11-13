package br.com.softplan.person.service.util;

import org.springframework.web.util.UriUtils;

import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.Calendar;

/**
 * Utility class to check strings
 */
public class StringUtils {

    /**
     * Check if a string value is null or empty.
     */
    public static boolean isNullOrEmpty(String value) {
        return value == null || value.isEmpty();
    }

    /**
     * Convert an Integer to String with leading zeroes.
     */
    public static String padLeft(Long number, Integer digits) {
        char[] zeros = new char[digits];
        Arrays.fill(zeros, '0');
        String format = String.valueOf(zeros);

        DecimalFormat df = new DecimalFormat(format);
        return df.format(number);
    }

    /**
     * Encode url
     */
    public static String encodeURL(String text) {
        try {
            return UriUtils.encode(text, "UTF-8");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * validSerialBoard
     * @param serialCode
     * @param isSerialProduct
     * @return true or false
     */
    public static Boolean validSerialProduct(String serialCode, boolean isSerialProduct){

        if (serialCode.length() == 12) {
            String company = String.valueOf(serialCode.charAt(0));
            company += String.valueOf(serialCode.charAt(1));

            String year = String.valueOf(serialCode.charAt(2));
            year += String.valueOf(serialCode.charAt(3));

            String week = String.valueOf(serialCode.charAt(4));
            week += String.valueOf(serialCode.charAt(5));

            Integer type = Integer.parseInt(String.valueOf(serialCode.charAt(6)));

            String serial = "";
            for (int i = 7; i < serialCode.length(); i++) {
                serial += String.valueOf(serialCode.charAt(i));
            }

            if(company.equals("TT") == true && isSerialProduct){
                return validateWeeks(serial, week, year, type, true);
            } else if(company.equals("TJ") == true && !isSerialProduct){
                return true;
            }
        }
        return false;
    }

    /**
     * validSerialBoard
     * @param SerialBoard
     * @return true or false
     */
    public static Boolean validSerialBoard(String SerialBoard){

        if (SerialBoard.length() == 12) {
            String company = String.valueOf(SerialBoard.charAt(0));
            company += String.valueOf(SerialBoard.charAt(1));

            String year = String.valueOf(SerialBoard.charAt(2));
            year += String.valueOf(SerialBoard.charAt(3));

            String week = String.valueOf(SerialBoard.charAt(4));
            week += String.valueOf(SerialBoard.charAt(5));

            Integer type = Integer.parseInt(String.valueOf(SerialBoard.charAt(6)));

            String serial = "";
            for (int i = 7; i < SerialBoard.length(); i++) {
                serial += String.valueOf(SerialBoard.charAt(i));
            }

            if(company.equals("TJ") == true){
                return validateWeeks(serial, week, year, type,false);
            }
        }
        return false;
    }

     /**
     * validSerialBoard
     * @param serial
     * @param year
     * @return true or false
     */
    private static Boolean validateWeeks(String serial, String week, String year, Integer type, boolean serialProduct) {
        Calendar cal = Calendar.getInstance();
        Calendar calSerial = Calendar.getInstance();
        calSerial.setFirstDayOfWeek(Calendar.MONDAY);
        calSerial.setMinimalDaysInFirstWeek(4); // ISO 8601 standard
        int yearSerial = Integer.parseInt("20" + year);
        calSerial.set(Calendar.YEAR, yearSerial);
        int yearTmp = cal.get(Calendar.YEAR);
        String lastYear  = Integer.toString((Integer)yearTmp);
        Integer last = Integer.parseInt(lastYear.substring(2));
        String maxSerialStr = "";
        for (int i = 0; i < serial.length(); i++) {
            maxSerialStr += 9;
        }
        int newSerial = Integer.parseInt(serial);
        int maxSerial = Integer.parseInt(maxSerialStr);

        if ((Integer.parseInt(year) == last) || (Integer.parseInt(year) == (last - 1))|| (Integer.parseInt(year) == (last + 1))){
            if(Integer.parseInt(week) > 0 && Integer.parseInt(week) <= calSerial.getWeeksInWeekYear() ){ // 53 or 52 Number max week valid
                if(Integer.parseInt(serial) > 0 && Integer.parseInt(serial) <= maxSerial){ // 999999 Number max serial valid
                    if(serialProduct){
                        if((type == 0|| type == 1 || type == 2) && (newSerial < 514)){ // 1 == product 12wt and 2 == product 24wt
                            return true;
                        }else {
                            if((type == 0 || type == 1 || type == 2) && (newSerial > 513)) { // 1 == product 12wt and 2 == product 24wt
                                return true;
                            }
                        }
                    }else {
                        if(type == 1 || type == 2){ // 1 == product 12wt and 2 == product 24wt
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }
}
