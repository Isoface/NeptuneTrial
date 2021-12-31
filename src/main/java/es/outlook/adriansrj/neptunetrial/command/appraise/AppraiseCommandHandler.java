package es.outlook.adriansrj.neptunetrial.command.appraise;

import es.outlook.adriansrj.neptunetrial.command.CommandHandler;
import es.outlook.adriansrj.neptunetrial.gui.AppraiseGUIBuilder;
import es.outlook.adriansrj.neptunetrial.main.NeptuneTrial;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

/**
 * @author AdrianSR / 30/12/2021 / 01:41 p. m.
 */
public final class AppraiseCommandHandler extends CommandHandler {
	
	public AppraiseCommandHandler ( NeptuneTrial plugin ) {
		super ( plugin , "appraise" );
	}
	
	@Override
	public boolean onCommand ( @NotNull CommandSender sender , @NotNull Command command , @NotNull String label ,
			@NotNull String[] args ) {
		if ( sender instanceof Player ) {
			Player player = ( Player ) sender;
			
			player.openInventory ( new AppraiseGUIBuilder ( ).title ( "Appraise" ).build ( player ) );
			
			//			Player    player = ( Player ) sender;
			//			ItemStack item   = player.getInventory ( ).getItemInMainHand ( );
			//
			//			if ( item.getType ( ) != Material.AIR ) {
			//				EnumVanillaFish vanilla_fish = EnumVanillaFish.of ( item.getType ( ) );
			//
			//				if ( vanilla_fish == null ) {
			//					sender.sendMessage ( ChatColor.RED + "You are not holding a fish" );
			//					return true;
			//				}
			//
			//				if ( FishUtil.appraiseFish ( item ) ) {
			//					player.updateInventory ( );
			//
			//					sender.sendMessage (
			//							ChatColor.GREEN + "Fish quality: " + ChatColor.GOLD
			//									+ String.format ( "%.2f" , item.getItemMeta ( ).getPersistentDataContainer ( )
			//									.get ( Constants.QUALITY_NAMESPACE , PersistentDataType.DOUBLE ) ) );
			//				} else {
			//					sender.sendMessage ( ChatColor.RED + "This fish is already appraised!" );
			//				}
			//			} else {
			//				sender.sendMessage ( ChatColor.RED + "You are not holding any item" );
			//			}
		} else {
			sender.sendMessage ( ChatColor.RED + "Must be an online player to execute this command!" );
		}
		
		return true;
	}
}
