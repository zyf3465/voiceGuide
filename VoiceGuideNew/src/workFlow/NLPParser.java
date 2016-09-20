package workFlow;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

public  class NLPParser {
	
	public  Map execute(String functionName, String jsonString) {
		
		Map elementMap = new HashMap();
		JSONObject jsonWord = preProcess(functionName,jsonString);

		return elementMap;
	}

	public JSONObject preProcess(String functionName, String jsonString){

		JSONArray jsonParaArray = new JSONArray(jsonString); // 段落的列表

		JSONArray jsonWordArray = jsonParaArray.getJSONArray(0).getJSONArray(0); //每个取第一个元素，词的列表
		
		int wordId = 0;
		JSONObject jsonWord = null;
		while (wordId < jsonWordArray.length()) {

			 jsonWord = jsonWordArray.getJSONObject(wordId);
			 
		}
		return jsonWord;

	}


}
