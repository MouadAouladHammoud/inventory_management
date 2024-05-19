package com.mouad.stockmanagement.services.impl;

import com.mouad.stockmanagement.dto.ClientDto;
import com.mouad.stockmanagement.exception.EntityNotFoundException;
import com.mouad.stockmanagement.exception.ErrorCodes;
import com.mouad.stockmanagement.exception.InvalidEntityException;
import com.mouad.stockmanagement.exception.InvalidOperationException;
import com.mouad.stockmanagement.model.ClientOrder;
import com.mouad.stockmanagement.repository.ClientRepository;
import com.mouad.stockmanagement.repository.ClientOrderRepository;
import com.mouad.stockmanagement.services.ClientService;
import com.mouad.stockmanagement.validator.ClientValidator;

import java.util.List;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class ClientServiceImpl implements ClientService {

    private ClientRepository clientRepository;
    private ClientOrderRepository clientOrderRepository;
    @Autowired
    public ClientServiceImpl(ClientRepository clientRepository, ClientOrderRepository clientOrderRepository) {
        this.clientRepository = clientRepository;
        this.clientOrderRepository = clientOrderRepository;
    }

    @Override
    public ClientDto save(ClientDto dto) {
        List<String> errors = ClientValidator.validate(dto);
        if (!errors.isEmpty()) {
            log.error("Client is not valid {}", dto);
            throw new InvalidEntityException("Le client n'est pas valide", ErrorCodes.CLIENT_NOT_VALID, errors);
        }
        return ClientDto.fromEntity(
            clientRepository.save(ClientDto.toEntity(dto))
        );
    }

    @Override
    public ClientDto findById(Integer id) {
        if (id == null) {
            log.error("Client ID is null");
            return null;
        }
        return clientRepository.findById(id)
            .map(ClientDto::fromEntity)
            .orElseThrow(() -> new EntityNotFoundException("Aucun Client avec l'ID = " + id + " n' ete trouve dans la BDD", ErrorCodes.CLIENT_NOT_FOUND));
    }

    @Override
    public List<ClientDto> findAll() {
        return clientRepository.findAll().stream()
            .map(ClientDto::fromEntity)
            .collect(Collectors.toList());
    }

    @Override
    public void delete(Integer id) {
        if (id == null) {
            log.error("Client ID is null");
            return;
        }
        List<ClientOrder> commandeClients = clientOrderRepository.findAllByClientId(id);
        if (!commandeClients.isEmpty()) {
            throw new InvalidOperationException("Impossible de supprimer un client qui a deja des commande clients", ErrorCodes.CLIENT_ALREADY_IN_USE);
        }
        clientRepository.deleteById(id);
    }
}
