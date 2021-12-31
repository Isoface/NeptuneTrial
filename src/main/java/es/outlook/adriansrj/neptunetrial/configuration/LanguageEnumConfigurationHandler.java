package es.outlook.adriansrj.neptunetrial.configuration;

import es.outlook.adriansrj.neptunetrial.main.NeptuneTrial;
import org.apache.commons.lang.StringEscapeUtils;
import org.bukkit.configuration.file.YamlConfiguration;

/**
 * @author AdrianSR / 26/12/2021 / 02:02 p. m.
 */
public abstract class LanguageEnumConfigurationHandler < E extends Enum < ? extends ConfigurationEntry > >
		extends EnumConfigurationHandler < E > {
	
	/**
	 * Constructs the configuration handler.
	 *
	 * @param plugin the battle royale plugin instance.
	 */
	protected LanguageEnumConfigurationHandler ( NeptuneTrial plugin ) {
		super ( plugin );
	}
	
	// we must escape the content of the entries
	// at the moment of saving defaults as there
	// are some entries that contains new-lines.
	@Override
	protected int saveDefaultConfiguration ( YamlConfiguration yaml ) {
		int save = 0;
		
		for ( E uncast : safeGetEnumClass ( ).getEnumConstants ( ) ) {
			ConfigurationEntry entry         = ( ConfigurationEntry ) uncast;
			String             key           = entry.getKey ( );
			Object             default_value = entry.getDefaultValue ( );
			
			if ( !yaml.isSet ( key ) ) {
				if ( default_value instanceof String ) {
					yaml.set ( key , StringEscapeUtils.escapeJava ( ( String ) default_value ) );
				} else {
					yaml.set ( key , default_value );
				}
				
				save++;
			}
		}
		return save;
	}
}
