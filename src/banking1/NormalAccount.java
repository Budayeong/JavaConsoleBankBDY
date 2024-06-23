package banking1;

public class NormalAccount extends Account {
//	이자비율
	private int rate;

	public NormalAccount(String num, String name, int balance, int rate) {
		super(num, name, balance);
		this.rate = rate;
	}
	
	public int getRate() {
		return rate;
	}

	public void setRate(int rate) {
		this.rate = rate;
	}

	
	
}
