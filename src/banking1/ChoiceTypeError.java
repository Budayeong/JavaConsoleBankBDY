package banking1;

public class ChoiceTypeError extends Exception {

	public ChoiceTypeError() {
		super("메뉴는 1~7 까지의 정수만 입력가능합니다");
	}
	
}
