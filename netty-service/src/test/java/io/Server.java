package io;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.Charset;

public class Server {

    public static void main(String[] args) throws IOException, InterruptedException {

//        String cmdPing = "cmd";
//        Runtime run = Runtime.getRuntime();
//        Process process = run.exec(cmdPing);
//        new Thread(() -> {
//            BufferedReader br = new BufferedReader(new InputStreamReader(process.getInputStream(), Charset.forName("UTF-8")));
//            while (true) {
//                try {
//                    System.out.println(br.readLine());
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//
//            }
//        }).start();
//
//        new Thread(()->{
//            BufferedReader bufferedReader=new BufferedReader(new InputStreamReader(System.in));
//            PrintWriter out = new PrintWriter(process.getOutputStream(),true);
//            while (true) {
//                String s = null;
//                try {
//                    s = bufferedReader.readLine();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//                System.out.println(s);
//                out.println(s);
//            }
//        }).start();


        ServerSocket socket = new ServerSocket(9999);
        while (true) {
            Socket client = socket.accept();
            new Thread(() -> {
                try {
                    Process process = Runtime.getRuntime().exec("cmd");
                    new Thread(() -> {
                        try {
                            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(client.getInputStream(),  Charset.forName("GBK")));
                            PrintWriter out = new PrintWriter(process.getOutputStream(), true);
                            while (true) {
                                String cmd = bufferedReader.readLine();
                                if ("exit".equals(cmd)) {
                                    break;
                                }
                                out.println(cmd);
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        } finally {
                            try {
                                process.destroy();
                                client.close();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }).start();

                    new Thread(() -> {
                        try {
                            BufferedReader processReader = new BufferedReader(new InputStreamReader(process.getInputStream(),  Charset.forName("GBK")));
                            PrintWriter out = new PrintWriter(client.getOutputStream(), true);
                            while (true) {
                                try {
                                    String content = processReader.readLine();
                                    out.println(content);
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        } finally {
                            try {
                                process.destroy();
                                client.close();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }).start();

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }).start();


        }
    }
}
