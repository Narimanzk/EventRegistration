package ca.mcgill.ecse321.eventregistration.dto;

public class BitcoinDto {
	
	private String userID;
	private int amount;
	
	
	public BitcoinDto() {
		
	}


	public BitcoinDto(String userID, int amount) {
		this.userID = userID;
		this.amount = amount;
	}


	public String getUserID() {
		return userID;
	}


	public void setUserID(String userID) {
		this.userID = userID;
	}


	public int getAmount() {
		return amount;
	}


	public void setAmount(int amount) {
		this.amount = amount;
	}
	
	
	
	

}
