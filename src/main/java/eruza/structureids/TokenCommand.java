package eruza.structureids;

import java.util.List;

import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ChatComponentText;

public class TokenCommand extends CommandBase {

	@Override
	public String getCommandName() {
		return "givetoken";
	}

	@Override
	public String getCommandUsage(ICommandSender p_71518_1_) {
		return "/givetoken <player> <token name>";
	}

	@Override
	public int getRequiredPermissionLevel()
	{
		return 2;
	}

	@Override
	public boolean isUsernameIndex(String[] args, int index)
	{
		return index == 0;
	}

	@Override
	public List<?> addTabCompletionOptions(ICommandSender par1ICommandSender, String[] par2ArrayOfStr)
	{
		return par2ArrayOfStr.length == 1 ? getListOfStringsMatchingLastWord(par2ArrayOfStr, this.getPlayers()) : null;
	}

	protected String[] getPlayers()
	{
		return MinecraftServer.getServer().getAllUsernames();
	}

	@Override
	public void processCommand(ICommandSender sender, String[] args) {
		String toName = args[0];
		EntityPlayerMP toPlayer = getPlayer(sender, args[0]);

		StringBuffer buf = new StringBuffer();
		for (int i = 1; i < args.length; i++) buf.append( args[i] + " " );
		String tokenName = buf.toString().trim().replace(" Token", "");
		if(tokenName == null) {
			sender.addChatMessage(new ChatComponentText("You need to provide the name of the token!"));
			return;
		}

		ItemStack token = StructureIds.getKeyedToken(tokenName);
		if(!toPlayer.inventory.addItemStackToInventory(token)) {
			sender.addChatMessage(new ChatComponentText("Giving token failed, is " + toName + "'s inventory full?"));
			toPlayer.addChatMessage(new ChatComponentText("Giving token failed, is your inventory full?"));
		}
	}
}
