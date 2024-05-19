package com.mouad.stockmanagement.services.impl;

import com.mouad.stockmanagement.dto.ProductDto;
import com.mouad.stockmanagement.dto.ClientDto;
import com.mouad.stockmanagement.dto.ClientOrderDto;
import com.mouad.stockmanagement.dto.ClientOrderLineDto;
import com.mouad.stockmanagement.dto.StockMovementDto;

import com.mouad.stockmanagement.exception.EntityNotFoundException;
import com.mouad.stockmanagement.exception.ErrorCodes;
import com.mouad.stockmanagement.exception.InvalidEntityException;
import com.mouad.stockmanagement.exception.InvalidOperationException;

import com.mouad.stockmanagement.model.Product;
import com.mouad.stockmanagement.model.Client;
import com.mouad.stockmanagement.model.ClientOrder;
import com.mouad.stockmanagement.model.OrderStatus;
import com.mouad.stockmanagement.model.ClientOrderLine;
import com.mouad.stockmanagement.model.StockMovementSource;
import com.mouad.stockmanagement.model.StockMovementType;

import com.mouad.stockmanagement.repository.ProductRepository;
import com.mouad.stockmanagement.repository.ClientRepository;
import com.mouad.stockmanagement.repository.ClientOrderRepository;
import com.mouad.stockmanagement.repository.ClientOrderLineRepository;

import com.mouad.stockmanagement.services.ClientOrderService;
import com.mouad.stockmanagement.services.StockMovementService;

import com.mouad.stockmanagement.validator.ProductValidator;
import com.mouad.stockmanagement.validator.ClientOrderValidator;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
@Slf4j
public class ClientOrderServiceImpl implements ClientOrderService {
    private ClientOrderRepository clientOrderRepository;
    private ClientOrderLineRepository clientOrderLineRepository;
    private ClientRepository clientRepository;
    private ProductRepository productRepository;
    private StockMovementService stockMovementService;
    @Autowired
    public ClientOrderServiceImpl(ClientOrderRepository clientOrderRepository,
                                  ClientRepository clientRepository,
                                  ProductRepository productRepository,
                                  ClientOrderLineRepository clientOrderLineRepository,
                                  StockMovementService stockMovementService) {
        this.clientOrderRepository = clientOrderRepository;
        this.clientOrderLineRepository = clientOrderLineRepository;
        this.clientRepository = clientRepository;
        this.productRepository = productRepository;
        this.stockMovementService = stockMovementService;
    }

    @Override
    public ClientOrderDto save(ClientOrderDto dto) {
        List<String> errors = ClientOrderValidator.validate(dto);

        if (!errors.isEmpty()) {
            log.error("Commande client n'est pas valide");
            throw new InvalidEntityException("La commande client n'est pas valide", ErrorCodes.CLIENT_ORDER_NOT_VALID, errors);
        }

        if (dto.getId() != null && dto.isOrderDelivered()) {
            throw new InvalidOperationException("Impossible de modifier la commande lorsqu'elle est livree", ErrorCodes.CLIENT_ORDER_NOT_MODIFIABLE);
        }

        Optional<Client> client = clientRepository.findById(dto.getClient().getId());
        if (client.isEmpty()) {
            log.warn("Client with ID {} was not found in the DB", dto.getClient().getId());
            throw new EntityNotFoundException("Aucun client avec l'ID" + dto.getClient().getId() + " n'a ete trouve dans la BDD", ErrorCodes.CLIENT_NOT_FOUND);
        }

        List<String> productErrors = new ArrayList<>();

        if (dto.getClientOrderLines() != null) {
            dto.getClientOrderLines().forEach(ligCmdClt -> {
                if (ligCmdClt.getProduct() != null) {
                    Optional<Product> article = productRepository.findById(ligCmdClt.getProduct().getId());
                    if (article.isEmpty()) {
                        productErrors.add("L'article avec l'ID " + ligCmdClt.getProduct().getId() + " n'existe pas");
                    }
                } else {
                    productErrors.add("Impossible d'enregister une commande avec un aticle NULL");
                }
            });
        }

        if (!productErrors.isEmpty()) {
            log.warn("");
            throw new InvalidEntityException("Article n'existe pas dans la BDD", ErrorCodes.PRODUCT_NOT_FOUND, productErrors);
        }

        dto.setOrderDate(Instant.now());
        ClientOrder savedCmdClt = clientOrderRepository.save(ClientOrderDto.toEntity(dto));

        if (dto.getClientOrderLines() != null) {
            dto.getClientOrderLines().forEach(ligCmdClt -> {
                ClientOrderLine clientOrderLine = ClientOrderLineDto.toEntity(ligCmdClt);
                clientOrderLine.setClientOrder(savedCmdClt);
                clientOrderLine.setCompanyId(dto.getCompanyId());
                ClientOrderLine orderLine = clientOrderLineRepository.save(clientOrderLine);
                processStockExit(orderLine);
            });
        }

        return ClientOrderDto.fromEntity(savedCmdClt);
    }

