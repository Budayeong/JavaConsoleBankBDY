package banking1;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashSet;
import java.util.InputMismatchException;
import java.util.Iterator;
import java.util.Scanner;

//핸들러 클래스
public class AccountManager {
	HashSet<Account> account = new HashSet<Account>();
	private AutoSaver autoSaver;
	
//	사용자 메뉴출력
	public static void showMenu() {
		System.out.println("-------Menu-------");
		System.out.println("1. 계좌개설");
		System.out.println("2. 입금");
		System.out.println("3. 출금");
		System.out.println("4. 전체계좌정보출력");
		System.out.println("5. 계좌정보삭제");
		System.out.println("6. 저장옵션");
		System.out.println("7. 프로그램 종료");
		System.out.print("메뉴선택: ");
	}
	
//	계좌개설
	public void makeAccount() {
		Account inputAccount = null; 
		Scanner sc = new Scanner(System.in);
		
		System.out.println("***신규계좌개설***"); 
		System.out.println("----- 계좌선택 -----");
		System.out.println("1. 보통계좌");
		System.out.println("2. 신용신뢰계좌");
		System.out.println("3. 특판계좌");
		System.out.print("선택: ");
		
		int accountType = sc.nextInt();
		sc.nextLine();
		
//		*** 1~2 외 숫자를 입력했을경우 반려
		if(accountType != 1 && accountType != 2 && accountType != 3) {
			System.out.println("\n*** 메뉴선택은 1~3 중 하나만 선택가능합니다");
			return;
		}
		
//		각 계좌 공통 내용 먼저 출력
		System.out.print("계좌번호: "); 
		String num = sc.nextLine();
		System.out.print("고객이름: ");
		String name = sc.nextLine();
		System.out.print("잔고: ");
		int balance = sc.nextInt();
		System.out.print("기본이자%(정수형태로입력): ");
		int rate = sc.nextInt();
//		버퍼에 남아있는 내용정리
		sc.nextLine();
		
		
//		1. 보통계좌 선택
		if(accountType==1) {
			inputAccount = new NormalAccount(num, name, balance, rate);
		}
//		2. 신용신뢰계좌 선택
		else if (accountType==2) {
//			신용계좌 선택 시 신용등급 내용 추가 출력
			System.out.print("신용등급(A,B,C등급): ");
			String credit = sc.nextLine();
			
			inputAccount = new HighCreditAccount(num, name, balance, rate, credit);
		}
//		3. 특판계좌 선택
		else if (accountType==3) {
			inputAccount = new SpecialAccount(num, name, balance, rate);
			System.out.println("특판계좌 생성");
		}
		
		
//		[계좌번호 중복확인]
//		입력된 계좌번호의 중복여부 판단
//		contatins: Accoount(계좌 최상위 클래스) 의 오버라이딩 된 hashcode, equals에 의해 계좌번호가 동일한 객체가 account에 있는지 비교
//		if:	true를 반환한 경우 = 동일한 계좌번호 인스턴스가 존재한다
		if(account.contains(inputAccount)) {
			System.out.print("\n** 중복계좌발견** 덮어쓸까요?(y or n) : ");
			
//			중복계좌를 덮어쓸지 말지 사용자에게 입력을 받음
			switch(sc.nextLine()) {
//			y 입력: 기존 계좌정보를 제거하고 새로운 정보로 덮어씀
			case "y":{
//				account 배열에 존재하는 inputAccount와 동일한 계좌번호의 인스턴스가 삭제됨
				account.remove(inputAccount);
//				accout 배열에 현재 사용자가 입력한 계좌를 저장
				account.add(inputAccount);
				System.out.println("기존 정보가 삭제되고 새로운 정보로 변경되었습다");
				break;
			}
//			n 입력: 기존 계좌정보 유지
			case "n":
//				기존 account 내용 변경없이 유지, 입력받은 계좌 무시
				System.out.println("해당 계좌번호에 대한 기존 정보를 유지합니다");
			}
		}
//		else: false를 반환한 경우 =  입력된 계좌번호가 기존에 존재하지 않는다
		else { 
//			account에 동일한 객체가 없으므로 바로 저장
			account.add(inputAccount);
			System.out.println("\n계좌개설이 완료되었습니다.");
		}
}
	
//	입금
	public void depositMoney() {
		Scanner sc = new Scanner(System.in);
		
		
//		inputNum: 사용자가 입력한 계좌번호, inputBalance: 사용자가 입금할 입금액
		System.out.println("***입  금***"); 
		System.out.println("계좌번호와 입금할 금액을 입력하세요");
		System.out.print("계좌번호: ");
		String inputNum = sc.nextLine();
		System.out.print("입금액: ");
		int deposit=0;
		
		try {
			deposit= sc.nextInt();
//			*** 음수는 입금 불가 -> 반려
			if(deposit<0) {
				System.out.println("\n*** 입금은 양수만 가능합니다(음수불가)");
				return;
			}
//			*** 입금은 500원단위로만 가능 -> 500원 단위입금이 아니면 반려
			if(!(deposit%500==0)) {
				System.out.println("\n *** 입금은 500원 단위로만 가능합니다");
				return;
			}
		}
//		*** 금액 입력 시 문자는 입력불가 -> 반려
		catch (InputMismatchException e) {
			System.out.println("\n***금액 입력 시 문자는 입력할 수 없습니다");
			return;
		}
	
		
		Iterator<Account> itr = account.iterator();
		while(itr.hasNext()) {
			Account thisAccont = itr.next();
//			입력된 계좌번호가 존재하는지 확인 : account에 저장된 계좌정보의 번호와 사용자가 입력한 계좌번호 내용 비교
//			if: ture 반환시 -> 사용자가 입력한 계좌번호가 account 에 존재할때
			if(inputNum.equals(thisAccont.getNum())) {
//				1.특판계좌인 경우
				if(thisAccont instanceof SpecialAccount) {
					SpecialAccount special = (SpecialAccount) thisAccont;
//					입금횟수 +1
					special.setDmCount(special.getDmCount()+1);
//					기본이자
					double sRate = special.getRate()/100.0;
//					현재 계좌의 잔고
					int todayBalance = special.getBalance();
					
//					현재 입금횟수가 짝수인지 판단
					if(special.getDmCount()%2==0) {
//						짝수이면: 500원 축하금 지급
						special.setBalance((int)(todayBalance + (todayBalance*sRate) + deposit + 500));
						System.out.println("짝수번째 입금으로 500원 축하금이 지급되었습니다");
					}
					else {
//						특판계좌: 잔고 + (잔고 * 기본이자) + 입금액
						special.setBalance((int)(todayBalance + (todayBalance*sRate) + deposit));
					}
					System.out.println("현재 계좌 잔고: " + special.getBalance() + "원");
					System.out.println("현재 입금 횟수: " + special.getDmCount() + "회");
					return;
				}
//				2.보통계좌인 경우
				else if(thisAccont instanceof NormalAccount) {
					NormalAccount normal = (NormalAccount)thisAccont;
//					기본이자
					double iRate =  normal.getRate()/100.0;
//					현재 계좌의 잔고
					int todayBalance = normal.getBalance();
//					보통계좌: 잔고 + (잔고 * 기본이자) + 입금액
					thisAccont.setBalance((int)(todayBalance + (todayBalance*iRate) + deposit));
					System.out.println("\n입금이 완료되었습니다");
					return;
				}
//				2. 신용계좌인 경우
				else if (thisAccont instanceof HighCreditAccount) {
					HighCreditAccount highCredit = (HighCreditAccount) thisAccont;
//					기본이자
					double rate =  highCredit.getRate()/100.0;
//					현재 계좌의 잔고
					int todayBalance = highCredit.getBalance();
//					추가이자 변수
					double cRate=0.0;
//					추가이자 설정
//					해당 계좌의 등급을 확인해서 A,B,C 등급에 따라 차등 설정
					switch (highCredit.getCredit()) {
					case "A": {
						cRate = 0.07;
						break;
						}
					case "B" :{
						cRate = 0.04;
						break;
						}
					case "C" :{
						cRate = 0.02;
						break;
						}
					}
//					신용계좌 : 잔고 + (잔고 * 기본이자) + (잔고 * 추가이자) + 입금액
					highCredit.setBalance((int)(todayBalance + (todayBalance*rate) + (todayBalance*cRate) + deposit));
					System.out.println("\n입금이 완료되었습니다");
					
					return;
				}
			}
		}//while끝
//		while문으로 accout를 모두 돌 동안, 같은정보의 계좌가 존재하지않을 경우 안내 후 종료
		System.out.println("\n입력된 계좌번호가 존재하지않습니다");
}

//	출금
	public void withdrawMoney() {
		
		Scanner sc = new Scanner(System.in);
		
		System.out.println("***출  금***"); 
		System.out.println("계좌번호와 출금할 금액을 입력하세요");
		System.out.print("계좌번호: ");
		String inputNum = sc.nextLine();
		System.out.print("출금액: ");
		int outMoney = sc.nextInt();
		sc.nextLine();
		
//		*** 음수는 출금 불가 -> 반려
		if(outMoney<0) {
			System.out.println("\n*** 음수는 출금 불가능합니다");
			return;
		}
//		*** 출금은 1000원 단위로 가능 -> 1000원 단위가 아니면 반려
		if(!(outMoney%1000==0)) {
			System.out.println("\n*** 출금은 1000원 단위로만 가능합니다");
			return;
		}
		
//		출금을 원하는 계좌번호의 존재여부 확인
		Iterator<Account> itr = account.iterator();
		while(itr.hasNext()) {
			Account thisAccont = itr.next();
//			if: 저장된 계좌정보가 존재하면
			if(inputNum.equals(thisAccont.getNum())) {
//				출금 금액이 잔고보다 많을 때
				if(thisAccont.getBalance()<outMoney) {
					System.out.println("*** 잔고가 부족합니다. 금액전체를 출금할까요?");
					System.out.println("YES: 금액전체 출금처리");
					System.out.println("NO: 출금요청취소");
					System.out.print("선택: ");
					
//					사용자에게 YES, NO 입력받음
					switch (sc.nextLine()) {
//					YES : 금액 전체 출금처리
					case "YES": {
						System.out.println("\n통장잔고 전액: " + thisAccont.getBalance()+"원 출금이 완료되었습니다");
						thisAccont.setBalance(0);
						System.out.println("현재 통장잔고: " + thisAccont.getBalance() + "원''");
						return;
					}
//					NO : 출금 요청 취소 처리
					case "NO": {
						System.out.println("\n출금을 취소합니다");
						return;
					}
					}
				}
//				출금금액보다 잔액이 클때(정상출금)
				else {
					thisAccont.setBalance(thisAccont.getBalance()-outMoney);
					System.out.println("\n"+outMoney+"원 출금이 완료되었습니다");
					return;
				}
			}
//			else: 사용자 입력 계좌번호가 저장된 계좌정보에 없을때 -> 반려
			else {
//				안내 후 종료
				System.out.println("\n입력된 계좌정보가 존재하지 않습니다");
				return;
			}
		}
	}
	
//	전체 계좌정보 출력
	public void showAccInfo() {
		System.out.println("***계좌정보출력***"); 
		
		for(Account e : account) {
//			1.특판계좌 - 입금회차까지 출력
			if (e instanceof SpecialAccount) {
				SpecialAccount special = (SpecialAccount) e;
				System.out.println("------------------"); 
				System.out.println("계좌번호> " + special.getNum());
				System.out.println("고객이름> " + special.getName());
				System.out.println("잔고> " + special.getBalance() + "원");
				System.out.println("기본이자> " + special.getRate() +"%");
				System.out.println("입금회차> " + special.getDmCount() +"회");
				System.out.println("------------------"); 
			}
//			2.보통계좌 - 이자까지 출력
			else if(e instanceof NormalAccount) {
				NormalAccount normal = (NormalAccount)e;
				System.out.println("------------------"); 
				System.out.println("계좌번호> " + normal.getNum());
				System.out.println("고객이름> " + normal.getName());
				System.out.println("잔고> " + normal.getBalance() + "원");
				System.out.println("기본이자> " + normal.getRate() +"%");
				System.out.println("------------------"); 
			}
//			3.신용계좌 - 등급까지 출력
			else if (e instanceof HighCreditAccount) {
				HighCreditAccount highCredit = (HighCreditAccount)e;
				System.out.println("------------------"); 
				System.out.println("계좌번호> " + highCredit.getNum());
				System.out.println("고객이름> " + highCredit.getName());
				System.out.println("잔고> " + highCredit.getBalance() + "원");
				System.out.println("기본이자> " + highCredit.getRate() +"%");
				System.out.println("신용등급> " + highCredit.getCredit() + "등급");
				System.out.println("------------------"); 
			}
		}
		System.out.println("전체계좌정보 출력이 완료되었습니다.");
		System.out.println();
	}
	
//	계좌정보 삭제
	public void deleteAccount() {
		Scanner sc = new Scanner(System.in);

		System.out.println("***계좌정보삭제***");
		System.out.println("삭제할 계좌번호를 입력하세요");
		System.out.print("계좌번호: ");
		
		String deleteAccout = sc.nextLine();
		
		for(Account e : account) {
//			저장된 계좌정보가 존재하면
			if(e.getNum().equals(deleteAccout)) {
				account.remove(e);
				System.out.println("\n계좌정보가 삭제되었습니다.");
				return;
			}
		}
		System.out.println("\n일치하는 계좌정보가 없습니다");

	}
	
//	계좌정보 저장
	public void saveAccountInfo() {
		try {
			ObjectOutputStream out =new ObjectOutputStream(new FileOutputStream("src/banking1/AccountInfo.obj"));
			for(Account f : account) {
				out.writeObject(f);
			}
		} catch (IOException e) {
			System.out.println("직렬화 중 예외발생");
			e.printStackTrace();
		}
	}

//	계좌정보 가져오기
	public void readAccountInfo() {
//		계좌정보를 다 읽으면 예외가 발생하기때문에 try-catch처리
		try {
			ObjectInputStream in = new ObjectInputStream(new FileInputStream("src/banking1/AccountInfo.obj"));
			while(true) {
				Account ac = (Account) in.readObject();
				account.add(ac);
			}
		} catch (Exception e) {
			System.out.println("기존 계좌정보를 가져왔습니다\n");
		}
		
	}
	
//	계좌정보 자동저장
	public void autoSave() {
		
		Scanner sc = new Scanner(System.in);
		
		System.out.println("1. 자동저장 ON");
		System.out.println("2. 자동저장 OFF");
		System.out.print("선택: ");
		
		int autoSaveChoice = sc.nextInt();
		sc.nextLine();
		
//		1. 자동저장 ON
		if(autoSaveChoice==1) {
			System.out.println("\n자동저장 시작");
			autoSaver = new AutoSaver(this);
			autoSaver.start();
		}
//		2. 자동저장 OFF
		else if (autoSaveChoice==2) {
			System.out.println("\n자동저장 종료");
			autoSaver.interrupt();
		}
		
	}



}
