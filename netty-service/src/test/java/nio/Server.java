package nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

public class Server {

    public static void main(String[] args) throws IOException {

        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        serverSocketChannel.bind(new InetSocketAddress(9999));
        serverSocketChannel.configureBlocking(false);

        // epoll_create
        Selector selector = Selector.open();
        // epoll_ctl
        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);

        while (true) {
            // epoll_wait

            while (selector.select() > 0) {
                Set<SelectionKey> keys = selector.selectedKeys();
                Iterator<SelectionKey> iterator = keys.iterator();
                while (iterator.hasNext()) {
                    SelectionKey key = iterator.next();
                    iterator.remove();
                    if (key.isAcceptable()) {
                        acceptable(key);
                    } else if (key.isReadable()) {
                        readable(key);
                    } else if (key.isWritable()) {
                        writable(key);
                    }
                }
            }

        }
    }

    public static void acceptable(SelectionKey key) throws IOException {
        ServerSocketChannel socketChannel = (ServerSocketChannel) key.channel();
        SocketChannel client = socketChannel.accept();
        System.out.println("有客户端进行连接," + client.getRemoteAddress());
        client.configureBlocking(false);
        ByteBuffer buffer = ByteBuffer.allocate(8192);
        client.register(key.selector(), SelectionKey.OP_READ, buffer);
    }

    public static void readable(SelectionKey key) throws IOException {
        SocketChannel client = (SocketChannel) key.channel();
        ByteBuffer buffer = (ByteBuffer) key.attachment();
        buffer.clear();
        int read = 0;
        while (true) {
            try {
                read = client.read(buffer);
            } catch (IOException e) {
                client.close();
            }
            if (read > 0) {
                buffer.flip();
                int limit = buffer.limit();
                for (int i = 0; i < limit; i++) {
                    System.out.print((char) buffer.get());
                }
                client.register(key.selector(), SelectionKey.OP_WRITE, buffer);
            } else if (read == 0) {
                break;
            } else {
                System.out.println("客户端离开," + client.getRemoteAddress());
                key.cancel();
                client.close();
                break;
            }
        }
    }

    public static void writable(SelectionKey key) throws IOException {
        SocketChannel client = (SocketChannel) key.channel();
        ByteBuffer buffer = (ByteBuffer) key.attachment();
        buffer.flip();
        while (buffer.hasRemaining()) {
            buffer.compact();
            buffer.put("111...\r\n".getBytes());
            buffer.flip();
            client.write(buffer);
        }
        buffer.clear();
        client.register(key.selector(), SelectionKey.OP_READ, buffer);
    }
}
