package es.outlook.adriansrj.neptunetrial.storage;

import es.outlook.adriansrj.neptunetrial.enums.EnumMainConfiguration;
import es.outlook.adriansrj.neptunetrial.enums.EnumStat;
import es.outlook.adriansrj.neptunetrial.main.NeptuneTrial;
import es.outlook.adriansrj.neptunetrial.util.StringUtil;
import es.outlook.adriansrj.neptunetrial.util.reflection.EnumReflection;
import org.apache.commons.lang.Validate;
import org.sqlite.jdbc4.JDBC4Connection;

import java.sql.*;
import java.util.*;

/**
 * @author AdrianSR / 31/12/2021 / 06:39 p. m.
 */
public class DataStorageSQLite implements DataStorage {
	
	/**
	 * SQLite's jdbc URL format.
	 */
	protected static final String URL_FORMAT = "jdbc:sqlite:%s";
	
	protected static final String PLAYER_ID_TABLE_NAME = "br_player_id";
	protected static final String STATS_TABLE_NAME     = "br_player_stats";
	protected static final String UID_COLUMN           = "UUID";
	protected static final String NAME_COLUMN          = "NAME";
	protected static final String KEY_COLUMN           = "_KEY";
	protected static final String VALUE_COLUMN         = "VALUE";
	
	protected final String     file_path;
	protected final String     connection_url;
	protected       Connection connection;
	
	public DataStorageSQLite ( NeptuneTrial plugin ) {
		this.file_path      = EnumMainConfiguration.DATABASE_SQLITE_PATH.getAsString ( );
		this.connection_url = String.format ( URL_FORMAT , file_path );
	}
	
	@Override
	public boolean setUp ( ) throws SQLException {
		// connecting
		this.connection = new JDBC4Connection ( connection_url , file_path , new Properties ( ) );
		
		// checking structure of the tables
		tablesStructureCheck ( );
		// creating not existing tables
		tablesCheck ( );
		
		return true;
	}
	
	protected void tablesStructureCheck ( ) throws SQLException {
		// checking structure of the stats table
		tableStructureCheck ( STATS_TABLE_NAME , UID_COLUMN , KEY_COLUMN , VALUE_COLUMN );
	}
	
	protected void tablesCheck ( ) throws SQLException {
		// creating player id table
		try ( PreparedStatement statement = connection.prepareStatement ( String.format (
				"CREATE TABLE IF NOT EXISTS %s (" +
						"%s VARCHAR(36) PRIMARY KEY NOT NULL, " +
						"%s VARCHAR(36) UNIQUE NOT NULL)" ,
				PLAYER_ID_TABLE_NAME , UID_COLUMN , NAME_COLUMN ) ) ) {
			
			statement.executeUpdate ( );
		}
		
		// creating stats table
		try ( PreparedStatement statement = connection.prepareStatement ( String.format (
				"CREATE TABLE IF NOT EXISTS %s (" +
						"%s VARCHAR(36) NOT NULL, " +
						"%s VARCHAR(36) NOT NULL, " +
						"%s INTEGER NOT NULL, " +
						"PRIMARY KEY (%s, %s), " +
						"FOREIGN KEY (%s) REFERENCES %s(%s))" ,
				STATS_TABLE_NAME , UID_COLUMN , KEY_COLUMN , VALUE_COLUMN ,
				UID_COLUMN , KEY_COLUMN ,
				UID_COLUMN , PLAYER_ID_TABLE_NAME , UID_COLUMN ) ) ) {
			
			statement.executeUpdate ( );
		}
	}
	
	@Override
	public Map < UUID, Map < EnumStat, Integer > > getStoredStatValues ( ) throws Exception {
		Map < UUID, Map < EnumStat, Integer > > result = new HashMap <> ( );
		
		for ( UUID uuid : queryIds ( ) ) {
			result.put ( uuid , getStatValues ( uuid ) );
		}
		
		return result;
	}
	
	@Override
	public Map < EnumStat, Integer > getStatValues ( UUID uuid ) throws Exception {
		Validate.notNull ( uuid , "uuid cannot be null" );
		
		Map < EnumStat, Integer > result = new EnumMap <> ( EnumStat.class );
		
		try ( PreparedStatement statement = connection.prepareStatement ( String.format (
				"SELECT %s, %s FROM %s WHERE %s = ?" ,
				KEY_COLUMN , VALUE_COLUMN , STATS_TABLE_NAME , UID_COLUMN ) ) ) {
			
			statement.setString ( 1 , uuid.toString ( ) );
			
			try ( ResultSet result_set = statement.executeQuery ( ) ) {
				while ( result_set.next ( ) ) {
					EnumStat stat_type = EnumReflection.getEnumConstant ( EnumStat.class , StringUtil.defaultIfBlank (
							result_set.getString ( KEY_COLUMN ) , StringUtil.EMPTY ) );
					
					if ( stat_type != null ) {
						result.put ( stat_type , Math.max ( result_set.getInt ( VALUE_COLUMN ) , 0 ) );
					}
				}
			}
		}
		
		return result;
	}
	
	@Override
	public int getStatValue ( UUID uuid , EnumStat stat_type ) throws Exception {
		Validate.notNull ( uuid , "uuid cannot be null" );
		Validate.notNull ( stat_type , "stat type cannot be null" );
		
		return ( int ) query0 ( STATS_TABLE_NAME , uuid , stat_type.name ( ) , 0 );
	}
	
	@Override
	public void setStatValue ( UUID uuid , String name , EnumStat stat_type , int value ) throws Exception {
		Validate.notNull ( uuid , "uuid cannot be null" );
		
		set0 ( STATS_TABLE_NAME , uuid , name , stat_type.name ( ) , value );
	}
	
