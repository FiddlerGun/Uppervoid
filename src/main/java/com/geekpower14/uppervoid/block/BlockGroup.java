package com.geekpower14.uppervoid.block;

import com.google.gson.JsonElement;
import net.samagames.tools.ParticleEffect;
import net.samagames.tools.SimpleBlock;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;

public class BlockGroup
{
    private static final SimpleBlock VOID = new SimpleBlock(Material.AIR);

    private final SimpleBlock blockFine;
    private final SimpleBlock blockWarning;
    private final SimpleBlock blockCritical;

    public BlockGroup(JsonElement group)
    {
        String[] blocks = group.getAsString().split(", ");
        String[] blockFineData = blocks[0].split(":");
        this.blockFine = new SimpleBlock(Material.matchMaterial(blockFineData[0]), Integer.valueOf(blockFineData[1]));

        String[] blockWarningData = blocks[1].split(":");
        this.blockWarning = new SimpleBlock(Material.matchMaterial(blockWarningData[0]), Integer.valueOf(blockWarningData[1]));

        String[] blockCriticalData = blocks[2].split(":");
        this.blockCritical = new SimpleBlock(Material.matchMaterial(blockCriticalData[0]), Integer.valueOf(blockCriticalData[1]));
    }

    public boolean isThis(Block block)
    {
        if (block == null)
            return false;

        if (this.is(block, this.blockFine))
            return true;
        else if (this.is(block, this.blockWarning))
            return true;
        else if (this.is(block, this.blockCritical))
            return true;

        return false;
    }

    @SuppressWarnings("deprecation")
    public boolean damage(Block block, int damage)
    {
        boolean result = false;

        for (int i = 0; i < damage; i++)
            if (this.setNext(block))
                result = true;

        if(result)
        {
            ParticleEffect.VILLAGER_HAPPY.display(0.2F, 0.1F, 0.2F, 10F, 1, block.getLocation().add(0.5D, 1.1D, 0.5D), 50);
            ParticleEffect.BLOCK_CRACK.display(new ParticleEffect.BlockData(block.getType(), block.getData()), 0.2F, 0.3F, 0.2F, 10F, 5, block.getLocation().add(0.5D, 1.1D, 0.5D), 50);
        }

        return result;
    }

    private boolean setNext(Block block)
    {
        if (block == null)
            return false;

        if (this.is(block, this.blockFine))
        {
            this.setNext(block, this.blockWarning);
            return true;
        }
        else if (this.is(block, this.blockWarning))
        {
            this.setNext(block, this.blockCritical);
            return true;
        }
        else if (this.is(block, this.blockCritical))
        {
            this.setNext(block, VOID);
            this.setNext(block.getRelative(BlockFace.DOWN), VOID);
            return true;
        }

        return false;
    }

    @SuppressWarnings("deprecation")
    private void setNext(Block block, SimpleBlock simpleBlock)
    {
        block.setType(simpleBlock.getType());
        block.setData(simpleBlock.getData());
    }

    @SuppressWarnings("deprecation")
    private boolean is(Block block, SimpleBlock modal)
    {
        return block.getType() == modal.getType() && block.getData() == modal.getData();
    }
}
