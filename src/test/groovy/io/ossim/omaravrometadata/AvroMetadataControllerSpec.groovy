package io.ossim.omaravrometadata

import grails.testing.gorm.DataTest
import grails.testing.web.controllers.ControllerUnitTest
import grails.testing.spring.AutowiredTest
import groovy.json.JsonSlurper
import groovy.json.JsonOutput
import spock.lang.Specification

class AvroMetadataControllerSpec extends Specification implements AutowiredTest, DataTest, ControllerUnitTest<AvroMetadataController> {
    Closure doWithSpring() {
        { ->
            metadataService AvroMetadataService
        }
    }

    AvroMetadataService metadataService
    JsonSlurper jsonSlurper

    void setup() {
        jsonSlurper = new JsonSlurper()
    }


    void "testing addAvroMetadata"() {
        given:
        def msg = jsonSlurper.parse(new File(
                this.getClass().getResource("examples/26JAN16TS0109001_130755_DS0110L_33N106W_001X___SVV_0101_OBS_IMAG.json").toURI()
        ))
        request.JSON = JsonOutput.toJson(msg)
        //request.method = 'POST'

        when:
        controller.addAvroMetadata()

        then:
        status = 200

    }
}
