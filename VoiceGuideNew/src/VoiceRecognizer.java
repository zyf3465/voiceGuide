import com.jdnull.speechRec.baiduAPI.Recognizer;

public class VoiceRecognizer extends Processor{
	String excute(String filename) {
		// receive from browser's mic data
		Recognizer rz = new Recognizer();
		String result = "";
		try {
			result = rz.recognize(filename);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result;
	}
}
