package com.cafecraft.geology.mixin;

import net.minecraft.tag.BlockTags;
import net.minecraft.util.Identifier;
import net.minecraft.world.biome.source.BiomeAccessType;
import net.minecraft.world.biome.source.HorizontalVoronoiBiomeAccessType;
import net.minecraft.world.dimension.DimensionType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.OptionalLong;

@Mixin(DimensionType.class)
public interface OverworldDimensionTypeAccessor {
    @Accessor("OVERWORLD")
    @Mutable
    public static void setOverworld(DimensionType dimensionType) {
        throw new AssertionError();
    }
}
