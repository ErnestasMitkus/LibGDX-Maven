package com.ernesto.java;

import com.badlogic.gdx.backends.lwjgl.LwjglAWTCanvas;
import com.ernesto.core.MyGame;

import javax.swing.*;
import java.awt.*;

public class MyGameEditor extends JFrame {

    private static final int CANVAS_WIDTH = 683;
    private static final int CANVAS_HEIGHT = 360;
    private static final int WINDOW_WIDTH = CANVAS_WIDTH + 400;
    private static final int WINDOW_HEIGHT = CANVAS_HEIGHT + 210;


    public MyGameEditor() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        final Container container = getContentPane();
        container.setLayout(new BorderLayout());

        LwjglAWTCanvas canvas = new LwjglAWTCanvas(new MyGame());
//        canvas.getCanvas().setSize(CANVAS_WIDTH, CANVAS_HEIGHT);
//        canvas.getCanvas().setMaximumSize(new Dimension(CANVAS_WIDTH, CANVAS_HEIGHT));
        canvas.getCanvas().setPreferredSize(new Dimension(CANVAS_WIDTH, CANVAS_HEIGHT));
        container.setLayout(new GridLayout());
        container.add(canvas.getCanvas(), BorderLayout.WEST);
        container.add(new Button("Change"), BorderLayout.EAST);

        pack();
        setVisible(true);
        setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new MyGameEditor();
            }
        });
    }

}
