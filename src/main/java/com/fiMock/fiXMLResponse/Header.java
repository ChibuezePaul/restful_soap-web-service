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
 * <p>Java class for Header complex type.
 * <p>
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;complexType name="Header">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="ResponseHeader" type="{http://webservice.fiusb.ci.infosys.com}ResponseHeader"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType ( XmlAccessType.FIELD )
@XmlType ( name = "Header", propOrder = {
	  "responseHeader"
} )
public class Header {
	
	@XmlElement ( name = "ResponseHeader", required = true )
	protected ResponseHeader responseHeader;
	
	/**
	 * Gets the value of the responseHeader property.
	 *
	 * @return possible object is
	 * {@link ResponseHeader }
	 */
	public ResponseHeader getResponseHeader () {
		return responseHeader;
	}
	
	/**
	 * Sets the value of the responseHeader property.
	 *
	 * @param value allowed object is
	 *              {@link ResponseHeader }
	 */
	public void setResponseHeader ( ResponseHeader value ) {
		this.responseHeader = value;
	}
	
	@Override
	public String toString () {
		return "\n<ResponseHeader>" + responseHeader + "</ResponseHeader>\n";
	}
}
