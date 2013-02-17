/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.koolsource.KSSM.PlayerList;

import com.koolsource.KSSM.Includes.WebReader;
import com.koolsource.KSSM.Main;

/**
 *
 * @author Johnathan Chamberlain
 */
public class Functions {
    private final Main plugin;
    
    Functions(Main aThis){
        this.plugin = aThis;
    }
    
    public void clearList(){
        WebReader web = this.plugin.getWeb("PlayerList", "ClearList");
        web.makeRequest();
    }
    
}
