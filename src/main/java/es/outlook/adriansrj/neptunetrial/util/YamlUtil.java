package es.outlook.adriansrj.neptunetrial.util;

import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.ConfigurationSection;

import java.util.Objects;

/**
 * @author AdrianSR / 14/12/2021 / 09:07 p. m.
 */
public class YamlUtil {
	
	/**
	 * Sets the specified path to the given value if and only if it is not already set.
	 * <p>
	 * If value is null, the entry will be removed. Any existing entry will be replaced, regardless of what the new
	 * value is.
	 * <p>
	 * Some implementations may have limitations on what you may store. See their individual javadocs for details. No
	 * implementations should allow you to store {@link Configuration}s or {@link ConfigurationSection}s, please use
	 * {@link ConfigurationSection#createSection(String)} for that.
	 * <p>
	 *
	 * @param section the configuration section to set.
	 * @param path    the path of the object to set.
	 * @param value   new value to set the path to.
	 */
	public static boolean setNotSet ( ConfigurationSection section , String path , Object value ) {
		if ( section.isSet ( path ) ) {
			return false;
		}
		
		section.set ( path , value );
		return true;
	}
	
	/**
	 * Sets the specified path to the given value if and only if it the value at the provided path is not the same as
	 * {@code value}.
	 * <p>
	 * If value is null, the entry will be removed. Any existing entry will be replaced, regardless of what the new
	 * value is.
	 * <p>
	 * Some implementations may have limitations on what you may store. See their individual javadocs for details. No
	 * implementations should allow you to store {@link Configuration}s or {@link ConfigurationSection}s, please use
	 * {@link ConfigurationSection#createSection(String)} for that.
	 * <p>
	 *
	 * @param section the configuration section to set.
	 * @param path    the path of the object to set.
	 * @param value   new value to set the path to.
	 */
	public static boolean setNotEqual ( ConfigurationSection section , String path , Object value ) {
		if ( setNotSet ( section , path , value ) ) {
			return true;
		} else if ( Objects.equals ( value , section.get ( path ) ) ) {
			return false;
		}
		
		section.set ( path , value );
		return true;
	}
	
	/**
	 * Creates an empty {@link ConfigurationSection} within the specified {@link ConfigurationSection} if and only if
	 * there is no another section with the same name as the provided.
	 * <p>
	 *
	 * @param section the section at which the new section will be created.
	 * @param name    the name for the section.
	 *
	 * @return the newly created section, or the already existing section.
	 */
	public static ConfigurationSection createNotExisting ( ConfigurationSection section , String name ) {
		return section.isConfigurationSection ( name ) ? section.getConfigurationSection ( name )
				: section.createSection ( name );
	}
}
