package filter;

import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.servlet.DispatcherType;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import com.jdnull.speechRec.baiduAPI.Recognizer;

@WebFilter(urlPatterns={"/engine/*"}, asyncSupported=true, dispatcherTypes={DispatcherType.REQUEST}
         )  

/**
 * 
 * @将声音转换为文本
 *
 */
public class Voice2StrFilter implements Filter {  
   
   @Override  
   public void destroy() {  
      System.out.println("destory filter……");  
   }  
   
   @Override  
   public void doFilter(ServletRequest request, ServletResponse response,  
         FilterChain chain) throws IOException, ServletException {  
      System.out.println(" Voice2StrFilter filter……");  
      
      DiskFileItemFactory factory = new DiskFileItemFactory();  
      
      // 如果没以下两行设置的话，上传大的文件会占用很多内存，  
      // 设置暂时存放的存储室 ,这个存储室,可以和最终存储文件的目录不同  
      /** 
       * 原理 它是先存到 暂时存储室，然后在真正写到 对应目录的硬盘上， 按理来说 当上传一个文件时，其实是上传了两份，第一个是以 .tmp 
       * 格式的 然后再将其真正写到 对应目录的硬盘上 
       */  
      factory.setRepository(new File("voice.wav"));  
      // 设置 缓存的大小，当上传文件的容量超过该缓存时，直接放到 暂时存储室  
      factory.setSizeThreshold(1024 * 1024);  

      // 高水平的API文件上传处理  
      ServletFileUpload upload = new ServletFileUpload(factory);  
        
      try {  
          // 提交上来的信息都在这个list里面  
          // 这意味着可以上传多个文件  
          // 请自行组织代码  
          List<FileItem> list = upload.parseRequest((HttpServletRequest) request);  
          // 获取上传的文件  
          FileItem item = list.get(0);  
          // 真正写到磁盘上  
          item.write(new File("voice.wav")); // 第三方提供的  
                              // 输出信息,前端页面获取,这里用的json格式             
        
      } catch (FileUploadException e) {  
          e.printStackTrace();  
      } catch (Exception e) {  
          e.printStackTrace();  
      } 
      chain.doFilter(request, response);  
      
      Recognizer rec = new Recognizer();
      try {
		request.setAttribute("plainStr", rec.recognize("voice.wav"));
	} catch (Exception e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
   }  
   
   @Override  
   public void init(FilterConfig filterConfig) throws ServletException {  
    //  String param1 = filterConfig.getInitParameter("param1");  
   }  
   
} 