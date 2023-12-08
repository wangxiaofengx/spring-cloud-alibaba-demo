package rpc.server.service;

import rpc.bo.Car;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class CarService implements rpc.api.CarService {
    @Override
    public List<Car> list() {

        Car car1 = new Car().setId(UUID.randomUUID().getMostSignificantBits()).setName("小汽车");
        Car car2 = new Car().setId(UUID.randomUUID().getMostSignificantBits()).setName("大卡车");
        Car car3 = new Car().setId(UUID.randomUUID().getMostSignificantBits()).setName("公交车");

        List<Car> cars = new ArrayList<>();
        cars.add(car1);
        cars.add(car2);
        cars.add(car3);
//        try {
//            Thread.sleep(10);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
        return cars;
    }

    @Override
    public Car findById(Long id) {
        return new Car().setId(id).setName("汽车");
    }
}
