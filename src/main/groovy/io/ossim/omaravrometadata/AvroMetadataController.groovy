package io.ossim.omaravrometadata

import com.wordnik.swagger.annotations.Api
import com.wordnik.swagger.annotations.ApiOperation
import com.wordnik.swagger.annotations.ApiParam
import com.wordnik.swagger.annotations.ApiResponse
import com.wordnik.swagger.annotations.ApiResponses
import groovy.json.JsonBuilder
import groovy.json.JsonSlurper
import groovy.util.logging.Slf4j
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RestController

/**
 * The REST API Controller that exposes GET, POST, and DELETE methods for interfacing with the "avro-metadata" table in DynamoDB
 */
@Api(value="/avroMetadata", description="Allows retrieval and creation of AvroMetadata objects from DynamoDB")
@RestController
@RequestMapping(value = "/avroMetadata")
@Slf4j
class AvroMetadataController
{
    /**
     * The service to use in order to interface with the DynamoDB table
     */
    @Autowired
    private AvroMetadataService avroMetadataService

    @Value('${omar.avro.metadata.imageId:imageId}')
    String imageIdKey

    final JsonBuilder jsonBuilder = new JsonBuilder()

    final JsonSlurper jsonSlurper = new JsonSlurper()

    /**
     * HTTP POST endpoint for adding an AvroMetadata obejct to the "avro-metadata" table in DynamoDB
     * @param avroMetadata the JSON representation of the AVRO metadata for the image
     * @param imageId the ID of the image to use as the key for the AvroMetadata object
     * @return Error if unsuccessful, the added AvroMetadata object and an HTTP OK response code if successful
     */
    @ApiOperation(value = "Add an AvroMetadata object to the avro-metadata table of DynamoDB using the imageId as a key")
    @ApiResponses(value = [
        @ApiResponse(code = 200, message = "Successfully added AvroMetadata to the DynamoDB"),
        @ApiResponse(code = 400, message = "Failed to add AvroMetadata to the DynamoDB")
    ])
    @RequestMapping(value = "/add", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    Object addAvroMetadata(@ApiParam(value = "AvroMetadata JSON String", required = true)
                                      @RequestBody String avroMetadata)
    {
        log.info("Adding AvroMetadata from RequestBody to database.")

        final def parsedJson = jsonSlurper.parseText(avroMetadata)
        final def message = jsonSlurper.parseText(parsedJson.Message)

        AvroMetadata toAdd = new AvroMetadata()
        toAdd.setImageId(message."${imageIdKey}")
        toAdd.setAvroMetadata(avroMetadata)
        AvroMetadata addedAvroMetadata = avroMetadataService.addAvroMetadata(toAdd)

        if (addedAvroMetadata == null)
        {
            return jsonBuilder(statusCode: HttpStatus.BAD_REQUEST.value(), status: "failed", data: "Could not add AvroMetadata to database")
        }
        return jsonBuilder(statusCode: HttpStatus.OK.value(), status: "success", data: addedAvroMetadata)
    }

    /**
     * HTTP GET endpoint for retrieving an AvroMetadata object from the "avro-metadata" table in DynamoDB
     * @param imageId the ID of the image to use as the key when retrieving the AvroMetadata object
     * @return Error if unsuccessful, the retrieved AvroMetadata object and an HTTP OK response code if successful
     */
    @ApiOperation(value = "Get an AvroMetadata object from the avro-metadata table of DynamoDB using the imageId as a key")
    @RequestMapping(value = "/get/{imageId}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    Object getAvroMetadata(@ApiParam(value = "Key of the AvroMetadata object to retrieve from DynamoDB", required = true)
                                      @PathVariable("imageId") String imageId)
    {
        log.info("Fetching AvroMetadata with Image ID ${imageId}")
        AvroMetadata avroMetadata = avroMetadataService.getAvroMetadata(imageId)

        if (avroMetadata == null)
        {
            log.error("AvroMetadata with Image ID ${imageId} not found.")
            return jsonBuilder(statusCode: HttpStatus.OK.value(), status: "success", data: "AvroMetadata for imageId ${imageId} not found" as String)
        }
        return jsonBuilder(statusCode: HttpStatus.OK.value(), status: "success", data: avroMetadata)
    }

    /**
     * HTTP GET endpoint for retrieving all AvroMetadata objects from the "avro-metadata" table in DynamoDB
     * @return Error if unsuccessful, a list of AvroMetadata objects and an HTTP OK response code if successful
     */
    @ApiOperation(value = "View a list of AvroMetadata objects stored in the avro-metadata table of DynamoDB")
    @RequestMapping(value = "/list", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    Object listAllAvroMetadata()
    {
        log.info("Retrieving all AvroMetadata objects")
        List<AvroMetadata> list = avroMetadataService.listAvroMetadata()

        if (list.isEmpty())
        {
            return jsonBuilder(statusCode: HttpStatus.OK.value(), status: "success", data: "No AvroMetadata found in DynamoDB")
        }
        return jsonBuilder(statusCode: HttpStatus.OK.value(), status: "success", data: list)
    }

    /**
     * HTTP DELETE endpoint for deleting an AvroMetadata object from the "avro-metadata" table in DynamoDB
     * @param imageId the ID of the image to use as the key to specify which AvroMetadta object to remove from the "avro-metadata" table
     * @return Error if unsuccessful, a success message and an HTTP OK response code if successful
     */
    @ApiOperation(value = "Delete an AvroMetadata object using the imageId as the key")
    @RequestMapping(value = "/delete/{imageId}", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_VALUE)
    Object deleteAvroMetadata(@ApiParam(value = "Key of the AvroMetadata object to delete from DynamoDB")
                                         @PathVariable("imageId") String imageId)
    {
        log.info("Deleting AvroMetadata matching ${imageId}")
        boolean deleted = avroMetadataService.deleteAvroMetadata(imageId)

        if (deleted)
        {
            return jsonBuilder(statusCode: HttpStatus.OK.value(), status: "success", data: "Successfully found and deleted AvroMetadata for ${imageId}" as String)
        }
        return jsonBuilder(statusCode: HttpStatus.OK.value(), status: "success", data: "Could not find AvroMetadata to delete for ${imageId}" as String)
    }
}