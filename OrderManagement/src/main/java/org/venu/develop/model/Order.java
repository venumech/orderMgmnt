package org.venu.develop.model;

import java.io.Serializable;
import java.util.List;

/*
 * When the database access is not set up, we need to implement serializable (marker interface)
 * for the object, 'Order' to be serialized and the data('Order Object graph') is saved to the hard disk.
 * 
 */

@SuppressWarnings("serial")
public class Order implements Serializable{   
	private Long id;
	private Address from;
	private Address to;
	private List<LineItem> lines;
	private String instructions;
	
	/**
	 * @return the id
	 */
	public Long getId() {
		return id;
	}
	/**
	 * @param id the id to set
	 */
	public void setId(Long id) {
		this.id = id;
	}
	/**
	 * @return the fromAdress
	 */
	public Address getFrom() {
		return from;
	}
	/**
	 * @param fromAdress the fromAdress to set
	 */
	public void setFrom(Address fromAdress) {
		this.from = fromAdress;
	}
	/**
	 * @return the toAdress
	 */
	public Address getTo() {
		return to;
	}
	/**
	 * @param toAdress the toAdress to set
	 */
	public void setTo(Address toAdress) {
		this.to = toAdress;
	}
	/**
	 * @return the instructions
	 */
	public String getInstructions() {
		return instructions;
	}
	/**
	 * @param instructions the instructions to set
	 */
	public void setInstructions(String instructions) {
		this.instructions = instructions;
	}
	/**
	 * @return the lineItem
	 */
	public List<LineItem> getLines() {
		return lines;
	}
	/**
	 * @param lineItem the lineItem to set
	 */
	public void setLines(List<LineItem> lineItem) {
		lines = lineItem;
	}
	
	public String toString(){
		
		 return "["+this.id + "," + this.from.toString() + "," + 
				 this.to. toString()  + "," + "[{" + this.getLines() + "}]" + "," + 
				 this.instructions +"]"; 
	}
}
