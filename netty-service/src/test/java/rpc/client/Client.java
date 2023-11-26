package rpc.client;

import com.google.common.collect.Maps;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import rpc.api.CarService;
import rpc.protocol.request.Body;
import rpc.protocol.request.Head;
import rpc.protocol.response.Decode;
import rpc.protocol.response.Response;
import rpc.util.SerializableUtil;

import java.io.IOException;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.net.InetSocketAddress;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Client {


    ClientTransfer clientTransfer = new ClientTransfer();

    public static void main(String[] args) throws InterruptedException {
        Client client = new Client();
        client.run();
    }

    public void run() throws InterruptedException {
        CarService carService = getCarService();
        ExecutorService executorService = Executors.newWorkStealingPool();
        LocalDateTime begin = LocalDateTime.now();
        int count = 1000;
        CountDownLatch countDownLatch = new CountDownLatch(count);
        for (int i = 0; i < count; i++) {
            executorService.execute(() -> {
                try {
                    carService.list();
                } finally {
                    countDownLatch.countDown();
                }
            });
        }
        countDownLatch.await();
        LocalDateTime end = LocalDateTime.now();
        Duration between = Duration.between(begin, end);
        System.out.println(between.toMillis());
        clientTransfer.destroy();
//        System.out.println(list);
    }

    public CarService getCarService() {
        return (CarService) Proxy.newProxyInstance(this.getClass().getClassLoader(), new Class[]{CarService.class}, new InvocationHandler() {
            @Override
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                Body body = new Body();
                body.setName(CarService.class.getName());
                body.setMethod(method.getName());
                body.setParameterTypes(args == null ? null : Arrays.stream(args).map(Object::getClass).toArray(Class[]::new));
                body.setArgs(args);

                rpc.protocol.response.Body result = clientTransfer.send(body);
                return result.getResult();
            }
        });
    }


    class ClientTransfer {

        Map<Long, CompletableFuture<rpc.protocol.response.Body>> futureMap = Maps.newConcurrentMap();

        volatile Channel client;

        public Channel getChannel() {
            if (client != null) {
                return client;
            }
            synchronized (this) {
                if (client != null) {
                    return client;
                }
                Bootstrap bootstrap = new Bootstrap();
                NioEventLoopGroup eventExecutors = new NioEventLoopGroup(1);
                client = bootstrap.group(eventExecutors)
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
                        .connect(new InetSocketAddress(9999)).channel();
            }
            return client;
        }

        public rpc.protocol.response.Body send(Body body) throws IOException {

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
            channel.close().syncUninterruptibly();
        }
    }

}
