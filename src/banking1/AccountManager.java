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


//컨트롤 클래스로 프로그램의 전반적인 기능을 구현
public class AccountManager {
//	계좌정보는 최대 50개만 저장가능
	HashSet<Account> account = new HashSet<Account>();
//	Account[] account = new Account[50];
	int idx=0;
	
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
		System.out.print("선택: ");
		
		int accountType = sc.nextInt();
		sc.nextLine();
		
//		1~2 외 숫자를 입력했을경우 반려
		if(!(accountType==1 || accountType==2)) {
			System.out.println("\n*** 메뉴선택은 1~2 중 하나만 선택가능합니다");
			return;
		}
		
		System.out.print("계좌번호: "); 
		String num = sc.nextLine();
		System.out.print("고객이름: ");
		String name = sc.nextLine();
		System.out.print("잔고: ");
		int balance = sc.nextInt();
		System.out.print("기본이자%(정수형태로입력): ");
		int interest = sc.nextInt();
		sc.nextLine();
		
		
//		1. 보통계좌 선택
		if(accountType==1) {
			inputAccount = new NormalAccount(num, name, balance, interest);
		}
//		2. 신용신뢰계좌 선택
		else if (accountType==2) {
			System.out.print("신용등급(A,B,C등급): ");
			String credit = sc.nextLine();
			
			inputAccount = new HighCreditAccount(num, name, balance, interest, credit);
		}
		
//		[계좌번호 중복확인]
//		입력된 계좌번호의 중복여부 판단
		if(account.contains(inputAccount)) {
			System.out.print("\n** 중복계좌발견** 덮어쓸까요?(y or n) : ");
			
			switch(sc.nextLine()) {
//			y: 기존 계좌정보를 제거하고 새로운 정보로 덮어씀
			case "y":{
				account.remove(inputAccount);
				account.add(inputAccount);
				System.out.println("기존 정보가 삭제되고 새로운 정보로 변경되었습다");
				break;
			}
//			n: 기존 계좌정보 유지
			case "n":
				System.out.println("해당 계좌번호에 대한 기존 정보를 유지합니다");
			}
		}
//		입력된 계좌번호가 기존에 존재하지않으면 바로 계좌개설
		else { 
			account.add(inputAccount);
			System.out.println("\n계좌개설이 완료되었습니다.");
		}
}
	
//	입금
	public void depositMoney() {
		
		Scanner sc = new Scanner(System.in);
		
		System.out.println("***입  금***"); 
		System.out.println("계좌번호와 입금할 금액을 입력하세요");
		System.out.print("계좌번호: ");
		String inputNum = sc.nextLine();
		System.out.print("입금액: ");
		int inputBalance=0;
		
		try {
			inputBalance= sc.nextInt();
//			*** 음수는 입금 불가
			if(inputBalance<0) {
				System.out.println("\n*** 입금은 양수만 가능합니다(음수불가)");
				return;
			}
//			*** 입금은 500원단위로만 가능
			if(!(inputBalance%500==0)) {
				System.out.println("\n *** 입금은 500원 단위로만 가능합니다");
				return;
			}
		}
//		*** 금액 입력 시 문자는 입력불가
		catch (InputMismatchException e) {
			System.out.println("\n***금액 입력 시 문자는 입력할 수 없습니다");
			return;
		}
	
		Iterator<Account> itr = account.iterator();
		while(itr.hasNext()) {
			Account thisAccont = itr.next();
//			입력된 계좌번호가 존재하면
			if(inputNum.equals(thisAccont.getNum())) {
//				1.보통계좌인 경우
				if(thisAccont instanceof NormalAccount) {
					NormalAccount normal = (NormalAccount)thisAccont;
//					기본이자
					double interestRate =  normal.getInterest()/100.0;
//					현재 계좌의 잔고
					int todayBalance = normal.getBalance();
//					보통계좌: 잔고 + (잔고 * 기본이자) + 입금액
					thisAccont.setBalance((int)(todayBalance + (todayBalance*interestRate) + inputBalance));
					System.out.println("\n입금이 완료되었습니다");
					
					return;
				}
//				2. 신용계좌인 경우
				else {
					HighCreditAccount highCredit = (HighCreditAccount) thisAccont;
//					기본이자
					double interestRate =  highCredit.getInterest()/100.0;
//					현재 계좌의 잔고
					int todayBalance = highCredit.getBalance();
//					추가이자 변수
					double creditInterest=0.0;
//					추가이자 설정
					switch (highCredit.getCredit()) {
					case "A": {
						creditInterest = 0.07;
						break;
						}
					case "B" :{
						creditInterest = 0.04;
						break;
						}
					case "C" :{
						creditInterest = 0.02;
						break;
						}
					}
					highCredit.setBalance((int)(todayBalance + (todayBalance*interestRate) + (todayBalance*creditInterest) + inputBalance));
					System.out.println("\n입금이 완료되었습니다");
					
					return;
				}
			}
		}//while끝
		System.out.println("입력된 계좌번호가 존재하지않습니다");
}

