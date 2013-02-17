/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.koolsource.KSSM.Includes;

import java.util.Iterator;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author john
 */
public class LogWriter{

    private static final Logger log = Logger.getLogger("Minecraft");
    public static final String prefix = "[KSFB]";
    private static boolean debug;

    /**
     *
     * @param status
     */
    public void setDebug(boolean status){
        LogWriter.debug = status;
    }
    
    /**
     *
     * @return
     */
    public Logger getLog() {
        return log;
    }

    /**
     *
     * @return
     */
    public String getPrefix() {
        return prefix;
    }

    /**
     *
     * @param prefix
     */
    public void setPrefix(String prefix) {
        
    }
    
    /**
     *
     * @param message
     */
    public static void info(String message) {
        log.log(Level.INFO, LogWriter.prefix + " {0}", message);
    }
    
    /**
     *
     * @param message
     */
    public static void error(String message) {
        log.log(Level.SEVERE, LogWriter.prefix + " {0}", message);
    }

    /**
     *
     * @param message
     */
    public static void warning(String message) {
        log.log(Level.WARNING, LogWriter.prefix + " {0}", message);
    }

    /**
     *
     * @param message
     */
    public static void config(String message) {
        log.log(Level.CONFIG, LogWriter.prefix + " {0}", message);
    }

    /**
     *
     * @param level
     * @param message
     */
    public static void log(Level level, String message) {
        log.log(level, LogWriter.prefix + " {0}", message);
    }

    /**
     *
     * @param message
     */
    public static void debug(String message) {
        if (LogWriter.debug) {
            log.log(Level.INFO, LogWriter.prefix + "[DEBUG] {0}", message);
        }
    }
    
    /**
     *
     * @param map
     */
    public static void dump(Map map){
        Iterator it = map.entrySet().iterator();
        while(it.hasNext()){
            Map.Entry pairs = (Map.Entry)it.next();
            LogWriter.debug(pairs.getKey() + " = " + pairs.getValue());
        }
    }
    
    
}
