

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.sf.json.JSONObject;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopScoreDocCollector;

import workFlow.Function;
import workFlow.Node;

@WebServlet("/engine/main")
public class MainServlet extends HttpServlet {

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public MainServlet() {
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
		IndexReader reader =  (IndexReader)sct.getAttribute("reader");
		HttpSession session = request.getSession();
		String contentPath = getServletContext().getRealPath("/");  
        String askPath = contentPath + "/audio";
        
        String actionType = request.getParameter("actionType");
        String question =  request.getParameter("question");
        
		String functionId = (String) request.getAttribute("functionId");
		String functionName = (String) request.getAttribute("functionName");
		
		String storedText = (String) request.getAttribute("storedText");
		
		//String nodeId = (String) request.getAttribute("nodeId");
	
		
		Map functionMap = (HashMap)sct.getAttribute("functionMap"); 
		session.setAttribute("functionId",functionMap.get(functionId));
		Function function = (Function) functionMap.get(functionId);
		//Node node = (Node) function.getNodeList().get(nodeId);
		
		if("QUESTION".equals(actionType)){	//提问
			TxtReader ask = new TxtReader();
			ask.excute(question,askPath);
			
			response.getWriter().print("{audio:\""+askPath+"\"}");
		
		}else if("REDIRECT".equals(actionType)){
			//跳转
			String urlName ="";
			try {
				urlName = redirect( functionName,reader);//跳转url
			} catch (ParseException e) {
				e.printStackTrace();
			}
			functionMap.put("urlName", urlName);
			
			response.getWriter().print(JSONObject.fromObject(functionMap));
			
		}else if("STORE".equals(actionType)){
			//保存输入
			response.getWriter().print("{text:\""+storedText+"\"}");d
		}

	}
	

	
	private String  redirect(String functionName, IndexReader reader ) throws IOException, ParseException, ParseException, ParseException{ 

		IndexSearcher searcher = new IndexSearcher(reader);           // 3. search 
		int hitsPerPage = 1; 
		Query q = new QueryParser("functionName",new StandardAnalyzer()).parse(functionName);
		TopScoreDocCollector collector = TopScoreDocCollector.create(hitsPerPage); 
		searcher.search(q, collector); 
		ScoreDoc[] hits = collector.topDocs().scoreDocs; 

		String url = "" ;

		for(int i=0;i<hits.length;++i) { 
			int docId = hits[i].doc; 
			Document d = searcher.doc(docId);//
			url = d.get("url");
		} 
		System.out.println("url:"+ url);
		
		return url;
		
	} 
}
