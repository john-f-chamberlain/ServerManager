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

    /**
     *
     * @param anArray
     * @param position
     * @return
     */
    public static String[] arrayShift(String[] anArray, int position) {
        String[] array = null;

        for (int i = (position - 1); i >= 0; i--) {
            array[i + 1] = array[i];
        }
        return array;
    }

    /**
     * Concatenates an array of strings into a single string starting at array
     * index <em>startIndex</em> and separated by <em>separator</em>
     * 
     * @param startIndex Integer
     * @param string String
     * @param seperator String
     * @return
     */
    public static String combineSplit(int startIndex, String[] string, String seperator) {
        StringBuilder builder = new StringBuilder();

        for (int i = startIndex; i < string.length; i++) {
            builder.append(string[i]);
            builder.append(seperator);
        }

        builder.deleteCharAt(builder.length() - seperator.length());
        return builder.toString();
    }
    
    /**
     * Determines if a string is an integer in a safe manner, used primarily for
     * MySQL queries.
     * 
     *
     * @param i String
     * @return Boolean
     */
    public static boolean isInt(String i){
        try{
            int parseInt = Integer.parseInt(i);
        }catch(NumberFormatException e){
            return false;
        }
        return true;
    }
    
    /**
     * A very definitive way of determining if a string is empty
     * Checks if a string is not only null but empty or zero length
     * 
     * @param i String
     * @return boolean
     */
    public static boolean isNull(String i){
        if(i == null){
            return true;
        }
        if(i.equals("")){
            return true;
        }
        if(i.trim().equals("")){
            return true;
        }
        if(i.isEmpty()){
            return true;
        }
        if(i.length() == 0){
            return true;
        }
        return false;
        
    }
    /**
     * Replication of PHP's <em>fstring</em> function
     * Produces a formatted string based on a template
     * 
     * @param original
     * @return
     */
    public static String fString(String original){
        return null;
        
        
    }
}
