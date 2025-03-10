/*
 * SPDX-FileCopyrightText: 2022 klikli-dev
 *
 * SPDX-License-Identifier: MIT
 */

package com.klikli_dev.theurgy.content.render;

import net.minecraft.client.Minecraft;

public class ClientTicks {
    public static long ticks = 0;
    public static float partialTicks = 0;
    public static float delta = 0;
    public static float total = 0;

    private static void calcDelta() {
        float oldTotal = total;
        total = ticks + partialTicks;
        delta = total - oldTotal;
    }

    public static void renderTickStart(float pt) {
        partialTicks = pt;
    }

    public static void renderTickEnd() {
        calcDelta();
    }

    public static void endClientTick(Minecraft mc) {
        ticks++;
        partialTicks = 0;

        calcDelta();
    }

    public static float getPartialTicksHandlePause() {
        Minecraft mc = Minecraft.getInstance();
        return mc.getTimer().getGameTimeDeltaPartialTick(mc.isPaused());
    }
}
