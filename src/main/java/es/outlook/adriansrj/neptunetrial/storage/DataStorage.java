package es.outlook.adriansrj.neptunetrial.storage;

import es.outlook.adriansrj.neptunetrial.enums.EnumStat;
import org.bukkit.entity.Player;

import java.util.Map;
import java.util.UUID;

/**
 * @author AdrianSR / 16/12/2021 / 06:21 p. m.
 */
public interface DataStorage {
	
	boolean setUp ( ) throws Exception;
	
	// stats
	Map < UUID, Map < EnumStat, Integer > > getStoredStatValues ( ) throws Exception;
	
	Map < EnumStat, Integer > getStatValues ( UUID uuid ) throws Exception;
	
	int getStatValue ( UUID uuid , EnumStat stat_type ) throws Exception;
	
	void setStatValue ( UUID uuid , String name , EnumStat stat_type , int value ) throws Exception;
	
	default void setStatValue ( Player player , EnumStat stat_type , int value ) throws Exception {
		setStatValue ( player.getUniqueId ( ) , player.getName ( ) , stat_type , value );
	}
	
	default Map < EnumStat, Integer > getStatValues ( Player player ) throws Exception {
		return getStatValues ( player.getUniqueId ( ) );
	}
	
	default int getStatValue ( Player player , EnumStat stat_type ) throws Exception {
		return getStatValue ( player.getUniqueId ( ) , stat_type );
	}
	
	void dispose ( );
}