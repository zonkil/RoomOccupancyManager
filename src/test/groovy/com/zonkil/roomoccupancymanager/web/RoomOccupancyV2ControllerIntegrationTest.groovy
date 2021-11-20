package com.zonkil.roomoccupancymanager.web

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.web.servlet.MockMvc
import spock.lang.Specification

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@SpringBootTest
@AutoConfigureMockMvc
class RoomOccupancyV2ControllerIntegrationTest extends Specification {

    @Autowired
    MockMvc mockMvc

    def "shouldDoCalculation"() {
        given:
        def numPrem = 3
        def numEcon = 3
        def url = "/v2/occupancy?numberOfPremiumRooms=${numPrem}&numberOfEconomyRooms=${numEcon}".toString()
        when:
        def result = mockMvc.perform(get(url)).andExpect(status().isOk()).andReturn()

        then:
        result.getResponse().getContentAsString() == "{\"premiumOccupancy\":3,\"premiumTotalMoney\":738.00,\"economyOccupancy\":3,\"economyTotalMoney\":167.99}"
    }
}
