package com.example.hackathon.model;

public class BidDto1 {
	  private String projectTitle;
	    private String clientName;
	    private Double amount;
	    private String proposal;
	    private String status;
		public String getProjectTitle() {
			return projectTitle;
		}
		public void setProjectTitle(String projectTitle) {
			this.projectTitle = projectTitle;
		}
		public String getClientName() {
			return clientName;
		}
		public void setClientName(String clientName) {
			this.clientName = clientName;
		}
		public Double getAmount() {
			return amount;
		}
		public void setAmount(Double amount) {
			this.amount = amount;
		}
		public String getProposal() {
			return proposal;
		}
		public void setProposal(String proposal) {
			this.proposal = proposal;
		}
		public String getStatus() {
			return status;
		}
		public void setStatus(String status) {
			this.status = status;
		}
		public BidDto1(String projectTitle, String clientName, Double amount, String proposal, String status) {
	        this.projectTitle = projectTitle;
	        this.clientName = clientName;
	        this.amount = amount;
	        this.proposal = proposal;
	        this.status = status;
	    }
		public BidDto1() {
			super();
			// TODO Auto-generated constructor stub
		}

}