package controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import services.ComboService;

import java.io.IOException;

@WebServlet("/admin/combos")
class AdminComboServlet extends HttpServlet {
    private ComboService comboService;

    @Override
    public void init() throws ServletException {
        comboService = new ComboService();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setAttribute("combos", comboService.getAllCombos());
        request.getRequestDispatcher("/WEB-INF/views/admin/combos.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        String action = request.getParameter("action");

        if ("delete".equals(action)) {
            int comboId = Integer.parseInt(request.getParameter("comboId"));
            comboService.deleteCombo(comboId);
        }

        doGet(request, response);
    }
}
