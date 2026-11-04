package com.example.quickmute;

import com.mojang.brigadier.arguments.StringArgumentType;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.minecraft.client.Minecraft;

import java.util.LinkedHashMap;
import java.util.Map;

public class QuickMuteClient implements ClientModInitializer {

    // команда -> "время пункт"
    private static final Map<String, String> MUTES = new LinkedHashMap<>();
    static {
        MUTES.put("osk",     "1h 2.3");
        MUTES.put("flood",   "30m 2.1");
        MUTES.put("oskrod",  "4h 2.8");
        MUTES.put("capslock","30m 2.2");
    }

    @Override
    public void onInitializeClient() {
        ClientCommandRegistrationCallback.EVENT.register((dispatcher, registryAccess) -> {
            MUTES.forEach((name, args) ->
                dispatcher.register(
                    ClientCommandManager.literal(name)
                        .then(ClientCommandManager.argument("nick", StringArgumentType.word())
                            .executes(ctx -> {
                                String nick = StringArgumentType.getString(ctx, "nick");
                                Minecraft mc = Minecraft.getInstance();
                                if (mc.player != null) {
                                    mc.player.connection.sendCommand("tempmute " + nick + " " + args);
                                }
                                return 1;
                            }))
                )
            );
        });
    }
}
