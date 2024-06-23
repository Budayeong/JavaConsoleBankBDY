package banking1;

public class HighCreditAccount extends Account {
//	이자비율 정보
	private int rate;
//	신용등급
	private String credit;
	
	public HighCreditAccount(String num, String name, int balance, int rate, String credit) {
		super(num, name, balance);
		this.rate = rate;
		this.credit = credit;
	}


	public int getRate() {
		return rate;
	}


	public void setRate(int rate) {
		this.rate = rate;
	}


	public String getCredit() {
		return credit;
	}

	public void setCredit(String credit) {
		this.credit = credit;
	}
	
	
	
	
	
	
}
