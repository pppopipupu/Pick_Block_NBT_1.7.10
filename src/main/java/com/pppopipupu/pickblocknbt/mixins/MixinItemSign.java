package com.pppopipupu.pickblocknbt.mixins;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemSign;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntitySign;
import net.minecraft.world.World;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ItemSign.class)
public abstract class MixinItemSign {

    @Inject(
        method = "Lnet/minecraft/item/ItemSign;onItemUse(Lnet/minecraft/item/ItemStack;Lnet/minecraft/entity/player/EntityPlayer;Lnet/minecraft/world/World;IIIIFFF)Z",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/World;getTileEntity(III)Lnet/minecraft/tileentity/TileEntity;"),
        cancellable = true,
        remap = false

    )
    public void onItemUse(ItemStack p_77648_1_, EntityPlayer p_77648_2_, World p_77648_3_, int p_77648_4_,
        int p_77648_5_, int p_77648_6_, int p_77648_7_, float p_77648_8_, float p_77648_9_, float p_77648_10_,
        CallbackInfoReturnable<Boolean> cir) {
        if (p_77648_1_.hasTagCompound() && p_77648_1_.getTagCompound()
            .hasKey("BlockEntityTag", 10)) {
            TileEntitySign tileentitysign = (TileEntitySign) p_77648_3_
                .getTileEntity(p_77648_4_, p_77648_5_, p_77648_6_);
            if (tileentitysign != null) {
                NBTTagCompound itemNbt = p_77648_1_.getTagCompound();
                NBTTagCompound blockEntityTag = itemNbt.getCompoundTag("BlockEntityTag");
                NBTTagCompound nbtToApply = (NBTTagCompound) blockEntityTag.copy();
                nbtToApply.setInteger("x", p_77648_4_);
                nbtToApply.setInteger("y", p_77648_5_);
                nbtToApply.setInteger("z", p_77648_6_);
                tileentitysign.readFromNBT(nbtToApply);
                tileentitysign.markDirty();
                p_77648_3_.markBlockForUpdate(p_77648_4_, p_77648_5_, p_77648_6_);
                cir.setReturnValue(true);
            }
        }
    }
}
