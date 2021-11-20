package com.zonkil.roomoccupancymanager.web

import com.zonkil.roomoccupancymanager.persistance.repositories.GuestRepository
import com.zonkil.roomoccupancymanager.service.v2.RoomOccupancyServiceV2
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.test.web.servlet.MockMvc
import spock.lang.Specification

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@WebMvcTest(RoomOccupancyV2Controller.class)
class RoomOccupancyV2ControllerTest extends Specification {

    @Autowired
    MockMvc mockMvc
    @MockBean
    GuestRepository guestRepository
    @MockBean
    RoomOccupancyServiceV2 roomOccupancyServiceV2;

    void "testParameterValidation"() {
        given:
        def url = "/v2/occupancy?numberOfPremiumRooms=${numPrem}&numberOfEconomyRooms=${numEcon}".toString()
        expect:
        mockMvc.perform(
                get(url)).andExpect(status().is(expectedStatus))
                .andReturn()

        where:
        numPrem | numEcon | expectedStatus
        0       | 0       | 200
        2       | 30      | 200
        1       | 3       | 200
        "aaa"   | 30      | 400
        2       | "aaa"   | 400
        -1      | 3       | 400
        1       | -3      | 400
        1.5     | 3       | 400
        1       | 3.5     | 400

    }

    void "testMissingParameter"() {
        expect:
        mockMvc.perform(
                get(url)).andExpect(status().is(expectedStatus))
                .andReturn()

        where:
        url                                                           | expectedStatus
        "/v2/occupancy"                                               | 400
        "/v2/occupancy?"                                              | 400
        "/v2/occupancy?numberOfPremiumRooms=1"                        | 400
        "/v2/occupancy?numberOfPremiumRooms=1&numberOfEconomyRooms=1" | 200
        "/v2/occupancy?numberOfEconomyRooms=1"                        | 400
        "/v2/occupancy?numberOfEconomyRooms=1&numberOfPremiumRooms=1" | 200
    }
}
