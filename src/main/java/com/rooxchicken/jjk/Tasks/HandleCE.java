package com.rooxchicken.jjk.Tasks;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.Plugin;

import com.rooxchicken.jjk.JJKPlugin;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;

public class HandleCE extends Task
{
    public HandleCE(Plugin _plugin)
    {
        super(_plugin);
        tickThreshold = 4;
    }

    @Override
    public void run()
    {
        for(Player player : Bukkit.getServer().getOnlinePlayers())
        {
            String message = "";
            PersistentDataContainer data = player.getPersistentDataContainer();
            if(data.has(JJKPlugin.cursedEnergyKey, PersistentDataType.INTEGER))
            {
                int ce = data.get(JJKPlugin.cursedEnergyKey, PersistentDataType.INTEGER);
                int maxCE = data.get(JJKPlugin.maxCursedEnergyKey, PersistentDataType.INTEGER);
                ce += 2;
                if(ce > maxCE)
                    ce = maxCE;
                data.set(JJKPlugin.cursedEnergyKey, PersistentDataType.INTEGER, ce);

                message += ChatColor.DARK_BLUE + "" + ce + "/" + maxCE + " [";
                int filled = (int)JJKPlugin.ClampD(ce/(maxCE/10), 1, 10);

                for(int i = 0; i < filled; i++)
                    message += "#";
                for(int i = 0; i < 10-filled; i++)
                    message += "_";
                player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(message + "]"));
            }
        }
    }
}
