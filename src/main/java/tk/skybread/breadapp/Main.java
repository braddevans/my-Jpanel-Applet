package tk.skybread.breadapp;

import tk.skybread.breadapp.Database.DatabaseImpl;
import tk.skybread.breadapp.Utils.NewTabUtil;
import tk.skybread.breadapp.view.LoggerTab;
import tk.skybread.breadapp.view.MainWindow;
import tk.skybread.breadapp.view.TopMenu;

import java.awt.*;
import java.sql.SQLException;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Level;
import javax.swing.*;

public class Main extends JFrame {
	private JFrame frame = new JFrame("Bread App [Testing]");
	private static Main instance;

	public static Main getInstance() {
		return instance;
	}

	public Main() {
		instance = this;
		try {

			frame.getContentPane().add(BorderLayout.NORTH, TopMenu.mbar());
			frame.add(NewTabUtil.add("Main", MainWindow.init()));
			frame.add(NewTabUtil.add("Logger", LoggerTab.panel()), BorderLayout.CENTER);
			frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

			frame.pack();
			frame.setMinimumSize(new Dimension(1200, 900));
			frame.setVisible(true);

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		Main main = new Main();

		DatabaseImpl.connect();
		DatabaseImpl.init();
		KeepDbConnectionAlive();

		LoggerTab.Logger(Level.INFO,"Startup Complete");
	}

	public static void KeepDbConnectionAlive() {
		Timer timer = new Timer();
		timer.schedule(new TimerTask() {
			@Override
			public void run() {
				try {
					int rows = DatabaseImpl.numberOfRows();
					LoggerTab.Logger(Level.INFO,"[DB keep Alive] task complete number of rows: " + rows);
				} catch (Exception e) {
					LoggerTab.Logger(Level.WARNING, e.toString());
				}
			}
		}, 0, 1000 * 60 * 2);

	}
}
