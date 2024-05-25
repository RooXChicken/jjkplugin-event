package com.rooxchicken.jjk.Commands;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;

public class GiveItems implements CommandExecutor
{
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args)
    {
        if(!sender.isOp())
        {
            sender.sendMessage("You need to be OP silly!");
            return false;
        }

        Bukkit.dispatchCommand(sender, "give @s stick{display:{Name:'{\"text\":\"Shrine\",\"color\":\"gray\",\"bold\":true,\"italic\":true}',Lore:['{\"text\":\"Fires cleave (rc) and dismantle (atk)\",\"color\":\"gray\",\"bold\":true}']}} 1");
        Bukkit.dispatchCommand(sender, "give @s stick{display:{Name:'{\"text\":\"Limitless\",\"color\":\"aqua\",\"bold\":true,\"italic\":true}',Lore:['{\"text\":\"Allows for infinity (shift+rc) and red/blue (lc/rc)\",\"color\":\"aqua\",\"bold\":true}']}} 1");
        Bukkit.dispatchCommand(sender, "give @s stick{display:{Name:'{\"text\":\"Boogie Woogie\",\"color\":\"dark_blue\",\"bold\":true,\"italic\":true}',Lore:['{\"text\":\"Swap with the entity you are facing (rc), select entities and swap them (lc)\",\"color\":\"dark_blue\",\"bold\":true,\"italic\":true}']}} 1");
        Bukkit.dispatchCommand(sender, "give @s stick{display:{Name:'{\"text\":\"Projection Sorcery\",\"color\":\"dark_gray\",\"bold\":true,\"italic\":true}',Lore:['{\"text\":\"Move quickly for 1 second (rc) and freeze enemies for 1 second (lc)\",\"color\":\"dark_gray\",\"bold\":true,\"italic\":true}']}} 1");
        Bukkit.dispatchCommand(sender, "give @s stick{display:{Name:'{\"text\":\"Inverse\",\"color\":\"red\",\"bold\":true,\"italic\":true}',Lore:['{\"text\":\"Inverts damage (strong is weak, weak is powerful)\",\"color\":\"red\",\"bold\":true,\"italic\":true}']}} 1");
        Bukkit.dispatchCommand(sender, "give @s iron_sword{display:{Name:'{\"text\":\"Inverted Spear of Heaven\",\"color\":\"white\",\"bold\":true,\"italic\":true}',Lore:['{\"text\":\"Bypasses infinity and allows the user to block slashes with a shield\",\"color\":\"white\",\"bold\":true,\"italic\":true}']}} 1");
        Bukkit.dispatchCommand(sender, "give @s iron_sword{display:{Name:'{\"text\":\"Soul Split Katana\",\"color\":\"black\",\"bold\":true,\"italic\":true}',Lore:['{\"text\":\"Ignores most durability\",\"color\":\"black\",\"bold\":true,\"italic\":true}']}} 1");

        return true;
    }

}
