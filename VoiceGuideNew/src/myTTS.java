
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

import org.json.JSONObject;

public class myTTS {

  //private static final String serverURL = "http://vop.baidu.com/server_api";
    private static final String serverURL = "http://tsn.baidu.com/text2audio";
    private static String token = "";
    private static final String testFileName = "test.pcm";
    
    //put your own params here
    private static final String apiKey = "xwVCTeT1oNRpEYx92sHaTKkm";
    private static final String secretKey = "79855d53626b9e23112356cb768e1584";
    private static final String cuid = "ac:bc:32:96:3b:d5";

    //myTTS
    //public static String text;
    
    public static void main(String[] args) throws Exception {
    	String str = " 你好，请问您需要办理什么业务啊？";
    	/*
    	getToken();
    	 String tex =  urlencode(str) ;
    	 String lan =  urlencode("zh");
    	 String tok =  urlencode(token);
    	 String ctp =  urlencode("1");
    	 String cui=  urlencode(cuid);
    	String sURL=serverURL
    			    +"?tex="+tex
    			    +"&lan="+lan
    			    +"&tok="+tok
    			    +"&ctp="+ctp
    			    +"&cuid="+cui;
    	System.out.print(sURL);
    	*/
    	String recvStr = get(str,"myTTS.mp3") ;
    	System.out.print("\n");
    	System.out.print(recvStr);
    }

    private static void getToken() throws Exception {
        String getTokenURL = "https://openapi.baidu.com/oauth/2.0/token?grant_type=client_credentials" + 
            "&client_id=" + apiKey + "&client_secret=" + secretKey;
        HttpURLConnection conn = (HttpURLConnection) new URL(getTokenURL).openConnection();
        token = new JSONObject(printResponse(conn)).getString("access_token");
    }
    //发送一个GET请求
    public static String get(String str,String path) throws Exception{
    	
    	getToken();
   	    String tex =  urlencode(str) ;
   	    String lan =  urlencode("zh");
   	    String tok =  urlencode(token);
   	    String ctp =  urlencode("1");
   	    String cui=  urlencode(cuid);
   	    String sURL=serverURL
   			    +"?tex="+tex
   			    +"&lan="+lan
   			    +"&tok="+tok
   			    +"&ctp="+ctp
   			    +"&cuid="+cui;
   	    System.out.print(sURL);
   	
    	  HttpURLConnection httpConn=null;
    	  /*
    	   * BufferedReader in=null;
    	   */
    	  InputStream in=null;
    	  FileOutputStream f = null;
    	  try {
    	   URL url=new URL(sURL);
    	   httpConn=(HttpURLConnection)url.openConnection();
    	   //读取响应
    	   
    	   if(httpConn.getResponseCode()==HttpURLConnection.HTTP_OK){
    	   /*
    	    StringBuffer content=new StringBuffer();
    	    String tempStr="";
    	    in=new BufferedReader(new InputStreamReader(httpConn.getInputStream()));
    	    while((tempStr=in.readLine())!=null){
    	     content.append(tempStr);
    	     System.out.print("+1");
    	    }
    	    return content.toString();
    	    */
    		in=httpConn.getInputStream();
    	    f = new FileOutputStream(path);//创建文件输出流
    	    byte [] bb=new byte[1024];  //接收缓存
    	    int len;
    	    while( (len=in.read(bb))>0){ //接收
    	          f.write(bb, 0, len);  //写入文件
    	    }
    		return new String("succeed");
    	   }else{
    	    throw new Exception("请求出现了问题!");
    	   }
    	  } catch (IOException e) {
    	   e.printStackTrace();
    	  }finally{
    	   in.close();
    	   f.close();
    	   httpConn.disconnect();
    	  }
    	  return null;
    }
    //发送一个GET请求,参数形式key1=value1&key2=value2...
    public static String post(String path,String params) throws Exception{
    	  HttpURLConnection httpConn=null;
    	  BufferedReader in=null;
    	  PrintWriter out=null;
    	  try {
    	   URL url=new URL(path);
    	   httpConn=(HttpURLConnection)url.openConnection();
    	   httpConn.setRequestMethod("POST");
    	   httpConn.setDoInput(true);
    	   httpConn.setDoOutput(true);
    	   //发送post请求参数
    	   out=new PrintWriter(httpConn.getOutputStream());
    	   out.println(params);
    	   out.flush();
    	   //读取响应
    	   if(httpConn.getResponseCode()==HttpURLConnection.HTTP_OK){
    	    StringBuffer content=new StringBuffer();
    	    String tempStr="";
    	    in=new BufferedReader(new InputStreamReader(httpConn.getInputStream()));
    	    while((tempStr=in.readLine())!=null){
    	     content.append(tempStr);
    	    }
    	    return content.toString();
    	   }else{
    	    throw new Exception("请求出现了问题!");
    	   }
    	  } catch (IOException e) {
    	   e.printStackTrace();
    	  }finally{
    	   in.close();
    	   out.close();
    	   httpConn.disconnect();
    	  }
    	  return null;
    }
    
    private static String urlencode(String str){
    	try {
			//return URLEncoder.encode(str,"UTF-8" );
    		return URLEncoder.encode( URLEncoder.encode(str,"UTF-8" ) ,"UTF-8" );
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	return null;
    }
    
    private static String printResponse(HttpURLConnection conn) throws Exception {
        if (conn.getResponseCode() != 200) {
            // request error
            return "";
        }
        InputStream is = conn.getInputStream();
        BufferedReader rd = new BufferedReader(new InputStreamReader(is));
        String line;
        StringBuffer response = new StringBuffer();
        while ((line = rd.readLine()) != null) {
            response.append(line);
            response.append('\r');
        }
        rd.close();
        System.out.println(new JSONObject(response.toString()).toString(4));
        return response.toString();
    }
    /*
    private static byte[] loadFile(File file) throws IOException {
        InputStream is = new FileInputStream(file);

        long length = file.length();
        byte[] bytes = new byte[(int) length];

        int offset = 0;
        int numRead = 0;
        while (offset < bytes.length
                && (numRead = is.read(bytes, offset, bytes.length - offset)) >= 0) {
            offset += numRead;
        }

        if (offset < bytes.length) {
            is.close();
            throw new IOException("Could not completely read file " + file.getName());
        }

        is.close();
        return bytes;
    }
    */
}
