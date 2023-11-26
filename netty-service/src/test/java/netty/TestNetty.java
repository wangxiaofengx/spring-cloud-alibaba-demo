package netty;

import io.netty.bootstrap.Bootstrap;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.CharsetUtil;
import org.junit.Test;

import java.io.IOException;
import java.net.InetSocketAddress;

public class TestNetty {

    @Test
    public void test() {
        ByteBuf buffer = ByteBufAllocator.DEFAULT.buffer();
    }

    @Test
    public void clientMode() throws InterruptedException, IOException {
        NioEventLoopGroup nioEventLoopGroup = new NioEventLoopGroup();
        NioSocketChannel nioSocketChannel = new NioSocketChannel();
        ChannelPipeline pipeline = nioSocketChannel.pipeline();
        pipeline.addLast(new Handler());
        nioEventLoopGroup.register(nioSocketChannel);
        nioSocketChannel.connect(new InetSocketAddress(9999));
        ChannelFuture channelFuture = nioSocketChannel.writeAndFlush(Unpooled.copiedBuffer("test123".getBytes()));
        channelFuture.sync();
        ChannelFuture close = nioSocketChannel.close();
        close.sync();
    }

    @Test
    public void serverMode() throws IOException {
        NioEventLoopGroup nioEventLoopGroup = new NioEventLoopGroup();
        NioServerSocketChannel server = new NioServerSocketChannel();
        ChannelPipeline pipeline = server.pipeline();
        pipeline.addLast(new AcceptHandler(nioEventLoopGroup));
        nioEventLoopGroup.register(server);
        server.bind(new InetSocketAddress(9999));
        System.in.read();
    }

    @Test
    public void nettyClient() throws InterruptedException, IOException {
        EventLoopGroup nioEventLoopGroup = new NioEventLoopGroup(1);
        Bootstrap bootstrap = new Bootstrap();
        ChannelFuture connect = bootstrap.group(nioEventLoopGroup)
                .channel(NioSocketChannel.class)
                .handler(new ChannelInitializer<NioSocketChannel>() {
                    @Override
                    protected void initChannel(NioSocketChannel ch) throws Exception {
                        ChannelPipeline pipeline = ch.pipeline();
                        pipeline.addLast(new Handler());
                    }
                })
                .connect(new InetSocketAddress(9999));
        connect.sync();
        connect.channel().writeAndFlush(Unpooled.copiedBuffer("hello world", CharsetUtil.UTF_8));
        connect.channel().close().sync();
//        System.in.read();
    }

    @Test
    public void nettyServer() throws InterruptedException, IOException {
        EventLoopGroup nioEventLoopGroup = new NioEventLoopGroup(1);
        ServerBootstrap serverBootstrap = new ServerBootstrap();
        ChannelFuture bind = serverBootstrap
                .group(nioEventLoopGroup)
                .channel(NioServerSocketChannel.class)
                .childHandler(new ChannelInitializer<NioSocketChannel>() {
                    @Override
                    protected void initChannel(NioSocketChannel ch) throws Exception {
                        ChannelPipeline pipeline = ch.pipeline();
                        pipeline.addLast(new Handler());
                    }
                }).bind(9999);

//        bind.sync();
//        bind.channel().closeFuture().sync();
    }

    class AcceptHandler extends ChannelInboundHandlerAdapter {

        EventLoopGroup selector;

        public AcceptHandler(EventLoopGroup selector) {
            this.selector = selector;
        }

        @Override
        public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
            NioSocketChannel client = (NioSocketChannel) msg;
            ChannelPipeline pipeline = client.pipeline();
            pipeline.addLast(new Handler());
            selector.register(client);
        }

        @Override
        public void channelActive(ChannelHandlerContext ctx) throws Exception {
            System.out.println("channelActive");
        }

        @Override
        public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
            System.out.println("channelRegistered");
        }
    }

    class Handler extends ChannelInboundHandlerAdapter {
        @Override
        public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
            System.out.println("channelRead");
            ByteBuf byteBuf = (ByteBuf) msg;
            int length = byteBuf.readableBytes();
            CharSequence charSequence = byteBuf.readCharSequence(length, CharsetUtil.UTF_8);
            System.out.println(charSequence.toString());
            ctx.writeAndFlush(Unpooled.copiedBuffer("收到。。", CharsetUtil.UTF_8));
        }

        @Override
        public void channelActive(ChannelHandlerContext ctx) throws Exception {
            System.out.println("channelActive");
        }

        @Override
        public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
            System.out.println("channelRegistered");
        }
    }
}
