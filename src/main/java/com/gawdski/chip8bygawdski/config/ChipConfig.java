package com.gawdski.chip8bygawdski.config;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.auto.value.AutoValue;

/**
 * @author Anna Gawda
 * 16.03.2018
 */

@AutoValue
public abstract class ChipConfig {

    @JsonCreator
    public static ChipConfig create(@JsonProperty(value = "screenScale") int screenScale) {
        return new AutoValue_ChipConfig(screenScale);
    }

    public abstract int getScreenScale();
}
