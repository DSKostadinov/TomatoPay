package com.example.demo.controller;

import com.example.demo.domain.Transaction;
import com.example.demo.domain.Type;
import com.example.demo.domain.request.CreateTransactionRequest;
import com.example.demo.service.TransactionService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.math.BigDecimal;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = TransactionController.class)
class TransactionControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    TransactionService transactionService;

    @Autowired
    ObjectMapper objectMapper;

    String BASE_URL = "/transactions";
    String uuid = UUID.randomUUID().toString();

    @Test
    void testSave() throws Exception {
        mockMvc.perform(post(BASE_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(
                        new CreateTransactionRequest(uuid, "GBP", new BigDecimal(10), "desc", Type.DEBIT))))
                .andExpect(status().isCreated());

        verify(transactionService, times(1)).save(any());
    }

    @Test
    void testFindById() throws Exception {
        mockMvc.perform(get(String.join("/", BASE_URL, uuid)))
                .andExpect(status().isOk());

        verify(transactionService, times(1)).findById(UUID.fromString(uuid));
    }

    @Test
    void testFindAll() throws Exception {
        mockMvc.perform(get(String.join("/", BASE_URL)))
                .andExpect(status().isOk());

        verify(transactionService, times(1)).findAll(any());
    }

    @Test
    void testDelete() throws Exception {
        mockMvc.perform(delete(String.join("/", BASE_URL, uuid)))
                .andExpect(status().isNoContent());

        verify(transactionService, times(1)).deleteById(UUID.fromString(uuid));
    }

    @Test
    void testUpdate() throws Exception {
        when(transactionService.findById(any())).thenReturn(
                new Transaction(
                        UUID.fromString((uuid)),
                        UUID.fromString((uuid)),
                        "GBP",
                        new BigDecimal("10"),
                        "desc",
                        Type.DEBIT
                ));

        MvcResult result = mockMvc.perform(put(String.join("/", BASE_URL, uuid).concat("?newDesc=test")))
                .andExpect(status().isOk())
                .andReturn();

        assertTrue(result.getResponse().getContentAsString().contains("description\":\"test\""));

        verify(transactionService, times(1)).findById(UUID.fromString((uuid)));
    }
}
