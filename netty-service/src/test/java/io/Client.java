package io;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.nio.charset.Charset;

public class Client {

    public static void main(String[] args) throws IOException {

        Socket socket = new Socket("127.0.0.1", 9999);

        new Thread(() -> {
            try {
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
                PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
                while (true) {
                    String s = bufferedReader.readLine();
                    System.out.println(s);
                    out.println(s);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();


        new Thread(() -> {
            try {
                BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream(), Charset.forName("GBK")));
                while (true) {
                    String s = reader.readLine();
                    System.out.println(s);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
    }
}
