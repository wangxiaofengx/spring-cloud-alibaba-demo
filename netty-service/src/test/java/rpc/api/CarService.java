package rpc.api;

import rpc.bo.Car;

import java.util.List;

public interface CarService {

    List<Car> list();

    Car findById(Long id);
}
