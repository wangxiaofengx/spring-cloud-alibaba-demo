package rpc.protocol;

import com.google.common.collect.Maps;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import rpc.protocol.request.Head;
import rpc.protocol.response.Body;
import rpc.protocol.response.Decode;
import rpc.protocol.response.Response;
import rpc.util.SerializableUtil;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.*;
import java.util.concurrent.CompletableFuture;

public class Transfer {

    Map<Long, CompletableFuture<Body>> futureMap = Maps.newConcurrentMap();
    int pollSize = 2;
    Channel[] channels = new Channel[pollSize];
    Random random = new Random();
    NioEventLoopGroup eventExecutors = new NioEventLoopGroup(5);

    public Channel getChannel() {
        int index = random.nextInt(pollSize);
        Channel channel = channels[index];
        if (channel != null) {
            return channel;
        }
        synchronized (this) {
            channel = channels[index];
            if (channel != null) {
                return channel;
            }
            Bootstrap bootstrap = new Bootstrap();
            Channel newChannel = bootstrap.group(eventExecutors)
                    .channel(NioSocketChannel.class)
                    .handler(new ChannelInitializer<NioSocketChannel>() {
                        @Override
                        protected void initChannel(NioSocketChannel ch) throws Exception {
                            ChannelPipeline pipeline = ch.pipeline();
                            pipeline.addLast(new Decode());
                            pipeline.addLast(new ChannelInboundHandlerAdapter() {
                                @Override
                                public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
                                    ctx.executor().execute(() -> {
                                        System.out.println("channelRead:" + Thread.currentThread().getName());
                                        Response response = (Response) msg;
                                        CompletableFuture<Body> bodyCompletableFuture = futureMap.get(response.getHead().getId());
                                        bodyCompletableFuture.complete(response.getBody());
                                        futureMap.remove(response.getHead().getId());
                                    });
                                }
                            });
                        }
                    })
                    .connect(new InetSocketAddress(9999)).channel();
            channels[index] = newChannel;
        }
        return channels[index];
    }

    public rpc.protocol.response.Body send(rpc.protocol.request.Body body) throws IOException {
        Channel channel = getChannel();

        byte[] bodyBytes = SerializableUtil.toBytes(body);
        Head head = new Head();
        head.setId(UUID.randomUUID().getLeastSignificantBits());
        head.setBodyLength(bodyBytes.length);
        byte[] headBytes = SerializableUtil.toBytes(head);

        CompletableFuture<rpc.protocol.response.Body> objectCompletableFuture = new CompletableFuture<>();
        futureMap.put(head.getId(), objectCompletableFuture);

        ByteBuf buffer = PooledByteBufAllocator.DEFAULT.directBuffer(headBytes.length + bodyBytes.length);
        buffer.writeBytes(headBytes);
        buffer.writeBytes(bodyBytes);
        channel.writeAndFlush(buffer);

        return objectCompletableFuture.join();
    }

    public void destroy() {
//        eventExecutors.shutdownGracefully();
        for (int i = 0; i < channels.length; i++) {
            Channel channel = channels[i];
            if (channel != null) {
                channel.close();
//                channel.closeFuture().syncUninterruptibly();
            }
        }
    }
}
