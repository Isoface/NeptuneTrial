package es.outlook.adriansrj.neptunetrial.configuration.lang;

import es.outlook.adriansrj.neptunetrial.configuration.LanguageEnumConfigurationHandler;
import es.outlook.adriansrj.neptunetrial.enums.EnumFile;
import es.outlook.adriansrj.neptunetrial.enums.EnumLanguage;
import es.outlook.adriansrj.neptunetrial.main.NeptuneTrial;

import java.io.File;

/**
 * @author AdrianSR / 04/09/2021 / 03:29 p. m.
 */
public final class LanguageConfigurationHandler extends LanguageEnumConfigurationHandler < EnumLanguage > {
	
	/**
	 * Constructs the configuration handler.
	 *
	 * @param plugin the battle royale plugin instance.
	 */
	public LanguageConfigurationHandler ( NeptuneTrial plugin ) {
		super ( plugin );
	}
	
	@Override
	public File getFile ( ) {
		return EnumFile.LANGUAGE_CONFIGURATION.getFile ( );
	}
	
	@Override
	public Class < EnumLanguage > getEnumClass ( ) {
		return EnumLanguage.class;
	}
}