package com.mouad.stockmanagement.controller.api;

import static com.mouad.stockmanagement.utils.Constants.APP_ROOT;

import com.mouad.stockmanagement.dto.StockMovementDto;
// import io.swagger.annotations.Api;
import java.math.BigDecimal;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

// @Api("stockMovement")
public interface StockMovementApi {
    @GetMapping(APP_ROOT + "/stockMovement/inventoryRealProduct/{productId}")
    BigDecimal inventoryRealProduct(@PathVariable("productId") Integer productId);

    @GetMapping(APP_ROOT + "/stockMovement/filter/product/{productId}")
    List<StockMovementDto> StockMovementProduct(@PathVariable("productId") Integer productId);

    @PostMapping(APP_ROOT + "/stockMovement/exit")
    StockMovementDto entryStock(@RequestBody StockMovementDto dto);

    @PostMapping(APP_ROOT + "/stockMovement/entry")
    StockMovementDto exitStock(@RequestBody StockMovementDto dto);

    @PostMapping(APP_ROOT + "/stockMovement/correctionpos")
    StockMovementDto adjustStockPos(@RequestBody StockMovementDto dto);

    @PostMapping(APP_ROOT + "/stockMovement/correctionneg")
    StockMovementDto adjustStockNeg(@RequestBody StockMovementDto dto);
}
