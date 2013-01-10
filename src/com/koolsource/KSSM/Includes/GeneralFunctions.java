/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.koolsource.KSSM.Includes;

/**
 *
 * @author john
 */
public class GeneralFunctions {

    public static String[] arrayShift(String[] anArray, int position) {
        String[] array = null;

        for (int i = (position - 1); i >= 0; i--) {
            array[i + 1] = array[i];
        }
        return array;
    }

    public static String combineSplit(int startIndex, String[] string, String seperator) {
        StringBuilder builder = new StringBuilder();

        for (int i = startIndex; i < string.length; i++) {
            builder.append(string[i]);
            builder.append(seperator);
        }

        builder.deleteCharAt(builder.length() - seperator.length());
        return builder.toString();
    }
    
    public static boolean isInt(String i){
        try{
            int parseInt = Integer.parseInt(i);
        }catch(NumberFormatException e){
            return false;
        }
        return true;
    }
}
