package es.outlook.adriansrj.neptunetrial.main;

import es.outlook.adriansrj.neptunetrial.configuration.ConfigurationHandler;
import es.outlook.adriansrj.neptunetrial.enums.EnumDirectory;
import es.outlook.adriansrj.neptunetrial.enums.EnumPluginHandler;
import es.outlook.adriansrj.neptunetrial.handler.PluginHandler;
import org.bukkit.plugin.java.JavaPlugin;

public final class NeptuneTrial extends JavaPlugin {
	
	public static NeptuneTrial getInstance ( ) {
		return getPlugin ( NeptuneTrial.class );
	}
	
	@Override
	public void onEnable ( ) {
		// generating required directories
		for ( EnumDirectory directory : EnumDirectory.values ( ) ) {
			if ( directory.isRequired ( ) ) {
				directory.getDirectory ( ).mkdirs ( );
			}
		}
		
		// initializing handlers
		for ( EnumPluginHandler handler : EnumPluginHandler.values ( ) ) {
			try {
				if ( handler.canInitialize ( ) ) {
					PluginHandler instance = handler.getHandlerClass ( )
							.getConstructor ( NeptuneTrial.class )
							.newInstance ( this );
					
					// ConfigurationHandler instances need to be initialized.
					if ( instance instanceof ConfigurationHandler ) {
						( ( ConfigurationHandler ) instance ).initialize ( );
					}
				}
			} catch ( Exception e ) {
				e.printStackTrace ( );
			}
		}
	}
}
