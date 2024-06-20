package banking1;

public class NormalAccount extends Account {
//	이자비율
	private int interest;

	public NormalAccount(String num, String name, int balance, int interest) {
		super(num, name, balance);
		this.interest = interest;
	}

	public int getInterest() {
		return interest;
	}

	public void setInterest(int interest) {
		this.interest = interest;
	}
	
	


	
	
}
