//
// Name: Savla, Jay
// Project: 3
// Due: October 17, 2018
// Course: CS-2450-01-F18
//
// Description: Standard memory game using the given icon images that measures time until completion.
// 		
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Collections;
import javax.swing.*;


public class MemoryGame {
	
	private int currentIndex;
	private int numberOfClicks = 0, secondCardIndex = 0;
	private ArrayList<JToggleButton> cards = new ArrayList<>();
	private Timer gameTimer;
	private Timer cardReset;
	private int timeUpdate = 0;
	private JLabel timeTaken;
	private boolean cardNotPressed = false;
	private ArrayList<JToggleButton> completed = new ArrayList<>();
	private ArrayList<JToggleButton> incomplete = new ArrayList<>();
	private boolean isDebug = false;
	
	@SuppressWarnings("deprecation")
	MemoryGame() {
		
		JFrame jfrm = new JFrame("Memory Game");
		jfrm.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		jfrm.setSize(1100, 700);
		JPanel mainPanel = new JPanel();
		mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
		JPanel timerPanel = new JPanel();
		JPanel cardPanel = new JPanel();
		timerPanel.setLayout(new BorderLayout());
		cardPanel.setLayout(new GridLayout(3,4));
		
		timeTaken = new JLabel("00:00:00");	
		timeTaken.setHorizontalAlignment(JLabel.CENTER);
		timeTaken.setFont(new Font("Serif", Font.BOLD, 20));	
		
		ImageIcon startingImage = new ImageIcon("MemoryGame.png");
		ImageIcon image1 = new ImageIcon("1.png");
		ImageIcon image2 = new ImageIcon("2.png");
		ImageIcon image3 = new ImageIcon("3.png");
		ImageIcon image4 = new ImageIcon("4.png");
		ImageIcon image5 = new ImageIcon("5.png");
		ImageIcon image6 = new ImageIcon("6.png");

		for (int i = 0; i < 12; i++) { // adds JToggleButtons to ArrayList
			cards.add(new JToggleButton(startingImage));	
		}
		
		ArrayList<ImageIcon> images = new ArrayList<>(); // adds images to ArrayList
		images.add(image1);
		images.add(image2);
		images.add(image3);
		images.add(image4);
		images.add(image5);
		images.add(image6);
		
		for (int i = 0; i < cards.size(); i++) { // sets selected icon, disabled icon, disabledSelected icon
			cards.get(i).setSelectedIcon(images.get(i%6));
			cards.get(i).setDisabledIcon(startingImage);
			cards.get(i).setDisabledSelectedIcon(images.get(i%6));
			cards.get(i).setHorizontalAlignment(JToggleButton.CENTER);
			cards.get(i).setVerticalAlignment(JToggleButton.CENTER);
		}
		
		Collections.shuffle(cards); // randomizes order of cards
		for (int i = 0; i < cards.size(); ++i) { // adds buttons to jfrm
			cardPanel.add(cards.get(i));
			cards.get(i).addItemListener(new CardFlipped());
		}
		
		ActionListener cardResetListener = new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				getIncompleteCards();
				cards.get(currentIndex).setSelected(false);
				cards.get(secondCardIndex).setSelected(false);
				for (int i = 0; i < cards.size(); ++i) {
					cards.get(i).setEnabled(true);
				}
				for (int j = 0; j < completed.size(); ++j) {
					completed.get(j).setEnabled(false);
				}
				cardReset.stop();
			}
		};
		
		ActionListener gameTimerListener = new ActionListener() {
			public void actionPerformed(ActionEvent ae) {	
				gameTimer.setInitialDelay(0);
				updateTime();
			}
		};
		
		gameTimer = new Timer(1000, gameTimerListener);
		cardReset = new Timer(1500, cardResetListener);
		
		mainPanel.add(timerPanel);
		mainPanel.add(cardPanel);
		
		jfrm.add(mainPanel);
		
		// HOMEWORK 1
		
		ActionListener exitListener = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent ae) {
				// TODO Auto-generated method stub
				System.exit(0);
			}	
		};
		
		ActionListener pauseListener = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent ae) {
				// TODO Auto-generated method stub
				if (gameTimer.isRunning()) {
					gameTimer.stop();
				}
			}	
		};
		
		ActionListener resumeListener = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent ae) {
				// TODO Auto-generated method stub
				if (timeUpdate != 0) {
					gameTimer.start();
				}
			}	
		};
		
		ActionListener revealListener = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent ae) {
				// TODO Auto-generated method stub
				gameTimer.stop();
				for (int i = 0; i < cards.size(); ++i) {
					cards.get(i).setSelected(true);
					cards.get(i).setEnabled(false);
					cardNotPressed = true;
				}
			}	
		};
		
		ActionListener helpListener = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent ae) {
				// TODO Auto-generated method stub
				JOptionPane.showMessageDialog(mainPanel, "<html>Press two cards. If they match, they will remain flipped over. <br>If they do "
						+ "not match, they will display for 1.5 seconds and then revert to their original face. <br>In these 1.5 seconds, you will be"
						+ " unable to select any other cards.");
			}	
		};
		
		ActionListener aboutListener = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent ae) {
				// TODO Auto-generated method stub
				JOptionPane.showMessageDialog(mainPanel, "<html>Standard memory game created by Jay Savla. <br>"
						+ "The objective is to match pairs of cards with the same image on the back side.", null, JOptionPane.INFORMATION_MESSAGE, startingImage);
			}	
		};
		
		JMenuBar jmb = new JMenuBar();
		JMenu actionMenu = new JMenu("Action"); 
		actionMenu.setMnemonic(KeyEvent.VK_A);
		
		JMenu gameTimerMenu = new JMenu("Game Timer"); 
		gameTimerMenu.setMnemonic(KeyEvent.VK_T);
		
		JMenuItem pauseButton = new JMenuItem("Pause");
		pauseButton.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_P, InputEvent.CTRL_MASK));
		pauseButton.addActionListener(pauseListener);
		
		JMenuItem resumeButton = new JMenuItem("Resume");
		resumeButton.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_R, InputEvent.CTRL_MASK));
		resumeButton.addActionListener(resumeListener);
		
		gameTimerMenu.add(pauseButton);
		gameTimerMenu.add(resumeButton);
		
		actionMenu.add(gameTimerMenu);
		
		JMenuItem revealButton = new JMenuItem("Reveal");
		revealButton.setMnemonic(KeyEvent.VK_R);
		revealButton.addActionListener(revealListener);
		
		actionMenu.add(gameTimerMenu);
		actionMenu.add(revealButton);
		
		actionMenu.addSeparator();
		
		JMenuItem exitButton = new JMenuItem("Exit");
		exitButton.setMnemonic(KeyEvent.VK_X);
		exitButton.addActionListener(exitListener);
		
		actionMenu.add(exitButton);
		
		JMenu helpMenu = new JMenu("Help");
		helpMenu.setMnemonic(KeyEvent.VK_H);
		
		JMenuItem helpButton = new JMenuItem("View Help...");
		helpButton.setMnemonic(KeyEvent.VK_H);	
		helpButton.addActionListener(helpListener);
		
		helpMenu.add(helpButton);
		
		helpMenu.addSeparator();
		
		JMenuItem aboutButton = new JMenuItem("About");
		aboutButton.setMnemonic(KeyEvent.VK_A);
		aboutButton.addActionListener(aboutListener);
		
		helpMenu.add(aboutButton);
		
		jmb.add(actionMenu);
		jmb.add(helpMenu);
		
		timerPanel.add(jmb);
		timerPanel.add(timeTaken, BorderLayout.NORTH);
		
		jfrm.setVisible(true);
	}
	
	private class CardFlipped implements ItemListener {
		public void itemStateChanged(ItemEvent ie) {
		// TODO Auto-generated method stub
			
			gameTimer.start();
			
			if (cardReset.isRunning()) {
				return;
			}
			
			JToggleButton temp = (JToggleButton) ie.getItem();

			for (int i = 0; i < cards.size(); i++) {
				if (temp == cards.get(i)) {
					currentIndex = i;
					cards.get(currentIndex).setEnabled(false);
					numberOfClicks++;
				}
			}
			if (numberOfClicks % 2 == 0 && !cardNotPressed) {
				if (currentIndex == secondCardIndex) {
					numberOfClicks--;
					}
				else if (cards.get(currentIndex).getSelectedIcon() != cards.get(secondCardIndex).getSelectedIcon()) {
					getIncompleteCards();
					for (int i = 0; i < incomplete.size(); ++i) {
						incomplete.get(i).setEnabled(false);
					}
					cardReset.start();
				}
				else if (cards.get(currentIndex).getSelectedIcon() == cards.get(secondCardIndex).getSelectedIcon() && currentIndex != secondCardIndex) {
					cards.get(currentIndex).setEnabled(false);
					cards.get(secondCardIndex).setEnabled(false);
					completed.add(cards.get(currentIndex));
					completed.add(cards.get(secondCardIndex));
				}
			}
			else {
				secondCardIndex = currentIndex;
			}
			stopGameTimer();
		}	
	}
	
	void updateTime() { // updates the contents of timeTaken label 
		int minutes = timeUpdate / 60;
		int seconds = timeUpdate - (minutes*60);
		int hours = minutes / 60;
		String time = String.format("%02d:%02d:%02d", hours, minutes, seconds);
		++timeUpdate;
		timeTaken.setText(time);
		
	}
	
	void stopGameTimer() { // determines when to end the game
		if (cards.get(0).isSelected() && cards.get(1).isSelected() && cards.get(2).isSelected() && cards.get(3).isSelected() &&
			cards.get(4).isSelected() && cards.get(5).isSelected() && cards.get(6).isSelected() && cards.get(7).isSelected() &&
			cards.get(8).isSelected() && cards.get(9).isSelected() && cards.get(10).isSelected() && cards.get(11).isSelected()) {
				gameTimer.stop();
		}

	}
	
	void getIncompleteCards() { 
		boolean contains = false;
		for (int i = 0; i < cards.size(); ++i) {
			for (int j = 0; j < completed.size(); ++j) {
				if (cards.get(i) == completed.get(j)) {
					contains = true;
					break;
				}
			}
			if (!contains) {
				incomplete.add(cards.get(i));
			}
			else {
				contains = false;
			}
		}
	}
		
	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				new MemoryGame();
			}
		});
	}
}

