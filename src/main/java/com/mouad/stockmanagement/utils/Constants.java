package com.mouad.stockmanagement.utils;

public interface Constants {
    String APP_ROOT = "stockmanagement/v1";

    String COMPANY_ENDPOINT = APP_ROOT + "/company";
    String USER_ENDPOINT = APP_ROOT + "/users";
    String SUPPLIER_ENDPOINT = APP_ROOT + "/suppliers";

    String SUPPLIER_ORDER_ENDPOINT = APP_ROOT + "/supplierOrders";
    String CREATE_SUPPLIER_ORDER_ENDPOINT = SUPPLIER_ORDER_ENDPOINT + "/create";
    String FIND_SUPPLIER_ORDER_BY_ID_ENDPOINT = SUPPLIER_ORDER_ENDPOINT + "/{supplierOrderId}";
    String FIND_SUPPLIER_ORDER_BY_CODE_ENDPOINT = SUPPLIER_ORDER_ENDPOINT + "/filter/{supplierOrderCode}";
    String FIND_ALL_SUPPLIER_ORDERS_ENDPOINT = SUPPLIER_ORDER_ENDPOINT + "/all";
    String DELETE_SUPPLIER_ORDER_ENDPOINT = SUPPLIER_ORDER_ENDPOINT + "/delete/{supplierOrderId}";

    String SALES_ENDPOINT = APP_ROOT + "/sales";
    String AUTHENTICATION_ENDPOINT = APP_ROOT + "/auth";

}
