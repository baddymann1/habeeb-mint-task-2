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
public class UpdateCustomerDTO implements Serializable {
    private Long id;
    @NotBlank(message = "name cannot be null")
    private String name;

    @NotBlank(message = "phone number cannot be null")
    private String phoneNo;
}
