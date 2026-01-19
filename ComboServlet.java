package controller;


import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import services.ComboService;

import java.io.IOException;

@WebServlet("/combo")
public class ComboServlet extends HttpServlet {
    private ComboService comboService;

    @Override
    public void init() throws ServletException {
        comboService = new ComboService();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String numberOfPeopleParam = request.getParameter("people");

        if (numberOfPeopleParam != null && !numberOfPeopleParam.isEmpty()) {
            try {
                int numberOfPeople = Integer.parseInt(numberOfPeopleParam);
                request.setAttribute("combos", comboService.getSuitableCombos(numberOfPeople));
                request.setAttribute("selectedPeople", numberOfPeople);
            } catch (NumberFormatException e) {
                request.setAttribute("combos", comboService.getAllCombos());
            }
        } else {
            request.setAttribute("combos", comboService.getAllCombos());
        }

        request.getRequestDispatcher("/WEB-INF/views/combo.jsp").forward(request, response);
    }
}

