package io.ossim.omaravrometadata

import com.wordnik.swagger.annotations.Api
import com.wordnik.swagger.annotations.ApiOperation
import groovy.util.logging.Slf4j
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RestController

/**
 * The REST API Controller that exposes GET, POST, and DELETE methods for interfacing with the "avro-metadata" table in DynamoDB
 */
@Api(value="avrometadata", description="Allows retrieval and creation of AvroMetadata objects from DynamoDB")
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

    /**
     * HTTP POST endpoint for adding an AvroMetadata obejct to the "avro-metadata" table in DynamoDB
     * @param avroMetadata the JSON representation of the AVRO metadata for the image
     * @param imageId the ID of the image to use as the key for the AvroMetadata object
     * @return Error if unsuccessful, the added AvroMetadata object and an HTTP OK response code if successful
     */
    @ApiOperation(value = "Add an AvroMetadata object to the avro-metadata table of DynamoDB using the imageId as a key")
    @RequestMapping(value = "/add/{imageId}", method = RequestMethod.POST)
    ResponseEntity<?> addAvroMetadata(@RequestBody String avroMetadata, @PathVariable("imageId") String imageId)
    {
        log.info("Adding AvroMetadata from RequestBody to database.")

        AvroMetadata toAdd = new AvroMetadata()
        toAdd.setImageId(imageId)
        toAdd.setAvroMetadata(avroMetadata)
        AvroMetadata addedAvroMetadata = avroMetadataService.addAvroMetadata(toAdd)

        if (addedAvroMetadata == null)
        {
            return new ResponseEntity("Could not add AvroMetadata to database", HttpStatus.BAD_REQUEST)
        }
        return new ResponseEntity<AvroMetadata>(addedAvroMetadata, HttpStatus.OK)
    }

    /**
     * HTTP GET endpoint for retrieving an AvroMetadata object from the "avro-metadata" table in DynamoDB
     * @param imageId the ID of the image to use as the key when retrieving the AvroMetadata object
     * @return Error if unsuccessful, the retrieved AvroMetadata object and an HTTP OK response code if successful
     */
    @ApiOperation(value = "Get an AvroMetadata object from the avro-metadata table of DynamoDB using the imageId as a key")
    @RequestMapping(value = "/get/{imageId}", method = RequestMethod.GET)
    ResponseEntity<?> getAvroMetadata(@PathVariable("imageId") String imageId)
    {
        log.info("Fetching AvroMetadata with Image ID ${imageId}")
        AvroMetadata avroMetadata = avroMetadataService.getAvroMetadata(imageId)

        if (avroMetadata == null)
        {
            log.error("AvroMetadata with Image ID ${imageId} not found.")
            return new ResponseEntity("AvroMetadata with imageId ${imageId} not found", HttpStatus.NOT_FOUND)
        }
        return new ResponseEntity<AvroMetadata>(avroMetadata, HttpStatus.OK)
    }

    /**
     * HTTP GET endpoint for retrieving all AvroMetadata objects from the "avro-metadata" table in DynamoDB
     * @return Error if unsuccessful, a list of AvroMetadata objects and an HTTP OK response code if successful
     */
    @ApiOperation(value = "View a list of AvroMetadata objects stored in the avro-metadata table of DynamoDB")
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    ResponseEntity<?> listAllAvroMetadata()
    {
        log.info("Retrieving all AvroMetadata objects")
        List<AvroMetadata> list = avroMetadataService.listAvroMetadata()

        if (list.isEmpty())
        {
            return new ResponseEntity("No AvroMetadata found", HttpStatus.NOT_FOUND)
        }
        return new ResponseEntity<List<AvroMetadata>>(list, HttpStatus.OK)
    }

    /**
     * HTTP DELETE endpoint for deleting an AvroMetadata object from the "avro-metadata" table in DynamoDB
     * @param imageId the ID of the image to use as the key to specify which AvroMetadta object to remove from the "avro-metadata" table
     * @return Error if unsuccessful, a success message and an HTTP OK response code if successful
     */
    @ApiOperation(value = "Delete an AvroMetadata object using the imageId as the key")
    @RequestMapping(value = "/delete/{imageId}", method = RequestMethod.DELETE)
    ResponseEntity<?> deleteAvroMetadata(@PathVariable("imageId") String imageId)
    {
        log.info("Deleting AvroMetadata matching ${imageId}")
        boolean deleted = avroMetadataService.deleteAvroMetadata(imageId)

        if (deleted)
        {
            return new ResponseEntity("Successfully found and deleted AvroMetadata for ${imageId}", HttpStatus.OK)
        }
        return new ResponseEntity("No AvroMetadata found for ${imageId}", HttpStatus.NOT_FOUND)
    }
}