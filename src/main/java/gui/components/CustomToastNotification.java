package gui.components;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class CustomToastNotification {

	public static void showNotification(JPanel parent, String message, String type) {
	    JPanel toastPanel = new JPanel();
	    toastPanel.setLayout(new BorderLayout());
	    toastPanel.setSize(300, 80);
	    toastPanel.setBackground(getBackgroundColor(type));

	    JLabel iconLabel = new JLabel(getIcon(type));
	    iconLabel.setPreferredSize(new Dimension(60, 40));
	    toastPanel.add(iconLabel, BorderLayout.WEST);

	    JLabel messageLabel = new JLabel(message);
	    messageLabel.setForeground(Color.WHITE);
	    messageLabel.setFont(new Font("Arial", Font.BOLD, 14));
	    toastPanel.add(messageLabel, BorderLayout.CENTER);

	    JLabel closeButton = new JLabel("X", SwingConstants.CENTER);
	    closeButton.setPreferredSize(new Dimension(40, 40));
	    closeButton.setForeground(Color.WHITE);
	    closeButton.setFont(new Font("Arial", Font.BOLD, 16));
	    closeButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
	    closeButton.addMouseListener(new java.awt.event.MouseAdapter() {
	        @Override
	        public void mouseClicked(java.awt.event.MouseEvent e) {
	            parent.remove(toastPanel);
	            parent.repaint();
	        }
	    });


	    toastPanel.add(closeButton, BorderLayout.EAST);

	    JProgressBar progressBar = new JProgressBar();
	    progressBar.setIndeterminate(false);
	    progressBar.setMaximum(100);
	    progressBar.setValue(100);
	    progressBar.setPreferredSize(new Dimension(250, 10));
	    progressBar.setBorder(null);
	    toastPanel.add(progressBar, BorderLayout.SOUTH);

	    int x = parent.getWidth() - toastPanel.getWidth() - 20;
	    int y = 20;
	    toastPanel.setLocation(x, y);
	    toastPanel.setVisible(true);
	    parent.setLayout(null);
	    parent.add(toastPanel);
	    parent.setComponentZOrder(toastPanel, 0);
	    parent.repaint();

	    Timer timer = new Timer(10, new ActionListener() {
	        int progress = 100;

	        @Override
	        public void actionPerformed(ActionEvent e) {
	            progress -= 1;
	            progressBar.setValue(progress);
	            if (progress <= 0) {
	                parent.remove(toastPanel);
	                parent.repaint();
	                ((Timer) e.getSource()).stop();
	            }
	        }
	    });
//	    timer.setInitialDelay(1000);
	    timer.start();
	}



    private static Color getBackgroundColor(String type) {
        switch (type.toLowerCase()) {
            case "success":
                return new Color(84, 211, 92);
            case "warning":
                return new Color(237, 196, 57);
            case "error":
                return new Color(244, 71, 60);
            case "info":
                return new Color(63, 164, 248);
            default:
                return Color.GRAY;
        }
    }

    private static Icon getIcon(String type) {
        String path = "";
        switch (type.toLowerCase()) {
            case "success":
                path = "/image/success.png";
                break;
            case "warning":
                path = "/image/warning.png";
                break;
            case "error":
                path = "/image/error.png";
                break;
            case "info":
                path = "/image/info.png";
                break;
            default:
                return null;
        }
        java.net.URL resource = CustomToastNotification.class.getResource(path);
        if (resource == null) {
            System.err.println("Loi: " + path);
            return null;
        }

        return new ImageIcon(resource);
    }


}
