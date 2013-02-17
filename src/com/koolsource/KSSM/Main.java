package com.koolsource.KSSM;

import com.koolsource.KSSM.BanList.BanListCommand;
import com.koolsource.KSSM.BanList.BanListListener;
import com.koolsource.KSSM.ForumBridge.ForumBridgeCommand;
import com.koolsource.KSSM.Includes.LogWriter;
import com.koolsource.KSSM.Includes.WebReader;
import com.koolsource.KSSM.IssueReport.IssueReportCommand;
import com.koolsource.KSSM.PlayerList.PlayerListCommand;
import com.koolsource.KSSM.PlayerNote.PlayerNoteCommand;
import com.koolsource.KSSM.PlayerNote.PlayerNoteListener;
import com.koolsource.KSSM.Verification.VerificationCommand;
import net.milkbowl.vault.chat.Chat;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.permission.Permission;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {

    // How many warns are needed to initiate an autokick?
    public static final Integer autoKickThreshold = 3;
    // How many autokicks are needed to initiate an autoTempBan?
    public static final Integer autoTempbanThreshold = 3;
    // How long should the temporary ban last if issued?
    public static final String autoTempbanDuration = "1d";
    // How many autoTempBans are needed to initiate an autoPermBan?
    public static final Integer autoBanThreshold = 9;
    //                                          You have been kicked for being warned too many times.\\
    //                                          You have been temporarily banned for being kicked too many times.
    public static final String autoKickReason = "You have been %s for being %s too many times.";
    // Debug flag enables additional, verbouse debug messages including web-call data.
    private static Boolean debug = true;
    // Logo used to prefix most chat messages.
    public static final String ChatLogo = ChatColor.RED + "[" + ChatColor.YELLOW + "KSFB" + ChatColor.RED + "] " + ChatColor.RESET;
    private Permission perms;
    private Economy econ;
    private Chat chat;
    private static Boolean error = false;
    private static String httpUrl = "http://control.koolsource.com/api.php";
    private static String securityKey = ""
            + "9B6114D977501345C800EE1358D542FB";
    /*+ "3EFDBCBEDE5234C19C9301648AD703E1"
     + "7A432D10B28832EC550D9ACA99064F30"
     + "8CDD487818F436A19C048251E82C1F1C"
     + "7160A8AC727CF7CC48B6741FC093493B"
     + "BCAA81C5FDC1F4E2681B297032F27980"
     + "D80EE3F532CF9A73333DB1460FED0BF7"
     + "FD70AF504408FEE216CF3AF2F55FDD5D"
     + "D0837CCA2E88D96EAF0AF0A064E5B9BC"
     + "6755A6F24D912B371F6111178D508745"
     + "20923FC28EF6A949CA9ACF843A0604CC"
     + "79E5FF0F1EDFEA20653067F07B859F5A"
     + "22E1A4D005AA936472DF68671AC0897E"
     + "38BF049F927370204D811E01CB161808"
     + "0DF417E5B5599D3ECF30229A53A32A34"
     + "615E3A1E5069FF2DFC50B7B611AE3627";*/

    /**
     *
     */
    @Override
    public void onEnable() {

        if (!this.setupPermissions() && !this.setupChat() && !this.setupEconomy()) {
            LogWriter.error("Disabling due to no Vault dependency found!");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }

        BanListCommand BanListCommand = new BanListCommand(this);
        ForumBridgeCommand ForumBridgeCommand = new ForumBridgeCommand(this);
        IssueReportCommand IssueReportCommand = new IssueReportCommand(this);
        PlayerNoteCommand PlayerNoteCommand = new PlayerNoteCommand(this);
        VerificationCommand VerificationCommand = new VerificationCommand(this);
        PlayerListCommand PlayerListCommand = new PlayerListCommand(this);

        //# Banlist
        this.getServer().getPluginManager().registerEvents(new BanListListener(this), this);
        this.getCommand("checkban").setExecutor(BanListCommand);
        this.getCommand("ban").setExecutor(BanListCommand);
        this.getCommand("kick").setExecutor(BanListCommand);
        this.getCommand("warn").setExecutor(BanListCommand);
        this.getCommand("unban").setExecutor(BanListCommand);

        //# Forum Bridge
        //this.getServer().getPluginManager().registerEvents(new ForumBridgeListener(this), this);
        //this.getCommand("KSFB").setExecutor(ForumBridgeCommand);
        //this.getCommand("sync").setExecutor(ForumBridgeCommand);

        //# Issue Reports
        //this.getServer().getPluginManager().registerEvents(new IssueReportListener(this), this);
        //this.getCommand("KSIR").setExecutor(IssueReportCommand);

        //# Player List
        //this.getServer().getPluginManager().registerEvents(new PlayerListListener(this), this);
        //this.getCommand("KSPL").setExecutor(PlayerListCommand);

        //# Player Notes
        this.getServer().getPluginManager().registerEvents(new PlayerNoteListener(this), this);
        this.getCommand("noteadd").setExecutor(PlayerNoteCommand);
        this.getCommand("noteaddoff").setExecutor(PlayerNoteCommand);
        this.getCommand("noterem").setExecutor(PlayerNoteCommand);
        this.getCommand("notelist").setExecutor(PlayerNoteCommand);
        this.getCommand("watch").setExecutor(PlayerNoteCommand);
        this.getCommand("unwatch").setExecutor(PlayerNoteCommand);
        

        //# Verification
        //this.getServer().getPluginManager().registerEvents(new VerificationListener(this), this);
        //this.getCommand("KSVC").setExecutor(VerificationCommand);


        LogWriter.info("KSSM Loaded Successfully");
    }

    /**
     * Sets up the WebReader class for easy use, automatically adds required
     * parameters.
     *
     * @param aSection Section of the API to request.
     * @param anAction Action of the API section to request.
     * @return WebReader
     */
    public WebReader getWeb(String aSection, String anAction) {
        WebReader web = new WebReader();
        web.setURL(httpUrl);
        web.setType(web.POST);
        web.setParam("SecurityKey", securityKey);
        web.setParam("ServerID", this.getServer().getServerId());
        web.setParam("Section", aSection);
        web.setParam("Action", anAction);
        return web;
    }

    /**
     * Finds a player based on partial match anywhere in their real name or
     * nickname. Real names take priority over nicknames when a request is made.
     * As soon as a match is made that player's object is returned. If no match
     * is found <em>null</em> is returned.
     *
     * @param name
     * @return Player or Null if no player is found.
     */
    public Player findPlayer(String name) {
        String partName;
        for (Player p : this.getServer().getOnlinePlayers()) {
            partName = p.getName();
            if (partName.contains(name)) {
                LogWriter.debug("[FindPlayer] Found player with displayname with string: " + name + ". RealName: " + p.getName());
                return p;
            }
        }

        for (Player p : this.getServer().getOnlinePlayers()) {
            partName = p.getDisplayName();
            if (partName.contains(name)) {
                LogWriter.debug("[FindPlayer] Found player with displayname with string: " + name + ". DisplayName: " + partName + "; RealName: " + p.getName());
                return p;
            }
        }

        LogWriter.debug("[FindPlayer] Unable to find player with string: " + name);
        return null;

    }

    private boolean setupPermissions() {
        if (getServer().getPluginManager().getPlugin("Vault") == null) {
            LogWriter.debug("[PRM] Unable to find Vault Plugin");
            return false;
        }
        RegisteredServiceProvider<Permission> rsp = getServer().getServicesManager().getRegistration(Permission.class);
        if (rsp == null) {
            LogWriter.debug("[PRM] Unable to register service provider");
            return false;
        }
        perms = rsp.getProvider();
        if (perms == null) {
            LogWriter.debug("[PRM] Provider = null");
        }
        return perms != null;
    }

    private boolean setupEconomy() {
        if (getServer().getPluginManager().getPlugin("Vault") == null) {
            LogWriter.debug("[ECO] Unable to find Vault Plugin");
            return false;
        }
        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            LogWriter.debug("[ECO] Unable to register service provider");
            return false;
        }
        econ = rsp.getProvider();
        if (econ == null) {
            LogWriter.debug("[ECO] Provider = null");
        }
        return econ != null;
    }

    private boolean setupChat() {

        if (getServer().getPluginManager().getPlugin("Vault") == null) {
            LogWriter.debug("[CHT] Unable to find Vault Plugin");
            return false;
        }
        RegisteredServiceProvider<Chat> rsp = getServer().getServicesManager().getRegistration(Chat.class);
        if (rsp == null) {
            LogWriter.debug("[CHT] Unable to register service provider");
            return false;
        }
        chat = rsp.getProvider();
        if (chat == null) {
            LogWriter.debug("[CHT] Provider = null");
        }
        return chat != null;
    }

    /**
     * Returns the current Debug status.
     *
     * @return Boolean
     */
    public static boolean getDebug() {
        return Main.debug;
    }

    /**
     * Returns a reference to the Permissions class.
     *
     * @return Permission class reference.
     */
    public Permission getPerm() {
        return this.perms;
    }

    /**
     * Returns a reference to the Economy class.
     *
     * @return Economy class reference.
     */
    public Economy getEco() {
        return this.econ;
    }

    /**
     * Returns a reference to the Chat class.
     *
     * @return Chat class reference.
     */
    public Chat getChat() {
        return this.chat;
    }

    /**
     * Returns the security key for making web calls.
     *
     * @deprecated Just request Main.securityKey.
     * @return SecurityKey
     */
    private static String getSecurityKey() {
        return Main.securityKey;
    }

    /**
     * Returns the HTTP URL for making web calls.
     *
     * @deprecated Just request Main.httpUrl.
     * @return HTTP URL
     */
    public static String getHttpUrl() {
        return Main.httpUrl;
    }

    /**
     * Sends a pre-formatted "no permission" message to the player
     * <em>aSender</em>.
     *
     * @param aSender Player class reference.
     */
    public static void noPermission(Player aSender) {
        aSender.sendMessage(ChatColor.RED + "You do not have access to this command.");
    }
}
