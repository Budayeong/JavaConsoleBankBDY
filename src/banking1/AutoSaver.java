package banking1;

public class AutoSaver extends Thread {

	AccountManager manager = new AccountManager();
	
//	데몬쓰레드로 생성
	public AutoSaver() {
		setDaemon(true);
	}

//	자동저장
	@Override
	public void run() {
		try {
			while(true) {
				System.out.println("자동저장 됨");
				sleep(5000);
			}
		} catch (InterruptedException e) {
			System.out.println("자동저장 종료되었습니다");
			return;
		}
		
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
}
