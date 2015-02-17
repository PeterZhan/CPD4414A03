/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package CPD4414.A03.Svlt;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author Hongliang Zhang
 */
@WebServlet(name = "ProductServlet", urlPatterns = {"/products"})
public class ProductServlet extends HttpServlet {

    private Connection connection = null;

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
        try (PrintWriter out = response.getWriter()) {
            /* TODO output your page here. You may use following sample code. */
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet ProductServlet</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet ProductServlet at " + request.getContextPath() + "</h1>");
            out.println("</body>");
            out.println("</html>");
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String productid = request.getParameter("id");
     //   productid = request.getServletPath();
        try {
            PrintWriter out = response.getWriter();
            // processRequest(request, response);
            String query = "SELECT * FROM product";

            if (productid != null && !productid.equals("")) {
                query = query + " where ProductID='" + productid + "'";
            }

            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            System.out.println(query);
            String outStr = "[";
            while (rs.next()) {
                Product p = new Product(rs.getInt("ProductID"), rs.getString("Name"), rs.getString("Description"), rs.getInt("Quantity"));
                outStr += JsonHelper.toJSON(p) + ",";

            }

            if (outStr.endsWith(",")) {
                outStr = outStr.substring(0, outStr.length() - 1);
            }

            outStr += "]";

            out.println(outStr);
        } catch (SQLException ex) {
            Logger.getLogger(ProductServlet.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            //processRequest(request, response);
            String name = request.getParameter("name");
            String description = request.getParameter("description");
            int quantity = Integer.parseInt(request.getParameter("quantity"));

            String query = "INSERT INTO product(name,description,quantity) VALUES(?,?,?)";
            PreparedStatement pstmt = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);

            System.out.println(query);

            pstmt.setString(1, name);
            pstmt.setString(2, description);
            pstmt.setInt(3, quantity);
            int updates = pstmt.executeUpdate();
            if (updates <= 0) {
                response.setStatus(500);
            } else {

                int autoIncKeyFromApi = -1;

                ResultSet rs = pstmt.getGeneratedKeys();

                if (rs.next()) {
                    autoIncKeyFromApi = rs.getInt(1);
                    response.sendRedirect("http://localhost:8080/webServletA03/products?id=" + autoIncKeyFromApi);
                } else {

                    response.setStatus(500);

                }

            }

        } catch (SQLException ex) {
            response.setStatus(500);
            Logger.getLogger(ProductServlet.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

      //  super.doDelete(req, resp); //To change body of generated methods, choose Tools | Templates.
      try {
          //  super.doPut(req, resp); //To change body of generated methods, choose Tools | Templates.

           
            int productid = Integer.parseInt(req.getParameter("id"));

            String query = "delete from product  WHERE ProductID = ?";
            PreparedStatement pstmt = connection.prepareStatement(query);
            System.out.println(query);

            pstmt.setInt(1, productid);
         
            int updates = pstmt.executeUpdate();

            if (updates <= 0) {
                System.out.println(updates);
                resp.setStatus(500);
            } else {

                PrintWriter out = resp.getWriter();        
                out.println("");

               

            }

       
    }
    catch (Exception ex) {
            Logger.getLogger(ProductServlet.class.getName()).log(Level.SEVERE, null, ex);
            ex.printStackTrace();
    }              
           
      
      
      
      
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
          //  super.doPut(req, resp); //To change body of generated methods, choose Tools | Templates.

            String name = req.getParameter("name");
            System.out.println(req.getParameter("description"));
            String description = req.getParameter("description");
            System.out.println(req.getParameter("quantity"));
            int quantity = Integer.parseInt(req.getParameter("quantity"));
            int productid = Integer.parseInt(req.getParameter("id"));

            String query = "UPDATE product SET name = ?, description= ?, quantity=? WHERE ProductID = ?";
            PreparedStatement pstmt = connection.prepareStatement(query);
            System.out.println(query);

            pstmt.setString(1, name);
            pstmt.setString(2, description);
            pstmt.setInt(3, quantity);
            pstmt.setInt(4, productid);
            int updates = pstmt.executeUpdate();

            if (updates <= 0) {
                System.out.println(updates);
                resp.setStatus(500);
            } else {

               
               resp.sendRedirect("http://localhost:8080/webServletA03/products?id=" + productid);
               

               

            }

       
    }
    catch (Exception ex) {
            Logger.getLogger(ProductServlet.class.getName()).log(Level.SEVERE, null, ex);
            ex.printStackTrace();
    }

}

/**
 * Returns a short description of the servlet.
 *
 * @return a String containing servlet description
 */
@Override
        public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

    
    @Override
        public void init(ServletConfig config) throws ServletException {
        connection = getConnection();
        

    }
    
    @Override
        public void destroy(){
      if (connection != null)
          try {
              connection.close();
              connection = null;
      

} catch (SQLException ex) {
          Logger.getLogger(ProductServlet.class  

.getName()).log(Level.SEVERE, null, ex);
      }
   }
    
    private Connection getConnection() {
        Connection connection = null;
        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            System.out.println("Class not found exception! " + e.getMessage());
        }

        String url = "jdbc:mysql://localhost:3306/cpd4414assign3";
        try {
            connection = DriverManager.getConnection(url,
                    "usertest", "123456");            
        } catch (SQLException e) {
            System.out.println("Failed to Connect! " + e.getMessage());
            e.printStackTrace();
        }
        return connection;
    }
    
    
    private void doStatement() {
        try {            
           
            String query = "SELECT * FROM sample";        
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            while (rs.next()) {
                System.out.printf("%s\t%s\n", rs.getString("name"), rs.getString("age"));
            }        
            
        } catch (SQLException e) {
            System.err.println("Error SELECTing: " + e.getMessage());
        }         
    }
    
