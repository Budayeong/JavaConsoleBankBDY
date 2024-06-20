package banking1;

public class SpecialAccount extends NormalAccount {
	
	private int dmCount = 0;

	public SpecialAccount(String num, String name, int balance, int interest) {
		super(num, name, balance, interest);
	}

	public int getDmCount() {
		return dmCount;
	}

	public void setDmCount(int dmCount) {
		this.dmCount = dmCount;
	}
	
	
	
	
	 
}
