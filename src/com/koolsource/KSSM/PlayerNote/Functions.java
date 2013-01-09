/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.koolsource.KSSM.PlayerNote;

import com.koolsource.KSSM.Main;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 *
 * @author john
 */
public class Functions {
    private final Main plugin;
    
    Functions(Main aThis){
        this.plugin = aThis;
    }

    boolean createNote(CommandSender aSender, String[] anArray) {
        if(!this.plugin.getPerm().has(aSender, "kssm.playernote.create")) {Main.noPermission((Player) aSender);return true;}
        
        return true;
    }

    boolean deleteNote(CommandSender aSender, String[] anArray) {
        if(!this.plugin.getPerm().has(aSender, "kssm.playernote.delete")) {Main.noPermission((Player) aSender);return true;}
        
        return true;
    }

    boolean viewNote(CommandSender aSender, String[] anArray) {
        if(!this.plugin.getPerm().has(aSender, "kssm.playernote")) {Main.noPermission((Player) aSender);return true;}
        
        return true;
    }
    
}
