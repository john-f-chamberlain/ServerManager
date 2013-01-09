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

    public void setDebug(boolean status){
        this.debug = status;
    }
    
    public Logger getLog() {
        return log;
    }

    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(String prefix) {
        
    }
    
    public static void info(String message) {
        log.log(Level.INFO, LogWriter.prefix + " " + message);
    }
    
    public static void error(String message) {
        log.log(Level.SEVERE, LogWriter.prefix + " " + message);
    }

    public static void warning(String message) {
        log.log(Level.WARNING, LogWriter.prefix + " " + message);
    }

    public static void config(String message) {
        log.log(Level.CONFIG, LogWriter.prefix + " " + message);
    }

    public static void log(Level level, String message) {
        log.log(level, LogWriter.prefix + " " + message);
    }

    public static void debug(String message) {
        if (LogWriter.debug) {
            log.log(Level.INFO, LogWriter.prefix + "[DEBUG] " + message);
        }
    }
    
    public static void dump(Map mp){
        Iterator it = mp.entrySet().iterator();
        while(it.hasNext()){
            Map.Entry pairs = (Map.Entry)it.next();
            LogWriter.debug(pairs.getKey() + " = " + pairs.getValue());
        }
    }
    
    
}
