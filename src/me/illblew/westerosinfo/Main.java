
package me.illblew.westerosinfo;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Level;

import javax.net.ssl.SSLContext;

import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin implements Listener {

	private HashMap<String,String> responsedb = new HashMap<String,String>();

    private ArrayList<String> readLines(String filename) throws IOException   
    {  
        FileReader fileReader = new FileReader(new File(this.getDataFolder(),filename));  
          
        BufferedReader bufferedReader = new BufferedReader(fileReader);  
        ArrayList<String> lines = new ArrayList<String>();  
        String line = null;  
          
        while ((line = bufferedReader.readLine()) != null)   
        {  
            lines.add(line);  
        }  
          
        bufferedReader.close();  
          
        return lines;
    }   
	
	    public void onDisable() {
	       
	        log("Westeros Info has been disabled.");
	    }
	 
	    public void onEnable() {
	    	
	        PluginManager pm = getServer().getPluginManager();
	 
	        //Register all the events for pooping
	        pm.registerEvents(this, this);
                this.getDataFolder().mkdirs();
	 
	        log("Westeros Info has successfully enabled.");
	        try { //Attempt to load files
				for(String entry : this.readLines("list.wst")){
					String[] parts = entry.split(",", 2);
					if(parts.length == 2) {
						responsedb.put(simplify(parts[0].toLowerCase()), parts[1]);
					}
				}
			} catch (IOException e) {
			}
			
	    }
	 
	    public void log(String text) {
	    	this.getLogger().log(Level.INFO, text);
	    }
	    
	    private static String simplify(String s) {
	    	return s.replaceAll("[ \\?!\\.]", "");
	    }

	    @EventHandler
	    public void whenPlayerJoins(PlayerJoinEvent event) {
	    	
	      event.getPlayer().sendMessage(ChatColor.RED + "Welcome to Westeroscraft! Visit /warps for a list of warps!");
	    }
	    @EventHandler
	    public void whenPlayerSays(AsyncPlayerChatEvent event) {
                String response = responsedb.get(simplify(event.getMessage().toLowerCase()));
                if(response != null) {
                        event.setCancelled(true);
	                event.getPlayer().sendMessage(ChatColor.RED + response);
	            }
	        }
}



