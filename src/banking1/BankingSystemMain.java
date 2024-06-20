package banking1;

import java.util.InputMismatchException;
import java.util.Scanner;
import java.util.concurrent.ForkJoinPool.ManagedBlocker;

//main 메서드를 포함한 클래스. 프로그램은 여기서 실행
public class BankingSystemMain implements ICustomDefine{
	public static void main(String[] args) {
		
//		사용자 입력을 받기위해
		Scanner scan = new Scanner(System.in);
		
//		기능호출을 위한 핸들러 객체
		AccountManager manager = new AccountManager();
		
//		기존 입력한 계좌정보 가져옴
		manager.readAccountInfo();
		
		while(true) {
//			1. 사용자에게 메뉴 출력
			manager.showMenu();
			int choice = 0;
			
			try {
//			2. 사용자로부터 수행할 기능의 메뉴 입력 받음
				choice= scan.nextInt();
//				1~5 외의 숫자를 입력한 경우 예외발생
				if(!(choice>=1 && choice<8)) {
					ChoiceTypeError typeError = new ChoiceTypeError();
					throw typeError;
				}
			}
			catch (ChoiceTypeError e) {
				System.out.println();
				System.out.println("*** "+e.getMessage());
			}
			catch (InputMismatchException e) {
				scan.nextLine();
				System.out.println();
				System.out.println("*** 문자는 입력할 수 없습니다");
			}
			
//			3. 선택한 번호에 따라 메서드 호출
			switch(choice) {
			case MAKE: 
				manager.makeAccount();
				break;
			case DEPOSIT:
				manager.depositMoney();
				break;
			case WITHDRAW:
				manager.withdrawMoney();
				break;
			case INQUIRE:
				manager.showAccInfo();
				break; 
			case DELETE:
				manager.deleteAccount();
				break;
			case AUTOSAVE:
				manager.autoSave();
				break;
			case EXIT:
				System.out.println("프로그램종료");
				manager.saveAccountInfo();
				return;			
			}
		}
		
		
	} //메인끝
}//클래스 끝
