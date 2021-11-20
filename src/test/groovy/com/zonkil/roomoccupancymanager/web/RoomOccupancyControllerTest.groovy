package com.zonkil.roomoccupancymanager.web

import com.zonkil.roomoccupancymanager.domain.GuestsFactory
import com.zonkil.roomoccupancymanager.service.RoomOccupancyService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.test.web.servlet.MockMvc
import spock.lang.Specification

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@WebMvcTest(RoomOccupancyController.class)
class RoomOccupancyControllerTest extends Specification {

    @MockBean
    RoomOccupancyService roomOccupancyService

    @MockBean
    GuestsFactory guestsFactory

    @Autowired
    MockMvc mockMvc

    void "testParameterValidation"() {
        given:
        def url = "/occupancy?numberOfPremiumRooms=${numPrem}&numberOfEconomyRooms=${numEcon}&allGuests=${guests}".toString()
        expect:
        mockMvc.perform(
                get(url)).andExpect(status().is(expectedStatus))
                .andReturn()

        where:
        numPrem | numEcon | guests           | expectedStatus
        0       | 0       | ""               | 200
        2       | 30      | "1,2,456"        | 200
        1       | 3       | "1,2,300.2"      | 200
        1       | 3       | "1.2,2.99,300.2" | 200
        "aaa"   | 30      | "1,2,456"        | 400
        2       | "aaa"   | "1,2,456"        | 400
        2       | 3       | "1,2,aaa"        | 400
        -1      | 3       | "1,2,300"        | 400
        1       | -3      | "1,2,300"        | 400
        1       | 3       | "-1,2,300"       | 400
        1.5     | 3       | "-1,2,300"       | 400
        1       | 3.5     | "-1,2,300"       | 400

    }


    void "testMissingParameter"() {
        expect:
        mockMvc.perform(
                get(url)).andExpect(status().is(expectedStatus))
                .andReturn()

        where:
        url                                                           | expectedStatus
        "/occupancy"                                                           | 400
        "/occupancy?"                                                          | 400
        "/occupancy?numberOfPremiumRooms=1"                                    | 400
        "/occupancy?numberOfPremiumRooms=1&numberOfEconomyRooms=1"             | 400
        "/occupancy?numberOfPremiumRooms=1&allGuests=1"                        | 400
        "/occupancy?numberOfEconomyRooms=1"                                    | 400
        "/occupancy?numberOfEconomyRooms=1&numberOfPremiumRooms=1"             | 400
        "/occupancy?numberOfEconomyRooms=1&allGuests=1"                        | 400
        "/occupancy?allGuests=1"                                               | 400
        "/occupancy?allGuests=1&numberOfPremiumRooms=1"                        | 400
        "/occupancy?allGuests=1&numberOfEconomyRooms=1"                        | 400
        "/occupancy?allGuests=1&numberOfEconomyRooms=1&numberOfPremiumRooms=1" | 200
    }
}
