//
// This file was generated by the JavaTM Architecture for fiXML Binding(JAXB) Reference Implementation, v2.2.7
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2019.07.15 at 12:39:42 PM WAT 
//


package com.fiMock.fiXMLResponse;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for CustomerModOutputStruct complex type.
 * <p>
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;complexType name="CustomerModOutputStruct">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="cifid" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="desc" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="entity" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="service" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="status" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType ( XmlAccessType.FIELD )
@XmlType ( name = "CustomerModOutputStruct", propOrder = {
	  "cifid" ,
	  "desc" ,
	  "entity" ,
	  "service" ,
	  "status"
} )
public class CustomerModOutputStruct {
	
	@XmlElement ( name = "cifid", required = true )
	protected String cifid;
	@XmlElement ( name = "desc", required = true )
	protected String desc;
	@XmlElement ( name = "entity", required = true )
	protected String entity;
	@XmlElement ( name = "service", required = true )
	protected String service;
	@XmlElement ( name = "status", required = true )
	protected String status;
	
	/**
	 * Gets the value of the cifid property.
	 *
	 * @return possible object is
	 * {@link String }
	 */
	public String getcifid () {
		return cifid;
	}
	
	/**
	 * Sets the value of the cifid property.
	 *
	 * @param value allowed object is
	 *              {@link String }
	 */
	public void setcifid ( String value ) {
		this.cifid = value;
	}
	
	/**
	 * Gets the value of the desc property.
	 *
	 * @return possible object is
	 * {@link String }
	 */
	public String getDesc () {
		return desc;
	}
	
	/**
	 * Sets the value of the desc property.
	 *
	 * @param value allowed object is
	 *              {@link String }
	 */
	public void setDesc ( String value ) {
		this.desc = value;
	}
	
	/**
	 * Gets the value of the entity property.
	 *
	 * @return possible object is
	 * {@link String }
	 */
	public String getEntity () {
		return entity;
	}
	
	/**
	 * Sets the value of the entity property.
	 *
	 * @param value allowed object is
	 *              {@link String }
	 */
	public void setEntity ( String value ) {
		this.entity = value;
	}
	
	/**
	 * Gets the value of the service property.
	 *
	 * @return possible object is
	 * {@link String }
	 */
	public String getService () {
		return service;
	}
	
	/**
	 * Sets the value of the service property.
	 *
	 * @param value allowed object is
	 *              {@link String }
	 */
	public void setService ( String value ) {
		this.service = value;
	}
	
	/**
	 * Gets the value of the status property.
	 *
	 * @return possible object is
	 * {@link String }
	 */
	public String getStatus () {
		return status;
	}
	
	/**
	 * Sets the value of the status property.
	 *
	 * @param value allowed object is
	 *              {@link String }
	 */
	public void setStatus ( String value ) {
		this.status = value;
	}
	
	@Override
	public String toString () {
		return "\n<cifid>" + cifid + "</cifid>\n" +
			  "<desc>" + desc + "</desc>\n" +
			  "<entity>" + entity + "</entity>\n" +
			  "<service>" + service + "</service>\n" +
			  "<status>" + status + "</status>\n";
	}
}
