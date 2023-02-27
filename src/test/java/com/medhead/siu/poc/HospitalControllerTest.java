package com.medhead.siu.poc;

import static org.hamcrest.CoreMatchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

@SpringBootTest
@AutoConfigureMockMvc
public class HospitalControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void testGetHospitals() throws Exception {
        mockMvc.perform(get("/hospitals"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name", is("Walton Community Hospital - Virgin Care Services Ltd")))
                .andExpect(jsonPath("$[1319].id", is(1320)));
    }

    @Test
    public void testGetSpecialities() throws Exception {
        mockMvc.perform(get("/specialities"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name", is("Anaesthetics")))
                .andExpect(jsonPath("$[79].id", is(80)));
    }


    @Test
    public void testGetHospitalsBySpeciality() throws Exception {
        MvcResult result = mockMvc.perform(get("/hospitals/speciality/1"))
                .andExpect(status().isOk())
                .andReturn();
        String content = result.getResponse().getContentAsString();
        JSONArray contentAsJson = new JSONArray(content);
        assert contentAsJson.length() == 156;
    }

    @Test
    public void getNearestHospital() throws Exception {
        mockMvc.perform(get("/hospitals/51.379997253417969/-0.40604206919670105"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("Walton Community Hospital - Virgin Care Services Ltd")));
    }
}
