package com.jdnull.speechRec.baiduAPI;

import java.io.*;
import java.net.HttpURLConnection;
//import java.net.InterfaceAddress;
import java.net.URL;

import javax.xml.bind.DatatypeConverter;

import org.json.JSONObject;

import com.jdnull.sound.utilities.*;


public class Recognizer {

    private static final String serverURL = "http://vop.baidu.com/server_api";
    private static String token = "";
    private static final String audioFromBroswer = "myRecording00";
    private static final String testFileName = audioFromBroswer+"-formatted.wav";
    //put your own params here
    private static final String apiKey = "LS1pRiw3Zanan9vnZ8c5otDf";
    private static final String secretKey = "7f5a4cc70969916379eba47e4694ce4b";
    private static final String cuid = "b8:e8:56:34:0f:b8"; // JD-Mac mac address

    public static void main(String[] args) throws Exception {
//        SampleRateConverter.convert("16000", audioFromBroswer+".wav", audioFromBroswer+"-formatted.wav");
//        getToken();
//        method1();
//        method2();
    	JSONObject jsonObject = new JSONObject();
    	jsonObject.put("task", "task1");
    	jsonObject.put("node", "1");
    	jsonObject.put("url", "http");
    	System.out.println(jsonObject.toString());
    }

    public String recognize(String filename) throws Exception {
        String formattedFilename = filename.replaceFirst(".wav", "-formatted.wav");
        SampleRateConverter.convert("16000", filename, formattedFilename);

        getToken();

        return method1(formattedFilename);
    }

//    public static void txtToBinary(String txtname) throws Exception {
//        File txtFile = new File(txtname);
//        BufferedReader reader = new BufferedReader(new FileReader(txtFile));
//        String tempStr = reader.readLine();
//        reader.close();
//
//        String[] tempStrArr = tempStr.split(",");
//
//        DataOutputStream writer = new DataOutputStream(new FileOutputStream(audioFromBroswer+".pcm"));
//        for (String s :
//                tempStrArr) {
//            if(!s.equals("")) {
//                writer.writeFloat(Float.parseFloat(s));
//            }
//        }
//        writer.close();
//    }
//

    private static void getToken() throws Exception {
        String getTokenURL = "https://openapi.baidu.com/oauth/2.0/token?grant_type=client_credentials" + 
            "&client_id=" + apiKey + "&client_secret=" + secretKey;
        HttpURLConnection conn = (HttpURLConnection) new URL(getTokenURL).openConnection();
        token = new JSONObject(printResponse(conn)).getString("access_token");
    }

    private static String method1(String formattedFilename) throws Exception {
        File pcmFile = new File(formattedFilename);
        HttpURLConnection conn = (HttpURLConnection) new URL(serverURL).openConnection();

        // construct params
        JSONObject params = new JSONObject();
        params.put("format", "wav");
        params.put("rate", 16000);
        params.put("channel", "1");
        params.put("token", token);
        params.put("cuid", cuid);
        params.put("len", pcmFile.length());
        params.put("speech", DatatypeConverter.printBase64Binary(loadFile(pcmFile)));

        // add request header
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "application/json; charset=utf-8");

        conn.setDoInput(true);
        conn.setDoOutput(true);

        // send request
        DataOutputStream wr = new DataOutputStream(conn.getOutputStream());
        wr.writeBytes(params.toString());
        wr.flush();
        wr.close();

        String baiduResult = "no result";
        BufferedReader rd = null ;
		try{
			if (conn.getResponseCode() != 200) {
				return "error";
			}
			InputStream is = conn.getInputStream();
			rd = new BufferedReader(new InputStreamReader(is));
			String line;
			while ((line = rd.readLine()) != null) {
				if(line.contains("\"result\":")){
					int begin =line.indexOf("\"result\":[");
					int end = line.indexOf("\"]", begin);
					
					baiduResult =  (line.substring(begin,end))
							.replace("]", "").replace("[", "")
							.replace(",", "").replace(" ","").replace("，", "")
							.replace("\"result\":", "").replace("\"", "");
					break;
				}
			}

		}catch(Exception e){
			e.printStackTrace();
		}finally{
			rd.close();
		}
		System.out.println("baiduresult:  "+baiduResult);
		
		return baiduResult;

    }

    private static void method2() throws Exception {
        File pcmFile = new File(testFileName);
        HttpURLConnection conn = (HttpURLConnection) new URL(serverURL
                + "?cuid=" + cuid + "&token=" + token).openConnection();

        // add request header
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "audio/pcm; rate=16000");

        conn.setDoInput(true);
        conn.setDoOutput(true);

        // send request
        DataOutputStream wr = new DataOutputStream(conn.getOutputStream());
        wr.write(loadFile(pcmFile));
        wr.flush();
        wr.close();

        printResponse(conn);
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
       // System.out.println(new JSONObject(response.toString()).toString(4));
        return response.toString();
    }

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
}
