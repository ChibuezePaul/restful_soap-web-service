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
 * <p>Java class for SignatureAddResponse complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="SignatureAddResponse">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="SignatureAddRs" type="{http://webservice.fiusb.ci.infosys.com}SignatureAddRs"/>
 *         &lt;element name="SignatureAdd_CustomData" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "SignatureAddResponse", propOrder = {
    "signatureAddRs",
    "signatureAddCustomData"
})
public class SignatureAddResponse {

    @XmlElement(name = "SignatureAddRs", required = true)
    protected SignatureAddRs signatureAddRs;
    @XmlElement(name = "SignatureAdd_CustomData", required = true)
    protected String signatureAddCustomData;

    /**
     * Gets the value of the signatureAddRs property.
     * 
     * @return
     *     possible object is
     *     {@link SignatureAddRs }
     *     
     */
    public SignatureAddRs getSignatureAddRs() {
        return signatureAddRs;
    }

    /**
     * Sets the value of the signatureAddRs property.
     * 
     * @param value
     *     allowed object is
     *     {@link SignatureAddRs }
     *     
     */
    public void setSignatureAddRs(SignatureAddRs value) {
        this.signatureAddRs = value;
    }

    /**
     * Gets the value of the signatureAddCustomData property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSignatureAddCustomData() {
        return signatureAddCustomData;
    }

    /**
     * Sets the value of the signatureAddCustomData property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSignatureAddCustomData(String value) {
        this.signatureAddCustomData = value;
    }
    
    @Override
    public String toString () {
        return "\n<SignatureAddRs>" + signatureAddRs + "</SignatureAddRs>\n" +
              "</SignatureAddRs><SignatureAdd_CustomData/>\n";
    }
}
