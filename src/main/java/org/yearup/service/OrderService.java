package org.yearup.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.yearup.models.*;
import org.yearup.repository.OrderLineItemRepository;
import org.yearup.repository.OrderRepository;

import java.time.LocalDateTime;
import java.util.Map;

@Service
public class OrderService {
    private final OrderRepository orderRepository;
    private final OrderLineItemRepository orderLineItemRepository;
    private final ShoppingCartService shoppingCartService;
    private final ProfileService profileService;

    public OrderService(
            OrderRepository orderRepository,
            OrderLineItemRepository orderLineItemRepository,
            ShoppingCartService shoppingCartService,
            ProfileService profileService
    ) {
        this.orderRepository = orderRepository;
        this.orderLineItemRepository = orderLineItemRepository;
        this.shoppingCartService = shoppingCartService;
        this.profileService = profileService;
    }

    @Transactional
    public Order checkout(int userId) {
        ShoppingCart cart = shoppingCartService.getByUserId(userId);

        if (cart.getItems().isEmpty()) {
            throw new IllegalArgumentException("Cart is empty");
        }
        
        Profile profile = profileService.getByUserId(userId);

        Order order = new Order();
        order.setUserId(userId);
        order.setDate(LocalDateTime.now());
        order.setAddress(profile.getAddress());
        order.setCity(profile.getCity());
        order.setState(profile.getState());
        order.setZip(profile.getZip());
        order.setShippingAmount(0);

        Order savedOrder = orderRepository.save(order);

        for (Object value : cart.getItems().values()) {
            ShoppingCartItem cartItem = (ShoppingCartItem) value;
            Product product = cartItem.getProduct();

            OrderLineItem lineItem = new OrderLineItem();
            lineItem.setOrderId(savedOrder.getOrderId());
            lineItem.setProduct(product);
            lineItem.setProductId(product.getProductId());
            lineItem.setSalesPrice(product.getPrice());
            lineItem.setQuantity(cartItem.getQuantity());
            lineItem.setDiscount(cartItem.getDiscountPercent());

            OrderLineItem savedLineItem = orderLineItemRepository.save(lineItem);
            savedLineItem.setProduct(product);

            savedOrder.getLineItems().add(savedLineItem);
        }

        shoppingCartService.clearCart(userId);

        return savedOrder;
    }
}