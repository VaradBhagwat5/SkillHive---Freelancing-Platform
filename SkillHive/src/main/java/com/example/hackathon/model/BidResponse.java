package com.example.hackathon.model;


public class BidResponse {

	private Long id;
    private String projectTitle;
    private String bidderName;  // This should match the bidder's name (freelancer's username)
    private Double amount;
    private String message;     // This should map to the proposal/message of the bid
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getProjectTitle() {
		return projectTitle;
	}
	public void setProjectTitle(String projectTitle) {
		this.projectTitle = projectTitle;
	}
	public String getBidderName() {
		return bidderName;
	}
	public void setBidderName(String bidderName) {
		this.bidderName = bidderName;
	}
	public Double getAmount() {
		return amount;
	}
	public void setAmount(Double amount) {
		this.amount = amount;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public BidResponse(Long id, String projectTitle, String bidderName, Double amount, String message) {
		super();
		this.id = id;
		this.projectTitle = projectTitle;
		this.bidderName = bidderName;
		this.amount = amount;
		this.message = message;
	}

   
}