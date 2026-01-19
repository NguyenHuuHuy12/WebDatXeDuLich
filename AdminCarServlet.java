package controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.CarType;
import services.CarTypeService;

import java.io.IOException;

@WebServlet("/admin/cars")
class AdminCarServlet extends HttpServlet {
    private CarTypeService carTypeService;

    @Override
    public void init() throws ServletException {
        carTypeService = new CarTypeService();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setAttribute("carTypes", carTypeService.getAllCarTypes());
        request.getRequestDispatcher("/WEB-INF/views/admin/cars.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        String action = request.getParameter("action");

        if ("add".equals(action)) {
            CarType carType = new CarType();
            carType.setName(request.getParameter("name"));
            carType.setSeats(Integer.parseInt(request.getParameter("seats")));
            carType.setImageUrl(request.getParameter("imageUrl"));
            carType.setDescription(request.getParameter("description"));
            carType.setPricePerKm(Double.parseDouble(request.getParameter("pricePerKm")));

            String result = carTypeService.addCarType(carType);

            if ("success".equals(result)) {
                request.setAttribute("success", "Thêm loại xe thành công");
            } else {
                request.setAttribute("error", result);
            }

        } else if ("delete".equals(action)) {
            int carTypeId = Integer.parseInt(request.getParameter("carTypeId"));
            carTypeService.deleteCarType(carTypeId);
        }

        doGet(request, response);
    }
}