    @Override
    public ClientOrderDto updateOrderStatus(Integer orderId, OrderStatus orderStatus) {
        checkOrderId(orderId);
        if (!StringUtils.hasLength(String.valueOf(orderStatus))) {
            log.error("L'etat de la commande client is NULL");
            throw new InvalidOperationException("Impossible de modifier l'etat de la commande avec un etat null", ErrorCodes.CLIENT_ORDER_NOT_MODIFIABLE);
        }

        ClientOrderDto clientOrder = checkOrderStatus(orderId);
        clientOrder.setOrderStatus(orderStatus);

        ClientOrder clientOrderSaved  = clientOrderRepository.save(ClientOrderDto.toEntity(clientOrder));
        if (clientOrder.isOrderDelivered()) {
            updateStockMovement(orderId);
        }

        return ClientOrderDto.fromEntity(clientOrderSaved);
    }

    @Override
    public ClientOrderDto updateQuantityOrder(Integer orderId, Integer orderLineId, BigDecimal quantity) {
        checkOrderId(orderId);
        checkOrderLineId(orderLineId);

        if (quantity == null || quantity.compareTo(BigDecimal.ZERO) == 0) {
            log.error("L'ID de la ligne commande is NULL");
            throw new InvalidOperationException("Impossible de modifier l'etat de la commande avec une quantite null ou ZERO", ErrorCodes.CLIENT_ORDER_NOT_MODIFIABLE);
        }

        ClientOrderDto clientOrderDto = checkOrderStatus(orderId);
        Optional<ClientOrderLine> clientOrderLineOptional = findClientOrderLine(orderLineId);

        ClientOrderLine clientOrderLine = clientOrderLineOptional.get();
        clientOrderLine.setQuantity(quantity);
        clientOrderLineRepository.save(clientOrderLine);

        return clientOrderDto;
    }

    @Override
    public ClientOrderDto updateClient(Integer orderId, Integer idClient) {
        checkOrderId(orderId);
        if (idClient == null) {
            log.error("L'ID du client is NULL");
            throw new InvalidOperationException("Impossible de modifier l'etat de la commande avec un ID client null", ErrorCodes.CLIENT_ORDER_NOT_MODIFIABLE);
        }

        ClientOrderDto clientOrderDto = checkOrderStatus(orderId);
        Optional<Client> clientOptional = clientRepository.findById(idClient);
        if (clientOptional.isEmpty()) {
            throw new EntityNotFoundException("Aucun client n'a ete trouve avec l'ID " + idClient, ErrorCodes.CLIENT_NOT_FOUND);
        }
        clientOrderDto.setClient(ClientDto.fromEntity(clientOptional.get()));

        return ClientOrderDto.fromEntity(
            clientOrderRepository.save(ClientOrderDto.toEntity(clientOrderDto))
        );
    }

    @Override
    public ClientOrderDto updateProduct(Integer orderId, Integer orderLineId, Integer productId) {
        checkOrderId(orderId);
        checkOrderLineId(orderLineId);
        checkProductId(productId, "nouvel");

        ClientOrderDto clientOrder = checkOrderStatus(orderId);

        Optional<ClientOrderLine> clientOrderLine = findClientOrderLine(orderLineId);
        Optional<Product> productOptional = productRepository.findById(productId);

        if (productOptional.isEmpty()) {
            throw new EntityNotFoundException("Aucune article n'a ete trouve avec l'ID " + productId, ErrorCodes.PRODUCT_NOT_FOUND);
        }

        List<String> errors = ProductValidator.validate(ProductDto.fromEntity(productOptional.get()));
        if (!errors.isEmpty()) {
            throw new InvalidEntityException("Article invalid", ErrorCodes.PRODUCT_NOT_VALID, errors);
        }

        ClientOrderLine clientOrderLineSaved = clientOrderLine.get();
        clientOrderLineSaved.setProduct(productOptional.get());
        clientOrderLineRepository.save(clientOrderLineSaved);

        return clientOrder;
    }

    @Override
    public ClientOrderDto deleteProduct(Integer orderId, Integer orderLineId) {
        checkOrderId(orderId);
        checkOrderLineId(orderLineId);

        ClientOrderDto clientOrder = checkOrderStatus(orderId);
        // Just to check the ClientOrderLine and inform the client in case it is absent
        findClientOrderLine(orderLineId);
        clientOrderLineRepository.deleteById(orderLineId);

        return clientOrder;
    }

