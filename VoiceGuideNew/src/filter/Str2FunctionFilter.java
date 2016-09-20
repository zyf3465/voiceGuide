package filter;

import java.io.IOException;

import javax.servlet.DispatcherType;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;

@WebFilter(urlPatterns={"/engine/*"}, asyncSupported=true, dispatcherTypes={DispatcherType.REQUEST}
         )  

/**
 * 
 * @将声音转换为文本
 *
 */
public class Str2FunctionFilter implements Filter {  
   
   @Override  
   public void destroy() {  
      System.out.println("destory filter……");  
   }  
   
   @Override  
   public void doFilter(ServletRequest request, ServletResponse response,  
         FilterChain chain) throws IOException, ServletException {  
      System.out.println("Str2FunctionFilter filter……");  
      
      String plainStr = (String) request.getAttribute("plainStr");
      
      
      
   }  
   
   @Override  
   public void init(FilterConfig filterConfig) throws ServletException {  
    //  String param1 = filterConfig.getInitParameter("param1");  
   }  
   
} 