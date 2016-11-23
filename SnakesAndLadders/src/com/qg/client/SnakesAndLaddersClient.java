package com.qg.client;

import jdk.internal.util.xml.impl.Input;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import javax.swing.*;
import javax.xml.bind.SchemaOutputResolver;

import static sun.audio.AudioPlayer.player;

public class SnakesAndLaddersClient extends JFrame {
    private final int PORT = 3000; // Port number
    private PrintWriter out;
    private BufferedReader in;
    private JButton shake;
    private static int num;//编号
    private static SnakesAndLaddersGUI gui = new SnakesAndLaddersGUI();

    /**
     * 显示投色子页面的GUI
     */
    private void showGUI() {
        //背景
        ImageIcon icon = new ImageIcon(SnakesAndLaddersClient.class.getResource("board.gif"));
        JLabel background = new JLabel(icon);
        getLayeredPane().add(background, new Integer(Integer.MIN_VALUE));
        background.setBounds(0, 0, 420, 300);
        ((JPanel) getContentPane()).setOpaque(false);
        // 界面
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle("玩家页面");
        setSize(420, 300);// 设置大小;
        setLayout(null);// 设置布局
        setResizable(false);

        //加入游戏
        JLabel playNum = new JLabel("当前玩家人数：" + num);
        playNum.setBounds(20, 50, 80, 20);
        // JButton joinButton=new JButton("加入游戏");
        //  joinButton.addActionListener(new ActionListener() {
        //     @Override
        //      public void actionPerformed(ActionEvent e) {
        //         if(num<=5) startGame();
        //          else
        //     }
        // });
        //  joinButton.setBounds(120,50,80,20);

        JLabel playColor = new JLabel("玩家" + Data.color[num]);
        playColor.setBounds(10, 100, 80, 20);

        playColor.setFont(new Font("宋体",Font.BOLD,16));
        shake = new JButton("摇色子");
        shake.setBounds(120, 100, 80, 20);
        JTextField pointNum = new JTextField();// 显示色子
        pointNum.setBounds(220, 100, 80, 20);
        JButton start = new JButton("开始");
        start.addActionListener((ActionEvent e)-> {
                start.setEnabled(false);
                showSnakesAndLaddersGUI();
        });
        start.setBounds(320, 100, 80, 20);
        // 添加点击事件为选择一个随机数字
        shake.addActionListener((ActionEvent e)-> {
                int dice = (int) (Math.random() * 60) % 6 + 1;
                out.println(num + ":" + dice);//玩家编号和点数
                shake.setEnabled(false);
                pointNum.setText(dice + "");
        });
        add(shake);
        add(playColor);
        add(pointNum);
        add(start);
    }

    /**
     * 开始连接通信
     */
    private void startSocket() {
        try {
            // Try to connect to server at IP address/port combination
            Socket soc = new Socket("127.0.0.1", PORT);
            // Now get the 'streams' - you use these to send messages
            out = new PrintWriter(soc.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(soc.getInputStream()));
            num = Integer.parseInt(in.readLine());

        } catch (Exception e) {
            e.printStackTrace(); // Displays Error if things go wrong....
        }
    }

    private static int[] pos = new int[5];
    ;

    /**
     *   接收广播信息
     */
    private void recevice() {
        int receviceNum = num;
        for (int i = 0; i < 5; i++) {
            pos[i] = 1;
            gui.setPosition(i, 1);
        }
        try {
            while (true) {
                String pointNum = in.readLine();
                System.out.println(pointNum);
                String[] playerPoint = pointNum.split(":");

                int playerp = Integer.parseInt(playerPoint[0]);
                int dice = Integer.parseInt(playerPoint[1]);
                int playerNum = Integer.parseInt(playerPoint[2]);
                while (dice > 0) {
                    gui.setPosition(playerp, pos[playerp]++);//决定是否在第一格
                    dice--;
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                System.out.println(receviceNum);
                if (receviceNum % playerNum == 0) {
                    shake.setEnabled(true);
                }
                receviceNum++;

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 开始游戏
     */
    private static void startGame() {
        SnakesAndLaddersClient client = new SnakesAndLaddersClient();
        client.startSocket();
        if (num >= 5) {
            JOptionPane.showMessageDialog(null, "人数已满");
            return;
        }
        client.showGUI();
        client.setVisible(true);
        client.recevice();
    }


    /**
     * 游戏界面
     */
    private static void showSnakesAndLaddersGUI() {
        JFrame f = new JFrame();
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setSize(450, 450);

        f.getContentPane().add(gui);
        f.show();
        gui.setNumberOfPlayers(5);
        try {
            Thread.sleep(1500);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        startGame();
    }

}

