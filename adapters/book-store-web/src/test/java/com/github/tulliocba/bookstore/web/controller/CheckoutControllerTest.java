package com.github.tulliocba.bookstore.web.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tulliocba.bookstore.store.application.port.in.CheckoutUseCase;
import com.github.tulliocba.bookstore.store.application.port.in.CheckoutUseCase.CheckoutCommand;
import com.github.tulliocba.bookstore.store.application.port.in.CheckoutUseCase.Item;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.HashSet;

import static java.util.Arrays.asList;

@WebMvcTest(controllers = CheckoutController.class)
public class CheckoutControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CheckoutUseCase checkoutUseCase;

    @Test
    public void should_succeeds_when_checkout_without_promotion_code() throws Exception {

        final CheckoutCommand command = new CheckoutCommand(1L,
                new HashSet<>(asList(
                        new Item(1L, 2),
                        new Item(2L, 1),
                        new Item(3L, 2))), null);

        final MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.post("/book-store/checkout")
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8")
                .content(new ObjectMapper().writeValueAsBytes(command));

        mockMvc.perform(requestBuilder)
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isCreated());

        BDDMockito.then(checkoutUseCase).should(BDDMockito.times(1)).checkout(command);

    }
}
