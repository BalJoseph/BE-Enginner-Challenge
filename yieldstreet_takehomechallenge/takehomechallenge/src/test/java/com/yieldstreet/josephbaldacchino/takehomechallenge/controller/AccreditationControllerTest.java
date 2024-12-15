package com.yieldstreet.josephbaldacchino.takehomechallenge.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.yieldstreet.josephbaldacchino.takehomechallenge.model.accreditationcreation.AccreditationCreationModel;
import com.yieldstreet.josephbaldacchino.takehomechallenge.model.accreditationcreation.AccreditationCreationResponseModel;
import com.yieldstreet.josephbaldacchino.takehomechallenge.model.accreditationcreation.DocumentCreationModel;
import com.yieldstreet.josephbaldacchino.takehomechallenge.model.accreditationsretrieval.AccreditationStatusModel;
import com.yieldstreet.josephbaldacchino.takehomechallenge.model.accreditationsretrieval.UserAccreditationsResponseModel;
import com.yieldstreet.josephbaldacchino.takehomechallenge.model.accreditationstatusfinalization.AccreditationStatusFinalizationModel;
import com.yieldstreet.josephbaldacchino.takehomechallenge.model.database.Accreditation;
import com.yieldstreet.josephbaldacchino.takehomechallenge.model.database.UserAccreditation;
import com.yieldstreet.josephbaldacchino.takehomechallenge.service.DatabaseService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class AccreditationControllerTest {

    @Autowired private DatabaseService dbService;
    @Autowired private MockMvc mockMvc;

    String validUserId = "userA";
    String invalidUserId = "doesn'tExist";

    String userIdWithPending = "userA";
    String userIdWithoutPending = "userB";

    String confirmedAccreditationId = "confirmed";
    String failedAccreditationId = "failed";
    String expiredAccreditationId = "expired";
    String pendingAccreditationId = "pending";

    @Autowired
    private ObjectMapper objectMapper;

    //Check for if user exists
    @Test
    public void testGetAccreditationUserExists() throws Exception{
        //user A exists so we expect an OK response
        mockMvc.perform(get("/user/"+validUserId+"/accreditation")).andExpect(status().isOk());
    }

    @Test
    public void testGetAccreditationUserDoesNotExists() throws Exception{
        //user A exists so we expect an OK response
        mockMvc.perform(get("/user/"+invalidUserId+"/accreditation")).andExpect(status().isNotFound());
    }

    @Test
    public void testGetAccreditationGetsAll() throws Exception{

        MvcResult mvcResult = mockMvc.perform(get("/user/"+validUserId+"/accreditation")).andExpect(status().isOk()).andReturn();

        String jsonResponse = mvcResult.getResponse().getContentAsString();
        System.out.println(jsonResponse);
        UserAccreditationsResponseModel uarm = objectMapper.readValue(jsonResponse, UserAccreditationsResponseModel.class);

        assertEquals(dbService.getUserAccreditations(validUserId).size(), uarm.getAccreditation_statuses().size());
    }

    @Test
    public void testCreateAccreditationInvalidForm() throws Exception{
        //This is invalid because document is null
        AccreditationCreationModel acm = new AccreditationCreationModel(userIdWithoutPending, "BY_NET_WORTH", null);

        String jsonRequest = objectMapper.writeValueAsString(acm);

        mockMvc.perform(post("/user/accreditation")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonRequest))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testCreateAccreditationInvalidUser() throws Exception{
        DocumentCreationModel dcm = new DocumentCreationModel("name", "mime_type", "content");

        //the given userId is invalid
        AccreditationCreationModel acm = new AccreditationCreationModel(invalidUserId, "BY_NET_WORTH", dcm);

        String jsonRequest = objectMapper.writeValueAsString(acm);

        mockMvc.perform(post("/user/accreditation")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isUnauthorized());
    }

    @Test
    public void testCreateAccreditationWhileBeingPending() throws Exception{
        DocumentCreationModel dcm = new DocumentCreationModel("name", "mime_type", "content");

        //the given userId has a pending Accreditation
        AccreditationCreationModel acm = new AccreditationCreationModel(userIdWithPending, "BY_NET_WORTH", dcm);

        String jsonRequest = objectMapper.writeValueAsString(acm);

        mockMvc.perform(post("/user/accreditation")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonRequest))
                .andExpect(status().isMethodNotAllowed());
    }

    @Test
    public void testCreateAccreditationSuccessful() throws  Exception{
        DocumentCreationModel dcm = new DocumentCreationModel("name", "mime_type", "content");

        //the given userId should be able to accept it
        AccreditationCreationModel acm = new AccreditationCreationModel(userIdWithoutPending, "BY_NET_WORTH", dcm);

        int accreditationsBefore = dbService.getUserAccreditations(userIdWithoutPending).size();

        String jsonRequest = objectMapper.writeValueAsString(acm);

        MvcResult mvcResult =  mockMvc.perform(post("/user/accreditation")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonRequest))
                .andExpect(status().isCreated())
                .andReturn();

        assertEquals(accreditationsBefore + 1, dbService.getUserAccreditations(userIdWithoutPending).size());

        String jsonResponse = mvcResult.getResponse().getContentAsString();
        System.out.println(jsonResponse);
    }

    @Test
    public void testUpdateBadAccreditationId() throws Exception{
        String invalidAccreditationId = "invalid";

        AccreditationStatusFinalizationModel asfm = new AccreditationStatusFinalizationModel("CONFIRMED");
        String jsonRequest = objectMapper.writeValueAsString(asfm);

        mockMvc.perform(put("/user/accreditation/"+invalidAccreditationId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonRequest))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testUpdateInvalidForm() throws Exception{
        AccreditationStatusFinalizationModel asfm = new AccreditationStatusFinalizationModel("invalid");
        String jsonRequest = objectMapper.writeValueAsString(asfm);

        mockMvc.perform(put("/user/accreditation/"+pendingAccreditationId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonRequest))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testUpdatingExpired() throws Exception{
        AccreditationStatusFinalizationModel asfm = new AccreditationStatusFinalizationModel("CONFIRMED");
        String jsonRequest = objectMapper.writeValueAsString(asfm);

        mockMvc.perform(put("/user/accreditation/"+expiredAccreditationId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonRequest))
                .andExpect(status().isMethodNotAllowed());

    }

    @Test
    public void testUpdatingFailed() throws Exception{
        AccreditationStatusFinalizationModel asfm = new AccreditationStatusFinalizationModel("EXPIRED");
        String jsonRequest = objectMapper.writeValueAsString(asfm);

        mockMvc.perform(put("/user/accreditation/"+failedAccreditationId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonRequest))
                .andExpect(status().isMethodNotAllowed());

    }

    @Test
    public void testUpdatingConfirmedNotExpired() throws Exception{
        AccreditationStatusFinalizationModel asfm = new AccreditationStatusFinalizationModel("FAILED");
        String jsonRequest = objectMapper.writeValueAsString(asfm);

        mockMvc.perform(put("/user/accreditation/"+confirmedAccreditationId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonRequest))
                .andExpect(status().isMethodNotAllowed());

    }

    @Test
    public void testUpdatingConfirmedExpired() throws Exception{
        AccreditationStatusFinalizationModel asfm = new AccreditationStatusFinalizationModel("EXPIRED");
        String jsonRequest = objectMapper.writeValueAsString(asfm);

        mockMvc.perform(put("/user/accreditation/"+confirmedAccreditationId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonRequest))
                .andExpect(status().isOk());

    }
}