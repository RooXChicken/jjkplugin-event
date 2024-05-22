package com.rooxchicken.jjk.CursedTechniques;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.util.Vector;

import com.rooxchicken.jjk.JJKPlugin;
import com.rooxchicken.jjk.Tasks.Blue;
import com.rooxchicken.jjk.Tasks.InfinityBarrier;
import com.rooxchicken.jjk.Tasks.Red;
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
    public void activateBlue(PlayerInteractEvent event)
    {
        if(event.getAction() != Action.LEFT_CLICK_AIR && event.getAction() != Action.LEFT_CLICK_BLOCK)
            return;
        
        Player player = event.getPlayer();
        ItemStack item = event.getItem();

        if(player.isSneaking())
            useBlue(player, item);
        else
        {
            if(item == null || !item.hasItemMeta())
                return;
        
            if(!item.getItemMeta().getDisplayName().equals("§b§l§oLimitless"))
                return;

            Location loc;
            Block b = JJKPlugin.getBlock(player, 50);
            if(b != null)
                loc = b.getLocation();
            else
                {
                    Entity e = JJKPlugin.getTarget(player, 50);
                    if(e != null)
                        loc = e.getLocation();
                    else
                        return;
                }

            if(!JJKPlugin.useCursedEnergy(player, 200))
                return;

            player.getWorld().playSound(player.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 1, 1);

            loc.add(new Vector(0, 1, 0));
            Location start = player.getLocation().clone();
            Location pos = start.clone();
            loc.setDirection(player.getLocation().getDirection());
            player.teleport(loc);
            loc.add(0, 1, 0);

            player.getWorld().playSound(loc, Sound.ENTITY_ENDERMAN_TELEPORT, 1, 1);

            for(int i = 0; i < 50; i++)
            {
                player.getWorld().spawnParticle(Particle.REDSTONE, pos.clone().add(new Vector(0, 1, 0)), 1, 0, 0, 0, new Particle.DustOptions(Color.BLUE, 2f));
                for(Object o : JJKPlugin.getNearbyEntities(pos, 1))
                {
                    if(o instanceof LivingEntity)
                    {
                        ((LivingEntity)o).damage(8);
                    }
                }
                pos.add(pos.getDirection().multiply(start.distance(loc)).multiply(1/50.0));
            }
        }
    }

    @EventHandler
    public void activateBlue(EntityDamageByEntityEvent event)
    {
        if(!(event.getDamager() instanceof Player))
            return;
        
        Player player = (Player)event.getDamager();
        ItemStack item = player.getInventory().getItemInMainHand();

        useBlue(player, item);
    }

    private void useBlue(Player player, ItemStack item)
    {
        if(item == null || !item.hasItemMeta() || !player.isSneaking())
            return;
        
        if(item.getItemMeta().getDisplayName().equals("§b§l§oLimitless"))
        {
            for(Task task : JJKPlugin.tasks)
            {
                if(task instanceof Blue)
                {
                    if(((Blue)task).getPlayer() == player)
                    {
                        ((Blue)task).cancel = true;
                        return;
                    }
                }
            }

            if(!JJKPlugin.useCursedEnergy(player, 300))
                return;

            JJKPlugin.tasks.add(new Blue(plugin, player));
        }
    }

    @EventHandler
    public void useRed(PlayerInteractEvent event)
    {
        if(event.getAction() != Action.RIGHT_CLICK_AIR && event.getAction() != Action.RIGHT_CLICK_BLOCK)
            return;
        
        Player player = event.getPlayer();
        ItemStack item = event.getItem();

        if(item == null || !item.hasItemMeta() || player.isSneaking())
            return;
        
        if(item.getItemMeta().getDisplayName().equals("§b§l§oLimitless"))
        {
            if(!JJKPlugin.useCursedEnergy(player, 300))
                return;

            JJKPlugin.tasks.add(new Red(plugin, player));
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
