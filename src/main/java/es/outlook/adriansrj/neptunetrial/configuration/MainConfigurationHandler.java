package es.outlook.adriansrj.neptunetrial.configuration;

import es.outlook.adriansrj.neptunetrial.enums.EnumFile;
import es.outlook.adriansrj.neptunetrial.enums.EnumMainConfiguration;
import es.outlook.adriansrj.neptunetrial.handler.PluginHandler;
import es.outlook.adriansrj.neptunetrial.main.NeptuneTrial;
import es.outlook.adriansrj.neptunetrial.util.YamlUtil;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

/**
 * @author AdrianSR / 16/12/2021 / 06:10 p. m.
 */
public final class MainConfigurationHandler extends PluginHandler {
	
	/**
	 * Constructs the plugin handler.
	 *
	 * @param plugin the plugin to handle.
	 */
	public MainConfigurationHandler ( NeptuneTrial plugin ) {
		super ( plugin );
		
		// saving defaults
		File              file = EnumFile.MAIN_CONFIGURATION_FILE.safeGetFile ( );
		YamlConfiguration yaml = YamlConfiguration.loadConfiguration ( file );
		
		if ( saveDefaultConfiguration ( yaml ) > 0 ) {
			try {
				yaml.save ( file );
			} catch ( IOException ex ) {
				ex.printStackTrace ( );
			}
		}
		
		// then loading configuration
		loadConfiguration ( yaml );
	}
	
	private int saveDefaultConfiguration ( ConfigurationSection section ) {
		int save = 0;
		
		for ( EnumMainConfiguration entry : EnumMainConfiguration.values ( ) ) {
			if ( YamlUtil.setNotSet ( section , entry.getKey ( ) , entry.getDefaultValue ( ) ) ) {
				save++;
			}
		}
		return save;
	}
	
	private void loadConfiguration ( ConfigurationSection section ) {
		for ( EnumMainConfiguration entry : EnumMainConfiguration.values ( ) ) {
			entry.load ( section );
		}
	}
}
