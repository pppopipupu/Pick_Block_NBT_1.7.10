package com.pppopipupu.pickblocknbt;

import com.pppopipupu.pickblocknbt.network.PacketRequestNBT;

import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import cpw.mods.fml.relauncher.Side;

public class CommonProxy {

    public static SimpleNetworkWrapper network;

    // preInit "Run before anything else. Read your config, create blocks, items, etc, and register them with the
    // GameRegistry." (Remove if not needed)
    public void preInit(FMLPreInitializationEvent event) {
        network = NetworkRegistry.INSTANCE.newSimpleChannel(PickBlockNBT.MODID);
        network.registerMessage(PacketRequestNBT.Handler.class, PacketRequestNBT.class, 0, Side.SERVER);
    }
}
