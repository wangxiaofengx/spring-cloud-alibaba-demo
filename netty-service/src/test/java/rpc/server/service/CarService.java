package rpc.server.service;

import rpc.bo.Car;
import rpc.bo.User;

import java.util.ArrayList;
import java.util.List;

public class CarService implements rpc.api.CarService {
    @Override
    public List<Car> list() {

        Car car1 = new Car().setId(1l).setName("小汽车");
        Car car2 = new Car().setId(2l).setName("大卡车");
        Car car3 = new Car().setId(3l).setName("公交车");

        List<Car> cars = new ArrayList<>();
        cars.add(car1);
        cars.add(car2);
        cars.add(car3);
        return cars;
    }
}
