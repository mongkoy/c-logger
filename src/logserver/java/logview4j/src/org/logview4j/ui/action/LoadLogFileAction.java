package org.logview4j.ui.action;

import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JFileChooser;
import org.logview4j.config.ConfigurationKey;
import org.logview4j.config.ConfigurationManager;
import org.logview4j.listener.FileProcessor;
import org.logview4j.threads.LogView4JThreadManager;

/**
 *
 * @author Jefty Negapatan
 */
public class LoadLogFileAction extends AbstractAction {

	protected static final String LOG_OUTPUT_FOLDER = ConfigurationManager.getInstance().getString(ConfigurationKey.LOG_OUTPUT_FOLDER);

	public LoadLogFileAction() {
		putValue(Action.NAME, "Load log file...");
	}

	public void actionPerformed(ActionEvent e) {
		JFileChooser fileChooser = new JFileChooser(LOG_OUTPUT_FOLDER);
		int option;
		fileChooser.setMultiSelectionEnabled(false);
		option = fileChooser.showOpenDialog(null);
		if (option == JFileChooser.APPROVE_OPTION) {
			FileProcessor fileProcessor;
			fileProcessor = new FileProcessor(fileChooser.getSelectedFile());
			LogView4JThreadManager.getInstance().execute(fileProcessor);
		}
	}
}
