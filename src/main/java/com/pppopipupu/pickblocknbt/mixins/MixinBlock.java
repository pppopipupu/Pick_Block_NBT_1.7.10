package com.pppopipupu.pickblocknbt.mixins;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;

import org.lwjgl.input.Keyboard;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.pppopipupu.pickblocknbt.CommonProxy;
import com.pppopipupu.pickblocknbt.network.PacketRequestNBT;

@Mixin(Block.class)
public abstract class MixinBlock {

    @Inject(
        method = "Lnet/minecraft/block/Block;getPickBlock(Lnet/minecraft/util/MovingObjectPosition;Lnet/minecraft/world/World;IIILnet/minecraft/entity/player/EntityPlayer;)Lnet/minecraft/item/ItemStack;",
        at = @At("HEAD"),
        cancellable = true,
        remap = false

    )
    private void getPickBlock(MovingObjectPosition target, World world, int x, int y, int z, EntityPlayer player,
        CallbackInfoReturnable<ItemStack> cir) {
        boolean isCtrlKeyDown = Keyboard.isKeyDown(Keyboard.KEY_LCONTROL) || Keyboard.isKeyDown(Keyboard.KEY_RCONTROL);
        if (isCtrlKeyDown && player.capabilities.isCreativeMode) {
            CommonProxy.network.sendToServer(new PacketRequestNBT(getPickBlock(target, world, x, y, z), x, y, z));
            cir.setReturnValue(null);

        }
    }

    @Deprecated
    @Shadow
    public ItemStack getPickBlock(MovingObjectPosition target, World world, int x, int y, int z) {
        return null;
    }

}
