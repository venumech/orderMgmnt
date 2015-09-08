package org.venu.develop.model;

import java.io.Serializable;

/*
 * need to implement serializable (marker interface) when referenced to serialize the object, 'Order' to the hard disk.
 */
public class Address implements Serializable{

	private static final long serialVersionUID = 1L;
	private String city;
	private String state;
	private String zip;
	
	public Address (){}
	
	public Address (String city, String state, String zip ){
		this.city=city;
		this.state=state;
		this.zip=zip;
	}
	/**
	 * @return the city
	 */
	public String getCity() {
		return city;
	}
	/**
	 * @return the state
	 */
	public String getState() {
		return state;
	}
	/**
	 * @return the zip
	 */
	public String getZip() {
		return zip;
	}
	/**
	 * @param city the city to set
	 */
	public void setCity(String city) {
		this.city = city;
	}
	/**
	 * @param state the state to set
	 */
	public void setState(String state) {
		this.state = state;
	}
	/**
	 * @param zip the zip to set
	 */
	public void setZip(String zip) {
		this.zip = zip;
	}
	
	public String toString(){
		
		return "[" + this.city + "," + this.state +"," + this.zip + "]";
	}
}
