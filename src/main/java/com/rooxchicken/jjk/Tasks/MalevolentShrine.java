package com.rooxchicken.jjk.Tasks;

import java.io.File;
import java.io.FileInputStream;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.util.Vector;

import com.rooxchicken.jjk.JJKPlugin;
import com.rooxchicken.jjk.CursedTechniques.Shrine;
import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.bukkit.BukkitWorld;
import com.sk89q.worldedit.extent.clipboard.Clipboard;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardFormat;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardFormats;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardReader;
import com.sk89q.worldedit.function.operation.Operation;
import com.sk89q.worldedit.function.operation.Operations;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.session.ClipboardHolder;

import net.minecraft.server.network.TextFilter.e;

public class MalevolentShrine extends Task
{
    public int tickThreshold = 1;

    private Player player;
    private Location startPos;
    private Shrine shrine;

    private EditSession shrines;

    private int i = 0;

    public MalevolentShrine(Plugin _plugin, Shrine _shrine, Player _player, Location _startPos)
    {
        super(_plugin);

        player = _player;
        shrine = _shrine;
        startPos = _startPos.clone();

        startPos.add(player.getLocation().clone().subtract(startPos)).setDirection(player.getLocation().getDirection());
        player.getWorld().playSound(startPos, Sound.BLOCK_BEACON_ACTIVATE, 3f, 0.5f);

        Location shrineLocation = JJKPlugin.getBlock(player, 100, 90).getLocation();
        double rad = Math.toRadians(player.getLocation().getYaw());
        double xOffset = Math.sin(rad);
        double zOffset = Math.cos(rad);

        shrineLocation.add(8*xOffset, 0, -8*zOffset);

        startPos = shrineLocation.clone().add(0, 4, 0);
        BukkitWorld weWorld = new BukkitWorld(player.getWorld());
        Clipboard clipboard;
        File file = new File("shrine.schem");
       
        ClipboardFormat format = ClipboardFormats.findByFile(file);
        try (ClipboardReader reader = format.getReader(new FileInputStream(file)))
        {
            clipboard = reader.read();
            
            try (EditSession editSession = WorldEdit.getInstance().newEditSession(weWorld))
            {
                Operation operation = new ClipboardHolder(clipboard)
                        .createPaste(editSession)
                        .to(BlockVector3.at(shrineLocation.getX(), shrineLocation.getY(), shrineLocation.getZ()))
                        // configure here
                        .build();
                Operations.complete(operation);
                shrines = editSession;
            }
        }
        catch(Exception e)
        {
            Bukkit.getLogger().info("Error when pasting shrine schematic!");
            e.printStackTrace();
        }
    }

    @Override
    public void run()
    {
        for(int s = 0; s < 4; s++)
            shrine.slash(player, startPos, true, true, 9, (i % 3 == 0), 0.4f);

        i++;

        if(i > 200)
            cancel = true;
    }

    @Override
    public void onCancel()
    {
        shrines.undo(shrines);
    }
}
