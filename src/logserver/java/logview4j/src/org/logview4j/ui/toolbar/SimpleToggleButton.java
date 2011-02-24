package org.logview4j.ui.toolbar;

import javax.swing.ImageIcon;

import org.logview4j.ui.image.ImageManager;

/**
 * 
 */
public class SimpleToggleButton extends ToolBarButton {

	protected boolean on = false;
	protected ImageIcon offIcon = null;
	protected ImageIcon onIcon = null;

	/**
	 * Creates a new simple toggle button
	 * @param name the name of the button
	 * @param altText the alt text
	 * @param b
	 * @param offIcon the off icon
	 * @param onIcon the on icon
	 */
	public SimpleToggleButton(String name, String altText, String offIconName,
			String onIconName, boolean selected) {

		super(name, altText, offIconName);

		offIcon = ImageManager.getInstance().getImage(offIconName);
		onIcon = ImageManager.getInstance().getImage(onIconName);

		if (selected) {
			toggleOn();
		}

		updateToggleState();
	}

	protected void init() {
		super.init();
	}

	/**
	 * Flips the toggle state
	 */
	public void toggleOn() {
		on = !on;
		updateToggleState();
	}

	/**
	 * Fetches the on state
	 * @return true if on, false if off
	 */
	public boolean isOn() {
		return on;
	}

	/**
	 * Updates the toggle state
	 */
	private void updateToggleState() {
		if (on) {
			setIcon(onIcon);
		} else {
			setIcon(offIcon);
		}

	}
}
