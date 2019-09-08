package eu.sajuk.tsdev.zebrastogglesneak;

import java.text.DecimalFormat;

import net.minecraft.client.GameSettings;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.potion.Effects;
import net.minecraft.util.MovementInput;
import net.minecraft.util.math.Vec3d;

public class MovementInputModded extends MovementInput {

	private final GameSettings gameSettings;
	public boolean sprint;
	private ZebrasToggleSneak ZTS;
	private Minecraft mc;
	private int sneakWasPressed;
	private int sprintWasPressed;
	private ClientPlayerEntity player;
	private float originalFlySpeed = -1.0F;
	private float boostedFlySpeed;

	public MovementInputModded(GameSettings gameSettings, ZebrasToggleSneak ZTS) {
		this.gameSettings = gameSettings;
		this.sprint = false;
		this.ZTS = ZTS;
		this.mc = Minecraft.getInstance(); // we'll need replace the static ref by a link passed as parameter
		this.sneakWasPressed = 0;
		this.sprintWasPressed = 0;
	}

	@Override
	public void tick(boolean slow, boolean noDampening) {
		
		player = mc.player;
		moveStrafe = 0.0F;
		moveForward = 0.0F;

		if (this.forwardKeyDown = gameSettings.keyBindForward.isKeyDown()) moveForward++;
		if (this.backKeyDown = gameSettings.keyBindBack.isKeyDown()) moveForward--;
		if (this.leftKeyDown = gameSettings.keyBindLeft.isKeyDown()) moveStrafe++;
		if (this.rightKeyDown = gameSettings.keyBindRight.isKeyDown()) moveStrafe--;

		jump = gameSettings.keyBindJump.isKeyDown();
		
		if (ZTS.config.toggleSneak) {
			if (gameSettings.keyBindSneak.isKeyDown()) {
				if (sneakWasPressed == 0) {
					if (sneak) {
						sneakWasPressed = -1;
					} else if (player.isRidingHorse() || player.abilities.isFlying) {
						sneakWasPressed = ZTS.config.keyHoldTicks + 1;
					} else {
						sneakWasPressed = 1;
					}
					sneak = !sneak;
				} else if (sneakWasPressed > 0){
					sneakWasPressed++;
				}
			} else {
				if ((ZTS.config.keyHoldTicks > 0) && (sneakWasPressed > ZTS.config.keyHoldTicks)) sneak = false;
				sneakWasPressed = 0;
			}
		} else {
			sneak = gameSettings.keyBindSneak.isKeyDown();
		}
		
		if (sneak) {
			moveStrafe *= 0.3F;
			moveForward *= 0.3F;
		}
		
		if (ZTS.config.toggleSprint) {
			if (gameSettings.keyBindSprint.isKeyDown()) {
				if (sprintWasPressed == 0) {
					if (sprint) {
						sprintWasPressed = -1;
					} else if (player.abilities.isFlying) {
						sprintWasPressed = ZTS.config.keyHoldTicks + 1;
					} else {
						sprintWasPressed = 1;
					}
					sprint = !sprint;
				} else if (sprintWasPressed > 0){
					sprintWasPressed++;
				}
			} else {
				if ((ZTS.config.keyHoldTicks > 0) && (sprintWasPressed > ZTS.config.keyHoldTicks)) sprint = false;
				sprintWasPressed = 0;
			}
		} else sprint = false;
		
		// sprint conditions same as in net.minecraft.client.entity.EntityPlayerSP.onLivingUpdate()
		// check for hungry or flying. But nvm, if conditions not met, sprint will 
		// be canceled there afterwards anyways 
		if (sprint && moveForward == 1.0F && player.onGround && !player.isHandActive()
				&& !player.isPotionActive(Effects.BLINDNESS)) player.setSprinting(true);
		
		if (ZTS.config.flyBoost && player.abilities.isCreativeMode && player.abilities.isFlying 
				&& (mc.getRenderViewEntity() == player) && sprint) {
			
			if (originalFlySpeed < 0.0F || this.player.abilities.getFlySpeed() != boostedFlySpeed)
				originalFlySpeed = this.player.abilities.getFlySpeed();
			boostedFlySpeed = originalFlySpeed * ZTS.config.flyBoostFactor;
			player.abilities.setFlySpeed(boostedFlySpeed);
			
			Vec3d motion = player.getMotion();
			if (sneak) 
			{
				player.setMotion(motion.x, motion.y - (0.15D * (double)(ZTS.config.flyBoostFactor - 1.0F)), motion.z);
			}
			if (jump)
			{
				player.setMotion(motion.x, motion.y + (0.15D * (double)(ZTS.config.flyBoostFactor - 1.0F)), motion.z);
			}
				
		} else {
			if (player.abilities.getFlySpeed() == boostedFlySpeed)
				this.player.abilities.setFlySpeed(originalFlySpeed);
			originalFlySpeed = -1.0F;
		}

	}
	
	public String displayText() {
		
		// This is a slightly refactored version of Deez's UpdateStatus( ... ) function
		// found here https://github.com/DouweKoopmans/ToggleSneak/blob/master/src/main/java/deez/togglesneak/CustomMovementInput.java
		
		String displayText = "";
		boolean isFlying = mc.player.abilities.isFlying;
		boolean isRiding = mc.player.isRidingHorse();
		boolean isHoldingSneak = gameSettings.keyBindSneak.isKeyDown();
		boolean isHoldingSprint = gameSettings.keyBindSprint.isKeyDown();
		
		if (isFlying) {
			if (originalFlySpeed > 0.0F) {
				displayText += "[Flying (" + (new DecimalFormat("#.0")).format(boostedFlySpeed/originalFlySpeed) + "x Boost)]  ";								
			} else {
				displayText += "[Flying]  ";				
			}
		}
		if (isRiding) displayText += "[Riding]  ";
		
		if (sneak) {

			if (isFlying) displayText += "[Descending]  ";
			else if (isRiding) displayText += "[Dismounting]  ";
			else if (isHoldingSneak) displayText += "[Sneaking (Key Held)]  ";
			else displayText += "[Sneaking (Toggled)]  ";

		} else if (sprint && !isFlying && !isRiding) {

			if (isHoldingSprint) displayText += "[Sprinting (Key Held)]";
			else displayText += "[Sprinting (Toggled)]";
		}
		
		return displayText.trim();
	}
}
