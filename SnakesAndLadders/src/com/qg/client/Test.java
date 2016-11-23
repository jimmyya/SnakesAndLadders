package com.qg.client;

import javax.swing.JFrame;

public class Test {

	public static void main(String[] a) {

		JFrame f = new JFrame();
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.setSize(450, 450);

		SnakesAndLaddersGUI gui = new SnakesAndLaddersGUI();
		f.getContentPane().add(gui);
		f.show();

		gui.setNumberOfPlayers(5);
		int[] pos = new int[5];
		for (int i = 0; i < 5; i++) {
			pos[i] = 1;
			gui.setPosition(i, 1);
		}
		try {
			Thread.sleep(1500);
		} catch (Exception e) {
			e.printStackTrace();
		}

		while (true) {
			try {
				int dice;
				Thread.sleep(500);
				for (int i = 0; i < 5; i++) {
					dice = (int) (Math.random() * 6);
					while (dice > 0) {
						gui.setPosition(i, pos[i]++);
						dice--;
						Thread.sleep(100);

					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}
