package net.gogoikawa.calculoos;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.StringArgumentType;
import net.fabricmc.api.ModInitializer;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;

public class Calculoos implements ModInitializer {
	public static final String MODID = "calculoos";
	public static final Logger LOGGER = LoggerFactory.getLogger(MODID);

	@Override
	public void onInitialize() {
		CommandRegistrationCallback.EVENT.register((dispatcher, dedicated, server) -> {
			dispatcher.register(CommandManager.literal("calc")
					.then(CommandManager.argument("calculation", StringArgumentType.greedyString())
							.executes(new CommandCalculator())
					));
		});
	}
}