//	출금
	public void withdrawMoney() {
		
		Scanner sc = new Scanner(System.in);
		
		System.out.println("***출  금***"); 
		System.out.println("계좌번호와 출금할 금액을 입력하세요");
		System.out.print("계좌번호: ");
		String inputNum = sc.nextLine();
		System.out.print("출금액: ");
		int inputBalance= sc.nextInt();
		sc.nextLine();
//		*** 음수는 출금 불가
		if(inputBalance<0) {
			System.out.println("\n*** 음수는 출금 불가능합니다");
			return;
		}
		if(!(inputBalance%1000==0)) {
			System.out.println("\n*** 출금은 1000원 단위로만 가능합니다");
			return;
		}
		
		Iterator<Account> itr = account.iterator();
		while(itr.hasNext()) {
			Account thisAccont = itr.next();
//			저장된 계좌정보가 존재하면
			if(inputNum.equals(thisAccont.getNum())) {
//				출금 금액이 잔고보다 많을 때
				if(thisAccont.getBalance()<inputBalance) {
					System.out.println("*** 잔고가 부족합니다. 금액전체를 출금할까요?");
					System.out.println("YES: 금액전체 출금처리");
					System.out.println("NO: 출금요청취소");
					System.out.print("선택: ");
					
					switch (sc.nextLine()) {
//					YES : 금액 전체 출금처리
					case "YES": {
						System.out.println("\n통장잔고 전액: " + thisAccont.getBalance()+"원 출금이 완료되었습니다");
						thisAccont.setBalance(0);
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
					thisAccont.setBalance(thisAccont.getBalance()-inputBalance);
					System.out.println("\n"+inputBalance+"원 출금이 완료되었습니다");
					return;
				}
			}
		}
		
//			저장된 계좌정보가 존재하지않으면
		System.out.println("입력된 계좌정보가 존재하지 않습니다");
	}
	
//	전체 계좌정보 출력
	public void showAccInfo() {
		System.out.println("***계좌정보출력***"); 
		
		for(Account e : account) {
//			1.보통계좌 - 이자까지 출력
			if(e instanceof NormalAccount) {
				NormalAccount normal = (NormalAccount)e;
				System.out.println("------------------"); 
				System.out.println("계좌번호> " + normal.getNum());
				System.out.println("고객이름> " + normal.getName());
				System.out.println("잔고> " + normal.getBalance() + "원");
				System.out.println("기본이자> " + normal.getInterest() +"%");
				System.out.println("------------------"); 
			}
//			2.신용계좌 - 등급까지 출력
			else {
				HighCreditAccount highCredit = (HighCreditAccount)e;
				System.out.println("------------------"); 
				System.out.println("계좌번호> " + highCredit.getNum());
				System.out.println("고객이름> " + highCredit.getName());
				System.out.println("잔고> " + highCredit.getBalance() + "원");
				System.out.println("기본이자> " + highCredit.getInterest() +"%");
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
			ObjectOutputStream out =new ObjectOutputStream(new FileOutputStream("src/banking1/account.obj"));
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
		try {
			ObjectInputStream in = new ObjectInputStream(new FileInputStream("src/banking1/account.obj"));
			while(true) {
				Account ac = (Account) in.readObject();
				account.add(ac);
			}
		} catch (Exception e) {
//			
		}
		
	}
	
//	계좌정보 자동저장
	public void autoSave() {
		
		Scanner sc = new Scanner(System.in);
		
		System.out.println("1. 자동저장 ON");
		System.out.println("2. 자동저장 OFF");
		System.out.print("선택: ");
		
		AutoSaver as = new AutoSaver();
		int autoSave = sc.nextInt();
		sc.nextLine();
		System.out.println(autoSave);
		
//		1. 자동저장 ON
		if(autoSave==1) {
			System.out.println("\n자동저장 시작");
			as.start();
		}
//		2. 자동저장 OFF
		else if (autoSave==2) {
			System.out.println("\n자동저장 종료");
			try {
				as.sleep(10000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			as.interrupt();
		}
		
	}



}
