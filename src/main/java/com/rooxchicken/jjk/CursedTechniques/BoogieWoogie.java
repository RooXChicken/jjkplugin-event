package com.rooxchicken.jjk.CursedTechniques;

import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Snowball;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.util.Vector;

import com.rooxchicken.jjk.JJKPlugin;
import com.rooxchicken.jjk.Data.BoogieWoogieTargets;
import com.rooxchicken.jjk.Tasks.InfinityBarrier;
import com.rooxchicken.jjk.Tasks.Task;

public class BoogieWoogie implements Listener
{
    private Plugin plugin;
    private HashMap<Player, BoogieWoogieTargets> targets;

    public Scoreboard scoreboard;

    public BoogieWoogie(Plugin _plugin)
    {
        plugin = _plugin;
        targets = new HashMap<Player, BoogieWoogieTargets>();

        scoreboard = Bukkit.getServer().getScoreboardManager().getMainScoreboard();
        scoreboard.registerNewTeam("BoogieWoogieSelect");
        scoreboard.getTeam("BoogieWoogieSelect").setColor(ChatColor.DARK_BLUE);
        scoreboard.getTeam("BoogieWoogieSelect").setDisplayName(ChatColor.DARK_BLUE + "boogie");
    }

    public void boogie(Player player, Entity p, Entity t)
    {
        if(p == null || t == null)
            return;
        
        Location first = p.getLocation().clone();
        Location second = t.getLocation().clone();
        // Vector vFirst = p.getVelocity();
        // Vector vSecond = t.getVelocity();

        p.teleport(second);
        t.teleport(first);

        // p.setVelocity(vSecond);
        // t.setVelocity(vFirst);

        double offset = 0.2;
        player.getWorld().spawnParticle(Particle.REDSTONE, p.getLocation(), 50, offset, 0.5, offset, new Particle.DustOptions(Color.BLUE, 1));
        player.getWorld().spawnParticle(Particle.REDSTONE, p.getLocation().add(0, 0.75, 0), 30, offset/2, 0.75, offset/2, new Particle.DustOptions(Color.WHITE, 0.75f));
        player.getWorld().spawnParticle(Particle.REDSTONE, t.getLocation(), 50, offset, 0.5, offset, new Particle.DustOptions(Color.BLUE, 1));
        player.getWorld().spawnParticle(Particle.REDSTONE, t.getLocation().add(0, 0.75, 0), 30, offset/2, 0.75, offset/2, new Particle.DustOptions(Color.WHITE, 0.75f));

        player.getWorld().playSound(player.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 1, 1);
        player.getWorld().playSound(player.getLocation(), Sound.BLOCK_BASALT_STEP, 1, 1);
    }

    @EventHandler
    public void activateBlue(EntityDamageByEntityEvent event)
    {
        if(!(event.getDamager() instanceof Player))
            return;
    
        Player player = (Player)event.getDamager();
        ItemStack item = player.getInventory().getItemInMainHand();

        if(item == null || !item.hasItemMeta())
            return;
        
        if(item.getItemMeta().getDisplayName().equals("§1§l§oBoogie Woogie"))
        {
            selectBoogie(player, item);
        }
    }

    @EventHandler
    public void activateBoogieWoogie(PlayerInteractEvent event)
    {
        if(event.getAction() != Action.RIGHT_CLICK_AIR && event.getAction() != Action.RIGHT_CLICK_BLOCK)
            return;
        
        Player player = event.getPlayer();
        ItemStack item = event.getItem();
        
        if(item == null || !item.hasItemMeta())
            return;
        
        if(item.getItemMeta().getDisplayName().equals("§1§l§oBoogie Woogie"))
        {
            Entity target = JJKPlugin.getTarget(player, 80);
            if(target == null)
            {
                if(targets.get(player).p == null || (targets.get(player).p != null && targets.get(player).t != null))
                    return;
                if(!JJKPlugin.useCursedEnergy(player, 60))
                    return;

                boogie(player, targets.get(player).p, player);
                targets.get(player).cancel();
                return;
            }
            
            if(!JJKPlugin.useCursedEnergy(player, 60))
                return;
            
            boogie(player, player, target);
        }
    }

    private void selectBoogie(Player player, ItemStack item)
    {
        if(!targets.containsKey(player))
        targets.put(player, new BoogieWoogieTargets(player, this));

        if(player.isSneaking())
        {
            targets.get(player).cancel();
            targets.remove(player);

            player.playSound(player.getLocation(), Sound.BLOCK_STONE_BREAK, 1, 1);
            
            return;
        }
        
        if(item == null || !item.hasItemMeta())
            return;
        
        if(item.getItemMeta().getDisplayName().equals("§1§l§oBoogie Woogie"))
        {
            Entity target = JJKPlugin.getTarget(player, 80);

            if(targets.get(player).setOrBoogie(target, this))
                targets.remove(player);
        }
    }

    @EventHandler
    public void selectBoogieWoogie(PlayerInteractEvent event)
    {
        if(event.getAction() != Action.LEFT_CLICK_AIR)
            return;
        
        Player player = event.getPlayer();
        ItemStack item = event.getItem();

        if(item == null || !item.hasItemMeta())
            return;
        
        if(!item.getItemMeta().getDisplayName().equals("§1§l§oBoogie Woogie"))
            return;

        selectBoogie(player, item);
    }
}
