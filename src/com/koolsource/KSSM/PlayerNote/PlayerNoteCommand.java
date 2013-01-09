/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.koolsource.KSSM.PlayerNote;

import com.koolsource.KSSM.Main;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import com.koolsource.KSSM.PlayerNote.Functions;
/**
 *
 * @author john
 */
public class PlayerNoteCommand implements CommandExecutor{
    private final Main plugin;
    private final Functions function;

    public PlayerNoteCommand(Main aThis) {
        this.function = new Functions(aThis);
        this.plugin = aThis;
    }

    @Override
    public boolean onCommand(CommandSender aSender, Command aCommand, String aString, String[] anArray) {
        // /note create playername This player was being bad.
        String command = anArray[0];
        
        if(command.equals("create")){
            return this.function.createNote(aSender, anArray);
        }else if(command.equals("delete")){
            return this.function.deleteNote(aSender, anArray);
        }else if(command.equals("view")){
            return this.function.viewNote(aSender, anArray);
        }
        
        
        return true;
    }
    
}
