package services;

import dao.CarTypeDAO;
import model.CarType;

import java.util.List;

class CarTypeService {
    private CarTypeDAO carTypeDAO;

    public CarTypeService() {
        this.carTypeDAO = new CarTypeDAO();
    }

    /**
     * Lấy tất cả loại xe
     */
    public List<CarType> getAllCarTypes() {
        return carTypeDAO.getAllCarTypes();
    }

    /**
     * Lấy loại xe theo số chỗ
     */
    public List<CarType> getCarTypesBySeats(int seats) {
        return carTypeDAO.getCarTypesBySeats(seats);
    }

    /**
     * Lấy loại xe theo ID
     */
    public CarType getCarTypeById(int carTypeId) {
        return carTypeDAO.getCarTypeById(carTypeId);
    }

   
}