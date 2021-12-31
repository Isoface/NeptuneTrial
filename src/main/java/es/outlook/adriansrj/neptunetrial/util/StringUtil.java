package es.outlook.adriansrj.neptunetrial.util;

import org.apache.commons.lang.StringUtils;
import org.bukkit.ChatColor;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @author AdrianSR / 15/12/2021 / 08:05 a. m.
 */
public class StringUtil extends StringUtils {
	
	/**
	 * Limits the provided string to have a maximum of characters.
	 * <p>
	 * @param string the string to limit.
	 * @param max_length the maximum length ( the maximum number of characters ).
	 * @return the limited string.
	 */
	public static String limit ( String string , int max_length ) {
		return string.length() > max_length ? ( string.substring ( 0 , max_length ) ) : string;
	}
	
	/**
	 * Replaces from the strings of the provided collection the Bukkit internal
	 * color character {@link ChatColor#COLOR_CHAR} by the provided character.
	 * <p>
	 * @param alt_char   the character replacer.
	 * @param collection the strings collection to replace.
	 * @return a new list containing the processed strings.
	 */
	public static List < String > untranslateAlternateColorCodes ( char alt_char , Collection < String > collection ) {
		final List < String > list = new ArrayList < String > ( );
		for ( String string : collection ) {
			list.add ( untranslateAlternateColorCodes ( alt_char , string ) );
		}
		return list;
	}
	
	/**
	 * Replaces from the strings of the provided list the Bukkit internal color
	 * character {@link ChatColor#COLOR_CHAR} by the well known color character
	 * '{@literal &}'.
	 * <p>
	 * @param collection the strings collection to replace.
	 * @return a new list containing the processed strings.
	 */
	public static List < String > untranslateAlternateColorCodes ( Collection < String > collection ) {
		return untranslateAlternateColorCodes ( '&' , collection );
	}
	
	/**
	 * Translates the strings of the provided collection using an alternate color
	 * code character into a string that uses the internal {@link ChatColor#COLOR_CHAR}
	 * color code character. The alternate color code character will only be
	 * replaced if it is immediately followed by 0-9, A-F, a-f, K-O, k-o, R or r.
	 * <p>
	 * @param alt_char   The alternate color code character to replace. Ex:
	 *                   {@literal &}
	 * @param collection the collection of strings to translate.
	 * @return a new {@link List} of string containing the translated strings.
	 */
	public static List < String > translateAlternateColorCodes ( char alt_char , Collection < String > collection ) {
		final List < String > list = new ArrayList <> ( );
		
		for ( String string : collection ) {
			list.add ( translateAlternateColorCodes ( alt_char , string ) );
		}
		
		return list;
	}
	
	/**
	 * Translates the strings of the provided collection using the alternate color
	 * code character '{@literal &}' into a string that uses the internal
	 * {@link ChatColor#COLOR_CHAR} color code character. The alternate color code character
	 * will only be replaced if it is immediately followed by 0-9, A-F, a-f, K-O,
	 * k-o, R or r.
	 * <p>
	 * @param collection the collection of strings to translate.
	 * @return a new {@link List} of string containing the translated strings.
	 */
	public static List < String > translateAlternateColorCodes ( Collection < String > collection ) {
		return translateAlternateColorCodes ( '&' , collection );
	}
	
	/**
	 * Translates a string using an alternate color code character into a string
	 * that uses the internal {@link ChatColor#COLOR_CHAR} color code character. The
	 * alternate color code character will only be replaced if it is immediately
	 * followed by 0-9, A-F, a-f, K-O, k-o, R or r.
	 * <p>
	 * @param alt_char The alternate color code character to replace. Ex:
	 *                 {@literal &}
	 * @param string   Text containing the alternate color code character.
	 * @return Text containing the ChatColor.COLOR_CODE color code character.
	 * @see ChatColor#translateAlternateColorCodes(char , String)
	 */
	public static String translateAlternateColorCodes ( char alt_char , String string ) {
		return ChatColor.translateAlternateColorCodes ( alt_char , string );
	}
	
	/**
	 * Translates a string using the alternate color code character '{@literal &}'
	 * into a string that uses the internal {@link ChatColor#COLOR_CHAR} color code
	 * character. The alternate color code character will only be replaced if it is
	 * immediately followed by 0-9, A-F, a-f, K-O, k-o, R or r.
	 * <p>
	 * @param string Text containing the alternate color code character.
	 * @return Text containing the ChatColor.COLOR_CODE color code character.
	 * @see #translateAlternateColorCodes(char , String)
	 */
	public static String translateAlternateColorCodes ( String string ) {
		return translateAlternateColorCodes ( '&' , string );
	}
	
	/**
	 * Replaces the Bukkit internal color character {@link ChatColor#COLOR_CHAR} by the
	 * provided character.
	 * <p>
	 * @param alt_char the character replacer.
	 * @param string   the target string.
	 * @return the result.
	 */
	public static String untranslateAlternateColorCodes ( char alt_char , String string ) {
		char[] contents = string.toCharArray ( );
		for ( int i = 0 ; i < contents.length ; i++ ) {
			if ( contents[ i ] == ChatColor.COLOR_CHAR ) {
				contents[ i ] = alt_char;
			}
		}
		return new String ( contents );
	}
	
	/**
	 * Replaces the Bukkit internal color character {@link ChatColor#COLOR_CHAR} by the well
	 * known color character '{@literal &}'.
	 * <p>
	 * @param string the target string.
	 * @return the result.
	 */
	public static String untranslateAlternateColorCodes ( String string ) {
		return untranslateAlternateColorCodes ( '&' , string );
	}
}
