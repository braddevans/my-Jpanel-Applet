package tk.skybread.breadapp.view;

import tk.skybread.breadapp.Database.DatabaseImpl;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Level;
import javax.swing.*;

public class MainWindow extends JPanel{
	private static JSplitPane splitPane;

	public static JPanel init() throws SQLException {
		JPanel panel = new JPanel(new BorderLayout());

		splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, panel1(), panel2());
		splitPane.setOneTouchExpandable(true);
		splitPane.setDividerLocation(250);

		panel.add(splitPane);
		return panel;
	}

	public static JPanel panel1() throws SQLException {
		JPanel panel = new JPanel(new BorderLayout());
		HashMap<Integer, String> hmap = new HashMap<Integer, String>();


		for (int i = 0; i < 100; i++) {
			ResultSet rs = DatabaseImpl.ServersFromId(i);
			while (rs.next()) {
				hmap.put(rs.getInt("ID"), rs.getString("Server"));
			}
		}

		Object[] keys = hmap.keySet().toArray();
		Object[] values = hmap.values().toArray();
		JList list = new JList(values);

		list.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent evt) {
				JList list = (JList)evt.getSource();
				if (evt.getClickCount() == 1) {

					// Double-click detected
					int index = list.locationToIndex(evt.getPoint());
					LoggerTab.Logger(Level.INFO, "list item: " + keys[index]);
					try {
						ResultSet rs = DatabaseImpl.StatusFromId(index);
						while (rs.next()) {
							LoggerTab.Logger(Level.INFO, "list item status: " + rs.getString("Status"));
						}
					} catch (SQLException e) {
						e.printStackTrace();
					}
				}
			}
		});

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setViewportView(list);
		list.setLayoutOrientation(JList.VERTICAL);
		panel.add(scrollPane);

		return panel;
	}

	public static JPanel panel2() throws SQLException {
		JPanel panel = new JPanel(new BorderLayout());
		JScrollPane scrollPane = new JScrollPane();
		JTextArea content = new JTextArea(15, 30);

		scrollPane.setViewportView(content);
		panel.add(scrollPane);

		return panel;
	}


}
