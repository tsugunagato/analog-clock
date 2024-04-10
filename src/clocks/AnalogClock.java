package clocks;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalTime;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.Timer;

public class AnalogClock {
	static final int WIDTH = 280;
	static final int HEIGHT = 310;
	public static void main(String... args) {
		
		JFrame frame = new JFrame("LOG-LOCK");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(WIDTH, HEIGHT);
		//リサイズ不可
		frame.setResizable(false);
		//常に手前
		frame.setAlwaysOnTop(true);
		//アイコン設定
		ImageIcon icon = new ImageIcon("././image/padlock.png");
		frame.setIconImage(icon.getImage());
		
		//表示位置を画面右上に指定
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		frame.setLocation(screenSize.width - WIDTH + 7, 0);
		
		//針・文字盤を描画
		ClockPanel.updateTime();
		ClockPanel clockPanel = new ClockPanel();
		clockPanel.setBackground(new Color(31, 44, 40));
		frame.add(clockPanel);
		
		frame.setVisible(true);
	}
}

class ClockPanel extends JPanel {
	public static int hour;
	public static int minute;
	public static int second;
	
	//コンストラクタ
	public ClockPanel() {
		Timer timer = new Timer(100, new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				updateTime();
				repaint();
			}
		});
		timer.start();
	}
	
	public static void updateTime() {
		LocalTime now = LocalTime.now();
		hour = now.getHour();
		minute = now.getMinute();
		second = now.getSecond();
	}
	
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		//中心座標と針の長さの限界値を決める
		int centerX = getWidth() / 2;
		int centerY = getHeight() / 2;
		int radius = Math.min(centerX, centerY);
		
		//文字盤の描画
		drawClockNumbers(g, centerX, centerY, radius);
		
		//短針の描画
		drawHand(g, centerX, centerY, radius * 0.5, (hour%12 + minute / 60.0) * 30, new Color(180, 233, 0), 4);
		//長針の描画
		drawHand(g, centerX, centerY, radius * 0.7, minute * 6, new Color(222, 231, 228), 3);
		//秒針の描画
		drawHand(g, centerX, centerY, radius * 0.7, second * 6, new Color(100, 100, 100), 2);
	}
	
	//針の描画
	private void drawHand(Graphics g, int centerX, int centerY, double length, double angle, Color color, int str) {
		int x = centerX + (int) (length * Math.sin(Math.toRadians(angle)));
		int y = centerY - (int) (length * Math.cos(Math.toRadians(angle)));
		
		Graphics2D g2d = (Graphics2D) g.create();
		// アンチエイリアス処理
	    g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		
		g2d.setColor(color);
		//針の先を丸める
		g2d.setStroke(new BasicStroke(str, BasicStroke.CAP_ROUND, BasicStroke.JOIN_BEVEL));
		g2d.drawLine(centerX, centerY, x, y);
		g2d.dispose();
	}
	
	//文字盤の描画
	private void drawClockNumbers(Graphics g, int centerX, int centerY, int radius) {
		Graphics2D g2d = (Graphics2D) g.create();
		
		// アンチエイリアス処理
	    g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		
		String clockFont = "BIZ UDGothic";
		//数字を描画
		for(int i = 1; i <= 12; i++) {
			//「現在時」の文字盤だけ大太文字にする
			if(i != hour % 12 && hour % 12 != 0) {
				g2d.setFont(new Font(clockFont, Font.PLAIN, 15));
				g2d.setColor(Color.WHITE);
			} else {
				g2d.setFont(new Font(clockFont, Font.BOLD, 18));
				g2d.setColor(new Color(180, 233, 0));
			}
			//12分割とおまじない
			double angle = Math.toRadians(i * 30 - 90);
			int x = (int) (centerX + (radius - 20) * Math.cos(angle)) - 3;
			int y = (int) (centerY + (radius - 20) * Math.sin(angle)) + 6;
			g2d.drawString(Integer.toString(i), x, y);
		}
	}
}