package com.reclipse.redstoneactivation.mixin;

import com.reclipse.redstoneactivation.Config;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.RedStoneOreBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(RedStoneOreBlock.class)
public class RedStoneOreBlockMixin {

    @Inject(method = "attack", at = @At("HEAD"))
    private void onAttack(BlockState state, Level level, BlockPos pos, Player player, CallbackInfo ci) {
        redstoneactivation$giveRewardIfUnlit(state, level, player);
    }

    @Inject(method = "use", at = @At("HEAD"))
    private void onUse(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit, CallbackInfoReturnable<InteractionResult> cir) {
        redstoneactivation$giveRewardIfUnlit(state, level, player);
    }

    @Inject(method = "stepOn", at = @At("HEAD"))
    private void onStepOn(Level level, BlockPos pos, BlockState state, Entity entity, CallbackInfo ci) {
        if (entity instanceof Player player && !entity.isSteppingCarefully()) {
            redstoneactivation$giveRewardIfUnlit(state, level, player);
        }
    }

    @Unique
    private void redstoneactivation$giveRewardIfUnlit(BlockState state, Level level, Player player) {
        if (level.isClientSide()) return;
        if (state.getValue(RedStoneOreBlock.LIT)) return;

        ItemStack reward = new ItemStack(Config.rewardItem.get());
        if (!player.addItem(reward)) {
            player.drop(reward, false);
        }
    }
}
