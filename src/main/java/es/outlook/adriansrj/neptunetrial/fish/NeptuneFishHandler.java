package es.outlook.adriansrj.neptunetrial.fish;

import es.outlook.adriansrj.neptunetrial.configuration.ConfigurationHandler;
import es.outlook.adriansrj.neptunetrial.enums.EnumDirectory;
import es.outlook.adriansrj.neptunetrial.enums.EnumVanillaFish;
import es.outlook.adriansrj.neptunetrial.main.NeptuneTrial;
import es.outlook.adriansrj.neptunetrial.util.ConsoleUtil;
import es.outlook.adriansrj.neptunetrial.util.Constants;
import es.outlook.adriansrj.neptunetrial.util.file.YamlFileFilter;
import es.outlook.adriansrj.neptunetrial.util.fish.FishUtil;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.*;

/**
 * @author AdrianSR / 29/12/2021 / 03:23 p. m.
 */
public final class NeptuneFishHandler extends ConfigurationHandler {
	
	private static final NeptuneFish[] FISH_EXAMPLES = {
			new NeptuneFish ( "cod-fish" ,
							  ChatColor.GOLD + "Cod Fish" ,
							  Arrays.asList (
									  "" ,
									  ChatColor.YELLOW + "Not a big deal, keep fishing!" ,
									  "" ,
									  ChatColor.GOLD + "Weight: " + ChatColor.GREEN + Constants.WEIGHT_PLACEHOLDER + "oz"
							  ) ,
							  EnumVanillaFish.COD , 51.0D ) ,
			
			new NeptuneFish ( "fatty-cod-fish" ,
							  ChatColor.GOLD + "Fatty Cod Fish" ,
							  Arrays.asList (
									  "" ,
									  ChatColor.YELLOW + "A fatty fish for a fatty man" ,
									  "" ,
									  ChatColor.GOLD + "Weight: " + ChatColor.GREEN + Constants.WEIGHT_PLACEHOLDER + "oz"
							  ) ,
							  EnumVanillaFish.COD , 45.0D ) ,
			
			new NeptuneFish ( "salmon-ella" ,
							  ChatColor.GOLD + "Salmon-ella" ,
							  Arrays.asList (
									  "" ,
									  ChatColor.YELLOW + "No, you will not get" ,
									  ChatColor.YELLOW + "salmonella if you eat me," ,
									  ChatColor.YELLOW + "wont you?" ,
									  "" ,
									  ChatColor.GOLD + "Weight: " + ChatColor.GREEN + Constants.WEIGHT_PLACEHOLDER + "oz"
							  ) ,
							  EnumVanillaFish.SALMON , 21.5D ) ,
			
			new NeptuneFish ( "tropical" ,
							  ChatColor.GOLD + "Tropical" ,
							  Arrays.asList (
									  "" ,
									  ChatColor.YELLOW + "Tastes better when hot" ,
									  "" ,
									  ChatColor.GOLD + "Weight: " + ChatColor.GREEN + Constants.WEIGHT_PLACEHOLDER + "oz"
							  ) ,
							  EnumVanillaFish.TROPICAL , 5.5D ) ,
			
			new NeptuneFish ( "troublesome-trout" ,
							  ChatColor.GOLD + "Troublesome Trout" ,
							  Arrays.asList (
									  "" ,
									  ChatColor.YELLOW + "A slick devilish fish" ,
									  "" ,
									  ChatColor.GOLD + "Weight: " + ChatColor.GREEN + Constants.WEIGHT_PLACEHOLDER + "oz"
							  ) ,
							  EnumVanillaFish.PUFFERFISH , 11.1D ) ,
	};
	
	public static NeptuneFishHandler getInstance ( ) {
		return getPluginHandler ( NeptuneFishHandler.class );
	}
	
	private final Map < String, NeptuneFish > fish_map = new HashMap <> ( );
	
	/**
	 * Constructs the plugin handler.
	 *
	 * @param plugin the plugin to handle.
	 */
	public NeptuneFishHandler ( NeptuneTrial plugin ) {
		super ( plugin );
	}
	
	public Map < String, NeptuneFish > getFishMap ( ) {
		return Collections.unmodifiableMap ( fish_map );
	}
	
	public Collection < NeptuneFish > getFishes ( ) {
		return Collections.unmodifiableCollection ( fish_map.values ( ) );
	}
	
	public NeptuneFish getFish ( String name ) {
		return fish_map.get ( FishUtil.formatName ( name ) );
	}
	
	@Override
	public void initialize ( ) {
		// saving fish examples
		File directory = EnumDirectory.FISH_DIRECTORY.getDirectory ( );
		
		if ( !directory.exists ( ) ) {
			if ( directory.mkdirs ( ) ) {
				for ( NeptuneFish fish : FISH_EXAMPLES ) {
					File file = new File ( directory , fish.getName ( ).toLowerCase ( )
							+ "." + YamlFileFilter.YML_EXTENSION );
					
					try {
						if ( file.createNewFile ( ) ) {
							YamlConfiguration yaml = YamlConfiguration.loadConfiguration ( file );
							
							fish.save ( yaml );
							yaml.save ( file );
						} else {
							throw new IllegalStateException ( "couldn't generate example fish file" );
						}
					} catch ( IOException e ) {
						e.printStackTrace ( );
					}
				}
			} else {
				throw new IllegalStateException ( "couldn't generate fish directory" );
			}
		}
		
		// then loading configuration
		loadConfiguration ( );
	}
	
	@Override
	public void loadConfiguration ( ) {
		File   directory = EnumDirectory.FISH_DIRECTORY.getDirectory ( );
		File[] files     = directory.exists ( ) ? directory.listFiles ( new YamlFileFilter ( ) ) : null;
		
		if ( files != null ) {
			for ( File file : files ) {
				YamlConfiguration yaml = YamlConfiguration.loadConfiguration ( file );
				NeptuneFish       fish = new NeptuneFish ( ).load ( yaml );
				
				if ( fish.isValid ( ) ) {
					fish_map.put ( fish.getName ( ) , fish );
					
					ConsoleUtil.sendPluginMessage (
							ChatColor.GREEN , "'" + fish.getName ( ) + "' loaded." ,
							NeptuneTrial.getInstance ( ) );
				} else {
					ConsoleUtil.sendPluginMessage (
							ChatColor.RED , "Fish '" + file.getName ( ) + "' is invalid" ,
							NeptuneTrial.getInstance ( ) );
				}
			}
		} else {
			ConsoleUtil.sendPluginMessage (
					ChatColor.RED , "Fish directory is empty" ,
					NeptuneTrial.getInstance ( ) );
		}
	}
	
	@Override
	public void save ( ) {
		// nothing to save
	}
}
