package com.rooxchicken.jjk.Tasks;

import java.util.List;
import java.util.Collection;

import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.Plugin;
import org.bukkit.util.Vector;

import com.rooxchicken.jjk.JJKPlugin;

import net.minecraft.server.network.TextFilter.e;
import net.minecraft.sounds.SoundEffect;
import net.minecraft.world.level.block.SoundEffectType;

public class HollowPurple extends Task
{
    private Player player;
    private Location start;

    private Location blueLoc;
    private Location redLoc;
    private Location purpleLoc;
    
    private int t = 0;
    private int animTick = 0;
    private double distance = 2.5;

    private double size = 1;
    private int count = 25;

    private double rad = 0;
    private double xOffset = 0;
    private double zOffset = 0;

    private double[] cacheX;
    private double[] cacheZ;
    
    private Color[] blueColors;
    private Color[] redColors;
    private Color[] purpleColors;

    public HollowPurple(Plugin _plugin, Player _player)
    {
        super(_plugin);
        player = _player;
        start = player.getEyeLocation().clone().add(0, 1, 0);

        rad = Math.toRadians(start.getYaw());
        xOffset = Math.sin(rad);
        zOffset = Math.cos(rad);

        blueLoc = start.clone().add(2*xOffset, 0, -2*zOffset);
        redLoc = blueLoc.clone();
        purpleLoc = blueLoc.clone();

        size = 1;
        count = 25;

        //double offset = start.getYaw() + 90.0;
        // if(offset > 360)
        //     offset -= 360;
        // if(offset < 0)
        //     offset += 360;
        //rad = Math.toRadians(offset);
        // xOffset = Math.cos(rad)*distance;
        // zOffset = Math.sin(rad)*distance;

        // blueLoc.add(xOffset, 0, zOffset);
        // redLoc.add(-xOffset, 0, -zOffset);

        // blueLoc.add(xOffset, 0, zOffset);
        // redLoc.subtract(xOffset, 0, zOffset);

        tickThreshold = 1;

        cacheX = new double[count];
        cacheZ = new double[count];

        for(int i = 0; i < count; i++)
        {
            cacheX[i] = -1;
            cacheZ[i] = -1;
        }

        blueColors = new Color[] {Color.BLUE, Color.AQUA, Color.BLUE};
        redColors = new Color[] {Color.RED, Color.ORANGE, Color.RED};
        purpleColors = new Color[] {Color.PURPLE, Color.FUCHSIA, Color.PURPLE};
        player.getWorld().playSound(purpleLoc, Sound.BLOCK_BEACON_POWER_SELECT, 1, 0.8f);
    }

    private void resetCache()
    {
        cacheX = new double[count];
        cacheZ = new double[count];

        for(int i = 0; i < count; i++)
        {
            cacheX[i] = -1;
            cacheZ[i] = -1;
        }
    }

    private void purple()
    {
        if(cacheX.length != count)
        {
            resetCache();
        }
        //JJKPlugin.tasks.add(new PurpleEffect(null, purpleLoc, player));
        double offset = 0.02;
        for(int i = 0; i < count; i++)
        {
            double sphereOffset = Math.sin(Math.toRadians((i * (90.0/count) ) * 2 - 90.0));
            double sphereOffsetXZ = Math.sin(Math.toRadians((i * (90.0/count) ) * 2));
            //double yDirOffset = Math.sin(Math.toRadians((i*(180.0/count))) * (90.0/count));
            for(int k = 0; k < count; k++)
            {
                Location particlePos = purpleLoc.clone();
                double xOffset = 0;
                double zOffset = 0;

                if(cacheX[k] == -1)
                {
                    double rad = Math.toRadians(k*(360.0/count));
                    xOffset = Math.sin(rad);
                    zOffset = Math.cos(rad);
                    
                    cacheX[k] = xOffset;
                    cacheZ[k] = zOffset;
                }
                else
                {
                    xOffset = cacheX[k];
                    zOffset = cacheZ[k];
                }

                player.getWorld().spawnParticle(Particle.REDSTONE, particlePos.add(xOffset * sphereOffsetXZ * size, sphereOffset * size, zOffset * sphereOffsetXZ * size), 1, offset, offset, offset, new Particle.DustOptions(purpleColors[(int)(Math.random()*3)], 1f));
                if(particlePos.getBlock().getType() != Material.BEDROCK)
                    particlePos.getBlock().breakNaturally();
                for(Object o : JJKPlugin.getNearbyEntities(particlePos, 1))
                {
                    if(o instanceof LivingEntity && o != player)
                    {
                        ((LivingEntity)o).damage(30);
                    }
                }
            }
        }
    }

    private void generateSpheres()
    {
        if(cacheX.length != count)
        {
            resetCache();
        }
        double offset = 0.05;
        for(int i = 0; i < count; i++)
        {
            double sphereOffset = Math.sin(Math.toRadians((i * (90.0/count) ) * 2 - 90.0));
            double sphereOffsetXZ = Math.sin(Math.toRadians((i * (90.0/count) ) * 2));
            for(int k = 0; k < count; k++)
            {
                Location particlePos = start.clone();
                double xOffset = 0;
                double zOffset = 0;

                if(cacheX[k] == -1)
                {
                    double rad = Math.toRadians(k*(360.0/count));
                    xOffset = Math.sin(rad);
                    zOffset = Math.cos(rad);
                    
                    cacheX[k] = xOffset;
                    cacheZ[k] = zOffset;
                }
                else
                {
                    xOffset = cacheX[k];
                    zOffset = cacheZ[k];
                }

                player.getWorld().spawnParticle(Particle.REDSTONE, blueLoc.clone().add(Math.cos(rad)*distance, 0, Math.sin(rad)*distance).add(xOffset * sphereOffsetXZ * size, sphereOffset * size, zOffset * sphereOffsetXZ * size), 1, offset, offset, offset, new Particle.DustOptions(blueColors[(int)(Math.random()*3)], 1f));
                player.getWorld().spawnParticle(Particle.REDSTONE, redLoc.clone().add(Math.cos(rad)*-distance, 0, Math.sin(rad)*-distance).add(xOffset * sphereOffsetXZ * size, sphereOffset * size, zOffset * sphereOffsetXZ * size), 1, offset, offset, offset, new Particle.DustOptions(redColors[(int)(Math.random()*3)], 1f));
            }
        }
    }

    @Override
    public void run()
    {
        animTick++;
        if(animTick < 40)
        {
            generateSpheres();

            distance = 2.5 * ((40-animTick)/40.0);
            return;
        }

        if(animTick == 40)
        {
            player.getWorld().playSound(purpleLoc, Sound.ITEM_TRIDENT_THUNDER, 1, 0.5f);
            generateSpheres();
            size = 0.7;
            count = 40;
        }

        purple();

        if(animTick > 40 && animTick < 52)
        {
            size += 0.1;

            return;
        }

        if(animTick < 60)
            return;
        if(animTick == 60)
        {
            player.getWorld().playSound(purpleLoc, Sound.ITEM_TRIDENT_THUNDER, 1, 0.8f);

        }

        purpleTick();

        t++;

        if(t > 140)
            cancel = true;
    }

    private void purpleTick()
    {
        purpleLoc.add(purpleLoc.getDirection());
        player.getWorld().playSound(purpleLoc, Sound.BLOCK_AMETHYST_BLOCK_RESONATE, 1, 1.2f);
    }
    
}
