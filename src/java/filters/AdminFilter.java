/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package filters;

import dataaccess.NotesDBException;
import dataaccess.UserDB;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 *
 * @author 725899
 */
@WebFilter(filterName = "AdminFilter", servletNames = {"UserServlet"})
public class AdminFilter implements Filter {
    
   private FilterConfig filterConfig = null;
    
    @Override
    public void doFilter(ServletRequest request, ServletResponse response,
            FilterChain chain)
            throws IOException, ServletException {
        
        // this code executes before the servlet
        // ...
        
        // ensuring user admin
        HttpSession session = ((HttpServletRequest)request).getSession();
        String username = (String)session.getAttribute("username");
        UserDB db = new UserDB();
        
       try {
           if (db.getUser(username).getRole().getRoleID() == 1) {              
                // yes, go onwards to the servlet or next filter
                chain.doFilter(request, response);
                return;
           }
           else {
                // user is not admin, punish with throwing back to login!
                session.invalidate();
               ((HttpServletResponse)response).sendRedirect("login");
               return;
           }
       } catch (NotesDBException ex) {
           // handle bad stuff.
       }
        
       // this code executes after the servlet
       // ...
            
    }

    @Override
    public void destroy() {        
    }

    @Override
    public void init(FilterConfig filterConfig) {        
        this.filterConfig = filterConfig;
    }
}
