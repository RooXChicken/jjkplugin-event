package com.rooxchicken.jjk.Commands;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;

import com.rooxchicken.jjk.JJKPlugin;

public class ResetCE implements CommandExecutor
{
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args)
    {
        if(!sender.isOp())
        {
            sender.sendMessage("You need to be OP silly!");
            return false;
        }

        Bukkit.getServer().getPlayer(sender.getName()).getPersistentDataContainer().set(JJKPlugin.cursedEnergyKey, PersistentDataType.INTEGER, 2000);

        return true;
    }

}
