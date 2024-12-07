package com.codeforall.online.javabank.command;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.format.annotation.NumberFormat;

/**
 * A class which represents the form-backing bean to fulfill when doing a transfer
 */
public class TransferDto {

    @NotNull(message = "Source account is mandatory")
    private Integer srcId;

    @NotNull(message = "Destination account is mandatory")
    private Integer dstId;

    @NumberFormat
    @NotBlank(message = "InitialAmount is mandatory")
    @NotNull(message = "Amount is mandatory")
    private String amount;

    /**
     * Get the source id of the transfer DTO
     * @return the transfer DTO source id
     */
    public Integer getSrcId() {
        return srcId;
    }

    /**
     * Get the destination id of the transfer DTO
     * @return the transfer DTO destination id
     */
    public Integer getDstId() {
        return dstId;
    }

    /**
     * Get the amount of the transfer DTO
     * @return the transfer DTO amount
     */
    public String getAmount() {
        return amount;
    }

    /**
     * Set the source id of the transfer DTO
     * @param srcId the id to set
     */
    public void setSrcId(Integer srcId) {
        this.srcId = srcId;
    }

    /**
     * Set the destination id of the transfer DTO
     * @param dstId the id to set
     */
    public void setDstId(Integer dstId) {
        this.dstId = dstId;
    }

    /**
     * Set the amount of the transfer DTO
     * @param amount the amount to set
     */
    public void setAmount(String amount) {
        this.amount = amount;
    }
}
