package com.mouad.stockmanagement.dto;

import com.mouad.stockmanagement.model.Client;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.List;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ClientDto {
    private Integer id;
    private String lastName;
    private String firstName;
    private AddressDto address;
    private String profilePicture;
    private String email;
    private String phoneNumber;
    private Integer companyId;

    @JsonIgnore
    private List<ClientOrderDto> clientOrders;

    public static ClientDto fromEntity(Client client) {
        if (client == null) {
            return null;
        }
        return ClientDto.builder()
            .id(client.getId())
            .lastName(client.getLastName())
            .firstName(client.getFirstName())
            .address(AddressDto.fromEntity(client.getAddress()))
            .profilePicture(client.getProfilePicture())
            .email(client.getEmail())
            .phoneNumber(client.getPhoneNumber())
            .companyId(client.getCompanyId())
            .build();
    }

    public static Client toEntity(ClientDto dto) {
        if (dto == null) {
            return null;
        }
        Client client = new Client();
        client.setId(dto.getId());
        client.setLastName(dto.getLastName());
        client.setFirstName(dto.getFirstName());
        client.setAddress(AddressDto.toEntity(dto.getAddress()));
        client.setProfilePicture(dto.getProfilePicture());
        client.setEmail(dto.getEmail());
        client.setPhoneNumber(dto.getPhoneNumber());
        client.setCompanyId(dto.getCompanyId());
        return client;
    }
}
