package ca.mcgill.ecse321.eventregistration.service.payment;

public final class TestPaymentWithBitcoinData {
	// Error Message expected
	public static final String ID_FORMAT_ERROR = "User id is null or has wrong format!";
	public static final String AMOUNT_NEGATIVE_ERROR = "Payment amount cannot be negative!";
	public static final String PAY_WITH_NULL_ERROR = "Registration and payment cannot be null!";

	// test 1 testPayWithBitcoin
	public static final String TEST01_PERSON_NAME = "Person";
	public static final String TEST01_EVENT_NAME = "SoccerGame";
	public static final String TEST01_VALID_ID = "ACGF-6875";
	public static final int TEST01_VALID_AMOUNT = 10;

	// test 2 testMultipleValidPays
	public static final String[] TEST02_VALID_IDS = { "FSTD-1232", "STVG-2412", "WTFS-2345", "GGPS-5124", "PAFG-6146" };
	public static final int[] TEST02_VALID_AMOUNTS = { 567, 110, 1523, 321, 235 };
	public static final String[] TEST02_PERSON_NAMES = { "Bob", "Alice", "Jack", "Seven", "Tiga" };
	public static final String[] TEST02_EVENT_NAMES = { "Soccer Game", "Movie", "Car Show", "Concert", "Exam" };

	// test 3 testMultiplePaysBreakFormat
	public static final int TEST03_BREAK_INDEX = 2;
	public static final String[] TEST03_PPARTIAL_BREAK_IDS = { "FSTD-1232", "STVG-2412", "2345-WTSG", "GGPS-5124",
			"PAFG-6146" };
	public static final int[] TEST03_VALID_AMOUNTS = { 567, 234, 1523, 678, 235 };
	public static final String[] TEST03_PERSON_NAMES = { "Bob", "Alice", "Jack", "Seven", "Tiga" };
	public static final String[] TEST03_EVENT_NAMES = { "Soccer Game", "Movie", "Car Show", "Concert", "Exam" };

	// test 4 testMultiplePaysBreakNegative
	public static final int TEST04_BREAK_INDEX = 3;
	public static final String[] TEST04_VALID_IDS = { "FSTD-1232", "STVG-2412", "WTFS-2345", "GGPS-5124", "PAFG-6146" };
	public static final int[] TEST04_PARTIAL_BREAK_AMOUNTS = { 567, 234, 1523, -678, 235 };
	public static final String[] TEST04_PERSON_NAMES = { "Bob", "Alice", "Jack", "Seven", "Tiga" };
	public static final String[] TEST04_EVENT_NAMES = { "Soccer Game", "Movie", "Car Show", "Concert", "Exam" };

	// test 5 testUpdatePay
	public static final String TEST05_PERSON_NAME = "Person";
	public static final String TEST05_EVENT_NAME = "SoccerGame";
	public static final String TEST05_INITIAL_ID = "AWRT-6875";
	public static final int TEST05_INITIAL_AMOUNT = 10;
	public static final String TEST05_AFTER_ID = "AWRT-6275";
	public static final int TEST05_AFTER_AMOUNT = 100;

	// test 06 testCreateBitcoin
	public static final String TEST06_VALID_ID = "ECSE-0321";
	public static final int TEST06_VALID_AMOUNT = 100;

	// test 07 testPayWithBitcoinNull
	public static final String TEST07_PERSON_NAME = "Person";
	public static final String TEST07_EVENT_NAME = "SoccerGame";

	// test 08 testPayWithRegistrationNull
	public static final String TEST08_PERSON_NAME = "Person";
	public static final String TEST08_EVENT_NAME = "SoccerGame";

	// test 09 testCreateBitcoinWrongFormat
	public static final String TEST09_WRONG_ID = "ThisIDIsWrong";
	public static final int TEST09_VALID_AMOUNT = 100;

	// test 10 testCreateBitcoinLongFormat
	public static final String TEST10_WRONG_ID = "ACGF-6875-1234";
	public static final int TEST10_VALID_AMOUNT = 100;

	// test 11 testCreateBitcoinNull
	public static final String TEST11_WRONG_ID = null;
	public static final int TEST11_VALID_AMOUNT = 100;

	// test 12 testCreateBitcoinEmpty
	public static final String TEST12_WRONG_ID = "";
	public static final int TEST12_VALID_AMOUNT = 100;

	// test 13 testCreateBitcoinSpace
	public static final String TEST13_WRONG_ID = " ";
	public static final int TEST13_VALID_AMOUNT = 100;

	// test 14 testCreateBitcoinZero
	public static final String TEST14_VALID_ID = "ECSE-0321";
	public static final int TEST14_VALID_AMOUNT = 0;

	// test 15 testCreateBitcoinNegative
	public static final String TEST15_VALID_ID = "ECSE-0321";
	public static final int TEST15_WRONG_AMOUNT = -10;
}
