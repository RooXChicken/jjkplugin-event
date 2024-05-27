package com.rooxchicken.jjk.CursedTechniques;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Material;
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
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.util.Vector;

import com.rooxchicken.jjk.JJKPlugin;
import com.rooxchicken.jjk.Tasks.Blue;
import com.rooxchicken.jjk.Tasks.HollowPurple;
import com.rooxchicken.jjk.Tasks.InfinityBarrier;
import com.rooxchicken.jjk.Tasks.Red;
import com.rooxchicken.jjk.Tasks.Task;

class ActionState
{
    public boolean rightClick = false;
    public boolean leftClick = false;
}

public class Infinity implements Listener
{
    private Plugin plugin;

    private HashMap<Player, ActionState> players;

    public Infinity(Plugin _plugin)
    {
        plugin = _plugin;
        players = new HashMap<Player, ActionState>();
    }

    public void tick()
    {
        for(Map.Entry<Player,ActionState> entry : players.entrySet())
        {
            Player player = entry.getKey();
            ItemStack item = player.getInventory().getItemInMainHand();

            if(player.isSneaking() && entry.getValue().rightClick && entry.getValue().leftClick)
            {
                if(!JJKPlugin.useCursedEnergy(player, 1800))
                    return;

                JJKPlugin.tasks.add(new HollowPurple(null, player));
                entry.getValue().leftClick = false;
                entry.getValue().rightClick = false;
                return;
            }

            if(entry.getValue().leftClick)
            {
                if(player.isSneaking())
                    useBlue(player, item);
                else
                    teleportBlue(player, item);
            }
            if(entry.getValue().rightClick)
            {
                if(player.isSneaking())
                    activateInfinity(player, item);
                else
                    useRed(player, item);
            }
        }
        players.clear();
    }

    @EventHandler
    private void useItem(PlayerInteractEvent event)
    {
        Player player = event.getPlayer();

        if(!players.containsKey(player))
            players.put(player, new ActionState());

        if(event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK)
            players.get(player).rightClick = true;
        if(event.getAction() == Action.LEFT_CLICK_AIR || event.getAction() == Action.LEFT_CLICK_BLOCK)
            players.get(player).leftClick = true;
    }

    public void activateInfinity(Player player, ItemStack item)
    {
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

    private void teleportBlue(Player player, ItemStack item)
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

    @EventHandler
    public void activateBlue(EntityDamageByEntityEvent event)
    {
        if(!(event.getDamager() instanceof Player))
            return;
    
        Player player = (Player)event.getDamager();
        ItemStack item = player.getInventory().getItemInMainHand();

        if(item == null || !item.hasItemMeta() || !player.isSneaking())
            return;
        
        if(item.getItemMeta().getDisplayName().equals("§b§l§oLimitless"))
        {
            if(!players.containsKey(player))
                players.put(player, new ActionState());
            
            players.get(player).leftClick = true;
        }
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

    public void useRed(Player player, ItemStack item)
    {
        if(item == null || !item.hasItemMeta() || player.isSneaking())
            return;
        
        if(item.getItemMeta().getDisplayName().equals("§b§l§oLimitless"))
        {
            if(player.getCooldown(Material.LIGHT_BLUE_DYE) > 0)
                return;
            if(!JJKPlugin.useCursedEnergy(player, 300))
                return;

            JJKPlugin.tasks.add(new Red(plugin, player));
            player.setCooldown(item.getType(), 40);
        }
    }

    @EventHandler
    public void cancelDamage(EntityDamageEvent event)
    {
        if(event.getCause() == DamageCause.ENTITY_ATTACK || event.getCause() == DamageCause.ENTITY_SWEEP_ATTACK)
            return;
        if(!(event.getEntity() instanceof Player))
            return;

        event.setCancelled(checkCancel((Player)event.getEntity(), event.getFinalDamage()));
    }

    @EventHandler
    public void cancelDamage(EntityDamageByEntityEvent event)
    {
        if(!(event.getDamager() instanceof Player) || !(event.getEntity() instanceof Player))
        {
            if(!(event.getEntity() instanceof Player))
                return;
                
            event.setCancelled(checkCancel((Player)event.getEntity(), event.getFinalDamage()));
            return;
        }
        
        Player damager = (Player)event.getDamager();
        Player player = (Player)event.getEntity();
        ItemStack weapon = damager.getInventory().getItemInMainHand();

        if(weapon == null || !weapon.hasItemMeta())
            return;
        
        boolean hasInfinity = false;

        for(Task t : JJKPlugin.tasks)
            if(t instanceof InfinityBarrier)
                if(((InfinityBarrier)t).player == player)
                {
                    hasInfinity = true;
                }

        if(weapon.getItemMeta().getDisplayName().equals("§f§l§oInverted Spear of Heaven") && hasInfinity)
        {
            player.getWorld().playSound(player.getLocation(), Sound.ENTITY_ELDER_GUARDIAN_CURSE, 0.4f, 1.6f);
            return;
        }

        event.setCancelled(checkCancel((Player)event.getEntity(), event.getFinalDamage()));
    }

    private boolean checkCancel(Player player, double damage)
    {
        for(Task t : JJKPlugin.tasks)
        {
            if(t instanceof InfinityBarrier)
                if(((InfinityBarrier)t).cancelDamage(player, damage))
                {
                    return true;
                }
        }

        return false;
    }

}
