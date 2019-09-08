package eu.sajuk.tsdev.zebrastogglesneak;

import net.minecraft.client.MainWindow;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.AbstractGui;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.resources.I18n;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType;
import net.minecraftforge.eventbus.api.SubscribeEvent;


public class GuiDrawer extends AbstractGui {

	private final Minecraft mc = Minecraft.getInstance();
	private final ZebrasToggleSneak ZTS;
	private final MovementInputModded MIM;
	private int mcDisplayWidth = -1, mcDisplayHeight = -1;
	private int x1, x2;
	private int sneaky1, sneaky2, sprinty1, sprinty2;
	private String hPos, vPos;
	private String[] hPosOptions, vPosOptions;
	private String sprintTxt, sneakTxt, onlyTxt;

	public GuiDrawer(ZebrasToggleSneak zTS, MovementInputModded mIM) {
		super();
		ZTS = zTS;
		MIM = mIM;
	}
	
	public void setDrawPosition(String hPos, String vPos, String[] hPosOptions, String[] vPosOptions) {
		//System.out.println("HPOS: " + hPos + ", VPOS: " + vPos + ", HPOSOPTS: " + hPosOptions.toString() + ", VPOSOPTS: " + vPosOptions.toString());
		this.hPos = hPos; this.vPos = vPos;
		this.hPosOptions = hPosOptions; this.vPosOptions = vPosOptions;
		//sprintTxt = I18n.format("zebrastogglesneak.display.label.sprint");
		//sneakTxt = I18n.format("zebrastogglesneak.display.label.sneak");
		sprintTxt = "sprint";
		sneakTxt = "sneak";
        mcDisplayWidth = -1;
        mcDisplayHeight = -1;
	}

	@SubscribeEvent
	public void afterDraw (RenderGameOverlayEvent.Post event) {

		if (event.getType() != ElementType.ALL) return;
		if (ZTS.displayStatus() == 1) {
			computeDrawPosIfChanged();
			fill(x1, sneaky1, x2, sneaky2, ZTS.config.toggleSneak?colorPack(0,0,196,196):colorPack(196,196,196,64));	    	
			drawString(mc.fontRenderer, sneakTxt, x1 + 2, sneaky1 + 2,
					MIM.sneak?colorPack(255,255,0,96):colorPack(64,64,64,128));
			fill(x1, sprinty1, x2, sprinty2, ZTS.config.toggleSprint?colorPack(0,0,196,196):colorPack(196,196,196,64));	    	
			drawString(mc.fontRenderer, sprintTxt, x1 + 2, sprinty1 + 2,
					MIM.sprint?colorPack(255,255,0,96):colorPack(64,64,64,128));
			/*
			fill(2, 226, 6, 229, colorPack(0, 255, 20, 127));
			fill(
					(int)(2),
					(int)(226),
					(int)(16 * mc.mainWindow.getGuiScaleFactor()),
					(int)(240 * mc.mainWindow.getGuiScaleFactor()),
					colorPack(0, 255, 20, 127)
					);
			System.out.println(mc.mainWindow.getGuiScaleFactor());
			*/
		} else if (ZTS.displayStatus() == 2) {
			// no optimization here - I don't like the text only display anyway
	        computeTextPos(onlyTxt = MIM.displayText());
			drawString(mc.fontRenderer, onlyTxt, x1, sneaky1, colorPack(255,255,255,192));
		}
	}
	
	public void computeDrawPosIfChanged() {
		MainWindow screen = mc.mainWindow;
		//if ((mcDisplayWidth == mc.displayWidth) && (mcDisplayHeight == mc.displayHeight)) return;
		if ((mcDisplayWidth == screen.getScaledWidth()) && (mcDisplayHeight == screen.getScaledHeight())) return;
		//System.out.println("rectX1: " + x1 + ", rectX2: " + x2 + ", recySnY1: " + sneaky1 + ", rectSnY2: " + sneaky2 + ", rectSpY1: " + sprinty1 + ", rectSpY2: " + sprinty2);
		//System.out.println("screenX: " + screen.getWidth() + ", screenY: " + screen.getHeight());
		
        int displayWidth = screen.getScaledWidth();
		int textWidth = Math.max(mc.fontRenderer.getStringWidth(sprintTxt), mc.fontRenderer.getStringWidth(sneakTxt));
        if (hPos.equals(hPosOptions[2])) {
        	x2 = displayWidth - 2;
        	x1 = x2 - 2 - textWidth - 2;
        } else if (hPos.equals(hPosOptions[1])) {
        	x1 = (displayWidth / 2) - (textWidth / 2) - 2;
        	x2 = x1 + 2 + textWidth + 2;        	
        } else {
        	x1 = 2;
        	x2 = x1 + 2 + textWidth + 2;
        }

        int displayHeight = screen.getScaledHeight();
		int textHeight = mc.fontRenderer.FONT_HEIGHT;
        if (vPos.equals(vPosOptions[2])) {
        	sprinty2 = displayHeight - 2;
        	sprinty1 = sprinty2 - 2 - textHeight - 2;
        	sneaky2 = sprinty1 - 2;
        	sneaky1 = sneaky2 - 2 - textHeight - 2;
        } else if (vPos.equals(vPosOptions[1])) {
        	sneaky1 = (displayHeight / 2) - 1 - 2 - textHeight - 2;
        	sneaky2 = sneaky1 + 2 + textHeight + 2;
        	sprinty1 = sneaky2 + 2;
        	sprinty2 = sprinty1 + 2 + textHeight + 2;
        } else {
        	sneaky1 = 2;
        	sneaky2 = sneaky1 + 2 + textHeight + 2;
        	sprinty1 = sneaky2 + 2;
        	sprinty2 = sprinty1 + 2 + textHeight + 2;
        }
		
        mcDisplayWidth = screen.getScaledWidth();
        mcDisplayHeight = screen.getScaledHeight();
	}

	public void computeTextPos(String displayTxt) {
		
		MainWindow screen = mc.mainWindow;
		
        int displayWidth = screen.getScaledWidth();
        int textWidth = mc.fontRenderer.getStringWidth(displayTxt);
        if (hPos.equals(hPosOptions[2])) {
        	x1 = displayWidth - textWidth - 2;
        } else if (hPos.equals(hPosOptions[1])) {
        	x1 = (displayWidth / 2) - (textWidth / 2) - 2;
        } else {
        	x1 = 2;
        	x2 = x1 + 2 + textWidth + 2;        	
        }

        int displayHeight = screen.getScaledHeight();
		int textHeight = mc.fontRenderer.FONT_HEIGHT;
        if (vPos.equals(vPosOptions[2])) {
        	sneaky1 = displayHeight - 2;
        } else if (vPos.equals(vPosOptions[1])) {
        	sneaky1 = (displayHeight / 2) + textHeight/2;
        } else {
        	sneaky1 = 2 + textHeight;
        }
	}

	private int colorPack (int red, int green, int blue, int alpha){
		return ((red & 255) << 16) | ((green & 255) << 8) | (blue & 255) | ((alpha & 255) << 24);
	}
	
}
