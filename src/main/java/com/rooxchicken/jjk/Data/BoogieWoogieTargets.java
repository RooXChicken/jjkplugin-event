package com.rooxchicken.jjk.Data;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scoreboard.Scoreboard;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLib;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.reflect.StructureModifier;
import com.comphenix.protocol.wrappers.WrappedDataValue;
import com.comphenix.protocol.wrappers.WrappedDataWatcher;
import com.comphenix.protocol.wrappers.WrappedDataWatcher.Registry;
import com.comphenix.protocol.wrappers.WrappedDataWatcher.Serializer;
import com.comphenix.protocol.wrappers.WrappedDataWatcher.WrappedDataWatcherObject;
import com.comphenix.protocol.wrappers.WrappedWatchableObject;
import com.google.common.collect.Lists;
import com.rooxchicken.jjk.JJKPlugin;
import com.rooxchicken.jjk.CursedTechniques.BoogieWoogie;

public class BoogieWoogieTargets
{
    public Player player;
    private BoogieWoogie boogieWoogie;

    public Entity p = null;
    public Entity t = null;

    public BoogieWoogieTargets(Player _player, BoogieWoogie _boogieWoogie)
    {
        player = _player;
        boogieWoogie = _boogieWoogie;
    }

    public boolean setOrBoogie(Entity e, BoogieWoogie boogie)
    {
        if(p != null && p.isDead())
            p = null;
        if(t != null && t.isDead())
            t = null;
        
        
        if(p == null && e != null)
        {
            p = e;
            player.getWorld().playSound(player.getLocation(), Sound.ENTITY_ARROW_HIT_PLAYER, 1, 1);
            addGlow(player, p);
        }
        else if(t == null && e != null)
        {
            t = e;

            if(p.equals(t))
            {
                cancel();
                return false;
            }
            player.getWorld().playSound(player.getLocation(), Sound.ENTITY_ARROW_HIT_PLAYER, 1, 1);
            addGlow(player, t);
        }
        else if(p != null && t != null)
        {
            if(!JJKPlugin.useCursedEnergy(player, 60))
                return false;
                
            boogie.boogie(player, p, t);
            
            removeGlow(player, p);
            removeGlow(player, t);

            return true;
        }

        return false;
    }

    public void cancel()
    {
        player.getWorld().playSound(player.getLocation(), Sound.BLOCK_STONE_BREAK, 1, 1);
        if(p != null)
            removeGlow(player, p);
        if(t != null)
            removeGlow(player, t);
        p = null;
        t = null;
    }

    public void addGlow(Player player, Entity entity)
    {
        if(entity instanceof Player)
            boogieWoogie.scoreboard.getTeam("BoogieWoogieSelect").addEntry(((Player)entity).getDisplayName());
        else
            boogieWoogie.scoreboard.getTeam("BoogieWoogieSelect").addEntry(entity.getUniqueId().toString());

        PacketContainer packet = ProtocolLibrary.getProtocolManager().createPacket(PacketType.Play.Server.ENTITY_METADATA);
        packet.getIntegers().write(0, entity.getEntityId()); //Set packet's entity id

        List<WrappedDataValue> wrappedDataValueList = new ArrayList<>();
        byte data = 0x0;
        if(entity.getFireTicks() > 0)
            data += 0x01;
        if(entity instanceof Player && ((Player)entity).isSneaking())
            data += 0x02;
        if(entity instanceof Player && ((Player)entity).isSprinting())
            data += 0x08;
        if(entity instanceof Player && ((Player)entity).isSwimming())
            data += 0x10;
        if(entity instanceof Player && ((Player)entity).isInvisible())
            data += 0x20;
        if(entity instanceof Player && ((Player)entity).isFlying())
            data += 0x80;

        data += 0x40;
        wrappedDataValueList.add(new WrappedDataValue(0, Registry.get(Byte.class), data));

        packet.getDataValueCollectionModifier().write(0, wrappedDataValueList);
        
        ProtocolLibrary.getProtocolManager().sendServerPacket(player, packet);
    }

    public void removeGlow(Player player, Entity entity)
    {
        if(entity instanceof Player)
            boogieWoogie.scoreboard.getTeam("BoogieWoogieSelect").removeEntry(((Player)entity).getDisplayName());
        else
            boogieWoogie.scoreboard.getTeam("BoogieWoogieSelect").removeEntry(entity.getUniqueId().toString());
        PacketContainer packet = ProtocolLibrary.getProtocolManager().createPacket(PacketType.Play.Server.ENTITY_METADATA);
        packet.getIntegers().write(0, entity.getEntityId()); //Set packet's entity id

        List<WrappedDataValue> wrappedDataValueList = new ArrayList<>();
        byte data = 0x0;
        if(entity.getFireTicks() > 0)
            data += 0x01;
        if(entity instanceof Player && ((Player)entity).isSneaking())
            data += 0x02;
        if(entity instanceof Player && ((Player)entity).isSprinting())
            data += 0x08;
        if(entity instanceof Player && ((Player)entity).isSwimming())
            data += 0x10;
        if(entity instanceof Player && ((Player)entity).isInvisible())
            data += 0x20;
        if(entity instanceof Player && ((Player)entity).isFlying())
            data += 0x80;

        wrappedDataValueList.add(new WrappedDataValue(0, Registry.get(Byte.class), data));

        packet.getDataValueCollectionModifier().write(0, wrappedDataValueList);
        
        ProtocolLibrary.getProtocolManager().sendServerPacket(player, packet);
    }
}
