package com.challenge.coupon;

import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.rules.SpringClassRule;
import org.springframework.test.context.junit4.rules.SpringMethodRule;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class CouponApplicationTests {

    @ClassRule
    public static final SpringClassRule springClassRule = new SpringClassRule();

    @Rule
    public final SpringMethodRule springMethodRule = new SpringMethodRule();

    @Autowired
    private MockMvc mockMvc;

    /**
     * Se envia una lista de favoriteados con el monto del cupón
     */
    @Test
    public void coupon_ok() throws Exception {
        /**
         *
         * Se envia la lista de item_ids y el monto del cupón y se verifica que devuelva
         * los items que tendría que comprar el usuario
         *
         * Request body:
         * {
         * "item_ids": ["MLA811601014", "MLA811601018", "MLA811601022",
         * "MLA811601025", "MLA811601027"],
         * "amount": 1500
         * }
         *
         * Respose body:
         * {
         *     "item_ids": [
         *         "MLA811601014",
         *         "MLA811601018",
         *         "MLA811601022",
         *         "MLA811601027"
         *     ],
         *     "total": 1470.0
         * }
         */

        String jsonRequest = "{\"item_ids\":[\"MLA811601014\",\"MLA811601018\",\"MLA811601022\",\"MLA811601025\",\"MLA811601027\"],\"amount\":1500}";

        String actualResult = mockMvc.perform(post("/coupon")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        String expectedResult = "{\"item_ids\":[\"MLA811601014\",\"MLA811601018\",\"MLA811601022\",\"MLA811601027\"],\"total\":1470.0}";

        assertEquals(expectedResult, actualResult);
    }

    /**
     * Se envia una lista de favoriteados con item_id invalido
     */
    @Test
    public void coupon_bad_item_id_request() throws Exception {
        /**
         *
         * Se envia la lista de item_ids y el monto del cupón y se verifica que devuelva
         * los items que tendría que comprar el usuario
         *
         * Request body:
         * {
         * "item_ids": ["MLA1&", "MLA2", "MLA3", "MLA4", "MLA5"],
         * "amount": 500
         * }
         *
         */

        String jsonRequest = "{\"item_ids\":[\"MLA1&\",\"MLA2\",\"MLA3\",\"MLA4\",\"MLA5\"],\"amount\":500}";

        String actualResult = mockMvc.perform(post("/coupon")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isBadRequest())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertTrue(actualResult.contains("item_ids must be alphanumeric"));
    }

    /**
     * Se envia una lista de favoriteados con amount invalido
     */
    @Test
    public void coupon_bad_amount_request() throws Exception {
        /**
         *
         * Se envia la lista de item_ids y el monto del cupón y se verifica que devuelva
         * los items que tendría que comprar el usuario
         *
         * Request body:
         * {
         * "item_ids": ["MLA1", "MLA2", "MLA3", "MLA4", "MLA5"],
         * "amount": 0
         * }
         *
         */

        String jsonRequest = "{\"item_ids\":[\"MLA1\",\"MLA2\",\"MLA3\",\"MLA4\",\"MLA5\"],\"amount\":0}";

        String actualResult = mockMvc.perform(post("/coupon")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isBadRequest())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertTrue(actualResult.contains("amount is not valid"));
    }

    /**
     * Se envia una lista de favoriteados sin el campo item_id
     */
    @Test
    public void coupon_without_item_id_request() throws Exception {
        /**
         *
         * Se envia la lista de item_ids y el monto del cupón y se verifica que devuelva
         * los items que tendría que comprar el usuario
         *
         * Request body:
         * {
         * "amount": 500
         * }
         *
         */

        String jsonRequest = "{\"amount\":500}";

        String actualResult = mockMvc.perform(post("/coupon")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isBadRequest())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertTrue(actualResult.contains("item_ids is required"));
    }

    /**
     * Se envia una lista de favoriteados sin el campo amount
     */
    @Test
    public void coupon_without_amount_request() throws Exception {
        /**
         *
         * Se envia la lista de item_ids y el monto del cupón y se verifica que devuelva
         * los items que tendría que comprar el usuario
         *
         * {
         * "item_ids": ["MLA1", "MLA2", "MLA3", "MLA4", "MLA5"],
         * "amount": 0
         * }
         *
         */

        String jsonRequest = "{\"item_ids\":[\"MLA1\",\"MLA2\",\"MLA3\",\"MLA4\",\"MLA5\"]}";

        String actualResult = mockMvc.perform(post("/coupon")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isBadRequest())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertTrue(actualResult.contains("amount is required"));
    }

    /**
     * Se consulta la lista de favoriteados
     */
    @Test
    public void favorites_ok() throws Exception {
        /**
         *
         * Se envia la lista de item_ids y el monto del cupón y se verifica que devuelva
         * en el endpoint de favoriteados por lo menos uno de la lista
         *
         */

        String jsonRequest = "{\"item_ids\":[\"MLA811601014\",\"MLA811601018\",\"MLA811601022\",\"MLA811601025\",\"MLA811601027\"],\"amount\":1500}";

        mockMvc.perform(post("/coupon")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isOk());


        String actualResult = mockMvc.perform(get("/coupon/stats")
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertTrue(actualResult.contains("MLA811601014"));
    }
}

