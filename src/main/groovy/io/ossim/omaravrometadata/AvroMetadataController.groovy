package io.ossim.omaravrometadata

import groovy.util.logging.Slf4j
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping(value = "/api")
@Slf4j
class AvroMetadataController
{
    @Autowired
    private AvroMetadataService avroMetadataService

    @RequestMapping(value = "/avrometadata/post/{imageId}", method = RequestMethod.POST)
    ResponseEntity<AvroMetadata> addAvroMetadata(@RequestBody String avroMetadata, @PathVariable("imageId") String imageId)
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

    @RequestMapping(value = "/avrometadata/get/{imageId}", method = RequestMethod.GET)
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

    @RequestMapping(value = "/avrometadata/list")
    ResponseEntity<List<AvroMetadata>> listAllAvroMetadata()
    {
        log.info("Retrieving all AvroMetadata objects")
        List<AvroMetadata> list = avroMetadataService.listAvroMetadata()

        if (list.isEmpty())
        {
            return new ResponseEntity("No AvroMetadata found", HttpStatus.NOT_FOUND)
        }
        return new ResponseEntity<List<AvroMetadata>>(list, HttpStatus.OK)
    }
}