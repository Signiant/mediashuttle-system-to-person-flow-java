package com.workflows.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.workflows.service.SystemToPersonService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.openapitools.client.model.PackageTokenResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(SystemToPersonController.class)
@ContextConfiguration(classes = {SystemToPersonController.class})
public class SystemToPersonControllerTest {
    @Autowired
    private MockMvc mvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    SystemToPersonService systemToPersonService;

    @Test
    public void testCreateFileAcquisitionLink() throws Exception
    {
        PackageTokenResponse packageTokenResponse = new PackageTokenResponse();
        packageTokenResponse.addGrantsItem(PackageTokenResponse.GrantsEnum.UPLOAD);
        when(systemToPersonService.createTransferLink(any())).thenReturn(packageTokenResponse);
        MvcResult result = mvc.perform(MockMvcRequestBuilders
                        .get("/fileAcquisition")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        String jsonResponse = result.getResponse().getContentAsString();
        PackageTokenResponse response = objectMapper.readValue(jsonResponse, PackageTokenResponse.class);
        assertEquals(packageTokenResponse.getGrants().toString(), response.getGrants().toString());

    }

    @Test
    public void testCreateFileDistributionLink() throws Exception {
        PackageTokenResponse packageTokenResponse = new PackageTokenResponse();
        packageTokenResponse.addGrantsItem(PackageTokenResponse.GrantsEnum.DOWNLOAD);

        when(systemToPersonService.createTransferLink(any())).thenReturn(packageTokenResponse);
        MvcResult result = mvc.perform(MockMvcRequestBuilders
                        .get("/fileDistribution")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        String jsonResponse = result.getResponse().getContentAsString();
        PackageTokenResponse response = objectMapper.readValue(jsonResponse, PackageTokenResponse.class);
        assertEquals(packageTokenResponse.getGrants().toString(), response.getGrants().toString());

    }
}