    @Override
    public ClientOrderDto findById(Integer id) {
        if (id == null) {
            log.error("Commande client ID is NULL");
            return null;
        }
        return clientOrderRepository.findById(id)
            .map(ClientOrderDto::fromEntity)
            .orElseThrow(() -> new EntityNotFoundException("Aucune commande client n'a ete trouve avec l'ID " + id, ErrorCodes.CLIENT_ORDER_NOT_FOUND));
    }

    @Override
    public ClientOrderDto findByCode(String code) {
        if (!StringUtils.hasLength(code)) {
            log.error("Commande client CODE is NULL");
            return null;
        }
        return clientOrderRepository.findClientOrderByCode(code)
            .map(ClientOrderDto::fromEntity)
            .orElseThrow(() -> new EntityNotFoundException("Aucune commande client n'a ete trouve avec le CODE " + code, ErrorCodes.CLIENT_ORDER_NOT_FOUND));
    }

    @Override
    public List<ClientOrderDto> findAll() {
        return clientOrderRepository.findAll().stream()
            .map(ClientOrderDto::fromEntity)
            .collect(Collectors.toList());
    }

    @Override
    public List<ClientOrderLineDto> findAllClientOrderLinesByClientOrderId(Integer orderId) {
        return clientOrderLineRepository.findAllByClientOrderId(orderId).stream()
            .map(ClientOrderLineDto::fromEntity)
            .collect(Collectors.toList());
    }

    @Override
    public void delete(Integer id) {
        if (id == null) {
            log.error("Commande client ID is NULL");
            return;
        }
        List<ClientOrderLine> clientOrderLine = clientOrderLineRepository.findAllByClientOrderId(id);
        if (!clientOrderLine.isEmpty()) {
            throw new InvalidOperationException("Impossible de supprimer une commande client deja utilisee", ErrorCodes.CLIENT_ORDER_ALREADY_IN_USE);
        }
        clientOrderRepository.deleteById(id);
    }


    private void processStockExit(ClientOrderLine line) {
        StockMovementDto stockMovementDto = StockMovementDto.builder()
            .product(ProductDto.fromEntity(line.getProduct()))
            .movementDate(Instant.now())
            .stockMovementType(StockMovementType.EXIT)
            .stockMovementSource(StockMovementSource.ORDER_CLIENT)
            .quantity(line.getQuantity())
            .companyId(line.getCompanyId())
            .build();
        stockMovementService.exitStock(stockMovementDto);
    }

    private void checkOrderId(Integer orderId) {
        if (orderId == null) {
            log.error("Commande client ID is NULL");
            throw new InvalidOperationException("Impossible de modifier l'etat de la commande avec un ID null", ErrorCodes.CLIENT_ORDER_NOT_MODIFIABLE);
        }
    }

    private ClientOrderDto checkOrderStatus(Integer orderId) {
        ClientOrderDto clientOrder = findById(orderId);
        if (clientOrder.isOrderDelivered()) {
            throw new InvalidOperationException("Impossible de modifier la commande lorsqu'elle est livree", ErrorCodes.CLIENT_ORDER_NOT_MODIFIABLE);
        }
        return clientOrder;
    }

    private void updateStockMovement(Integer orderId) {
        List<ClientOrderLine> clientOrderLines = clientOrderLineRepository.findAllByClientOrderId(orderId);
        clientOrderLines.forEach(line -> {
            processStockExit(line);
        });
    }

    private void checkOrderLineId(Integer orderLineId) {
        if (orderLineId == null) {
            log.error("L'ID de la ligne commande is NULL");
            throw new InvalidOperationException("Impossible de modifier l'etat de la commande avec une ligne de commande null", ErrorCodes.CLIENT_ORDER_NOT_MODIFIABLE);
        }
    }

    private Optional<ClientOrderLine> findClientOrderLine(Integer orderLineId) {
        Optional<ClientOrderLine> clientOrderLineOptional = clientOrderLineRepository.findById(orderLineId);
        if (clientOrderLineOptional.isEmpty()) {
            throw new EntityNotFoundException("Aucune ligne commande client n'a ete trouve avec l'ID " + orderLineId, ErrorCodes.CLIENT_ORDER_NOT_FOUND);
        }
        return clientOrderLineOptional;
    }

    private void checkProductId(Integer productId, String msg) {
        if (productId == null) {
            log.error("L'ID de " + msg + " is NULL");
            throw new InvalidOperationException("Impossible de modifier l'etat de la commande avec un " + msg + " ID article null", ErrorCodes.CLIENT_ORDER_NOT_MODIFIABLE);
        }
    }
}
