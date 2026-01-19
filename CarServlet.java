package controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import services.CarTypeService;

import java.io.IOException;


@WebServlet("/cars")
class CarServlet extends HttpServlet {
    private CarTypeService carTypeService;

    @Override
    public void init() throws ServletException {
        carTypeService = new CarTypeService();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String seatsParam = request.getParameter("seats");

        if (seatsParam != null && !seatsParam.isEmpty()) {
            try {
                int seats = Integer.parseInt(seatsParam);
                request.setAttribute("carTypes", carTypeService.getCarTypesBySeats(seats));
                request.setAttribute("selectedSeats", seats);
            } catch (NumberFormatException e) {
                request.setAttribute("carTypes", carTypeService.getAllCarTypes());
            }
        } else {
            request.setAttribute("carTypes", carTypeService.getAllCarTypes());
        }

        request.getRequestDispatcher("/WEB-INF/views/car.jsp").forward(request, response);
    }
}

