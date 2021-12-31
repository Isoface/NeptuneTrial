package es.outlook.adriansrj.neptunetrial.storage;

import es.outlook.adriansrj.neptunetrial.enums.EnumDataStorage;
import es.outlook.adriansrj.neptunetrial.enums.EnumMainConfiguration;
import es.outlook.adriansrj.neptunetrial.handler.PluginHandler;
import es.outlook.adriansrj.neptunetrial.main.NeptuneTrial;
import es.outlook.adriansrj.neptunetrial.util.ConsoleUtil;
import es.outlook.adriansrj.neptunetrial.util.reflection.EnumReflection;
import org.bukkit.ChatColor;

/**
 * @author AdrianSR / 16/12/2021 / 06:02 p. m.
 */
public final class DataStorageHandler extends PluginHandler {
	
	public static DataStorageHandler getInstance ( ) {
		return getPluginHandler ( DataStorageHandler.class );
	}
	
	private final DataStorage data_storage;
	
	/**
	 * Constructs the plugin handler.
	 *
	 * @param plugin the plugin to handle.
	 */
	public DataStorageHandler ( NeptuneTrial plugin ) {
		super ( plugin );
		
		// this will throw an exception that
		// will disable the plugin in case
		// it cannot connect to the database.
		// trying to connect to database
		DataStorage data_storage = null;
		
		if ( EnumMainConfiguration.ENABLE_DATABASE.getAsBoolean ( ) ) {
			Exception error         = null;
			String    error_message = null;
			String    type_name     = EnumMainConfiguration.DATABASE_TYPE.getAsString ( );
			EnumDataStorage type = EnumReflection.getEnumConstant (
					EnumDataStorage.class , type_name.toUpperCase ( ) );
			
			if ( type != null ) {
				boolean successfully;
				
				try {
					data_storage = type.getImplementationClass ( ).getConstructor (
							NeptuneTrial.class ).newInstance ( plugin );
					
					if ( data_storage.setUp ( ) ) {
						successfully = true;
						ConsoleUtil.sendPluginMessage (
								ChatColor.GREEN , "Connected to database" , plugin );
					} else {
						successfully  = false;
						error_message = "Couldn't connect to database!";
					}
				} catch ( Exception ex ) {
					error         = ex;
					successfully  = false;
					error_message = "Couldn't connect to database!";
				}
				
				if ( !successfully && data_storage != null ) {
					data_storage.dispose ( );
					data_storage = null;
				}
			} else {
				error_message = "Unknown database type '" + type_name + "'!";
			}
			
			if ( error_message != null ) {
				ConsoleUtil.sendPluginMessage ( ChatColor.RED , error_message , plugin );
			}
			
			if ( error != null ) {
				error.printStackTrace ( );
			}
		}
		
		this.data_storage = data_storage;
	}
	
	public DataStorage getDataStorage ( ) {
		return data_storage;
	}
}
