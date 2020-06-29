/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package view;

import common.FileUtility;
import entity.Account;
import entity.Image;
import logic.AccountLogic;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import logic.AccountLogic;
import logic.ImageLogic;
import logic.BoardLogic;
import logic.HostLogic;
import logic.LogicFactory;

/**
 *
 * @author Khosla
 */

@WebServlet(name = "ImageDelivery", urlPatterns = {"/image/*"})
public class ImageDelivery extends HttpServlet {
    
    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
    }
    
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param req  servlet request
     * @param resp servlet response
     *
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException      if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        log("GET");
        doPost(req, resp);
    }
    
    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param req  servlet request
     * @param resp servlet response
     *
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException      if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        log("POST");   
    }
    
    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Deliver Images To Browser.";
    }

    private static final boolean DEBUG = true;

    public void log( String msg) {
        if(DEBUG){
            String message = String.format( "[%s] %s", getClass().getSimpleName(), msg);
            getServletContext().log( message);
        }
    }

    public void log( String msg, Throwable t) {
        String message = String.format( "[%s] %s", getClass().getSimpleName(), msg);
        getServletContext().log( message, t);
    }
    
}
