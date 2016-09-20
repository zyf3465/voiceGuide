
public class TxtReader  {
	public void excute(String str,String path) {
		
		try {
			if( myTTS.get(str, path).equals("succeed") ){
				System.out.print("TTS succeed");
			}
		} catch (Exception e) {
			e.printStackTrace();
		};
	}
}
