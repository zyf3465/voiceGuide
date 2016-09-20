

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopScoreDocCollector;

import com.jdnull.speechRec.baiduAPI.Recognizer;
//import com.sun.javafx.tk.Toolkit.Task;

/**
 * Servlet implementation class voiceServlet
 */
@WebServlet("/query")
public class voiceServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final String fileName = "voice.wav";

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public voiceServlet() {
		super();
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.println("get");
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8"); 
		ServletContext sct=getServletConfig().getServletContext();   
		//此处不是创建session 而是去取已经创建的session
		HttpSession session = request.getSession();
		Task task;
		TaskNode node;
		//如果已经取到，说明已经登录
//		if(session==null)
//		{
			task = (Task)sct.getAttribute("task1"); // task type
			node = task.getNodes().get(0); // node id
//		} else {
//			task = (Task)session.getAttribute("task");
//			node = (TaskNode)session.getAttribute("node");
//		}

		IndexReader reader = (IndexReader)sct.getAttribute("reader"); 
		
		String mp3Url = "";
		String url = "";
		String recResult = "";
		String account = "";
		String receiver = "";
		String money = "";
		
		boolean shouldBreak = false;
		while(!shouldBreak) {
			if (task.getTaskType()== "task1") {
				switch (node.getId()) {
//				case 0:
//					mp3Url = node.getProcessor().excute("您好，请问您需要什么帮助？");
//					session.setAttribute("task", task);
//					session.setAttribute("node", node);
//					response.getWriter().print(mp3Url);
//					shouldBreak = true;
//					break;
					
				case 0:
					wavfileUpload(request);
					recResult = node.getProcessor().excute("voice.wav"); // recognizer
					
//					try {
//						recResult = redirect(recResult,reader,response);
//					} catch (ParseException e) {
//						// TODO Auto-generated catch block
//						e.printStackTrace();
//					}
					
					
					node = task.getNodes().get(node.getNext_id());
//					String nextTaskType = node.getProcessor().excute(recResult); // distribute
					String nextTaskType = "task2";
					task = (Task)sct.getAttribute(nextTaskType);
					node = task.getNodes().get(0);
					break;
				default:
					break;
				}
			} else if (task.taskType == "task2") {
				switch (node.getId()) {
				case 0:
					mp3Url = node.getProcessor().excute("请问收款人是中行客户还是非中行客户？");
					session.setAttribute("task", task);
					session.setAttribute("node", node);
					shouldBreak = true;
					System.out.println("sssssssssss  "+mp3Url);
					response.getWriter().print("{audio:\""+mp3Url+"\"}");
					break;
					
				case 1:
					wavfileUpload(request);
					recResult = node.getProcessor().excute("voice.wav"); // recognizer
					node = task.getNodes().get(node.getNext_id());
					break;
					
				case 2:
					url = node.getProcessor().excute(recResult);
					task = (Task)sct.getAttribute("task3");
					node = task.getNodes().get(0);
					break;
					// send url

				default:
					break;
				}
			} else {
				switch (node.getId()) {
				case 0:
					mp3Url = node.getProcessor().excute("您有工资卡，中银信用卡，请告诉我您选择哪一个账户作为付款账号呢？");
					session.setAttribute("task", task);
					session.setAttribute("node", node);
					shouldBreak = true;
					break;
				case 1:
					wavfileUpload(request);
					recResult = node.getProcessor().excute("voice.wav"); // recognizer
					if (recResult.contains("工资")) {
						account = "工资卡";
					} else {
						account = "中银信用卡";
					}
					node = task.getNodes().get(node.getNext_id());
					break;
				case 2:
					mp3Url = node.getProcessor().excute("您最近的收款人有三个收款人，1、刁主管，2、曹高经，3、李朋，请告诉我他的序号？");
					session.setAttribute("task", task);
					session.setAttribute("node", node);
					session.setAttribute("account", account);
					shouldBreak = true;
					break;
				case 3:
					wavfileUpload(request);
					recResult = node.getProcessor().excute("voice.wav"); // recognizer
					if (recResult.contains("1") || recResult.contains("一")) {
						receiver = "刁主管";
					} else if (recResult.contains("2") || recResult.contains("二")) {
						receiver = "曹高经";
					} else if (recResult.contains("3") || recResult.contains("三")) {
						receiver = "李朋";
					}
					node = task.getNodes().get(node.getNext_id());
					break;
				case 4:
					mp3Url = node.getProcessor().excute("请说出金额？");
					session.setAttribute("task", task);
					session.setAttribute("node", node);
					session.setAttribute("receiver", receiver);
					shouldBreak = true;
					break;
				case 5:
					wavfileUpload(request);
					recResult = node.getProcessor().excute("voice.wav"); // recognizer
					money = recResult;
					node = task.getNodes().get(node.getNext_id());
					break;
				case 6:
					String txt = "请确认是否从"
							+ session.getAttribute("account")
							+ "向"
							+ session.getAttribute("receiver")
							+ "转"
							+ money
							+ "元?";
					mp3Url = node.getProcessor().excute(txt);
					session.setAttribute("task", task);
					session.setAttribute("node", node);
					session.setAttribute("money", money);
					shouldBreak = true;
					break;
				case 7:
					wavfileUpload(request);
					recResult = node.getProcessor().excute("voice.wav"); // recognizer
					
					break;

				default:
					break;
				}
			}
		}
		
		
				
//		Task task1 = (Task)sct.getAttribute("task1");
//		Task task2 = (Task)sct.getAttribute("task2");
//		Task task3 = (Task)sct.getAttribute("task3");
		
//		if (sessionFlag == 0) {
//			wavfileUpload(request);
//			
//			TaskNode taskNode = task1.getNodes().get(0);
//			TxtReader txtReader = (TxtReader)taskNode.getProcessor();
//			recResult = txtReader.excute("voice.wav"); // recognizer
//			sessionFlag = 1;
//			activeTask = "task1";
//			nextNode = taskNode.getNext_id();
//			
//			TaskNode taskNode2 = task1.getNodes().get(nextNode);
//			TaskDistributer taskDistributer = (TaskDistributer)taskNode2.getProcessor();
//			activeTask = taskDistributer.excute(recResult);
//			nextNode = taskNode2.getNext_id();
//			
//			TaskNode taskNode3 = task1.getNodes().get(nextNode);
			
//		}
		
		 
		
		
		

//		Recognizer rz = new Recognizer();
//
//		try {
//			String result = rz.recognize(fileName);//
//			//redirect(result,reader,response);
//
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
	}
	
	private void wavfileUpload(HttpServletRequest request) {
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
            List<FileItem> list = upload.parseRequest(request);  
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
	}

	private String  redirect(String result, IndexReader reader , HttpServletResponse response) throws IOException, ParseException, ParseException, ParseException{ 

		IndexSearcher searcher = new IndexSearcher(reader);           // 3. search 
		int hitsPerPage = 1; 
		Query q = new QueryParser("functionName",new StandardAnalyzer()).parse(result);
		TopScoreDocCollector collector = TopScoreDocCollector.create(hitsPerPage); 
		searcher.search(q, collector); 
		ScoreDoc[] hits = collector.topDocs().scoreDocs; 

		String url = "" ;
		response.setContentType("text/html;charset=utf-8");   

		Document hitD = null;
		for(int i=0;i<hits.length;++i) { 
			int docId = hits[i].doc; 
			Document d = searcher.doc(docId);//
			url = d.get("url");
			hitD = d;
		} 
		System.out.println("url:"+ url);
		
		return hitD.get("functionName");
//		response.getWriter().print(url);
	} 

}
