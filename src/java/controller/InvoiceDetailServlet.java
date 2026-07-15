package controller;

import dao.InvoiceDetailDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;

import model.InvoiceDetail;

@WebServlet("/invoice-detail")
public class InvoiceDetailServlet extends HttpServlet{

    InvoiceDetailDAO dao=new InvoiceDetailDAO();

    @Override
    protected void doGet(HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException,IOException{

        String action=request.getParameter("action");

        if(action==null){

            action="add";

        }

        switch(action){

            case "add":

                request.setAttribute(
                "invoiceId",
                request.getParameter("id"));

                request.getRequestDispatcher(
                "/view/addInvoiceDetail.jsp")
                .forward(request,response);

                break;

            case "edit":

                // lát làm

                break;

            case "delete":

                int detailId=
                Integer.parseInt(request.getParameter("detailId"));

                int invoiceId=
                Integer.parseInt(request.getParameter("invoiceId"));

                dao.delete(detailId,invoiceId);

                response.sendRedirect(
                request.getContextPath()
                +"/invoice-detail?action=add&id="
                +invoiceId);

                break;

        }

    }

    @Override
    protected void doPost(HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException,IOException{

        request.setCharacterEncoding("UTF-8");

        String action=request.getParameter("action");

        if(action.equals("insert")){

            InvoiceDetail d=
            new InvoiceDetail();

            d.setInvoiceId(
            Integer.parseInt(
            request.getParameter("invoiceId")));

            d.setItemName(
            request.getParameter("itemName"));

            d.setUnit(
            request.getParameter("unit"));

            d.setQuantity(
            Integer.parseInt(
            request.getParameter("quantity")));

            d.setUnitPrice(
            Double.parseDouble(
            request.getParameter("unitPrice")));

            dao.insert(d);

            response.sendRedirect(
            request.getContextPath()
            +"/invoice-detail?action=add&id="
            +d.getInvoiceId());

        }

    }

}