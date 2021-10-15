package com.cafecraft.geology;

import com.cafecraft.geology.mixin.GeneratorTypeAccessor;
import com.cafecraft.geology.mixin.OverworldDimensionTypeAccessor;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.biome.v1.OverworldBiomes;
import net.fabricmc.fabric.api.biome.v1.OverworldClimate;
import net.minecraft.block.Blocks;
import net.minecraft.client.world.GeneratorType;
import net.minecraft.tag.BlockTags;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.BuiltinRegistries;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeEffects;
import net.minecraft.world.biome.GenerationSettings;
import net.minecraft.world.biome.SpawnSettings;
import net.minecraft.world.biome.source.HorizontalVoronoiBiomeAccessType;
import net.minecraft.world.dimension.DimensionType;
import net.minecraft.world.gen.chunk.*;
import net.minecraft.world.gen.feature.DefaultBiomeFeatures;
import net.minecraft.world.gen.surfacebuilder.ConfiguredSurfaceBuilder;
import net.minecraft.world.gen.surfacebuilder.SurfaceBuilder;
import net.minecraft.world.gen.surfacebuilder.TernarySurfaceConfig;

import java.util.Collections;
import java.util.Optional;
import java.util.OptionalLong;

public class Geology implements ModInitializer {

	// Biome
	private static final ConfiguredSurfaceBuilder<TernarySurfaceConfig> TEST_SURFACE_BUILDER = SurfaceBuilder.DEFAULT.withConfig(
			new TernarySurfaceConfig(Blocks.DIAMOND_BLOCK.getDefaultState(), Blocks.DIORITE.getDefaultState(), Blocks.BASALT.getDefaultState()));

	private static final Biome ROCKS = createRockBiome();
	private static Biome createRockBiome() {
		SpawnSettings.Builder spawnSettings = new SpawnSettings.Builder();

		DefaultBiomeFeatures.addFarmAnimals(spawnSettings);
		DefaultBiomeFeatures.addMonsters(spawnSettings, 95, 5, 100);

		GenerationSettings.Builder generationSettings = new GenerationSettings.Builder();
		generationSettings.surfaceBuilder(TEST_SURFACE_BUILDER);
		DefaultBiomeFeatures.addDefaultUndergroundStructures(generationSettings);
		DefaultBiomeFeatures.addLandCarvers(generationSettings);
		DefaultBiomeFeatures.addDefaultLakes(generationSettings);
		DefaultBiomeFeatures.addDungeons(generationSettings);
		//DefaultBiomeFeatures.addMineables(generationSettings);
		//DefaultBiomeFeatures.addDefaultOres(generationSettings);
		DefaultBiomeFeatures.addDefaultDisks(generationSettings);
		DefaultBiomeFeatures.addSprings(generationSettings);
		DefaultBiomeFeatures.addFrozenTopLayer(generationSettings);

		return (new Biome.Builder())
				.precipitation(Biome.Precipitation.SNOW)
				.category(Biome.Category.DESERT)
				.depth(0.125F)
				.scale(0.05F)
				.temperature(0.8F)
				.downfall(0.4F)
				.effects((new BiomeEffects.Builder())
					.waterColor(0xffdb00)
					.waterFogColor(0x050533)
					.fogColor(0xc0d8ff)
					.skyColor(0x77adff)
					.build())
				.spawnSettings(spawnSettings.build())
				.generationSettings(generationSettings.build())
				.build();
	}

	// Biome Key
	public static final RegistryKey<Biome> ROCKS_KEY = RegistryKey.of(Registry.BIOME_KEY, new Identifier("geology", "rocks"));

	// World Generator
	protected static DimensionType NEW_OVERWORLD;

	private static final GeneratorType TEST = new GeneratorType("test") {
		@Override
		protected ChunkGenerator getChunkGenerator(net.minecraft.util.registry.Registry<Biome> biomeRegistry, net.minecraft.util.registry.Registry<ChunkGeneratorSettings> chunkGeneratorSettingsRegistry, long seed) {
			FlatChunkGeneratorConfig config = new FlatChunkGeneratorConfig(new StructuresConfig(Optional.empty(), Collections.emptyMap()), biomeRegistry);
			config.updateLayerBlocks();
			return new FlatChunkGenerator(config);
		}
	};

	@Override
	public void onInitialize() {
		// Changes the world height to whatever we want (in this case, 1024 blocks)
		// (World height might have to be divisible by 16?)
		NEW_OVERWORLD = DimensionType.create(OptionalLong.empty(), true, false, false, true, 1.0D, false, false, true, false, true, 0, 1024, 1024, HorizontalVoronoiBiomeAccessType.INSTANCE, BlockTags.INFINIBURN_OVERWORLD.getId(), new Identifier("overworld"), 1.0F);
		OverworldDimensionTypeAccessor.setOverworld(NEW_OVERWORLD);

		// Register Surface Builder
		Registry.register(BuiltinRegistries.CONFIGURED_SURFACE_BUILDER, new Identifier("geology", "test"), TEST_SURFACE_BUILDER);
		// Register Biome
		Registry.register(BuiltinRegistries.BIOME, ROCKS_KEY.getValue(), ROCKS);

		// Register World Generator
		GeneratorTypeAccessor.getValues().add(TEST);

		OverworldBiomes.addContinentalBiome(ROCKS_KEY, OverworldClimate.DRY, 3D);
		OverworldBiomes.addContinentalBiome(ROCKS_KEY, OverworldClimate.TEMPERATE, 2D);
	}
}
