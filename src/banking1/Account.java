package banking1;

import java.io.Serializable;
import java.util.Objects;

//계좌정보를 표현한 클래스로 NormalAccount, HighCreditAccount의 부모클래스
abstract public class Account implements Serializable {
	
	private String num;
	private String name;
	private int balance;

	public Account(String num, String name, int balance) {
		super();
		this.num = num;
		this.name = name;
		this.balance = balance;
	}

	public String getNum() {
		return num;
	}

	public void setNum(String num) {
		this.num = num;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getBalance() {
		return balance;
	}

	public void setBalance(int balance) {
		this.balance = balance;
	}
	
//	계좌번호를 사용해 해시코드 생성
	@Override
	public int hashCode() {
		return Objects.hash(this.getName());
	}
	
	
//	계좌번호가 동일하면 중복 계좌로 판단
	@Override
	public boolean equals(Object obj) {
		if(obj instanceof Account) {
			Account account = (Account)obj;
			if(this.getNum().equals(account.getNum()))
				return true;
		}
		return false;
	}

	
	
}
