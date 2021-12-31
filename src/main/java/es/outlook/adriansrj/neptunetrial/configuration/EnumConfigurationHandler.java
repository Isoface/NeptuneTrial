package es.outlook.adriansrj.neptunetrial.configuration;

import es.outlook.adriansrj.neptunetrial.main.NeptuneTrial;
import es.outlook.adriansrj.neptunetrial.util.YamlUtil;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

/**
 * @author AdrianSR / 22/08/2021 / Time: 09:18 p. m.
 */
public abstract class EnumConfigurationHandler < E extends Enum < ? extends ConfigurationEntry > >
		extends SingleFileConfigurationHandler {
	
	/**
	 * Constructs the configuration handler.
	 *
	 * @param plugin the battle royale plugin instance.
	 */
	protected EnumConfigurationHandler ( NeptuneTrial plugin ) {
		super ( plugin );
	}
	
	@Override
	public void initialize ( ) {
		File file = safeGetFile ( );
		
		if ( !file.exists ( ) ) {
			try {
				file.getParentFile ( ).mkdirs ( );
				file.createNewFile ( );
			} catch ( IOException ex ) {
				throw new IllegalStateException (
						"couldn't generate configuration file: " + file.getName ( ) , ex );
			}
		}
		
		// saving defaults
		YamlConfiguration yaml = YamlConfiguration.loadConfiguration ( file );
		
		if ( saveDefaultConfiguration ( yaml ) > 0 ) {
			try {
				yaml.save ( file );
			} catch ( IOException ex ) {
				ex.printStackTrace ( );
			}
		}
		
		// then loading configuration
		loadConfiguration ( );
	}
	
	/**
	 * Gets the class that corresponds to the enumeration this handler handles.
	 *
	 * @return handling enum class.
	 */
	public abstract Class < E > getEnumClass ( );
	
	/**
	 * @return handling enum class.
	 *
	 * @throws IllegalArgumentException if returned null.
	 */
	protected final Class < E > safeGetEnumClass ( ) {
		Class < E > enum_class = getEnumClass ( );
		
		if ( enum_class != null ) {
			return enum_class;
		} else {
			throw new IllegalArgumentException ( "getEnumClass() returned null" );
		}
	}
	
	@Override
	public void loadConfiguration ( ) {
		YamlConfiguration yaml = YamlConfiguration.loadConfiguration ( safeGetFile ( ) );
		
		for ( E uncast : safeGetEnumClass ( ).getEnumConstants ( ) ) {
			( ( ConfigurationEntry ) uncast ).load ( yaml );
		}
	}
	
	/**
	 * Saves the entries that not set right now in the configuration file.
	 *
	 * @param yaml the configuration yaml.
	 *
	 * @return the number of changes performed.
	 */
	protected int saveDefaultConfiguration ( YamlConfiguration yaml ) {
		int save = 0;
		
		for ( E uncast : safeGetEnumClass ( ).getEnumConstants ( ) ) {
			ConfigurationEntry entry = ( ConfigurationEntry ) uncast;
			
			if ( YamlUtil.setNotSet ( yaml , entry.getKey ( ) , entry.getDefaultValue ( ) ) ) {
				save++;
			}
		}
		return save;
	}
	
	@Override
	public void save ( ) {
		File              file = safeGetFile ( );
		YamlConfiguration yaml = YamlConfiguration.loadConfiguration ( file );
		int               save = 0;
		
		for ( E uncast : safeGetEnumClass ( ).getEnumConstants ( ) ) {
			if ( uncast instanceof ConfigurationEntrySettable ) {
				ConfigurationEntrySettable entry = ( ConfigurationEntrySettable ) uncast;
				
				if ( YamlUtil.setNotEqual ( yaml , entry.getKey ( ) , entry.getValue ( ) ) ) {
					save++;
				}
			}
		}
		
		if ( save > 0 ) {
			try {
				yaml.save ( file );
			} catch ( IOException e ) {
				e.printStackTrace ( );
			}
		}
	}
}