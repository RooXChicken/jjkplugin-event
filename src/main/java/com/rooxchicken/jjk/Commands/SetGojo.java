package com.rooxchicken.jjk.Commands;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import com.rooxchicken.jjk.JJKPlugin;

public class SetGojo implements CommandExecutor
{
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args)
    {
        if(!sender.isOp())
        {
            sender.sendMessage("You need to be OP silly!");
            return false;
        }

        Player player = Bukkit.getServer().getPlayer(args[0]);

        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "give " + player.getName() + " light_blue_dye{display:{Name:'{\"text\":\"Limitless\",\"color\":\"aqua\",\"bold\":true,\"italic\":true}',Lore:['{\"text\":\"Allows for infinity (shift+rc) and red/blue (lc/rc)\",\"color\":\"aqua\",\"bold\":true}']}} 1");
        
        PersistentDataContainer data = player.getPersistentDataContainer();
        data.set(JJKPlugin.cursedEnergyKey, PersistentDataType.INTEGER, 2000);
        data.set(JJKPlugin.maxCursedEnergyKey, PersistentDataType.INTEGER, 2000);

        player.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(40);

        return true;
    }

}
