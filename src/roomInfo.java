
public class roomInfo {
	private String HostName="";
	private String roomName="";
	private String numPlayer ="";

	roomInfo(String a){
		System.out.println("Generating roomInfo");
		System.out.println("a=" +a);
		String temp="";
		for (int i = 0;i<a.length();i++) {
			
			temp += a.charAt(i);
			if (a.charAt(i) == '/' && i!= a.length()-1) {
				temp = temp.replace(temp.substring(temp.length()-1), "");
//				System.out.println("temp = "+temp);
				if (roomName.equals("")) {
					roomName = temp;
					temp ="";
				} else if (HostName.equals("")) {
					HostName = temp;
					temp="";
				}
			} else if (i==a.length()-1) {
				numPlayer = temp;
			}
		}
		System.out.println("roomName = "+roomName +" host = "+ HostName + " numPlayer= "+ numPlayer);
	}
	
	public String getRoomName2 () {
		return roomName;
	}
	public String getHostName() {
		return HostName;
	}
	public String getNumPlayer() {
		return numPlayer;
	}
}
