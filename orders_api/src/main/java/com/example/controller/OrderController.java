package com.example.controller;

import com.example.dto.OrderDto;
import com.example.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Optional;

@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @Operation(summary = "Create a new Order")
    @ApiResponse(responseCode = "201", description = "The new Order has been created", content = {
            @Content(mediaType = "application/json", schema = @Schema(implementation = OrderDto.class))
    })
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<OrderDto> create(@RequestBody OrderDto order) {
        return orderService.createOrder(order);
    }

    @Operation(summary = "Retrieve the list of Orders")
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public Flux<OrderDto> fetchAll() {
        return orderService.fetchOrders();
    }

    @Operation(summary = "Retrieve the details of a given Order by its Id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Returns the details of the Order", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = OrderDto.class))
            }),
            @ApiResponse(responseCode = "404", description = "The id does not match to an existing Order")
    })
    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Mono<ResponseEntity<OrderDto>> fetchById(@PathVariable String id) {
        return orderService.fetchOrderById(id)
                .map(ResponseEntity::ok)
                .onErrorReturn(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @Operation(summary = "Perform a specific update operation on an Order by its Id")
    @Parameter(name = "operation", description = "The order operation", example = "?operation=CANCEL")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Returns the details of the Order", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = OrderDto.class))
            }),
            @ApiResponse(responseCode = "404", description = "The id does not match to an existing Order")
    })
    @PutMapping("/{id}")
    public Mono<ResponseEntity<OrderDto>> update(@PathVariable String id,
                                 @RequestParam(name = "operation") OrderUpdateCriteria criteria) {
        return orderService.updateOrder(id, criteria)
                .map(ResponseEntity::ok)
                .onErrorReturn(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }
}
