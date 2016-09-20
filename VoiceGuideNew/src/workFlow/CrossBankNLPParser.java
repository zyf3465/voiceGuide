package workFlow;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

public class CrossBankNLPParser extends NLPParser{

	public Map execute(String functionName, String jsonString) {
		Map elementMap = new HashMap();

		JSONArray jsonParaArray = new JSONArray(jsonString); // 段落的列表
		int paraId = 0;
		while (paraId < jsonParaArray.length()) {

			JSONArray jsonSentArray = jsonParaArray.getJSONArray(paraId); // 句子的列表
			int sentId = 0;
			while (sentId < jsonSentArray.length()) {

				JSONArray jsonWordArray = jsonSentArray.getJSONArray(sentId); // 词的列表
				int wordId = 0;
				while (wordId < jsonWordArray.length()) {

					JSONObject jsonWord = jsonWordArray.getJSONObject(wordId);
					if (jsonWord.get("semrelate").equals("Root")) {
						elementMap.put("service", jsonWord.getString("cont"));
					} else if (jsonWord.get("pos").equals("m")) {
						elementMap.put("number", jsonWord.getString("cont"));
					} else if (jsonWord.get("pos").equals("nh")) {
						elementMap.put("person", jsonWord.getString("cont"));
					}
					wordId++;
				}
				sentId++;
			}
			paraId++;
		}
		return elementMap;
	}
}
