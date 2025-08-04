package com.pppopipupu.pickblocknbt.mixins;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ItemBlock.class)
public abstract class MixinItemBlock {

    @Inject(
        method = "Lnet/minecraft/item/ItemBlock;placeBlockAt(Lnet/minecraft/item/ItemStack;Lnet/minecraft/entity/player/EntityPlayer;Lnet/minecraft/world/World;IIIIFFFI)Z",
        at = @At(value = "TAIL"

        ),
        remap = false

    )
    private void PlaceBlockAt(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int side,
        float hitX, float hitY, float hitZ, int metadata, CallbackInfoReturnable<Boolean> cir) {

        if (stack.hasTagCompound() && stack.getTagCompound()
            .hasKey("BlockEntityTag", 10)) {
            TileEntity tileEntity = world.getTileEntity(x, y, z);
            if (tileEntity != null) {
                NBTTagCompound itemNbt = stack.getTagCompound();
                NBTTagCompound blockEntityTag = itemNbt.getCompoundTag("BlockEntityTag");
                NBTTagCompound nbtToApply = (NBTTagCompound) blockEntityTag.copy();
                nbtToApply.setInteger("x", x);
                nbtToApply.setInteger("y", y);
                nbtToApply.setInteger("z", z);
                tileEntity.readFromNBT(nbtToApply);
                tileEntity.markDirty();
                world.markBlockForUpdate(x, y, z);
            }
        }

    }
}
