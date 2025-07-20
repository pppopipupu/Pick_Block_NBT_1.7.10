package com.pppopipupu.pickblocknbt.network;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import io.netty.buffer.ByteBuf;

public class PacketRequestNBT implements IMessage {

    private int x, y, z;

    public PacketRequestNBT() {}

    public PacketRequestNBT(int x, int y, int z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        this.x = buf.readInt();
        this.y = buf.readInt();
        this.z = buf.readInt();
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeInt(this.x);
        buf.writeInt(this.y);
        buf.writeInt(this.z);
    }

    public static class Handler implements IMessageHandler<PacketRequestNBT, IMessage> {

        @Override
        public IMessage onMessage(PacketRequestNBT message, MessageContext ctx) {

            EntityPlayerMP player = ctx.getServerHandler().playerEntity;
            World world = player.worldObj;

            int x = message.x;
            int y = message.y;
            int z = message.z;

            if (player.getDistanceSq(x, y, z) > 64.0D) {
                return null;
            }

            if (!player.capabilities.isCreativeMode) {
                return null;
            }

            Block block = world.getBlock(x, y, z);
            if (block == null || block.isAir(world, x, y, z)) {
                return null;
            }

            ItemStack resultStack = block.getPickBlock(player.rayTrace(5.0D, 1.0F), world, x, y, z, player);
            if (resultStack == null) {
                return null;
            }

            TileEntity tileEntity = world.getTileEntity(x, y, z);

            if (tileEntity != null) {

                NBTTagCompound nbt = new NBTTagCompound();
                tileEntity.writeToNBT(nbt);
                nbt.removeTag("x");
                nbt.removeTag("y");
                nbt.removeTag("z");

                if (!nbt.hasNoTags()) {
                    NBTTagCompound nbttagcompound1 = new NBTTagCompound();
                    NBTTagList nbttaglist = new NBTTagList();
                    nbttaglist.appendTag(new NBTTagString("(+NBT)"));
                    nbttagcompound1.setTag("Lore", nbttaglist);
                    resultStack.setTagInfo("display", nbttagcompound1);
                    resultStack.setTagInfo("BlockEntityTag", nbt);
                }
            }

            player.inventory.mainInventory[player.inventory.currentItem] = resultStack;

            player.updateHeldItem();

            return null;
        }
    }

}
