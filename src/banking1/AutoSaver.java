package banking1;

public class AutoSaver extends Thread {
	
	AccountManager manager;

//	데몬쓰레드로 생성
	public AutoSaver(AccountManager manager) {
		this.manager = manager;
		setDaemon(true);
	}

//	자동저장
	@Override
	public void run() {
		try {
			while(true) {
				manager.saveAccountInfo();
				sleep(5000);
			}
		} catch (InterruptedException e) {
			return;
		}
		
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
}
