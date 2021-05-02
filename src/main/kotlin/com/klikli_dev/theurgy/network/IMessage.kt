/*
 * MIT License
 *
 * Copyright 2021 klikli-dev
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and
 * associated documentation files (the "Software"), to deal in the Software without restriction, including
 * without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies
 * of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following
 * conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial
 * portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED,
 * INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR
 * PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE
 * LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT
 * OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR
 * OTHER DEALINGS IN THE SOFTWARE.
 */
package com.klikli_dev.theurgy.network

import net.minecraft.client.Minecraft
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.entity.player.ServerPlayerEntity
import net.minecraft.network.PacketBuffer
import net.minecraft.server.MinecraftServer
import net.minecraftforge.api.distmarker.Dist
import net.minecraftforge.api.distmarker.OnlyIn
import net.minecraftforge.fml.LogicalSide
import net.minecraftforge.fml.network.NetworkEvent
import java.util.function.Supplier

interface IMessage {
    fun encode(buf: PacketBuffer)
    fun decode(buf: PacketBuffer)

    @OnlyIn(Dist.CLIENT)
    fun onClientReceived(minecraft: Minecraft, player: PlayerEntity, context: NetworkEvent.Context)
    fun onServerReceived(
        minecraftServer: MinecraftServer, player: ServerPlayerEntity,
        context: NetworkEvent.Context
    )

    fun handle(ctx: Supplier<NetworkEvent.Context>) {
        ctx.get().apply{
            if (this.direction.receptionSide == LogicalSide.SERVER) {
                this.enqueueWork {
                    val server = ctx.get().sender!!.world.server
                    onServerReceived(server!!, ctx.get().sender!!, ctx.get())
                }
            } else {
                this.enqueueWork {
                    val minecraft = Minecraft.getInstance()
                    onClientReceived(minecraft, minecraft.player!!, ctx.get())
                }
            }
            this.packetHandled = true
        }
    }
}