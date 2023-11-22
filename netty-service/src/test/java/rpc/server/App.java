package rpc.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import rpc.protocol.request.Head;

import java.io.*;

public class App {

    static int HEAD_LENGTH = 0;

    static {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ObjectOutputStream objectOutputStream = null;
        try {
            objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
            objectOutputStream.writeObject(new Head());
            HEAD_LENGTH = byteArrayOutputStream.size();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                byteArrayOutputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                objectOutputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    public static void main(String[] args) throws IOException {

//        System.out.println(byteArrayOutputStream.toByteArray().length);
//        App app = new App();
//        app.run();
    }

    public void run() {
        ServerBootstrap server = new ServerBootstrap();
        NioEventLoopGroup nioEventLoopGroup = new NioEventLoopGroup(1);
        server.group(nioEventLoopGroup)
                .channel(NioServerSocketChannel.class)
                .childHandler(new ChannelInitializer<NioSocketChannel>() {
                    @Override
                    protected void initChannel(NioSocketChannel ch) throws Exception {
                        ChannelPipeline pipeline = ch.pipeline();
//                        pipeline.addLast()
                    }
                })
                .bind(9999);
    }

    class Handler extends ChannelInboundHandlerAdapter {
        public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
            ByteBuf byteBuf = (ByteBuf) msg;
            int length = byteBuf.readableBytes();
            if (length >= HEAD_LENGTH) {
                byte[] bytes = new byte[HEAD_LENGTH];
                byteBuf.readBytes(bytes);
                ObjectInputStream objectInputStream = new ObjectInputStream(new ByteArrayInputStream(bytes));
                Head head = (Head) objectInputStream.readObject();
                Integer length1 = head.getBodyLength();
            }
//            CharSequence charSequence = byteBuf.readCharSequence(length, CharsetUtil.UTF_8);
//            System.out.println(charSequence.toString());
//            ctx.writeAndFlush(Unpooled.copiedBuffer("收到。。", CharsetUtil.UTF_8));
        }
    }
}
