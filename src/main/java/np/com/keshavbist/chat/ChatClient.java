package np.com.keshavbist.chat;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.StreamObserver;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.net.InetAddress;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import  javax.swing.JFrame;
import javax.swing.text.AttributeSet;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;


/**
 * Created by kbist on 7/4/2017.
 */
public class ChatClient extends JFrame {
    private JPanel panel;
    private JTextField name;
    private JTextField message;
    public JTextPane text;
    public JScrollPane scrollPane;
    private static String IP;
    private static String token;
    private ManagedChannel channel;
    private StreamObserver<ChatRequest> observer;


    ChatClient(String server){
        super("Chat Client");
        channel = ManagedChannelBuilder.forAddress(server, 8090)
                .usePlaintext(true)
                .build();
        ChatServiceGrpc.ChatServiceStub asyncStub = ChatServiceGrpc.newStub(channel);
        observer = asyncStub.chatPool(new StreamObserver<ChatResponse>() {
            @Override
            public void onNext(ChatResponse value) {
                String from = value.getMessage().getUser().split("@")[1];
                String tokenId = value.getMessage().getToken();
                if(tokenId.equals(ChatClient.token) && from.equals(ChatClient.IP)){
                    appendToPane(text,
                            "\n[" +  value.getMessage().getUser() + "]\n",
                            Color.BLUE,
                            StyleConstants.ALIGN_LEFT);
                    appendToPane(text,
                            value.getMessage().getMessage() + "\n",
                            Color.RED,
                            StyleConstants.ALIGN_LEFT);
                }else{
                    appendToPane(text,
                            "\n[" +  value.getMessage().getUser() + "]\n",
                            Color.DARK_GRAY,
                            StyleConstants.ALIGN_RIGHT);
                    appendToPane(text,
                            value.getMessage().getMessage() + "\n",
                            Color.BLACK,
                            StyleConstants.ALIGN_RIGHT);
                    ChatClient.this.toFront();
                    ChatClient.this.repaint();
                    Toolkit.getDefaultToolkit().beep();
                }

            }

            @Override
            public void onError(Throwable t) {
            }

            @Override
            public void onCompleted() {

            }
        });

        this.setLayout(new BorderLayout());
        this.setSize(600, 700);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        panel = new JPanel();
        panel.setLayout(new FlowLayout());
        name = new JTextField();
        name.setPreferredSize(new Dimension(100, 25));
        message = new JTextField();
        message.setPreferredSize(new Dimension(250, 25));
        message.addKeyListener(new KeyListener() {
                    @Override
                    public void keyTyped(KeyEvent e) {
                    }
                    @Override
                    public void keyPressed(KeyEvent e) {
                        if (name.getText().length() == 0){
                            message.setText(null);
                            appendToPane(text, "SET YOUR USER NAME!!\nYou cannot send message without user name\n", Color.RED, StyleConstants.ALIGN_CENTER);
                            message.setText(null);
                        }else if (e.getKeyCode() == 10) {
                            try {
                                if (!message.getText().equals(" ") && (message.getText().length() != 0)) {
                                    observer.onNext(
                                            ChatRequest.newBuilder()
                                                    .setUser(name.getText() + "@" + ChatClient.IP)
                                                    .setMessage(message.getText())
                                                    .setToken(ChatClient.token)
                                                    .build());
                                }
                            } catch (NullPointerException ne) {}
                            message.setText(null);
                        }

                    }

                    @Override
                    public void keyReleased(KeyEvent e) {}
                });
        panel.add(name);
        panel.add(message);
        this.add(panel, BorderLayout.SOUTH);
        text = new JTextPane();
        scrollPane = new JScrollPane(text);
        scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane.getVerticalScrollBar().addAdjustmentListener(new AdjustmentListener() {
            public void adjustmentValueChanged(AdjustmentEvent e) {
                e.getAdjustable().setValue(e.getAdjustable().getMaximum());
            }
        });
        this.add(scrollPane, BorderLayout.CENTER);

        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                super.windowClosing(e);
                try {
                    observer.onCompleted();
                    channel.shutdown().awaitTermination(5, TimeUnit.SECONDS);
                } catch (InterruptedException e1) {
                    e1.printStackTrace();
                }
                e.getWindow().dispose();
            }
        });
        this.setVisible(true);
    }

    private void appendToPane(JTextPane tp, String msg, Color c, int align)
    {
        StyleContext sc = StyleContext.getDefaultStyleContext();
        AttributeSet aset = sc.addAttribute(SimpleAttributeSet.EMPTY, StyleConstants.Foreground, c);

        aset = sc.addAttribute(aset, StyleConstants.FontFamily, "Lucida Console");
        aset = sc.addAttribute(aset, StyleConstants.Alignment, align);

        int len = tp.getDocument().getLength();
        tp.setCaretPosition(len);
        tp.setCharacterAttributes(aset, false);
        tp.replaceSelection(msg);
    }

    public static void main(String[] args) throws Exception {
        IP = InetAddress.getLocalHost().getHostAddress().toString();
        token = new String(new Random(12345).doubles().toString());
        SwingUtilities.invokeLater(
                new Runnable() {
                    @Override
                    public void run() {
                        if (args.length > 0)
                            new ChatClient(args[0]);
                        else
                            new ChatClient("127.0.0.1");
                    }
                }
        );
    }
}
