package es.outlook.adriansrj.neptunetrial.util;

import org.bukkit.configuration.ConfigurationSection;

/**
 * @author AdrianSR / 29/12/2021 / 02:58 p. m.
 */
public interface Configurable < T > {
	
	int save ( ConfigurationSection section );
	
	T load ( ConfigurationSection section );
	
	boolean isValid ( );
}
