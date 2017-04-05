package com.googlecode.bpmn_simulator.gui.preferences;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.MessageFormat;
import java.util.regex.Pattern;

import javax.swing.AbstractCellEditor;
import javax.swing.DefaultCellEditor;
import javax.swing.JColorChooser;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SortOrder;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;

import org.jdesktop.swingx.JXButton;
import org.jdesktop.swingx.JXSearchField;
import org.jdesktop.swingx.JXTable;
import org.jdesktop.swingx.color.EyeDropperColorChooserPanel;
import org.jdesktop.swingx.sort.RowFilters;
import org.jdesktop.swingx.table.TableColumnExt;
import org.jdesktop.swingx.util.PaintUtils;

import com.googlecode.bpmn_simulator.animation.execution.Animator;

@SuppressWarnings("serial")
public class ElementsTable
		extends JXTable {

	public ElementsTable(final ElementsTableModel tableModel, final boolean showName) {
		super(tableModel);
		createDefaultColumnsFromModel();
		setColumnControlVisible(true);
		setSortable(true);
		setEditable(true);
		setColumnSelectionAllowed(false);
		setRowSelectionAllowed(false);
		putClientProperty(USE_DTCR_COLORMEMORY_HACK, Boolean.FALSE);
		prepareColumns();
		final int nameColumnViewIndex = convertColumnIndexToView(ElementsTableModel.COLUMN_NAME);
		if (showName) {
			setSortOrder(nameColumnViewIndex, SortOrder.ASCENDING);
		} else {
			removeColumn(getColumn(nameColumnViewIndex));
			setSortOrder(convertColumnIndexToView(ElementsTableModel.COLUMN_TYPE), SortOrder.ASCENDING);
		}
		packTable(4);
	}

	private void prepareColumns() {
		setDefaultRenderer(Color.class, new ColorRenderer());
		setDefaultEditor(Color.class, new ColorEditor());
		setColumn(ElementsTableModel.COLUMN_TYPE, "Type", null, null);
		setColumn(ElementsTableModel.COLUMN_NAME, "Name", null, null);
		setColumn(ElementsTableModel.COLUMN_STEP_COUNT, "Duration", new StepsRenderer(), new StepsEditor());
		setColumn(ElementsTableModel.COLUMN_FOREGROUND_COLOR, "Foreground", null, null);
		setColumn(ElementsTableModel.COLUMN_BACKGROUND_COLOR, "Background", null, null);
	}

	public void setColumn(final int columnIndexModel, final String title,
			final TableCellRenderer renderer, final TableCellEditor editor) {
		final TableColumnExt column = getColumnExt(convertColumnIndexToView(columnIndexModel));
		if (column != null) {
			column.setHeaderValue(title);
			column.setCellRenderer(renderer);
			column.setCellEditor(editor);
		}
	}

	public static JComponent decorate(final ElementsTable table) {
		table.setFillsViewportHeight(true);
		final JXSearchField search = new JXSearchField();
		search.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(final ActionEvent e) {
				final String text = search.getText();
				table.setRowFilter(text.isEmpty() ? null : RowFilters.regexFilter(Pattern.CASE_INSENSITIVE, text, 0));
			}
		});
		final JPanel panel = new JPanel(new BorderLayout(2, 2));
		panel.add(search, BorderLayout.PAGE_START);
		final JScrollPane scroll = new JScrollPane(table);
		panel.add(scroll, BorderLayout.CENTER);
		return panel;
	}

	public static class ColorRenderer
			extends DefaultTableCellRenderer {

		public ColorRenderer() {
			super();
			setHorizontalAlignment(SwingConstants.CENTER);
		}

		@Override
		public Component getTableCellRendererComponent(final JTable table,
				final Object value, final boolean isSelected, final boolean hasFocus,
				final int row, final int column) {
			final Color color = (Color) value;
			final Component component = super.getTableCellRendererComponent(table, (color == null) ? null : PaintUtils.toHexString(color), isSelected, hasFocus, row, column);
			if (color != null) {
				component.setBackground(color);
				component.setForeground(PaintUtils.computeForeground(color));
			}
			return component;
		}

	}

	public static class ColorEditor
			extends AbstractCellEditor
			implements TableCellEditor {

		private final JXButton button = new JXButton();

		private Color currentColor;

		public ColorEditor() {
			super();
			button.setContentAreaFilled(false);
			button.setOpaque(true);
			button.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(final ActionEvent e) {
					final JColorChooser colorChooser = new JColorChooser(currentColor);
					colorChooser.addChooserPanel(new EyeDropperColorChooserPanel());
					JDialog dialog = JColorChooser.createDialog(button, null, true, colorChooser, new ActionListener() {
						@Override
						public void actionPerformed(final ActionEvent e) {
							setColor(colorChooser.getColor());
						}
					}, null);
					dialog.setVisible(true);
				}
			});
		}

		private void setColor(final Color color) {
			currentColor = color;
			if (color != null) {
				button.setBackground(color);
				button.setForeground(PaintUtils.computeForeground(color));
				button.setText(PaintUtils.toHexString(color));
			}
		}

		@Override
		public Object getCellEditorValue() {
			return currentColor;
		}

		@Override
		public Component getTableCellEditorComponent(final JTable table, final Object value,
				final boolean isSelected, final int row, final int column) {
			setColor((Color) value);
			return button;
		}

	}

	public static class StepsRenderer
			extends DefaultTableCellRenderer {

		public StepsRenderer() {
			setHorizontalAlignment(SwingConstants.RIGHT);
		}

		@Override
		public Component getTableCellRendererComponent(final JTable table, final Object value,
				final boolean isSelected, final boolean hasFocus,
				final int row, final int column) {
			Object displayValue = value;
			if ((value != null) && (value instanceof Number)) {
				final double sec = Animator.stepsToSeconds(((Number) value).intValue());
				displayValue = MessageFormat.format("{0,number} sec", sec);
			}
			return super.getTableCellRendererComponent(table, displayValue, isSelected, hasFocus,
					row, column);
		}

	}

	public static class StepsEditor
			extends DefaultCellEditor {

		public StepsEditor() {
			super(new JTextField());
		}

		@Override
		public Component getTableCellEditorComponent(final JTable table, final Object value,
				final boolean isSelected, final int row, final int column) {
			final Double sec = Double.valueOf(Animator.stepsToSeconds(((Integer) value)));
			return super.getTableCellEditorComponent(table, sec, isSelected, row, column);
		}

		@Override
		public boolean stopCellEditing() {
			try {
				getCellEditorValue();
				return super.stopCellEditing();
			} catch (NumberFormatException e) {
				return false;
			}
		}

		@Override
		public Object getCellEditorValue() {
			final double sec = Double.parseDouble((String) super.getCellEditorValue());
			return Integer.valueOf(Animator.secondsToSteps(sec));
		}

	}

}
