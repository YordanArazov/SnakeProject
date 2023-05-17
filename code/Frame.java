package code;

import java.awt.EventQueue;
import javax.swing.JFrame;

public class Frame extends JFrame {

	private static final long serialVersionUID = 1L;


	public Frame() {
        
        initUI();
    }
    
    private void initUI() {
        add(new Panel());
        
        setResizable(false);
        pack();
        
        setTitle("Snake Game");
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
    

    public static void main(String[] args) {
        
        EventQueue.invokeLater(() -> {
            JFrame ex = new Frame();
            ex.setVisible(true);
        });
    }
}

