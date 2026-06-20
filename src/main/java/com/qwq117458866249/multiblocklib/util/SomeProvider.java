package com.qwq117458866249.multiblocklib.util;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;

import java.util.Optional;
import java.util.stream.Stream;

public class SomeProvider implements HolderLookup.Provider {
    @Override
    public Stream<ResourceKey<? extends Registry<?>>> listRegistryKeys() {
        return Stream.empty();
    }

    @Override
    public <T> Optional<? extends HolderLookup.RegistryLookup<T>> lookup(ResourceKey<? extends Registry<? extends T>> resourceKey) {
        return Optional.empty();
    }
}
