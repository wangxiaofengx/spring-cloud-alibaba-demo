package rpc.client;

import rpc.api.CarService;
import rpc.bo.Car;
import rpc.protocol.Transfer;
import rpc.protocol.request.Body;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Client {

    Transfer transfer = new Transfer();

    public static void main(String[] args) throws InterruptedException {
        Client client = new Client();
        client.run();
    }

    public void run() throws InterruptedException {
        CarService carService = getCarService();
        ExecutorService executorService = Executors.newFixedThreadPool(10);
        LocalDateTime begin = LocalDateTime.now();
        int count = 10;
        CountDownLatch countDownLatch = new CountDownLatch(count);
        for (int i = 0; i < count; i++) {
            executorService.execute(() -> {
                try {
//                    List<Long> collect = carService.list().stream().map(Car::getId).collect(Collectors.toList());
//                    System.out.println(collect);
                    Car car = carService.findById(1l);
                    System.out.println(car);
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    countDownLatch.countDown();
                }
            });
        }
        countDownLatch.await();
        LocalDateTime end = LocalDateTime.now();
        Duration between = Duration.between(begin, end);

        transfer.destroy();
        executorService.shutdown();
        System.out.println(between.toMillis());
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

                rpc.protocol.response.Body result = transfer.send(body);
                return result.getResult();
            }
        });
    }
}
