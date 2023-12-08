package rpc.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import rpc.protocol.request.Body;
import rpc.protocol.request.Decode;
import rpc.protocol.request.Request;
import rpc.server.service.CarService;
import rpc.server.service.UserService;
import rpc.util.SerializableUtil;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class Server {

    static Map<String, Object> serviceMap = new HashMap<>();

    static {
        serviceMap.put(rpc.api.CarService.class.getName(), new CarService());
        serviceMap.put(rpc.api.UserService.class.getName(), new UserService());
    }

    public static void main(String[] args) throws IOException, InterruptedException {
        Server app = new Server();
        app.run();
    }

    public void run() throws InterruptedException {
        int port = 9999;
        ServerBootstrap server = new ServerBootstrap();
        NioEventLoopGroup nioEventLoopGroup = new NioEventLoopGroup(5);
        ChannelFuture bind = server.group(nioEventLoopGroup)
                .channel(NioServerSocketChannel.class)
                .childHandler(new ChannelInitializer<NioSocketChannel>() {
                    @Override
                    protected void initChannel(NioSocketChannel ch) throws Exception {
                        ChannelPipeline pipeline = ch.pipeline();
                        pipeline.addLast(new Decode());
                        pipeline.addLast(new Handler());
                    }
                })
                .bind(port);
        System.out.println("server已启动，端口：" + port);
        bind.channel().closeFuture().sync();
    }

    class Handler extends ChannelInboundHandlerAdapter {
        public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
            Request request = (Request) msg;
            Body body = request.getBody();
            String name = body.getName();
            Object o = serviceMap.get(name);
            Method method = o.getClass().getMethod(body.getMethod(), body.getParameterTypes());
            String readThread = Thread.currentThread().getName();
            Channel client = ctx.channel();
            System.out.println("client remoteAddress:" + client.remoteAddress());
            ctx.executor().parent().next().execute(() -> {
//            ctx.executor().execute(() -> {
                try {
                    String exeThread = Thread.currentThread().getName();
//                    System.out.println("readThread:" + readThread + ",exeThread:" + exeThread);
                    Object result = method.invoke(o, body.getArgs());
                    rpc.protocol.response.Body responseBody = new rpc.protocol.response.Body();
                    responseBody.setCode(1).setResult(result);

                    byte[] responseBodyBytes = SerializableUtil.toBytes(responseBody);
                    rpc.protocol.response.Head responseHead = new rpc.protocol.response.Head();
                    responseHead.setId(request.getHead().getId()).setBodyLength(responseBodyBytes.length);
                    byte[] responseHeadBytes = SerializableUtil.toBytes(responseHead);

                    ByteBuf buffer = PooledByteBufAllocator.DEFAULT.directBuffer(responseHeadBytes.length + responseBodyBytes.length);
                    buffer.writeBytes(responseHeadBytes);
                    buffer.writeBytes(responseBodyBytes);
                    ctx.writeAndFlush(buffer);
                } catch (IllegalAccessException e) {
                    throw new RuntimeException(e);
                } catch (InvocationTargetException e) {
                    throw new RuntimeException(e);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });
        }
    }
}
