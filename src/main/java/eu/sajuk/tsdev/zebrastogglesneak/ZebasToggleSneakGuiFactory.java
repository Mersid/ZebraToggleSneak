package eu.sajuk.tsdev.zebrastogglesneak;

import java.util.Set;

import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.client.IModGuiFactory;

public abstract class ZebasToggleSneakGuiFactory<RuntimeOptionGuiHandler> implements IModGuiFactory {

	@Override
	public void initialize(Minecraft minecraftInstance) {}

	@Override
	public Set<RuntimeOptionCategoryElement> runtimeGuiCategories() { return null; }

	public RuntimeOptionGuiHandler getHandlerFor(RuntimeOptionCategoryElement element) { return null; }

}
