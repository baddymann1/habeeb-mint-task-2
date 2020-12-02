package com.mint.task2.domain.customer.dto;

import lombok.*;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

@Setter
@Getter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class AddCustomerDTO implements Serializable {


    @NotBlank(message = "name cannot be null")
    private String name;

    @NotBlank(message = "phone number cannot be null")
    private String phoneNo;
}
