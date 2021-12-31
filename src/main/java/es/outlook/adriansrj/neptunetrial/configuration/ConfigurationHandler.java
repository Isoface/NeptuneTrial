package es.outlook.adriansrj.neptunetrial.configuration;

import es.outlook.adriansrj.neptunetrial.handler.PluginHandler;
import es.outlook.adriansrj.neptunetrial.main.NeptuneTrial;

/**
 * @author AdrianSR / 22/08/2021 / Time: 09:05 p. m.
 */
public abstract class ConfigurationHandler extends PluginHandler {
	
	/**
	 * Constructs the configuration handler.
	 *
	 * @param plugin the battle royale plugin instance.
	 */
	protected ConfigurationHandler ( NeptuneTrial plugin ) {
		super ( plugin );
	}
	
	/**
	 * This method is called just after this configuration handler is constructed. Should handle all the initial logic,
	 * such as, saving the default configuration.
	 */
	public abstract void initialize ( );
	
	/**
	 * Loads the configuration from the corresponding configuration file/files this handler handles.
	 */
	public abstract void loadConfiguration ( );
	
	/**
	 * Saves the configuration in the corresponding configuration file/files this handler handles.
	 */
	public abstract void save ( );
}