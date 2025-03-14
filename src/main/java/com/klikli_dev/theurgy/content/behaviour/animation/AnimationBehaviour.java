// SPDX-FileCopyrightText: 2023 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.content.behaviour.animation;

import net.minecraft.world.level.block.entity.BlockEntity;
import software.bernie.geckolib.animatable.GeoBlockEntity;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animation.AnimationController;
import software.bernie.geckolib.animation.AnimationState;
import software.bernie.geckolib.animation.PlayState;
import software.bernie.geckolib.util.GeckoLibUtil;

public abstract class AnimationBehaviour<T extends BlockEntity & GeoBlockEntity> {

    protected final AnimatableInstanceCache animatableInstanceCache;
    protected T blockEntity;
    protected boolean wasProcessingLastTick;

    public AnimationBehaviour(T blockEntity) {
        this.blockEntity = blockEntity;
        this.animatableInstanceCache = GeckoLibUtil.createInstanceCache(blockEntity);
    }

    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.animatableInstanceCache;
    }

    public abstract <E extends GeoBlockEntity> PlayState animationHandler(AnimationState<E> event);

}
