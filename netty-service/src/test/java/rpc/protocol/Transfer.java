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
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public class Transfer {

    Map<Long, CompletableFuture<Body>> futureMap = Maps.newConcurrentMap();

    volatile ChannelFuture channelFuture;

    public Channel getChannel() {
        if (channelFuture != null) {
            return channelFuture.channel();
        }
        synchronized (this) {
            if (channelFuture != null) {
                return channelFuture.channel();
            }
            Bootstrap bootstrap = new Bootstrap();
            NioEventLoopGroup eventExecutors = new NioEventLoopGroup(1);
            channelFuture = bootstrap.group(eventExecutors)
                    .channel(NioSocketChannel.class)
                    .handler(new ChannelInitializer<NioSocketChannel>() {
                        @Override
                        protected void initChannel(NioSocketChannel ch) throws Exception {
                            ChannelPipeline pipeline = ch.pipeline();
                            pipeline.addLast(new Decode());
                            pipeline.addLast(new ChannelInboundHandlerAdapter() {
                                @Override
                                public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
                                    Response response = (Response) msg;
                                    futureMap.get(response.getHead().getId()).complete(response.getBody());
                                }
                            });
                        }
                    })
                    .connect(new InetSocketAddress(9999));
        }
        return channelFuture.channel();
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
        Channel channel = getChannel();
        channel.close();
//        channel.closeFuture().syncUninterruptibly();
    }
}
