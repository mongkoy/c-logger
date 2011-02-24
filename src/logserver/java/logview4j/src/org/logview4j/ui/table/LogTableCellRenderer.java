/*
 * Copyright 1999-2005 The Apache Software Foundation.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * 
 * $Id: LogTableCellRenderer.java,v 1.8 2006/02/22 02:21:30 jpassenger Exp $
 */
package org.logview4j.ui.table;

import java.awt.Color;
import java.awt.Component;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;
import org.logview4j.config.ConfigurationKey;
import org.logview4j.config.ConfigurationManager;

/**
 * A cell renderer for the log table
 */
public class LogTableCellRenderer extends JLabel implements TableCellRenderer {

	private final Date date = new Date();
	/**
	 * Timestamp display format
	 */
	private static final SimpleDateFormat dateFormatter = new SimpleDateFormat
		(ConfigurationManager.getInstance().getString
		(ConfigurationKey.TIMESTAMP_DISPLAY_FORMAT));
	/**
	 * Orange marker color
	 * TODO make this configurable
	 */
	private static final Color MARKER_COLOR = new Color(222, 127, 0);
	private static final Color MARKER_COLOR_BRIGHTER = new Color(222, 127, 0).brighter();

	public LogTableCellRenderer() {
		setOpaque(true);
		setBorder(BorderFactory.createEmptyBorder(0, 2, 0, 2));
	}

	/**
	 * Renders a cell in the table
	 * @param table the table to render into
	 * @param value teh value to render
	 * @param isSelected true if cell is selected
	 * @param hasFocus true if the cell has focus
	 * @param row the row id
	 * @param column the column id
	 * @return the component to render, in this case, this
	 */
	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {

		boolean isMarked = false;
		try {
			isMarked = ((LogTable) table).getLogEvent(row).isMarked();
		} catch (NullPointerException e) {
			System.out.println("table = " + table);

		}

		if (isMarked && isSelected) {
			setForeground(MARKER_COLOR_BRIGHTER);
			setBackground(table.getSelectionBackground());
		} else if (isSelected) {
			setForeground(table.getSelectionForeground());
			setBackground(table.getSelectionBackground());
		} else if (isMarked) {
			setForeground(table.getSelectionForeground());
			setBackground(MARKER_COLOR);
		} else {
			setForeground(table.getForeground());
			setBackground(table.getBackground());
		}

		if (value instanceof String) {
			setText(value.toString());
		} else if (value instanceof Long) {
			date.setTime(((LogTable) table).getLogEvent(row).getWhen());
			setText(dateFormatter.format(date));
		} else if (value instanceof ImageIcon) {
			setIcon((ImageIcon) value);
		} else {
			setIcon(null);
			setText(null);
		}

		return this;
	}
}
