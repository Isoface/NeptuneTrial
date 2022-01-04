package es.outlook.adriansrj.neptunetrial.enums;

import es.outlook.adriansrj.neptunetrial.command.appraise.AppraiseCommandHandler;
import es.outlook.adriansrj.neptunetrial.configuration.MainConfigurationHandler;
import es.outlook.adriansrj.neptunetrial.configuration.lang.LanguageConfigurationHandler;
import es.outlook.adriansrj.neptunetrial.fish.NeptuneFishCookingHandler;
import es.outlook.adriansrj.neptunetrial.fish.NeptuneFishEatingHandler;
import es.outlook.adriansrj.neptunetrial.fish.NeptuneFishFishingHandler;
import es.outlook.adriansrj.neptunetrial.fish.NeptuneFishHandler;
import es.outlook.adriansrj.neptunetrial.gui.AppriseGUIHandler;
import es.outlook.adriansrj.neptunetrial.handler.PluginHandler;
import es.outlook.adriansrj.neptunetrial.storage.DataStorageHandler;

import java.util.concurrent.Callable;

/**
 * @author AdrianSR / 29/12/2021 / 03:23 p. m.
 */
public enum EnumPluginHandler {
	
	// order is vital
	
	LANGUAGE_CONFIGURATION_HANDLER ( LanguageConfigurationHandler.class ),
	MAIN_CONFIGURATION_HANDLER ( MainConfigurationHandler.class ),
	DATA_STORAGE_HANDLER ( DataStorageHandler.class ),
	FISH_HANDLER ( NeptuneFishHandler.class ),
	FISHING_HANDLER ( NeptuneFishFishingHandler.class ),
	EATING_HANDLER ( NeptuneFishEatingHandler.class ),
	COOKING_HANDLER ( NeptuneFishCookingHandler.class ) ,
	APPRAISE_COMMAND_HANDLER ( AppraiseCommandHandler.class ),
	APPRAISE_GUI_HANDLER ( AppriseGUIHandler.class ),
	
	;
	
	private final Class < ? extends PluginHandler > clazz;
	private final Callable < Boolean >              init_flag;
	
	EnumPluginHandler ( Class < ? extends PluginHandler > clazz , Callable < Boolean > init_flag ) {
		this.clazz     = clazz;
		this.init_flag = init_flag;
	}
	
	EnumPluginHandler ( Class < ? extends PluginHandler > clazz ) {
		this ( clazz , ( ) -> Boolean.TRUE /* no special checks are required */ );
	}
	
	public Class < ? extends PluginHandler > getHandlerClass ( ) {
		return clazz;
	}
	
	public boolean canInitialize ( ) throws Exception {
		return init_flag.call ( );
	}
}