	@Override
	public void dispose ( ) {
		if ( connection != null ) {
			try {
				connection.close ( );
			} catch ( SQLException e ) {
				e.printStackTrace ( );
			}
		}
	}
	
	// --------- utils
	
	protected void tableStructureCheck ( String name , String... structure ) throws SQLException {
		if ( tableExists ( name ) ) {
			int     index   = 0;
			boolean invalid = false;
			
			try ( PreparedStatement statement = connection.prepareStatement ( "PRAGMA table_info([" + name + "])" ) ;
					ResultSet result = statement.executeQuery ( ) ) {
				while ( result.next ( ) && !invalid ) {
					String column_name = result.getString ( 2 );
					
					if ( index < structure.length ) {
						if ( !Objects.equals ( column_name , structure[ index ] ) ) {
							invalid = true;
						}
					} else {
						invalid = true;
					}
					
					index++;
				}
			}
			
			// the table will be dropped in case it is
			// not the right structure.
			if ( invalid ) {
				try ( PreparedStatement drop_statement = this.connection.prepareStatement (
						"DROP TABLE " + name ) ) {
					
					drop_statement.executeUpdate ( );
				}
			}
		}
	}
	
	protected boolean tableExists ( String table ) throws SQLException {
		DatabaseMetaData meta   = connection.getMetaData ( );
		boolean          exists = false;
		
		// unlike MySQL, we have to make sure that the result set
		// will be closed, otherwise, the table will get locked.
		try ( ResultSet result = meta.getTables ( null , null , table , null ) ) {
			exists = result.next ( );
		}
		
		return exists;
	}
	
	// set for tables with structure: [ UID_COLUMN | KEY_COLUMN | VALUE_COLUMN ]
	protected void set0 ( String table_name , UUID uuid ,
			String name , String key , Object value ) throws SQLException {
		Validate.notNull ( value , "value cannot be null" );
		
		// registering player id
		idCheck ( uuid , name );
		
		// then inserting
		try ( PreparedStatement statement = connection.prepareStatement ( String.format (
				"INSERT INTO %s (%s, %s, %s) VALUES (?, ?, ?) ON CONFLICT (%s, %s) DO UPDATE SET %s = ?" ,
				table_name , UID_COLUMN , KEY_COLUMN , VALUE_COLUMN , UID_COLUMN , KEY_COLUMN , VALUE_COLUMN ) ) ) {
			
			statement.setString ( 1 , uuid.toString ( ) );
			statement.setString ( 2 , key );
			statement.setObject ( 3 , value );
			statement.setObject ( 4 , value );
			
			statement.executeUpdate ( );
		}
	}
	
	// query for tables with structure: [ UID_COLUMN | KEY_COLUMN | VALUE_COLUMN ]
	protected Object query0 ( String table_name ,
			UUID uuid , String key , Object default_value ) throws Exception {
		Validate.notNull ( uuid , "uuid cannot be null" );
		Validate.isTrue ( StringUtil.isNotBlank ( table_name ) , "table_name cannot be null/blank" );
		
		List < Object > result = querySeveral0 ( table_name , uuid , key );
		
		return result.isEmpty ( ) ? default_value : result.get ( 0 );
	}
	
	// query for tables with structure: [ UID_COLUMN | KEY_COLUMN | VALUE_COLUMN ]
	protected List < Object > querySeveral0 ( String table_name , UUID uuid , String key ) throws Exception {
		Validate.notNull ( uuid , "uuid cannot be null" );
		Validate.isTrue ( StringUtil.isNotBlank ( table_name ) , "table_name cannot be null/blank" );
		
		List < Object > result = new ArrayList <> ( );
		
		try ( PreparedStatement statement = connection.prepareStatement ( String.format (
				"SELECT %s FROM %s WHERE %s = ? AND %s = ?" ,
				VALUE_COLUMN , table_name , UID_COLUMN , KEY_COLUMN ) ) ) {
			
			statement.setString ( 1 , uuid.toString ( ) );
			statement.setString ( 2 , key );
			
			try ( ResultSet result_set = statement.executeQuery ( ) ) {
				if ( result_set.next ( ) ) {
					Object next = result_set.getObject ( VALUE_COLUMN );
					
					if ( next != null ) {
						result.add ( next );
					}
				}
			}
		}
		
		return result;
	}
	
	protected void idCheck ( UUID uuid , String name ) throws SQLException {
		try ( PreparedStatement statement = connection.prepareStatement ( String.format (
				"INSERT OR IGNORE INTO %s (%s, %s) VALUES (?, ?)" ,
				PLAYER_ID_TABLE_NAME , UID_COLUMN , NAME_COLUMN ) ) ) {
			
			statement.setString ( 1 , uuid.toString ( ) );
			statement.setString ( 2 , name );
			
			statement.executeUpdate ( );
		}
	}
	
	// query for stored player unique ids (UUID)
	protected Set < UUID > queryIds ( ) throws Exception {
		Set < UUID > ids = new HashSet <> ( );
		
		try ( PreparedStatement statement = connection.prepareStatement ( String.format (
				"SELECT %s FROM %s" , UID_COLUMN , PLAYER_ID_TABLE_NAME ) ) ) {
			try ( ResultSet result_set = statement.executeQuery ( ) ) {
				while ( result_set.next ( ) ) {
					try {
						ids.add ( UUID.fromString ( result_set.getString ( UID_COLUMN ) ) );
					} catch ( IllegalArgumentException ex ) {
						// ignore invalid uuid.
					}
				}
			}
		}
		
		return ids;
	}
}
