package banking1;

public class HighCreditAccount extends Account {
//	이자비율 정보
	private int interest;
//	신용등급
	private String credit;
	
	public HighCreditAccount(String num, String name, int balance, int interest, String credit) {
		super(num, name, balance);
		this.interest = interest;
		this.credit = credit;
	}

	public int getInterest() {
		return interest;
	}

	public void setInterest(int interest) {
		this.interest = interest;
	}

	public String getCredit() {
		return credit;
	}

	public void setCredit(String credit) {
		this.credit = credit;
	}
	
	
	
	
	
	
}