    private void doPreparedStatement() {
        try {            
            String query = "SELECT * FROM sample WHERE id = ?";        
            PreparedStatement pstmt = connection.prepareStatement(query);
            int[] idList = {1, 3};
            for (int id : idList) {
                pstmt.setInt(1, id);
                ResultSet rs = pstmt.executeQuery();
                while (rs.next()) {
                    System.out.printf("%s\t%s\n", rs.getString("name"), rs.getString("age"));
                }        
            }
         
        } catch (SQLException e) {
            System.err.println("Error SELECTing: " + e.getMessage());
        }         
    }
   /* 
    public static void doCRUDExample() {
        try {            
            Connection conn = getConnection();
            // Our Dataset
            List<Person> people = new ArrayList<>();
            people.add(new Person("John", 32));
            people.add(new Person("Frank", 29));
            people.add(new Person("Susie", 37));            
            
            // Create!
            String query = "INSERT INTO sample (name, age) VALUES (?, ?)";
            PreparedStatement pstmt = conn.prepareStatement(query);
            System.out.println(query);
            for (Person person : people) {
                pstmt.setString(1, person.getName());
                pstmt.setInt(2, person.getAge());
                int updates = pstmt.executeUpdate();
                System.out.printf("Updated %d rows with %s/%d.\n", updates, 
                        person.getName(), person.getAge());                
            }
            
            // Read!
            query = "SELECT * FROM sample";        
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            System.out.println(query);
            while (rs.next()) {
                System.out.printf("%s\t%s\n", rs.getString("name"), rs.getString("age"));
            }        
            
            // Update!
            query = "UPDATE sample SET age = ? WHERE name = ?";
            pstmt = conn.prepareStatement(query);
            System.out.println(query);
            for (Person person : people) {
                pstmt.setInt(1, person.getAge() + 1);
                pstmt.setString(2, person.getName());                
                int updates = pstmt.executeUpdate();
                System.out.printf("Updated %d rows with %s/%d.\n", updates, 
                        person.getName(), person.getAge());                
            }
            
            // Read Again!
            query = "SELECT * FROM sample";        
            stmt = conn.createStatement();
            rs = stmt.executeQuery(query);
            System.out.println(query);
            while (rs.next()) {
                System.out.printf("%s\t%s\n", rs.getString("name"), rs.getString("age"));
            }
            
            // DELETE!
            query = "DELETE FROM sample WHERE name = ?";
            pstmt = conn.prepareStatement(query);
            System.out.println(query);
            for (Person person : people) {
                pstmt.setString(1, person.getName());
                int updates = pstmt.executeUpdate();
                System.out.printf("Updated %d rows with %s.\n", updates, 
                        person.getName());                
            }
            
            // Read Again!
            query = "SELECT * FROM sample";        
            stmt = conn.createStatement();
            rs = stmt.executeQuery(query);
            System.out.println(query);
            while (rs.next()) {
                System.out.printf("%s\t%s\n", rs.getString("name"), rs.getString("age"));
            }
            
            conn.close();
        } catch (SQLException e) {
            System.err.println("Error with SQL: " + e.getMessage());
        }  
    }
    */
    
}
