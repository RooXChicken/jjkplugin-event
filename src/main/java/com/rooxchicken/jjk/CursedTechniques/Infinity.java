package com.rooxchicken.jjk.CursedTechniques;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

import com.rooxchicken.jjk.JJKPlugin;
import com.rooxchicken.jjk.Tasks.InfinityBarrier;
import com.rooxchicken.jjk.Tasks.Task;

public class Infinity implements Listener
{
    private Plugin plugin;

    public Infinity(Plugin _plugin)
    {
        plugin = _plugin;
    }


    @EventHandler
    public void activateInfinity(PlayerInteractEvent event)
    {
        if(event.getAction() != Action.RIGHT_CLICK_AIR && event.getAction() != Action.RIGHT_CLICK_BLOCK)
            return;
        
        Player player = event.getPlayer();
        ItemStack item = event.getItem();

        if(item == null || !item.hasItemMeta() || !player.isSneaking())
            return;
        
        if(item.getItemMeta().getDisplayName().equals("§b§l§oLimitless"))
        {
            for(Task t : JJKPlugin.tasks)
            {
                if(t instanceof InfinityBarrier)
                    if(((InfinityBarrier)t).checkDisabled(player))
                        return;
            }


            if(!JJKPlugin.useCursedEnergy(player, 50))
                return;

            JJKPlugin.tasks.add(new InfinityBarrier(plugin, player));
        }
    }

    @EventHandler
    public void cancelDamage(EntityDamageEvent event)
    {
        if(!(event.getEntity() instanceof Player))
            return;

        for(Task t : JJKPlugin.tasks)
        {
            if(t instanceof InfinityBarrier)
                if(((InfinityBarrier)t).cancelDamage((Player)event.getEntity(), event.getDamage()))
                {
                    event.setCancelled(true);
                    return;
                }
        }
    }

}
