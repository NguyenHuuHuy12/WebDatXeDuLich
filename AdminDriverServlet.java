package controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import services.DriverService;

import java.io.IOException;


@WebServlet("/admin/drivers")
class AdminDriverServlet extends HttpServlet {
    private DriverService driverService;

    @Override
    public void init() throws ServletException {
        driverService = new DriverService();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setAttribute("drivers", driverService.getAllDrivers());
        request.getRequestDispatcher("/WEB-INF/views/admin/drivers.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        String action = request.getParameter("action");

        if ("add".equals(action)) {
            String fullName = request.getParameter("fullName");
            String phone = request.getParameter("phone");
            String licenseNumber = request.getParameter("licenseNumber");
            int experienceYears = Integer.parseInt(request.getParameter("experienceYears"));

            String result = driverService.addDriver(fullName, phone, licenseNumber, experienceYears);

            if ("success".equals(result)) {
                request.setAttribute("success", "Thêm tài xế thành công");
            } else {
                request.setAttribute("error", result);
            }

        } else if ("delete".equals(action)) {
            int driverId = Integer.parseInt(request.getParameter("driverId"));
            driverService.deleteDriver(driverId);
        }

        doGet(request, response);
    }
}



